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
 * File: ServerRegisterListener.java
 * Type: com.solarwars.net.ServerRegisterListener
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net;

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
