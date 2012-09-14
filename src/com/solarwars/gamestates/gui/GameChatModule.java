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
import com.jme3.math.ColorRGBA;
import com.solarwars.SolarWarsApplication;
import com.solarwars.input.InputMappings;
import com.solarwars.logic.Player;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.messages.ChatMessage;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class GameChatModule.
 *
 * @author Hans
 */
public class GameChatModule implements ActionListener {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Element chatLayer;
    private ListBox<ChatItem> listBoxChat;
    private Screen screen;
    /** The input manager. */
    private InputManager inputManager;
    /** The network manager. */
    private NetworkManager networkManager;
    private Nifty niftyGUI;
    /** The visible. */
    private boolean visible;
    private Element textInput;
    private boolean hadWinner = false;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Instantiates a new chat module.
     *
     * @param inputManager the input manager
     */
    public GameChatModule(Nifty niftyGUI, NetworkManager networkManager) {
        this.niftyGUI = niftyGUI;
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
        chatLayer = niftyGUI.getCurrentScreen().findElementByName("chat");
        chatLayer.hide();
        listBoxChat = niftyGUI.getCurrentScreen().findNiftyControl("chat_text_box", ListBox.class);
        textInput = niftyGUI.getCurrentScreen().findElementByName("chat_text_field");
        textInput.disableFocus();
        networkManager.setCurrentChatModule(this);
    }

    /**
     * Destroy.
     */
    public void destroy() {
        inputManager.removeListener(this);
    }

    /**
     * Player says.
     *
     * @param p the p
     * @param message the message
     */
    public void playerSays(Player p, String message) {
        listBoxChat.addItem(new ChatItem(message,
                ChatItem.ChatMsgType.PLAYER,
                p.getName(), p.getColor()));
    }

    /**
     * Player leaves.
     *
     * @param p the p
     */
    public void playerLeaves(Player p) {
        if (p.isLeaver()) {
            listBoxChat.addItem(new ChatItem(p.getName() + " leaves the game!",
                    ChatItem.ChatMsgType.LEAVER,
                    "#SERVER", p.getColor()));
            chatLayer.show();
        }
    }

    /**
     * Player defeats.
     *
     * @param victorious the victorious
     * @param defeated the defeated
     */
    public void playerDefeats(Player victorious, Player defeated) {
        listBoxChat.addItem(new ChatItem(victorious.getName()
                + " defeats "
                + defeated.getName() + "!",
                ChatItem.ChatMsgType.DEFEAT,
                "SERVER", ColorRGBA.White));
        chatLayer.show();
    }

    public void playerJoins(Player thisPlayer) {
        listBoxChat.addItem(new ChatItem(thisPlayer.getName() + " joins the game!",
                ChatItem.ChatMsgType.JOINS,
                "SERVER", thisPlayer.getColor()));
        chatLayer.show();

    }

    public void playerWins(Player winner) {
        if (!hadWinner) {
            listBoxChat.addItem(new ChatItem(winner.getName() + " wins the game!",
                    ChatItem.ChatMsgType.WIN,
                    "SERVER", winner.getColor()));
            chatLayer.show();
            hadWinner = true;
        }
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


    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && name.equals(InputMappings.PLAYER_CHAT)) {
            if (chatLayer.isVisible()) {
                chatLayer.hide();
                textInput.disableFocus();
            } else {
                chatLayer.show();
                textInput.setFocus();
            }
        }
    }
}
