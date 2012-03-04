/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.Vector3f;
import entities.AbstractPlanet;
import entities.SimpleShip;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class Gameplay {
    
    public static final String PLANET_SELECT = "SelectPlanet";
    public static final String PLANET_ATTACK = "AttackPlanet";
    
    private SolarWarsGame game;
    private SolarWarsApplication application;
    private ActionLib actionLib;
    
    private static Gameplay instance;
    
    public static void initialize() {
        if (instance == null) {
            instance = new Gameplay();
        }
    }
    
    private Gameplay() {
        actionLib = ActionLib.getInstance();
        game = SolarWarsGame.getInstance();
        application = game.getApplication();
        
        
        PlanetAction selectPlanet = new PlanetAction(PLANET_SELECT) {

            @Override
            public void doAction(AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {
                    
                    int ships = planet.getShips();
                    p.selectPlanet(planet);
                    if (ships < 4 || p.getShipSelectionCount() > ships-4)
                        p.setShipSelectionCount(ships);
                    else
                        p.setShipSelectionCount(p.getShipSelectionCount()+4);
                        
                } else if (planet.getOwner() != null) {
                    
                } else {
                    
                }
            }
        };
        
        PlanetAction attackPlanet = new PlanetAction(PLANET_ATTACK) {

            @Override
            public void doAction(AbstractPlanet planet, Player p) {
                if (planet.getOwner() != p) {
                    if (p.hasSelectedPlanet()) {
                        AbstractPlanet sel = p.getSelectedPlanet();
                        if (sel.getOwner() == p) {
                            for (int i = 0; i < p.getShipSelectionCount(); i++) {
                                SimpleShip s = new SimpleShip(
                                        application.getAssetManager(), 
                                        level.Level.getCurrentLevel(), 
                                        sel.getPosition().clone(), 
                                        p);
                                s.createShip();
                                level.Level.getCurrentLevel().addShip(p, s);
                                s.moveToPlanet(planet);
                                sel.decrementShips();
                            }
                            p.setShipSelectionCount(0);
                        }
                    }
                }
            }
        };
        
        actionLib.getPlanetActions().put(PLANET_SELECT, selectPlanet);
        actionLib.getPlanetActions().put(PLANET_ATTACK, attackPlanet);
    }
}
