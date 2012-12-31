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
 * Project: Solarwars Project
 * File: ControllerItem.java
 * Type: com.solarwars.gamestates.gui.ControllerItem
 * 
 * Documentation created: 31.12.2012 - 12:57:44 by Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
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
