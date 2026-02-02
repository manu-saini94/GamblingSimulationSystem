import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages multiple gaming sessions across gamblers.
 * Responsibilities:
 * - Session lifecycle orchestration
 * - Prevent duplicate active sessions
 * - Maintain active and completed session tracking
 * - Provide session retrieval and reporting
 */
public class GameSessionManager {
    private Map<String, GamingSession> activeSessions;
    private List<GamingSession> completedSessions;

    /**
     * Initializes session manager.
     */
    public GameSessionManager() {
        this.activeSessions = new HashMap<>();
        this.completedSessions = new ArrayList<>();
    }

    /**
     * Starts new gaming session.
     *
     * @param gamblerId Gambler identifier.
     * @param parameters Session configuration.
     * @return Created GamingSession.
     * @throws Exception If gambler already has active session.
     */
    public GamingSession startNewSession(String gamblerId, SessionParameters parameters) throws Exception {
        if (activeSessions.containsKey(gamblerId)) {
            throw new Exception("Gambler already has an active session. End or pause it first.");
        }

        GamingSession session = new GamingSession(gamblerId, parameters);
        session.start();
        activeSessions.put(gamblerId, session);

        return session;
    }

    /**
     * Continues session by playing multiple games.
     * @param gamblerId Gambler identifier.
     * @param numberOfGames Number of games to play.
     * @param betAmount Bet amount per game.
     * @throws Exception If session invalid.
     */
    public void continueSession(String gamblerId, int numberOfGames, double betAmount) throws Exception {
        GamingSession session = getActiveSession(gamblerId);

        System.out.println(String.format("\n--- Continuing session with %d games ---", numberOfGames));

        int gamesCompleted = 0;
        for (int i = 0; i < numberOfGames; i++) {
            if (session.isEnded()) {
                System.out.println("Session ended during gameplay");
                break;
            }

            try {
                session.playGame(betAmount, session.parameters.getDefaultWinProbability());
                gamesCompleted++;

                // Small delay for readability
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("Cannot continue: " + e.getMessage());
                break;
            }
        }

        System.out.println(String.format("Completed %d out of %d games",
                gamesCompleted, numberOfGames));

        // Move to completed if ended
        if (session.isEnded()) {
            completedSessions.add(session);
            activeSessions.remove(gamblerId);
        }
    }

    /**
     *
     * Pauses active session.
     * @param gamblerId Gambler identifier.
     * @param reason Pause reason.
     * @throws Exception If session not found.
     */
    public void pauseSession(String gamblerId, String reason) throws Exception {
        GamingSession session = getActiveSession(gamblerId);
        session.pause(reason);
    }

    /**
     * Resumes paused session.
     * @param gamblerId Gambler identifier.
     * @throws Exception If session not found or invalid state.
     */
    public void resumeSession(String gamblerId) throws Exception {
        GamingSession session = activeSessions.get(gamblerId);
        if (session == null) {
            throw new Exception("No session found for gambler");
        }
        session.resume();
    }

    /**
     *
     * Ends session manually.
     * @param gamblerId Gambler identifier.
     * @return Completed GamingSession.
     * @throws Exception If session not found.
     */
    public GamingSession endSession(String gamblerId) throws Exception {
        GamingSession session = activeSessions.remove(gamblerId);
        if (session == null) {
            throw new Exception("No active session found");
        }

        session.endManually();
        completedSessions.add(session);

        return session;
    }

    /**
     * Retrieves active session.
     * @param gamblerId Gambler identifier.
     * @return Active GamingSession.
     * @throws Exception If session not found.
     */
    private GamingSession getActiveSession(String gamblerId) throws Exception {
        GamingSession session = activeSessions.get(gamblerId);
        if (session == null) {
            throw new Exception("No active session found for gambler");
        }
        return session;
    }

    // Get session info
    public GamingSession getSession(String gamblerId) {
        return activeSessions.get(gamblerId);
    }

    public List<GamingSession> getCompletedSessions() {
        return new ArrayList<>(completedSessions);
    }

    public Map<String, GamingSession> getActiveSessions() {
        return new HashMap<>(activeSessions);
    }
}
