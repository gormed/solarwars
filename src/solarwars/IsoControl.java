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
 * File: IsoControl.java
 * Type: solarwars.IsoControl
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import entities.AbstractPlanet;
import entities.Cross;
import entities.ShipGroup;
import gui.GameGUI;
import gui.elements.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import logic.ActionLib;
import logic.Gameplay;

/**
 * The Class IsoControl.
 */
public class IsoControl {

    private static IsoControl instance;

    public static IsoControl getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IsoControl(SolarWarsApplication.getInstance());
    }
    /** The root node. */
    private Node rootNode;
    /** The shootables node. */
    private Node shootablesNode;
    private final AssetManager assetManager;

    /**
     * Gets the shootables node.
     *
     * @return the shootables node
     */
    public Node getShootablesNode() {
        return shootablesNode;
    }
    /** The marker node. */
    private MarkerNode markerNode;
    /** The last node. */
    private Node lastNode;
    private boolean dragging = false;
    private Vector3f lastXZPlanePos;
    private Vector3f startXZPlanePos;
    private Vector2f startScreenPos;
    // planet selection
    private ArrayList<AbstractPlanet> planetSelection;
    private ArrayList<MarkerNode> markerNodes;
    // Panels for rect
    private Panel leftDrag;
    private Panel topDrag;
    private Panel rightDrag;
    private Panel bottomDrag;
    private Panel centerDrag;
    // debug
//    private Cross startDrag;
//    private Cross endDrag;
    /** The cam. */
    private Camera cam;
    /** The action listener. */
    private ActionListener actionListener;
    private InputManager inputManager;
    /** The action lib. */
    private ActionLib actionLib;
    private GameGUI gui;

    /**
     * Gets the action listener.
     *
     * @return the action listener
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Instantiates a new iso control.
     *
     * @param assetManager the asset manager
     * @param rootNode the root node
     * @param cam the cam
     * @param inputManager the input manager
     */
    private IsoControl(SolarWarsApplication application) {
        assetManager = application.getAssetManager();
        inputManager = application.getInputManager();

        this.rootNode = application.getRootNode();
        this.cam = application.getCamera();
        this.actionLib = ActionLib.getInstance();

        this.markerNode = new MarkerNode();

        // /** Explosion effect. Uses Texture from jme3-test-data library! */
        // marker = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle,
        // 1);
        // Material debris_mat = new Material(assetManager,
        // "Common/MatDefs/Misc/Particle.j3md");
        // debris_mat.setTexture("Texture",
        // assetManager.loadTexture("Textures/Effects/marker.png"));
        // marker.setMaterial(debris_mat);
        // marker.setImagesX(1);
        // marker.setImagesY(1); // 3x3 texture animation
        // marker.setStartSize(0.5f);
        // marker.setEndSize(0.5f);
        // marker.setLowLife(0.18f);
        // marker.setHighLife(0.18f);
        //
        // // marker.setLowLife(0.55f);
        // // marker.setHighLife(0.6f);
        //
        // marker.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0,
        // 0));
        // marker.setStartColor(new ColorRGBA(0.1f, 0.1f, 1f, 1f));
        // marker.setEndColor(new ColorRGBA(0.1f, 0.1f, 1f, 0.3f));
        // //debris.getParticleInfluencer().setGravity(6f);
        // //marker.getParticleInfluencer().setVelocityVariation(.60f);
        // markerNode.attachChild(marker);
        // marker.emitAllParticles();

        initialize(rootNode);
    }

    /**
     * Initializes the.
     *
     * @param rootNode the root node
     * @param inputManager the input manager
     */
    private void initialize(final Node rootNode) {
        shootablesNode = new Node("Shootables");
        rootNode.attachChild(shootablesNode);
        // Defining the "Shoot" action: Determine what was hit and how to
        // respond.
        actionListener = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {

                gui = Gameplay.getCurrentLevel().getGui();
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

                if (keyPressed && action == 1) {
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    Ray ray = new Ray(click3d, dir);

                    float t = -ray.getOrigin().y / ray.getDirection().y;
                    Vector3f currentXZPlanePos =
                            ray.getDirection().clone().
                            mult(t).addLocal(ray.getOrigin().clone());

                    if (!dragging) {
                        startXZPlanePos = currentXZPlanePos;
                        startScreenPos = click2d.clone();
                        planetSelection = new ArrayList<AbstractPlanet>();
                        if (markerNodes != null && !markerNodes.isEmpty()) {
                            for (MarkerNode markerNode : markerNodes) {
                                removeMarker(markerNode);
                            }
                            markerNodes.clear();
                        }
                        markerNodes = new ArrayList<MarkerNode>();
//                        startDrag = new Cross(assetManager);
//                        startDrag.setLocalTranslation(startXZPlanePos);
//                        rootNode.attachChild(startDrag);
                    }
                    dragging = true;
                }

                if ((action == 1 || action == 2) && !keyPressed) {
                    // 1. Reset results list.
                    dragging = false;
                    CollisionResults results = new CollisionResults();
                    // 2. calculate ray from camera to mouse pointer
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    Ray ray = new Ray(click3d, dir);

                    if (action == 1) {
//                        rootNode.detachChild(startDrag);
                        float width = Math.abs(lastXZPlanePos.x - startXZPlanePos.x);
                        float height = Math.abs(lastXZPlanePos.z - startXZPlanePos.z);
                        if (width > 0.2f && height > 0.2f) {
                            removeMarker(markerNode);
                            float leftX = (startXZPlanePos.x <= lastXZPlanePos.x) ? startXZPlanePos.x : lastXZPlanePos.x;
                            float topY = (startXZPlanePos.z <= lastXZPlanePos.z) ? startXZPlanePos.z : lastXZPlanePos.z;
                            Rectangle2D rect = new Rectangle2D.Float(
                                    leftX, topY, width, height);
                            selectPlanets(rect);
                            MarkerNode markerNode = null;
                            if (!planetSelection.isEmpty()) {
                                for (AbstractPlanet planet : planetSelection) {

                                    markerNode = new MarkerNode();
                                    markerNodes.add(markerNode);
                                    repositMarker(planet, markerNode);

                                }
                                actionLib.invokePlanetAction(
                                        IsoControl.getInstance(),
                                        null,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_MULTI_SELECT);

                            }

                            lastXZPlanePos = null;
                            startScreenPos = null;
                            startXZPlanePos = null;
                            if (centerDrag != null) {
                                gui.removeGUIElement(centerDrag);
                                centerDrag = null;
                            }
                            if (leftDrag != null) {
                                gui.removeGUIElement(leftDrag);
                                leftDrag = null;
                            }
                            if (topDrag != null) {
                                gui.removeGUIElement(topDrag);
                                topDrag = null;
                            }
                            if (rightDrag != null) {
                                gui.removeGUIElement(rightDrag);
                                rightDrag = null;
                            }
                            if (bottomDrag != null) {
                                gui.removeGUIElement(bottomDrag);
                                bottomDrag = null;
                            }
                            return;
                        }

                    }

                    // 3. Collect intersections between Ray and Shootables in
                    // results list.
                    shootablesNode.collideWith(ray, results);
                    // 4. Print the results
                    System.out.println("----- 3D Collisions? " + results.size()
                            + "-----");
                    for (int i = 0; i < results.size(); i++) {
                        // For each hit, we know distance, impact point, name of
                        // geometry.
                        float dist = results.getCollision(i).getDistance();
                        Vector3f pt = results.getCollision(i).getContactPoint();
                        String hit = results.getCollision(i).getGeometry().getName();
                        System.out.println("* Collision #" + i);
                        System.out.println("  You shot " + hit + " at " + pt
                                + ", " + dist + " wu away.");
                    }
                    // 5. Use the results (we mark the hit object)
                    if (results.size() > 0) {
                        CollisionResult actual;

                        ShipGroup nearestShipGroup = null;
                        AbstractPlanet nearestPlanet = null;
                        Node temp = null;

                        // The closest collision point is what was truly hit:
                        CollisionResult closest = results.getClosestCollision();
                        temp = closest.getGeometry().getParent().getParent();

                        if (temp != null && temp instanceof AbstractPlanet) {
                            nearestPlanet = (AbstractPlanet) temp;
                            lastNode = nearestPlanet;
                            if (action == 1) {
                                actionLib.invokePlanetAction(null, nearestPlanet,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_SELECT);

                                repositMarker(nearestPlanet, markerNode);
                                return;

                            } else if (action == 2 && !Hub.getLocalPlayer().hasLost()) {
                                actionLib.invokePlanetAction(null, nearestPlanet,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_ATTACK);
                                return;
                            }

                        }


                        for (int i = 0; i < results.size(); i++) {
                            actual = results.getCollision(i);
                            temp = actual.getGeometry().getParent().getParent();
                            if (temp != null) {
                                if (temp instanceof AbstractPlanet) {
                                    nearestPlanet = (AbstractPlanet) temp;
                                } else if (temp instanceof ShipGroup) {
                                    nearestShipGroup = (ShipGroup) temp;
                                }
                            }
                            if (nearestPlanet != null && nearestShipGroup != null) {
                                break;
                            }
                        }

                        if (nearestPlanet != null) {
                            lastNode = nearestPlanet;
                            if (action == 1) {
                                actionLib.invokePlanetAction(null, nearestPlanet,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_SELECT);

                                repositMarker(nearestPlanet, markerNode);

                            } else if (action == 2 && !Hub.getLocalPlayer().hasLost()) {
                                actionLib.invokePlanetAction(null, nearestPlanet,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_ATTACK);
                            }

                        } else if (nearestShipGroup != null) {
                            lastNode = nearestShipGroup;
                            if (action == 1 && !Hub.getLocalPlayer().hasLost()) {
                                repositMarker(nearestShipGroup, markerNode);
                                actionLib.invokeShipAction(null, nearestShipGroup,
                                        Hub.getLocalPlayer(),
                                        Gameplay.SHIP_REDIRECT);
                            }

                        }

                        // Let's interact - we mark the hit with a red dot.
                        // markerNode.setLocalTranslation(closest.getContactPoint());

                        lastNode = closest.getGeometry().getParent();

                        AbstractPlanet p = null;
                        ShipGroup sg = null;
                        Node n = lastNode.getParent();

                        if (n instanceof AbstractPlanet) {
                            p = (AbstractPlanet) n;


                        } else if (n instanceof ShipGroup) {
                        }

                    } else {
                        // No hits? Then remove the mark.
//                        if (lastNode != null) {
//                            lastNode.detachChild(markerNode);
//                        }
                        // rootNode.detachChild(markerNode);
                    }
                }



                if ((action == 3 || action == 4) && !Hub.getLocalPlayer().hasLost()) {
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
     * Reposit marker.
     *
     * @param p the p
     */
    private void repositMarker(AbstractPlanet planet, MarkerNode markerNode) {
//        if (lastNode != null) {
//            lastNode.detachChild(markerNode);
//        }
        removeMarker(markerNode);

        planet.getTransformNode().attachChild(markerNode);

        float s = planet.getSize() * 2.6f;

        markerNode.setScale(s);

//        geometry.setLocalScale(s);
//        geometry.setLocalTranslation(-s / 2, 0, -s / 2);
        // marker.killAllParticles();
        // marker.setStartSize(p.getSize() + 0.2f);
        // marker.setEndSize(p.getSize() + 0.2f);
        // marker.emitAllParticles();
    }

    private void removeMarker(MarkerNode markerNode) {
        Node parent = markerNode.getParent();
        if (parent != null) {
            parent.detachChild(markerNode);
        }

    }

    /**
     * Reposit marker.
     *
     * @param g the g
     */
    private void repositMarker(ShipGroup shipGroup, MarkerNode markerNode) {
//        if (lastNode != null) {
//            lastNode.detachChild(markerNode);
//        }

        removeMarker(markerNode);

        shipGroup.getTransformNode().attachChild(markerNode);

        float s = shipGroup.getSize() * 2.0f;

        markerNode.setScale(s);
        // marker.killAllParticles();
        // marker.setStartSize(g.getSize()*8 + 0.2f);
        // marker.setEndSize(g.getSize()*8 + 0.2f);
        // marker.emitAllParticles();
    }

    private void selectPlanets(Rectangle2D rectangle) {
        Set<Entry<Integer, AbstractPlanet>> planetSet = Gameplay.getCurrentLevel().getPlanetSet();

        for (Map.Entry<Integer, AbstractPlanet> entry : planetSet) {
            Vector3f pos = entry.getValue().getPosition().clone();
            Point2D planetPos = new Point2D.Float(pos.x, pos.z);
            AbstractPlanet planet = entry.getValue();
            if (rectangle.contains(planetPos)) {
                if (planet.getOwner() == null || !planet.getOwner().equals(Hub.getLocalPlayer())) {
                    continue;
                }
                planetSelection.add(entry.getValue());
            }
        }
    }

    public ArrayList<AbstractPlanet> getSelectedPlanets() {
        ArrayList<AbstractPlanet> planets = new ArrayList<AbstractPlanet>(planetSelection);
        return planets;

    }

    void updateSelection(float tpf) {
        if (centerDrag != null) {
            gui.removeGUIElement(centerDrag);
            centerDrag = null;
        }
        if (leftDrag != null) {
            gui.removeGUIElement(leftDrag);
        }
        if (topDrag != null) {
            gui.removeGUIElement(topDrag);
        }
        if (rightDrag != null) {
            gui.removeGUIElement(rightDrag);
        }
        if (bottomDrag != null) {
            gui.removeGUIElement(bottomDrag);
        }
        if (!dragging || startScreenPos == null) {
//            if (startDrag != null) {
//                rootNode.detachChild(startDrag);
//            }
//            if (endDrag != null) {
//                rootNode.detachChild(endDrag);
//            }
            return;
        }
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        float t = -ray.getOrigin().y / ray.getDirection().y;
        Vector3f currentXZPlanePos =
                ray.getDirection().clone().
                mult(t).addLocal(ray.getOrigin().clone());
        float width = click2d.x - startScreenPos.x;
        float height = click2d.y - startScreenPos.y;

        ColorRGBA frame = ColorRGBA.Orange.clone();
        frame.a = 0.35f;
        ColorRGBA center = ColorRGBA.Orange.clone();
        center.a = 0.09f;
//        if (endDrag != null) {
//            rootNode.detachChild(endDrag);
//        }
//        endDrag = new Cross(assetManager);
//        endDrag.setLocalTranslation(currentXZPlanePos);
//        rootNode.attachChild(endDrag);

        //Vector2f centerDragPos = click2d.clone();

        centerDrag = new Panel(
                "centerSel",
                new Vector3f(
                startScreenPos.x + (width) * 0.5f,
                startScreenPos.y + (height) * 0.5f,
                0),
                new Vector2f(
                        Math.abs(width / 2), 
                        Math.abs(height / 2)),
                center);

        leftDrag = new Panel(
                "leftSel",
                new Vector3f(
                startScreenPos.x,
                startScreenPos.y + (height) * 0.5f,
                0),
                new Vector2f(
                1,
                Math.abs(height / 2)),
                frame);

        topDrag = new Panel(
                "topSel",
                new Vector3f(
                startScreenPos.x + (width) * 0.5f,
                startScreenPos.y,
                0),
                new Vector2f(
                Math.abs(width / 2),
                1),
                frame);

        rightDrag = new Panel(
                "rightSel",
                new Vector3f(
                click2d.x,
                startScreenPos.y + (height) * 0.5f,
                0),
                new Vector2f(
                1,
                Math.abs(height / 2)),
                frame);

        bottomDrag = new Panel(
                "bottomSel",
                new Vector3f(
                startScreenPos.x + (width) * 0.5f,
                click2d.y,
                0),
                new Vector2f(
                Math.abs(width / 2),
                1),
                frame);

        gui.addGUIElement(centerDrag);
        gui.addGUIElement(leftDrag);
        gui.addGUIElement(topDrag);
        gui.addGUIElement(rightDrag);
        gui.addGUIElement(bottomDrag);

        for (MarkerNode marker : markerNodes) {
            removeMarker(marker);
        }
        markerNodes.clear();
        lastXZPlanePos = currentXZPlanePos;
    }

    /**
     * Adds the shootable.
     *
     * @param spat the spat
     */
    public void addShootable(Spatial spat) {
        shootablesNode.attachChild(spat);
    }

    /**
     * Removes the shootable.
     *
     * @param spat the spat
     */
    public void removeShootable(Spatial spat) {
        shootablesNode.detachChild(spat);
    }

    void cleanUp() {
        lastXZPlanePos = null;
        startScreenPos = null;
        startXZPlanePos = null;
        if (centerDrag != null) {
            gui.removeGUIElement(centerDrag);
            centerDrag = null;
        }
        if (leftDrag != null) {
            gui.removeGUIElement(leftDrag);
            leftDrag = null;
        }
        if (topDrag != null) {
            gui.removeGUIElement(topDrag);
            topDrag = null;
        }
        if (rightDrag != null) {
            gui.removeGUIElement(rightDrag);
            rightDrag = null;
        }
        if (bottomDrag != null) {
            gui.removeGUIElement(bottomDrag);
            bottomDrag = null;
        }
//        if (startDrag != null) {
//            rootNode.detachChild(startDrag);
//        }
//        if (endDrag != null) {
//            rootNode.detachChild(endDrag);
//        }

        dragging = false;
    }

    private class MarkerNode extends Node {

        private Geometry geometry;

        public MarkerNode() {
            super("Marker Transform");

            Quad q = new Quad(1, 1);

            geometry = new Geometry("MarkerGeometry", q);
            Material material = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("ColorMap",
                    assetManager.loadTexture("Textures/gui/marker.png"));
            material.setColor("Color", ColorRGBA.Orange);
            material.setColor("GlowColor", ColorRGBA.Orange);

            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            geometry.setMaterial(material);

            /**
             * Objects with transparency need to be in the render bucket for
             * transparent objects:
             */
            geometry.setQueueBucket(Bucket.Translucent);

            float angles[] = {(float) -Math.PI / 2, (float) -Math.PI / 2, 0};

            // geometry.setLocalTranslation(-0.5f, 0, -0.5f);
            geometry.setLocalRotation(new Quaternion(angles));

            this.attachChild(geometry);

        }

        public void setScale(float s) {
            geometry.setLocalScale(s);
            geometry.setLocalTranslation(-s / 2, 0, -s / 2);
        }
    }
}
