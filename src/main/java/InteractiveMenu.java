import java.util.Scanner;

/**
 * Handles all user input and menu navigation logic.
 * Features:
 * - Main menu navigation
 * - Game menu navigation
 * - Safe numeric input parsing
 * - Confirmation prompts
 * - Error and success messaging
 */
public class InteractiveMenu {
    private Scanner scanner;
    private GameStatusDisplay display;
    public InteractiveMenu() {
        this.scanner = new Scanner(System.in);
        this.display = new GameStatusDisplay();
    }

    /**
     * Displays main system menu and returns selected option.
     * @return selected menu option number
     */
    public int displayMainMenu() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         GAMBLING SIMULATOR             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1. Start New Session");
        System.out.println("  2. View Current Status");
        System.out.println("  3. Place Bet");
        System.out.println("  4. Auto-Play Multiple Games");
        System.out.println("  5. View Session Statistics");
        System.out.println("  6. Pause Session");
        System.out.println("  7. End Session");
        System.out.println("  8. Settings");
        System.out.println("  0. Exit");
        System.out.println();
        return promptForInt("Select option", 0, 8);
    }

    /**
     * Displays game operation menu.
     * @return selected game menu option
     */
    public int displayGameMenu() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           GAME MENU                    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1. Place Single Bet");
        System.out.println("  2. Quick Bet (Use Previous Amount)");
        System.out.println("  3. Auto-Play 5 Games");
        System.out.println("  4. Auto-Play 10 Games");
        System.out.println("  5. Custom Auto-Play");
        System.out.println("  0. Back to Main Menu");
        System.out.println();
        return promptForInt("Select option", 0, 5);
    }

    /**
     * Prompts user to enter bet amount with validation.
     * Ensures:
     * - Numeric input
     * - Within min/max bet limits
     * - Does not exceed current stake

     * @param currentStake current available stake
     * @param minBet minimum allowed bet
     * @param maxBet maximum allowed bet
     * @return validated bet amount
     */
    public double promptForBetAmount(double currentStake, double minBet, double maxBet) {
        System.out.println();
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│          PLACE YOUR BET                │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println();
        System.out.println(String.format("  Current Stake:  $%,.2f", currentStake));
        System.out.println(String.format("  Min Bet:        $%,.2f", minBet));
        System.out.println(String.format("  Max Bet:        $%,.2f", Math.min(maxBet, currentStake)));
        System.out.println();
        while(true) {
            System.out.print("  Enter bet amount: $");
            String input = scanner.nextLine().trim();
            try {
                double bet = Double.parseDouble(input);
                if(bet < minBet) {
                    System.out.println(String.format("  ⚠ Bet must be at least $%.2f", minBet));
                    continue;
                }
                if(bet > maxBet) {
                    System.out.println(String.format("  ⚠ Bet cannot exceed $%.2f", maxBet));
                    continue;
                }
                if(bet > currentStake) {
                    System.out.println(String.format("  ⚠ Insufficient stake (have $%.2f)", currentStake));
                    continue;
                }
                return bet;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Prompts user for double value within range.
     * @param prompt message to display
     * @param min minimum allowed value
     * @param max maximum allowed value
     * @return validated double input
     */
    public double promptForDouble(String prompt, double min, double max) {
        while(true) {
            System.out.print(String.format("  %s (%.2f - %.2f): ", prompt, min, max));
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if(value < min || value > max) {
                    System.out.println(String.format("  ⚠ Value must be between %.2f and %.2f", min, max));
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Invalid input. Please enter a number.");
            }
        }
    }

    /**
     *
     * Prompts user for integer value within range.
     * @param prompt prompt text
     * @param min minimum value
     * @param max maximum value
     * @return validated integer input
     */
    public int promptForInt(String prompt, int min, int max) {
        while(true) {
            System.out.print(String.format("  %s (%d-%d): ", prompt, min, max));
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if(value < min || value > max) {
                    System.out.println(String.format("  ⚠ Value must be between %d and %d", min, max));
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Prompts user for yes/no confirmation.
     * @param message confirmation message
     * @return true if user confirms
     */
    public boolean confirmAction(String message) {
        System.out.print(String.format("  %s (y/n): ", message));
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
    public void displayMessage(String message) {
        System.out.println();
        System.out.println("  " + message);
        System.out.println();
    }
    public void displayError(String error) {
        System.out.println();
        System.out.println("  ❌ ERROR: " + error);
        System.out.println();
    }
    public void displaySuccess(String message) {
        System.out.println();
        System.out.println("  ✓ " + message);
        System.out.println();
    }
    public void pressEnterToContinue() {
        System.out.print("\n  Press ENTER to continue...");
        scanner.nextLine();
    }
    public void clearScreen() {
        // Simple clear - prints blank lines
        for(int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    public GameStatusDisplay getDisplay() {
        return display;
    }
    public void close() {
        scanner.close();
    }
}
