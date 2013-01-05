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
 * File: PlanetActionMessage.java
 * Type: com.solarwars.net.messages.PlanetActionMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.solarwars.logic.PlayerState;

/**
 * The Class PlanetActionMessage.
 *
 * @author Hans
 */
@Serializable
public class PlanetActionMessage extends AbstractMessage {

    /** The client time. */
    private long clientTime;
    /** The server time. */
    private long serverTime;
    /** The action name. */
    private String actionName;
    /** The player id. */
    private int playerID;
    /** The planet id. */
    private int planetID;
    //private int planetShips;
    /** The player state. */
    private PlayerState playerState;

    /**
     * Gets the action name.
     *
     * @return the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Gets the client time.
     *
     * @return the client time
     */
    public long getClientTime() {
        return clientTime;
    }

    /**
     * Gets the player id.
     *
     * @return the player id
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Gets the server time.
     *
     * @return the server time
     */
    public long getServerTime() {
        return serverTime;
    }

    /**
     * Gets the planet id.
     *
     * @return the planet id
     */
    public int getPlanetID() {
        return planetID;
    }

    /**
     * Gets the player state.
     *
     * @return the player state
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

//    public int getPlanetShips() {
//        return planetShips;
//    }
    /**
     * Instantiates a new planet action message.
     */
    public PlanetActionMessage() {
    }

    /**
     * Instantiates a new planet action message.
     *
     * @param clientTime the client time
     * @param actionName the action name
     * @param playerID the player id
     * @param state the state
     * @param planetID the planet id
     */
    public PlanetActionMessage(long clientTime, String actionName, int playerID, PlayerState state, int planetID) {
        this.clientTime = clientTime;
        this.actionName = actionName;
        this.playerID = playerID;
        this.planetID = planetID;
        this.playerState = state;
//        this.planetShips = planetShips;
    }

    /**
     * Instantiates a new planet action message.
     *
     * @param clientTime the client time
     * @param serverTime the server time
     * @param actionName the action name
     * @param playerID the player id
     * @param state the state
     * @param planetID the planet id
     */
    public PlanetActionMessage(long clientTime, long serverTime, String actionName, int playerID, PlayerState state, int planetID) {
        this.clientTime = clientTime;
        this.serverTime = serverTime;
        this.actionName = actionName;
        this.playerID = playerID;
        this.planetID = planetID;
        this.playerState = state;
//        this.planetShips = planetShips;
    }
}
