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
 * File: BasePlanet.java
 * Type: entities.BasePlanet
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import logic.Level;
import logic.Player;

/**
 * The Class BasePlanet.
 */
public class BasePlanet extends AbstractPlanet {

    /**
     * Instantiates a new base planet.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param size the size
     */
    public BasePlanet(AssetManager assetManager, Level level, Vector3f position, int sizeID) {
        super(assetManager, level, position, sizeID);
    }

    /* (non-Javadoc)
     * @see entities.AbstractPlanet#createPlanet()
     */
    @Override
    public void createPlanet() {
        //Geometry
        Sphere s = new Sphere(SPHERE_Z_SAMPLES, SPHERE_RADIAL_SAMPLES, size);

        s.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(s);
        geometry = new Geometry("BasePlanet_" + id, s);
        //geometry.setLocalRotation(new Quaternion(angles));

        //Material
        material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Specular", new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));//new ColorRGBA(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f));

        if (material.getMaterialDef().getName().equals("Phong Lighting")) {
            Texture t = assetManager.loadTexture("Textures/Shader/toon.png");
            //t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            //t.setMagFilter(Texture.MagFilter.Nearest);
            material.setTexture("ColorRamp", t);
            material.setColor("Diffuse", new ColorRGBA(0.25f, 0.25f, 0.25f, 1.0f));
            material.setBoolean("VertexLighting", true);
        }

        geometry.setMaterial(material);
        transformNode.attachChild(geometry);
    }
}
