/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import java.util.HashMap;

/**
 *
 * @author Hans
 */
public class FontLoader {
    private static FontLoader instance;
    
    public static FontLoader getInstance() {
        if (instance != null)
            return instance;
        return instance = new FontLoader();
    }    
    
    private FontLoader() {
        this.fonts = new HashMap<String, BitmapFont>();
    }
    
    private AssetManager assetManager = null;
    
    private final String[] fontNames = {
        "SolarWars32"
    };
    
    private HashMap<String, BitmapFont> fonts;

    
    public void initialize(AssetManager assetManager) {
        this.assetManager = assetManager;
        loadFonts();
    }
    
    private void loadFonts() {
        for(String s : fontNames) {
            loadFont(s);
        }
    }
    
    private void loadFont(String fontName) {
        
        BitmapFont fnt = assetManager.loadFont(
                "Interface/Fonts/" + fontName + ".fnt");
        fonts.put(fontName, fnt);
        
    }
    
    public BitmapFont getFont(String fontName) {
        return fonts.get(fontName);
    }
    
}
