import java.util.Random;

/**
 * Implements outcome determination using house edge.
 * Adjusts win probability by reducing it based on house edge.
 * Used for realistic casino simulation models.
 */
public class WeightedProbabilityStrategy implements OutcomeStrategy {
    private Random random;
    private double houseEdge;

    /**
     * Creates weighted probability strategy.
     * @param houseEdge House advantage percentage (0.0 - 1.0).
     */
    public WeightedProbabilityStrategy(double houseEdge) {
        this.random = new Random();
        this.houseEdge = houseEdge;
    }

    /**
     * Determines outcome using adjusted probability.
     * @param winProbability Base probability before house adjustment.
     * @return True if adjusted probability results in win.
     */
    @Override
    public boolean determineOutcome(double winProbability) {
        double adjustedProbability = winProbability * (1 - houseEdge);
        return random.nextDouble() < adjustedProbability;
    }

    /**
     * Returns strategy name including house edge.
     * @return Strategy name string.
     */
    @Override
    public String getStrategyName() {
        return "Weighted (House Edge: " + (houseEdge * 100) + "%)";
    }
}
