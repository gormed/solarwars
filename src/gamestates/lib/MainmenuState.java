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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: MainmenuState.java
 * Type: gamestates.lib.MainmenuState
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.elements.Button;
import gui.elements.Label;
import gui.GameGUI;
import gui.elements.BeatBox;
import solarwars.AudioManager;
import solarwars.SolarWarsGame;

/**
 * The Class MainmenuState.
 */
public class MainmenuState extends Gamestate {

    /** The gui. */
    private GameGUI gui;
    /** The singleplayer button. */
    private Button singleplayerButton;
    /** The multiplayer button. */
    private Button multiplayerButton;
    /** The solarwars. */
    private Label solarwars;
    /** The quit button. */
    private Button quitButton;
    /** The game. */
    private SolarWarsGame game;
    
    private BeatBox beatBox;
    /*
     * 
     */

    /**
     * Instantiates a new mainmenu state.
     *
     * @param game the game
     */
    public MainmenuState(SolarWarsGame game) {
        super(GamestateManager.MAINMENU_STATE);
        this.game = game;
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame swgame) {
        gui = new GameGUI(swgame);

        solarwars = new Label("SOLARWARS", new Vector3f(gui.getWidth() / 2,
                9 * gui.getHeight() / 10, 4), new Vector3f(2, 2, 1), ColorRGBA.White, gui) {

            private float time;

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            public void updateGUI(float tpf) {

                time += tpf;

                if (time < 0.2f) {
                    text.setText(title + "_");
                } else if (time < 0.4f) {
                    text.setText(title);
                } else {
                    time = 0;
                }
            }
        };


        singleplayerButton = new Button("Singleplayer",
                new Vector3f(gui.getWidth() / 2,
                7 * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Cyan,
                ColorRGBA.DarkGray, gui) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    startSingleplayer();
                }
            }

            @Override
            public void updateGUI(float tpf) {
            }
        };

        multiplayerButton = new Button("Multiplayer",
                new Vector3f(gui.getWidth() / 2,
                6 * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Cyan,
                ColorRGBA.DarkGray, gui) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    startMultiplayer();
                }
            }

            @Override
            public void updateGUI(float tpf) {
            }
        };

        quitButton = new Button("Quit Game", new Vector3f(gui.getWidth() / 2,
                5 * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Cyan, ColorRGBA.DarkGray, gui) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                game.getApplication().stop();
            }

            @Override
            public void updateGUI(float tpf) {
            }
        };

        gui.addGUIElement(solarwars);
        gui.addGUIElement(singleplayerButton);
        gui.addGUIElement(multiplayerButton);
        gui.addGUIElement(quitButton);
        
        beatBox = new BeatBox();
        beatBox.setupSounds();
        beatBox.play();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        gui.cleanUpGUI();

        gui = null;
        
        beatBox.stop();
        beatBox = null;
    }

    /**
     * Start singleplayer.
     */
    private void startSingleplayer() {
        GamestateManager.getInstance().enterState(GamestateManager.SINGLEPLAYER_STATE);
    }

    /**
     * Start multiplayer.
     */
    private void startMultiplayer() {
        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }
}
