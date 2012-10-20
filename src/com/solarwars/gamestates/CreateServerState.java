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
package com.solarwars.gamestates;

import com.jme3.math.ColorRGBA;
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
import com.solarwars.controls.ControlManager;
import com.solarwars.gamestates.gui.ConnectedPlayerItem;
import com.solarwars.gamestates.gui.GameChatModule;
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
import com.solarwars.net.messages.PlayerReadyMessage;
import com.solarwars.net.messages.StartGameMessage;
import com.solarwars.net.messages.StringMessage;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class CreateServerState.
 */
public class CreateServerState extends Gamestate
        implements ServerRegisterListener, ClientRegisterListener {

    public static final String SERVER_FULL_MSG = "Server is full!";
    public static final String SERVER_NOT_IN_LOBBY_MSG = "Server is locked! The game is probably running...";
    // GUI
    private int maxPlayerNumber = SolarWarsSettings.getInstance().getMaxPlayerNumber();
    private String hostPlayerName;
    private ColorRGBA hostPlayerColor;
    private HashMap<Integer, Player> refreshedPlayers;
    private String seedString = SolarWarsSettings.getInstance().getSeed();
    private ListBox<ConnectedPlayerItem> serverLobbyBox;
    private GameChatModule gameChatModule;
    // Network
    private ServerHub serverHub;
    private NetworkManager networkManager = NetworkManager.getInstance();
    private SolarWarsServer solarWarsServer;
    private Client serverClient;
    private ClientMessageListener clientMessageListener = new ClientMessageListener();
    private ServerMessageListener serverMessageListener = new ServerMessageListener();
    private ClientConnectedListener clientConnectedListener = new ClientConnectedListener();
    // Logic
    private boolean gameStarted = false;
    private boolean serverEstablished = false;
    private long clientSeed;
    private boolean playersChanged;
    private long serverSeed = 42;
    private String hardSeed[] = {
        "42", "23", "666", "ESEL", "1337", "QWERT",
        "ASDF", ":D", ":-)", ":-P", "SOLARWARS",
        "ALT-F4", "ALTERNATIVLOS", "DOUGLAS ADAMS", "MAGRATHEA", "ERDE",
        "ALPHA CENTAURI", "PER ANHALTER INS ALL"};

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
                serverClient.removeMessageListener(clientMessageListener,
                        PlayerAcceptedMessage.class, PlayerLeavingMessage.class,
                        StartGameMessage.class, PlayerReadyMessage.class);
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

    @SuppressWarnings("unchecked")
    private void setupNiftyGUI() {
        serverLobbyBox = screen.findNiftyControl("server_lobby_box", ListBox.class);
        serverLobbyBox.clear();
        // CHAT ------------------------------------------
        // attach chat module
        gameChatModule = new GameChatModule(niftyGUI, NetworkManager.getInstance());
        // get input fields
        textInput = niftyGUI.getCurrentScreen().findElementByName("chat_text_field");
        textInputField = textInput.findNiftyControl("chat_text_field", TextField.class);
        // add input handler for button click to send message
        textInput.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        sendMessage();
                        return true;
                    default:
                        return false;
                }
            }
        });
        textInput.setFocus();
        // CHAT ------------------------------------------
    }

    /**
     * Starts a multiplayer game as server.
     */
    private void startGame() {
        application.attachCameraAndControl();
        com.solarwars.logic.Level mpLevel = new com.solarwars.logic.Level(
                SolarWarsApplication.getInstance().getRootNode(),
                SolarWarsApplication.getInstance().getAssetManager(),
                SolarWarsApplication.getInstance().getControlManager(),
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

        gameStarted = false;
        game.getApplication().setPauseOnLostFocus(false);
        serverHub = ServerHub.getInstance();
        refreshedPlayers = new HashMap<Integer, Player>();

        niftyGUI.gotoScreen("create_server");
        setupNiftyGUI();

        // =========================================
        // SETUP SERVER
        // =========================================
        setupServer();

        Player host = ServerHub.getHostPlayer();
        if (host != null) {
            host.setReady(true);
            serverLobbyBox.addItem(
                    new ConnectedPlayerItem(host.getName(),
                    host.getColor(), true));
        }
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
            serverClient.removeMessageListener(clientMessageListener,
                    PlayerAcceptedMessage.class, PlayerLeavingMessage.class,
                    StartGameMessage.class, PlayerReadyMessage.class);
        }
    }

    /**
     * Setup server.
     */
    private void setupServer() {
        if (networkManager.isMultiplayerGame() || networkManager.isServerRunning()) {
            serverEstablished = false;
            return;
        }
        ServerHub.resetPlayerID(0);
        int id = ServerHub.getContiniousPlayerID();

        hostPlayerName = SolarWarsSettings.getInstance().getPlayerName();
        hostPlayerColor = ColorRGBA.Blue.clone();

        Player hostPlayer = new Player(hostPlayerName,
                Player.PLAYER_COLORS[id], id, true);
        hostPlayer.initialize(true);
        ControlManager.getInstance().pullControl(hostPlayer);

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
        } catch (Exception e) {

            Logger.getLogger(
                    CreateServerState.class.getName()).log(Level.SEVERE,
                    e.getMessage(),
                    e);
            serverEstablished = false;
        }
        if (solarWarsServer != null) {
            serverEstablished = true;
        } else {
            serverEstablished = false;
        }
    }

    public String getSeedString() {
        seedString = SolarWarsSettings.getInstance().getSeed();
        Random r = new Random(convertGenericSeed(seedString));
        return hardSeed[r.nextInt(hardSeed.length)];
    }

    public String getServerName() {
        return SolarWarsServer.SERVER_NAME;
    }

    public String getServerVersion() {
        return String.format("v%1.2f", SolarWarsServer.SERVER_VERSION * 1f);
    }

    public String getServerIP() {
        return NetworkManager.getInstance().getClientIPAdress().getHostAddress();
    }

    private long convertGenericSeed(String seed) {
        char[] chars = seed.toCharArray();
        try {
            serverSeed = 0;
            for (Character c : chars) {
                serverSeed += c.hashCode();
            }
        } catch (Exception e) {
            serverSeed = SolarWarsServer.SERVER_VERSION;
        }
        return serverSeed;
    }

    public void onStartServer() {
        if (solarWarsServer.getGameServer().getConnections().size() <= 1) {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_ERROR);
            return;
        }
        boolean allReady = true;
        for (Player p : ServerHub.getPlayers()) {
            if (!p.isReady()) {
                gameChatModule.serverSays("Not all players are ready!");
                gameChatModule.localPlayerSendChatMessage(ServerHub.getHostPlayer().getID(), "Please get ready, " + p.getName() + "!");
                allReady = false;
            }
        }
        if (allReady) {
            startServer();
        } else {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_ERROR);
        }
    }

    @NiftyEventSubscriber(id = "server_seed")
    public void onServerSeedBoxChanged(final String id,
            final TextFieldChangedEvent event) {
        serverSeed = convertGenericSeed(event.getText());
        SolarWarsSettings.getInstance().setSeed(event.getText());
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
     * @param seed the seed
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
     * @param seed the level-seed
     */
    private void startClient(long seed) {

        this.clientSeed = seed;
        gameStarted = true;
    }

    public void onCancelServer() {
        cancelServer();
    }

    /**
     * Cancel server.
     */
    private void cancelServer() {

        networkManager.removeClientRegisterListener(this);
        networkManager.serverRemoveClientMessageListener(serverMessageListener);
        networkManager.serverRemoveConnectionListener(clientConnectedListener);
        networkManager.clientRemoveMessageListener(clientMessageListener);
        gameChatModule.destroy();

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
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_ERROR);
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
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
                PlayerReadyMessage.class,
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
                PlayerReadyMessage.class,
                StartGameMessage.class);
    }

    /*
     * Refreshes the player labels
     * 
     * @param players the players
     */
    private void refreshPlayers(HashMap<Integer, Player> players) {
        if (!playersChanged) {
            return;
        }
        serverLobbyBox.clear();

        for (Player p : players.values()) {
            if (p != null) {
                serverLobbyBox.addItem(
                        new ConnectedPlayerItem(
                        p.getName(),
                        p.getColor().clone(),
                        p.isReady()));
            }
        }
        playersChanged = false;
    }
    //==========================================================================
    //===   Chat
    //==========================================================================
    private Element textInput;
    private TextField textInputField;

    /**
     * Sends a message to the chat area
     */
    public void sendMessage() {
        String message = textInputField.getText();
        if (checkMessageStyle(message)) {
            gameChatModule.localPlayerSendChatMessage(Hub.getLocalPlayer().getID(), message);
            gameChatModule.playerSays(Hub.getLocalPlayer(), message);
        }
        textInputField.setText("");
        textInput.setFocus();
    }

    private boolean checkMessageStyle(String message) {
        boolean messageLengthOkay = message.length() >= 2;
        return messageLengthOkay;
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
            if (!solarWarsServer.getServerState().
                    equals(SolarWarsServer.ServerState.LOBBY)) {
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

                PlayerConnectingMessage pcm =
                        (PlayerConnectingMessage) message;
                boolean isHost = pcm.isHost();
                // creates a connecting player on the server
                Player newPlayer;
                if (!isHost) {
                    newPlayer = new Player(
                            pcm.getName(),
                            Player.getUnusedColor(ServerHub.getPlayers(), source.getId()),
                            source.getId());
                    newPlayer.initialize(false);
                    // ServerHub.getContiniousPlayerID());
                } else {
                    newPlayer = ServerHub.getHostPlayer();
                }
                serverHub.addPlayer(newPlayer);

                System.out.println("Player " + newPlayer.getName() + "[ID#"
                        + newPlayer.getID() + "] joined the Game.");
                solarWarsServer.addConnectingPlayer(newPlayer, source);

                refreshedPlayers = ServerHub.playersByID;
                playersChanged = true;
            } else if (message instanceof PlayerReadyMessage) {
                PlayerReadyMessage readyMessage = (PlayerReadyMessage) message;
                serverHub.getPlayer(source.getId()).setReady(readyMessage.isReady());
                solarWarsServer.getGameServer().
                        broadcast(Filters.notEqualTo(source), readyMessage);
                playersChanged = true;
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
         * 
         * 
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
                    if (!Hub.getInstance().isInitialized()) {
                        thisPlayer.initialize(true);
                        ControlManager.getInstance().pullControl(thisPlayer);
                        Hub.getInstance().initialize(thisPlayer, players);
                    }
                    gameChatModule.playerJoins(thisPlayer);
                } else {
                    if (Hub.getInstance().addPlayer(thisPlayer)) {
                        thisPlayer.initialize(false);
                        gameChatModule.playerJoins(thisPlayer);
                    }
                }
                refreshedPlayers = new HashMap<Integer, Player>(Hub.playersByID);
                playersChanged = true;
            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                Player p = plm.getPlayer();
                p.setLeaver(true);
                gameChatModule.playerLeaves(p);
                Hub.getInstance().removePlayer(p);

                refreshedPlayers = new HashMap<Integer, Player>(Hub.playersByID);
                playersChanged = true;
            } else if (message instanceof StartGameMessage) {
                StartGameMessage sgm = (StartGameMessage) message;
                long seed = sgm.getSeed();
                if (!gameStarted) {
                    startClient(seed);
                }

            } else if (message instanceof PlayerReadyMessage) {
                PlayerReadyMessage readyMessage = (PlayerReadyMessage) message;
                Player p = Hub.playersByID.get(readyMessage.getPlayerID());
                p.setReady(readyMessage.isReady());
                playersChanged = true;
            }
        }
    }
}
