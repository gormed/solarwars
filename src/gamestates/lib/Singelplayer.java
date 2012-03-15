/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import gamestates.Gamestate;
import gui.GameGUI;
import gui.Percentage;
import logic.Player;
import logic.level.Level;
import solarwars.Hub;

/**
 *
 * @author Hans
 */
public class Singelplayer extends Gamestate {

    private solarwars.SolarWarsApplication application;
    private solarwars.SolarWarsGame game;
    private Level currentLevel;
    private GameGUI gui;
    private Hub hub;

    public Singelplayer(solarwars.SolarWarsGame game) {
        super("Singelplayer State");
        this.game = game;
        this.application = this.game.getApplication();
    }

    @Override
    protected void loadContent() {
        hub = Hub.getInstance();
        setupSingleplayer();
        setupGUI();
        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl());
        currentLevel.generateLevel(System.currentTimeMillis());
        currentLevel.setupPlayers();
    }

    @Override
    protected void unloadContent() {
        
    }

    @Override
    public void update(float tpf) {
        currentLevel.updateLevel(tpf);
        gui.updateGUIElements(tpf);
    }

    private void setupSingleplayer() {
        Player human = new Player("Human", ColorRGBA.Blue);
        hub.addPlayer(human);
        hub.setLocalPlayer(human);

        Player ai = new Player("AI", ColorRGBA.Red);
        hub.addPlayer(ai);
    }

    private void setupGUI() {
        gui = new GameGUI(game);
        gui.addGUIElement(new Percentage(gui));

    }

    public void load(long seed) {
        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl(),
                seed);
    }

    public Level save() {
        return currentLevel;
    }
}
