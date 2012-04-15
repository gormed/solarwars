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
 * File: MultiplayerMatchState.java
 * Type: gamestates.lib.MultiplayerMatchState
 * 
 * Documentation created: 31.03.2012 - 19:27:45 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ClientStateListener.DisconnectInfo;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.GameGUI;
import gui.elements.GameOverGUI;
import gui.elements.PauseGUI;
import gui.elements.Percentage;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import logic.Gameplay;
import logic.MultiplayerGameplay;
import logic.Level;
import net.NetworkManager;
import solarwars.AudioManager;
import solarwars.Hub;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 * The Class MultiplayerMatchState.
 */
public class MultiplayerMatchState extends Gamestate {

    private GameGUI gui;
    private SolarWarsGame game;
    private PauseActionListener pauseListener;
    private PauseGUI pause;
    private Hub hub;
    private Level currentLevel;
    private MultiplayerGameplay gameplay;
    private Client client;
    private final SolarWarsApplication application;
    private PlayerStateListener playerStateListener = new PlayerStateListener();
    private boolean lostConnection;

    /**
     * Instantiates a new multiplayer match state.
     */
    public MultiplayerMatchState() {
        super(GamestateManager.MULTIPLAYER_MATCH_STATE);
        this.application = SolarWarsApplication.getInstance();

    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (!lostConnection) {
            gameplay.update(tpf);
            gui.updateGUIElements(tpf);
            currentLevel.updateLevel(tpf);
        } else if (lostConnection && !currentLevel.isGameOver()) {
            GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
        }
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        lostConnection = false;
        hub = Hub.getInstance();
        this.game = game;
        application.setPauseOnLostFocus(false);
        client = NetworkManager.getInstance().getThisClient();
        client.addClientStateListener(playerStateListener);
        gameplay = MultiplayerGameplay.getInstance();
        setupGUI();
        currentLevel = Gameplay.getCurrentLevel();
        currentLevel.generateLevel();
        currentLevel.setupPlayers(Hub.playersByID);

        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        lostConnection = false;

        Future fut = application.enqueue(new Callable() {

            public Object call()
                    throws Exception {

                return NetworkManager.getInstance().closeAllConnections(true);
            }
        });

        GameOverGUI.getInstance().hide();
        application.getInputManager().removeListener(pauseListener);
        pauseListener = null;

        hub = null;

        currentLevel.destroy();

        gui.cleanUpGUI();
        gui = null;

        if (client != null) {
            client.removeClientStateListener(playerStateListener);
        }
        gameplay.destroy();
        gameplay = null;
        application.detachIsoCameraControl();

    }

    /**
     * Setup gui.
     */
    private void setupGUI() {
        gui = new GameGUI(game);
        gui.addGUIElement(new Percentage(gui));
        pause = new PauseGUI(game, gui);

        pauseListener = new PauseActionListener();

        application.getInputManager().addMapping(
                SolarWarsApplication.INPUT_MAPPING_PAUSE,
                new KeyTrigger(KeyInput.KEY_P),
                new KeyTrigger(KeyInput.KEY_PAUSE),
                new KeyTrigger(KeyInput.KEY_ESCAPE));
        game.getApplication().getInputManager().addListener(
                pauseListener,
                SolarWarsApplication.INPUT_MAPPING_PAUSE);
    }

    /**
     * The listener interface for receiving playerState events.
     * The class that is interested in processing a playerState
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addPlayerStateListener<code> method. When
     * the playerState event occurs, that object's appropriate
     * method is invoked.
     *
     * @see PlayerStateEvent
     */
    private class PlayerStateListener implements ClientStateListener {

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientConnected(com.jme3.network.Client)
         */
        public void clientConnected(Client c) {
        }

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientDisconnected(com.jme3.network.Client, com.jme3.network.ClientStateListener.DisconnectInfo)
         */
        public void clientDisconnected(Client c, DisconnectInfo info) {
            System.out.print("[Client #" + c.getId() + "] - Disconnect from server: ");

            if (info != null) {
                System.out.println(info.reason);
                lostConnection = true;
            } else {
                System.out.println("client closed");
                lostConnection = true;
            }
//                if (c.equals(client)) {
//                    noServerFound = true;
//                }
        }
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
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                return;
            }
            if (name.equals(SolarWarsApplication.INPUT_MAPPING_PAUSE)) {
                pause.togglePause();
            }
        }
    }
}