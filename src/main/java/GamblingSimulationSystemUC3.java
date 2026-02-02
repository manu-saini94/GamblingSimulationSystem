import java.util.List;

/**
 * ================================================================
 * UC3 DEMO: Betting Mechanism Execution Driver
 * ================================================================
 * This class demonstrates the complete end-to-end execution of the
 * UC3 Betting Mechanism module.
 * Purpose:
 * - Acts as a simulation entry point for betting operations.
 * - Demonstrates multiple betting strategies in controlled sessions.
 * - Validates probability-based outcome simulation.
 * - Demonstrates automatic stake updates after bet settlement.
 * UC3 Functional Coverage Demonstrated:
 * 1. Single Bet Placement
 *    - Allows manual bet placement with amount and probability.
 *    - Validates bet amount against current stake and system limits.
 * 2. Probability-Based Outcome Simulation
 *    - Uses random probability simulation to determine win/loss.
 *    - Mimics real-world betting uncertainty.
 * 3. Automatic Stake Adjustment
 *    - Updates gambler stake after each bet settlement.
 *    - Applies winnings or deducts losses automatically.
 * 4. Strategy-Based Betting
 *    Demonstrates multiple strategy implementations:
 *    - Fixed Amount Strategy
 *    - Percentage-Based Strategy
 *    - Martingale Strategy
 *    - Fibonacci Strategy
 *    - Manual Bet Execution
 * 5. Session-Based Betting
 *    - Groups multiple bets into a single session.
 *    - Tracks session start and end timestamps.
 *    - Tracks session performance statistics.
 * 6. Consecutive Betting Execution
 *    - Automates multiple bet placements in sequence.
 *    - Stops execution if stake drops below minimum bet requirement.
 * Demo Scenarios Executed:
 * Demo 1:
 * - Fixed amount betting session.
 * Demo 2:
 * - Percentage-based betting session.
 * Demo 3:
 * - Martingale recovery strategy session.
 * Demo 4:
 * - Fibonacci progression strategy session.
 * Demo 5:
 * - Manual bet placement and settlement.
 * Key Features Demonstrated:
 * - Strategy Pattern implementation for betting logic.
 * - Real-time stake updates.
 * - Probability-driven outcome engine.
 * - Session-level analytics and reporting.
 * - Bet lifecycle tracking (Pending → Settled).
 * - Exception handling for invalid operations.
 * System Design Concepts Illustrated:
 * - Domain-driven design (Bet, Session, Strategy separation).
 * - Strategy Pattern for flexible bet calculation.
 * - Session Aggregation Model.
 * - Transaction-style settlement processing.
 * Intended Usage:
 * - Functional demonstration of UC3 capabilities.
 * - Integration and system testing reference.
 * - Developer onboarding and understanding betting workflow.
 * - LLD and architecture demonstration for review or interviews.
 * ================================================================
 */
public class GamblingSimulationSystemUC3 {

    public static void main(String[] args) {
        BettingService service = new BettingService(10.0, 500.0);
        String gamblerId = "GAMBLER-001";

        try {
            System.out.println("=== BETTING MECHANISM DEMO ===\n");

            // Initialize gambler
            service.initializeGambler(gamblerId, 1000.0);
            System.out.println("Gambler initialized with $1000.00\n");

            // Demo 1: Fixed Amount Strategy
            System.out.println("\n========== DEMO 1: Fixed Amount Strategy ==========");
            BetStrategy fixedStrategy = new FixedAmountStrategy(50.0);
            service.startSession(gamblerId, fixedStrategy);
            service.placeConsecutiveBets(gamblerId, 5, 0.5);
            service.endSession(gamblerId);

            // Reset stake
            service.initializeGambler(gamblerId, 1000.0);

            // Demo 2: Percentage Strategy
            System.out.println("\n\n========== DEMO 2: Percentage Strategy (5%) ==========");
            BetStrategy percentageStrategy = new PercentageStrategy(0.05);
            service.startSession(gamblerId, percentageStrategy);
            service.placeConsecutiveBets(gamblerId, 5, 0.48);
            service.endSession(gamblerId);

            // Reset stake
            service.initializeGambler(gamblerId, 1000.0);

            // Demo 3: Martingale Strategy
            System.out.println("\n\n========== DEMO 3: Martingale Strategy ==========");
            BetStrategy martingaleStrategy = new MartingaleStrategy(25.0);
            service.startSession(gamblerId, martingaleStrategy);
            service.placeConsecutiveBets(gamblerId, 8, 0.45);
            service.endSession(gamblerId);

            // Reset stake
            service.initializeGambler(gamblerId, 1000.0);

            // Demo 4: Fibonacci Strategy
            System.out.println("\n\n========== DEMO 4: Fibonacci Strategy ==========");
            BetStrategy fibStrategy = new FibonacciStrategy(10.0);
            service.startSession(gamblerId, fibStrategy);
            service.placeConsecutiveBets(gamblerId, 10, 0.5);
            service.endSession(gamblerId);

            // Demo 5: Manual betting
            System.out.println("\n\n========== DEMO 5: Manual Single Bets ==========");
            service.initializeGambler(gamblerId, 500.0);
            BetStrategy manualStrategy = new FixedAmountStrategy(50.0);
            service.startSession(gamblerId, manualStrategy);

            Bet bet1 = service.placeBet(gamblerId, 75.0, 0.6);
            service.settleBet(bet1);

            Bet bet2 = service.placeBet(gamblerId, 100.0, 0.4);
            service.settleBet(bet2);

            Bet bet3 = service.placeBet(gamblerId, 50.0, 0.7);
            service.settleBet(bet3);

            service.endSession(gamblerId);

            // Summary
            System.out.println("\n\n========== SESSION SUMMARY ==========");
            List<BettingSession> sessions = service.getCompletedSessions();
            for (int i = 0; i < sessions.size(); i++) {
                System.out.println("\n" + sessions.get(i).getSummary());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
