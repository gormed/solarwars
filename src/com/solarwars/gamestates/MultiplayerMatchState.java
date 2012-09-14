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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;


import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.IsoControl;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.gui.GameChatModule;
import com.solarwars.gamestates.gui.GameOverModule;
import com.solarwars.gamestates.gui.GameStatsModule;
import com.solarwars.input.InputMappings;
import com.solarwars.input.PausePopupController;
import com.solarwars.logic.Level;
import com.solarwars.logic.MultiplayerGameplay;
import com.solarwars.logic.Player;
import com.solarwars.net.NetworkManager;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;

/**
 * The Class MultiplayerMatchState.
 */
public class MultiplayerMatchState extends Gamestate {

    // GUI
    private PausePopupController pauseListener;
    private Element statsLayer;
    private GameStatsModule gameStatsModule;
    private GameOverModule gameOverModule;
    private GameChatModule gameChatModule;
    // Network and game
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
        super(SolarWarsGame.MULTIPLAYER_MATCH_STATE);
        this.application = SolarWarsApplication.getInstance();
        // create pause listener
        pauseListener = new PausePopupController(niftyGUI);
        gameOverModule = new GameOverModule(niftyGUI);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (!lostConnection) {
                gameplay.update(tpf);
                currentLevel.updateLevel(tpf);
                updateNifty(tpf);
                if (!gameOverModule.isVisible() && 
                        (currentLevel.isGameOver() || Hub.getLocalPlayer().hasLost())) {
                    gameOverModule.showPopup();
                    gameChatModule.playerWins(Player.getWinner());
                }
            } else if (lostConnection && !currentLevel.isGameOver()) {
                switchToState(SolarWarsGame.MULTIPLAYER_STATE);
//                GamestateManager.getInstance().enterState(GamestateManager.MULTIPLAYER_STATE);
            }
        } else {
        }
    }

    private void updateNifty(float tpf) {
        // find old text
        Element niftyElement = niftyGUI.getCurrentScreen().
                findElementByName("percentage");
        // swap old with new text
        niftyElement.getRenderer(TextRenderer.class).
                setText(refreshPercentage() + "%");
        gameStatsModule.update(tpf);
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
        hub = Hub.getInstance();
        application.setPauseOnLostFocus(false);
        client = NetworkManager.getInstance().getThisClient();
        client.addClientStateListener(playerStateListener);
        gameplay = MultiplayerGameplay.getInstance();

        currentLevel = SolarWarsGame.getInstance().getCurrentLevel();
        currentLevel.generateLevel();
        //currentLevel.setupPlayers(Hub.playersByID);


        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_LOAD);
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
                    "Server did not shut down in time: {0} {1}",
                    new Object[]{ex.getMessage(), ex});
        }

//        GameOverGUI.getInstance().hide();
//        application.getInputManager().removeListener(pauseListener);
//        pauseListener = null;

        hub = null;

        currentLevel.destroy();

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
        textInput.setFocus();
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
//                if (c.equals(client)) {
//                    noServerFound = true;
//                }
        }
    }
}