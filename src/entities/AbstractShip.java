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
 * File: AbstractShip.java
 * Type: entities.AbstractShip
 * 
 * Documentation created: 14.07.2012 - 19:38:03 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import logic.AbstractGameplay;
import logic.Level;
import logic.ActionLib;
import logic.DeathmatchGameplay;
import logic.Player;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class AbstractShip.
 */
public abstract class AbstractShip extends Node {

    /** The SHI p_ size. */
    protected static float SHIP_SIZE = 0.2f;
    /** The asset manager. */
    protected static AssetManager assetManager = 
            SolarWarsApplication.getInstance().getAssetManager();
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The transform node. */
//    protected Node transformNode;
    /** The level. */
    protected Level level;
    /** The id. */
    protected int id;
    /** The position. */
    protected Vector3f position;
    
    protected float speed = AbstractGameplay.SHIP_SPEED;
    /** The ship group. */
    protected ShipGroup shipGroup;
    /** The owner. */
    protected Player owner;
    /** The order. */
    protected AbstractPlanet order;
    
    /** The ship batch spatial. */
    protected Spatial shipBatchSpatial;
    
    /** The batch manager. */
    protected ShipBatchManager batchManager;

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
        this.id = Level.getContiniousPlanetID();
        this.owner = p;
        this.level = level;
        this.batchManager = level.getBatchManager();
        this.position = position;
        this.shipGroup = g;

//        this.transformNode = new Node("Ship Transform Node " + id);
//        this.attachChild(transformNode);
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
    private void freeShip() {
        if (shipGroup == null) {
            return;
        } else {
            ActionLib.getInstance().invokeShipAction(
                    this, 
                    0,
                    shipGroup, 
                    owner, 
                    DeathmatchGameplay.SHIP_ARRIVES);
        }
        batchManager.freeShipBatch(shipBatchSpatial);
        level.removeShip(owner, this);
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

        shipBatchSpatial.setLocalRotation(mat);
    }

    /**
     * Updates the ship.
     *
     * @param tpf the tpf
     */
    public void updateShip(float tpf) {
        if (order != null && !level.isGameOver()) {
            Vector3f planetLoc = order.getPosition();
            Vector3f dir = planetLoc.subtract(position);

            Vector3f impactPos = order.position.clone();
            Vector3f offset = dir.clone();
            offset.normalizeLocal().negateLocal().multLocal(order.getSize() + 0.05f);
            impactPos.addLocal(offset);

            Vector3f impact = impactPos.clone();
            impact.subtractLocal(position);

            if (impact.length() < 0.1f || dir.length() < 0.01f) {

                if (owner.equals(Hub.getLocalPlayer())) {
                    ActionLib.getInstance().invokePlanetAction(
                            this, 
                            0L,
                            order, 
                            owner, 
                            DeathmatchGameplay.PLANET_CAPTURE);

                }

                order.emitImpactParticles(this.owner.getColor(), ColorRGBA.BlackNoAlpha, impactPos, dir);

                freeShip();


            } else {
                dir.normalizeLocal();
                dir.multLocal(tpf * speed);
//                System.out.println("Time: " + tpf);
                position.addLocal(dir);
                shipBatchSpatial.setLocalTranslation(position);

            }
        }
    }
}
