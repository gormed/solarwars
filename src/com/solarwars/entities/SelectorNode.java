/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solarwars.entities;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Hans
 */
public class SelectorNode extends MarkerNode {

    public SelectorNode() {
        super();
        setName("Selector Node");
    }

    @Override
    protected void createMarker() {
        Quad q = new Quad(1, 1);
        markerGeometry = new Geometry("MarkerGeometry", q);
        markerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        markerMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/selector.png"));
        markerMaterial.setColor("Color", start);
        currentFadeColor = start;
        markerMaterial.setColor("GlowColor", ColorRGBA.White);
        markerMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        markerGeometry.setMaterial(markerMaterial);
        /**
         * Objects with transparency need to be in the render bucket for
         * transparent objects:
         */
        markerGeometry.setQueueBucket(RenderQueue.Bucket.Translucent);
        float[] angles = {(float) -Math.PI / 2,  (float) -Math.PI / 2, 0};
        // geometry.setLocalTranslation(-0.5f, 0, -0.5f);
        markerGeometry.setLocalRotation(new Quaternion(angles));
        Node rotation = new Node();
        float[] angles2 = {0,  (float) (Math.random() * 2 * Math.PI), 0};
        rotation.setLocalRotation(new Quaternion(angles2));
        rotation.attachChild(markerGeometry);
        this.attachChild(rotation);
    }

    @Override
    public void updateMarker(float tpf) {
        updateScaleFade(tpf);
        updateRange(tpf);
    }
    
    
}
