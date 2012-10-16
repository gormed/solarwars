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
package com.solarwars.controls;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.solarwars.*;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.ShipGroup;
import com.solarwars.gamestates.gui.DragRectangleGUI;
import com.solarwars.logic.ActionLib;
import com.solarwars.logic.DeathmatchGameplay;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The Class AbstractControl for the players interaction. actions.
 *
 * @author Hans Ferchland
 */
public abstract class AbstractControl {

    protected static final Logger logger = Logger.getLogger(AbstractControl.class.getName());
    //==========================================================================
    //      Private Fields
    //==========================================================================
    // general
    protected Node rootNode = SolarWarsApplication.getInstance().getRootNode();
    protected Node shootablesNode;
    protected final AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
    protected MarkerNode markerNode;
    // planet & shipgroup selection
    protected ArrayList<AbstractPlanet> planetSelection;
    protected ArrayList<ShipGroup> shipGroupSelection;
    protected ArrayList<MarkerNode> markerNodes;
    protected boolean controlPressed = false;
    // Panels for rect
    protected DragRectangleGUI dragRectangle;
    // Dragging
    private boolean dragging = false;
    private Vector3f lastXZPlanePos;
    private Vector3f startXZPlanePos;
    private Vector2f startScreenPos = Vector2f.ZERO.clone();
    // debug
    protected Camera cam = SolarWarsApplication.getInstance().getCamera();
    protected InputManager inputManager = SolarWarsApplication.getInstance().getInputManager();
    protected ActionLib actionLib;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Instantiates a new iso control.
     *
     * @param application the application
     */
    public AbstractControl() {
        this.actionLib = ActionLib.getInstance();
        this.markerNode = new MarkerNode();
    }

    /**
     * Creates the dragging raectangle for selection geometry.
     *
     * @param width
     * @param height
     * @param click2d
     */
    public void createDragRectGeometry() {
        dragRectangle.setEnabled(true);
    }

    /**
     * Initializes the players main controls.
     *
     * @param rootNode the root node
     */
    public void initialize() {
        dragRectangle = new DragRectangleGUI();
        AppStateManager stateManager = SolarWarsApplication.getInstance().getStateManager();
        stateManager.attach(dragRectangle);

        shootablesNode = new Node("Shootables");
        rootNode.attachChild(shootablesNode);
    }

    protected abstract void onControlModifier(String name, boolean isPressed);

    /**
     * Is executed if the player clicks on left or right mouse button but not on
     * its hold.
     */
    protected abstract void onSelectionPressed(String name, Vector2f point);

    /**
     * Is executed if the left or right mouse button is released. This can end
     * in a drag action that ended or a normal left click for selection or right
     * click for attack.
     */
    protected abstract boolean onSelectEntity(String name, Vector2f point);

    public abstract void addControlListener();

    public abstract void removeControlListener();

    protected abstract Vector2f getClickedPoint();

    protected void onDragSelectEntity(Vector2f point) {
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
            markerNodes = new ArrayList<MarkerNode>();
        }
        dragging = true;
    }

    protected void onPercentageChange(float amount, boolean down) {
        if (!down) {
            amount *= -1.0f;
        }
        Hub.getLocalPlayer().refreshShipPercentage(amount);
    }

    /**
     * Is executed if it is a normal left click for selection or right click for
     * attack.
     */
    protected boolean playerReleases(CollisionResults results, boolean attack) {
        // use the results and check if there was something hit
        if (results.size() > 0) {
            CollisionResult actual;
            ShipGroup nearestShipGroup = null;
            AbstractPlanet nearestPlanet = null;
            Node temp;
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
                            nearestPlanet,
                            Hub.getLocalPlayer(),
                            DeathmatchGameplay.PLANET_ATTACK);
                    return true;
                }
            } else {
                actionLib.invokePlanetAction(
                        null,
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
                null,
                Hub.getLocalPlayer(),
                DeathmatchGameplay.PLANET_SELECT);
        removeMarker(markerNode);
        clearMultiMarkers();
        return false;
    }

    /**
     * Is executed if a player ends mouse-dragging to collect the plantes that
     * where selected.
     */
    protected boolean playerEndsDragEntities() {
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

                    MarkerNode markerNodeLoc;
                    if (!planetSelection.isEmpty()) {
                        for (AbstractPlanet planet : planetSelection) {

                            markerNodeLoc = new MarkerNode();
                            markerNodes.add(markerNodeLoc);
                            repositMarker(planet, markerNodeLoc);

                        }
                        actionLib.invokePlanetAction(
                                this,
                                null,
                                Hub.getLocalPlayer(),
                                DeathmatchGameplay.PLANET_MULTI_SELECT);
                        final String multiSelectMsg = "Player multi selected " + planetSelection.size() + " planet(s).";
                        logger.info(multiSelectMsg);
                    }
                } else {
                    selectShipGroups(rect);

                    MarkerNode markerNodeLoc;
                    if (!shipGroupSelection.isEmpty()) {
                        for (ShipGroup shipGroup : shipGroupSelection) {

                            markerNodeLoc = new MarkerNode();
                            markerNodes.add(markerNodeLoc);
                            repositMarker(shipGroup, markerNodeLoc);

                        }
                        actionLib.invokeShipAction(
                                this,
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

                dragRectangle.hide();
                return true;
            }
            dragRectangle.hide();
        }
        return false;
    }

    protected boolean onAttackOrSelect(Vector2f point, boolean attack) {
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
        // if player releases not having dragged before
        if (playerReleases(results, attack)) {
            return true;
        }
        return false;
    }

    public boolean isControlPressed() {
        return controlPressed;
    }

    public boolean isDragging() {
        return dragging;
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
     * Clears all the markers in multi select.
     */
    private void clearMultiMarkers() {
        if (markerNodes != null && !markerNodes.isEmpty()) {
            for (MarkerNode mn : markerNodes) {
                removeMarker(mn);
            }
            markerNodes.clear();
        }
    }

    /**
     * Reposit a marker on a selected planet.
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
     * Remove a marker completly fron current planet.
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
     * Gets all ShipGroup contained in a 2D rectangle on the xz plane and adds
     * all to the shipGroupSelection.
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
    public void updateSelection(float tpf) {
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

        updateDragRect(getClickedPoint());
    }

    private void updateDragRect(Vector2f click2d) {
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

        if (dragRectangle == null) {
            return;
        }

        float width = click2d.x - startScreenPos.x;
        float height = click2d.y - startScreenPos.y;

        dragRectangle.setWidth((int) width);
        dragRectangle.setHeight((int) height);

        dragRectangle.setX((int) (startScreenPos.x));
        dragRectangle.setY((int) (startScreenPos.y));

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
    public void cleanUp() {
        lastXZPlanePos = null;
        startScreenPos = null;
        startXZPlanePos = null;
        dragging = false;
    }
}
