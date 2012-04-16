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
package logic;

import solarwars.Hub;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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
import entities.LevelBackground;
import gui.GameGUI;
import gui.elements.GameOverGUI;
import solarwars.IsoControl;
import solarwars.SolarWarsApplication;

/**
 * The Class Level.
 */
public class Level {

    public static final String[] LEVEL_SIZE_NAME = {
        "NONE",
        "ONE PLAYER",
        "TWO PLAYER",
        "THREE PLAYER",
        "FOUR PLAYER",
        "FIVE PLAYER",
        "SIX PLAYER",
        "SEVEN PLAYER",
        "EIGHT PLAYER"
    };
    private static float[] PLAYERS_CAMERA_HEIGHT = {
        10,
        12,
        12,
        13,
        14,
        14,
        15,
        15,
        15
    };

    /**
     * Gets the camera height for a given player count
     * @param players
     * @return 
     */
    public static float getLevelSize(int players) {
        if (players > 1 && players < 9) {
            return PLAYERS_CAMERA_HEIGHT[players];
        }
        return PLAYERS_CAMERA_HEIGHT[0];
    }
    /** The PLANE t_ id. */
    private static int PLANET_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    public static int getContiniousPlanetID() {
        return PLANET_ID++;
    }
    /** The SHI p_ id. */
    private static int SHIP_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    public static int getContiniousShipID() {
        return SHIP_ID++;
    }
    /** The SHI p_ id. */
    private static int SHIP_GROUP_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    public static int getContiniousShipGroupID() {
        return SHIP_GROUP_ID++;
    }

    void resetEntityIDs() {
        SHIP_ID = 0;
        SHIP_GROUP_ID = 0;
        PLANET_ID = 0;
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
    /** The background. */
    private LevelBackground background;
    /** The particle node for particle emitters of the planets */
    private Node particleEmitters;
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
    /** The currently used game gui. */
    private GameGUI gui;
    /**
     * Indicates that the level is fully loaded into scene-graph
     */
    private boolean levelLoaded = false;
    /** indicates that the game ended */
    private boolean gameOver = false;
    private boolean watchGame = false;

    /**
     * Retrieve information about the game-end.
     * @return true if ended, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * 
     * Set game over flag.
     * 
     * @param gameOver the value to set
     */
    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public GameGUI getGui() {
        return gui;
    }

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
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, GameGUI gui) {
        this.gui = gui;
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
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, GameGUI gui, HashMap<Integer, Player> players, long seed) {
        this.gui = gui;
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
        this.particleEmitters = new Node("Particle Emitters Node");
        this.levelNode.attachChild(particleEmitters);

        // create a ship node for each player

        for (Map.Entry<Integer, Player> entrySet : Hub.playersByID.entrySet()) {
            Player p = entrySet.getValue();
            Node n = new Node("Player " + p.getId() + " ShipNode");
            this.shipNodes.put(p, n);
            this.allShipsNode.attachChild(n);
        }


    }

    public void generateLevel() {
        LevelGenerator g = new LevelGenerator(this);
        g.generate(seed);
    }

    public void generateLevel(long seed) {
        this.seed = seed;
        LevelGenerator g = new LevelGenerator(this);
        g.generate(this.seed);
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
        shipGroupList.remove(s.getId());
    }

    public void addParticleEmitter(ParticleEmitter emitter) {
        particleEmitters.attachChild(emitter);
    }

    public void removeParticleEmitter(ParticleEmitter emitter) {
        particleEmitters.detachChild(emitter);
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

        p.capturePlanet(planet);
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
        float[] random = {
            0.2f, 0.225f, 0.25f, 0.275f, 0.3f,
            0.325f, 0.35f, 0.375f, 0.4f};
        return random[r.nextInt(random.length)];
        //return (0.6f + r.nextFloat()) / 4;
    }

    /**
     * Updates the level.
     *
     * @param tpf the tpf
     */
    public void updateLevel(float tpf) {
        if (!levelLoaded || gameOver) {
            GameOverGUI.getInstance().display();
            return;
        }

        if (Hub.getLocalPlayer().hasLost()) {
            Player.localPlayerLooses();
        } else if (Hub.getLocalPlayer().getDefeatedPlayer() > -1 || Player.lastPlayer()) {
            Player.localPlayerWins();
            Hub.getLocalPlayer().setDefeatedPlayer(-1);
        }

        for (Map.Entry<Integer, AbstractPlanet> entry : getPlanetSet()) {
            entry.getValue().updatePlanet(tpf);
        }

        for (Map.Entry<Integer, ShipGroup> entry : shipGroupList.entrySet()) {
            entry.getValue().updateShipGroup(tpf);
        }

        for (Map.Entry<Integer, AbstractShip> entry : shipList.entrySet()) {
            entry.getValue().updateShip(tpf);
        }

        for (AbstractShip s : removeShipsList) {
            //s.destroyParticleEmitter();
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

        this.particleEmitters.detachAllChildren();
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
        this.control = null;
        this.gui.cleanUpGUI();
        this.gui = null;
    }

    class LevelGenerator {
        public static final float PLANET_SPACE = 1.0f;

        private Level level;
        /* linksunten, linksoben, rechtsoben, rechtsunten */
        private Vector3f[] corners = {
            Vector3f.ZERO, Vector3f.ZERO,
            Vector3f.ZERO, Vector3f.ZERO};

        public LevelGenerator(Level hull) {
            level = hull;
            getCorners();
        }

        private void getCorners() {

            Vector2f leftBottom = new Vector2f(0, 0);
            Vector2f leftTop = new Vector2f(0, gui.getHeight());
            Vector2f rightTop = new Vector2f(gui.getWidth(), gui.getHeight());
            Vector2f rightBottom = new Vector2f(gui.getWidth(), 0);

            corners[0] = getWorldCoordsOnXZPlane(leftBottom, 0);
            corners[1] = getWorldCoordsOnXZPlane(leftTop, 0);
            corners[2] = getWorldCoordsOnXZPlane(rightTop, 0);
            corners[3] = getWorldCoordsOnXZPlane(rightBottom, 0);
        }

        private Vector3f getWorldCoordsOnXZPlane(Vector2f screenCoords, float planeHeight) {
            Camera cam = SolarWarsApplication.getInstance().getCamera();

            Vector2f click2d = screenCoords;
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);

            float t = (planeHeight - ray.getOrigin().y) / ray.getDirection().y;
            Vector3f XZPlanePos =
                    ray.getDirection().clone().
                    mult(t).addLocal(ray.getOrigin().clone());
            return XZPlanePos;
        }

        public void generateOld(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background = new LevelBackground(solarwars.SolarWarsGame.getInstance());
            level.rootNode.attachChild(background);

            AbstractPlanet p;
            level.seed = seed;
            Random r = new Random(seed);

            for (int i = -5; i <= 5; i++) {
                for (int j = -4; j <= 4; j++) {
                    if (r.nextBoolean()) {
                        p = new BasePlanet(
                                assetManager, level,
                                new Vector3f(i, 0, j),
                                generateSize(r));
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

        public void generate(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background = new LevelBackground(solarwars.SolarWarsGame.getInstance());
            level.rootNode.attachChild(background);

            AbstractPlanet p;
            level.seed = seed;
            Random r = new Random(seed);

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);
            
            for (float x = leftBottomX-PLANET_SPACE; x >= topRightX+PLANET_SPACE; x--) {
                for (float z = topRightZ-PLANET_SPACE; z >= leftBottomZ+PLANET_SPACE; z--) {
                    if (r.nextBoolean()) {
                        p = new BasePlanet(
                                assetManager, level,
                                new Vector3f(x, 0, z),
                                generateSize(r));
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
    }
}
