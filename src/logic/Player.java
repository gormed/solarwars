/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.jme3.math.ColorRGBA;
import entities.AbstractPlanet;
import entities.ShipGroup;
import java.util.ArrayList;

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
    private AI artificial;
    private AbstractPlanet selectedPlanet;
    private ShipGroup selectedShipGroup;
    private float shipPercentage = 0.5f;
    private ArrayList<AbstractPlanet> planets;
    private ArrayList<ShipGroup> shipGroups;

    public Player(String name, ColorRGBA color) {
        this.name = name;
        this.color = color;
        this.id = getContiniousID();
        planets = new ArrayList<AbstractPlanet>();
        shipGroups = new ArrayList<ShipGroup>();
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
        selectedShipGroup = null;
    }

    boolean hasSelectedPlanet() {
        return selectedPlanet != null;
    }

    AbstractPlanet getSelectedPlanet() {
        return selectedPlanet;
    }

    public void setShipPercentage(float p) {
        if (p > 1.0f) {
            p = 1.0f;
        }
        if (p < 0.0f) {
            p = 0.0f;
        }
        shipPercentage = p;
    }

    public float getShipPercentage() {
        return shipPercentage;
    }

    void selectShipGroup(ShipGroup g) {
        selectedShipGroup = g;
        selectedPlanet = null;
    }

    boolean hasSelectedShipGroup() {
        return selectedShipGroup != null;
    }

    ShipGroup getSelectedShipGroup() {
        return selectedShipGroup;
    }

    public boolean isAI() {
        return artificial != null;
    }

    AI getAI() {
        return artificial;
    }

    void setAI(AI ai) {
        artificial = ai;
    }

    void createShipGroup(ShipGroup sg) {
        shipGroups.add(sg);
    }

    void destroyShipGroup(ShipGroup sg) {
        shipGroups.remove(sg);
    }

    void capturePlanet(AbstractPlanet planet) {
        if (planet.hasOwner()) {
            planet.getOwner().uncapturePlanet(planet);
        }
        planet.setOwner(this);
        planets.add(planet);
    }

    void uncapturePlanet(AbstractPlanet planet) {
        planets.remove(planet);
    }

    ArrayList<AbstractPlanet> getPlanets() {
        return planets;
    }

    ArrayList<ShipGroup> getShipGroups() {
        return shipGroups;
    }
}
