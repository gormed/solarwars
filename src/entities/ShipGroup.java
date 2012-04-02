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
 * File: ShipGroup.java
 * Type: entities.ShipGroup
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.Random;
import logic.Level;
import logic.Player;

/**
 * The Class ShipGroup.
 */
public class ShipGroup extends Node {


    /** The ships. */
    private ArrayList<AbstractShip> ships;
    /** The owner. */
    private Player owner;
    /** The transform node. */
    protected Node transformNode;
    /** The order. */
    private AbstractPlanet order;
    /** The position. */
    private Vector3f position;
    /** The geometry. */
    private Geometry geometry;
    /** The id. */
    private int id;
    /** The level. */
    private Level level;
    /** The size. */
    private float size;

    /**
     * Instantiates a new ship group.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param p the p
     * @param creator the creator
     * @param target the target
     * @param shipCount the ship count
     */
    public ShipGroup(AssetManager assetManager, Level level, Player p, AbstractPlanet creator, AbstractPlanet target, int shipCount) {
        this.owner = p;
        this.order = target;
        this.level = level;
        this.ships = new ArrayList<AbstractShip>();
        this.transformNode = new Node("ShipGroup Transform Node " + id);
        this.attachChild(transformNode);
        id = Level.getContiniousShipGroupID();
        createShips(assetManager, creator, target, shipCount);

    }

    /**
     * Creates the ships.
     *
     * @param assetManager the asset manager
     * @param creator the creator
     * @param target the target
     * @param shipCount the ship count
     */
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

        if (shipCount > 1) {
            size = (minX > minZ) ? minX : minZ;
        } else {
            size = 0.2f;
        }
        Sphere s = new Sphere(6, 6,
                size);

        geometry = new Geometry("ShipGroup_" + id, s);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //material.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/marker.png"));
        material.setColor("Color", new ColorRGBA(0, 0, 1, 0));

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

    /**
     * Gets the random position.
     *
     * @param p the p
     * @param r the r
     * @return the random position
     */
    private Vector3f getRandomPosition(AbstractPlanet p, Random r) {
        Vector3f pos = p.getPosition().clone();
        float randX = -p.getSize() + r.nextFloat() * p.getSize() * 2;
        float randZ = -p.getSize() + r.nextFloat() * p.getSize() * 2;

        Vector3f rand = new Vector3f(randX, 0, randZ);

        return pos.add(rand);
    }

    /**
     * Move to planet.
     *
     * @param p the p
     */
    public void moveToPlanet(AbstractPlanet p) {
        order = p;
        for (AbstractShip s : ships) {
            s.moveToPlanet(p);
        }
    }

    /**
     * Updates the ship group.
     *
     * @param tpf the tpf
     */
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

    /**
     * Removes the ship.
     *
     * @param s the s
     */
    public void removeShip(AbstractShip s) {
        ships.remove(s);
    }

    /**
     * Gets the ship groups id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ship count.
     *
     * @return the ship count
     */
    public int getShipCount() {
        return ships.size();
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
     * Gets the order.
     *
     * @return the order
     */
    public AbstractPlanet getOrder() {
        return order;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }
}
