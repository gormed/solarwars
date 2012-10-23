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
 * File: GamepadControl.java
 * Type: com.solarwars.controls.GamepadControl
 * 
 * Documentation created: 16.10.2012 - 15:21:52 by Hans Ferchland <hans.ferchland at gmx dot de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.controls;

import com.jme3.input.Joystick;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.solarwars.MarkerNode;
import com.solarwars.SolarWarsGame;
import com.solarwars.controls.input.InputMappings;
import com.solarwars.entities.AbstractPlanet;
import com.solarwars.logic.Player;
import com.solarwars.logic.path.AIMap;
import com.solarwars.logic.path.AIPlanetEdge;
import com.solarwars.logic.path.AIPlanetNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

/**
 * The class GamepadControl.
 *
 * @author Hans Ferchland <hans.ferchland at gmx dot de>
 * @version
 */
public class GamepadControl extends AbstractControl {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final Joystick joystick;
    private ActionListener gamepadListener;
    private ActionListener selectionListener;
    private MarkerNode cursor;
    private AIPlanetNode selectedNode;
    private static AIMap map;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public GamepadControl(Joystick joystick) {
        this.joystick = joystick;

        gamepadListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                Vector2f point = getClickedPoint();
                if (isPressed) {
                    onSelectionPressed(name, point);
                } else if (!isPressed && onSelectEntity(name, point)) {
                } else {
                    onGamepadTriggers(name);
                }
            }
        };
        selectionListener = new ActionListener() {
            final float RIGHT_TOP = (float) (Math.PI * 0.25f);
            final float LEFT_TOP = (float) (Math.PI * 0.75f);
            final float LEFT_BOTTOM = (float) (Math.PI * 1.25f);
            final float RIGHT_BOTTOM = (float) (Math.PI * 1.75f);

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    if (selectedNode == null) {
//                        return;
                        if (!controllingPlayer.getPlanets().isEmpty()) {
                            onSelectPlanet(controllingPlayer.getPlanets().get(0));
                        } else {
                            return;
                        }
                    }
                    AbstractPlanet p = null;
                    if (name.equals(InputMappings.AXIS_LS_DOWN)) {
                        p = select(0);
                    }
                    if (name.equals(InputMappings.AXIS_LS_LEFT)) {
                        p = select(1);
                    }
                    if (name.equals(InputMappings.AXIS_LS_RIGHT)) {
                        p = select(2);
                    }
                    if (name.equals(InputMappings.AXIS_LS_UP)) {
                        p = select(3);
                    }
                    if (p != null) {
                        onSelectPlanet(p);
                    }
                    System.out.println(name + " pressed");
                } else {
//                    System.out.println(name + " released");
                }
            }

            private AbstractPlanet select(int dir) {
                ArrayList<AIPlanetEdge> valid = new ArrayList<AIPlanetEdge>();
                for (AIPlanetEdge edge : selectedNode.getEdges()) {
                    if (dir == 0) {
                        selectDown(edge, valid);
                    } else if (dir == 1) {
                        selectLeft(edge, valid);
                    } else if (dir == 2) {
                        selectRight(edge, valid);
                    } else if (dir == 3) {
                        selectTop(edge, valid);
                    }
                }
                if (valid.isEmpty()) {
                    return null;
                } else {
                    return getClosest(valid);
                }
            }

            private AbstractPlanet getClosest(ArrayList<AIPlanetEdge> valid) {
                Comparator<AIPlanetEdge> comparator = new Comparator<AIPlanetEdge>() {
                    @Override
                    public int compare(AIPlanetEdge o1, AIPlanetEdge o2) {
                        if (o1.getLength() <= o2.getLength()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                };
                Collections.sort(valid, comparator);
                return valid.get(0).getTo().getPlanet();
            }

            private void selectDown(AIPlanetEdge edge, ArrayList<AIPlanetEdge> valid) {
                if (edge.getAngle() >= LEFT_BOTTOM
                        && edge.getAngle() < RIGHT_BOTTOM) {
                    valid.add(edge);
                }
            }

            private void selectLeft(AIPlanetEdge edge, ArrayList<AIPlanetEdge> valid) {
                if (edge.getAngle() >= LEFT_TOP
                        && edge.getAngle() < LEFT_BOTTOM) {
                    valid.add(edge);
                }
            }

            private void selectRight(AIPlanetEdge edge, ArrayList<AIPlanetEdge> valid) {
                if (edge.getAngle() < RIGHT_TOP
                        && edge.getAngle() >= RIGHT_BOTTOM) {
                    valid.add(edge);
                }
            }

            private void selectTop(AIPlanetEdge edge, ArrayList<AIPlanetEdge> valid) {
                if (edge.getAngle() >= RIGHT_TOP
                        && edge.getAngle() < LEFT_TOP) {
                    valid.add(edge);
                }
            }
        };

    }

    @Override
    public void initialize(Player controllingPlayer) {
        super.initialize(controllingPlayer);

        cursor = new MarkerNode();

    }

    public void onSelectPlanet(AbstractPlanet planet) {
        repositCursor(planet);
        selectedNode = map.find(planet);
    }

    /**
     * Is executed for the shoulder triggers on gamepad.
     */
    protected void onGamepadTriggers(String name) {
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

    private void setupMappings() {
        //<editor-fold defaultstate="collapsed" desc="GAMEPAD XBOX MAPPINGS">
        //        inputManager.addMapping("D-Pad Left", new JoyAxisTrigger(joystick.getJoyId(), JoyInput.AXIS_POV_X, true));
        //        inputManager.addMapping("D-Pad Right", new JoyAxisTrigger(joystick.getJoyId(), JoyInput.AXIS_POV_X, false));
        //        inputManager.addMapping("D-Pad Down", new JoyAxisTrigger(joystick.getJoyId(), JoyInput.AXIS_POV_Y, true));
        //        inputManager.addMapping("D-Pad Up", new JoyAxisTrigger(joystick.getJoyId(), JoyInput.AXIS_POV_Y, false));
        //        inputManager.addMapping("Axis LS Up", new JoyAxisTrigger(joystick.getJoyId(), 0, true));
        //        inputManager.addMapping("Axis LS Down", new JoyAxisTrigger(joystick.getJoyId(), 0, false));
        //        inputManager.addMapping("Axis LS Left", new JoyAxisTrigger(joystick.getJoyId(), 1, true));
        //        inputManager.addMapping("Axis LS Right", new JoyAxisTrigger(joystick.getJoyId(), 1, false));
        //        inputManager.addMapping("Axis RS Up", new JoyAxisTrigger(joystick.getJoyId(), 2, true));
        //        inputManager.addMapping("Axis RS Down", new JoyAxisTrigger(joystick.getJoyId(), 2, false));
        //        inputManager.addMapping("Axis RS Left", new JoyAxisTrigger(joystick.getJoyId(), 3, true));
        //        inputManager.addMapping("Axis RS Right", new JoyAxisTrigger(joystick.getJoyId(), 3, false));
        //        inputManager.addMapping("Button A", new JoyButtonTrigger(joystick.getJoyId(), 0));
        //        inputManager.addMapping("Button B", new JoyButtonTrigger(joystick.getJoyId(), 1));
        //        inputManager.addMapping("Button X", new JoyButtonTrigger(joystick.getJoyId(), 2));
        //        inputManager.addMapping("Button Y", new JoyButtonTrigger(joystick.getJoyId(), 3));
        //        inputManager.addMapping("Button LB", new JoyButtonTrigger(joystick.getJoyId(), 4));
        //        inputManager.addMapping("Button RB", new JoyButtonTrigger(joystick.getJoyId(), 5));
        //        inputManager.addMapping("Button BACK", new JoyButtonTrigger(joystick.getJoyId(), 6));
        //        inputManager.addMapping("Button START", new JoyButtonTrigger(joystick.getJoyId(), 7));
        //        inputManager.addMapping("Button LT", new JoyButtonTrigger(joystick.getJoyId(), 8));
        //        inputManager.addMapping("Button RT", new JoyButtonTrigger(joystick.getJoyId(), 9));
        //        inputManager.addMapping("Button RT", new JoyAxisTrigger(joystick.getJoyId(), 4, true));
        //        inputManager.addMapping("Button LT", new JoyAxisTrigger(joystick.getJoyId(), 4, false));

        //        inputManager.addListener(gamepadListener, "Button A", "Button B", "Button X", "Button Y", "Button LB", "Button RB", "Button BACK", "Button START", "Button LT", "Button RT", "Axis LS Right", "Axis LS Left", "Axis LS Up", "Axis LS Down", "Axis RS Left", "Axis RS Right", "Axis RS Up", "Axis RS Down", "D-Pad Left", "D-Pad Right", "D-Pad Down", "D-Pad Up");
        //</editor-fold>
        inputManager.addListener(gamepadListener,
                InputMappings.LEFT_CLICK_SELECT,
                InputMappings.RIGHT_CLICK_ATTACK,
                InputMappings.PERCENT_DOWN,
                InputMappings.PERCENT_UP);
    }

    @Override
    protected void onControlModifier(String name, boolean isPressed) {
    }

    @Override
    protected void onSelectionPressed(String name, Vector2f point) {
        if (name.equals(InputMappings.LEFT_CLICK_SELECT)) {
            onDragSelectEntity(point);
            final String mouseDownMsg = "A-Button down @["
                    + point.x + "/" + point.y + "]";
            logger.log(Level.INFO, mouseDownMsg);
        }
    }

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

    @Override
    public void addControlListener() {
        if (map == null) {
            map = new AIMap();
            map.generateMap(SolarWarsGame.getCurrentGameplay().getCurrentLevel());
        }
        inputManager.addListener(gamepadListener,
                InputMappings.LEFT_CLICK_SELECT,
                InputMappings.RIGHT_CLICK_ATTACK,
                InputMappings.PERCENT_DOWN,
                InputMappings.PERCENT_UP);

        inputManager.addListener(selectionListener,
                InputMappings.AXIS_LS_DOWN,
                InputMappings.AXIS_LS_LEFT,
                InputMappings.AXIS_LS_RIGHT,
                InputMappings.AXIS_LS_UP);
        
        if (!controllingPlayer.getPlanets().isEmpty()) {
            onSelectPlanet(controllingPlayer.getPlanets().get(0));
        }
    }

    @Override
    public void removeControlListener() {

        inputManager.removeListener(gamepadListener);
    }

    @Override
    protected Vector2f getClickedPoint() {
        Vector3f pos;
        if (cursor.getPlanet() != null) {
            pos = cursor.getPlanet().getPosition();
        } else if (cursor.getShipGroup() != null) {
            pos = cursor.getShipGroup().getPosition();
        } else {
            return null;
        }
        Vector3f proj = cam.getScreenCoordinates(pos);
        return new Vector2f(proj.x, proj.y);
    }

        /**
     * Reposit a marker on a selected planet.
     *
     * @param planet the planet
     * @param markerNode the marker node
     */
    private void repositCursor(AbstractPlanet planet) {
        removeCursor();
        planet.getTransformNode().attachChild(cursor);
        cursor.setPlanet(planet);
    }

    /**
     * Remove a marker completly fron current planet.
     *
     * @param markerNode the marker node
     */
    private void removeCursor() {
        Node parent = cursor.getParent();
        if (parent != null) {
            parent.detachChild(cursor);
        }

    }
    
    @Override
    public void update(float tpf) {
//        cursor.updateMarker(tpf);
        super.update(tpf);
        cursor.updateMarker(tpf);
    }
}
