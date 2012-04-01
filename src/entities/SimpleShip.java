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
 * File: SimpleShip.java
 * Type: entities.SimpleShip
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import logic.level.Level;
import logic.Player;

/**
 * The Class SimpleShip.
 */
@Serializable
public class SimpleShip extends AbstractShip {

    public SimpleShip() {
    }

    /**
     * Instantiates a new simple ship.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param p the p
     * @param g the g
     */
    public SimpleShip(AssetManager assetManager, Level level, Vector3f position, Player p, ShipGroup g) {
        super(assetManager, level, position, p, g);
    }

    /* (non-Javadoc)
     * @see entities.AbstractShip#createShip()
     */
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
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        Vector3f offset = new Vector3f(0, 0, -SHIP_SIZE / 2);

        geometry.setLocalTranslation(offset.x,
                offset.y, offset.z);
        geometry.setLocalRotation(new Quaternion(angles));

        //Cross c = new Cross(assetManager);
        //transformNode.attachChild(c);
        transformNode.attachChild(geometry);

        transformNode.setLocalTranslation(position);
    }
}
