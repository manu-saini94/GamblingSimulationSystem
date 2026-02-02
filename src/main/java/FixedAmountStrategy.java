import java.util.List;


/**
 * FixedAmountStrategy is created for betting strategy that always returns fixed bet amount.
 * Useful for:
 * - Low risk betting
 * - Consistent wagering behavior
 */
public class FixedAmountStrategy extends BetStrategy {
    private double fixedAmount;

    /**
     * Creates fixed amount betting strategy.
     * @param fixedAmount Constant bet amount.
     */
    public FixedAmountStrategy(double fixedAmount) {
        super("Fixed Amount");
        this.fixedAmount = fixedAmount;
    }

    /**
     *
     * Returns fixed bet amount.
     *
     * @param currentStake Current stake.
     * @param minBet Minimum bet.
     * @param maxBet Maximum bet.
     * @param previousBets Previous bets list.
     *
     * @return Fixed bet amount within limits.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        return clampBetAmount(fixedAmount, minBet, maxBet);
    }

    public double getFixedAmount() { return fixedAmount; }
    public void setFixedAmount(double fixedAmount) { this.fixedAmount = fixedAmount; }
}