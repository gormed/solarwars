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
 * File: ActionLib.java
 * Type: logic.ActionLib
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.HashMap;
import java.util.logging.Logger;
import solarwars.SolarWarsApplication;

/**
 * The Class ActionLib.
 */
public class ActionLib {

    /** The general actions. */
    private HashMap<String, GeneralAction> generalActions;
    /** The planet actions. */
    private HashMap<String, PlanetAction> planetActions;
    /** The ship actions. */
    private HashMap<String, ShipGroupAction> shipActions;
    /** The instance. */
    private static ActionLib instance;
    private static final Logger logger = Logger.getLogger(ActionLib.class.getName());

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
        logger.setLevel(SolarWarsApplication.GLOBAL_LOGGING_LEVEL);
        logger.setParent(SolarWarsApplication.getClientLogger());
        logger.setUseParentHandlers(true);
        generalActions = new HashMap<String, GeneralAction>();
        planetActions = new HashMap<String, PlanetAction>();
        shipActions = new HashMap<String, ShipGroupAction>();
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
    public HashMap<String, ShipGroupAction> getShipActions() {
        return shipActions;
    }

    /**
     * Invoke general action.
     *
     * @param sender the sender
     * @param a the a
     * @param b the b
     * @param actionName the action name
     */
    public void invokeGeneralAction(Object sender, Player a, Player b, String actionName) {
        // if invoked from other client dont resend
        if (sender instanceof MultiplayerGameplay) {
            generalActions.get(actionName).doAction(sender, a, b);
            final String generalActionMsg = "GeneralAction invoked for another "
                    + "player via Network, type is " + actionName + " from #"
                    + a.getId() + "/" + a.getName();
            logger.log(java.util.logging.Level.FINE, generalActionMsg,
                    new Object[]{a, b});
            // else if invoked from this client, send via network to server
        } else {
            generalActions.get(actionName).doAction(sender, a, b);
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendGeneralActionMessage(actionName, a, b);
            }
            final String generalActionMsg = "GeneralAction invoked for local player"
                    + ", type is " + actionName + " from #"
                    + a.getId() + "/" + a.getName();
            logger.log(java.util.logging.Level.FINE, generalActionMsg,
                    new Object[]{a, b});
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
    public void invokePlanetAction(Object sender, long delay, AbstractPlanet planet, Player p, String actionName) {
        // if invoked from other client dont resend
        if (sender instanceof MultiplayerGameplay) {
            planetActions.get(actionName).doAction(sender, delay, planet, p);
            String planetActionMsg = "PlanetAction invoked for another "
                    + "player via Network, type is " + actionName;
            if (p != null && planet != null) {
                planetActionMsg +=
                        " from #" + p.getId() + "/"
                        + p.getName() + " to planet #" + planet.getId();
            }
            logger.log(java.util.logging.Level.FINE, planetActionMsg,
                    new Object[]{planet, p});
            // else if invoked from this client, send via network to server
        } else {
            planetActions.get(actionName).doAction(sender, delay, planet, p);
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendPlanetActionMessage(actionName, planet);
            }
            String planetActionMsg = "PlanetAction invoked for local player, "
                    + "type is " + actionName;
            if (p != null && planet != null) {
                planetActionMsg +=
                        " from #" + p.getId() + "/"
                        + p.getName() + " to planet #" + planet.getId();
            }
            logger.log(java.util.logging.Level.FINE, planetActionMsg,
                    new Object[]{planet, p});
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
    public void invokeShipAction(Object sender, long delay, ShipGroup shipGroup, Player p, String actionName) {
        shipActions.get(actionName).doAction(sender, shipGroup, p);
        final String shipActionMsg =
                "ShipAction is invoked for local player, type " + actionName
                + " for player #" + p.getId() + "/" + p.getName()
                + " for shipgroup #" + shipGroup.getId();
        logger.log(java.util.logging.Level.FINE,
                shipActionMsg, new Object[]{shipGroup, p});
    }
}
