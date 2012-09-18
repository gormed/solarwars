/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or application 
 * in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: SolarWars
 * File: DeathmatchGameplay.java
 * Type: com.solarwars.logic.DeathmatchGameplay
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import java.util.ArrayList;

import com.solarwars.AudioManager;
import com.solarwars.IsoControl;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.AbstractShip;
import com.solarwars.entities.Ranged;
import com.solarwars.entities.ShipGroup;
import com.solarwars.logic.actions.GeneralAction;
import com.solarwars.logic.actions.PlanetAction;
import com.solarwars.logic.actions.ShipGroupAction;
import com.solarwars.net.NetworkManager;

/**
 * The Class DeathmatchGameplay implements the handling of 
 * deathmatch singleplayer and multiplayer matches.
 * @author Hans Ferchland
 */
public class DeathmatchGameplay extends AbstractGameplay {
    // Action Strings

    public static final String PLANET_SELECT = "SelectPlanet";
    public static final String PLANET_MULTI_SELECT = "MultiSelectPlanet";
    public static final String PLANET_ATTACK = "AttackPlanet";
    public static final String PLANET_CAPTURE = "CapturePlanet";
    public static final String SHIP_SELECT = "RedirectShip";
    public static final String SHIP_ARRIVES = "ArrivesShip";
    public static final String SHIP_MULTI_SELECT = "MultiSelectShips";
    public static final String GAME_OVER = "GameOver";
    public static final String DEFEATED = "Defeated";

    public DeathmatchGameplay() {
        super();
//        SHIP_SPEED = 0.75f;
//        PLANET_RANGE = 1.75f;
    }

    /**
     * Implements the normal gameplay from first versions. Only deathmatch mode.
     */
    @Override
    protected void createGameplay() {
        // ========================================================
        // PLANET ACTIONS
        // ========================================================

        PlanetAction selectPlanet = new PlanetAction(PLANET_SELECT) {

            @Override
            public boolean doAction(Object sender, long delay,
                    AbstractPlanet planet, Player p) {
                if (planet == null || planet.getOwner() == null || planet.getOwner() != p) {
                    p.selectPlanet(null);
                    return true;
                }
                if (planet.getOwner() == p) {
                    p.clearPlanetMultiSelect();
                    p.selectPlanet(planet);
                    return true;
                }
                return false;
            }
        };

        PlanetAction multiSelectPlanet = new PlanetAction(PLANET_MULTI_SELECT) {

            @Override
            public boolean doAction(Object sender, long delay,
                    AbstractPlanet planet, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return false;
                }
                if (sender instanceof IsoControl) {
                    IsoControl control = (IsoControl) sender;
                    ArrayList<AbstractPlanet> selection = control.getSelectedPlanets();
                    p.multiSelectPlanets(selection);
                    return true;
                }
                return false;
            }
        };

        PlanetAction attackPlanet = new PlanetAction(PLANET_ATTACK) {

            @Override
            public boolean doAction(Object sender, long delay,
                    AbstractPlanet target, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return false;
                }
                if (p.hasMultiSelectedPlanets()) {
                    return multiAttackPlanet(p.getMultiSelectPlanets(),
                            target,
                            p);

                } else if (p.hasSelectedPlanet()) {
                    return singleAttackPlanet(p.getSelectedPlanet(), target, p);
                } else if (p.hasMultiSelectedShipGroups()) {
                    return multiAttackShipGroup(p.getMultiSelectShipGroups(),
                            target);
                } else if (p.hasSelectedShipGroup()) {
                    return singleAttackShipGroup(p.getSelectedShipGroup(),
                            target);
                }
                return false;
                // target.syncronize(delay);
            }

            private boolean isDistanceToFar(Ranged rangedObject,
                    AbstractPlanet target) {

                float dist = rangedObject.getPosition().distance(target.getPosition());
                if (dist > rangedObject.getRange() || !AbstractGameplay.RANGE_ENABLED) {
                    return true;
                }
                return false;
            }

            private boolean multiAttackPlanet(ArrayList<AbstractPlanet> planets,
                    AbstractPlanet target, Player p) {
                for (AbstractPlanet planet : planets) {
                    singleAttackPlanet(planet, target, p);
                }
                return true;
            }

            private boolean multiAttackShipGroup(ArrayList<ShipGroup> ships,
                    AbstractPlanet target) {
                for (ShipGroup sg : ships) {
                    singleAttackShipGroup(sg, target);
                }
                return true;
            }

            private boolean singleAttackShipGroup(ShipGroup shipGroup,
                    AbstractPlanet target) {
                if (shipGroup != null) {

                    if (isDistanceToFar(shipGroup, target)) {
                        return false;
                    }
                    shipGroup.moveToPlanet(target);
                    return true;
                }
                return false;
            }

            private boolean singleAttackPlanet(AbstractPlanet selection,
                    AbstractPlanet target, Player p) {
                if (selection.getOwner() == p && !selection.equals(target)) {

                    if (isDistanceToFar(selection, target)) {
                        return false;
                    }

                    int selected = (int) (selection.getShipCount() * p.getShipPercentage());
                    ShipGroup sg = new ShipGroup(application.getAssetManager(),
                            currentLevel, p, selection, target, selected);
                    currentLevel.addShipGroup(p, sg);
                    p.createShipGroup(sg);
                    return true;

                }
                return false;
            }
        };

        PlanetAction capturePlanet = new PlanetAction(PLANET_CAPTURE) {

            @Override
            public boolean doAction(Object sender, long delay,
                    AbstractPlanet planet, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return false;
                }
                if (planet.getOwner() == p) {
                    planet.incrementShips();
                    AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_BEEP);
                    return true;
                } else {
                    planet.decrementShips();
                    AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_BEEP);
                    if (planet.getShipCount() <= 0) {
                        p.capturePlanet(planet);
                        planet.emitCaptureParticles();
                        AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_CAPTURE);
                    }
                    return true;
                }
            }
        };

        actionLib.getPlanetActions().put(PLANET_SELECT, selectPlanet);
        actionLib.getPlanetActions().put(PLANET_MULTI_SELECT, multiSelectPlanet);
        actionLib.getPlanetActions().put(PLANET_ATTACK, attackPlanet);
        actionLib.getPlanetActions().put(PLANET_CAPTURE, capturePlanet);

        // ========================================================
        // SHIP ACTIONS
        // ========================================================

        ShipGroupAction redirectShipGroup = new ShipGroupAction(SHIP_SELECT) {

            @Override
            public boolean doAction(Object sender, ShipGroup shipGroup,
                    Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return false;
                }
                if (shipGroup.getOwner() == p) {
                    p.clearShipGroupMultiSelect();
                    p.selectShipGroup(shipGroup);
                    return true;
                }
                return false;
            }
        };

        ShipGroupAction shipArrives = new ShipGroupAction(SHIP_ARRIVES) {

            @Override
            public boolean doAction(Object sender, ShipGroup shipGroup,
                    Player p) {
                if (sender instanceof AbstractShip) {
                    AbstractShip s = (AbstractShip) sender;
                    shipGroup.removeShip(s);
                    if (shipGroup.getShipCount() < 1) {
                        currentLevel.removeShipGroup(p, shipGroup);
                        p.destroyShipGroup(shipGroup);
                        Player.reportPlayerLost(null, p);
                    }
                    return true;
                }
                return false;
            }
        };

        ShipGroupAction multiSelectShipGroup = new ShipGroupAction(
                SHIP_MULTI_SELECT) {

            @Override
            public boolean doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return false;
                }
                if (sender instanceof IsoControl) {
                    IsoControl control = (IsoControl) sender;
                    ArrayList<ShipGroup> selection = control.getSelectedShipGroups();
                    p.multiSelectShipGroup(selection);
                    return true;
                }
                return false;

            }
        };

        actionLib.getShipActions().put(SHIP_SELECT, redirectShipGroup);
        actionLib.getShipActions().put(SHIP_ARRIVES, shipArrives);
        actionLib.getShipActions().put(SHIP_MULTI_SELECT, multiSelectShipGroup);

        // ========================================================
        // GENERAL ACTIONS
        // ========================================================

        GeneralAction gameOver = new GeneralAction(GAME_OVER) {

            @Override
            public boolean doAction(Object sender, Player a, Player b) {

                Player victorious = a;
                Player defeated = b;
                if (defeated.hasLost()) {
                    return false;
                }
                defeated.setLost();
                if (victorious != null) {
                    victorious.setDefeatedPlayer(defeated.getId());
                    if (NetworkManager.getInstance().isMultiplayerGame()) {
                        NetworkManager.getInstance().getChatModule().playerDefeats(victorious, defeated);
                    }
                    return true;
                }
                return false;
            }
        };

        GeneralAction playerDefeated = new GeneralAction(DEFEATED) {

            @Override
            public boolean doAction(Object sender, Player a, Player b) {
                return false;
            }
        };

        actionLib.getGeneralActions().put(GAME_OVER, gameOver);
        actionLib.getGeneralActions().put(DEFEATED, playerDefeated);

    }
}
