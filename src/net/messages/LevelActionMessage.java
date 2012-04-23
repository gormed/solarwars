/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.HashMap;

/**
 *
 * @author Hans
 */
@Serializable
public class LevelActionMessage extends AbstractMessage {

    private HashMap<Integer, Integer> planetShipCount;
    private long levelSeed;
    private long serverTime;

    public LevelActionMessage() {
    }

    public LevelActionMessage(HashMap<Integer, Integer> planetShipCount, long levelSeed, long serverTime) {
        this.planetShipCount = planetShipCount;
        this.levelSeed = levelSeed;
        this.serverTime = serverTime;
    }

    public long getLevelSeed() {
        return levelSeed;
    }

    public HashMap<Integer, Integer> getPlanetShipCount() {
        return planetShipCount;
    }

    public long getServerTime() {
        return serverTime;
    }
    
    
}
