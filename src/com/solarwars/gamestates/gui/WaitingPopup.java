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
 * File: WaitingPopup.java
 * Type: com.solarwars.gamestates.gui.WaitingPopup
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;

/**
 * The class StartGamePopup.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class WaitingPopup {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Nifty niftyGUI;
    private Element waitingPopup;
    private boolean visible = false;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public WaitingPopup(Nifty niftyGUI) {
        this.niftyGUI = niftyGUI;
    }

    public void showPopup() {
        if (visible) {
            return;
        }
        if (waitingPopup == null) {
            waitingPopup = niftyGUI.createPopup("waiting_popup");
        }
        niftyGUI.showPopup(niftyGUI.getCurrentScreen(), waitingPopup.getId(), null);
        visible = true;
    }

    public void hidePopup() {
        if (waitingPopup != null && visible) {
            niftyGUI.closePopup(waitingPopup.getId());
            waitingPopup = null;
            visible = false;
        }
    }
}
