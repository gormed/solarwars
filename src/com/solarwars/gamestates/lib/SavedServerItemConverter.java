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
 * File: SavedServerItemConverter.java
 * Type: com.solarwars.gamestates.lib.SavedServerItemConverter
 * 
 * Documentation created: 07.09.2012 - 01:12:35 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.lib;

import com.solarwars.gamestates.lib.MultiplayerState.SavedServerItem;
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
        
        return ((serverNameRenderer.getFont() == null) ? 
                0 : serverNameRenderer.getFont().getWidth(item.getName()))
                + ((serverIPRenderer.getFont() == null) ? 
                0 : serverIPRenderer.getFont().getWidth(item.getIp()));
    }
}
