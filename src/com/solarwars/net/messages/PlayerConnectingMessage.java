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
 * File: PlayerConnectingMessage.java
 * Type: com.solarwars.net.messages.PlayerConnectingMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.math.ColorRGBA;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * The Class PlayerConnectingMessage.
 */
@Serializable
public class PlayerConnectingMessage extends AbstractMessage {

    /** The name. */
    private String name;
    
    /** The color. */
    private ColorRGBA color;
    
    /** The is host. */
    private boolean isHost;

    /**
     * Gets the color.
     *
     * @return the color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if is host.
     *
     * @return true, if is host
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * Instantiates a new player connecting message.
     */
    public PlayerConnectingMessage() {
    }

    /**
     * Instantiates a new player connecting message.
     *
     * @param name the name
     * @param color the color
     * @param isHost the is host
     */
    public PlayerConnectingMessage(String name, ColorRGBA color, boolean isHost) {
        this.name = name;
        this.color = color;
        this.isHost = isHost;
    }
}
