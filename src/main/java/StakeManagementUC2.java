import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ================================================================
 * UC2: Stake Management Operations - Execution Entry Point
 * ================================================================

 * This class demonstrates the complete working workflow of the
 * Stake Management Operations module (UC2).

 * Purpose:
 * - Acts as a simulation runner for stake lifecycle operations.
 * - Demonstrates real-time financial transaction handling.
 * - Validates boundary management and reporting capabilities.

 * Demonstrated Functional Flow:

 * 1. Stake Initialization
 *    - Creates gambler stake account.
 *    - Applies boundary validation rules.
 *    - Starts real-time monitoring session.

 * 2. Real-Time Stake Tracking
 *    - Retrieves current stake balance.
 *    - Demonstrates real-time state access.

 * 3. Bet Outcome Processing
 *    - Simulates multiple betting outcomes.
 *    - Automatically updates stake balance.
 *    - Records audit transactions.
 *    - Updates monitoring analytics.

 * 4. External Financial Operations
 *    - Deposit processing.
 *    - Withdrawal processing.
 *    - Boundary validations during transactions.

 * 5. Stake Fluctuation Monitoring
 *    - Peak stake tracking.
 *    - Lowest stake tracking.
 *    - Volatility calculations.
 *    - Percentage change from initial stake.

 * 6. Boundary Validation
 *    - Validates stake against configured limits.
 *    - Provides early warning signals.

 * 7. Stake History Reporting
 *    - Generates full session financial report.
 *    - Provides transaction summaries.
 *    - Shows transaction history timeline.

 * Key Demonstrated Features:
 * - Complete transaction audit trail.
 * - Real-time monitoring analytics.
 * - Automated boundary validation.
 * - Financial risk monitoring.
 * - Detailed reporting and filtering.
 * - Exception handling workflow demonstration.

 * This class is intended for:
 * - Demo execution
 * - Integration testing
 * - Functional validation
 * - Developer understanding of UC2 flow

 * ================================================================
 */
public class StakeManagementUC2 {
    public static void main(String[] args) {
        StakeManagementService service = new StakeManagementService();
        String gamblerId = "GAMBLER-001";

        try {
            System.out.println("=== STAKE MANAGEMENT OPERATIONS DEMO ===\n");

            // 1. Initialize stake
            System.out.println("1. Initializing stake...");
            service.initializeStake(gamblerId, 500.0, 100.0, 2000.0);
            System.out.println();

            // 2. Track current stake
            System.out.println("2. Tracking current stake...");
            double currentStake = service.getCurrentStake(gamblerId);
            System.out.println("Current Stake: $" + currentStake);
            System.out.println();

            // 3. Process bet outcomes
            System.out.println("3. Processing bet outcomes...");
            service.processBetOutcome(gamblerId, "BET-001", 50.0, true);  // Win
            service.processBetOutcome(gamblerId, "BET-002", 30.0, false); // Loss
            service.processBetOutcome(gamblerId, "BET-003", 75.0, true);  // Win
            service.processBetOutcome(gamblerId, "BET-004", 40.0, false); // Loss
            service.processBetOutcome(gamblerId, "BET-005", 100.0, true); // Win
            System.out.println();

            // Additional deposits and withdrawals
            System.out.println("Processing deposit and withdrawal...");
            service.deposit(gamblerId, 200.0, "Additional deposit");
            service.withdraw(gamblerId, 150.0, "Partial withdrawal");
            System.out.println();

            // 4. Monitor fluctuations
            System.out.println("4. Monitoring stake fluctuations...");
            Map<String, Object> fluctuations = service.getStakeFluctuations(gamblerId);
            System.out.println("Fluctuation Analysis:");
            fluctuations.forEach((key, value) -> {
                if (value instanceof Double) {
                    System.out.println(String.format("  %s: %.2f", key, (Double) value));
                } else {
                    System.out.println(String.format("  %s: %s", key, value));
                }
            });
            System.out.println();

            // 5. Validate boundaries
            System.out.println("5. Validating stake boundaries...");
            service.validateStakeBoundaries(gamblerId);
            System.out.println();

            // 6. Generate reports
            System.out.println("6. Generating stake history report...");
            StakeHistoryReport report = service.generateStakeHistoryReport(gamblerId);
            System.out.println(report.generateSummary());

            System.out.println("\nRecent Transactions:");
            List<StakeTransaction> recentTxns = service.getTransactionHistory(
                    gamblerId,
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now());

            recentTxns.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
