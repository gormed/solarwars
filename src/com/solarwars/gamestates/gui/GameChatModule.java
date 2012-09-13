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
 * File: GameChatModule.java
 * Type: com.solarwars.net.GameChatModule
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.solarwars.SolarWarsApplication;
import com.solarwars.input.InputMappings;
import com.solarwars.logic.Player;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.messages.ChatMessage;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

/**
 * The Class GameChatModule.
 *
 * @author Hans
 */
public class GameChatModule implements ActionListener, Controller {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private ScrollPanel scrollPanel;
    private Element textArea;
    private Screen screen;
    /** The input manager. */
    private InputManager inputManager;
    /** The network manager. */
    private NetworkManager networkManager;
    /** The visible. */
    private boolean visible;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Instantiates a new chat module.
     *
     * @param inputManager the input manager
     */
    public GameChatModule(NetworkManager networkManager) {
        initialize(networkManager);
    }

    /**
     * Initializes the.
     *
     * @param gameGUI the game gui
     * @param networkManager the network manager
     */
    private void initialize(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.addMapping(InputMappings.PLAYER_CHAT, new KeyTrigger(KeyInput.KEY_LMENU));
            inputManager.addListener(this, InputMappings.PLAYER_CHAT);
        }
    }

    /**
     * Destroy.
     */
    public void destroy() {
//        gameGUI.removeGUIElement(chatGUI);
        inputManager.removeListener(this);
    }

    /**
     * Player says.
     *
     * @param p the p
     * @param message the message
     */
    public void playerSays(Player p, String message) {
//        chatGUI.playerSays(p, message);
//        if (!chatGUI.isFadeDirection()) {
//            chatGUI.peek();
//        }
    }

    /**
     * Player leaves.
     *
     * @param p the p
     */
    public void playerLeaves(Player p) {
        if (p.isLeaver()) {
            return;
        }
//        chatGUI.serverSays(
//                p.getName() + " leaves the game...");
//        if (!chatGUI.isFadeDirection()) {
//            chatGUI.peek();
//        }
    }

    /**
     * Player defeats.
     *
     * @param victorious the victorious
     * @param defeated the defeated
     */
    public void playerDefeats(Player victorious, Player defeated) {
//        chatGUI.serverSays(
//                victorious.getName()
//                + " defeats "
//                + defeated.getName() + "!");
//        if (!chatGUI.isFadeDirection()) {
//            chatGUI.peek();
//        }
    }

    /**
     * Local player send chat message.
     *
     * @param id the id
     * @param message the message
     */
    public void localPlayerSendChatMessage(int id, String message) {
        ChatMessage chatMessage = new ChatMessage(id, message);
        networkManager.getThisClient().send(chatMessage);
    }

//    /**
//     * Change gui.
//     *
//     * @param newGUI the new gui
//     */
//    public void changeGUI(GameGUI newGUI) {
//        if (gameGUI != null) {
//            gameGUI.removeGUIElement(chatGUI);
//        }
//        this.gameGUI = newGUI;
//        gameGUI.addGUIElement(chatGUI);
//        visible = false;
//        chatGUI.hide();
//    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && name.equals(InputMappings.PLAYER_CHAT)) {
//            visible = !visible;
//            if (visible) {
//                chatGUI.show();
//                IsoCamera.getInstance().setEnabled(false);
//            } else {
//                chatGUI.hide();
//                IsoCamera.getInstance().setEnabled(true);
//            }
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen, Element element,
            Properties parameter, Attributes controlDefinitionAttributes) {
        this.screen = screen;
        scrollPanel = element.findNiftyControl("scroll_panel", ScrollPanel.class);
        textArea = element.findElementByName("text_area");
    }

    @Override
    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onFocus(boolean getFocus) {
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }

    public void setAutoScroll(AutoScroll scroll) {
        scrollPanel.setAutoScroll(scroll);
    }

    public AutoScroll getAutoScroll() {
        return scrollPanel.getAutoScroll();
    }

    public void append(String text) {
        setText(getText() + text);
    }

    public void setText(String text) {
        textArea.getRenderer(TextRenderer.class).setText(text);
        screen.layoutLayers();
        textArea.setHeight(textArea.getRenderer(TextRenderer.class).getTextHeight());
    }

    public String getText() {
        return textArea.getRenderer(TextRenderer.class).getOriginalText();
    }
}
