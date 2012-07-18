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
 * File: MultiplayerGameplay.java
 * Type: logic.MultiplayerGameplay
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import entities.AbstractPlanet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import net.NetworkManager;
import net.messages.GeneralActionMessage;
import net.messages.LevelActionMessage;
import net.messages.PlanetActionMessage;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class MultiplayerGameplay.
 *
 * @author Hans
 */
public class MultiplayerGameplay {

    public static final boolean MULTIPLAYER_GAMEPLAY_DEBUG = true;
    /** The instance. */
    private static MultiplayerGameplay instance;

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
    private ClientGameplayListener gameplayListener = new ClientGameplayListener();
    /** The action lib. */
    private ActionLib actionLib = ActionLib.getInstance();
    /** The recieved messages. */
    private volatile Queue<Message> recievedMessages = new LinkedList<Message>();

    /**
     * Send planet action message.
     *
     * @param actionName the action name
     * @param planet the planet
     */
    public void sendPlanetActionMessage(String actionName, AbstractPlanet planet) {
        if (client == null || !client.isConnected()) {
            return;
        }
        int id = -1;
        int ships = 0;
        if (planet != null) {
            id = planet.getId();
            ships = planet.getShipCount();
        }
        PlanetActionMessage planetActionMessage =
                new PlanetActionMessage(
                System.currentTimeMillis(),
                actionName,
                Hub.getLocalPlayer().getId(),
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
        if (client == null || !client.isConnected()) {
            return;
        }
        GeneralActionMessage generalActionMessage;
        if (sender == null) {
            generalActionMessage = new GeneralActionMessage(
                    actionName,
                    -1,
                    null,
                    reciever.getId(),
                    reciever.getState());
        } else {

            generalActionMessage = new GeneralActionMessage(
                    actionName,
                    sender.getId(),
                    sender.getState(),
                    reciever.getId(),
                    reciever.getState());
        }
        client.send(generalActionMessage);
    }

    /**
     * Updates the MultiplayerGameplay by polling the next network message from the queque.
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
                        Gameplay.getCurrentLevel().
                        getPlanet(serverMessage.getPlanetID());
                //TODO: Clean up the mess, nobody needs this anymore!
                long sendTime = serverMessage.getClientTime();
                long serverTime = serverMessage.getServerTime();
                long currentTime = System.currentTimeMillis();
                long delay = currentTime - serverTime;

                actionLib.invokePlanetAction(
                        MultiplayerGameplay.getInstance(),
                        delay,
                        planet,
                        p,
                        serverMessage.getActionName());



                if (MULTIPLAYER_GAMEPLAY_DEBUG) {
                    System.out.println("ID#" + serverMessage.getPlayerID()
                            + "/" + serverMessage.getPlayerState().name
                            + " sent a " + serverMessage.getActionName()
                            + " with delay " + delay);
                }
            } else if (m instanceof GeneralActionMessage) {
                GeneralActionMessage serverMessage = (GeneralActionMessage) m;


            } else if (m instanceof LevelActionMessage) {
                LevelActionMessage actionMessage = (LevelActionMessage) m;

//                for (Map.Entry<Integer, Integer> entry : actionMessage.getPlanetShipCount().entrySet()) {
//                    int id = entry.getKey();
//                    int shipCount = entry.getValue();
//                    Gameplay.getCurrentLevel().getPlanet(id).setShipCount(shipCount);
//                }
                long currentTime = System.currentTimeMillis();
                long delay = currentTime - actionMessage.getServerTime();
                SolarWarsApplication.getInstance().syncronize(delay * .001f);
            }
            synchronized (recievedMessages) {
                recievedMessages.remove(m);
            }
        }
    }

    /**
     * Destroys the gameplay on exit.
     */
    public void destroy() {
        synchronized (recievedMessages) {
            recievedMessages.clear();
            recievedMessages = null;
        }
        if (client != null) {
            client.removeMessageListener(
                    gameplayListener,
                    PlanetActionMessage.class,
                    GeneralActionMessage.class);
        }
        gameplayListener = null;

        instance = null;

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
