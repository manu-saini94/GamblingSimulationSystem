import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StakeHistoryReport class generates comprehensive stake history reports.
 * Includes:
 * - Financial summaries
 * - Transaction statistics
 * - Profit / Loss analytics
 * - Transaction type breakdowns
 * - Session performance metrics
 */
public class StakeHistoryReport {
    private String gamblerId;
    private LocalDateTime generatedAt;
    private double initialStake;
    private double currentStake;
    private double peakStake;
    private double lowestStake;
    private double totalDeposits;
    private double totalWithdrawals;
    private double totalWinnings;
    private double totalLosses;
    private double netProfitLoss;
    private int totalTransactions;
    private List<StakeTransaction> transactions;
    private Map<TransactionType, Integer> transactionTypeCounts;
    private Map<TransactionType, Double> transactionTypeAmounts;

    /**
     * Creates stake history report snapshot.
     * @param gamblerId Unique gambler identifier.
     * @param initialStake Initial stake at session start.
     * @param currentStake Current stake value.
     * @param transactions Complete transaction history list.
     * @param monitor Stake monitoring analytics instance.
     */
    public StakeHistoryReport(String gamblerId, double initialStake, double currentStake,
                              List<StakeTransaction> transactions, StakeMonitor monitor) {
        this.gamblerId = gamblerId;
        this.generatedAt = LocalDateTime.now();
        this.initialStake = initialStake;
        this.currentStake = currentStake;
        this.transactions = new ArrayList<>(transactions);
        this.totalTransactions = transactions.size();
        this.peakStake = monitor.getPeakStake();
        this.lowestStake = monitor.getLowestStake();

        calculateTotals();
        this.netProfitLoss = currentStake - initialStake;
    }

    /**
     * Calculates aggregate totals and transaction distribution metrics.
     * This method:
     * - Initializes transaction summary maps
     * - Calculates total deposits, withdrawals, winnings, and losses
     * - Computes transaction type counts and amount totals
     * Used internally during report generation.
     */
    private void calculateTotals() {
        transactionTypeCounts = new HashMap<>();
        transactionTypeAmounts = new HashMap<>();

        totalDeposits = 0.0;
        totalWithdrawals = 0.0;
        totalWinnings = 0.0;
        totalLosses = 0.0;

        for (StakeTransaction txn : transactions) {
            transactionTypeCounts.merge(txn.getType(), 1, Integer::sum);
            transactionTypeAmounts.merge(txn.getType(), txn.getAmount(), Double::sum);

            switch (txn.getType()) {
                case DEPOSIT:
                    totalDeposits += txn.getAmount();
                    break;
                case WITHDRAWAL:
                    totalWithdrawals += txn.getAmount();
                    break;
                case BET_WIN:
                    totalWinnings += txn.getAmount();
                    break;
                case BET_LOSS:
                    totalLosses += txn.getAmount();
                    break;
            }
        }
    }

    /**
     *
     * Generates formatted textual summary report.
     * @return Formatted multi-line report string.
     */
    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== STAKE HISTORY REPORT ==========\n");
        sb.append(String.format("Gambler ID: %s\n", gamblerId));
        sb.append(String.format("Generated: %s\n\n",
                generatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        sb.append("--- STAKE OVERVIEW ---\n");
        sb.append(String.format("Initial Stake: $%.2f\n", initialStake));
        sb.append(String.format("Current Stake: $%.2f\n", currentStake));
        sb.append(String.format("Peak Stake: $%.2f\n", peakStake));
        sb.append(String.format("Lowest Stake: $%.2f\n", lowestStake));
        sb.append(String.format("Net Profit/Loss: $%.2f (%.2f%%)\n\n",
                netProfitLoss, (netProfitLoss / initialStake) * 100));

        sb.append("--- TRANSACTION SUMMARY ---\n");
        sb.append(String.format("Total Transactions: %d\n", totalTransactions));
        sb.append(String.format("Total Deposits: $%.2f\n", totalDeposits));
        sb.append(String.format("Total Withdrawals: $%.2f\n", totalWithdrawals));
        sb.append(String.format("Total Winnings: $%.2f\n", totalWinnings));
        sb.append(String.format("Total Losses: $%.2f\n\n", totalLosses));

        sb.append("--- TRANSACTION TYPE BREAKDOWN ---\n");
        for (TransactionType type : transactionTypeCounts.keySet()) {
            sb.append(String.format("%s: %d transactions, Total: $%.2f\n",
                    type, transactionTypeCounts.get(type),
                    transactionTypeAmounts.getOrDefault(type, 0.0)));
        }

        sb.append("\n==========================================\n");
        return sb.toString();
    }

    public List<StakeTransaction> getTransactions() { return transactions; }
    public double getNetProfitLoss() { return netProfitLoss; }
}

