import java.time.LocalDateTime;
import java.util.UUID;

public class GamblerProfile {
    private String gamblerId;
    private String name;
    private String email;
    private double initialStake;
    private double currentStake;
    private double winThreshold;
    private double lossThreshold;
    private int totalBets;
    private int wins;
    private int losses;
    private LocalDateTime createdAt;
    private boolean isActive;

    public GamblerProfile(String name, String email, double initialStake,
                          double winThreshold, double lossThreshold) {
        this.gamblerId = "GAMBLER-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.email = email;
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.winThreshold = winThreshold;
        this.lossThreshold = lossThreshold;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void updatePersonalInfo(String name, String email) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
    }

    public void recordBet(boolean won, double amount) {
        totalBets++;
        if (won) {
            wins++;
            currentStake += amount;
        } else {
            losses++;
            currentStake -= amount;
        }
    }

    public void resetProfile(double newInitialStake) {
        this.initialStake = newInitialStake;
        this.currentStake = newInitialStake;
        this.totalBets = 0;
        this.wins = 0;
        this.losses = 0;
    }

    // Getters
    public String getGamblerId() { return gamblerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getCurrentStake() { return currentStake; }
    public void setCurrentStake(double stake) { this.currentStake = stake; }
    public double getInitialStake() { return initialStake; }
    public double getWinThreshold() { return winThreshold; }
    public double getLossThreshold() { return lossThreshold; }
    public int getTotalBets() { return totalBets; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}
