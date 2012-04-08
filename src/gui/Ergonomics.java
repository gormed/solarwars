/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Hans
 */
public final class Ergonomics {
    private static Ergonomics instance;
    
    private Ergonomics() {
        
    }
    
    public static Ergonomics getInstance() {
        if (instance != null)
            return instance;
        return instance = new Ergonomics();
    }
    
    private String ipAddress = "127.0.0.1";
    private String name = "Player";
    private int players = 2;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
    
}
