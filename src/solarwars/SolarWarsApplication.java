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
 * File: SolarWarsApplication.java
 * Type: solarwars.SolarWarsApplication
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
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
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.NetworkManager;

/**
 * The Class SolarWarsApplication.
 */
public class SolarWarsApplication extends Application {

    public static final boolean USE_LOG_FILES = false;
    public static final Level GLOBAL_LOGGING_LEVEL = Level.ALL;
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
        return instance = new SolarWarsApplication(true);
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
    private boolean bloomEnabled = false;
    private String pingString = "";
    /** The show fps. */
    private boolean showFps = true;
    /** The action listener. */
    private AppActionListener actionListener = new AppActionListener();
    /** The post processor. */
    private FilterPostProcessor postProcessor;
    /** The bloom filter. */
    private BloomFilter bloomFilter =
            new BloomFilter(BloomFilter.GlowMode.Objects);
    /** The game. */
    private SolarWarsGame game;
    /** The lost focus. */
    private boolean lostFocus = false;
    /** value for the delay if in network */
    private float tempDelay;
    private float lastDelay = 0;
    private float currentDelay = 0;
    private float interpolator = 0;
    /** indicates that the application already is at current max delay */
    private boolean syncronized;
    private float realTimePerFrame;
    private float correctedTimePerFrame;
    private FileHandler logFileHandler;
    private String fileName;
    /** The logger for the complete client, called 'solarwars'*/
    private static final Logger clientLogger =
            Logger.getLogger(SolarWarsApplication.class.getPackage().getName() /* solarwars */);

    public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        StringTokenizer st2 = new StringTokenizer(t, ":", false);
        t = "";
        while (st2.hasMoreElements()) {
            t += st2.nextElement();
        }
        return t;
    }

    public static Logger getClientLogger() {
        return clientLogger;
    }

    public String getClientLogFileName() {
        return fileName;
    }

    /**
     * Instantiates a new solar wars application.
     */
    private SolarWarsApplication(boolean noUse) {
        super();
        try {
            if (USE_LOG_FILES) {
                fileName = new Date(System.currentTimeMillis()).toString();
                fileName = removeSpaces(fileName);
                logFileHandler = new FileHandler(fileName + ".swlog", true);
                logFileHandler.setLevel(GLOBAL_LOGGING_LEVEL);
                clientLogger.addHandler(logFileHandler);

            }
            clientLogger.setLevel(GLOBAL_LOGGING_LEVEL);
            Logger.getLogger(SolarWarsApplication.class.getName()).setLevel(GLOBAL_LOGGING_LEVEL);

        } catch (IOException ex) {
            clientLogger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            clientLogger.log(Level.SEVERE, null, ex);
        }
        assetManager = JmeSystem.newAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
    }

    public SolarWarsApplication() {
        super();
        bloomEnabled = true;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        getInstance().initSettings();
        getInstance().start();
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

    /**
     * Gets the post processor.
     *
     * @return the post processor
     */
    public FilterPostProcessor getPostProcessor() {
        return postProcessor;
    }

    /**
     * Gets the camera view port.
     *
     * @return the camera view port
     */
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
     * Initializes basic settings.
     */
    public void initSettings() {
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
            settings.put("USE_VA", false);
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#start()
     */
    @Override
    public void start() {
        //initSettings();
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
        // setup gui node
        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        // setup and show debug text
        loadFPSText();
        loadStatsView();
        // setup post effects
        setupPostEffects();
        // load up main scene
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);

        InputMappings.getInstance().initialize(inputManager);

        // add app action listener for mappings
        inputManager.addListener(actionListener,
                InputMappings.KEYBOARD_EXIT,
                InputMappings.DEBUG_CAMERA_POS,
                InputMappings.DEBUG_MEMORY,
                InputMappings.DEBUG_HIDE_STATS);

        // SETUP GAME CONTENT
        // hide stats
        setDisplayStatView(false);
        setDisplayFps(false);
        // setup lights
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(2, -10, 0).normalizeLocal());
        sun.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
        rootNode.addLight(sun);
        // load, init and start game
        game = SolarWarsGame.getInstance();
        game.initialize(this);
        game.start();
        attachLogger();
    }

    private void attachLogger() {
        Logger inputLogger = Logger.getLogger(InputManager.class.getName());
        inputLogger.setUseParentHandlers(true);
        inputLogger.setParent(clientLogger);

    }

    /**
     * Setup post effects.
     */
    private void setupPostEffects() {

        // setup post effects
        postProcessor = new FilterPostProcessor(assetManager);
        if (bloomEnabled) {
            bloomFilter.setDownSamplingFactor(2);
            bloomFilter.setBloomIntensity(0.75f);
            postProcessor.addFilter(bloomFilter);
            viewPort.addProcessor(postProcessor);
        }
    }

    /**
     * Attach iso camera control.
     */
    public void attachIsoCameraControl() {
        if (inputManager != null) {
            // Init controls
            isoControl = IsoControl.getInstance();

            isoCam = IsoCamera.getInstance();
            isoCam.initialize(cam, rootNode);
            isoCam.setMoveSpeed(5f);
            isoCam.registerWithInput(inputManager);
            lastScreenPos = new Vector2f(cam.getWidth() / 2, cam.getHeight() / 2);

            isoControl.addControlListener();

        }
        isoCam.reset();
    }

    /**
     * Detach iso camera control.
     */
    public void detachIsoCameraControl() {
        if (inputManager != null && isoCam != null) {
            isoCam.destroy();
            isoCam = null;
            isoControl.cleanUp();
            isoControl.removeControlListener();
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
        if (lostFocus == true) {
            tpf = 0;
            lostFocus = false;
        }

//        tpf += (lastDelay + currentDelay) * timer.getTimePerFrame();

        //<editor-fold defaultstate="collapsed" desc="Frames Per Second and Ping Output">
        if (showFps) {
            secondCounter += timer.getTimePerFrame();
            frameCounter++;
            if (secondCounter >= 1.0f) {
                int fps = (int) (frameCounter / secondCounter);
                float ping = ((lastDelay + tempDelay) / 2f) * 1000f;
                if (ping > 0.1f) {
                    pingString = String.format("%3.2f", ping) + "";
                }
                fpsText.setText("FPS: " + fps + " | PING: " + pingString);
                secondCounter = 0.0f;
                frameCounter = 0;
            }
        }
        //</editor-fold>

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

    /* (non-Javadoc)
     * @see com.jme3.app.Application#loseFocus()
     */
    @Override
    public void loseFocus() {
        super.loseFocus();
        lostFocus = true;
    }

    /**
     * Simple update.
     *
     * @param tpf the tpf
     */
    public void simpleUpdate(float tpf) {
        try {
            realTimePerFrame = tpf;
            resetSync();
            correctedTimePerFrame = tpf + (lastDelay + currentDelay) * timer.getTimePerFrame();
            game.update(correctedTimePerFrame);
            endSync();
            if (isoCam != null && isoControl != null) {
                isoControl.updateSelection(tpf);
                if (isoCam.isDragged()) {
                    Vector2f currentSceenPos = inputManager.getCursorPosition().clone();
                    isoCam.dragCamera(tpf, currentSceenPos);
                }
            }
        } catch (NullPointerException nullPointerException) {

            clientLogger.log(Level.FINE, nullPointerException.getMessage(), nullPointerException);
        } catch (IllegalArgumentException illegalArgumentException) {
            clientLogger.log(Level.FINE, illegalArgumentException.getMessage(), illegalArgumentException);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            clientLogger.log(Level.FINE, aioobe.getMessage(), aioobe);
        } catch (RuntimeException runtimeException) {
            clientLogger.log(Level.FINE, runtimeException.getMessage(), runtimeException);
        } catch (StackOverflowError stackOverflowError) {
            clientLogger.log(Level.FINER, stackOverflowError.getMessage(), stackOverflowError);
            logFileHandler.close();
        } catch (Exception exception) {
            clientLogger.log(Level.FINEST, exception.getMessage(), exception);
            logFileHandler.close();
        } finally {
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
            NetworkManager.getInstance().closeAllConnections(NetworkManager.WAIT_FOR_CLIENTS);
            clientLogger.log(Level.INFO, "Connections closed!");
        }
        if (USE_LOG_FILES) {
            logFileHandler.close();
        }
        super.destroy();
    }

    private void resetSync() {
        this.tempDelay = 0;
        this.syncronized = false;

    }

    public void syncronize(float delay) {
//        if (Hub.getLocalPlayer().isHost()) {
//            this.tempDelay = 0;
//            return;
//        }
        if ((this.tempDelay > delay)) {
            return;
        }
        this.tempDelay = delay;

//        System.out.println(Math.round(System.currentTimeMillis() + 0.00001) + " - Ping: " + ((lastDelay + delay) / 2f) * (int) 1000 + "ms");
        this.syncronized = true;
    }

    private void endSync() {
        this.lastDelay = currentDelay;
        this.currentDelay = tempDelay;
    }

    public boolean isSyncronized() {
        return syncronized;
    }

    public boolean isPaused() {
        return paused;
    }

    public float getCorrectedTimePerFrame() {
        return correctedTimePerFrame;
    }

    public float getRealTimePerFrame() {
        return realTimePerFrame;
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
        @Override
        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(InputMappings.KEYBOARD_EXIT)) {
                stop();
            } else if (name.equals(InputMappings.DEBUG_CAMERA_POS)) {
                if (cam != null) {
                    Vector3f loc = cam.getLocation();
                    Quaternion rot = cam.getRotation();
                    System.out.println("Camera Position: ("
                            + loc.x + ", " + loc.y + ", " + loc.z + ")");
                    System.out.println("Camera Rotation: " + rot);
                    System.out.println("Camera Direction: " + cam.getDirection());
                }
            } else if (name.equals(InputMappings.DEBUG_MEMORY)) {
                BufferUtils.printCurrentDirectMemory(null);
            } else if (name.equals(InputMappings.DEBUG_HIDE_STATS)) {
                boolean show = showFps;
                setDisplayFps(!show);
                setDisplayStatView(!show);
            }
        }
    }
}
