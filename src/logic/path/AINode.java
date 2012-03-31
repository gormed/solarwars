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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: AINode.java
 * Type: logic.path.AINode
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.path;

import entities.AbstractPlanet;
import java.util.ArrayList;
import logic.Player;

/**
 * The Class AINode.
 */
public class AINode {

    /** The planet. */
    private AbstractPlanet planet;
    
    /** The size. */
    private float size;
    
    /** The population. */
    private int population;
    
    /** The owner. */
    private Player owner;
    
    /** The edges. */
    private ArrayList<AIEdge> edges;

    /**
     * Instantiates a new aI node.
     *
     * @param planet the planet
     */
    public AINode(AbstractPlanet planet) {
        this.planet = planet;
        this.owner = planet.getOwner();
        this.size = planet.getSize();
        this.population = planet.getShips();

    }

    /**
     * Connect planets.
     *
     * @param connectors the connectors
     */
    void connectPlanets(ArrayList<AINode> connectors) {
        for (AINode n : connectors) {
            createEdge(n);
        }
    }

    /**
     * Creates the edge.
     *
     * @param node the node
     */
    private void createEdge(AINode node) {
        AIEdge e = new AIEdge(this, node);
        edges.add(e);

    }

    /**
     * Gets the planet.
     *
     * @return the planet
     */
    AbstractPlanet getPlanet() {
        return planet;
    }
}
