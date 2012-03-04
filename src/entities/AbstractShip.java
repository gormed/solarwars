/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import level.Level;
import logic.Player;

/**
 *
 * @author Hans
 */
public abstract class AbstractShip extends Node {

    private static int SHIP_ID = 0;

    private static int getContiniousID() {
        return SHIP_ID++;
    }
    protected static float SHIP_SIZE = 0.25f;
    protected AssetManager assetManager;
    protected Geometry geometry;
    protected Material material;
    protected Node transformNode;
    protected Level level;
    protected int id;
    protected Vector3f position;
    protected ShipGroup shipGroup;
    protected Player owner;
    protected AbstractPlanet order;

    public AbstractShip(AssetManager assetManager, Level level, Vector3f position, Player p) {
        super();
        id = getContiniousID();
        this.owner = p;
        this.assetManager = assetManager;
        this.level = level;
        this.position = position;

        transformNode = new Node("Ship Transform Node " + id);
        this.attachChild(transformNode);
    }

    public abstract void createShip();

    public int getId() {
        return id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public boolean hasShipGroup() {
        return shipGroup != null;
    }

    public ShipGroup getShipGroup() {
        return shipGroup;
    }

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

    public void updateShip(float tpf) {
        if (order != null) {
            Vector3f planetLoc = order.getPosition();
            Vector3f dir = planetLoc.subtract(position);
            if (dir.length() < 0.1f) {
                if (order.getOwner() == owner) {
                    order.incrementShips();
                } else {
                    order.decrementShips();
                    if (order.getShips() == 0) {
                        order.setOwner(owner);
                    }
                }
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
