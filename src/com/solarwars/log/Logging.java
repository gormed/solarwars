package com.solarwars.log;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Helper class for the Logging
 * 
 * @author fxdapokalypse
 *
 */
public class Logging {
	/**
	 * Java property which defines the logging configuration file.
	 */
	private static final String PROPERTY_CONFIG_FILE = "java.util.logging.config.file";
	
	/**
	 * Sets the property PROPERTY_CONFIG_FILE if it isn't set.
	 * That allow to provide the system property 
	 * "Djava.util.logging.config.file" to change the
	 * initial configuration file for the logging.
	 * 
	 */
	static{
		System.setProperty(PROPERTY_CONFIG_FILE, System.getProperty(PROPERTY_CONFIG_FILE, "logging.properties"));
	}
	
	/**
	 * Loads the initial logging configuration and makes 
	 * sure that all handlers will be closed when the program exits
	 */
	public static void init()  {
		final LogManager logManager = LogManager.getLogManager();
		
		File file = new File("log");
		if(!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}
		try {
			logManager.readConfiguration();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * Hook wich close all log handlers.
		 */
		final Thread CleanUpHook = new Thread(new Runnable() {
			
			@SuppressWarnings("unused")
			private void closeAllHandlers(Logger logger) {
				for(Handler handler : logger.getHandlers()) {
					handler.flush();
					handler.close();
					logger.removeHandler(handler);
				}
			}
			
			@Override
			public void run() {
				logManager.getLogger("").info("close all log handlers");
				logManager.reset();
			}
		});
		Runtime.getRuntime().addShutdownHook(CleanUpHook);
	}
	
	
}
