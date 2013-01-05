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
 * File: ControlsState.java
 * Type: com.solarwars.gamestates.lib.ControlsState
 *
 * Documentation created: 30.12.2012 - 22:38:01 by Hans Ferchland
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.solarwars.AudioManager;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.controls.ControlManager;
import com.solarwars.gamestates.gui.ControllerItem;
import com.solarwars.gamestates.gui.MappingItem;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import java.util.List;
import java.util.logging.Level;

/**
 * CLass that represents the state to configure the game controls - either
 * keyboard+mouse or gamepad.
 *
 * @author Hans Ferchland
 */
public class ControlsState extends Gamestate {

    public static final String DETECTED_CONTROLS_BOX = "detected_controls_box";
    public static final String CONTROL_MAPPING_BOX = "control_mapping_box";
    private static final String[] MAPPINGS = {"SELECT", "ATTACK", "PERCENTAGE UP",
        "PERCENTAGE DOWN", "SCORES", "EXIT", "PAUSE",
        "CHAT", "MOVE VERTICAL", "MOVE HORIZONTAL" };
    private ListBox<ControllerItem> controlsListBox;
    private ListBox<MappingItem> mappingsListBox;

    public ControlsState() {
        super("Controls");
    }

    @Override
    protected void loadContent() {
        niftyGUI.gotoScreen("controls");
        controlsListBox = screen.findNiftyControl(
                DETECTED_CONTROLS_BOX, ListBox.class);
        mappingsListBox = screen.findNiftyControl(
                CONTROL_MAPPING_BOX, ListBox.class);

        mappingsListBox.clear();
        controlsListBox.clear();

        for (Joystick j : ControlManager.getInstance().getJoysticks()) {
            controlsListBox.addItem(new ControllerItem(j.getName(), j));
        }
        mappingsListBox.addItem(new MappingItem("Hallo", "Mapping"));
    }

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

    public void onBackButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.OPTIONS_STATE);
    }

    @NiftyEventSubscriber(id = DETECTED_CONTROLS_BOX)
    public void onListBoxSelectionChanged(final String id,
            final ListBoxSelectionChangedEvent<ControllerItem> event) {
        List<ControllerItem> selection = event.getSelection();

        if (!selection.isEmpty() && selection.get(0) != null) {
            AudioManager.getInstance().
                    playSoundInstance(AudioManager.SOUND_CLICK);
            Joystick j = selection.get(0).getJoystick();
            setMappingsList(j);
        }
    }

    private void setMappingsList(Joystick j) {
        List<JoystickButton> buttons = j.getButtons();
        List<JoystickAxis> axis = j.getAxes();
        int idx = 0;
        mappingsListBox.clear();
        for (String s : MAPPINGS) {
            String key;
            if (idx < buttons.size()) {
                key = buttons.get(idx).getName();
            } else {
                key = axis.get(idx-buttons.size()).getName();
            }
            mappingsListBox.addItem(new MappingItem(s,key));
            idx++;
        }
        //JoystickButton b = j.getButton("A");
    }
}
