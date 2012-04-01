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
import solarwars.SolarWarsGame;

/**
 * The Class PauseGUI.
 */
public class PauseGUI extends GUIElement implements ClickableGUI {

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
        createPanel();

        pauseLabel = new Label("PAUSE", new Vector3f(gui.getWidth() / 2,
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
        
        continueGame = new Button(
                "Continue Game",
                new Vector3f(
                        gui.getWidth() / 2, 
                        gui.getHeight() / 1.8f, 0),
                Vector3f.UNIT_XYZ, 
                ColorRGBA.Orange,
                ColorRGBA.DarkGray,
                gui) {

            @Override
            public void updateGUI(float tpf) {
                
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                unpause();
            }
        };

        mainMenu = new Button("Mainmenu",
                new Vector3f(
                        gui.getWidth() / 2,
                        gui.getHeight() / 2.2f, 0),
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
        
        attachChild(pauseLabel);
        attachChild(mainMenu);
        attachChild(continueGame);
        
        //gui.addGUIElement(mainMenu);


    }

    /**
     * Creates the panel.
     */
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
    }
    
    /**
     * Pause.
     */
    public void pause() {
        gui.addGUIElement(this);
    }
    
    /**
     * Unpause.
     */
    public void unpause() {
        gui.removeGUIElement(this);
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
