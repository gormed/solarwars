/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.AbstractPlanet;

/**
 *
 * @author Hans
 */
public abstract class PlanetAction {

    private final String name;

    public String getName() {
        return name;
    }

    public PlanetAction(String name) {
        this.name = name;
    }

    public abstract void doAction(AbstractPlanet planet, Player p);
}
