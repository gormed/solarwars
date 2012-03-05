/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.ColorRGBA;
import entities.AbstractPlanet;
import entities.AbstractShip;
import entities.ShipGroup;

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
    private ColorRGBA color;
    private int id;
    private int shipCount = 0;
    
    private AbstractPlanet selectedPlanet;
    private AbstractShip selectedShip;
    private ShipGroup selectedShipGroup;
    
    private int selectedShips = 0;
    
    private float shipPercentage = 0.5f;

    public Player(String name, ColorRGBA color) {
        this.name = name;
        this.color = color;
        this.id = getContiniousID();
    }

    public ColorRGBA getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updatePlayer() {
    }

    void selectPlanet(AbstractPlanet p) {
        selectedPlanet = p;
        selectedShip = null;
        selectedShipGroup = null;
    }
    
    boolean hasSelectedPlanet() {
        return selectedPlanet != null;
    }
    
    AbstractPlanet getSelectedPlanet() {
        return selectedPlanet;
    }
    
    public void setShipPercentage(float p) {
        if (p > 1.0f)
            p = 1.0f;
        if (p < 0.0f)
            p = 0.0f;
        shipPercentage = p;
    }
    
    public float getShipPercentage() {
        return shipPercentage;
    }

    void selectShip(AbstractShip s) {
        selectedShip = s;
        selectedPlanet = null;
        selectedShipGroup = null;
    }

    void selectShipGroup(ShipGroup g) {
        selectedShipGroup = g;
        selectedPlanet = null;
        selectedShip = null;
    }
}
