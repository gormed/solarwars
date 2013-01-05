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
 * File: Logging.java
 * Type: com.solarwars.log.Logging
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
     * Sets the property PROPERTY_CONFIG_FILE if it isn't set. That allow to
     * provide the system property "Djava.util.logging.config.file" to change
     * the initial configuration file for the logging.
     *
     */
    static {
        System.setProperty(PROPERTY_CONFIG_FILE, System.getProperty(PROPERTY_CONFIG_FILE, "logging.properties"));
    }

    /**
     * Loads the initial logging configuration and makes sure that all handlers
     * will be closed when the program exits
     */
    public static void init() {
        final LogManager logManager = LogManager.getLogManager();

        File file = new File("log");
        if (!file.exists() || !file.isDirectory()) {
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
                for (Handler handler : logger.getHandlers()) {
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
