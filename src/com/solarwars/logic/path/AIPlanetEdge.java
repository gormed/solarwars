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
 * File: AIEdge.java
 * Type: com.solarwars.logic.path.AIEdge
 * 
 * Documentation created: 14.07.2012 - 19:38:03 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.path;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * The Class AIEdge.
 */
public class AIPlanetEdge {

    private AIPlanetNode from;
    private AIPlanetNode to;
    private float length;  //TODO Matthias 
    private float angle;

    /**
     * Instantiates a new aI edge.
     *
     * @param from the from
     * @param to the to
     */
    public AIPlanetEdge(AIPlanetNode from, AIPlanetNode to) {
        this.from = from;
        this.to = to;
        calculateLength();
        calculateAngle();
    }

    /**
     * Calculate length.
     */
    private void calculateLength() {
        length = to.getPlanet().getPosition().distance(from.getPlanet().getPosition());
    }

    private void calculateAngle() {
        Vector2f from2d = new Vector2f(
                from.getPlanet().getPosition().x,
                from.getPlanet().getPosition().z);
        Vector2f to2d = new Vector2f(
                to.getPlanet().getPosition().x,
                to.getPlanet().getPosition().z);
        angle = from2d.angleBetween(to2d);
    }

    public float getAngle() {
        return angle;
    }

    public float getLength() {
        return length;
    }

    public AIPlanetNode getFrom() {
        return from;
    }

    public AIPlanetNode getTo() {
        return to;
    }
}
