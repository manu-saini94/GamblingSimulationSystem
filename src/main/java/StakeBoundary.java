/**
 * Manages stake boundary validation and warning thresholds.
 * Responsibilities:
 * - Maintains minimum and maximum stake limits.
 * - Provides early warning threshold ranges.
 * - Validates stake boundary compliance.
 * Warning Logic:
 * - Lower Warning = 20% above minimum boundary.
 * - Upper Warning = 80% of maximum boundary.
 */
public class StakeBoundary {
    private double minStake;
    private double maxStake;
    private double warningLowerThreshold;
    private double warningUpperThreshold;

    /**
     * Creates stake boundary configuration with automatic warning thresholds.
     * Warning Threshold Rules:
     * - Lower Warning Threshold = 20% above minimum stake
     * - Upper Warning Threshold = 80% of maximum stake
     * Used to define operational stake limits and early warning detection.
     *
     * @param minStake Minimum allowed stake value.
     * @param maxStake Maximum allowed stake value.
     */
    public StakeBoundary(double minStake, double maxStake) {
        this.minStake = minStake;
        this.maxStake = maxStake;
        this.warningLowerThreshold = minStake * 1.2; // 20% above minimum
        this.warningUpperThreshold = maxStake * 0.8; // 80% of maximum
    }

    // Getters and Setters
    public double getMinStake() { return minStake; }
    public void setMinStake(double minStake) { this.minStake = minStake; }
    public double getMaxStake() { return maxStake; }
    public void setMaxStake(double maxStake) { this.maxStake = maxStake; }
    public double getWarningLowerThreshold() { return warningLowerThreshold; }
    public void setWarningLowerThreshold(double warningLowerThreshold) {
        this.warningLowerThreshold = warningLowerThreshold;
    }
    public double getWarningUpperThreshold() { return warningUpperThreshold; }
    public void setWarningUpperThreshold(double warningUpperThreshold) {
        this.warningUpperThreshold = warningUpperThreshold;
    }

    /**
     * Checks if stake value is within allowed boundaries.
     * @param stake Stake value to validate.
     * @return true if stake is within configured boundaries.
     */
    public boolean isWithinBounds(double stake) {
        return stake >= minStake && stake <= maxStake;
    }

    /**
     * Checks if stake is approaching minimum boundary.
     * @param stake Stake value to evaluate.
     * @return true if stake is near lower warning threshold.
     */
    public boolean isNearLowerBound(double stake) {
        return stake < warningLowerThreshold && stake >= minStake;
    }

    /**
     * Checks if stake is approaching maximum boundary.
     * @param stake Stake value to evaluate.
     * @return true if stake is near upper warning threshold.
     */
    public boolean isNearUpperBound(double stake) {
        return stake > warningUpperThreshold && stake <= maxStake;
    }
}
