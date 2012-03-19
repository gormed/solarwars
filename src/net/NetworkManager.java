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

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class NetworkManager.
 */
public class NetworkManager {

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
    private int port;
    private Inet4Address clientIPAdress;
    private Inet4Address serverIPAdress;
    private Client thisClient;
    private SolarWarsServer thisServer;

    public Inet4Address getClientIPAdress() {
        return clientIPAdress;
    }

    void setClientIPAdress(Inet4Address clientIPAdress) {
        this.clientIPAdress = clientIPAdress;
    }

    public int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    public Inet4Address getServerIPAdress() {
        return serverIPAdress;
    }

    void setServerIPAdress(Inet4Address serverIPAdress) {
        this.serverIPAdress = serverIPAdress;
    }

    public void setupClient() {
        if (serverIPAdress == null || port < 1)
            return;
        
        try {
            thisClient = Network.connectToServer(serverIPAdress.getHostAddress(), port);
            thisClient.start();
            thisClient.addMessageListener(new ClientListener(), StringMessage.class);
            StringMessage s = new StringMessage("Hello server!");
            thisClient.send(s);
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        Serializer.registerClass(StringMessage.class);
    }
    
    public void setupServer() {
        thisServer = SolarWarsServer.getInstance();
        thisServer.start();
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
}
