/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.GUIElement;
import gui.GameGUI;
import java.util.ArrayList;

/**
 *
 * @author Hans
 */
public class ChatGUI extends GUIElement {

    private GameGUI gui;
    private Panel textArea;
    
    private ArrayList<String> chatLog;
    
    public ChatGUI(GameGUI gui) {
        this.gui = gui;
        
        textArea = new Panel("TextArea", 
                new Vector3f(
                        gui.getWidth() / 10,
                        gui.getHeight() / 10, 
                        0), 
                new Vector2f(
                        gui.getWidth() / 2,
                        gui.getHeight() / 4), 
                ColorRGBA.White);
        attachChild(textArea);
        
        chatLog = new ArrayList<String>();
    }

    
    
    @Override
    public void updateGUI(float tpf) {
        textArea.updateGUI(tpf);
    }

    @Override
    public void setVisible(boolean show) {
        textArea.setVisible(show);
    }
    
    
    
}
