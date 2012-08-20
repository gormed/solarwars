/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or application 
 * in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: SolarWars
 * File: GameplayException.java
 * Type: logic.GameplayException
 * 
 * Documentation created: 19.08.2012 - 02:58:15 by Hans
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

/**
 * The class GameplayException.
 * @author Hans Ferchland
 * @version
 */
public class GameplayException extends Exception {
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public GameplayException(String msg) {
        super(msg);
    }

    public GameplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameplayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GameplayException(Throwable cause) {
        super(cause);
    }
}
