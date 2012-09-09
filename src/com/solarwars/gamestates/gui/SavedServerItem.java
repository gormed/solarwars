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
 * Documentation created: 08.09.2012 - 13:46:31 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

/**
 * The class Class.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 */
public class SavedServerItem {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final String name;
    private final String ip;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public SavedServerItem(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }
}
