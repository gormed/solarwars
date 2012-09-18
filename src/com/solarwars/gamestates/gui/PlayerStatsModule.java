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
 * File: PlayerStatsModule.java
 * Type: com.solarwars.gamestates.gui.PlayerStatsModule
 * 
 * Documentation created: 18.09.2012 - 22:59:14 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.solarwars.logic.Player;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The class PlayerStatsModule.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class PlayerStatsModule {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Nifty niftyGUI;
    private float refreshCounter = 0;
    private Element statsLayer;
    private final Element percentageLabel;
    private final Element shipsLabel;
    private final Element planetsLabel;
    private final Element gainLabel;
    private final Element powerLabel;
    private Player player;
    private PlayerStatsItem statsItem;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public PlayerStatsModule(Nifty niftyGUI, Player player, GameStatsModule gameStatsModule) {
        this.niftyGUI = niftyGUI;
        this.player = player;
        
        for (PlayerStatsItem item : gameStatsModule.getPlayerStatsBox().getItems()) {
            if (item.getPlayer().equals(player)) {
                statsItem = item;
                break;
            }
        }
        
        this.statsLayer = niftyGUI.getCurrentScreen().
                findElementByName("player_stats_hud");
//        statsLayer.fi 
        percentageLabel = niftyGUI.getCurrentScreen().
                findElementByName("percentage");
        shipsLabel = statsLayer.findElementByName("ships_text");

        planetsLabel = statsLayer.findElementByName("planet_text");

        gainLabel = statsLayer.findElementByName("gain_text");

        powerLabel = statsLayer.findElementByName("power_text");
    }

    public void update(float tpf) {
        // swap old with new text
        percentageLabel.getRenderer(TextRenderer.class).
                setText(refreshPercentage() + "%");
        shipsLabel.getRenderer(TextRenderer.class).
                setText(statsItem.getShips()+"");
        planetsLabel.getRenderer(TextRenderer.class).
                setText(statsItem.getPlanets()+"");
        gainLabel.getRenderer(TextRenderer.class).
                setText(statsItem.getShipsPerSec()+"");
        powerLabel.getRenderer(TextRenderer.class).
                setText(statsItem.getPower()+"%");
    }

    public int refreshPercentage() {
        return (int) (player.getShipPercentage() * 100);
    }
}
