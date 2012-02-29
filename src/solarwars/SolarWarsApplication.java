package solarwars;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferUtils;
import level.Level;

/**
 * SolarWars SolarWarsApplication Class
 * 
 * @author Hans Ferchland
 */
public class SolarWarsApplication extends Application {

    public static final String INPUT_MAPPING_EXIT = "SOLARWARS_Exit";
    public static final String INPUT_MAPPING_CAMERA_POS = "SOLARWARS_CameraPos";
    public static final String INPUT_MAPPING_MEMORY = "SOLARWARS_Memory";
    public static final String INPUT_MAPPING_HIDE_STATS = "SOLARWARS_HideStats";
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");
    protected float secondCounter = 0.0f;
    protected int frameCounter = 0;
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    protected StatsView statsView;
    protected IsoCamera isoCam;
    protected boolean showSettings = true;
    private boolean showFps = true;
    private AppActionListener actionListener = new AppActionListener();
    
    
    public IsoCamera getIsoCam() {
        return isoCam;
    }

    /**
     * Retrieves guiNode
     * @return guiNode Node object
     *
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * Retrieves rootNode
     * @return rootNode Node object
     *
     */
    public Node getRootNode() {
        return rootNode;
    }

    public boolean isShowSettings() {
        return showSettings;
    }

    private class AppActionListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(INPUT_MAPPING_EXIT)) {
                stop();
            } else if (name.equals(INPUT_MAPPING_CAMERA_POS)) {
                if (cam != null) {
                    Vector3f loc = cam.getLocation();
                    Quaternion rot = cam.getRotation();
                    System.out.println("Camera Position: ("
                            + loc.x + ", " + loc.y + ", " + loc.z + ")");
                    System.out.println("Camera Rotation: " + rot);
                    System.out.println("Camera Direction: " + cam.getDirection());
                }
            } else if (name.equals(INPUT_MAPPING_MEMORY)) {
                BufferUtils.printCurrentDirectMemory(null);
            } else if (name.equals(INPUT_MAPPING_HIDE_STATS)) {
                boolean show = showFps;
                setDisplayFps(!show);
                setDisplayStatView(!show);
            }
        }
    }

    public static void main(String[] args) {
        SolarWarsApplication app = new SolarWarsApplication();
        app.start();
    }

    @Override
    public void start() {
        // set some default settings in-case
        // settings dialog is not shown
        boolean loadSettings = false;
        if (settings == null) {
            setSettings(new AppSettings(true));
            loadSettings = true;
        }

        // show settings dialog
        if (showSettings) {
            if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
                return;
            }
        }
        //re-setting settings they can have been merged from the registry.
        setSettings(settings);
        super.start();
    }

    @Override
    public void initialize() {
        super.initialize();

        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        loadFPSText();
        loadStatsView();
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);

        if (inputManager != null) {
            isoCam = new FlyByCamera(cam);
            isoCam.setMoveSpeed(1f);
            isoCam.registerWithInput(inputManager);

            if (context.getType() == Type.Display) {
                inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            }

            inputManager.addMapping(INPUT_MAPPING_CAMERA_POS, new KeyTrigger(KeyInput.KEY_C));
            inputManager.addMapping(INPUT_MAPPING_MEMORY, new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F5));
            inputManager.addListener(actionListener, INPUT_MAPPING_EXIT,
                    INPUT_MAPPING_CAMERA_POS, INPUT_MAPPING_MEMORY, INPUT_MAPPING_HIDE_STATS);

        }

        this.se
        
        Level l = new Level(rootNode, assetManager);
        l.generateLevel(System.currentTimeMillis());

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -10, 0).normalizeLocal());
        sun.setColor(ColorRGBA.White);

        rootNode.addLight(sun);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
