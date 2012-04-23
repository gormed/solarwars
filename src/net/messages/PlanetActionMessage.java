/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import entities.AbstractPlanet;
import logic.PlayerState;

/**
 *
 * @author Hans
 */
@Serializable
public class PlanetActionMessage extends AbstractMessage {

    private long clientTime;
    private long serverTime;
    private String actionName;
    private int playerID;
    private int planetID;
    //private int planetShips;
    private PlayerState playerState;

    public String getActionName() {
        return actionName;
    }

    public long getClientTime() {
        return clientTime;
    }

    public int getPlayerID() {
        return playerID;
    }

    public long getServerTime() {
        return serverTime;
    }

    public int getPlanetID() {
        return planetID;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

//    public int getPlanetShips() {
//        return planetShips;
//    }

    public PlanetActionMessage() {
    }

    public PlanetActionMessage(long clientTime, String actionName, int playerID, PlayerState state, int planetID) {
        this.clientTime = clientTime;
        this.actionName = actionName;
        this.playerID = playerID;
        this.planetID = planetID;
        this.playerState = state;
//        this.planetShips = planetShips;
    }

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
