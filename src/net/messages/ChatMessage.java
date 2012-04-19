/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Hans
 */
@Serializable
public class ChatMessage extends AbstractMessage {

    private int playerID;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(int playerID, String message) {
        this.playerID = playerID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getPlayerID() {
        return playerID;
    }
}
