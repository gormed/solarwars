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
 * File: ConnectedPlayerItemConverter.java
 * Type: com.solarwars.gamestates.gui.ConnectedPlayerItemConverter
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.math.ColorRGBA;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.Color;

/**
 * The class ConnectedPlayerItemConverter.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class ConnectedPlayerItemConverter
        implements ListBoxViewConverter<ConnectedPlayerItem> {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private static final String PLAYER_NAME_TEXT = "#player-name-text";
    private static final String PLAYER_COLOR_PANEL = "#player-color-panel";
    private static final String PLAYER_READY_CHECK = "#player-ready-text";
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    @Override
    public void display(Element listBoxItem, ConnectedPlayerItem item) {
        final Element serverName =
                listBoxItem.findElementByName(PLAYER_NAME_TEXT);
        final TextRenderer serverNameRenderer =
                serverName.getRenderer(TextRenderer.class);
        final Element playerColor =
                listBoxItem.findElementByName(PLAYER_COLOR_PANEL);
        final PanelRenderer playerColorRenderer =
                playerColor.getRenderer(PanelRenderer.class);
        final Element playerReady =
                listBoxItem.findElementByName(PLAYER_READY_CHECK);
        final TextRenderer playerReadyRenderer =
                playerReady.getRenderer(TextRenderer.class);
        if (item != null) {
            serverNameRenderer.setText(item.getName());
            ColorRGBA color = item.getColor();
            playerColorRenderer.setBackgroundColor(
                    new Color(color.r, color.g, color.b, color.a));
            playerReadyRenderer.setText((item.isReady()) ? "ready" : "not ready");
        } else {
            serverNameRenderer.setText("");
            playerColorRenderer.setBackgroundColor(Color.BLACK);
            playerReadyRenderer.setText("not ready");
        }
    }

    @Override
    public int getWidth(Element listBoxItem, ConnectedPlayerItem item) {
        final Element serverName =
                listBoxItem.findElementByName(PLAYER_NAME_TEXT);
        final TextRenderer serverNameRenderer =
                serverName.getRenderer(TextRenderer.class);
        final Element playerColor =
                listBoxItem.findElementByName(PLAYER_COLOR_PANEL);
        final CheckBox playerReady =
                listBoxItem.findNiftyControl(PLAYER_READY_CHECK, CheckBox.class);

        int width = ((serverNameRenderer.getFont() == null)
                ? 0 : serverNameRenderer.getFont().getWidth(item.getName()))
                + ((playerColor == null)
                ? 0 : playerColor.getWidth())
                + ((playerReady == null)
                ? 0 : playerReady.getWidth());
        return width;
    }
}
