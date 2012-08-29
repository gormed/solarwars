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
 * File: AIMap.java
 * Type: com.solarwars.logic.path.AIMap
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Level;


/**
 * The Class AIMap.
 */
public class AIMap {

    private HashMap<Integer, AINode> map;

    /**
     * Instantiates a new aI map.
     */
    public AIMap() {
        map = new HashMap<Integer, AINode>();
    }

    /**
     * Generates an new map.
     *
     * @param level the level
     */
    public void generateMap(Level level) {
        ArrayList<AINode> planets = new ArrayList<AINode>();
        
        for (Map.Entry<Integer, AbstractPlanet> entry : level.getPlanetSet()) {
            AbstractPlanet p = entry.getValue();
            AINode n = new AINode(p);
            map.put(p.getId(), n);
            planets.add(n);
        }


        for (Map.Entry<Integer, AINode> cursor : map.entrySet()) {
            cursor.getValue().connectPlanets(planets);
        }
        planets.clear();
        planets = null;
        
    }
}
