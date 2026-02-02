/**
 * Holds configurable session limits and betting configuration.
 * Includes:
 * - Stake boundaries
 * - Bet limits
 * - Session limits (time and game count)
 * - Default win probability
 */
public class SessionParameters {
    private double initialStake;
    private double upperLimit;
    private double lowerLimit;
    private double minBetAmount;
    private double maxBetAmount;
    private int maxGamesPerSession;
    private long maxSessionDurationMinutes;
    private double defaultWinProbability;

    /**
     *
     * Creates session parameter configuration.
     *
     * @param initialStake Initial session stake.
     * @param upperLimit Maximum allowed stake.
     * @param lowerLimit Minimum allowed stake.
     */
    public SessionParameters(double initialStake, double upperLimit, double lowerLimit) {
        this.initialStake = initialStake;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.minBetAmount = 10.0;
        this.maxBetAmount = 500.0;
        this.maxGamesPerSession = 1000;
        this.maxSessionDurationMinutes = 180; // 3 hours
        this.defaultWinProbability = 0.5;
    }

    // Getters and Setters
    public double getInitialStake() { return initialStake; }
    public double getUpperLimit() { return upperLimit; }
    public double getLowerLimit() { return lowerLimit; }
    public double getMinBetAmount() { return minBetAmount; }
    public void setMinBetAmount(double minBetAmount) { this.minBetAmount = minBetAmount; }
    public double getMaxBetAmount() { return maxBetAmount; }
    public void setMaxBetAmount(double maxBetAmount) { this.maxBetAmount = maxBetAmount; }
    public int getMaxGamesPerSession() { return maxGamesPerSession; }
    public void setMaxGamesPerSession(int maxGamesPerSession) {
        this.maxGamesPerSession = maxGamesPerSession;
    }
    public long getMaxSessionDurationMinutes() { return maxSessionDurationMinutes; }
    public void setMaxSessionDurationMinutes(long maxSessionDurationMinutes) {
        this.maxSessionDurationMinutes = maxSessionDurationMinutes;
    }
    public double getDefaultWinProbability() { return defaultWinProbability; }
    public void setDefaultWinProbability(double defaultWinProbability) {
        this.defaultWinProbability = defaultWinProbability;
    }

    /**
     * Checks if stake is within session boundaries.
     * @param stake Stake value to validate.
     * @return True if stake is within limits.
     */
    public boolean isWithinBoundaries(double stake) {
        return stake > lowerLimit && stake < upperLimit;
    }

    /**
     * Checks if stake reached upper boundary.
     * @param stake Current stake.
     * @return True if upper limit reached.
     */
    public boolean hasReachedUpperLimit(double stake) {
        return stake >= upperLimit;
    }

    /**
     * Checks if stake reached lower boundary.
     * @param stake Current stake.
     * @return True if lower limit reached.
     */
    public boolean hasReachedLowerLimit(double stake) {
        return stake <= lowerLimit;
    }
}
