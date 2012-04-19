/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.math.Vector2f;
import gui.ClickableGUI;
import gui.GUIElement;
import java.util.ArrayList;

/**
 *
 * @author Hans
 */
public class Anchor extends GUIElement implements ClickableGUI {

    private ArrayList<GUIElement> attachedGUIElements = new ArrayList<GUIElement>();

    public void addElement(GUIElement e) {
        attachedGUIElements.add(e);
        attachChild(e);
    }

    public void removeElement(GUIElement e) {
        attachedGUIElements.remove(e);
        detachChild(e);
    }

    @Override
    public void updateGUI(float tpf) {
        ArrayList<GUIElement> clone = new ArrayList<GUIElement>(attachedGUIElements);
        for (GUIElement element : clone) {
            element.updateGUI(tpf);
        }
    }

    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        ArrayList<GUIElement> clone = new ArrayList<GUIElement>(attachedGUIElements);
        for (GUIElement element : clone) {
            if (element instanceof ClickableGUI) {
                ((ClickableGUI) element).onClick(cursor, isPressed, tpf);
            }
        }
    }

    public boolean canGainFocus() {
        return false;
    }
}
