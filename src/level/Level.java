/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.AbstractPlanet;
import entities.BasePlanet;
import java.util.ArrayList;
import java.util.Random;
import solarwars.IsoControl;

/**
 *
 * @author Hans
 */
public class Level {

    private Node rootNode;
    private Node levelNode;
    private Node labelNode;

    private ArrayList<AbstractPlanet> planetList;
    private AssetManager assetManager;
    private IsoControl control;

    public Node getLevelNode() {
        return levelNode;
    }

    public Node getLabelNode() {
        return labelNode;
    }
    
    public Level(Node rootNode, AssetManager assetManager, IsoControl control) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.planetList = new ArrayList<AbstractPlanet>();
        this.control = control;
        this.labelNode = new Node("Planet Labels");
        
        this.rootNode.attachChild(labelNode);
    }

    public void generateLevel(long seed) {
        levelNode = new Node("LevelNode");

        AbstractPlanet p;

        Random r = new Random(seed);


        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (r.nextBoolean()) {
                    p = new BasePlanet(assetManager, this, new Vector3f(-9 + i, 0, -9 + j), generateSize(r));
                    p.createPlanet();
                    planetList.add(p);
                    //control.addShootable(p.getGeometry());
                }
            }
        }
        control.addShootable(levelNode);
        //rootNode.attachChild(control.getShootablesNode());
    }
    
    private float generateSize(Random r) {
        return (0.8f + r.nextFloat()) / 4;
    }
    
    public void updateLevel(float tpf) {
        for (AbstractPlanet p : planetList) {
            p.updateLabel();
        }
    }
}
