/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Hans
 */
public class ShipGroup extends Node {
    private ArrayList<AbstractShip> ships;
    
    public ShipGroup(ArrayList<AbstractShip> ships) {
        this.ships = ships;
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
