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
 * File: Class.java
 * Type: com.solarwars.gamestates.lib.Class
 * 
 * Documentation created: 08.09.2012 - 15:10:26 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;

/**
 * The class AddServerPopup.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class AddServerPopup {
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Nifty niftyGUI;
    private Element addServerPopup;
    private boolean visible = false;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public AddServerPopup(Nifty niftyGUI) {
        this.niftyGUI = niftyGUI;
    }

    public String getNewServerIP() {
        TextField ip = niftyGUI.getCurrentScreen().findNiftyControl("ip_address", TextField.class);
        return ip.getText();
    }

    public String getNewServerName() {
        TextField name = niftyGUI.getCurrentScreen().findNiftyControl("server_name", TextField.class);
        return name.getText();
    }

    public void showPopup() {
        if (visible) {
            return;
        }
        if (addServerPopup == null) {
            addServerPopup = niftyGUI.createPopup("add_server_popup");
        }
        niftyGUI.showPopup(niftyGUI.getCurrentScreen(), addServerPopup.getId(), null);
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
        element.startEffect(EffectEventId.onCustom, new EndNotify() {

            @Override
            public void perform() {
                for (EndNotify ed : endNotify) {
                    ed.perform();
                }
            }
        }, "in");
    }

    public void hideElement(final Element element, final EndNotify... endNotify) {
        element.startEffect(EffectEventId.onCustom, new EndNotify() {

            @Override
            public void perform() {
                element.hideWithoutEffect();
                for (EndNotify ed : endNotify) {
                    ed.perform();
                }
            }
        }, "out");
    }
}
