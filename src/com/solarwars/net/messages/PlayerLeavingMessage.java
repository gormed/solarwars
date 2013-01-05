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
 * File: PlayerLeavingMessage.java
 * Type: com.solarwars.net.messages.PlayerLeavingMessage
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.solarwars.logic.Player;

/**
 * The Class PlayerLeavingMessage.
 */
@Serializable
public class PlayerLeavingMessage extends AbstractMessage {

    /** The player. */
    private Player player;

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Instantiates a new player leaving message.
     */
    public PlayerLeavingMessage() {
    }

    /**
     * Instantiates a new player leaving message.
     *
     * @param player the player
     */
    public PlayerLeavingMessage(Player player) {
        this.player = player;
    }
}
