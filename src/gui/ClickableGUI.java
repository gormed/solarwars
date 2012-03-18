/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.math.Vector2f;

/**
 *
 * @author Hans
 */
public interface ClickableGUI {
    public void onClick(Vector2f cursor, boolean isPressed, float tpf);
}
