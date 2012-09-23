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
 * File: PlayerReadyMessage.java
 * Type: com.solarwars.net.messages.PlayerReadyMessage
 * 
 * Documentation created: 19.09.2012 - 19:47:45 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * The class PlayerReadyMessage.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
@Serializable
public class PlayerReadyMessage extends AbstractMessage {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private int id;
    private boolean isReady;
    
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public PlayerReadyMessage() {
    }

    public PlayerReadyMessage(int id, boolean isReady) {
        this.id = id;
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }

    public int getPlayerID() {
        return id;
    }
}
