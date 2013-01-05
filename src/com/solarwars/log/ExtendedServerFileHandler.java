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
 * File: ExtendedServerFileHandler.java
 * Type: com.solarwars.log.ExtendedServerFileHandler
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.log;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * The Class is needed because the java.util.logging API
 * provides no way to configure a handler different for each logger.
 * With configure meant is the configuration over the java.util.logging.config.file.
 * 
 * @author fxdapokalypse
 *
 */
public class ExtendedServerFileHandler extends ExtendedFileHandler {

	public ExtendedServerFileHandler() throws IOException, SecurityException {
		this(LogManager.getLogManager().getProperty("com.solarwars.log.ExtendedServerFileHandler.pattern"));
	}

	public ExtendedServerFileHandler(String pattern, boolean append)
			throws IOException, SecurityException {
		super(pattern, append);
	}

	public ExtendedServerFileHandler(String pattern, int limit, int count,
			boolean append) throws IOException, SecurityException {
		super(pattern, limit, count, append);
	}

	public ExtendedServerFileHandler(String pattern, int limit, int count)
			throws IOException, SecurityException {
		super(pattern, limit, count);
	}

	public ExtendedServerFileHandler(String pattern) throws IOException,
			SecurityException {
		super(pattern);
	}

}
