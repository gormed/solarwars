/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import gui.GameGUI;
import gui.Percentage;
import logic.level.Level;
import logic.ActionLib;
import logic.Gameplay;
import logic.Player;

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
    
    private Level currentLevel;
    private AssetManager assetManager;
    private IsoControl isoControl;
    private InputManager inputManager;
    private Hub hub;
    private FontLoader fontLoader;
    private ActionLib actionLib;
    
    private GameGUI gui;

    public void initialize(SolarWarsApplication app) {
        application = app;
        assetManager = app.getAssetManager();
        isoControl = app.getIsoControl();
        actionLib = ActionLib.getInstance();
        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();
        hub = Hub.getInstance();
        
        Gameplay.initialize();
    }

    private void setupSingleplayer() {
        Player human = new Player("Human", ColorRGBA.Blue);
        hub.addPlayer(human);
        hub.setLocalPlayer(human);
        
        Player ai = new Player("AI", ColorRGBA.Red);
        hub.addPlayer(ai);
    }
    
    private void setupGUI() {
        gui = new GameGUI(this);
        gui.addGUIElement(new Percentage(gui));
        
    }

    public void load(long seed) {
        currentLevel = new Level(
                application.rootNode, assetManager, isoControl, seed);
    }

    public Level save() {
        return currentLevel;
    }

    public void start() {
        setupSingleplayer();
        setupGUI();
        currentLevel = new Level(
                application.rootNode, assetManager, isoControl);
        currentLevel.generateLevel(System.currentTimeMillis());
        currentLevel.setupPlayers();
        
        //SimpleShip s = new SimpleShip(assetManager, currentLevel, new Vector3f(0, 0, 0), p);
        //s.createShip();
        //currentLevel.addShip(p, s);

    }

    public void pause() {
        
    }

    public void resume() {
    }

    public void reset() {
    }

    public void terminate() {
    }
    
    void update(float tpf) {
        currentLevel.updateLevel(tpf);
        gui.updateGUIElements(tpf);
        
    }
}
