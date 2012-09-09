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

import com.jme3.math.ColorRGBA;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;
import de.lessvoid.nifty.controls.ListBox;

/**
 * The class GameStatsModule.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class GameStatsModule {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private ListBox<PlayerStatsItem> playerStateBox;
    private Level level;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public GameStatsModule(ListBox<PlayerStatsItem> playerStateBox, Level level) {
        this.playerStateBox = playerStateBox;
        this.level = level;
    }

    public void update(float tpf) {
        for (PlayerStatsItem item : playerStateBox.getItems()) {
            item.update(tpf);
//            updatePlayer(item, item.getPlayer());
        }
        playerStateBox.refresh();
    }
    
    public void addPlayer(Player player) {
        playerStateBox.addItem(new PlayerStatsItem(player));
    }
}
