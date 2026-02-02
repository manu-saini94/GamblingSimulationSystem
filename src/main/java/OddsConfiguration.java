/**
 * Configures odds calculation model.
 * Supports:
 * - Fixed multiplier odds
 * - Probability-based odds
 * - American odds format
 * - Decimal odds format
 */
public class OddsConfiguration {
    private double multiplier;
    private OddsType type;

    /**
     * Creates odds configuration.
     * @param multiplier Multiplier value used in payout calculation.
     * @param type Odds type.
     */
    public OddsConfiguration(double multiplier, OddsType type) {
        this.multiplier = multiplier;
        this.type = type;
    }

    /**
     *
     * Calculates winnings based on odds type.
     * @param betAmount Bet amount.
     * @param winProbability Win probability.
     * @return Calculated winnings.
     */
    public double calculateWinnings(double betAmount, double winProbability) {
        switch (type) {
            case FIXED:
                return betAmount * multiplier;
            case PROBABILITY_BASED:
                // Lower probability = higher payout
                double odds = (1.0 / winProbability) - 1.0;
                return betAmount * odds * multiplier;
            case AMERICAN:
                // American odds format
                return calculateAmericanOdds(betAmount, winProbability);
            case DECIMAL:
                return betAmount * (multiplier - 1.0);
            default:
                return betAmount;
        }
    }

    /**
     * Calculates winnings using American odds system.
     * @param betAmount Bet amount.
     * @param probability Win probability.
     * @return Winnings based on American odds.
     */
    private double calculateAmericanOdds(double betAmount, double probability) {
        if (probability > 0.5) {
            // Favorite (negative odds)
            double odds = -100 / ((1.0 / probability) - 1.0);
            return betAmount * (100.0 / Math.abs(odds));
        } else {
            // Underdog (positive odds)
            double odds = 100 * ((1.0 / probability) - 1.0);
            return betAmount * (odds / 100.0);
        }
    }

    public double getMultiplier() { return multiplier; }
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }
    public OddsType getType() { return type; }
}