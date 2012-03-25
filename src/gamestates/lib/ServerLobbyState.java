/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import java.util.ArrayList;
import logic.Player;
import net.NetworkManager;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class ServerLobbyState extends Gamestate {

    private Label lobby;
    private Panel backgroundPanel;
    private Panel line;
    private Panel playerPanel;
    private Button leave;
    private Button ready;
    private Label serverName;
    private GameGUI gui;
    private Hub hub;
    private String clientPlayerName;
    private ColorRGBA clientPlayerColor;
    private NetworkManager networkManager;
    private ArrayList<Vector3f> playerNamePos;
    private ArrayList<Label> playerLabels;
    public PlayerConnectionListener playerConnectionListener = new PlayerConnectionListener();

    public void setClientPlayerColor(ColorRGBA clientPlayerColor) {
        this.clientPlayerColor = clientPlayerColor;
    }

    public void setClientPlayerName(String clientPlayerName) {
        this.clientPlayerName = clientPlayerName;
    }

    public ServerLobbyState() {
        super(GamestateManager.SERVER_LOBBY_STATE);
    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        hub = Hub.getInstance();

        networkManager = NetworkManager.getInstance();

        playerNamePos = new ArrayList<Vector3f>();
        playerLabels = new ArrayList<Label>();




        lobby = new Label(
                "LOBBY",
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

        leave = new Button("Leave",
                new Vector3f(gui.getWidth() / 4, 1.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                leaveServer();
            }
        };

        ready = new Button("Ready",
                new Vector3f(3 * gui.getWidth() / 4, 1.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        serverName = new Label(networkManager.getServerIPAdress().getHostName(),
                new Vector3f(gui.getWidth() / 2, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        playerPanel = new Panel(
                "BackgroundPanel",
                new Vector3f(gui.getWidth() / 2, 4.25f * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.35f, gui.getHeight() * 0.2f),
                ColorRGBA.White);



        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(lobby);
        gui.addGUIElement(leave);
        gui.addGUIElement(serverName);
        gui.addGUIElement(playerPanel);
        gui.addGUIElement(ready);
    }

    @Override
    protected void unloadContent() {
        gui.removeGUIElement(backgroundPanel);
        gui.removeGUIElement(line);
        gui.removeGUIElement(lobby);
        gui.removeGUIElement(leave);
        gui.removeGUIElement(serverName);
        gui.removeGUIElement(playerPanel);
        gui.removeGUIElement(ready);
        gui = null;
    }

    private void leaveServer() {

        Client thisClient = networkManager.getThisClient();
        if (thisClient != null && thisClient.isConnected()) {
            PlayerLeavingMessage plm = new PlayerLeavingMessage(Hub.getLocalPlayer());

            thisClient.send(plm);
        }
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

    public class PlayerConnectionListener implements MessageListener<Client> {

        public void messageReceived(Client source, Message message) {
            if (message instanceof PlayerAcceptedMessage) {
                PlayerAcceptedMessage pam = (PlayerAcceptedMessage) message;
                Player thisPlayer = pam.getPlayer();
                ArrayList<Player> players = pam.getPlayers();

                Hub.getInstance().initialize(thisPlayer, players);

                for (Player p : players) {
                    addConnectedPlayer(p);
                }

            } else if (message instanceof PlayerLeavingMessage) {
                Client thisClient = networkManager.getThisClient();
                thisClient.close();

                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
            }
        }
    }
}
