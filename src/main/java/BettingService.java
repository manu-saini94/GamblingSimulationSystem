import java.util.*;

/**
 * Core service handling betting operations and session lifecycle.
 * Provides:
 * - Bet placement and settlement
 * - Probability outcome simulation
 * - Strategy-driven bet calculation
 * - Session management
 * - Stake validation
 * Designed for real-time betting environments.
 */
public class BettingService {
    private Map<String, Double> gamblerStakes;
    private Map<String, BettingSession> activeSessions;
    private List<BettingSession> completedSessions;
    private Random random;
    private double minBet;
    private double maxBet;

    /**
     * Initializes betting service with bet boundaries.
     *
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     */
    public BettingService   (double minBet, double maxBet) {
        this.gamblerStakes = new HashMap<>();
        this.activeSessions = new HashMap<>();
        this.completedSessions = new ArrayList<>();
        this.random = new Random();
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    /**
     *
     * Registers gambler stake in system.
     *
     * @param gamblerId Unique gambler identifier.
     * @param initialStake Initial stake amount.
     *
     */
    public void initializeGambler(String gamblerId, double initialStake) {
        gamblerStakes.put(gamblerId, initialStake);
    }

    /**
     *
     * Starts betting session for gambler.
     *
     * @param gamblerId Gambler identifier.
     * @param strategy Strategy to be used.
     *
     * @return Created BettingSession.
     *
     * @throws Exception If gambler not registered.
     */
    public BettingSession startSession(String gamblerId, BetStrategy strategy) throws Exception {
        if (!gamblerStakes.containsKey(gamblerId)) {
            throw new Exception("Gambler not found");
        }

        double currentStake = gamblerStakes.get(gamblerId);
        BettingSession session = new BettingSession(gamblerId, currentStake, strategy);
        activeSessions.put(gamblerId, session);

        System.out.println(String.format("Session started: %s with strategy: %s",
                session.getSessionId(), strategy.getName()));
        return session;
    }

    /**
     *
     * Places single bet manually.
     *
     * @param gamblerId Gambler identifier.
     * @param amount Bet amount.
     * @param winProbability Probability of win.
     *
     * @return Created Bet instance.
     *
     * @throws Exception If validation fails.
     */
    public Bet placeBet(String gamblerId, double amount, double winProbability) throws Exception {
        validateGambler(gamblerId);
        BettingSession session = getActiveSession(gamblerId);
        double currentStake = gamblerStakes.get(gamblerId);

        // 4. Validate bet amount against current stake
        validateBetAmount(amount, currentStake);

        Bet bet = new Bet(gamblerId, amount, winProbability, currentStake, session.getStrategy());
        session.addBet(bet);

        System.out.println(String.format("Bet placed: %s - Amount: $%.2f, Win Prob: %.1f%%",
                bet.getBetId(), amount, winProbability * 100));

        return bet;
    }

    /**
     *
     * Places bet using configured strategy.
     *
     * @param gamblerId Gambler identifier.
     * @param winProbability Win probability.
     *
     * @return Created Bet instance.
     *
     * @throws Exception If validation fails.
     */
    public Bet placeBetWithStrategy(String gamblerId, double winProbability) throws Exception {
        validateGambler(gamblerId);
        BettingSession session = getActiveSession(gamblerId);
        double currentStake = gamblerStakes.get(gamblerId);

        // 5. Calculate bet amount using strategy
        double amount = session.getStrategy().calculateBetAmount(
                currentStake, minBet, maxBet, session.getBets());

        return placeBet(gamblerId, amount, winProbability);
    }

    /**
     *
     * Determines bet result using random probability simulation.
     *
     * @param winProbability Win probability threshold.
     *
     * @return True if bet is won.
     */
    public boolean determineBetOutcome(double winProbability) {
        return random.nextDouble() < winProbability;
    }

    /**
     *
     * Settles bet and updates gambler stake.
     *
     * @param bet Bet to settle.
     * @throws Exception If bet already settled.
     */
    public void settleBet(Bet bet) throws Exception {
        if (bet.getStatus() != BetStatus.PENDING) {
            throw new Exception("Bet already settled");
        }

        boolean won = determineBetOutcome(bet.getWinProbability());
        bet.settle(won);

        String gamblerId = bet.getGamblerId();
        double currentStake = gamblerStakes.get(gamblerId);
        double newStake;

        if (won) {
            newStake = currentStake + bet.getActualWin();
            System.out.println(String.format("✓ BET WON! %s - Won $%.2f",
                    bet.getBetId(), bet.getActualWin()));
        } else {
            newStake = currentStake - bet.getAmount();
            System.out.println(String.format("✗ BET LOST! %s - Lost $%.2f",
                    bet.getBetId(), bet.getAmount()));
        }

        gamblerStakes.put(gamblerId, newStake);

        BettingSession session = activeSessions.get(gamblerId);
        if (session != null) {
            session.updateStake(newStake);
        }

        System.out.println(String.format("  Stake: $%.2f -> $%.2f",
                currentStake, newStake));
    }

    /**
     *
     * Executes multiple consecutive bets.
     *
     * @param gamblerId Gambler identifier.
     * @param numberOfBets Number of bets to execute.
     * @param winProbability Win probability for each bet.
     *
     * @return List of executed bets.
     *
     * @throws Exception If gambler validation fails.
     */
    public List<Bet> placeConsecutiveBets(String gamblerId, int numberOfBets,
                                          double winProbability) throws Exception {
        validateGambler(gamblerId);
        List<Bet> bets = new ArrayList<>();

        System.out.println(String.format("\n--- Starting %d consecutive bets ---", numberOfBets));

        for (int i = 0; i < numberOfBets; i++) {
            double currentStake = gamblerStakes.get(gamblerId);

            if (currentStake < minBet) {
                System.out.println(String.format(
                        "Stopping: Insufficient stake $%.2f (min: $%.2f)",
                        currentStake, minBet));
                break;
            }

            System.out.println(String.format("\n=== Bet %d/%d ===", i + 1, numberOfBets));

            Bet bet = placeBetWithStrategy(gamblerId, winProbability);
            settleBet(bet);
            bets.add(bet);

            // Small delay for readability
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n--- Consecutive betting completed ---");
        return bets;
    }

    /**
     *
     * Ends active betting session.
     *
     * @param gamblerId Gambler identifier.
     *
     * @return Completed BettingSession.
     *
     * @throws Exception If no active session exists.
     */
    public BettingSession endSession(String gamblerId) throws Exception {
        BettingSession session = activeSessions.remove(gamblerId);
        if (session == null) {
            throw new Exception("No active session found");
        }

        session.endSession();
        completedSessions.add(session);

        System.out.println("\n" + session.getSummary());
        return session;
    }

    /**
     *
     * Validates bet amount against limits and stake.
     *
     * @param amount Bet amount.
     * @param currentStake Current stake.
     * @throws Exception If validation fails.
     */
    private void validateBetAmount(double amount, double currentStake) throws Exception {
        if (amount <= 0) {
            throw new Exception("Bet amount must be positive");
        }

        if (amount < minBet) {
            throw new Exception(String.format(
                    "Bet amount $%.2f is below minimum $%.2f", amount, minBet));
        }

        if (amount > maxBet) {
            throw new Exception(String.format(
                    "Bet amount $%.2f exceeds maximum $%.2f", amount, maxBet));
        }

        if (amount > currentStake) {
            throw new Exception(String.format(
                    "Insufficient stake: $%.2f (need $%.2f)", currentStake, amount));
        }
    }

    /**
     *
     * Validates gambler existence.
     *
     * @param gamblerId Gambler identifier.
     *
     * @throws Exception If gambler not found.
     */
    private void validateGambler(String gamblerId) throws Exception {
        if (!gamblerStakes.containsKey(gamblerId)) {
            throw new Exception("Gambler not found");
        }
    }

    /**
     *
     * Retrieves active betting session.
     *
     * @param gamblerId Gambler identifier.
     * @return Active BettingSession.
     * @throws Exception If no session exists.
     */
    private BettingSession getActiveSession(String gamblerId) throws Exception {
        BettingSession session = activeSessions.get(gamblerId);
        if (session == null) {
            throw new Exception("No active session. Please start a session first.");
        }
        return session;
    }

    // Getters
    public double getCurrentStake(String gamblerId) {
        return gamblerStakes.getOrDefault(gamblerId, 0.0);
    }

    public List<BettingSession> getCompletedSessions() {
        return new ArrayList<>(completedSessions);
    }
}
