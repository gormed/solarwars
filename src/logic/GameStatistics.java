/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: GameStatistics.java
 * Type: logic.GameStatistics
 * 
 * Documentation created: 21.07.2012 - 15:07:02 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

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
