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
