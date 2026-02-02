/**
 * UC7: User Interaction Module Main Application
 * Provides full interactive gambling simulator experience.
 * Features:
 * - Interactive menu navigation
 * - Session lifecycle management
 * - Real-time game execution
 * - Auto-play functionality
 * - Settings configuration
 * Acts as presentation + orchestration layer.
 */
public class GamblingSimulationSystemUC7 {
    private InteractiveMenu menu;
    private GameStatusDisplay display;
    private SimpleGameEngine engine;
    private boolean sessionActive;
    private double minBet = 10.0;
    private double maxBet = 500.0;
    private double winProbability = 0.48;
    public GamblingSimulationSystemUC7() {
        this.menu = new InteractiveMenu();
        this.display = menu.getDisplay();
        this.sessionActive = false;
    }

    /**
     * Main application loop controlling menu navigation and system flow.
     */
    public void run() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO THE GAMBLING SIMULATOR               ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
        boolean running = true;
        while(running) {
            int choice = menu.displayMainMenu();
            switch(choice) {
                case 1:
                    startNewSession();
                    break;
                case 2:
                    viewCurrentStatus();
                    break;
                case 3:
                    placeBet();
                    break;
                case 4:
                    autoPlay();
                    break;
                case 5:
                    viewStatistics();
                    break;
                case 6:
                    pauseSession();
                    break;
                case 7:
                    endSession();
                    break;
                case 8:
                    settings();
                    break;
                case 0:
                    if(sessionActive) {
                        if(menu.confirmAction("You have an active session. End it?")) {
                            endSession();
                        }
                    }
                    running = false;
                    menu.displaySuccess("Thank you for playing!");
                    break;
            }
        }
        menu.close();
    }

    /**
     * Initializes new game session using user configuration input.
     */
    private void startNewSession() {
        if(sessionActive) {
            menu.displayError("Session already active. End current session first.");
            return;
        }
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│     NEW SESSION CONFIGURATION          │");
        System.out.println("└────────────────────────────────────────┘\n");
        double initialStake = menu.promptForDouble("Initial stake", 100, 10000);
        double lowerLimit = menu.promptForDouble("Lower limit (loss)", 50, initialStake - 1);
        double upperLimit = menu.promptForDouble("Upper limit (win)", initialStake + 1, 50000);
        engine = new SimpleGameEngine(initialStake, lowerLimit, upperLimit);
        sessionActive = true;
        menu.displaySuccess("Session started successfully!");
        display.displayCurrentStatus(engine.getCurrentStake(), engine.getInitialStake(), engine.getGamesPlayed(), engine.getWins(), engine.getLosses(), engine.getLowerLimit(), engine.getUpperLimit());
        menu.pressEnterToContinue();
    }

    /**
     * Displays current session financial and gameplay status.
     */
    private void viewCurrentStatus() {
        if(!sessionActive) {
            menu.displayError("No active session. Start a new session first.");
            return;
        }
        display.displayCurrentStatus(engine.getCurrentStake(), engine.getInitialStake(), engine.getGamesPlayed(), engine.getWins(), engine.getLosses(), engine.getLowerLimit(), engine.getUpperLimit());
        menu.pressEnterToContinue();
    }

    /**
     * Handles manual bet placement and result display.
     */
    private void placeBet() {
        if(!sessionActive) {
            menu.displayError("No active session. Start a new session first.");
            return;
        }
        if(engine.hasReachedLimits()) {
            menu.displayMessage("Session limits reached. Please view summary.");
            endSession();
            return;
        }
        double betAmount = menu.promptForBetAmount(engine.getCurrentStake(), minBet, maxBet);
        GameResult result = engine.playGame(betAmount, winProbability);
        display.displayGameOutcome(result.gameNumber, result.won, result.betAmount, result.winAmount, result.stakeBefore, result.stakeAfter);
        if(engine.hasReachedLimits()) {
            menu.displayMessage("Session limits reached!");
            menu.pressEnterToContinue();
            endSession();
        } else {
            menu.pressEnterToContinue();
        }
    }

    /**
     * Executes multiple automated game rounds.
     */
    private void autoPlay() {
        if(!sessionActive) {
            menu.displayError("No active session. Start a new session first.");
            return;
        }
        int games = menu.promptForInt("Number of games to play", 1, 50);
        double betAmount = menu.promptForBetAmount(engine.getCurrentStake(), minBet, maxBet);
        System.out.println(String.format("\n  Starting auto-play: %d games at $%.2f each\n", games, betAmount));
        for(int i = 0; i < games; i++) {
            if(engine.hasReachedLimits() || betAmount > engine.getCurrentStake()) {
                menu.displayMessage("Cannot continue - limits reached or insufficient stake");
                break;
            }
            GameResult result = engine.playGame(betAmount, winProbability);
            System.out.println(String.format("  Game %d: %s $%.2f | Stake: $%.2f", result.gameNumber, result.won ? "WON" : "LOST", result.won ? result.winAmount : result.betAmount, result.stakeAfter));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        viewCurrentStatus();
        if(engine.hasReachedLimits()) {
            endSession();
        }
    }

    /**
     * Displays session statistics dashboard.
     */
    private void viewStatistics() {
        if(!sessionActive) {
            menu.displayError("No active session.");
            return;
        }
        viewCurrentStatus();
    }

    /**
     * Simulates session pause state.
     */
    private void pauseSession() {
        if(!sessionActive) {
            menu.displayError("No active session.");
            return;
        }
        menu.displayMessage("Session paused. Select any menu option to resume.");
    }

    /**
     * Ends current session and displays final summary report.
     */
    private void endSession() {
        if(!sessionActive) {
            menu.displayError("No active session to end.");
            return;
        }
        String endReason;
        if(engine.getCurrentStake() <= engine.getLowerLimit()) {
            endReason = "Lower limit reached (Loss)";
        } else if(engine.getCurrentStake() >= engine.getUpperLimit()) {
            endReason = "Upper limit reached (Win)";
        } else {
            endReason = "Manual termination";
        }
        SessionSummary summary = engine.generateSummary(endReason);
        display.displaySessionSummary(summary);
        sessionActive = false;
        engine = null;
        menu.pressEnterToContinue();
    }

    /**
     * Allows modification of betting configuration settings.
     */
    private void settings() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│            SETTINGS                    │");
        System.out.println("└────────────────────────────────────────┘\n");
        System.out.println(String.format("  Current Min Bet:         $%.2f", minBet));
        System.out.println(String.format("  Current Max Bet:         $%.2f", maxBet));
        System.out.println(String.format("  Win Probability:         %.2f%%", winProbability * 100));
        System.out.println();
        if(menu.confirmAction("Change settings?")) {
            minBet = menu.promptForDouble("New min bet", 1, 100);
            maxBet = menu.promptForDouble("New max bet", minBet, 10000);
            winProbability = menu.promptForDouble("Win probability", 0.1, 0.9);
            menu.displaySuccess("Settings updated!");
        }
        menu.pressEnterToContinue();
    }
    public static void main(String[] args) {
        GamblingSimulationSystemUC7 demo = new GamblingSimulationSystemUC7();
        demo.run();
    }
}
