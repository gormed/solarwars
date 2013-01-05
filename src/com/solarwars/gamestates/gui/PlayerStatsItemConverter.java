/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: PlayerStatsItemConverter.java
 * Type: com.solarwars.gamestates.gui.PlayerStatsItemConverter
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.math.ColorRGBA;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.Color;

/**
 * The class PlayerStatsItemConverter.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class PlayerStatsItemConverter
        implements ListBoxViewConverter<PlayerStatsItem> {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private static final String STATS_PLAYER_NAME_TEXT = "#stats-player-name-text";
    private static final String STATS_PLAYER_SHIPS_TEXT = "#stats-player-ships-text";
    private static final String STATS_PLAYER_PLANET_TEXT = "#stats-player-planet-text";
    private static final String STATS_PLAYER_SHIPSPERSEC_TEXT = "#stats-player-shipspersec-text";
    private static final String STATS_PLAYER_COLOR_PANEL = "#stats-player-color-panel";
    private static final String STATS_PLAYER_POWER_TEXT = "#stats-player-power-text";
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    @Override
    public void display(Element listBoxItem, PlayerStatsItem item) {
        final Element playerName =
                listBoxItem.findElementByName(STATS_PLAYER_NAME_TEXT);
        final TextRenderer playerNameRenderer =
                playerName.getRenderer(TextRenderer.class);

        final Element playerShips =
                listBoxItem.findElementByName(STATS_PLAYER_SHIPS_TEXT);
        final TextRenderer playerShipsRenderer =
                playerShips.getRenderer(TextRenderer.class);

        final Element playerPlanets =
                listBoxItem.findElementByName(STATS_PLAYER_PLANET_TEXT);
        final TextRenderer playerPlanetsRenderer =
                playerPlanets.getRenderer(TextRenderer.class);

        final Element shipsPerSec =
                listBoxItem.findElementByName(STATS_PLAYER_SHIPSPERSEC_TEXT);
        final TextRenderer shipsPerSecRenderer =
                shipsPerSec.getRenderer(TextRenderer.class);

        final Element playerPower =
                listBoxItem.findElementByName(STATS_PLAYER_POWER_TEXT);
        final TextRenderer playerPowerRenderer =
                playerPower.getRenderer(TextRenderer.class);

        final Element playerColor =
                listBoxItem.findElementByName(STATS_PLAYER_COLOR_PANEL);
        final PanelRenderer playerColorRenderer =
                playerColor.getRenderer(PanelRenderer.class);

        if (item != null) {
            playerNameRenderer.setText(item.getName());
            playerShipsRenderer.setText(item.getShips() + "");
            playerPlanetsRenderer.setText(item.getPlanets() + "");
            shipsPerSecRenderer.setText(item.getShipsPerSec() + "");
            playerPowerRenderer.setText(item.getPower() + "%");
            ColorRGBA color = item.getColor();
            playerColorRenderer.setBackgroundColor(
                    new Color(color.r, color.g, color.b, color.a));
        } else {
            playerNameRenderer.setText("");
            playerShipsRenderer.setText("");
            playerPlanetsRenderer.setText("");
            shipsPerSecRenderer.setText("");
            playerPowerRenderer.setText("");
            playerColorRenderer.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public int getWidth(Element listBoxItem, PlayerStatsItem item) {
        final Element playerName =
                listBoxItem.findElementByName(STATS_PLAYER_NAME_TEXT);
        final TextRenderer playerNameRenderer =
                playerName.getRenderer(TextRenderer.class);

        final Element playerShips =
                listBoxItem.findElementByName(STATS_PLAYER_SHIPS_TEXT);
        final TextRenderer playerShipsRenderer =
                playerShips.getRenderer(TextRenderer.class);

        final Element playerPlanets =
                listBoxItem.findElementByName(STATS_PLAYER_PLANET_TEXT);
        final TextRenderer playerPlanetsRenderer =
                playerPlanets.getRenderer(TextRenderer.class);

        final Element shipsPerSec =
                listBoxItem.findElementByName(STATS_PLAYER_SHIPSPERSEC_TEXT);
        final TextRenderer shipsPerSecRenderer =
                shipsPerSec.getRenderer(TextRenderer.class);

        final Element playerPower =
                listBoxItem.findElementByName(STATS_PLAYER_POWER_TEXT);
        final TextRenderer playerPowerRenderer =
                playerPower.getRenderer(TextRenderer.class);

        final Element playerColor =
                listBoxItem.findElementByName(STATS_PLAYER_COLOR_PANEL);
        final PanelRenderer playerColorRenderer =
                playerColor.getRenderer(PanelRenderer.class);

        int width = ((playerNameRenderer.getFont() == null)
                ? 0 : playerNameRenderer.getFont().getWidth(item.getName()))
                + ((playerColor == null)
                ? 0 : playerColor.getWidth())
                + ((playerShipsRenderer.getFont() == null)
                ? 0 : playerShipsRenderer.getFont().getWidth(item.getShips()+""))
                + ((playerPlanetsRenderer.getFont() == null)
                ? 0 : playerPlanetsRenderer.getFont().getWidth(item.getPlanets()+""))
                + ((shipsPerSecRenderer.getFont() == null)
                ? 0 : shipsPerSecRenderer.getFont().getWidth(item.getShipsPerSec()+""))
                + ((playerPowerRenderer.getFont() == null)
                ? 0 : playerPowerRenderer.getFont().getWidth(item.getPower()+"%"))
                ;

        return width;
    }
}
