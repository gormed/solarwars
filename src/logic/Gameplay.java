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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: Gameplay.java
 * Type: logic.Gameplay
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.AbstractPlanet;
import entities.AbstractShip;
import entities.ShipGroup;
import gui.elements.GameOverGUI;
import java.util.Map;
import java.util.Random;
import solarwars.Hub;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 * The Class Gameplay.
 */
public class Gameplay {

    /** The Constant PLANET_SELECT. */
    public static final String PLANET_SELECT = "SelectPlanet";
    /** The Constant PLANET_ATTACK. */
    public static final String PLANET_ATTACK = "AttackPlanet";
    /** The Constant SHIP_REDIRECT. */
    public static final String SHIP_REDIRECT = "RedirectShip";
    /** The Constant SHIP_ARRIVES. */
    public static final String SHIP_ARRIVES = "ArrivesShip";
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

                        int selected = (int) (sel.getShipCount() * p.getShipPercentage());
                        ShipGroup sg = new ShipGroup(
                                application.getAssetManager(),
                                currentLevel,
                                p,
                                sel,
                                planet,
                                selected);
                        currentLevel.addShipGroup(p, sg);
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
                    if (planet.getShipCount() <= 0) {
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
                        currentLevel.removeShipGroup(p, shipGroup);
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

        GeneralAction gameOver = new GeneralAction(GAME_OVER) {

            @Override
            void doAction(Object sender, Player a, Player b) {

                Player victorious = a;
                Player defeated = b;

                if (victorious.getId() == Hub.getLocalPlayer().getId()) {
                    localPlayerWins();
                } else if (defeated.getId() == Hub.getLocalPlayer().getId()) {
                    localPlayerLooses();
                } else {
                    // Display that a defeated b
                }
            }

            private void localPlayerWins() {
                if (lastPlayer()) {
                    currentLevel.setGameOver(true);
                    GameOverGUI gameOverGUI =
                            new GameOverGUI(
                            game,
                            currentLevel.getGui(),
                            GameOverGUI.GameOverState.WON);

                    gameOverGUI.display();
                } else {
                    //Display: "You defeated..."
                }

            }

            private void localPlayerLooses() {
                if (lastPlayer()) {
                    currentLevel.setGameOver(true);
                }

                GameOverGUI gameOverGUI =
                        new GameOverGUI(
                        game,
                        currentLevel.getGui(),
                        GameOverGUI.GameOverState.LOST);
                gameOverGUI.display();
            }

            private boolean lastPlayer() {
                int lostPlayerCount = 0;
                for (Map.Entry<Integer, Player> entry : Hub.playersByID.entrySet()) {
                    Player p = entry.getValue();
                    if (p != null) {
                        if (p.hasLost()) {
                            lostPlayerCount++;
                        }
                    }
                }
                return Hub.playersByID.size() - 1 == lostPlayerCount;
            }
        };

        actionLib.getGeneralActions().put(GAME_OVER, gameOver);
    }
}
