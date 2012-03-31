/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: AbstractPlanet.java
 * Type: entities.AbstractPlanet
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
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
import logic.level.Level;
import logic.Player;
import solarwars.FontLoader;
import solarwars.IsoCamera;

/**
 * The Class AbstractPlanet.
 */
public abstract class AbstractPlanet extends Node {

    /** The Constant SHIP_REFRESH_MULTIPILER. */
    private static final int SHIP_REFRESH_MULTIPILER = 5;
    /** The PLANE t_ id. */
    private static int PLANET_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    private static int getContiniousID() {
        return PLANET_ID++;
    }
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
    /** The time. */
    protected float time;
    /** The owner. */
    protected Player owner;

    /**
     * Instantiates a new abstract planet.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param size the size
     */
    public AbstractPlanet(AssetManager assetManager, Level level, Vector3f position, float size) {
        this.id = getContiniousID();
        this.size = size;
        this.level = level;
        this.position = position;
        this.transformNode = new Node("Planet Transform Node " + id);
        this.transformNode.setLocalTranslation(position);
        this.assetManager = assetManager;
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
    public int getShips() {
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
        ships--;
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

    /**
     * Updates the label.
     *
     * @param tpf the tpf
     */
    public void updateLabel(float tpf) {
        refreshFont();

        if (owner != null) {
            time += tpf;
            if (time > SHIP_REFRESH_MULTIPILER * shipIncrement) {
                time = 0;
                ships += 1;
            }
        }
        label.setText(ships + "");
    }
}
