/**
 * Represents possible outcomes of a game.
 * WIN  → Player wins and receives payout.
 * LOSS → Player loses bet amount.
 * PUSH → Tie / No win no loss (stake returned).
 */
public enum GameOutcome {
    WIN,
    LOSS,
    PUSH
}
