/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: MultiplayerGameplay.java
 * Type: com.solarwars.logic.MultiplayerGameplay
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.messages.GeneralActionMessage;
import com.solarwars.net.messages.LevelActionMessage;
import com.solarwars.net.messages.PlanetActionMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The Class MultiplayerGameplay.
 *
 * @author Hans
 */
public class MultiplayerGameplay {

    public static final boolean MULTIPLAYER_GAMEPLAY_DEBUG = false;
    /** The instance. */
    private static MultiplayerGameplay instance;
    private static Level currentLevel;

    /**
     * Instantiates a new multiplayer gameplay.
     */
    private MultiplayerGameplay() {
        client = NetworkManager.getInstance().getThisClient();
        client.addMessageListener(
                gameplayListener,
                PlanetActionMessage.class,
                GeneralActionMessage.class,
                LevelActionMessage.class);

        currentLevel = SolarWarsGame.getInstance().getCurrentLevel();

    }

    /**
     * Gets the single instance of MultiplayerGameplay.
     *
     * @return single instance of MultiplayerGameplay
     */
    public static MultiplayerGameplay getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new MultiplayerGameplay();
        }
    }

    /**
     * Checks if is initialized.
     *
     * @return true, if is initialized
     */
    public static boolean isInitialized() {
        return instance != null;
    }
    /** The client. */
    private Client client;
    /** The gameplay listener. */
    private ClientGameplayListener gameplayListener =
            new ClientGameplayListener();
    /** The action lib. */
    private ActionLib actionLib = ActionLib.getInstance();
    /** The recieved messages. */
    private final Queue<Message> recievedMessages =
            new LinkedList<Message>();
    private double currentTickDiff = 0;
    private double lastTickDiff = 0;

    /**
     * Destroys the gameplay on exit.
     */
    public void destroy() {
        synchronized (recievedMessages) {
            recievedMessages.clear();
        }
        removeGameplayListener();
        gameplayListener = null;

        instance = null;

    }

    public void addGameplayListener() {
        if (client != null && gameplayListener != null) {
            client.addMessageListener(
                    gameplayListener,
                    PlanetActionMessage.class,
                    GeneralActionMessage.class);
        }
    }

    public void removeGameplayListener() {
        if (client != null && gameplayListener != null) {
            client.removeMessageListener(
                    gameplayListener,
                    PlanetActionMessage.class,
                    GeneralActionMessage.class);
        }
    }
    //==========================================================================
    //          SEND MESSAGES OVER NETWORK TO OTHER PLAYERS
    //==========================================================================

    /**
     * Checks if game still needs to send messages to other players via network.
     * @return true if still running a game, false otherwise
     */
    private boolean gameIsNotRunning() {
        return client == null
                || !client.isConnected()
                || currentLevel.isGameOver();
    }

    /**
     * Send planet action message.
     *
     * @param actionName the action name
     * @param planet the planet
     */
    public void sendPlanetActionMessage(String actionName, AbstractPlanet planet) {
        if (gameIsNotRunning()) {
            return;
        }
        int id = -1;
        int ships = 0;
        if (planet != null) {
            id = planet.getID();
            ships = planet.getShipCount();
        }
        PlanetActionMessage planetActionMessage =
                new PlanetActionMessage(
                System.currentTimeMillis(),
                actionName,
                Hub.getLocalPlayer().getID(),
                Hub.getLocalPlayer().getState(),
                id);
        client.send(planetActionMessage);
    }

    /**
     * Send general action message.
     *
     * @param actionName the action name
     * @param sender the sender
     * @param reciever the reciever
     */
    public void sendGeneralActionMessage(String actionName, Player sender, Player reciever) {
        if (gameIsNotRunning()) {
            return;
        }
        GeneralActionMessage generalActionMessage;
        if (sender == null) {
            generalActionMessage = new GeneralActionMessage(
                    actionName,
                    -1,
                    null,
                    reciever.getID(),
                    reciever.getState());
        } else {

            generalActionMessage = new GeneralActionMessage(
                    actionName,
                    sender.getID(),
                    sender.getState(),
                    reciever.getID(),
                    reciever.getState());
        }
        client.send(generalActionMessage);
    }

    //==========================================================================
    //          RECIEVE MESSAGES FROM OTHER OVER NETWORK
    //==========================================================================
    /**
     * Updates the MultiplayerGameplay by polling the next network message 
     * from the queque.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        Queue<Message> clone = null;
        synchronized (recievedMessages) {
            clone = new LinkedList<Message>(recievedMessages);
        }
        while (clone != null && !clone.isEmpty()) {
            Message m = clone.poll();

            if (m instanceof PlanetActionMessage) {
                PlanetActionMessage serverMessage = (PlanetActionMessage) m;

                Player p = Hub.getInstance().getPlayer(serverMessage.getPlayerID());
                p.applyState(serverMessage.getPlayerState());
                AbstractPlanet planet =
                        currentLevel.getPlanet(serverMessage.getPlanetID());

                actionLib.invokePlanetAction(
                        MultiplayerGameplay.getInstance(),
                        planet,
                        p,
                        serverMessage.getActionName());
            } else if (m instanceof GeneralActionMessage) {
                GeneralActionMessage serverMessage = (GeneralActionMessage) m;

//                Player a = Hub.getPlayers().get(serverMessage.getSender());
//                Player b = Hub.getPlayers().get(serverMessage.getReciever());
//                a.applyState(serverMessage.getSenderState());
//                b.applyState(serverMessage.getRecieverState());
//                
//                actionLib.invokeGeneralAction(
//                        MultiplayerGameplay.getInstance(), 
//                        a, b, 
//                        serverMessage.getActionName());
            } else if (m instanceof LevelActionMessage) {
                LevelActionMessage actionMessage = (LevelActionMessage) m;
                syncronizeClient(actionMessage);
            }
            synchronized (recievedMessages) {
                recievedMessages.remove(m);
            }
        }
    }

    /**
     * Syncs the client with the servers action message. Gives the client the
     * current game tick from the server and calculates the message delay and 
     * average delay of the client to the server.
     * @param actionMessage 
     */
    private void syncronizeClient(LevelActionMessage actionMessage) {
        double tick = DeathmatchGameplay.getGameTick();
        double serverTick = actionMessage.getGameTick();
        double tickDiff = tick - serverTick;
        currentTickDiff = tickDiff;
        double tickDelay = currentTickDiff - lastTickDiff;
        long currentTime = System.currentTimeMillis();
        long delay = currentTime - actionMessage.getServerTime();

        if (MULTIPLAYER_GAMEPLAY_DEBUG) {
            System.out.println(
                    currentTime
                    + " - delay: " + delay
                    + " - tickDelay: " + String.format("%1.4f", (float) tickDelay)
                    + " - tickDiff: " + String.format("%1.3f", (float) tickDiff));
        }
        SolarWarsApplication.getInstance().syncronize((float) (tickDelay));
        DeathmatchGameplay.GAMETICK += (currentTickDiff - lastTickDiff) * 0.5f;
        lastTickDiff = currentTickDiff;
    }

    /**
     * The listener interface for receiving clientGameplay events.
     * The class that is interested in processing a clientGameplay
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addClientGameplayListener<code> method. When
     * the clientGameplay event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ClientGameplayEvent
     */
    private class ClientGameplayListener implements MessageListener<Client> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        @Override
        public void messageReceived(Client source, Message message) {
//            System.out.println(
//                    "Client #" + source.getId() + " recieved a "
//                    + message.getClass().getSimpleName());
            if (recievedMessages != null) {
                synchronized (recievedMessages) {
                    recievedMessages.add(message);
                }
            }
        }
    }
}
