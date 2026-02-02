/**
 * Strategy interface for determining game outcomes.
 * Allows plugging different outcome determination models such as:
 * - Pure Random probability
 * - Weighted probability with house edge
 * - Future ML / pattern-based systems
 */
public interface OutcomeStrategy {

    /**
     * Determines if a game results in win or loss.
     * @param winProbability Probability of winning (0.0 to 1.0).
     * @return True if win, False if loss.
     */
    boolean determineOutcome(double winProbability);

    /**
     * Returns human readable strategy name.
     * @return Strategy name string.
     */
    String getStrategyName();
}
