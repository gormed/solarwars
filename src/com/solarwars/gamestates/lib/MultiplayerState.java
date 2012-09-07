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
 * File: MultiplayerState.java
 * Type: com.solarwars.gamestates.lib.MultiplayerState
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.lib;

import com.solarwars.AudioManager;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.Gamestate;
import com.solarwars.net.NetworkManager;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import java.util.logging.Level;

/**
 * The Class MultiplayerState.
 */
public class MultiplayerState extends Gamestate {

    public static class SavedServerItem {

        private final String name;
        private final String ip;

        public SavedServerItem(String name, String ip) {
            this.name = name;
            this.ip = ip;
        }

        public String getIp() {
            return ip;
        }

        public String getName() {
            return name;
        }
    }

    public static class AddServerPopup {

        private Nifty niftyGUI;
        private Element addServerPopup;
        private boolean visible = false;

        public AddServerPopup(Nifty niftyGUI) {
            this.niftyGUI = niftyGUI;
        }

        public String getNewServerIP() {
            TextField ip = niftyGUI.getCurrentScreen().
                    findNiftyControl("ip_address", TextField.class);
            return ip.getText();
        }

        public String getNewServerName() {
            TextField name = niftyGUI.getCurrentScreen().
                    findNiftyControl("server_name", TextField.class);
            return name.getText();
        }

        public void showPopup() {
            if (visible) {
                return;
            }
            if (addServerPopup == null) {
                addServerPopup = niftyGUI.createPopup("add_server_popup");
            }
            niftyGUI.showPopup(niftyGUI.getCurrentScreen(),
                    addServerPopup.getId(), null);
            visible = true;
        }

        public void hidePopup() {
            if (addServerPopup != null && visible) {
                niftyGUI.closePopup(addServerPopup.getId());
                visible = false;
            }
        }

        public void showElement(final Element element, final EndNotify... endNotify) {
            element.showWithoutEffects();
            element.startEffect(
                    EffectEventId.onCustom,
                    new EndNotify() {

                        @Override
                        public void perform() {
                            for (EndNotify ed : endNotify) {
                                ed.perform();
                            }
                        }
                    },
                    "in");
        }

        public void hideElement(final Element element, final EndNotify... endNotify) {
            element.startEffect(
                    EffectEventId.onCustom,
                    new EndNotify() {

                        @Override
                        public void perform() {
                            element.hideWithoutEffect();
                            for (EndNotify ed : endNotify) {
                                ed.perform();
                            }
                        }
                    },
                    "out");
        }
    }
    private NetworkManager networkManager;
    private String currentIPAddress = SolarWarsSettings.getInstance().
            getIpAddressFavouriteServer();
    private ListBox serverListBox;
    private SavedServerItem localServer =
            new SavedServerItem("LOCAL", "127.0.0.1");
    private SavedServerItem lastServer =
            new SavedServerItem("LAST", getFavIPAddress());
    private AddServerPopup addServerPopup;

    /**
     * Instantiates a new multiplayer state.
     */
    public MultiplayerState() {
        super(SolarWarsGame.MULTIPLAYER_STATE);

    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#loadContent(com.solarwars.SolarWarsGame)
     */
    @Override
    protected void loadContent() {
        // switch to multiplayer_menu gui
        niftyGUI.gotoScreen("multiplayer_menu");
        addServerPopup = new AddServerPopup(niftyGUI);
        serverListBox = screen.findNiftyControl(
                "saved_servers_box", ListBox.class);
//        if (!serverListBox.getItems().contains(localServer)) {
//            serverListBox.addItem(localServer);
//        }
//        if (!serverListBox.getItems().contains(lastServer)) {
//            serverListBox.addItem(lastServer);
//        }
        serverListBox.clear();
        serverListBox.addItem(localServer.name + " - " + localServer.ip);
        serverListBox.addItem(lastServer.name + " - " + lastServer.ip);
        // init network manager
        networkManager = NetworkManager.getInstance();
//        playerName = new TextBox(
//                ColorRGBA.Blue.clone(),
//                new Vector3f(gui.getWidth() / 2, 7 * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ.clone(),
//                SolarWarsSettings.getInstance().getPlayerName(),
//                ColorRGBA.White.clone(),
//                gui, false) {
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//
//            @Override
//            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
//                if (caption.length() > 8) {
//                    caption = caption.substring(0, caption.length() - 1);
//                }
//                SolarWarsSettings.getInstance().setPlayerName(caption);
//            }
//        };
//
//        joinServer = new Button("Join Sever",
//                new Vector3f(gui.getWidth() / 4f, 5.5f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ.clone(),
//                ColorRGBA.Orange.clone(),
//                ColorRGBA.White.clone(), gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//                if (!isPressed) {
//                    AudioManager.getInstance().
//                            playSoundInstance(AudioManager.SOUND_CLICK);
//                    joinServer();
//                }
//            }
//        };
//
//        serverip = new TextBox(
//                ColorRGBA.Blue.clone(),
//                new Vector3f(gui.getWidth() / 4f, 4.5f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ.clone(),
//                SolarWarsSettings.getInstance().getIpAddressFavouriteServer(),
//                ColorRGBA.White.clone(), gui, true) {
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//
//            @Override
//            protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
//                SolarWarsSettings.getInstance().setIpAddressFavouriteServer(caption);
//            }
//        };
//
//        back = new Button("Back",
//                new Vector3f(gui.getWidth() / 2, 1.5f * gui.getHeight() / 10, 0),
//                Vector3f.UNIT_XYZ.clone(),
//                ColorRGBA.Orange.clone(),
//                ColorRGBA.White.clone(), gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//                if (!isPressed) {
//                    AudioManager.getInstance().
//                            playSoundInstance(AudioManager.SOUND_CLICK);
//                    switchToState(SolarWarsGame.MAINMENU_STATE);
////                    GamestateManager.getInstance().enterState(GamestateManager.MAINMENU_STATE);
//                }
//            }
//        };
//
//        createServer = new Button("Create Sever",
//                new Vector3f(
//                        3 * gui.getWidth() / 4f, 
//                        5.5f * gui.getHeight() / 10, 
//                        0),
//                Vector3f.UNIT_XYZ.clone(),
//                ColorRGBA.Orange.clone(),
//                ColorRGBA.White.clone(), gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//                if (!isPressed) {
//                    AudioManager.getInstance().
//                            playSoundInstance(AudioManager.SOUND_CLICK);
//                    createServer();
//                }
//            }
//        };
//
//        multiplayerLabel = new Label(
//                "MULTIPLAYER",
//                new Vector3f(gui.getWidth() / 2, 9 * gui.getHeight() / 10, 4),
//                new Vector3f(2, 2, 1),
//                ColorRGBA.White.clone(), gui) {
//
//            private float time;
//
//            @Override
//            public void updateGUI(float tpf) {
//                time += tpf;
//
//                if (time < 0.2f) {
//                    text.setText(title + "_");
//                } else if (time < 0.4f) {
//                    text.setText(title);
//                } else {
//                    time = 0;
//                }
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//        };
//
//        backgroundPanel = new Panel(
//                "BackgroundPanel",
//                new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0),
//                new Vector2f(gui.getWidth() * 0.47f, gui.getHeight() * 0.47f),
//                ColorRGBA.Blue.clone());
//
//        line = new Panel("Line", new Vector3f(gui.getWidth() / 2, 8 * gui.getHeight() / 10, 0),
//                new Vector2f(gui.getWidth() * 0.4f, gui.getHeight() * 0.005f),
//                ColorRGBA.White.clone());

//        gui.addGUIElement(backgroundPanel);
//        gui.addGUIElement(line);
//        gui.addGUIElement(multiplayerLabel);
//        gui.addGUIElement(playerName);
//        gui.addGUIElement(joinServer);
//        gui.addGUIElement(createServer);
//        gui.addGUIElement(serverip);
//        gui.addGUIElement(back);
    }

    /* (non-Javadoc)
     * @see com.solarwars.gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
        try {
            SolarWarsSettings.getInstance().save();
        } catch (GameSettingsException e) {
            SolarWarsApplication.getClientLogger().
                    log(Level.WARNING, "{0} caused by {1}",
                    new Object[]{e.getMessage(), e.getCause().getMessage()});
        }
//        gui.cleanUpGUI();
//        gui = null;
    }

    public String getPlayerName() {
        return SolarWarsSettings.getInstance().getPlayerName();
    }

    public String getFavIPAddress() {
        return currentIPAddress = SolarWarsSettings.getInstance().getIpAddressFavouriteServer();
    }

    /**
     * Adds a new server to the favs.
     */
    @NiftyEventSubscriber(id = "add_new_server")
    public void onAddNewServerButton(final String id,
            final ButtonClickedEvent event) {
        serverListBox.addItem(addServerPopup.getNewServerName()
                + " - " + addServerPopup.getNewServerIP());
        currentIPAddress = addServerPopup.getNewServerIP();
        addServerPopup.hidePopup();
    }

    /**
     * Cancels the adding of a new fav.
     */
    @NiftyEventSubscriber(id = "add_server_back")
    public void onCancelAddServerButton(final String id,
            final ButtonClickedEvent event) {
        addServerPopup.hidePopup();
    }

    /**
     * Opens the add server popup.
     */
    @NiftyEventSubscriber(id = "delete_server")
    public void onDeleteServerButton(final String id,
            final ButtonClickedEvent event) {
        Object s = serverListBox.getSelection().get(0);
        if (s != null) {
            serverListBox.removeItem(s);
        }
    }

    /**
     * Opens the add server popup.
     */
    @NiftyEventSubscriber(id = "add_server")
    public void onAddServerButton(final String id,
            final ButtonClickedEvent event) {
        addServerPopup.showPopup();
    }

    /**
     * Creates the server.
     */
    @NiftyEventSubscriber(id = "create_server")
    public void onCreateServerButton(final String id,
            final ButtonClickedEvent event) {
        switchToState(SolarWarsGame.CREATE_SERVER_STATE);
    }

    /**
     * Join server.
     */
    @NiftyEventSubscriber(id = "join_server")
    public void onJoinServerButton(final String id,
            final ButtonClickedEvent event) {
        Object s = serverListBox.getSelection().get(0);
        if (s == null) {
            return;
        }
        String sel = (String) s;
        String[] splitt = sel.split(" - ");
        String ip = splitt[1];

        if (NetworkManager.checkIP(ip)) {
            switchToState(SolarWarsGame.SERVER_LOBBY_STATE);
        } else {
            AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_ERROR);
            // TODO YVES The server favs should be collected in an extra xml if possible
            ip = NetworkManager.getInstance().getClientIPAdress().getHostAddress();
            SolarWarsSettings.getInstance().setIpAddressFavouriteServer(ip);
        }
    }

    @NiftyEventSubscriber(id = "back")
    public void onBackButton(final String id,
            final ButtonClickedEvent event) {
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }
}
