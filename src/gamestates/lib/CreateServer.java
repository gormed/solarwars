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
import java.util.ArrayList;
import logic.Player;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class CreateServer extends Gamestate {

    private Label createServerLabel;
    private Panel backgroundPanel;
    private Panel line;
    private Panel playerPanel;
    private Button cancel;
    private Label maxPlayers;
    private TextBox playerCount;
    private GameGUI gui;
    private ArrayList<Vector3f> playerNamePos;
    private ArrayList<Label> playerLabels;
    private int maxPlayerNumber = 0;

    public CreateServer() {
        super(GamestateManager.CREATE_SERVER_STATE);


    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        playerNamePos = new ArrayList<Vector3f>();
        playerLabels = new ArrayList<Label>();

        for (int i = 0; i < 8; i++) {
            playerNamePos.add(new Vector3f(
                    gui.getWidth() / 3,
                    (6 - i * 0.5f) * gui.getHeight() / 10,
                    0));
        }

        createServerLabel = new Label(
                "CREATE SERVER",
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

        cancel = new Button("Cancel",
                new Vector3f(gui.getWidth() / 4, 1.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
            }
        };

        maxPlayers = new Label("Max Players",
                new Vector3f(gui.getWidth() / 4, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        playerCount = new TextBox(
                ColorRGBA.Blue,
                new Vector3f(3 * gui.getWidth() / 4, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                "2",
                ColorRGBA.White,
                gui, true) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
                int players = 0;
                try {
                    players = Integer.parseInt(caption);
                } catch (NumberFormatException e) {
                    caption = "";
                }
                if (players < 2 || players > 8) {
                    caption = "";
                } else {
                    maxPlayerNumber = players;
                }

            }
        };

        playerPanel = new Panel(
                "BackgroundPanel",
                new Vector3f(gui.getWidth() / 2, 4.25f * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.35f, gui.getHeight() * 0.2f),
                ColorRGBA.White);
        
        addConnectedPlayer(Hub.getLocalPlayer());

        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(createServerLabel);
        gui.addGUIElement(cancel);
        gui.addGUIElement(maxPlayers);
        gui.addGUIElement(playerCount);
        gui.addGUIElement(playerPanel);
    }

    @Override
    protected void unloadContent() {
        gui.removeGUIElement(backgroundPanel);
        gui.removeGUIElement(line);
        gui.removeGUIElement(createServerLabel);
        gui.removeGUIElement(cancel);
        gui.removeGUIElement(maxPlayers);
        gui.removeGUIElement(playerCount);
        gui.removeGUIElement(playerPanel);
        
        for (Label l : playerLabels) {
            gui.removeGUIElement(l);
        }
        playerLabels.clear();
        playerNamePos.clear();

        gui = null;
    }
    
    private void setupServer() {
        
    }
    
    private void addConnectedPlayer(Player p) {
        int id = playerLabels.size();
        Label player = new Label(p.getName(),
                playerNamePos.get(id),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Blue,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        gui.addGUIElement(player);
        playerLabels.add(
                id,
                player);
    }
}
