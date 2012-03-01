package solarwars;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
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
    public static final String INPUT_MAPPING_CLICK = "SOLARWARS_Click";
    
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");
    protected float secondCounter = 0.0f;
    protected int frameCounter = 0;
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    protected StatsView statsView;
    protected IsoCamera isoCam;
    protected IsoControl isoControl;
    protected boolean showSettings = true;
    private boolean showFps = true;
    private AppActionListener actionListener = new AppActionListener();
    private FilterPostProcessor postProcessor;

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

    /**
     * Toggles settings window to display at start-up
     * @param showSettings Sets true/false
     *
     */
    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    public void setupFilters() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            postProcessor = new FilterPostProcessor(assetManager);
            //fpp.setNumSamples(4);
            CartoonEdgeFilter toon = new CartoonEdgeFilter();
            toon.setEdgeColor(ColorRGBA.Yellow);
            postProcessor.addFilter(toon);
            viewPort.addProcessor(postProcessor);
        }
    }

    /**
     * Attaches FPS statistics to guiNode and displays it on the screen.
     *
     */
    public void loadFPSText() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        fpsText = new BitmapText(guiFont, false);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        guiNode.attachChild(fpsText);
    }

    public void setDisplayFps(boolean show) {
        showFps = show;
        fpsText.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    public void setDisplayStatView(boolean show) {
        statsView.setEnabled(show);
        statsView.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /**
     * Attaches Statistics View to guiNode and displays it on the screen
     * above FPS statistics line.
     *
     */
    public void loadStatsView() {
        statsView = new StatsView("Statistics View", assetManager, renderer.getStatistics());
        // move it up so it appears above fps text
        statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        guiNode.attachChild(statsView);
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

        isoControl = new IsoControl(assetManager, rootNode, cam, inputManager);
        
        if (inputManager != null) {
            isoCam = new IsoCamera(cam, rootNode);
            isoCam.setMoveSpeed(5f);
            isoCam.registerWithInput(inputManager);

            // Map C, M, F5 and ESC
            if (context.getType() == Type.Display) {
                inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            }
            
            inputManager.addMapping(INPUT_MAPPING_CAMERA_POS, new KeyTrigger(KeyInput.KEY_C));
            inputManager.addMapping(INPUT_MAPPING_MEMORY, new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F5));
            inputManager.addListener(actionListener, INPUT_MAPPING_EXIT,
                    INPUT_MAPPING_CAMERA_POS, INPUT_MAPPING_MEMORY, INPUT_MAPPING_HIDE_STATS);

            // Map left-button click
            inputManager.addMapping(INPUT_MAPPING_CLICK,
                    new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); 
            inputManager.addListener(isoControl.getActionListener(), INPUT_MAPPING_CLICK);

        }

        //isoCam.setEnabled(true);

        Level l = new Level(rootNode, assetManager, isoControl);
        l.generateLevel(System.currentTimeMillis());

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -10, 0).normalizeLocal());
        sun.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
        rootNode.addLight(sun);
    }

    @Override
    public void update() {
        super.update(); // makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;

        if (showFps) {
            secondCounter += timer.getTimePerFrame();
            frameCounter++;
            if (secondCounter >= 1.0f) {
                int fps = (int) (frameCounter / secondCounter);
                fpsText.setText("Frames per second: " + fps);
                secondCounter = 0.0f;
                frameCounter = 0;
            }
        }

        // update states
        stateManager.update(tpf);

        // simple update and root node
        simpleUpdate(tpf);
        rootNode.updateLogicalState(tpf);
        guiNode.updateLogicalState(tpf);
        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        // render states
        stateManager.render(renderManager);
        renderManager.render(tpf, context.isRenderable());
        simpleRender(renderManager);
        stateManager.postRender();
    }

    public void simpleUpdate(float tpf) {
    }

    public void simpleRender(RenderManager rm) {
    }
}
