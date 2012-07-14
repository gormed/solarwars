/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hans
 */
public class GameLogger {

    private static GameLogger instance;

    public static GameLogger getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new GameLogger();
    }

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
    
    private String executionPath;
    private AssetManager assetManager;
    private FileWriter logWriter;
    private PrintWriter printWriter;
}
