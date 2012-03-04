/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Hans
 */
public abstract class GeneralAction {
    private final String name;

    public String getName() {
        return name;
    }
    
    public GeneralAction(String name) {
        this.name = name;
    }
    
    public abstract void doAction(Player p);
}
