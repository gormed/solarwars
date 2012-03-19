/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import gui.elements.Label;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import gui.GameGUI;

/**
 *
 * @author Hans
 */
public abstract class Button extends Label {

    protected Geometry geometry;
    protected Material material;
    protected ColorRGBA buttonColor;

    public Button(String title, Vector3f position, Vector3f scale, ColorRGBA textColor, ColorRGBA buttonColor, GameGUI gui) {
        super(title, position, scale, textColor, gui);
        this.buttonColor = buttonColor;
        createButton();
    }

    private void createButton() {
        AssetManager assetManager = solarwars.SolarWarsApplication.getInstance().getAssetManager();

        float[] size = new float[2];

        size[0] = text.getLineWidth() / 1.5f;
        size[1] = text.getLineHeight() / 1.5f;

        Box b = new Box(size[0], size[1], 1);
        geometry = new Geometry(title + "_Button", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", buttonColor);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalScale(scale);

        detachChild(text);
        attachChild(geometry);
        attachChild(text);
    }

    @Override
    public abstract void updateGUI(float tpf);

    @Override
    public abstract void onClick(Vector2f cursor, boolean isPressed, float tpf);

    @Override
    public void setVisible(boolean show) {
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }
}
