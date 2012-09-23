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
 * File: SingleplayerMatchState.java
 * Type: com.solarwars.gamestates.lib.SingleplayerMatchState
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.IsoControl;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.gui.GameOverModule;
import com.solarwars.gamestates.gui.GameStatsModule;
import com.solarwars.input.InputMappings;
import com.solarwars.gamestates.gui.PausePopup;
import com.solarwars.gamestates.gui.PlayerStatsModule;
import com.solarwars.gamestates.gui.StartGamePopup;
import com.solarwars.logic.DeathmatchGameplay;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import com.solarwars.net.ServerHub;

/**
 * The Class SingleplayerMatchState.
 */
public class SingleplayerMatchState extends Gamestate {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Level currentLevel;
    private Hub hub;
    private PausePopup pausePopup;
    private StartGamePopup startGamePopup;
    private ActionListener pauseToggle;
    private GameStatsModule gameStatsModule;
    private PlayerStatsModule playerStatsModule;
    private GameOverModule gameOverModule;
    private boolean paused = false;
    private boolean started = false;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Instantiates a new singleplayer state.
     *
     * @param game the game
     */
    public SingleplayerMatchState() {
        super(SolarWarsGame.SINGLEPLAYER_STATE);
        hub = Hub.getInstance();
        pauseToggle = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed && name.equals(InputMappings.PAUSE_GAME)) {
                    paused = !paused;
                }
            }
        };
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        // create pause listener
        pausePopup = new PausePopup(niftyGUI);
        gameOverModule = new GameOverModule(niftyGUI);
        startGamePopup = new StartGamePopup(niftyGUI);

    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent()
     */
    @Override
    protected void loadContent() {
        started = false;
        // switch to singleplayer gui
        niftyGUI.gotoScreen("singleplayer");
        // setup game for singleplayer
        setupSingleplayer();
        // attach iso control
        application.attachIsoCameraControl();
        application.getInputManager().addListener(
                pauseToggle,
                InputMappings.PAUSE_GAME);
        // Create Level and setup gameplay
        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl(),
                Hub.playersByID);
        game.setupGameplay(new DeathmatchGameplay(), currentLevel);
        currentLevel.generateLevel(System.currentTimeMillis());
        // setup nifty properly
        setupNiftyGUI();
        startGamePopup.showPopup();
    }

    public void startGame() {
        // play startup sound
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
        started = true;
        startGamePopup.hidePopup();
    }

    /**
     * Setup gui.
     */
    private void setupNiftyGUI() {
        pausePopup.hidePopup();
        gameOverModule.hidePopup();
        startGamePopup.hidePopup();
        // attach listener for pause layer
        application.getInputManager().addListener(
                pausePopup,
                InputMappings.PAUSE_GAME);

        gameStatsModule = new GameStatsModule(niftyGUI, currentLevel);
        gameStatsModule.addPlayers(Hub.getPlayers());
        
        playerStatsModule = new PlayerStatsModule(
                niftyGUI, Hub.getLocalPlayer(), gameStatsModule);
        // creates the drag-rect geometry
        IsoControl.getInstance().
                createDragRectGeometry();

//        startGamePopup.showPopup();

    }
    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */

    @Override
    protected void unloadContent() {
        //pause gui
        gameStatsModule.destroy();
        application.getInputManager().removeListener(pausePopup);
        application.getInputManager().removeListener(pauseToggle);
        //level
        currentLevel.destroy();
        //3d controls
        application.detachIsoCameraControl();
        gameStatsModule = null;
        playerStatsModule = null;
        paused = false;
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled() && started) {
            if (!paused) {
                currentLevel.updateLevel(tpf);
                if (currentLevel.isGameOver()) {
                    gameOverModule.showPopup();
                }
            }
            updateNifty(tpf);
        }
    }

    private void updateNifty(float tpf) {
        gameStatsModule.update(tpf);
        // only after gamestats where updated!
        playerStatsModule.update(tpf);
    }

    /**
     * Setup singleplayer.
     */
    private void setupSingleplayer() {
        Player local = new Player("Human", 
                ColorRGBA.Blue.clone(), 
                ServerHub.getContiniousPlayerID(), true);
        Player ai = new Player("AI", 
                ColorRGBA.Red.clone(), 
                ServerHub.getContiniousPlayerID());

        hub.initialize(local, null);
        hub.addPlayer(ai);
        hub.addPlayer(local);
    }



    public void continueGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        pausePopup.hidePopup();
    }

    public void quitGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        pausePopup.hidePopup();
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }

    public void onWatchGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        gameOverModule.setWatchGame(true);
        gameOverModule.hidePopup();
    }

    public void onLeaveGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }
}
