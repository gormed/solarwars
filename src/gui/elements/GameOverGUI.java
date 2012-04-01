/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import gamestates.GamestateManager;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class GameOverGUI extends GUIElement implements ClickableGUI {

    public enum GameOverState {

        WON,
        LOST
    }
    /** The gui. */
    private GameGUI gui;
    /** The font. */
    protected BitmapFont font;
    /** The text. */
    protected BitmapText text;
    /** The geometry. */
    protected Geometry geometry;
    /** The material. */
    protected Material material;
    /** The game. */
    protected SolarWarsGame game;
    /** The display label. */
    protected Label gameOverLabel;
    /** The exit game. */
    protected Button mainMenuLabel;
    /** The continue game. */
    protected Button exitGameLabel;

    /**
     * Instantiates a new display gui.
     *
     * @param swgame the swgame
     * @param gui the gui
     */
    public GameOverGUI(SolarWarsGame swgame, final GameGUI gui, GameOverState state) {
        this.game = swgame;
        this.gui = gui;
        createPanel();

        String label;
        if (state == GameOverState.WON) {
            label = "Game won!";
        } else {
            label = "Game lost!";
        }

        gameOverLabel = new Label(label, new Vector3f(gui.getWidth() / 2,
                7 * gui.getHeight() / 10, 0), Vector3f.UNIT_XYZ, ColorRGBA.White, gui) {

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

        exitGameLabel = new Button(
                "Exit Game",
                new Vector3f(
                gui.getWidth() / 2,
                gui.getHeight() / 2.2f, 0),
                Vector3f.UNIT_XYZ,
                ColorRGBA.Orange,
                ColorRGBA.DarkGray,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                SolarWarsApplication.getInstance().stop();
            }
        };

        mainMenuLabel = new Button("Mainmenu",
                new Vector3f(
                gui.getWidth() / 2,
                gui.getHeight() / 1.8f, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.DarkGray, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                //game.getApplication().stop();
                GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
            }
        };

        attachChild(gameOverLabel);
        attachChild(mainMenuLabel);
        attachChild(exitGameLabel);

        //gui.addGUIElement(mainMenuLabel);


    }

    /**
     * Creates the panel.
     */
    private void createPanel() {
        AssetManager assetManager = game.getApplication().getAssetManager();

        Box b = new Box(gui.getWidth() / 4, gui.getHeight() / 5, 1);
        geometry = new Geometry("GameOverPanel", b);

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0f, 0f, 0.8f, 1f));
        geometry.setMaterial(material);
        geometry.setLocalTranslation(
                gui.getWidth() / 2,
                gui.getHeight() / 1.9f,
                0);
        attachChild(geometry);
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
        if (show) {
            display();
        } else {
            hide();
        }
    }

    /**
     * Pause.
     */
    public void display() {
        gui.addGUIElement(this);
    }

    /**
     * Unpause.
     */
    public void hide() {
        gui.removeGUIElement(this);
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
