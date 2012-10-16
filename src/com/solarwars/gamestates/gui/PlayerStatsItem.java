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
 * File: PlayerStatsItem.java
 * Type: com.solarwars.gamestates.gui.PlayerStatsItem
 * 
 * Documentation created: 08.09.2012 - 19:52:31 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.math.ColorRGBA;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.AbstractGameplay;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import java.util.Map.Entry;

/**
 * The class PlayerStatsItem.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class PlayerStatsItem {
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private String name;
    private int ships;
    private int planets;
    private int shipsPerSec = 1;
    private ColorRGBA color;
    private int power = 1;
    private Player player;
    // CALC
    private float avgSize;
    private float globalSize;
    private float avgGrowth;
    private float growthPerSecond;
    private Level level = SolarWarsGame.getCurrentGameplay().getCurrentLevel();

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public PlayerStatsItem(
            Player player) {
        this.player = player;
        this.name = player.getName();
        this.ships = player.getShipCount();
        this.planets = player.getPlanetCount();
        this.color = player.getColor();
    }

    public void update(float tpf) {
        float percent = 0;
        float growth = 0;
        if (level != null) {
            calculateGrowthAndGain(level);
//                percent = 
//                        (float) player.getPlanetCount()
//                        / (float) l.getPlanetSet().size();
//                percent *= 100;
            percent = getPercentageOfGain();
            percent *= 100;
            growth = getAvgGrowthPerSecond();

        }
        power = (int) percent;
        shipsPerSec = (int) growth;

        ships = player.getShipCount();
        planets = player.getPlanetCount();
//        String iconPath = getIconPath();
    }

    private String getIconPath() {
        String iconPath = "Interface/ScoreIcons/state_pause.png";
        if (player.hasLost()) {
            return iconPath = "Interface/ScoreIcons/state_stop.png";
        }
        if (!player.hasLost() && !SolarWarsApplication.getInstance().isPaused()) {
            return iconPath = "Interface/ScoreIcons/state_plays.png";
        }
        return iconPath;
    }

    private void calculateGrowthAndGain(Level level) {
        float size = 0;
        float growth = 0;
        avgSize = 0;
        avgGrowth = 0;
        globalSize = 0;
        growthPerSecond = 0;

        for (AbstractPlanet p : player.getPlanets()) {
            size = p.getSizeID();
            growth += AbstractGameplay.PLANET_INCREMENT_TIME[p.getSizeID()];
            avgSize += size;

        }
        avgGrowth = growth / (float) player.getPlanets().size();
        float times = 1.0f / avgGrowth;
        growthPerSecond = times * (float) player.getPlanets().size();
        for (Entry<Integer, AbstractPlanet> e : level.getPlanetSet()) {
            size = e.getValue().getSizeID();
            globalSize += size;

        }

//            avgSize /= (float) player.getPlanetCount();
//            globalSize /= (float) DeathmatchGameplay.getCurrentLevel().getPlanetSet().size();
    }

    private float getPercentageOfGain() {
        return avgSize / globalSize;
    }

    private float getAvgGrowthPerSecond() {
        return growthPerSecond;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanets(int planets) {
        this.planets = planets;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setShips(int ships) {
        this.ships = ships;
    }

    public void setShipsPerSec(int shipsPerSec) {
        this.shipsPerSec = shipsPerSec;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getPlanets() {
        return planets;
    }

    public int getPower() {
        return power;
    }

    public int getShips() {
        return ships;
    }

    public int getShipsPerSec() {
        return shipsPerSec;
    }
}
