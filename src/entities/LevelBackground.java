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
 * File: LevelBackground.java
 * Type: entities.LevelBackground
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;

import java.nio.ByteBuffer;
import java.util.Random;
import logic.FluidDynamics;
import solarwars.SolarWarsGame;

/**
 * The Class LevelBackground.
 */
public class LevelBackground extends Node {

    /** The Constant WIDTH. */
    public static final float WIDTH = 20;
    
    /** The Constant HEIGHT. */
    public static final float HEIGHT = 20;
    
    public static final int HEIGHTMAP_RES = 128;
    
    /** The geometry. */
    private Geometry geometry;
    
    /** The material. */
    private Material material;
    
    /** The game. */
    private SolarWarsGame game;

    /**
     * Instantiates a new level background.
     *
     * @param game the game
     */
    public LevelBackground(SolarWarsGame game, int seed) {
        super("LevelBackground");
        this.game = game;
        createBG(seed);
    }

    /**
     * Creates the bg.
     */
    private void createBG(int seed) {
      
        /*AssetManager assetManager = game.getApplication().getAssetManager();
        
        Quad q = new Quad(WIDTH, HEIGHT);
        geometry = new Geometry("BackgroundGeometry", q);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Enviorment/stars.png"));


        geometry.setMaterial(material);

        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        geometry.setLocalRotation(new Quaternion(angles));
        geometry.setLocalTranslation(-WIDTH/2, -2, -HEIGHT/2);
        
        attachChild(geometry);*/
        
        // im placing this here for testing purposes
        // dont know if right position - would class Level be better?
        // Roman
        
        FluidDynamics fd = new FluidDynamics();
        
        fd.init();
        
        fd.start(seed);
        
        int i,j,k;
        
	for (i=0;i<5;i++)
	{
	    for (j=0;j<10-i;j++) fd.push();
	    fd.simulate();
	}
	for (i=0;i<10;i++)
	{
	    fd.simulate();
	}

        ByteBuffer data=fd.createtexture();
        Image img=new Image(Image.Format.RGB8,fd.FLUID_RES,fd.FLUID_RES,data,null);
        Texture tex=new Texture2D();
        tex.setImage(img);

        AssetManager assetManager = game.getApplication().getAssetManager();
        
        Vector3f vertices[] = new Vector3f[(HEIGHTMAP_RES+1)*(HEIGHTMAP_RES+1)];
        Vector2f texcoords[] = new Vector2f[(HEIGHTMAP_RES+1)*(HEIGHTMAP_RES+1)];
        int indices[]=new int[HEIGHTMAP_RES*HEIGHTMAP_RES*6];
        
        k=0;
        for(i=0;i<=HEIGHTMAP_RES;i++)
        for(j=0;j<=HEIGHTMAP_RES;j++)
        {
            vertices[k]=new Vector3f();
            vertices[k].x=j*WIDTH/HEIGHTMAP_RES;
            vertices[k].y=i*WIDTH/HEIGHTMAP_RES;
            vertices[k].z=fd.densityat((float)j/HEIGHTMAP_RES,(float)i/HEIGHTMAP_RES);
            
            texcoords[k]=new Vector2f();
            texcoords[k].x=(float)j/HEIGHTMAP_RES;
            texcoords[k].y=(float)i/HEIGHTMAP_RES;
            
            k++;
        }
        
        k=0;
        for(i=0;i<HEIGHTMAP_RES;i++)
        for(j=0;j<HEIGHTMAP_RES;j++)
        {
            indices[k*6+0]=i*(HEIGHTMAP_RES+1)+j;
            indices[k*6+1]=i*(HEIGHTMAP_RES+1)+j+1;
            indices[k*6+2]=(i+1)*(HEIGHTMAP_RES+1)+j;

            indices[k*6+3]=i*(HEIGHTMAP_RES+1)+j+1;
            indices[k*6+4]=(i+1)*(HEIGHTMAP_RES+1)+j+1;
            indices[k*6+5]=(i+1)*(HEIGHTMAP_RES+1)+j;
            
            k++;
        }
        
        Mesh mesh = new Mesh();
        
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texcoords));
        mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();
        
        //Quad q = new Quad(WIDTH, HEIGHT);
        geometry = new Geometry("BackgroundGeometry", mesh);

        material = new Material(assetManager, "Shaders/scramble.j3md");
        
        //material.setTexture("ColorMap", assetManager.loadTexture("Textures/Enviorment/stars.png"));
        tex.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ColorMap",tex);
        
        Texture sct=assetManager.loadTexture("Textures/Effects/scramble.png");
        sct.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ScrambleMap",sct);


        geometry.setMaterial(material);

        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        geometry.setLocalRotation(new Quaternion(angles));
        geometry.setLocalTranslation(-WIDTH/2, -2, -HEIGHT/2);
        

        attachChild(geometry);
        
        
        
        float starx[]=fd.getStarXArray();
        float stary[]=fd.getStarYArray();
        int nstars=starx.length;

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Environment/smallstar.png"));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        material.getAdditionalRenderState().setDepthWrite(false);
    
        Random rgen=new Random(123);
        for (i=0;i<nstars;i++)
        {
            float s=rgen.nextFloat()*0.3f;
            geometry=new Geometry("BackgroundStar"+i,new Quad(s,s));
            geometry.setMaterial(material);
            geometry.setLocalRotation(new Quaternion(angles));
            geometry.setLocalTranslation(starx[i]*WIDTH/fd.FLUID_RES-WIDTH/2,-1,stary[i]*HEIGHT/fd.FLUID_RES-HEIGHT/2);
            geometry.setQueueBucket(Bucket.Transparent);
            this.attachChild(geometry);
        }
        
    }
}
