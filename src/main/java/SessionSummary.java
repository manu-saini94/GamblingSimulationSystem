import java.time.LocalDateTime;

/**
 * Data Transfer Object representing final session summary statistics.

 * Contains:
 * - Session timing information
 * - Financial performance metrics
 * - Game statistics
 * - Streak analytics

 * Used for end-of-session reporting.
 */
public class SessionSummary {
    String sessionId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    long durationSeconds;
    String endReason;
    double initialStake;
    double finalStake;
    double netProfit;
    double returnPercent;
    double totalWagered;
    double totalWon;
    double totalLost;
    int gamesPlayed;
    int wins;
    int losses;
    double winRate;
    double lossRate;
    int longestWinStreak;
    int longestLossStreak;
    double averageBet;
    double largestWin;
    double largestLoss;
    public SessionSummary(String sessionId) {
        this.sessionId = sessionId;
        this.startTime = LocalDateTime.now();
    }
}
