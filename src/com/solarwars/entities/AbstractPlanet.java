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
 * File: AbstractPlanet.java
 * Type: com.solarwars.entities.AbstractPlanet
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.entities;

import com.jme3.asset.AssetManager;
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
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.solarwars.FontLoader;
import com.solarwars.Hub;
import com.solarwars.IsoCamera;
import com.solarwars.logic.AbstractGameplay;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import com.solarwars.settings.SolarWarsSettings;

/**
 * The Class AbstractPlanet.
 */
public abstract class AbstractPlanet extends Node implements Ranged {

    public static final int SPHERE_Z_SAMPLES = 10;
    public static final int SPHERE_RADIAL_SAMPLES = 10;
    public static int PLANET_QUALITY = SolarWarsSettings.getInstance().getPlanetQuality();
    protected AssetManager assetManager;
    protected Material material;
    protected Geometry geometry;
    protected Node transformNode;
    protected float size;
    protected int sizeID;
    protected Level level;
    protected int id;
    protected Vector3f position;
    protected BitmapText label;
    protected float shipIncrement;
    protected int ships = 0;
    protected float shipGainTime;
    protected Player owner;
    private ParticleEmitter impact;
    private ParticleEmitter capture;
    protected float range = AbstractGameplay.PLANET_RANGE;
    private float shift = 0.0f;

    private void createImpactEmitter() {
        if (impact == null) {
            impact = new ParticleEmitter("ShipImpactEffect ",
                    ParticleMesh.Type.Triangle, 15);
            Material impact_mat = new Material(assetManager,
                    "Common/MatDefs/Misc/Particle.j3md");
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

    /**
     * Emit impact particles.
     *
     * @param start the start
     * @param end the end
     * @param pos the pos
     * @param dir the dir
     */
    void emitImpactParticles(ColorRGBA start, ColorRGBA end, Vector3f pos,
            Vector3f dir) {

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

    /**
     * Creates the capture emitter.
     */
    private void createCaptureEmitter() {
        if (capture == null) {
            capture = new ParticleEmitter("PlanetCaptureEffect ",
                    ParticleMesh.Type.Triangle, 5);
            Material capture_mat = new Material(assetManager,
                    "Common/MatDefs/Misc/Particle.j3md");
            capture_mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/shockwave.png"));
            capture.setMaterial(capture_mat);
            capture.setImagesX(1);
            capture.setImagesY(1);
            capture.setRotateSpeed(1);
            capture.setLowLife(0.6f);
            capture.setHighLife(0.8f);
            capture.setStartSize(getSize() - 0.1f);
            capture.setEndSize(2 * getSize() + 0.1f);
            capture.setSelectRandomImage(true);

            capture.setGravity(0, 0, 0);
            // capture.getParticleInfluencer().
            // setVelocityVariation(.90f);
            capture.getParticleInfluencer().setInitialVelocity(new Vector3f(0,
                    0, 0));

            capture.setEnabled(false);

            level.addParticleEmitter(capture);
        }
    }

    /**
     * Emit capture particles.
     */
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
     * @param sizeID the size id
     */
    public AbstractPlanet(AssetManager assetManager,
            Level level,
            Vector3f position,
            int sizeID) {
        this.id = Level.getContiniousPlanetID();
        this.size = AbstractGameplay.PLANET_SIZES[sizeID];
        this.sizeID = sizeID;
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
        // this.attachChild(label);
        this.shipIncrement = AbstractGameplay.PLANET_INCREMENT_TIME[sizeID];
    }

    /**
     * Creates the label.
     */
    private void createLabel() {
        BitmapFont f = FontLoader.getInstance().getFont("SolarWarsFont64");
        label = new BitmapText(f, false);
        label.setBox(new Rectangle(-3f, 0.15f, 6f, 3f));
        // label.setQueueBucket(Bucket.Transparent);
        label.setSize(0.3f);
        label.setColor(ColorRGBA.Orange);
        label.setText(shipIncrement + "");
        label.setAlignment(Align.Center);
        label.setCullHint(CullHint.Never);
        label.setQueueBucket(Bucket.Transparent);
        // algins position of the font
        refreshFont();
    }

    /**
     * Refresh font.
     */
    private void refreshFont() {
        Camera cam = IsoCamera.getInstance().getCam();
//
//        float width = label.getLineWidth();
//        float height = label.getHeight();
//
//        
        Vector3f pos = position.clone();
//        pos.addLocal(new Vector3f(width / 2, .15f, -height / 2));
//        pos.addLocal(position);
        
        Vector3f up = cam.getUp().clone();
        Vector3f dir = cam.getDirection().
                clone().negateLocal().normalizeLocal();
        Vector3f left = cam.getLeft().
                clone().normalizeLocal().negateLocal();

        Quaternion look = new Quaternion();
        look.fromAxes(left, up, dir);

        label.setLocalTransform(new Transform(pos, look));
        
//        Vector3f camPos = IsoCamera.getInstance().getCam().getLocation();
//        Vector3f fontPos = position.clone();

//        Vector3f up = IsoCamera.getInstance().getCam().getUp().clone();
//        Vector3f dir = camPos.subtract(fontPos);
////        Vector3f dir = Vector3f.UNIT_Y.clone().subtract(fontPos);
//
//        Vector3f left = IsoCamera.getInstance().getCam().getLeft().clone();
////        Vector3f left = Vector3f.UNIT_X.clone();
//        dir.normalizeLocal();
//        left.normalizeLocal();
//        left.negateLocal();
//
//        Quaternion look = new Quaternion();
//        look.fromAxes(left, up, dir);
//
//        Vector3f newPos = dir.clone();
//        newPos.normalizeLocal();
//        newPos.mult(size);
//
//        newPos = position.add(newPos);
//
//        Transform t = new Transform(newPos, look);
//
//        label.setLocalTransform(t);
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
        label.setColor(ColorRGBA.White.clone());
        /*material.setColor("Specular", owner.getColor());
         if (SolarWarsApplication.TOON_ENABLED)
         {
         material.setColor("Diffuse", ColorRGBA.LightGray);
         }
         else
         {
         material.setColor("Diffuse", owner.getColor());
         }
         if (SolarWarsApplication.BLOOM_ENABLED)
         {
         material.setColor("GlowColor", owner.getColor());
         }*/

        material.setColor("Color", owner.getColor().mult(new ColorRGBA(2, 2, 2, 1)));
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
    public int getID() {
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
     * Gets the size id.
     *
     * @return the size id
     */
    public int getSizeID() {
        return sizeID;
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

    public float getRange() {
        return range;
    }

    /**
     * Sets the ship count.
     *
     * @param c the new ship count
     */
    public void setShipCount(int c) {
        ships = c;
    }

    // public void setShipCount(int c, long delay) {
    // //ships = c;
    // if (owner != null && !level.isGameOver()) {
    // float peek = PLANET_REFRESH_MULTIPILER * shipIncrement;
    // float gap = delay / 10f;
    //
    // while (gap > 0) {
    // ships += 1;
    // gap -= peek;
    // }
    //
    // }
    // if (ships > c)
    // ships = c;
    // }
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
    @Override
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets the transform node.
     *
     * @return the transform node
     */
    public Node getTransformNode() {
        return transformNode;
    }

    /**
     * Updates the label.
     *
     * @param tpf the tpf
     */
    private void updateLabel(float tpf) {
        boolean visible = (owner == null || owner.equals(Hub.getLocalPlayer()) || Hub.getLocalPlayer().hasLost() || owner.isLocalPlayer());
        label.setCullHint(visible ? CullHint.Never : CullHint.Always);
        if (visible) {
            refreshFont();
            label.setText(ships + "");
        }
    }

    // public void syncronize(long delay) {
    // float secDelay = delay * .001f;
    // if (secDelay >= shipGainTime) {
    // float remains = secDelay - shipGainTime;
    // if (ships >= 1) {
    // ships--;
    // }
    // while (remains >= shipIncrement) {
    // remains -= shipIncrement;
    // if (ships >= 1) {
    // ships--;
    // }
    // }
    // shipGainTime = shipIncrement - remains;
    // } else {
    // shipGainTime -= secDelay;
    // }
    // }
    /**
     * Updates the label.
     *
     * @param tpf the tpf
     */
    public void updatePlanet(float tpf) {
        if (PLANET_QUALITY == 1) {
            material.setFloat("Shift", shift);
            shift += tpf * 0.08f / size;
        }

        shipGainTime += tpf;
        if (owner != null && !level.isGameOver()) {

            if (shipGainTime
                    > AbstractGameplay.PLANET_REFRESH_MULTIPILER * shipIncrement) {
                // while (shipGainTime > PLANET_REFRESH_MULTIPILER *
                // shipIncrement) {
                // shipGainTime = shipGainTime - (PLANET_REFRESH_MULTIPILER *
                // shipIncrement);
                ships++;
                // }
                // if (shipGainTime < 0) {
                shipGainTime = 0;
                // }
            }
        }
        updateLabel(tpf);
    }
}
