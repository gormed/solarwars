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
 * File: CheckBox.java
 * Type: com.solarwars.gui.elements.CheckBox
 * 
 * Documentation created: 29.08.2012 - 15:16:35 by Hans
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.solarwars.gui.ClickableGUI;
import com.solarwars.gui.GUIElement;

/**
 * The class CheckBox.
 * @author Hans Ferchland
 * @version
 */
public class CheckBox extends GUIElement implements ClickableGUI {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Panel frame;
    private Panel checkArea;
    private Node check;
    private Material checkMaterial;
    // Init
    private AssetManager assetManager;
    private final Vector3f position;
    private final Vector2f size;
    // Logic
    private boolean checked = false;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public CheckBox(String name, Vector3f pos, Vector2f size, ColorRGBA color) {
        super();
        this.name = name;
        this.position = pos;
        this.size = size;

        assetManager = com.solarwars.SolarWarsApplication.getInstance().getAssetManager();

        frame = new Panel(
                "CheckBox_Frame",
                Vector3f.ZERO.clone(),
                size.mult(1.1f),
                ColorRGBA.White.clone());

        checkArea = new Panel(
                "CheckArea",
                Vector3f.ZERO.clone(),
                size,
                ColorRGBA.Blue.clone()) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                onCheck(cursor, isPressed, tpf);
            }
        };

        check = new Node("CheckNode");
        Line l1 = new Line(
                new Vector3f(-1, -1, 0),
                new Vector3f(1, 1, 0));
        Line l2 = new Line(
                new Vector3f(-1, 1, 0),
                new Vector3f(1, -1, 0));
        Geometry line1 = new Geometry("Line1", l1);
        Geometry line2 = new Geometry("Line2", l2);
        checkMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        checkMaterial.setColor("Color", ColorRGBA.White.clone());
        checkMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        check.attachChild(line1);
        check.attachChild(line2);
        check.setMaterial(checkMaterial);
        check.setLocalScale(size.x * 1.2f, size.y * 1.2f, 1);

        setLocalTranslation(position);

        attachChild(frame);
        attachChild(checkArea);
        attachChild(check);

        setUnchecked();
    }

    @Override
    public void updateGUI(float tpf) {
    }

    public boolean onCheck(Vector2f cursor, boolean isPressed, float tpf) {
        if (!isPressed) {
            trigger();
            return checked;
        }
        return checked;
    }

    private void setChecked() {
        checked = true;
        check.setCullHint((checked) ? CullHint.Never : CullHint.Always);
    }

    private void setUnchecked() {
        checked = false;
        check.setCullHint((checked) ? CullHint.Never : CullHint.Always);
    }

    private boolean trigger() {
        checked = !checked;
        check.setCullHint((checked) ? CullHint.Never : CullHint.Always);
        return checked;
    }

    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }

    @Override
    public boolean canGainFocus() {
        return false;
    }
}
