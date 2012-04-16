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
 * Email me: hans.ferchland@gmx.de
 * 
 * Project: SolarWars
 * File: SolarWarsApplication.java
 * Type: solarwars.SolarWarsApplication
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferUtils;
import net.NetworkManager;

/**
 * The Class SolarWarsApplication.
 */
public class SolarWarsApplication extends Application {

    /** The Constant INPUT_MAPPING_EXIT. */
    public static final String INPUT_MAPPING_EXIT = "SOLARWARS_Exit";
    /** The Constant INPUT_MAPPING_PAUSE. */
    public static final String INPUT_MAPPING_PAUSE = "SOLARWARS_Pause";
    /** The Constant INPUT_MAPPING_CAMERA_POS. */
    public static final String INPUT_MAPPING_CAMERA_POS = "SOLARWARS_CameraPos";
    /** The Constant INPUT_MAPPING_MEMORY. */
    public static final String INPUT_MAPPING_MEMORY = "SOLARWARS_Memory";
    /** The Constant INPUT_MAPPING_HIDE_STATS. */
    public static final String INPUT_MAPPING_HIDE_STATS = "SOLARWARS_HideStats";
    /** The Constant INPUT_MAPPING_LEFT_CLICK. */
    public static final String INPUT_MAPPING_LEFT_CLICK = "SOLARWARS_LeftClick";
    /** The Constant INPUT_MAPPING_RIGHT_CLICK. */
    public static final String INPUT_MAPPING_RIGHT_CLICK = "SOLARWARS_RightClick";
    /** The Constant INPUT_MAPPING_WHEEL_UP. */
    public static final String INPUT_MAPPING_WHEEL_UP = "SOLARWARS_WheelUp";
    /** The Constant INPUT_MAPPING_WHEEL_DOWN. */
    public static final String INPUT_MAPPING_WHEEL_DOWN = "SOLARWARS_WheelDown";
    /** The instance. */
    private static SolarWarsApplication instance;
    //private static AppSettings settings;

    /**
     * Gets the single instance of SolarWarsApplication.
     *
     * @return single instance of SolarWarsApplication
     */
    public static SolarWarsApplication getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new SolarWarsApplication();
    }
    /** The root node. */
    protected Node rootNode = new Node("Root Node");
    /** The gui node. */
    protected Node guiNode = new Node("Gui Node");
    /** The second counter. */
    protected float secondCounter = 0.0f;
    /** The frame counter. */
    protected int frameCounter = 0;
    /** The fps text. */
    protected BitmapText fpsText;
    /** The gui font. */
    protected BitmapFont guiFont;
    /** The stats view. */
    protected StatsView statsView;
    /** The iso cam. */
    protected IsoCamera isoCam;
    /** The last screen pos. */
    protected Vector2f lastScreenPos;
    /** The iso control. */
    protected IsoControl isoControl;
    /** The show settings. */
    protected boolean showSettings = true;
    /** The show fps. */
    private boolean showFps = true;
    /** The action listener. */
    private AppActionListener actionListener = new AppActionListener();
    /** The post processor. */
    private FilterPostProcessor postProcessor;
    private BloomFilter bloomFilter =
            new BloomFilter(BloomFilter.GlowMode.Objects);
    /** The game. */
    private SolarWarsGame game;

    /**
     * Instantiates a new solar wars application.
     */
    private SolarWarsApplication() {
        super();
    }

    /**
     * Gets the iso cam.
     *
     * @return the iso cam
     */
    public IsoCamera getIsoCam() {
        return isoCam;
    }

    /**
     * Gets the gui node.
     *
     * @return the gui node
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * Gets the root node.
     *
     * @return the root node
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Gets the iso control.
     *
     * @return the iso control
     */
    public IsoControl getIsoControl() {
        return isoControl;
    }

    public FilterPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public ViewPort getCameraViewPort() {
        return viewPort;
    }

    /**
     * Checks if is show settings.
     *
     * @return true, if is show settings
     */
    public boolean isShowSettings() {
        return showSettings;
    }

    /**
     * Sets the show settings.
     *
     * @param showSettings the new show settings
     */
    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    /**
     * Setup filters.
     */
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
     * Load fps text.
     */
    public void loadFPSText() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        fpsText = new BitmapText(guiFont, false);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        guiNode.attachChild(fpsText);
    }

    /**
     * Sets the display fps.
     *
     * @param show the new display fps
     */
    public void setDisplayFps(boolean show) {
        showFps = show;
        fpsText.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /**
     * Sets the display stat view.
     *
     * @param show the new display stat view
     */
    public void setDisplayStatView(boolean show) {
        statsView.setEnabled(show);
        statsView.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /**
     * Load stats view.
     */
    public void loadStatsView() {
        statsView = new StatsView("Statistics View", assetManager, renderer.getStatistics());
        // move it up so it appears above fps text
        statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        guiNode.attachChild(statsView);
    }

    /**
     * The listener interface for receiving appAction events.
     * The class that is interested in processing a appAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addAppActionListener<code> method. When
     * the appAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see AppActionEvent
     */
    private class AppActionListener implements ActionListener {

        /* (non-Javadoc)
         * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
         */
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

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        getInstance().start();
    }

    private void initSettings() {
        if (settings == null) {
            settings = new AppSettings(false);

//            settings.setBitsPerPixel(24);
//            settings.setWidth(1024);
//
//            settings.setHeight(768);
            settings.put("Width", 1024);
            settings.put("Height", 768);
            settings.put("BitsPerPixel", 24);
            settings.put("Frequency", 60);
            settings.put("DepthBits", 24);
            settings.put("StencilBits", 0);
            settings.put("Samples", 4);
            settings.put("Fullscreen", false);
            settings.put("Title", "SolarWars_");
            settings.put("Renderer", AppSettings.LWJGL_OPENGL2);
            settings.put("AudioRenderer", AppSettings.LWJGL_OPENAL);
            settings.put("DisableJoysticks", true);
            settings.put("UseInput", true);
            settings.put("VSync", false);
            settings.put("FrameRate", 100);
            settings.put("SettingsDialogImage", "/Interface/solarwars_v2.png");
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#start()
     */
    @Override
    public void start() {
        initSettings();
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
        super.start(JmeContext.Type.Display);
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();

        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        loadFPSText();
        loadStatsView();


        postProcessor = new FilterPostProcessor(assetManager);
        bloomFilter.setDownSamplingFactor(2);
        bloomFilter.setBloomIntensity(0.75f);
        postProcessor.addFilter(bloomFilter);
        viewPort.addProcessor(postProcessor);


        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);


        // Map interface clicking for ingame and GUI and Debugging
        inputManager.addMapping(INPUT_MAPPING_LEFT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(INPUT_MAPPING_RIGHT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping(INPUT_MAPPING_WHEEL_DOWN,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(INPUT_MAPPING_WHEEL_UP,
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(INPUT_MAPPING_CAMERA_POS, new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping(INPUT_MAPPING_MEMORY, new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addListener(actionListener, INPUT_MAPPING_EXIT,
                INPUT_MAPPING_CAMERA_POS, INPUT_MAPPING_MEMORY, INPUT_MAPPING_HIDE_STATS);

        // SETUP GAME CONTENT
        
        setDisplayStatView(false);
        setDisplayFps(false);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(2, -10, 0).normalizeLocal());
        sun.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
        rootNode.addLight(sun);

        game = SolarWarsGame.getInstance();
        game.initialize(this);
        game.start();
    }

    public void attachIsoCameraControl() {
        if (inputManager != null) {
            // Init controls
            isoControl = IsoControl.getInstance();

            isoCam = IsoCamera.getInstance();
            isoCam.initialize(cam, rootNode);
            isoCam.setMoveSpeed(5f);
            isoCam.registerWithInput(inputManager);
            lastScreenPos = new Vector2f(cam.getWidth() / 2, cam.getHeight() / 2);

            inputManager.addListener(isoControl.getActionListener(),
                    INPUT_MAPPING_LEFT_CLICK, INPUT_MAPPING_RIGHT_CLICK,
                    INPUT_MAPPING_WHEEL_DOWN, INPUT_MAPPING_WHEEL_UP);

        }
    }

    public void detachIsoCameraControl() {
        if (inputManager != null && isoCam != null) {
            isoCam.destroy();
            isoCam = null;
            isoControl.cleanUp();
            inputManager.removeListener(isoControl.getActionListener());
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#update()
     */
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

    /**
     * Simple update.
     *
     * @param tpf the tpf
     */
    public void simpleUpdate(float tpf) {

        game.update(tpf);
        if (isoCam != null && isoCam.isDragged()) {
            Vector2f currentSceenPos = inputManager.getCursorPosition().clone();
            isoCam.dragCamera(tpf, currentSceenPos);
        }
    }

    /**
     * Simple render.
     *
     * @param rm the rm
     */
    public void simpleRender(RenderManager rm) {
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#destroy()
     */
    @Override
    public void destroy() {
        NetworkManager nm = NetworkManager.getInstance();
        if (nm != null && nm.isServerRunning()) {
            NetworkManager.getInstance().closeAllConnections(false);
        }
        super.destroy();
    }
}
