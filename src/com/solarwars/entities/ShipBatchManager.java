/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: ShipBatchManager.java
 * Type: com.solarwars.entities.ShipBatchManager
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * The Class ShipBatchManager creates and handles all ships displayed in game.
 *
 * It is resposible for the creation of ships, gives an instance to a logical
 * ship, that needs to display. To keep aware of the global ship count it
 * creates new batches if the players create more and more ships due their
 * planets.
 *
 * @author Hans Ferchland
 */
public class ShipBatchManager {

    /**
     * The asset manager.
     */
    private AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
    private Node shipBatchNode = new Node("ShipBatchNode");
    /**
     * The update timer.
     */
    private float updateTimer;
    private static final Logger logger = Logger.getLogger(ShipBatchManager.class.getName());
    private Vector3f outOfScreen = new Vector3f(0, 0, 100);
    /**
     * The used batches.
     */
    private HashMap<Integer, ArrayList<Spatial>> usedPlayerBatches =
            new HashMap<Integer, ArrayList<Spatial>>();
    /**
     * The unused batches.
     */
    private HashMap<Integer, ArrayList<Spatial>> unusedPlayerBatches =
            new HashMap<Integer, ArrayList<Spatial>>();
    /**
     * The current ship count.
     */
    private HashMap<Integer, Integer> currentShipCount = new HashMap<Integer, Integer>();
    /**
     * The desired ship count.
     */
    private HashMap<Integer, Integer> desiredShipCount = new HashMap<Integer, Integer>();
    private HashMap<Integer, Material> playerMaterialsHashMap =
            new HashMap<Integer, Material>();

    /**
     * Instantiates a new ship batch manager.
     */
    public ShipBatchManager(Level level) {
        level.getLevelNode().attachChild(shipBatchNode);
    }

    /**
     * Initializes the.
     *
     * @param shipCount the ship count
     */
    public void initialize(ArrayList<Player> players) {
        usedPlayerBatches.clear();
        unusedPlayerBatches.clear();
        for (Player p : players) {
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", p.getColor());
            material.setColor("GlowColor", p.getColor());
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            playerMaterialsHashMap.put(p.getID(), material);
        }
        initDatastructure(players);
        initBatchLists();
    }

    private void initBatchLists() {

        for (Map.Entry<Integer, ArrayList<Spatial>> entry :
                unusedPlayerBatches.entrySet()) {
            ArrayList<Spatial> playersBatches = entry.getValue();
            int playerID = entry.getKey();
            for (int i = 0; i < desiredShipCount.get(playerID); i++) {
                Spatial s = createNextBatch(playerID);
//                s.setLocalTranslation(outOfScreen);
                s.setCullHint(Spatial.CullHint.Always);
                playersBatches.add(s);
            }
            logger.log(java.util.logging.Level.INFO,
                    "Created {0} Ship-Batches for player id#{1}!",
                    new Object[]{desiredShipCount.get(playerID), playerID});
        }
    }

    private void initDatastructure(ArrayList<Player> players) {
        for (Player p : players) {
            currentShipCount.put(p.getID(), p.getShipCount());
            desiredShipCount.put(p.getID(), p.getShipCount());

            unusedPlayerBatches.put(p.getID(),
                    new ArrayList<Spatial>((int) (desiredShipCount.get(p.getID()) * 1.5f)));
            usedPlayerBatches.put(p.getID(),
                    new ArrayList<Spatial>((int) (desiredShipCount.get(p.getID()) * 1.5f)));
        }
    }

    public void destroy() {
        usedPlayerBatches.clear();
        unusedPlayerBatches.clear();
    }

    /**
     * Creates the next batch.
     *
     * @return the spatial
     */
    private Spatial createNextBatch(int playerID) {
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
        Spatial shipBatchSpatial =
                GeometryBatchFactory.optimize(shipBatchGeometry);
        Material material = playerMaterialsHashMap.get(playerID);

        shipBatchSpatial.setMaterial(material);

        shipBatchNode.attachChild(shipBatchSpatial);

        return shipBatchSpatial;
    }

    /**
     * Gets the ship batch.
     *
     * @return the ship batch
     */
    Spatial getShipBatch(Player p) {
        int playerID = p.getID();
        if (unusedPlayerBatches.containsKey(playerID)
                && usedPlayerBatches.containsKey(playerID)) {
            if (unusedPlayerBatches.get(playerID).isEmpty()) {
                Spatial s = createNextBatch(playerID);
                s.setCullHint(Spatial.CullHint.Dynamic);
                usedPlayerBatches.get(playerID).add(s);
                logger.log(java.util.logging.Level.INFO, "Used: {0} | Unused: {1}",
                        new Object[]{usedPlayerBatches.get(playerID).size(),
                            unusedPlayerBatches.get(playerID).size()});
                logger.warning("Created Ship-Batch at needs. Not that good!");
                return s;
            } else {
                Spatial s = unusedPlayerBatches.get(playerID).
                        get(unusedPlayerBatches.get(playerID).size() - 1);
                s.setCullHint(Spatial.CullHint.Dynamic);
                unusedPlayerBatches.get(playerID).remove(s);
                usedPlayerBatches.get(playerID).add(s);
                logger.info("Aquired Ship-Batch at needs. Perfect!");
                logger.log(java.util.logging.Level.INFO, "Used: {0} | Unused: {1}",
                        new Object[]{usedPlayerBatches.get(playerID).size(),
                            unusedPlayerBatches.get(playerID).size()});
                return s;
            }
        }
        return null;
    }

    /**
     * Free ship batch.
     *
     * @param b the b
     */
    void freeShipBatch(Player p, Spatial s) {
        unusedPlayerBatches.get(p.getID()).add(s);
        usedPlayerBatches.get(p.getID()).remove(s);
//        s.setCullHint(CullHint.Always);
        s.setLocalTranslation(outOfScreen);
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
        if (updateTimer > 1f) {

            int playerShips = 0;
            for (Map.Entry<Integer, Player> entry : Hub.playersByID.entrySet()) {

                int playerID = entry.getValue().getID();
                if (!unusedPlayerBatches.containsKey(playerID)
                        || !usedPlayerBatches.containsKey(playerID)) {
                    continue;
                }

                playerShips = entry.getValue().getShipCount();
                desiredShipCount.remove(playerID);
                desiredShipCount.put(playerID, playerShips);

                unusedPlayerBatches.get(playerID).ensureCapacity((int) (playerShips * 2f));
                usedPlayerBatches.get(playerID).ensureCapacity((int) (playerShips * 2f));
                if (currentShipCount.get(playerID) < desiredShipCount.get(playerID)) {
                    int step = 10 * Hub.playersByID.size();
                    logger.log(java.util.logging.Level.INFO,
                            "Creating {0} ShipBatches to reach desired count of {1} from current count of {2}.",
                            new Object[]{step, desiredShipCount.get(playerID),
                                currentShipCount.get(playerID)});
                    Spatial s;
                    for (int i = 0; i < step; i++) {
                        s = createNextBatch(playerID);
//                        s.setLocalTranslation(outOfScreen);
                        s.setCullHint(Spatial.CullHint.Always);
                        unusedPlayerBatches.get(playerID).add(s);
                        int temp = currentShipCount.get(playerID);
                        currentShipCount.remove(playerID);
                        currentShipCount.put(playerID, ++temp);
                    }

                }
            }


            updateTimer = 0;
        }
    }
}
