import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Core domain class managing a full gambling session lifecycle.
 * Responsibilities:
 * - Session lifecycle control (start, pause, resume, end)
 * - Game execution management
 * - Stake tracking
 * - Boundary enforcement
 * - Duration tracking
 * - Session analytics and reporting
 */
public class GamingSession {
    private String sessionId;
    private String gamblerId;
    SessionParameters parameters;
    private SessionStatus status;
    private SessionEndReason endReason;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime lastActivityTime;

    private double currentStake;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;

    private List<GameRecord> gameHistory;
    private List<PauseRecord> pauseHistory;
    private PauseRecord currentPause;

    private long totalPauseDurationSeconds;
    private long totalActiveDurationSeconds;

    private Random random;

    /**
     * Creates gaming session instance.
     *
     * @param gamblerId Gambler identifier.
     * @param parameters Session configuration parameters.
     */
    public GamingSession(String gamblerId, SessionParameters parameters) {
        this.sessionId = "SESSION-" + UUID.randomUUID().toString().substring(0, 8);
        this.gamblerId = gamblerId;
        this.parameters = parameters;
        this.status = SessionStatus.INITIALIZED;
        this.endReason = SessionEndReason.NONE;
        this.currentStake = parameters.getInitialStake();
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gameHistory = new ArrayList<>();
        this.pauseHistory = new ArrayList<>();
        this.totalPauseDurationSeconds = 0;
        this.totalActiveDurationSeconds = 0;
        this.random = new Random();
    }

    /**
     * Starts gaming session.
     * @throws Exception If session already started.
     */
    public void start() throws Exception {
        if (status != SessionStatus.INITIALIZED) {
            throw new Exception("Session already started");
        }
        this.startTime = LocalDateTime.now();
        this.lastActivityTime = startTime;
        this.status = SessionStatus.ACTIVE;

        System.out.println(String.format(
                "Session %s STARTED | Initial Stake: $%.2f | Limits: [$%.2f - $%.2f]",
                sessionId, currentStake, parameters.getLowerLimit(), parameters.getUpperLimit()));
    }

    /**
     *
     * Executes single game inside session.
     * @param betAmount Bet amount.
     * @param winProbability Probability of winning.
     * @return GameRecord for executed game.
     * @throws Exception If session not active or validation fails.
     */
    public GameRecord playGame(double betAmount, double winProbability) throws Exception {
        validateSessionActive();
        validateBetAmount(betAmount);
        checkSessionLimits();

        long gameStartTime = System.currentTimeMillis();

        gamesPlayed++;
        GameRecord game = new GameRecord(sessionId, gamesPlayed, betAmount,
                currentStake, LocalDateTime.now());

        // Determine outcome
        boolean won = random.nextDouble() < winProbability;
        double previousStake = currentStake;

        if (won) {
            gamesWon++;
            double winAmount = betAmount * ((1.0 / winProbability) - 1.0);
            currentStake += winAmount;
        } else {
            gamesLost++;
            currentStake -= betAmount;
        }

        long gameDuration = System.currentTimeMillis() - gameStartTime;
        game.completeGame(won, currentStake, gameDuration);
        gameHistory.add(game);
        lastActivityTime = LocalDateTime.now();

        System.out.println(String.format("  %s", game));

        // Check if limits reached after game
        checkAndHandleLimits();

        return game;
    }

    /**
     *
     * Pauses active gaming session.
     * @param reason Reason for pausing session.
     * @throws Exception If session not active.
     */
    public void pause(String reason) throws Exception {
        if (status != SessionStatus.ACTIVE) {
            throw new Exception("Can only pause active sessions");
        }

        currentPause = new PauseRecord(LocalDateTime.now(), reason);
        pauseHistory.add(currentPause);
        status = SessionStatus.PAUSED;

        System.out.println(String.format("Session %s PAUSED: %s", sessionId, reason));
    }

    /**
     * Resumes paused session.
     * @throws Exception If session not paused.
     */
    public void resume() throws Exception {
        if (status != SessionStatus.PAUSED) {
            throw new Exception("Session is not paused");
        }

        currentPause.resume();
        totalPauseDurationSeconds += currentPause.getPauseDurationSeconds();
        status = SessionStatus.ACTIVE;
        lastActivityTime = LocalDateTime.now();

        System.out.println(String.format("Session %s RESUMED (paused for %ds)",
                sessionId, currentPause.getPauseDurationSeconds()));

        currentPause = null;

        // Check if session should end due to timeout
        checkSessionTimeout();
    }

    /**
     * Ends session manually.
     */
    public void endManually() {
        if (status == SessionStatus.PAUSED || status == SessionStatus.ACTIVE) {
            endSession(SessionStatus.ENDED_MANUAL, SessionEndReason.MANUAL_END);
        }
    }

    /**
     * Internal session termination handler.
     *
     * @param newStatus Final session status.
     * @param reason Session end reason.
     *
     */
    private void endSession(SessionStatus newStatus, SessionEndReason reason) {
        this.endTime = LocalDateTime.now();
        this.status = newStatus;
        this.endReason = reason;

        calculateTotalDuration();

        String statusMessage = "";
        switch (reason) {
            case UPPER_LIMIT_REACHED:
                statusMessage = "WIN CONDITION - Upper limit reached!";
                break;
            case LOWER_LIMIT_REACHED:
                statusMessage = "LOSS CONDITION - Lower limit reached!";
                break;
            case MANUAL_END:
                statusMessage = "Manually ended";
                break;
            case TIMEOUT:
                statusMessage = "Session timeout";
                break;
            default:
                statusMessage = "Session ended";
        }

        System.out.println(String.format("\nSession %s ENDED: %s", sessionId, statusMessage));
        System.out.println(getSessionSummary());
    }

    /**
     * Checks and automatically handles boundary breaches.
     */
    private void checkAndHandleLimits() {
        if (parameters.hasReachedUpperLimit(currentStake)) {
            endSession(SessionStatus.ENDED_WIN, SessionEndReason.UPPER_LIMIT_REACHED);
        } else if (parameters.hasReachedLowerLimit(currentStake)) {
            endSession(SessionStatus.ENDED_LOSS, SessionEndReason.LOWER_LIMIT_REACHED);
        }
    }

    /**
     * Validates session is active and playable.
     * @throws Exception If session not active or paused.
     */
    private void validateSessionActive() throws Exception {
        if (status == SessionStatus.PAUSED) {
            throw new Exception("Session is paused. Resume before playing.");
        }
        if (status != SessionStatus.ACTIVE) {
            throw new Exception("Session is not active");
        }
    }


    /**
     * Validates bet amount against rules.s
     * @param betAmount Bet amount.
     * @throws Exception If bet invalid.
     */
    private void validateBetAmount(double betAmount) throws Exception {
        if (betAmount < parameters.getMinBetAmount()) {
            throw new Exception(String.format("Bet amount $%.2f below minimum $%.2f",
                    betAmount, parameters.getMinBetAmount()));
        }
        if (betAmount > parameters.getMaxBetAmount()) {
            throw new Exception(String.format("Bet amount $%.2f exceeds maximum $%.2f",
                    betAmount, parameters.getMaxBetAmount()));
        }
        if (betAmount > currentStake) {
            throw new Exception(String.format("Insufficient stake: $%.2f (need $%.2f)",
                    currentStake, betAmount));
        }
    }

    /**
     * Validates session-level constraints.
     * @throws Exception If session limits exceeded.
     */
    private void checkSessionLimits() throws Exception {
        if (gamesPlayed >= parameters.getMaxGamesPerSession()) {
            throw new Exception("Maximum games per session reached");
        }
        checkSessionTimeout();
    }

    /**
     * Checks if session exceeded max allowed duration.
     * @throws Exception If timeout reached.
     */
    private void checkSessionTimeout() throws Exception {
        if (startTime != null) {
            long sessionMinutes = Duration.between(startTime, LocalDateTime.now()).toMinutes();
            if (sessionMinutes >= parameters.getMaxSessionDurationMinutes()) {
                endSession(SessionStatus.ENDED_TIMEOUT, SessionEndReason.TIMEOUT);
                throw new Exception("Session timeout reached");
            }
        }
    }

    /**
     * Calculates total active and paused duration.
     */
    private void calculateTotalDuration() {
        if (startTime != null && endTime != null) {
            long totalSeconds = Duration.between(startTime, endTime).getSeconds();
            totalActiveDurationSeconds = totalSeconds - totalPauseDurationSeconds;
        }
    }

    /**
     * Generates full session summary report.
     * @return Formatted session summary string.
     */
    public String getSessionSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== SESSION SUMMARY ==========\n");
        sb.append(String.format("Session ID: %s\n", sessionId));
        sb.append(String.format("Gambler ID: %s\n", gamblerId));
        sb.append(String.format("Status: %s\n", status));
        sb.append(String.format("End Reason: %s\n\n", endReason));

        // Time information
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sb.append("--- Time Information ---\n");
        sb.append(String.format("Started: %s\n", startTime.format(formatter)));
        if (endTime != null) {
            sb.append(String.format("Ended: %s\n", endTime.format(formatter)));
            sb.append(String.format("Total Duration: %s\n", formatDuration(
                    Duration.between(startTime, endTime).getSeconds())));
            sb.append(String.format("Active Duration: %s\n", formatDuration(totalActiveDurationSeconds)));
            sb.append(String.format("Paused Duration: %s\n", formatDuration(totalPauseDurationSeconds)));
        }
        sb.append(String.format("Number of Pauses: %d\n\n", pauseHistory.size()));

        // Financial information
        sb.append("--- Financial Information ---\n");
        sb.append(String.format("Initial Stake: $%.2f\n", parameters.getInitialStake()));
        sb.append(String.format("Final Stake: $%.2f\n", currentStake));
        sb.append(String.format("Net Profit/Loss: $%.2f\n", currentStake - parameters.getInitialStake()));
        sb.append(String.format("Return: %.2f%%\n",
                ((currentStake - parameters.getInitialStake()) / parameters.getInitialStake()) * 100));
        sb.append(String.format("Lower Limit: $%.2f\n", parameters.getLowerLimit()));
        sb.append(String.format("Upper Limit: $%.2f\n\n", parameters.getUpperLimit()));

        // Game statistics
        sb.append("--- Game Statistics ---\n");
        sb.append(String.format("Games Played: %d\n", gamesPlayed));
        sb.append(String.format("Games Won: %d\n", gamesWon));
        sb.append(String.format("Games Lost: %d\n", gamesLost));
        if (gamesPlayed > 0) {
            sb.append(String.format("Win Rate: %.2f%%\n", (double) gamesWon / gamesPlayed * 100));
            double avgBet = gameHistory.stream().mapToDouble(GameRecord::getBetAmount).average().orElse(0);
            sb.append(String.format("Average Bet: $%.2f\n", avgBet));
        }

        sb.append("\n====================================\n");
        return sb.toString();
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public String getGamblerId() { return gamblerId; }
    public SessionStatus getStatus() { return status; }
    public SessionEndReason getEndReason() { return endReason; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getCurrentStake() { return currentStake; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesWon() { return gamesWon; }
    public int getGamesLost() { return gamesLost; }
    public List<GameRecord> getGameHistory() { return new ArrayList<>(gameHistory); }
    public List<PauseRecord> getPauseHistory() { return new ArrayList<>(pauseHistory); }
    public boolean isActive() { return status == SessionStatus.ACTIVE; }
    public boolean isPaused() { return status == SessionStatus.PAUSED; }
    public boolean isEnded() {
        return status == SessionStatus.ENDED_WIN ||
                status == SessionStatus.ENDED_LOSS ||
                status == SessionStatus.ENDED_MANUAL ||
                status == SessionStatus.ENDED_TIMEOUT;
    }
}

