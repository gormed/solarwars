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
 * File: StartGamePopup.java
 * Type: com.solarwars.gamestates.gui.StartGamePopup
 * 
 * Documentation created: 05.01.2013 - 22:12:55 by Hans Ferchland
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
public class StartGamePopup {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Nifty niftyGUI;
    private Element startGamePopup;
    private boolean visible = false;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public StartGamePopup(Nifty niftyGUI) {
        this.niftyGUI = niftyGUI;
    }

    public void showPopup() {
        if (visible) {
            return;
        }
        if (startGamePopup == null) {
            startGamePopup = niftyGUI.createPopup("start_game_popup");
        }
        niftyGUI.showPopup(niftyGUI.getCurrentScreen(), startGamePopup.getId(), null);
        visible = true;
    }

    public void hidePopup() {
        if (startGamePopup != null && visible) {
            niftyGUI.closePopup(startGamePopup.getId());
            startGamePopup = null;
            visible = false;
        }
    }
}
