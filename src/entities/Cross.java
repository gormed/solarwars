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
 * File: Cross.java
 * Type: entities.Cross
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;

/**
 * The Class Cross.
 */
public class Cross extends Node {

    /**
     * Instantiates a new cross.
     *
     * @param assetManager the asset manager
     */
    public Cross(AssetManager assetManager) {
        Vector3f zero = Vector3f.ZERO;
        Line x = new Line(zero, new Vector3f(1, 0, 0));
        Line y = new Line(zero, new Vector3f(0, 1, 0));
        Line z = new Line(zero, new Vector3f(0, 0, 1));
        
        Material m_x = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m_x.setColor("Color", ColorRGBA.Red);
        Material m_y = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m_y.setColor("Color", ColorRGBA.Blue);
        Material m_z = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m_z.setColor("Color", ColorRGBA.Green);
        
        Geometry g_x = new Geometry("X", x);
        g_x.setMaterial(m_x);
        g_x.setCullHint(CullHint.Never);
        Geometry g_y = new Geometry("Y", y);
        g_y.setMaterial(m_y);
        g_y.setCullHint(CullHint.Never);
        Geometry g_z = new Geometry("Z", z);
        g_z.setMaterial(m_z);
        g_z.setCullHint(CullHint.Never);

        this.attachChild(g_x);
        this.attachChild(g_y);
        this.attachChild(g_z);
    }
}
