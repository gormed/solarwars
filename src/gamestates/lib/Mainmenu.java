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
import gui.Button;
import gui.GameGUI;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class Mainmenu extends Gamestate {

    /** The gui. */
    private GameGUI gui;
    
    private Button startButton;
    private Button solarwars;
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
        
        solarwars = new Button("SOLARWARS", new Vector3f(gui.getWidth() / 2,
                gui.getHeight(), 4), new Vector3f(2, 2, 1), ColorRGBA.White, gui) {

            private float time;        
                    
            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            public void updateGUI(float tpf) {
                
                time += tpf;
                
                if (time < 0.2f) {
                    text.setText(title+"_");
                } else if (time < 0.4f) {
                    text.setText(title);
                } else {
                    time = 0;
                }
            }
        };
        
        
        startButton = new Button("Singleplayer",
                new Vector3f(gui.getWidth() / 2,
                3 * gui.getHeight() / 4, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Cyan
                ,gui) {

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
        
        quitButton = new Button("Quit Game", new Vector3f(gui.getWidth() / 2,
                2 *gui.getHeight() / 4, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Cyan, gui) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                game.getApplication().stop();
            }

            @Override
            public void updateGUI(float tpf) {
            }
        };
        
        gui.addGUIElement(solarwars);
        gui.addGUIElement(startButton);
        gui.addGUIElement(quitButton);
    }

    @Override
    protected void unloadContent() {
        gui.removeGUIElement(startButton);
        gui.removeGUIElement(quitButton);
        gui.removeGUIElement(solarwars);
        
        gui = null;
    }
    
    private void startSingleplayer() {
        GamestateManager gm = GamestateManager.getInstance();
        
        gm.enterState(GamestateManager.SINGLEPLAYER_STATE);
    }
}
