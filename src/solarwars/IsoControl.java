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
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

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

    /**
     * Returns the action listener of the control. 
     * It determines wether a node was hit or not on a given click.
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
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_CLICK) && !keyPressed) {
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
                        if (lastNode != null) {
                            lastNode.detachChild(markerNode);
                        }
                        lastNode = closest.getGeometry().getParent();
                        lastNode.attachChild(markerNode);
                        marker.killAllParticles();
                        marker.emitAllParticles();
                        //markerNode.setLocalScale(tpf, tpf, tpf);
                        //rootNode.attachChild(markerNode);
                    } else {
                        // No hits? Then remove the mark.
                        if (lastNode != null) {
                            lastNode.detachChild(markerNode);
                        }
                        //rootNode.detachChild(markerNode);
                    }
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

    /** 
     * Declaring the "Shoot" action and mapping to 
     * its triggers. 
     */
    public void setupContols(InputManager input) {
        input.addMapping("Shoot",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        input.addListener(actionListener, "Shoot");
    }
}
