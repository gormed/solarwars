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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: ServerRegisterListener.java
 * Type: net.ServerRegisterListener
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package net;

import com.jme3.network.Server;

/**
 * The listener interface for receiving serverRegister events.
 * The class that is interested in processing a serverRegister
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addServerRegisterListener<code> method. When
 * the serverRegister event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ServerRegisterEvent
 */
public interface ServerRegisterListener {
    
    /**
     * Register server listener.
     *
     * @param server the server
     */
    public void registerServerListener(Server server);
    
}
