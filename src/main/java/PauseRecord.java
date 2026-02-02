import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tracks pause and resume cycles within a gaming session.
 * Maintains:
 * - Pause timestamp
 * - Resume timestamp
 * - Pause reason
 * - Pause duration calculation
 */
public class PauseRecord {
    private LocalDateTime pausedAt;
    private LocalDateTime resumedAt;
    private String reason;

    /**
     *
     * Creates pause record entry.
     * @param pausedAt Pause timestamp.
     * @param reason Reason for pausing.
     */
    public PauseRecord(LocalDateTime pausedAt, String reason) {
        this.pausedAt = pausedAt;
        this.reason = reason;
    }

    /**
     * Marks pause record as resumed.
     */
    public void resume() {
        this.resumedAt = LocalDateTime.now();
    }

    /**
     * Calculates pause duration.
     * @return Pause duration in seconds.
     */
    public long getPauseDurationSeconds() {
        LocalDateTime end = resumedAt != null ? resumedAt : LocalDateTime.now();
        return Duration.between(pausedAt, end).getSeconds();
    }

    @Override
    public String toString() {
        return String.format("Paused: %s | Resumed: %s | Duration: %ds | Reason: %s",
                pausedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                resumedAt != null ? resumedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "Still paused",
                getPauseDurationSeconds(), reason);
    }
}
