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
 * File: MappingItemConverter.java
 * Type: com.solarwars.gamestates.gui.MappingItemConverter
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The class MappingItemConverter.
 *
 * @author Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
 */
public class MappingItemConverter
        implements ListBox.ListBoxViewConverter<MappingItem> {

    static final String MAPPING_NAME = "#mapping-name-text";
    static final String MAPPING_KEY = "#mapping-key-text";

    @Override
    public void display(Element listBoxItem, MappingItem item) {
        final Element mappingName =
                listBoxItem.findElementByName(MAPPING_NAME);
        final TextRenderer mappingNameRenderer =
                mappingName.getRenderer(TextRenderer.class);
        final Element mappingKey =
                listBoxItem.findElementByName(MAPPING_KEY);
        final TextRenderer mappingKeyRenderer =
                mappingKey.getRenderer(TextRenderer.class);

        if (item != null) {
            mappingNameRenderer.setText(item.getMappingName());
            mappingKeyRenderer.setText(item.getMappingKey());
        } else {
            mappingNameRenderer.setText("");
            mappingKeyRenderer.setText("");
        }
    }

    @Override
    public int getWidth(Element listBoxItem, MappingItem item) {
        final Element mappingName =
                listBoxItem.findElementByName(MAPPING_NAME);
        final TextRenderer mappingNameRenderer =
                mappingName.getRenderer(TextRenderer.class);
        final Element mappingKey =
                listBoxItem.findElementByName(MAPPING_KEY);
        final TextRenderer mappingKeyRenderer =
                mappingKey.getRenderer(TextRenderer.class);

        int width = ((mappingNameRenderer.getFont() == null)
                ? 0 : mappingNameRenderer.getFont().getWidth(item.getMappingName()))
                + ((mappingKeyRenderer.getFont() == null)
                ? 0 : mappingKeyRenderer.getFont().getWidth(item.getMappingKey()));
        return width;
    }
}
