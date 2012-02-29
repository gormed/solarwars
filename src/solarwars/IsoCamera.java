/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author Hans
 */
public class IsoCamera extends Camera {

    public static final float CAMERA_HEIGHT = 8;
    
    public IsoCamera(int width, int height) {
        super(width, height);
        this.setLocation(new Vector3f(0, CAMERA_HEIGHT, 0));
    }
    
}
