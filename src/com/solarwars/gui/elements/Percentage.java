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
 * File: Percentage.java
 * Type: gui.elements.Percentage
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.solarwars.gui.GameGUI;
import com.solarwars.Hub;

/**
 * The Class Percentage.
 */
public class Percentage extends Label {

    /** The percentage. */
    private int percentage = 50;
    /** The background. */
    private final Panel background;
    /** The background frame. */
    private final Panel backgroundFrame;

    /**
     * Instantiates a new percentage.
     *
     * @param gui the gui
     */
    public Percentage(GameGUI gui) {
        super((int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%",
                Vector3f.ZERO.clone(),
                new Vector3f(0.7f, 0.7f, 1),
                new ColorRGBA(1, 0.5f, 0, 0.7f), gui);

        this.setLocalTranslation(
                gui.getWidth() - text.getLineWidth()*0.5f,
                text.getHeight()*0.5f,
                0);
        background = new Panel("Percentage_Back",
                new Vector3f(
                0,
                -text.getHeight()*0.475f,
                0),
                new Vector2f(
                text.getLineWidth() * 2f,
                text.getHeight() * 1.1f),
                new ColorRGBA(0, 0, 1, 0.2f));
        backgroundFrame = new Panel("Percentage_Back",
                new Vector3f(
                0,
                -text.getHeight()*0.475f,
                0),
                new Vector2f(
                text.getLineWidth() * 2f,
                text.getHeight() * 1.0f),
                new ColorRGBA(0, 0, 1, 0.5f));

        float[] rotation = {0, 0, (float) Math.PI / 5};
        background.setRotation(new Quaternion(rotation));
        backgroundFrame.setRotation(new Quaternion(rotation));

        detachChild(text);
        attachChild(background);
        attachChild(backgroundFrame);
        attachChild(text);
    }

    /**
     * Refresh percentage.
     *
     * @return true, if successful
     */
    private int refreshPercentage() {
        return (int) (Hub.getLocalPlayer().getShipPercentage() * 100);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        percentage = refreshPercentage();

        background.updateGUI(tpf);
        backgroundFrame.updateGUI(tpf);
//        frame.updateGUI(tpf);

        text.setText(percentage + "%");
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        backgroundFrame.setVisible(show);
//        frame.setVisible(show);
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /* (non-Javadoc)
     * @see gui.elements.Label#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}