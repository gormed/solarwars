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
 * File: Label.java
 * Type: gui.elements.Label
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
 * The Class Label.
 */
public abstract class Label extends GUIElement implements ClickableGUI {

    /** The font. */
    protected BitmapFont font;
    
    /** The text. */
    protected BitmapText text;
    
    /** The color. */
    protected ColorRGBA color;
    
    /** The gui. */
    protected GameGUI gui;
    
    /** The screen position. */
    protected Vector3f screenPosition;
    
    /** The scale. */
    protected Vector3f scale;
    
    /** The title. */
    protected String title;

    /**
     * Instantiates a new label.
     *
     * @param title the title
     * @param position the position
     * @param scale the scale
     * @param color the color
     * @param gui the gui
     */
    public Label(String title, Vector3f position, Vector3f scale, ColorRGBA color, GameGUI gui) {
        super();//title + "_Button");
        screenPosition = position;
        this.color = color;
        this.scale = scale;
        this.title = title;
        createLabel(gui);
    }

    /**
     * Creates the label.
     *
     * @param gui the gui
     */
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

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public abstract void updateGUI(float tpf);

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }
    
    public void setCaption(String caption) {
        this.text.setText(caption);
    }
    
    public String getCaption() {
        return this.text.getText();
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

    public boolean canGainFocus() {
        return false;
    }
    
}