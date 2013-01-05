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
 * File: GameChatModule.java
 * Type: com.solarwars.gamestates.gui.GameChatModule
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.solarwars.SolarWarsApplication;
import com.solarwars.controls.input.InputMappings;
import com.solarwars.logic.Player;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.messages.ChatMessage;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class GameChatModule.
 *
 * @author Hans
 */
public class GameChatModule {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Element chatLayer;
    private ListBox<ChatItem> listBoxChat;
    private Scrollbar scrollbar;
    private Screen screen;
    /**
     * The input manager.
     */
    private InputManager inputManager;
    /**
     * The network manager.
     */
    private NetworkManager networkManager;
    private Nifty niftyGUI;
    /**
     * The visible.
     */
    private boolean visible;
    private Element textInput;
    private boolean hadWinner = false;
    private ActionListener keyboardAction;

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
    @SuppressWarnings("unchecked")
    private void initialize(NetworkManager networkManager) {

        this.networkManager = networkManager;
        this.inputManager = SolarWarsApplication.getInstance().getInputManager();

        scrollbar = niftyGUI.getCurrentScreen().
                findNiftyControl("scrollbar", Scrollbar.class);
        chatLayer = niftyGUI.getCurrentScreen().findElementByName("chat");
        chatLayer.hide();
        listBoxChat = niftyGUI.getCurrentScreen().findNiftyControl("chat_text_box", ListBox.class);
        listBoxChat.clear();
        textInput = niftyGUI.getCurrentScreen().findElementByName("chat_text_field");
        textInput.disableFocus();
        this.keyboardAction = new ActionListener() {
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
        };
        if (inputManager != null) {
            inputManager.addMapping(InputMappings.PLAYER_CHAT, new KeyTrigger(KeyInput.KEY_LMENU));
            inputManager.addListener(keyboardAction, InputMappings.PLAYER_CHAT);
        }
        networkManager.setCurrentChatModule(this);
    }

    /**
     * Destroy.
     */
    public void destroy() {
        inputManager.removeListener(keyboardAction);
        keyboardAction = null;
    }

    private void autoScroll() {
        listBoxChat.selectItemByIndex(listBoxChat.itemCount() - 1);
    }

    /**
     * Prints to the text box and splits message if nessecary.
     *
     * @param p the sending player
     * @param color the color of the message
     * @param message the message content
     * @param type the messages type
     * @param server the flag indication if server is saying or player
     */
    private void printToTextbox(Player p, ColorRGBA color, String message, ChatItem.ChatMsgType type, boolean server) {
        final int maxLength = 30;
        int restLength = message.length();
        int firstLength = 0;
        while (restLength > maxLength) {
            String messagePartOne = message.substring(firstLength, maxLength + firstLength);
            firstLength += maxLength;
            restLength -= maxLength;
            listBoxChat.addItem(new ChatItem(messagePartOne,
                    type,
                    (server ? "SERVER" : p.getName()), color));

//            String messagePartTwo = message.substring(maxLength, message.length() - 1);
//            listBoxChat.addItem(new ChatItem(messagePartTwo,
//                    type,
//                    (server ? "SERVER" : p.getName()), color));
        }
        String messagePartTwo = message.substring(firstLength, message.length());
        listBoxChat.addItem(new ChatItem(messagePartTwo, type,
                (server ? "SERVER" : p.getName()), color));
    }

    /**
     * Player says.
     *
     * @param p the p
     * @param message the message
     */
    public void playerSays(Player p, String message) {
        printToTextbox(p, p.getColor(), message,
                ChatItem.ChatMsgType.PLAYER, false);
//        listBoxChat.addItem(new ChatItem(message,
//                ChatItem.ChatMsgType.PLAYER,
//                p.getName(), p.getColor()));
        if (p.isHost()) {
            if (!chatLayer.isVisible()) {
                chatLayer.show();
            }
        }
        autoScroll();
    }

    /**
     * Player leaves.
     *
     * @param p the p
     */
    public void playerLeaves(Player p) {
        if (p.isLeaver()) {
            printToTextbox(p, p.getColor(), p.getName() + " leaves the game!",
                    ChatItem.ChatMsgType.LEAVER, true);
            if (!chatLayer.isVisible()) {
                chatLayer.show();
            }
            autoScroll();
        }
    }

    /**
     * Player defeats.
     *
     * @param victorious the victorious
     * @param defeated the defeated
     */
    public void playerDefeats(Player victorious, Player defeated) {
        printToTextbox(victorious, ColorRGBA.White.clone(), victorious.getName()
                + " defeats "
                + defeated.getName() + "!", ChatItem.ChatMsgType.DEFEAT, true);
//        listBoxChat.addItem(new ChatItem(victorious.getName()
//                + " defeats "
//                + defeated.getName() + "!",
//                ChatItem.ChatMsgType.DEFEAT,
//                "SERVER", ColorRGBA.White.clone()));
        if (!chatLayer.isVisible()) {
            chatLayer.show();
        }
        autoScroll();
    }

    public void playerJoins(Player thisPlayer) {
        printToTextbox(thisPlayer, thisPlayer.getColor(),
                thisPlayer.getName() + " joins the game!",
                ChatItem.ChatMsgType.JOINS, true);
//        listBoxChat.addItem(new ChatItem(thisPlayer.getName() + " joins the game!",
//                ChatItem.ChatMsgType.JOINS,
//                "SERVER", thisPlayer.getColor()));
        if (!chatLayer.isVisible()) {
            chatLayer.show();
        }
        autoScroll();

    }

    public void playerWins(Player winner) {
        if (!hadWinner) {
            printToTextbox(winner, winner.getColor(), winner.getName() + " wins the game!",
                    ChatItem.ChatMsgType.WIN, true);
//            listBoxChat.addItem(new ChatItem(winner.getName() + " wins the game!",
//                    ChatItem.ChatMsgType.WIN,
//                    "SERVER", winner.getColor()));
            if (!chatLayer.isVisible()) {
                chatLayer.show();
            }
            autoScroll();
            hadWinner = true;
        }
    }

    public void serverSays(String string) {
        printToTextbox(null, ColorRGBA.White.clone(), string,
                ChatItem.ChatMsgType.SERVER, true);
//        listBoxChat.addItem(new ChatItem(string,
//                ChatItem.ChatMsgType.SERVER,
//                "SERVER", ColorRGBA.White.clone()));
        if (!chatLayer.isVisible()) {
            chatLayer.show();
        }
        autoScroll();
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
}
