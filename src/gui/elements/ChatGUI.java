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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: ChatGUI.java
 * Type: gui.elements.ChatGUI
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.GUIElement;
import gui.GameGUI;
import java.util.ArrayList;

/**
 * The Class ChatGUI.
 */
public class ChatGUI extends GUIElement {

    /** The gui. */
    private GameGUI gui;
    
    /** The text area. */
    private Panel textArea;
    
    /** The chat log. */
    private ArrayList<String> chatLog;
    
    /**
     * Instantiates a new chat gui.
     *
     * @param gui the gui
     */
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

    
    
    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        textArea.updateGUI(tpf);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        textArea.setVisible(show);
    }
    
    
    
}
