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
 * File: AIEdge.java
 * Type: logic.path.AIEdge
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.path;

import com.jme3.math.Vector3f;

/**
 * The Class AIEdge.
 */
public class AIEdge {
    
    /** The from. */
    private AINode from;
    
    /** The to. */
    private AINode to;
    
    /** The length. */
    private float length;

    /**
     * Instantiates a new aI edge.
     *
     * @param from the from
     * @param to the to
     */
    public AIEdge(AINode from, AINode to) {
        this.from = from;
        this.to = to;
        calculateLength();
    }
    
    /**
     * Calculate length.
     */
    private void calculateLength() {
        Vector3f l;
        
        l = to.getPlanet().getPosition().subtract(from.getPlanet().getPosition());
        
        length = l.length();
    }
}
