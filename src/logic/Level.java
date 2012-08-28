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
 * File: Level.java
 * Type: logic.Level
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import solarwars.Hub;
import solarwars.IsoControl;
import solarwars.SolarWarsApplication;

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
import entities.LevelBackground;
import entities.ShipBatchManager;
import entities.ShipGroup;
import gui.GameGUI;
import gui.elements.GameOverGUI;

/**
 * The Class Level.
 */
public class Level {

    public static final int PLAYER_START_SHIP_COUNT = 100;
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
    public static float[] PLAYERS_CAMERA_HEIGHT = {
        8,
        10,
        10,
        12,
        13,
        14,
        15,
        16,
        16
    };
    public static float[] PLANET_INCREMENT_TIME = {
        2f, 1.5f, 1.2f, 1.0f, 0.9f,
        0.8f, 0.7f, 0.6f, 0.50f, 0.50f};
    public static float[] PLANET_SIZES = {
        0.2f, 0.225f, 0.25f, 0.275f, 0.3f,
        0.325f, 0.35f, 0.375f, 0.4f, 0.425f};

    /**
     * Gets the camera height for a given player count.
     *
     * @param players the players
     * @return the level size
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

    /**
     * Reset entity i ds.
     */
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
    /** The particle node for particle emitters of the planets. */
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
    /** The players by id. */
    private HashMap<Integer, Player> playersByID;
    /** The batch manager. */
    private ShipBatchManager batchManager;
    /** Indicates that the level is fully loaded into scene-graph. */
    private boolean levelLoaded = false;
    /** indicates that the game ended. */
    private boolean gameOver = false;
    /** The watch game. */
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
        MultiplayerGameplay.getInstance().removeGameplayListener();
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
     * @param gui the gui
     * @param players the players
     */
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, GameGUI gui, HashMap<Integer, Player> players) {
        this.gui = gui;
        this.playersByID = players;
        setup(rootNode, assetManager, control, System.currentTimeMillis());
    }

    /**
     * Instantiates a new level.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param gui the gui
     * @param players the players
     * @param seed the seed
     */
    public Level(Node rootNode, AssetManager assetManager, IsoControl control, GameGUI gui, HashMap<Integer, Player> players, long seed) {
        this.gui = gui;
        this.playersByID = players;
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

        batchManager = new ShipBatchManager(this);
        batchManager.initialize(Hub.playersByID.size() * 100);
    }

    /**
     * Generate level.
     */
    public void generateLevel() {
        LevelGenerator g = new LevelGenerator(this);
        g.generateSquare(seed);
    }

    /**
     * Generate level.
     *
     * @param seed the seed
     */
    public void generateLevel(long seed) {
        this.seed = seed;
        LevelGenerator g = new LevelGenerator(this);
        g.generateSquare(this.seed);
    }

    /**
     * Adds the ship.
     *
     * @param p the p
     * @param s the s
     */
    public void addShip(Player p, AbstractShip s) {
//        Node shipNode = shipNodes.get(p);
//        shipNode.attachChild(s);

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
//        Node shipNode = shipNodes.get(p);
//        shipNode.detachChild(s);

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

    /**
     * Adds the particle emitter.
     *
     * @param emitter the emitter
     */
    public void addParticleEmitter(ParticleEmitter emitter) {
        particleEmitters.attachChild(emitter);
    }

    /**
     * Removes the particle emitter.
     *
     * @param emitter the emitter
     */
    public void removeParticleEmitter(ParticleEmitter emitter) {
        particleEmitters.detachChild(emitter);
    }

    /**
     * Gets the planet set.
     *
     * @return the planet iterator
     */
    public Set<Entry<Integer, AbstractPlanet>> getPlanetSet() {
        return planetList.entrySet();
    }

    /**
     * Gets the planet iterator.
     *
     * @return the planet iterator
     */
    public Set<Entry<Integer, ShipGroup>> getShipGroupSet() {
        return shipGroupList.entrySet();
    }

    /**
     * Gets the planet.
     *
     * @param id the id
     * @return the planet
     */
    public AbstractPlanet getPlanet(int id) {
        return planetList.get(id);
    }

    /**
     * Gets the ship group.
     *
     * @param id the id
     * @return the ship group
     */
    public ShipGroup getShipGroup(int id) {
        return shipGroupList.get(id);
    }

    /**
     * Gets the ship.
     *
     * @param id the id
     * @return the ship
     */
    public AbstractShip getShip(int id) {
        return shipList.get(id);
    }

    public ShipBatchManager getBatchManager() {
        return batchManager;
    }

    /**
     * Updates the level.
     *
     * @param tpf the tpf
     */
    public void updateLevel(float tpf) {

//        if (!levelLoaded || gameOver) {
//            GameOverGUI.getInstance().display();
//            return;
//        }
        
        // some code to reposition camera dependant on mouse position
        // added by roman
        
        /*Vector2f cp=SolarWarsApplication.getInstance().getInputManager().getCursorPosition();
        Camera cam = SolarWarsApplication.getInstance().getCamera();
        Vector3f loc=cam.getLocation().clone();
        loc.x=-(float)(cp.x-512)*0.002f;
        loc.z=(float)(cp.y-384)*0.002f-6.0f;
        cam.setLocation(loc);
        cam.lookAt(new Vector3f(0,-2.0f,0), new Vector3f(0,1,0));*/
        
        background.update(tpf);
        
        DeathmatchGameplay.GAMETICK += (double) SolarWarsApplication.getInstance().getRealTimePerFrame();

        if (Hub.getLocalPlayer().hasLost()) {
            Player.localPlayerLooses();
        } else if (Hub.getLocalPlayer().getDefeatedPlayer() > -1 || Player.lastPlayer()) {
            Player.localPlayerWins();
            Hub.getLocalPlayer().setDefeatedPlayer(-1);
        }

        for (Map.Entry<Integer, AbstractPlanet> entry : getPlanetSet()) {
            entry.getValue().updatePlanet(tpf);
        }

        batchManager.refreshBatchSize(tpf);

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

    /**
     * Destroy.
     */
    public void destroy() {
        GameOverGUI.getInstance().hide();
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
        this.batchManager.destroy();
        this.batchManager = null;

        // free lists
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

    /**
     * The Class LevelGenerator.
     */
    class LevelGenerator {

        /** The Constant PLANET_SPACE_HORIZ. */
        public static final float PLANET_SPACE_HORIZ = 1.0f;
        /** The Constant PLANET_SPACE_VERT. */
        public static final float PLANET_SPACE_VERT = 0.5f;
        /** The SUBPLANETS. */
        public final int[] SUBPLANETS = {0, 1, 2, 3, 4, 4, 6, 8, 8};
        /** The level. */
        private Level level;
        /* linksunten, linksoben, rechtsoben, rechtsunten */
        /** The corners. */
        private Vector3f[] corners = {
            Vector3f.ZERO, Vector3f.ZERO,
            Vector3f.ZERO, Vector3f.ZERO};
        /* space coordinates */
        /** The sp coord. */
        private boolean[][] spCoord;
        /*Spielerplaneten - vektorielle Koordinaten */
        /** The positionen. */
        Stack<Vector2f> positionen = new Stack<Vector2f>();
        /* Randomizer for the level, keeps the same behavior on all clients */
        /** The randomizer. */
        private Random randomizer;
        /* Anzahl der Ringe, die sich noch hinter einem Spieler befinden (außen) */
        /** The ringe behind. */
        private int ringeBehind = 1;

        /**
         * Instantiates a new level generator.
         *
         * @param hull the hull
         */
        public LevelGenerator(Level hull) {
            level = hull;
            getCorners();
            initArrays();
        }

        /**
         * Gets the corners.
         *
         * @return the corners
         */
        private void getCorners() {

//            final Vector2f leftBottom = new Vector2f(0, 0);
//            final Vector2f leftTop = new Vector2f(0, gui.getHeight());
//            final Vector2f rightTop = new Vector2f(gui.getWidth(), gui.getHeight());
//            final Vector2f rightBottom = new Vector2f(gui.getWidth(), 0);

//            corners[0] = getWorldCoordsOnXZPlane(leftBottom, 0);
//            corners[1] = getWorldCoordsOnXZPlane(leftTop, 0);
//            corners[2] = getWorldCoordsOnXZPlane(rightTop, 0);
//            corners[3] = getWorldCoordsOnXZPlane(rightBottom, 0);

            corners[0] = Vector3f.UNIT_XYZ.clone();
            corners[1] = Vector3f.UNIT_XYZ.clone();
            corners[2] = Vector3f.UNIT_XYZ.clone();
            corners[3] = Vector3f.UNIT_XYZ.clone();
        }

        /**
         * Inits the arrays.
         */
        private void initArrays() {

            /* Initialisiere spCoord */
            int space = (level.playersByID.size()) * 2 + (ringeBehind * 2 + 1);
            spCoord = new boolean[space][space];
            for (int i = 0; i < space; i++) {
                for (int j = 0; j < space; j++) {
                    spCoord[i][j] = true;
                }
            }
        }

        /**
         * Gets the world coords on xz plane.
         *
         * @param screenCoords the screen coords
         * @param planeHeight the plane height
         * @return the world coords on xz plane
         */
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

        /**
         * Generate old.
         *
         * @param seed the seed
         */
        public void generateOld(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background = new LevelBackground(solarwars.SolarWarsGame.getInstance(),(int)seed);
            level.rootNode.attachChild(background);

            AbstractPlanet p;
            level.seed = seed;
            randomizer = new Random(seed);

            for (int i = -5; i <= 5; i++) {
                for (int j = -4; j <= 4; j++) {
                    if (randomizer.nextBoolean()) {
                        int size = generateSize();
                        p = new BasePlanet(
                                assetManager, level,
                                new Vector3f(i, 0, j), size);
                        p.createPlanet();
                        p.setShipCount(5 + randomizer.nextInt(5) + (int) (p.getSize() * (randomizer.nextFloat() * 100.0f)));

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
         * Generate.
         *
         * @param seed the seed
         */
        public void generate(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance(),(int)seed);
            level.rootNode.attachChild(background);


            level.seed = seed;
            randomizer = new Random(seed);
            int playerCount = Hub.playersByID.size();

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);

            int bigPlanetCount = playerCount;
            int semiBigPlanetCount = playerCount + 2 + randomizer.nextInt(playerCount);
            int planetCount = 10 + randomizer.nextInt(playerCount * 10);

            for (int i = 0; i < bigPlanetCount; i++) {
                float x =
                        randomizer.nextFloat() * (leftBottomX - PLANET_SPACE_HORIZ)
                        + randomizer.nextFloat() * (topRightX + PLANET_SPACE_HORIZ);
                float z =
                        randomizer.nextFloat() * (topRightZ - PLANET_SPACE_VERT)
                        + randomizer.nextFloat() * (leftBottomZ + PLANET_SPACE_VERT);
                float dist = 0.5f * randomizer.nextInt(SUBPLANETS[PLANET_SIZES.length - 1]);
                int size = PLANET_SIZES.length - 1;

                int ships = getRandomShipCount(size);
                AbstractPlanet p = createPlanet(size, x, z, ships);
                createSubPlanets(p, dist);

            }

//            for (float x = leftBottomX - PLANET_SPACE_HORIZ;
//                    x >= topRightX + PLANET_SPACE_HORIZ; x--) {
//                for (float z = topRightZ - PLANET_SPACE_VERT;
//                        z >= leftBottomZ + PLANET_SPACE_VERT; z--) {
//                    if (r.nextBoolean()) {
//                        createPlanet(r, x, z);
//                        System.out.print(".");
//                    }
//                }
//            }
            if (control != null) {
                control.addShootable(levelNode);
            }

            System.out.println("Level generated!");
        }

        /**
         * Generate classic.
         *
         * @param seed the seed
         */
        public void generateClassic(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance(),(int)seed);
            level.rootNode.attachChild(background);


            level.seed = seed;
            randomizer = new Random(seed);

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);


            for (float x = leftBottomX - PLANET_SPACE_HORIZ;
                    x >= topRightX + PLANET_SPACE_HORIZ; x--) {
                for (float z = topRightZ - PLANET_SPACE_VERT;
                        z >= leftBottomZ + PLANET_SPACE_VERT; z--) {
                    if (randomizer.nextFloat() > 0.65f) {
                        createPlanet(x, z);
                        System.out.print(".");
                    }
                }
            }
            if (control != null) {
                control.addShootable(levelNode);
            }

            setupPlayers(level.playersByID);
            levelLoaded = true;
            System.out.println("Level generated!");
        }

        /**
         * Baut das Level mit einem quadratischen Spielfeld auf, bei dem
         * jeder Spieler den gleichen Abstand bis zum nächsten hat.
         * Startpositionen sind dabei zufällig und die Anzahl der Ringe ist
         * abhängig von der Spieleranzahl bzw. von dem Datenfeld ringeBehind!
         * 
         * @param seed Zufallsvariable für den Aufbau.
         */
        public void generateSquare(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance(),(int)seed);
            level.rootNode.attachChild(background);


            level.seed = seed;
            randomizer = new Random(seed);

//            int leftBottomX = Math.round(corners[0].x);
//            int leftBottomZ = Math.round(corners[0].z);
//            int topRightX = Math.round(corners[2].x);
//            int topRightZ = Math.round(corners[2].z);

            int playerCount = level.playersByID.size();
            int pointerX = 0;
            int pointerZ = 0;
            int arrayX = playerCount;
            int arrayZ = playerCount;
            int decrement = 0;
            int counter = 0;
            int zwCounter = 0;
            int multiplier = 1;
            int startPlanetNumber = 0;
            boolean top = true;
            boolean startPlanet = false;
            boolean lastRow = false;

            //ERZEUGUNG DES SPIELERRINGS

            arrayX = 0 + ringeBehind;
            arrayZ = -1 + ringeBehind;

            pointerX = playerCount;
            pointerZ = playerCount + 1;
            for (int lauf = 0; lauf < playerCount * 8; lauf++) {
                // Counter-Initialisierung
                // Wenn noch an der ersten Spalte gebaut wird nur counter++
                if (lastRow == false) {
                    if (counter < playerCount * 2 + 1) {
                        counter++;

                        arrayZ++;

                        pointerZ--;
                    } // Wenn oben erstellt wird, initialisiere counter und zwCounter entsprechend
                    else if (top == true) {
                        zwCounter = counter;
                        counter = playerCount * 8 + decrement;

                        arrayZ = 0 + ringeBehind;
                        arrayX++;

                        pointerZ = playerCount;
                        pointerX--;
                        decrement--;
                        top = false;
                        if (counter == playerCount * 6 + 1) {
                            lastRow = true;
                        }
                    } // Wenn unten erstellt wird, normaler counter++ über zwCounter
                    else {
                        zwCounter++;
                        counter = zwCounter;

                        arrayZ = (playerCount * 2) + ringeBehind;

                        pointerZ = -playerCount;
                        top = true;
                    }
                } else {
                    counter--;
                    pointerZ--;

                    arrayZ++;

                }
                // Planetenerzeugung
                // An einer zufälligen Stelle den ersten Spielerplanet erstellen
                if ((counter < (playerCount * 2 + 1) && randomTake() == true && startPlanet == false)
                        || (counter == playerCount * 2 + 1 && startPlanet == false)
                        || (counter == 8 && startPlanet == false)) {
                    positionen.push(new Vector2f(pointerX, pointerZ));
                    setSpCoordFalse(arrayX, arrayZ);
                    startPlanetNumber = counter;
                    startPlanet = true;
                } // Sobald 7 freie Planeten erstellt wurden, den nächsten Spielerplanet erstellen
                else if (counter % 8 == startPlanetNumber) {
                    positionen.push(new Vector2f(pointerX, pointerZ));
                    setSpCoordFalse(arrayX, arrayZ);
                    multiplier++;
                } // ganz normalen Planet erstellen
                else {
                    // Random Planet erzeugen
                    if (randomizer.nextFloat() > 0.65f) {
                        createPlanet(pointerX, pointerZ);
                        System.out.print(".");
                    }
                    // createPlanet(pointerX, pointerZ);
                    setSpCoordFalse(arrayX, arrayZ);
                }
            }

            createPlayerPositions();

            //ERZEUGUNG DES RESTLICHEN RASTERS
            //Schleife für Anzahl der Ringe
            for (int i = 0; i < playerCount + (ringeBehind + 1); i++) {
                pointerZ = +i;
                pointerX = +i;
                arrayZ = (playerCount + ringeBehind) - i;
                arrayX = (playerCount + ringeBehind) - i;
                //Schleife für X-Koord
                for (int j = 0; j < (i * 2 + 1); j++) {
                    //Schleife für Z-Koord
                    for (int k = 0; k < (i * 2 + 1); k++) {
                        if (platz(arrayX, arrayZ) == true) {
                            // Random Planet erzeugen
                            if (randomizer.nextFloat() > 0.65f) {
                                createPlanet(pointerX, pointerZ);
                                System.out.print(".");
                            }
                            // createPlanet(pointerX, pointerZ);
                            setSpCoordFalse(arrayX, arrayZ);
                        }
                        pointerZ--;
                        arrayZ++;
                    }
                    pointerZ = +i;
                    arrayZ = (playerCount + ringeBehind) - i;
                    pointerX--;
                    arrayX++;
                }

            }

            if (control != null) {
                control.addShootable(levelNode);
            }

            // setupPlayers(level.playersByID, r);

            levelLoaded = true;
            System.out.println("Level generated!");
        }

        /**
         * Platz.
         *
         * @param xKoord the x koord
         * @param zKoord the z koord
         * @return true, if successful
         */
        private boolean platz(int xKoord, int zKoord) {
            return spCoord[xKoord][zKoord];
        }

        /**
         * Sets the sp coord false.
         *
         * @param xKoord the x koord
         * @param zKoord the z koord
         */
        private void setSpCoordFalse(int xKoord, int zKoord) {
            spCoord[xKoord][zKoord] = false;
        }

        /**
         * Generate size.
         *
         * @return the int
         */
        private int generateSize() {

            return randomizer.nextInt(PLANET_SIZES.length);
        }

        /**
         * Gets the random pos.
         *
         * @return the random pos
         */
        private Vector2f getRandomPos() {
            boolean found = false;
            while (!found && !positionen.isEmpty()) {
                int idx = randomizer.nextInt(positionen.size());
                if (positionen.get(idx) != null) {
                    Vector2f rand = positionen.get(idx);
                    positionen.remove(idx);
                    return rand;
                }
            }
            return null;
        }

        /**
         * Creates the player positions.
         */
        private void createPlayerPositions() {
            if (level.playersByID.size() > positionen.size()) {
                return;
            }

            for (Map.Entry<Integer, Player> entrySet : level.playersByID.entrySet()) {
                Player p = entrySet.getValue();
                Vector2f v = getRandomPos();
                createPlayerPlanet(p, v.x, v.y);
            }
        }

        /**
         * Random take.
         *
         * @return true, if successful
         */
        private boolean randomTake() {
            if (randomizer.nextFloat() < 1.0f / (level.playersByID.size() * 2 + 1.0f)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Creates the sub planets.
         *
         * @param basePlanet the base planet
         * @param maxDist the max dist
         */
        private void createSubPlanets(AbstractPlanet basePlanet, float maxDist) {
            int planetCount = 1 + randomizer.nextInt(basePlanet.getSizeID());
            maxDist = planetCount * 0.8f;
            float avgTurns = (float) Math.PI * 2 / planetCount;
            float turns = 0;

            float avgDist = maxDist / planetCount;
            float dist = 0;

            Vector2f center = new Vector2f(
                    basePlanet.getPosition().x,
                    basePlanet.getPosition().z);

            for (int i = 0; i < planetCount; i++) {
                int size = randomizer.nextInt(basePlanet.getSizeID() - 2);
                if (size < 1) {
                    size = 1;
                }
                int ships = getRandomShipCount(size);

                float randTurn = randomizer.nextFloat() * (float) Math.PI * 2;
                Vector2f pos = getRotatedPosition(
                        center,
                        dist += avgDist,
                        turns += avgTurns);
                createPlanet(size, pos.x, pos.y, ships);
            }
        }

        /**
         * Gets the rotated position.
         *
         * @param center the center
         * @param dist the dist
         * @param rotated the rotated
         * @return the rotated position
         */
        private Vector2f getRotatedPosition(Vector2f center, float dist, float rotated) {
            Vector2f pos = new Vector2f(1, 0);
            pos.multLocal(dist);
            pos.rotateAroundOrigin(rotated, true);
//            pos.x = pos.x * (float) Math.cos(rotated) - pos.y * (float) Math.sin(rotated);
//            pos.y = pos.y * (float) Math.cos(rotated) + pos.x * (float) Math.sin(rotated);
            return pos;
        }

        /**
         * Gets the random ship count.
         *
         * @param size the size
         * @return the random ship count
         */
        private int getRandomShipCount(int size) {
            return randomizer.nextInt((int) ((size + 1) * 1.5f))
                    + (int) (20 / Level.PLANET_INCREMENT_TIME[size]);
        }

        /**
         * Creates the planet.
         *
         * @param size the size
         * @param x the x
         * @param z the z
         * @param shipCount the ship count
         * @return the abstract planet
         */
        private AbstractPlanet createPlanet(int size, float x, float z, int shipCount) {
            AbstractPlanet p = new BasePlanet(
                    assetManager, level,
                    new Vector3f(x, 0, z),
                    size);

            p.createPlanet();
            p.setShipCount(shipCount);

            planetList.put(p.getId(), p);
            freePlanetsNode.attachChild(p);
            return p;
        }

        /**
         * Creates the planet.
         *
         * @param x the x
         * @param z the z
         * @return the abstract planet
         */
        private AbstractPlanet createPlanet(float x, float z) {

            int size = generateSize();
            AbstractPlanet p = new BasePlanet(
                    assetManager, level,
                    new Vector3f(x, 0, z),
                    size);
            p.createPlanet();
            p.setShipCount(getRandomShipCount(size));

            planetList.put(p.getId(), p);
            freePlanetsNode.attachChild(p);
            return p;
        }

        /**
         * Creates  a       planet at given position for a given player.
         *
         * @param owner the owner
         * @param x the x
         * @param z the z
         * @return          new generated planet for the player
         */
        private AbstractPlanet createPlayerPlanet(Player owner, float x, float z) {

            // set size to maximum
            int size = PLANET_SIZES.length - 1;
            // create BasePlanet for player on given position
            AbstractPlanet p = new BasePlanet(
                    assetManager, level,
                    new Vector3f(x, 0, z),
                    size);
            // init planet geometry
            p.createPlanet();
            // set ships
            p.setShipCount(PLAYER_START_SHIP_COUNT);
            // owner aquires planet
            owner.capturePlanet(p);
            // add planet into list
            planetList.put(p.getId(), p);
            // create nodes for the player and add the planet
            setupPlayer(owner, p);

            return p;
        }

        /**
         * Setup players.
         *
         * @param p the p
         * @param startPlanet the start planet
         */
        public void setupPlayer(Player p, AbstractPlanet startPlanet) {
            Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
            planetNodes.put(p, playersPlanetsNode);
            levelNode.attachChild(playersPlanetsNode);

            freePlanetsNode.detachChild(startPlanet);
            playersPlanetsNode.attachChild(startPlanet);

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

            System.out.println("Players setup!");
        }

        /**
         * Gets the random planet.
         *
         * @param p the p
         * @return the random planet
         */
        private AbstractPlanet getRandomPlanet(Player p) {
            boolean found = false;

            int idx = randomizer.nextInt(planetList.size());
            AbstractPlanet planet = planetList.get(idx);

            while (!found) {
                // TODO: change level generation
                if (planet.getOwner() == null && planet.getSize() >= PLANET_SIZES[PLANET_SIZES.length - 2]) {
                    found = true;
                    break;
                }
                idx = randomizer.nextInt(planetList.size());
                planet = planetList.get(idx);
            }

            p.capturePlanet(planet);
            planet.setShipCount(PLAYER_START_SHIP_COUNT);
            return planet;
        }
    }
}
