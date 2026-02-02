import java.util.List;

public abstract class BettingStrategy {
    protected String name;

    public BettingStrategy(String name) { this.name = name; }

    public abstract double calculateBetAmount(double currentStake, double minBet,
                                              double maxBet, List<BetRecord> history);

    public String getName() { return name; }
}
