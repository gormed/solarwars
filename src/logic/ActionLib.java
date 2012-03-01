/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

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
}
