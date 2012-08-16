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
 * Type: solarwars.InputMappings
 * 
 * Documentation created: 19.07.2012 - 17:55:16 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.TouchTrigger;

/**
 * The singleton InputMappings for the solarwars game input mappings for
 * keyboard and mouse.
 * 
 * @author Hans Ferchland
 */
public class InputMappings
{
	// ==========================================================================
	// === Singleton
	// ==========================================================================

	/**
	 * The hidden constructor of InputMappings.
	 */
	private InputMappings()
	{}

	/**
	 * The static method to retrive the one and only instance of InputMappings.
	 */
	public static InputMappings getInstance ()
	{
		return InputMappingsHolder.INSTANCE;
	}

	/**
	 * The holder-class InputMappingsHolder for the InputMappings.
	 */
	private static class InputMappingsHolder
	{

		private static final InputMappings	INSTANCE	= new InputMappings();
	}

	// ==========================================================================
	// === Mappings
	// ==========================================================================
	public static final String	KEYBOARD_EXIT		= "SOLARWARS_Exit";
	public static final String	KEYBOARD_PAUSE		= "SOLARWARS_Pause";
	public static final String	KEYBOARD_TABSCORE	= "SOLARWARS_tabScore";
	public static final String	KEYBOARD_CHAT		= "SOLARWARS_chat";
	public static final String	KEYBOARD_CONTROL	= "SOLARWARS_CONTROL";

	public static final String	DEBUG_CAMERA_POS	= "SOLARWARS_CameraPos";
	public static final String	DEBUG_HIDE_STATS	= "SOLARWARS_HideStats";
	public static final String	DEBUG_MEMORY		= "SOLARWARS_Memory";

	public static final String	MOUSE_LEFT_CLICK	= "SOLARWARS_LeftClick";
	public static final String	MOUSE_RIGHT_CLICK	= "SOLARWARS_RightClick";
	public static final String	MOUSE_WHEEL_UP		= "SOLARWARS_WheelUp";
	public static final String	MOUSE_WHEEL_DOWN	= "SOLARWARS_WheelDown";

	public static final String	KEY_V				= "SOLARWARS_KEY_V";

	// ==========================================================================
	// === Methods
	// ==========================================================================

	public void initialize (InputManager inputManager)
	{
		// Map interface clicking for ingame and GUI and Debugging
		inputManager.addMapping(InputMappings.MOUSE_LEFT_CLICK,
								new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(InputMappings.MOUSE_RIGHT_CLICK,
								new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(InputMappings.MOUSE_WHEEL_DOWN,
								new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
										false));
		inputManager.addMapping(InputMappings.MOUSE_WHEEL_UP,
								new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
										true));

		inputManager.addMapping(InputMappings.DEBUG_CAMERA_POS,
								new KeyTrigger(KeyInput.KEY_C));
		inputManager.addMapping(InputMappings.DEBUG_MEMORY,
								new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping(InputMappings.DEBUG_HIDE_STATS,
								new KeyTrigger(KeyInput.KEY_F3));

		inputManager.addMapping(InputMappings.KEYBOARD_TABSCORE,
								new KeyTrigger(KeyInput.KEY_TAB));
		inputManager.addMapping(InputMappings.KEYBOARD_CONTROL,
								new KeyTrigger(KeyInput.KEY_LCONTROL),
								new KeyTrigger(KeyInput.KEY_RCONTROL));
		inputManager.addMapping(InputMappings.KEY_V,
								new KeyTrigger(KeyInput.KEY_V));

		inputManager.addMapping("Touch", new TouchTrigger(0));

	}
}
