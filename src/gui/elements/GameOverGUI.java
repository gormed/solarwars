/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.GamestateManager;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import logic.Gameplay;
import solarwars.AudioManager;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class GameOverGUI extends GUIElement implements ClickableGUI {

    public boolean canGainFocus() {
        return false;
    }

    public enum GameOverState {

        WON,
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
    protected Panel background;
    private boolean watchGame = false;
    private static GameOverGUI instance;

    public static GameOverGUI getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance =
                new GameOverGUI(
                SolarWarsGame.getInstance(),
                Gameplay.getCurrentLevel().getGui(),
                GameOverState.WON);
    }

    /**
     * Instantiates a new display gui.
     *
     * @param swgame the swgame
     * @param gui the gui
     */
    private GameOverGUI(SolarWarsGame swgame, final GameGUI gui, GameOverState state) {
        this.game = swgame;
        this.gui = gui;


        String label;
        if (state == GameOverState.WON) {
            label = "Game won!";
        } else {
            label = "Game lost!";
        }

        background = new Panel(
                "GameOverPanel",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 4, gui.getHeight() / 5),
                new ColorRGBA(0, 0, 1, 0.7f));


        gameOverLabel = new Label(label, new Vector3f(gui.getWidth() / 2,
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

    }

    public void setGameOverState(GameOverState state) {
        String label;
        if (state == GameOverState.WON) {
            label = "Game won!";
        } else {
            label = "Game lost!";
        }

        gameOverLabel.setCaption(label);

    }

    public boolean isWatchGame() {
        return watchGame;
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        gameOverLabel.updateGUI(tpf);
        mainMenuLabel.updateGUI(tpf);
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
            gui.addGUIElement(background);
            gui.addGUIElement(gameOverLabel);
            gui.addGUIElement(mainMenuLabel);
            gui.addGUIElement(watchGameLabel);
            setVisible(true);
        }
    }

    /**
     * Unpause.
     */
    public void hide() {
        if (gui != null) {
            gui.removeGUIElement(background);
            gui.removeGUIElement(gameOverLabel);
            gui.removeGUIElement(mainMenuLabel);
            gui.removeGUIElement(watchGameLabel);
            setVisible(false);
        }
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
