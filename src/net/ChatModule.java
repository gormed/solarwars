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
 * File: ChatModule.java
 * Type: net.ChatModule
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import gui.GameGUI;
import gui.elements.ChatGUI;
import logic.Player;
import net.messages.ChatMessage;
import solarwars.InputMappings;
import solarwars.IsoCamera;

/**
 * The Class ChatModule.
 *
 * @author Hans
 */
public class ChatModule implements ActionListener {

    /** The game gui. */
    private GameGUI gameGUI;
    
    /** The chat gui. */
    private ChatGUI chatGUI;
    
    /** The input manager. */
    private InputManager inputManager;
    
    /** The network manager. */
    private NetworkManager networkManager;
    
    /** The visible. */
    private boolean visible;

    /**
     * Instantiates a new chat module.
     *
     * @param inputManager the input manager
     */
    public ChatModule(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Initializes the.
     *
     * @param gameGUI the game gui
     * @param networkManager the network manager
     */
    public void initialize(GameGUI gameGUI, NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.chatGUI = new ChatGUI(gameGUI, this);
        this.gameGUI = gameGUI;
        inputManager.addMapping(InputMappings.KEYBOARD_CHAT, new KeyTrigger(KeyInput.KEY_LMENU));
        inputManager.addListener(this, InputMappings.KEYBOARD_CHAT);
        chatGUI.setVisible(false);
        gameGUI.addGUIElement(chatGUI);
        chatGUI.hide();
    }

    /**
     * Destroy.
     */
    public void destroy() {
        gameGUI.removeGUIElement(chatGUI);
        inputManager.removeListener(this);
    }

    /**
     * Player says.
     *
     * @param p the p
     * @param message the message
     */
    public void playerSays(Player p, String message) {
        chatGUI.playerSays(p, message);
        if (!chatGUI.isFadeDirection()) {
            chatGUI.peek();
        }
    }

    /**
     * Player leaves.
     *
     * @param p the p
     */
    public void playerLeaves(Player p) {
        if (p.isLeaver())
            return;
        chatGUI.serverSays(
                p.getName() + " leaves the game...");
        if (!chatGUI.isFadeDirection()) {
            chatGUI.peek();
        }
    }

    /**
     * Player defeats.
     *
     * @param victorious the victorious
     * @param defeated the defeated
     */
    public void playerDefeats(Player victorious, Player defeated) {
        chatGUI.serverSays(
                victorious.getName()
                + " defeats "
                + defeated.getName() + "!");
        if (!chatGUI.isFadeDirection()) {
            chatGUI.peek();
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

    /**
     * Change gui.
     *
     * @param newGUI the new gui
     */
    public void changeGUI(GameGUI newGUI) {
        if (gameGUI != null) {
            gameGUI.removeGUIElement(chatGUI);
        }
        this.gameGUI = newGUI;
        gameGUI.addGUIElement(chatGUI);
        visible = false;
        chatGUI.hide();
    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && name.equals(InputMappings.KEYBOARD_CHAT)) {
            visible = !visible;
            if (visible) {
                chatGUI.show();
                IsoCamera.getInstance().setEnabled(false);
            } else {
                chatGUI.hide();
                IsoCamera.getInstance().setEnabled(true);
            }
        }
    }
}
