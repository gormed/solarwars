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
 * File: ControllerItemConverter.java
 * Type: com.solarwars.gamestates.gui.ControllerItemConverter
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The class ControllerItemConverter.
 *
 * @author Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
 */
public class ControllerItemConverter
        implements ListBox.ListBoxViewConverter<ControllerItem> {

    static final String CONTROLLER_NAME = "#controller-name-text";
    static final String CONTROLLER_ID = "#controller-id-text";

    @Override
    public void display(Element listBoxItem, ControllerItem item) {
        final Element controllerName =
                listBoxItem.findElementByName(CONTROLLER_NAME);
        final TextRenderer controllerNameRenderer =
                controllerName.getRenderer(TextRenderer.class);
        final Element controllerID =
                listBoxItem.findElementByName(CONTROLLER_ID);
        final TextRenderer controllerIDRenderer =
                controllerID.getRenderer(TextRenderer.class);

        if (item != null) {
            controllerNameRenderer.setText(item.getName());
            controllerIDRenderer.setText(item.getId()+"");
        } else {
            controllerNameRenderer.setText("");
            controllerIDRenderer.setText("");
        }
    }

    @Override
    public int getWidth(Element listBoxItem, ControllerItem item) {
        final Element controllerName =
                listBoxItem.findElementByName(CONTROLLER_NAME);
        final TextRenderer controllerNameRenderer =
                controllerName.getRenderer(TextRenderer.class);
        final Element controllerID =
                listBoxItem.findElementByName(CONTROLLER_ID);
        final TextRenderer controllerIDRenderer =
                controllerID.getRenderer(TextRenderer.class);
        int width = ((controllerNameRenderer.getFont() == null) ? 
                0 : controllerNameRenderer.getFont().getWidth(item.getName()))
                + ((controllerIDRenderer.getFont() == null) ? 
                0 : controllerIDRenderer.getFont().getWidth(item.getId()+""));
        return width;
    }
}
