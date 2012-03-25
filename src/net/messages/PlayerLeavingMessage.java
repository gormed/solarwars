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
public class PlayerLeavingMessage extends AbstractMessage {

    private Player player;
    private boolean closeConnection = false;

    public boolean getCloseConnection() {
        return closeConnection;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerLeavingMessage() {
    }

    public PlayerLeavingMessage(Player player) {
        this.player = player;
    }

    public PlayerLeavingMessage(boolean closeConnection) {
        this.closeConnection = closeConnection;
    }
}
