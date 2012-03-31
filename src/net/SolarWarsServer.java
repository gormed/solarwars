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
    
    public static final int SERVER_VERSION = 10;
    public static final String SERVER_NAME = "SolarWars Server";

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
        Serializer.registerClass(Player.class);

        //this.createServer = (CreateServerState) GamestateManager.getInstance().getGamestate(GamestateManager.CREATE_SERVER_STATE);
    }
    
    private Server gameServer;

    public Server getGameServer() {
        return gameServer;
    }
    
    private int udpPort = NetworkManager.DEFAULT_PORT;
    private int tcpPort = NetworkManager.DEFAULT_PORT;
    private String serverName = "unnamed";
    private HashMap<Player, HostedConnection> connectedPlayers;
    private ArrayList<ServerRegisterListener> registerListeners;
    private ArrayList<Player> joinedPlayers;
    private ArrayList<Player> leavingPlayers;
    private boolean isRunning;

    public void start(String serverName) {
        this.serverName = serverName;
        start();
    }
    
    @Override
    public void start() {
        super.start(JmeContext.Type.Headless);
        connectedPlayers = new HashMap<Player, HostedConnection>(8);
        joinedPlayers = new ArrayList<Player>();
        leavingPlayers = new ArrayList<Player>();
        registerListeners = new ArrayList<ServerRegisterListener>();
        isRunning = true;
    }

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

    @Override
    public void simpleRender(RenderManager rm) {
    }

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

    boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return udpPort;
    }

    public void setPort(int port) {
        this.udpPort = port;
    }

    public String getIPAddress()
            throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
    
    public void addServerRegisterListener(ServerRegisterListener rl) {
        registerListeners.add(rl);
    }

    public void removeConnectionListener(ConnectionListener l) {
        gameServer.removeConnectionListener(l);
    }

    public void removeClientMessageListener(MessageListener<HostedConnection> hc, Class... classes) {
        gameServer.removeMessageListener(hc, classes);
    }

    public void addConnectingPlayer(Player p, HostedConnection hc) {
        connectedPlayers.put(p, hc);
        joinedPlayers.add(p);
    }

    public void removeLeavingPlayer(Player p) {
        
        leavingPlayers.add(p);
    }

    private void respondPlayer(Player p, boolean connecting) {
        if (connecting) {
            boolean isHost = p.isHost();
            HostedConnection hc = connectedPlayers.get(p);
            PlayerAcceptedMessage pam = 
                    new PlayerAcceptedMessage(p, 
                            ServerHub.getPlayers(),  
                            isHost);

            gameServer.broadcast(Filters.equalTo(hc), pam);
        } else {
            HostedConnection hc = connectedPlayers.get(p);
            PlayerLeavingMessage plm = new PlayerLeavingMessage(p);

            gameServer.broadcast(Filters.notEqualTo(hc), plm);
            connectedPlayers.remove(p);
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
