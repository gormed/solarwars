/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.elements.Button;
import gui.elements.Label;
import gui.GameGUI;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class Mainmenu extends Gamestate {

    /** The gui. */
    private GameGUI gui;
    private Button singleplayerButton;
    private Button multiplayerButton;
    private Label solarwars;
    private Button quitButton;
    private SolarWarsGame game;
    /*
     * 
     */

    public Mainmenu(SolarWarsGame game) {
        super(GamestateManager.MAINMENU_STATE);
        this.game = game;
    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

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
    }

    @Override
    protected void unloadContent() {
        gui.removeGUIElement(singleplayerButton);
        gui.removeGUIElement(multiplayerButton);
        gui.removeGUIElement(quitButton);
        gui.removeGUIElement(solarwars);

        gui = null;
    }

    private void startSingleplayer() {
        GamestateManager.getInstance().enterState(GamestateManager.SINGLEPLAYER_STATE);
    }
    
    
    private void startMultiplayer() {
        GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
    }
}
