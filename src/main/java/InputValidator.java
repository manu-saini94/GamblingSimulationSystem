public class InputValidator {
    private double minStake;
    private double maxStake;
    private double minBet;
    private double maxBet;

    public InputValidator(double minStake, double maxStake, double minBet, double maxBet) {
        this.minStake = minStake;
        this.maxStake = maxStake;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public void validateInitialStake(double stake) throws ValidationException {
        if (stake <= 0) {
            throw new ValidationException("Initial stake must be positive");
        }
        if (stake < minStake) {
            throw new ValidationException(
                    String.format("Initial stake $%.2f below minimum $%.2f", stake, minStake));
        }
        if (stake > maxStake) {
            throw new ValidationException(
                    String.format("Initial stake $%.2f exceeds maximum $%.2f", stake, maxStake));
        }
    }

    public void validateBetAmount(double betAmount, double currentStake) throws ValidationException {
        if (betAmount <= 0) {
            throw new ValidationException("Bet amount must be positive");
        }
        if (betAmount < minBet) {
            throw new ValidationException(
                    String.format("Bet $%.2f below minimum $%.2f", betAmount, minBet));
        }
        if (betAmount > maxBet) {
            throw new ValidationException(
                    String.format("Bet $%.2f exceeds maximum $%.2f", betAmount, maxBet));
        }
        if (betAmount > currentStake) {
            throw new ValidationException(
                    String.format("Insufficient stake: $%.2f (need $%.2f)", currentStake, betAmount));
        }
    }

    public void validateLimits(double lowerLimit, double upperLimit,
                               double initialStake) throws ValidationException {
        if (lowerLimit < 0) {
            throw new ValidationException("Lower limit cannot be negative");
        }
        if (upperLimit <= lowerLimit) {
            throw new ValidationException("Upper limit must be greater than lower limit");
        }
        if (initialStake <= lowerLimit) {
            throw new ValidationException("Initial stake must be greater than lower limit");
        }
        if (initialStake >= upperLimit) {
            throw new ValidationException("Initial stake must be less than upper limit");
        }
    }

    public void validateProbability(double probability) throws ValidationException {
        if (probability < 0.0 || probability > 1.0) {
            throw new ValidationException("Probability must be between 0 and 1");
        }
    }

    public double parseNumeric(String input, String fieldName) throws ValidationException {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        try {
            double value = Double.parseDouble(input.trim());
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new ValidationException(fieldName + " must be a valid number");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid number");
        }
    }
}
