/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author Hans
 */
public class ClientListener implements MessageListener<Client> {

    public void messageReceived(Client source, Message message) {
        if (message instanceof StringMessage) {
            // do something with the message
            StringMessage stringMessage = (StringMessage) message;
            System.out.println("Client #" + source.getId() + " received: '" + stringMessage.getMessage() + "'");
        }
    }
}
