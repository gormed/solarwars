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
 * File: Panel.java
 * Type: gui.elements.Panel
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.solarwars.gui.GUIElement;
import com.solarwars.SolarWarsApplication;

/**
 * The Class Panel.
 */
public class Panel extends GUIElement {

    /** The screen position. */
    protected Vector3f screenPosition;
    
    protected Quaternion rotation = Quaternion.IDENTITY;
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The color. */
    protected ColorRGBA color;
    /** The size. */
    protected Vector2f size;

    /**
     * Instantiates a new panel.
     *
     * @param name the name
     * @param pos the pos
     * @param size the size
     * @param color the color
     */
    public Panel(String name, Vector3f pos, Vector2f size, ColorRGBA color) {
        super();
        this.screenPosition = pos;
        this.name = name;
        this.color = color;
        this.size = size;
        createPanel();
    }

    /**
     * Creates the panel.
     */
    private void createPanel() {
        AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();

        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geometry = new Geometry(name + "_Geometry", b);
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geometry.setMaterial(material);

        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalScale(size.x, size.y, 1);
        attachChild(geometry);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        geometry.setLocalTranslation(screenPosition);
        geometry.setLocalRotation(rotation);
        geometry.setLocalScale(size.x, size.y, 1);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        geometry.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /**
     * Gets the color.
     *
     * @return the color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * Gets the screen position.
     *
     * @return the screen position
     */
    public Vector3f getScreenPosition() {
        return screenPosition;
    }

    public void setScreenPosition(Vector3f screenPosition) {
        this.screenPosition = screenPosition;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }
}