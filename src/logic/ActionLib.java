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
 * File: ActionLib.java
 * Type: logic.ActionLib
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.HashMap;

/**
 * The Class ActionLib.
 */
public class ActionLib {

    /** The general actions. */
    private HashMap<String, GeneralAction> generalActions;
    /** The planet actions. */
    private HashMap<String, PlanetAction> planetActions;
    /** The ship actions. */
    private HashMap<String, ShipAction> shipActions;
    /** The instance. */
    private static ActionLib instance;

    /**
     * Gets the single instance of ActionLib.
     *
     * @return single instance of ActionLib
     */
    public static ActionLib getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new ActionLib();
    }

    /**
     * Instantiates a new action lib.
     */
    private ActionLib() {
        generalActions = new HashMap<String, GeneralAction>();
        planetActions = new HashMap<String, PlanetAction>();
        shipActions = new HashMap<String, ShipAction>();
    }

    /**
     * Initializes the.
     */
    public void initialize() {
    }

    /**
     * Gets the general actions.
     *
     * @return the general actions
     */
    public HashMap<String, GeneralAction> getGeneralActions() {
        return generalActions;
    }

    /**
     * Gets the planet actions.
     *
     * @return the planet actions
     */
    public HashMap<String, PlanetAction> getPlanetActions() {
        return planetActions;
    }

    /**
     * Gets the ship actions.
     *
     * @return the ship actions
     */
    public HashMap<String, ShipAction> getShipActions() {
        return shipActions;
    }

    /**
     * Invoke general action.
     *
     * @param sender the sender
     * @param p the p
     * @param actionName the action name
     */
    public void invokeGeneralAction(Object sender, Player a, Player b, String actionName) {
        if (sender instanceof MultiplayerGameplay) {
            generalActions.get(actionName).doAction(sender, a, b);
        } else {
            generalActions.get(actionName).doAction(sender, a, b);
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendGeneralActionMessage(actionName, a, b);
            }
        }
    }

    /**
     * Invoke planet action.
     *
     * @param sender the sender
     * @param planet the planet
     * @param p the p
     * @param actionName the action name
     */
    public void invokePlanetAction(Object sender, AbstractPlanet planet, Player p, String actionName) {
        if (sender instanceof MultiplayerGameplay) {
            planetActions.get(actionName).doAction(sender, planet, p);
        } else {
            planetActions.get(actionName).doAction(sender, planet, p);
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendPlanetActionMessage(actionName, planet);
            }
        }
    }

    /**
     * Invoke ship action.
     *
     * @param sender the sender
     * @param shipGroup the ship group
     * @param p the p
     * @param actionName the action name
     */
    public void invokeShipAction(Object sender, ShipGroup shipGroup, Player p, String actionName) {
        shipActions.get(actionName).doAction(sender, shipGroup, p);
    }
}
