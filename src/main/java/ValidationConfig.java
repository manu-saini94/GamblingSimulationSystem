/**
 * Stores validation configuration rules for stakes,
 * bets, and probabilities.
 */
public class ValidationConfig {
    private double minStake;
    private double maxStake;
    private double minBet;
    private double maxBet;
    private double minProbability;
    private double maxProbability;
    private boolean allowZeroStake;
    private boolean strictMode;

    public ValidationConfig() {
        this.minStake = 1.0;
        this.maxStake = 1000000.0;
        this.minBet = 1.0;
        this.maxBet = 100000.0;
        this.minProbability = 0.01;
        this.maxProbability = 0.99;
        this.allowZeroStake = false;
        this.strictMode = true;
    }

    // Getters and Setters
    public double getMinStake() { return minStake; }
    public void setMinStake(double minStake) { this.minStake = minStake; }
    public double getMaxStake() { return maxStake; }
    public void setMaxStake(double maxStake) { this.maxStake = maxStake; }
    public double getMinBet() { return minBet; }
    public void setMinBet(double minBet) { this.minBet = minBet; }
    public double getMaxBet() { return maxBet; }
    public void setMaxBet(double maxBet) { this.maxBet = maxBet; }
    public double getMinProbability() { return minProbability; }
    public void setMinProbability(double minProbability) { this.minProbability = minProbability; }
    public double getMaxProbability() { return maxProbability; }
    public void setMaxProbability(double maxProbability) { this.maxProbability = maxProbability; }
    public boolean isAllowZeroStake() { return allowZeroStake; }
    public void setAllowZeroStake(boolean allowZeroStake) { this.allowZeroStake = allowZeroStake; }
    public boolean isStrictMode() { return strictMode; }
    public void setStrictMode(boolean strictMode) { this.strictMode = strictMode; }
}
