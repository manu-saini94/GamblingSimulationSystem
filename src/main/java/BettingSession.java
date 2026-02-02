import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a betting session for a gambler.
 * Tracks:
 * - Session duration
 * - Strategy used
 * - Bet history
 * - Stake changes
 * - Session performance summary
 */
public class BettingSession {
    private String sessionId;
    private String gamblerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double initialStake;
    private double currentStake;
    private List<Bet> bets;
    private BetStrategy strategy;
    private boolean active;

    /**
     *
     * Creates betting session instance.
     *
     * @param gamblerId Unique gambler identifier.
     * @param initialStake Starting stake.
     * @param strategy Strategy used for session.
     */
    public BettingSession(String gamblerId, double initialStake, BetStrategy strategy) {
        this.sessionId = "SESSION-" + UUID.randomUUID().toString().substring(0, 8);
        this.gamblerId = gamblerId;
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.bets = new ArrayList<>();
        this.strategy = strategy;
        this.startTime = LocalDateTime.now();
        this.active = true;
    }

    /**
     * Adds bet to session history.
     *
     * @param bet Bet instance to record.
     */
    public void addBet(Bet bet) {
        bets.add(bet);
    }

    /**
     * Updates current session stake.
     *
     * @param newStake Updated stake value.
     *
     */
    public void updateStake(double newStake) {
        this.currentStake = newStake;
    }

    /**
     * Ends session and records end timestamp.
     *
     */
    public void endSession() {
        this.endTime = LocalDateTime.now();
        this.active = false;
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public String getGamblerId() { return gamblerId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getInitialStake() { return initialStake; }
    public double getCurrentStake() { return currentStake; }
    public List<Bet> getBets() { return new ArrayList<>(bets); }
    public BetStrategy getStrategy() { return strategy; }
    public boolean isActive() { return active; }

    /**
     * Generates session summary report.
     *
     * @return Formatted session summary string.
     */
    public String getSummary() {
        int wins = (int) bets.stream().filter(b -> b.getOutcome() == BetOutcome.WIN).count();
        int losses = (int) bets.stream().filter(b -> b.getOutcome() == BetOutcome.LOSS).count();
        double totalWagered = bets.stream().mapToDouble(Bet::getAmount).sum();
        double netProfit = currentStake - initialStake;

        return String.format(
                "Session %s:\n" +
                        "  Duration: %s to %s\n" +
                        "  Strategy: %s\n" +
                        "  Total Bets: %d (W:%d L:%d)\n" +
                        "  Total Wagered: $%.2f\n" +
                        "  Initial Stake: $%.2f\n" +
                        "  Final Stake: $%.2f\n" +
                        "  Net Profit/Loss: $%.2f (%.2f%%)",
                sessionId,
                startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                endTime != null ? endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "Active",
                strategy.getName(), bets.size(), wins, losses, totalWagered,
                initialStake, currentStake, netProfit, (netProfit / initialStake) * 100
        );
    }
}
