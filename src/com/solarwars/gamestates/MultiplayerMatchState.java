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
 * File: MultiplayerMatchState.java
 * Type: com.solarwars.gamestates.lib.MultiplayerMatchState
 * 
 * Documentation created: 14.07.2012 - 19:37:57 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;


import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.IsoControl;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.gui.GameChatModule;
import com.solarwars.gamestates.gui.GameOverModule;
import com.solarwars.gamestates.gui.GameStatsModule;
import com.solarwars.input.InputMappings;
import com.solarwars.gamestates.gui.PausePopup;
import com.solarwars.gamestates.gui.PlayerStatsModule;
import com.solarwars.gamestates.gui.StartGamePopup;
import com.solarwars.gamestates.gui.WaitingPopup;
import com.solarwars.logic.Level;
import com.solarwars.logic.MultiplayerGameplay;
import com.solarwars.logic.Player;
import com.solarwars.net.NetworkManager;
import com.solarwars.net.messages.PlayerLeavingMessage;
import com.solarwars.net.messages.StartGameMessage;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;

/**
 * The Class MultiplayerMatchState.
 */
public class MultiplayerMatchState extends Gamestate {

    // GUI
    private PausePopup pauseListener;
    private Element statsLayer;
    private GameStatsModule gameStatsModule;
    private PlayerStatsModule playerStatsModule;
    private GameOverModule gameOverModule;
    private GameChatModule gameChatModule;
    private StartGamePopup startGamePopup;
    private WaitingPopup waitingPopup;
    // Network and game
    private Hub hub;
    private NetworkManager networkManager;
    private Level currentLevel;
    private MultiplayerGameplay gameplay;
    private Client client;
    private final SolarWarsApplication application;
    private ClientActionListener clientActionListener =
            new ClientActionListener();
    private PlayerStateListener playerStateListener =
            new PlayerStateListener();
    private boolean lostConnection;
    private boolean started = false;

    /**
     * Instantiates a new multiplayer match state.
     */
    public MultiplayerMatchState() {
        super(SolarWarsGame.MULTIPLAYER_MATCH_STATE);
        this.application = SolarWarsApplication.getInstance();
        this.networkManager = NetworkManager.getInstance();

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        // create pause listener
        pauseListener = new PausePopup(niftyGUI);
        gameOverModule = new GameOverModule(niftyGUI);
        startGamePopup = new StartGamePopup(niftyGUI);
        waitingPopup = new WaitingPopup(niftyGUI);
    }


    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (!lostConnection && started) {
                gameplay.update(tpf);
                currentLevel.updateLevel(tpf);
                updateNifty(tpf);
                if (!gameOverModule.isVisible()
                        && (currentLevel.isGameOver()
                        || Hub.getLocalPlayer().hasLost())) {
                    gameOverModule.showPopup();
                    if (currentLevel.isGameOver()) {
                        gameChatModule.playerWins(Player.getWinner());
                    }
                }
            } else if (lostConnection && !currentLevel.isGameOver()) {
                switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
            }
        }
    }

    public void startGame() {
        if (Hub.getLocalPlayer().isHost()) {
            client.send(new StartGameMessage(true));
        }
    }

    private void startMatch() {
        // play startup sound
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
        started = true;
        if (Hub.getLocalPlayer().isHost()) {
            startGamePopup.hidePopup();
        } else {
            waitingPopup.hidePopup();
        }
    }

    private void updateNifty(float tpf) {
        gameStatsModule.update(tpf);
        // only after gamestats where updated!
        playerStatsModule.update(tpf);
    }
    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent(com.solarwars.SolarWarsGame)
     */

    @Override
    protected void loadContent() {
        // NIFTY GUI
        niftyGUI.gotoScreen("multiplayer");
        setupNiftyGUI();

        // LOGIC
        lostConnection = false;
        started = false;
        hub = Hub.getInstance();
        application.setPauseOnLostFocus(false);
        client = NetworkManager.getInstance().getThisClient();
        client.addClientStateListener(playerStateListener);
        client.addMessageListener(clientActionListener,
                PlayerLeavingMessage.class, StartGameMessage.class);
        gameplay = MultiplayerGameplay.getInstance();

        currentLevel = SolarWarsGame.getInstance().getCurrentLevel();
        currentLevel.generateLevel();

        if (Hub.getLocalPlayer().isHost()) {
            startGamePopup.showPopup();
        } else {
            waitingPopup.showPopup();
        }
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        //pause gui
        application.getInputManager().removeListener(pauseListener);

        lostConnection = false;
//        NetworkManager networkManager = NetworkManager.getInstance();
//        networkManager.getChatModule().destroy();
        gameChatModule.destroy();

        Future<Thread> fut = application.enqueue(new Callable<Thread>() {

            @Override
            public Thread call() throws Exception {
                return NetworkManager.getInstance().closeAllConnections(NetworkManager.WAIT_FOR_CLIENTS);
            }
        });

        try {
            Thread diconnector = fut.get(
                    NetworkManager.MAXIMUM_DISCONNECT_TIMEOUT,
                    TimeUnit.SECONDS);
            diconnector.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiplayerMatchState.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                    ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MultiplayerMatchState.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                    ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(MultiplayerMatchState.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                    "Server-timeout: {0} {1}",
                    new Object[]{ex.getMessage(), ex});
        }

        hub = null;
        gameStatsModule = null;
        playerStatsModule = null;
        currentLevel.destroy();

        if (client != null) {
            client.removeClientStateListener(playerStateListener);
            client.removeMessageListener(clientActionListener,
                    StartGameMessage.class,
                    PlayerLeavingMessage.class);
        }

        gameplay.destroy();
        gameplay = null;
        application.detachIsoCameraControl();
    }

    /**
     * Setup gui.
     */
    private void setupNiftyGUI() {
        pauseListener.hidePopup();
        gameOverModule.hidePopup();
        // attach listener for pause layer
        application.getInputManager().addListener(
                pauseListener,
                InputMappings.PAUSE_GAME);
        // attach stats module
        gameStatsModule = new GameStatsModule(niftyGUI, currentLevel);
        gameStatsModule.addPlayers(Hub.getPlayers());

        playerStatsModule = new PlayerStatsModule(
                niftyGUI, Hub.getLocalPlayer(), gameStatsModule);
        // CHAT ------------------------------------------
        // attach chat module
        gameChatModule = new GameChatModule(niftyGUI, NetworkManager.getInstance());
        // get input fields
        textInput = niftyGUI.getCurrentScreen().findElementByName("chat_text_field");
        textInputField = textInput.findNiftyControl("chat_text_field", TextField.class);
        // add input handler for button click to send message
        textInput.addInputHandler(new KeyInputHandler() {

            @Override
            public boolean keyEvent(NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        sendMessage();
                        return true;
                }
                return false;
            }
        });
        // CHAT ------------------------------------------

        // creates the drag-rect geometry
        IsoControl.getInstance().
                createDragRectGeometry();
    }

    public void continueGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        pauseListener.hidePopup();
    }

    public void quitGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        pauseListener.hidePopup();
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
    }

    public void onWatchGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        gameOverModule.setWatchGame(true);
        gameOverModule.hidePopup();
    }

    public void onLeaveGame() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        gameOverModule.hidePopup();
        switchToState(SolarWarsGame.MULTIPLAYER_STATE);
    }

    public int refreshPercentage() {
        return (int) (Hub.getLocalPlayer().getShipPercentage() * 100);
    }
    //==========================================================================
    //===   Chat
    //==========================================================================
    private Element textInput;
    private TextField textInputField;

    /**
     * Sends a message to the chat area
     */
    public void sendMessage() {
        String message = textInputField.getText();
        if (checkMessageStyle(message)) {
            gameChatModule.localPlayerSendChatMessage(Hub.getLocalPlayer().getId(), message);
            gameChatModule.playerSays(Hub.getLocalPlayer(), message);
        }
        textInputField.setText("");
        textInput.setFocus();
    }

    private boolean checkMessageStyle(String message) {
        boolean messageLengthOkay = message.length() >= 2;
        return messageLengthOkay;
    }
    //==========================================================================
    //===   Network Listener
    //==========================================================================

    private class ClientActionListener implements MessageListener<Client> {

        /* (non-Javadoc)
         * @see com.jme3.network.MessageListener#messageReceived(java.lang.Object, com.jme3.network.Message)
         */
        @Override
        public void messageReceived(Client source, Message message) {

            if (message instanceof StartGameMessage) {
                StartGameMessage startGameMessage = (StartGameMessage) message;
                if (startGameMessage.isIngame()) {
                    startMatch();
                }
            } else if (message instanceof PlayerLeavingMessage) {
                PlayerLeavingMessage plm = (PlayerLeavingMessage) message;
                Player p = plm.getPlayer();
                p.setLeaver(true);
                gameChatModule.playerLeaves(p);
                Hub.getInstance().removePlayer(p);
            }
        }
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
        @Override
        public void clientConnected(Client c) {
        }

        /* (non-Javadoc)
         * @see com.jme3.network.ClientStateListener#clientDisconnected(com.jme3.network.Client, com.jme3.network.ClientStateListener.DisconnectInfo)
         */
        @Override
        public void clientDisconnected(Client c, DisconnectInfo info) {
            System.out.print("[Client #" + c.getId() + "] - Disconnect from server: ");

            if (info != null) {
                System.out.println(info.reason);
                lostConnection = true;
            } else {
                System.out.println("client closed");
                lostConnection = true;
            }
        }
    }
}
