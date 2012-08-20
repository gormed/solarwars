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
 * File: Gamestate.java
 * Type: gamestates.Gamestate
 * 
 * Documentation created: 14.07.2012 - 19:37:57 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates;

import solarwars.SolarWarsGame;

/**
 * The Class Gamestate.
 */
public abstract class Gamestate {

    /** The name. */
    private String name;
    protected SolarWarsGame game;

    /**
     * Instantiates a new gamestate.
     *
     * @param name the name
     */
    public Gamestate(String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Enter.
     */
    void enter() {
        game = SolarWarsGame.getInstance();
        loadContent(game);
    }

    /**
     * Leave.
     */
    void leave() {
        unloadContent();
    }

    /**
     * Pause.
     */
    public void pause() {
    }

    /**
     * Resume.
     */
    public void resume() {
    }

    /**
     * Reset.
     */
    public void reset() {
    }

    /**
     * Terminate.
     */
    public void terminate() {
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public abstract void update(float tpf);

    /**
     * Load content.
     *
     * @param game the game
     */
    protected abstract void loadContent(SolarWarsGame game);

    /**
     * Unload content.
     */
    protected abstract void unloadContent();
}
