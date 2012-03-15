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
 * File: Percentage.java
 * Type: gui.Percentage
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import solarwars.Hub;

/**
 * The Class Percentage.
 */
public class Percentage extends GUIElement {

    /** The font. */
    private BitmapFont font;
    
    /** The text. */
    private BitmapText text;
    
    /** The gui. */
    private GameGUI gui;

    /**
     * Instantiates a new percentage.
     *
     * @param gui the gui
     */
    public Percentage(GameGUI gui) {
        this.gui = gui;
        font = gui.game.getApplication().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(font, false);
        text.setText("Percentage: " + (int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%");
        text.setColor(ColorRGBA.Cyan);
        
        text.setLocalTranslation(gui.getWidth()/2 - text.getLineWidth()/2, gui.getHeight() - text.getLineHeight(), 0);
        
        this.attachChild(text);
        setVisible(true);
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
}
