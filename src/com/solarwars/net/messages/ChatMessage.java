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
 * File: ChatMessage.java
 * Type: com.solarwars.net.messages.ChatMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * The Class ChatMessage.
 *
 * @author Hans
 */
@Serializable
public class ChatMessage extends AbstractMessage {

    /** The player id. */
    private int playerID;
    
    /** The message. */
    private String message;

    /**
     * Instantiates a new chat message.
     */
    public ChatMessage() {
    }

    /**
     * Instantiates a new chat message.
     *
     * @param playerID the player id
     * @param message the message
     */
    public ChatMessage(int playerID, String message) {
        this.playerID = playerID;
        this.message = message;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the player id.
     *
     * @return the player id
     */
    public int getPlayerID() {
        return playerID;
    }
}
