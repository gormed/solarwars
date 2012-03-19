/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import gui.elements.TextBox;
import logic.Player;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class Multiplayer extends Gamestate {

    private Label multiplayerLabel;
    private Panel backgroundPanel;
    private Panel line;
    private TextBox playerName;
    private Button joinServer;
    private Button createServer;
    private Button back;
    private TextBox serverip;
    private GameGUI gui;
    private Hub hub;

    public Multiplayer() {
        super(GamestateManager.MULTIPLAYER_STATE);

    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        playerName = new TextBox(
                ColorRGBA.Blue,
                new Vector3f(gui.getWidth() / 2, 7 * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                "NAME",
                ColorRGBA.White,
                gui, false) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
            }
        };

        joinServer = new Button("Join Sever",
                new Vector3f(gui.getWidth() / 4f, 5.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed)
                    joinServer();
            }
        };

        serverip = new TextBox(
                ColorRGBA.Blue,
                new Vector3f(gui.getWidth() / 4f, 4.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, "127.0.0.1",
                ColorRGBA.White, gui, true) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
            }
        };

        back = new Button("Back",
                new Vector3f(gui.getWidth() / 2, 1.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
            }
        };

        createServer = new Button("Create Sever",
                new Vector3f(3 * gui.getWidth() / 4f, 5.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed)
                    createServer();
            }
        };

        multiplayerLabel = new Label(
                "MULTIPLAYER",
                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
                new Vector3f(2, 2, 1),
                ColorRGBA.White, gui) {

            private float time;

            @Override
            public void updateGUI(float tpf) {
                time += tpf;

                if (time < 0.2f) {
                    text.setText(title + "_");
                } else if (time < 0.4f) {
                    text.setText(title);
                } else {
                    time = 0;
                }
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        backgroundPanel = new Panel(
                "BackgroundPanel",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
                ColorRGBA.Blue);

        line = new Panel("Line", new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
                ColorRGBA.White);

        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(multiplayerLabel);
        gui.addGUIElement(playerName);
        gui.addGUIElement(joinServer);
        gui.addGUIElement(createServer);
        gui.addGUIElement(serverip);
        gui.addGUIElement(back);
    }

    @Override
    protected void unloadContent() {

        gui.removeGUIElement(backgroundPanel);
        gui.removeGUIElement(line);
        gui.removeGUIElement(multiplayerLabel);
        gui.removeGUIElement(playerName);
        gui.removeGUIElement(joinServer);
        gui.removeGUIElement(createServer);
        gui.removeGUIElement(serverip);
        gui.removeGUIElement(back);
        gui = null;
    }
    
    private void createHUB() {
        Player human = new Player(playerName.getCaption(), ColorRGBA.Blue);
        hub = Hub.getInstance();
        Hub.resetPlayerID();
        hub.initialize(human);
    }
    
    private void createServer() {
        createHUB();
        GamestateManager.getInstance().enterState(GamestateManager.CREATE_SERVER_STATE);
    }
    
    private void joinServer() {
        
    }
}
