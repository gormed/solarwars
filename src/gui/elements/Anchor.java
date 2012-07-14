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
 * File: Anchor.java
 * Type: gui.elements.Anchor
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.math.Vector2f;
import gui.ClickableGUI;
import gui.GUIElement;
import java.util.ArrayList;

/**
 * The Class Anchor.
 *
 * @author Hans
 */
public class Anchor extends GUIElement implements ClickableGUI {

    /** The attached gui elements. */
    private ArrayList<GUIElement> attachedGUIElements = new ArrayList<GUIElement>();

    /**
     * Adds the element.
     *
     * @param e the e
     */
    public void addElement(GUIElement e) {
        attachedGUIElements.add(e);
        attachChild(e);
    }

    /**
     * Removes the element.
     *
     * @param e the e
     */
    public void removeElement(GUIElement e) {
        attachedGUIElements.remove(e);
        detachChild(e);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        ArrayList<GUIElement> clone = new ArrayList<GUIElement>(attachedGUIElements);
        for (GUIElement element : clone) {
            element.updateGUI(tpf);
        }
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        ArrayList<GUIElement> clone = new ArrayList<GUIElement>(attachedGUIElements);
        for (GUIElement element : clone) {
            if (element instanceof ClickableGUI) {
                ((ClickableGUI) element).onClick(cursor, isPressed, tpf);
            }
        }
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#canGainFocus()
     */
    public boolean canGainFocus() {
        return false;
    }
}
