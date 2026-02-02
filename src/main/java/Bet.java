import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Bet class represents a single betting transaction placed by a gambler.

 * Responsibilities:
 * - Stores bet financial and probability details.
 * - Calculates potential winnings based on odds.
 * - Tracks stake before and after bet settlement.
 * - Maintains lifecycle state (Pending, Settled).
 * - Supports strategy attribution for analytics.

 * Used for:
 * - Session tracking
 * - Financial audit trail
 * - Outcome analytics
 */
public class Bet {
    private String betId;
    private String gamblerId;
    private double amount;
    private double winProbability;
    private BetOutcome outcome;
    private LocalDateTime placedAt;
    private LocalDateTime settledAt;
    private double stakeBeforeBet;
    private double stakeAfterBet;
    private double potentialWin;
    private double actualWin;
    private BetStrategy strategy;
    private BetStatus status;

    /**
     *
     * Creates a new bet instance with calculated potential winnings.
     *
     * @param gamblerId Unique gambler identifier.
     * @param amount Bet amount placed.
     * @param winProbability Probability of winning (0.0 – 1.0).
     * @param currentStake Stake available before placing bet.
     * @param strategy Betting strategy used for bet placement.
     */
    public Bet(String gamblerId, double amount, double winProbability,
               double currentStake, BetStrategy strategy) {
        this.betId = "BET-" + UUID.randomUUID().toString().substring(0, 8);
        this.gamblerId = gamblerId;
        this.amount = amount;
        this.winProbability = winProbability;
        this.stakeBeforeBet = currentStake;
        this.strategy = strategy;
        this.placedAt = LocalDateTime.now();
        this.status = BetStatus.PENDING;
        this.potentialWin = calculatePotentialWin(amount, winProbability);
    }

    /**
     *
     * Calculates potential winning amount based on probability odds.
     *
     * @param betAmount Bet amount placed.
     * @param probability Winning probability.
     *
     * @return Potential payout amount excluding original bet.
     */
    private double calculatePotentialWin(double betAmount, double probability) {
        // Simple odds calculation: lower probability = higher payout
        if (probability <= 0 || probability >= 1) {
            return betAmount;
        }
        double odds = (1.0 / probability) - 1.0;
        return betAmount * odds;
    }

    /**
     * Settles the bet based on outcome and updates financial results.
     *
     * @param won True if bet was won, false otherwise.
     *
     */
    public void settle(boolean won) {
        this.outcome = won ? BetOutcome.WIN : BetOutcome.LOSS;
        this.settledAt = LocalDateTime.now();
        this.status = BetStatus.SETTLED;

        if (won) {
            this.actualWin = potentialWin;
            this.stakeAfterBet = stakeBeforeBet + actualWin;
        } else {
            this.actualWin = 0.0;
            this.stakeAfterBet = stakeBeforeBet - amount;
        }
    }

    // Getters
    public String getBetId() { return betId; }
    public String getGamblerId() { return gamblerId; }
    public double getAmount() { return amount; }
    public double getWinProbability() { return winProbability; }
    public BetOutcome getOutcome() { return outcome; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public double getStakeBeforeBet() { return stakeBeforeBet; }
    public double getStakeAfterBet() { return stakeAfterBet; }
    public double getPotentialWin() { return potentialWin; }
    public double getActualWin() { return actualWin; }
    public BetStrategy getStrategy() { return strategy; }
    public BetStatus getStatus() { return status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %s - $%.2f at %.1f%% prob | %s | Stake: $%.2f -> $%.2f",
                betId, outcome != null ? outcome : "PENDING", amount,
                winProbability * 100, strategy.getClass().getSimpleName(),
                stakeBeforeBet, stakeAfterBet);
    }
}