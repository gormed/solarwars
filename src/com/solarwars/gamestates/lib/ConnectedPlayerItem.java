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
 * File: ConnectedPlayerItem.java
 * Type: com.solarwars.gamestates.lib.ConnectedPlayerItem
 * 
 * Documentation created: 07.09.2012 - 22:13:25 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.lib;

import com.jme3.math.ColorRGBA;

/**
 * The class ConnectedPlayerItem.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
class ConnectedPlayerItem {
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private String name;
    private ColorRGBA color;
    private boolean ready = false;
    private float handycap = 1;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public ConnectedPlayerItem(String name, ColorRGBA color) {
        this.name = name;
        this.color = color;
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
