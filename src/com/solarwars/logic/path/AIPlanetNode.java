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
 * File: AINode.java
 * Type: com.solarwars.logic.path.AINode
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.path;

import java.util.ArrayList;

import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Player;


/**
 * The Class AINode.
 */
public class AIPlanetNode {

    /** The planet. */
    private AbstractPlanet planet;
    
    /** The size. */
    private float size;
    
    /** The population. */
    private int population;
    
    /** The owner. */
    private Player owner;
    
    /** The edges. */
    private ArrayList<AIPlanetEdge> edges;

    /**
     * Instantiates a new aI node.
     *
     * @param planet the planet
     */
    public AIPlanetNode(AbstractPlanet planet) {
        this.planet = planet;
        this.owner = planet.getOwner();
        this.size = planet.getSize();
        this.population = planet.getShipCount();

    }

    /**
     * Connect planets.
     *
     * @param connectors the connectors
     */
    void connectPlanets(ArrayList<AIPlanetNode> connectors) {
        for (AIPlanetNode n : connectors) {
            createEdge(n);
        }
    }

    /**
     * Creates the edge.
     *
     * @param node the node
     */
    private void createEdge(AIPlanetNode node) {
        AIPlanetEdge e = new AIPlanetEdge(this, node);
        edges.add(e);

    }

    /**
     * Gets the planet.
     *
     * @return the planet
     */
    public AbstractPlanet getPlanet() {
        return planet;
    }

    public ArrayList<AIPlanetEdge> getEdges() {
        return new ArrayList<AIPlanetEdge>(edges);
    }
    
    
}
