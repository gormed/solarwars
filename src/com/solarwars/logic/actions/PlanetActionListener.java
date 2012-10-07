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
 * File: PlanetActionListener.java
 * Type: com.solarwars.logic.PlanetActionListener
 * 
 * Documentation created: 24.09.2012 - 02:41:41 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.actions;

import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Player;

/**
 * The class PlanetActionListener.
 *
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public interface PlanetActionListener {

    public void onPlanetAction(Object sender, AbstractPlanet planet, Player p,
            String actionName);
}
