import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents full result of a single game.
 * Tracks:
 * - Bet details
 * - Outcome
 * - Winnings / losses
 * - Stake before and after game
 * - Strategy used
 */
public class GameResult {
    private String gameId;
    private String sessionId;
    private LocalDateTime timestamp;
    private double betAmount;
    private double winProbability;
    private GameOutcome outcome;
    private double winnings;
    private double losses;
    private double netResult;
    private double stakeBeforeGame;
    private double stakeAfterGame;
    private OddsConfiguration odds;
    private String outcomeStrategy;

    /**
     *
     * Creates game result container.
     * @param sessionId Session identifier.
     * @param betAmount Bet amount.
     * @param winProbability Probability used for outcome.
     * @param stakeBeforeGame Stake before game execution.
     * @param odds Odds configuration.
     * @param outcomeStrategy Strategy name used.
     */
    public GameResult(String sessionId, double betAmount, double winProbability,
                      double stakeBeforeGame, OddsConfiguration odds, String outcomeStrategy) {
        this.gameId = "GAME-" + UUID.randomUUID().toString().substring(0, 8);
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
        this.betAmount = betAmount;
        this.winProbability = winProbability;
        this.stakeBeforeGame = stakeBeforeGame;
        this.odds = odds;
        this.outcomeStrategy = outcomeStrategy;
    }

    /**
     * Applies outcome and calculates financial impact.
     * @param outcome Game outcome.
     */
    public void setOutcome(GameOutcome outcome) {
        this.outcome = outcome;

        if (outcome == GameOutcome.WIN) {
            this.winnings = odds.calculateWinnings(betAmount, winProbability);
            this.losses = 0.0;
            this.netResult = winnings;
            this.stakeAfterGame = stakeBeforeGame + winnings;
        } else if (outcome == GameOutcome.LOSS) {
            this.winnings = 0.0;
            this.losses = betAmount;
            this.netResult = -losses;
            this.stakeAfterGame = stakeBeforeGame - losses;
        } else { // PUSH
            this.winnings = 0.0;
            this.losses = 0.0;
            this.netResult = 0.0;
            this.stakeAfterGame = stakeBeforeGame;
        }
    }

    // Getters
    public String getGameId() { return gameId; }
    public String getSessionId() { return sessionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getBetAmount() { return betAmount; }
    public double getWinProbability() { return winProbability; }
    public GameOutcome getOutcome() { return outcome; }
    public double getWinnings() { return winnings; }
    public double getLosses() { return losses; }
    public double getNetResult() { return netResult; }
    public double getStakeBeforeGame() { return stakeBeforeGame; }
    public double getStakeAfterGame() { return stakeAfterGame; }

    @Override
    public String toString() {
        String symbol = outcome == GameOutcome.WIN ? "✓" : (outcome == GameOutcome.LOSS ? "✗" : "=");
        return String.format("%s %s | Bet: $%.2f | %s | Net: %s$%.2f | Stake: $%.2f -> $%.2f",
                symbol, gameId, betAmount, outcome,
                netResult >= 0 ? "+" : "", netResult, stakeBeforeGame, stakeAfterGame);
    }
}
