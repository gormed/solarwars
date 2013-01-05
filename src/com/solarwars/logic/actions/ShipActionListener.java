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
 * File: ShipActionListener.java
 * Type: com.solarwars.logic.actions.ShipActionListener
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.actions;

import com.solarwars.entities.ShipGroup;
import com.solarwars.logic.Player;

/**
 * The class ShipActionListener.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public interface ShipActionListener {
    public void onShipAction(Object sender, ShipGroup shipGroup, Player p,
            String actionName);
}
