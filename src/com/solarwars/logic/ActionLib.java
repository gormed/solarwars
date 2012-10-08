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

import com.solarwars.Hub;
import com.solarwars.SolarWarsGame;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.ShipGroup;
import com.solarwars.logic.actions.GeneralAction;
import com.solarwars.logic.actions.GeneralActionListener;
import com.solarwars.logic.actions.PlanetAction;
import com.solarwars.logic.actions.PlanetActionListener;
import com.solarwars.logic.actions.PlayerLostListener;
import com.solarwars.logic.actions.PlayerWinsListener;
import com.solarwars.logic.actions.ShipActionListener;
import com.solarwars.logic.actions.ShipGroupAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * The Class ActionLib.
 */
public class ActionLib {

    private HashMap<String, GeneralAction> generalActions;
    private HashMap<String, PlanetAction> planetActions;
    private HashMap<String, ShipGroupAction> shipActions;
    private static ActionLib instance;
    private static final Logger logger = Logger.getLogger(ActionLib.class.getName());
    // TODO: finish listener implementation
    private HashSet<PlanetActionListener> planetActionMessageListeners =
            new HashSet<PlanetActionListener>();
    private HashSet<ShipActionListener> shipActionMessageListeners =
            new HashSet<ShipActionListener>();
    private HashSet<GeneralActionListener> generalActionMessageListeners =
            new HashSet<GeneralActionListener>();
    protected HashSet<PlayerLostListener> playerLostListeners =
            new HashSet<PlayerLostListener>();
    protected HashSet<PlayerWinsListener> playerWinsListeners =
            new HashSet<PlayerWinsListener>();

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

    HashSet<PlayerLostListener> getPlayerLostListeners() {
        return playerLostListeners;
    }

    HashSet<PlayerWinsListener> getPlayerWinsListeners() {
        return playerWinsListeners;
    }

    /**
     * Invoke general action.
     *
     * @param sender the sender
     * @param player1 the a
     * @param player2 the b
     * @param actionName the action name
     */
    public void invokeGeneralAction(Object sender, Player player1, Player player2,
            String actionName) {
        for (GeneralActionListener gal : generalActionMessageListeners) {
            gal.onGeneralAction(sender, player1, player2, actionName);
        }
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
     * @param sender the sender
     * @param planet the planet
     * @param p the p
     * @param actionName the action name
     */
    public void invokePlanetAction(Object sender,
            AbstractPlanet planet, Player p,
            String actionName) {
        for (PlanetActionListener pal : planetActionMessageListeners) {
            pal.onPlanetAction(sender, planet, p, actionName);
        }
        // if invoked from other client dont resend
        if (sender instanceof MultiplayerGameplay) {
            if (!planetActions.get(actionName).doAction(sender, planet,
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
            if (!planetActions.get(actionName).doAction(sender, planet,
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
     * Invoke ship action on client. This message wont go over network!
     *
     * @param sender the sender
     * @param shipGroup the ship group
     * @param p the p
     * @param actionName the action name
     */
    public void invokeShipAction(Object sender,
            ShipGroup shipGroup, Player p,
            String actionName) {
        for (ShipActionListener sal : shipActionMessageListeners) {
            sal.onShipAction(sender, shipGroup, p, actionName);
        }
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

    void invokePlayerLost(Player player, float tpf) {
        for (PlayerLostListener pll : playerLostListeners) {
            pll.onPlayerLost(player, tpf);
        }
        if (Hub.getLastPlayer() != null) {
            SolarWarsGame.getCurrentGameplay().getCurrentLevel().setGameOver(true);
        }
    }

    void invokePlayerWins(Player player, float tpf) {
        for (PlayerWinsListener pwl : playerWinsListeners) {
            pwl.onPlayerWins(player, tpf);
        }
        if (Hub.isLastPlayer()) {
            SolarWarsGame.getCurrentGameplay().getCurrentLevel().setGameOver(true);
        }
        Hub.getLocalPlayer().setDefeatedPlayer(-1);
    }

    //==========================================================================
    //          Action Listener Handling
    //==========================================================================
    public void addPlanetActionListener(PlanetActionListener pal) {
        planetActionMessageListeners.add(pal);
    }

    public void addShipActionListener(ShipActionListener sal) {
        shipActionMessageListeners.add(sal);
    }

    public void addGeneralActionListener(GeneralActionListener gal) {
        generalActionMessageListeners.add(gal);
    }

    public void addPlayerLostListener(PlayerLostListener pll) {
        playerLostListeners.add(pll);
    }

    public void addPlayerWinsListener(PlayerWinsListener pwl) {
        playerWinsListeners.add(pwl);
    }

    public void removePlanetActionListener(PlanetActionListener pal) {
        planetActionMessageListeners.remove(pal);
    }

    public void removeShipActionListener(ShipActionListener sal) {
        shipActionMessageListeners.remove(sal);
    }

    public void removeGeneralActionListener(GeneralActionListener gal) {
        generalActionMessageListeners.remove(gal);
    }

    public void removePlayerLostListener(PlayerLostListener pll) {
        playerLostListeners.remove(pll);
    }

    public void removePlayerWinsListener(PlayerWinsListener pwl) {
        playerWinsListeners.remove(pwl);
    }
}
