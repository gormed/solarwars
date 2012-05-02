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
import java.util.Stack;
import solarwars.IsoControl;
import solarwars.SolarWarsApplication;

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
        0.325f, 0.35f, 0.375f, 0.4f, 0.45f};

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
    private HashMap<Integer, Player> playersByID;
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


    }

    public void generateLevel() {
        LevelGenerator g = new LevelGenerator(this);
        g.generateSquare(seed);
    }

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

    private int generateSize(Random r) {

        return r.nextInt(PLANET_SIZES.length);
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

        public static final float PLANET_SPACE_HORIZ = 1.0f;
        public static final float PLANET_SPACE_VERT = 0.5f;
        public final int[] SUBPLANETS = {0, 1, 2, 3, 4, 4, 6, 8, 8};
        private Level level;
        /* linksunten, linksoben, rechtsoben, rechtsunten */
        private Vector3f[] corners = {
            Vector3f.ZERO, Vector3f.ZERO,
            Vector3f.ZERO, Vector3f.ZERO};
        /* space coordinates */
        private boolean[][] spCoord;
        /*Spielerplaneten - vektorielle Koordinaten */
        Stack<Vector2f> positionen = new Stack<Vector2f>();

        public LevelGenerator(Level hull) {
            level = hull;
            getCorners();
            initArrays();
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
        
        private void initArrays(){
            
            /* Initialisiere spCoord */
            int space = (level.playersByID.size())*2+1;
            spCoord = new boolean[space][space];
            for (int i = 0; i < space; i++){
                for (int j = 0; j < space; j++){
                    spCoord[i][j] = true;
                }                
            }
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
                        int size = generateSize(r);
                        p = new BasePlanet(
                                assetManager, level,
                                new Vector3f(i, 0, j), size);
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
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance());
            level.rootNode.attachChild(background);


            level.seed = seed;
            Random r = new Random(seed);
            int playerCount = Hub.playersByID.size();

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);

            int bigPlanetCount = playerCount;
            int semiBigPlanetCount = playerCount + 2 + r.nextInt(playerCount);
            int planetCount = 10 + r.nextInt(playerCount * 10);

            for (int i = 0; i < bigPlanetCount; i++) {
                float x =
                        r.nextFloat() * (leftBottomX - PLANET_SPACE_HORIZ)
                        + r.nextFloat() * (topRightX + PLANET_SPACE_HORIZ);
                float z =
                        r.nextFloat() * (topRightZ - PLANET_SPACE_VERT)
                        + r.nextFloat() * (leftBottomZ + PLANET_SPACE_VERT);
                float dist = 0.5f * r.nextInt(SUBPLANETS[PLANET_SIZES.length - 1]);
                int size = PLANET_SIZES.length - 1;

                int ships = getRandomShipCount(r, size);
                AbstractPlanet p = createPlanet(size, x, z, ships);
                createSubPlanets(p, r, dist);

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

        public void generateClassic(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance());
            level.rootNode.attachChild(background);


            level.seed = seed;
            Random r = new Random(seed);

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);


            for (float x = leftBottomX - PLANET_SPACE_HORIZ;
                    x >= topRightX + PLANET_SPACE_HORIZ; x--) {
                for (float z = topRightZ - PLANET_SPACE_VERT;
                        z >= leftBottomZ + PLANET_SPACE_VERT; z--) {
                    if (r.nextFloat() > 0.65f) {
                        createPlanet(r, x, z);
                        System.out.print(".");
                    }
                }
            }
            if (control != null) {
                control.addShootable(levelNode);
            }

            setupPlayers(level.playersByID, r);

            System.out.println("Level generated!");
        }
        
        public void generateSquare(long seed) {
            System.out.print("[" + seed + "] Generating level...");
            // create a node for the planet-labels
            level.labelNode = new Node("Planet Labels");
            // attach the labels on the root!
            level.rootNode.attachChild(labelNode);
            level.background =
                    new LevelBackground(solarwars.SolarWarsGame.getInstance());
            level.rootNode.attachChild(background);


            level.seed = seed;
            Random r = new Random(seed);

            int leftBottomX = Math.round(corners[0].x);
            int leftBottomZ = Math.round(corners[0].z);
            int topRightX = Math.round(corners[2].x);
            int topRightZ = Math.round(corners[2].z);

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
            int PlanetCounter = 0;
            boolean top = true;
            boolean startPlanet = false;
            
            //Schleife für Anzahl der Ringe
            for (int i = 0; i <= playerCount ;i++){                
                pointerZ = +i;
                pointerX = +i;
                arrayZ = playerCount-i;
                arrayX = playerCount-i;
                //Schleife für X-Koord
                for (int j = 0; j < (i*2+1) ; j++){
                    //Schleife für Z-Koord
                    for (int k = 0; k < (i*2+1) ; k++){
                        if (platz(arrayX, arrayZ) == true){
                            // CREATE PLANET - defekt?
                                // if (r.nextFloat() > 650) {
                                // createPlanet(r, pointerX, pointerZ);
                                // System.out.print(".");
                                // }
                                createPlanet(r, pointerX, pointerZ);
                                setSpCoordFalse(arrayX, arrayZ);
                                
                                
                                //Wenn letzter Ring erzeugt wird
                                if (i == playerCount){
                                    
                                    // COUNTER INITIALISIERUNG
                                    // Wenn noch an der ersten Spalte gebaut wird nur counter++
                                    if (playerCount*2+1 < counter){
                                        counter++;
                                    }
                                    // Wenn oben erstellt wird, initialisiere counter und zwCounter entsprechend
                                    else if (top == true){
                                        zwCounter = counter;
                                        counter = playerCount*8+decrement;
                                        decrement--;
                                        top = false;
                                        }
                                        // Wenn unten erstellt wird, normaler counter++ über zwCounter
                                        else{
                                        zwCounter++;
                                        counter = zwCounter;
                                        top = true;
                                        }
                                 
                                    // PLANETENERZEUGUNG
                                       // An einer zufälligen Stelle den ersten Spielerplanet erstellen
                                    if (( counter < 8 && randomTake() == true ||counter == 8) && startPlanet == false){
                                        positionen.push(new Vector2f(pointerX, pointerZ));
                                        setSpCoordFalse(j,k);
                                        startPlanetNumber = counter;
                                        startPlanet = true;
                                        }
                                        // Sobald 7 freie Planeten erstellt wurden, den nächsten Spielerplanet erstellen
                                    else if (counter == startPlanetNumber+8*multiplier){
                                        positionen.push(new Vector2f(pointerX, pointerZ));
                                        PlanetCounter++;
                                        setSpCoordFalse(j,k);
                                        multiplier++;
                                        }
                                        // ganz normalen Planet erstellen
                                        else{
                                            createPlanet(r, pointerX, pointerZ);
                                            setSpCoordFalse(arrayX, arrayZ);
                                    }
                                    
                                }
                        }
                        pointerZ--;
                        arrayZ++;
                    }
                    pointerZ = +i;
                    arrayZ = playerCount-i;
                    pointerX--;
                    arrayX++;
                }
                
            }
            
            createPlayerPositions(r);
            
            if (control != null) {
                control.addShootable(levelNode);
            }

            // setupPlayers(level.playersByID, r);
            
            levelLoaded = true;
            System.out.println("Level generated!");
        }
        
        private boolean platz(int xKoord, int zKoord){
            return spCoord[xKoord][zKoord];
        }
        
        private void setSpCoordFalse(int xKoord, int zKoord){
            spCoord[xKoord][zKoord] = false;
        }
        
        private Vector2f getRandomPos(Random r) {
            boolean found = false;
            while (!found && !positionen.isEmpty()) {
                int idx = r.nextInt(positionen.size());
                if (positionen.get(idx) != null) {
                    Vector2f rand = positionen.get(idx);
                    positionen.remove(idx);
                    return rand;
                }
            }
            return null;
        }
        
        private void createPlayerPositions(Random r){
            for (Map.Entry<Integer, Player> entrySet : level.playersByID.entrySet()) {
                Player p = entrySet.getValue();
                Vector2f v = getRandomPos(r);
                createPlayerPlanet( r,p,v.x, v.y);
            } 
        }
            
        
        private boolean randomTake(){
            if (gibZufallszahl(1000) > 875){
                return true;
            }
            else{
                return false;
            }   
        }
        
        // gibt eine Zufallszahl zwischen 1 und pMaximum zurück
    public int gibZufallszahl(int pMaximum) {
        return  (int) ((Math.random()*pMaximum)+1);
    }

        private void createSubPlanets(AbstractPlanet basePlanet, Random r, float maxDist) {
            int planetCount = 1 + r.nextInt(basePlanet.getSizeID());
            maxDist = planetCount * 0.8f;
            float avgTurns = (float) Math.PI * 2 / planetCount;
            float turns = 0;

            float avgDist = maxDist / planetCount;
            float dist = 0;

            Vector2f center = new Vector2f(
                    basePlanet.getPosition().x,
                    basePlanet.getPosition().z);

            for (int i = 0; i < planetCount; i++) {
                int size = r.nextInt(basePlanet.getSizeID() - 2);
                if (size < 1) {
                    size = 1;
                }
                int ships = getRandomShipCount(r, size);

                float randTurn = r.nextFloat() * (float) Math.PI * 2;
                Vector2f pos = getRotatedPosition(
                        center,
                        dist += avgDist,
                        turns += avgTurns);
                createPlanet(size, pos.x, pos.y, ships);
            }
        }

        private Vector2f getRotatedPosition(Vector2f center, float dist, float rotated) {
            Vector2f pos = new Vector2f(1, 0);
            pos.multLocal(dist);
            pos.rotateAroundOrigin(rotated, true);
//            pos.x = pos.x * (float) Math.cos(rotated) - pos.y * (float) Math.sin(rotated);
//            pos.y = pos.y * (float) Math.cos(rotated) + pos.x * (float) Math.sin(rotated);
            return pos;
        }

        private int getRandomShipCount(Random r, int size) {
            return 5 + r.nextInt(5)
                    + (int) (PLANET_SIZES[size] * (r.nextFloat() * 100.0f));
        }

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

        private AbstractPlanet createPlanet(Random r, float x, float z) {
            int size = generateSize(r);
            AbstractPlanet p = new BasePlanet(
                    assetManager, level,
                    new Vector3f(x, 0, z),
                    size);
            p.createPlanet();
            p.setShipCount(
                    5 + r.nextInt(5)
                    + (int) (p.getSize() * (r.nextFloat() * 100.0f)));

            planetList.put(p.getId(), p);
            freePlanetsNode.attachChild(p);
            return p;
        }

        /**
         * Creates  a       planet at given position for a given player
         * @param   r       the random generator
         * @param   owner   the desired owner of the planet
         * @param   x       coord on the xz plane
         * @param   z       coord on the xz plane
         * @return          new generated planet for the player
         */
        private AbstractPlanet createPlayerPlanet(Random r, Player owner, float x, float z) {
            
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
            // set owner
            p.setOwner(owner);
            // add planet into list
            planetList.put(p.getId(), p);
            // create nodes for the player and add the planet
            setupPlayer(owner, p, r);
            
            return p;
        }

        /**
         * Setup players.
         *
         * @param players the players
         */
        public void setupPlayer(Player p, AbstractPlanet startPlanet, Random r) {
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
        public void setupPlayers(HashMap<Integer, Player> players, Random r) {

            for (Map.Entry<Integer, Player> entrySet : players.entrySet()) {
                Player p = entrySet.getValue();
                Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
                planetNodes.put(p, playersPlanetsNode);
                levelNode.attachChild(playersPlanetsNode);

                AbstractPlanet randomPlanet = getRandomPlanet(p, r);

                freePlanetsNode.detachChild(randomPlanet);
                playersPlanetsNode.attachChild(randomPlanet);
            }
            levelLoaded = true;
            System.out.println("Players setup!");
        }

        /**
         * Gets the random planet.
         *
         * @param p the p
         * @return the random planet
         */
        private AbstractPlanet getRandomPlanet(Player p, Random r) {
            boolean found = false;

            int idx = r.nextInt(planetList.size());
            AbstractPlanet planet = planetList.get(idx);

            while (!found) {
                // TODO: change level generation
                if (planet.getOwner() == null && planet.getSize() >= PLANET_SIZES[PLANET_SIZES.length - 2]) {
                    found = true;
                    break;
                }
                idx = r.nextInt(planetList.size());
                planet = planetList.get(idx);
            }

            p.capturePlanet(planet);
            planet.setShipCount(PLAYER_START_SHIP_COUNT);
            return planet;
        }
    }
}
