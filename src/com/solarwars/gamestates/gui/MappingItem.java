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
 * File: MappingItem.java
 * Type: com.solarwars.gamestates.gui.MappingItem
 * 
 * Documentation created: 05.01.2013 - 22:12:53 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gamestates.gui;

/**
 * The class MappingItem.
 *
 * @author Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
 */
public class MappingItem {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private String mappingName;
    private String mappingKey;
    
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public MappingItem(String mappingName, String mappingKey) {
        this.mappingName = mappingName;
        this.mappingKey = mappingKey;
    }

    public String getMappingKey() {
        return mappingKey;
    }

    public String getMappingName() {
        return mappingName;
    }
}
