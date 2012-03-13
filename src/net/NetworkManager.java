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
    private NetworkManager instance;
    
    public NetworkManager getInstance() {
        if (instance != null)
            return instance;
        return instance = new NetworkManager();
    }

    private NetworkManager() {
    
    }
}
