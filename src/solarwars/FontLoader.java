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
 * File: FontLoader.java
 * Type: solarwars.FontLoader
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import java.util.HashMap;

/**
 * The Class FontLoader.
 */
public class FontLoader {
    
    /** The instance. */
    private static FontLoader instance;
    
    /**
     * Gets the single instance of FontLoader.
     *
     * @return single instance of FontLoader
     */
    public static FontLoader getInstance() {
        if (instance != null)
            return instance;
        return instance = new FontLoader();
    }    
    
    /**
     * Instantiates a new font loader.
     */
    private FontLoader() {
        this.fonts = new HashMap<String, BitmapFont>();
    }
    
    /** The asset manager. */
    private AssetManager assetManager = null;
    
    /** The font names. */
    private final String[] fontNames = {
        "SolarWarsFont32",
            "SolarWarsFont64"
    };
    
    /** The fonts. */
    private HashMap<String, BitmapFont> fonts;

    
    /**
     * Initializes the.
     *
     * @param assetManager the asset manager
     */
    public void initialize(AssetManager assetManager) {
        this.assetManager = assetManager;
        loadFonts();
    }
    
    /**
     * Load fonts.
     */
    private void loadFonts() {
        for(String s : fontNames) {
            loadFont(s);
        }
    }
    
    /**
     * Load font.
     *
     * @param fontName the font name
     */
    private void loadFont(String fontName) {
        
        BitmapFont fnt = assetManager.loadFont(
                "Interface/Fonts/" + fontName + ".fnt");
        fonts.put(fontName, fnt);
        
    }
    
    /**
     * Gets the font.
     *
     * @param fontName the font name
     * @return the font
     */
    public BitmapFont getFont(String fontName) {
        return fonts.get(fontName);
    }
    
}
