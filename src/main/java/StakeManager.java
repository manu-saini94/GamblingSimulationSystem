import java.util.ArrayList;
import java.util.List;

public class StakeManager {
    private double currentStake;
    private double initialStake;
    private double minBoundary;
    private double maxBoundary;
    private List<StakeTransaction> transactionHistory;
    private double peakStake;
    private double lowestStake;

    public StakeManager(double initialStake, double minBoundary, double maxBoundary) {
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.transactionHistory = new ArrayList<>();
        this.peakStake = initialStake;
        this.lowestStake = initialStake;

        recordTransaction(TransactionType.INITIAL_STAKE, initialStake, 0, initialStake);
    }

    public void processBetOutcome(double betAmount, boolean won) throws Exception {
        double previousStake = currentStake;
        double newStake;

        if (won) {
            newStake = currentStake + betAmount;
            recordTransaction(TransactionType.BET_WIN, betAmount, previousStake, newStake);
        } else {
            newStake = currentStake - betAmount;
            recordTransaction(TransactionType.BET_LOSS, betAmount, previousStake, newStake);
        }

        if (newStake < minBoundary) {
            throw new Exception("Stake below minimum boundary");
        }

        currentStake = newStake;
        updatePeakAndLowest(newStake);
    }

    private void recordTransaction(TransactionType type, double amount,
                                   double previous, double newBalance) {
        transactionHistory.add(new StakeTransaction(type, amount, previous, newBalance));
    }

    private void updatePeakAndLowest(double stake) {
        if (stake > peakStake) peakStake = stake;
        if (stake < lowestStake) lowestStake = stake;
    }

    public boolean isWithinBoundaries() {
        return currentStake >= minBoundary && currentStake <= maxBoundary;
    }

    public double getCurrentStake() { return currentStake; }
    public double getInitialStake() { return initialStake; }
    public double getMinBoundary() { return minBoundary; }
    public double getMaxBoundary() { return maxBoundary; }
    public double getPeakStake() { return peakStake; }
    public double getLowestStake() { return lowestStake; }
    public List<StakeTransaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}

