/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author $VPG000-AS97GL8M4U71
 */
public class GamestateManager {

    private static GamestateManager instance;
    private HashMap<String, Gamestate> gamestates;
    private Gamestate currentState;
    private Gamestate nextState;

    private GamestateManager() {
        gamestates = new HashMap<String, Gamestate>();
    }

    public static GamestateManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new GamestateManager();
    }

    public void initialize(Gamestate startState) {
        addState(startState);
        currentState = startState;
    }

    public void start() {
        currentState.enter();
    }

    public void enterState(String nextState) {
        if (gamestates.containsKey(nextState)) {
            this.nextState = gamestates.get(nextState);
            this.currentState.leave();
            this.currentState = null;
            this.nextState.enter();
            this.currentState = this.nextState;
            System.out.println("Gamestate: " + nextState + " enterd!");
        }
    }

    public void addState(Gamestate g) {
        gamestates.put(g.getName(), g);
        System.out.println("Gamestate: " + g.getName() + " added!");
    }

    public void removeState(Gamestate g) {
        gamestates.remove(g.getName());
        System.out.println("Gamestate: " + g.getName() + " removed!");
    }

    public void update(float tpf) {
        if (currentState != null) {
            currentState.update(tpf);
        }
    }

    public void pause() {
        if (currentState != null) {
            currentState.pause();
        }
    }

    public void resume() {
        if (currentState != null) {
            currentState.resume();
        }
    }

    public void reset() {
        if (currentState != null) {
            currentState.reset();
        }
    }

    public void terminate() {
        if (currentState != null) {
            currentState.terminate();
        }
    }
}
