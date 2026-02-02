import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a single stake-related financial transaction.
 * Maintains complete audit trail information including:
 * - Transaction identifiers
 * - Gambler association
 * - Transaction type classification
 * - Balance before and after transaction
 * - Timestamp tracking
 * - Optional bet traceability
 * Used for:
 * - Audit logging
 * - Financial history reporting
 * - Compliance tracking
 * - Session analytics
 */

public class StakeTransaction {
    private String transactionId;
    private String gamblerId;
    private LocalDateTime timestamp;
    private TransactionType type;
    private double amount;
    private double previousBalance;
    private double newBalance;
    private String description;
    private String betId;

    /**
     *
     * Creates a new stake transaction audit record.
     * Automatically generates:
     * - Unique transaction ID
     * - Timestamp of transaction creation
     * Captures:
     * - Transaction classification
     * - Balance before and after transaction
     * - Optional bet traceability
     * @param gamblerId Unique gambler identifier.
     * @param type Transaction type classification.
     * @param amount Transaction amount processed.
     * @param previousBalance Stake balance before transaction execution.
     * @param newBalance Stake balance after transaction execution.
     * @param description Human-readable transaction description.
     * @param betId Optional bet identifier for traceability (nullable).
     */
    public StakeTransaction(String gamblerId, TransactionType type, double amount,
                            double previousBalance, double newBalance, String description, String betId) {
        this.transactionId = UUID.randomUUID().toString();
        this.gamblerId = gamblerId;
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.description = description;
        this.betId = betId;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getGamblerId() { return gamblerId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getPreviousBalance() { return previousBalance; }
    public double getNewBalance() { return newBalance; }
    public String getDescription() { return description; }
    public String getBetId() { return betId; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: $%.2f (Balance: $%.2f -> $%.2f) - %s",
                timestamp.format(formatter), type, amount, previousBalance, newBalance, description);
    }
}
