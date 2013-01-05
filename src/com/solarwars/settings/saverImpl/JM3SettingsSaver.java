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
 * File: JM3SettingsSaver.java
 * Type: com.solarwars.settings.saverImpl.JM3SettingsSaver
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
