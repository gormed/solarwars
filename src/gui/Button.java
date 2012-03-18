/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import solarwars.FontLoader;

/**
 *
 * @author Hans
 */
public abstract class Button extends GUIElement implements ClickableGUI {

    /** The font. */
    protected BitmapFont font;
    /** The text. */
    protected BitmapText text;
    
    protected ColorRGBA color;
    /** The gui. */
    private GameGUI gui;
    private Vector3f screenPosition;
    private Vector3f scale;
    protected String title;

    public Button(String title, Vector3f position, Vector3f scale, ColorRGBA color, GameGUI gui) {
        super();//title + "_Button");
        screenPosition = position;
        this.color = color;
        this.scale = scale;
        this.title = title;
        createButton(gui);
    }

    private void createButton(GameGUI gui) {
        this.gui = gui;

        font = FontLoader.getInstance().getFont("SolarWarsFont32");
        text = new BitmapText(font, false);
        text.setText(title);
        text.setColor(color);
        Vector3f offset = new Vector3f(-text.getLineWidth() / 2, 
                -text.getLineHeight(), 
                0);
        offset.multLocal(scale);
        setLocalTranslation(screenPosition.add(offset));
        setLocalScale(scale);
        this.attachChild(text);
        setVisible(true);
    }

    @Override
    public abstract void updateGUI(float tpf);

    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

}