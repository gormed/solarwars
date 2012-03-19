/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author Hans
 */
public class ServerListener implements MessageListener<HostedConnection> {

    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof StringMessage) {
            // do something with the message
            StringMessage stringMessage = (StringMessage) message;
            System.out.println(
                    "Server received '" + 
                    stringMessage.getMessage() + 
                    "' from client #" + source.getId());
        }
    }
}
