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
    private ArrayList<AbstractPlanet> planetList;
    private AssetManager assetManager;
    private IsoControl control;

    public Node getLevelNode() {
        return levelNode;
    }

    public Level(Node rootNode, AssetManager assetManager, IsoControl control) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.planetList = new ArrayList<AbstractPlanet>();
        this.control = control;
    }

    public void generateLevel(long seed) {
        levelNode = new Node("LevelNode");

        AbstractPlanet p;

        Random r = new Random(seed);


        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (r.nextBoolean()) {
                    p = new BasePlanet(this, new Vector3f(-9 + i, 0, -9 + j), (0.3f + r.nextFloat()) / 3);
                    p.createPlanet(assetManager);
                    planetList.add(p);
                    //control.addShootable(p.getGeometry());
                }
            }
        }
        control.addShootable(levelNode);
        //rootNode.attachChild(control.getShootablesNode());
    }
}
