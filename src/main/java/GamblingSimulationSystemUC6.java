import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * UC6: Input Validation and Error Handling Demonstration
 * ============================================================

 * Demonstrates:
 * - Stake validation
 * - Bet validation
 * - Limit validation
 * - Numeric parsing safety
 * - Probability validation
 * - Batch validation
 * - Session parameter validation

 * Shows real-world validation scenarios with error handling.
 */
public class GamblingSimulationSystemUC6 {
    public static void main(String[] args) {
        System.out.println("=== INPUT VALIDATION AND ERROR HANDLING DEMO ===\n");

        // Configure validation rules
        ValidationConfig config = new ValidationConfig();
        config.setMinStake(100.0);
        config.setMaxStake(10000.0);
        config.setMinBet(10.0);
        config.setMaxBet(1000.0);
        config.setMinProbability(0.1);
        config.setMaxProbability(0.9);
        config.setStrictMode(true);

        InputValidator validator = new InputValidator(config);

        // Demo 1: Validate Initial Stake
        System.out.println("========== DEMO 1: Initial Stake Validation ==========\n");
        double[] testStakes = {-100.0, 0.0, 50.0, 150.0, 5000.0, 15000.0, Double.NaN, Double.POSITIVE_INFINITY};

        for (double stake : testStakes) {
            try {
                validator.validateInitialStake(stake);
                System.out.println(String.format("✓ Stake $%.2f: VALID", stake));
            } catch (StakeValidationException e) {
                System.out.println(String.format("✗ Stake $%.2f: %s", stake, e.getMessage()));
            }
        }

        // Demo 2: Bet Amount Validation
        System.out.println("\n\n========== DEMO 2: Bet Amount Validation ==========\n");
        double currentStake = 500.0;
        System.out.println(String.format("Current Stake: $%.2f\n", currentStake));

        double[] testBets = {-50.0, 0.0, 5.0, 50.0, 500.0, 600.0, 1500.0};

        for (double bet : testBets) {
            try {
                validator.validateBetAmount(bet, currentStake);
                System.out.println(String.format("✓ Bet $%.2f: VALID", bet));
            } catch (BetValidationException e) {
                System.out.println(String.format("✗ Bet $%.2f: %s", bet, e.getMessage()));
            }
        }

        // Demo 3: Limit Validation
        System.out.println("\n\n========== DEMO 3: Limit Validation ==========\n");
        Object[][] testLimits = {
                {200.0, 1000.0, 500.0, "Valid limits"},
                {500.0, 300.0, 400.0, "Upper < Lower"},
                {-100.0, 1000.0, 500.0, "Negative lower"},
                {200.0, 1000.0, 150.0, "Initial stake < lower"},
                {200.0, 1000.0, 1100.0, "Initial stake > upper"},
                {200.0, 200.0, 500.0, "Upper = Lower"}
        };

        for (Object[] test : testLimits) {
            double lower = (Double) test[0];
            double upper = (Double) test[1];
            double initial = (Double) test[2];
            String description = (String) test[3];

            try {
                validator.validateLimits(lower, upper, initial);
                System.out.println(String.format("✓ %s [L:$%.2f U:$%.2f I:$%.2f]: VALID",
                        description, lower, upper, initial));
            } catch (LimitValidationException e) {
                System.out.println(String.format("✗ %s: %s", description, e.getMessage()));
            }
        }

        // Demo 4: Numeric Input Parsing
        System.out.println("\n\n========== DEMO 4: Numeric Input Parsing ==========\n");
        String[] testInputs = {"100.50", "-50", "abc", "123.456.789", "", null, "  250.75  ",
                "Infinity", "NaN"};

        for (String input : testInputs) {
            try {
                double value = validator.parseAndValidateNumeric(input, "testField");
                System.out.println(String.format("✓ Input '%s': Parsed as %.2f", input, value));
            } catch (ValidationException e) {
                System.out.println(String.format("✗ Input '%s': %s", input, e.getMessage()));
            }
        }

        // Demo 5: Prevent Negative Stakes
        System.out.println("\n\n========== DEMO 5: Negative Stake Prevention ==========\n");
        double[] stakeTests = {1000.0, 0.0, -50.0, -0.01, Double.NaN};

        for (double stake : stakeTests) {
            try {
                validator.validateStakeNonNegative(stake);
                System.out.println(String.format("✓ Stake $%.2f: Non-negative", stake));
            } catch (StakeValidationException e) {
                System.out.println(String.format("✗ Stake $%.2f: %s", stake, e.getMessage()));
            }
        }

        // Demo 6: Probability Validation
        System.out.println("\n\n========== DEMO 6: Probability Validation ==========\n");
        double[] testProbabilities = {-0.5, 0.0, 0.05, 0.5, 0.85, 0.95, 1.0, 1.5, Double.NaN};

        for (double prob : testProbabilities) {
            try {
                validator.validateProbability(prob);
                System.out.println(String.format("✓ Probability %.2f: VALID", prob));
            } catch (ProbabilityValidationException e) {
                System.out.println(String.format("✗ Probability %.2f: %s", prob, e.getMessage()));
            }
        }

        // Demo 7: Comprehensive Session Validation
        System.out.println("\n\n========== DEMO 7: Session Parameter Validation ==========\n");

        ValidationResult result1 = validator.validateSessionParameters(
                500.0, 200.0, 1000.0, 10.0, 200.0, 0.5);
        System.out.println("Test 1 - Valid parameters:");
        System.out.println(result1);

        System.out.println("\nTest 2 - Invalid parameters:");
        ValidationResult result2 = validator.validateSessionParameters(
                500.0, 600.0, 400.0, 10.0, 200.0, 1.5);
        System.out.println(result2);

        System.out.println("\nTest 3 - Parameters with warnings:");
        ValidationResult result3 = validator.validateSessionParameters(
                500.0, 50.0, 10000.0, 10.0, 300.0, 0.5);
        System.out.println(result3);

        // Demo 8: Batch Validation
        System.out.println("\n\n========== DEMO 8: Batch Validation ==========\n");
        Map<String, Object> batchInputs = new HashMap<>();
        batchInputs.put("initialStake", 500.0);
        batchInputs.put("betAmount", 50.0);
        batchInputs.put("winProbability", 0.5);
        batchInputs.put("invalidStake", -100.0);
        batchInputs.put("invalidProbability", 1.5);
        batchInputs.put("nullValue", null);

        Map<String, ValidationResult> batchResults = validator.validateBatch(batchInputs);

        batchResults.forEach((field, result) -> {
            System.out.println(String.format("\nField '%s': %s",
                    field, result.isValid() ? "✓ VALID" : "✗ INVALID"));
            if (!result.isValid()) {
                result.getErrors().forEach(error ->
                        System.out.println("  - " + error));
            }
        });

        System.out.println("\n\n=== VALIDATION DEMO COMPLETED ===");
    }
}
