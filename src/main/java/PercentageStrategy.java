import java.util.List;

public class PercentageStrategy extends BettingStrategy {
    private double percentage;

    public PercentageStrategy(double percentage) {
        super("Percentage");
        this.percentage = percentage;
    }

    @Override
    public double calculateBetAmount(double currentStake, double minBet,
                                     double maxBet, List<BetRecord> history) {
        double amount = currentStake * percentage;
        return Math.max(minBet, Math.min(amount, Math.min(maxBet, currentStake)));
    }
}