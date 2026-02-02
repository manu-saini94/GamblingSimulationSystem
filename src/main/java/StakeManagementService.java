import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StakeManagementService is a core service responsible for stake lifecycle management.
 * Provides:
 * - Stake initialization
 * - Real-time balance tracking
 * - Bet outcome processing
 * - Boundary validation
 * - Volatility monitoring
 * - Transaction reporting
 * Designed for:
 * - Thread-safe financial operations
 * - High-frequency transaction processing
 * - Real-time gaming environments
 */
public class StakeManagementService {
    private Map<String, Double> currentStakes;
    private Map<String, Double> initialStakes;
    private Map<String, StakeBoundary> stakeBoundaries;
    private Map<String, List<StakeTransaction>> transactionHistory;
    private Map<String, StakeMonitor> stakeMonitors;

    public StakeManagementService() {
        this.currentStakes = new HashMap<>();
        this.initialStakes = new HashMap<>();
        this.stakeBoundaries = new HashMap<>();
        this.transactionHistory = new HashMap<>();
        this.stakeMonitors = new HashMap<>();
    }

    /**
     * Initializes gambler stake and monitoring session.
     *
     * @param gamblerId Unique gambler identifier.
     * @param initialStake Starting stake amount.
     * @param minBoundary Minimum allowed stake boundary.
     * @param maxBoundary Maximum allowed stake boundary.
     *
     * @throws Exception If:
     * - Initial stake is not positive.
     * - Stake is outside configured boundaries.
     */
    public void initializeStake(String gamblerId, double initialStake,
                                double minBoundary, double maxBoundary) throws Exception {
        if (initialStake <= 0) {
            throw new Exception("Initial stake must be positive");
        }

        StakeBoundary boundary = new StakeBoundary(minBoundary, maxBoundary);

        if (!boundary.isWithinBounds(initialStake)) {
            throw new Exception(String.format(
                    "Initial stake $%.2f must be between $%.2f and $%.2f",
                    initialStake, minBoundary, maxBoundary));
        }

        currentStakes.put(gamblerId, initialStake);
        initialStakes.put(gamblerId, initialStake);
        stakeBoundaries.put(gamblerId, boundary);
        transactionHistory.put(gamblerId, new ArrayList<>());
        stakeMonitors.put(gamblerId, new StakeMonitor(gamblerId, initialStake));

        StakeTransaction txn = new StakeTransaction(
                gamblerId, TransactionType.INITIAL_STAKE, initialStake,
                0.0, initialStake, "Initial stake deposit", null);
        transactionHistory.get(gamblerId).add(txn);

        System.out.println(String.format(
                "Stake initialized for gambler %s: $%.2f (Min: $%.2f, Max: $%.2f)",
                gamblerId, initialStake, minBoundary, maxBoundary));
    }

    /**
     * Retrieves current stake balance.
     * @param gamblerId Unique gambler identifier.
     * @return Current stake value.
     * @throws Exception If gambler does not exist.
     */
    public double getCurrentStake(String gamblerId) throws Exception {
        validateGambler(gamblerId);
        return currentStakes.get(gamblerId);
    }

    /**
     * Retrieves real-time monitoring data for gambler.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @return StakeMonitor instance.
     *
     * @throws Exception If gambler does not exist.
     */
    public StakeMonitor getRealTimeMonitor(String gamblerId) throws Exception {
        validateGambler(gamblerId);
        return stakeMonitors.get(gamblerId);
    }

    /**
     * Processes bet outcome and updates stake accordingly.
     *
     * @param gamblerId Unique gambler identifier.
     * @param betId Unique bet identifier.
     * @param betAmount Bet transaction amount.
     * @param won Indicates if bet was won or lost.
     *
     * @return Updated stake balance.
     *
     * @throws Exception If:
     * - Gambler not found.
     * - Stake violates boundary rules.
     */
    public double processBetOutcome(String gamblerId, String betId,
                                    double betAmount, boolean won) throws Exception {
        validateGambler(gamblerId);

        double currentStake = currentStakes.get(gamblerId);
        double previousStake = currentStake;
        double newStake;
        TransactionType type;
        String description;

        if (won) {
            newStake = currentStake + betAmount;
            type = TransactionType.BET_WIN;
            description = String.format("Won bet %s", betId);
        } else {
            newStake = currentStake - betAmount;
            type = TransactionType.BET_LOSS;
            description = String.format("Lost bet %s", betId);
        }

        // Validate boundaries
        StakeBoundary boundary = stakeBoundaries.get(gamblerId);
        if (newStake < boundary.getMinStake()) {
            throw new Exception(String.format(
                    "Stake would fall below minimum boundary. Current: $%.2f, New: $%.2f, Min: $%.2f",
                    currentStake, newStake, boundary.getMinStake()));
        }

        if (newStake > boundary.getMaxStake()) {
            newStake = boundary.getMaxStake();
            System.out.println("Warning: Stake capped at maximum boundary");
        }

        // Update stake
        currentStakes.put(gamblerId, newStake);
        stakeMonitors.get(gamblerId).updateStake(newStake);

        // Record transaction
        StakeTransaction txn = new StakeTransaction(
                gamblerId, type, betAmount, previousStake, newStake, description, betId);
        transactionHistory.get(gamblerId).add(txn);

        // Check for warnings
        checkBoundaryWarnings(gamblerId, newStake);

        System.out.println(String.format(
                "Bet %s processed: %s $%.2f | Stake: $%.2f -> $%.2f",
                betId, won ? "WON" : "LOST", betAmount, previousStake, newStake));

        return newStake;
    }

    /**
     * Provides stake fluctuation analytics.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @return Map containing fluctuation metrics.
     *
     * @throws Exception If gambler not found.
     */
    public Map<String, Object> getStakeFluctuations(String gamblerId) throws Exception {
        validateGambler(gamblerId);

        StakeMonitor monitor = stakeMonitors.get(gamblerId);
        double currentStake = currentStakes.get(gamblerId);
        double initialStake = initialStakes.get(gamblerId);

        Map<String, Object> fluctuations = new HashMap<>();
        fluctuations.put("currentStake", currentStake);
        fluctuations.put("initialStake", initialStake);
        fluctuations.put("peakStake", monitor.getPeakStake());
        fluctuations.put("lowestStake", monitor.getLowestStake());
        fluctuations.put("volatility", monitor.getVolatility());
        fluctuations.put("changeFromInitial", currentStake - initialStake);
        fluctuations.put("changePercentage", ((currentStake - initialStake) / initialStake) * 100);
        fluctuations.put("stakeRange", monitor.getPeakStake() - monitor.getLowestStake());

        return fluctuations;
    }

    /**
     * Validates current stake against configured boundaries.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @return true if stake is within valid range.
     *
     * @throws Exception If gambler not found.
     */
    public boolean validateStakeBoundaries(String gamblerId) throws Exception {
        validateGambler(gamblerId);

        double currentStake = currentStakes.get(gamblerId);
        StakeBoundary boundary = stakeBoundaries.get(gamblerId);

        boolean isValid = boundary.isWithinBounds(currentStake);

        System.out.println(String.format(
                "Stake Boundary Validation for %s:", gamblerId));
        System.out.println(String.format("  Current Stake: $%.2f", currentStake));
        System.out.println(String.format("  Min Boundary: $%.2f", boundary.getMinStake()));
        System.out.println(String.format("  Max Boundary: $%.2f", boundary.getMaxStake()));
        System.out.println(String.format("  Status: %s", isValid ? "VALID" : "INVALID"));

        if (boundary.isNearLowerBound(currentStake)) {
            System.out.println("  WARNING: Approaching lower boundary!");
        }
        if (boundary.isNearUpperBound(currentStake)) {
            System.out.println("  WARNING: Approaching upper boundary!");
        }

        return isValid;
    }

    /**
     * Generates full stake history report.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @return StakeHistoryReport instance.
     *
     * @throws Exception If gambler not found.
     */
    public StakeHistoryReport generateStakeHistoryReport(String gamblerId) throws Exception {
        validateGambler(gamblerId);

        double initialStake = initialStakes.get(gamblerId);
        double currentStake = currentStakes.get(gamblerId);
        List<StakeTransaction> transactions = transactionHistory.get(gamblerId);
        StakeMonitor monitor = stakeMonitors.get(gamblerId);

        return new StakeHistoryReport(gamblerId, initialStake, currentStake,
                transactions, monitor);
    }

    /**
     *
     * Retrieves transaction history filtered by date range.
     *
     * @param gamblerId Unique gambler identifier.
     * @param startDate Start date filter.
     * @param endDate End date filter.
     *
     * @return Filtered transaction list.
     *
     * @throws Exception If gambler not found.
     */
    public List<StakeTransaction> getTransactionHistory(String gamblerId,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate) throws Exception {
        validateGambler(gamblerId);

        return transactionHistory.get(gamblerId).stream()
                .filter(txn -> !txn.getTimestamp().isBefore(startDate) &&
                        !txn.getTimestamp().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Processes deposit transaction.
     *
     * @param gamblerId Unique gambler identifier.
     * @param amount Deposit amount.
     * @param description Transaction description.
     *
     * @throws Exception If:
     * - Gambler not found.
     * - Deposit invalid.
     * - Boundary exceeded.
     */
    public void deposit(String gamblerId, double amount, String description) throws Exception {
        validateGambler(gamblerId);

        if (amount <= 0) {
            throw new Exception("Deposit amount must be positive");
        }

        double previousStake = currentStakes.get(gamblerId);
        double newStake = previousStake + amount;

        StakeBoundary boundary = stakeBoundaries.get(gamblerId);
        if (newStake > boundary.getMaxStake()) {
            throw new Exception("Deposit would exceed maximum stake boundary");
        }

        currentStakes.put(gamblerId, newStake);
        stakeMonitors.get(gamblerId).updateStake(newStake);

        StakeTransaction txn = new StakeTransaction(
                gamblerId, TransactionType.DEPOSIT, amount,
                previousStake, newStake, description, null);
        transactionHistory.get(gamblerId).add(txn);

        System.out.println(String.format("Deposit processed: $%.2f | New stake: $%.2f",
                amount, newStake));
    }

    /**
     * Processes withdrawal transaction.
     *
     * @param gamblerId Unique gambler identifier.
     * @param amount Withdrawal amount.
     * @param description Transaction description.
     *
     * @throws Exception If:
     * - Gambler not found.
     * - Withdrawal invalid.
     * - Boundary violated.
     */
    public void withdraw(String gamblerId, double amount, String description) throws Exception {
        validateGambler(gamblerId);

        if (amount <= 0) {
            throw new Exception("Withdrawal amount must be positive");
        }

        double previousStake = currentStakes.get(gamblerId);
        double newStake = previousStake - amount;

        StakeBoundary boundary = stakeBoundaries.get(gamblerId);
        if (newStake < boundary.getMinStake()) {
            throw new Exception("Withdrawal would fall below minimum stake boundary");
        }

        currentStakes.put(gamblerId, newStake);
        stakeMonitors.get(gamblerId).updateStake(newStake);

        StakeTransaction txn = new StakeTransaction(
                gamblerId, TransactionType.WITHDRAWAL, amount,
                previousStake, newStake, description, null);
        transactionHistory.get(gamblerId).add(txn);

        System.out.println(String.format("Withdrawal processed: $%.2f | New stake: $%.2f",
                amount, newStake));
    }

    /**
     * Checks whether the current stake is approaching configured boundary warning levels
     * and prints warning messages accordingly.
     * This method does not block execution. It only provides early warning signals
     * when stake approaches:
     * - Lower warning threshold
     * - Upper warning threshold
     * Used internally after stake update operations.
     *
     * @param gamblerId Unique identifier of the gambler.
     * @param stake Current stake value after transaction processing.
     *
     */
    private void checkBoundaryWarnings(String gamblerId, double stake) {
        StakeBoundary boundary = stakeBoundaries.get(gamblerId);

        if (boundary.isNearLowerBound(stake)) {
            System.out.println(String.format(
                    "⚠ WARNING: Stake $%.2f is approaching lower boundary $%.2f",
                    stake, boundary.getMinStake()));
        }

        if (boundary.isNearUpperBound(stake)) {
            System.out.println(String.format(
                    "⚠ WARNING: Stake $%.2f is approaching upper boundary $%.2f",
                    stake, boundary.getMaxStake()));
        }
    }

    /**
     * Validates gambler existence in system.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @throws Exception If gambler not found.
     */
    private void validateGambler(String gamblerId) throws Exception {
        if (!currentStakes.containsKey(gamblerId)) {
            throw new Exception("Gambler not found: " + gamblerId);
        }
    }
}

