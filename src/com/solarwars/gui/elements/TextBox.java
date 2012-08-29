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
 * File: TextBox.java
 * Type: com.solarwars.gui.elements.TextBox
 * 
 * Documentation created: 14.07.2012 - 19:38:03 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui.elements;


import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.solarwars.SolarWarsApplication;
import com.solarwars.gui.ClickableGUI;
import com.solarwars.gui.GUIElement;
import com.solarwars.gui.GameGUI;
import com.solarwars.input.KeyInputManager;
import com.solarwars.input.KeyboardListener;

/**
 * The Class TextBox.
 */
public abstract class TextBox extends GUIElement implements ClickableGUI {

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
    /** The size. */
    protected Vector2f size;
    /** The caption. */
    protected String caption;
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The box color. */
    protected ColorRGBA boxColor;
    /** The text listener. */
    protected ActionListener textListener;
    /** The time. */
    private float time;
    /** The is number box. */
    private boolean isNumberBox = false;
    /** The numeric mappings added. */
    private static boolean numericMappingsAdded = false;

    /**
     * Checks if is checks if is number box.
     *
     * @return true, if is checks if is number box
     */
    public boolean isIsNumberBox() {
        return isNumberBox;
    }

    /**
     * Sets the caption.
     *
     * @param caption the new caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Gets the caption.
     *
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Instantiates a new text box.
     *
     * @param color the color
     * @param screenPosition the screen position
     * @param scale the scale
     * @param caption the caption
     * @param boxColor the box color
     * @param gui the gui
     * @param numberBox the number box
     */
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

    /**
     * Instantiates a new text box.
     *
     * @param color the color
     * @param screenPosition the screen position
     * @param scale the scale
     * @param size the size
     * @param caption the caption
     * @param boxColor the box color
     * @param gui the gui
     * @param numberBox the number box
     */
    public TextBox(ColorRGBA color, Vector3f screenPosition,
            Vector3f scale, Vector2f size, String caption,
            ColorRGBA boxColor, GameGUI gui, boolean numberBox) {
        super();
        this.name = caption;
        this.color = color;
        this.screenPosition = screenPosition;
        this.scale = scale;
        this.caption = caption;
        this.boxColor = boxColor;
        this.gui = gui;
        this.size = size;
        createTextBox(gui);
        this.isNumberBox = numberBox;
        createListener();

    }

    /**
     * Instantiates a new text box.
     *
     * @param color the color
     * @param screenPosition the screen position
     * @param scale the scale
     * @param caption the caption
     * @param boxColor the box color
     * @param gui the gui
     */
    public TextBox(ColorRGBA color, Vector3f screenPosition,
            Vector3f scale, String caption,
            ColorRGBA boxColor, GameGUI gui) {
        super();
        this.name = caption;
        this.color = color;
        this.screenPosition = screenPosition;
        this.scale = scale;
        this.caption = caption;
        this.boxColor = boxColor;
        this.gui = gui;
        createTextBox(gui);
    }

    /**
     * Creates the listener.
     */
    private void createListener() {

        if (!isNumberBox) {
            this.textListener = new TextBoxActionListener(this);
        } else {
            this.textListener = new NumberBoxActionListener(this);
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.scene.Node#detachChild(com.jme3.scene.Spatial)
     */
//    @Override
//    public int detachChild(Spatial child) {
//
//        destroy();
//        return super.detachChild(child);
//    }
    /**
     * Destroy.
     */
    private void destroy() {
        com.solarwars.SolarWarsApplication.getInstance().getInputManager().
                removeListener(textListener);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        if (caption.length() > 0) {
            text.setText(caption);
            Vector3f offset = new Vector3f(-text.getLineWidth() / 2,
                    text.getLineHeight() / 2,
                    0);
            offset.multLocal(scale);
            text.setLocalTranslation(screenPosition.add(offset));
            text.setLocalScale(scale);
        }
        text.setSize(44f);
        text.setColor(color);
        time += tpf;

        if (gui.getFocusElement() != null
                && gui.getFocusElement().equals(this)) {

            if (time < 0.2f) {
                text.setText(caption + "_");
            } else if (time < 0.4f) {
                text.setText(caption);
            } else {
                time = 0;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.solarwars.gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

    /**
     * On key trigger.
     *
     * @param key the key
     * @param isPressed the is pressed
     * @param tpf the tpf
     */
    protected abstract void onKeyTrigger(String key, boolean isPressed, float tpf);

    /* (non-Javadoc)
     * @see com.solarwars.gui.ClickableGUI#canGainFocus()
     */
    @Override
    public boolean canGainFocus() {
        return true;
    }

    /**
     * Creates the text box.
     *
     * @param gui the gui
     */
    private void createTextBox(GameGUI gui) {
        // Init
        AssetManager assetManager =
                com.solarwars.SolarWarsApplication.getInstance().getAssetManager();

        // Create Text
        font = assetManager.loadFont(
                "Interface/Fonts/SolarWarsFont64.fnt");
        text = new BitmapText(font, false);
        text.setName("BitmapText_" + name);
        text.setText(caption);
        text.setSize(44f);
        text.setColor(color);
        Vector3f offset = new Vector3f(
                -text.getLineWidth() / 2,
                text.getLineHeight() / 2,
                0);
        offset.multLocal(scale);
        text.setLocalTranslation(screenPosition.add(offset));
        text.setLocalScale(scale);

        // Create Box

        if (size == null) {
            size = new Vector2f();
            size.x = gui.getWidth() / 5;
            size.y = text.getLineHeight() / 1.5f;
        }


        Box b = new Box(size.x, size.y, 1);
        geometry = new Geometry(caption + "_TextBox", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", boxColor);
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalScale(scale);

        attachChild(geometry);
        attachChild(text);
    }

    /**
     * The listener interface for receiving numberBoxAction events.
     * The class that is interested in processing a numberBoxAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addNumberBoxActionListener<code> method. When
     * the numberBoxAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see NumberBoxActionEvent
     */
    private static class NumberBoxActionListener extends KeyboardListener {

        /**
         * Instantiates a new number box action listener.
         *
         * @param inputManager the input manager
         * @param textBox the text box
         */
        public NumberBoxActionListener(TextBox textBox) {
            this.textBox = textBox;
            this.inputManager = SolarWarsApplication.getInstance().getInputManager();
            addNumberListener(inputManager);
        }

        private void addNumberListener(InputManager inputManager) {
            if (inputManager == null) return;
            inputManager.addListener(this,
                    KeyInputManager.INPUT_MAPPING_0,
                    KeyInputManager.INPUT_MAPPING_1,
                    KeyInputManager.INPUT_MAPPING_2,
                    KeyInputManager.INPUT_MAPPING_3,
                    KeyInputManager.INPUT_MAPPING_4,
                    KeyInputManager.INPUT_MAPPING_5,
                    KeyInputManager.INPUT_MAPPING_6,
                    KeyInputManager.INPUT_MAPPING_7,
                    KeyInputManager.INPUT_MAPPING_8,
                    KeyInputManager.INPUT_MAPPING_9,
                    KeyInputManager.INPUT_MAPPING_POINT,
                    KeyInputManager.INPUT_MAPPING_BACKSPACE);
        }

        /* (non-Javadoc)
         * @see com.solarwars.gui.KeyboardListener#onAction(java.lang.String, boolean, float)
         */
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            GUIElement e = textBox.gui.getFocusElement();
            TextBox activeTextBox = null;
            if (e instanceof TextBox) {
                activeTextBox = (TextBox) e;
            } else {
                return;
            }

            if (!isPressed && activeTextBox.equals(textBox)) {
                if (name.equals(KeyInputManager.INPUT_MAPPING_BACKSPACE)
                        && textBox.caption.length() >= 1) {
                    textBox.caption =
                            textBox.caption.substring(0,
                            textBox.caption.length() - 1);
                } else if (!name.equals(KeyInputManager.INPUT_MAPPING_BACKSPACE)
                        && textBox.caption.length() < 15) {
                    textBox.caption += name;
                }

            }
            if (activeTextBox.equals(textBox)) {
                textBox.onKeyTrigger(name, isPressed, tpf);
            }
        }
    }

    /**
     * The listener interface for receiving textBoxAction events.
     * The class that is interested in processing a textBoxAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addTextBoxActionListener<code> method. When
     * the textBoxAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see TextBoxActionEvent
     */
    private static class TextBoxActionListener extends KeyboardListener {

        /**
         * Instantiates a new text box action listener.
         *
         * @param inputManager the input manager
         * @param box the box
         */
        public TextBoxActionListener(TextBox box) {
            super(box);
        }

        /* (non-Javadoc)
         * @see com.solarwars.gui.KeyboardListener#onAction(java.lang.String, boolean, float)
         */
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {

            GUIElement e = textBox.gui.getFocusElement();
            TextBox activeTextBox = null;
            if (e instanceof TextBox) {
                activeTextBox = (TextBox) e;
            } else {
                return;
            }

            if (!isPressed && activeTextBox.equals(textBox)) {
                if (name.equals(KeyInputManager.INPUT_MAPPING_BACKSPACE)
                        && textBox.caption.length() >= 1) {
                    textBox.caption =
                            textBox.caption.substring(0, textBox.caption.length() - 1);

                } else {
                    textBox.caption += name;
                }
            }
            if (activeTextBox.equals(textBox)) {
                textBox.onKeyTrigger(name, isPressed, tpf);
            }
        }
    }
}
