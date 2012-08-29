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
 * File: GameOverGUI.java
 * Type: com.solarwars.gui.elements.GameOverGUI
 * 
 * Documentation created: 14.07.2012 - 19:37:57 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.GamestateManager;
import com.solarwars.gui.ClickableGUI;
import com.solarwars.gui.GUIElement;
import com.solarwars.gui.GameGUI;


/**
 * The Class GameOverGUI.
 *
 * @author Hans
 */
public class GameOverGUI extends GUIElement implements ClickableGUI {

    /**
     * The Enum GameOverState.
     */
    public enum GameOverState {

        /** The WON. */
        WON,
        /** The LOST. */
        LOST
    }
    /** The gui. */
    private GameGUI gui;
    /** The game. */
    protected SolarWarsGame game;
    /** The display label. */
    protected Label gameOverLabel;
    /** The exit game. */
    protected Button mainMenuLabel;
    /** The continue game. */
    protected Button watchGameLabel;
    /** The background. */
    protected Panel background;
    /** The watch game. */
    private boolean watchGame = false;
    /** The instance. */
    private static GameOverGUI instance;

    /**
     * Gets the single instance of GameOverGUI.
     *
     * @return single instance of GameOverGUI
     */
    public static GameOverGUI getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance =
                new GameOverGUI(
                SolarWarsGame.getInstance(),
                GameGUI.getInstance());
    }

    /**
     * Instantiates a new display gui.
     *
     * @param swgame the swgame
     * @param gui the gui
     * @param state the state
     */
    private GameOverGUI(SolarWarsGame swgame, final GameGUI gui) {
        this.game = swgame;
        this.gui = gui;

        String gameOverStateString = "No state set";

        background = new Panel(
                "GameOverPanel",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 4, gui.getHeight() / 5),
                new ColorRGBA(0, 0, 1, 0.7f));


        gameOverLabel = new Label(gameOverStateString, new Vector3f(gui.getWidth() / 2,
                6.5f * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ, ColorRGBA.White, gui) {

            private float time;

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

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        watchGameLabel = new Button(
                "Watch Game",
                new Vector3f(
                gui.getWidth() / 2,
                gui.getHeight() / 2.0f, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Orange,
                ColorRGBA.DarkGray,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    hide();
                    watchGame = true;
                }
            }
        };

        mainMenuLabel = new Button("Mainmenu",
                new Vector3f(
                gui.getWidth() / 2,
                gui.getHeight() / 2.4f, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.DarkGray, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                //game.getApplication().stop();
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
                }
            }
        };
        attachToGUI(gui);
    }

    private void attachToGUI(final GameGUI gui) {
        attachChild(background);
        attachChild(gameOverLabel);
        attachChild(mainMenuLabel);
        attachChild(watchGameLabel);
        gui.addGUIElement(this);
//        gui.addGUIElement(background);
//        gui.addGUIElement(gameOverLabel);
//        gui.addGUIElement(mainMenuLabel);
//        gui.addGUIElement(watchGameLabel);
        setVisible(false);
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#canGainFocus()
     */
    @Override
    public boolean canGainFocus() {
        return false;
    }

    /**
     * Sets the game over state.
     *
     * @param state the new game over state
     */
    private void setGameOverState(GameOverState state) {
        String label;
        if (state == GameOverState.WON) {
            label = "Game won!";
        } else {
            label = "Game lost!";
        }

        gameOverLabel.setCaption(label);

    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        if ((SolarWarsGame.getInstance().getCurrentLevel().isGameOver() && !isVisible() && !watchGame)) {
            setGameOverState((Hub.getLocalPlayer().hasLost())
                    ? GameOverState.LOST
                    : GameOverState.WON);
            display();
        }
        if (isVisible()) {
            gameOverLabel.updateGUI(tpf);
            mainMenuLabel.updateGUI(tpf);
        }
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        gameOverLabel.setVisible(show);
        mainMenuLabel.setVisible(show);
        watchGameLabel.setVisible(show);
    }

    /**
     * Pause.
     */
    public void display() {
        if (gui != null) {
            setVisible(true);
        }
    }

    /**
     * Unpause.
     */
    public void hide() {
        if (gui != null) {
            setVisible(false);
        }
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}