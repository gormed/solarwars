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
 * File: Singelplayer.java
 * Type: gamestates.lib.Singelplayer
 * 
 * Documentation created: 15.03.2012 - 20:36:20 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import gamestates.Gamestate;
import gui.GameGUI;
import gui.Percentage;
import logic.Player;
import logic.level.Level;
import solarwars.Hub;

/**
 * The Class Singelplayer.
 */
public class Singelplayer extends Gamestate {

    /** The application. */
    private solarwars.SolarWarsApplication application;
    
    /** The game. */
    private solarwars.SolarWarsGame game;
    
    /** The current level. */
    private Level currentLevel;
    
    /** The gui. */
    private GameGUI gui;
    
    /** The hub. */
    private Hub hub;

    /**
     * Instantiates a new singelplayer.
     *
     * @param game the game
     */
    public Singelplayer(solarwars.SolarWarsGame game) {
        super("Singelplayer State");
        this.game = game;
        this.application = this.game.getApplication();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent()
     */
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

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        currentLevel.updateLevel(tpf);
        gui.updateGUIElements(tpf);
    }

    /**
     * Setup singleplayer.
     */
    private void setupSingleplayer() {
        Player human = new Player("Human", ColorRGBA.Blue);
        hub.addPlayer(human);
        hub.setLocalPlayer(human);

        Player ai = new Player("AI", ColorRGBA.Red);
        hub.addPlayer(ai);
    }

    /**
     * Setup gui.
     */
    private void setupGUI() {
        gui = new GameGUI(game);
        gui.addGUIElement(new Percentage(gui));

    }

    /**
     * Load.
     *
     * @param seed the seed
     */
    public void load(long seed) {
        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl(),
                seed);
    }

    /**
     * Save.
     *
     * @return the level
     */
    public Level save() {
        return currentLevel;
    }
}
