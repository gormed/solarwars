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
 * File: KeyInputManager.java
 * Type: gui.KeyInputManager
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import solarwars.SolarWarsApplication;

/**
 * The class KeyInputManager for key input handling.
 * @author Hans
 */
public class KeyInputManager {
    //==========================================================================
    //      Singleton
    //==========================================================================

    private KeyInputManager() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            initKeyboardMappings();
        }
    }

    public static KeyInputManager getInstance() {
        return KeyInputManagerHolder.INSTANCE;
    }

    private static class KeyInputManagerHolder {

        private static final KeyInputManager INSTANCE = new KeyInputManager();
    }
    //==========================================================================
    //      Static Fields
    //==========================================================================
    public static final boolean ANDROID_BUILD = true;
    //<editor-fold defaultstate="collapsed" desc="Mapping Strings">
    /** The Constant INPUT_MAPPING_A. */
    public static final String INPUT_MAPPING_A = "A";
    /** The Constant INPUT_MAPPING_B. */
    public static final String INPUT_MAPPING_B = "B";
    /** The Constant INPUT_MAPPING_C. */
    public static final String INPUT_MAPPING_C = "C";
    /** The Constant INPUT_MAPPING_D. */
    public static final String INPUT_MAPPING_D = "D";
    /** The Constant INPUT_MAPPING_E. */
    public static final String INPUT_MAPPING_E = "E";
    /** The Constant INPUT_MAPPING_F. */
    public static final String INPUT_MAPPING_F = "F";
    /** The Constant INPUT_MAPPING_G. */
    public static final String INPUT_MAPPING_G = "G";
    /** The Constant INPUT_MAPPING_H. */
    public static final String INPUT_MAPPING_H = "H";
    /** The Constant INPUT_MAPPING_I. */
    public static final String INPUT_MAPPING_I = "I";
    /** The Constant INPUT_MAPPING_J. */
    public static final String INPUT_MAPPING_J = "J";
    /** The Constant INPUT_MAPPING_K. */
    public static final String INPUT_MAPPING_K = "K";
    /** The Constant INPUT_MAPPING_L. */
    public static final String INPUT_MAPPING_L = "L";
    /** The Constant INPUT_MAPPING_M. */
    public static final String INPUT_MAPPING_M = "M";
    /** The Constant INPUT_MAPPING_N. */
    public static final String INPUT_MAPPING_N = "N";
    /** The Constant INPUT_MAPPING_O. */
    public static final String INPUT_MAPPING_O = "O";
    /** The Constant INPUT_MAPPING_P. */
    public static final String INPUT_MAPPING_P = "P";
    /** The Constant INPUT_MAPPING_Q. */
    public static final String INPUT_MAPPING_Q = "Q";
    /** The Constant INPUT_MAPPING_R. */
    public static final String INPUT_MAPPING_R = "R";
    /** The Constant INPUT_MAPPING_S. */
    public static final String INPUT_MAPPING_S = "S";
    /** The Constant INPUT_MAPPING_T. */
    public static final String INPUT_MAPPING_T = "T";
    /** The Constant INPUT_MAPPING_U. */
    public static final String INPUT_MAPPING_U = "U";
    /** The Constant INPUT_MAPPING_V. */
    public static final String INPUT_MAPPING_V = "V";
    /** The Constant INPUT_MAPPING_W. */
    public static final String INPUT_MAPPING_W = "W";
    /** The Constant INPUT_MAPPING_X. */
    public static final String INPUT_MAPPING_X = "X";
    /** The Constant INPUT_MAPPING_Y. */
    public static final String INPUT_MAPPING_Y = "Y";
    /** The Constant INPUT_MAPPING_Z. */
    public static final String INPUT_MAPPING_Z = "Z";
    /** The Constant INPUT_MAPPING_0. */
    public static final String INPUT_MAPPING_0 = "0";
    /** The Constant INPUT_MAPPING_1. */
    public static final String INPUT_MAPPING_1 = "1";
    /** The Constant INPUT_MAPPING_2. */
    public static final String INPUT_MAPPING_2 = "2";
    /** The Constant INPUT_MAPPING_3. */
    public static final String INPUT_MAPPING_3 = "3";
    /** The Constant INPUT_MAPPING_4. */
    public static final String INPUT_MAPPING_4 = "4";
    /** The Constant INPUT_MAPPING_5. */
    public static final String INPUT_MAPPING_5 = "5";
    /** The Constant INPUT_MAPPING_6. */
    public static final String INPUT_MAPPING_6 = "6";
    /** The Constant INPUT_MAPPING_7. */
    public static final String INPUT_MAPPING_7 = "7";
    /** The Constant INPUT_MAPPING_8. */
    public static final String INPUT_MAPPING_8 = "8";
    /** The Constant INPUT_MAPPING_9. */
    public static final String INPUT_MAPPING_9 = "9";
    /** The Constant INPUT_MAPPING_SPACE. */
    public static final String INPUT_MAPPING_SPACE = " ";
    /** The Constant INPUT_MAPPING_SCORE. */
    public static final String INPUT_MAPPING_SCORE = "-";
    /** The Constant INPUT_MAPPING_UNDERSCORE. */
    public static final String INPUT_MAPPING_UNDERSCORE = "_";
    /** The Constant INPUT_MAPPING_BACKSPACE. */
    public static final String INPUT_MAPPING_BACKSPACE = "";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_POINT = ".";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_QUESTIONMARK = "?";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_COMMA = ",";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_EXCREMATIONMARK = "!";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_ENTER = "Enter";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_STRG = "Strg";
    /** The Constant INPUT_MAPPING_POINT. */
    public static final String INPUT_MAPPING_SHIFT = "Shift";
    
    
    
    //</editor-fold>
    //==========================================================================
    //      Private Fields
    //==========================================================================
    private InputManager inputManager;
    //==========================================================================
    //      Methods
    //==========================================================================

    private void initKeyboardMappings() {
        //<editor-fold defaultstate="collapsed" desc="Add PC Keyboard Mappings">
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_0,
                new KeyTrigger(KeyInput.KEY_0), new KeyTrigger(KeyInput.KEY_NUMPAD0));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_1,
                new KeyTrigger(KeyInput.KEY_1), new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_2,
                new KeyTrigger(KeyInput.KEY_2), new KeyTrigger(KeyInput.KEY_NUMPAD2));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_3,
                new KeyTrigger(KeyInput.KEY_3), new KeyTrigger(KeyInput.KEY_NUMPAD3));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_4,
                new KeyTrigger(KeyInput.KEY_4), new KeyTrigger(KeyInput.KEY_NUMPAD4));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_5,
                new KeyTrigger(KeyInput.KEY_5), new KeyTrigger(KeyInput.KEY_NUMPAD5));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_6,
                new KeyTrigger(KeyInput.KEY_6), new KeyTrigger(KeyInput.KEY_NUMPAD6));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_7,
                new KeyTrigger(KeyInput.KEY_7), new KeyTrigger(KeyInput.KEY_NUMPAD7));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_8,
                new KeyTrigger(KeyInput.KEY_8), new KeyTrigger(KeyInput.KEY_NUMPAD8));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_9,
                new KeyTrigger(KeyInput.KEY_9), new KeyTrigger(KeyInput.KEY_NUMPAD9));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_A,
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_B,
                new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_C,
                new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_D,
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_E,
                new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_F,
                new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_G,
                new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_H,
                new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_I,
                new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_J,
                new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_K,
                new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_L,
                new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_M,
                new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_N,
                new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_O,
                new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_P,
                new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_Q,
                new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_R,
                new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_S,
                new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_T,
                new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_U,
                new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_V,
                new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_W,
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_X,
                new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_Y,
                new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_Z,
                new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_SCORE,
                new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_SPACE,
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_UNDERSCORE,
                new KeyTrigger(KeyInput.KEY_UNDERLINE));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_BACKSPACE,
                new KeyTrigger(KeyInput.KEY_BACK), new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_POINT,
                new KeyTrigger(KeyInput.KEY_PERIOD),
                new KeyTrigger(KeyInput.KEY_NUMPADCOMMA));
        //            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_QUESTIONMARK,
        //                    new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addMapping(KeyInputManager.INPUT_MAPPING_COMMA,
                new KeyTrigger(KeyInput.KEY_COMMA));
        //            inputManager.addMapping(KeyInputMap.INPUT_MAPPING_EXCREMATIONMARK,
        //                    new KeyTrigger(KeyInput.KEY_COMMA));

        
        //</editor-fold>
    }
}
