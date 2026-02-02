import java.util.List;


/**
 * Reverse Martingale betting strategy (Paroli system).
 * Behavior:
 * - Doubles bet after each win.
 * - Resets to base bet after loss.
 * Goal:
 * - Capitalize on winning streaks.
 * Risk Level:
 * - Medium risk.
 */
public class ReverseMartingaleStrategy extends BetStrategy {
    private double baseBet;

    /**
     * Creates Reverse Martingale strategy.
     *
     * @param baseBet Base starting bet amount.
     */
    public ReverseMartingaleStrategy(double baseBet) {
        super("Reverse Martingale");
        this.baseBet = baseBet;
    }

    /**
     *
     * Calculates bet amount using Reverse Martingale logic.
     * Rules:
     * - If last bet was WIN → double last bet.
     * - If last bet was LOSS → reset to base bet.
     *
     * @param currentStake Current gambler stake.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets Bet history list.
     *
     * @return Calculated bet amount using Reverse Martingale logic.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        if (previousBets.isEmpty()) {
            return clampBetAmount(baseBet, minBet, maxBet);
        }

        Bet lastBet = previousBets.get(previousBets.size() - 1);

        if (lastBet.getOutcome() == BetOutcome.WIN) {
            double newAmount = lastBet.getAmount() * 2;
            return clampBetAmount(newAmount, minBet, maxBet);
        } else {
            return clampBetAmount(baseBet, minBet, maxBet);
        }
    }
}
