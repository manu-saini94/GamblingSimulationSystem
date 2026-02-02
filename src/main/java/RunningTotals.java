import java.util.ArrayList;
import java.util.List;

/**
 * Tracks real-time cumulative financial state.
 * Tracks:
 * - Running balance
 * - Total winnings and losses
 * - Balance history per game
 * - Profit/loss progression
 */
public class RunningTotals {
    private double cumulativeWinnings;
    private double cumulativeLosses;
    private double runningBalance;
    private List<Double> balanceHistory;
    private List<Double> profitLossHistory;

    /**
     * Initializes running totals tracker.
     * @param initialBalance Starting balance.
     */
    public RunningTotals(double initialBalance) {
        this.cumulativeWinnings = 0.0;
        this.cumulativeLosses = 0.0;
        this.runningBalance = initialBalance;
        this.balanceHistory = new ArrayList<>();
        this.profitLossHistory = new ArrayList<>();
        balanceHistory.add(initialBalance);
        profitLossHistory.add(0.0);
    }

    /**
     * Records win transaction.
     * @param amount Winning amount.
     */
    public void recordWin(double amount) {
        cumulativeWinnings += amount;
        runningBalance += amount;
        balanceHistory.add(runningBalance);
        profitLossHistory.add(cumulativeWinnings - cumulativeLosses);
    }

    /**
     * Records loss transaction.
     * @param amount Loss amount.
     */
    public void recordLoss(double amount) {
        cumulativeLosses += amount;
        runningBalance -= amount;
        balanceHistory.add(runningBalance);
        profitLossHistory.add(cumulativeWinnings - cumulativeLosses);
    }

    /**
     * Records push outcome (no change).
     */
    public void recordPush() {
        balanceHistory.add(runningBalance);
        profitLossHistory.add(cumulativeWinnings - cumulativeLosses);
    }

    public double getCumulativeWinnings() { return cumulativeWinnings; }
    public double getCumulativeLosses() { return cumulativeLosses; }
    public double getRunningBalance() { return runningBalance; }
    public List<Double> getBalanceHistory() { return new ArrayList<>(balanceHistory); }
    public List<Double> getProfitLossHistory() { return new ArrayList<>(profitLossHistory); }
    public double getNetProfitLoss() { return cumulativeWinnings - cumulativeLosses; }
}