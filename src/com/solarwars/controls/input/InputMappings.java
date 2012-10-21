/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: InputMappings.java
 * Type: com.solarwars.InputMappings
 * 
 * Documentation created: 19.07.2012 - 17:55:16 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.controls.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.TouchTrigger;

/**
 * The singleton InputMappings for the com.solarwars game com.solarwars.input
 * mappings for keyboard and mouse.
 *
 * @author Hans Ferchland
 */
public class InputMappings {

    // ==========================================================================
    // === Singleton
    // ==========================================================================
    /**
     * The hidden constructor of InputMappings.
     */
    private InputMappings() {
    }

    /**
     * The static method to retrive the one and only instance of InputMappings.
     */
    public static InputMappings getInstance() {
        return InputMappingsHolder.INSTANCE;
    }

    /**
     * The holder-class InputMappingsHolder for the InputMappings.
     */
    private static class InputMappingsHolder {

        private static final InputMappings INSTANCE = new InputMappings();
    }
    // ==========================================================================
    // === Mappings
    // ==========================================================================
    public static final String EXIT_GAME = "SOLARWARS_Exit";
    public static final String PAUSE_GAME = "SOLARWARS_Pause";
    public static final String GAME_SCORES = "SOLARWARS_tabScore";
    public static final String PLAYER_CHAT = "SOLARWARS_chat";
    public static final String CONTROL_MODIFIER = "SOLARWARS_CONTROL";
    public static final String DEBUG_CAMERA_POS = "SOLARWARS_CameraPos";
    public static final String DEBUG_HIDE_STATS = "SOLARWARS_HideStats";
    public static final String DEBUG_MEMORY = "SOLARWARS_Memory";
    public static final String DEBUG_NIFTY_GUI = "SOLARWARS_DebugNifty";
    public static final String LEFT_CLICK_SELECT = "SOLARWARS_LeftClick";
    public static final String RIGHT_CLICK_ATTACK = "SOLARWARS_RightClick";
    public static final String PERCENT_UP = "SOLARWARS_WheelUp";
    public static final String PERCENT_DOWN = "SOLARWARS_WheelDown";
    public static final String KEY_COPY_V = "SOLARWARS_KEY_V";
    public static final String AXIS_LS_DOWN = "Axis LS Down";
    public static final String AXIS_LS_LEFT = "Axis LS Left";
    public static final String AXIS_LS_RIGHT = "Axis LS Right";
    public static final String AXIS_LS_UP = "Axis LS Up";
    private static boolean displayOSCursor = true;
    // ==========================================================================
    // === Methods
    // ==========================================================================

    public void initialize(InputManager inputManager) {

        inputManager.setCursorVisible(displayOSCursor);
        // Map interface clicking for ingame and GUI and Debugging
        inputManager.addMapping(InputMappings.LEFT_CLICK_SELECT,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT),
                // Button A JOY0
                new JoyButtonTrigger(0, 0));
        inputManager.addMapping(InputMappings.RIGHT_CLICK_ATTACK,
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT),
                // Button B JOY0
                new JoyButtonTrigger(0, 1));
        inputManager.addMapping(InputMappings.PERCENT_DOWN,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
                false),
                // Button LT JOY0
                new JoyButtonTrigger(0, 8));
        inputManager.addMapping(InputMappings.PERCENT_UP,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
                true),
                // Button RT JOY0
                new JoyButtonTrigger(0, 9));

        inputManager.addMapping(InputMappings.DEBUG_CAMERA_POS,
                new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping(InputMappings.DEBUG_MEMORY,
                new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(InputMappings.DEBUG_HIDE_STATS,
                new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addMapping(InputMappings.DEBUG_NIFTY_GUI,
                new KeyTrigger(KeyInput.KEY_F4));

        inputManager.addMapping(InputMappings.GAME_SCORES,
                new KeyTrigger(KeyInput.KEY_TAB),
                // Button BACK JOY0
                new JoyButtonTrigger(0, 6));
        inputManager.addMapping(InputMappings.CONTROL_MODIFIER,
                new KeyTrigger(KeyInput.KEY_LCONTROL),
                new KeyTrigger(KeyInput.KEY_RCONTROL));
        inputManager.addMapping(
                InputMappings.PAUSE_GAME,
                new KeyTrigger(KeyInput.KEY_P),
                new KeyTrigger(KeyInput.KEY_PAUSE),
                new KeyTrigger(KeyInput.KEY_ESCAPE),
                // Button START JOY0
                new JoyButtonTrigger(0, 7));

        inputManager.addMapping(AXIS_LS_UP, new JoyAxisTrigger(0, 0, true));
        inputManager.addMapping(AXIS_LS_DOWN, new JoyAxisTrigger(0, 0, false));
        inputManager.addMapping(AXIS_LS_LEFT, new JoyAxisTrigger(0, 1, true));
        inputManager.addMapping(AXIS_LS_RIGHT, new JoyAxisTrigger(0, 1, false));


        inputManager.addMapping("Touch", new TouchTrigger(0));

    }
}
