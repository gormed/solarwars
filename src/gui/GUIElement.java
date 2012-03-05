/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.scene.Node;

/**
 *
 * @author Hans
 */
public abstract class GUIElement extends Node {

    public abstract void updateGUI(float tpf);
    
    public abstract void setVisible(boolean show);
}
