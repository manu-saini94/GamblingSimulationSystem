/**
 * Defines all supported stake transaction types.
 * Categories:
 * - INITIAL_STAKE : Initial funding transaction.
 * - BET_PLACED : Bet amount reserved.
 * - BET_WIN : Winning payout transaction.
 * - BET_LOSS : Losing bet deduction.
 * - DEPOSIT : External fund addition.
 * - WITHDRAWAL : External fund removal.
 * - ADJUSTMENT : Manual correction.
 * - RESET : Session reset transaction.
 */
public enum TransactionType {
    INITIAL_STAKE,
    BET_PLACED,
    BET_WIN,
    BET_LOSS,
    DEPOSIT,
    WITHDRAWAL,
    ADJUSTMENT,
    RESET
}
