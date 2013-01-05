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
 * File: PlanetAction.java
 * Type: com.solarwars.logic.actions.PlanetAction
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.actions;

import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Player;

/**
 * The Class PlanetAction.
 */
public abstract class PlanetAction {

    private final String name;

    public String getName() {
        return name;
    }

    public PlanetAction(String name) {
        this.name = name;
    }

    //public abstract void doAction(Object sender, AbstractPlanet planet, Player p);

   
    public abstract boolean doAction(Object sender, AbstractPlanet planet, Player p);
}
