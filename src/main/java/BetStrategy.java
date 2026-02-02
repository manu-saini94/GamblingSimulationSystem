import java.util.List;


/**
 * Base abstract class defining betting strategy behavior.

 * Responsibilities:
 * - Defines contract for bet amount calculation.
 * - Provides common helper utilities.

 * Enables Strategy Pattern implementation for dynamic bet calculation.
 */
public abstract class BetStrategy {
    protected String name;

    public BetStrategy() {
    }

    /**
     *
     * Initializes strategy with display name.
     *
     * @param name Strategy name.
     */
    public BetStrategy(String name) {
        this.name = name;
    }

    /**
     *
     * Calculates bet amount based on strategy rules.
     *
     * @param currentStake Current available gambler stake.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     * @param previousBets List of previous session bets.
     *
     * @return Calculated bet amount.
     */
    public abstract double calculateBetAmount(double currentStake, double minBet, double maxBet,
                                              List<Bet> previousBets);

    public String getName() { return name; }

    /**
     *
     * Ensures bet amount stays within allowed boundaries.
     *
     * @param amount Proposed bet amount.
     * @param minBet Minimum allowed bet.
     * @param maxBet Maximum allowed bet.
     *
     * @return Adjusted bet amount within limits.
     */
    protected double clampBetAmount(double amount, double minBet, double maxBet) {
        return Math.max(minBet, Math.min(amount, maxBet));
    }
}