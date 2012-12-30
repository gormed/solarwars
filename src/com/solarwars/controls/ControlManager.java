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
 * File: ControlManager.java
 * Type: com.solarwars.controls.ControlManager
 * 
 * Documentation created: 18.10.2012 - 17:46:29 by Hans Ferchland <hans.ferchland at gmx dot de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.controls;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.solarwars.SolarWarsApplication;
import com.solarwars.logic.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Singleton Class for managing the controls that are available.
 *
 * @author Hans Ferchland <hans.ferchland at gmx dot de>
 */
public class ControlManager {

    public enum MachineType {

        PC,
        MAC,
        ANDROID,
        BROWSER
    }
    //==========================================================================
    //      Singleton
    //==========================================================================

    /**
     *
     */
    private ControlManager() {
        controllers = new HashMap<Integer, AbstractControl>();
        unusedControllers = new ArrayList<AbstractControl>();
        joysticks = new ArrayList<Joystick>();
        usedControllers = new HashMap<Player, AbstractControl>();
    }

    /**
     * Gets the singletons one and only ref.
     *
     * @return
     */
    public static ControlManager getInstance() {
        return ControlManagerHolder.INSTANCE;
    }

    private static class ControlManagerHolder {

        private static final ControlManager INSTANCE = new ControlManager();
    }
    private static Integer CONTROLLER_ID = 0;

    private static Integer getUniqueControllerID() {
        return CONTROLLER_ID++;
    }
    //==========================================================================
    //      Protected & Private Fields
    //==========================================================================
    private boolean initialized = false;
    private MachineType machineType;
    private ArrayList<Joystick> joysticks;
    private HashMap<Integer, AbstractControl> controllers;
    private HashMap<Player, AbstractControl> usedControllers;
    private ArrayList<AbstractControl> unusedControllers;
    private InputManager inputManager;
    protected Node shootablesNode;
    protected Node rootNode = SolarWarsApplication.getInstance().getRootNode();
    //==========================================================================
    //      Methods
    //==========================================================================

    /**
     *
     * @param inputManager
     */
    public void initialize(InputManager inputManager, Node rootNode) {
        if (initialized) {
            return;
        }

        this.inputManager = inputManager;
        this.rootNode = rootNode;
        this.shootablesNode = new Node("Shootables");
        this.rootNode.attachChild(shootablesNode);

        detectMachineType();
        detectControllers();
        initialized = true;
    }

    public void freeControllers() {
        for (Map.Entry<Player, AbstractControl> entry : usedControllers.entrySet()) {
            AbstractControl c = entry.getValue();
            c.destroy();
            unusedControllers.add(entry.getValue());
        }
        usedControllers.clear();
    }

    public void destroy() {
        if (!initialized) {
            return;
        }
        
        freeControllers();
        usedControllers.clear();
        unusedControllers.clear();
        controllers.clear();
        joysticks.clear();
        shootablesNode.detachAllChildren();
        rootNode.detachChild(shootablesNode);
        shootablesNode = null;
        
        initialized = false;
    }

    /**
     *
     * @return
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     *
     * @return
     */
    public int getUnusedControllers() {
        return unusedControllers.size();
    }

    /**
     *
     * @return
     */
    public int getTotalControllers() {
        return controllers.size();
    }

    public AbstractControl getControl(Player p) {
        if (usedControllers.isEmpty()) {
            return null;
        }
        return usedControllers.get(p);
    }

    public AbstractControl getControl(int playerID) {
        if (usedControllers.isEmpty()) {
            return null;
        }
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            if (e.getKey().getID() == playerID) {
                return e.getValue();
            }
        }
        return null;
    }

    public AbstractControl pullControl(Player p) {
        if (unusedControllers.isEmpty()) {
            return null;
        }
        if (p.isLocalPlayer() && usedControllers.get(p) == null) {
            AbstractControl c = unusedControllers.remove(unusedControllers.size() - 1);
            c.initialize(p);
            usedControllers.put(p, c);
            return c;
        }
        return null;
    }

    public void pushControl(Player p) {
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            if (e.getKey().getID() == p.getID()) {
                AbstractControl c = getControl(p.getID());
                usedControllers.remove(c.getControllingPlayer());
                unusedControllers.add(c);
            }
        }
    }

    public void createDragRectGeometry() {
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            e.getValue().createDragRectGeometry();
        }
    }

    public void attachControlListeners() {
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            e.getValue().addControlListener();
        }
    }

    public void detachControlListeners() {
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            e.getValue().removeControlListener();
        }
    }

    public void update(float tpf) {
        for (Map.Entry<Player, AbstractControl> e : usedControllers.entrySet()) {
            e.getValue().update(tpf);
        }
    }

    /**
     * Adds the shootable to the nodes that can be hit.
     *
     * @param spat the spat
     */
    public void addShootable(Spatial spat) {
        shootablesNode.attachChild(spat);
    }

    /**
     * Removes the shootable from the nodes that can be hit.
     *
     * @param spat the spat
     */
    public void removeShootable(Spatial spat) {
        shootablesNode.detachChild(spat);
    }
    
    /**
     * Gets a copy of the joysticks detected by jme3.
     * @return a cloned list.
     */
    public ArrayList<Joystick> getJoysticks() {
        return new ArrayList<Joystick>(joysticks);
    }

    Node getShootablesNode() {
        return shootablesNode;
    }

    private void detectMachineType() {
        machineType = MachineType.PC;
    }

    private void detectControllers() {
        if (MachineType.PC == machineType) {
            AbstractControl pc = new StandardControl();
            controllers.put(getUniqueControllerID(), pc);
        }

        joysticks.addAll(Arrays.asList(inputManager.getJoysticks()));

        for (Joystick j : joysticks) {
            if (!"Controller (XBOX 360 For Windows)".equals(j.getName())) {
                continue;
            }
            GamepadControl gamepad = new GamepadControl(j);
            controllers.put(getUniqueControllerID(), gamepad);
        }

        unusedControllers.addAll(controllers.values());
    }
    
    
}
