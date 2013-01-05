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
 * File: ClientRegisterListener.java
 * Type: com.solarwars.net.ClientRegisterListener
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.net;

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
