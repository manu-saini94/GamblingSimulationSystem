/**
 * Defines reasons why a session ended.
 * Used for audit logging and reporting.
 */
public enum SessionEndReason {
    UPPER_LIMIT_REACHED,
    LOWER_LIMIT_REACHED,
    MANUAL_END,
    TIMEOUT,
    ERROR,
    NONE
}
