/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import logic.Player;

/**
 *
 * @author Hans
 */
@Serializable
public class PlayerAcceptedMessage extends AbstractMessage {

    private Player player;
    private boolean isHost;
    private ArrayList<Player> allPlayers;


    public boolean isIsHost() {
        return isHost;
    }

    public ArrayList<Player> getPlayers() {
        return allPlayers;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerAcceptedMessage() {
    }

    public PlayerAcceptedMessage(Player player,
            ArrayList<Player> allPlayers,
            boolean isHost) {
        this.player = player;
        this.isHost = isHost;
        this.allPlayers = allPlayers;
    }
}
