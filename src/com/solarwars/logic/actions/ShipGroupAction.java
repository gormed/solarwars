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
 * File: ShipGroupAction.java
 * Type: com.solarwars.logic.actions.ShipGroupAction
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.actions;

import com.solarwars.entities.ShipGroup;
import com.solarwars.logic.Player;

/**
 * The Class ShipGroupAction.
 */
public abstract class ShipGroupAction {

    /** The name. */
    private final String name;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Instantiates a new ship action.
     *
     * @param name the name
     */
    public ShipGroupAction(String name) {
        this.name = name;
    }

    /**
     * Do action.
     *
     * @param sender the sender
     * @param shipGroup the ship group
     * @param p the p
     */
    public abstract boolean doAction(Object sender, ShipGroup shipGroup, Player p);
}
