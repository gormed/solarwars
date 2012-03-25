/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: AIMap.java
 * Type: logic.path.AIMap
 * 
 * Documentation created: 15.03.2012 - 20:36:19 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.path;

import entities.AbstractPlanet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import logic.level.Level;

/**
 * The Class AIMap.
 */
public class AIMap {

    /** The map. */
    private HashMap<Integer, AINode> map;

    /**
     * Instantiates a new aI map.
     */
    public AIMap() {
        map = new HashMap<Integer, AINode>();
    }

    /**
     * Generate map.
     *
     * @param level the level
     */
    public void generateMap(Level level) {
        Iterator<AbstractPlanet> it = level.getPlanetIterator();
        ArrayList<AINode> planets = new ArrayList<AINode>();
        
        while (it.hasNext()) {
            AbstractPlanet p = it.next();
            AINode n = new AINode(p);
            map.put(p.getId(), n);
            planets.add(n);
        }

        for (Map.Entry<Integer, AINode> cursor : map.entrySet()) {
            cursor.getValue().connectPlanets(planets);
        }
        planets.clear();
        planets = null;
        it = null;
    }
}