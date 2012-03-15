/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 by Hans Ferchland
 * 
 * 
 * SolarWars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * SolarWars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: GameGUI.java
 * Type: gui.GameGUI
 * 
 * Documentation created: 15.03.2012 - 20:36:19 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import java.util.ArrayList;
import solarwars.SolarWarsGame;

/**
 * The Class GameGUI.
 */
public class GameGUI {
    
    /** The game. */
    SolarWarsGame game;
    
    /** The gui elemetns. */
    private ArrayList<GUIElement> guiElemetns;
    
    /** The width. */
    private float width;

    /**
     * Gets the height.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public float getWidth() {
        return width;
    }
    
    /** The height. */
    private float height;
    
    /**
     * Instantiates a new game gui.
     *
     * @param game the game
     */
    public GameGUI(SolarWarsGame game) {
        this.game = game;
        this.width = game.getApplication().getCamera().getWidth();
        this.height = game.getApplication().getCamera().getHeight();
        this.guiElemetns = new ArrayList<GUIElement>();
    }
    
    /**
     * Adds the gui element.
     *
     * @param guiElement the gui element
     */
    public void addGUIElement(GUIElement guiElement) {
        game.getApplication().getGuiNode().attachChild(guiElement);
        guiElemetns.add(guiElement);
    }
    
    /**
     * Removes the gui element.
     *
     * @param guiElement the gui element
     */
    public void removeGUIElement(GUIElement guiElement) {
        game.getApplication().getGuiNode().detachChild(guiElement);
        guiElemetns.remove(guiElement);
    }
    
    /**
     * Updates the gui elements.
     *
     * @param tpf the tpf
     */
    public void updateGUIElements(float tpf) {
        for (GUIElement e : guiElemetns) {
            e.updateGUI(tpf);
        }
    }
}
