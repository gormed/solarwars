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
 * File: GUIElement.java
 * Type: gui.GUIElement
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import com.jme3.scene.Node;

/**
 * The Class GUIElement.
 */
public abstract class GUIElement extends Node {

    /** The visible. */
    private boolean visible = true;
    
    /**
     * Updates the gui.
     *
     * @param tpf the tpf
     */
    public abstract void updateGUI(float tpf);
    
    /**
     * Sets the GUIElements visibility.
     *
     * @param show the new visible value
     */
    public void setVisible(boolean show) {
        visible = show;
    }
    
    /**
     * Retrieves the visibility of the GUIElement.
     *
     * @return true, if is visible
     */
    public boolean isVisible() {
        return visible;
    }
}
