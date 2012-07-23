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
 * File: PlayerState.java
 * Type: logic.PlayerState
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 * The Class PlayerState.
 *
 * @author Hans
 */
@Serializable
public class PlayerState {

    /**
     * Instantiates a new player state.
     */
    public PlayerState() {
    }

    /**
     * Instantiates a new player state.
     *
     * @param selectedPlanetId the selected planet id
     * @param selectedShipGroupId the selected ship group id
     * @param name the name
     * @param color the color
     */
    public PlayerState(int selectedPlanetId, int selectedShipGroupId, String name, ColorRGBA color) {
        this.selectedPlanetId = selectedPlanetId;
        this.selectedShipGroupId = selectedShipGroupId;
        this.name = name;
        this.color = color;
    }
    /** The selected planet. */
    public int selectedPlanetId;
    /** The selected ship group. */
    public int selectedShipGroupId;
    /** The ship percentage. */
    public float shipPercentage = 0.5f;
    /** The ship count. */
    public int shipCount = 0;
    /** The name. */
    public String name;
    /** The color. */
    public ColorRGBA color;
    /** The lost. */
    public boolean lost;
    
    public boolean leaver = false;
    /** The defeated player id. */
    public int defeatedPlayerID = -1;
    /** The multi selected planets. */
    public ArrayList<Integer> multiSelectedPlanets = new ArrayList<Integer>();
    /** The multi selected shipgroups. */
    public ArrayList<Integer> multiSelectedShipGroups = new ArrayList<Integer>();
}
