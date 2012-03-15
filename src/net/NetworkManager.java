/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

/**
 *
 * @author Hans
 */
public class NetworkManager {
    private static NetworkManager instance;
    
    public static NetworkManager getInstance() {
        if (instance != null)
            return instance;
        return instance = new NetworkManager();
    }

    private NetworkManager() {
    
    }
}
