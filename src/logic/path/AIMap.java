/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.path;

import entities.AbstractPlanet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import logic.level.Level;

/**
 *
 * @author Hans
 */
public class AIMap {

    private HashMap<Integer, AINode> map;

    public AIMap() {
        map = new HashMap<Integer, AINode>();
    }

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
