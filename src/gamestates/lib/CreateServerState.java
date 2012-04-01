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
 * File: CreateServerState.java
 * Type: gamestates.lib.CreateServerState
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
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
import logic.Gameplay;
import logic.Player;
import net.ClientRegisterListener;
import net.NetworkManager;
import net.ServerRegisterListener;
import net.ServerHub;
import net.messages.PlayerConnectingMessage;
import net.SolarWarsServer;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import net.messages.StartGameMessage;
import net.messages.StringMessage;
import solarwars.Hub;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 * The Class CreateServerState.
 */
public class CreateServerState extends Gamestate implements ServerRegisterListener, ClientRegisterListener {

    /** The create server label. */
    private Label createServerLabel;
    /** The your ip. */
    private Label yourIP;
    /** The address. */
    private Label address;
    /** The background panel. */
    private Panel backgroundPanel;
    /** The line. */
    private Panel line;
    /** The player panel. */
    private Panel playerPanel;
    /** The cancel. */
    private Button cancel;
    /** The start. */
    private Button start;
    /** The max players. */
    private Label maxPlayers;
    /** The player count. */
    private TextBox playerCount;
    /** The gui. */
    private GameGUI gui;
    /** The server hub. */
    private ServerHub serverHub;
    /** The solar wars server. */
    private SolarWarsServer solarWarsServer;
    /** The server as the client */
    private Client serverClient;
    /** The player name pos. */
    private HashMap<Integer, Vector3f> playerNamePos;
    /** The player labels. */
    private HashMap<Integer, Label> playerLabels;
    /** The player label idx. */
    private HashMap<Player, Integer> playerLabelIdx;
    /** The max player number. */
    private int maxPlayerNumber = 0;
    /** The host player name. */
    private String hostPlayerName;
    /** The host player color. */
    private ColorRGBA hostPlayerColor;
    /** The network manager. */
    private NetworkManager networkManager;
    /** The client message listener. */
    private ClientMessageListener clientMessageListener = new ClientMessageListener();
    /** The server message listener. */
    private ServerMessageListener serverMessageListener = new ServerMessageListener();
    /** The server connection listener. */
    private ServerConnectionListener serverConnectionListener = new ServerConnectionListener();
    /** indicates that the game is set up and can be started */
    private boolean gameStarted = false;

    /**
     * Sets the host player color.
     * 
     * Input Methods, that set the inital point of the state until loadContent is called
     *
     * @param hostPlayerColor the new host player color
     */
    public void setHostPlayerColor(ColorRGBA hostPlayerColor) {
        this.hostPlayerColor = hostPlayerColor;
    }

    /**
     * Sets the host player name.
     * 
     * Input Methods, that set the inital point of the state until loadContent is called
     *
     * @param hostPlayerName the new host player name
     */
    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }

    /**
     * Instantiates a new CreateServerState.
     */
    public CreateServerState() {
        super(GamestateManager.CREATE_SERVER_STATE);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
        if (gameStarted) {
            Server server = solarWarsServer.getGameServer();
            server.removeMessageListener(serverMessageListener);
            server.removeConnectionListener(serverConnectionListener);
            serverClient.removeMessageListener(clientMessageListener);
            solarWarsServer.enterLevel();
            GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_MATCH_STATE);
        }
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        networkManager = NetworkManager.getInstance();
        serverHub = ServerHub.getInstance();
        playerNamePos = new HashMap<Integer, Vector3f>();
        playerLabels = new HashMap<Integer, Label>();
        playerLabelIdx = new HashMap<Player, Integer>();


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
                startServer();
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

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        serverClient.removeMessageListener(clientMessageListener);


        playerLabels.clear();
        playerNamePos.clear();

        gui.cleanUpGUI();

        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }

        gui = null;
    }

    /**
     * Setup server.
     */
    private void setupServer() {
        ServerHub.resetPlayerID(0);
        int id = ServerHub.getContiniousPlayerID();
        Player hostPlayer = new Player(hostPlayerName, Player.PLAYER_COLORS[id], id, true);

        serverHub.initialize(hostPlayer, null);
        //hub.initialize(new Player(hostPlayerName, hostPlayerColor), null);
        try {
            solarWarsServer = networkManager.setupServer(hostPlayerName);
            solarWarsServer.addServerRegisterListener(this);
            networkManager.addClientRegisterListener(this);
            serverClient = networkManager.setupClient(hostPlayerName,
                    hostPlayerColor, true);

            //networkManager.getThisClient().addMessageListener(clientMessageListener, PlayerAcceptedMessage.class);

            networkManager.setClientIPAdress(InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Start server.
     */
    private void startServer() {
        long seed = createLevel();
        StartGameMessage gameMessage =
                new StartGameMessage(seed, ServerHub.getPlayers());
        solarWarsServer.getGameServer().broadcast(
                Filters.in(solarWarsServer.getGameServer().getConnections()),
                gameMessage);
    }

    /**
     * Creates the level.
     */
    private long createLevel() {
        long seed = System.currentTimeMillis();
        solarWarsServer.prepareLevel(seed);
//        logic.level.Level mpLevel = new logic.level.Level(
//                solarWarsServer.getRootNode(), 
//                solarWarsServer.getAssetManager(),
//                null, ServerHub.playersByID, seed);
        return seed;
    }

    /**
     * 
     * Starts the level.
     * 
     * @param seed the level-seed
     * @param players the players connected to the server
     */
    private void startLevel(long seed, ArrayList<Player> players) {
        logic.Level mpLevel =
                new logic.Level(
                SolarWarsApplication.getInstance().getRootNode(),
                SolarWarsApplication.getInstance().getAssetManager(),
                SolarWarsApplication.getInstance().getIsoControl(),
                gui,
                Hub.playersByID, seed);
        Gameplay.initialize(mpLevel);
        gameStarted = true;
    }

    /**
     * Cancel server.
     */
    private void cancelServer() {
        System.out.println("Closing server...");
        //solarWarsServer.removeClientMessageListener(serverMessageListener, PlayerAcceptedMessage.class, PlayerLeavingMessage.class);
        networkManager.removeClientRegisterListener(this);
        networkManager.closeServer(false);
        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }

    /**
     * Adds the connected player.
     *
     * @param p the p
     */
    private void addConnectedPlayer(Player p) {
//        Label temp = playerLabels.get(p.getId());
//        if (temp != null) {
//            gui.removeGUIElement(temp);
//        }
//        playerLabels.remove(p.getId());


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
        int id = playerLabelIdx.get(p);
        Label player = playerLabels.get(id);
        if (player != null) {
            playerLabels.remove(id);
            gui.removeGUIElement(player);
        }
    }

    /* (non-Javadoc)
     * @see net.ServerRegisterListener#registerServerListener(com.jme3.network.Server)
     */
    public void registerServerListener(Server gameServer) {
        gameServer.addConnectionListener(serverConnectionListener);
        gameServer.addMessageListener(serverMessageListener,
                PlayerConnectingMessage.class, StartGameMessage.class);

    }

    /* (non-Javadoc)
     * @see net.ClientRegisterListener#registerClientListener(com.jme3.network.Client)
     */
    public void registerClientListener(Client client) {
        client.addMessageListener(clientMessageListener,
                PlayerAcceptedMessage.class,
                PlayerLeavingMessage.class,
                StartGameMessage.class);
    }

    /*
     * Refreshes the player labels
     */
    private void refreshPlayers(ArrayList<Player> players) {
        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }

        playerLabels.clear();

        for (Player p : players) {
            addConnectedPlayer(p);
        }
    }

    /**
     * The listener interface for receiving serverConnection events.
     * The class that is interested in processing a serverConnection
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addServerConnectionListener<code> method. When
     * the serverConnection event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ServerConnectionEvent
     */
    private class ServerConnectionListener implements ConnectionListener {

        /* (non-Javadoc)
         * @see com.jme3.network.ConnectionListener#connectionAdded(com.jme3.network.Server, com.jme3.network.HostedConnection)
         */
        public void connectionAdded(Server server, HostedConnection conn) {
            StringMessage s = new StringMessage("Welcome client #" + conn.getId() + "!");
            solarWarsServer.getGameServer().broadcast(s);
        }

        /* (non-Javadoc)
         * @see com.jme3.network.ConnectionListener#connectionRemoved(com.jme3.network.Server, com.jme3.network.HostedConnection)
         */
        public void connectionRemoved(Server server, HostedConnection conn) {
            Player discPlayer = conn.getAttribute("PlayerObject");
            solarWarsServer.removeLeavingPlayer(discPlayer);
            removeLeavingPlayer(discPlayer);
            serverHub.removePlayer(discPlayer);
        }
    }

    /**
     * The listener interface for receiving serverMessage events.
     * The class that is interested in processing a serverMessage
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addServerMessageListener<code> method. When
     * the serverMessage event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ServerMessageEvent
     */
    private class ServerMessageListener implements MessageListener<HostedConnection> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof PlayerConnectingMessage) {

                PlayerConnectingMessage pcm = (PlayerConnectingMessage) message;
                boolean isHost = pcm.isHost();
                // creates a connecting player on the server
                Player newPlayer;
                if (!isHost) {
                    newPlayer = new Player(
                            pcm.getName(), Player.PLAYER_COLORS[source.getId()],
                            source.getId());
                    //ServerHub.getContiniousPlayerID());
                } else {
                    newPlayer = ServerHub.getHostPlayer();
                }
                serverHub.addPlayer(newPlayer);


                System.out.println("Player " + newPlayer.getName() + "[ID#" + newPlayer.getId() + "] joined the Game.");
                solarWarsServer.addConnectingPlayer(newPlayer, source);
                source.setAttribute("PlayerObject", newPlayer);
                source.setAttribute("PlayerID", newPlayer.getId());
                source.setAttribute("PlayerName", newPlayer.getName());

                refreshPlayers(ServerHub.getPlayers());
            }
        }
    }

    /**
     * The listener interface for receiving clientMessage events.
     * The class that is interested in processing a clientMessage
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addClientMessageListener<code> method. When
     * the clientMessage event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ClientMessageEvent
     */
    public class ClientMessageListener implements MessageListener<Client> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        public void messageReceived(Client source, Message message) {
            System.out.println(
                    "Client #" + source.getId() + " recieved a "
                    + message.getClass().getSimpleName());
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

            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                Player p = plm.getPlayer();

                Hub.getInstance().removePlayer(p);
            } else if (message instanceof StartGameMessage) {
                StartGameMessage sgm = (StartGameMessage) message;
                long seed = sgm.getSeed();
                ArrayList<Player> players = sgm.getPlayers();

                //SolarWarsApplication.getInstance().enqueue(null)
                startLevel(seed, players);
            }

        }
    }
}
