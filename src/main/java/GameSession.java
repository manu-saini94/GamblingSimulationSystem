import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSession {
    private String sessionId;
    private String gamblerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    private double initialStake;
    private double currentStake;
    private double upperLimit;
    private double lowerLimit;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private List<LocalDateTime> pauseTimes;
    private List<LocalDateTime> resumeTimes;

    public GameSession(String gamblerId, double initialStake,
                       double upperLimit, double lowerLimit) {
        this.sessionId = "SESSION-" + UUID.randomUUID().toString().substring(0, 8);
        this.gamblerId = gamblerId;
        this.startTime = LocalDateTime.now();
        this.status = SessionStatus.ACTIVE;
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.pauseTimes = new ArrayList<>();
        this.resumeTimes = new ArrayList<>();
    }

    public void recordGame(boolean won, double newStake) {
        gamesPlayed++;
        if (won) wins++; else losses++;
        currentStake = newStake;

        checkLimits();
    }

    public void pause() {
        if (status == SessionStatus.ACTIVE) {
            status = SessionStatus.PAUSED;
            pauseTimes.add(LocalDateTime.now());
        }
    }

    public void resume() {
        if (status == SessionStatus.PAUSED) {
            status = SessionStatus.ACTIVE;
            resumeTimes.add(LocalDateTime.now());
        }
    }

    private void checkLimits() {
        if (currentStake >= upperLimit) {
            status = SessionStatus.ENDED_WIN;
            endTime = LocalDateTime.now();
        } else if (currentStake <= lowerLimit) {
            status = SessionStatus.ENDED_LOSS;
            endTime = LocalDateTime.now();
        }
    }

    public void endManually() {
        status = SessionStatus.ENDED_MANUAL;
        endTime = LocalDateTime.now();
    }

    public boolean canContinue() {
        return status == SessionStatus.ACTIVE &&
                currentStake > lowerLimit &&
                currentStake < upperLimit;
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public SessionStatus getStatus() { return status; }
    public double getCurrentStake() { return currentStake; }
    public double getInitialStake() { return initialStake; }
    public double getUpperLimit() { return upperLimit; }
    public double getLowerLimit() { return lowerLimit; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public long getDurationSeconds() {
        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        return Duration.between(startTime, end).getSeconds();
    }
}

