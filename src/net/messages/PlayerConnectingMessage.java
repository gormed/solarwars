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
 * File: PlayerConnectingMessage.java
 * Type: net.messages.PlayerConnectingMessage
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net.messages;

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
