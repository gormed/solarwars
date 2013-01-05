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
 * File: PlayerState.java
 * Type: com.solarwars.logic.PlayerState
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

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
    
    public int selectedPlanetId;
    public int selectedShipGroupId;
    public int defeatedPlayerID = -1;
    public int shipCount = 0;
    public float shipPercentage = 0.5f;
    public String name;
    public ColorRGBA color;
    public boolean hasLost;
    public boolean leaver = false;
    public boolean isReady = false;
    public ArrayList<Integer> multiSelectedPlanets = new ArrayList<Integer>();
    public ArrayList<Integer> multiSelectedShipGroups = new ArrayList<Integer>();
}
