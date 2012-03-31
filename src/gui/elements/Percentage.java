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
 * File: Percentage.java
 * Type: gui.elements.Percentage
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.GameGUI;
import solarwars.Hub;

/**
 * The Class Percentage.
 */
public class Percentage extends Label {

    /**
     * Instantiates a new percentage.
     *
     * @param gui the gui
     */
    public Percentage(GameGUI gui) {
        super("Percentage: " + (int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%",
               new Vector3f(
                    gui.getWidth()/2, 
                    9.5f * gui.getHeight()/10, 
                    0), 
                new Vector3f(0.75f, 0.75f, 1), 
                ColorRGBA.Orange, gui);
        
//        font = FontLoader.getInstance().getFont("SolarWarsFont32");
//        text = new BitmapText(font, false);
//        text.setText();
//        text.setColor(ColorRGBA.Orange);
//        
//        Vector3f scale = new Vector3f(0.5f, 0.5f, 1);
//        Vector3f pos = new Vector3f(
//                gui.getWidth()/2 - text.getLineWidth()/2, 
//                gui.getHeight() - text.getLineHeight(), 
//                0);
//        pos.multLocal(scale);
//        
//        setLocalTranslation(pos);
//        text.setLocalScale(scale);
//        
//        this.attachChild(text);
//        
//        setVisible(true);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        text.setText("Percentage: " + (int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%");
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /* (non-Javadoc)
     * @see gui.elements.Label#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        
    }
}
