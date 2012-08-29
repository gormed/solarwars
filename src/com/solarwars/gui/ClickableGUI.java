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
 * File: ClickableGUI.java
 * Type: com.solarwars.gui.ClickableGUI
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui;

import com.jme3.math.Vector2f;

/**
 * The Interface ClickableGUI.
 */
public interface ClickableGUI {
    
    /**
     * On click.
     *
     * @param cursor the cursor
     * @param isPressed the is pressed
     * @param tpf the tpf
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf);
    
    /**
     * Can gain focus.
     *
     * @return true, if successful
     */
    public boolean canGainFocus();
}