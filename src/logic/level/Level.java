/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: Level.java
 * Type: logic.level.Level
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
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
import java.util.Iterator;
import java.util.Random;
import logic.Player;
import solarwars.IsoControl;

/**
 * The Class Level.
 */
public class Level {

    /** The current level. */
    private static Level currentLevel = null;

    /**
     * Gets the current level.
     *
     * @return the current level
     */
    public static Level getCurrentLevel() {
        if (currentLevel != null) {
            return currentLevel;
        }
        return null;
    }
    
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
    
    /** The all ships node. */
    private Node allShipsNode;
    
    /** The ship nodes. */
    private HashMap<Player, Node> shipNodes;
    
    /** The planet list. */
    private ArrayList<AbstractPlanet> planetList;
    
    /** The ship list. */
    private ArrayList<AbstractShip> shipList;
    
    /** The ship group list. */
    private ArrayList<ShipGroup> shipGroupList;
    
    /** The remove ships list. */
    private ArrayList<AbstractShip> removeShipsList;
    
    /** The asset manager. */
    private AssetManager assetManager;
    
    /** The control. */
    private IsoControl control;
    
    /** The hub. */
    private Hub hub;

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
        setup(rootNode, assetManager, control, 0);
    }

    /**
     * Instantiates a new level.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param seed the seed
     */
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, long seed) {
        setup(rootNode, assetManager, control, seed);

        generateLevel(this.seed);
        setupPlayers();
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
        this.planetList = new ArrayList<AbstractPlanet>();
        this.shipList = new ArrayList<AbstractShip>();
        this.shipGroupList = new ArrayList<ShipGroup>();
        this.planetNodes = new HashMap<Player, Node>();
        this.shipNodes = new HashMap<Player, Node>();
        this.removeShipsList = new ArrayList<AbstractShip>();

        // create base-nodes
        this.levelNode = new Node("Level Node");
        this.freePlanetsNode = new Node("Free Planets Node");
        this.levelNode.attachChild(freePlanetsNode);
        this.allShipsNode = new Node("Level All Ships");
        this.levelNode.attachChild(allShipsNode);

        // create a ship node for each player
        for (int i = 0; i < hub.getPlayerCount(); i++) {
            Player p = hub.getPlayer(i);
            Node n = new Node(p.getName() + " ShipNode");
            this.shipNodes.put(p, n);
            this.allShipsNode.attachChild(n);
        }

        // create a node for the planet-labels
        this.labelNode = new Node("Planet Labels");
        // attach the labels on the root!
        this.rootNode.attachChild(labelNode);
    }

    /**
     * Generate level.
     *
     * @param seed the seed
     */
    public void generateLevel(long seed) {

        AbstractPlanet p;
        this.seed = seed;
        Random r = new Random(seed);

        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (r.nextBoolean()) {
                    p = new BasePlanet(assetManager, this, new Vector3f(-7 + i, 0, -9 + j), generateSize(r));
                    p.createPlanet();
                    p.setShipCount(5 + r.nextInt(5));

                    planetList.add(p);
                    freePlanetsNode.attachChild(p);
                    //control.addShootable(p.getGeometry());
                }
            }
        }

        control.addShootable(levelNode);
        currentLevel = this;
        //rootNode.attachChild(control.getShootablesNode());
    }

    /**
     * Setup players.
     */
    public void setupPlayers() {
        for (String s : Hub.playerNames) {
            Player p = hub.getPlayer(s);
            Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
            planetNodes.put(p, playersPlanetsNode);
            levelNode.attachChild(playersPlanetsNode);

            AbstractPlanet randomPlanet = getRandomPlanet(p);

            freePlanetsNode.detachChild(randomPlanet);
            playersPlanetsNode.attachChild(randomPlanet);
        }
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
        shipList.add(s);
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
        shipGroupList.add(s);
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
    public Iterator<AbstractPlanet> getPlanetIterator() {
        return planetList.iterator();
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

            if (planet.getOwner() == null && planet.getSize() > 0.3f) {
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
        for (AbstractPlanet p : planetList) {
            p.updateLabel(tpf);
        }
        for (ShipGroup sg : shipGroupList) {
            sg.updateShipGroup(tpf);
        }
        for (AbstractShip s : shipList) {
            s.updateShip(tpf);
        }
        
        for (AbstractShip s : removeShipsList) {
            shipList.remove(s);
        }
        
        removeShipsList.clear();
    }
}
