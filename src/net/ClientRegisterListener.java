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
 * File: ClientRegisterListener.java
 * Type: net.ClientRegisterListener
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import com.jme3.network.Client;

/**
 * The listener interface for receiving clientRegister events.
 * The class that is interested in processing a clientRegister
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addClientRegisterListener<code> method. When
 * the clientRegister event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ClientRegisterEvent
 */
public interface ClientRegisterListener {
        
    /**
     * Register client listener.
     *
     * @param client the client
     */
    public void registerClientListener(Client client);
    
}
