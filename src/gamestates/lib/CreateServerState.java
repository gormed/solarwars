/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import gui.elements.TextBox;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import net.NetworkManager;
import net.ServerHub;
import net.messages.PlayerConnectingMessage;
import net.SolarWarsServer;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class CreateServerState extends Gamestate {

    private Label createServerLabel;
    private Label yourIP;
    private Label address;
    private Panel backgroundPanel;
    private Panel line;
    private Panel playerPanel;
    private Button cancel;
    private Button start;
    private Label maxPlayers;
    private TextBox playerCount;
    private GameGUI gui;
    private ServerHub serverHub;
    private SolarWarsServer solarWarsServer;
    private HashMap<Integer, Vector3f> playerNamePos;
    private HashMap<Integer, Label> playerLabels;
    private int maxPlayerNumber = 0;
    private String hostPlayerName;
    private ColorRGBA hostPlayerColor;
    private NetworkManager networkManager;
    public ClientConnectionListener clientConListener = new ClientConnectionListener();
    public ServerConnetctionListener serverConListener = new ServerConnetctionListener();

    // Input Methods, that set the inital point of the state until loadContent is called
    public void setHostPlayerColor(ColorRGBA hostPlayerColor) {
        this.hostPlayerColor = hostPlayerColor;
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }

    public CreateServerState() {
        super(GamestateManager.CREATE_SERVER_STATE);
    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        networkManager = NetworkManager.getInstance();
        serverHub = ServerHub.getInstance();
        playerNamePos = new HashMap<Integer, Vector3f>();
        playerLabels = new HashMap<Integer, Label>();



        for (int i = 0; i < 8; i++) {
            playerNamePos.put(i, new Vector3f(
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
                cancelServer();
            }
        };

        start = new Button("Start",
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

        yourIP = new Label("Your IP:",
                new Vector3f(gui.getWidth() / 2, 1.75f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.White,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        // =========================================
        // SETUP SERVER
        // =========================================
        setupServer();

        address = new Label(networkManager.getServerIPAdress().getHostAddress(),
                new Vector3f(gui.getWidth() / 2, 1.25f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.White,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
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

        //addConnectedPlayer(Hub.getHostPlayer());



        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(createServerLabel);
        gui.addGUIElement(cancel);
        gui.addGUIElement(maxPlayers);
        gui.addGUIElement(playerCount);
        gui.addGUIElement(playerPanel);
        gui.addGUIElement(yourIP);
        gui.addGUIElement(address);
        gui.addGUIElement(start);
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
        gui.removeGUIElement(yourIP);
        gui.removeGUIElement(address);
        gui.removeGUIElement(start);

        solarWarsServer.removeClientMessageListener(serverConListener);

        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }
        playerLabels.clear();
        playerNamePos.clear();

        gui = null;
    }

    private void setupServer() {
        Player hostPlayer = new Player(hostPlayerName, hostPlayerColor, ServerHub.getContiniousPlayerID(), true);

        ServerHub.resetPlayerID(0);
        serverHub.initialize(hostPlayer, null);
        //hub.initialize(new Player(hostPlayerName, hostPlayerColor), null);
        try {
            solarWarsServer = networkManager.setupServer();
            networkManager.setupClient(hostPlayerName,
                    hostPlayerColor, true, clientConListener,
                    PlayerAcceptedMessage.class, PlayerLeavingMessage.class);

            networkManager.setClientIPAdress(InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cancelServer() {
        System.out.println("Closing server...");
        solarWarsServer.removeClientMessageListener(serverConListener, PlayerAcceptedMessage.class, PlayerLeavingMessage.class);
        networkManager.closeServer(false);
        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
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
        playerLabels.put(
                p.getId(),
                player);
    }

    private void removeLeavingPlayer(Player p) {
        Label player = playerLabels.get(p.getId());
        playerLabels.remove(player);
        gui.removeGUIElement(player);

    }

    public class ServerConnetctionListener implements MessageListener<HostedConnection> {

        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof PlayerConnectingMessage) {

                PlayerConnectingMessage pcm = (PlayerConnectingMessage) message;
                boolean isHost = pcm.isHost();
                // creates a connecting player on the server
                Player newPlayer = null;
                if (!isHost) {
                    newPlayer = new Player(
                            pcm.getName(), pcm.getColor(), 
                            ServerHub.getContiniousPlayerID());
                } else {
                    newPlayer = ServerHub.getHostPlayer();
                }
                serverHub.addPlayer(newPlayer);

                addConnectedPlayer(newPlayer);
                System.out.println("Player " + newPlayer.getName() + "[" + newPlayer.getColor().toString() + "] joined the Game.");
                solarWarsServer.addConnectingPlayer(newPlayer, source);

            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                // removes the sending Player from server

                Player p = plm.getPlayer();

                removeLeavingPlayer(p);
                solarWarsServer.removeLeavingPlayer(p, source);
            }
        }
    }

    public class ClientConnectionListener implements MessageListener<Client> {

        public void messageReceived(Client source, Message message) {
            if (message instanceof PlayerAcceptedMessage) {
                PlayerAcceptedMessage pam = (PlayerAcceptedMessage) message;


            } else if (message instanceof PlayerLeavingMessage) {
                Client thisClient = networkManager.getThisClient();
                thisClient.close();

                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
            }
        }
    }
}
