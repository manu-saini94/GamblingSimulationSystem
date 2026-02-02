import java.util.List;


/**
 * Martingale strategy doubles bet after loss and resets after win.
 * Goal:
 * - Recover previous losses on next win.
 * Risk:
 * - High capital requirement.
 */
public class MartingaleStrategy extends BetStrategy {
    private double baseBet;

    /**
     *
     * Creates Martingale strategy.
     *
     * @param baseBet Base starting bet amount.
     */
    public MartingaleStrategy(double baseBet) {
        super("Martingale");
        this.baseBet = baseBet;
    }


    /**
     *
     * Calculates bet amount based on last bet outcome.
     *
     * @param currentStake Current stake.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets Bet history.
     *
     * @return Calculated Martingale bet amount.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        if (previousBets.isEmpty()) {
            return clampBetAmount(baseBet, minBet, maxBet);
        }

        Bet lastBet = previousBets.get(previousBets.size() - 1);

        if (lastBet.getOutcome() == BetOutcome.LOSS) {
            double newAmount = lastBet.getAmount() * 2;
            return clampBetAmount(newAmount, minBet, maxBet);
        } else {
            return clampBetAmount(baseBet, minBet, maxBet);
        }
    }

    public double getBaseBet() { return baseBet; }
}


