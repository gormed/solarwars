/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.Random;
import logic.level.Level;
import logic.Player;

/**
 *
 * @author Hans
 */
public class ShipGroup extends Node {

    private static int SHIP_ID = 0;

    private static int getContiniousID() {
        return SHIP_ID++;
    }
    private ArrayList<AbstractShip> ships;
    private Player owner;
    protected Node transformNode;
    private AbstractPlanet order;
    private Vector3f position;
    private Geometry geometry;
    private int id;
    private Level level;
    private float size;

    public ShipGroup(AssetManager assetManager, Level level, Player p, AbstractPlanet creator, AbstractPlanet target, int shipCount) {
        this.owner = p;
        this.order = target;
        this.level = level;
        this.ships = new ArrayList<AbstractShip>();
        this.transformNode = new Node("ShipGroup Transform Node " + id);
        this.attachChild(transformNode);
        id = getContiniousID();
        createShips(assetManager, creator, target, shipCount);

    }

    private void createShips(AssetManager assetManager, AbstractPlanet creator, AbstractPlanet target, int shipCount) {
        Random r = new Random(System.currentTimeMillis());

        Vector3f pos = new Vector3f();
        Vector3f shipPos;
        float maxX = -100, minX = 100, maxZ = -100, minZ = 100;

        for (int i = 0; i < shipCount; i++) {
            shipPos = getRandomPosition(creator, r);

            SimpleShip s = new SimpleShip(
                    assetManager,
                    level,
                    shipPos,
                    owner,
                    this);

            s.createShip();
            this.ships.add(s);
            level.addShip(owner, s);
            s.moveToPlanet(target);
            creator.decrementShips();

            pos.addLocal(shipPos);

            if (shipPos.x < minX) {
                minX = shipPos.x;
            } else if (shipPos.x > maxX) {
                maxX = shipPos.x;
            }

            if (shipPos.z < minZ) {
                minZ = shipPos.z;
            } else if (shipPos.z > maxZ) {
                maxZ = shipPos.z;
            }
        }
        pos.divideLocal(shipCount);
        position = new Vector3f(pos);

        minX = maxX - minX;
        minZ = maxZ - minZ;

        if (shipCount > 1)
            size = (minX > minZ) ? minX : minZ;
        else
            size = 0.2f;
        Sphere s = new Sphere(6, 6,
                size);

        geometry = new Geometry("ShipGroup_" + id, s);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //material.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/marker.png"));
        material.setColor("Color", new ColorRGBA(0, 0, 1, 0.1f));

        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);


        geometry.setMaterial(material);

        /** Objects with transparency need to be in 
         * the render bucket for transparent objects: */
        geometry.setQueueBucket(Bucket.Translucent);


        float angles[] = {
            (float) -Math.PI / 2, (float) -Math.PI / 2, 0
        };

        geometry.setLocalRotation(new Quaternion(angles));

        transformNode.attachChild(geometry);
    }

    private Vector3f getRandomPosition(AbstractPlanet p, Random r) {
        Vector3f pos = p.getPosition().clone();
        float randX = -p.getSize() + r.nextFloat() * p.getSize() * 2;
        float randZ = -p.getSize() + r.nextFloat() * p.getSize() * 2;

        Vector3f rand = new Vector3f(randX, 0, randZ);

        return pos.add(rand);
    }

    public void moveToPlanet(AbstractPlanet p) {
        order = p;
        for (AbstractShip s : ships) {
            s.moveToPlanet(p);
        }
    }

    public void updateShipGroup(float tpf) {
        if (order != null) {
            Vector3f planetLoc = order.getPosition();
            Vector3f dir = planetLoc.subtract(position);
            if (dir.length() < 0.001f) {
            } else {
                dir.normalizeLocal();
                dir.multLocal(tpf);
                position.addLocal(dir);
                transformNode.setLocalTranslation(position);
            }
        }
    }

    public void removeShip(AbstractShip s) {
        ships.remove(s);
    }
    
    public int getShipCount() {
        return ships.size();
    }

    public float getSize() {
        return size;
    }

    public AbstractPlanet getOrder() {
        return order;
    }

    public Player getOwner() {
        return owner;
    }
}
