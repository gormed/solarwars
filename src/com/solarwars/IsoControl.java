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
 * Type: com.solarwars.IsoControl
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars;

import com.jme3.app.state.AppStateManager;
import com.solarwars.gamestates.gui.DragRectangleGUI;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;
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
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.ShipGroup;
import com.solarwars.input.InputMappings;
import com.solarwars.logic.actions.ActionLib;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.Player;
import com.solarwars.settings.SolarWarsSettings;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jme3tools.optimize.GeometryBatchFactory;

/**
 * The Class IsoControl for the players interaction on screen. 
 * Handles main input through mouse clicks and drags, fires the events for 
 * planet and ship actions.
 * @author Hans Ferchland
 */
public class IsoControl {

    private static final boolean DEBUG_RAYCASTS =
            SolarWarsSettings.getInstance().isDEBUG_RAYCASTSEnabled();
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
    private IsoControl() {
        this.actionLib = ActionLib.getInstance();
        this.markerNode = new MarkerNode();
        initialize();
    }

    /**
     * Gets the instance of the singleton or creates it.
     * @return the one and only reference
     */
    public static IsoControl getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IsoControl();
    }
    //==========================================================================
    //      Private Fields
    //==========================================================================
    // general
    private Node rootNode = SolarWarsApplication.getInstance().getRootNode();
    private Node shootablesNode;
    private final AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
    private MarkerNode markerNode;
    // Dragging
    private boolean dragging = false;
    private Vector3f lastXZPlanePos;
    private Vector3f startXZPlanePos;
    private Vector2f startScreenPos = Vector2f.ZERO.clone();
    // planet & shipgroup selection
    private ArrayList<AbstractPlanet> planetSelection;
    private ArrayList<ShipGroup> shipGroupSelection;
    private ArrayList<MarkerNode> markerNodes;
    private boolean controlPressed = false;
    // Panels for rect
    private DragRectangleGUI dragRectangle;
    // debug
//    private Cross startDrag;
//    private Cross endDrag;
    private Camera cam = SolarWarsApplication.getInstance().getCamera();
    private ActionListener mouseActionListener;
    private ActionListener keyActionListener;
    private TouchListener touchListener; // kommt von Android
    private InputManager inputManager = SolarWarsApplication.getInstance().getInputManager();
    private ActionLib actionLib;
    private static final Logger logger = Logger.getLogger(IsoControl.class.getName());

    // ==========================================================================
    //      Methods
    //==========================================================================
    /**
     * Creates the dragging raectangle for selection geometry.
     * @param width 
     * @param height 
     * @param click2d 
     */
    public void createDragRectGeometry() {

        dragRectangle.setEnabled(true);

    }

    private void clearMultiMarkers() {
        if (markerNodes != null && !markerNodes.isEmpty()) {
            for (MarkerNode mn : markerNodes) {
                removeMarker(mn);
            }
            markerNodes.clear();
        }
    }

    /**
     * Initializes the players main controls.
     *
     * @param rootNode the root node
     */
    private void initialize() {
        dragRectangle = new DragRectangleGUI();
        AppStateManager stateManager = SolarWarsApplication.getInstance().getStateManager();
        stateManager.attach(dragRectangle);

        shootablesNode = new Node("Shootables");
        rootNode.attachChild(shootablesNode);

        // register action listener for right and left clicks 
        // and the mouse-weel
        mouseActionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {

                Vector2f point = inputManager.getCursorPosition();
                if (keyPressed) {
                    onButtonDown(name, point);
                } else {
                    if (onButtonUp(name, point)) {
                        return;
                    }
                }
                onMouseWeel(name);

            }
        };

        keyActionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(InputMappings.CONTROL_MODIFIER)) {
                    controlPressed = isPressed;
                }
            }
        };

        touchListener = new TouchListener() {

            @Override
            public void onTouch(String name, TouchEvent event, float tpf) {
                float x;
                float y;
                float pressure;
                if (event.getType() == TouchEvent.Type.DOWN) {
                    x = event.getX();
                    y = event.getY();
                    onDragSelectEntity(new Vector2f(x, y));
                    pressure = event.getPressure();
                } else if (event.getType() == TouchEvent.Type.UP) {
                    x = event.getX();
                    y = event.getY();
                    onAttackOrSelect(new Vector2f(x, y), false);
//                    onButtonDown(name);
                } else if (event.getType() == TouchEvent.Type.DOUBLETAP) {
                    x = event.getX();
                    y = event.getY();
                    onAttackOrSelect(new Vector2f(x, y), true);
                } else if (event.getType() == TouchEvent.Type.SCROLL) {
                    float deltaY = event.getDeltaY();
                    boolean up = (deltaY >= 0) ? true : false;
                    onPercentageChange(0.05f, up);
                }

//                Log.e("", "Event Type " + event.getType());
                event.setConsumed();
            }
        };
    }

    /**
     * Is executed if the player clicks on left or right mouse button
     * but not on its hold.
     */
    private void onButtonDown(String name, Vector2f point) {
        if (name.equals(InputMappings.LEFT_CLICK_SELECT)) {
            onDragSelectEntity(point);
            final String mouseDownMsg = "Left mouse-button down @["
                    + point.x + "/" + point.y + "]";
            logger.log(Level.INFO, mouseDownMsg);
        }
    }

    private void onDragSelectEntity(Vector2f point) {
        Vector2f click2d = point;
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        float t = -ray.getOrigin().y / ray.getDirection().y;
        Vector3f currentXZPlanePos = ray.getDirection().clone().mult(t).addLocal(ray.getOrigin().clone());
        if (!dragging) {
            startXZPlanePos = currentXZPlanePos;
            startScreenPos = click2d.clone();
            planetSelection = new ArrayList<AbstractPlanet>();
            shipGroupSelection = new ArrayList<ShipGroup>();
            clearMultiMarkers();
            updateDragRect(click2d);
            dragRectangle.show();
//            centerDrag.setVisible(true);
//            leftDrag.setVisible(true);
//            topDrag.setVisible(true);
//            rightDrag.setVisible(true);
//            bottomDrag.setVisible(true);
            markerNodes = new ArrayList<MarkerNode>();
            //                        startDrag = new Cross(assetManager);
            //                        startDrag.setLocalTranslation(startXZPlanePos);
            //                        rootNode.attachChild(startDrag);
        }
        dragging = true;
    }

    /**
     * Is executed if the mouse-weel is scrolled.
     */
    private void onMouseWeel(String name) {
        Player local = Hub.getLocalPlayer();
        if (!(name.equals(InputMappings.PERCENT_DOWN)
                || name.equals(InputMappings.PERCENT_UP))) {
            return;
        }
        boolean down = (name.equals(InputMappings.PERCENT_DOWN)) ? true : false;

        float amount = 0.05f;
        onPercentageChange(amount, down);
        final String percentageChangeS = local.getName() + " changed percentage to " + String.format("%3.0f", local.getShipPercentage() * 100f) + "%";
        logger.log(Level.FINE, percentageChangeS);
    }
    //TODO MB Auslagern

    private void onPercentageChange(float amount, boolean down) {
        if (!down) {
            amount *= -1.0f;
        }
        Hub.getLocalPlayer().refreshShipPercentage(amount);
    }

    /**
     * Is executed if the left or right mouse button is released.
     * This can end in a drag action that ended or a normal left click
     * for selection or right click for attack.
     */
    private boolean onButtonUp(String name, Vector2f point) {
        boolean attack = (name.equals(InputMappings.LEFT_CLICK_SELECT)
                && (name.equals(InputMappings.LEFT_CLICK_SELECT)
                || name.equals(InputMappings.RIGHT_CLICK_ATTACK)))
                ? false : true;
        if (name.equals(InputMappings.LEFT_CLICK_SELECT)
                || name.equals(InputMappings.RIGHT_CLICK_ATTACK)) {
            return onAttackOrSelect(point, attack);
        }
        return false;
    }

    /**
     * Is executed if it is a normal left click
     * for selection or right click for attack.
     */
    private boolean playerReleases(CollisionResults results, boolean attack) {
        // use the results and check if there was something hit
        if (results.size() > 0) {
            CollisionResult actual = null;
            ShipGroup nearestShipGroup = null;
            AbstractPlanet nearestPlanet = null;
            Node temp = null;
            // order the collisions so that at 0 is the closest
            results.getClosestCollision();
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
                    }
                }
            }
            // was planet found
            //<editor-fold defaultstate="collapsed" desc="Planet Found Section">
            if (nearestPlanet != null) {
                // ...check if left button click
                if (!attack) {
                    // invoke select action
                    actionLib.invokePlanetAction(
                            null,
                            0L,
                            nearestPlanet,
                            Hub.getLocalPlayer(),
                            DeathmatchGameplay.PLANET_SELECT);
                    // finally set marker
                    repositMarker(nearestPlanet, markerNode);
                    return true;

                } // ...check for right botton click
                // TODO: move hasLost check to actionLib
                else if (attack && !Hub.getLocalPlayer().hasLost()) {
                    // invoke attack action
                    actionLib.invokePlanetAction(
                            null,
                            0L,
                            nearestPlanet,
                            Hub.getLocalPlayer(),
                            DeathmatchGameplay.PLANET_ATTACK);
                    return true;
                }
            } else {
                actionLib.invokePlanetAction(
                        null,
                        0L,
                        null,
                        Hub.getLocalPlayer(),
                        DeathmatchGameplay.PLANET_SELECT);
                removeMarker(markerNode);
                clearMultiMarkers();
                return true;
            }
            //</editor-fold>
            // was shipgroup found
            //<editor-fold defaultstate="collapsed" desc="ShipGroup Found Section">
            if (nearestShipGroup != null) {
                // ...check if left button click
                // TODO: move hasLost check to actionLib
                if (!attack && !Hub.getLocalPlayer().hasLost()) {

                    // invoke ship redirect action
                    actionLib.invokeShipAction(
                            null,
                            0,
                            nearestShipGroup,
                            Hub.getLocalPlayer(),
                            DeathmatchGameplay.SHIP_SELECT);
                    repositMarker(nearestShipGroup, markerNode);
                    return true;
                }
            }
            //</editor-fold>
        }
        actionLib.invokePlanetAction(
                null,
                0L,
                null,
                Hub.getLocalPlayer(),
                DeathmatchGameplay.PLANET_SELECT);
        removeMarker(markerNode);
        clearMultiMarkers();
        return false;
    }

    /**
     * Prints the results for a ray cast action ordered from nearest to
     * farest.
     */
    private void printRaycastResults(CollisionResults results) {
        // 4. Print the results
        if (results.size() <= 0) {
            logger.log(Level.FINE, "Nothing was hit in raycasting!", results);
            return;
        }
        logger.log(Level.FINE, "There were " + results.size() + " hits! If logging FINER see below:", results);
        String hits = "";
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of
            // geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();
            hits += "* Collision #" + i;
            hits += " - You shot " + hit + " at " + pt
                    + ", " + dist + " wu away.\n";
        }
        logger.log(Level.FINER, hits, results);
    }

    /**
     * Is executed if a player ends mouse-dragging to collect the plantes
     * that where selected.
     */
    private boolean playerEndsDragEntities() {
        // if player was dragging and releases the mouse to select planets
        if (startXZPlanePos != null
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

                    MarkerNode markerNodeLoc = null;
                    if (!planetSelection.isEmpty()) {
                        for (AbstractPlanet planet : planetSelection) {

                            markerNodeLoc = new MarkerNode();
                            markerNodes.add(markerNodeLoc);
                            repositMarker(planet, markerNodeLoc);

                        }
                        actionLib.invokePlanetAction(
                                IsoControl.getInstance(),
                                0L,
                                null,
                                Hub.getLocalPlayer(),
                                DeathmatchGameplay.PLANET_MULTI_SELECT);
                        final String multiSelectMsg = "Player multi selected " + planetSelection.size() + " planet(s).";
                        logger.info(multiSelectMsg);
                    }
                } else {
                    selectShipGroups(rect);

                    MarkerNode markerNodeLoc = null;
                    if (!shipGroupSelection.isEmpty()) {
                        for (ShipGroup shipGroup : shipGroupSelection) {

                            markerNodeLoc = new MarkerNode();
                            markerNodes.add(markerNodeLoc);
                            repositMarker(shipGroup, markerNodeLoc);

                        }
                        actionLib.invokeShipAction(
                                IsoControl.getInstance(),
                                0L,
                                null,
                                Hub.getLocalPlayer(),
                                DeathmatchGameplay.SHIP_MULTI_SELECT);
                        final String multiSelectMsg = "Player multi selected " + shipGroupSelection.size() + " shipgroup(s).";
                        logger.info(multiSelectMsg);
                    }
                }
                //

                lastXZPlanePos = null;
                startScreenPos = null;
                startXZPlanePos = null;
//                centerDrag.setVisible(false);
//                leftDrag.setVisible(false);
//                topDrag.setVisible(false);
//                rightDrag.setVisible(false);
//                bottomDrag.setVisible(false);

                dragRectangle.hide();
                return true;
            }
            dragRectangle.hide();
        }
        return false;
    }

    private boolean onAttackOrSelect(Vector2f point, boolean attack) {
        // reset drag flag
        dragging = false;
        // clear collision results
        CollisionResults results = new CollisionResults();
        // get 3d mouse position
        Vector2f click2d = point;
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // create ray for raycasting
        Ray ray = new Ray(click3d, dir);
        // check if play ended a drag and leave if so
        if (playerEndsDragEntities()) {
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
        if (playerReleases(results, attack)) {
            return true;
        }
        return false;
    }

    public void addControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.addListener(mouseActionListener,
                    InputMappings.LEFT_CLICK_SELECT,
                    InputMappings.RIGHT_CLICK_ATTACK,
                    InputMappings.PERCENT_DOWN,
                    InputMappings.PERCENT_UP);

            inputManager.addListener(keyActionListener,
                    InputMappings.CONTROL_MODIFIER);

            inputManager.addListener(touchListener, new String[]{"Touch"});
        }
    }

    public void removeControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.removeListener(mouseActionListener);
            inputManager.removeListener(keyActionListener);
        }
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
        removeMarker(markerNode);
        planet.getTransformNode().attachChild(markerNode);
        markerNode.setPlanet(planet);
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
        removeMarker(markerNode);
        shipGroup.getTransformNode().attachChild(markerNode);
        markerNode.setShipGroup(shipGroup);
    }

    /**
     * Gets all planets contained in a 2D rectangle on the xz plane and adds all
     * to the planetSelection.
     *
     * @param rectangle the rectangle
     */
    private void selectPlanets(Rectangle2D rectangle) {

        // gets the set of plantes as a ref
        Set<Entry<Integer, AbstractPlanet>> planetSet = SolarWarsGame.getInstance().getCurrentLevel().getPlanetSet();
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
        Set<Entry<Integer, ShipGroup>> planetSet = SolarWarsGame.getInstance().getCurrentLevel().getShipGroupSet();
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
     * @param tpf (the Time per Frame)
     */
    void updateSelection(float tpf) {
        if (markerNode != null) {
            markerNode.updateMarker(tpf);
        }
        if (markerNodes != null && !markerNodes.isEmpty()) {
            for (MarkerNode marker : markerNodes) {
                marker.updateMarker(tpf);
            }
        }

        if (!dragging || startScreenPos == null) {
            return;
        }
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).
                subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        float t = -ray.getOrigin().y / ray.getDirection().y;
        Vector3f currentXZPlanePos =
                ray.getDirection().clone().
                mult(t).addLocal(ray.getOrigin().clone());

        updateDragRect(click2d);

        lastXZPlanePos = currentXZPlanePos;
    }

    private void updateDragRect(Vector2f click2d) {
        if (dragRectangle == null) {
            return;
        }

        float width = click2d.x - startScreenPos.x;
        float height = click2d.y - startScreenPos.y;

        dragRectangle.setWidth((int) width);
        dragRectangle.setHeight((int) height);
//        dragRectangle.setPaddingTop(
//                new SizeValue((int) (startScreenPos.y + (height) * 0.5f)
//                + "px"));
//
//        dragRectangle.setPaddingLeft(
//                new SizeValue((int) (startScreenPos.x + (width) * 0.5f)
//                + "px"));
        dragRectangle.setX((int) (startScreenPos.x));
        dragRectangle.setY((int) (startScreenPos.y));
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

        dragging = false;

    }

    /**
     * The Class MarkerNode.
     */
    private class MarkerNode extends Node {

        public static final float MARKER_PLANET_ADJUST = 0.0f;
        /** The Constant SELECTION_ANIMATION_SPEED. */
        public static final int SELECTION_ANIMATION_SPEED = 2;
        /** The scale. */
        private float scale;
        /** The fade scale. */
        private float fadeScale;
        /** The running. */
        private float running;
        /** The material. */
        private Material markerMaterial;
        /** The geometry. */
        private Geometry markerGeometry;
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
        private float rangeFade = 0;
        private float range = 1;
        private Spatial rangeBatch;
        private Node rangeNode;
        private ColorRGBA rangeColor = ColorRGBA.White.clone();
        private Material rangeMaterial;
        private Geometry rangeCylinder;
        private ShipGroup shipGroup;
        private AbstractPlanet planet;

        /**
         * Instantiates a new marker node.
         */
        public MarkerNode() {
            super("Marker Transform");

            createMarker();
            createRange();
        }

        private void createMarker() {
            Quad q = new Quad(1, 1);

            markerGeometry = new Geometry("MarkerGeometry", q);
            markerMaterial = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            markerMaterial.setTexture("ColorMap",
                    assetManager.loadTexture("Textures/gui/marker.png"));
            markerMaterial.setColor("Color", start);
            currentFadeColor = start;
            markerMaterial.setColor("GlowColor", ColorRGBA.White);

            markerMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            markerGeometry.setMaterial(markerMaterial);

            /**
             * Objects with transparency need to be in the render bucket for
             * transparent objects:
             */
            markerGeometry.setQueueBucket(Bucket.Translucent);

            float angles[] = {(float) -Math.PI / 2, (float) -Math.PI / 2, 0};

            // geometry.setLocalTranslation(-0.5f, 0, -0.5f);
            markerGeometry.setLocalRotation(new Quaternion(angles));

            this.attachChild(markerGeometry);
        }

        /**
         * Creates the bounding-volume for the range-checks.
         * @param game the MazeTDGame singleton
         */
        private void createRange() {
            rangeNode = new Node("RangeNode");
            Cylinder c = new Cylinder(
                    2,
                    15,
                    1, MARKER_PLANET_ADJUST,
                    true);

            rangeMaterial = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            rangeMaterial.setColor("Color", new ColorRGBA(MARKER_PLANET_ADJUST, MARKER_PLANET_ADJUST, 1, MARKER_PLANET_ADJUST));
            rangeMaterial.getAdditionalRenderState().
                    setBlendMode(BlendMode.Alpha);

            rangeMaterial.getAdditionalRenderState().setDepthWrite(false);

//            rangeMaterial.getAdditionalRenderState().setWireframe(true);
            rangeMaterial.getAdditionalRenderState().setDepthWrite(false);
            float[] angles = {(float) Math.PI / 2, 0, 0};

            rangeCylinder = new Geometry("CollisionCylinderGeometry", c);

            rangeCylinder.setLocalTranslation(0, -MARKER_PLANET_ADJUST + ((float) Math.random() * MARKER_PLANET_ADJUST), 0);
            rangeCylinder.setLocalRotation(new Quaternion(angles));

            rangeCylinder.setQueueBucket(Bucket.Transparent);
            rangeNode.attachChild(rangeCylinder);

            rangeBatch = GeometryBatchFactory.optimize(rangeNode);
            rangeBatch.setMaterial(rangeMaterial);

            this.attachChild(rangeBatch);
        }

        /**
         * Updates the marker.
         *
         * @param tpf the tpf
         */
        public void updateMarker(float tpf) {
            updateScaleFade(tpf);
            updateColorFade(tpf);
            updateRange(tpf);
        }

        private void updateRange(float tpf) {
            // Update range geometry
            if (planet != null) {
                range = planet.getRange();

            } else if (shipGroup != null) {
                range = shipGroup.getRange();
            }
            rangeBatch.setLocalScale(range);
            // Update range opacity
            this.rangeFade += tpf;
            if (rangeFade > Math.PI) {
                rangeFade = 0;
            }
            float alpha = (0.0125f * (5f + (float) Math.sin((4f * rangeFade))));
            rangeColor.a = alpha;
            rangeMaterial.setColor("Color", rangeColor);
        }

        private void updateScaleFade(float tpf) {
            running += tpf;

            if (running > 2 * Math.PI) {
                running = 0;
            }
            // size
            fadeScale = 0.05f * (float) Math.sin((double) running * SELECTION_ANIMATION_SPEED) + scale + MARKER_PLANET_ADJUST;
            markerGeometry.setLocalScale(fadeScale);
            markerGeometry.setLocalTranslation(-fadeScale / 2, 0, -fadeScale / 2);
        }

        private void updateColorFade(float tpf) {
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
                    markerMaterial.setColor("Color", currentFadeColor);
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
                    markerMaterial.setColor("Color", currentFadeColor);
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
         * @param scale the new scale
         */
        private void setScaleAndRange(float scale, float range) {
            this.scale = scale;
            this.range = range;
            markerMaterial.setColor("Color", start);
            currentFadeColor = start.clone();
            fadeScale = 0;
            tick = 0;
            fadeDir = true;
            markerGeometry.setLocalScale(scale);
            markerGeometry.setLocalTranslation(-scale / 2, 0, -scale / 2);
        }

        public void setPlanet(AbstractPlanet abstractPlanet) {
            this.shipGroup = null;
            this.planet = abstractPlanet;
            float s = planet.getSize() * 2.6f;
            setScaleAndRange(s, planet.getRange());
            rangeBatch.setCullHint(
                    (planet.getOwner() == null) ? CullHint.Always : CullHint.Never);
            if (planet.getOwner() != null) {
                ColorRGBA c = planet.getOwner().getColor().clone();
                rangeColor = c;
//                rangeColor.a = 0.05f;
                rangeMaterial.setColor("Color", rangeColor);
            }
        }

        public void setShipGroup(ShipGroup group) {
            this.planet = null;
            this.shipGroup = group;
            float s = shipGroup.getSize() * 2.0f;
            setScaleAndRange(s, shipGroup.getRange());
            if (shipGroup.getOwner() != null) {
                ColorRGBA c = shipGroup.getOwner().getColor().clone();
                rangeColor = c;
//                rangeColor.a = 0.05f;
                rangeMaterial.setColor("Color", rangeColor);
            }
        }
    }
}
