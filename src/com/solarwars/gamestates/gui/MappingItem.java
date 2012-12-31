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
 * Project: Solarwars Project
 * File: MappingItem.java
 * Type: com.solarwars.gamestates.gui.MappingItem
 * 
 * Documentation created: 31.12.2012 - 13:50:55 by Hans Ferchland <hans{dot}ferchland{at}gmx{dot}de>
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
