import java.util.List;


/**
 * Betting strategy that calculates bet as percentage of current stake.
 * Useful for:
 * - Risk proportional betting
 * - Capital preservation
 */
public class PercentageStrategy extends BetStrategy {
    private double percentage; // 0.0 to 1.0

    /**
     * Creates percentage based strategy.
     *
     * @param percentage Percentage value (0.0 – 1.0).
     */
    public PercentageStrategy(double percentage) {
        super("Percentage Based");
        this.percentage = percentage;
    }

    /**
     *
     * Calculates bet amount based on stake percentage.
     *
     * @param currentStake Current stake value.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets Previous bets history.
     *
     * @return Calculated percentage-based bet amount.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        double amount = currentStake * percentage;
        return clampBetAmount(amount, minBet, maxBet);
    }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
}
