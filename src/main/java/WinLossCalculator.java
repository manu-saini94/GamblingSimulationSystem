import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service handling win/loss calculations.
 * Responsibilities:
 * - Outcome determination
 * - Stake updates
 * - Running totals tracking
 * - Streak calculation
 * - Statistics generation
 */
public class WinLossCalculator {
    private String sessionId;
    private OutcomeStrategy outcomeStrategy;
    private OddsConfiguration oddsConfig;
    private List<GameResult> gameResults;
    private RunningTotals runningTotals;
    private double currentStake;

    /**
     * Creates win/loss calculation engine.
     * @param sessionId Session identifier.
     * @param initialStake Starting stake.
     * @param outcomeStrategy Outcome determination strategy.
     * @param oddsConfig Odds configuration.
     */
    public WinLossCalculator(String sessionId, double initialStake,
                             OutcomeStrategy outcomeStrategy, OddsConfiguration oddsConfig) {
        this.sessionId = sessionId;
        this.currentStake = initialStake;
        this.outcomeStrategy = outcomeStrategy;
        this.oddsConfig = oddsConfig;
        this.gameResults = new ArrayList<>();
        this.runningTotals = new RunningTotals(initialStake);
    }

    /**
     * Determines game outcome using configured strategy.
     * @param winProbability Probability of win.
     * @return GameOutcome result.
     */
    public GameOutcome determineOutcome(double winProbability) {
        boolean won = outcomeStrategy.determineOutcome(winProbability);
        return won ? GameOutcome.WIN : GameOutcome.LOSS;
    }

    /**
     * Executes full game simulation including stake update.
     * @param betAmount Bet amount.
     * @param winProbability Win probability.
     * @return GameResult generated.
     * @throws Exception If insufficient stake.
     */
    public GameResult playGame(double betAmount, double winProbability) throws Exception {
        if (betAmount > currentStake) {
            throw new Exception("Insufficient stake for bet");
        }

        // Create game result
        GameResult result = new GameResult(sessionId, betAmount, winProbability,
                currentStake, oddsConfig,
                outcomeStrategy.getStrategyName());

        // Determine outcome
        GameOutcome outcome = determineOutcome(winProbability);
        result.setOutcome(outcome);

        // Update stake
        currentStake = result.getStakeAfterGame();

        // 4. Maintain running totals
        switch (outcome) {
            case WIN:
                runningTotals.recordWin(result.getWinnings());
                break;
            case LOSS:
                runningTotals.recordLoss(result.getLosses());
                break;
            case PUSH:
                runningTotals.recordPush();
                break;
        }

        gameResults.add(result);
        return result;
    }

    /**
     * Calculates win to loss ratio.
     * @return Win/Loss ratio.
     */
    public double getWinLossRatio() {
        long wins = gameResults.stream().filter(r -> r.getOutcome() == GameOutcome.WIN).count();
        long losses = gameResults.stream().filter(r -> r.getOutcome() == GameOutcome.LOSS).count();
        return losses > 0 ? (double) wins / losses : wins;
    }

    /**
     * Calculates win rate percentage.
     * @return Win rate value (0 to 1).
     */
    public double getWinRate() {
        if (gameResults.isEmpty()) return 0.0;
        long wins = gameResults.stream().filter(r -> r.getOutcome() == GameOutcome.WIN).count();
        return (double) wins / gameResults.size();
    }

    /**
     * Calculates current win and loss streaks.
     * @return Map containing streak values.
     */
    public Map<String, Integer> getCurrentStreaks() {
        Map<String, Integer> streaks = new HashMap<>();

        if (gameResults.isEmpty()) {
            streaks.put("currentWinStreak", 0);
            streaks.put("currentLossStreak", 0);
            return streaks;
        }

        int winStreak = 0;
        int lossStreak = 0;

        for (int i = gameResults.size() - 1; i >= 0; i--) {
            GameOutcome outcome = gameResults.get(i).getOutcome();

            if (outcome == GameOutcome.WIN) {
                if (lossStreak > 0) break;
                winStreak++;
            } else if (outcome == GameOutcome.LOSS) {
                if (winStreak > 0) break;
                lossStreak++;
            } else {
                break;
            }
        }

        streaks.put("currentWinStreak", winStreak);
        streaks.put("currentLossStreak", lossStreak);
        return streaks;
    }

    /**
     * Returns full statistics object.
     * @return WinLossStatistics instance.
     */
    public WinLossStatistics getStatistics() {
        return new WinLossStatistics(gameResults);
    }

    /**
     * Returns running totals tracker.
     * @return RunningTotals instance.
     */
    public RunningTotals getRunningTotals() {
        return runningTotals;
    }

    // Getters
    public double getCurrentStake() { return currentStake; }
    public List<GameResult> getGameResults() { return new ArrayList<>(gameResults); }
    public int getTotalGames() { return gameResults.size(); }

    /**
     * Prints recent game results.
     * @param count Number of recent results to print.
     */
    public void printRecentResults(int count) {
        System.out.println("\n--- Recent Game Results ---");
        int start = Math.max(0, gameResults.size() - count);
        for (int i = start; i < gameResults.size(); i++) {
            System.out.println(String.format("Game %d: %s", i + 1, gameResults.get(i)));
        }
    }
}
