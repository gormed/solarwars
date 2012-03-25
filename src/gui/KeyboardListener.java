/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Hans
 */
public abstract class KeyboardListener implements ActionListener {

    public KeyboardListener() {
    }

    public KeyboardListener(InputManager inputManager) {

        deleteMappings(inputManager);

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

        inputManager.addMapping(KeyInputMap.INPUT_MAPPING_POINT, new KeyTrigger(KeyInput.KEY_PERIOD));

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
                KeyInputMap.INPUT_MAPPING_POINT);

    }

    private void deleteMappings(InputManager inputManager) {
        try {
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_0);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_1);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_2);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_3);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_4);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_5);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_6);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_7);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_8);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_9);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_A);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_B);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_C);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_D);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_E);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_F);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_G);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_H);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_I);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_J);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_K);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_L);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_M);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_N);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_O);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_P);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_Q);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_R);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_S);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_T);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_U);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_V);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_W);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_X);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_Y);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_Z);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_SCORE);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_SPACE);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_UNDERSCORE);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_BACKSPACE);
            inputManager.deleteMapping(KeyInputMap.INPUT_MAPPING_POINT);
        } catch (IllegalArgumentException e) {
        }
    }

    public abstract void onAction(String name, boolean isPressed, float tpf);
}
