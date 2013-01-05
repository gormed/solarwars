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
 * File: PlayerReadyMessage.java
 * Type: com.solarwars.net.messages.PlayerReadyMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
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
