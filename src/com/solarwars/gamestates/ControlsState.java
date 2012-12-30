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
import com.solarwars.AudioManager;
import com.solarwars.Hub;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.controls.ControlManager;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.controls.ListBox;
import java.util.logging.Level;

/**
 * CLass that represents the state to configure the game controls - 
 * either keyboard+mouse or gamepad.
 * @author Hans Ferchland
 */
public class ControlsState extends Gamestate {
    private ListBox controlsListBox;

    public ControlsState() {
        super("Controls");
    }

    @Override
    protected void loadContent() {
        niftyGUI.gotoScreen("controls");
        controlsListBox = screen.findNiftyControl(
                "detected_controls_box", ListBox.class);
        
        for (Joystick j : ControlManager.getInstance().getJoysticks()) {
            controlsListBox.addItem(j.getName());
        }
        
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
}
