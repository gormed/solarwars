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
 * File: ShipGroupAction.java
 * Type: com.solarwars.logic.ShipGroupAction
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import com.solarwars.entities.ShipGroup;

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
    abstract boolean doAction(Object sender, ShipGroup shipGroup, Player p);
}
