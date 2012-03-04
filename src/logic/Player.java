/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author Hans
 */
public class Player {
    private static int PLAYER_ID = 0;

    private static int getContiniousID() {
        return PLAYER_ID++;
    }
    
    private String name;

    public ColorRGBA getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    private ColorRGBA color;
    private int id;
    private int shipCount;
    
    public Player(String name, ColorRGBA color) {
        this.name = name;
        this.color = color;
        this.id = getContiniousID();
    }
    
    public void updatePlayer() {
        
    }
}
