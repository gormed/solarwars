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
 * File: GeneralActionMessage.java
 * Type: com.solarwars.net.messages.GeneralActionMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.solarwars.logic.PlayerState;

/**
 * The Class GeneralActionMessage.
 *
 * @author Hans
 */
@Serializable
public class GeneralActionMessage extends AbstractMessage {


    /** The sender. */
    private int sender;
    
    /** The sender state. */
    private PlayerState senderState;
    
    /** The reciever. */
    private int reciever;
    
    /** The reciever state. */
    private PlayerState recieverState;
    
    /** The action name. */
    private String actionName;

    /**
     * Gets the action name.
     *
     * @return the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Gets the reciever.
     *
     * @return the reciever
     */
    public int getReciever() {
        return reciever;
    }

    /**
     * Gets the reciever state.
     *
     * @return the reciever state
     */
    public PlayerState getRecieverState() {
        return recieverState;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public int getSender() {
        return sender;
    }

    /**
     * Gets the sender state.
     *
     * @return the sender state
     */
    public PlayerState getSenderState() {
        return senderState;
    }

    /**
     * Instantiates a new general action message.
     */
    public GeneralActionMessage() {
    }

    /**
     * Instantiates a new general action message.
     *
     * @param actionName the action name
     * @param sender the sender
     * @param senderState the sender state
     * @param reciever the reciever
     * @param recieverState the reciever state
     */
    public GeneralActionMessage(String actionName, int sender, PlayerState senderState, int reciever, PlayerState recieverState) {
        this.sender = sender;
        this.senderState = senderState;
        this.reciever = reciever;
        this.recieverState = recieverState;
        this.actionName = actionName;
    }
}
