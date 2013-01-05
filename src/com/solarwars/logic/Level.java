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
 * Type: com.solarwars.logic.Level
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Node;
import com.solarwars.Hub;
import com.solarwars.controls.ControlManager;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.AbstractShip;
import com.solarwars.entities.ShipBatchManager;
import com.solarwars.entities.ShipGroup;
import com.solarwars.logic.level.GeneratorSquare;
import com.solarwars.logic.level.LevelGenerator;
import com.solarwars.net.NetworkManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class Level discribes a level for SolarWars. There may be only one level
 * associated with only one gameplay.
 *
 * @author Hans Ferchland
 */
public class Level {

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
    /**
     * The PLANE t_ id.
     */
    private static int PLANET_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    public static int getContiniousPlanetID() {
        return PLANET_ID++;
    }
    /**
     * The SHI p_ id.
     */
    private static int SHIP_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    public static int getContiniousShipID() {
        return SHIP_ID++;
    }
    /**
     * The SHI p_ id.
     */
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
     * The seed.
     */
    private long seed = 0;
    /**
     * The root node.
     */
    private Node rootNode;
    /**
     * The level node.
     */
    private Node levelNode;
    /**
     * The planet nodes.
     */
    private HashMap<Player, Node> planetNodes;
    /**
     * The free planets node.
     */
    private Node freePlanetsNode;
    /**
     * The label node.
     */
    private Node labelNode;
    /**
     * The particle node for particle emitters of the planets.
     */
    private Node particleEmitters;
    /**
     * The all ships node.
     */
    private Node allShipsNode;
    /**
     * The ship nodes.
     */
    private HashMap<Player, Node> shipNodes;
    /**
     * The planet list.
     */
    private HashMap<Integer, AbstractPlanet> planetList;
    /**
     * The ship list.
     */
    private HashMap<Integer, AbstractShip> shipList;
    /**
     * The ship group list.
     */
    private HashMap<Integer, ShipGroup> shipGroupList;
    /**
     * The remove ships list.
     */
    private ArrayList<AbstractShip> removeShipsList;
    /**
     * The asset manager.
     */
    private AssetManager assetManager;
    private AbstractGameplay gameplay;
    /**
     * The control.
     */
    private ControlManager control;
    /**
     * The players by id.
     */
    private HashMap<Integer, Player> playersByID;
    /**
     * The batch manager.
     */
    private ShipBatchManager batchManager;
    /**
     * indicates that the game ended.
     */
    private boolean gameOver = false;
    /**
     * The watch game.
     */
    private boolean watchGame = false;
    private LevelGenerator levelGenerator;

    /**
     * Instantiates a new level.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param gui the gui
     * @param players the players
     */
    public Level(Node rootNode, AssetManager assetManager, ControlManager control,
            HashMap<Integer, Player> players) {
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
    public Level(Node rootNode, AssetManager assetManager,
            ControlManager control, HashMap<Integer, Player> players, long seed) {
        this.playersByID = players;
        setup(rootNode, assetManager, control, seed);
    }

    /**
     * Reset entity ids.
     */
    void resetEntityIDs() {
        SHIP_ID = 0;
        SHIP_GROUP_ID = 0;
        PLANET_ID = 0;
    }

    /**
     * Retrieve information about the game-end.
     *
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
        if (NetworkManager.getInstance().isMultiplayerGame()) {
            MultiplayerGameplay.getInstance().removeGameplayListener();
        }
    }

    /**
     *
     * Retruns the loading state of the level. Do not update the level while not
     * loaded properly.
     *
     * @return the state of level loading, true for loaded, false otherwise
     */
    public boolean isLevelLoaded() {
        return levelGenerator.isLevelLoaded();
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

    public HashMap<Integer, Player> getPlayersByID() {
        return playersByID;
    }

    public Node getAllShipsNode() {
        return allShipsNode;
    }

    public Node getFreePlanetsNode() {
        return freePlanetsNode;
    }

    public HashMap<Player, Node> getPlanetNodes() {
        return planetNodes;
    }

    public ControlManager getControlManager() {
        return control;
    }

    public HashMap<Integer, AbstractPlanet> getPlanetList() {
        return planetList;
    }

    public long getSeed() {
        return seed;
    }

    public LevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Setup.
     *
     * @param rootNode the root node
     * @param assetManager the asset manager
     * @param control the control
     * @param seed the seed
     */
    private void setup(Node rootNode, AssetManager assetManager,
            ControlManager control, long seed) {
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
        this.labelNode = new Node("Planet Labels");
        this.rootNode.attachChild(labelNode);

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
            Node n = new Node("Player " + p.getID() + " ShipNode");
            this.shipNodes.put(p, n);
            this.allShipsNode.attachChild(n);
        }

        batchManager = new ShipBatchManager(this);
        batchManager.initialize(Hub.getPlayers());
    }

    /**
     * Generate level.
     */
    public void generateLevel() {
        generateLevel(this.seed);
    }

    /**
     * Generate level.
     *
     * @param seed the seed
     */
    public void generateLevel(final long seed) {
        this.seed = seed;
        levelGenerator = new GeneratorSquare(this);
        levelGenerator.generate(seed);
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
     * Gets the desired planet by its id.
     *
     * @param id the id
     * @return the planet
     */
    public AbstractPlanet getPlanet(int id) {
        return planetList.get(id);
    }

    /**
     * Gets the desired ship group by id.
     *
     * @param id the id
     * @return the ship group
     */
    public ShipGroup getShipGroup(int id) {
        return shipGroupList.get(id);
    }

    /**
     * Gets a ship by its unique id.
     *
     * @param id the id
     * @return the ship with the given id
     */
    public AbstractShip getShip(int id) {
        return shipList.get(id);
    }

    /**
     * Gets the manager for ship batches, means templates for ships that can be
     * provided faster.
     *
     * @return the current manager
     */
    public ShipBatchManager getBatchManager() {
        return batchManager;
    }

    /**
     * Updates the current level which is always bound to a gameplay.
     *
     * @param tpf the time per frame
     */
    public void updateLevel(float tpf) {
        // if level wasnt loaded properly exit!
        if (!levelGenerator.isLevelLoaded()) {
            return;
        }

        //<editor-fold defaultstate="collapsed" desc="Camera Reposit from roman">
        // some code to reposition camera dependant on mouse position
        // added by roman

        /*Vector2f cp=SolarWarsApplication.getInstance().getInputManager().getCursorPosition();
         * Camera cam = SolarWarsApplication.getInstance().getCamera();
         * Vector3f loc=cam.getLocation().clone();
         * loc.x=-(float)(cp.x-512)*0.002f;
         * loc.z=(float)(cp.y-384)*0.002f-6.0f;
         * cam.setLocation(loc);
         * cam.lookAt(new Vector3f(0,-2.0f,0), new Vector3f(0,1,0));*/
        //</editor-fold>

        // update background of level
        levelGenerator.getBackground().update(tpf);
        // update current gameplay (eg player win or lost, the time)
        gameplay.update(tpf);
        // update all planets
        for (Map.Entry<Integer, AbstractPlanet> entry : getPlanetSet()) {
            entry.getValue().updatePlanet(tpf);
        }
        // recalculate if new batches for ships are needed
        batchManager.refreshBatchSize(tpf);
        // update all the ship-groups
        for (Map.Entry<Integer, ShipGroup> entry : shipGroupList.entrySet()) {
            entry.getValue().updateShipGroup(tpf);
        }
        // update all the ships in all groups
        for (Map.Entry<Integer, AbstractShip> entry : shipList.entrySet()) {
            entry.getValue().updateShip(tpf);
        }
        // destroy ships not needed
        for (AbstractShip s : removeShipsList) {
            shipList.remove(s.getId());
        }
        // clear ships
        removeShipsList.clear();
    }

    public void initGameplay(AbstractGameplay gameplay) {
        this.gameplay = gameplay;
    }

    /**
     * Destroy.
     */
    public void destroy() {
        control.removeShootable(levelNode);

        this.rootNode.detachChild(labelNode);
        levelGenerator.dispose();
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

        // discard needed system refs
        this.rootNode = null;
        this.assetManager = null;
        this.control = null;
        this.gameplay.destroy();
        this.gameplay = null;
    }
}
