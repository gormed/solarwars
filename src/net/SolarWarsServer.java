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
 * File: SolarWarsServer.java
 * Type: net.SolarWarsServer
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import net.messages.PlayerConnectingMessage;
import net.messages.StringMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import logic.PlayerState;
import net.messages.PlanetActionMessage;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import net.messages.StartGameMessage;

/**
 * The Class SolarWarsServer.
 */
public class SolarWarsServer extends SimpleApplication {

    /** The Constant SERVER_VERSION. */
    public static final int SERVER_VERSION = 10;
    /** The Constant SERVER_NAME. */
    public static final String SERVER_NAME = "SolarWars Server";
    /** The server app. */
    private static SolarWarsServer serverApp;

    /**
     * Gets the single instance of SolarWarsServer.
     *
     * @return single instance of SolarWarsServer
     */
    public static SolarWarsServer getInstance() {
        if (serverApp != null) {
            return serverApp;
        }
        return serverApp = new SolarWarsServer();
    }

    /**
     * Instantiates a new solar wars server.
     */
    private SolarWarsServer() {
        super();
        // Add messages to serializer so they can be read, DO THIS FIRST
        Serializer.registerClass(StringMessage.class);
        Serializer.registerClass(PlayerConnectingMessage.class);
        Serializer.registerClass(PlayerLeavingMessage.class);
        Serializer.registerClass(PlayerAcceptedMessage.class);
        Serializer.registerClass(StartGameMessage.class);
        Serializer.registerClass(PlanetActionMessage.class);
        Serializer.registerClass(PlayerState.class);
        Serializer.registerClass(Player.class);

        Serializer.registerClass(entities.AbstractPlanet.class);
        Serializer.registerClass(entities.AbstractShip.class);
        Serializer.registerClass(entities.BasePlanet.class);
        Serializer.registerClass(entities.ShipGroup.class);
        Serializer.registerClass(entities.SimpleShip.class);
        //this.createServer = (CreateServerState) GamestateManager.getInstance().getGamestate(GamestateManager.CREATE_SERVER_STATE);
    }
    /** The game server. */
    private Server gameServer;

    /**
     * Gets the game server.
     *
     * @return the game server
     */
    public Server getGameServer() {
        return gameServer;
    }
    /** The udp port. */
    private int udpPort = NetworkManager.DEFAULT_PORT;
    /** The tcp port. */
    private int tcpPort = NetworkManager.DEFAULT_PORT;
    /** The server name. */
    private String serverName = "unnamed";
    /** The connected players. */
    private HashMap<Player, HostedConnection> connectedPlayers;
    /** The register listeners. */
    private ArrayList<ServerRegisterListener> registerListeners;
    /** The joined players. */
    private ArrayList<Player> joinedPlayers;
    /** The leaving players. */
    private ArrayList<Player> leavingPlayers;
    /** The is running. */
    private boolean isRunning;
    private long seed;
    private GameplayListener gameplayListener = new GameplayListener();

    /**
     * Start.
     *
     * @param serverName the server name
     */
    public void start(String serverName) {
        this.serverName = serverName;
        start();
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#start()
     */
    @Override
    public void start() {
        super.start(JmeContext.Type.Headless);
        connectedPlayers = new HashMap<Player, HostedConnection>(8);
        joinedPlayers = new ArrayList<Player>();
        leavingPlayers = new ArrayList<Player>();
        registerListeners = new ArrayList<ServerRegisterListener>();
        isRunning = true;
    }

    public void prepareLevel(long seed) {
        this.seed = seed;
    }

    public void enterLevel() {
        gameServer.addMessageListener(gameplayListener, PlanetActionMessage.class);
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleInitApp()
     */
    @Override
    public void simpleInitApp() {
        try {

            gameServer = Network.createServer(
                    SERVER_NAME,
                    SERVER_VERSION,
                    tcpPort, udpPort);
            gameServer.start();
            gameServer.addMessageListener(new ServerListener(), StringMessage.class);
            for (ServerRegisterListener rl : registerListeners) {
                rl.registerServerListener(gameServer);
            }

            //NetworkManager.getInstance().registerServerListeners();
            //addClientMessageListener(createServer.serverConListener, PlayerConnectingMessage.class, PlayerLeavingMessage.class);
            //gameServer.addConnectionListener(connections);

        } catch (IOException ex) {
            Logger.getLogger(SolarWarsServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleUpdate(float)
     */
    @Override
    public void simpleUpdate(float tpf) {
        for (Player p : joinedPlayers) {
            respondPlayer(p, true);
        }
        joinedPlayers.clear();

        for (Player p : leavingPlayers) {
            respondPlayer(p, false);
        }
        leavingPlayers.clear();
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleRender(com.jme3.renderer.RenderManager)
     */
    @Override
    public void simpleRender(RenderManager rm) {
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#destroy()
     */
    @Override
    public void destroy() {

        connectedPlayers.clear();
        joinedPlayers.clear();
        leavingPlayers.clear();
        registerListeners.clear();

        gameServer.close();
        isRunning = false;
        System.out.println("...Server closed!");
        super.destroy();
    }

    /**
     * Checks if is running.
     *
     * @return true, if is running
     */
    boolean isRunning() {
        return isRunning;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return udpPort;
    }

    /**
     * Sets the port.
     *
     * @param port the new port
     */
    public void setPort(int port) {
        this.udpPort = port;
    }

    /**
     * Gets the iP address.
     *
     * @return the iP address
     * @throws UnknownHostException the unknown host exception
     */
    public String getIPAddress()
            throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Adds the server register listener.
     *
     * @param rl the rl
     */
    public void addServerRegisterListener(ServerRegisterListener rl) {
        registerListeners.add(rl);
    }

    /**
     * Removes the connection listener.
     *
     * @param l the l
     */
    public void removeConnectionListener(ConnectionListener l) {
        gameServer.removeConnectionListener(l);
    }

    /**
     * Removes the client message listener.
     *
     * @param hc the hc
     * @param classes the classes
     */
    public void removeClientMessageListener(MessageListener<HostedConnection> hc, Class... classes) {
        gameServer.removeMessageListener(hc, classes);
    }

    /**
     * Adds the connecting player.
     *
     * @param p the p
     * @param hc the hc
     */
    public void addConnectingPlayer(Player p, HostedConnection hc) {
        connectedPlayers.put(p, hc);
        joinedPlayers.add(p);
    }

    /**
     * Removes the leaving player.
     *
     * @param p the p
     */
    public void removeLeavingPlayer(Player p) {

        leavingPlayers.add(p);
    }

    /**
     * Respond player.
     *
     * @param p the p
     * @param connecting the connecting
     */
    private void respondPlayer(Player p, boolean connecting) {
        if (connecting) {
            boolean isHost = p.isHost();
            HostedConnection hc = connectedPlayers.get(p);
            PlayerAcceptedMessage joiningPlayer =
                    new PlayerAcceptedMessage(p,
                    ServerHub.getPlayers(),
                    isHost, true);

            gameServer.broadcast(Filters.equalTo(hc), joiningPlayer);

            Collection<HostedConnection> connections = gameServer.getConnections();

            for (Map.Entry<Player, HostedConnection> entrySet : connectedPlayers.entrySet()) {
                Player player = entrySet.getKey();
                HostedConnection connection = entrySet.getValue();

                if (player != null && !player.equals(p) && connection != null) {
                    isHost = player.isHost();
                    PlayerAcceptedMessage otherPlayer =
                            new PlayerAcceptedMessage(
                            p,
                            ServerHub.getPlayers(),
                            isHost,
                            false);
                    gameServer.broadcast(Filters.equalTo(connection), otherPlayer);
                }
            }
        } else {
            HostedConnection hc = connectedPlayers.get(p);
            PlayerLeavingMessage plm = new PlayerLeavingMessage(p);

            gameServer.broadcast(Filters.notEqualTo(hc), plm);
            connectedPlayers.remove(p);
        }
    }

    /**
     * The listener interface for receiving server events.
     * The class that is interested in processing a server
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addServerListener<code> method. When
     * the server event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ServerEvent
     */
    private class ServerListener implements MessageListener<HostedConnection> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof StringMessage) {
                // do something with the message
                StringMessage stringMessage = (StringMessage) message;
                System.out.println(
                        "Server received '"
                        + stringMessage.getMessage()
                        + "' from client #" + source.getId());
            }
        }
    }

    private class GameplayListener implements MessageListener<HostedConnection> {

        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof PlanetActionMessage) {
                PlanetActionMessage clientMessage = (PlanetActionMessage) message;

                PlanetActionMessage serverMessage =
                        new PlanetActionMessage(
                        clientMessage.getClientTime(),
                        System.currentTimeMillis(),
                        clientMessage.getActionName(),
                        clientMessage.getPlayerID(),
                        clientMessage.getPlayerState(),
                        clientMessage.getPlanetID());

                gameServer.broadcast(Filters.notEqualTo(source), serverMessage);
            }
        }
    }
}
