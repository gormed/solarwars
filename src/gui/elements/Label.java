/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import solarwars.FontLoader;

/**
 *
 * @author Hans
 */
public abstract class Label extends GUIElement implements ClickableGUI {

    /** The font. */
    protected BitmapFont font;
    /** The text. */
    protected BitmapText text;
    
    protected ColorRGBA color;
    /** The gui. */
    protected GameGUI gui;
    protected Vector3f screenPosition;
    protected Vector3f scale;
    protected String title;

    public Label(String title, Vector3f position, Vector3f scale, ColorRGBA color, GameGUI gui) {
        super();//title + "_Button");
        screenPosition = position;
        this.color = color;
        this.scale = scale;
        this.title = title;
        createLabel(gui);
    }

    private void createLabel(GameGUI gui) {
        this.gui = gui;

        font = FontLoader.getInstance().getFont("SolarWarsFont32");
        text = new BitmapText(font, false);
        text.setText(title);
        text.setColor(color);
        Vector3f offset = new Vector3f(-text.getLineWidth() / 2, 
                text.getLineHeight() / 2, 
                0);
        offset.multLocal(scale);
        text.setLocalTranslation(screenPosition.add(offset));
        text.setLocalScale(scale);
        attachChild(text);
    }

    @Override
    public abstract void updateGUI(float tpf);

    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

}