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
 * Type: com.solarwars.logic.ActionLib
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import java.util.HashMap;
import java.util.logging.Logger;

import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.ShipGroup;


/**
 * The Class ActionLib.
 */
public class ActionLib {

    private HashMap<String, GeneralAction> generalActions;
    private HashMap<String, PlanetAction> planetActions;
    private HashMap<String, ShipGroupAction> shipActions;
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
     * @param sender
     *            the sender
     * @param player1
     *            the a
     * @param player2
     *            the b
     * @param actionName
     *            the action name
     */
    public void invokeGeneralAction(Object sender, Player player1, Player player2,
            String actionName) {
        // if invoked from other client dont resend
        if (sender instanceof MultiplayerGameplay) {
            if (!generalActions.get(actionName).doAction(sender, player1, player2)) {
                return;
            }
            final String generalActionMsg = "GeneralAction invoked for another "
                    + "player via Network, type is "
                    + actionName
                    + " from #"
                    + player1.getId() + "/" + player1.getName();
            logger.log(java.util.logging.Level.FINE, generalActionMsg,
                    new Object[]{player1, player2});
            // else if invoked from this client, send via network to server
        } else {
            if (!generalActions.get(actionName).doAction(sender, player1, player2)) {
                return;
            }
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendGeneralActionMessage(actionName, player1, player2);
            }
            final String generalActionMsg = "GeneralAction invoked for local player"
                    + ", type is "
                    + actionName
                    + " from #"
                    + player1.getId()
                    + "/"
                    + player1.getName();
            logger.log(java.util.logging.Level.FINE, generalActionMsg,
                    new Object[]{player1, player2});
        }
    }

    /**
     * Invoke planet action.
     * 
     * @param sender
     *            the sender
     * @param planet
     *            the planet
     * @param p
     *            the p
     * @param actionName
     *            the action name
     */
    public void invokePlanetAction(Object sender, long delay,
            AbstractPlanet planet, Player p,
            String actionName) {
        // if invoked from other client dont resend
        if (sender instanceof MultiplayerGameplay) {
            if (!planetActions.get(actionName).doAction(sender, delay, planet,
                    p)) {
                return;
            }
            String planetActionMsg = "PlanetAction invoked for another "
                    + "player via Network, type is " + actionName;
            if (p != null && planet != null) {
                planetActionMsg += " from #" + p.getId() + "/" + p.getName()
                        + " to planet #" + planet.getId();
            }
            logger.log(java.util.logging.Level.FINE, planetActionMsg,
                    new Object[]{planet, p});
            // else if invoked from this client, send via network to server
        } else {
            if (!planetActions.get(actionName).doAction(sender, delay, planet,
                    p)) {
                return;
            }
            if (MultiplayerGameplay.isInitialized()) {
                MultiplayerGameplay.getInstance().sendPlanetActionMessage(actionName, planet);
            }
            String planetActionMsg = "PlanetAction invoked for local player, "
                    + "type is " + actionName;
            if (p != null && planet != null) {
                planetActionMsg += " from #" + p.getId() + "/" + p.getName()
                        + " to planet #" + planet.getId();
            }
            logger.log(java.util.logging.Level.FINE, planetActionMsg,
                    new Object[]{planet, p});
        }
    }

    /**
     * Invoke ship action.
     * 
     * @param sender
     *            the sender
     * @param shipGroup
     *            the ship group
     * @param p
     *            the p
     * @param actionName
     *            the action name
     */
    public void invokeShipAction(Object sender, long delay,
            ShipGroup shipGroup, Player p,
            String actionName) {
        if (!shipActions.get(actionName).doAction(sender, shipGroup, p)) {
            return;
        }
        final String shipActionMsg = "ShipAction is invoked for local player, type "
                + actionName
                + " for player #"
                + p.getId()
                + "/"
                + p.getName()
                + " for shipgroup #"
                + ((shipGroup != null) ? shipGroup.getId() : "MultiSelect");
        logger.log(java.util.logging.Level.FINE, shipActionMsg, new Object[]{shipGroup, p});
    }
}
