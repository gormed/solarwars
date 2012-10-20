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
 * File: StandardControl.java
 * Type: com.solarwars.controls.StandardControl
 * 
 * Documentation created: 12.10.2012 - 18:50:44 by Hans Ferchland <hans.ferchland at gmx dot de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.controls;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.controls.input.InputMappings;
import com.solarwars.logic.Player;
import java.util.logging.Level;

/**
 * The class StandardControl for keyboard- and mouse-interaction.
 *
 * @author Hans Ferchland <hans.ferchland at gmx dot de>
 * @version 2.1
 */
public class StandardControl extends AbstractControl {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final ActionListener mouseActionListener;
    private final ActionListener keyActionListener;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Creates a control for mouse and keyboard interactions.
     */
    public StandardControl() {
        // register action listener for right and left clicks 
        // and the mouse-weel
        mouseActionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {

                Vector2f point = inputManager.getCursorPosition();
                if (keyPressed) {
                    onSelectionPressed(name, point);
                } else if (!keyPressed && onSelectEntity(name, point)) {
                } else {
                    onMouseWeel(name);
                }

            }
        };
        // register listener for ctrl key to trigger the control-flag
        keyActionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                onControlModifier(name, isPressed);
            }
        };

    }

    /**
     * Listens for ctrl key to trigger the control-flag.
     *
     * @param name
     * @param isPressed
     */
    @Override
    protected void onControlModifier(String name, boolean isPressed) {
        if (name.equals(InputMappings.CONTROL_MODIFIER)) {
            controlPressed = isPressed;
        }
    }

    /**
     * Listens for left mouse click to drag the selection-rectangle.
     *
     * @param name
     * @param point
     */
    @Override
    protected void onSelectionPressed(String name, Vector2f point) {
        if (name.equals(InputMappings.LEFT_CLICK_SELECT)) {
            onDragSelectEntity(point);
            final String mouseDownMsg = "Left mouse-button down @["
                    + point.x + "/" + point.y + "]";
            logger.log(Level.INFO, mouseDownMsg);
        }
    }

    /**
     * Listnens for left or right click to select or attack. This is multi-ship-
     * and multi-planet-select as well as single select a shipgroup or planet or
     * to attack.
     *
     * @param name the action-name for left or right mouse
     * @param point the point clicked in screen-space
     * @return true if there was a select or attack action, false if nothing was
     * hit or selected.
     */
    @Override
    protected boolean onSelectEntity(String name, Vector2f point) {
        boolean attack = (name.equals(InputMappings.LEFT_CLICK_SELECT)
                && (name.equals(InputMappings.LEFT_CLICK_SELECT)
                || name.equals(InputMappings.RIGHT_CLICK_ATTACK)))
                ? false : true;
        if (name.equals(InputMappings.LEFT_CLICK_SELECT)
                || name.equals(InputMappings.RIGHT_CLICK_ATTACK)) {
            return onAttackOrSelect(point, attack);
        }
        return false;
    }

    /**
     * Adds the control listnener for keyboard an mouse.
     */
    @Override
    public void addControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.addListener(mouseActionListener,
                    InputMappings.LEFT_CLICK_SELECT,
                    InputMappings.RIGHT_CLICK_ATTACK,
                    InputMappings.PERCENT_DOWN,
                    InputMappings.PERCENT_UP);

            inputManager.addListener(keyActionListener,
                    InputMappings.CONTROL_MODIFIER);

//            inputManager.addListener(touchListener, new String[]{"Touch"});
        }
    }

    /**
     * Removes the control listnener for keyboard an mouse.
     */
    @Override
    public void removeControlListener() {
        inputManager = SolarWarsApplication.getInstance().getInputManager();
        if (inputManager != null) {
            inputManager.removeListener(mouseActionListener);
            inputManager.removeListener(keyActionListener);
        }
    }

    /**
     * Gets the current clicked point from the input manager.
     *
     * @return
     */
    @Override
    protected Vector2f getClickedPoint() {
        return inputManager.getCursorPosition();
    }

    /**
     * Is executed if the mouse-weel is scrolled.
     */
    protected void onMouseWeel(String name) {
        Player local = controllingPlayer;

        if (!(name.equals(InputMappings.PERCENT_DOWN)
                || name.equals(InputMappings.PERCENT_UP))) {
            return;
        }
        boolean down = (name.equals(InputMappings.PERCENT_DOWN)) ? true : false;
        float amount = 0.05f;

        onPercentageChange(amount, down);
        final String percentageChangeS = local.getName() + " changed percentage to " + String.format("%3.0f", local.getShipPercentage() * 100f) + "%";

        logger.log(Level.FINE, percentageChangeS);
    }
}