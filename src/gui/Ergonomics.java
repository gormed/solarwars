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
 * File: Ergonomics.java
 * Type: gui.Ergonomics
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

/**
 * The Class Ergonomics.
 *
 * @author Hans
 */
public final class Ergonomics {
    
    /** The instance. */
    private static Ergonomics instance;
    
    /**
     * Instantiates a new ergonomics.
     */
    private Ergonomics() {
        
    }
    
    /**
     * Gets the single instance of Ergonomics.
     *
     * @return single instance of Ergonomics
     */
    public static Ergonomics getInstance() {
        if (instance != null)
            return instance;
        return instance = new Ergonomics();
    }
    
    /** The ip address. */
    private String ipAddress = "127.0.0.1";
    
    /** The name. */
    private String name = ">Player";
    
    /** The players. */
    private int players = 8;
    
    /** The seed. */
    private String seed = "42";

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddress the new ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the players.
     *
     * @return the players
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Sets the players.
     *
     * @param players the new players
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * Gets the seed.
     *
     * @return the seed
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Sets the seed.
     *
     * @param seed the new seed
     */
    public void setSeed(String seed) {
        this.seed = seed;
    }
    
}
