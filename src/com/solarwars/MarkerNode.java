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
 * File: MarkerNode.java
 * Type: com.solarwars.MarkerNode
 * 
 * Documentation created: 09.10.2012 - 21:07:57 by Hans Ferchland <hans.ferchland at gmx dot de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.entities.ShipGroup;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * The Class MarkerNode.
 */
public class MarkerNode extends Node {
    public static final float MARKER_PLANET_ADJUST = 0.0f;
    public static final int SELECTION_ANIMATION_SPEED = 2;
    public static final float RANGE_FADE_BASE = 0.02f;
    private float scale;
    private float fadeScale;
    private float running;
    private Material markerMaterial;
    private Geometry markerGeometry;
    private final AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
    private ColorRGBA start = ColorRGBA.Orange.clone();
    private ColorRGBA end = ColorRGBA.White.clone();
    private ColorRGBA currentFadeColor = start.clone();
    private boolean fadeDir = true;
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
        markerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        markerMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/marker.png"));
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
        float[] angles = {(float) -Math.PI / 2, (float) -Math.PI / 2, 0};
        // geometry.setLocalTranslation(-0.5f, 0, -0.5f);
        markerGeometry.setLocalRotation(new Quaternion(angles));
        this.attachChild(markerGeometry);
    }

    /**
     * Creates the bounding-volume for the range-checks.
     *
     * @param game the MazeTDGame singleton
     */
    private void createRange() {
        rangeNode = new Node("RangeNode");
        Cylinder c = new Cylinder(2, 15, 1, MARKER_PLANET_ADJUST, true);
        rangeMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        rangeMaterial.setColor("Color", new ColorRGBA(MARKER_PLANET_ADJUST, MARKER_PLANET_ADJUST, 1, MARKER_PLANET_ADJUST));
        rangeMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
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
        float alpha = RANGE_FADE_BASE * (5f + (float) Math.sin(4f * rangeFade));
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
        if (tick > 0.005f) {
            if (fadeDir) {
                // color
                currentFadeColor = currentFadeColor.add(new ColorRGBA(0.01f, 0.01f, 0.01f, 0));
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
        rangeBatch.setCullHint((planet.getOwner() == null) ? CullHint.Always : CullHint.Never);
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
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}