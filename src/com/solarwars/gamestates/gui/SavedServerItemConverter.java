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
 * File: SavedServerItemConverter.java
 * Type: com.solarwars.gamestates.gui.SavedServerItemConverter
 * 
 * Documentation created: 05.01.2013 - 22:12:56 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The class SavedServerItemConverter.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class SavedServerItemConverter 
    implements ListBoxViewConverter<SavedServerItem> {

    private static final String SERVER_NAME_TEXT = "#server-name-text";
    private static final String SERVER_IP_TEXT = "#server-ip-text";

    @Override
    public void display(Element listBoxItem, SavedServerItem item) {
        final Element serverName =
                listBoxItem.findElementByName(SERVER_NAME_TEXT);
        final TextRenderer serverNameRenderer =
                serverName.getRenderer(TextRenderer.class);
        final Element serverIP =
                listBoxItem.findElementByName(SERVER_IP_TEXT);
        final TextRenderer serverIPRenderer =
                serverIP.getRenderer(TextRenderer.class);

        if (item != null) {
            serverNameRenderer.setText(item.getName());
            serverIPRenderer.setText(item.getIp());
        } else {
            serverNameRenderer.setText("");
            serverIPRenderer.setText("");
        }
    }

    @Override
    public int getWidth(Element listBoxItem, SavedServerItem item) {
        final Element serverName =
                listBoxItem.findElementByName(SERVER_NAME_TEXT);
        final TextRenderer serverNameRenderer =
                serverName.getRenderer(TextRenderer.class);
        final Element serverIP =
                listBoxItem.findElementByName(SERVER_IP_TEXT);
        final TextRenderer serverIPRenderer =
                serverIP.getRenderer(TextRenderer.class);
        int width = ((serverNameRenderer.getFont() == null) ? 
                0 : serverNameRenderer.getFont().getWidth(item.getName()))
                + ((serverIPRenderer.getFont() == null) ? 
                0 : serverIPRenderer.getFont().getWidth(item.getIp()));
        return width;
    }
}
