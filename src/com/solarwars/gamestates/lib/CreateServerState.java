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
 * File: CreateServerState.java
 * Type: com.solarwars.gamestates.lib.CreateServerState
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.lib;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


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
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.gui.GameGUI;
import com.solarwars.gui.elements.Anchor;
import com.solarwars.gui.elements.Button;
import com.solarwars.gui.elements.Label;
import com.solarwars.gui.elements.Panel;
import com.solarwars.gui.elements.TextBox;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.Player;
import com.solarwars.net.ClientRegisterListener;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.ServerHub;
import com.solarwars.net.ServerRegisterListener;
import com.solarwars.net.SolarWarsServer;
import com.solarwars.net.messages.PlayerAcceptedMessage;
import com.solarwars.net.messages.PlayerConnectingMessage;
import com.solarwars.net.messages.PlayerLeavingMessage;
import com.solarwars.net.messages.StartGameMessage;
import com.solarwars.net.messages.StringMessage;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;

/**
 * The Class CreateServerState.
 */
public class CreateServerState extends Gamestate
        implements ServerRegisterListener, ClientRegisterListener {

    public static final String SERVER_FULL_MSG = "Server is full!";
    public static final String SERVER_NOT_IN_LOBBY_MSG = "Server is locked! The game is probably running...";
    // GUI
    private Label createServerLabel;
    private Label yourIPLabel;
    private Label addressLabel;
    private Panel backgroundPanel;
    private Panel linePanel;
    private Panel playerPanel;
    private Button cancelButton;
    private Button startButton;
    private Label maxPlayersLabel;
    private TextBox playerCountBox;
    private Label seedLabel;
    private TextBox levelSeedBox;
    private HashMap<Integer, Vector3f> playerNamePos;
    private HashMap<Integer, Label> playerLabels;
    private GameGUI gui;
    // Game
    private ServerHub serverHub;
    private SolarWarsServer solarWarsServer;
    private Client serverClient;
    private Anchor playerLabelsNode;
    private HashMap<Player, Integer> playerLabelIdx;
    private HashMap<Integer, Player> refreshedPlayers;
    private int maxPlayerNumber = SolarWarsSettings.getInstance().getMaxPlayerNumber();
    private String hostPlayerName;
    private ColorRGBA hostPlayerColor;
    private NetworkManager networkManager;
    private ClientMessageListener clientMessageListener = new ClientMessageListener();
    private ServerMessageListener serverMessageListener = new ServerMessageListener();
    private ClientConnectedListener clientConnectedListener = new ClientConnectedListener();
    private boolean gameStarted = false;
    private boolean serverEstablished = false;
    private final SolarWarsApplication application;
    private long clientSeed;
    private boolean playersChanged;
    private String seedString = SolarWarsSettings.getInstance().getSeed();
    private long serverSeed = 42;

    /**
     * Instantiates a new CreateServerState.
     */
    public CreateServerState() {
        super(SolarWarsGame.CREATE_SERVER_STATE);
        this.application = SolarWarsApplication.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (!serverEstablished) {
                cancelServer();
            }
            if (gameStarted) {
                Server server = solarWarsServer.getGameServer();
                server.removeMessageListener(serverMessageListener);
                // server.removeConnectionListener(serverConnectionListener);
                serverClient.removeMessageListener(clientMessageListener);
                solarWarsServer.enterLevel();
                startGame();
                
                switchToState(SolarWarsGame.MULTIPLAYER_MATCH_STATE);
//                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_MATCH_STATE);
            } else {
                refreshPlayers(refreshedPlayers);
            }
        } else {
            
        }
        // if (!leavingPlayers.isEmpty()) {
        // for (Player p : leavingPlayers) {
        // removeLeavingPlayer(p);
        // }
        // }
        // leavingPlayers.clear();
    }

    /**
     * Starts a multiplayer game as server.
     */
    private void startGame() {
        application.attachIsoCameraControl();
        com.solarwars.logic.Level mpLevel = new com.solarwars.logic.Level(
                SolarWarsApplication.getInstance().getRootNode(),
                SolarWarsApplication.getInstance().getAssetManager(),
                SolarWarsApplication.getInstance().getIsoControl(),
                Hub.playersByID, clientSeed);
        game.setupGameplay(new DeathmatchGameplay(), mpLevel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.solarwars.gamestates.Gamestate#loadContent(com.solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent() {

        gui = GameGUI.getInstance();
        gameStarted = false;
        game.getApplication().setPauseOnLostFocus(false);
        networkManager = NetworkManager.getInstance();
        serverHub = ServerHub.getInstance();
        playerNamePos = new HashMap<Integer, Vector3f>();
        playerLabels = new HashMap<Integer, Label>();
        playerLabelsNode = new Anchor();
        playerLabelIdx = new HashMap<Player, Integer>();
        // = new ArrayList<Player>();
        refreshedPlayers = new HashMap<Integer, Player>();


        for (int i = 0; i < 8; i++) {
            playerNamePos.put(i, new Vector3f(gui.getWidth() / 3,
                    (6 - i * 0.5f) * gui.getHeight() / 10, 0));
        }

        createServerLabel = new Label("CREATE SERVER", new Vector3f(
                gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4), new Vector3f(
                2, 2, 1), ColorRGBA.White, gui) {

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

        backgroundPanel = new Panel("BackgroundPanel", new Vector3f(
                gui.getWidth() / 2, gui.getHeight() / 2, 0), new Vector2f(
                gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
                ColorRGBA.Blue);

        linePanel = new Panel("Line", new Vector3f(gui.getWidth() / 2,
                8 * gui.getHeight() / 10, 0), new Vector2f(
                gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
                ColorRGBA.White);

        cancelButton = new Button("Cancel", new Vector3f(gui.getWidth() / 4,
                1.5f * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ,
                ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_CLICK);
                    cancelServer();
                }
            }
        };

        startButton = new Button("Start", new Vector3f(3 * gui.getWidth() / 4,
                1.5f * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ,
                ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_CLICK);
                    startServer();
                }
            }
        };

        yourIPLabel = new Label("Your IP:", new Vector3f(gui.getWidth() / 2,
                1.75f * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        seedLabel = new Label("Seed", new Vector3f(gui.getWidth() / 8,
                7f * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ,
                ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        levelSeedBox = new TextBox(ColorRGBA.Blue, new Vector3f(
                4.0f * gui.getWidth() / 10, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, new Vector2f(gui.getWidth() / 6, 30),
                SolarWarsSettings.getInstance().getSeed(), ColorRGBA.White, gui, false) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed,
                    float tpf) {
                char[] chars = caption.toCharArray();
                try {
                    serverSeed = 0;
                    for (Character c : chars) {
                        serverSeed += c.hashCode();
                    }
                } catch (Exception e) {
                    caption = serverSeed + "";
                }

                SolarWarsSettings.getInstance().setSeed(caption);
            }
        };

        maxPlayersLabel = new Label("Players", new Vector3f(
                7 * gui.getWidth() / 10, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        playerCountBox = new TextBox(ColorRGBA.Blue, new Vector3f(
                8.5f * gui.getWidth() / 10, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, new Vector2f(40, 30),
                SolarWarsSettings.getInstance().getMaxPlayerNumber() + "", ColorRGBA.White,
                gui, true) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed,
                    float tpf) {
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

                int playerNumber = 2;
                try {
                    if (caption.equals("")) {
                        playerNumber = 8;
                    } else {
                        playerNumber = Integer.parseInt(caption);
                    }
                } catch (Exception e) {
                    caption = playerNumber + "";
                } finally {
                    SolarWarsSettings.getInstance().setMaxPlayerNumber(playerNumber);
                }
            }
        };

        playerPanel = new Panel("BackgroundPanel", new Vector3f(
                gui.getWidth() / 2, 4.25f * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.35f, gui.getHeight() * 0.2f),
                ColorRGBA.White);

        // =========================================
        // SETUP SERVER
        // =========================================
        setupServer();

        addressLabel = new Label(
                networkManager.getServerIPAdress().getHostAddress(),
                new Vector3f(gui.getWidth() / 2, 1.25f * gui.getHeight() / 10,
                0), Vector3f.UNIT_XYZ, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        // addConnectedPlayer(Hub.getHostPlayer());

        // chatGUI = new ChatGUI(gui);
        // chatGUI.setVisible(true);


        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(linePanel);
        gui.addGUIElement(createServerLabel);
        gui.addGUIElement(cancelButton);
        gui.addGUIElement(playerCountBox);
        gui.addGUIElement(playerPanel);
        gui.addGUIElement(playerLabelsNode);
        gui.addGUIElement(yourIPLabel);
        gui.addGUIElement(addressLabel);
        gui.addGUIElement(startButton);
        gui.addGUIElement(seedLabel);
        gui.addGUIElement(levelSeedBox);
        gui.addGUIElement(maxPlayersLabel);
        networkManager.getChatModule().initialize(gui, networkManager);
        // gui.addGUIElement(chatGUI);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        try {
            SolarWarsSettings.getInstance().save();
        } catch (GameSettingsException e) {
            e.printStackTrace();
        }

        if (serverClient != null) {
            serverClient.removeMessageListener(clientMessageListener);
        }

        playerLabels.clear();
        playerNamePos.clear();
        playerLabelIdx.clear();

        gui.cleanUpGUI();

        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }
        this.playerLabelIdx = null;
        this.playerLabels = null;
        this.playerNamePos = null;
        gui = null;
    }

    /**
     * Setup server.
     */
    private void setupServer() {
        ServerHub.resetPlayerID(0);
        int id = ServerHub.getContiniousPlayerID();
        
        hostPlayerName = SolarWarsSettings.getInstance().getPlayerName();
        hostPlayerColor = ColorRGBA.Blue.clone();
        
        Player hostPlayer = new Player(hostPlayerName,
                Player.PLAYER_COLORS[id], id, true);
        
        serverHub.initialize(hostPlayer, null);
        // hub.initialize(new Player(hostPlayerName, hostPlayerColor), null);
        try {
            solarWarsServer = networkManager.setupServer(System.currentTimeMillis()
                    + "");
            serverEstablished = true;
            solarWarsServer.addServerRegisterListener(this);
            networkManager.addClientRegisterListener(this);
            serverClient = networkManager.setupClient(hostPlayerName,
                    hostPlayerColor, true);

            // networkManager.getThisClient().addMessageListener(clientMessageListener,
            // PlayerAcceptedMessage.class);

            networkManager.setClientIPAdress(InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            Logger.getLogger(CreateServerState.class.getName()).log(Level.WARNING,
                    ex.getMessage(),
                    ex);
            serverEstablished = false;
        } catch (IOException ex) {
            if (ex instanceof ConnectException) {
                Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE,
                        "Unable to establish server! Reason: "
                        + ex.getMessage(),
                        ex);
            } else {
                Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE,
                        ex.getMessage(),
                        ex);
            }
            serverEstablished = false;
        }
    }

    /**
     * Start server.
     */
    private void startServer() {
        createLevel(serverSeed);
        StartGameMessage gameMessage = new StartGameMessage(serverSeed,
                ServerHub.getPlayers());
        solarWarsServer.getGameServer().broadcast(Filters.in(solarWarsServer.getGameServer().getConnections()),
                gameMessage);
    }

    /**
     * Creates the level.
     * 
     * @param seed
     *            the seed
     */
    private void createLevel(long seed) {
        if (seed == 0) {
            seed = System.currentTimeMillis();
        }
        solarWarsServer.prepareLevel(seed);
    }

    /**
     * Starts the level.
     * 
     * @param seed
     *            the level-seed
     */
    private void startClient(long seed) {

        this.clientSeed = seed;
        gameStarted = true;
    }

    /**
     * Cancel server.
     */
    private void cancelServer() {

        networkManager.removeClientRegisterListener(this);
        networkManager.serverRemoveClientMessageListener(serverMessageListener);
        networkManager.serverRemoveConnectionListener(clientConnectedListener);
        networkManager.clientRemoveMessageListener(clientMessageListener);

        if (serverEstablished) {
            Future<Thread> fut = application.enqueue(new Callable<Thread>() {

                @Override
                public Thread call() throws Exception {

                    return networkManager.closeAllConnections(true);
                }
            });
            try {
                Thread connect = fut.get(NetworkManager.MAXIMUM_DISCONNECT_TIMEOUT,
                        TimeUnit.SECONDS);
                connect.interrupt();
            } catch (InterruptedException ex) {
                Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE,
                        ex.getMessage(),
                        ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE,
                        ex.getMessage(),
                        ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(CreateServerState.class.getName()).log(Level.SEVERE,
                        "Server did not shut down in time: {0} {1}",
                        new Object[]{
                            ex.getMessage(),
                            ex});
            }
        }
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }

    /**
     * Adds the connected player.
     * 
     * @param p
     *            the p
     */
    private void addConnectedPlayer(Player p) {
        // Label temp = playerLabels.get(p.getId());
        // if (temp != null) {
        // gui.removeGUIElement(temp);
        // }
        // playerLabels.remove(p.getId());


        int id = playerLabels.size();

        playerLabelIdx.put(p, id);

        Label player = new Label(p.getName(), playerNamePos.get(id),
                Vector3f.UNIT_XYZ.clone(), ColorRGBA.Blue.clone(), gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };
        player.setFontColor(p.getColor());
        playerLabelsNode.addElement(player);
        playerLabels.put(id, player);
    }

    /**
     * Removes the leaving player.
     * 
     * @param p
     *            the p
     * @deprecated
     */
    @Deprecated
    private void removeLeavingPlayer(Player p) {
        if (playerLabelIdx.containsKey(p)) {
            int id = playerLabelIdx.get(p);
            Label player = playerLabels.get(id);
            if (player != null) {
                playerLabels.remove(id);
                playerLabelsNode.removeElement(player);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.solarwars.net.ServerRegisterListener#registerServerListener(com.jme3.network.Server
     * )
     */
    @Override
    public void registerServerListener(Server gameServer) {
        gameServer.addConnectionListener(clientConnectedListener);
        gameServer.addMessageListener(serverMessageListener,
                PlayerConnectingMessage.class,
                StartGameMessage.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.solarwars.net.ClientRegisterListener#registerClientListener(com.jme3.network.Client
     * )
     */
    @Override
    public void registerClientListener(Client client) {
        client.addMessageListener(clientMessageListener,
                PlayerAcceptedMessage.class,
                PlayerLeavingMessage.class,
                StartGameMessage.class);
    }

    /*
     * Refreshes the player labels
     * 
     * @param players the players
     */
    private void refreshPlayers(HashMap<Integer, Player> players) {
        if (playerLabels == null || gui == null || !playersChanged) {
            return;
        }
        HashMap<Integer, Player> clone = new HashMap<Integer, Player>(players);
        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            playerLabelsNode.removeElement(entry.getValue());
        }

        playerLabels.clear();

        for (Map.Entry<Integer, Player> entry : clone.entrySet()) {
            addConnectedPlayer(entry.getValue());
        }
        playersChanged = false;
    }

    /**
     * The listener interface for receiving serverConnection events. The class
     * that is interested in processing a serverConnection event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addServerConnectionListener<code> method. When
     * the serverConnection event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see ServerConnectionEvent
     */
    private class ClientConnectedListener implements ConnectionListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.jme3.network.ConnectionListener#connectionAdded(com.jme3.network
         * .Server, com.jme3.network.HostedConnection)
         */
        @Override
        public void connectionAdded(Server server, HostedConnection conn) {
            if (!solarWarsServer.getServerState().equals(SolarWarsServer.ServerState.LOBBY)) {
                conn.close(SERVER_NOT_IN_LOBBY_MSG);
            } else if (server.getConnections().size() > maxPlayerNumber) {
                conn.close(SERVER_FULL_MSG);
            } else {
                StringMessage s = new StringMessage("Welcome client #"
                        + conn.getId() + "!");
                solarWarsServer.getGameServer().broadcast(s);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.jme3.network.ConnectionListener#connectionRemoved(com.jme3.network
         * .Server, com.jme3.network.HostedConnection)
         */
        @Override
        public void connectionRemoved(Server server, HostedConnection conn) {
            Player discPlayer = conn.getAttribute("PlayerObject");
            if (discPlayer != null) {

                solarWarsServer.removeLeavingPlayer(discPlayer);
                // leavingPlayers.add(discPlayer);
                ServerHub.getInstance().removePlayer(discPlayer);
                refreshedPlayers = ServerHub.playersByID;
                playersChanged = true;
            }
        }
    }

    /**
     * The listener interface for receiving serverMessage events. The class that
     * is interested in processing a serverMessage event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addServerMessageListener<code> method. When
     * the serverMessage event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see ServerMessageEvent
     */
    private class ServerMessageListener implements
            MessageListener<HostedConnection> {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.jme3.network.MessageListener#messageReceived(java.lang.Object,
         * com.jme3.network.Message)
         */
        @Override
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof PlayerConnectingMessage) {

                PlayerConnectingMessage pcm = (PlayerConnectingMessage) message;
                boolean isHost = pcm.isHost();
                // creates a connecting player on the server
                Player newPlayer;
                if (!isHost) {
                    newPlayer = new Player(pcm.getName(),
                            Player.getUnusedColor(ServerHub.getPlayers(),
                            source.getId()),
                            // Player.PLAYER_COLORS[source.getId()],
                            source.getId());
                    // ServerHub.getContiniousPlayerID());
                } else {
                    newPlayer = ServerHub.getHostPlayer();
                }
                serverHub.addPlayer(newPlayer);


                System.out.println("Player " + newPlayer.getName() + "[ID#"
                        + newPlayer.getId() + "] joined the Game.");
                solarWarsServer.addConnectingPlayer(newPlayer, source);


                refreshedPlayers = ServerHub.playersByID;
                playersChanged = true;
                // refreshPlayers(ServerHub.getPlayers());
            }
        }
    }

    /**
     * The listener interface for receiving clientMessage events. The class that
     * is interested in processing a clientMessage event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addClientMessageListener<code> method. When
     * the clientMessage event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see ClientMessageEvent
     */
    public class ClientMessageListener implements MessageListener<Client> {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.jme3.network.MessageListener#messageReceived(java.lang.Object,
         * com.jme3.network.Message)
         */
        @Override
        public void messageReceived(Client source, Message message) {
            System.out.println("Client #" + source.getId() + " recieved a "
                    + message.getClass().getSimpleName());
            // if this player or any other player connects to the server
            if (message instanceof PlayerAcceptedMessage) {
                PlayerAcceptedMessage pam = (PlayerAcceptedMessage) message;
                // get the firing player
                Player thisPlayer = pam.getPlayer();
                //
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
                p.setLeaver(true);
                NetworkManager.getInstance().getChatModule().playerLeaves(p);
                Hub.getInstance().removePlayer(p);
            } else if (message instanceof StartGameMessage) {
                StartGameMessage sgm = (StartGameMessage) message;
                long seed = sgm.getSeed();
                ArrayList<Player> players = sgm.getPlayers();

                // SolarWarsApplication.getInstance().enqueue(null)
                if (!gameStarted) {
                    startClient(seed);
                }
            }

        }
    }
}
