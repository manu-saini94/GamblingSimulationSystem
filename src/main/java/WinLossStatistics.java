import java.util.List;

/**
 * Provides advanced statistical analysis of game results.
 * Includes:
 * - Win/Loss counts
 * - Financial metrics
 * - Profit factor
 * - Streak tracking
 * - Performance metrics
 */
public class WinLossStatistics {
    private int totalGames;
    private int wins;
    private int losses;
    private int pushes;
    private double totalWinnings;
    private double totalLosses;
    private double netProfit;
    private double winRate;
    private double averageWin;
    private double averageLoss;
    private double profitFactor;
    private int currentWinStreak;
    private int currentLossStreak;
    private int longestWinStreak;
    private int longestLossStreak;
    private double largestWin;
    private double largestLoss;

    /**
     * Creates statistics object from results list.
     * @param results List of game results.
     */
    public WinLossStatistics(List<GameResult> results) {
        calculateStatistics(results);
    }

    /**
     * Calculates all statistical metrics.
     * @param results Game result list.
     */
    private void calculateStatistics(List<GameResult> results) {
        this.totalGames = results.size();
        this.wins = 0;
        this.losses = 0;
        this.pushes = 0;
        this.totalWinnings = 0.0;
        this.totalLosses = 0.0;
        this.largestWin = 0.0;
        this.largestLoss = 0.0;

        int tempWinStreak = 0;
        int tempLossStreak = 0;
        this.longestWinStreak = 0;
        this.longestLossStreak = 0;

        for (GameResult result : results) {
            switch (result.getOutcome()) {
                case WIN:
                    wins++;
                    totalWinnings += result.getWinnings();
                    if (result.getWinnings() > largestWin) {
                        largestWin = result.getWinnings();
                    }
                    tempWinStreak++;
                    tempLossStreak = 0;
                    if (tempWinStreak > longestWinStreak) {
                        longestWinStreak = tempWinStreak;
                    }
                    break;
                case LOSS:
                    losses++;
                    totalLosses += result.getLosses();
                    if (result.getLosses() > largestLoss) {
                        largestLoss = result.getLosses();
                    }
                    tempLossStreak++;
                    tempWinStreak = 0;
                    if (tempLossStreak > longestLossStreak) {
                        longestLossStreak = tempLossStreak;
                    }
                    break;
                case PUSH:
                    pushes++;
                    tempWinStreak = 0;
                    tempLossStreak = 0;
                    break;
            }
        }

        this.currentWinStreak = tempWinStreak;
        this.currentLossStreak = tempLossStreak;
        this.netProfit = totalWinnings - totalLosses;
        this.winRate = totalGames > 0 ? (double) wins / totalGames : 0.0;
        this.averageWin = wins > 0 ? totalWinnings / wins : 0.0;
        this.averageLoss = losses > 0 ? totalLosses / losses : 0.0;
        this.profitFactor = totalLosses > 0 ? totalWinnings / totalLosses :
                (totalWinnings > 0 ? Double.POSITIVE_INFINITY : 0.0);
    }

    /**
     * Generates formatted statistics report.
     * @return Report string.
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== WIN/LOSS STATISTICS ==========\n");

        sb.append("\n--- Overall Results ---\n");
        sb.append(String.format("Total Games: %d\n", totalGames));
        sb.append(String.format("Wins: %d (%.2f%%)\n", wins, winRate * 100));
        sb.append(String.format("Losses: %d (%.2f%%)\n", losses, (double) losses / totalGames * 100));
        sb.append(String.format("Pushes: %d (%.2f%%)\n", pushes, (double) pushes / totalGames * 100));

        sb.append("\n--- Financial Summary ---\n");
        sb.append(String.format("Total Winnings: $%.2f\n", totalWinnings));
        sb.append(String.format("Total Losses: $%.2f\n", totalLosses));
        sb.append(String.format("Net Profit/Loss: %s$%.2f\n",
                netProfit >= 0 ? "+" : "", netProfit));
        sb.append(String.format("Average Win: $%.2f\n", averageWin));
        sb.append(String.format("Average Loss: $%.2f\n", averageLoss));
        sb.append(String.format("Largest Win: $%.2f\n", largestWin));
        sb.append(String.format("Largest Loss: $%.2f\n", largestLoss));
        sb.append(String.format("Profit Factor: %.2f\n", profitFactor));

        sb.append("\n--- Streak Analysis ---\n");
        sb.append(String.format("Current Win Streak: %d\n", currentWinStreak));
        sb.append(String.format("Current Loss Streak: %d\n", currentLossStreak));
        sb.append(String.format("Longest Win Streak: %d\n", longestWinStreak));
        sb.append(String.format("Longest Loss Streak: %d\n", longestLossStreak));

        sb.append("\n--- Performance Metrics ---\n");
        sb.append(String.format("Win Rate: %.2f%%\n", winRate * 100));
        sb.append(String.format("Win/Loss Ratio: %.2f\n",
                losses > 0 ? (double) wins / losses : wins));
        sb.append(String.format("Return on Risk: %.2f%%\n",
                totalLosses > 0 ? (netProfit / totalLosses) * 100 : 0.0));

        sb.append("\n========================================\n");
        return sb.toString();
    }

    // Getters
    public int getTotalGames() { return totalGames; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getPushes() { return pushes; }
    public double getTotalWinnings() { return totalWinnings; }
    public double getTotalLosses() { return totalLosses; }
    public double getNetProfit() { return netProfit; }
    public double getWinRate() { return winRate; }
    public double getAverageWin() { return averageWin; }
    public double getAverageLoss() { return averageLoss; }
    public double getProfitFactor() { return profitFactor; }
    public int getCurrentWinStreak() { return currentWinStreak; }
    public int getCurrentLossStreak() { return currentLossStreak; }
    public int getLongestWinStreak() { return longestWinStreak; }
    public int getLongestLossStreak() { return longestLossStreak; }
    public double getLargestWin() { return largestWin; }
    public double getLargestLoss() { return largestLoss; }
}

