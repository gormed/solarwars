/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates;

/**
 *
 * @author Hans Ferchland
 */
public abstract class Gamestate {

    private String name;

    public Gamestate(String name) {
    }

    public String getName() {
        return name;
    }

    void enter() {
        loadContent();
    }

    void leave() {
        unloadContent();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void reset() {
    }

    public void terminate() {
    }

    public abstract void update(float tpf);

    protected abstract void loadContent();

    protected abstract void unloadContent();
}
