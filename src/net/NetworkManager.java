/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: NetworkManager.java
 * Type: net.NetworkManager
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import net.messages.PlayerConnectingMessage;
import net.messages.StringMessage;
import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import gamestates.lib.ServerLobbyState;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;

/**
 * The Class NetworkManager.
 */
public class NetworkManager {

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
     * Instantiates a new network manager.
     */
    private NetworkManager() {
    }
    private int port = DEFAULT_PORT;
    private InetAddress clientIPAdress;
    private InetAddress serverIPAdress;
    private Client thisClient;
    private SolarWarsServer thisServer;

    public Client getThisClient() {
        return thisClient;
    }

    public InetAddress getClientIPAdress() {
        return clientIPAdress;
    }

    public void setClientIPAdress(InetAddress clientIPAdress) {
        this.clientIPAdress = clientIPAdress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getServerIPAdress() {
        return serverIPAdress;
    }

    public void setServerIPAdress(InetAddress serverIPAdress) {
        this.serverIPAdress = serverIPAdress;
    }

    public void setupClient(String name, ColorRGBA color, boolean isHost, MessageListener<Client> listener, Class... classes)
            throws IOException {
        if (serverIPAdress == null || port < 1) {
            return;
        }

        Serializer.registerClass(StringMessage.class);
        Serializer.registerClass(PlayerConnectingMessage.class);
        Serializer.registerClass(PlayerLeavingMessage.class);
        Serializer.registerClass(PlayerAcceptedMessage.class);
        Serializer.registerClass(Player.class);

        thisClient = Network.connectToServer(serverIPAdress.getHostAddress(), port);

        thisClient.start();
        thisClient.addMessageListener(new ClientListener(), StringMessage.class);
        if (listener != null) {
            // PlayerAcceptedMessage.class, PlayerLeavingMessage.class
            thisClient.addMessageListener(listener, classes);
        }

        StringMessage s = new StringMessage(name + " joins the server!");
        PlayerConnectingMessage pcm = new PlayerConnectingMessage(name, color, isHost);
        thisClient.send(s);
        thisClient.send(pcm);
    }

    public SolarWarsServer setupServer()
            throws IOException {
        thisServer = SolarWarsServer.getInstance();
        thisServer.start();
        try {
            serverIPAdress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return thisServer;
    }

    public void closeServer(boolean wait) {
        //thisClient.close();
        //thisClient = null;
        thisServer.stop(wait);
        thisServer = null;

        System.out.println("...Server closed!");
    }

    public boolean isServerRunning() {
        if (thisServer == null) {
            return false;
        }
        return thisServer.isRunning();
    }

    public static boolean checkIP(String sip) {
        String[] parts = sip.split("\\.");
        for (String s : parts) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255) {
                return false;
            }
        }
        return true;
    }

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
     *
     * @author Hans
     */
    private class ClientListener implements MessageListener<Client> {

        public void messageReceived(Client source, Message message) {
            if (message instanceof StringMessage) {
                // do something with the message
                StringMessage stringMessage = (StringMessage) message;
                System.out.println("Client #" + source.getId() + " received: '" + stringMessage.getMessage() + "'");
            }
        }
    }
}
