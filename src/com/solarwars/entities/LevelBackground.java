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
 * Type: com.solarwars.entities.LevelBackground
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.entities;

import java.nio.ByteBuffer;


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
import com.solarwars.SolarWarsGame;
import com.solarwars.logic.FluidDynamics;
import com.solarwars.settings.SolarWarsSettings;

/**
 * The Class LevelBackground.
 */
public class LevelBackground extends Node {

    public static int BACKGROUND_QUALITY = SolarWarsSettings.getInstance().getBackgroundQuality();
    /** The Constant WIDTH. */
    public static final float WIDTH = 15;
    /** The Constant HEIGHT. */
    public static final float HEIGHT = 15;
    public static final int HEIGHTMAP_RES = 128;
    /** The geometry. */
    private Geometry geometry;
    /** Star geometry and animation params */
    private Geometry stargeo[];
    int nstars;
    float timeframe;
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

    public void update(float tpf) {
        /* animate the star field */
        int i;
        timeframe += tpf;

        if (BACKGROUND_QUALITY == 2) {
            material.setVector2("Shift", new Vector2f(timeframe * 0.01f, (float) Math.cos(timeframe * 0.01f)));
        }
        if (BACKGROUND_QUALITY >= 1) {
            for (i = 0; i < nstars; i++) {
                stargeo[i].setLocalScale(((float) Math.cos(timeframe + (float) i) * 0.5f + 0.5f) * 0.2f * ((float) i / (float) nstars));
            }
        }
    }

    /**
     * Creates the bg.
     */
    private void createBG(int seed) {
        BACKGROUND_QUALITY = SolarWarsSettings.getInstance().getBackgroundQuality();
        if (BACKGROUND_QUALITY == 0) {
            createMQContent(seed);
            for (int i = 0; i < nstars; i++) {
                stargeo[i].setLocalScale(((float) 
                        Math.cos(timeframe + (float) i) * 0.5f + 0.5f) * 
                        0.2f * ((float) i / (float) nstars));
            }
        } else if (BACKGROUND_QUALITY == 1) {
            createMQContent(seed);
        } else if (BACKGROUND_QUALITY == 2) {
            createHQContent(seed);
        }
    }

    private void createLQContent() {
        AssetManager assetManager = game.getApplication().getAssetManager();

        Quad q = new Quad(WIDTH, HEIGHT);
        geometry = new Geometry("BackgroundGeometry", q);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Enviorment/stars.png"));


        geometry.setMaterial(material);

        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        geometry.setLocalRotation(new Quaternion(angles));
        geometry.setLocalTranslation(-WIDTH / 2, -2, -HEIGHT / 2);

        attachChild(geometry);
    }

    private void createHQContent(int seed) {


        // im placing this here for testing purposes
        // dont know if right position - would class Level be better?
        // Roman

        /* run the fluid dynamics simulation */

        FluidDynamics fd = new FluidDynamics();

        fd.init();

        fd.start(seed);

        int i, j;

        for (i = 0; i < 5; i++) {
            for (j = 0; j < 7 - i; j++) {
                fd.push();  // less supernovas the further we are in time
            }
            fd.simulate();
        }
        for (i = 0; i < 10; i++) {
            fd.simulate();
        }

        /* get the simulation result and store into texture */

        ByteBuffer data = fd.createtexture();
        Image img = new Image(Image.Format.RGB8, FluidDynamics.FLUID_RES, FluidDynamics.FLUID_RES, data, null);
        Texture tex = new Texture2D();
        tex.setImage(img);

        AssetManager assetManager = game.getApplication().getAssetManager();
        /* NOTE: i do not think this is needed, when geometry is generated the right way */
        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        createNebula(fd, assetManager, tex, angles);
        createMQContent(fd, assetManager, angles);
    }

    private void createNebula(FluidDynamics fd, AssetManager assetManager, Texture tex, float[] angles) {
        int i;
        int j;
        /* build a heightmap for more 3d-looking fog */
        Vector3f vertices[] = new Vector3f[(HEIGHTMAP_RES + 1) * (HEIGHTMAP_RES + 1)];
        Vector2f texcoords[] = new Vector2f[(HEIGHTMAP_RES + 1) * (HEIGHTMAP_RES + 1)];
        int indices[] = new int[HEIGHTMAP_RES * HEIGHTMAP_RES * 6];
        int k = 0;
        for (i = 0; i <= HEIGHTMAP_RES; i++) {
            for (j = 0; j <= HEIGHTMAP_RES; j++) {
                vertices[k] = new Vector3f();
                vertices[k].x = j * WIDTH / HEIGHTMAP_RES;
                vertices[k].y = i * WIDTH / HEIGHTMAP_RES;
                vertices[k].z = fd.densityat((float) j / HEIGHTMAP_RES, (float) i / HEIGHTMAP_RES) * 0.5f;

                texcoords[k] = new Vector2f();
                texcoords[k].x = (float) j / HEIGHTMAP_RES;
                texcoords[k].y = (float) i / HEIGHTMAP_RES;

                k++;
            }
        }
        k = 0;
        for (i = 0; i < HEIGHTMAP_RES; i++) {
            for (j = 0; j < HEIGHTMAP_RES; j++) {
                indices[k * 6 + 0] = i * (HEIGHTMAP_RES + 1) + j;
                indices[k * 6 + 1] = i * (HEIGHTMAP_RES + 1) + j + 1;
                indices[k * 6 + 2] = (i + 1) * (HEIGHTMAP_RES + 1) + j;

                indices[k * 6 + 3] = i * (HEIGHTMAP_RES + 1) + j + 1;
                indices[k * 6 + 4] = (i + 1) * (HEIGHTMAP_RES + 1) + j + 1;
                indices[k * 6 + 5] = (i + 1) * (HEIGHTMAP_RES + 1) + j;

                k++;
            }
        }
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texcoords));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();
        geometry = new Geometry("BackgroundGeometry", mesh);
        /* generate material for displaying fog */
        /* the scramble shader will do a bit of 2d-displacement mapping */
        material = new Material(assetManager, "Shaders/scramble.j3md");
        tex.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ColorMap", tex);
        Texture sct = assetManager.loadTexture("Textures/Effects/scramble.png");
        sct.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ScrambleMap", sct);
        /* do not draw over level geometry */
        material.getAdditionalRenderState().setDepthWrite(false);
        geometry.setMaterial(material);
        geometry.setLocalRotation(new Quaternion(angles));

        geometry.setLocalTranslation(-WIDTH / 2, -0.5f, -HEIGHT / 2);


        attachChild(geometry);
    }

    private void createMQContent(FluidDynamics fd, AssetManager assetManager, float[] angles) {
        Vector3f[] vertices;
        Vector2f[] texcoords;
        int[] indices;
        Mesh mesh;
        int i;
        /* generate the stars */
        /* build nice 0-centered quad geometry,
        reuse old mesh object */
        vertices = new Vector3f[4];
        texcoords = new Vector2f[4];
        indices = new int[6];
        vertices[0] = new Vector3f(-1, -1, 0);
        vertices[1] = new Vector3f(1, -1, 0);
        vertices[2] = new Vector3f(-1, 1, 0);
        vertices[3] = new Vector3f(1, 1, 0);
        texcoords[0] = new Vector2f(0, 0);
        texcoords[1] = new Vector2f(1, 0);
        texcoords[2] = new Vector2f(0, 1);
        texcoords[3] = new Vector2f(1, 1);
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 1;
        indices[4] = 3;
        indices[5] = 2;
        mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texcoords));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();
        /* get star positions from fd simulation */
        float starx[] = fd.getStarXArray();
        float stary[] = fd.getStarYArray();
        nstars = starx.length;
        stargeo = new Geometry[nstars];
        /* star material */
        Material smaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        smaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/Environment/smallstar.png"));
        smaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        smaterial.getAdditionalRenderState().setDepthWrite(false);
        /* finally, create stars */
        for (i = 0; i < nstars; i++) {
            stargeo[i] = new Geometry("BackgroundStar" + i, mesh);
            stargeo[i].setMaterial(smaterial);
            stargeo[i].setLocalRotation(new Quaternion(angles));
            stargeo[i].setLocalTranslation(stary[i] * WIDTH / FluidDynamics.FLUID_RES - WIDTH / 2, -0.5f, starx[i] * HEIGHT / FluidDynamics.FLUID_RES - HEIGHT / 2);
            stargeo[i].setQueueBucket(Bucket.Transparent);
            this.attachChild(stargeo[i]);
        }
    }

    private void createMQContent(int seed) {
        // im placing this here for testing purposes
        // dont know if right position - would class Level be better?
        // Roman

        /* run the fluid dynamics simulation */

        FluidDynamics fd = new FluidDynamics();

        fd.init();

        fd.start(seed);

        int i, j;

        for (i = 0; i < 5; i++) {
            for (j = 0; j < 7 - i; j++) {
                fd.push();  // less supernovas the further we are in time
            }
            fd.simulate();
        }
        for (i = 0; i < 10; i++) {
            fd.simulate();
        }

        /* get the simulation result and store into texture */

        ByteBuffer data = fd.createtexture();
        Image img = new Image(Image.Format.RGB8, FluidDynamics.FLUID_RES, FluidDynamics.FLUID_RES, data, null);
        Texture tex = new Texture2D();
        tex.setImage(img);

        AssetManager assetManager = game.getApplication().getAssetManager();
        /* NOTE: i do not think this is needed, when geometry is generated the right way */
        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };
        createMQContent(fd, assetManager, angles);
    }
}
