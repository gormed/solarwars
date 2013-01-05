/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: GameSettingsException.java
 * Type: com.solarwars.settings.GameSettingsException
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.settings;

/**
 * Indicates unexpected behaviors for the Game Settings classes.
 * e.g.: GameSettings can't be loaded. 
 * @author fxdapokalypse
 *
 */
public class GameSettingsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9203051555789764691L;

	public GameSettingsException() {
		super();
	}

	public GameSettingsException(String arg0) {
		super(arg0);
	}

	public GameSettingsException(Throwable arg0) {
		super(arg0);
	}

	public GameSettingsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
