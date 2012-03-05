/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class GameGUI {
    
    SolarWarsGame game;
    private ArrayList<GUIElement> guiElemetns;
    private float width;

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
    private float height;
    
    public GameGUI(SolarWarsGame game) {
        this.game = game;
        this.width = game.getApplication().getCamera().getWidth();
        this.height = game.getApplication().getCamera().getHeight();
        this.guiElemetns = new ArrayList<GUIElement>();
    }
    
    public void addGUIElement(GUIElement guiElement) {
        game.getApplication().getGuiNode().attachChild(guiElement);
        guiElemetns.add(guiElement);
    }
    
    public void removeGUIElement(GUIElement guiElement) {
        game.getApplication().getGuiNode().detachChild(guiElement);
        guiElemetns.remove(guiElement);
    }
    
    public void updateGUIElements(float tpf) {
        for (GUIElement e : guiElemetns) {
            e.updateGUI(tpf);
        }
    }
}
