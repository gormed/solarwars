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
 * Type: gamestates.lib.ServerLobbyState
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.DeathmatchGameplay;
import logic.Player;
import net.ClientRegisterListener;
import net.NetworkManager;
import net.NetworkManager.ClientConnectionState;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import net.messages.StartGameMessage;
import solarwars.AudioManager;
import solarwars.Hub;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * The Class ServerLobbyState.
 */
public class ServerLobbyState extends Gamestate implements ClientRegisterListener {

    /** The lobby. */
    private Label lobbyLabel;
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
    /** The refreshed players. */
    private HashMap<Integer, Player> refreshedPlayers;
    /** The players changed. */
    private boolean playersChanged;
    /** The player state listener. */
    private PlayerStateListener playerStateListener = new PlayerStateListener();
    /** The player connection listener. */
    private PlayerConnectionListener playerConnectionListener = new PlayerConnectionListener();
    /** The no server found. */
    private ClientConnectionState clientState = ClientConnectionState.CONNECTING;
    /** indicates that the game is set up and can be started. */
    private boolean gameStarted = false;
    /** The application. */
    private final SolarWarsApplication application;
    /** The client seed. */
    private long clientSeed;
    private Thread connectorThread = null;

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
        this.application = SolarWarsApplication.getInstance();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        
        if (clientState == ClientConnectionState.ERROR
                || clientState == ClientConnectionState.DISCONNECTED) {
            if (clientState == ClientConnectionState.ERROR) {
                AudioManager.getInstance().
                        playSoundInstance(AudioManager.SOUND_ERROR);
            }
            disconnect();
            GamestateManager.getInstance().
                    enterState(GamestateManager.MULTIPLAYER_STATE);
        }
        if (clientState == ClientConnectionState.CONNECTED) {
            networkManager.getChatModule().
                    initialize(gui, networkManager);
            serverName.setCaption(
                    client.getGameName() + " ver." + client.getVersion()
                    + " - "
                    + networkManager.getServerIPAdress().getHostName());

            float w = serverName.getText().getLineWidth();
            float h = serverName.getText().getHeight();
            serverName.setAlginment(new Rectangle(0, 0, w, h), Align.Left);
            clientState = ClientConnectionState.JOINED;
        }
        if (gameStarted && clientState == ClientConnectionState.JOINED) {
            startGame();
            GamestateManager.getInstance().
                    enterState(GamestateManager.MULTIPLAYER_MATCH_STATE);
        } else {
            refreshPlayers(refreshedPlayers);
        }

        //if (!client.isConnected())
    }

    /**
     * Start game.
     */
    private void startGame() {
        application.attachIsoCameraControl();
        logic.Level mpLevel =
                new logic.Level(
                SolarWarsApplication.getInstance().getRootNode(),
                SolarWarsApplication.getInstance().getAssetManager(),
                SolarWarsApplication.getInstance().getIsoControl(),
                gui,
                Hub.playersByID, clientSeed);
        game.setupGameplay(new DeathmatchGameplay(), mpLevel);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = GameGUI.getInstance();
        gameStarted = false;
        clientState = ClientConnectionState.CONNECTING;
        playersChanged = false;
        game.getApplication().setPauseOnLostFocus(false);

        networkManager = NetworkManager.getInstance();

        playerNamePos = new HashMap<Integer, Vector3f>();
        playerLabels = new HashMap<Integer, Label>();
        playerLabelIdx = new HashMap<Player, Integer>();
        refreshedPlayers = new HashMap<Integer, Player>();

        for (int i = 0; i < 8; i++) {
            playerNamePos.put(i, new Vector3f(
                    gui.getWidth() / 3,
                    (6 - i * 0.5f) * gui.getHeight() / 10,
                    0));
        }

        lobbyLabel = new Label(
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

        line = new Panel("Line",
                new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
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
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    leaveServer();
                }
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
                if (!isPressed) {

                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                }
            }
        };

        serverName = new Label("Connecting to " + serverIPAddress,
                new Vector3f(gui.getWidth() / 2, 7f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Orange,
                gui) {

            private float time;

            @Override
            public void updateGUI(float tpf) {
                if (clientState == ClientConnectionState.JOINED) {
                    return;
                }
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
//        float w = serverName.getText().getLineWidth();
//        float h = serverName.getText().getHeight();
//        serverName.setAlginment(new Rectangle(0, 0, w, h), Align.Center);

        playerPanel = new Panel(
                "BackgroundPanel",
                new Vector3f(gui.getWidth() / 2, 4.25f * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.35f, gui.getHeight() * 0.2f),
                ColorRGBA.White);



        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(lobbyLabel);
        gui.addGUIElement(leave);
        gui.addGUIElement(serverName);
        gui.addGUIElement(playerPanel);
        gui.addGUIElement(ready);

        connectorThread = new Thread("ConnectionThread") {

            @Override
            public void run() {
                clientState = joinServer();
            }
        };
        connectorThread.start();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        gameStarted = false;
        playersChanged = false;

        playerLabels.clear();
        playerNamePos.clear();
        playerLabelIdx.clear();
        gui.cleanUpGUI();

        if (client != null) {
            client.removeMessageListener(
                    playerConnectionListener,
                    PlayerAcceptedMessage.class,
                    PlayerLeavingMessage.class,
                    StartGameMessage.class);
            client.removeClientStateListener(playerStateListener);
        }

        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }
        
        this.networkManager = null;
        this.clientState = ClientConnectionState.DISCONNECTED;
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
     * @see net.ClientRegisterListener#registerClientListener(com.jme3.network.Client)
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
        if (playerLabels == null || gui == null || !playersChanged) {
            return;
        }
        HashMap<Integer, Player> clone = new HashMap<Integer, Player>(players);
        for (Map.Entry<Integer, Label> entry : playerLabels.entrySet()) {
            gui.removeGUIElement(entry.getValue());
        }

        playerLabels.clear();

        for (Map.Entry<Integer, Player> entry : clone.entrySet()) {
            addConnectedPlayer(entry.getValue());
        }
        playersChanged = false;
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

        player.setFontColor(p.getColor());
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
//                if (c.equals(client)) {
//                    noServerFound = true;
//                }
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
