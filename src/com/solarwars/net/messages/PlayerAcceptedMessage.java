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
 * File: PlayerAcceptedMessage.java
 * Type: com.solarwars.net.messages.PlayerAcceptedMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.solarwars.logic.Player;

import java.util.ArrayList;

/**
 * The Class PlayerAcceptedMessage.
 */
@Serializable
public class PlayerAcceptedMessage extends AbstractMessage {

    /** The player. */
    private Player player;
    
    /** The is host. */
    private boolean isHost;
    
    /** The is connecting. */
    private boolean isConnecting;
    
    /** The all players. */
    private ArrayList<Player> allPlayers;


    /**
     * Checks if is checks if is host.
     *
     * @return true, if is checks if is host
     */
    public boolean isIsHost() {
        return isHost;
    }

    /**
     * Gets the players.
     *
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return allPlayers;
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if is connecting.
     *
     * @return true, if is connecting
     */
    public boolean isConnecting() {
        return isConnecting;
    }

    /**
     * Instantiates a new player accepted message.
     */
    public PlayerAcceptedMessage() {
    }

    /**
     * Instantiates a new player accepted message.
     *
     * @param player the player
     * @param allPlayers the all players
     * @param isHost the is host
     * @param isConnecting the is connecting
     */
    public PlayerAcceptedMessage(Player player,
            ArrayList<Player> allPlayers,
            boolean isHost,
            boolean isConnecting) {
        this.player = player;
        this.isHost = isHost;
        this.allPlayers = allPlayers;
        this.isConnecting = isConnecting;
    }
}
