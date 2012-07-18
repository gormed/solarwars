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
 * Type: solarwars.SolarWarsGame
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import gamestates.GamestateManager;
import gamestates.lib.CreateServerState;
import gamestates.lib.MainmenuState;
import gamestates.lib.MultiplayerMatchState;
import gamestates.lib.MultiplayerState;
import gamestates.lib.ServerLobbyState;
import gamestates.lib.SingleplayerState;
import gamestates.lib.TutorialState;
import logic.ActionLib;
import net.NetworkManager;

/**
 * The Class SolarWarsGame.
 */
public class SolarWarsGame {

    /** The instance. */
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
    /** The asset manager. */
    private AssetManager assetManager;
    /** The gamestate manager. */
    private GamestateManager gamestateManager;
    /** The network manager. */
    private NetworkManager networkManager;
    /** The iso control. */
    private IsoControl isoControl;
    /** The input manager. */
    private InputManager inputManager;
    /** The font loader. */
    private FontLoader fontLoader;
    /** The action lib. */
    private ActionLib actionLib;
    /** The audio manager. */
    private AudioManager audioManager;

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
        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();


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
        gamestateManager.addState(sp);
        gamestateManager.addState(mp);
        gamestateManager.addState(m);
        gamestateManager.addState(cs);
        gamestateManager.addState(sls);
        gamestateManager.addState(mms);
        gamestateManager.addState(ts);
        // init gamestate manager with mainmenu state
        gamestateManager.initialize(m);
        // start the game with the init state
        gamestateManager.start();
    }

    /**
     * Pause.
     */
    public void pause() {
        gamestateManager.pause();
    }

    /**
     * Resume.
     */
    public void resume() {
        gamestateManager.resume();
    }

    /**
     * Reset.
     */
    public void reset() {
        gamestateManager.reset();
    }

    /**
     * Terminate.
     */
    public void terminate() {
        gamestateManager.terminate();
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    void update(float tpf) {
        gamestateManager.update(tpf);

    }
}
