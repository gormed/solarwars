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
 * File: IsoControl.java
 * Type: solarwars.IsoControl
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
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
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import entities.AbstractPlanet;
import entities.ShipGroup;
import gui.GameGUI;
import gui.elements.Panel;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import logic.ActionLib;
import logic.Gameplay;

/**
 * The Class IsoControl for the players interaction on screen. 
 * Handles main input through mouse clicks and drags, fires the events for 
 * planet and ship actions.
 * @author Hans Ferchland
 */
public class IsoControl {

    /** The Constant DEBUG_RAYCASTS. */
    private static final boolean DEBUG_RAYCASTS = false;
    //==========================================================================
    //      Singleton
    //==========================================================================
    /** The one and only reference. */
    private static IsoControl instance;

    /**
     * Instantiates a new iso control.
     *
     * @param application the application
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
     * Gets the instance of the singleton or creates it.
     * @return the one and only reference
     */
    public static IsoControl getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IsoControl(SolarWarsApplication.getInstance());
    }
    //==========================================================================
    //      Private Fields
    //==========================================================================
    // general
    /** The root node. */
    private Node rootNode;
    /** The shootables node. */
    private Node shootablesNode;
    /** inner ref to the asset manager. */
    private final AssetManager assetManager;
    /** The marker node. */
    private MarkerNode markerNode;
    // Dragging
    /** The dragg ing flag indicates if currently dragging. */
    private boolean dragging = false;
    /** The last xz plane pos. */
    private Vector3f lastXZPlanePos;
    /** The start xz plane pos. */
    private Vector3f startXZPlanePos;
    /** The start screen pos. */
    private Vector2f startScreenPos;
    // planet & shipgroup selection
    /** The planet selection. */
    private ArrayList<AbstractPlanet> planetSelection;
    /** The shipgroup selection. */
    private ArrayList<ShipGroup> shipGroupSelection;
    /** The marker nodes. */
    private ArrayList<MarkerNode> markerNodes;
    private boolean controlPressed = false;
    // Panels for rect
    /** The left drag. */
    private Panel leftDrag;
    /** The top drag. */
    private Panel topDrag;
    /** The right drag. */
    private Panel rightDrag;
    /** The bottom drag. */
    private Panel bottomDrag;
    /** The center drag. */
    private Panel centerDrag;
    // debug
//    private Cross startDrag;
//    private Cross endDrag;
    /** The cam. */
    private Camera cam;
    /** The mouse action listener. */
    private ActionListener mouseActionListener;
    /** The key action listener. */
    private ActionListener keyActionListener;
    /** The input manager. */
    private InputManager inputManager;
    /** The action lib. */
    private ActionLib actionLib;
    /** inner ref to the games gui. */
    private GameGUI gui;
    //==========================================================================
    //      Methods
    //==========================================================================

    /**
     * Initializes the players main controls.
     *
     * @param rootNode the root node
     */
    private void initialize(final Node rootNode) {
        shootablesNode = new Node("Shootables");
        rootNode.attachChild(shootablesNode);
        // register action listener for right and left clicks 
        // and the mouse-weel
        mouseActionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {

                gui = Gameplay.getCurrentLevel().getGUI();

                if (keyPressed) {
                    onButtonDown(name);
                } else {
                    if (onButtonUp(name)) {
                        return;
                    }
                }
                onMouseWeel(name);

            }

            /**
             * Is executed if the player clicks on left or right mouse button
             * but not on its hold.
             */
            private void onButtonDown(String name) {
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
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
                        shipGroupSelection = new ArrayList<ShipGroup>();
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
            }

            /**
             * Is executed if the mouse-weel is scrolled.
             */
            private void onMouseWeel(String name) {
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_DOWN)) {
                    Hub.getLocalPlayer().refreshShipPercentage(0.05f);
                }
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_WHEEL_UP)) {
                    Hub.getLocalPlayer().refreshShipPercentage(-0.05f);
                }
            }

            /**
             * Is executed if the left or right mouse button is released.
             * This can end in a drag action that ended or a normal left click
             * for selection or right click for attack.
             */
            private boolean onButtonUp(String name) {
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)
                        || name.equals(SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK)) {
                    // reset drag flag
                    dragging = false;
                    // clear collision results
                    CollisionResults results = new CollisionResults();
                    // get 3d mouse position
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    // create ray for raycasting
                    Ray ray = new Ray(click3d, dir);
                    // check if play ended a drag and leave if so
                    if (playerEndsDrag(name)) {
                        return true;
                    }

                    // RAYCASTING
                    // Collect intersections between Ray and Shootables in results list.
                    shootablesNode.collideWith(ray, results);
                    // print if in debug
                    if (DEBUG_RAYCASTS) {
                        printRaycastResults(results);
                    }
                    // if player releases not having dragged before
                    if (playerReleases(results, name)) {
                        return true;
                    }
                }
                return false;
            }

            /**
             * Is executed if it is a normal left click
             * for selection or right click for attack.
             */
            private boolean playerReleases(CollisionResults results, String name) {
                // use the results and check if there was something hit
                if (results.size() > 0) {
                    CollisionResult actual = null;
                    ShipGroup nearestShipGroup = null;
                    AbstractPlanet nearestPlanet = null;
                    Node temp = null;
                    // order the collisions so that at 0 is the closest
                    results.getClosestCollision();
//                    temp = closest.getGeometry().getParent().getParent();
//                    // if we got an abstract planet here
//                    if (temp != null && temp instanceof AbstractPlanet) {
//                        nearestPlanet = (AbstractPlanet) temp;
//                        lastNode = nearestPlanet;
//                        if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
//                            actionLib.invokePlanetAction(null, nearestPlanet,
//                                    Hub.getLocalPlayer(),
//                                    Gameplay.PLANET_SELECT);
//                            repositMarker(nearestPlanet, markerNode);
//                            return true;
//                        } else if (name.equals(SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK)
//                                && !Hub.getLocalPlayer().hasLost()) {
//                            actionLib.invokePlanetAction(null, nearestPlanet,
//                                    Hub.getLocalPlayer(),
//                                    Gameplay.PLANET_ATTACK);
//                            return true;
//                        }
//                    }
                    // iterate through collisions begin with the closest
                    for (int i = 0; i < results.size(); i++) {
                        // currently iterated collision
                        actual = results.getCollision(i);
                        // get the node that is parent of the node that holds
                        // the geometry, that is either a planet or a
                        // shipgroup
                        temp = actual.getGeometry().getParent().getParent();
                        // if that thing is not null
                        if (temp != null) {
                            // check if its a planet
                            if (temp instanceof AbstractPlanet) {
                                nearestPlanet = (AbstractPlanet) temp;
                                // if planet found first, break
                                break;
                            } else if (temp instanceof ShipGroup) {
                                nearestShipGroup = (ShipGroup) temp;
                                // if shipgroup found first, break
                                break;
                            }
                        }
                    }
                    // was planet found
                    if (nearestPlanet != null) {
                        // ...check if left button click
                        if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
                            // invoke select action
                            actionLib.invokePlanetAction(
                                    null,
                                    0L,
                                    nearestPlanet,
                                    Hub.getLocalPlayer(),
                                    Gameplay.PLANET_SELECT);
                            // finally set marker
                            repositMarker(nearestPlanet, markerNode);

                        } // ...check for reight botton click 
                        // TODO: move hasLost check to actionLib
                        else if (name.equals(SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK)
                                && !Hub.getLocalPlayer().hasLost()) {
                            // invoke attack action
                            actionLib.invokePlanetAction(
                                    null, 
                                    0L,
                                    nearestPlanet,
                                    Hub.getLocalPlayer(),
                                    Gameplay.PLANET_ATTACK);
                        }

                    } // was shipgroup found
                    else if (nearestShipGroup != null) {
                        // ...check if left button click
                        // TODO: move hasLost check to actionLib
                        if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)
                                && !Hub.getLocalPlayer().hasLost()) {
                            repositMarker(nearestShipGroup, markerNode);
                            // invoke ship redirect action
                            actionLib.invokeShipAction(
                                    null, 
                                    0,
                                    nearestShipGroup,
                                    Hub.getLocalPlayer(),
                                    Gameplay.SHIP_SELECT);
                        }

                    }
//                    if (actual != null) {
//                        // Let's interact - we mark the hit with a red dot.
//                        // markerNode.setLocalTranslation(closest.getContactPoint());
//                        lastNode = actual.getGeometry().getParent();
//                        AbstractPlanet p = null;
//                        ShipGroup sg = null;
//                        Node n = lastNode.getParent();
//                        if (n instanceof AbstractPlanet) {
//                            p = (AbstractPlanet) n;
//
//
//                        } else if (n instanceof ShipGroup) {
//                        }
//                    }
                }
                return false;
            }

            /**
             * Prints the results for a ray cast action ordered from nearest to
             * farest.
             */
            private void printRaycastResults(CollisionResults results) {
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
            }

            /**
             * Is executed if a player ends mouse-dragging to collect the plantes
             * that where selected. 
             */
            private boolean playerEndsDrag(String name) {
                // if player was dragging and releases the mouse to select planets
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)
                        && startXZPlanePos != null
                        && lastXZPlanePos != null) {
                    //  rootNode.detachChild(startDrag);
                    // calculate width and height of the drag rect
                    float width = Math.abs(lastXZPlanePos.x - startXZPlanePos.x);
                    float height = Math.abs(lastXZPlanePos.z - startXZPlanePos.z);
                    // if the rect is big enough
                    if (width > 0.2f && height > 0.2f) {
                        // remove old marker of player
                        removeMarker(markerNode);
                        // get the left-top coordinate via 1337 confusing conditional assignment
                        float leftX = (startXZPlanePos.x <= lastXZPlanePos.x) ? startXZPlanePos.x : lastXZPlanePos.x;
                        float topY = (startXZPlanePos.z <= lastXZPlanePos.z) ? startXZPlanePos.z : lastXZPlanePos.z;
                        // create the rectangle from it
                        Rectangle2D rect = new Rectangle2D.Float(
                                leftX, topY, width, height);
                        if (!isControlPressed()) {
                            // select all planets in rectangle
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
                                        0L,
                                        null,
                                        Hub.getLocalPlayer(),
                                        Gameplay.PLANET_MULTI_SELECT);

                            }
                        } else {
                            selectShipGroups(rect);

                            MarkerNode markerNode = null;
                            if (!shipGroupSelection.isEmpty()) {
                                for (ShipGroup shipGroup : shipGroupSelection) {

                                    markerNode = new MarkerNode();
                                    markerNodes.add(markerNode);
                                    repositMarker(shipGroup, markerNode);

                                }
                                actionLib.invokeShipAction(
                                        IsoControl.getInstance(),
                                        0L,
                                        null,
                                        Hub.getLocalPlayer(),
                                        Gameplay.SHIP_MULTI_SELECT);

                            }
                        }
                        //

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
                        return true;
                    }
                }
                return false;
            }
        };

        keyActionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CTRL)) {
                    controlPressed = isPressed;
                }
            }
        };

    }

    public void addControlListener() {
        inputManager.addListener(mouseActionListener,
                SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK,
                SolarWarsApplication.INPUT_MAPPING_RIGHT_CLICK,
                SolarWarsApplication.INPUT_MAPPING_WHEEL_DOWN,
                SolarWarsApplication.INPUT_MAPPING_WHEEL_UP);
        inputManager.addListener(keyActionListener,
                SolarWarsApplication.INPUT_MAPPING_LEFT_CTRL);
    }

    public void removeControlListener() {
        inputManager.removeListener(mouseActionListener);
        inputManager.removeListener(keyActionListener);
    }

    public boolean isControlPressed() {
        return controlPressed;
    }

    /**
     * Gets the shootables node.
     *
     * @return the shootables node
     */
    public Node getShootablesNode() {
        return shootablesNode;
    }

    /**
     * Reposit the marker on a selected planet.
     *
     * @param planet the planet
     * @param markerNode the marker node
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

    /**
     * Remove marker completly fron current planet.
     *
     * @param markerNode the marker node
     */
    private void removeMarker(MarkerNode markerNode) {
        Node parent = markerNode.getParent();
        if (parent != null) {
            parent.detachChild(markerNode);
        }

    }

    /**
     * Reposit marker.
     *
     * @param shipGroup the ship group
     * @param markerNode the marker node
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

    /**
     * Gets all planets contained in a 2D rectangle on the xz plane and adds all
     * to the planetSelection.
     *
     * @param rectangle the rectangle
     */
    private void selectPlanets(Rectangle2D rectangle) {
        // gets the set of plantes as a ref
        Set<Entry<Integer, AbstractPlanet>> planetSet = Gameplay.getCurrentLevel().getPlanetSet();
        // iterate through the sets planets each
        for (Map.Entry<Integer, AbstractPlanet> entry : planetSet) {
            // get planet pos
            Vector3f pos = entry.getValue().getPosition().clone();
            // convert to 2D point
            Point2D planetPos = new Point2D.Float(pos.x, pos.z);
            // get the planet ref
            AbstractPlanet planet = entry.getValue();
            // check if planet pos is in rectangle
            if (rectangle.contains(planetPos)) {
                // if not owned by local player get the next planet in the planet set
                if (planet.getOwner() == null || !planet.getOwner().equals(Hub.getLocalPlayer())) {
                    continue;
                }
                // else add planet to the selection
                planetSelection.add(planet);
            }
        }
    }

    /**
     * Gets all ShipGroup contained in a 2D rectangle on the xz plane and adds all
     * to the shipGroupSelection.
     *
     * @param rectangle the rectangle
     */
    private void selectShipGroups(Rectangle2D rectangle) {
        // gets the set of plantes as a ref
        Set<Entry<Integer, ShipGroup>> planetSet = Gameplay.getCurrentLevel().getShipGroupSet();
        // iterate through the sets planets each
        for (Map.Entry<Integer, ShipGroup> entry : planetSet) {
            // get planet pos
            Vector3f pos = entry.getValue().getPosition().clone();
            // convert to 2D point
            Point2D shipGroupPos = new Point2D.Float(pos.x, pos.z);
            // get the planet ref
            ShipGroup shipGroup = entry.getValue();
            // check if planet pos is in rectangle
            if (rectangle.contains(shipGroupPos)) {
                // if not owned by local player get the next planet in the planet set
                if (shipGroup.getOwner() == null || !shipGroup.getOwner().equals(Hub.getLocalPlayer())) {
                    continue;
                }
                // else add planet to the selection
                shipGroupSelection.add(shipGroup);
            }
        }
    }

    /**
     * Gets a copy of the selected planets.
     *
     * @return the selected planets
     */
    public ArrayList<AbstractPlanet> getSelectedPlanets() {
        ArrayList<AbstractPlanet> planets = new ArrayList<AbstractPlanet>(planetSelection);
        return planets;

    }

    /**
     * Gets a copy of the selected ShipGroups.
     *
     * @return the selected planets
     */
    public ArrayList<ShipGroup> getSelectedShipGroups() {
        ArrayList<ShipGroup> sgs = new ArrayList<ShipGroup>(shipGroupSelection);
        return sgs;

    }

    /**
     * Updates the drag-rectangle and/or marker-animation.
     *
     * @param tpf the tpf
     */
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
        if (markerNode != null) {
            markerNode.updateMarker(tpf);
        }
        if (markerNodes != null && !markerNodes.isEmpty()) {
            for (MarkerNode marker : markerNodes) {
                marker.updateMarker(tpf);
            }
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

    /**
     * Clean up.
     */
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

    /**
     * The Class MarkerNode.
     */
    private class MarkerNode extends Node {

        /** The Constant SELECTION_ANIMATION_SPEED. */
        public static final int SELECTION_ANIMATION_SPEED = 2;
        /** The scale. */
        private float scale;
        /** The fade scale. */
        private float fadeScale;
        /** The running. */
        private float running;
        /** The material. */
        private Material material;
        /** The geometry. */
        private Geometry geometry;
        /** The start. */
        private ColorRGBA start = ColorRGBA.Orange.clone();
        /** The end. */
        private ColorRGBA end = ColorRGBA.White.clone();
        /** The current fade color. */
        private ColorRGBA currentFadeColor = start.clone();
        /** The fade dir. */
        private boolean fadeDir = true;
        /** The tick. */
        private float tick = 0f;

        /**
         * Instantiates a new marker node.
         */
        public MarkerNode() {
            super("Marker Transform");

            Quad q = new Quad(1, 1);

            geometry = new Geometry("MarkerGeometry", q);
            material = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("ColorMap",
                    assetManager.loadTexture("Textures/gui/marker.png"));
            material.setColor("Color", start);
            currentFadeColor = start;
            material.setColor("GlowColor", ColorRGBA.White);

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

        /**
         * Updates the marker.
         *
         * @param tpf the tpf
         */
        public void updateMarker(float tpf) {
            running += tpf;
            if (running > 2 * Math.PI) {
                running = 0;
            }
            // size
            fadeScale = 0.05f * (float) Math.sin((double) running * SELECTION_ANIMATION_SPEED) + scale + 0.02f;
            geometry.setLocalScale(fadeScale);
            geometry.setLocalTranslation(-fadeScale / 2, 0, -fadeScale / 2);
//            if (fadeScale > scale + 0.1f) {
//
//                // size
//                fadeScale = 0.1f * (float) Math.sin((double) running);
//                geometry.setLocalScale(fadeScale);
//                geometry.setLocalTranslation(-fadeScale / 2, 0, -fadeScale / 2);
//            } else if (fadeScale < scale - 0.1f) {
//                // size
//                fadeScale += 0.001f;
//                geometry.setLocalScale(fadeScale);
//                geometry.setLocalTranslation(-fadeScale / 2, 0, -fadeScale / 2);
//            }
            if (tick > 0.005f) {
                if (fadeDir) {

                    // color
                    currentFadeColor = currentFadeColor.add(
                            new ColorRGBA(0.01f, 0.01f, 0.01f, 0));
                    material.setColor("Color", currentFadeColor);
                    if (currentFadeColor.r >= 1f && currentFadeColor.g >= 1f && currentFadeColor.b >= 1) {
                        fadeDir = false;
                    }
                } else {
                    // color
                    if (currentFadeColor.g > start.g) {
                        currentFadeColor.g -= 0.01f;
                    }
                    if (currentFadeColor.b > start.b) {
                        currentFadeColor.b -= 0.01f;
                    }
                    material.setColor("Color", currentFadeColor);
                    if (currentFadeColor.g <= 0.5 && currentFadeColor.b <= 0) {
                        currentFadeColor = start.clone();
                        fadeDir = true;
                    }
                }
                //currentFadeColor = start.

                tick = 0;
            }

            tick += tpf;

        }

        /**
         * Sets the scale.
         *
         * @param s the new scale
         */
        public void setScale(float s) {
            scale = s;
            material.setColor("Color", start);
            currentFadeColor = start.clone();
            fadeScale = 0;
            tick = 0;
            fadeDir = true;
            geometry.setLocalScale(scale);
            geometry.setLocalTranslation(-scale / 2, 0, -scale / 2);
        }
    }
}
