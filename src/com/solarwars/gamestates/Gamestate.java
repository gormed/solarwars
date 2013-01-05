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
 * File: Gamestate.java
 * Type: com.solarwars.gamestates.Gamestate
 * 
 * Documentation created: 05.01.2013 - 22:12:54 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * The Class Gamestate.
 */
public abstract class Gamestate extends AbstractAppState implements ScreenController {

    /** The name. */
    private String name;
    protected SolarWarsGame game;
    protected Nifty niftyGUI;
    protected Screen screen;
    protected SolarWarsApplication application;
    protected AppStateManager stateManager;

    /**
     * Instantiates a new gamestate.
     *
     * @param name the name
     */
    public Gamestate(String name) {
        this.name = name;
        this.game = SolarWarsGame.getInstance();
        this.application = game.getApplication();
        this.niftyGUI = application.getNiftyGUI();
        this.stateManager = application.getStateManager();
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Inits the Gamestate with the apps state manager and the app itself.
     * @param stateManager
     * @param app 
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.setEnabled(false);
        super.initialize(stateManager, app);
    }

    /**
     * Enables or disables this state. 
     * There can be more than one state at a time!
     * @param enabled true if state is updateing or false otherwise.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            enter();
        } else {
            leave();
        }
    }

    /**
     * Enter.
     */
    private void enter() {
        loadContent();
    }

    /**
     * Leave.
     */
    private void leave() {
        unloadContent();
    }

    /**
     * Load content.
     *
     * 
     */
    protected abstract void loadContent();

    /**
     * Unload content.
     */
    protected abstract void unloadContent();

    @Override
    public void onEndScreen() {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.screen = screen;
    }

//    @Override
//    public void stateAttached(AppStateManager stateManager) {
//        enter();
//    }
//
//    @Override
//    public void stateDetached(AppStateManager stateManager) {
//        leave();
//    }

    public void switchToState(String name) {
        setEnabled(false);
        game.triggerGamestate(name, true);
    }
}
