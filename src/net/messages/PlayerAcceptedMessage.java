/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import logic.Player;

/**
 *
 * @author Hans
 */
@Serializable
public class PlayerAcceptedMessage extends AbstractMessage {

    private Player player;
    
    private boolean isHost;

    public Player getPlayer() {
        return player;
    }

    public PlayerAcceptedMessage() {
    }

    public PlayerAcceptedMessage(Player player, boolean isHost) {
        this.player = player;
        this.isHost = isHost;
    }
}
