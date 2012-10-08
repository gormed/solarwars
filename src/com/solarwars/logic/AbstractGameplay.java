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
import com.solarwars.logic.actions.PlanetActionListener;
import java.util.HashSet;

/**
 * The class AbstractGameplay.
 * @author Hans Ferchland
 */
public abstract class AbstractGameplay {

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
     * Should be overridden by subclasses to create own actions for ships to
     * enable diffrent gameplay settings.
     */
    protected abstract void createGameplay();

    public abstract void update(float tpf);
    
    /**
     * @param level (will be the currentLevel)
     */
    public void initialize(Level level) {
        if (initialized) {
            return;
        }
        currentLevel = level;
        currentLevel.initGameplay(this);
        currentLevel.resetEntityIDs();
        createGameplay();
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Level getCurrentLevel() throws GameplayException {
        if (isInitialized()) {
            return currentLevel;
        } else {
            throw new GameplayException("Gameplay wasn't initilized "
                    + "with a level! - No Level");
        }
    }
}
