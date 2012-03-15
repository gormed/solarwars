/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: GUIElement.java
 * Type: gui.GUIElement
 * 
 * Documentation created: 15.03.2012 - 20:36:19 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import com.jme3.scene.Node;

/**
 * The Class GUIElement.
 */
public abstract class GUIElement extends Node {

    /**
     * Updates the gui.
     *
     * @param tpf the tpf
     */
    public abstract void updateGUI(float tpf);
    
    /**
     * Sets the visible.
     *
     * @param show the new visible
     */
    public abstract void setVisible(boolean show);
}
