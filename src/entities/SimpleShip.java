/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import level.Level;
import logic.Player;

/**
 *
 * @author Hans
 */
public class SimpleShip extends AbstractShip {

    public SimpleShip(AssetManager assetManager, Level level, Vector3f position, Player p) {
        super(assetManager, level, position, p);
    }

    @Override
    public void createShip() {
        Quad q = new Quad(SHIP_SIZE, SHIP_SIZE);
        geometry = new Geometry("SimpleShip " + id, q);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Ships/ship.png"));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);


        geometry.setMaterial(material);

        /** Objects with transparency need to be in 
         * the render bucket for transparent objects: */
        geometry.setQueueBucket(Bucket.Translucent);

        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI/2, 0
        };
        
        Vector3f offset = new Vector3f(0, 0, -SHIP_SIZE/2);

        geometry.setLocalTranslation(offset.x,
                offset.y, offset.z);
        geometry.setLocalRotation(new Quaternion(angles));

        //Cross c = new Cross(assetManager);
        //transformNode.attachChild(c);
        transformNode.attachChild(geometry);
        
        transformNode.setLocalTranslation(position);
    }
}
