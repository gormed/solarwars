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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: LevelBackground.java
 * Type: logic.level.LevelBackground
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import solarwars.SolarWarsGame;

/**
 * The Class LevelBackground.
 */
public class LevelBackground extends Node {

    /** The Constant WIDTH. */
    public static final float WIDTH = 20;
    
    /** The Constant HEIGHT. */
    public static final float HEIGHT = 20;
    
    /** The geometry. */
    private Geometry geometry;
    
    /** The material. */
    private Material material;
    
    /** The game. */
    private SolarWarsGame game;

    /**
     * Instantiates a new level background.
     *
     * @param game the game
     */
    public LevelBackground(SolarWarsGame game) {
        super("LevelBackground");
        this.game = game;
        createBG();
    }

    /**
     * Creates the bg.
     */
    private void createBG() {
        AssetManager assetManager = game.getApplication().getAssetManager();
        
        Quad q = new Quad(WIDTH, HEIGHT);
        geometry = new Geometry("BackgroundGeometry", q);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Enviorment/starmap_1.jpg"));


        geometry.setMaterial(material);

        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        geometry.setLocalRotation(new Quaternion(angles));
        geometry.setLocalTranslation(-WIDTH/2, -2, -HEIGHT/2);
        
        attachChild(geometry);
    }
}
