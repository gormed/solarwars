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
 * File: SingleplayerState.java
 * Type: com.solarwars.gamestates.lib.SingleplayerState
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.lib;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.IsoControl;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.input.InputMappings;
import com.solarwars.input.PauseActionListener;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import com.solarwars.net.ServerHub;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The Class SingleplayerState.
 */
public class SingleplayerState extends Gamestate {

    /** The current level. */
    private Level currentLevel;
    /** The hub. */
    private Hub hub;
    /** The pause listener. */
    private PauseActionListener pauseListener;

    /**
     * Instantiates a new singleplayer state.
     *
     * @param game the game
     */
    public SingleplayerState() {
        super(SolarWarsGame.SINGLEPLAYER_STATE);
        hub = Hub.getInstance();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        // create pause listener
        pauseListener = new PauseActionListener(niftyGUI);

    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent()
     */
    @Override
    protected void loadContent() {
        // switch to singleplayer gui
        niftyGUI.gotoScreen("singleplayer");
        // setup game for singleplayer
        setupSingleplayer();
        // attach iso control
        application.attachIsoCameraControl();

        // Create Level and setup gameplay
        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl(),
                Hub.playersByID);
        game.setupGameplay(new DeathmatchGameplay(), currentLevel);
        currentLevel.generateLevel(System.currentTimeMillis());

        pauseListener.hidePopup();
        // attach listener for pause layer
        application.getInputManager().addListener(
                pauseListener,
                InputMappings.PAUSE_GAME);

        // creates the drag-rect geometry
        IsoControl.getInstance().createDragRectGeometry();

        // play startup sound
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        //pause gui
        application.getInputManager().removeListener(pauseListener);
//        pauseListener = null;
        //level
        currentLevel.destroy();
        //3d controls
        application.detachIsoCameraControl();
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            currentLevel.updateLevel(tpf);
            updateNifty();
        }
    }

    private void updateNifty() {
        // find old text
        Element niftyElement = niftyGUI.getCurrentScreen().
                findElementByName("percentage");
        // swap old with new text
        niftyElement.getRenderer(TextRenderer.class).
                setText(refreshPercentage() + "%");
    }

    /**
     * Setup singleplayer.
     */
    private void setupSingleplayer() {
        Player local = new Player("Human", ColorRGBA.Blue, ServerHub.getContiniousPlayerID(), true);
        Player ai = new Player("AI", ColorRGBA.Red, ServerHub.getContiniousPlayerID());

        hub.initialize(local, null);
        hub.addPlayer(ai);
        hub.addPlayer(local);
    }

    public int refreshPercentage() {
        return (int) (Hub.getLocalPlayer().getShipPercentage() * 100);
    }

    public void continueGame() {
        pauseListener.hidePopup();
    }

    public void quitGame() {
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }
}
