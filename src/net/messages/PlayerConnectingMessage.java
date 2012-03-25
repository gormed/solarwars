/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.math.ColorRGBA;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Hans
 */
@Serializable
public class PlayerConnectingMessage extends AbstractMessage {

    private String name;
    private ColorRGBA color;
    private boolean isHost;

    public ColorRGBA getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public boolean isHost() {
        return isHost;
    }

    public PlayerConnectingMessage() {
    }

    public PlayerConnectingMessage(String name, ColorRGBA color, boolean isHost) {
        this.name = name;
        this.color = color;
        this.isHost = isHost;
    }
}
