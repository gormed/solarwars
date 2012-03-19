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
import logic.Player;

/**
 * The Class Hub.
 */
public class Hub {

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
    
    public static void resetPlayerID() {
        PLAYER_ID = 0;
    }
    
    /** The player names. */
    public static ArrayList<String> playerNames;
    /** The players. */
    private HashMap<String, Player> players;
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
        players = new HashMap<String, Player>();
        playerNames = new ArrayList<String>();
    }

    /**
     * Initializes the player-HUB.
     */
    public void initialize(Player localPlayer) {
        if (this.localPlayer != null) {
            players.remove(this.localPlayer.getName());
            playerNames.remove(this.localPlayer.getId());
        }
        this.localPlayer = localPlayer;
        addPlayer(localPlayer);
    }

    /**
     * Adds the player.
     *
     * @param p the p
     */
    public void addPlayer(Player p) {
        players.put(p.getName(), p);
        playerNames.add(p.getId(), p.getName());
    }

    /**
     * Removes the player.
     *
     * @param p the p
     */
    public void removePlayer(Player p) {
        players.remove(p.getName());
        playerNames.remove(p.getId());
    }

    /**
     * Gets the player.
     *
     * @param name the name
     * @return the player
     */
    public Player getPlayer(String name) {
        return players.get(name);
    }

    /**
     * Gets the player.
     *
     * @param id the id
     * @return the player
     */
    public Player getPlayer(int id) {
        return players.get(playerNames.get(id));
    }

    /**
     * Gets the player count.
     *
     * @return the player count
     */
    public int getPlayerCount() {
        return players.size();
    }
}
