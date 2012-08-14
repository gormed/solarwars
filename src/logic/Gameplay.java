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
 * File: Gameplay.java
 * Type: logic.Gameplay
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.AbstractPlanet;
import entities.AbstractShip;
import entities.Ranged;
import entities.ShipGroup;
import java.util.ArrayList;
import net.NetworkManager;
import solarwars.AudioManager;
import solarwars.IsoControl;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 * The Class Gameplay.
 */
public class Gameplay {

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
    private SolarWarsGame game;
    private SolarWarsApplication application;
    private ActionLib actionLib;
    private static Gameplay instance;
    private static Level currentLevel = null;
    static double GAMETICK = 0;

    public static double getGameTick() {
        return GAMETICK;
    }

    /**
     * Gets the current level.
     * 
     * @return the current level
     */
    public static Level getCurrentLevel() {
        if (currentLevel != null) {
            return currentLevel;
        }
        return null;
    }

    /**
     * Initializes the.
     * 
     * @param level
     *            the level
     */
    public static void initialize(Level level) {
        if (instance == null) {
            instance = new Gameplay();
        }
        currentLevel = level;
        currentLevel.resetEntityIDs();
    }

    /**
     * Instantiates a new gameplay.
     */
    private Gameplay() {
        actionLib = ActionLib.getInstance();
        game = SolarWarsGame.getInstance();
        application = game.getApplication();

        // ========================================================
        // PLANET ACTIONS
        // ========================================================

        PlanetAction selectPlanet = new PlanetAction(PLANET_SELECT) {

            @Override
            public boolean doAction(Object sender, long delay,
                    AbstractPlanet planet, Player p) {
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
                if (dist > rangedObject.getRange()) {
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
            boolean doAction(Object sender, ShipGroup shipGroup, Player p) {
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
            boolean doAction(Object sender, Player a, Player b) {

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
            boolean doAction(Object sender, Player a, Player b) {
                return false;
            }
        };

        actionLib.getGeneralActions().put(GAME_OVER, gameOver);
        actionLib.getGeneralActions().put(DEFEATED, playerDefeated);
    }
}
