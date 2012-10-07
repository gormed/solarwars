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
 * File: ShipActionListener.java
 * Type: com.solarwars.logic.ShipActionListener
 * 
 * Documentation created: 24.09.2012 - 02:46:38 by Hans Ferchland <hans.ferchland at gmx.de>
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
