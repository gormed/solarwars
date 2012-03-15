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
 * File: Player.java
 * Type: logic.Player
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.ColorRGBA;
import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.ArrayList;

/**
 * The Class Player.
 */
public class Player {

    /** The PLAYE r_ id. */
    private static int PLAYER_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    private static int getContiniousID() {
        return PLAYER_ID++;
    }
    
    /** The name. */
    private String name;
    
    /** The color. */
    private ColorRGBA color;
    
    /** The id. */
    private int id;
    
    /** The ship count. */
    private int shipCount = 0;
    
    /** The artificial. */
    private AI artificial;
    
    /** The selected planet. */
    private AbstractPlanet selectedPlanet;
    
    /** The selected ship group. */
    private ShipGroup selectedShipGroup;
    
    /** The ship percentage. */
    private float shipPercentage = 0.5f;
    
    /** The planets. */
    private ArrayList<AbstractPlanet> planets;
    
    /** The ship groups. */
    private ArrayList<ShipGroup> shipGroups;

    /**
     * Instantiates a new player.
     *
     * @param name the name
     * @param color the color
     */
    public Player(String name, ColorRGBA color) {
        this.name = name;
        this.color = color;
        this.id = getContiniousID();
        planets = new ArrayList<AbstractPlanet>();
        shipGroups = new ArrayList<ShipGroup>();
    }

    /**
     * Gets the color.
     *
     * @return the color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the player.
     */
    public void updatePlayer() {
    }

    /**
     * Select planet.
     *
     * @param p the p
     */
    void selectPlanet(AbstractPlanet p) {
        selectedPlanet = p;
        selectedShipGroup = null;
    }

    /**
     * Checks for selected planet.
     *
     * @return true, if successful
     */
    boolean hasSelectedPlanet() {
        return selectedPlanet != null;
    }

    /**
     * Gets the selected planet.
     *
     * @return the selected planet
     */
    AbstractPlanet getSelectedPlanet() {
        return selectedPlanet;
    }

    /**
     * Sets the ship percentage.
     *
     * @param p the new ship percentage
     */
    public void setShipPercentage(float p) {
        if (p > 1.0f) {
            p = 1.0f;
        }
        if (p < 0.0f) {
            p = 0.0f;
        }
        shipPercentage = p;
    }

    /**
     * Gets the ship percentage.
     *
     * @return the ship percentage
     */
    public float getShipPercentage() {
        return shipPercentage;
    }

    /**
     * Select ship group.
     *
     * @param g the g
     */
    void selectShipGroup(ShipGroup g) {
        selectedShipGroup = g;
        selectedPlanet = null;
    }

    /**
     * Checks for selected ship group.
     *
     * @return true, if successful
     */
    boolean hasSelectedShipGroup() {
        return selectedShipGroup != null;
    }

    /**
     * Gets the selected ship group.
     *
     * @return the selected ship group
     */
    ShipGroup getSelectedShipGroup() {
        return selectedShipGroup;
    }

    /**
     * Checks if is aI.
     *
     * @return true, if is aI
     */
    public boolean isAI() {
        return artificial != null;
    }

    /**
     * Gets the aI.
     *
     * @return the aI
     */
    AI getAI() {
        return artificial;
    }

    /**
     * Sets the aI.
     *
     * @param ai the new aI
     */
    void setAI(AI ai) {
        artificial = ai;
    }

    /**
     * Creates the ship group.
     *
     * @param sg the sg
     */
    void createShipGroup(ShipGroup sg) {
        shipGroups.add(sg);
    }

    /**
     * Destroy ship group.
     *
     * @param sg the sg
     */
    void destroyShipGroup(ShipGroup sg) {
        shipGroups.remove(sg);
    }

    /**
     * Capture planet.
     *
     * @param planet the planet
     */
    void capturePlanet(AbstractPlanet planet) {
        if (planet.hasOwner()) {
            planet.getOwner().uncapturePlanet(planet);
        }
        planet.setOwner(this);
        planets.add(planet);
    }

    /**
     * Uncapture planet.
     *
     * @param planet the planet
     */
    void uncapturePlanet(AbstractPlanet planet) {
        planets.remove(planet);
    }

    /**
     * Gets the planets.
     *
     * @return the planets
     */
    ArrayList<AbstractPlanet> getPlanets() {
        return planets;
    }

    /**
     * Gets the ship groups.
     *
     * @return the ship groups
     */
    ArrayList<ShipGroup> getShipGroups() {
        return shipGroups;
    }
}
