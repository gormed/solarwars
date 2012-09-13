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
 * File: GameStatsModule.java
 * Type: com.solarwars.gamestates.gui.GameStatsModule
 * 
 * Documentation created: 08.09.2012 - 17:54:19 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.solarwars.SolarWarsApplication;
import com.solarwars.input.InputMappings;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import java.util.ArrayList;

/**
 * The class GameStatsModule.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class GameStatsModule implements ActionListener {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private float refreshCounter = 0;
    private final Element statsLayer;
    private ListBox<PlayerStatsItem> playerStateBox;
    private Level level;
    private InputManager inputManager =
            SolarWarsApplication.getInstance().getInputManager();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public GameStatsModule(Element statsLayer, ListBox<PlayerStatsItem> playerStateBox, Level level) {
        this.statsLayer = statsLayer;
        this.playerStateBox = playerStateBox;
        this.level = level;
        initialize();
        this.statsLayer.hide();
        this.playerStateBox.clear();
    }

    private void initialize() {
        this.inputManager.addListener(this, InputMappings.GAME_SCORES);
    }

    public void update(float tpf) {
        refreshCounter += tpf;
        if (refreshCounter > 1f) {
            for (PlayerStatsItem item : playerStateBox.getItems()) {
                item.update(tpf);
//            updatePlayer(item, item.getPlayer());
            }
            playerStateBox.refresh();
            refreshCounter = 0;
        }
    }

    private void addPlayer(Player player) {
        playerStateBox.addItem(new PlayerStatsItem(player));
    }

    public void addPlayers(ArrayList<Player> players) {
        for (Player p : players) {
            addPlayer(p);
        }
    }

    public void destroy() {
        if (inputManager != null) {
            inputManager.removeListener(this);
        }
    }


    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(InputMappings.GAME_SCORES)) {
            if (isPressed) {
                statsLayer.show();
            } else {
                statsLayer.hide();
            }
        }
    }
}
