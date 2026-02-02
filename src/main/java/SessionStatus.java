/**
 * Represents lifecycle states of a gaming session.
 * States:
 * - INITIALIZED → Session created but not started.
 * - ACTIVE → Session is actively running.
 * - PAUSED → Session temporarily halted.
 * - ENDED_WIN → Ended due to reaching upper limit.
 * - ENDED_LOSS → Ended due to reaching lower limit.
 * - ENDED_MANUAL → Ended by manual user action.
 * - ENDED_TIMEOUT → Ended due to session duration timeout.
 * - ENDED_ERROR → Ended due to system error.
 */
public enum SessionStatus {
    INITIALIZED,
    ACTIVE,
    PAUSED,
    ENDED_WIN,
    ENDED_LOSS,
    ENDED_MANUAL,
    ENDED_TIMEOUT,
    ENDED_ERROR
}
