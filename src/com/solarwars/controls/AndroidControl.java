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
 * File: AndroidControl.java
 * Type: com.solarwars.controls.AndroidControl
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.controls;

import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector2f;
import com.solarwars.SolarWarsApplication;
import java.util.logging.Level;

/**
 * The class AndroidConntrol.
 *
 * @author Hans Ferchland <hans.ferchland at gmx dot de>
 * @version
 */
public class AndroidControl extends AbstractControl {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final TouchListener touchListener;
    private TouchEvent lastEvent = null;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public AndroidControl() {
        touchListener = new TouchListener() {
            @Override
            public void onTouch(String name, TouchEvent event, float tpf) {

                lastEvent = event;
                float pressure;
                if (event.getType() == TouchEvent.Type.DOWN) {
                    onSelectionPressed(name, getClickedPoint());
                } else if (event.getType() == TouchEvent.Type.UP) {
                    onSelectEntity(name, getClickedPoint());
                } else if (event.getType() == TouchEvent.Type.DOUBLETAP) {
                    onAttackOrSelect(getClickedPoint(), true);
                } else if (event.getType() == TouchEvent.Type.SCROLL) {
                    float deltaY = event.getDeltaY();
                    boolean up = (deltaY >= 0) ? true : false;
                    onPercentageChange(0.05f, up);
                }

//                Log.e("", "Event Type " + event.getType());
                event.setConsumed();
            }
        };
    }

    @Override
    protected void onControlModifier(String name, boolean isPressed) {
        controlPressed = isPressed;
    }

    @Override
    protected void onSelectionPressed(String name, Vector2f point) {
        onDragSelectEntity(point);
        final String mouseDownMsg = "Left mouse-button down @["
                + point.x + "/" + point.y + "]";
        logger.log(Level.INFO, mouseDownMsg);
    }

    @Override
    protected boolean onSelectEntity(String name, Vector2f point) {
        return onAttackOrSelect(point, false);
    }

    @Override
    public void addControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.addListener(touchListener, new String[]{"Touch"});
        }
    }

    @Override
    public void removeControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.removeListener(touchListener);
        }
    }

    @Override
    protected Vector2f getClickedPoint() {
        return new Vector2f(lastEvent.getX(), lastEvent.getY());
    }
}