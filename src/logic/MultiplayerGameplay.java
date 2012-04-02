/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import entities.AbstractPlanet;
import java.util.LinkedList;
import java.util.Queue;
import net.NetworkManager;
import net.messages.GeneralActionMessage;
import net.messages.PlanetActionMessage;
import solarwars.Hub;

/**
 *
 * @author Hans
 */
public class MultiplayerGameplay {

    private static MultiplayerGameplay instance;

    private MultiplayerGameplay() {
        client = NetworkManager.getInstance().getThisClient();
        client.addMessageListener(
                gameplayListener,
                PlanetActionMessage.class,
                GeneralActionMessage.class);
    }

    public static MultiplayerGameplay getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new MultiplayerGameplay();
        }
    }

    public static boolean isInitialized() {
        return instance != null;
    }
    private Client client;
    private ClientGameplayListener gameplayListener = new ClientGameplayListener();
    private ActionLib actionLib = ActionLib.getInstance();
    private Queue<Message> recievedMessages = new LinkedList<Message>();

    public void sendPlanetActionMessage(String actionName, AbstractPlanet planet) {
        if (client == null) {
            return;
        }

        PlanetActionMessage planetActionMessage =
                new PlanetActionMessage(
                System.currentTimeMillis(),
                actionName,
                Hub.getLocalPlayer().getId(),
                Hub.getLocalPlayer().getState(),
                planet.getId());
        client.send(planetActionMessage);
    }

    public void sendGeneralActionMessage(String actionName, Player sender, Player reciever) {
        if (client == null) {
            return;
        }

        GeneralActionMessage generalActionMessage = new GeneralActionMessage(
                actionName,
                sender.getId(),
                sender.getState(),
                reciever.getId(),
                reciever.getState());

        client.send(generalActionMessage);
    }

    public void update(float tpf) {
        while (recievedMessages != null && !recievedMessages.isEmpty()) {
            Message m = recievedMessages.poll();

            if (m instanceof PlanetActionMessage) {
                PlanetActionMessage serverMessage = (PlanetActionMessage) m;

                Player p = Hub.getInstance().getPlayer(serverMessage.getPlayerID());
                p.applyState(serverMessage.getPlayerState());
                AbstractPlanet planet =
                        Gameplay.getCurrentLevel().
                        getPlanet(serverMessage.getPlanetID());
                actionLib.invokePlanetAction(
                        MultiplayerGameplay.getInstance(),
                        planet,
                        p,
                        serverMessage.getActionName());
            } else if (m instanceof GeneralActionMessage) {
                GeneralActionMessage serverMessage = (GeneralActionMessage) m;


            }
        }
    }

    public void destroy() {
        recievedMessages.clear();
        recievedMessages = null;
        
        if (client != null) {
            client.removeMessageListener(
                    gameplayListener,
                    PlanetActionMessage.class,
                    GeneralActionMessage.class);
        }
        gameplayListener = null;
        
        instance = null;
        
    }

    private class ClientGameplayListener implements MessageListener<Client> {

        public void messageReceived(Client source, Message message) {
            System.out.println(
                    "Client #" + source.getId() + " recieved a "
                    + message.getClass().getSimpleName());
            recievedMessages.add(message);
        }
    }
}
