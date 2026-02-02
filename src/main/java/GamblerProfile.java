import java.time.LocalDateTime;

/**
 * GamblerProfile class represents the core gambler profile entity.
 * Responsibilities:
 * - Stores personal details and contact information.
 * - Maintains financial data including initial and current stake.
 * - Tracks betting statistics such as total bets, wins, and losses.
 * - Maintains threshold limits for win and loss conditions.
 * - Tracks timestamps for creation and last update.
 * - Maintains account activation status.
 * - Contains associated betting preferences.
 */
public class GamblerProfile {
    private String gamblerId;
    private String name;
    private String email;
    private String phone;
    private double initialStake;
    private double currentStake;
    private double winThreshold;
    private double lossThreshold;
    private int totalBets;
    private int wins;
    private int losses;
    private double totalWinnings;
    private double totalLosses;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private boolean isActive;
    private BettingPreferences preferences;

    public GamblerProfile(String gamblerId, String name, String email, String phone,
                          double initialStake, double winThreshold, double lossThreshold) {
        this.gamblerId = gamblerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.initialStake = initialStake;
        this.currentStake = initialStake;
        this.winThreshold = winThreshold;
        this.lossThreshold = lossThreshold;
        this.totalBets = 0;
        this.wins = 0;
        this.losses = 0;
        this.totalWinnings = 0.0;
        this.totalLosses = 0.0;
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.isActive = true;
        this.preferences = new BettingPreferences();
    }

    // Getters and Setters
    public String getGamblerId() { return gamblerId; }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        this.lastUpdated = LocalDateTime.now();
    }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        this.lastUpdated = LocalDateTime.now();
    }
    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        this.phone = phone;
        this.lastUpdated = LocalDateTime.now();
    }
    public double getInitialStake() { return initialStake; }
    public double getCurrentStake() { return currentStake; }
    public void setCurrentStake(double currentStake) {
        this.currentStake = currentStake;
        this.lastUpdated = LocalDateTime.now();
    }
    public double getWinThreshold() { return winThreshold; }
    public void setWinThreshold(double winThreshold) {
        this.winThreshold = winThreshold;
        this.lastUpdated = LocalDateTime.now();
    }
    public double getLossThreshold() { return lossThreshold; }
    public void setLossThreshold(double lossThreshold) {
        this.lossThreshold = lossThreshold;
        this.lastUpdated = LocalDateTime.now();
    }
    public int getTotalBets() { return totalBets; }
    public void incrementTotalBets() { this.totalBets++; }
    public int getWins() { return wins; }
    public void incrementWins() { this.wins++; }
    public int getLosses() { return losses; }
    public void incrementLosses() { this.losses++; }
    public double getTotalWinnings() { return totalWinnings; }
    public void addWinnings(double amount) { this.totalWinnings += amount; }
    public double getTotalLosses() { return totalLosses; }
    public void addLosses(double amount) { this.totalLosses += amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) {
        this.isActive = active;
        this.lastUpdated = LocalDateTime.now();
    }
    public BettingPreferences getPreferences() { return preferences; }
    public void setPreferences(BettingPreferences preferences) {
        this.preferences = preferences;
        this.lastUpdated = LocalDateTime.now();
    }
}
