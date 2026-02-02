import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Core game engine responsible for executing betting logic.
 * Responsibilities:
 * - Execute game outcome simulation
 * - Maintain session financial state
 * - Track game history
 * - Generate session summary reports
 */
public class SimpleGameEngine {
    private String sessionId;
    private double currentStake;
    private double initialStake;
    private double lowerLimit;
    private double upperLimit;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private double totalWagered;
    private double totalWon;
    private double totalLost;
    private LocalDateTime startTime;
    private Random random;
    private List< GameResult > gameHistory;
    private double lastBetAmount;
    public SimpleGameEngine(double initialStake, double lowerLimit, double upperLimit) {
        this.sessionId = "SESSION-" + UUID.randomUUID().toString().substring(0, 8);
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.totalWagered = 0;
        this.totalWon = 0;
        this.totalLost = 0;
        this.startTime = LocalDateTime.now();
        this.random = new Random();
        this.gameHistory = new ArrayList< >();
        this.lastBetAmount = 0;
    }

    /**
     * Executes single game round using probability simulation.
     * Updates:
     * - Stake balance
     * - Win/Loss counters
     * - Total wagered/won/lost

     * @param betAmount bet placed
     * @param winProbability probability of winning
     * @return game result object
     */
    public GameResult playGame(double betAmount, double winProbability) {
        gamesPlayed++;
        totalWagered += betAmount;
        lastBetAmount = betAmount;
        double stakeBefore = currentStake;
        boolean won = random.nextDouble() < winProbability;
        double winAmount = 0;
        if(won) {
            wins++;
            winAmount = betAmount * ((1.0 / winProbability) - 1.0);
            currentStake += winAmount;
            totalWon += winAmount;
        } else {
            losses++;
            currentStake -= betAmount;
            totalLost += betAmount;
        }
        GameResult result = new GameResult(gamesPlayed, won, betAmount, winAmount, stakeBefore, currentStake);
        gameHistory.add(result);
        return result;
    }

    /**
     * Generates session summary with full analytics.
     * @param endReason reason session ended
     * @return session summary object
     */
    public SessionSummary generateSummary(String endReason) {
        SessionSummary summary = new SessionSummary(sessionId);
        summary.startTime = startTime;
        summary.endTime = LocalDateTime.now();
        summary.durationSeconds = Duration.between(startTime, summary.endTime).getSeconds();
        summary.endReason = endReason;
        summary.initialStake = initialStake;
        summary.finalStake = currentStake;
        summary.netProfit = currentStake - initialStake;
        summary.returnPercent = (summary.netProfit / initialStake) * 100;
        summary.totalWagered = totalWagered;
        summary.totalWon = totalWon;
        summary.totalLost = totalLost;
        summary.gamesPlayed = gamesPlayed;
        summary.wins = wins;
        summary.losses = losses;
        summary.winRate = gamesPlayed > 0 ? (double) wins / gamesPlayed * 100 : 0;
        summary.lossRate = gamesPlayed > 0 ? (double) losses / gamesPlayed * 100 : 0;
        summary.averageBet = gamesPlayed > 0 ? totalWagered / gamesPlayed : 0;
        summary.largestWin = gameHistory.stream().filter(g -> g.won).mapToDouble(g -> g.winAmount).max().orElse(0);
        summary.largestLoss = gameHistory.stream().filter(g -> !g.won).mapToDouble(g -> g.betAmount).max().orElse(0);
        // Calculate streaks
        int currentStreak = 0;
        int maxWinStreak = 0;
        int maxLossStreak = 0;
        boolean lastWasWin = false;
        for(GameResult game: gameHistory) {
            if(game.won) {
                if(lastWasWin) {
                    currentStreak++;
                } else {
                    currentStreak = 1;
                    lastWasWin = true;
                }
                maxWinStreak = Math.max(maxWinStreak, currentStreak);
            } else {
                if(!lastWasWin && gamesPlayed > 0) {
                    currentStreak++;
                } else {
                    currentStreak = 1;
                    lastWasWin = false;
                }
                maxLossStreak = Math.max(maxLossStreak, currentStreak);
            }
        }
        summary.longestWinStreak = maxWinStreak;
        summary.longestLossStreak = maxLossStreak;
        return summary;
    }

    /**
     * Checks whether session stake reached win or loss limits.
     * @return true if limits reached
     */
    public boolean hasReachedLimits() {
        return currentStake <= lowerLimit || currentStake >= upperLimit;
    }
    // Getters
    public double getCurrentStake() {
        return currentStake;
    }
    public double getInitialStake() {
        return initialStake;
    }
    public double getLowerLimit() {
        return lowerLimit;
    }
    public double getUpperLimit() {
        return upperLimit;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public int getWins() {
        return wins;
    }
    public int getLosses() {
        return losses;
    }
    public double getLastBetAmount() {
        return lastBetAmount;
    }
}
