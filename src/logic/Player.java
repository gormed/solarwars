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
 * File: Player.java
 * Type: logic.Player
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.ArrayList;
import java.util.Map;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 * The Class Player.
 */
@Serializable
public class Player {

    /** The Constant PLAYER_COLORS. */
    public static final ColorRGBA[] PLAYER_COLORS = {
        ColorRGBA.Blue, ColorRGBA.Red,
        ColorRGBA.Green, ColorRGBA.LightGray,
        ColorRGBA.Yellow, ColorRGBA.Cyan,
        new ColorRGBA(0.2f, 0.0f, 0.5f, 1.0f), ColorRGBA.Magenta};

    /**
     * Gets the unused color.
     *
     * @param players the players
     * @param start the start
     * @return the unused color
     */
    public static ColorRGBA getUnusedColor(ArrayList<Player> players, int start) {
        ColorRGBA color = ColorRGBA.randomColor();
        for (int i = start; i < PLAYER_COLORS.length; i++) {
            color = PLAYER_COLORS[i];
            for (Player p : players) {
                if (p.getColor().equals(color)) {
                    continue;
                }
            }
            break;
        }

        return color;
    }

    /**
     * Invokes Game over action if player has lost game.
     *
     * @param victorious the victorious
     * @param defeated the defeated
     */
    static void reportPlayerLost(Player victorious, Player defeated) {
        if (defeated != null && !defeated.hasShips() && !defeated.hasLost()) {

            ActionLib.getInstance().
                    invokeGeneralAction(null, victorious, defeated, DeathmatchGameplay.GAME_OVER);
        }
    }

    /**
     * Last player.
     *
     * @return true, if successful
     */
    static boolean lastPlayer() {
        int lostPlayerCount = 0;
        for (Map.Entry<Integer, Player> entry : Hub.playersByID.entrySet()) {
            Player p = entry.getValue();
            if (p != null) {
                if (p.hasLost()) {
                    lostPlayerCount++;
                }
            }
        }
        return Hub.playersByID.size() - 1 == lostPlayerCount;
    }

    /**
     * Local player wins.
     */
    static void localPlayerWins() {
        if (lastPlayer()) {
            SolarWarsGame.getInstance().getCurrentLevel().setGameOver(true);
        } else {
            //TODO: Display: "You defeated..."
        }

    }

    /**
     * Local player looses.
     */
    static void localPlayerLooses() {
        if (lastPlayer()) {
            SolarWarsGame.getInstance().getCurrentLevel().setGameOver(true);
        }
    }
    private static boolean hostSet = false;
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
    /** defines the state of a player. */
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

    /**
     * Gets the state.
     *
     * @return the state
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Apply state.
     *
     * @param newState the new state
     */
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

    public boolean setHost() {
        if (hostSet) {
            return false;
        }
        return isHost = hostSet = true;
    }

    /**
     * 
     * Returns if the player is defeated and out of game.
     * 
     * @return true if player lost, false if still playing.
     */
    public boolean hasLost() {
        return state.hasLost;
    }

    /**
     * Sets the lost.
     */
    void setLost() {
        state.hasLost = true;
    }

    /**
     * Gets the defeated player.
     *
     * @return the defeated player
     */
    public int getDefeatedPlayer() {
        return state.defeatedPlayerID;
    }

    /**
     * Sets the defeated player.
     *
     * @param id the new defeated player
     */
    void setDefeatedPlayer(int id) {
        state.defeatedPlayerID = id;
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
     * Clear multi select.
     */
    void clearPlanetMultiSelect() {
        state.multiSelectedPlanets.clear();
    }

    /**
     * Multi select planets.
     *
     * @param planets the planets
     */
    void multiSelectPlanets(ArrayList<AbstractPlanet> planets) {
        state.multiSelectedPlanets.clear();
        state.multiSelectedShipGroups.clear();
        state.selectedPlanetId = -1;
        state.selectedShipGroupId = -1;

        ArrayList<Integer> planetIDs = new ArrayList<Integer>();
        for (AbstractPlanet p : planets) {
            planetIDs.add(p.getId());
        }
        state.multiSelectedPlanets = planetIDs;
    }

    /**
     * Checks for multi selected planets.
     *
     * @return true, if successful
     */
    boolean hasMultiSelectedPlanets() {
        return state.multiSelectedPlanets != null && !state.multiSelectedPlanets.isEmpty();
    }

    /**
     * Gets the multi select planets.
     *
     * @return the multi select planets
     */
    ArrayList<AbstractPlanet> getMultiSelectPlanets() {
        Level current = SolarWarsGame.getInstance().getCurrentLevel();
        ArrayList<AbstractPlanet> aps = new ArrayList<AbstractPlanet>();
        for (Integer i : state.multiSelectedPlanets) {
            aps.add(current.getPlanet(i));
        }
        return aps;
    }

    /**
     * Select planet.
     *
     * @param p the p
     */
    void selectPlanet(AbstractPlanet p) {
        state.multiSelectedPlanets.clear();
        state.multiSelectedShipGroups.clear();
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
        return SolarWarsGame.getInstance().
                getCurrentLevel().getPlanet(state.selectedPlanetId);
    }

    /**
     * Refresh ship percentage.
     *
     * @param p the p
     */
    public void refreshShipPercentage(float p) {
        if (hasLost()) {
            return;
        }
        float percentage = state.shipPercentage + p;

        if (percentage > 1.0f) {
            percentage = 1.0f;
        }
        if (percentage < 0.0f) {
            percentage = 0.0f;
        }
        state.shipPercentage = percentage;
    }

    /**
     * Sets the ship percentage.
     *
     * @param p the new ship percentage
     */
    @Deprecated
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

    /**
     * Gets the ship count.
     *
     * @return the ship count
     */
    public int getShipCount() {
        int ships = 0;

        for (ShipGroup sg : shipGroups) {
            ships += sg.getShipCount();
        }

        for (AbstractPlanet p : planets) {
            ships += p.getShipCount();
        }

        if (ships == 0 && planets.size() > 0) {
            return 1;
        }

        return ships;
    }

    public boolean isLeaver() {
        return state.leaver;
    }

    public void setLeaver(boolean value) {
        state.leaver = value;
    }

    /**
     * Gets the planet count.
     *
     * @return the planet count
     */
    public int getPlanetCount() {
        return planets.size();
    }

    /**
     * Checks for ships.
     *
     * @return true, if successful
     */
    public boolean hasShips() {
        return (getShipCount() > 0);
    }

    /**
     * Clear multi select.
     */
    void clearShipGroupMultiSelect() {
        state.multiSelectedShipGroups.clear();
    }

    /**
     * Multi select planets.
     *
     * @param planets the planets
     */
    void multiSelectShipGroup(ArrayList<ShipGroup> shipGroupSelction) {

        state.multiSelectedPlanets.clear();
        state.multiSelectedShipGroups.clear();
        state.selectedPlanetId = -1;
        state.selectedShipGroupId = -1;

        ArrayList<Integer> shipGroupIDs = new ArrayList<Integer>();
        for (ShipGroup s : shipGroups) {
            shipGroupIDs.add(s.getId());
        }
        state.multiSelectedShipGroups = shipGroupIDs;
    }

    /**
     * Checks for multi selected planets.
     *
     * @return true, if successful
     */
    boolean hasMultiSelectedShipGroups() {
        return state.multiSelectedShipGroups != null && !state.multiSelectedShipGroups.isEmpty();
    }

    /**
     * Gets the multi select planets.
     *
     * @return the multi select planets
     */
    ArrayList<ShipGroup> getMultiSelectShipGroups() {
        ArrayList<ShipGroup> sgs = new ArrayList<ShipGroup>();
        Level current = SolarWarsGame.getInstance().getCurrentLevel();
        for (Integer i : state.multiSelectedShipGroups) {
            sgs.add(current.getShipGroup(i));
        }
        return sgs;
    }

    /**
     * Select ship group.
     *
     * @param g the g
     */
    void selectShipGroup(ShipGroup g) {
        state.multiSelectedPlanets.clear();
        state.multiSelectedShipGroups.clear();
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
        return SolarWarsGame.getInstance().
                getCurrentLevel().getShipGroup(state.selectedShipGroupId);
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
     *  TODO: Change back to package visibility
     * @return the planets
     */
    public ArrayList<AbstractPlanet> getPlanets() {
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
