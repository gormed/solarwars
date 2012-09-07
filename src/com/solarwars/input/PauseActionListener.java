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
 * File: PauseListener.java
 * Type: com.solarwars.input.PauseListener
 * 
 * Documentation created: 06.09.2012 - 00:08:05 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;

/**
 * The listener interface for receiving pauseAction 
 * events (ESC, P and Pause/Untbr).
 * The class that is interested in processing a pauseAction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * SolarWarsApplication component's 
 * <code>getInputManager().addListener()<code> method. When
 * the pauseAction event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ActionListener
 * @see InputListener
 * @see InputManager
 * 
 * @author Hans Ferchland
 * @version
 */
public class PauseActionListener implements ActionListener {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private boolean paused = false;
    private boolean fadeing = false;
    private Element pausePopup;
    private Nifty niftyGUI;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public PauseActionListener(Nifty niftyGUI) {
        this.niftyGUI = niftyGUI;
    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (!isPressed && name.equals(InputMappings.PAUSE_GAME)) {
            onPause();
        }
    }

    protected void onPause() {
        triggerPause();
    }

    public void triggerPause() {
        triggerPausePopup(!paused);
    }

    private void triggerPausePopup(boolean paused) {
        if (paused) {
            showPopup();
        } else {
            hidePopup();
        }
    }

    public void showPopup() {
        if (paused || fadeing) {
            return;
        }
        if (pausePopup == null) {
            pausePopup = niftyGUI.createPopup("pause_popup");
        }
        niftyGUI.showPopup(niftyGUI.getCurrentScreen(),
                pausePopup.getId(), null);
        fadeing = true;
        paused = true;
        showElement(pausePopup, new EndNotify() {

            @Override
            public void perform() {
                fadeing = false;
            }
        });
    }

    public void hidePopup() {
        if (pausePopup != null && paused && !fadeing) {
            fadeing = true;
            hideElement(
                    pausePopup,
                    new EndNotify() {

                        @Override
                        public void perform() {
                            niftyGUI.closePopup(pausePopup.getId());
                            paused = false;
                            fadeing = false;
                        }
                    });

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
