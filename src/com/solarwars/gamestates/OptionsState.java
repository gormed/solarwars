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
 * Type: com.solarwars.gamestates.lib.TutorialState
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.solarwars.AudioManager;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.gui.GameGUI;
import com.solarwars.gui.elements.Button;
import com.solarwars.gui.elements.CheckBox;
import com.solarwars.gui.elements.Label;
import com.solarwars.gui.elements.Panel;


/**
 * The Class TutorialState.
 *
 * @author Hans
 */
public class OptionsState extends Gamestate {

    /** The tutorial label. */
    private Label optionsLabel;
    /** The background. */
    private Panel background;
    /** The gui. */
    private GameGUI gui;
    /** The back. */
    private Button back;
    /** The line. */
    private Panel line;
    
    private CheckBox box_test;

    /**
     * Instantiates a new tutorial state.
     */
    public OptionsState() {
        super(SolarWarsGame.OPTIONS_STATE);
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
        gui = GameGUI.getInstance();

        createStateGUI();

        gui.addGUIElement(optionsLabel);
        gui.addGUIElement(background);
        gui.addGUIElement(line);
        gui.addGUIElement(box_test);
        gui.addGUIElement(back);
    }

    private void createStateGUI() {
        optionsLabel = new Label(
                "Options",
                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
                new Vector3f(2, 2, 1),
                ColorRGBA.White.clone(),
                gui) {

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

        background = new Panel(
                "BackgroundFrame",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
                ColorRGBA.Blue.clone());
        
        line = new Panel("Line", new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
                ColorRGBA.White.clone());

        box_test = new CheckBox(
                "Ckeck_Test",
                new Vector3f(
                        gui.getWidth() / 10, 
                        7.0f * gui.getHeight() / 10, 
                        0),
                new Vector2f(24f, 24f), 
                ColorRGBA.Blue.clone());

        back = new Button("Back",
                new Vector3f(gui.getWidth() / 10, 1.0f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ.clone(),
                ColorRGBA.Orange.clone(),
                ColorRGBA.White.clone(), 
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    switchToState(SolarWarsGame.MAINMENU_STATE);
//                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
                }
            }
        };
        
        
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        gui.cleanUpGUI();
        gui = null;
    }
}
