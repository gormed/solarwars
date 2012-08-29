package com.solarwars.settings.loaderImpl;

import java.io.IOException;
import java.io.InputStream;

import com.solarwars.settings.BaseSettingsLoader;
import com.solarwars.settings.GameSettings;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SettingsLoader;


public class JM3SettingsLoader extends BaseSettingsLoader {
	/**
	 * @see SettingsLoader
	 */
	@Override
	public GameSettings load(GameSettings settings, InputStream in)
			throws GameSettingsException {
		try {
			settings.toAppSettings().load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameSettingsException("An error occurred while the configuration load process is running.", e);
		}
		return settings;
	}
}
