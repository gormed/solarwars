/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import solarwars.AudioManager;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class TutorialState extends Gamestate {

    private Label tutorialLabel;
    private TutorialPanel background;
    private GameGUI gui;
    private Button back;
    private Panel backgroundFrame;

    public TutorialState() {
        super(GamestateManager.TUTORIAL_STATE);
    }

    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        tutorialLabel = new Label(
                "Tutorial",
                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
                new Vector3f(2, 2, 1),
                ColorRGBA.White, gui) {

            private float time;

            @Override
            public void updateGUI(float tpf) {
                time += tpf;

                if (time < 0.2f) {
                    text.setText(title + "_?");
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
        
        backgroundFrame = new Panel(
                "Tutorial_BackgroundFrame",
                new Vector3f(
                0.5f * gui.getWidth(),
                0.5f * gui.getHeight() - 50,
                0),
                new Vector2f(510, 310),
//                Vector2f.UNIT_XY.clone().
//                multLocal(gui.getHeight() / 2),
                new ColorRGBA(0, 0, 1, 0.5f));

        background = new TutorialPanel(
                "Tutorial_Background",
                new Vector3f(
                0.5f * gui.getWidth(),
                0.5f * gui.getHeight() - 50,
                0),
                new Vector2f(500, 300),
//                Vector2f.UNIT_XY.clone().
//                multLocal(gui.getHeight() / 2),
                ColorRGBA.White);
        back = new Button("Back",
                new Vector3f(gui.getWidth() / 10, 1.0f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
                }
            }
        };
        
        gui.addGUIElement(tutorialLabel);
        gui.addGUIElement(backgroundFrame);
        gui.addGUIElement(background);
        gui.addGUIElement(back);
    }

    @Override
    protected void unloadContent() {
        gui.cleanUpGUI();
        gui = null;
    }

    private class TutorialPanel extends Panel {

        public TutorialPanel(String name, Vector3f pos, Vector2f size, ColorRGBA color) {
            super(name, pos, size, color);
            AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();

            material.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/solarwars-tutorial.png"));
        }
    }
}
