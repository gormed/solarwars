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
 * File: PlanetAction.java
 * Type: logic.PlanetAction
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.AbstractPlanet;

/**
 * The Class PlanetAction.
 */
public abstract class PlanetAction {

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
     * Instantiates a new planet action.
     *
     * @param name the name
     */
    public PlanetAction(String name) {
        this.name = name;
    }

    //public abstract void doAction(Object sender, AbstractPlanet planet, Player p);

    /**
     * Do action.
     *
     * @param sender the sender
     * @param planet the planet
     * @param p the p
     */
    abstract void doAction(Object sender, long delay, AbstractPlanet planet, Player p);
}
