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
 * File: ChatItem.java
 * Type: com.solarwars.gamestates.gui.ChatItem
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
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
