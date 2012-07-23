/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: TexturedPanel.java
 * Type: gui.elements.TexturedPanel
 * 
 * Documentation created: 21.07.2012 - 16:03:02 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import solarwars.SolarWarsApplication;

/**
 * The class TexturedPanel.
 * @author Hans Ferchland
 * @version
 */
public class TexturedPanel extends Panel {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Texture texture;
    private String path;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public TexturedPanel(String name, Vector3f pos, Vector2f size, String path) {
        super(name, pos, size, ColorRGBA.White.clone());
        
        AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
        this.texture = assetManager.loadTexture(path);
        this.path = path;
        material.setTexture("ColorMap", texture);
    }

    public void changeTexture(String path) {
        if (this.path.equals(path))
            return;
        this.path = path;
        AssetManager assetManager = SolarWarsApplication.getInstance().getAssetManager();
        this.texture = assetManager.loadTexture(path);
        material.setTexture("ColorMap", texture);
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
