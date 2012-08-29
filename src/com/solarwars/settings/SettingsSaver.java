package com.solarwars.settings;

import java.io.File;
import java.io.OutputStream;

/**
 * Interface for a SettingsLoader.
 * 
 * @author fxdapokalypse
 *
 */
public interface SettingsSaver
{	
	/**
	 * Parameter setter which can use for the
	 * configuration of the SettingsSaver
	 * e.g: username and password for a password
	 * protected settingsSaver.
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
	public void getParam(String parameter);
	
	/**
	 * Save the GameSettings into a specified OutputStream.
	 * 
	 * @param settings
	 * 	The settings which will be stored.
	 * @param out
	 * 	A OutputStream which provide access to the destination data source for the settings.
	 * @return
	 * 	The specified settings instance for method chaining.
	 * @throws GameSettingsException
	 * 		if an error occurs while the settings are saved.
	 */
	GameSettings save(GameSettings settings, OutputStream out) throws GameSettingsException;
	/**
	 * Save the GameSettings into a specified File.
	 * 
	 * @param settings
	 *  The settings which will be stored.
	 * @param configurationFile
	 * 	The configuration file in which the settings will be stored.
	 * @return
	 * 	The specified settings instance for method chaining.
	 * @throws GameSettingsException
	 * 		if an error occurs while the settings are saved.
	 */
	GameSettings save(GameSettings settings, File configurationFile) throws GameSettingsException;
	/**
	 * Save the GameSettings into the default data source of the Saver.
	 * 
	 * @param settings
	 * 	The settings which will be stored.
	 * @return
	 * 	The specified settings instance for method chaining.
	 * @throws GameSettingsException
	 * 	if an error occurs while the settings are saved.
	 */
	GameSettings save(GameSettings settings) throws GameSettingsException;
	
}


