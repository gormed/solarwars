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
 * File: MultiplayerState.java
 * Type: gamestates.lib.MultiplayerState
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.Ergonomics;
import gui.GameGUI;
import gui.elements.Button;
import gui.elements.Label;
import gui.elements.Panel;
import gui.elements.TextBox;
import net.NetworkManager;
import solarwars.AudioManager;
import solarwars.SolarWarsGame;

/**
 * The Class MultiplayerState.
 */
public class MultiplayerState extends Gamestate {

    /** The multiplayer label. */
    private Label multiplayerLabel;
    /** The background panel. */
    private Panel backgroundPanel;
    /** The line. */
    private Panel line;
    /** The player name. */
    private TextBox playerName;
    /** The join server. */
    private Button joinServer;
    /** The create server. */
    private Button createServer;
    /** The back. */
    private Button back;
    /** The serverip. */
    private TextBox serverip;
    /** The gui. */
    private GameGUI gui;
    /** The network manager. */
    private NetworkManager networkManager;

    /**
     * Instantiates a new multiplayer state.
     */
    public MultiplayerState() {
        super(GamestateManager.MULTIPLAYER_STATE);

    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
        gui.updateGUIElements(tpf);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent(SolarWarsGame game) {
        gui = new GameGUI(game);
        networkManager = NetworkManager.getInstance();
        playerName = new TextBox(
                ColorRGBA.Blue,
                new Vector3f(gui.getWidth() / 2, 7 * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ,
                Ergonomics.getInstance().getName(),
                ColorRGBA.White,
                gui, false) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
                Ergonomics.getInstance().setName(caption);
            }
        };

        joinServer = new Button("Join Sever",
                new Vector3f(gui.getWidth() / 4f, 5.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    joinServer();
                }
            }
        };

        serverip = new TextBox(
                ColorRGBA.Blue,
                new Vector3f(gui.getWidth() / 4f, 4.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, Ergonomics.getInstance().getIpAddress(),
                ColorRGBA.White, gui, true) {

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }

            @Override
            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
                Ergonomics.getInstance().setIpAddress(caption);
            }
        };

        back = new Button("Back",
                new Vector3f(gui.getWidth() / 2, 1.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange,
                ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
                }
            }
        };

        createServer = new Button("Create Sever",
                new Vector3f(3 * gui.getWidth() / 4f, 5.5f * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ, ColorRGBA.Orange, ColorRGBA.White, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                if (!isPressed) {
                    AudioManager.getInstance().
                            playSoundInstance(AudioManager.SOUND_CLICK);
                    createServer();
                }
            }
        };

        multiplayerLabel = new Label(
                "MULTIPLAYER",
                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
                new Vector3f(2, 2, 1),
                ColorRGBA.White, gui) {

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

        backgroundPanel = new Panel(
                "BackgroundPanel",
                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
                ColorRGBA.Blue);

        line = new Panel("Line", new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
                new Vector2f(gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
                ColorRGBA.White);

        gui.addGUIElement(backgroundPanel);
        gui.addGUIElement(line);
        gui.addGUIElement(multiplayerLabel);
        gui.addGUIElement(playerName);
        gui.addGUIElement(joinServer);
        gui.addGUIElement(createServer);
        gui.addGUIElement(serverip);
        gui.addGUIElement(back);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        gui.cleanUpGUI();
        gui = null;
    }

    /**
     * Creates the server.
     */
    private void createServer() {
        GamestateManager gm = GamestateManager.getInstance();
        Gamestate g = gm.getGamestate(GamestateManager.CREATE_SERVER_STATE);
        if (g instanceof CreateServerState) {
            CreateServerState cs = (CreateServerState) g;
            cs.setHostPlayerName(playerName.getCaption());
            cs.setHostPlayerColor(ColorRGBA.Blue);
        }
        GamestateManager.getInstance().enterState(GamestateManager.CREATE_SERVER_STATE);
    }

    /**
     * Join server.
     */
    private void joinServer() {

        String ip = serverip.getCaption();

        if (NetworkManager.checkIP(ip)) {


            GamestateManager gm = GamestateManager.getInstance();
            Gamestate g = gm.getGamestate(GamestateManager.SERVER_LOBBY_STATE);
            if (g instanceof ServerLobbyState) {
                ServerLobbyState serverLobbyState = (ServerLobbyState) g;
                serverLobbyState.setClientPlayerName(playerName.getCaption());
                serverLobbyState.setClientPlayerColor(ColorRGBA.Red);
                serverLobbyState.setServerIPAddress(ip);

                gm.enterState(GamestateManager.SERVER_LOBBY_STATE);
            }
        } else {
            serverip.setCaption("255.255.255.255");
        }
    }
}
