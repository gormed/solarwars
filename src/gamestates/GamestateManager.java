/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or application 
 * in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: SolarWars
 * File: GamestateManager.java
 * Type: gamestates.GamestateManager
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import solarwars.SolarWarsApplication;

/**
 * The Class GamestateManager.
 */
public class GamestateManager {

    /** The Constant SINGLEPLAYER_STATE. */
    public static final String SINGLEPLAYER_STATE = "Singleplayer";
    /** The Constant MULTIPLAYER_STATE. */
    public static final String MULTIPLAYER_STATE = "Multiplayer";
    /** The Constant MAINMENU_STATE. */
    public static final String MAINMENU_STATE = "Mainmenu";
    /** The Constant OPTIONS_STATE. */
    public static final String OPTIONS_STATE = "Options";
    /** The Constant CREATE_SERVER_STATE. */
    public static final String CREATE_SERVER_STATE = "Create Server";
    /** The Constant SERVER_LOBBY_STATE. */
    public static final String SERVER_LOBBY_STATE = "Server Lobby";
    /** The Constant MULTIPLAYER_MATCH_STATE. */
    public static final String MULTIPLAYER_MATCH_STATE = "Multiplayer Match";
    /** The Constant TUTORIAL_STATE. */
    public static final String TUTORIAL_STATE = "Tutorial";
    /** The instance. */
    private static GamestateManager instance;
    /** The gamestates. */
    private HashMap<String, Gamestate> gamestates;
    /** The current state. */
    private Gamestate currentState;
    /** The next state. */
    private Gamestate nextState;
    private static final Logger logger = Logger.getLogger(GamestateManager.class.getName());
    /** The lock update. */
    private static volatile boolean lockUpdate = false;

    /**
     * Checks if is locked.
     *
     * @return true, if is locked
     */
    public static synchronized boolean isLocked() {
        return lockUpdate;
    }

    /**
     * Lock.
     */
    static synchronized void lock() {
        lockUpdate = true;
    }

    /**
     * Unlock.
     */
    static synchronized void unlock() {
        lockUpdate = false;
    }

    /**
     * Instantiates a new gamestate manager.
     */
    private GamestateManager() {
        logger.setLevel(SolarWarsApplication.GLOBAL_LOGGING_LEVEL);
        logger.setUseParentHandlers(true);
        logger.setParent(SolarWarsApplication.getClientLogger());
        gamestates = new HashMap<String, Gamestate>();
    }

    /**
     * Gets the single instance of GamestateManager.
     *
     * @return single instance of GamestateManager
     */
    public static GamestateManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new GamestateManager();
    }

    /**
     * Initializes the.
     *
     * @param startState the start state
     */
    public void initialize(Gamestate startState) {
        addState(startState);
        currentState = startState;
        final String initMsg = "GamestateManager initialized with start-state " + currentState.getName();
        logger.info(initMsg);
    }

    /**
     * Start.
     */
    public void start() {
        currentState.enter();
        final String startMsg = "GamestateManager started, entering " + currentState.getName() + "...";
        logger.info(startMsg);
    }

    /**
     * Enter state.
     *
     * @param nextState the next state
     */
    public synchronized void enterState(String nextState) {
        lock();
        if (gamestates.containsKey(nextState)) {
            this.nextState = gamestates.get(nextState);

            this.currentState.leave();
            final String leaveStateMsg = "Gamestate: " + currentState.getName() + " left!";
            logger.info(leaveStateMsg);
            this.currentState = null;
            this.nextState.enter();
            final String enterStateMsg = "Gamestate: " + nextState + " enterd!";
            logger.info(enterStateMsg);
            this.currentState = this.nextState;
        }
        unlock();
    }

    /**
     * Adds the state.
     *
     * @param g the g
     */
    public void addState(Gamestate g) {
        gamestates.put(g.getName(), g);
        final String gsAddedMsg = "Gamestate: " + g.getName() + " added!";
        logger.info(gsAddedMsg);
    }

    /**
     * Removes the state.
     *
     * @param g the g
     */
    public void removeState(Gamestate g) {
        gamestates.remove(g.getName());
        final String gsremovedMsg = "Gamestate: " + g.getName() + " removed!";
        logger.info(gsremovedMsg);
    }

    /**
     * Gets the gamestate.
     *
     * @param name the name
     * @return the gamestate
     */
    public Gamestate getGamestate(String name) {
        if (!gamestates.containsKey(name)) {
            return null;
        }
        return gamestates.get(name);
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (currentState != null && !isLocked()) {
            currentState.update(tpf);
        }
    }

    /**
     * Pause.
     */
    public void pause() {
        if (currentState != null) {
            currentState.pause();
        }
    }

    /**
     * Resume.
     */
    public void resume() {
        if (currentState != null) {
            currentState.resume();
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        if (currentState != null) {
            currentState.reset();
        }
    }

    /**
     * Terminate.
     */
    public void terminate() {
        if (currentState != null) {
            currentState.terminate();
        }
    }
}
