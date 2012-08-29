package settings;

import java.io.File;
import java.io.InputStream;

/**
 * Interface for a SettingsLoader.
 * 
 * @author fxdapokalypse
 *
 */
public interface SettingsLoader {
	
	/**
	 * Parameter setter which can use for the
	 * configuration of the SettingsLoader 
	 * e.g: username and password for a password
	 * protected settingsLoader.
	 * 
	 * @param parameter
	 * 	name of the parameter
	 * @param value
	 * 	value of the parameter
	 */
	public void setParam(String parameter, String value);
	/**
	 * Parameter getter
	 * 
	 * @param parameter
	 * 	name of the parameter
	 * @return
	 *  parameter value or null if the specified parameter isn't defined.
	 */
	public String getParam(String parameter);
	/**
	 * Load the GameSettings by a specified InputStream.
	 * 
	 * @param settings
	 * 	the GameSettings instance in which the settings will be stored.
	 * @param in
	 * 	A InputStream which provide access to the data source of the settings.
	 * @return
	 * 	The specified settings instance for method chaining.
	 * @throws GameSettingsException
	 *  if an error occurs while the settings are loaded.
	 */
	GameSettings load(GameSettings settings, InputStream in) throws GameSettingsException;
	/**
	 * Load the GameSettings by a specified File.
	 * 
	 * @param settings
	 * 	the GameSettings instance in which the settings will be stored.
	 * @param configurationFile
	 * 	The configuration to be loaded.
	 * @return the specified settings instance for method chaining.
	 * @throws GameSettingsException
	 * 	if an error occurs while the settings are loaded.
	 */
	GameSettings load(GameSettings settings, File configurationFile) throws GameSettingsException;
	/**
	 * Load the GameSettings by the default data source of the Loader.
	 * 
	 * @param settings
	 * 	the GameSettings instance in which the settings will be stored.
	 * @return
	 *  The specified settings instance for method chaining.
	 * @throws GameSettingsException
	 * 	if an error occurs while the settings are loaded.
	 */
	GameSettings load(GameSettings settings) throws GameSettingsException;
}
