/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import gamestates.Gamestate;
import gamestates.GamestateManager;
import solarwars.SolarWarsGame;

/**
 *
 * @author Hans
 */
public class MultiplayerMatchState extends Gamestate {

    public MultiplayerMatchState() {
        super(GamestateManager.MULTIPLAYER_MATCH_STATE);
    }

    @Override
    public void update(float tpf) {
        
    }

    @Override
    protected void loadContent(SolarWarsGame game) {
        
    }

    @Override
    protected void unloadContent() {
        
    }
}
