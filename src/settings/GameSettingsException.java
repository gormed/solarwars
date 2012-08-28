package settings;

/**
 * Indicates unexpected behaviors for the Game Settings classes.
 * e.g.: GameSettings can't be loaded. 
 * @author fxdapokalypse
 *
 */
public class GameSettingsException extends Exception {

        private static final long serialVersionUID = 235;
    
	public GameSettingsException() {
		// TODO Auto-generated constructor stub
	}

	public GameSettingsException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GameSettingsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GameSettingsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
