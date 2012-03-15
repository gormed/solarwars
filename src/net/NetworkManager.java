/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: NetworkManager.java
 * Type: net.NetworkManager
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

/**
 * The Class NetworkManager.
 */
public class NetworkManager {
    
    /** The instance. */
    private static NetworkManager instance;
    
    /**
     * Gets the single instance of NetworkManager.
     *
     * @return single instance of NetworkManager
     */
    public static NetworkManager getInstance() {
        if (instance != null)
            return instance;
        return instance = new NetworkManager();
    }

    /**
     * Instantiates a new network manager.
     */
    private NetworkManager() {
    
    }
}
