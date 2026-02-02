
/**
 * BettingPreferences class stores configurable betting behavior preferences for a gambler.
 * Includes:
 * - Maximum and minimum allowed bet amount.
 * - Preferred game type.
 * - Auto-play configuration.
 * - Session time limits.
 * Used to control betting boundaries and session safety rules.
 */
public class BettingPreferences {
    private double maxBetAmount;
    private double minBetAmount;
    private String preferredGameType;
    private boolean autoPlay;
    private int sessionTimeLimit; // in minutes

    public BettingPreferences() {
        this.maxBetAmount = 1000.0;
        this.minBetAmount = 10.0;
        this.preferredGameType = "SLOT";
        this.autoPlay = false;
        this.sessionTimeLimit = 120;
    }

    // Getters and Setters
    public double getMaxBetAmount() { return maxBetAmount; }
    public void setMaxBetAmount(double maxBetAmount) { this.maxBetAmount = maxBetAmount; }
    public double getMinBetAmount() { return minBetAmount; }
    public void setMinBetAmount(double minBetAmount) { this.minBetAmount = minBetAmount; }
    public String getPreferredGameType() { return preferredGameType; }
    public void setPreferredGameType(String preferredGameType) { this.preferredGameType = preferredGameType; }
    public boolean isAutoPlay() { return autoPlay; }
    public void setAutoPlay(boolean autoPlay) { this.autoPlay = autoPlay; }
    public int getSessionTimeLimit() { return sessionTimeLimit; }
    public void setSessionTimeLimit(int sessionTimeLimit) { this.sessionTimeLimit = sessionTimeLimit; }
}
