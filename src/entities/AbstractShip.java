/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Hans
 */
public abstract class AbstractShip extends Node {

    private static int SHIP_ID = 0;

    private static int getContiniousID() {
        return SHIP_ID++;
    }
    protected static float SHIP_SIZE = 0.15f;
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

    private void removeFromShipGroup() {
        if (shipGroup == null) {
            return;
        } else {
            ActionLib.getInstance().invokeShipAction(
                    this, shipGroup, owner, Gameplay.SHIP_ARRIVES);
        }
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
