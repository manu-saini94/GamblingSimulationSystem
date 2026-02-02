import java.util.Random;

/**
 * Implements pure random outcome determination.
 * Uses uniform random number generator.
 * Useful for:
 * - Fair probability simulations
 * - Testing deterministic results using seeded constructor
 */
public class RandomOutcomeStrategy implements OutcomeStrategy {
    private Random random;

    /**
     * Creates random strategy with non-deterministic seed.
     */
    public RandomOutcomeStrategy() {
        this.random = new Random();
    }

    /**
     * Creates random strategy using fixed seed.
     * @param seed Random seed for deterministic outcome testing.
     */
    public RandomOutcomeStrategy(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Determines outcome using random probability comparison.
     * @param winProbability Probability threshold for win.
     * @return True if win condition met.
     */
    @Override
    public boolean determineOutcome(double winProbability) {
        return random.nextDouble() < winProbability;
    }

    /**
     * Returns strategy name.
     * @return Strategy name.
     */
    @Override
    public String getStrategyName() {
        return "Random";
    }
}
