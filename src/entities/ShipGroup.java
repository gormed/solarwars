/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.scene.Node;
import java.util.ArrayList;
import logic.Player;

/**
 *
 * @author Hans
 */
public class ShipGroup extends Node {
    private ArrayList<AbstractShip> ships;
    private Player owner;
    
    public ShipGroup(ArrayList<AbstractShip> ships, Player p) {
        this.ships = ships;
        this.owner = p;
        attachAll();
    }
    
    public ShipGroup() {
        
    }
    
    private void attachAll() {
        for (AbstractShip a : ships) {
            this.attachChild(a);
        }
    }
    
    public void addShip(AbstractShip ship) {
        ships.add(ship); 
        this.attachChild(ship); 
    }
}
