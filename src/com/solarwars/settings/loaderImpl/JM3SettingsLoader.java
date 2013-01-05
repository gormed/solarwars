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
 * File: JM3SettingsLoader.java
 * Type: com.solarwars.settings.loaderImpl.JM3SettingsLoader
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
