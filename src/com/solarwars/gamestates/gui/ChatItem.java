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
 * File: ChatItem.java
 * Type: com.solarwars.gamestates.gui.ChatItem
 * 
 * Documentation created: 13.09.2012 - 19:12:39 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.math.ColorRGBA;

/**
 * The class ChatItem.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class ChatItem {

    public enum ChatMsgType {

        SERVER,
        LEAVER,
        JOINS,
        DEFEAT,
        WIN,
        PLAYER
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private String message;
    private final ChatMsgType type;
    private final String name;
    private final ColorRGBA color;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public ChatItem(String message, ChatMsgType type, String name, ColorRGBA color) {
        this.message = message;
        this.type = type;
        this.name = name;
        this.color = color;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatMsgType getType() {
        return type;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
    
}
