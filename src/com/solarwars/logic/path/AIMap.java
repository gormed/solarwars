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
 * File: AIMap.java
 * Type: com.solarwars.logic.path.AIMap
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
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

    public static AIMap instance;
    public static final boolean DEBUG_MAP = true;
    public static final boolean DEBUG_MAP_PLANET = true;
    public static final boolean DEBUG_MAP_EDGES = false;
    private HashMap<Integer, AIPlanetNode> map;
    private Node debugNode;
    private Node planetDebugNode;

    /**
     * Instantiates a new aI map.
     */
    private AIMap() {
        map = new HashMap<Integer, AIPlanetNode>();
        debugNode = new Node("AIMap Debug Node");
        planetDebugNode = new Node();
        debugNode.attachChild(planetDebugNode);
        // TODO Hans Debugging?
        enabelDebugMode(DEBUG_MAP);
        SolarWarsApplication.getInstance().getRootNode().attachChild(debugNode);
    }

    public static AIMap getInstance() {
        if (instance == null) {
            return instance = new AIMap();
        }
        return instance;
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
            planetDebugNode.attachChild(n.debugNode);
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
