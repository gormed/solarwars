package net;

import net.messages.PlayerConnectingMessage;
import net.messages.StringMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import gamestates.GamestateManager;
import gamestates.lib.CreateServerState;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Player;
import net.messages.PlayerAcceptedMessage;
import net.messages.PlayerLeavingMessage;

/**
 * test
 * @author normenhansen
 */
public class SolarWarsServer extends SimpleApplication {

    private static SolarWarsServer serverApp;

    public static SolarWarsServer getInstance() {
        if (serverApp != null) {
            return serverApp;
        }
        return serverApp = new SolarWarsServer();
    }

    private SolarWarsServer() {
        super();
        // Add messages to serializer so they can be read, DO THIS FIRST
        Serializer.registerClass(StringMessage.class);
        Serializer.registerClass(PlayerConnectingMessage.class);
        Serializer.registerClass(PlayerLeavingMessage.class);
        Serializer.registerClass(PlayerAcceptedMessage.class);

        this.createServer = (CreateServerState) GamestateManager.getInstance().getGamestate(GamestateManager.CREATE_SERVER_STATE);
    }
    private Server gameServer;

    Server getGameServer() {
        return gameServer;
    }
    private int port = NetworkManager.DEFAULT_PORT;
    private HashMap<Player, HostedConnection> connectedPlayers;
    private UserListener connections = new UserListener();
    private CreateServerState createServer;
    private boolean isRunning;

    @Override
    public void start() {
        super.start(JmeContext.Type.Headless);
        connectedPlayers = new HashMap<Player, HostedConnection>(8);
        isRunning = true;
    }

    @Override
    public void simpleInitApp() {
        try {

            gameServer = Network.createServer(port);
            gameServer.start();
            gameServer.addMessageListener(new ServerListener(), StringMessage.class);
            addClientMessageListener(createServer, PlayerConnectingMessage.class, PlayerLeavingMessage.class);
            gameServer.addConnectionListener(connections);

        } catch (IOException ex) {
            Logger.getLogger(SolarWarsServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void destroy() {
        gameServer.close();
        isRunning = false;
        super.destroy();
    }

    boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIPAddress()
            throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public void addConnectionListener(ConnectionListener l) {
        gameServer.addConnectionListener(l);
    }

    public void removeConnectionListener(ConnectionListener l) {
        gameServer.removeConnectionListener(l);
    }

    public void addClientMessageListener(MessageListener<HostedConnection> hc, Class... classes ) {
        gameServer.addMessageListener(hc, classes);
    }

    public void removeClientMessageListener(MessageListener<HostedConnection> hc, Class... classes) {
        gameServer.removeMessageListener(hc, classes);
    }

    public void addConnectingPlayer(Player p, HostedConnection hc) {
        connectedPlayers.put(p, hc);
    }

    public void removeLeavingPlayer(Player p, HostedConnection hc) {
        connectedPlayers.remove(p);
    }

    /**
     * 
     * @author Hans
     */
    private class UserListener implements ConnectionListener {

        public void connectionAdded(Server server, HostedConnection conn) {
            StringMessage s = new StringMessage("Welcome client #" + conn.getId() + "!");
            gameServer.broadcast(s);
        }

        public void connectionRemoved(Server server, HostedConnection conn) {
        }
    }

    /**
     *
     * @author Hans
     */
    private class ServerListener implements MessageListener<HostedConnection> {

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
}
