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
 * File: SimpleShip.java
 * Type: com.solarwars.entities.SimpleShip
 * 
 * Documentation created: 14.07.2012 - 19:38:01 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.solarwars.logic.Level;
import com.solarwars.logic.Player;

/**
 * The Class SimpleShip.
 */
public class SimpleShip extends AbstractShip {

    /**
     * Instantiates a new simple ship.
     *
     * @param assetManager the asset manager
     * @param level the level
     * @param position the position
     * @param p the p
     * @param g the g
     */
    public SimpleShip(AssetManager assetManager, Level level, Vector3f position, Player p, ShipGroup g) {
        super(assetManager, level, position, p, g);

    }

    /* (non-Javadoc)
     * @see com.solarwars.entities.AbstractShip#createShip()
     */
    @Override
    public void createShip() {
        shipBatchSpatial = level.getBatchManager().getShipBatch(owner);
        shipBatchSpatial.setLocalTranslation(position);
    }
}
