import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BettingEngine {
    private BettingStrategy strategy;
    private List<BetRecord> betHistory;
    private Random random;

    public BettingEngine(BettingStrategy strategy) {
        this.strategy = strategy;
        this.betHistory = new ArrayList<>();
        this.random = new Random();
    }

    public BetRecord placeBet(double currentStake, double minBet, double maxBet,
                              double winProbability) throws Exception {
        double betAmount = strategy.calculateBetAmount(currentStake, minBet, maxBet, betHistory);

        if (betAmount > currentStake) {
            throw new Exception("Insufficient stake");
        }

        BetRecord bet = new BetRecord(betAmount);
        boolean won = determineOutcome(winProbability);
        double winAmount = won ? betAmount * ((1.0 / winProbability) - 1.0) : 0;

        bet.settle(won, winAmount);
        betHistory.add(bet);

        return bet;
    }

    private boolean determineOutcome(double winProbability) {
        return random.nextDouble() < winProbability;
    }

    public List<BetRecord> getBetHistory() { return new ArrayList<>(betHistory); }
}