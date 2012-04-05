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
 * File: PauseGUI.java
 * Type: gui.elements.PauseGUI
 * 
 * Documentation created: 31.03.2012 - 19:27:49 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.GamestateManager;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import solarwars.SolarWarsGame;

/**
 * The Class PauseGUI.
 */
public class PauseGUI extends GUIElement implements ClickableGUI {

    /** The gui. */
    private GameGUI gui;
    protected Panel background;
    /** The game. */
    protected SolarWarsGame game;
    /** The pause label. */
    protected Label pauseLabel;
    /** The exit game. */
    protected Button mainMenu;
    /** The continue game. */
    protected Button continueGame;

    /**
     * Instantiates a new pause gui.
     *
     * @param swgame the swgame
     * @param gui the gui
     */
    public PauseGUI(SolarWarsGame swgame, final GameGUI gui) {
        this.game = swgame;
        this.gui = gui;

        background = new Panel(
                "PausePanel",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, -1),
                new Vector2f(gui.getWidth() / 4, gui.getHeight() / 5),
                ColorRGBA.Blue);

        pauseLabel = new Label("PAUSE",
                new Vector3f(gui.getWidth() / 2,
                6.5f * gui.getHeight() / 10,
                0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.White, gui) {

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

        continueGame = new Button(
                "Continue Game",
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
                    unpause();
                }
            }
        };

        mainMenu = new Button("Mainmenu",
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
                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
                }
            }
        };

    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        pauseLabel.updateGUI(tpf);
        mainMenu.updateGUI(tpf);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        pauseLabel.setVisible(show);
        mainMenu.setVisible(show);
        continueGame.setVisible(show);
    }

    /**
     * Pause.
     */
    public void pause() {
        if (gui != null) {
            gui.addGUIElement(background);
            gui.addGUIElement(pauseLabel);
            gui.addGUIElement(mainMenu);
            gui.addGUIElement(continueGame);
        }
        //setVisible(true);
    }

    /**
     * Unpause.
     */
    public void unpause() {
        if (gui != null) {
            gui.removeGUIElement(background);
            gui.removeGUIElement(pauseLabel);
            gui.removeGUIElement(mainMenu);
            gui.removeGUIElement(continueGame);
        }
        //setVisible(false);
    }

    public void togglePause() {
        if (gui.containsGUIElement(background)) {
            unpause();
        } else {
            pause();
        }
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
