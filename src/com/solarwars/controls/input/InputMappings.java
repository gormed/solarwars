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
    // Select
    public static final String LEFT_CLICK_SELECT = "SOLARWARS_LeftClickSelect";
    
    public static final String GAMEPAD_PLAYER1_A_SELECT = "SOLARWARS_Gamepad_Player1_ASelect";
    public static final String GAMEPAD_PLAYER2_A_SELECT = "SOLARWARS_Gamepad_Player2_ASelect";
    public static final String GAMEPAD_PLAYER3_A_SELECT = "SOLARWARS_Gamepad_Player3_ASelect";
    public static final String GAMEPAD_PLAYER4_A_SELECT = "SOLARWARS_Gamepad_Player4_ASelect";
    
    public static final String GAMEPAD_PLAYER1_RB_SELECT = "SOLARWARS_Gamepad_Player1_SequSelect_Forward";
    public static final String GAMEPAD_PLAYER2_RB_SELECT = "SOLARWARS_Gamepad_Player2_SequSelect_Forward";
    public static final String GAMEPAD_PLAYER3_RB_SELECT = "SOLARWARS_Gamepad_Player3_SequSelect_Forward";
    public static final String GAMEPAD_PLAYER4_RB_SELECT = "SOLARWARS_Gamepad_Player4_SequSelect_Forward";
    
    public static final String GAMEPAD_PLAYER1_LB_SELECT = "SOLARWARS_Gamepad_Player1_SequSelect_Backward";
    public static final String GAMEPAD_PLAYER2_LB_SELECT = "SOLARWARS_Gamepad_Player2_SequSelect_Backward";
    public static final String GAMEPAD_PLAYER3_LB_SELECT = "SOLARWARS_Gamepad_Player3_SequSelect_Backward";
    public static final String GAMEPAD_PLAYER4_LB_SELECT = "SOLARWARS_Gamepad_Player4_SequSelect_Backward";
    
    public static final String GAMEPAD_PLAYER1_SELECT_ALL = "SOLARWARS_Gamepad_Player1_Select_All";
    public static final String GAMEPAD_PLAYER2_SELECT_ALL = "SOLARWARS_Gamepad_Player2_Select_All";
    public static final String GAMEPAD_PLAYER3_SELECT_ALL = "SOLARWARS_Gamepad_Player3_Select_All";
    public static final String GAMEPAD_PLAYER4_SELECT_ALL = "SOLARWARS_Gamepad_Player4_Select_All";
    // Attack
    public static final String RIGHT_CLICK_ATTACK = "SOLARWARS_RightClickAttack";
    
    public static final String GAMEPAD_PLAYER1_B_ATTACK = "SOLARWARS_Gamepad_Player1_BAttack";
    public static final String GAMEPAD_PLAYER2_B_ATTACK = "SOLARWARS_Gamepad_Player2_BAttack";
    public static final String GAMEPAD_PLAYER3_B_ATTACK = "SOLARWARS_Gamepad_Player3_BAttack";
    public static final String GAMEPAD_PLAYER4_B_ATTACK = "SOLARWARS_Gamepad_Player4_BAttack";
    // Percentage
    public static final String PERCENT_WEEL_UP = "SOLARWARS_Wheel_PercentageUp";
    public static final String PERCENT_WEEL_DOWN = "SOLARWARS_Wheel_PercentageDown";
    
    public static final String GAMEPAD_PLAYER1_PERCENT_TRIGGER_UP = "SOLARWARS_Gamepad_Player1_Trigger_PercentageUp";
    public static final String GAMEPAD_PLAYER1_PERCENT_TRIGGER_DOWN = "SOLARWARS_Gamepad_Player1_Trigger_PercentageDown";
    
    public static final String GAMEPAD_PLAYER2_PERCENT_TRIGGER_UP = "SOLARWARS_Gamepad_Player2_Trigger_PercentageUp";
    public static final String GAMEPAD_PLAYER2_PERCENT_TRIGGER_DOWN = "SOLARWARS_Gamepad_Player2_Trigger_PercentageDown";
    
    public static final String GAMEPAD_PLAYER3_PERCENT_TRIGGER_UP = "SOLARWARS_Gamepad_Player3_Trigger_PercentageUp";
    public static final String GAMEPAD_PLAYER3_PERCENT_TRIGGER_DOWN = "SOLARWARS_Gamepad_Player3_Trigger_PercentageDown";
    
    public static final String GAMEPAD_PLAYER4_PERCENT_TRIGGER_UP = "SOLARWARS_Gamepad_Player4_Trigger_PercentageUp";
    public static final String GAMEPAD_PLAYER4_PERCENT_TRIGGER_DOWN = "SOLARWARS_Gamepad_Player4_Trigger_PercentageDown";
    // Gamepad Mouse
    public static final String GAMEPAD_PLAYER1_DPAD_LS_DOWN = "SOLARWARS_Gamepad_Player1_Axis LS Down";
    public static final String GAMEPAD_PLAYER1_DPAD_LS_LEFT = "SOLARWARS_Gamepad_Player1_Axis LS Left";
    public static final String GAMEPAD_PLAYER1_DPAD_LS_RIGHT = "SOLARWARS_Gamepad_Player1_Axis LS Right";
    public static final String GAMEPAD_PLAYER1_DPAD_LS_UP = "SOLARWARS_Gamepad_Player1_Axis LS Up";
    
    public static final String GAMEPAD_PLAYER2_DPAD_LS_DOWN = "SOLARWARS_Gamepad_Player2_Axis LS Down";
    public static final String GAMEPAD_PLAYER2_DPAD_LS_LEFT = "SOLARWARS_Gamepad_Player2_Axis LS Left";
    public static final String GAMEPAD_PLAYER2_DPAD_LS_RIGHT = "SOLARWARS_Gamepad_Player2_Axis LS Right";
    public static final String GAMEPAD_PLAYER2_DPAD_LS_UP = "SOLARWARS_Gamepad_Player2_Axis LS Up";
    
    public static final String GAMEPAD_PLAYER3_DPAD_LS_DOWN = "SOLARWARS_Gamepad_Player3_Axis LS Down";
    public static final String GAMEPAD_PLAYER3_DPAD_LS_LEFT = "SOLARWARS_Gamepad_Player3_Axis LS Left";
    public static final String GAMEPAD_PLAYER3_DPAD_LS_RIGHT = "SOLARWARS_Gamepad_Player3_Axis LS Right";
    public static final String GAMEPAD_PLAYER3_DPAD_LS_UP = "SOLARWARS_Gamepad_Player3_Axis LS Up";
    
    public static final String GAMEPAD_PLAYER4_DPAD_LS_DOWN = "SOLARWARS_Gamepad_Player4_Axis LS Down";
    public static final String GAMEPAD_PLAYER4_DPAD_LS_LEFT = "SOLARWARS_Gamepad_Player4_Axis LS Left";
    public static final String GAMEPAD_PLAYER4_DPAD_LS_RIGHT = "SOLARWARS_Gamepad_Player4_Axis LS Right";
    public static final String GAMEPAD_PLAYER4_DPAD_LS_UP = "SOLARWARS_Gamepad_Player4_Axis LS Up";
    // 
    private static boolean displayOSCursor = true;

    // ==========================================================================
    // === Methods
    // ==========================================================================
    public void initialize(InputManager inputManager) {

        inputManager.setCursorVisible(displayOSCursor);
        // Map interface clicking for ingame and GUI and Debugging
        inputManager.addMapping(InputMappings.LEFT_CLICK_SELECT,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        // Button A JOY0
        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_A_SELECT,
                new JoyButtonTrigger(0, 0),
                new JoyButtonTrigger(1, 0),
                new JoyButtonTrigger(2, 0),
                new JoyButtonTrigger(3, 0));

        inputManager.addMapping(InputMappings.RIGHT_CLICK_ATTACK,
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        // Button B JOY0
        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_B_ATTACK,
                new JoyButtonTrigger(0, 1),
                new JoyButtonTrigger(1, 1),
                new JoyButtonTrigger(2, 1),
                new JoyButtonTrigger(3, 1));

        inputManager.addMapping(InputMappings.PERCENT_WEEL_DOWN,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
                false));
        // Button LT JOY0
        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_PERCENT_TRIGGER_DOWN,
                new JoyAxisTrigger(0, 4, true),
                new JoyAxisTrigger(1, 4, true),
                new JoyAxisTrigger(2, 4, true),
                new JoyAxisTrigger(3, 4, true));
//                new JoyButtonTrigger(0, 8),
//                new JoyButtonTrigger(1, 8),
//                new JoyButtonTrigger(2, 8),
//                new JoyButtonTrigger(3, 8));

        inputManager.addMapping(InputMappings.PERCENT_WEEL_UP,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
                true));
        // Button RT JOY0
        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_PERCENT_TRIGGER_UP,
                new JoyAxisTrigger(0, 4, false),
                new JoyAxisTrigger(1, 4, false),
                new JoyAxisTrigger(2, 4, false),
                new JoyAxisTrigger(3, 4, false));
//                new JoyButtonTrigger(0, 9), 
//                new JoyButtonTrigger(1, 9), 
//                new JoyButtonTrigger(2, 9), 
//                new JoyButtonTrigger(3, 9));

        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_LB_SELECT,
                new JoyButtonTrigger(0, 4),
                new JoyButtonTrigger(1, 4),
                new JoyButtonTrigger(2, 4),
                new JoyButtonTrigger(3, 4));
        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_RB_SELECT,
                new JoyButtonTrigger(0, 5),
                new JoyButtonTrigger(1, 5),
                new JoyButtonTrigger(2, 5),
                new JoyButtonTrigger(3, 5));

        inputManager.addMapping(InputMappings.GAMEPAD_PLAYER1_SELECT_ALL,
                new JoyButtonTrigger(0, 3),
                new JoyButtonTrigger(1, 3),
                new JoyButtonTrigger(2, 3),
                new JoyButtonTrigger(3, 3));

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

        inputManager.addMapping(GAMEPAD_PLAYER1_DPAD_LS_LEFT, new JoyAxisTrigger(0, 6, true));
        inputManager.addMapping(GAMEPAD_PLAYER1_DPAD_LS_RIGHT, new JoyAxisTrigger(0, 6, false));
        inputManager.addMapping(GAMEPAD_PLAYER1_DPAD_LS_DOWN, new JoyAxisTrigger(0, 7, true));
        inputManager.addMapping(GAMEPAD_PLAYER1_DPAD_LS_UP, new JoyAxisTrigger(0, 7, false));

//        inputManager.addMapping(DPAD_LS_LEFT, new JoyAxisTrigger(0, JoyInput.AXIS_POV_X, true));
//        inputManager.addMapping(DPAD_LS_RIGHT, new JoyAxisTrigger(1, JoyInput.AXIS_POV_X, false));
//        inputManager.addMapping(DPAD_LS_DOWN, new JoyAxisTrigger(2, JoyInput.AXIS_POV_Y, true));
//        inputManager.addMapping(DPAD_LS_UP, new JoyAxisTrigger(3, JoyInput.AXIS_POV_Y, false));

//        inputManager.addMapping(DPAD_LS_UP, new JoyAxisTrigger(0, 0, true));
//        inputManager.addMapping(DPAD_LS_DOWN, new JoyAxisTrigger(0, 0, false));
//        inputManager.addMapping(DPAD_LS_LEFT, new JoyAxisTrigger(0, 1, true));
//        inputManager.addMapping(DPAD_LS_RIGHT, new JoyAxisTrigger(0, 1, false));

        inputManager.addMapping("Touch", new TouchTrigger(0));

    }
}
