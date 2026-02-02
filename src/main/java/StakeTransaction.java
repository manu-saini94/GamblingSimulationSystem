import java.time.LocalDateTime;
import java.util.UUID;

public class StakeTransaction {
    private String transactionId;
    private LocalDateTime timestamp;
    private TransactionType type;
    private double amount;
    private double previousBalance;
    private double newBalance;

    public StakeTransaction(TransactionType type, double amount,
                            double previousBalance, double newBalance) {
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getNewBalance() { return newBalance; }
}
