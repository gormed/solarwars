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
 * File: ShipAction.java
 * Type: logic.ShipAction
 * 
 * Documentation created: 31.03.2012 - 19:27:49 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.ShipGroup;

/**
 * The Class ShipAction.
 */
public abstract class ShipAction {

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
    public ShipAction(String name) {
        this.name = name;
    }

    /**
     * Do action.
     *
     * @param sender the sender
     * @param shipGroup the ship group
     * @param p the p
     */
    abstract void doAction(Object sender, ShipGroup shipGroup, Player p);
}
