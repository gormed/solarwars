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
 * File: MainmenuState.java
 * Type: com.solarwars.gamestates.lib.MainmenuState
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.solarwars.AudioManager;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.gui.BeatBox;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 * The Class MainmenuState.
 */
public class MainmenuState extends Gamestate {
    private BeatBox beatBox;


    /**
     * Instantiates a new mainmenu state.
     *
     * @param game the game
     */
    public MainmenuState() {
        super(SolarWarsGame.MAINMENU_STATE);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent(com.solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent() {
        niftyGUI.gotoScreen("mainmenu");
        beatBox = new BeatBox();
        beatBox.setupSounds();
        beatBox.play();
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        beatBox.stop();
        beatBox = null;
    }

    /**
     * Start singleplayer.
     */
    public void startSingleplayer() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.SINGLEPLAYER_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.SINGLEPLAYER_STATE);
    }

    /**
     * Start multiplayer.
     */
    public void startMultiplayer() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }

    public void openOptions() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.OPTIONS_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.OPTIONS_STATE);
    }

    public void openTutorial() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.TUTORIAL_STATE);
//        GamestateManager.getInstance().enterState(GamestateManager.TUTORIAL_STATE);
    }

    public void quitGame() {
        game.getApplication().stop();
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        
    }
}
