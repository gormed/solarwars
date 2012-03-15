/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import gamestates.GamestateManager;
import gamestates.lib.Singelplayer;
import logic.ActionLib;
import logic.Gameplay;
import net.NetworkManager;

/**
 *
 * @author Hans
 */
public class SolarWarsGame {

    private static SolarWarsGame instance;

    private SolarWarsGame() {
    }

    public static SolarWarsGame getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new SolarWarsGame();
    }
    private SolarWarsApplication application;

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
    

    public void initialize(SolarWarsApplication app) {
        application = app;
        assetManager = app.getAssetManager();
        isoControl = app.getIsoControl();
        gamestateManager = GamestateManager.getInstance();
        networkManager = NetworkManager.getInstance();
        
        actionLib = ActionLib.getInstance();
        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();
        

        Gameplay.initialize();
    }

    public void start() {
        Singelplayer s = new Singelplayer(this);
        gamestateManager.initialize(s);
        gamestateManager.start();
    }

    public void pause() {
        gamestateManager.pause();
    }

    public void resume() {
        gamestateManager.resume();
    }

    public void reset() {
        gamestateManager.reset();
    }

    public void terminate() {
        gamestateManager.terminate();
    }

    void update(float tpf) {
        gamestateManager.update(tpf);
    }
}
