import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single game played within a gaming session.
 * Tracks:
 * - Bet details
 * - Outcome
 * - Stake before and after game
 * - Game duration
 * - Timestamp
 * Used for:
 * - Session audit trail
 * - Game analytics
 * - Historical reporting
 */
public class GameRecord {
    private String gameId;
    private String sessionId;
    private int gameNumber;
    private double betAmount;
    private boolean won;
    private double stakeBeforeGame;
    private double stakeAfterGame;
    private LocalDateTime playedAt;
    private long durationMillis;

    /**
     *
     * Creates new game record entry.
     *
     * @param sessionId Session identifier.
     * @param gameNumber Sequential game number in session.
     * @param betAmount Bet amount placed.
     * @param stakeBeforeGame Stake value before game execution.
     * @param playedAt Timestamp when game started.
     */
    public GameRecord(String sessionId, int gameNumber, double betAmount,
                      double stakeBeforeGame, LocalDateTime playedAt) {
        this.gameId = "GAME-" + UUID.randomUUID().toString().substring(0, 8);
        this.sessionId = sessionId;
        this.gameNumber = gameNumber;
        this.betAmount = betAmount;
        this.stakeBeforeGame = stakeBeforeGame;
        this.playedAt = playedAt;
    }

    /**
     *
     * Completes game record with final outcome and metrics.
     *
     * @param won Whether game was won.
     * @param stakeAfterGame Stake after game completion.
     * @param durationMillis Game execution duration in milliseconds.
     *
     */
    public void completeGame(boolean won, double stakeAfterGame, long durationMillis) {
        this.won = won;
        this.stakeAfterGame = stakeAfterGame;
        this.durationMillis = durationMillis;
    }

    // Getters
    public String getGameId() { return gameId; }
    public int getGameNumber() { return gameNumber; }
    public double getBetAmount() { return betAmount; }
    public boolean isWon() { return won; }
    public double getStakeBeforeGame() { return stakeBeforeGame; }
    public double getStakeAfterGame() { return stakeAfterGame; }
    public LocalDateTime getPlayedAt() { return playedAt; }

    @Override
    public String toString() {
        return String.format("Game #%d: %s $%.2f | Stake: $%.2f -> $%.2f (%dms)",
                gameNumber, won ? "WON" : "LOST", betAmount,
                stakeBeforeGame, stakeAfterGame, durationMillis);
    }
}
