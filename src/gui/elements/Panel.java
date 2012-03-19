/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import gui.GUIElement;
import solarwars.SolarWarsApplication;

/**
 *
 * @author Hans
 */
public class Panel extends GUIElement {

    protected Vector3f screenPosition;
    protected Geometry geometry;
    protected Material material;
    protected ColorRGBA color;
    protected Vector2f size;

    public Panel(String name, Vector3f pos, Vector2f size, ColorRGBA color) {
        super();
        this.screenPosition = pos;
        this.name = name;
        this.color = color;
        this.size = size;
        createPanel();
    }

    private void createPanel() {
        AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();

        Box b = new Box(Vector3f.ZERO, size.x, size.y, 1);
        geometry = new Geometry(name + "_Geometry", b);
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        attachChild(geometry);
    }

    @Override
    public void updateGUI(float tpf) {
    }

    @Override
    public void setVisible(boolean show) {
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }
}
