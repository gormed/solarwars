/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.ShipGroup;

/**
 *
 * @author Hans
 */
public abstract class ShipAction {

    private final String name;

    public String getName() {
        return name;
    }

    public ShipAction(String name) {
        this.name = name;
    }

    public abstract void doAction(ShipGroup shipGroup, Player p);
}
