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
 * File: Level.java
 * Type: logic.level.Level
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.level;

import solarwars.Hub;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.AbstractPlanet;
import entities.AbstractShip;
import entities.BasePlanet;
import entities.ShipGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import logic.Player;
import solarwars.IsoControl;

/**
 * The Class Level.
 */
public class Level {

    /** The seed. */
    private long seed = 0;
    /** The root node. */
    private Node rootNode;
    /** The level node. */
    private Node levelNode;
    /** The planet nodes. */
    private HashMap<Player, Node> planetNodes;
    /** The free planets node. */
    private Node freePlanetsNode;
    /** The label node. */
    private Node labelNode;
    /** The background. */
    private LevelBackground background;
    /** The all ships node. */
    private Node allShipsNode;
    /** The ship nodes. */
    private HashMap<Player, Node> shipNodes;
    /** The planet list. */
    private HashMap<Integer, AbstractPlanet> planetList;
    /** The ship list. */
    private HashMap<Integer, AbstractShip> shipList;
    /** The ship group list. */
    private HashMap<Integer, ShipGroup> shipGroupList;
    /** The remove ships list. */
    private ArrayList<AbstractShip> removeShipsList;
    /** The asset manager. */
    private AssetManager assetManager;
    /** The control. */
    private IsoControl control;
    /** The hub. */
    private Hub hub;
    /**
     * Indicates that the level is fully loaded into scene-graph
     */
    private boolean levelLoaded = false;

    /**
     * 
     * Retruns the loading state of the level. 
     * Do not update the level while not loaded properly.
     * 
     * @return the state of level loading, true for loaded, false otherwise
     */
    public boolean isLevelLoaded() {
        return levelLoaded;
    }

    /**
     * Gets the level node.
     *
     * @return the level node
     */
    public Node getLevelNode() {
        return levelNode;
    }

    /**
     * Gets the label node.
     *
     * @return the label node
     */
    public Node getLabelNode() {
        return labelNode;
    }

    /**
     * Instantiates a new level.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     */
    public Level(Node rootNode, AssetManager assetManager, IsoControl control) {
        setup(rootNode, assetManager, control, System.currentTimeMillis());
    }

    /**
     * Instantiates a new level.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param players the players
     * @param seed the seed
     */
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, HashMap<Integer, Player> players, long seed) {
        setup(rootNode, assetManager, control, seed);
    }

    /**
     * Setup.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param seed the seed
     */
    private void setup(Node rootNode, AssetManager assetManager, IsoControl control, long seed) {
        this.seed = seed;

        // Init needed system refs
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.hub = Hub.getInstance();
        this.control = control;

        // Init lists
        this.planetList = new HashMap<Integer, AbstractPlanet>(100);
        this.shipList = new HashMap<Integer, AbstractShip>(300);
        this.shipGroupList = new HashMap<Integer, ShipGroup>(50);
        this.planetNodes = new HashMap<Player, Node>(8);
        this.shipNodes = new HashMap<Player, Node>(8);
        this.removeShipsList = new ArrayList<AbstractShip>(100);

        // create base-nodes
        this.levelNode = new Node("Level Node");
        this.freePlanetsNode = new Node("Free Planets Node");
        this.levelNode.attachChild(freePlanetsNode);
        this.allShipsNode = new Node("Level All Ships");
        this.levelNode.attachChild(allShipsNode);

        // create a ship node for each player

        for (Map.Entry<Integer, Player> entrySet : Hub.playersByID.entrySet()) {
            Player p = entrySet.getValue();
            Node n = new Node("Player " + p.getId() + " ShipNode");
            this.shipNodes.put(p, n);
            this.allShipsNode.attachChild(n);
        }


    }

    public void generateLevel() {
        generateLevel(this.seed);
    }

    /**
     * Generate level.
     *
     * @param seed the seed
     */
    public void generateLevel(long seed) {

        System.out.print("[" + seed + "] Generating level...");
        // create a node for the planet-labels
        this.labelNode = new Node("Planet Labels");
        // attach the labels on the root!
        this.rootNode.attachChild(labelNode);
        this.background = new LevelBackground(solarwars.SolarWarsGame.getInstance());
        this.rootNode.attachChild(background);

        AbstractPlanet p;
        this.seed = seed;
        Random r = new Random(seed);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (r.nextBoolean()) {
                    p = new BasePlanet(assetManager, this, new Vector3f(-6 + i, 0, -6 + j), generateSize(r));
                    p.createPlanet();
                    p.setShipCount(5 + r.nextInt(5) + (int) (p.getSize() * (r.nextFloat() * 100.0f)));

                    planetList.put(p.getId(), p);
                    freePlanetsNode.attachChild(p);
                    System.out.print(".");
                }
            }
        }
        if (control != null) {
            control.addShootable(levelNode);
        }

        System.out.println("Level generated!");
    }

    /**
     * Setup players.
     *
     * @param players the players
     */
    public void setupPlayers(HashMap<Integer, Player> players) {
        for (Map.Entry<Integer, Player> entrySet : players.entrySet()) {
            Player p = entrySet.getValue();
            Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
            planetNodes.put(p, playersPlanetsNode);
            levelNode.attachChild(playersPlanetsNode);

            AbstractPlanet randomPlanet = getRandomPlanet(p);

            freePlanetsNode.detachChild(randomPlanet);
            playersPlanetsNode.attachChild(randomPlanet);
        }
        levelLoaded = true;
        System.out.println("Players setup!");
    }

    /**
     * Adds the ship.
     *
     * @param p the p
     * @param s the s
     */
    public void addShip(Player p, AbstractShip s) {
        Node shipNode = shipNodes.get(p);
        shipNode.attachChild(s);
        shipList.put(s.getId(), s);
    }

    /**
     * Adds the ship group.
     *
     * @param p the p
     * @param s the s
     */
    public void addShipGroup(Player p, ShipGroup s) {
        Node shipNode = shipNodes.get(p);
        shipNode.attachChild(s);
        shipGroupList.put(s.getId(), s);
    }

    /**
     * Removes the ship.
     *
     * @param p the p
     * @param s the s
     */
    public void removeShip(Player p, AbstractShip s) {
        Node shipNode = shipNodes.get(p);
        shipNode.detachChild(s);
        removeShipsList.add(s);
    }

    /**
     * Removes the ship group.
     *
     * @param p the p
     * @param s the s
     */
    public void removeShipGroup(Player p, ShipGroup s) {
        Node shipNode = shipNodes.get(p);
        shipNode.detachChild(s);
        shipGroupList.remove(s);
    }

    /**
     * Gets the planet iterator.
     *
     * @return the planet iterator
     */
    public Set<Entry<Integer, AbstractPlanet>> getPlanetSet() {
        return planetList.entrySet();
    }

    public AbstractPlanet getPlanet(int id) {
        return planetList.get(id);
    }

    public ShipGroup getShipGroup(int id) {
        return shipGroupList.get(id);
    }

    public AbstractShip getShip(int id) {
        return shipList.get(id);
    }

    /**
     * Gets the random planet.
     *
     * @param p the p
     * @return the random planet
     */
    private AbstractPlanet getRandomPlanet(Player p) {
        Random r = new Random(seed);
        boolean found = false;

        int idx = r.nextInt(planetList.size());
        AbstractPlanet planet = planetList.get(idx);

        while (!found) {

            if (planet.getOwner() == null && planet.getSize() > 0.35f) {
                found = true;
                break;
            }
            idx = r.nextInt(planetList.size());
            planet = planetList.get(idx);
        }

        planet.setOwner(p);
        planet.setShipCount(100);
        return planet;
    }

    /**
     * Generate size.
     *
     * @param r the r
     * @return the float
     */
    private float generateSize(Random r) {
        return (0.6f + r.nextFloat()) / 4;
    }

    /**
     * Updates the level.
     *
     * @param tpf the tpf
     */
    public void updateLevel(float tpf) {
        if (!levelLoaded) {
            return;
        }

        for (Map.Entry<Integer, AbstractPlanet> entry : getPlanetSet()) {
            entry.getValue().updateLabel(tpf);
        }

        for (Map.Entry<Integer, ShipGroup> entry : shipGroupList.entrySet()) {
            entry.getValue().updateShipGroup(tpf);
        }

        for (Map.Entry<Integer, AbstractShip> entry : shipList.entrySet()) {
            entry.getValue().updateShip(tpf);
        }

        for (AbstractShip s : removeShipsList) {
            shipList.remove(s.getId());
        }

        removeShipsList.clear();
    }

    public void destroy() {
        control.removeShootable(levelNode);

        this.rootNode.detachChild(labelNode);
        this.rootNode.detachChild(background);
        this.background = null;
        this.labelNode = null;

        this.freePlanetsNode.detachAllChildren();
        this.allShipsNode.detachAllChildren();
        this.levelNode.detachAllChildren();

        this.levelNode = null;
        this.freePlanetsNode = null;
        this.allShipsNode = null;

        // Init lists
        this.planetList.clear();
        this.shipList.clear();
        this.shipGroupList.clear();
        this.planetNodes.clear();
        this.shipNodes.clear();
        this.removeShipsList.clear();

        // Init needed system refs
        this.rootNode = null;
        this.assetManager = null;
        this.hub = null;
        this.control = null;

    }
}
