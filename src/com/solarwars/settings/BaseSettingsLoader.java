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
 * File: BaseSettingsLoader.java
 * Type: com.solarwars.settings.BaseSettingsLoader
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for a settings loader which provide
 * a template implementation for a SettingLoader.
 * 
 *  A subclass must only provides the method 
 *  load(GameSettings settings, InputStream in). 
 * 
 * @author fxdapokalyse
 *
 */
public abstract class BaseSettingsLoader implements SettingsLoader {
	
	
	private static final File DEFAULT_CONFIGURATION  = new File("gameSettings.cfg");
	
	private Map<String, String> parameter = new HashMap<String, String>();
	

	@Override
	public void setParam(String parameter, String value) {
		this.parameter.put(parameter, value);
	}

	@Override
	public String getParam(String parameter) {
		return this.parameter.get(parameter);

	}
	
	
	
	@Override
	public GameSettings load(GameSettings settings, File configurationFile)
			throws GameSettingsException {
		FileInputStream in = null;
		FileLock lock = null;
		try {
			in = new FileInputStream(configurationFile);
			//lock = in.getChannel().lock();
			return load(settings, in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new GameSettingsException("Configuration file cannot found.", e);
		} finally {
			try {
				if(lock != null) {
					lock.release();
					lock = null;
				}
				if(in != null) {
					in.close();
					in = null;
				}
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public GameSettings load(GameSettings settings)
			throws GameSettingsException {
		return load(settings, DEFAULT_CONFIGURATION);
	}
	
}
