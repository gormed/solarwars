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
package com.solarwars.gamestates;

import com.solarwars.AudioManager;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gamestates.gui.AddServerPopup;
import com.solarwars.gamestates.gui.SavedServerItem;
import com.solarwars.net.NetworkManager;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import java.util.List;
import java.util.logging.Level;

/**
 * The Class MultiplayerState.
 */
public class MultiplayerState extends Gamestate {

    private NetworkManager networkManager;
    private String currentIPAddress;
    private ListBox<SavedServerItem> serverListBox;
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
        currentIPAddress = SolarWarsSettings.getInstance().
                getIpAddressFavouriteServer();
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
    @SuppressWarnings("unchecked")
    @Override
    protected void loadContent() {
        // switch to multiplayer_menu gui
        niftyGUI.gotoScreen("multiplayer_menu");
        addServerPopup = new AddServerPopup(niftyGUI);
        serverListBox = screen.findNiftyControl(
                "saved_servers_box", ListBox.class);

        serverListBox.clear();
        serverListBox.addItem(localServer);
        serverListBox.addItem(lastServer);

        // init network manager
        networkManager = NetworkManager.getInstance();

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
    public void onAddNewServerButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        serverListBox.addItem(new SavedServerItem(
                addServerPopup.getNewServerName(),
                addServerPopup.getNewServerIP()));

//        serverListBox.addItem(addServerPopup.getNewServerName()
//                + " - " + addServerPopup.getNewServerIP());
        addServerPopup.hidePopup();
    }

    /**
     * Cancels the adding of a new fav.
     */
    public void onCancelAddServerButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        addServerPopup.hidePopup();
    }

    /**
     * Opens the add server popup.
     */
    public void onDeleteServerButton() {
        Object s = serverListBox.getSelection().get(0);
        if (s != null && s instanceof SavedServerItem) {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_CLICK);
            serverListBox.removeItem((SavedServerItem) s);
        }
    }

    /**
     * Opens the add server popup.
     */
    public void onAddServerButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        addServerPopup.showPopup();
    }

    @NiftyEventSubscriber(id = "saved_servers_box")
    public void onListBoxSelectionChanged(final String id,
            final ListBoxSelectionChangedEvent<SavedServerItem> event) {
        List<SavedServerItem> selection = event.getSelection();

        if (!selection.isEmpty() && selection.get(0) != null) {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_CLICK);
            currentIPAddress = selection.get(0).getIp();
        }
    }

//    public void listBoxItemClicked() {
//        SavedServerItem focus = serverListBox.getFocusItem();
//        int idx = serverListBox.getFocusItemIndex();
//        ArrayList<SavedServerItem> clone =
//                new ArrayList<SavedServerItem>(serverListBox.getSelection());
//        for (SavedServerItem item : clone) {
//            serverListBox.deselectItem(item);
//        }
//        if (focus != null) {
//            serverListBox.selectItemByIndex(idx);
//        }
//
//        for (SavedServerItem item : clone) {
//            if (!item.equals(focus)) {
//                serverListBox.deselectItem(item);
//            }
//        }
//    }
    @NiftyEventSubscriber(id = "player_name")
    public void onPlayerNameChanged(final String id,
            final TextFieldChangedEvent event) {
        SolarWarsSettings.getInstance().setPlayerName(event.getText());
    }

    /**
     * Creates the server.
     */
    public void onCreateServerButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.CREATE_SERVER_STATE);
    }

    /**
     * Join server.
     */
    public void onJoinServerButton() {
        String ip = currentIPAddress;

        if (NetworkManager.checkIP(ip)) {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_CLICK);
            SolarWarsSettings.getInstance().setIpAddressFavouriteServer(ip);
            switchToState(SolarWarsGame.SERVER_LOBBY_STATE);
        } else {
            AudioManager.getInstance().playSoundInstance(AudioManager.SOUND_ERROR);
            // TODO YVES The server favs should be collected in an extra xml if possible
            ip = NetworkManager.getInstance().getClientIPAdress().getHostAddress();
            SolarWarsSettings.getInstance().setIpAddressFavouriteServer(ip);
        }
    }

//    @NiftyEventSubscriber(id = "back")
    public void onBackButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }
}
