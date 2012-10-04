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
 * File: GameOverModule.java
 * Type: com.solarwars.gamestates.gui.GameOverModule
 * 
 * Documentation created: 11.09.2012 - 14:11:43 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;

/**
 * The class GameOverModule.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class GameOverModule {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final Nifty niftyGUI;
    private Element gameOverPopup;
    private boolean watchGame = false;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public GameOverModule(Nifty niftyGUI) {
        this.niftyGUI = niftyGUI;
        watchGame = false;
    }

    public void setWatchGame(boolean value) {
        this.watchGame = value;
    }

    public boolean isWatchGame() {
        return watchGame;
    }

    public void showPopup() {
        if ((gameOverPopup != null && gameOverPopup.isVisible()) || isWatchGame()) {
            return;
        }
        if (gameOverPopup == null) {
            gameOverPopup = niftyGUI.createPopup("gameover_popup");
        }

        niftyGUI.showPopup(niftyGUI.getCurrentScreen(),
                gameOverPopup.getId(), null);
        showElement(gameOverPopup, new EndNotify() {

            @Override
            public void perform() {
            }
        });
    }

    public void hidePopup(EndNotify endNotify) {
        if (gameOverPopup != null) {
            EndNotify closeNotify = new EndNotify() {

                        @Override
                        public void perform() {
                            niftyGUI.closePopup(gameOverPopup.getId());
                        }
                    };
            hideElement(gameOverPopup, closeNotify, endNotify);
        }
    }
    
    public void hidePopup() {
        if (gameOverPopup != null) {
            EndNotify closeNotify = new EndNotify() {

                        @Override
                        public void perform() {
                            niftyGUI.closePopup(gameOverPopup.getId());
                        }
                    };
            hideElement(gameOverPopup, closeNotify);
        }
    }
    
    public boolean isVisible() {
        return (gameOverPopup != null && gameOverPopup.isVisible());
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
