import java.util.ArrayList;
import java.util.List;

public class WinLossCalculator {
    private List<GameRecord> gameRecords;
    private double totalWinnings;
    private double totalLosses;
    private int consecutiveWins;
    private int consecutiveLosses;
    private int longestWinStreak;
    private int longestLossStreak;

    public WinLossCalculator() {
        this.gameRecords = new ArrayList<>();
    }

    public GameRecord calculateOutcome(double betAmount, double winProbability,
                                       boolean won, double currentStake) {
        double winnings = 0;
        double losses = 0;

        if (won) {
            winnings = betAmount * ((1.0 / winProbability) - 1.0);
            totalWinnings += winnings;
            consecutiveWins++;
            consecutiveLosses = 0;
            if (consecutiveWins > longestWinStreak) {
                longestWinStreak = consecutiveWins;
            }
        } else {
            losses = betAmount;
            totalLosses += losses;
            consecutiveLosses++;
            consecutiveWins = 0;
            if (consecutiveLosses > longestLossStreak) {
                longestLossStreak = consecutiveLosses;
            }
        }

        GameRecord record = new GameRecord(won, betAmount, winnings, losses,
                currentStake, currentStake + winnings - losses);
        gameRecords.add(record);
        return record;
    }

    public double getWinLossRatio() {
        long wins = gameRecords.stream().filter(r -> r.won).count();
        long losses = gameRecords.stream().filter(r -> !r.won).count();
        return losses > 0 ? (double) wins / losses : wins;
    }

    public double getWinRate() {
        if (gameRecords.isEmpty()) return 0;
        long wins = gameRecords.stream().filter(r -> r.won).count();
        return (double) wins / gameRecords.size();
    }

    // Getters
    public double getTotalWinnings() { return totalWinnings; }
    public double getTotalLosses() { return totalLosses; }
    public int getConsecutiveWins() { return consecutiveWins; }
    public int getConsecutiveLosses() { return consecutiveLosses; }
    public int getLongestWinStreak() { return longestWinStreak; }
    public int getLongestLossStreak() { return longestLossStreak; }
}
