/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import logic.PlayerState;

/**
 *
 * @author Hans
 */
@Serializable
public class GeneralActionMessage extends AbstractMessage {


    private int sender;
    private PlayerState senderState;
    private int reciever;
    private PlayerState recieverState;
    private String actionName;

    public String getActionName() {
        return actionName;
    }

    public int getReciever() {
        return reciever;
    }

    public PlayerState getRecieverState() {
        return recieverState;
    }

    public int getSender() {
        return sender;
    }

    public PlayerState getSenderState() {
        return senderState;
    }

    public GeneralActionMessage() {
    }

    public GeneralActionMessage(String actionName, int sender, PlayerState senderState, int reciever, PlayerState recieverState) {
        this.sender = sender;
        this.senderState = senderState;
        this.reciever = reciever;
        this.recieverState = recieverState;
        this.actionName = actionName;
    }
}
