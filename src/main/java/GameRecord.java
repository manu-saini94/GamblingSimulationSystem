public class GameRecord {
    boolean won;
    double betAmount;
    double winnings;
    double losses;
    double stakeBefore;
    double stakeAfter;

    public GameRecord(boolean won, double betAmount, double winnings, double losses,
                      double stakeBefore, double stakeAfter) {
        this.won = won;
        this.betAmount = betAmount;
        this.winnings = winnings;
        this.losses = losses;
        this.stakeBefore = stakeBefore;
        this.stakeAfter = stakeAfter;
    }
}
