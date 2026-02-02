
/**
 * GamblerStatistics is a DTO class used to expose gambler performance statistics.
 * Provides:
 * - Current financial state and net profit/loss.
 * - Betting performance metrics (wins, losses, win rate).
 * - Aggregated financial metrics (total winnings, total losses).
 * - Average bet calculation.
 * - Threshold status indicators.
 * Used for reporting, analytics, and dashboard visualization.
 */
public class GamblerStatistics {
    private String gamblerId;
    private String name;
    private double currentStake;
    private double initialStake;
    private double netProfit;
    private int totalBets;
    private int wins;
    private int losses;
    private double winRate;
    private double totalWinnings;
    private double totalLosses;
    private double averageBetAmount;
    private boolean hasReachedWinThreshold;
    private boolean hasReachedLossThreshold;

    public GamblerStatistics(GamblerProfile profile) {
        this.gamblerId = profile.getGamblerId();
        this.name = profile.getName();
        this.currentStake = profile.getCurrentStake();
        this.initialStake = profile.getInitialStake();
        this.netProfit = profile.getCurrentStake() - profile.getInitialStake();
        this.totalBets = profile.getTotalBets();
        this.wins = profile.getWins();
        this.losses = profile.getLosses();
        this.winRate = totalBets > 0 ? (double) wins / totalBets * 100 : 0.0;
        this.totalWinnings = profile.getTotalWinnings();
        this.totalLosses = profile.getTotalLosses();
        this.averageBetAmount = totalBets > 0 ? (totalWinnings + totalLosses) / totalBets : 0.0;
        this.hasReachedWinThreshold = profile.getCurrentStake() >= profile.getWinThreshold();
        this.hasReachedLossThreshold = profile.getCurrentStake() <= profile.getLossThreshold();
    }

    @Override
    public String toString() {
        return String.format(
                "Gambler Statistics:\n" +
                        "ID: %s\n" +
                        "Name: %s\n" +
                        "Current Stake: $%.2f\n" +
                        "Initial Stake: $%.2f\n" +
                        "Net Profit/Loss: $%.2f\n" +
                        "Total Bets: %d\n" +
                        "Wins: %d\n" +
                        "Losses: %d\n" +
                        "Win Rate: %.2f%%\n" +
                        "Total Winnings: $%.2f\n" +
                        "Total Losses: $%.2f\n" +
                        "Average Bet: $%.2f\n" +
                        "Win Threshold Reached: %s\n" +
                        "Loss Threshold Reached: %s",
                gamblerId, name, currentStake, initialStake, netProfit,
                totalBets, wins, losses, winRate, totalWinnings, totalLosses,
                averageBetAmount, hasReachedWinThreshold, hasReachedLossThreshold
        );
    }
}

