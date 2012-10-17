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
 * File: AbstractGameplay.java
 * Type: com.solarwars.logic.AbstractGameplay
 * 
 * Documentation created: 17.08.2012 - 19:51:15 by Hans
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;

/**
 * The class AbstractGameplay.
 *
 * @author Hans Ferchland
 */
public abstract class AbstractGameplay {
    public static float[] PLANET_SIZES = {0.2F, 0.225F, 0.25F, 0.275F, 0.3F, 0.325F, 0.35F, 0.375F, 0.4F, 0.425F};
    public static float[] PLANET_INCREMENT_TIME = {2.0F, 1.5F, 1.2F, 1.0F, 0.9F, 0.8F, 0.7F, 0.6F, 0.5F, 0.5F};
    public static final int PLAYER_START_SHIP_COUNT = 100;

    /**
     * The constructor of AbstractGameplay.
     */
    public AbstractGameplay() {
        actionLib = ActionLib.getInstance();
        game = SolarWarsGame.getInstance();
        application = game.getApplication();
    }

    //==========================================================================
    //      Static Methods
    //==========================================================================
    /**
     * Gets the current game tick calculated for each client.
     *
     * @see MultiplayerGameplay
     * @return the game tick as a double in millisecs
     */
    public static double getGameTick() {
        return GAMETICK;
    }
    //==========================================================================
    //      Static Fields
    //==========================================================================
    public static double GAMETICK = 0;
    public static float PLANET_REFRESH_MULTIPILER = 1;
    public static float SHIP_SPEED = 1;
    public static float PLANET_RANGE = 1.5f;
    public static boolean RANGE_ENABLED = true;
    //==========================================================================
    //      Protected Fields
    //==========================================================================
    protected SolarWarsGame game;
    protected SolarWarsApplication application;
    protected ActionLib actionLib;
    protected boolean initialized = false;
    protected Level currentLevel = null;
    // ==========================================================================
    //      Methods
    //==========================================================================

    /**
     * Has to get overridden by subclasses to create own actions for ships to
     * enable diffrent gameplay settings.
     */
    protected abstract void createGameplay();

    /**
     * Updates the defined gameplay of the subclass.
     *
     * @param tpf the time per frame
     */
    public abstract void update(float tpf);

    /**
     * Initializes the gameplay with a given level and reversed. Creates the
     * gameplay by excuting the createGameplay() method.
     *
     * @param level (will be the currentLevel)
     */
    public void initialize(Level level) {
        if (initialized) {
            return;
        }
        actionLib.initialize();
        currentLevel = level;
        currentLevel.initGameplay(this);
        currentLevel.resetEntityIDs();
        createGameplay();
        initialized = true;
    }
    
    /**
     * Destroys the gameplay if not needed anymore.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }
        actionLib.destroy();
        actionLib = null;
        currentLevel = null;
        initialized = false;
    }

    /**
     * Checks if the gameplay is already init.
     *
     * @return true if so, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Gets the current gameplay if already initialized. Throws a
     * GameplayException otherwise.
     *
     * @return the current level given on init
     * @throws GameplayException
     * @see GameplayException
     */
    public Level getCurrentLevel() throws GameplayException {
        if (isInitialized()) {
            return currentLevel;
        } else {
            throw new GameplayException("Gameplay wasn't initilized "
                    + "with a level! - No Level");
        }
    }
}
