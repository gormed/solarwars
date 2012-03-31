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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: ServerLobbyState.java
 * Type: gamestates.lib.ServerLobbyState
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import net.ClientRegisterListener;
import net.NetworkManager;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import solarwars.Hub;
import solarwars.SolarWarsGame;

/**
 * The Class ServerLobbyState.
 */
public class ServerLobbyState extends Gamestate implements ClientRegisterListener {

    /** The lobby. */
    private Label lobby;
    /** The background panel. */
    private Panel backgroundPanel;
    /** The line. */
    private Panel line;
    /** The player panel. */
    private Panel playerPanel;
    /** The leave. */
    private Button leave;
    /** The ready. */
    private Button ready;
    /** The server name. */
    private Label serverName;
    /** The gui. */
    private GameGUI gui;
    /** The hub. */
    private Hub hub;
    /** The client player name. */
    private String clientPlayerName;
    /** The client player color. */
    private ColorRGBA clientPlayerColor;
    /** The server ip address. */
    private String serverIPAddress;
    /** The network manager. */
    private NetworkManager networkManager;
    /** The client. */
    private Client client;
    /** The player name pos. */
    private HashMap<Integer, Vector3f> playerNamePos;
    /** The player labels. */
    private HashMap<Integer, Label> playerLabels;
    /** The player label idx. */
    private HashMap<Player, Integer> playerLabelIdx;
    /** The player state listener. */
    private PlayerStateListener playerStateListener = new PlayerStateListener();
    /** The player connection listener. */
    private PlayerConnectionListener playerConnectionListener = new PlayerConnectionListener();
    /** The no server found. */
    private boolean noServerFound;

    /**
     * Sets the client player color.
     * 
     * Input Methods, that set the inital point of the state until loadContent is called
     *
     * @param clientPlayerColor the new client player color
     */
    public void setClientPlayerColor(ColorRGBA clientPlayerColor) {
        this.clientPlayerColor = clientPlayerColor;
    }

    /**
     * Sets the server ip address.
     * 
     * Input Methods, that set the inital point of the state until loadContent is called
     *
     * @param serverIPAddress the new server ip address
     */
    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    /**
     * Sets the client player name.
     * 
     * Input Methods, that set the inital point of the state until loadContent is called
     *
     * @param clientPlayerName the new client player name
     */
    public void setClientPlayerName(String clientPlayerName) {
        this.clientPlayerName = clientPlayerName;
    }

    /**
     * Instantiates a new server lobby state.
     */
    public ServerLobbyState() {
        super(GamestateManager.SERVER_LOBBY_STATE);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
        if (noServerFound) {
            disconnect();
        }
        //if (!client.isConnected())
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        hub = Hub.getInstance();

        networkManager = NetworkManager.getInstance();

        playerNamePos = new HashMap<Integer, Vector3f>();
        playerLabels = new HashMap<Integer, Label>();
        playerLabelIdx = new HashMap<Player, Integer>();

        for (int i = 0; i < 8; i++) {
            playerNamePos.put(i, new Vector3f(
                    gui.getWidth() / 3,
                    (6 - i * 0.5f) * gui.getHeight() / 10,
                    0));
        }

        joinServer();

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

        serverName = new Label(client.getGameName() + " ver." + client.getVersion() + " - " + networkManager.getServerIPAdress().getHostName(),
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

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        playerLabels.clear();
        playerNamePos.clear();
        gui.cleanUpGUI();

        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }

        this.hub = null;
        this.networkManager = null;
        this.noServerFound = false;
        this.playerLabelIdx = null;
        this.playerLabels = null;
        this.playerNamePos = null;
        this.gui = null;
    }

    /**
     * Leave server.
     */
    private void leaveServer() {
        //Client thisClient = networkManager.getThisClient();
        if (client != null && client.isConnected()) {
            client.close();
        }
    }

    /**
     * Join server.
     */
    private void joinServer() {
        try {
            InetAddress add = InetAddress.getByAddress(NetworkManager.getByteInetAddress(serverIPAddress));
            networkManager.setServerIPAdress(add);
            networkManager.setClientIPAdress(InetAddress.getLocalHost());
            networkManager.addClientRegisterListener(this);
            client = networkManager.setupClient(clientPlayerName, clientPlayerColor, false);
        } catch (UnknownHostException ex) {
            System.err.println("Unkown host! Please verify the IP.");
            System.err.println(ex.getMessage());
            noServerFound = true;
        } catch (IOException ex) {
            if (ex instanceof ConnectException) {
                System.err.println("Server " + serverIPAddress + " refused connection. Reason: " + ex.getMessage());

            } else {
                Logger.getLogger(MultiplayerState.class.getName()).log(Level.SEVERE, null, ex);
            }
            noServerFound = true;
        }
    }

    /**
     * Disconnect.
     */
    private void disconnect() {
        networkManager.removeClientRegisterListener(this);
        client = null;
        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }

    /* (non-Javadoc)
     * @see net.ClientRegisterListener#registerClientListener(com.jme3.network.Client)
     */
    public void registerClientListener(Client client) {
        client.addMessageListener(playerConnectionListener,
                PlayerAcceptedMessage.class, PlayerLeavingMessage.class);
        client.addClientStateListener(playerStateListener);

    }

    /**
     * Adds the connected player.
     *
     * @param p the p
     */
    private void addConnectedPlayer(Player p) {

        int id = playerLabels.size();

        playerLabelIdx.put(p, id);

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
                id,
                player);
    }

    /**
     * Removes the leaving player.
     *
     * @param p the p
     */
    private void removeLeavingPlayer(Player p) {
        Label player = playerLabels.get(p.getId());
        if (player != null) {
            playerLabels.remove(p.getId());
            gui.removeGUIElement(player);
        }
    }

    /**
     * The listener interface for receiving playerState events.
     * The class that is interested in processing a playerState
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addPlayerStateListener<code> method. When
     * the playerState event occurs, that object's appropriate
     * method is invoked.
     *
     * @see PlayerStateEvent
     */
    private class PlayerStateListener implements ClientStateListener {

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientConnected(com.jme3.network.Client)
         */
        public void clientConnected(Client c) {
        }

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientDisconnected(com.jme3.network.Client, com.jme3.network.ClientStateListener.DisconnectInfo)
         */
        public void clientDisconnected(Client c, DisconnectInfo info) {
            System.out.print("[Client #" + c.getId() + "] - Disconnect from server: ");
            if (info != null) {
                System.out.println(info.reason);
            }
            if (c.equals(client)) {
                disconnect();
            }
        }
    }

    /**
     * The listener interface for receiving playerConnection events.
     * The class that is interested in processing a playerConnection
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addPlayerConnectionListener<code> method. When
     * the playerConnection event occurs, that object's appropriate
     * method is invoked.
     *
     * @see PlayerConnectionEvent
     */
    private class PlayerConnectionListener implements MessageListener<Client> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        public void messageReceived(Client source, Message message) {
            if (message instanceof PlayerAcceptedMessage) {
                PlayerAcceptedMessage pam = (PlayerAcceptedMessage) message;
                Player thisPlayer = pam.getPlayer();
                boolean isConnecting = pam.isConnecting();
                ArrayList<Player> players = pam.getPlayers();

                if (isConnecting) {
                    Hub.getInstance().initialize(thisPlayer, players);
                } else {
                    Hub.getInstance().addPlayer(thisPlayer);
                }
                
                refreshPlayers(players);

            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                Player p = plm.getPlayer();

                Hub.getInstance().removePlayer(p);
                removeLeavingPlayer(p);
            }
        }

        private void refreshPlayers(ArrayList<Player> players) {
            for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
                gui.removeGUIElement(entry.getValue());
            }

            playerLabels.clear();

            for (Player p : players) {
                addConnectedPlayer(p);
            }
        }
    }
}
