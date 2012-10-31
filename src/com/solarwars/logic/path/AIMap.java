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

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.solarwars.SolarWarsApplication;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AIMap.
 */
public class AIMap {

    private HashMap<Integer, AIPlanetNode> map;
    private Node debugNode;

    /**
     * Instantiates a new aI map.
     */
    public AIMap() {
        map = new HashMap<Integer, AIPlanetNode>();
        debugNode = new Node("AIMap Debug Node");
        // TODO Hans Debugging?
        enabelDebugMode(false);
        SolarWarsApplication.getInstance().getRootNode().attachChild(debugNode);
    }

    /**
     * Generates an new map.
     *
     * @param level the level
     */
    public void generateMap(Level level) {
        if (level == null) {
            return;
        }

        ArrayList<AIPlanetNode> planets = new ArrayList<AIPlanetNode>();

        for (Map.Entry<Integer, AbstractPlanet> entry : level.getPlanetSet()) {
            AbstractPlanet p = entry.getValue();
            AIPlanetNode n = new AIPlanetNode(p);
            map.put(p.getID(), n);
            debugNode.attachChild(n.debugNode);
            planets.add(n);
        }


        for (Map.Entry<Integer, AIPlanetNode> cursor : map.entrySet()) {

            cursor.getValue().connectPlanets(planets);
        }
        planets.clear();

    }

    public AIPlanetNode find(AbstractPlanet planet) {
        return map.get(planet.getID());
    }

    public void enabelDebugMode(boolean value) {
        if (value) {
            debugNode.setCullHint(Spatial.CullHint.Never);
        } else {
            debugNode.setCullHint(Spatial.CullHint.Always);
        }
    }

    public void destroy() {
        for (Map.Entry<Integer, AIPlanetNode> cursor : map.entrySet()) {
            cursor.getValue().destroy();
        }
        map.clear();
        debugNode.detachAllChildren();
        SolarWarsApplication.getInstance().getRootNode().detachChild(debugNode);
    }
}
