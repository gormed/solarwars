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
 * File: TutorialState.java
 * Type: gamestates.lib.TutorialState
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
 * The Class TutorialState.
 *
 * @author Hans
 */
public class TutorialState extends Gamestate {

    /** The tutorial label. */
    private Label tutorialLabel;
    
    /** The background. */
    private TutorialPanel background;
    
    /** The gui. */
    private GameGUI gui;
    
    /** The back. */
    private Button back;
    
    /** The background frame. */
    private Panel backgroundFrame;

    /**
     * Instantiates a new tutorial state.
     */
    public TutorialState() {
        super(GamestateManager.TUTORIAL_STATE);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = GameGUI.getInstance();
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

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        gui.cleanUpGUI();
        gui = null;
    }

    /**
     * The Class TutorialPanel.
     */
    private class TutorialPanel extends Panel {

        /**
         * Instantiates a new tutorial panel.
         *
         * @param name the name
         * @param pos the pos
         * @param size the size
         * @param color the color
         */
        public TutorialPanel(String name, Vector3f pos, Vector2f size, ColorRGBA color) {
            super(name, pos, size, color);
            AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();

            material.setTexture("ColorMap", assetManager.loadTexture("Textures/gui/solarwars-tutorial.png"));
        }
    }
}
