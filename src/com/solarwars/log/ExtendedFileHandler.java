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
 * File: ExtendedFileHandler.java
 * Type: com.solarwars.log.ExtendedFileHandler
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

/**
 * Advanced File Handler offers the possibility of variable</br>
 * resolves inside patterns.
 * </br>
 * The following variables are currently supported: </br> 
 * <ul>
 * 	<li>%date  - the current date in the format "dd.MM.yyyy"</li>
 *  <li>%time  - the current date in the "dow mon dd hh:mm:ss zzz yyyy"</li>
 *  <li>%utime - the current date as unix timestamp</li>
 * </ul>
 * 
 * @author fxdapokalypse
 *
 */
public class ExtendedFileHandler extends FileHandler {
	
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	
	public ExtendedFileHandler() throws IOException, SecurityException {
		this(LogManager.getLogManager().getProperty("com.solarwars.log.ExtendedFileHandler.pattern"));
		
	}
	
	public ExtendedFileHandler(String pattern, boolean append)
			throws IOException, SecurityException {
		super( preparePattern(pattern), append);
	}

	public ExtendedFileHandler(String pattern, int limit, int count,
			boolean append) throws IOException, SecurityException {
		super( preparePattern(pattern), limit, count, append);
	}

	public ExtendedFileHandler(String pattern, int limit, int count)
			throws IOException, SecurityException {
		super( preparePattern(pattern), limit, count);
	}

	public ExtendedFileHandler(String pattern) throws IOException,
			SecurityException {
		super( preparePattern(pattern));
	}
	
	private static String preparePattern(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date now = new Date();
		pattern = pattern.replaceAll("%date", sdf.format(now))
				  .replaceAll("%utime", now.getTime() + "")
				  .replaceAll("%time", now.toString() + "");
		return pattern;
	}
}
