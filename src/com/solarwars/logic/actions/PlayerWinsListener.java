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
 * File: PlayerWinsListener.java
 * Type: com.solarwars.logic.actions.PlayerWinsListener
 * 
 * Documentation created: 08.10.2012 - 23:47:11 by Hans Ferchland <hans.ferchland at gmx dot de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.solarwars.logic.actions;

import com.solarwars.logic.Player;

/**
 * The class PlayerWinsListener.
 * @author Hans Ferchland <hans.ferchland at gmx dot de>
 * @version
 */
public interface PlayerWinsListener {
    public void onPlayerWins(Player player, float tpf);
}