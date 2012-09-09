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
 * File: ServerLobbyState.java
 * Type: com.solarwars.gamestates.lib.ServerLobbyState
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.gamestates.gui.ConnectedPlayerItem;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.Player;
import com.solarwars.net.ClientRegisterListener;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.NetworkManager.ClientConnectionState;
import com.solarwars.net.messages.PlayerAcceptedMessage;
import com.solarwars.net.messages.PlayerLeavingMessage;
import com.solarwars.net.messages.StartGameMessage;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The Class ServerLobbyState.
 * @author Hans Ferchland
 */
public class ServerLobbyState extends Gamestate implements ClientRegisterListener {
    //==========================================================================
    //      Fields
    //==========================================================================

    // GUI
    private String clientPlayerName;
    private ColorRGBA clientPlayerColor;
    private String serverIPAddress;
    private Element serverIPLabel;
    private ListBox<ConnectedPlayerItem> serverLobbyBox;
    private float animateConnectCounter = 0;
    private Element serverNameLabel;
    // NETWORK
    private NetworkManager networkManager;
    private Client client;
    private HashMap<Integer, Player> refreshedPlayers;
    private boolean playersChanged;
    private PlayerStateListener playerStateListener = new PlayerStateListener();
    private PlayerConnectionListener playerConnectionListener =
            new PlayerConnectionListener();
    private ClientConnectionState clientState = ClientConnectionState.CONNECTING;
    private Thread connectorThread = null;
    // LOGIC
    private boolean gameStarted = false;
    private final SolarWarsApplication application;
    private long clientSeed;

    /**
     * Instantiates a new server lobby state.
     */
    public ServerLobbyState() {
        super(SolarWarsGame.SERVER_LOBBY_STATE);
        this.application = SolarWarsApplication.getInstance();
    }

    //==========================================================================
    //      GAMESTATE METHODS
    //==========================================================================

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (clientState == ClientConnectionState.ERROR
                    || clientState == ClientConnectionState.DISCONNECTED) {
                if (clientState == ClientConnectionState.ERROR) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_ERROR);
                }
                disconnect();
                switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//                GamestateManager.getInstance().
//                        enterState(GamestateManager.MULTIPLAYER_STATE);
            }
            if (clientState == ClientConnectionState.CONNECTED) {
                serverNameLabel.getRenderer(TextRenderer.class).
                        setText(client.getGameName() + " ver." + client.getVersion());
                serverIPLabel.getRenderer(TextRenderer.class).
                        setText(networkManager.getServerIPAdress().getHostName());
                clientState = ClientConnectionState.JOINED;
            }
            if (gameStarted && clientState == ClientConnectionState.JOINED) {
                startGame();
                switchToState(SolarWarsGame.MULTIPLAYER_MATCH_STATE);
//                GamestateManager.getInstance().
//                        enterState(GamestateManager.MULTIPLAYER_MATCH_STATE);
            } else if (clientState != ClientConnectionState.JOINED) {
                
                animateConnectCounter += tpf;
                if (animateConnectCounter > 0.5f) {
                    String text = serverIPLabel.getRenderer(TextRenderer.class).
                            getOriginalText() + ".";
                    serverIPLabel.getRenderer(TextRenderer.class).
                            setText(text);
                    animateConnectCounter = 0;
                }
            }
            refreshPlayers(refreshedPlayers);
        }
        //if (!client.isConnected())
    }

    /**
     * Start game.
     */
    private void startGame() {
        application.attachIsoCameraControl();
        com.solarwars.logic.Level mpLevel =
                new com.solarwars.logic.Level(
                SolarWarsApplication.getInstance().getRootNode(),
                SolarWarsApplication.getInstance().getAssetManager(),
                SolarWarsApplication.getInstance().getIsoControl(),
                Hub.playersByID, clientSeed);
        game.setupGameplay(new DeathmatchGameplay(), mpLevel);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent(com.solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent() {

        serverIPAddress = SolarWarsSettings.getInstance().getIpAddressFavouriteServer();
        clientPlayerColor = ColorRGBA.Red.clone();
        clientPlayerName = SolarWarsSettings.getInstance().getPlayerName();
        refreshedPlayers = new HashMap<Integer, Player>();

        gameStarted = false;
        clientState = ClientConnectionState.CONNECTING;
        playersChanged = false;
        game.getApplication().setPauseOnLostFocus(false);
        networkManager = NetworkManager.getInstance();

        niftyGUI.gotoScreen("server_lobby");

        serverLobbyBox = screen.findNiftyControl("server_lobby_box", ListBox.class);
        serverNameLabel = screen.findElementByName("server_lobby_server_name_label");
        serverIPLabel = screen.findElementByName("server_lobby_server_ip_label");

        // swap old with new text
        serverIPLabel.getRenderer(TextRenderer.class).
                setText(serverIPAddress);

        connectorThread = new Thread("ConnectionThread") {

            @Override
            public void run() {
                clientState = joinServer();
            }
        };

        connectorThread.start();

//        for (int i = 0; i < 8; i++) {
//            playerNamePos.put(i, new Vector3f(
//                    gui.getWidth() / 3,
//                    (6 - i * 0.5f) * gui.getHeight() / 10,
//                    0));
//        }
//
//        lobbyLabel = new Label(
//                "LOBBY",
//                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
//                new Vector3f(2, 2, 1),
//                ColorRGBA.White, gui) {
//
//            private float time;
//
//            @Override
//            public void updateGUI(float tpf) {
//                time += tpf;
//
//                if (time < 0.2f) {
//                    text.setText(title + "_");
//                } else if (time < 0.4f) {
//                    text.setText(title);
//                } else {
//                    time = 0;
//                }
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//        };
//
//        backgroundPanel = new Panel(
//                "BackgroundPanel",
//                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
//                new Vector2f(gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
//                ColorRGBA.Blue);
//
//        line = new Panel("Line",
//                new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
//                new Vector2f(gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
//                ColorRGBA.White);
//
//        leave = new Button("Leave",
//                new Vector3f(gui.getWidth() / 4, 1.5f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
//                ColorRGBA.White, gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//                if (!isPressed) {
//                    AudioManager.getInstance().
//                            playSoundInstance(AudioManager.SOUND_CLICK);
//                    leaveServer();
//                }
//            }
//        };
//
//        ready = new Button("Ready",
//                new Vector3f(3 * gui.getWidth() / 4, 1.5f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
//                ColorRGBA.White, gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//                if (!isPressed) {
//
//                    AudioManager.getInstance().
//                            playSoundInstance(AudioManager.SOUND_CLICK);
//                }
//            }
//        };
//
//        serverName = new Label("Connecting to " + serverIPAddress,
//                new Vector3f(gui.getWidth() / 2, 7f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ,
//                ColorRGBA.Orange,
//                gui) {
//
//            private float time;
//
//            @Override
//            public void updateGUI(float tpf) {
//                if (clientState == ClientConnectionState.JOINED) {
//                    return;
//                }
//                time += tpf;
//
//                if (time < 0.2f) {
//                    text.setText(title + "_");
//                } else if (time < 0.4f) {
//                    text.setText(title);
//                } else {
//                    time = 0;
//                }
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//        };
////        float w = serverName.getText().getLineWidth();
////        float h = serverName.getText().getHeight();
////        serverName.setAlginment(new Rectangle(0, 0, w, h), Align.Center);
//
//        playerPanel = new Panel(
//                "BackgroundPanel",
//                new Vector3f(gui.getWidth() / 2, 4.25f * gui.getHeight() / 10, 0),
//                new Vector2f(gui.getWidth() * 0.35f, gui.getHeight() * 0.2f),
//                ColorRGBA.White);
//
//
//
//        gui.addGUIElement(backgroundPanel);
//        gui.addGUIElement(line);
//        gui.addGUIElement(lobbyLabel);
//        gui.addGUIElement(leave);
//        gui.addGUIElement(serverName);
//        gui.addGUIElement(playerPanel);
//        gui.addGUIElement(ready);

    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        gameStarted = false;
        playersChanged = false;

        if (client != null) {
            client.removeMessageListener(
                    playerConnectionListener,
                    PlayerAcceptedMessage.class,
                    PlayerLeavingMessage.class,
                    StartGameMessage.class);
            client.removeClientStateListener(playerStateListener);
        }

        this.networkManager = null;
        this.clientState = ClientConnectionState.DISCONNECTED;

    }
    //==========================================================================
    //      NIFTY GUI EVENT METHODS
    //==========================================================================

    @NiftyEventSubscriber(id = "leave")
    public void onLeaveButton(final String id,
            final ButtonClickedEvent event) {
        leaveServer();
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
    }

    @NiftyEventSubscriber(id = "ready")
    public void onReadyButton(final String id,
            final ButtonClickedEvent event) {
    }
    //==========================================================================
    //      INTERNAL GUI REQUESTS
    //==========================================================================

    /**
     * Leave server.
     */
    private void leaveServer() {
        //Client thisClient = networkManager.getThisClient();
        if (client != null && client.isConnected()) {
            client.close();
        }

        try {
            connectorThread.interrupt();
        } catch (Exception e) {
            Logger.getLogger(ServerLobbyState.class.getName()).
                    log(Level.SEVERE, e.getMessage(), e);
        }
        disconnect();
        clientState = ClientConnectionState.DISCONNECTED;
    }

    /**
     * Join server.
     */
    private NetworkManager.ClientConnectionState joinServer() {
        try {
            networkManager.setClientIPAdress(InetAddress.getLocalHost());
            InetAddress add = InetAddress.getByAddress(
                    NetworkManager.getByteInetAddress(serverIPAddress));
            networkManager.setServerIPAdress(add);
            networkManager.addClientRegisterListener(this);
            client = networkManager.setupClient(
                    clientPlayerName, clientPlayerColor, false);
            return ClientConnectionState.CONNECTED;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MultiplayerState.class.getName()).
                    log(Level.SEVERE, ex.getMessage(), ex);
            return ClientConnectionState.ERROR;
        } catch (IOException ex) {
            Logger.getLogger(MultiplayerState.class.getName()).
                    log(Level.SEVERE, ex.getMessage(), ex);
            return ClientConnectionState.ERROR;
        }
    }

    /**
     * Starts the level and changes the gamestate.
     *
     * @param seed the level-seed
     */
    private void startClient(long seed) {
        this.clientSeed = seed;
        gameStarted = true;
    }

    /**
     * Disconnect.
     */
    private void disconnect() {
        networkManager.removeClientRegisterListener(this);
    }

    /* (non-Javadoc)
     * @see com.solarwars.net.ClientRegisterListener#registerClientListener(com.jme3.network.Client)
     */
    @Override
    public void registerClientListener(Client client) {
        client.addMessageListener(playerConnectionListener,
                PlayerAcceptedMessage.class,
                PlayerLeavingMessage.class,
                StartGameMessage.class);
        client.addClientStateListener(playerStateListener);

    }

    /**
     * Refresh players.
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
                        new ConnectedPlayerItem(p.getName(), p.getColor()));
            }
        }
        playersChanged = false;
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
        @Override
        public void clientConnected(Client c) {
        }

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientDisconnected(com.jme3.network.Client, com.jme3.network.ClientStateListener.DisconnectInfo)
         */
        @Override
        public void clientDisconnected(Client c, DisconnectInfo info) {
            System.out.print("[Client #" + c.getId() + "] - Disconnect from server: ");

            if (info != null) {
                System.out.println(info.reason);
                clientState = ClientConnectionState.DISCONNECTED;
            } else {
                System.out.println("client closed");
                clientState = ClientConnectionState.DISCONNECTED;
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
        @Override
        public void messageReceived(Client source, Message message) {
            System.out.println(
                    "Client #" + source.getId() + " recieved a "
                    + message.getClass().getSimpleName());

            // PLAYER ACCEPTED
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

                refreshedPlayers = new HashMap<Integer, Player>(Hub.playersByID);
                playersChanged = true;
                //refreshPlayers(players);

                // PLAYER LEAVING
            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                Player p = plm.getPlayer();
                p.setLeaver(true);
                NetworkManager.getInstance().getChatModule().playerLeaves(p);
                Hub.getInstance().removePlayer(p);
                refreshedPlayers = new HashMap<Integer, Player>(Hub.playersByID);
                playersChanged = true;
                // START GAME
            } else if (message instanceof StartGameMessage) {
                StartGameMessage sgm = (StartGameMessage) message;
                long seed = sgm.getSeed();
                ArrayList<Player> players = sgm.getPlayers();

                startClient(seed);
            }


        }
    }
}
