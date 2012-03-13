/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.path;

import entities.AbstractPlanet;
import java.util.ArrayList;
import logic.Player;

/**
 *
 * @author Hans
 */
public class AINode {

    private AbstractPlanet planet;
    private float size;
    private int population;
    private Player owner;
    private ArrayList<AIEdge> edges;

    public AINode(AbstractPlanet planet) {
        this.planet = planet;
        this.owner = planet.getOwner();
        this.size = planet.getSize();
        this.population = planet.getShips();

    }

    void connectPlanets(ArrayList<AINode> connectors) {
        for (AINode n : connectors) {
            createEdge(n);
        }
    }

    private void createEdge(AINode node) {
        AIEdge e = new AIEdge(this, node);
        edges.add(e);

    }

    AbstractPlanet getPlanet() {
        return planet;
    }
}
