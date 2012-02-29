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
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
import java.util.Random;
import level.Level;

/**
 *
 * @author Hans
 */
public class BasePlanet extends AbstractPlanet {

    public BasePlanet(Level level, Vector3f position, float size) {
        super(level, position, size);
    }
    
    @Override
    public void createPlanet(AssetManager assetManager) {
        //Geometry
        
        Sphere s = new Sphere(SPHERE_Z_SAMPLES, SPHERE_RADIAL_SAMPLES, size);
        s.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(s);
        geometry = new Geometry("BasePlanet_" + id, s);
        
        //Material
        
        Random r = new Random(System.currentTimeMillis());
        
        Material mat_lit = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_lit.setBoolean("UseMaterialColors",true); 
        mat_lit.setColor("Specular", new ColorRGBA(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f));
        mat_lit.setColor("Diffuse", new ColorRGBA(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f));
        geometry.setMaterial(mat_lit);
        
        transformNode.attachChild(geometry);
    }
    
}
