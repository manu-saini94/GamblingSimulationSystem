import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * GamblerProfileService is a service layer responsible for executing all Gambler Profile management operations.
 * Responsibilities:
 * - Manages gambler profile lifecycle.
 * - Performs validation checks.
 * - Calculates statistics and financial states.
 * - Controls session reset logic with proportional thresholds.
 * - Maintains in-memory repository of gambler profiles.
 * Business Rules:
 * - Minimum stake must meet system requirement.
 * - Win threshold must be greater than initial stake.
 * - Loss threshold must be lower than initial stake.
 */
public class GamblerProfileService {
    private Map<String, GamblerProfile> gamblerRepository;
    private static final double MINIMUM_STAKE_REQUIREMENT = 100.0;

    public GamblerProfileService() {
        this.gamblerRepository = new HashMap<>();
    }

    /**
     * Creates a new gambler profile.
     * Validations:
     * - Initial stake must meet minimum requirement.
     * - Win threshold must be greater than initial stake.
     * - Loss threshold must be less than initial stake.
     *
     * @param name Name of the gambler.
     * @param email Email address of the gambler.
     * @param phone Phone number of the gambler.
     * @param initialStake Initial amount deposited by gambler.
     * @param winThreshold Target amount at which gambler is expected to stop after profit.
     * @param lossThreshold Minimum amount after which gambler should stop to prevent loss.
     *
     * @return Newly created GamblerProfile object.
     *
     * @throws Exception If:
     *                   - Initial stake is below minimum requirement.
     *                   - Win threshold is not greater than initial stake.
     *                   - Loss threshold is not less than initial stake.
     */
    public GamblerProfile createGambler(String name, String email, String phone,
                                        double initialStake, double winThreshold,
                                        double lossThreshold) throws Exception {
        if (initialStake < MINIMUM_STAKE_REQUIREMENT) {
            throw new Exception("Initial stake must be at least $" + MINIMUM_STAKE_REQUIREMENT);
        }
        if (winThreshold <= initialStake) {
            throw new Exception("Win threshold must be greater than initial stake");
        }
        if (lossThreshold >= initialStake) {
            throw new Exception("Loss threshold must be less than initial stake");
        }

        String gamblerId = UUID.randomUUID().toString();
        GamblerProfile profile = new GamblerProfile(gamblerId, name, email, phone,
                initialStake, winThreshold, lossThreshold);
        gamblerRepository.put(gamblerId, profile);
        System.out.println("Gambler created successfully: " + gamblerId);
        return profile;
    }

    /**
     *
     * Updates gambler personal information or betting preferences.
     * Supports partial updates:
     * - Name
     * - Email
     * - Phone
     * - Betting Preferences
     * - Threshold values
     * Updates last modified timestamp after successful update.
     * @param gamblerId Unique gambler identifier.
     * @param name Updated name (nullable if not updating).
     * @param email Updated email (nullable if not updating).
     * @param phone Updated phone number (nullable if not updating).
     *
     * @throws Exception If gambler profile is not found.
     */
    public void updateGamblerInfo(String gamblerId, String name, String email, String phone) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        if (name != null) profile.setName(name);
        if (email != null) profile.setEmail(email);
        if (phone != null) profile.setPhone(phone);
        System.out.println("Gambler information updated successfully");
    }

    /**
     * Updates gambler betting preferences.
     *
     * @param gamblerId Unique gambler identifier.
     * @param preferences New betting preference configuration.
     *
     * @throws Exception If gambler profile is not found.
     */
    public void updateBettingPreferences(String gamblerId, BettingPreferences preferences) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        profile.setPreferences(preferences);
        System.out.println("Betting preferences updated successfully");
    }

    /**
     * Updates win and loss threshold values.
     *
     * @param gamblerId Unique gambler identifier.
     * @param winThreshold Updated win threshold (nullable if not updating).
     * @param lossThreshold Updated loss threshold (nullable if not updating).
     *
     * @throws Exception If:
     *                   - Gambler not found.
     *                   - Win threshold is invalid.
     *                   - Loss threshold is invalid.
     */
    public void updateThresholds(String gamblerId, Double winThreshold, Double lossThreshold) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        if (winThreshold != null) {
            if (winThreshold <= profile.getInitialStake()) {
                throw new Exception("Win threshold must be greater than initial stake");
            }
            profile.setWinThreshold(winThreshold);
        }
        if (lossThreshold != null) {
            if (lossThreshold >= profile.getInitialStake()) {
                throw new Exception("Loss threshold must be less than initial stake");
            }
            profile.setLossThreshold(lossThreshold);
        }
        System.out.println("Thresholds updated successfully");
    }

    /**
     * Retrieves calculated gambler statistics.
     *
     * @param gamblerId Unique gambler identifier.
     * @return GamblerStatistics object containing financial and performance metrics.
     * @throws Exception If gambler profile is not found.
     */
    public GamblerStatistics getGamblerStatistics(String gamblerId) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        return new GamblerStatistics(profile);
    }

    /**
     * Retrieves current stake amount of gambler.
     * @param gamblerId Unique gambler identifier.
     * @return Current available stake amount.
     * @throws Exception If gambler profile is not found.
     */
    public double getCurrentFinancialStatus(String gamblerId) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        return profile.getCurrentStake();
    }

     /**
      * Validates gambler eligibility for betting.
      * Conditions Checked:
      * - Account must be active.
      * - Current stake must meet minimum requirement.
      * - Loss threshold must not be reached.
      * - Win threshold status is flagged for cash-out recommendation.
     *
     * @param gamblerId Unique gambler identifier.
     * @return true if gambler is eligible, false otherwise.
     * @throws Exception If gambler profile is not found.
     */
    public boolean validateEligibility(String gamblerId) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);

        if (!profile.isActive()) {
            System.out.println("Gambler account is inactive");
            return false;
        }

        if (profile.getCurrentStake() < MINIMUM_STAKE_REQUIREMENT) {
            System.out.println("Insufficient stake. Minimum required: $" + MINIMUM_STAKE_REQUIREMENT);
            return false;
        }

        if (profile.getCurrentStake() <= profile.getLossThreshold()) {
            System.out.println("Loss threshold reached. Cannot continue betting");
            return false;
        }

        if (profile.getCurrentStake() >= profile.getWinThreshold()) {
            System.out.println("Win threshold reached. Consider cashing out");
            return true;
        }

        return true;
    }

     /**
      * Resets gambler profile for a new betting session.
      * Operations:
      * - Resets current stake to new initial stake.
      * - Recalculates win and loss thresholds proportionally.
      * Ensures:
      * - Minimum stake requirement is maintained.
      * Used when starting a fresh gaming session.
     * @param gamblerId Unique gambler identifier.
     * @param newInitialStake New stake amount for fresh session.
     * @throws Exception If:
     *                   - Gambler profile is not found.
     *                   - New stake is below minimum requirement.
     */
    public void resetGamblerProfile(String gamblerId, double newInitialStake) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);

        if (newInitialStake < MINIMUM_STAKE_REQUIREMENT) {
            throw new Exception("New initial stake must be at least $" + MINIMUM_STAKE_REQUIREMENT);
        }

        double oldInitialStake = profile.getInitialStake();
        double oldWinThreshold = profile.getWinThreshold();
        double oldLossThreshold = profile.getLossThreshold();

        // Calculate proportional thresholds
        double winRatio = oldWinThreshold / oldInitialStake;
        double lossRatio = oldLossThreshold / oldInitialStake;

        // Reset financial data
        profile.setCurrentStake(newInitialStake);
        profile.setWinThreshold(newInitialStake * winRatio);
        profile.setLossThreshold(newInitialStake * lossRatio);

        System.out.println("Gambler profile reset successfully for new session");
        System.out.println("New Initial Stake: $" + newInitialStake);
        System.out.println("New Win Threshold: $" + profile.getWinThreshold());
        System.out.println("New Loss Threshold: $" + profile.getLossThreshold());
    }

    // Helper method to get profile
    private GamblerProfile getGamblerProfile(String gamblerId) throws Exception {
        GamblerProfile profile = gamblerRepository.get(gamblerId);
        if (profile == null) {
            throw new Exception("Gambler not found: " + gamblerId);
        }
        return profile;
    }

    /**
     * Deactivates gambler account to prevent further betting activity.
     *
     * @param gamblerId Unique gambler identifier.
     *
     * @throws Exception If gambler profile is not found.
     */
    public void deactivateGambler(String gamblerId) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        profile.setActive(false);
        System.out.println("Gambler account deactivated");
    }

    /**
     * Records a bet and updates financial and statistical data.
     *
     * @param gamblerId Unique gambler identifier.
     * @param betAmount Amount used in the bet.
     * @param won Indicates whether gambler won or lost the bet.
     * @throws Exception If gambler profile is not found.
     */
    public void recordBet(String gamblerId, double betAmount, boolean won) throws Exception {
        GamblerProfile profile = getGamblerProfile(gamblerId);
        profile.incrementTotalBets();

        if (won) {
            profile.incrementWins();
            profile.addWinnings(betAmount);
            profile.setCurrentStake(profile.getCurrentStake() + betAmount);
        } else {
            profile.incrementLosses();
            profile.addLosses(betAmount);
            profile.setCurrentStake(profile.getCurrentStake() - betAmount);
        }
    }
}

