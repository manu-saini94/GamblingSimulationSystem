/**
 * Base exception class for all validation-related errors.
 * Provides structured error details including error type,
 * affected field name, and attempted invalid value.
 */
public class ValidationException extends Exception {
    private ValidationErrorType errorType;
    private String field;
    private Object attemptedValue;

    /**
     * Creates a validation exception with detailed context.
     *
     * @param message Human readable error message.
     * @param errorType Category of validation error.
     * @param field Name of the field that failed validation.
     * @param attemptedValue Value that caused validation failure.
     */
    public ValidationException(String message, ValidationErrorType errorType,
                               String field, Object attemptedValue) {
        super(message);
        this.errorType = errorType;
        this.field = field;
        this.attemptedValue = attemptedValue;
    }

    public ValidationErrorType getErrorType() { return errorType; }
    public String getField() { return field; }
    public Object getAttemptedValue() { return attemptedValue; }

    @Override
    public String toString() {
        return String.format("[%s] %s (Field: %s, Value: %s)",
                errorType, getMessage(), field, attemptedValue);
    }
}

/**
 * Exception for stake-related validation failures.
 */
class StakeValidationException extends ValidationException {
    public StakeValidationException(String message, String field, Object attemptedValue) {
        super(message, ValidationErrorType.STAKE_ERROR, field, attemptedValue);
    }
}

/**
 * Exception for bet amount validation failures.
 */
class BetValidationException extends ValidationException {
    public BetValidationException(String message, String field, Object attemptedValue) {
        super(message, ValidationErrorType.BET_ERROR, field, attemptedValue);
    }
}

/**
 * Exception for session limit validation failures.
 */
class LimitValidationException extends ValidationException {
    public LimitValidationException(String message, String field, Object attemptedValue) {
        super(message, ValidationErrorType.LIMIT_ERROR, field, attemptedValue);
    }
}

/**
 * Exception for probability validation failures.
 */
class ProbabilityValidationException extends ValidationException {
    public ProbabilityValidationException(String message, String field, Object attemptedValue) {
        super(message, ValidationErrorType.PROBABILITY_ERROR, field, attemptedValue);
    }
}

/**
 * Defines categories of validation errors used across the system.
 */
enum ValidationErrorType {
    STAKE_ERROR,
    BET_ERROR,
    LIMIT_ERROR,
    PROBABILITY_ERROR,
    NUMERIC_ERROR,
    RANGE_ERROR,
    NULL_ERROR
}