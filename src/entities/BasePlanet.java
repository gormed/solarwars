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
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import logic.level.Level;

/**
 *
 * @author Hans
 */
public class BasePlanet extends AbstractPlanet {

    public BasePlanet(AssetManager assetManager, Level level, Vector3f position, float size) {
        super(assetManager, level, position, size);
    }

    @Override
    public void createPlanet() {
        //Geometry

        Sphere s = new Sphere(SPHERE_Z_SAMPLES, SPHERE_RADIAL_SAMPLES, size);

        s.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(s);
        geometry = new Geometry("BasePlanet_" + id, s);

        //Material
        material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Specular", new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));//new ColorRGBA(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f));
        geometry.setMaterial(material);

        if (material.getMaterialDef().getName().equals("Phong Lighting")) {
            Texture t = assetManager.loadTexture("Textures/Shader/toon.png");
            //t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            //t.setMagFilter(Texture.MagFilter.Nearest);
            material.setTexture("ColorRamp", t);
            material.setColor("Diffuse", new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
            material.setBoolean("VertexLighting", true);
        }

        transformNode.attachChild(geometry);
    }
}
