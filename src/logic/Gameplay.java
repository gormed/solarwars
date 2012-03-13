/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.AbstractPlanet;
import entities.AbstractShip;
import entities.ShipGroup;
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
    public static final String SHIP_REDIRECT = "RedirectShip";
    public static final String SHIP_ARRIVES = "ArrivesShip";
    public static final String PLANET_CAPTURE = "CapturePlanet";
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
            public void doAction(Object sender, AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {

                    p.selectPlanet(planet);

                } else if (planet.getOwner() != null) {
                } else {
                }
            }
        };

        PlanetAction attackPlanet = new PlanetAction(PLANET_ATTACK) {

            @Override
            public void doAction(Object sender, AbstractPlanet planet, Player p) {
                if (p.hasSelectedPlanet()) {
                    AbstractPlanet sel = p.getSelectedPlanet();
                    if (sel.getOwner() == p && !sel.equals(planet)) {

                        int selected = (int) (sel.getShips() * p.getShipPercentage());
                        ShipGroup sg = new ShipGroup(
                                application.getAssetManager(),
                                logic.level.Level.getCurrentLevel(),
                                p,
                                sel,
                                planet,
                                selected);
                        logic.level.Level.getCurrentLevel().addShipGroup(p, sg);
                        p.createShipGroup(sg);
                    }
                } else if (p.hasSelectedShipGroup()) {
                    ShipGroup sg = p.getSelectedShipGroup();
                    sg.moveToPlanet(planet);
                }
            }
        };

        PlanetAction capturePlanet = new PlanetAction(PLANET_CAPTURE) {

            @Override
            public void doAction(Object sender, AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {
                    planet.incrementShips();
                } else {
                    planet.decrementShips();
                    if (planet.getShips() == 0) {
                        p.capturePlanet(planet);
                    }
                }
            }
        };

        actionLib.getPlanetActions().put(PLANET_SELECT, selectPlanet);
        actionLib.getPlanetActions().put(PLANET_ATTACK, attackPlanet);
        actionLib.getPlanetActions().put(PLANET_CAPTURE, capturePlanet);

        // ========================================================
        // SHIP ACTIONS
        // ========================================================

        ShipAction redirectShipGroup = new ShipAction(SHIP_REDIRECT) {

            @Override
            public void doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (shipGroup.getOwner() == p) {
                    p.selectShipGroup(shipGroup);
                }
            }
        };

        ShipAction shipArrives = new ShipAction(SHIP_ARRIVES) {

            @Override
            public void doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (sender instanceof AbstractShip) {
                    AbstractShip s = (AbstractShip) sender;
                    shipGroup.removeShip(s);
                    if (shipGroup.getShipCount() < 1) {
                        logic.level.Level.getCurrentLevel().removeShipGroup(p, shipGroup);
                        p.destroyShipGroup(shipGroup);
                    }
                }
            }
        };

        actionLib.getShipActions().put(SHIP_REDIRECT, redirectShipGroup);
        actionLib.getShipActions().put(SHIP_ARRIVES, shipArrives);

        // ========================================================
        // GENERAL ACTIONS
        // ========================================================



    }
}
