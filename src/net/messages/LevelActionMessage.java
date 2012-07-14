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
 * File: LevelActionMessage.java
 * Type: net.messages.LevelActionMessage
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.HashMap;

/**
 * The Class LevelActionMessage.
 *
 * @author Hans
 */
@Serializable
public class LevelActionMessage extends AbstractMessage {

    /** The planet ship count. */
    private HashMap<Integer, Integer> planetShipCount;
    
    /** The level seed. */
    private long levelSeed;
    
    /** The server time. */
    private long serverTime;

    /**
     * Instantiates a new level action message.
     */
    public LevelActionMessage() {
    }

    /**
     * Instantiates a new level action message.
     *
     * @param planetShipCount the planet ship count
     * @param levelSeed the level seed
     * @param serverTime the server time
     */
    public LevelActionMessage(HashMap<Integer, Integer> planetShipCount, long levelSeed, long serverTime) {
        this.planetShipCount = planetShipCount;
        this.levelSeed = levelSeed;
        this.serverTime = serverTime;
    }

    /**
     * Gets the level seed.
     *
     * @return the level seed
     */
    public long getLevelSeed() {
        return levelSeed;
    }

    /**
     * Gets the planet ship count.
     *
     * @return the planet ship count
     */
    public HashMap<Integer, Integer> getPlanetShipCount() {
        return planetShipCount;
    }

    /**
     * Gets the server time.
     *
     * @return the server time
     */
    public long getServerTime() {
        return serverTime;
    }
    
    
}
