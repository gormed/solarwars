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

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.solarwars.controls.AbstractControl;
import com.solarwars.gamestates.CreateServerState;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.gamestates.MainmenuState;
import com.solarwars.gamestates.MultiplayerMatchState;
import com.solarwars.gamestates.MultiplayerState;
import com.solarwars.gamestates.OptionsState;
import com.solarwars.gamestates.ServerLobbyState;
import com.solarwars.gamestates.SingleplayerMatchState;
import com.solarwars.gamestates.TutorialState;
import com.solarwars.input.KeyInputManager;
import com.solarwars.logic.AbstractGameplay;
import com.solarwars.logic.ActionLib;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.GameplayException;
import com.solarwars.logic.Level;
import com.solarwars.net.NetworkManager;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The Class SolarWarsGame.
 */
public class SolarWarsGame {

    //==========================================================================
    //      Gamestates
    //==========================================================================
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
    //==========================================================================
    //      Singleton
    //==========================================================================
    private static SolarWarsGame instance;

    /**
     * Instantiates a new solar wars game.
     */
    private SolarWarsGame() {
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
    //==========================================================================
    //      Fields
    //==========================================================================
    /** The application. */
    private SolarWarsApplication application;
    private AssetManager assetManager;
    private NetworkManager networkManager;
    private AbstractControl control;
    private InputManager inputManager;
    private FontLoader fontLoader;
    private ActionLib actionLib;
    private AudioManager audioManager;
    private KeyInputManager keyInputManager;
    private AppStateManager stateManager;
    private HashMap<String, Gamestate> gamestates = new HashMap<String, Gamestate>();
    private static AbstractGameplay currentGameplay;
    private static final Logger logger = Logger.getLogger(SolarWarsGame.class.getName());
    //==========================================================================
    //      Methods
    //==========================================================================

    /**
     * Initializes the.
     *
     * @param app the app
     */
    public void initialize(SolarWarsApplication app) {
        application = app;

        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();

        control = app.getControl();

        audioManager = AudioManager.getInstance();
        audioManager.initialize();

        networkManager = NetworkManager.getInstance();

        actionLib = ActionLib.getInstance();
        currentGameplay = new DeathmatchGameplay();

        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();
        keyInputManager = KeyInputManager.getInstance();

        logger.info("SolarWarsGame initialized!");

    }

    /**
     * Starts the game, creates all gamestates and enters the first state.
     */
    public void start() {
        MainmenuState mainmenu = new MainmenuState();
        SingleplayerMatchState singleplayer = new SingleplayerMatchState();
        MultiplayerState multiplayer = new MultiplayerState();
        CreateServerState createServerState = new CreateServerState();
        ServerLobbyState serverLobbyState = new ServerLobbyState();
        MultiplayerMatchState multiplayerMatchState = new MultiplayerMatchState();
        TutorialState tutorialState = new TutorialState();
        OptionsState optionsState = new OptionsState();

//        mainmenu.initialize(stateManager, application);
//        singleplayer.initialize(stateManager, application);
//        multiplayer.initialize(stateManager, application);
//        createServerState.initialize(stateManager, application);
//        serverLobbyState.initialize(stateManager, application);
//        multiplayerMatchState.initialize(stateManager, application);
//        tutorialState.initialize(stateManager, application);
//        optionsState.initialize(stateManager, application);

        stateManager.attach(mainmenu);
        stateManager.attach(singleplayer);
        stateManager.attach(createServerState);
        stateManager.attach(multiplayerMatchState);
        stateManager.attach(multiplayer);
        stateManager.attach(optionsState);
        stateManager.attach(serverLobbyState);
        stateManager.attach(tutorialState);

        gamestates.put(MAINMENU_STATE, mainmenu);
        gamestates.put(SINGLEPLAYER_STATE, singleplayer);
        gamestates.put(MULTIPLAYER_STATE, multiplayer);
        gamestates.put(CREATE_SERVER_STATE, createServerState);
        gamestates.put(SERVER_LOBBY_STATE, serverLobbyState);
        gamestates.put(MULTIPLAYER_MATCH_STATE, multiplayerMatchState);
        gamestates.put(TUTORIAL_STATE, tutorialState);
        gamestates.put(OPTIONS_STATE, optionsState);

//        mainmenu.setEnabled(true);

//        application.getNiftyGUI().fromXml("Interface/Nifty/MainNifty.xml", "startup", 
//                mainmenu, singleplayer, multiplayer, createServerState,
//                serverLobbyState, multiplayerMatchState, tutorialState,
//                optionsState);
        
        application.getNiftyGUI().
                registerScreenController(
                mainmenu, singleplayer, multiplayer, createServerState,
                serverLobbyState, multiplayerMatchState, tutorialState,
                optionsState);

        application.getNiftyGUI().addXml("Interface/Nifty/NiftyClientGUI.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/NiftyPopups.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/MainMenuState.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/MultiplayerState.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/SingleplayerGUI.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/CreateServerState.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/ServerLobbyState.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/MultiplayerGUI.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/TutorialState.xml");
        application.getNiftyGUI().addXml("Interface/Nifty/OptionsState.xml");

        application.getNiftyGUI().gotoScreen("startup");

//        attachGamestate(MAINMENU_STATE);
        triggerGamestate(MAINMENU_STATE, true);

        logger.info("SolarWarsGame started!");
    }

    /**
     * Pause.
     */
    public void pause() {
//        gamestateManager.pause();
        logger.info("SolarWarsGame paused!");
    }

    /**
     * Resume.
     */
    public void resume() {
//        gamestateManager.resume();
        logger.info("SolarWarsGame resumed!");
    }

    /**
     * Reset.
     */
    public void reset() {
//        gamestateManager.reset();
        logger.info("SolarWarsGame reset!");
    }

    /**
     * Terminate.
     */
    public void terminate() {
//        gamestateManager.terminate();
        logger.info("SolarWarsGame terminated!");
    }

    /**
     * @param timePerFrame
     */
    void update(float timePerFrame) {
        
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
            return null;
        }
    }

    /**
     * Triggers a gamestate on or off.
     * @param name the name of the gamestate to trigger on/off
     * @param value the value for enable/disable, on = true & off = false
     * @return true if everything went okay, false if gamestate wasnt found 
     * in the SolarWarsGame.gamestates HashMap or in the stateManagers list
     */
    public boolean triggerGamestate(String name, boolean value) {
        Gamestate gs = gamestates.get(name);
        if (gs != null && stateManager.hasState(gs)) {
            gs.setEnabled(value);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the application.
     *
     * @return the application
     */
    public SolarWarsApplication getApplication() {
        return application;
    }
}
