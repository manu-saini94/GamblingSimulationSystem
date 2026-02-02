import java.util.Scanner;

/**
 * Handles safe console input with retry logic and validation.
 */
public class SafeInputHandler {
    private InputValidator validator;
    private Scanner scanner;

    /**
     * Creates safe input handler.
     * @param validator Input validator instance.
     */
    public SafeInputHandler(InputValidator validator) {
        this.validator = validator;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prompts user for stake input.
     * @param prompt Prompt message.
     * @param min Minimum allowed.
     * @param max Maximum allowed.
     * @return Validated stake value.
     */
    public double promptForStake(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt + String.format(" ($%.2f - $%.2f): ", min, max));
                String input = scanner.nextLine();
                double value = validator.parseAndValidateNumeric(input, "stake");
                validator.validateInitialStake(value);

                if (value < min || value > max) {
                    System.out.println(String.format("⚠ Value must be between $%.2f and $%.2f", min, max));
                    continue;
                }

                return value;
            } catch (ValidationException e) {
                System.out.println("❌ " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }

    /**
     * Prompts user for bet input.
     * @param prompt Prompt text.
     * @param currentStake Current stake.
     * @param min Minimum bet.
     * @param max Maximum bet.
     * @return Validated bet amount.
     */
    public double promptForBet(String prompt, double currentStake, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt + String.format(" ($%.2f - $%.2f, Current Stake: $%.2f): ",
                        min, Math.min(max, currentStake), currentStake));
                String input = scanner.nextLine();
                double value = validator.parseAndValidateNumeric(input, "bet");
                validator.validateBetAmount(value, currentStake);

                if (value < min || value > max) {
                    System.out.println(String.format("⚠ Value must be between $%.2f and $%.2f", min, max));
                    continue;
                }

                return value;
            } catch (ValidationException e) {
                System.out.println("❌ " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }

    /**
     * Prompts user for probability input.
     * @param prompt Prompt text.
     * @return Validated probability.
     */
    public double promptForProbability(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (0.0 - 1.0): ");
                String input = scanner.nextLine();
                double value = validator.parseAndValidateNumeric(input, "probability");
                validator.validateProbability(value);
                return value;
            } catch (ValidationException e) {
                System.out.println("❌ " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
