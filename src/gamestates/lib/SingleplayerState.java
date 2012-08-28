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
 * File: SingleplayerState.java
 * Type: gamestates.lib.SingleplayerState
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.GameOverGUI;
import gui.elements.PauseGUI;
import gui.elements.Percentage;
import gui.elements.ScoresGUI;
import input.InputMappings;
import logic.DeathmatchGameplay;
import logic.Level;
import logic.Player;
import net.ServerHub;
import solarwars.AudioManager;
import solarwars.Hub;
import solarwars.IsoControl;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;

/**
 * The Class SingleplayerState.
 */
public class SingleplayerState extends Gamestate {

    /** The application. */
    private solarwars.SolarWarsApplication application;
    /** The game. */
    private solarwars.SolarWarsGame game;
    /** The current level. */
    private Level currentLevel;
    /** The gui. */
    private GameGUI gui;
    /** The pause. */
    private PauseGUI pause;
    /** The tab scores. */
    private ScoresGUI tabScores;
    /** The hub. */
    private Hub hub;
    /** The pause listener. */
    private PauseActionListener pauseListener;
    private GameOverGUI gameOverGUI;

    /**
     * Instantiates a new singleplayer state.
     *
     * @param game the game
     */
    public SingleplayerState(solarwars.SolarWarsGame game) {
        super(GamestateManager.SINGLEPLAYER_STATE);
        this.game = game;
        this.application = this.game.getApplication();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent()
     */
    @Override
    protected void loadContent(solarwars.SolarWarsGame game) {
        gui = GameGUI.getInstance();
        hub = Hub.getInstance();
        setupSingleplayer();
        application.attachIsoCameraControl();

        currentLevel = new Level(
                application.getRootNode(),
                application.getAssetManager(),
                application.getIsoControl(),
                gui, Hub.playersByID);
        game.setupGameplay(new DeathmatchGameplay(), currentLevel);
        currentLevel.generateLevel(System.currentTimeMillis());
        //currentLevel.setupPlayers(Hub.playersByID);
        setupGUI();
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        application.getInputManager().removeListener(pauseListener);
        pauseListener = null;

        hub = null;

        currentLevel.destroy();
        tabScores.destroy();
        tabScores = null;
        gui.cleanUpGUI();
        gui = null;
        application.detachIsoCameraControl();
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {

        currentLevel.updateLevel(tpf);
    }

    /**
     * Setup singleplayer.
     */
    private void setupSingleplayer() {
        Player local = new Player("Human", ColorRGBA.Green, ServerHub.getContiniousPlayerID(), true);
        Player ai = new Player("AI", ColorRGBA.Red, ServerHub.getContiniousPlayerID());

        hub.initialize(local, null);
        hub.addPlayer(ai);
        hub.addPlayer(local);
    }

    /**
     * Setup gui.
     */
    private void setupGUI() {
        //percentage label
        gui.addGUIElement(new Percentage(gui));
        // setup the pause menue function
        createPauseGUI();
        // init game over gui
        gameOverGUI = GameOverGUI.getInstance();
        gameOverGUI.hide();
        // setup the tab-score menue function
        createScoresGUI();
        // creates the drag-rect geometry
        IsoControl.getInstance().createDragRectGeometry();
    }

    private void createScoresGUI() {
        // init scores panel
        tabScores = new ScoresGUI(gui);

        //tabScores.setVisible(false);
        application.getInputManager().
                addListener(
                tabScores.getActionListener(),
                InputMappings.GAME_SCORES);
    }

    /**
     * Creates the pause gui and its listeners.
     */
    private void createPauseGUI() {

        pause = new PauseGUI(game, gui);
        pauseListener = new PauseActionListener();
        game.getApplication().getInputManager().addListener(
                pauseListener,
                InputMappings.PAUSE_GAME);
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
                gui,
                Hub.playersByID,
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

    /**
     * The listener interface for receiving pauseAction events.
     * The class that is interested in processing a pauseAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addPauseActionListener<code> method. When
     * the pauseAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see PauseActionEvent
     */
    private class PauseActionListener implements ActionListener {

        /* (non-Javadoc)
         * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
         */
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                return;
            }
            if (name.equals(InputMappings.PAUSE_GAME)) {
                pause.togglePause();
            }
        }
    }
}
