/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import solarwars.Hub;

/**
 *
 * @author Hans
 */
public class Percentage extends GUIElement {

    private BitmapFont font;
    private BitmapText text;
    private GameGUI gui;

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

    @Override
    public void updateGUI(float tpf) {
        text.setText("Percentage: " + (int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%");
    }

    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }
}
