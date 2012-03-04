/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import entities.Cross;
import entities.SimpleShip;
import level.Hub;
import level.Level;
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
    private Level currentLevel;
    private AssetManager assetManager;
    private IsoControl isoControl;
    private InputManager inputManager;
    private Hub hub;
    protected FontLoader fontLoader;

    public void initialize(SolarWarsApplication app) {
        application = app;
        assetManager = app.getAssetManager();
        isoControl = app.getIsoControl();
        // Init fonts
        fontLoader = FontLoader.getInstance();
        fontLoader.initialize(assetManager);
        inputManager = app.getInputManager();
        hub = Hub.getInstance();

    }

    private void setupSingleplayer() {
        Player human = new Player("Human", ColorRGBA.Blue);
        hub.addPlayer(human);
        Player ai = new Player("AI", ColorRGBA.Red);
        hub.addPlayer(ai);
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
        currentLevel = new Level(
                application.rootNode, assetManager, isoControl);
        currentLevel.generateLevel(System.currentTimeMillis());
        Player p = Hub.getInstance().getPlayer(0);
        
        
        SimpleShip s = new SimpleShip(assetManager, currentLevel, new Vector3f(0, 0, 0), p);
        s.createShip();
        currentLevel.addShip(p, s);

    }

    public void pause() {
        
    }

    public void resume() {
    }

    public void reset() {
    }

    public void terminate() {
    }
}
