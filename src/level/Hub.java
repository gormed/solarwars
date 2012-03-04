/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import java.util.ArrayList;
import java.util.HashMap;
import logic.Player;

/**
 *
 * @author Hans
 */
public class Hub {
    public static ArrayList<String> playerNames;
    private HashMap<String, Player> players;
    
    private static Hub instance;
    
    public static Hub getInstance() {
        if (instance != null)
            return instance;
        return instance = new Hub();
    }

    private Hub() {
        players = new HashMap<String, Player>();
        playerNames = new ArrayList<String>();
    }
    
    public void initialize() {
        
    }
    
    public void addPlayer(Player p) {
        players.put(p.getName(), p);
        playerNames.add(p.getId(),p.getName());
    }
    
    public void removePlayer(Player p) {
        players.remove(p.getName());
        playerNames.remove(p.getId());
    }
    
    public Player getPlayer(String name) {
        return players.get(name);
    }
    
    public Player getPlayer(int id) {
        return players.get(playerNames.get(id));
    }
    
    public int getPlayerCount() {
        return players.size();
    }
}
