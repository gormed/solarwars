/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import logic.Player;

/**
 *
 * @author Hans
 */
public class ServerHub {

    /** The PLAYER ID. */
    private static int PLAYER_ID = 0;

    /**
     * Gets the continious player id.
     *
     * @return the continious id
     */
    public static int getContiniousPlayerID() {
        return PLAYER_ID++;
    }

    public static int getCurrentPlayerID() {
        return PLAYER_ID;
    }

    public static void resetPlayerID(int id) {
        PLAYER_ID = id;
        playersByID = new HashMap<Integer, Player>(8);
        playersByName = new HashMap<String, Player>(8);
    }

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        if (!playersByID.isEmpty()) {
            for (Map.Entry<Integer, Player> cursor : playersByID.entrySet()) {
                players.add(cursor.getValue());
            }
        }
        return players;
    }
    /** The player names. */
    public static HashMap<Integer, Player> playersByID;
    /** The players. */
    private static HashMap<String, Player> playersByName;
    /** The local player. */
    private static Player hostPlayer;

    /**
     * Gets the local player.
     *
     * @return the local player
     */
    public static Player getHostPlayer() {
        return hostPlayer;
    }
    /** The instance. */
    private static ServerHub instance;

    /**
     * Gets the single instance of Hub.
     *
     * @return single instance of Hub
     */
    public static ServerHub getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new ServerHub();
    }

    /**
     * Instantiates a new hub.
     */
    private ServerHub() {
        playersByName = new HashMap<String, Player>(8);
        playersByID = new HashMap<Integer, Player>(8);
    }

    /**
     * Initializes the player-HUB.
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
