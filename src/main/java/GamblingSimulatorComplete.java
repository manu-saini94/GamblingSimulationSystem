

import java.util.*;


// ============================================================================
// MAIN APPLICATION - INTEGRATES ALL USE CASES
// ============================================================================

public class GamblingSimulatorComplete {
    private UserInterface ui;
    private InputValidator validator;
    private GamblerProfile currentGambler;
    private GameSession currentSession;
    private StakeManager stakeManager;
    private BettingEngine bettingEngine;
    private WinLossCalculator winLossCalculator;

    private static final double MIN_STAKE = 100.0;
    private static final double MAX_STAKE = 10000.0;
    private static final double MIN_BET = 10.0;
    private static final double MAX_BET = 1000.0;
    private static final double WIN_PROBABILITY = 0.48;

    public GamblingSimulatorComplete() {
        this.ui = new UserInterface();
        this.validator = new InputValidator(MIN_STAKE, MAX_STAKE, MIN_BET, MAX_BET);
    }

    public void run() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO THE GAMBLING SIMULATOR               ║");
        System.out.println("║   All 7 Use Cases Integrated                      ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            int choice = ui.displayMainMenu();

            try {
                switch (choice) {
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
                        autoPlayGames();
                        break;
                    case 5:
                        pauseSession();
                        break;
                    case 6:
                        endSession();
                        break;
                    case 0:
                        if (currentSession != null && currentSession.canContinue()) {
                            ui.displayMessage("Ending active session...");
                            endSession();
                        }
                        running = false;
                        ui.displayMessage("Thank you for playing!");
                        break;
                    default:
                        ui.displayError("Invalid option");
                }
            } catch (Exception e) {
                ui.displayError(e.getMessage());
            }
        }

        ui.close();
    }

    private void startNewSession() throws ValidationException {
        if (currentSession != null && currentSession.canContinue()) {
            ui.displayError("Session already active. End current session first.");
            return;
        }

        ui.displayMessage("=== NEW SESSION SETUP ===");

        // Use Case 1: Create Gambler Profile
        System.out.print("  Enter your name: ");
        String name = new Scanner(System.in).nextLine();
        System.out.print("  Enter your email: ");
        String email = new Scanner(System.in).nextLine();

        System.out.print("  Enter initial stake ($100-$10000): $");
        double initialStake = validator.parseNumeric(new Scanner(System.in).nextLine(), "Initial stake");
        validator.validateInitialStake(initialStake);

        System.out.print("  Enter lower limit (loss threshold): $");
        double lowerLimit = validator.parseNumeric(new Scanner(System.in).nextLine(), "Lower limit");

        System.out.print("  Enter upper limit (win threshold): $");
        double upperLimit = validator.parseNumeric(new Scanner(System.in).nextLine(), "Upper limit");

        // Use Case 6: Validate all inputs
        validator.validateLimits(lowerLimit, upperLimit, initialStake);

        // Use Case 1: Initialize gambler profile
        currentGambler = new GamblerProfile(name, email, initialStake, upperLimit, lowerLimit);

        // Use Case 2: Initialize stake manager
        stakeManager = new StakeManager(initialStake, lowerLimit, upperLimit);

        // Use Case 4: Start game session
        currentSession = new GameSession(currentGambler.getGamblerId(),
                initialStake, upperLimit, lowerLimit);

        // Use Case 3: Initialize betting engine with fixed strategy
        bettingEngine = new BettingEngine(new FixedAmountStrategy(50.0));

        // Use Case 5: Initialize win/loss calculator
        winLossCalculator = new WinLossCalculator();

        ui.displayMessage("Session started successfully!");
        ui.displayCurrentStatus(currentGambler, currentSession);
        ui.pressEnterToContinue();
    }

    private void viewCurrentStatus() {
        if (currentSession == null) {
            ui.displayError("No active session. Start a new session first.");
            return;
        }

        // Use Case 7: Display current status
        ui.displayCurrentStatus(currentGambler, currentSession);

        // Use Case 2: Display stake fluctuations
        System.out.println("  STAKE MONITORING:");
        System.out.println(String.format("  Peak Stake:        $%,.2f", stakeManager.getPeakStake()));
        System.out.println(String.format("  Lowest Stake:      $%,.2f", stakeManager.getLowestStake()));
        System.out.println();

        ui.pressEnterToContinue();
    }

    private void placeBet() throws Exception {
        if (currentSession == null || !currentSession.canContinue()) {
            ui.displayError("No active session or session has ended.");
            return;
        }

        // Use Case 7: Prompt for bet amount
        double betAmount = ui.promptForBetAmount(stakeManager.getCurrentStake(),
                MIN_BET, MAX_BET);

        // Use Case 6: Validate bet amount
        validator.validateBetAmount(betAmount, stakeManager.getCurrentStake());

        // Use Case 3: Place bet using betting engine
        BetRecord bet = bettingEngine.placeBet(stakeManager.getCurrentStake(),
                MIN_BET, MAX_BET, WIN_PROBABILITY);

        boolean won = bet.isWon();
        double winAmount = bet.getWinAmount();
        double stakeBefore = stakeManager.getCurrentStake();

        // Use Case 2: Process stake changes
        stakeManager.processBetOutcome(won ? winAmount : betAmount, won);

        double stakeAfter = stakeManager.getCurrentStake();

        // Use Case 5: Calculate win/loss
        winLossCalculator.calculateOutcome(betAmount, WIN_PROBABILITY, won, stakeBefore);

        // Use Case 1: Update gambler profile
        currentGambler.recordBet(won, won ? winAmount : betAmount);
        currentGambler.setCurrentStake(stakeAfter);

        // Use Case 4: Record game in session
        currentSession.recordGame(won, stakeAfter);

        // Use Case 7: Display game outcome
        ui.displayGameOutcome(currentSession.getGamesPlayed(), won, betAmount,
                winAmount, stakeBefore, stakeAfter);

        // Check if session should end
        if (!currentSession.canContinue()) {
            ui.displayMessage("Session limits reached!");
            ui.pressEnterToContinue();
            endSession();
        } else {
            ui.pressEnterToContinue();
        }
    }

    private void autoPlayGames() throws Exception {
        if (currentSession == null || !currentSession.canContinue()) {
            ui.displayError("No active session.");
            return;
        }

        System.out.print("  How many games to auto-play? ");
        int numGames = Integer.parseInt(new Scanner(System.in).nextLine());

        System.out.print("  Bet amount per game: $");
        double betAmount = validator.parseNumeric(new Scanner(System.in).nextLine(), "Bet amount");
        validator.validateBetAmount(betAmount, stakeManager.getCurrentStake());

        ui.displayMessage(String.format("Starting auto-play: %d games at $%.2f each",
                numGames, betAmount));

        for (int i = 0; i < numGames; i++) {
            if (!currentSession.canContinue() ||
                    betAmount > stakeManager.getCurrentStake()) {
                ui.displayMessage("Auto-play stopped - limits reached or insufficient stake");
                break;
            }

            // Use Case 3: Place bet
            BetRecord bet = bettingEngine.placeBet(stakeManager.getCurrentStake(),
                    MIN_BET, MAX_BET, WIN_PROBABILITY);

            boolean won = bet.isWon();
            double winAmount = bet.getWinAmount();

            // Use Case 2: Update stake
            stakeManager.processBetOutcome(won ? winAmount : betAmount, won);

            // Use Case 5: Calculate statistics
            winLossCalculator.calculateOutcome(betAmount, WIN_PROBABILITY, won,
                    stakeManager.getCurrentStake());

            // Use Case 1 & 4: Update profile and session
            currentGambler.recordBet(won, won ? winAmount : betAmount);
            currentGambler.setCurrentStake(stakeManager.getCurrentStake());
            currentSession.recordGame(won, stakeManager.getCurrentStake());

            System.out.println(String.format("  Game %d: %s | Stake: $%.2f",
                    i + 1, won ? "WIN" : "LOSS", stakeManager.getCurrentStake()));

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        ui.displayCurrentStatus(currentGambler, currentSession);

        if (!currentSession.canContinue()) {
            endSession();
        } else {
            ui.pressEnterToContinue();
        }
    }

    private void pauseSession() {
        if (currentSession == null) {
            ui.displayError("No active session.");
            return;
        }

        // Use Case 4: Pause session
        currentSession.pause();
        ui.displayMessage("Session paused. Select any option to resume.");
        ui.pressEnterToContinue();

        // Use Case 4: Resume session
        currentSession.resume();
        ui.displayMessage("Session resumed.");
    }

    private void endSession() {
        if (currentSession == null) {
            ui.displayError("No active session to end.");
            return;
        }

        // Use Case 4: End session
        currentSession.endManually();

        // Use Case 7: Display session summary
        ui.displaySessionSummary(currentSession, winLossCalculator);

        // Use Case 2: Display transaction history summary
        System.out.println("  TRANSACTION HISTORY:");
        List<StakeTransaction> transactions = stakeManager.getTransactionHistory();
        System.out.println(String.format("  Total Transactions: %d", transactions.size()));
        System.out.println(String.format("  Final Balance:      $%,.2f",
                stakeManager.getCurrentStake()));
        System.out.println();

        // Reset for new session
        currentSession = null;
        currentGambler = null;
        stakeManager = null;
        bettingEngine = null;
        winLossCalculator = null;

        ui.pressEnterToContinue();
    }

    public static void main(String[] args) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║        COMPLETE GAMBLING SIMULATION SYSTEM                ║");
        System.out.println("║                                                           ║");
        System.out.println("║  Integrating All 7 Use Cases:                             ║");
        System.out.println("║  1. Gambler Profile Management                            ║");
        System.out.println("║  2. Stake Management Operations                           ║");
        System.out.println("║  3. Betting Mechanism                                     ║");
        System.out.println("║  4. Game Session Management                               ║");
        System.out.println("║  5. Win/Loss Calculation                                  ║");
        System.out.println("║  6. Input Validation and Error Handling                   ║");
        System.out.println("║  7. User Interaction                                      ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        GamblingSimulatorComplete simulator = new GamblingSimulatorComplete();
        simulator.run();
    }
}