import java.util.List;

/**
 * ================================================================
 * UC4 DEMO: Game Session Management Simulation Driver
 * ================================================================
 * This class serves as the execution entry point for demonstrating
 * the complete Game Session Management workflow defined in UC4.
 * Purpose:
 * - Simulates real-world gambling session lifecycle scenarios.
 * - Demonstrates automated session control using stake boundaries.
 * - Validates pause/resume session behavior.
 * - Demonstrates session statistics and reporting generation.
 * UC4 Functionalities Demonstrated:
 * 1. Start New Gambling Session
 *    - Initializes session using configurable SessionParameters.
 *    - Validates initial stake boundaries and configuration.
 *    - Starts session timer and activity tracking.
 * 2. Continue Session Gameplay
 *    - Executes multiple games sequentially.
 *    - Applies automatic outcome determination using probability.
 *    - Updates stake in real time after each game.
 *    - Stops automatically when limits or constraints are reached.
 * 3. Pause and Resume Sessions
 *    - Supports multiple pause/resume cycles.
 *    - Tracks pause duration and reasons.
 *    - Separates active gameplay time from paused duration.
 * 4. Automatic Session Ending
 *    Session ends automatically when:
 *    - Upper limit (Win condition) is reached.
 *    - Lower limit (Loss condition) is reached.
 *    - Session timeout duration is reached.
 * 5. Manual Session Termination
 *    - Allows explicit manual termination.
 *    - Preserves session analytics and history.
 * 6. Session Duration Tracking
 *    Tracks:
 *    - Total session time
 *    - Active gameplay duration
 *    - Total paused duration
 * 7. Session Analytics and Reporting
 *    Provides:
 *    - Game-by-game history
 *    - Win/Loss statistics
 *    - Financial summary (profit/loss, ROI)
 *    - Pause history
 * Demo Scenarios Covered:
 * Demo 1:
 * - Session ending due to upper limit (win condition).
 * Demo 2:
 * - Session ending due to lower limit (loss condition).
 * Demo 3:
 * - Multiple pause and resume cycles with duration tracking.
 * Demo 4:
 * - Manual session termination.
 * Key System Design Concepts Demonstrated:
 * - Session Lifecycle State Management
 * - Domain Driven Design (GamingSession as Aggregate Root)
 * - Boundary Driven Session Control
 * - Time Tracking (Active vs Paused)
 * - Session Orchestration using GameSessionManager
 * Intended Usage:
 * - Functional UC4 demonstration.
 * - Integration and regression testing reference.
 * - Developer onboarding and architecture understanding.
 * - Low Level Design interview demonstration.
 * ================================================================
 */
public class GamblingSimulationSystemUC4 {
    public static void main(String[] args) {
        GameSessionManager manager = new GameSessionManager();
        String gamblerId = "GAMBLER-001";

        try {
            System.out.println("=== GAME SESSION MANAGEMENT DEMO ===\n");

            // Demo 1: Session ending with upper limit (WIN)
            System.out.println("\n========== DEMO 1: Win Condition (Upper Limit) ==========");
            SessionParameters params1 = new SessionParameters(500.0, 700.0, 200.0);
            params1.setDefaultWinProbability(0.65); // Higher win probability
            GamingSession session1 = manager.startNewSession(gamblerId, params1);
            manager.continueSession(gamblerId, 20, 50.0);

            // Demo 2: Session ending with lower limit (LOSS)
            System.out.println("\n\n========== DEMO 2: Loss Condition (Lower Limit) ==========");
            gamblerId = "GAMBLER-002";
            SessionParameters params2 = new SessionParameters(500.0, 1000.0, 300.0);
            params2.setDefaultWinProbability(0.35); // Lower win probability
            GamingSession session2 = manager.startNewSession(gamblerId, params2);
            manager.continueSession(gamblerId, 20, 40.0);

            // Demo 3: Pause and Resume
            System.out.println("\n\n========== DEMO 3: Pause and Resume ==========");
            gamblerId = "GAMBLER-003";
            SessionParameters params3 = new SessionParameters(600.0, 1200.0, 200.0);
            GamingSession session3 = manager.startNewSession(gamblerId, params3);

            manager.continueSession(gamblerId, 5, 40.0);

            System.out.println("\n--- Pausing session ---");
            manager.pauseSession(gamblerId, "Taking a break");
            Thread.sleep(2000); // Simulate 2 second pause

            System.out.println("\n--- Resuming session ---");
            manager.resumeSession(gamblerId);
            manager.continueSession(gamblerId, 5, 40.0);

            System.out.println("\n--- Pausing again ---");
            manager.pauseSession(gamblerId, "Phone call");
            Thread.sleep(1500);

            System.out.println("\n--- Resuming again ---");
            manager.resumeSession(gamblerId);
            manager.continueSession(gamblerId, 3, 40.0);

            manager.endSession(gamblerId);

            // Demo 4: Manual end
            System.out.println("\n\n========== DEMO 4: Manual End ==========");
            gamblerId = "GAMBLER-004";
            SessionParameters params4 = new SessionParameters(500.0, 2000.0, 100.0);
            GamingSession session4 = manager.startNewSession(gamblerId, params4);
            manager.continueSession(gamblerId, 8, 50.0);

            System.out.println("\n--- Ending session manually ---");
            manager.endSession(gamblerId);

            // Display all completed sessions
            System.out.println("\n\n========== ALL COMPLETED SESSIONS ==========");
            List<GamingSession> completed = manager.getCompletedSessions();
            for (int i = 0; i < completed.size(); i++) {
                System.out.println(String.format("\n--- Session %d ---", i + 1));
                System.out.println(completed.get(i).getSessionSummary());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
