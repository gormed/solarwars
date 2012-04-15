/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hans
 */
@Serializable
public class PlayerState {

    public PlayerState() {
    }

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
    
    public boolean lost;
    
    public int defeatedPlayerID = -1;
    
    public ArrayList<Integer> multiSelectedPlanets = new ArrayList<Integer>();
}
