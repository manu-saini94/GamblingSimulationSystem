import java.util.HashMap;
import java.util.Map;

/**
 * Central validation engine for gambling simulation system.
 * Provides validation for stake, bet, limits, numeric parsing,
 * probability and batch validation.
 */
public class InputValidator {
    private ValidationConfig config;

    /**
     * Creates validator using provided configuration.
     * @param config Validation configuration.
     */
    public InputValidator(ValidationConfig config) {
        this.config = config;
    }

    public InputValidator() {
        this(new ValidationConfig());
    }

    /**
     * Validates initial stake value.
     * @param stake Stake value.
     * @throws StakeValidationException If stake invalid.
     */
    public void validateInitialStake(double stake) throws StakeValidationException {
        if (Double.isNaN(stake) || Double.isInfinite(stake)) {
            throw new StakeValidationException(
                    "Initial stake must be a valid number", "initialStake", stake);
        }

        if (stake < 0) {
            throw new StakeValidationException(
                    "Initial stake cannot be negative", "initialStake", stake);
        }

        if (stake == 0 && !config.isAllowZeroStake()) {
            throw new StakeValidationException(
                    "Initial stake must be positive", "initialStake", stake);
        }

        if (stake < config.getMinStake()) {
            throw new StakeValidationException(
                    String.format("Initial stake $%.2f is below minimum $%.2f",
                            stake, config.getMinStake()),
                    "initialStake", stake);
        }

        if (stake > config.getMaxStake()) {
            throw new StakeValidationException(
                    String.format("Initial stake $%.2f exceeds maximum $%.2f",
                            stake, config.getMaxStake()),
                    "initialStake", stake);
        }
    }

    /**
     * Validates bet amount against stake and configuration.
     * @param betAmount Bet value.
     * @param currentStake Current stake value.
     * @throws BetValidationException If bet invalid.
     */
    public void validateBetAmount(double betAmount, double currentStake)
            throws BetValidationException {

        if (Double.isNaN(betAmount) || Double.isInfinite(betAmount)) {
            throw new BetValidationException(
                    "Bet amount must be a valid number", "betAmount", betAmount);
        }

        if (betAmount <= 0) {
            throw new BetValidationException(
                    "Bet amount must be positive", "betAmount", betAmount);
        }

        if (betAmount < config.getMinBet()) {
            throw new BetValidationException(
                    String.format("Bet amount $%.2f is below minimum $%.2f",
                            betAmount, config.getMinBet()),
                    "betAmount", betAmount);
        }

        if (betAmount > config.getMaxBet()) {
            throw new BetValidationException(
                    String.format("Bet amount $%.2f exceeds maximum $%.2f",
                            betAmount, config.getMaxBet()),
                    "betAmount", betAmount);
        }

        if (betAmount > currentStake) {
            throw new BetValidationException(
                    String.format("Insufficient stake: Bet $%.2f exceeds current stake $%.2f",
                            betAmount, currentStake),
                    "betAmount", betAmount);
        }
    }

    /**
     * Validates session limits.
     * @param lowerLimit Lower stake boundary.
     * @param upperLimit Upper stake boundary.
     * @param initialStake Initial session stake.
     * @throws LimitValidationException If limits invalid.
     */
    public void validateLimits(double lowerLimit, double upperLimit, double initialStake)
            throws LimitValidationException {

        if (Double.isNaN(lowerLimit) || Double.isInfinite(lowerLimit)) {
            throw new LimitValidationException(
                    "Lower limit must be a valid number", "lowerLimit", lowerLimit);
        }

        if (Double.isNaN(upperLimit) || Double.isInfinite(upperLimit)) {
            throw new LimitValidationException(
                    "Upper limit must be a valid number", "upperLimit", upperLimit);
        }

        if (lowerLimit < 0) {
            throw new LimitValidationException(
                    "Lower limit cannot be negative", "lowerLimit", lowerLimit);
        }

        if (upperLimit <= lowerLimit) {
            throw new LimitValidationException(
                    String.format("Upper limit $%.2f must be greater than lower limit $%.2f",
                            upperLimit, lowerLimit),
                    "limits", String.format("lower=%.2f, upper=%.2f", lowerLimit, upperLimit));
        }

        if (initialStake <= lowerLimit) {
            throw new LimitValidationException(
                    String.format("Initial stake $%.2f must be greater than lower limit $%.2f",
                            initialStake, lowerLimit),
                    "initialStake", initialStake);
        }

        if (initialStake >= upperLimit) {
            throw new LimitValidationException(
                    String.format("Initial stake $%.2f must be less than upper limit $%.2f",
                            initialStake, upperLimit),
                    "initialStake", initialStake);
        }
    }

    /**
     * Parses numeric input and validates numeric correctness.
     * @param input String numeric input.
     * @param fieldName Field name for error reporting.
     * @return Parsed numeric value.
     * @throws ValidationException If parsing or numeric validation fails.
     */
    public double parseAndValidateNumeric(String input, String fieldName)
            throws ValidationException {

        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(
                    String.format("Field '%s' cannot be null or empty", fieldName),
                    ValidationErrorType.NULL_ERROR, fieldName, input);
        }

        try {
            double value = Double.parseDouble(input.trim());

            if (Double.isNaN(value)) {
                throw new ValidationException(
                        String.format("Field '%s' contains NaN value", fieldName),
                        ValidationErrorType.NUMERIC_ERROR, fieldName, input);
            }

            if (Double.isInfinite(value)) {
                throw new ValidationException(
                        String.format("Field '%s' contains infinite value", fieldName),
                        ValidationErrorType.NUMERIC_ERROR, fieldName, input);
            }

            return value;

        } catch (NumberFormatException e) {
            throw new ValidationException(
                    String.format("Field '%s' contains invalid numeric value: %s", fieldName, input),
                    ValidationErrorType.NUMERIC_ERROR, fieldName, input);
        }
    }

    /**
     * Validates stake is non-negative.
     * @param stake Stake value.
     * @throws StakeValidationException If stake invalid.
     */
    public void validateStakeNonNegative(double stake) throws StakeValidationException {
        if (Double.isNaN(stake)) {
            throw new StakeValidationException(
                    "Stake value is NaN", "stake", stake);
        }

        if (stake < 0) {
            throw new StakeValidationException(
                    String.format("Stake cannot be negative: $%.2f", stake),
                    "stake", stake);
        }

        if (config.isStrictMode() && stake == 0 && !config.isAllowZeroStake()) {
            throw new StakeValidationException(
                    "Stake cannot be zero in strict mode", "stake", stake);
        }
    }

    /**
     * Validates probability range.
     * @param probability Probability value.
     * @throws ProbabilityValidationException If invalid.
     */
    public void validateProbability(double probability) throws ProbabilityValidationException {
        if (Double.isNaN(probability) || Double.isInfinite(probability)) {
            throw new ProbabilityValidationException(
                    "Probability must be a valid number", "probability", probability);
        }

        if (probability < 0.0 || probability > 1.0) {
            throw new ProbabilityValidationException(
                    String.format("Probability %.4f must be between 0.0 and 1.0", probability),
                    "probability", probability);
        }

        if (probability < config.getMinProbability()) {
            throw new ProbabilityValidationException(
                    String.format("Probability %.4f is below minimum %.4f",
                            probability, config.getMinProbability()),
                    "probability", probability);
        }

        if (probability > config.getMaxProbability()) {
            throw new ProbabilityValidationException(
                    String.format("Probability %.4f exceeds maximum %.4f",
                            probability, config.getMaxProbability()),
                    "probability", probability);
        }
    }

    /**
     * Performs comprehensive session parameter validation.
     * @param initialStake Initial stake.
     * @param lowerLimit Lower boundary.
     * @param upperLimit Upper boundary.
     * @param minBet Minimum bet.
     * @param maxBet Maximum bet.
     * @param defaultProbability Default probability.
     * @return ValidationResult object.
     */
    public ValidationResult validateSessionParameters(double initialStake, double lowerLimit,
                                                      double upperLimit, double minBet,
                                                      double maxBet, double defaultProbability) {
        ValidationResult result = new ValidationResult();

        try {
            validateInitialStake(initialStake);
        } catch (ValidationException e) {
            result.addError(e.getMessage());
        }

        try {
            validateLimits(lowerLimit, upperLimit, initialStake);
        } catch (ValidationException e) {
            result.addError(e.getMessage());
        }

        try {
            if (minBet <= 0) {
                result.addError("Minimum bet must be positive");
            }
            if (maxBet <= 0) {
                result.addError("Maximum bet must be positive");
            }
            if (maxBet < minBet) {
                result.addError("Maximum bet must be greater than minimum bet");
            }
            if (minBet > initialStake) {
                result.addError("Minimum bet cannot exceed initial stake");
            }
        } catch (Exception e) {
            result.addError("Invalid bet range configuration: " + e.getMessage());
        }

        try {
            validateProbability(defaultProbability);
        } catch (ValidationException e) {
            result.addError(e.getMessage());
        }

        // Warnings
        if (upperLimit > initialStake * 10) {
            result.addWarning("Upper limit is very high compared to initial stake");
        }
        if (lowerLimit < initialStake * 0.1) {
            result.addWarning("Lower limit is very low compared to initial stake");
        }
        if (maxBet > initialStake * 0.5) {
            result.addWarning("Maximum bet is more than 50% of initial stake");
        }

        return result;
    }

    /**
     * Performs batch validation for multiple inputs.
     * @param inputs Map of field name to value.
     * @return Map of validation results.
     */
    public Map<String, ValidationResult> validateBatch(Map<String, Object> inputs) {
        Map<String, ValidationResult> results = new HashMap<>();

        inputs.forEach((key, value) -> {
            ValidationResult result = new ValidationResult();
            try {
                if (value instanceof Double) {
                    double numValue = (Double) value;

                    if (key.contains("stake") || key.contains("Stake")) {
                        validateStakeNonNegative(numValue);
                    } else if (key.contains("probability") || key.contains("Probability")) {
                        validateProbability(numValue);
                    } else if (key.contains("bet") || key.contains("Bet")) {
                        if (numValue <= 0) {
                            result.addError("Value must be positive");
                        }
                    }
                } else if (value == null) {
                    result.addError("Value cannot be null");
                } else if (value instanceof String) {
                    parseAndValidateNumeric((String) value, key);
                }
            } catch (ValidationException e) {
                result.addError(e.getMessage());
            }
            results.put(key, result);
        });

        return results;
    }
}