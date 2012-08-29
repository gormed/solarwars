package com.solarwars.settings.saverImpl;

import java.io.IOException;
import java.io.OutputStream;

import com.solarwars.settings.BaseSettingsSaver;
import com.solarwars.settings.GameSettings;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SettingsSaver;

/**
 * SettingSaver for flat propertie files.
 * 
 * @author fxdapokalypse
 *
 */
public class JM3SettingsSaver extends BaseSettingsSaver {
	
	/**
	 * @see SettingsSaver
	 */
	@Override
	public GameSettings save(GameSettings settings, OutputStream out)
			throws GameSettingsException {
		
		try {
			settings.toAppSettings().save(out);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameSettingsException("An error occurred while the configuration save process is running.", e);
		}
		return settings;
	}
	
}
