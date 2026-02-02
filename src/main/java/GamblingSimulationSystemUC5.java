import java.util.List;
import java.util.Map;

/**
 * ================================================================
 * UC5 DEMO: Win/Loss Calculation and Statistical Analysis System
 * ================================================================
 * Demonstrates:
 * - Multiple outcome determination strategies
 * - Multiple odds calculation models
 * - Running totals and balance tracking
 * - Advanced win/loss statistical analytics
 * - Streak tracking and performance metrics

 * Demo Scenarios:
 * - Fixed odds simulation
 * - Probability-based odds simulation
 * - House edge simulation
 * - Streak tracking
 * - Running balance analysis
 * ================================================================
 */
public class GamblingSimulationSystemUC5 {
    public static void main(String[] args) {
        try {
            System.out.println("=== WIN/LOSS CALCULATION SYSTEM DEMO ===\n");

            // Demo 1: Fixed Odds with Random Outcomes
            System.out.println("\n========== DEMO 1: Fixed Odds (2x) - Random Outcomes ==========");
            OutcomeStrategy randomStrategy = new RandomOutcomeStrategy(12345L);
            OddsConfiguration fixedOdds = new OddsConfiguration(2.0, OddsType.FIXED);
            WinLossCalculator calculator1 = new WinLossCalculator(
                    "SESSION-001", 1000.0, randomStrategy, fixedOdds);

            System.out.println("Playing 20 games with 50% win probability...\n");
            for (int i = 0; i < 20; i++) {
                GameResult result = calculator1.playGame(50.0, 0.5);
                if ((i + 1) % 5 == 0) {
                    System.out.println(result);
                    System.out.println(String.format("  Running Total - Wins: $%.2f | Losses: $%.2f | Net: $%.2f",
                            calculator1.getRunningTotals().getCumulativeWinnings(),
                            calculator1.getRunningTotals().getCumulativeLosses(),
                            calculator1.getRunningTotals().getNetProfitLoss()));
                }
            }

            WinLossStatistics stats1 = calculator1.getStatistics();
            System.out.println(stats1.generateReport());

            // Demo 2: Probability-Based Odds
            System.out.println("\n========== DEMO 2: Probability-Based Odds ==========");
            OutcomeStrategy randomStrategy2 = new RandomOutcomeStrategy();
            OddsConfiguration probOdds = new OddsConfiguration(1.0, OddsType.PROBABILITY_BASED);
            WinLossCalculator calculator2 = new WinLossCalculator(
                    "SESSION-002", 1000.0, randomStrategy2, probOdds);

            System.out.println("Playing 15 games with varying probabilities...\n");
            double[] probabilities = {0.7, 0.5, 0.3, 0.6, 0.4, 0.8, 0.5, 0.3, 0.7, 0.5,
                    0.4, 0.6, 0.5, 0.3, 0.7};

            for (int i = 0; i < probabilities.length; i++) {
                GameResult result = calculator2.playGame(40.0, probabilities[i]);
                System.out.println(String.format("Game %d (Prob: %.0f%%): %s",
                        i + 1, probabilities[i] * 100, result));
            }

            System.out.println(calculator2.getStatistics().generateReport());

            // Demo 3: Weighted Probability with House Edge
            System.out.println("\n========== DEMO 3: House Edge Simulation ==========");
            OutcomeStrategy houseEdgeStrategy = new WeightedProbabilityStrategy(0.05); // 5% house edge
            OddsConfiguration decimalOdds = new OddsConfiguration(2.5, OddsType.DECIMAL);
            WinLossCalculator calculator3 = new WinLossCalculator(
                    "SESSION-003", 1000.0, houseEdgeStrategy, decimalOdds);

            System.out.println("Playing 25 games with 5% house edge...\n");
            for (int i = 0; i < 25; i++) {
                calculator3.playGame(30.0, 0.5);
            }

            calculator3.printRecentResults(10);
            System.out.println(calculator3.getStatistics().generateReport());

            // Demo 4: Streak Tracking
            System.out.println("\n========== DEMO 4: Consecutive Win/Loss Tracking ==========");
            OutcomeStrategy streakStrategy = new RandomOutcomeStrategy(99999L);
            OddsConfiguration streakOdds = new OddsConfiguration(1.8, OddsType.FIXED);
            WinLossCalculator calculator4 = new WinLossCalculator(
                    "SESSION-004", 1000.0, streakStrategy, streakOdds);

            System.out.println("Playing 30 games and tracking streaks...\n");
            for (int i = 0; i < 30; i++) {
                GameResult result = calculator4.playGame(25.0, 0.48);

                if ((i + 1) % 5 == 0) {
                    Map<String, Integer> streaks = calculator4.getCurrentStreaks();
                    System.out.println(String.format("\nAfter %d games:", i + 1));
                    System.out.println(String.format("  Current Win Streak: %d",
                            streaks.get("currentWinStreak")));
                    System.out.println(String.format("  Current Loss Streak: %d",
                            streaks.get("currentLossStreak")));
                    System.out.println(String.format("  Win/Loss Ratio: %.2f",
                            calculator4.getWinLossRatio()));
                    System.out.println(String.format("  Win Rate: %.2f%%",
                            calculator4.getWinRate() * 100));
                }
            }

            System.out.println(calculator4.getStatistics().generateReport());

            // Demo 5: Running Totals Visualization
            System.out.println("\n========== DEMO 5: Running Totals Analysis ==========");
            OutcomeStrategy finalStrategy = new RandomOutcomeStrategy();
            OddsConfiguration finalOdds = new OddsConfiguration(2.2, OddsType.FIXED);
            WinLossCalculator calculator5 = new WinLossCalculator(
                    "SESSION-005", 500.0, finalStrategy, finalOdds);

            System.out.println("Playing 15 games with balance tracking...\n");
            for (int i = 0; i < 15; i++) {
                calculator5.playGame(35.0, 0.5);
            }

            RunningTotals totals = calculator5.getRunningTotals();
            List<Double> balanceHistory = totals.getBalanceHistory();

            System.out.println("\nBalance Progression:");
            for (int i = 0; i < balanceHistory.size(); i++) {
                if (i % 3 == 0) {
                    System.out.println(String.format("Game %d: $%.2f", i, balanceHistory.get(i)));
                }
            }

            System.out.println(String.format("\nFinal Summary:"));
            System.out.println(String.format("  Initial Stake: $500.00"));
            System.out.println(String.format("  Final Stake: $%.2f", totals.getRunningBalance()));
            System.out.println(String.format("  Cumulative Winnings: $%.2f", totals.getCumulativeWinnings()));
            System.out.println(String.format("  Cumulative Losses: $%.2f", totals.getCumulativeLosses()));
            System.out.println(String.format("  Net Profit/Loss: $%.2f", totals.getNetProfitLoss()));

            System.out.println(calculator5.getStatistics().generateReport());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
