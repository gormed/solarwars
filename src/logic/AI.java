/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.ArrayList;

/**
 *
 * @author Hans
 */
public class AI {

    private Player player;
    private ArrayList<AbstractPlanet> planets;
    private ArrayList<ShipGroup> shipGroups;
    
    public AI(Player player) {
        this.player = player;
        planets = new ArrayList<AbstractPlanet>();
        shipGroups = new ArrayList<ShipGroup>();
    }
    
}
