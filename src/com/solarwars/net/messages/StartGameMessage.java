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
 * File: StartGameMessage.java
 * Type: com.solarwars.net.messages.StartGameMessage
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.solarwars.logic.Player;

import java.util.ArrayList;

/**
 * The Class StartGameMessage.
 *
 * @author Hans
 */
@Serializable
public class StartGameMessage extends AbstractMessage {

    /** The seed. */
    private long seed;
    /** The players. */
    private ArrayList<Player> players;
    private boolean ingame = false;

    /**
     * Gets the players.
     *
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the seed.
     *
     * @return the seed
     */
    public long getSeed() {
        return seed;
    }

    public boolean isIngame() {
        return ingame;
    }

    /**
     * Instantiates a new player accepted message.
     */
    public StartGameMessage() {
    }

    /**
     * Instantiates a new start game message.
     *
     * @param seed the seed
     * @param players the players
     */
    public StartGameMessage(long seed, ArrayList<Player> players) {
        this.seed = seed;
        this.players = players;
    }

    public StartGameMessage(boolean ingame) {
        this.ingame = ingame;
    }
}
