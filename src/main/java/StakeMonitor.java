import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides real-time monitoring of stake movements during session.
 * Tracks:
 * - Current stake
 * - Peak stake reached
 * - Lowest stake reached
 * - Stake volatility across transactions
 * - Historical stake timeline
 * Used for:
 * - Risk monitoring
 * - Session analytics
 * - Behavioral tracking
 */
public class StakeMonitor {
    private String gamblerId;
    private double currentStake;
    private double peakStake;
    private double lowestStake;
    private double volatility;
    private List<Double> stakeHistory;
    private LocalDateTime sessionStart;

    /**
     * Initializes real-time monitoring session.
     * @param gamblerId Unique gambler identifier.
     * @param initialStake Starting stake for monitoring session.
     */
    public StakeMonitor(String gamblerId, double initialStake) {
        this.gamblerId = gamblerId;
        this.currentStake = initialStake;
        this.peakStake = initialStake;
        this.lowestStake = initialStake;
        this.volatility = 0.0;
        this.stakeHistory = new ArrayList<>();
        this.stakeHistory.add(initialStake);
        this.sessionStart = LocalDateTime.now();
    }

    /**
     * Updates stake values and recalculates analytics.
     * @param newStake Updated stake value.
     */
    public void updateStake(double newStake) {
        stakeHistory.add(newStake);
        currentStake = newStake;

        if (newStake > peakStake) {
            peakStake = newStake;
        }
        if (newStake < lowestStake) {
            lowestStake = newStake;
        }

        calculateVolatility();
    }

    /**
     * Calculates stake volatility based on historical stake changes.
     * Volatility is defined as:
     * Average absolute difference between consecutive stake values.
     * If insufficient history exists (less than 2 entries),
     * volatility is set to zero.
     * Used internally whenever stake history updates.
     */
    private void calculateVolatility() {
        if (stakeHistory.size() < 2) {
            volatility = 0.0;
            return;
        }

        double sum = 0.0;
        for (int i = 1; i < stakeHistory.size(); i++) {
            double change = Math.abs(stakeHistory.get(i) - stakeHistory.get(i - 1));
            sum += change;
        }
        volatility = sum / (stakeHistory.size() - 1);
    }

    public double getCurrentStake() { return currentStake; }
    public double getPeakStake() { return peakStake; }
    public double getLowestStake() { return lowestStake; }
    public double getVolatility() { return volatility; }
    public List<Double> getStakeHistory() { return new ArrayList<>(stakeHistory); }
    public LocalDateTime getSessionStart() { return sessionStart; }
}

