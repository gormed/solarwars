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
 * File: PlayerAcceptedMessage.java
 * Type: net.messages.PlayerAcceptedMessage
 * 
 * Documentation created: 31.03.2012 - 19:27:49 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import logic.Player;

/**
 * The Class PlayerAcceptedMessage.
 */
@Serializable
public class PlayerAcceptedMessage extends AbstractMessage {

    /** The player. */
    private Player player;
    
    /** The is host. */
    private boolean isHost;
    
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
