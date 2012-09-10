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
 * File: BasePlanet.java
 * Type: com.solarwars.entities.BasePlanet
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.solarwars.logic.Level;

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
     * @param sizeID the size id
     */
    public BasePlanet(AssetManager assetManager, 
            Level level, Vector3f position, int sizeID) {
        super(assetManager, level, position, sizeID);
        this.range = 3.0f;
    }

    /* (non-Javadoc)
     * @see com.solarwars.entities.AbstractPlanet#createPlanet()
     */
    @Override
    public void createPlanet() {
        //<editor-fold defaultstate="collapsed" desc="OLD GEOMENTRY CREATION">
        //Geometry
        
        /*
         * Sphere s = new Sphere(SPHERE_Z_SAMPLES, SPHERE_RADIAL_SAMPLES, size);
         * 
         * s.setTextureMode(Sphere.TextureMode.Projected);
         * TangentBinormalGenerator.generate(s);
         * geometry = new Geometry("BasePlanet_" + id, s);
         * //geometry.setLocalRotation(new Quaternion(angles));
         * 
         * //Material
         * material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
         * //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         * material.setBoolean("UseMaterialColors", true);
         * material.setColor("Specular", new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));//new ColorRGBA(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f));
         * material.setTexture("DiffuseMap",
         * assetManager.loadTexture("Textures/Planets/noise.png"));
         * material.setColor("Diffuse", new ColorRGBA(0.25f, 0.25f, 0.25f, 1.0f));
         * 
         * if (material.getMaterialDef().getName().equals("Phong Lighting")
         * && SolarWarsApplication.TOON_ENABLED) {
         * Texture t = assetManager.loadTexture("Textures/Shader/toon.png");
         * //t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
         * //t.setMagFilter(Texture.MagFilter.Nearest);
         * material.setTexture("ColorRamp", t);
         * 
         * material.setBoolean("VertexLighting", true);
         * }
         * 
         */
        //</editor-fold>
        geometry = new Geometry("BasePlanet_" + id, new Quad(size*2,size*2));
        
        float rot=(float)Math.random()*(float)Math.PI*2.0f;
        float angles[] = {
            (float) -Math.PI / 2, rot,0
        };

        geometry.setLocalTranslation(size*(-(float)Math.cos(-rot)-(float)Math.sin(-rot)),0,
                                     size*((float)Math.cos(-rot)-(float)Math.sin(-rot)));
        geometry.setLocalRotation(new Quaternion(angles));
        
        material = new Material(assetManager, "Shaders/planet.j3md");
        
        Texture surface = assetManager.loadTexture("Textures/Planets/planet-surface.png");
        surface.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ColorMap",surface);
        
        Texture transform=assetManager.loadTexture("Textures/Planets/planet-transform.png");
        transform.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("TransformMap",transform);

        Texture halo=assetManager.loadTexture("Textures/Planets/planet-halo.png");
        transform.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("HaloMap",halo);

        material.setColor("Color",new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        
        material.setFloat("VerticalShift",(float)Math.random());
        
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        material.getAdditionalRenderState().setAlphaTest(true);
        material.getAdditionalRenderState().setAlphaFallOff(0.1f);
        //material.getAdditionalRenderState().setDepthWrite(false);

        geometry.setMaterial(material);
        transformNode.attachChild(geometry);
    }
}
