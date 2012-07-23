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
 * File: ShipBatchManager.java
 * Type: entities.ShipBatchManager
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import java.util.ArrayList;
import java.util.Map;
import jme3tools.optimize.GeometryBatchFactory;
import logic.Player;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class ShipBatchManager.
 *
 * @author Hans
 */
public class ShipBatchManager {

    /** The instance. */
    private static ShipBatchManager instance;
    /** The asset manager. */
    private static AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();

    /**
     * Gets the single instance of ShipBatchManager.
     *
     * @return single instance of ShipBatchManager
     */
    public static ShipBatchManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new ShipBatchManager();
    }
    /** The update timer. */
    private float updateTimer;

    /**
     * Instantiates a new ship batch manager.
     */
    private ShipBatchManager() {
    }

    /**
     * Initializes the.
     *
     * @param shipCount the ship count
     */
    public void initialize(int shipCount) {
        usedBatches.clear();
        unusedBatches.clear();
        currentShipCount = shipCount;
        desiredShipCount = shipCount;
        unusedBatches.ensureCapacity((int) (desiredShipCount * 1.5f));
        usedBatches.ensureCapacity((int) (desiredShipCount * 1.5f));
        for (int i = 0; i < shipCount; i++) {
            unusedBatches.add(createNextBatch());
        }
    }

    /**
     * Creates the next batch.
     *
     * @return the spatial
     */
    private Spatial createNextBatch() {
        Node shipBatchGeometry = new Node("ShipBatch");
        Vector3f v1 = new Vector3f(0, 0, -0.05f);
        Vector3f v2 = new Vector3f(0.125f, 0, 0);
        Vector3f v3 = new Vector3f(0, 0, 0.05f);

        Line l1 = new Line(v1, v2);
        Line l2 = new Line(v2, v3);
        Line l3 = new Line(v3, v1);

        Geometry line0 = new Geometry("Line1", l1);
        Geometry line1 = new Geometry("Line2", l2);
        Geometry line2 = new Geometry("Line3", l3);


        shipBatchGeometry.attachChild(line0);
        shipBatchGeometry.attachChild(line1);
        shipBatchGeometry.attachChild(line2);
        Spatial shipBatchSpatial = GeometryBatchFactory.optimize(shipBatchGeometry);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        shipBatchSpatial.setMaterial(mat);

        return shipBatchSpatial;
    }
    /** The used batches. */
    private ArrayList<Spatial> usedBatches = new ArrayList<Spatial>();
    /** The unused batches. */
    private ArrayList<Spatial> unusedBatches = new ArrayList<Spatial>();
    /** The current ship count. */
    private int currentShipCount;
    /** The desired ship count. */
    private int desiredShipCount;

    /**
     * Gets the ship batch.
     *
     * @return the ship batch
     */
    Spatial getShipBatch() {
        if (unusedBatches.isEmpty()) {
            Spatial s = createNextBatch();
            usedBatches.add(s);
            System.out.println("Created new Batch");
            System.out.println("Used: " + usedBatches.size() + " | Unused: " + unusedBatches.size());
            return s;
        }
        Spatial s = unusedBatches.get(unusedBatches.size() - 1);
        unusedBatches.remove(s);
        usedBatches.add(s);

//        System.out.println("Activated unused Batch");
//        System.out.println("Used: " + usedBatches.size() + " | Unused: " + unusedBatches.size());
        return s;
    }

    /**
     * Free ship batch.
     *
     * @param b the b
     */
    void freeShipBatch(Spatial b) {
        unusedBatches.add(b);
        usedBatches.remove(b);

//        System.out.println("Freed Active Batch");
//        System.out.println("Used: " + usedBatches.size() + " | Unused: " + unusedBatches.size());
    }

    /**
     * Refresh batch size.
     *
     * @param tpf the tpf
     */
    public void refreshBatchSize(float tpf) {

        updateTimer += tpf;
        if (updateTimer > 0.16f) {

            int globalShips = 0;
            for (Map.Entry<Integer, Player> entry : Hub.playersByID.entrySet()) {
                globalShips += entry.getValue().getShipCount();
            }

            desiredShipCount = globalShips;

            unusedBatches.ensureCapacity((int) (desiredShipCount * 2f));
            usedBatches.ensureCapacity((int) (desiredShipCount * 2f));
            if (currentShipCount < desiredShipCount) {
                int step = 10;
                for (int i = 0; i < step; i++) {
                    unusedBatches.add(createNextBatch());
                }
                currentShipCount++;
            }
            updateTimer = 0;
        }
    }
}
