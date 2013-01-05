/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: StartGameMessage.java
 * Type: com.solarwars.net.messages.StartGameMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
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
