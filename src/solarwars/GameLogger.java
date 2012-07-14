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
 * File: GameLogger.java
 * Type: solarwars.GameLogger
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.asset.AssetManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class GameLogger.
 *
 * @author Hans
 */
public class GameLogger {

    /** The instance. */
    private static GameLogger instance;

    /**
     * Gets the single instance of GameLogger.
     *
     * @return single instance of GameLogger
     */
    public static GameLogger getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new GameLogger();
    }

    /**
     * Instantiates a new game logger.
     */
    private GameLogger() {
        assetManager = SolarWarsApplication.getInstance().getAssetManager();
        try {
            executionPath = System.getProperty("user.dir");
            String temp = System.getProperty("user.dir").replace("\\", "/");
            System.out.print("Executing at =>" + temp);
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }
        try {
            logWriter = new FileWriter(executionPath+"\\gamelog.swlog");
        } catch (IOException ex) {
            Logger.getLogger(GameLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** The execution path. */
    private String executionPath;
    
    /** The asset manager. */
    private AssetManager assetManager;
    
    /** The log writer. */
    private FileWriter logWriter;
    
    /** The print writer. */
    private PrintWriter printWriter;
}
