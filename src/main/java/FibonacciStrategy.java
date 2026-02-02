import java.util.ArrayList;
import java.util.List;


/**
 * Fibonacci betting strategy based on Fibonacci sequence progression.
 * Behavior:
 * - Move forward in Fibonacci sequence after loss.
 * - Move back two steps after win.
 * Goal:
 * - Gradual recovery of losses with controlled bet growth.
 * Risk Level:
 * - Medium risk.
 */
public class FibonacciStrategy extends BetStrategy {
    private double baseBet;
    private List<Integer> fibSequence;
    private int currentPosition;

    /**
     * Creates Fibonacci betting strategy.
     *
     * @param baseBet Base multiplier for Fibonacci sequence.
     */
    public FibonacciStrategy(double baseBet) {
        super("Fibonacci");
        this.baseBet = baseBet;
        this.fibSequence = generateFibonacci(20);
        this.currentPosition = 0;
    }

    /**
     *
     * Generates Fibonacci sequence of specified length.
     *
     * @param n Number of Fibonacci values to generate.
     *
     * @return List containing Fibonacci sequence values.
     */
    private List<Integer> generateFibonacci(int n) {
        List<Integer> fib = new ArrayList<>();
        fib.add(1);
        fib.add(1);
        for (int i = 2; i < n; i++) {
            fib.add(fib.get(i - 1) + fib.get(i - 2));
        }
        return fib;
    }

    /**
     *
     * Calculates bet amount using Fibonacci progression logic.
     * Rules:
     * - LOSS → move forward in Fibonacci sequence.
     * - WIN → move back two steps in sequence.
     *
     * @param currentStake Current gambler stake.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets Bet history.
     *
     * @return Calculated Fibonacci strategy bet amount.
     */
    @Override
    public double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                     List<Bet> previousBets) {
        if (!previousBets.isEmpty()) {
            Bet lastBet = previousBets.get(previousBets.size() - 1);

            if (lastBet.getOutcome() == BetOutcome.LOSS) {
                currentPosition = Math.min(currentPosition + 1, fibSequence.size() - 1);
            } else if (lastBet.getOutcome() == BetOutcome.WIN) {
                currentPosition = Math.max(currentPosition - 2, 0);
            }
        }

        double amount = baseBet * fibSequence.get(currentPosition);
        return clampBetAmount(amount, minBet, maxBet);
    }
}
