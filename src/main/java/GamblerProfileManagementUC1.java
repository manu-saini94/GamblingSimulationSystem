/**
 * ================================================================
 * UC1: Gambler Profile Management
 * ================================================================
 * This module manages the complete lifecycle of a gambler profile
 * including creation, updates, validation, statistics retrieval,
 * and session reset functionality.
 * Functional Use Cases:
 * 1. Create Gambler Profile
 *    - Creates a new gambler with initial stake.
 *    - Validates minimum stake requirement.
 *    - Validates win and loss threshold boundaries.
 * 2. Update Gambler Details
 *    - Updates personal information (name, email, phone).
 *    - Updates betting preferences and threshold settings.
 * 3. Retrieve Gambler Statistics
 *    - Provides financial status and performance metrics.
 *    - Includes win rate, total bets, net profit/loss.
 * 4. Validate Gambler Eligibility
 *    - Ensures gambler is active.
 *    - Checks minimum stake requirement.
 *    - Checks win/loss threshold status.
 * 5. Reset Gambler Profile
 *    - Resets session financial data.
 *    - Applies proportional threshold recalculation.
 * Key Features:
 * - Input validation for all operations.
 * - Timestamp tracking for audit purposes.
 * - Threshold proportional recalculation during reset.
 * - Centralized error handling.
 * - Utility methods for bet tracking and account status management.
 * ================================================================
 */
public class GamblerProfileManagementUC1 {

    public static void main(String[] args) {
        GamblerProfileService service = new GamblerProfileService();

        try {
            System.out.println("=== Gambler Profile Management System ===\n");

            // 1. Create new gambler
            System.out.println("1. Creating new gambler...");
            GamblerProfile gambler = service.createGambler(
                    "John Doe",
                    "john.doe@email.com",
                    "555-1234",
                    500.0,   // initial stake
                    1000.0,  // win threshold
                    200.0    // loss threshold
            );
            String gamblerId = gambler.getGamblerId();
            System.out.println("Gambler ID: " + gamblerId + "\n");

            // 2. Update personal information
            System.out.println("2. Updating gambler information...");
            service.updateGamblerInfo(gamblerId, "John Smith", "john.smith@email.com", "555-5678");

            // Update betting preferences
            BettingPreferences prefs = new BettingPreferences();
            prefs.setMaxBetAmount(500.0);
            prefs.setMinBetAmount(20.0);
            prefs.setPreferredGameType("POKER");
            prefs.setAutoPlay(false);
            prefs.setSessionTimeLimit(180);
            service.updateBettingPreferences(gamblerId, prefs);
            System.out.println();

            // Simulate some bets
            System.out.println("Simulating betting activity...");
            service.recordBet(gamblerId, 50.0, true);
            service.recordBet(gamblerId, 30.0, false);
            service.recordBet(gamblerId, 40.0, true);
            service.recordBet(gamblerId, 20.0, true);
            System.out.println();

            // 3. Retrieve statistics
            System.out.println("3. Retrieving gambler statistics...");
            GamblerStatistics stats = service.getGamblerStatistics(gamblerId);
            System.out.println(stats);
            System.out.println();

            // 4. Validate eligibility
            System.out.println("4. Validating gambler eligibility...");
            boolean isEligible = service.validateEligibility(gamblerId);
            System.out.println("Eligibility Status: " + (isEligible ? "ELIGIBLE" : "NOT ELIGIBLE"));
            System.out.println();

            // 5. Reset profile for new session
            System.out.println("5. Resetting gambler profile for new session...");
            service.resetGamblerProfile(gamblerId, 750.0);

            // Display updated statistics
            System.out.println("\nUpdated Statistics:");
            GamblerStatistics updatedStats = service.getGamblerStatistics(gamblerId);
            System.out.println(updatedStats);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
