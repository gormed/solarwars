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
 * File: AbstractPlanet.java
 * Type: entities.AbstractPlanet
 * 
 * Documentation created: 31.03.2012 - 19:27:49 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import logic.Level;
import logic.Player;
import solarwars.FontLoader;
import solarwars.Hub;
import solarwars.IsoCamera;

/**
 * The Class AbstractPlanet.
 */
public abstract class AbstractPlanet extends Node {

    /** The Constant SHIP_REFRESH_MULTIPILER. */
    private static final int SHIP_REFRESH_MULTIPILER = 5;
    /** The Constant SPHERE_Z_SAMPLES. */
    public static final int SPHERE_Z_SAMPLES = 20;
    /** The Constant SPHERE_RADIAL_SAMPLES. */
    public static final int SPHERE_RADIAL_SAMPLES = 20;
    /** The asset manager. */
    protected AssetManager assetManager;
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The transform node. */
    protected Node transformNode;
    /** The size. */
    protected float size;
    /** The level. */
    protected Level level;
    /** The id. */
    protected int id;
    /** The position. */
    protected Vector3f position;
    /** The label. */
    protected BitmapText label;
    /** The ship increment. */
    protected float shipIncrement;
    /** The ships. */
    protected int ships = 0;
    /** The shipGainTime. */
    protected float shipGainTime;
    /** The owner. */
    protected Player owner;
    private ParticleEmitter impact;
    private ParticleEmitter capture;

    private void createImpactEmitter() {
        if (impact == null) {
            impact = new ParticleEmitter("ShipImpactEffect ", ParticleMesh.Type.Triangle, 15);
            Material impact_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
            impact_mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/blob.png"));
            impact.setMaterial(impact_mat);
            impact.setImagesX(1);
            impact.setImagesY(1);
            impact.setRotateSpeed(1);
            impact.setLowLife(0.3f);
            impact.setHighLife(1.1f);
            impact.setStartSize(0.1f);
            impact.setEndSize(0.2f);
            impact.setSelectRandomImage(true);

            impact.setGravity(0, 0, 0);
            impact.getParticleInfluencer().setVelocityVariation(.10f);

            impact.setEnabled(false);

            level.addParticleEmitter(impact);
        }
    }

    void emitImpactParticles(ColorRGBA start, ColorRGBA end, Vector3f pos, Vector3f dir) {

        impact.setLowLife(0.3f);
        impact.setHighLife(1.1f);
        impact.getParticleInfluencer().setVelocityVariation(.10f);
        impact.getParticleInfluencer().setInitialVelocity(dir.normalizeLocal().negateLocal().mult(0.23f));

        impact.setEnabled(true);

        impact.setParticlesPerSec(200);

        impact.setStartColor(start);
        impact.setEndColor(end);

        impact.setShape(new EmitterPointShape(pos));

        impact.emitAllParticles();

        impact.setParticlesPerSec(0);
    }

    private void createCaptureEmitter() {
        if (capture == null) {
            capture = new ParticleEmitter(
                    "PlanetCaptureEffect ", 
                    ParticleMesh.Type.Triangle, 
                    5);
            Material capture_mat = 
                    new Material(
                            assetManager, 
                            "Common/MatDefs/Misc/Particle.j3md");
            capture_mat.setTexture(
                    "Texture", 
                    assetManager.loadTexture("Textures/Effects/shockwave.png"));
            capture.setMaterial(capture_mat);
            capture.setImagesX(1);
            capture.setImagesY(1);
            capture.setRotateSpeed(1);
            capture.setLowLife(0.6f);
            capture.setHighLife(0.8f);
            capture.setStartSize(getSize()-0.1f);
            capture.setEndSize(2*getSize() + 0.1f);
            capture.setSelectRandomImage(true);

            capture.setGravity(0, 0, 0);
//            capture.getParticleInfluencer().
//                    setVelocityVariation(.90f);
            capture.getParticleInfluencer().
                    setInitialVelocity(new Vector3f(0, 0, 0));

            capture.setEnabled(false);

            level.addParticleEmitter(capture);
        }
    }

    public void emitCaptureParticles() {

        capture.killAllParticles();
        capture.setEnabled(true);

        capture.setParticlesPerSec(500);

        capture.setStartColor(owner.getColor());
        capture.setEndColor(ColorRGBA.BlackNoAlpha);

        capture.setShape(new EmitterPointShape(position));

        capture.emitAllParticles();

        capture.setParticlesPerSec(0);
    }

    /**
     * Instantiates a new abstract planet.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param size the size
     */
    public AbstractPlanet(AssetManager assetManager, Level level, Vector3f position, float size) {
        this.id = Level.getContiniousPlanetID();
        this.size = size;
        this.level = level;
        this.position = position;
        this.transformNode = new Node("Planet Transform Node " + id);
        this.transformNode.setLocalTranslation(position);
        this.assetManager = assetManager;
        this.createImpactEmitter();
        this.createCaptureEmitter();
        this.owner = null;
        createLabel();

        this.attachChild(transformNode);
        level.getLabelNode().attachChild(label);
        //this.attachChild(label);

    }

    /**
     * Creates the label.
     */
    public void createLabel() {
        BitmapFont f = FontLoader.getInstance().getFont("SolarWarsFont32");
        label = new BitmapText(f, false);
        label.setBox(new Rectangle(-3f, 0.15f, 6f, 3f));
        label.setQueueBucket(Bucket.Transparent);
        label.setSize(0.3f);
        label.setColor(ColorRGBA.Orange);
        label.setText(shipIncrement + "");
        label.setAlignment(Align.Center);
        label.setCullHint(CullHint.Never);

//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("GlowColor", ColorRGBA.Orange);
//        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
//        
//        label.setMaterial(mat);

        // algins position of the font
        refreshFont();

        //label.attachChild(new Cross(assetManager));
        // calculate ships generated by the planet 
        // TODO move this to logic
        calculateIncrement();
    }

    /**
     * Refresh font.
     */
    private void refreshFont() {
        Vector3f camPos = IsoCamera.getInstance().getCam().getLocation();
        Vector3f fontPos = position.clone();

        Vector3f up = IsoCamera.getInstance().getCam().getUp().clone();
        Vector3f dir = camPos.subtract(fontPos);

        Vector3f left = IsoCamera.getInstance().getCam().getLeft().clone();
        dir.normalizeLocal();
        left.normalizeLocal();
        left.negateLocal();

        Quaternion look = new Quaternion();
        look.fromAxes(left, up, dir);

        Vector3f newPos = dir.clone();
        newPos.normalizeLocal();
        newPos.mult(size);

        newPos = position.add(newPos);

        Transform t = new Transform(newPos, look);

        label.setLocalTransform(t);
    }

    /**
     * Checks for owner.
     *
     * @return true, if successful
     */
    public boolean hasOwner() {
        return owner != null;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param p the new owner
     */
    public void setOwner(Player p) {
        owner = p;
        material.setColor("Specular", owner.getColor());
        material.setColor("Diffuse", ColorRGBA.White);
        material.setColor("GlowColor", owner.getColor());
    }

    /**
     * Creates the planet.
     */
    public abstract void createPlanet();

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * Gets the geometry.
     *
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Gets the ships.
     *
     * @return the ships
     */
    public int getShipCount() {
        return ships;
    }

    /**
     * Sets the ship count.
     *
     * @param c the new ship count
     */
    public void setShipCount(int c) {
        ships = c;
    }

    /**
     * Decrement ships.
     */
    public void decrementShips() {
        if (ships > 0) {
            ships--;
        }
    }

    /**
     * Increment ships.
     */
    public void incrementShips() {
        ships++;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    public Node getTransformNode() {
        return transformNode;
    }
    

    /**
     * Calculate increment.
     */
    protected void calculateIncrement() {
        float seed = (4 * size) - 0.6f;
        shipIncrement = (1 - (seed)) + 0.09f;
        if (shipIncrement > 1) {
            shipIncrement = 1;
        }
    }

    private void updateLabel(float tpf) {

        boolean visible = (owner == null || owner.equals(Hub.getLocalPlayer()));
        label.setCullHint(visible ? CullHint.Never : CullHint.Always);
        if (visible) {
            refreshFont();
            label.setText(ships + "");
        }

        shipGainTime += tpf;
        if (owner != null && !level.isGameOver()) {

            if (shipGainTime > SHIP_REFRESH_MULTIPILER * shipIncrement) {
                shipGainTime = 0;
                ships += 1;
            }
        }

    }

    /**
     * Updates the label.
     *
     * @param tpf the tpf
     */
    public void updatePlanet(float tpf) {

        updateLabel(tpf);
    }
}
