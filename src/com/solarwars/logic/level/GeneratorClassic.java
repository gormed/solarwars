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
 * File: GeneratorClassic.java
 * Type: com.solarwars.logic.level.GeneratorClassic
 * 
 * Documentation created: 10.09.2012 - 21:01:07 by Hans Ferchland <hans.ferchland at gmx.de>
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.logic.level;

import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.solarwars.SolarWarsApplication;
import com.solarwars.entities.LevelBackground;
import com.solarwars.logic.Level;
import java.util.Random;

/**
 * The class GeneratorClassic.
 * @author Hans Ferchland <hans.ferchland at gmx.de>
 * @version
 * @deprecated
 */
@Deprecated
public class GeneratorClassic extends LevelGenerator {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /* linksunten, linksoben, rechtsoben, rechtsunten */
    /** The corners. */
    private Vector3f[] corners = {Vector3f.ZERO, Vector3f.ZERO, Vector3f.ZERO, Vector3f.ZERO};

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public GeneratorClassic(Level level) {
        super(level);
        getCorners();
    }

    @Override
    public boolean generate(long seed) {
        generateClassic(seed);
        return levelLoaded;
    }

    /**
     * Generate classic.
     *
     * @param seed the seed
     */
    public void generateClassic(long seed) {
        System.out.print("[" + seed + "] Generating level...");
        levelLoaded = false;
        background =
                new LevelBackground(com.solarwars.SolarWarsGame.getInstance(), (int) seed);
        level.getRootNode().attachChild(background);

        randomizer = new Random(seed);
        int leftBottomX = Math.round(corners[0].x);
        int leftBottomZ = Math.round(corners[0].z);
        int topRightX = Math.round(corners[2].x);
        int topRightZ = Math.round(corners[2].z);
        for (float x = leftBottomX - PLANET_SPACE_HORIZ; x >= topRightX + PLANET_SPACE_HORIZ; x--) {
            for (float z = topRightZ - PLANET_SPACE_VERT; z >= leftBottomZ + PLANET_SPACE_VERT; z--) {
                if (randomizer.nextFloat() > 0.65F) {
                    createPlanet(x, z);
                    System.out.print(".");
                }
            }
        }
        if (level.getControl() != null) {
            level.getControl().addShootable(level.getLevelNode());
        }
        setupPlayers(level.getPlayersByID());
        levelLoaded = true;
        System.out.println("Level generated!");
    }

    /**
     * Gets the corners.
     *
     * @return the corners
     */
    private void getCorners() {
//                    final Vector2f leftBottom = new Vector2f(0, 0);
//                    final Vector2f leftTop = new Vector2f(0, gui.getHeight());
//                    final Vector2f rightTop = new Vector2f(gui.getWidth(), gui.getHeight());
//                    final Vector2f rightBottom = new Vector2f(gui.getWidth(), 0);
//                    corners[0] = getWorldCoordsOnXZPlane(leftBottom, 0);
//                    corners[1] = getWorldCoordsOnXZPlane(leftTop, 0);
//                    corners[2] = getWorldCoordsOnXZPlane(rightTop, 0);
//                    corners[3] = getWorldCoordsOnXZPlane(rightBottom, 0);
        corners[0] = Vector3f.UNIT_XYZ.clone();
        corners[1] = Vector3f.UNIT_XYZ.clone();
        corners[2] = Vector3f.UNIT_XYZ.clone();
        corners[3] = Vector3f.UNIT_XYZ.clone();
    }

    /**
     * Gets the world coords on xz plane.
     *
     * @param screenCoords the screen coords
     * @param planeHeight the plane height
     * @return the world coords on xz plane
     */
    private Vector3f getWorldCoordsOnXZPlane(Vector2f screenCoords, float planeHeight) {
        Camera cam = SolarWarsApplication.getInstance().getCamera();
        Vector2f click2d = screenCoords;
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0.0F).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1.0F).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        float t = (planeHeight - ray.getOrigin().y) / ray.getDirection().y;
        Vector3f XZPlanePos = ray.getDirection().clone().mult(t).addLocal(ray.getOrigin().clone());
        return XZPlanePos;
    }

    @Override
    public void dispose() {
        level.getRootNode().detachChild(background);

    }
}
