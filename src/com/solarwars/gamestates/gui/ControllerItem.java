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
 * File: ControllerItem.java
 * Type: com.solarwars.gamestates.gui.ControllerItem
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.input.Joystick;

/**
 * The class ControllerItem.
 *
 * @author Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
 */
public class ControllerItem {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private final String name;
    private final Joystick joystick;
    private final int id;
    
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public ControllerItem(String name, Joystick joystick) {
        this.name = name;
        this.joystick = joystick;
        this.id = joystick.getJoyId();
    }

    public String getName() {
        return name;
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public int getId() {
        return id;
    }
}
