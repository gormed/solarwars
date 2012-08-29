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
 * File: SolarWarsGame.java
 * Type: com.solarwars.SolarWarsGame
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.solarwars.gamestates.GamestateManager;
import com.solarwars.gamestates.lib.CreateServerState;
import com.solarwars.gamestates.lib.MainmenuState;
import com.solarwars.gamestates.lib.MultiplayerMatchState;
import com.solarwars.gamestates.lib.MultiplayerState;
import com.solarwars.gamestates.lib.OptionsState;
import com.solarwars.gamestates.lib.ServerLobbyState;
import com.solarwars.gamestates.lib.SingleplayerState;
import com.solarwars.gamestates.lib.TutorialState;
import com.solarwars.gui.GameGUI;
import com.solarwars.input.KeyInputManager;
import com.solarwars.logic.AbstractGameplay;
import com.solarwars.logic.ActionLib;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.GameplayException;
import com.solarwars.logic.Level;
import com.solarwars.net.NetworkManager;

import java.util.logging.Logger;

/**
 * The Class SolarWarsGame.
 */
public class SolarWarsGame {

    private static SolarWarsGame instance;

    /**
     * Instantiates a new solar wars game.
     */
    private SolarWarsGame() {
        logger.setLevel(SolarWarsApplication.GLOBAL_LOGGING_LEVEL);
        logger.setUseParentHandlers(true);
    }

    /**
     * Gets the single instance of SolarWarsGame.
     *
     * @return single instance of SolarWarsGame
     */
    public static SolarWarsGame getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new SolarWarsGame();
    }
    /** The application. */
    private SolarWarsApplication application;

    /**
     * Gets the application.
     *
     * @return the application
     */
    public SolarWarsApplication getApplication() {
        return application;
    }
    private AssetManager assetManager;
    private GamestateManager gamestateManager;
    private NetworkManager networkManager;
    private IsoControl isoControl;
    private InputManager inputManager;
    private FontLoader fontLoader;
    private ActionLib actionLib;
    private AudioManager audioManager;
    private KeyInputManager keyInputManager;
    private GameGUI gameGUI;
    private static AbstractGameplay currentGameplay;
    private static final Logger logger = Logger.getLogger(SolarWarsGame.class.getName());

    /**
     * Initializes the.
     *
     * @param app the app
     */
    public void initialize(SolarWarsApplication app) {
        application = app;
        assetManager = app.getAssetManager();

        isoControl = IsoControl.getInstance();

        audioManager = AudioManager.getInstance();
        audioManager.initialize();

        gamestateManager = GamestateManager.getInstance();
        networkManager = NetworkManager.getInstance();

        actionLib = ActionLib.getInstance();
        currentGameplay = new DeathmatchGameplay();

        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();
        keyInputManager = KeyInputManager.getInstance();
        
        gameGUI = GameGUI.getInstance();
        logger.info("SolarWarsGame initialized!");

    }

    /**
     * Starts the game, creates all gamestates and enters the first state.
     */
    public void start() {
        MainmenuState m = new MainmenuState(this);
        SingleplayerState sp = new SingleplayerState(this);
        MultiplayerState mp = new MultiplayerState();
        CreateServerState cs = new CreateServerState();
        ServerLobbyState sls = new ServerLobbyState();
        MultiplayerMatchState mms = new MultiplayerMatchState();
        TutorialState ts = new TutorialState();
        OptionsState os = new OptionsState();
        gamestateManager.addState(sp);
        gamestateManager.addState(mp);
        gamestateManager.addState(m);
        gamestateManager.addState(cs);
        gamestateManager.addState(sls);
        gamestateManager.addState(mms);
        gamestateManager.addState(ts);
        gamestateManager.addState(os);
        // init gamestate manager with mainmenu state
        gamestateManager.initialize(m);
        // start the game with the init state
        gamestateManager.start();
        logger.info("SolarWarsGame started!");
    }

    /**
     * Pause.
     */
    public void pause() {
        gamestateManager.pause();
        logger.info("SolarWarsGame paused!");
    }

    /**
     * Resume.
     */
    public void resume() {
        gamestateManager.resume();
        logger.info("SolarWarsGame resumed!");
    }

    /**
     * Reset.
     */
    public void reset() {
        gamestateManager.reset();
        logger.info("SolarWarsGame reset!");
    }

    /**
     * Terminate.
     */
    public void terminate() {
        gamestateManager.terminate();
        logger.info("SolarWarsGame terminated!");
    }

    /**
     * @param timePerFrame
     */
    void update(float timePerFrame) {
        gameGUI.updateGUIElements(timePerFrame);
        gamestateManager.update(timePerFrame);
    }

    public static AbstractGameplay getCurrentGameplay() {
        if (currentGameplay != null) {
            return currentGameplay;
        }
        return new DeathmatchGameplay();
    }

    public void setupGameplay(AbstractGameplay gameplay,
            Level level) {
        currentGameplay = gameplay;
        currentGameplay.initialize(level);
    }

    public Level getCurrentLevel() {
        try {
            return currentGameplay.getCurrentLevel();
        } catch (GameplayException ex) {
            Logger.getLogger(SolarWarsGame.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
            gamestateManager.enterState(GamestateManager.MAINMENU_STATE);
            return null;
        }
    }
}
