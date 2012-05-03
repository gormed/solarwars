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
 * File: KeyboardListener.java
 * Type: gui.KeyboardListener
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import gui.elements.TextBox;

/**
 * The listener interface for receiving keyboard events.
 * The class that is interested in processing a keyboard
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addKeyboardListener<code> method. When
 * the keyboard event occurs, that object's appropriate
 * method is invoked.
 *
 * @see KeyboardEvent
 */
public abstract class KeyboardListener implements ActionListener {

    /** The mappings added. */
    private static boolean mappingsAdded = false;
    protected InputManager inputManager;
    /** The text box. */
    protected TextBox textBox;

    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Instantiates a new keyboard listener.
     */
    public KeyboardListener() {
    }

    /**
     * Instantiates a new keyboard listener.
     *
     * @param inputManager the input manager
     */
    public KeyboardListener(InputManager inputManager, TextBox textBox) {
        this.textBox = textBox;
        this.inputManager = inputManager;
        //deleteMappings(inputManager);
        if (!mappingsAdded) {
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_0, new KeyTrigger(KeyInput.KEY_0), new KeyTrigger(KeyInput.KEY_NUMPAD0));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_1, new KeyTrigger(KeyInput.KEY_1), new KeyTrigger(KeyInput.KEY_NUMPAD1));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_2, new KeyTrigger(KeyInput.KEY_2), new KeyTrigger(KeyInput.KEY_NUMPAD2));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_3, new KeyTrigger(KeyInput.KEY_3), new KeyTrigger(KeyInput.KEY_NUMPAD3));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_4, new KeyTrigger(KeyInput.KEY_4), new KeyTrigger(KeyInput.KEY_NUMPAD4));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_5, new KeyTrigger(KeyInput.KEY_5), new KeyTrigger(KeyInput.KEY_NUMPAD5));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_6, new KeyTrigger(KeyInput.KEY_6), new KeyTrigger(KeyInput.KEY_NUMPAD6));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_7, new KeyTrigger(KeyInput.KEY_7), new KeyTrigger(KeyInput.KEY_NUMPAD7));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_8, new KeyTrigger(KeyInput.KEY_8), new KeyTrigger(KeyInput.KEY_NUMPAD8));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_9, new KeyTrigger(KeyInput.KEY_9), new KeyTrigger(KeyInput.KEY_NUMPAD9));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_A, new KeyTrigger(KeyInput.KEY_A));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_B, new KeyTrigger(KeyInput.KEY_B));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_C, new KeyTrigger(KeyInput.KEY_C));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_D, new KeyTrigger(KeyInput.KEY_D));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_E, new KeyTrigger(KeyInput.KEY_E));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_F, new KeyTrigger(KeyInput.KEY_F));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_G, new KeyTrigger(KeyInput.KEY_G));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_H, new KeyTrigger(KeyInput.KEY_H));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_I, new KeyTrigger(KeyInput.KEY_I));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_J, new KeyTrigger(KeyInput.KEY_J));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_K, new KeyTrigger(KeyInput.KEY_K));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_L, new KeyTrigger(KeyInput.KEY_L));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_M, new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_N, new KeyTrigger(KeyInput.KEY_N));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_O, new KeyTrigger(KeyInput.KEY_O));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_P, new KeyTrigger(KeyInput.KEY_P));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_Q, new KeyTrigger(KeyInput.KEY_Q));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_R, new KeyTrigger(KeyInput.KEY_R));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_S, new KeyTrigger(KeyInput.KEY_S));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_T, new KeyTrigger(KeyInput.KEY_T));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_U, new KeyTrigger(KeyInput.KEY_U));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_V, new KeyTrigger(KeyInput.KEY_V));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_W, new KeyTrigger(KeyInput.KEY_W));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_X, new KeyTrigger(KeyInput.KEY_X));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_Y, new KeyTrigger(KeyInput.KEY_Y));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_Z, new KeyTrigger(KeyInput.KEY_Z));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_SCORE, new KeyTrigger(KeyInput.KEY_MINUS));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_SPACE, new KeyTrigger(KeyInput.KEY_SPACE));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_UNDERSCORE, new KeyTrigger(KeyInput.KEY_UNDERLINE));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_BACKSPACE, new KeyTrigger(KeyInput.KEY_BACK), new KeyTrigger(KeyInput.KEY_DELETE));

            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_POINT,
                    new KeyTrigger(KeyInput.KEY_PERIOD),
                    new KeyTrigger(KeyInput.KEY_NUMPADCOMMA));
//            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_QUESTIONMARK,
//                    new KeyTrigger(KeyInput.KEY_PERIOD));
            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_COMMA,
                    new KeyTrigger(KeyInput.KEY_COMMA));
//            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_EXCREMATIONMARK,
//                    new KeyTrigger(KeyInput.KEY_COMMA));
            mappingsAdded = true;
        }

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
                KeyInputMap.INPUT_MAPPING_A,
                KeyInputMap.INPUT_MAPPING_B,
                KeyInputMap.INPUT_MAPPING_C,
                KeyInputMap.INPUT_MAPPING_D,
                KeyInputMap.INPUT_MAPPING_E,
                KeyInputMap.INPUT_MAPPING_F,
                KeyInputMap.INPUT_MAPPING_G,
                KeyInputMap.INPUT_MAPPING_H,
                KeyInputMap.INPUT_MAPPING_I,
                KeyInputMap.INPUT_MAPPING_J,
                KeyInputMap.INPUT_MAPPING_K,
                KeyInputMap.INPUT_MAPPING_L,
                KeyInputMap.INPUT_MAPPING_M,
                KeyInputMap.INPUT_MAPPING_N,
                KeyInputMap.INPUT_MAPPING_O,
                KeyInputMap.INPUT_MAPPING_P,
                KeyInputMap.INPUT_MAPPING_Q,
                KeyInputMap.INPUT_MAPPING_R,
                KeyInputMap.INPUT_MAPPING_S,
                KeyInputMap.INPUT_MAPPING_T,
                KeyInputMap.INPUT_MAPPING_U,
                KeyInputMap.INPUT_MAPPING_V,
                KeyInputMap.INPUT_MAPPING_W,
                KeyInputMap.INPUT_MAPPING_X,
                KeyInputMap.INPUT_MAPPING_Y,
                KeyInputMap.INPUT_MAPPING_Z,
                KeyInputMap.INPUT_MAPPING_SCORE,
                KeyInputMap.INPUT_MAPPING_SPACE,
                KeyInputMap.INPUT_MAPPING_UNDERSCORE,
                KeyInputMap.INPUT_MAPPING_BACKSPACE,
                KeyInputMap.INPUT_MAPPING_POINT,
                KeyInputMap.INPUT_MAPPING_QUESTIONMARK,
                KeyInputMap.INPUT_MAPPING_COMMA,
                KeyInputMap.INPUT_MAPPING_EXCREMATIONMARK);

    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    public abstract void onAction(String name, boolean isPressed, float tpf);
}
