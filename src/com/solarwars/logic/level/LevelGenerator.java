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
 * File: LevelGenerator.java
 * Type: com.solarwars.logic.level.LevelGenerator
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.level;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.solarwars.SolarWarsApplication;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.SimplePlanet;
import com.solarwars.entities.LevelBackground;
import com.solarwars.logic.AbstractGameplay;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * The class LevelGenerator.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public abstract class LevelGenerator {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The Constant PLANET_SPACE_HORIZ. */
    public static final float PLANET_SPACE_HORIZ = 1.0F;
    /** The Constant PLANET_SPACE_VERT. */
    public static final float PLANET_SPACE_VERT = 0.5F;
    /** The level. */
    protected Level level;
    /*Spielerplaneten - vektorielle Koordinaten */
    protected Stack<Vector2f> playerPlanetPositions = new Stack<Vector2f>();
    /* Randomizer for the level, keeps the same behavior on all clients */
    protected Random randomizer;
    protected AssetManager assetManager;
    /** The background. */
    protected LevelBackground background;
    /** Indicates that the level is fully loaded into scene-graph. */
    protected boolean levelLoaded = false;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Instantiates a new level generator.
     *
     * @param hull the hull
     */
    public LevelGenerator(Level level) {
        this.level = level;
        this.assetManager = SolarWarsApplication.getInstance().getAssetManager();
    }

    public abstract boolean generate(long seed);

    public boolean isLevelLoaded() {
        return levelLoaded;
    }
    
    /**
     * Generate size.
     *
     * @return the int
     */
    protected int generateSize() {
        return randomizer.nextInt(AbstractGameplay.PLANET_SIZES.length);
    }

    /**
     * Gets the random pos.
     *
     * @return the random pos
     */
    private Vector2f getRandomPos() {
        boolean found = false;
        while (!found && !playerPlanetPositions.isEmpty()) {
            int idx = randomizer.nextInt(playerPlanetPositions.size());
            if (playerPlanetPositions.get(idx) != null) {
                Vector2f rand = playerPlanetPositions.get(idx);
                playerPlanetPositions.remove(idx);
                return rand;
            }
        }
        return null;
    }

    /**
     * Creates the player positions.
     */
    protected void createPlayerPositions() {
        if (level.getPlayersByID().size() > playerPlanetPositions.size()) {
            return;
        }
        for (Map.Entry<Integer, Player> entrySet : level.getPlayersByID().entrySet()) {
            Player p = entrySet.getValue();
            Vector2f v = getRandomPos();
            createPlayerPlanet(p, v.x, v.y);
        }
    }

    /**
     * Gets the random ship count.
     *
     * @param size the size
     * @return the random ship count
     */
    protected int getRandomShipCount(int size) {
        return randomizer.nextInt((int) ((size + 1) * 1.5F))
                + (int) (20 / AbstractGameplay.PLANET_INCREMENT_TIME[size]);
    }

    /**
     * Creates the planet.
     *
     * @param x the x
     * @param z the z
     * @return the abstract planet
     */
    protected AbstractPlanet createPlanet(float x, float z) {
        int size = generateSize();
        AbstractPlanet p =
                new SimplePlanet(assetManager,
                level, new Vector3f(x, 0, z), size);
        p.createPlanet();
        p.setShipCount(getRandomShipCount(size));
        level.getPlanetList().put(p.getID(), p);
        level.getFreePlanetsNode().attachChild(p);
        return p;
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
    protected AbstractPlanet createPlanet(int size, float x,
            float z, int shipCount) {
        AbstractPlanet p = new SimplePlanet(assetManager, level, new Vector3f(x, 0, z), size);
        p.createPlanet();
        p.setShipCount(shipCount);
        level.getPlanetList().put(p.getID(), p);
        level.getFreePlanetsNode().attachChild(p);
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
    private AbstractPlanet createPlayerPlanet(Player owner,
            float x, float z) {
        // set size to maximum
        int size = AbstractGameplay.PLANET_SIZES.length - 1;
        // create SimplePlanet for player on given position
        AbstractPlanet p = 
                new SimplePlanet(assetManager, level, 
                        new Vector3f(x, 0, z), size);
        // init planet geometry
        p.createPlanet();
        // set ships
        p.setShipCount(AbstractGameplay.PLAYER_START_SHIP_COUNT);
        // owner aquires planet
        owner.capturePlanet(p);
        // add planet into list
        level.getPlanetList().put(p.getID(), p);
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
    private void setupPlayer(Player p, AbstractPlanet startPlanet) {
        Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
        level.getPlanetNodes().put(p, playersPlanetsNode);
        level.getLevelNode().attachChild(playersPlanetsNode);
        level.getFreePlanetsNode().detachChild(startPlanet);
        playersPlanetsNode.attachChild(startPlanet);
    }

    /**
     * Setup players.
     *
     * @param players the players
     */
    protected void setupPlayers(HashMap<Integer, Player> players) {
        for (Map.Entry<Integer, Player> entrySet : players.entrySet()) {
            Player p = entrySet.getValue();
            Node playersPlanetsNode = new Node(p.getName() + " Planets Node");
            level.getPlanetNodes().put(p, playersPlanetsNode);
            level.getLevelNode().attachChild(playersPlanetsNode);
            AbstractPlanet randomPlanet = getRandomPlanet(p);
            level.getFreePlanetsNode().detachChild(randomPlanet);
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
        int idx = randomizer.nextInt(level.getPlanetList().size());
        AbstractPlanet planet = level.getPlanetList().get(idx);
        while (!found) {
            // TODO: change level generation
            if (planet.getOwner() == null && planet.getSize() >= 
                    AbstractGameplay.PLANET_SIZES[AbstractGameplay.PLANET_SIZES.length - 2]) {
                found = true;
                break;
            }
            idx = randomizer.nextInt(level.getPlanetList().size());
            planet = level.getPlanetList().get(idx);
        }
        p.capturePlanet(planet);
        planet.setShipCount(AbstractGameplay.PLAYER_START_SHIP_COUNT);
        return planet;
    }
    
    public LevelBackground getBackground() {
        return background;
    }

    public Random getRandomizer() {
        return randomizer;
    }
    
    public abstract void dispose();
}
