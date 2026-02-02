import java.time.format.DateTimeFormatter;

/**
 * Handles all visual console display rendering for game status,
 * game outcomes, and session summaries.

 * Responsibilities:
 * - Display current game financial state
 * - Display individual game results
 * - Display session summary reports
 * - Render stake visualization bar

 * Supports theme-based rendering and optional color support.
 */
public class GameStatusDisplay {
    private DisplayTheme theme;
    private boolean useColors;
    public GameStatusDisplay(DisplayTheme theme, boolean useColors) {
        this.theme = theme;
        this.useColors = useColors;
    }
    public GameStatusDisplay() {
        this(DisplayTheme.MODERN, false);
    }

    /**
     *
     * Displays the current financial and gameplay status of the session.

     * Includes:
     * - Current stake and profit/loss indicator
     * - Session limits and distances to boundaries
     * - Game statistics (wins, losses, win rate)
     * - Visual stake position bar

     * @param currentStake current stake balance
     * @param initialStake starting stake for session
     * @param gamesPlayed number of games played
     * @param wins total wins
     * @param losses total losses
     * @param lowerLimit session lower stake boundary
     * @param upperLimit session upper stake boundary
     */
    public void displayCurrentStatus(double currentStake, double initialStake, int gamesPlayed, int wins, int losses, double lowerLimit, double upperLimit) {
        System.out.println();
        printBorder(60, theme.getHeavy());
        System.out.println(centerText("CURRENT GAME STATUS", 60));
        printBorder(60, theme.getHeavy());
        double netChange = currentStake - initialStake;
        double changePercent = (netChange / initialStake) * 100;
        System.out.println(String.format("  Current Stake:     $%,.2f %s", currentStake, getChangeIndicator(netChange)));
        System.out.println(String.format("  Initial Stake:     $%,.2f", initialStake));
        System.out.println(String.format("  Net Change:        %s$%,.2f (%.2f%%)", netChange >= 0 ? "+" : "", netChange, changePercent));
        printBorder(60, theme.getLight());
        System.out.println(String.format("  Lower Limit:       $%,.2f", lowerLimit));
        System.out.println(String.format("  Upper Limit:       $%,.2f", upperLimit));
        double lowerDistance = currentStake - lowerLimit;
        double upperDistance = upperLimit - currentStake;
        System.out.println(String.format("  Distance to Loss:  $%,.2f", lowerDistance));
        System.out.println(String.format("  Distance to Win:   $%,.2f", upperDistance));
        printBorder(60, theme.getLight());
        System.out.println(String.format("  Games Played:      %d", gamesPlayed));
        System.out.println(String.format("  Wins:              %d", wins));
        System.out.println(String.format("  Losses:            %d", losses));
        if(gamesPlayed > 0) {
            double winRate = (double) wins / gamesPlayed * 100;
            System.out.println(String.format("  Win Rate:          %.2f%%", winRate));
        }
        printStakeBar(currentStake, lowerLimit, upperLimit, 50);
        printBorder(60, theme.getHeavy());
        System.out.println();
    }
    private void printStakeBar(double current, double lower, double upper, int width) {
        System.out.println();
        double range = upper - lower;
        double position = (current - lower) / range;
        int markerPos = (int)(position * width);
        System.out.print("  [");
        for(int i = 0; i < width; i++) {
            if(i == markerPos) {
                System.out.print("█");
            } else if(i < markerPos) {
                System.out.print("▓");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("]");
        System.out.println(String.format("  $%,.0f %s $%,.0f", lower, " ".repeat(width - 10), upper));
    }

    /**
     *
     * Displays formatted result of a completed game.

     * Shows:
     * - Win/Loss result
     * - Bet amount
     * - Winnings or loss amount
     * - Stake before and after game

     * @param gameNumber game sequence number
     * @param won whether game was won
     * @param betAmount bet placed
     * @param winAmount winnings earned
     * @param stakeBefore stake before game
     * @param stakeAfter stake after game
     */
    public void displayGameOutcome(int gameNumber, boolean won, double betAmount, double winAmount, double stakeBefore, double stakeAfter) {
        System.out.println();
        printBorder(60, theme.getLight());
        String outcome = won ? "★ YOU WIN! ★" : "✗ YOU LOSE";
        String color = won ? "" : "";
        System.out.println(centerText(String.format("GAME #%d RESULT", gameNumber), 60));
        printBorder(60, theme.getLight());
        System.out.println(centerText(outcome, 60));
        System.out.println();
        System.out.println(String.format("  Bet Amount:        $%,.2f", betAmount));
        if(won) {
            System.out.println(String.format("  Winnings:          +$%,.2f", winAmount));
        } else {
            System.out.println(String.format("  Loss:              -$%,.2f", betAmount));
        }
        System.out.println();
        System.out.println(String.format("  Previous Stake:    $%,.2f", stakeBefore));
        System.out.println(String.format("  New Stake:         $%,.2f %s", stakeAfter, getChangeIndicator(stakeAfter - stakeBefore)));
        System.out.println(String.format("  Change:            %s$%,.2f",
                (stakeAfter - stakeBefore) >= 0 ? "+" : "", stakeAfter - stakeBefore));
        printBorder(60, theme.getLight());
        System.out.println();
    }

    /**
     * Displays comprehensive session summary including financial
     * results, duration, statistics, and performance metrics.
     * @param summary session summary data object
     */
    public void displaySessionSummary(SessionSummary summary) {
        System.out.println();
        printBorder(70, theme.getHeavy());
        System.out.println(centerText("SESSION SUMMARY", 70));
        printBorder(70, theme.getHeavy());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println();
        System.out.println("  SESSION INFORMATION");
        printBorder(70, theme.getLight());
        System.out.println(String.format("  Session ID:         %s", summary.sessionId));
        System.out.println(String.format("  Started:            %s", summary.startTime.format(formatter)));
        System.out.println(String.format("  Ended:              %s", summary.endTime.format(formatter)));
        System.out.println(String.format("  Duration:           %s", formatDuration(summary.durationSeconds)));
        System.out.println(String.format("  End Reason:         %s", summary.endReason));
        System.out.println();
        System.out.println("  FINANCIAL SUMMARY");
        printBorder(70, theme.getLight());
        System.out.println(String.format("  Initial Stake:      $%,.2f", summary.initialStake));
        System.out.println(String.format("  Final Stake:        $%,.2f", summary.finalStake));
        System.out.println(String.format("  Net Profit/Loss:    %s$%,.2f", summary.netProfit >= 0 ? "+" : "", summary.netProfit));
        System.out.println(String.format("  Return:             %.2f%%", summary.returnPercent));
        System.out.println(String.format("  Total Wagered:      $%,.2f", summary.totalWagered));
        System.out.println(String.format("  Total Won:          $%,.2f", summary.totalWon));
        System.out.println(String.format("  Total Lost:         $%,.2f", summary.totalLost));
        System.out.println();
        System.out.println("  GAME STATISTICS");
        printBorder(70, theme.getLight());
        System.out.println(String.format("  Games Played:       %d", summary.gamesPlayed));
        System.out.println(String.format("  Games Won:          %d (%.2f%%)", summary.wins, summary.winRate));
        System.out.println(String.format("  Games Lost:         %d (%.2f%%)", summary.losses, summary.lossRate));
        System.out.println(String.format("  Longest Win Streak: %d", summary.longestWinStreak));
        System.out.println(String.format("  Longest Loss Streak:%d", summary.longestLossStreak));
        System.out.println(String.format("  Average Bet:        $%,.2f", summary.averageBet));
        System.out.println(String.format("  Largest Win:        $%,.2f", summary.largestWin));
        System.out.println(String.format("  Largest Loss:       $%,.2f", summary.largestLoss));
        if(summary.netProfit > 0) {
            System.out.println();
            printBorder(70, theme.getHeavy());
            System.out.println(centerText("★★★ CONGRATULATIONS! YOU WON! ★★★", 70));
            printBorder(70, theme.getHeavy());
        } else if(summary.netProfit < 0) {
            System.out.println();
            printBorder(70, theme.getHeavy());
            System.out.println(centerText("Better luck next time!", 70));
            printBorder(70, theme.getHeavy());
        }
        System.out.println();
    }
    // Helper methods
    private void printBorder(int width, String character) {
        System.out.println(character.repeat(width));
    }
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    private String getChangeIndicator(double change) {
        if(change > 0) return "↑";
        if(change < 0) return "↓";
        return "→";
    }
    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        if(hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, secs);
        } else if(minutes > 0) {
            return String.format("%dm %ds", minutes, secs);
        } else {
            return String.format("%ds", secs);
        }
    }
}
