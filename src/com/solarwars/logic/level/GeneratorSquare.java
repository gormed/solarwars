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
 * File: GeneratorSquare.java
 * Type: com.solarwars.logic.level.GeneratorSquare
 * 
 * Documentation created: 10.09.2012 - 21:04:22 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.level;

import com.jme3.math.Vector2f;
import com.solarwars.entities.LevelBackground;
import com.solarwars.logic.Level;
import java.util.Random;

/**
 * The class GeneratorSquare.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class GeneratorSquare extends LevelGenerator {

    //==========================================================================
    //===   Private Fields
    //==========================================================================

    /* Anzahl der Ringe, die sich noch hinter einem Spieler befinden (außen) */
    /** The ringe behind. */
    private int outerRings = 1;
    /* space coordinates */
    /** The sp coord. */
    private boolean[][] spCoord;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public GeneratorSquare(Level level) {
        super(level);
        initArrays();
    }

    /**
     * Inits the arrays.
     */
    private void initArrays() {
        /* Initialisiere spCoord */
        int space = (level.getPlayersByID().size()) * 2 + (outerRings * 2 + 1);
        spCoord = new boolean[space][space];
        for (int i = 0; i < space; i++) {
            for (int j = 0; j < space; j++) {
                spCoord[i][j] = true;
            }
        }
    }

    @Override
    public boolean generate(long seed) {
        generateSquare(seed);
        return levelLoaded;
    }

    /**
     * Baut das Level mit einem quadratischen Spielfeld auf, bei dem
     * jeder Spieler den gleichen Abstand bis zum nächsten hat.
     * Startpositionen sind dabei zufällig und die Anzahl der Ringe ist
     * abhängig von der Spieleranzahl bzw. von dem Datenfeld ringeBehind!
     *
     * @param seed Zufallsvariable für den Aufbau.
     */
    public void generateSquare(long seed) {
        System.out.print("[" + seed + "] Generating level...");

        background =
                new LevelBackground(com.solarwars.SolarWarsGame.getInstance(), (int) seed);
        level.getRootNode().attachChild(background);

        randomizer = new Random(seed);
        //            int leftBottomX = Math.round(corners[0].x);
        //            int leftBottomZ = Math.round(corners[0].z);
        //            int topRightX = Math.round(corners[2].x);
        //            int topRightZ = Math.round(corners[2].z);
        int playerCount = level.getPlayersByID().size();
        int pointerX = 0;
        int pointerZ = 0;
        int arrayX = playerCount;
        int arrayZ = playerCount;
        int decrement = 0;
        int counter = 0;
        int zwCounter = 0;
        int multiplier = 1;
        int startPlanetNumber = 0;
        boolean top = true;
        boolean startPlanet = false;
        boolean lastRow = false;
        //ERZEUGUNG DES SPIELERRINGS
        arrayX = 0 + outerRings;
        arrayZ = -1 + outerRings;
        pointerX = playerCount;
        pointerZ = playerCount + 1;
        for (int lauf = 0; lauf < playerCount * 8; lauf++) {
            // Counter-Initialisierung
            // Wenn noch an der ersten Spalte gebaut wird nur counter++
            if (lastRow == false) {
                if (counter < playerCount * 2 + 1) {
                    counter++;
                    arrayZ++;
                    pointerZ--;
                } // Wenn oben erstellt wird, initialisiere counter und zwCounter entsprechend
                else if (top == true) {
                    zwCounter = counter;
                    counter = playerCount * 8 + decrement;
                    arrayZ = 0 + outerRings;
                    arrayX++;
                    pointerZ = playerCount;
                    pointerX--;
                    decrement--;
                    top = false;
                    if (counter == playerCount * 6 + 1) {
                        lastRow = true;
                    }
                } // Wenn unten erstellt wird, normaler counter++ über zwCounter
                else {
                    zwCounter++;
                    counter = zwCounter;
                    arrayZ = (playerCount * 2) + outerRings;
                    pointerZ = -playerCount;
                    top = true;
                }
            } else {
                counter--;
                pointerZ--;
                arrayZ++;
            }
            // Planetenerzeugung
            // An einer zufälligen Stelle den ersten Spielerplanet erstellen
            if (
                    (counter < (playerCount * 2 + 1) && 
                    randomTake() == true && startPlanet == false) || 
                    (counter == playerCount * 2 + 1 && startPlanet == false) ||
                    (counter == 8 && startPlanet == false)) {
                playerPlanetPositions.push(new Vector2f(pointerX, pointerZ));
                setSpCoordFalse(arrayX, arrayZ);
                startPlanetNumber = counter;
                startPlanet = true;
            } // Sobald 7 freie Planeten erstellt wurden, den nächsten Spielerplanet erstellen
            else if (counter % 8 == startPlanetNumber) {
                playerPlanetPositions.push(new Vector2f(pointerX, pointerZ));
                setSpCoordFalse(arrayX, arrayZ);
                multiplier++;
            } // ganz normalen Planet erstellen
            else {
                // Random Planet erzeugen
                if (randomizer.nextFloat() > 0.65F) {
                    createPlanet(pointerX, pointerZ);
                    System.out.print(".");
                }
                // createPlanet(pointerX, pointerZ);
                setSpCoordFalse(arrayX, arrayZ);
            }
        }
        createPlayerPositions();
        //ERZEUGUNG DES RESTLICHEN RASTERS
        //Schleife für Anzahl der Ringe
        for (int i = 0; i < playerCount + (outerRings + 1); i++) {
            pointerZ = +i;
            pointerX = +i;
            arrayZ = (playerCount + outerRings) - i;
            arrayX = (playerCount + outerRings) - i;
            //Schleife für X-Koord
            for (int j = 0; j < (i * 2 + 1); j++) {
                //Schleife für Z-Koord
                for (int k = 0; k < (i * 2 + 1); k++) {
                    if (platz(arrayX, arrayZ) == true) {
                        // Random Planet erzeugen
                        if (randomizer.nextFloat() > 0.65F) {
                            createPlanet(pointerX, pointerZ);
                            System.out.print(".");
                        }
                        // createPlanet(pointerX, pointerZ);
                        setSpCoordFalse(arrayX, arrayZ);
                    }
                    pointerZ--;
                    arrayZ++;
                }
                pointerZ = +i;
                arrayZ = (playerCount + outerRings) - i;
                pointerX--;
                arrayX++;
            }
        }
        if (level.getControl() != null) {
            level.getControl().addShootable(level.getLevelNode());
        }
        // setupPlayers(level.playersByID, r);
        levelLoaded = true;
        System.out.println("Level generated!");
    }

    /**
     * Random take.
     *
     * @return true, if successful
     */
    private boolean randomTake() {
        if (randomizer.nextFloat() < 1.0F
                / (level.getPlayersByID().size() * 2 + 1.0F)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Platz.
     *
     * @param xKoord the x koord
     * @param zKoord the z koord
     * @return true, if successful
     */
    private boolean platz(int xKoord, int zKoord) {
        return spCoord[xKoord][zKoord];
    }

    /**
     * Sets the sp coord false.
     *
     * @param xKoord the x koord
     * @param zKoord the z koord
     */
    private void setSpCoordFalse(int xKoord, int zKoord) {
        spCoord[xKoord][zKoord] = false;
    }

    @Override
    public void dispose() {
        level.getRootNode().detachChild(background);

    }
}
