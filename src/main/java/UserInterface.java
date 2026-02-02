import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void displayCurrentStatus(GamblerProfile profile, GameSession session) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 CURRENT GAME STATUS");
        System.out.println("=".repeat(60));
        System.out.println(String.format("  Gambler:           %s", profile.getName()));
        System.out.println(String.format("  Current Stake:     $%,.2f", session.getCurrentStake()));
        System.out.println(String.format("  Initial Stake:     $%,.2f", session.getInitialStake()));
        System.out.println(String.format("  Net Change:        %s$%,.2f",
                session.getCurrentStake() >= session.getInitialStake() ? "+" : "",
                session.getCurrentStake() - session.getInitialStake()));
        System.out.println("-".repeat(60));
        System.out.println(String.format("  Lower Limit:       $%,.2f", session.getLowerLimit()));
        System.out.println(String.format("  Upper Limit:       $%,.2f", session.getUpperLimit()));
        System.out.println(String.format("  Games Played:      %d", session.getGamesPlayed()));
        System.out.println(String.format("  Wins:              %d", session.getWins()));
        System.out.println(String.format("  Losses:            %d", session.getLosses()));
        System.out.println("=".repeat(60) + "\n");
    }

    public double promptForBetAmount(double currentStake, double minBet, double maxBet) {
        while (true) {
            System.out.println("\n--- PLACE YOUR BET ---");
            System.out.println(String.format("  Current Stake: $%,.2f", currentStake));
            System.out.println(String.format("  Min Bet: $%.2f | Max Bet: $%.2f", minBet, maxBet));
            System.out.print("  Enter bet amount: $");

            try {
                double bet = Double.parseDouble(scanner.nextLine().trim());
                if (bet >= minBet && bet <= Math.min(maxBet, currentStake)) {
                    return bet;
                }
                System.out.println("  ⚠ Invalid amount. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Invalid input. Please enter a number.");
            }
        }
    }

    public void displayGameOutcome(int gameNumber, boolean won, double betAmount,
                                   double winAmount, double stakeBefore, double stakeAfter) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println(String.format("           GAME #%d RESULT: %s",
                gameNumber, won ? "★ YOU WIN! ★" : "✗ YOU LOSE"));
        System.out.println("-".repeat(60));
        System.out.println(String.format("  Bet Amount:        $%,.2f", betAmount));
        if (won) {
            System.out.println(String.format("  Winnings:          +$%,.2f", winAmount));
        } else {
            System.out.println(String.format("  Loss:              -$%,.2f", betAmount));
        }
        System.out.println(String.format("  Previous Stake:    $%,.2f", stakeBefore));
        System.out.println(String.format("  New Stake:         $%,.2f", stakeAfter));
        System.out.println("-".repeat(60) + "\n");
    }

    public void displaySessionSummary(GameSession session, WinLossCalculator calculator) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    SESSION SUMMARY");
        System.out.println("=".repeat(70));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n  SESSION INFORMATION");
        System.out.println("-".repeat(70));
        System.out.println(String.format("  Session ID:         %s", session.getSessionId()));
        System.out.println(String.format("  Status:             %s", session.getStatus()));
        System.out.println(String.format("  Started:            %s",
                session.getStartTime().format(formatter)));
        if (session.getEndTime() != null) {
            System.out.println(String.format("  Ended:              %s",
                    session.getEndTime().format(formatter)));
        }
        System.out.println(String.format("  Duration:           %d seconds",
                session.getDurationSeconds()));

        System.out.println("\n  FINANCIAL SUMMARY");
        System.out.println("-".repeat(70));
        System.out.println(String.format("  Initial Stake:      $%,.2f", session.getInitialStake()));
        System.out.println(String.format("  Final Stake:        $%,.2f", session.getCurrentStake()));
        double netProfit = session.getCurrentStake() - session.getInitialStake();
        System.out.println(String.format("  Net Profit/Loss:    %s$%,.2f",
                netProfit >= 0 ? "+" : "", netProfit));
        System.out.println(String.format("  Return:             %.2f%%",
                (netProfit / session.getInitialStake()) * 100));
        System.out.println(String.format("  Total Winnings:     $%,.2f", calculator.getTotalWinnings()));
        System.out.println(String.format("  Total Losses:       $%,.2f", calculator.getTotalLosses()));

        System.out.println("\n  GAME STATISTICS");
        System.out.println("-".repeat(70));
        System.out.println(String.format("  Games Played:       %d", session.getGamesPlayed()));
        System.out.println(String.format("  Games Won:          %d", session.getWins()));
        System.out.println(String.format("  Games Lost:         %d", session.getLosses()));
        System.out.println(String.format("  Win Rate:           %.2f%%",
                calculator.getWinRate() * 100));
        System.out.println(String.format("  Win/Loss Ratio:     %.2f",
                calculator.getWinLossRatio()));
        System.out.println(String.format("  Longest Win Streak: %d",
                calculator.getLongestWinStreak()));
        System.out.println(String.format("  Longest Loss Streak:%d",
                calculator.getLongestLossStreak()));

        System.out.println("\n" + "=".repeat(70) + "\n");
    }

    public int displayMainMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║       GAMBLING SIMULATOR MENU         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("  1. Start New Session");
        System.out.println("  2. View Current Status");
        System.out.println("  3. Place Bet");
        System.out.println("  4. Auto-Play Games");
        System.out.println("  5. Pause Session");
        System.out.println("  6. End Session");
        System.out.println("  0. Exit");
        System.out.print("\n  Select option: ");

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void displayMessage(String message) {
        System.out.println("\n  " + message + "\n");
    }

    public void displayError(String error) {
        System.out.println("\n  ❌ ERROR: " + error + "\n");
    }

    public void pressEnterToContinue() {
        System.out.print("\n  Press ENTER to continue...");
        scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
