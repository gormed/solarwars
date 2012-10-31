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
 * File: AINode.java
 * Type: com.solarwars.logic.path.AINode
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.path;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.solarwars.FontLoader;
import com.solarwars.IsoCamera;
import com.solarwars.entities.AbstractPlanet;
import java.util.ArrayList;

/**
 * The Class AINode.
 */
public class AIPlanetNode {

    /**
     * The planet.
     */
    private AbstractPlanet planet;
    Node debugNode;
    /**
     * The edges.
     */
    private ArrayList<AIPlanetEdge> edges = new ArrayList<AIPlanetEdge>();
    private BitmapText label;

    /**
     * Instantiates a new aI node.
     *
     * @param planet the planet
     */
    public AIPlanetNode(AbstractPlanet planet) {
        this.planet = planet;
        this.debugNode = new Node("Planet#" + planet.getID() + " Debug Node");
        createLabel();
        this.debugNode.attachChild(label);
    }

    /**
     * Connect planets.
     *
     * @param connectors the connectors
     */
    void connectPlanets(ArrayList<AIPlanetNode> connectors) {
        for (AIPlanetNode n : connectors) {
            if (!n.equals(this)) {
                createEdge(n);
            }
        }
    }

    private void createLabel() {
        BitmapFont f = FontLoader.getInstance().getFont("SolarWarsFont64");
        label = new BitmapText(f, false);
//        label.setBox(new Rectangle(-3f, 0.15f, 6f, 3f));
        // label.setQueueBucket(Bucket.Transparent);
        label.setSize(0.4f);
        label.setColor(ColorRGBA.Red);
        label.setText("#" + planet.getID());
//        label.setAlignment(BitmapFont.Align.Center);
        label.setCullHint(Spatial.CullHint.Never);
        label.setQueueBucket(RenderQueue.Bucket.Transparent);
        // algins position of the font
        refreshFont();
    }

    private void refreshFont() {
        Camera cam = IsoCamera.getInstance().getCam();

        float width = label.getLineWidth();
        float height = label.getHeight();

        Vector3f pos = planet.getPosition().clone();
        pos.addLocal(new Vector3f(-planet.getSize(), .15f, -planet.getSize()));

        Vector3f up = cam.getUp().clone();
        Vector3f dir = cam.getDirection().
                clone().negateLocal().normalizeLocal();
        Vector3f left = cam.getLeft().
                clone().normalizeLocal().negateLocal();

        Quaternion look = new Quaternion();
        look.fromAxes(left, up, dir);

        label.setLocalTransform(new Transform(pos, look));

//        Vector3f camPos = IsoCamera.getInstance().getCam().getLocation();
//        Vector3f fontPos = planet.getPosition().clone();
//        fontPos.z += planet.getSize() * 8.0f;
//        Vector3f up = IsoCamera.getInstance().getCam().getUp().clone();
//        Vector3f dir = camPos.subtract(fontPos);
////        Vector3f dir = Vector3f.UNIT_Y.clone().subtract(fontPos);
//
//        Vector3f left = IsoCamera.getInstance().getCam().getLeft().clone();
//        dir.normalizeLocal();
//        left.normalizeLocal();
//        left.negateLocal();
//
//        Quaternion look = new Quaternion();
//        look.fromAxes(left, up, dir);
//
//        Vector3f newPos = dir.clone();
//        newPos.normalizeLocal();
//        newPos.mult(planet.getSize());
//
//        newPos = planet.getPosition().add(newPos);
//
//        Transform t = new Transform(newPos, look);
//
//        label.setLocalTransform(t);
    }

    /**
     * Creates the edge.
     *
     * @param node the node
     */
    private void createEdge(AIPlanetNode node) {
        AIPlanetEdge e = new AIPlanetEdge(this, node);
        edges.add(e);

        debugNode.attachChild(e.createDebugGeometry());
        enabelDebugMode(false);
    }

    /**
     * Gets the planet.
     *
     * @return the planet
     */
    public AbstractPlanet getPlanet() {
        return planet;
    }

    public ArrayList<AIPlanetEdge> getEdges() {
        return new ArrayList<AIPlanetEdge>(edges);
    }

    public void enabelDebugMode(boolean value) {
        if (value) {
            debugNode.setCullHint(Spatial.CullHint.Never);
        } else {
            debugNode.setCullHint(Spatial.CullHint.Always);
        }
    }

    public void destroy() {
        edges.clear();
        debugNode.detachAllChildren();

    }
}
