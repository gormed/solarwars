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
 * File: KeyboardListener.java
 * Type: gui.KeyboardListener
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package input;

import com.jme3.input.InputManager;
import com.jme3.input.InputManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.ActionListener;
import gui.elements.TextBox;
import solarwars.SolarWarsApplication;

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
    
    /** The input manager. */
    protected InputManager inputManager;
    /** The text box. */
    protected TextBox textBox;

    /**
     * Gets the input manager.
     *
     * @return the input manager
     */
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
     * @param textBox the text box
     */
    public KeyboardListener(TextBox textBox) {
        this.textBox = textBox;
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        addTextListener();

    }

    private void addTextListener() {
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
                KeyInputManager.INPUT_MAPPING_A,
                KeyInputManager.INPUT_MAPPING_B,
                KeyInputManager.INPUT_MAPPING_C,
                KeyInputManager.INPUT_MAPPING_D,
                KeyInputManager.INPUT_MAPPING_E,
                KeyInputManager.INPUT_MAPPING_F,
                KeyInputManager.INPUT_MAPPING_G,
                KeyInputManager.INPUT_MAPPING_H,
                KeyInputManager.INPUT_MAPPING_I,
                KeyInputManager.INPUT_MAPPING_J,
                KeyInputManager.INPUT_MAPPING_K,
                KeyInputManager.INPUT_MAPPING_L,
                KeyInputManager.INPUT_MAPPING_M,
                KeyInputManager.INPUT_MAPPING_N,
                KeyInputManager.INPUT_MAPPING_O,
                KeyInputManager.INPUT_MAPPING_P,
                KeyInputManager.INPUT_MAPPING_Q,
                KeyInputManager.INPUT_MAPPING_R,
                KeyInputManager.INPUT_MAPPING_S,
                KeyInputManager.INPUT_MAPPING_T,
                KeyInputManager.INPUT_MAPPING_U,
                KeyInputManager.INPUT_MAPPING_V,
                KeyInputManager.INPUT_MAPPING_W,
                KeyInputManager.INPUT_MAPPING_X,
                KeyInputManager.INPUT_MAPPING_Y,
                KeyInputManager.INPUT_MAPPING_Z,
                KeyInputManager.INPUT_MAPPING_SCORE,
                KeyInputManager.INPUT_MAPPING_SPACE,
                KeyInputManager.INPUT_MAPPING_UNDERSCORE,
                KeyInputManager.INPUT_MAPPING_BACKSPACE,
                KeyInputManager.INPUT_MAPPING_POINT,
                KeyInputManager.INPUT_MAPPING_QUESTIONMARK,
                KeyInputManager.INPUT_MAPPING_COMMA,
                KeyInputManager.INPUT_MAPPING_EXCREMATIONMARK);
    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public abstract void onAction(String name, boolean isPressed, float tpf);
}
