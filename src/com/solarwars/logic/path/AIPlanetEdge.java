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
 * File: AIEdge.java
 * Type: com.solarwars.logic.path.AIEdge
 * 
 * Documentation created: 14.07.2012 - 19:38:03 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.path;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.solarwars.FontLoader;
import com.solarwars.IsoCamera;
import com.solarwars.SolarWarsApplication;
import com.solarwars.logic.Player;
import java.util.Random;

/**
 * The Class AIEdge.
 */
public class AIPlanetEdge {

    private AIPlanetNode from;
    private AIPlanetNode to;
    private float length;
    private float angle;
    private BitmapText label;
    private Geometry line;

    /**
     * Instantiates a new aI edge.
     *
     * @param from the from
     * @param to the to
     */
    public AIPlanetEdge(AIPlanetNode from, AIPlanetNode to) {
        this.from = from;
        this.to = to;
        calculateLength();
        calculateAngle();
    }

    Node createDebugGeometry() {
        Line l = new Line(from.getPlanet().getPosition(), to.getPlanet().getPosition());

        line = new Geometry("Line #" + from.getPlanet().getID()
                + " to #" + to.getPlanet().getID(), l);

        Material material = new Material(SolarWarsApplication.getInstance().
                getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");

        Player p = from.getPlanet().getOwner();
        ColorRGBA c;
        if (p == null) {
            c = ColorRGBA.White.clone();
            c.a = 0.5f;
            material.setColor("Color", c);
        } else {
            c = p.getColor();
            c.a = 0.5f;
            material.setColor("Color", c);
        }
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        line.setMaterial(material);

        createLabel();
        Node lineNode = new Node(line.getName() + "_Node");
        lineNode.attachChild(line);
        lineNode.attachChild(label);
//        Vector3f pos = to.getPlanet().getPosition().
//                subtract(from.getPlanet().getPosition());
//        lineNode.setLocalTranslation(pos.mult(0.5f));
        return lineNode;
    }

    Geometry getLine() {
        return line;
    }

    /**
     * Calculate length.
     */
    private void calculateLength() {
        length = from.getPlanet().getPosition().distance(to.getPlanet().getPosition());
    }

    private void calculateAngle() {
//        Vector3f start = from.getPlanet().getPosition().clone();
//        Vector3f end = to.getPlanet().getPosition().clone();
        Vector2f from2d = new Vector2f(
                from.getPlanet().getPosition().x,
                from.getPlanet().getPosition().z);
        Vector2f to2d = new Vector2f(
                to.getPlanet().getPosition().x,
                to.getPlanet().getPosition().z);
        Vector2f dist = to2d.subtract(from2d);
        Vector2f right = new Vector2f(-1, 0);
        angle = dist.angleBetween(right);
        angle = Math.abs(angle);
    }

    public float getAngle() {
        return angle;
    }

    public float getLength() {
        return length;
    }

    public AIPlanetNode getFrom() {
        return from;
    }

    public AIPlanetNode getTo() {
        return to;
    }

    private void createLabel() {
        BitmapFont f = FontLoader.getInstance().getFont("SolarWarsFont32");
        label = new BitmapText(f, false);
//        label.setBox(new Rectangle(-3f, 0.15f, 6f, 3f));
        // label.setQueueBucket(Bucket.Transparent);
        label.setSize(0.175f);
        label.setColor(ColorRGBA.Orange);
        label.setText(String.format("a = %1.2f d = %2.2f",
                getAngle(), getLength()));
//        label.setAlignment(BitmapFont.Align.Center);
        label.setCullHint(Spatial.CullHint.Never);
        label.setQueueBucket(RenderQueue.Bucket.Transparent);
        // algins position of the font
        refreshFont();
    }

    private void refreshFont() {
        Camera cam = IsoCamera.getInstance().getCam();


        Vector3f start = from.getPlanet().getPosition();
        Vector3f end = to.getPlanet().getPosition();

        float width = label.getLineWidth();
        Random r = new Random();
//        float height = r.nextFloat() * end.subtract(start).z;

        Vector3f position = new Vector3f(width / 2, .15f, 0);
        Vector3f fontPos = start.add(end.subtract(start).mult(0.4f + 0.75f * r.nextFloat()));
        position.addLocal(fontPos);

        Vector3f up = cam.getUp().clone();
        Vector3f dir = cam.getDirection().
                clone().negateLocal().normalizeLocal();
        Vector3f left = cam.getLeft().
                clone().normalizeLocal().negateLocal();

        Quaternion look = new Quaternion();
        look.fromAxes(left, up, dir);

        label.setLocalTransform(new Transform(position, look));

//        Vector3f camPos = IsoCamera.getInstance().getCam().getLocation();
//        Vector3f fontPos = to.getPlanet().getPosition().
//                subtract(from.getPlanet().getPosition());
//        Vector3f up = IsoCamera.getInstance().getCam().getUp().clone();
//        Vector3f dir = camPos.subtract(fontPos);
////        Vector3f dir = Vector3f.UNIT_Y.clone().subtract(fontPos);
//
//        Vector3f left = IsoCamera.getInstance().getCam().getLeft().clone();
//        dir.normalizeLocal();
//        left.negateLocal();
//        left.normalizeLocal();
//        up.normalizeLocal();
////        dir.negateLocal();
//
//        Quaternion look = new Quaternion();
//        look.fromAxes(left, up, dir);
//
//        Vector3f newPos = to.getPlanet().getPosition().
//                subtract(from.getPlanet().getPosition());
//
//        newPos.x -= label.getLineWidth() / 2;
//
//        Transform t = new Transform(newPos, look);
//
//        label.setLocalTransform(t);
    }
}
