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
 * File: GameStatistics.java
 * Type: com.solarwars.logic.GameStatistics
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

/**
 * The singleton for the statistics of a game-session for all players, recorded 
 * by this client.
 * @author Hans Ferchland
 */
public class GameStatistics {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of GameStatistics.
     */
    private GameStatistics() {
    }

    /**
     * The static method to retrive the one and only instance of GameStatistics.
     */
    public static GameStatistics getInstance() {
        return GameStatisticsHolder.INSTANCE;
    }

    /**
     * The holder-class GameStatisticsHolder for the GameStatistics.
     */
    private static class GameStatisticsHolder {

        private static final GameStatistics INSTANCE = new GameStatistics();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //==========================================================================
    //===   Methods
    //==========================================================================
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
