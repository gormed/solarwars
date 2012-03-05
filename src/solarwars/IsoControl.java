/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import entities.AbstractPlanet;
import entities.AbstractShip;
import logic.ActionLib;
import logic.Gameplay;
import logic.Player;

/**
 *
 * @author Hans
 */
public class IsoControl {

    private Node rootNode;
    private Node shootablesNode;

    public Node getShootablesNode() {
        return shootablesNode;
    }
    private Node markerNode;
    private Node lastNode;
    private ParticleEmitter marker;
    private Camera cam;
    private ActionListener actionListener;
    private ActionLib actionLib;

    /**
     * Returns the action listener of the control. 
     * It determines wether a node was hit or not on a given action.
     * 
     * @return 
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Creates a new Control in iso view to select things.
     * @param assetManager
     * @param rootNode
     * @param cam 
     */
    public IsoControl(AssetManager assetManager, Node rootNode, Camera cam, InputManager inputManager) {
        this.rootNode = rootNode;
        this.cam = cam;
        this.actionLib = ActionLib.getInstance();

        markerNode = new Node("Marker Transform");

        /** Explosion effect. Uses Texture from jme3-test-data library! */
        marker = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 1);
        Material debris_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/marker.png"));
        marker.setMaterial(debris_mat);
        marker.setImagesX(1);
        marker.setImagesY(1); // 3x3 texture animation
        marker.setStartSize(0.51f);
        marker.setEndSize(0.5f);
        marker.setLowLife(0.55f);
        marker.setHighLife(0.6f);

        marker.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        marker.setStartColor(new ColorRGBA(0.1f, 0.1f, 1f, 1f));
        marker.setEndColor(new ColorRGBA(0.1f, 0.1f, 1f, 0.3f));
        //debris.getParticleInfluencer().setGravity(6f);
        //marker.getParticleInfluencer().setVelocityVariation(.60f);
        markerNode.attachChild(marker);
        marker.emitAllParticles();

        initialize(rootNode, inputManager);
    }

    private void initialize(final Node rootNode, final InputManager inputManager) {
        shootablesNode = new Node("Shootables");
        rootNode.attachChild(shootablesNode);
        // Defining the "Shoot" action: Determine what was hit and how to respond. 
        actionListener = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {

                int action = -1;

                // LEFT CLICK = 1
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
                    action = 1;
                } // RIGHT CLICK = 2
                else if (name.equals(SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK)) {
                    action = 2;
                } // WHEEL DOWN
                else if (name.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_DOWN)) {
                    action = 3;
                } // WHEEL UP
                else if (name.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_UP)) {
                    action = 4;
                }

                if ((action == 1 || action == 2) && !keyPressed) {
                    // 1. Reset results list.
                    CollisionResults results = new CollisionResults();
                    // 2. calculate ray from camera to mouse pointer
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    Ray ray = new Ray(click3d, dir);
                    // 3. Collect intersections between Ray and Shootables in results list.
                    shootablesNode.collideWith(ray, results);
                    // 4. Print the results
                    System.out.println("----- Collisions? " + results.size() + "-----");
                    for (int i = 0; i < results.size(); i++) {
                        // For each hit, we know distance, impact point, name of geometry.
                        float dist = results.getCollision(i).getDistance();
                        Vector3f pt = results.getCollision(i).getContactPoint();
                        String hit = results.getCollision(i).getGeometry().getName();
                        System.out.println("* Collision #" + i);
                        System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                    }
                    // 5. Use the results (we mark the hit object)
                    if (results.size() > 0) {
                        // The closest collision point is what was truly hit:
                        CollisionResult closest = results.getClosestCollision();

                        // Let's interact - we mark the hit with a red dot.
                        //markerNode.setLocalTranslation(closest.getContactPoint());


                        lastNode = closest.getGeometry().getParent();

                        AbstractPlanet p = null;
                        AbstractShip s = null;
                        Node n = lastNode.getParent();

                        if (n instanceof AbstractPlanet) {
                            p = (AbstractPlanet) n;


                            if (action == 1) {
                                actionLib.invokePlanetAction(p, Hub.getLocalPlayer(), Gameplay.PLANET_SELECT);
                                
                                if (lastNode != null) {
                                    lastNode.detachChild(markerNode);
                                }
                                
                                lastNode.attachChild(markerNode);

                                marker.killAllParticles();
                                marker.setStartSize(p.getSize() + 0.15f);
                                marker.setEndSize(p.getSize() + 0.2f);
                                marker.emitAllParticles();

                            } else if (action == 2) {
                                actionLib.invokePlanetAction(p, Hub.getLocalPlayer(), Gameplay.PLANET_ATTACK);
                                actionLib.invokePlanetAction(p, Hub.getLocalPlayer(), Gameplay.PLANET_MOVE);
                            }
                        } else if (n instanceof AbstractShip) {
                            s = (AbstractShip) n;
                        }

                    } else {
                        // No hits? Then remove the mark.
                        if (lastNode != null) {
                            lastNode.detachChild(markerNode);
                        }
                        //rootNode.detachChild(markerNode);
                    }
                }

                if (action == 3 || action == 4) {
                    float percentage = Hub.getLocalPlayer().getShipPercentage();
                    if (action == 3) {
                        percentage += 0.025f;
                    } else {
                        percentage -= 0.025f;
                    }
                    Hub.getLocalPlayer().setShipPercentage(percentage);
                }
            }
        };
    }

    /**
     * Add a given spatial to the shootabels node.
     * @param spat the spatial to add
     */
    public void addShootable(Spatial spat) {
        shootablesNode.attachChild(spat);
    }

    /**
     * Remove a given spatial from the shooting node.
     * @param spat the spatial to remove
     */
    public void removeShootable(Spatial spat) {
        shootablesNode.detachChild(spat);
    }
}
