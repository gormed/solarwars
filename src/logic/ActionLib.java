/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.AbstractPlanet;
import entities.AbstractShip;
import java.util.HashMap;

/**
 *
 * @author Hans
 */
public class ActionLib {

    private HashMap<String, GeneralAction> generalActions;
    private HashMap<String, PlanetAction> planetActions;
    private HashMap<String, ShipAction> shipActions;
    private static ActionLib instance;

    public static ActionLib getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new ActionLib();
    }

    private ActionLib() {
        generalActions = new HashMap<String, GeneralAction>();
        planetActions = new HashMap<String, PlanetAction>();
        shipActions = new HashMap<String, ShipAction>();
    }

    public void initialize() {
    }

    public HashMap<String, GeneralAction> getGeneralActions() {
        return generalActions;
    }

    public HashMap<String, PlanetAction> getPlanetActions() {
        return planetActions;
    }

    public HashMap<String, ShipAction> getShipActions() {
        return shipActions;
    }

    public void invokeGeneralAction(Player p, String actionName) {
        generalActions.get(actionName).doAction(p);
    }

    public void invokePlanetAction(AbstractPlanet planet, Player p, String actionName) {
        planetActions.get(actionName).doAction(planet, p);
    }

    public void invokeShipAction(AbstractShip ship, Player p, String actionName) {
        shipActions.get(actionName).doAction(ship, p);
    }
}
