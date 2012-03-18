/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: GamestateManager.java
 * Type: gamestates.GamestateManager
 * 
 * Documentation created: 15.03.2012 - 20:36:19 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates;

import java.util.HashMap;

/**
 * The Class GamestateManager.
 */
public class GamestateManager {
    
    public static final String SINGLEPLAYER_STATE = "Singleplayer";
    public static final String MULTIPLAYER_STATE = "Multiplayer";
    public static final String MAINMENU_STATE = "Mainmenu";
    public static final String OPTIONS_STATE = "Options";

    /** The instance. */
    private static GamestateManager instance;
    
    /** The gamestates. */
    private HashMap<String, Gamestate> gamestates;
    
    /** The current state. */
    private Gamestate currentState;
    
    /** The next state. */
    private Gamestate nextState;

    /**
     * Instantiates a new gamestate manager.
     */
    private GamestateManager() {
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
    }

    /**
     * Start.
     */
    public void start() {
        currentState.enter();
    }

    /**
     * Enter state.
     *
     * @param nextState the next state
     */
    public void enterState(String nextState) {
        if (gamestates.containsKey(nextState)) {
            this.nextState = gamestates.get(nextState);
            this.currentState.leave();
            this.currentState = null;
            this.nextState.enter();
            this.currentState = this.nextState;
            System.out.println("Gamestate: " + nextState + " enterd!");
        }
    }

    /**
     * Adds the state.
     *
     * @param g the g
     */
    public void addState(Gamestate g) {
        gamestates.put(g.getName(), g);
        System.out.println("Gamestate: " + g.getName() + " added!");
    }

    /**
     * Removes the state.
     *
     * @param g the g
     */
    public void removeState(Gamestate g) {
        gamestates.remove(g.getName());
        System.out.println("Gamestate: " + g.getName() + " removed!");
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (currentState != null) {
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
