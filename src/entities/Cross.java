/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;

/**
 *
 * @author Hans
 */
public class Cross extends Node {

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
