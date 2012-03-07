/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.Vector3f;
import entities.AbstractPlanet;
import entities.ShipGroup;
import entities.SimpleShip;
import java.util.Random;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class Gameplay {

    public static final String PLANET_SELECT = "SelectPlanet";
    public static final String PLANET_ATTACK = "AttackPlanet";
    public static final String PLANET_MOVE = "MovePlanet";
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

        final Random r = new Random(System.currentTimeMillis());

        // ========================================================
        // PLANET ACTIONS
        // ========================================================

        PlanetAction selectPlanet = new PlanetAction(PLANET_SELECT) {

            @Override
            public void doAction(AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {

                    p.selectPlanet(planet);

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

                            int selected = (int) (sel.getShips() * p.getShipPercentage());
                            ShipGroup sg = new ShipGroup(
                                    application.getAssetManager(),
                                    level.Level.getCurrentLevel(),
                                    p,
                                    sel,
                                    planet,
                                    selected);
                            level.Level.getCurrentLevel().addShipGroup(p, sg);

                        }
                    }
                }
            }
        };

        PlanetAction movePlanet = new PlanetAction(PLANET_MOVE) {

            @Override
            public void doAction(AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {
                    if (p.hasSelectedPlanet()) {
                        int selected = (int) (p.getSelectedPlanet().getShips() * p.getShipPercentage());
                        ShipGroup sg = new ShipGroup(
                                application.getAssetManager(),
                                level.Level.getCurrentLevel(),
                                p,
                                p.getSelectedPlanet(),
                                planet,
                                selected);
                        level.Level.getCurrentLevel().addShipGroup(p, sg);
                    }
                }
            }
        };

        actionLib.getPlanetActions().put(PLANET_SELECT, selectPlanet);
        actionLib.getPlanetActions().put(PLANET_ATTACK, attackPlanet);
        actionLib.getPlanetActions().put(PLANET_MOVE, movePlanet);


        // ========================================================
        // SHIP ACTIONS
        // ========================================================




        // ========================================================
        // GENERAL ACTIONS
        // ========================================================

    }
}
