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

    /** The Constant PLANET_SELECT. */
    public static final String PLANET_SELECT = "SelectPlanet";
    /** The Constant PLANET_MULTI_SELECT. */
    public static final String PLANET_MULTI_SELECT = "MultiSelectPlanet";
    /** The Constant PLANET_ATTACK. */
    public static final String PLANET_ATTACK = "AttackPlanet";
    /** The Constant SHIP_SELECT. */
    public static final String SHIP_SELECT = "RedirectShip";
    /** The Constant SHIP_ARRIVES. */
    public static final String SHIP_ARRIVES = "ArrivesShip";
    /** The Constant SHIP_MULTI_SELECT. */
    public static final String SHIP_MULTI_SELECT = "MultiSelectShips";
    /** The Constant PLANET_CAPTURE. */
    public static final String PLANET_CAPTURE = "CapturePlanet";
    /** The Constant GAME_OVER. */
    public static final String GAME_OVER = "GameOver";
    /** The game. */
    private SolarWarsGame game;
    /** The application. */
    private SolarWarsApplication application;
    /** The action lib. */
    private ActionLib actionLib;
    /** The instance. */
    private static Gameplay instance;
    /** The current level. */
    private static Level currentLevel = null;

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
     * @param level the level
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
            public void doAction(Object sender, long delay, AbstractPlanet planet, Player p) {
                if (planet.getOwner() == p) {
                    p.clearPlanetMultiSelect();
                    p.selectPlanet(planet);

                } else if (planet.getOwner() != null) {
                } else {
                }
            }
        };

        PlanetAction multiSelectPlanet = new PlanetAction(PLANET_MULTI_SELECT) {

            @Override
            public void doAction(Object sender, long delay, AbstractPlanet planet, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return;
                }
                if (sender instanceof IsoControl) {
                    IsoControl control = (IsoControl) sender;
                    ArrayList<AbstractPlanet> selection = control.getSelectedPlanets();
                    p.multiSelectPlanets(selection);
                }

//                if (planet.getOwner() == p) {
//
//                    p.selectPlanet(planet);
//
//                } else if (planet.getOwner() != null) {
//                } else {
//                }
            }
        };

        PlanetAction attackPlanet = new PlanetAction(PLANET_ATTACK) {

            @Override
            public void doAction(Object sender, long delay, AbstractPlanet target, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return;
                }
                if (p.hasMultiSelectedPlanets()) {
                    multiAttackPlanet(p.getMultiSelectPlanets(), target, p);
                } else if (p.hasSelectedPlanet()) {
                    singleAttackPlanet(p.getSelectedPlanet(), target, p);
                } else if (p.hasMultiSelectedShipGroups()) {
                    multiAttackShipGroup(p.getMultiSelectShipGroups(), target);
                } else if (p.hasSelectedShipGroup()) {
                    singleAttackShipGroup(p.getSelectedShipGroup(), target);
                }
//                target.syncronize(delay);
            }

            private void multiAttackPlanet(ArrayList<AbstractPlanet> planets, AbstractPlanet target, Player p) {
                for (AbstractPlanet planet : planets) {
                    singleAttackPlanet(planet, target, p);
                }
            }
            private void multiAttackShipGroup(ArrayList<ShipGroup> ships, AbstractPlanet target) {
                for (ShipGroup sg : ships) {
                    singleAttackShipGroup(sg, target);
                }
            }
            private void singleAttackShipGroup(ShipGroup shipGroup, AbstractPlanet target) {
                if (shipGroup != null) {
                    shipGroup.moveToPlanet(target);
                }
            }

            private void singleAttackPlanet(AbstractPlanet selction, AbstractPlanet target, Player p) {
                if (selction.getOwner() == p && !selction.equals(target)) {

                    int selected = (int) (selction.getShipCount() * p.getShipPercentage());
                    ShipGroup sg = new ShipGroup(
                            application.getAssetManager(),
                            currentLevel,
                            p,
                            selction,
                            target,
                            selected);
                    currentLevel.addShipGroup(p, sg);
                    p.createShipGroup(sg);


                }
            }
        };

        PlanetAction capturePlanet = new PlanetAction(PLANET_CAPTURE) {

            @Override
            public void doAction(Object sender, long delay, AbstractPlanet planet, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return;
                }
                if (planet.getOwner() == p) {
                    planet.incrementShips();
                } else {
                    planet.decrementShips();
                    if (planet.getShipCount() <= 0) {
                        p.capturePlanet(planet);
                        planet.emitCaptureParticles();
                        AudioManager.getInstance().
                                playSoundInstance(AudioManager.SOUND_CAPTURE);
                    }
                }
                AudioManager.getInstance().
                        playSoundInstance(AudioManager.SOUND_BEEP);
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
            public void doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return;
                }
                if (shipGroup.getOwner() == p) {
                    p.clearShipGroupMultiSelect();
                    p.selectShipGroup(shipGroup);
                }
            }
        };

        ShipGroupAction shipArrives = new ShipGroupAction(SHIP_ARRIVES) {

            @Override
            public void doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (sender instanceof AbstractShip) {
                    AbstractShip s = (AbstractShip) sender;
                    shipGroup.removeShip(s);
                    if (shipGroup.getShipCount() < 1) {
                        currentLevel.removeShipGroup(p, shipGroup);
                        p.destroyShipGroup(shipGroup);
                        Player.reportPlayerLost(null, p);
                    }
                }
            }
        };

        ShipGroupAction multiSelectShipGroup = new ShipGroupAction(SHIP_MULTI_SELECT) {

            @Override
            void doAction(Object sender, ShipGroup shipGroup, Player p) {
                if (p.hasLost() || currentLevel.isGameOver()) {
                    return;
                }
                if (sender instanceof IsoControl) {
                    IsoControl control = (IsoControl) sender;
                    ArrayList<ShipGroup> selection = control.getSelectedShipGroups();
                    p.multiSelectShipGroup(selection);
                }
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
            void doAction(Object sender, Player a, Player b) {

                Player victorious = a;
                Player defeated = b;

                defeated.setLost();
                if (victorious != null) {
                    victorious.setDefeatedPlayer(defeated.getId());
                    if (NetworkManager.getInstance().isMultiplayerGame()) {
                        NetworkManager.getInstance().getChatModule().
                                playerDefeats(victorious, defeated);
                    }
                }

//                if (victorious.getId() == Hub.getLocalPlayer().getId()) {
//                } else if (defeated.getId() == Hub.getLocalPlayer().getId()) {
//                } else {
//                    // Display that a defeated b
//                }
            }
        };

        actionLib.getGeneralActions().put(GAME_OVER, gameOver);
    }
}
