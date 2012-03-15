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
 * File: AbstractShip.java
 * Type: entities.AbstractShip
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import logic.level.Level;
import logic.ActionLib;
import logic.Gameplay;
import logic.Player;

/**
 * The Class AbstractShip.
 */
public abstract class AbstractShip extends Node {

    /** The SHI p_ id. */
    private static int SHIP_ID = 0;

    /**
     * Gets the continious id.
     *
     * @return the continious id
     */
    private static int getContiniousID() {
        return SHIP_ID++;
    }
    
    /** The SHI p_ size. */
    protected static float SHIP_SIZE = 0.15f;
    
    /** The asset manager. */
    protected AssetManager assetManager;
    
    /** The geometry. */
    protected Geometry geometry;
    
    /** The material. */
    protected Material material;
    
    /** The transform node. */
    protected Node transformNode;
    
    /** The level. */
    protected Level level;
    
    /** The id. */
    protected int id;
    
    /** The position. */
    protected Vector3f position;
    
    /** The ship group. */
    protected ShipGroup shipGroup;
    
    /** The owner. */
    protected Player owner;
    
    /** The order. */
    protected AbstractPlanet order;

    /**
     * Instantiates a new abstract ship.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param p the p
     * @param g the g
     */
    public AbstractShip(AssetManager assetManager, Level level, Vector3f position, Player p, ShipGroup g) {
        super();

        this.id = getContiniousID();
        this.owner = p;
        this.assetManager = assetManager;
        this.level = level;
        this.position = position;
        this.shipGroup = g;

        this.transformNode = new Node("Ship Transform Node " + id);
        this.attachChild(transformNode);
    }

    /**
     * Creates the ship.
     */
    public abstract void createShip();

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
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
     * Checks for ship group.
     *
     * @return true, if successful
     */
    public boolean hasShipGroup() {
        return shipGroup != null;
    }

    /**
     * Gets the ship group.
     *
     * @return the ship group
     */
    public ShipGroup getShipGroup() {
        return shipGroup;
    }

    /**
     * Removes the from ship group.
     */
    private void removeFromShipGroup() {
        if (shipGroup == null) {
            return;
        } else {
            ActionLib.getInstance().invokeShipAction(
                    this, shipGroup, owner, Gameplay.SHIP_ARRIVES);
        }
    }

    /**
     * Move to planet.
     *
     * @param p the p
     */
    public void moveToPlanet(AbstractPlanet p) {
        order = p;
        Vector3f planetLoc = p.getPosition();
        Vector3f dir = planetLoc.subtract(position);
        dir.normalizeLocal();

        Vector3f left = dir.cross(Vector3f.UNIT_Y);
        left.normalizeLocal();

        Matrix3f mat = new Matrix3f();
        mat.fromAxes(dir, Vector3f.UNIT_Y, left);

        transformNode.setLocalRotation(mat);
    }

    /**
     * Updates the ship.
     *
     * @param tpf the tpf
     */
    public void updateShip(float tpf) {
        if (order != null) {
            Vector3f planetLoc = order.getPosition();
            Vector3f dir = planetLoc.subtract(position);
            if (dir.length() < 0.1f) {

                ActionLib.getInstance().invokePlanetAction(
                        this, order, owner, Gameplay.PLANET_CAPTURE);

                removeFromShipGroup();
                level.removeShip(owner, this);
                order = null;

            } else {
                dir.normalizeLocal();
                dir.multLocal(tpf);
                position.addLocal(dir);
                transformNode.setLocalTranslation(position);
            }
        }
    }
}
