import java.util.List;

/**
 * D'Alembert betting strategy.
 * Behavior:
 * - Increase bet by fixed increment after loss.
 * - Decrease bet by fixed increment after win.
 * Goal:
 * - Smooth and gradual stake recovery.
 * Risk Level:
 * - Low to medium risk.
 */
public class DAlembertStrategy extends BetStrategy {
    private double baseBet;
    private double increment;
    private double currentBet;

    public DAlembertStrategy() {

    }

    /**
     *
     * Creates D'Alembert betting strategy.
     *
     * @param baseBet Starting bet amount.
     * @param increment Amount to increase/decrease bet after outcomes.
     */
    public DAlembertStrategy(double baseBet, double increment) {
        super("D'Alembert");
        this.baseBet = baseBet;
        this.increment = increment;
        this.currentBet = baseBet;
    }

    /**
     *
     * Calculates bet amount using D'Alembert logic.
     * Rules:
     * - LOSS → Increase bet by increment.
     * - WIN → Decrease bet by increment (not below base bet).
     *
     * @param currentStake Current gambler stake.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets Bet history.
     *
     * @return Calculated D'Alembert bet amount.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        if (!previousBets.isEmpty()) {
            Bet lastBet = previousBets.get(previousBets.size() - 1);

            if (lastBet.getOutcome() == BetOutcome.LOSS) {
                currentBet += increment;
            } else if (lastBet.getOutcome() == BetOutcome.WIN) {
                currentBet = Math.max(baseBet, currentBet - increment);
            }
        }

        return clampBetAmount(currentBet, minBet, maxBet);
    }
}
