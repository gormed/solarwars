/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class PauseGUI extends GUIElement implements ClickableGUI {

    /** The gui. */
    private GameGUI gui;
    /** The font. */
    protected BitmapFont font;
    /** The text. */
    protected BitmapText text;
    protected Geometry geometry;
    protected Material material;
    protected SolarWarsGame game;
    protected Button pauseLabel;
    protected Button exitGame;
    protected Button continueGame;

    public PauseGUI(SolarWarsGame swgame, final GameGUI gui) {
        this.game = swgame;
        this.gui = gui;
        createPanel();

        pauseLabel = new Button("PAUSE", new Vector3f(gui.getWidth() / 2,
                3 * gui.getHeight() / 4, 0), Vector3f.UNIT_XYZ, ColorRGBA.White, gui) {

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
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 1.6f, 0),
                Vector3f.UNIT_XYZ, 
                ColorRGBA.Orange, 
                gui) {

            @Override
            public void updateGUI(float tpf) {
                
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                unpause();
            }
        };

        exitGame = new Button("Exit Game",
                new Vector3f(gui.getWidth() / 2,
                gui.getHeight() / 2, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                game.getApplication().stop();

            }
        };
        
        attachChild(pauseLabel);
        attachChild(exitGame);
        attachChild(continueGame);
        
        //gui.addGUIElement(exitGame);


    }

    private void createPanel() {
        AssetManager assetManager = game.getApplication().getAssetManager();

        Box b = new Box(gui.getWidth() / 4, gui.getHeight() / 5, 1);
        geometry = new Geometry("PausePanel", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0f, 0f, 0.8f, 1f));
        geometry.setMaterial(material);
        geometry.setLocalTranslation(
                gui.getWidth() / 2,
                gui.getHeight() / 1.9f,
                0);
        attachChild(geometry);
    }

    @Override
    public void updateGUI(float tpf) {
        pauseLabel.updateGUI(tpf);
        exitGame.updateGUI(tpf);
    }

    @Override
    public void setVisible(boolean show) {
    }
    
    public void pause() {
        gui.addGUIElement(this);
    }
    
    public void unpause() {
        gui.removeGUIElement(this);
    }

    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
