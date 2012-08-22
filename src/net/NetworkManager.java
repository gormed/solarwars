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
 * File: NetworkManager.java
 * Type: net.NetworkManager
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import net.messages.PlayerConnectingMessage;
import net.messages.StringMessage;
import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import logic.PlayerState;
import net.messages.ChatMessage;
import net.messages.GeneralActionMessage;
import net.messages.LevelActionMessage;
import net.messages.PlanetActionMessage;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;
import net.messages.StartGameMessage;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class NetworkManager.
 */
public class NetworkManager {

    public static final boolean WAIT_FOR_CLIENTS = true;
    public static final int MAXIMUM_DISCONNECT_TIMEOUT = 4;

    public enum ClientConnectionState {
        CONNECTING,
        CONNECTED,
        ERROR,
        DISCONNECTED,
        JOINED
    }
    /** The Constant DEFAULT_PORT. */
    public static final int DEFAULT_PORT = 6142;
    /** The instance. */
    private static NetworkManager instance;

    /**
     * Gets the single instance of NetworkManager.
     *
     * @return single instance of NetworkManager
     */
    public static NetworkManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new NetworkManager();
    }

    /**
     * Check ip.
     *
     * @param sip the sip
     * @return true, if successful
     */
    public static boolean checkIP(String sip) {
        String[] parts = sip.split("\\.");
        if (parts.length != 4 || (parts.length > 0 && parts[0].equals(""))) {
            return false;
        }
        try {
            for (String s : parts) {
                int i = Integer.parseInt(s);
                if (i < 0 || i > 255) {
                    return false;
                }
            }
        } catch (NumberFormatException exception) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.INFO, exception.getMessage(), exception);
            return false;
        }
        return true;
    }

    /**
     * Gets the byte inet address.
     *
     * @param ip the ip
     * @return the byte inet address
     */
    public static byte[] getByteInetAddress(String ip) {
        if (!checkIP(ip)) {
            return null;
        }

        byte[] address = new byte[4];
        int idx = 0;
        String[] parts = ip.split("\\.");
        for (String s : parts) {
            int i = Integer.parseInt(s);
            address[idx++] = (byte) i;
        }

        return address;
    }

    /**
     * Instantiates a new network manager.
     */
    private NetworkManager() {
        clientRegisterListeners = new ArrayList<ClientRegisterListener>();
        try {
            clientIPAdress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /** The udp port. */
    private int udpPort = DEFAULT_PORT;
    /** The tcp port. */
    private int tcpPort = DEFAULT_PORT;
    /** The client ip adress. */
    private InetAddress clientIPAdress;
    /** The server ip adress. */
    private InetAddress serverIPAdress;
    /** The is multiplayer game. */
    private boolean isMultiplayerGame = false;
    /** The this client. */
    private Client thisClient;
    /** The this server. */
    private SolarWarsServer thisServer;
    /** The chat module. */
    private ChatModule chatModule;
    /** The client register listeners. */
    private ArrayList<ClientRegisterListener> clientRegisterListeners;
    /** The client listener. */
    private ClientListener clientListener = new ClientListener();
    private Thread connectionCloser = null;

    /**
     * Gets the this client.
     *
     * @return the this client
     */
    public Client getThisClient() {
        return thisClient;
    }

    /**
     * Gets the chat module from the network manager. 
     * Chat module controls the visibilty of the chat 
     * gui and recieves messages from network.
     * Only is valid in a network session!
     * Is generally needed to move the chat gui to the next GameGUI ingame.
     * 
     * @return chat module from the current session
     */
    public ChatModule getChatModule() {
        return chatModule;
    }

    /**
     * Gets the client ip adress.
     *
     * @return the client ip adress
     */
    public InetAddress getClientIPAdress() {
        return clientIPAdress;
    }

    /**
     * Sets the client ip adress.
     *
     * @param clientIPAdress the new client ip adress
     */
    public void setClientIPAdress(InetAddress clientIPAdress) {
        this.clientIPAdress = clientIPAdress;
    }

    /**
     * Adds the client register listener.
     *
     * @param rl the rl
     */
    public void addClientRegisterListener(ClientRegisterListener rl) {
        clientRegisterListeners.add(rl);
    }

    /**
     * Removes the client register listener.
     *
     * @param rl the rl
     */
    public void removeClientRegisterListener(ClientRegisterListener rl) {
        clientRegisterListeners.remove(rl);
    }

    public void clientRemoveClientStateListener(ClientStateListener csl) {
        thisClient.removeClientStateListener(csl);
    }

    public void clientRemoveErrorListener(ErrorListener<? super Client> el) {
        thisClient.removeErrorListener(el);
    }

    public void clientRemoveMessageListener(MessageListener<? super Client> listener) {
        thisClient.removeMessageListener(listener);
    }

    public void serverRemoveClientMessageListener(MessageListener<? super HostedConnection> listener) {
        thisServer.removeClientMessageListener(listener);
    }

    public void serverRemoveConnectionListener(ConnectionListener listener) {
        thisServer.removeConnectionListener(listener);
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
     * Gets the server ip adress.
     *
     * @return the server ip adress
     */
    public InetAddress getServerIPAdress() {
        return serverIPAdress;
    }

    /**
     * Sets the server ip adress.
     *
     * @param serverIPAdress the new server ip adress
     */
    public void setServerIPAdress(InetAddress serverIPAdress) {
        this.serverIPAdress = serverIPAdress;
    }

    /**
     * Setup client.
     *
     * @param name the name
     * @param color the color
     * @param isHost the is host
     * @return the client
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Client setupClient(String name, ColorRGBA color, boolean isHost)
            throws IOException {
        if (serverIPAdress == null || udpPort < 1) {
            return null;
        }

        this.chatModule = new ChatModule(
                SolarWarsApplication.getInstance().getInputManager());

        Serializer.registerClass(StringMessage.class);
        Serializer.registerClass(ChatMessage.class);
        Serializer.registerClass(PlayerConnectingMessage.class);
        Serializer.registerClass(PlayerLeavingMessage.class);
        Serializer.registerClass(PlayerAcceptedMessage.class);
        Serializer.registerClass(StartGameMessage.class);
        Serializer.registerClass(PlanetActionMessage.class);
        Serializer.registerClass(GeneralActionMessage.class);
        Serializer.registerClass(LevelActionMessage.class);
        Serializer.registerClass(PlayerState.class);
        Serializer.registerClass(Player.class);

//        Serializer.registerClass(entities.AbstractPlanet.class);
//        Serializer.registerClass(entities.AbstractShip.class);
//        Serializer.registerClass(entities.BasePlanet.class);
//        Serializer.registerClass(entities.ShipGroup.class);
//        Serializer.registerClass(entities.SimpleShip.class);

        thisClient = Network.connectToServer(
                SolarWarsServer.SERVER_NAME,
                SolarWarsServer.SERVER_VERSION,
                serverIPAdress.getHostAddress(), tcpPort, udpPort);
        thisClient.addMessageListener(clientListener, StringMessage.class, ChatMessage.class);
        for (ClientRegisterListener rl : clientRegisterListeners) {
            rl.registerClientListener(thisClient);
        }

        thisClient.start();
        isMultiplayerGame = true;
        try {
            StringMessage s = new StringMessage(name + " joins the server!");
            PlayerConnectingMessage pcm = new PlayerConnectingMessage(name, color, isHost);
            thisClient.send(s);
            thisClient.send(pcm);
            return thisClient;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Setup server.
     *
     * @param serverName the server name
     * @return the solar wars server
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public SolarWarsServer setupServer(String serverName)
            throws IOException {
        thisServer = SolarWarsServer.getInstance();
        thisServer.start(serverName);

        isMultiplayerGame = true;
        try {
            serverIPAdress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return thisServer;
    }

    /**
     * Close server.
     *
     * @param wait the wait
     * @return the solar wars server
     */
    public Thread closeAllConnections(final boolean wait) {
        if (thisClient != null && thisClient.isConnected()) {
            thisClient.close();
        }
        thisClient = null;

        connectionCloser = new Thread("ConnectionCloserThread") {

            @Override
            public void run() {
                if (thisServer != null && thisServer.getGameServer() != null && thisServer.getGameServer().isRunning()) {
                    thisServer.stop(wait);
                }
            }
        };
//        thisServer = null;
        connectionCloser.start();


        isMultiplayerGame = false;
        return connectionCloser;
    }

    /**
     * Checks if is server running.
     *
     * @return true, if is server running
     */
    public boolean isServerRunning() {
        if (thisServer == null) {
            return false;
        }
        return thisServer.isRunning();
    }

    /**
     * Checks if is multiplayer game.
     *
     * @return true, if is multiplayer game
     */
    public boolean isMultiplayerGame() {
        return isMultiplayerGame;
    }

    /**
     * The listener interface for receiving client events.
     * The class that is interested in processing a client
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addClientListener<code> method. When
     * the client event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ClientEvent
     */
    private class ClientListener implements MessageListener<Client> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        @Override
        public void messageReceived(Client source, Message message) {
            if (message instanceof StringMessage) {
                // do something with the message
                StringMessage stringMessage = (StringMessage) message;
//                if (chatModule != null) {
//                    chatModule.playerSays(
//                            Hub.getInstance().getPlayer(source.getId()),
//                            stringMessage.getMessage());
//                }
                System.out.println("Client #" + source.getId() + " received: '" + stringMessage.getMessage() + "'");
            } else if (message instanceof ChatMessage) {
                ChatMessage chatMessage = (ChatMessage) message;
                if (chatModule != null) {
                    chatModule.playerSays(
                            Hub.getInstance().getPlayer(chatMessage.getPlayerID()),
                            chatMessage.getMessage());
                }
            }
        }
    }
}
