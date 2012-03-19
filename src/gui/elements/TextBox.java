/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import gui.KeyInputMap;
import gui.KeyboardListener;
import solarwars.FontLoader;

/**
 *
 * @author Hans
 */
public abstract class TextBox extends GUIElement implements ClickableGUI {

    /** The font. */
    protected BitmapFont font;
    /** The text. */
    protected BitmapText text;
    protected ColorRGBA color;
    /** The gui. */
    protected GameGUI gui;
    protected Vector3f screenPosition;
    protected Vector3f scale;
    protected String caption;

    public String getCaption() {
        return caption;
    }
    protected Geometry geometry;
    protected Material material;
    protected ColorRGBA boxColor;
    protected KeyboardListener textListener;
    private float time;
    private boolean isNumberBox = false;

    public boolean isIsNumberBox() {
        return isNumberBox;
    }

    public TextBox(ColorRGBA color, Vector3f screenPosition,
            Vector3f scale, String caption,
            ColorRGBA boxColor, GameGUI gui, boolean numberBox) {
        super();
        this.name = caption;
        this.color = color;
        this.screenPosition = screenPosition;
        this.scale = scale;
        this.caption = caption;
        this.boxColor = boxColor;
        this.gui = gui;
        createTextBox(gui);
        this.isNumberBox = numberBox;
        createListener();
    }

    private void createListener() {
        if (!isNumberBox) {
            this.textListener = new TextBoxActionListener(
                    solarwars.SolarWarsApplication.getInstance().getInputManager(), this);
        } else {

            this.textListener = new NumberBoxActionListener(
                    solarwars.SolarWarsApplication.getInstance().getInputManager(), this);
        }
    }

    public void destroy() {
        solarwars.SolarWarsApplication.getInstance().getInputManager().
                removeListener(textListener);
    }

    @Override
    public void updateGUI(float tpf) {
        text.setText(caption);
        Vector3f offset = new Vector3f(-text.getLineWidth() / 2,
                text.getLineHeight() / 2,
                0);
        offset.multLocal(scale);
        text.setLocalTranslation(screenPosition.add(offset));
        text.setLocalScale(scale);

        time += tpf;

        if (gui.getFocusElement() != null && gui.getFocusElement().equals(this)) {
            if (time < 0.2f) {
                text.setText(caption + "_");
            } else if (time < 0.4f) {
                text.setText(caption);
            } else {
                time = 0;
            }
        }
    }

    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);
    
    protected abstract void onKeyTrigger(String key, boolean isPressed, float tpf);

    private void createTextBox(GameGUI gui) {
        // Init
        AssetManager assetManager = solarwars.SolarWarsApplication.getInstance().getAssetManager();

        // Create Text
        font = FontLoader.getInstance().getFont("SolarWarsFont32");
        text = new BitmapText(font, false);
        text.setText(caption);
        text.setColor(color);
        Vector3f offset = new Vector3f(-text.getLineWidth() / 2,
                text.getLineHeight() / 2,
                0);
        offset.multLocal(scale);
        text.setLocalTranslation(screenPosition.add(offset));
        text.setLocalScale(scale);

        // Create Box

        float[] size = new float[2];

        size[0] = gui.getWidth() / 5;
        size[1] = text.getLineHeight() / 1.5f;

        Box b = new Box(size[0], size[1], 1);
        geometry = new Geometry(caption + "_TextBox", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", boxColor);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalScale(scale);

        attachChild(geometry);
        attachChild(text);
    }

    private class NumberBoxActionListener extends KeyboardListener {

        private TextBox textBox;

        public NumberBoxActionListener(InputManager inputManager, TextBox textBox) {
            this.textBox = textBox;

            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_0, new KeyTrigger(KeyInput.KEY_0));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_1, new KeyTrigger(KeyInput.KEY_1));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_2, new KeyTrigger(KeyInput.KEY_2));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_3, new KeyTrigger(KeyInput.KEY_3));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_4, new KeyTrigger(KeyInput.KEY_4));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_5, new KeyTrigger(KeyInput.KEY_5));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_6, new KeyTrigger(KeyInput.KEY_6));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_7, new KeyTrigger(KeyInput.KEY_7));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_8, new KeyTrigger(KeyInput.KEY_8));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_9, new KeyTrigger(KeyInput.KEY_9));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_POINT, new KeyTrigger(KeyInput.KEY_PERIOD));

            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_BACKSPACE, new KeyTrigger(KeyInput.KEY_BACK), new KeyTrigger(KeyInput.KEY_DELETE));

            inputManager.addListener(this,
                    KeyInputMap.INPUT_MAPPING_0,
                    KeyInputMap.INPUT_MAPPING_1,
                    KeyInputMap.INPUT_MAPPING_2,
                    KeyInputMap.INPUT_MAPPING_3,
                    KeyInputMap.INPUT_MAPPING_4,
                    KeyInputMap.INPUT_MAPPING_5,
                    KeyInputMap.INPUT_MAPPING_6,
                    KeyInputMap.INPUT_MAPPING_7,
                    KeyInputMap.INPUT_MAPPING_8,
                    KeyInputMap.INPUT_MAPPING_9,
                    KeyInputMap.INPUT_MAPPING_POINT,
                    KeyInputMap.INPUT_MAPPING_BACKSPACE);
        }

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            GUIElement e = gui.getFocusElement();
            TextBox activeTextBox = null;
            if (e instanceof TextBox) {
                activeTextBox = (TextBox) e;
            } else {
                return;
            }

            if (!isPressed && activeTextBox.equals(textBox)) {
                if (name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && caption.length() > 0) {
                    caption = caption.substring(0, caption.length() - 1);
                } else if (!name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && caption.length() < 15) {
                    caption += name;
                }
                onKeyTrigger(name, isPressed, tpf);
            }
        }
    }

    private class TextBoxActionListener extends KeyboardListener {

        private TextBox textBox;

        public TextBoxActionListener(InputManager inputManager, TextBox box) {
            super(inputManager);
            this.textBox = box;
        }

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {

            GUIElement e = gui.getFocusElement();
            TextBox activeTextBox = null;
            if (e instanceof TextBox) {
                activeTextBox = (TextBox) e;
            } else {
                return;
            }

            if (!isPressed && activeTextBox.equals(textBox)) {
                if (name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && caption.length() > 0) {
                    caption = caption.substring(0, caption.length() - 1);
                } else if (!name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && caption.length() < 19) {
                    caption += name;
                }
            }
        }
    }
}
