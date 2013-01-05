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
 * File: MainmenuState.java
 * Type: com.solarwars.gamestates.MainmenuState
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
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
    }

    /**
     * Start multiplayer.
     */
    public void startMultiplayer() {
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
    }

    public void openOptions() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.OPTIONS_STATE);
    }

    public void openTutorial() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.TUTORIAL_STATE);
    }

    public void quitGame() {
        game.getApplication().stop();
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);

    }
}
