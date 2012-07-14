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
 * File: ServerHub.java
 * Type: net.ServerHub
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import logic.Player;

/**
 * The Class ServerHub.
 */
public class ServerHub {

    /** The PLAYE r_ id. */
    private static int PLAYER_ID = 0;

    /**
     * Gets the continious player id.
     *
     * @return the continious player id
     */
    public static int getContiniousPlayerID() {
        return PLAYER_ID++;
    }

    /**
     * Gets the player id.
     *
     * @return the player id
     */
    public static int getPlayerID() {
        return PLAYER_ID;
    }

    /**
     * Reset player id.
     *
     * @param id the id
     */
    public static void resetPlayerID(int id) {
        PLAYER_ID = id;
        playersByID = new HashMap<Integer, Player>(8);
        playersByName = new HashMap<String, Player>(8);
    }

    /**
     * Gets the players.
     *
     * @return the players
     */
    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        if (!playersByID.isEmpty()) {
            for (Map.Entry<Integer, Player> cursor : playersByID.entrySet()) {
                players.add(cursor.getValue());
            }
        }
        return players;
    }
    
    /** The players by id. */
    public static HashMap<Integer, Player> playersByID;
    
    /** The players by name. */
    private static HashMap<String, Player> playersByName;
    
    /** The host player. */
    private static Player hostPlayer;

    /**
     * Gets the host player.
     *
     * @return the host player
     */
    public static Player getHostPlayer() {
        return hostPlayer;
    }
    
    /** The instance. */
    private static ServerHub instance;

    /**
     * Gets the single instance of ServerHub.
     *
     * @return single instance of ServerHub
     */
    public static ServerHub getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new ServerHub();
    }

    /**
     * Instantiates a new server hub.
     */
    private ServerHub() {
        playersByName = new HashMap<String, Player>(8);
        playersByID = new HashMap<Integer, Player>(8);
    }

    /**
     * Initializes the.
     *
     * @param hostPlayer the host player
     * @param players the players
     */
    public void initialize(Player hostPlayer, ArrayList<Player> players) {
        this.hostPlayer = hostPlayer;
        if (players != null) {
            for (Player p : players) {
                addPlayer(p);
            }
        }
    }

    /**
     * Adds the player.
     *
     * @param p the p
     */
    public void addPlayer(Player p) {
        playersByName.put(p.getName(), p);
        playersByID.put(p.getId(), p);
    }

    /**
     * Removes the player.
     *
     * @param p the p
     */
    public void removePlayer(Player p) {
        playersByName.remove(p.getName());
        playersByID.remove(p.getId());
    }

    /**
     * Gets the player.
     *
     * @param name the name
     * @return the player
     */
    public Player getPlayer(String name) {
        if (playersByName.containsKey(name)) {
            return playersByName.get(name);
        } else {
            return null;
        }
    }

    /**
     * Gets the player.
     *
     * @param id the id
     * @return the player
     */
    public Player getPlayer(int id) {
        if (playersByID.containsKey(id)) {
            return playersByID.get(id);
        } else {
            return null;
        }
    }

    /**
     * Gets the player count.
     *
     * @return the player count
     */
    public int getPlayerCount() {
        return playersByName.size();
    }
}
