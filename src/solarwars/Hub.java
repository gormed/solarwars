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
 * File: Hub.java
 * Type: solarwars.Hub
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import logic.Player;

/**
 * The Class Hub.
 */
public class Hub {

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        for (Map.Entry<String, Player> cursor : playersByName.entrySet()) {
            players.add(cursor.getValue().getId(), cursor.getValue());
        }

        return players;
    }
    /** The player names. */
    public static HashMap<Integer, Player> playersByID;
    /** The players. */
    private static HashMap<String, Player> playersByName;
    /** The local player. */
    private static Player localPlayer;

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
     * Initializes the player-HUB.
     */
    public void initialize(Player localPlayer, ArrayList<Player> players) {
        playersByID = new HashMap<Integer, Player>();
        playersByName = new HashMap<String, Player>();
        this.localPlayer = localPlayer;
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
