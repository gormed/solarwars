/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: DragRectangleGUI.java
 * Type: com.solarwars.gamestates.gui.DragRectangleGUI
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;
import com.solarwars.SolarWarsApplication;

/**
 * The class DragRectangleGUI.
 *
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class DragRectangleGUI extends AbstractAppState {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Node dragRectangle;
    private Material dragMaterial;
    private SolarWarsApplication application;
    private AppStateManager stateManager;
    private int x;
    private int y;
    private int width;
    private int height;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public DragRectangleGUI() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.application = (SolarWarsApplication) app;
        createGeometry(application);
        setEnabled(false);
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            dragRectangle.setLocalTranslation(x, y, 0);
            dragRectangle.setLocalScale(width, height, 0);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            application.getGuiNode().attachChild(dragRectangle);
        } else {
            application.getGuiNode().detachChild(dragRectangle);
        }
    }

    private void createGeometry(SolarWarsApplication app) {

        dragRectangle = new Node("DragRectangle");
        Vector3f v1 = new Vector3f(0, 0, 0);
        Vector3f v2 = new Vector3f(0, 1, 0);
        Vector3f v3 = new Vector3f(1, 1, 0);
        Vector3f v4 = new Vector3f(1, 0, 0);

        Line l1 = new Line(v1, v2);
        Line l2 = new Line(v2, v3);
        Line l3 = new Line(v3, v4);
        Line l4 = new Line(v4, v1);

        Geometry line0 = new Geometry("Line1", l1);
        Geometry line1 = new Geometry("Line2", l2);
        Geometry line2 = new Geometry("Line3", l3);
        Geometry line3 = new Geometry("Line3", l4);

        dragRectangle.attachChild(line0);
        dragRectangle.attachChild(line1);
        dragRectangle.attachChild(line2);
        dragRectangle.attachChild(line3);
        dragMaterial =
                new Material(app.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        dragMaterial.setColor("Color", ColorRGBA.Orange.clone());
        dragRectangle.setMaterial(dragMaterial);
        dragRectangle.setQueueBucket(Bucket.Gui);
//        application.getRootNode().attachChild(dragRectangle);
    }

    public void hide() {
        dragRectangle.setCullHint(CullHint.Always);
    }

    public void show() {

        dragRectangle.setCullHint(CullHint.Never);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
