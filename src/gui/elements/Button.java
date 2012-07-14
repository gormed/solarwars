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
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: SolarWars
 * File: Button.java
 * Type: gui.elements.Button
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import gui.GameGUI;

/**
 * The Class Button.
 */
public abstract class Button extends Label {

    /** The Constant BUTTON_COLOR. */
    private static final ColorRGBA BUTTON_COLOR =
            new ColorRGBA(0.0f, 0.0f, 1.0f, 0.75f);
    
    /** The Constant BUTTON_FRAME_COLOR. */
    private static final ColorRGBA BUTTON_FRAME_COLOR =
            new ColorRGBA(1f, 1f, 1f, 0.75f);
    
    /** The Constant BUTTON_FONT_COLOR. */
    private static final ColorRGBA BUTTON_FONT_COLOR = ColorRGBA.White.clone();
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The button color. */
    protected ColorRGBA buttonColor;
    
    /** The frame. */
    protected Panel frame;

    /**
     * Instantiates a new button.
     *
     * @param title the title
     * @param position the position
     * @param scale the scale
     * @param textColor the text color
     * @param buttonColor the button color
     * @param gui the gui
     */
    public Button(String title, Vector3f position, Vector3f scale, ColorRGBA textColor, ColorRGBA buttonColor, GameGUI gui) {
        super(title, position, scale, BUTTON_FONT_COLOR, gui);
        this.buttonColor = BUTTON_COLOR;
        createButton();
    }

    /**
     * Creates the button.
     */
    private void createButton() {
        AssetManager assetManager = solarwars.SolarWarsApplication.getInstance().getAssetManager();

        float[] size = new float[2];

        size[0] = text.getLineWidth() / 1.65f;
        size[1] = text.getLineHeight() / 1.7f;

        Box b = new Box(size[0], size[1], 1);
        geometry = new Geometry(title + "_Button", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", buttonColor);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalScale(scale);
        
        frame = new Panel(
                "Button_Frame", 
                screenPosition, 
                new Vector2f(size[0]+4, size[1]+4), 
                BUTTON_FRAME_COLOR);

        detachChild(text);
        attachChild(frame);
        attachChild(geometry);
        attachChild(text);
    }

    /* (non-Javadoc)
     * @see gui.elements.Label#canGainFocus()
     */
    @Override
    public boolean canGainFocus() {
        return true;
    }
    

    /* (non-Javadoc)
     * @see gui.elements.Label#updateGUI(float)
     */
    @Override
    public abstract void updateGUI(float tpf);

    /* (non-Javadoc)
     * @see gui.elements.Label#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

    /* (non-Javadoc)
     * @see gui.elements.Label#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        frame.setVisible(show);
        
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }
}
