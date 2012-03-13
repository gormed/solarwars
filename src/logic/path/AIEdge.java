/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.path;

import com.jme3.math.Vector3f;

/**
 *
 * @author Hans
 */
public class AIEdge {
    
    private AINode from;
    private AINode to;
    
    private float length;

    public AIEdge(AINode from, AINode to) {
        this.from = from;
        this.to = to;
        calculateLength();
    }
    
    private void calculateLength() {
        Vector3f l;
        
        l = to.getPlanet().getPosition().subtract(from.getPlanet().getPosition());
        
        length = l.length();
    }
}
