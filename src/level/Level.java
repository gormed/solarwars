/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

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
import java.util.Random;
import logic.Player;
import solarwars.IsoControl;

/**
 *
 * @author Hans
 */
public class Level {

    private static Level currentLevel = null;

    public static Level getCurrentLevel() {
        if (currentLevel != null) {
            return currentLevel;
        }
        return null;
    }
    private long seed = 0;
    private Node rootNode;
    private Node levelNode;
    private HashMap<Player, Node> planetNodes;
    private Node freePlanetsNode;
    private Node labelNode;
    private Node allShipsNode;
    
    private HashMap<Player, Node> shipNodes;
    private ArrayList<AbstractPlanet> planetList;
    private ArrayList<AbstractShip> shipList;
    private ArrayList<ShipGroup> shipGroupList;
    private AssetManager assetManager;
    private IsoControl control;
    private Hub hub;

    public Node getLevelNode() {
        return levelNode;
    }

    public Node getLabelNode() {
        return labelNode;
    }

    public Level(Node rootNode, AssetManager assetManager, IsoControl control) {
        setup(rootNode, assetManager, control, 0);
    }

    public Level(Node rootNode, AssetManager assetManager, IsoControl control, long seed) {
        setup(rootNode, assetManager, control, seed);

        generateLevel(this.seed);
        setupPlayers();
    }

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

    public void generateLevel(long seed) {
        
        AbstractPlanet p;
        this.seed = seed;
        Random r = new Random(seed);
        
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (r.nextBoolean()) {
                    p = new BasePlanet(assetManager, this, new Vector3f(-7 + i, 0, -9 + j), generateSize(r));
                    p.createPlanet();
                    p.setShipCount(5 + r.nextInt(10));
                    
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

    public void addShip(Player p, AbstractShip s) {
        Node shipNode = shipNodes.get(p);
        shipNode.attachChild(s);
        shipList.add(s);
    }

    public void removeShip(Player p, AbstractShip s) {
        Node shipNode = shipNodes.get(p);
        shipNode.detachChild(s);
    }

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

    private float generateSize(Random r) {
        return (0.6f + r.nextFloat()) / 4;
    }

    public void updateLevel(float tpf) {
        for (AbstractPlanet p : planetList) {
            p.updateLabel(tpf);
        }
        for (AbstractShip s : shipList) {
            s.updateShip(tpf);
        }
    }
}
