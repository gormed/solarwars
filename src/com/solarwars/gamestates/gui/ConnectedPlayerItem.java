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
 * File: ConnectedPlayerItem.java
 * Type: com.solarwars.gamestates.gui.ConnectedPlayerItem
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.math.ColorRGBA;

/**
 * The class ConnectedPlayerItem.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class ConnectedPlayerItem {
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private String name;
    private ColorRGBA color;
    private boolean ready = false;
    private float handycap = 1;
//    private Player player;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public ConnectedPlayerItem(String name, ColorRGBA color, boolean ready) {
        this.name = name;
        this.color = color;
        this.ready = ready;
//        this.player = player;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public float getHandycap() {
        return handycap;
    }

//    public Player getPlayer() {
//        return player;
//    }
    

    public boolean isReady() {
        return ready;
    }

    public void setHandycap(float handycap) {
        this.handycap = handycap;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
