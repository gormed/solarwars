/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.level;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class LevelBackground extends Node {

    public static final float WIDTH = 20;
    public static final float HEIGHT = 20;
    
    private Geometry geometry;
    private Material material;
    private SolarWarsGame game;

    public LevelBackground(SolarWarsGame game) {
        super("LevelBackground");
        this.game = game;
        createBG();
    }

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
