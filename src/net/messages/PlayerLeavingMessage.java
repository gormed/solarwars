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
 * File: PlayerLeavingMessage.java
 * Type: net.messages.PlayerLeavingMessage
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import logic.Player;

/**
 * The Class PlayerLeavingMessage.
 */
@Serializable
public class PlayerLeavingMessage extends AbstractMessage {

    /** The player. */
    private Player player;

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Instantiates a new player leaving message.
     */
    public PlayerLeavingMessage() {
    }

    /**
     * Instantiates a new player leaving message.
     *
     * @param player the player
     */
    public PlayerLeavingMessage(Player player) {
        this.player = player;
    }
}
