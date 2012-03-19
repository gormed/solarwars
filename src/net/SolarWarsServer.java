package net;

import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    }
    private Server gameServer;
    private int port;
    private UserListener connections = new UserListener();

    @Override
    public void start() {
        super.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {

        try {

            gameServer = Network.createServer(port);

            gameServer.start();
            gameServer.addMessageListener(new ServerListener(), StringMessage.class);

            gameServer.addConnectionListener(connections);
        } catch (IOException ex) {
            Logger.getLogger(SolarWarsServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        Serializer.registerClass(StringMessage.class);
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
        super.destroy();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public String getIPAddress() {
        return "localhost";
    }

    private class UserListener implements ConnectionListener {

        public void connectionAdded(Server server, HostedConnection conn) {
            StringMessage s = new StringMessage("Welcome!");
            gameServer.broadcast(s);
        }

        public void connectionRemoved(Server server, HostedConnection conn) {
        }
    }
}
