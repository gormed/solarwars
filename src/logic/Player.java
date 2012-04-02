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
 * File: Player.java
 * Type: logic.Player
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.ArrayList;

/**
 * The Class Player.
 */
@Serializable
public class Player {
    
    public static final ColorRGBA[] PLAYER_COLORS = {
        ColorRGBA.Blue, ColorRGBA.Red,
        ColorRGBA.Green, ColorRGBA.Orange,
        ColorRGBA.Yellow, ColorRGBA.Cyan,
        ColorRGBA.Brown, ColorRGBA.Magenta};

    /**
     * Invokes Game over action if player has lost game.
     * @param victorious
     * @param defeated 
     */
    private static void reportPlayerLost(Player victorious, Player defeated) {
        if (defeated != null && !defeated.hasShips()) {
            defeated.setLost();
            ActionLib.getInstance().
                    invokeGeneralAction(null, victorious, defeated, Gameplay.GAME_OVER);
        }
    }
    /** The id. */
    private int id;
    /** The artificial. */
    private AI artificial;
    /** The planets. */
    private ArrayList<AbstractPlanet> planets;
    /** The ship groups. */
    private ArrayList<ShipGroup> shipGroups;
    /** The is host. */
    private boolean isHost;
    /** defines the state of a player */
    private PlayerState state = new PlayerState();

    /**
     * Instantiates a new player.
     */
    public Player() {
    }

    /**
     * Instantiates a new player.
     *
     * @param name the name
     * @param color the color
     * @param id the id
     */
    public Player(String name, ColorRGBA color, int id) {
        state.name = name;
        state.color = color;
        this.id = id;
        planets = new ArrayList<AbstractPlanet>();
        shipGroups = new ArrayList<ShipGroup>();
    }

    /**
     * Instantiates a new player.
     *
     * @param name the name
     * @param color the color
     * @param id the id
     * @param isHost the is host
     */
    public Player(String name, ColorRGBA color, int id, boolean isHost) {
        state.name = name;
        state.color = color;
        this.id = id;
        planets = new ArrayList<AbstractPlanet>();
        shipGroups = new ArrayList<ShipGroup>();
    }
    
    public PlayerState getState() {
        return state;
    }
    
    void applyState(PlayerState newState) {
        this.state = newState;
    }

    /**
     * Gets the color.
     *
     * @return the color
     */
    public ColorRGBA getColor() {
        return state.color;
    }

    /**
     * Checks if is host.
     *
     * @return true, if is host
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * 
     * Returns if the player is defeated and out of game.
     * 
     * @return true if player lost, false if still playing.
     */
    public boolean hasLost() {
        return state.lost;
    }
    
    void setLost() {
        state.lost = true;
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
        return state.name;
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
        state.selectedPlanetId = p.getId();
        state.selectedShipGroupId = -1;
    }

    /**
     * Checks for selected planet.
     *
     * @return true, if successful
     */
    boolean hasSelectedPlanet() {
        return state.selectedPlanetId > -1;
    }

    /**
     * Gets the selected planet.
     *
     * @return the selected planet
     */
    AbstractPlanet getSelectedPlanet() {
        return Gameplay.getCurrentLevel().getPlanet(state.selectedPlanetId);
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
        state.shipPercentage = p;
    }

    /**
     * Gets the ship percentage.
     *
     * @return the ship percentage
     */
    public float getShipPercentage() {
        return state.shipPercentage;
    }
    
    public int getShipCount() {
        int ships = 0;
        
        for (ShipGroup sg : shipGroups) {
            ships += sg.getShipCount();
        }
        
        for (AbstractPlanet p : planets) {
            ships += p.getShipCount();
        }
        
        return ships;
    }
    
    public boolean hasShips() {
        return (getShipCount() > 0);
    }

    /**
     * Select ship group.
     *
     * @param g the g
     */
    void selectShipGroup(ShipGroup g) {
        state.selectedShipGroupId = g.getId();
        state.selectedPlanetId = -1;
    }

    /**
     * Checks for selected ship group.
     *
     * @return true, if successful
     */
    boolean hasSelectedShipGroup() {
        return state.selectedShipGroupId > -1;
    }

    /**
     * Gets the selected ship group.
     *
     * @return the selected ship group
     */
    ShipGroup getSelectedShipGroup() {
        return Gameplay.getCurrentLevel().getShipGroup(state.selectedShipGroupId);
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
        Player prevOwner = null;
        
        if (planet.hasOwner()) {
            prevOwner = planet.getOwner();
            prevOwner.uncapturePlanet(planet);
        }
        planet.setOwner(this);
        planets.add(planet);
        
        reportPlayerLost(this, prevOwner);
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
