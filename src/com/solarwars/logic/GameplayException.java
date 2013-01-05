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
 * File: GameplayException.java
 * Type: com.solarwars.logic.GameplayException
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

/**
 * The class GameplayException.
 * @author Hans Ferchland
 * @version
 */
public class GameplayException extends RuntimeException {

    public GameplayException() {
    }
    
    private static final long serialVersionUID = 42;
    
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    
    public GameplayException(String msg) {
        super(msg);
    }

    public GameplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameplayException(Throwable cause) {
        super(cause);
    }
}
