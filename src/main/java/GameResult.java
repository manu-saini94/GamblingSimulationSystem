/**
 * Represents single game execution result.
 * Contains:
 * - Game sequence number
 * - Win/Loss outcome
 * - Bet and win amounts
 * - Stake transition values
 */
public class GameResult {
    int gameNumber;
    boolean won;
    double betAmount;
    double winAmount;
    double stakeBefore;
    double stakeAfter;
    public GameResult(int gameNumber, boolean won, double betAmount, double winAmount, double stakeBefore, double stakeAfter) {
        this.gameNumber = gameNumber;
        this.won = won;
        this.betAmount = betAmount;
        this.winAmount = winAmount;
        this.stakeBefore = stakeBefore;
        this.stakeAfter = stakeAfter;
    }
}
