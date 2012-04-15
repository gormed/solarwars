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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: PlayerAcceptedMessage.java
 * Type: net.messages.PlayerAcceptedMessage
 * 
 * Documentation created: 31.03.2012 - 19:27:49 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import logic.Player;

/**
 *
 * @author Hans
 */
@Serializable
public class StartGameMessage extends AbstractMessage {

    private long seed;
    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public long getSeed() {
        return seed;
    }

    /**
     * Instantiates a new player accepted message.
     */
    public StartGameMessage() {
    }

    public StartGameMessage(long seed, ArrayList<Player> players) {
        this.seed = seed;
        this.players = players;
    }
}