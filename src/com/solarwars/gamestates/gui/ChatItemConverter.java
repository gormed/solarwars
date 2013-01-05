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
 * File: ChatItemConverter.java
 * Type: com.solarwars.gamestates.gui.ChatItemConverter
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
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.Color;

/**
 * The class ChatItemConverter.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class ChatItemConverter implements
        ListBoxViewConverter<ChatItem> {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private static final String CHAT_NAME_TEXT = "#chat-name-text";
    private static final String CHAT_COLOR_PANEL = "#chat-color-panel";
    private static final String CHAT_TEXT = "#chat-text";
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    @Override
    public void display(Element listBoxItem, ChatItem item) {
        final Element chatName =
                listBoxItem.findElementByName(CHAT_NAME_TEXT);
        final TextRenderer chatNameRenderer =
                chatName.getRenderer(TextRenderer.class);

        final Element chatText =
                listBoxItem.findElementByName(CHAT_TEXT);
        final TextRenderer chatTextRenderer =
                chatText.getRenderer(TextRenderer.class);

        final Element playerColor =
                listBoxItem.findElementByName(CHAT_COLOR_PANEL);
        final PanelRenderer playerColorRenderer =
                playerColor.getRenderer(PanelRenderer.class);

        if (item != null) {
            if (item.getType() == ChatItem.ChatMsgType.PLAYER) {
                chatNameRenderer.setText(item.getName());
                chatTextRenderer.setText(item.getMessage() + "");
                ColorRGBA color = item.getColor();
                playerColorRenderer.setBackgroundColor(
                        new Color(color.r, color.g, color.b, color.a));
            } else if (item.getType() == ChatItem.ChatMsgType.SERVER) {

                chatNameRenderer.setText("#SERVER");
                chatTextRenderer.setText(item.getMessage());
                ColorRGBA color = ColorRGBA.Orange.clone();
                chatTextRenderer.setColor(
                        new Color(color.r, color.g, color.b, color.a));
                playerColorRenderer.setBackgroundColor(Color.WHITE);
            } else if (item.getType() == ChatItem.ChatMsgType.JOINS) {
                chatNameRenderer.setText("#" + item.getName());
                chatTextRenderer.setText(item.getMessage());
                ColorRGBA color = ColorRGBA.Green.clone();
                chatTextRenderer.setColor(
                        new Color(color.r, color.g, color.b, color.a));
            } else if (item.getType() == ChatItem.ChatMsgType.LEAVER) {
                chatNameRenderer.setText("#" + item.getName());
                chatTextRenderer.setText(item.getMessage());
                ColorRGBA color = ColorRGBA.Yellow.clone();
                chatTextRenderer.setColor(
                        new Color(color.r, color.g, color.b, color.a));
            } else if (item.getType() == ChatItem.ChatMsgType.DEFEAT) {
                chatNameRenderer.setText("#" + item.getName());
                chatTextRenderer.setText(item.getMessage());
                ColorRGBA color = ColorRGBA.Red.clone();
                chatTextRenderer.setColor(
                        new Color(color.r, color.g, color.b, color.a));
            } else if (item.getType() == ChatItem.ChatMsgType.WIN) {
                chatNameRenderer.setText("#" + item.getName());
                chatTextRenderer.setText(item.getMessage());
                ColorRGBA color = ColorRGBA.Black.clone();
                chatTextRenderer.setColor(
                        new Color(color.r, color.g, color.b, color.a));
            }

            chatTextRenderer.setTextHAlign(HorizontalAlign.left);
        } else {
            chatNameRenderer.setText("");
            chatTextRenderer.setText("");
            playerColorRenderer.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public int getWidth(Element listBoxItem, ChatItem item) {
        final Element chatName =
                listBoxItem.findElementByName(CHAT_NAME_TEXT);
        final TextRenderer chatNameRenderer =
                chatName.getRenderer(TextRenderer.class);

        final Element chatText =
                listBoxItem.findElementByName(CHAT_TEXT);
        final TextRenderer chatTextRenderer =
                chatText.getRenderer(TextRenderer.class);

        final Element playerColor =
                listBoxItem.findElementByName(CHAT_COLOR_PANEL);

        int width = ((chatNameRenderer.getFont() == null)
                ? 0 : chatNameRenderer.getFont().getWidth(item.getName()))
                + ((playerColor == null)
                ? 0 : playerColor.getWidth())
                + ((chatTextRenderer.getFont() == null)
                ? 0 : chatTextRenderer.getFont().getWidth(item.getMessage() + ""));
        return width;
    }
}
