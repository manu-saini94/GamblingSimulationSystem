import java.time.LocalDateTime;
import java.util.UUID;

public class BetRecord {
    private String betId;
    private double amount;
    private boolean won;
    private double winAmount;
    private LocalDateTime timestamp;

    public BetRecord(double amount) {
        this.betId = "BET-" + UUID.randomUUID().toString().substring(0, 8);
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public void settle(boolean won, double winAmount) {
        this.won = won;
        this.winAmount = winAmount;
    }

    public String getBetId() { return betId; }
    public double getAmount() { return amount; }
    public boolean isWon() { return won; }
    public double getWinAmount() { return winAmount; }
}
