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
 * File: Hub.java
 * Type: com.solarwars.Hub
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars;

import com.solarwars.logic.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class Hub.
 */
public class Hub {

    /**
     * Gets the players.
     *
     * @return the players
     */
    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        for (Map.Entry<String, Player> cursor : playersByName.entrySet()) {
            players.add(cursor.getValue());
        }

        return players;
    }
    /** The players by id. */
    public static HashMap<Integer, Player> playersByID;
    /** The players by name. */
    private static HashMap<String, Player> playersByName;
    /** The local player. */
    private static Player localPlayer;

    public static Player getLastPlayer() {
        int lostPlayerCount = 0;
        int wonPlayerCount = 0;
        Player last = null;
        for (Map.Entry<Integer, Player> entry : Hub.playersByID.entrySet()) {
            Player p = entry.getValue();
            if (p != null) {
                if (p.hasLost()) {
                    lostPlayerCount++;
                } else {
                    last = p;
                    wonPlayerCount++;
                }
            }
        }
        if (wonPlayerCount == 1) {
            return last;
        }
        return null;
    }

    /**
     * Last player.
     *
     * @return true, if successful
     */
    public static boolean isLastPlayer() {
        return Hub.getLocalPlayer().equals(Hub.getLastPlayer());
    }
    private boolean initialized = false;

    /**
     * Gets the local player.
     *
     * @return the local player
     */
    public static Player getLocalPlayer() {
        return localPlayer;
    }
    /** The instance. */
    private static Hub instance;

    /**
     * Gets the single instance of Hub.
     *
     * @return single instance of Hub
     */
    public static Hub getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Hub();
    }

    /**
     * Instantiates a new hub.
     */
    private Hub() {
        playersByName = new HashMap<String, Player>(8);
        playersByID = new HashMap<Integer, Player>(8);
    }

    /**
     * Initializes the Hub to the other players.
     *
     * @param localPlayer the local player
     * @param players the players
     */
    public void initialize(Player localPlayer, ArrayList<Player> players) {
        if (initialized) {
            return;
        }
        playersByID = new HashMap<Integer, Player>();
        playersByName = new HashMap<String, Player>();
        Hub.localPlayer = localPlayer;
        addPlayer(localPlayer);
        if (players != null) {
            for (Player p : players) {
                if (p.getId() != localPlayer.getId()) {
                    addPlayer(p);
                }
            }
        }
        initialized = true;
    }

    /**
     * Adds the player.
     *
     * @param p the p
     */
    public boolean addPlayer(Player p) {
        if (playersByID.containsKey(p.getId())
                || playersByName.containsKey(p.getName())) {
            return false;
        }
        playersByName.put(p.getName(), p);
        playersByID.put(p.getId(), p);
        return true;
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

    public boolean isInitialized() {
        return initialized;
    }    

    public void destroy() {
        if (!initialized) {
            return;
        }
        playersByID.clear();
        playersByName.clear();
        localPlayer = null;
        initialized = false;
    }
}
