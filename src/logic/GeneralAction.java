/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: GeneralAction.java
 * Type: logic.GeneralAction
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

/**
 * The Class GeneralAction.
 */
public abstract class GeneralAction {
    
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
     * Instantiates a new general action.
     *
     * @param name the name
     */
    public GeneralAction(String name) {
        this.name = name;
    }
    
    /**
     * Do action.
     *
     * @param sender the sender
     * @param p the p
     */
    abstract void doAction(Object sender, Player p);
}
