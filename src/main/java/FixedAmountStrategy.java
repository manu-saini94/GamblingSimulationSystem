import java.util.List;

public class FixedAmountStrategy extends BettingStrategy {
    private double fixedAmount;

    public FixedAmountStrategy(double amount) {
        super("Fixed Amount");
        this.fixedAmount = amount;
    }

    @Override
    public double calculateBetAmount(double currentStake, double minBet,
                                     double maxBet, List<BetRecord> history) {
        return Math.max(minBet, Math.min(fixedAmount, Math.min(maxBet, currentStake)));
    }
}

