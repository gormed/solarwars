/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * solarwars Project (c) 2012 - 2013 
 * 
 * 		by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
 * 
 * solarwars is a strategy game in space. You have to eliminate 
 * all enemies to win. You can move ships between planets to capture 
 * other planets. Its oriented to multiplayer and singleplayer.
 * 
 * solarwars rights are by its owners/creators. 
 * You have no right to edit, publish and/or deliver the code or android 
 * application in any way! If that is done by someone, please report it!
 * 
 * Email me: hans{dot}ferchland{at}gmx{dot}de
 * 
 * Project: solarwars
 * File: OptionsState.java
 * Type: com.solarwars.gamestates.OptionsState
 * 
 * Documentation created: 05.01.2013 - 22:12:56 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates;

import com.solarwars.AudioManager;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class TutorialState.
 *
 * @author Hans
 */
public class OptionsState extends Gamestate {

    private static final String BACKGROUND_QUALITY_HIGH = "background-quality-high";
    private static final String BACKGROUND_QUALITY_LOW = "background-quality-low";
    private static final String BACKGROUND_QUALITY_MEDIUM = "background-quality-medium";
    private static final String PLANET_QUALITY_HIGH = "planet-quality-high";
    private static final String PLANET_QUALITY_LOW = "planet-quality-low";

    /**
     * Instantiates a new tutorial state.
     */
    public OptionsState() {
        super(SolarWarsGame.OPTIONS_STATE);
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
        niftyGUI.gotoScreen("options");
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        initRadioButtons(screen);
        screen.findNiftyControl("enable-bloom",
                CheckBox.class).setChecked(
                SolarWarsSettings.getInstance().isBloomEnabled());
        screen.findNiftyControl("enable-sound",
                CheckBox.class).setChecked(
                SolarWarsSettings.getInstance().isSoundEnabled());
        screen.findNiftyControl("enable-music",
                CheckBox.class).setChecked(
                SolarWarsSettings.getInstance().isMusicEnabled());
    }

    private void initRadioButtons(Screen screen) {
        int backgroundQuality = SolarWarsSettings.getInstance().getBackgroundQuality();
        switch (backgroundQuality) {
            case 1:
                screen.findNiftyControl(BACKGROUND_QUALITY_MEDIUM,
                        RadioButton.class).select();
                break;
            case 2:
                screen.findNiftyControl(BACKGROUND_QUALITY_HIGH,
                        RadioButton.class).select();
                break;
            case 0:
            default:
                screen.findNiftyControl(BACKGROUND_QUALITY_LOW,
                        RadioButton.class).select();
        }

        int planetQuality = SolarWarsSettings.getInstance().getPlanetQuality();
        switch (planetQuality) {
            case 1:
                screen.findNiftyControl(PLANET_QUALITY_HIGH,
                        RadioButton.class).select();
                break;
            case 0:
            default:
                screen.findNiftyControl(PLANET_QUALITY_LOW,
                        RadioButton.class).select();
        }
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

    public void onBackButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.MAINMENU_STATE);
    }

    
    public void onControlsButton() {
        AudioManager.getInstance().
                playSoundInstance(AudioManager.SOUND_CLICK);
        switchToState(SolarWarsGame.CONTROLS_STATE);
    }
    
    @NiftyEventSubscriber(id = "planet-quality")
    public void onPlanetQualityChanged(final String id,
            final RadioButtonGroupStateChangedEvent event) {
        if (PLANET_QUALITY_LOW.equals(event.getSelectedId())) {
            SolarWarsSettings.getInstance().setPlanetQuality(0);
        } else if (PLANET_QUALITY_HIGH.equals(event.getSelectedId())) {
            SolarWarsSettings.getInstance().setPlanetQuality(1);
        }
//        System.out.println("RadioButton [" + event.getSelectedId() + "] is now selected. The old selection was [" + event.getPreviousSelectedId() + "]");
    }

    @NiftyEventSubscriber(id = "background-quality")
    public void onBackgroundQualityChanged(final String id,
            final RadioButtonGroupStateChangedEvent event) {
        if (BACKGROUND_QUALITY_LOW.equals(event.getSelectedId())) {
            SolarWarsSettings.getInstance().setBackgroundQuality(0);
        } else if (BACKGROUND_QUALITY_MEDIUM.equals(event.getSelectedId())) {
            SolarWarsSettings.getInstance().setBackgroundQuality(1);
        } else if (BACKGROUND_QUALITY_HIGH.equals(event.getSelectedId())) {
            SolarWarsSettings.getInstance().setBackgroundQuality(2);
        }
    }

    @NiftyEventSubscriber(id = "options-nw-port-text")
    public void onPortTextChanged(final String id,
            final TextFieldChangedEvent event) {
        if (event.getText().length() > 0) {
            try {
                int port = Integer.parseInt(event.getText());
                SolarWarsSettings.getInstance().setDefaultPort(port);
            } catch (NumberFormatException formatException) {
                event.getTextFieldControl().setText(SolarWarsSettings.getInstance().getDefaultPort() + "");
                AudioManager.getInstance().
                        playSoundInstance(AudioManager.SOUND_ERROR);
                Logger.getLogger(OptionsState.class.getName()).warning("Wrong network port input!");
//                SolarWarsSettings.getInstance().setDefaultPort(6142);
            }
        }
    }

    @NiftyEventSubscriber(id = "enable-bloom")
    public void onEnableBloomChanged(final String id,
            final CheckBoxStateChangedEvent event) {
        SolarWarsSettings.getInstance().
                setBloomEnabled(event.isChecked());

    }

    @NiftyEventSubscriber(id = "enable-sound")
    public void onEnableSoundChanged(final String id,
            final CheckBoxStateChangedEvent event) {
        SolarWarsSettings.getInstance().
                setSoundEnabled(event.isChecked());

    }

    @NiftyEventSubscriber(id = "enable-music")
    public void onEnableMusicChanged(final String id,
            final CheckBoxStateChangedEvent event) {
        SolarWarsSettings.getInstance().
                setMusicEnabled(event.isChecked());

    }

    public String getDefaultPort() {
        return "" + SolarWarsSettings.getInstance().getDefaultPort();
    }
}
