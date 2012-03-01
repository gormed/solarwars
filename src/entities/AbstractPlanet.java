/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import level.Level;

/**
 *
 * @author Hans
 */
public abstract class AbstractPlanet {

    public static final int SPHERE_Z_SAMPLES = 20;
    public static final int SPHERE_RADIAL_SAMPLES = 20;
    
    private static int PLANET_ID = 0;
    
    private static int getContiniousID() {
        return PLANET_ID++;
    }
    
    protected Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }
    protected Node transformNode;
    protected float size;
    protected Level level;
    protected int id;
    protected Vector3f position;

    public AbstractPlanet(Level level, Vector3f position, float size) {
        this.id = getContiniousID();
        this.size = size;
        this.level = level;
        this.position = position;
        this.transformNode = new Node();
        this.transformNode.setLocalTranslation(position);
        
        level.getLevelNode().attachChild(transformNode);
        
    }
    
    public abstract void createPlanet(AssetManager assetManager);
}
