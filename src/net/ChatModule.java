/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import gui.GameGUI;
import gui.elements.ChatGUI;
import logic.Player;
import net.messages.ChatMessage;
import solarwars.IsoCamera;
import solarwars.SolarWarsApplication;

/**
 *
 * @author Hans
 */
public class ChatModule implements ActionListener {

    private GameGUI gameGUI;
    private ChatGUI chatGUI;
    private InputManager inputManager;
    private NetworkManager networkManager;
    private boolean visible;

    public ChatModule(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void initialize(GameGUI gameGUI, NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.chatGUI = new ChatGUI(gameGUI, this);
        this.gameGUI = gameGUI;
        inputManager.addMapping(SolarWarsApplication.INPUT_MAPPING_CHAT, new KeyTrigger(KeyInput.KEY_LMENU));
        inputManager.addListener(this, SolarWarsApplication.INPUT_MAPPING_CHAT);

        gameGUI.addGUIElement(chatGUI);
        chatGUI.hide();
    }

    public void destroy() {
        gameGUI.removeGUIElement(chatGUI);
        inputManager.removeListener(this);
    }

    public void playerSays(Player p, String message) {
        chatGUI.playerSays(p, message);
    }

    public void localPlayerSendChatMessage(int id, String message) {
        ChatMessage chatMessage = new ChatMessage(id, message);
        networkManager.getThisClient().send(chatMessage);
    }

    public void changeGUI(GameGUI newGUI) {
        if (gameGUI != null) {
            gameGUI.removeGUIElement(chatGUI);
        }
        this.gameGUI = newGUI;
        gameGUI.addGUIElement(chatGUI);
        visible = false;
        chatGUI.hide();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && name.equals(SolarWarsApplication.INPUT_MAPPING_CHAT)) {
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
