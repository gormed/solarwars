/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SolarWars Project (c) 2012 - 2012 
 * 
 *      by gormed, fxdapokalypse, kinxz, Londane, romanh, Senju
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
 * Type: com.solarwars.SolarWarsApplication
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferUtils;
import com.solarwars.controls.ControlManager;
import com.solarwars.controls.input.InputMappings;
import com.solarwars.log.Logging;
import com.solarwars.net.NetworkManager;
import com.solarwars.settings.SolarWarsSettings;
import de.lessvoid.nifty.Nifty;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The Class SolarWarsApplication.
 */
public class SolarWarsApplication extends Application {
    //==========================================================================
    //      MAIN METHOD
    //==========================================================================

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        getInstance().start();
    }
    //==========================================================================
    //      Static Fields
    //==========================================================================
    public static boolean TOON_ENABLED = SolarWarsSettings.getInstance().isToonEnabled();
    /**
     * Flag for Bloom-Effect
     */
    public static boolean BLOOM_ENABLED = SolarWarsSettings.getInstance().isBloomEnabled();
    public static boolean NIFTY_LOGGING = false;
    public static boolean NIFTY_USE_COLORED_PANELS = false;
    /**
     * The logger for the complete client, called 'com.solarwars'
     */
    private static final Logger clientLogger =
            Logger.getLogger(
            SolarWarsApplication.class.getPackage().getName() /* com.solarwars */);

    //==========================================================================
    //      Static Methods
    //==========================================================================
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

    //==========================================================================
    //      Singleton
    //==========================================================================
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

    /**
     * Instantiates a new solar wars application.
     *
     * @param thisHasNoUse Only here because we need a empty public constructor
     * for Android.
     */
    private SolarWarsApplication(boolean thisHasNoUse) {
        super();
        Logging.init();
        initSettings();
        assetManager = JmeSystem.newAssetManager(
                Thread.currentThread().getContextClassLoader().
                getResource("com/jme3/asset/Desktop.cfg"));
    }

    /**
     * Public constructor for android harness or so, Do not use!
     */
    public SolarWarsApplication() {
        super();
    }
    /**
     * The instance.
     */
    private static SolarWarsApplication instance;
    //==========================================================================
    //      Protected & Private Fields
    //==========================================================================
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");
    protected float secondCounter = 0.0f;
    protected int frameCounter = 0;
    protected Nifty niftyGUI;
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    protected StatsView statsView;
    protected IsoCamera isoCam;
    protected Vector2f lastScreenPos;
    protected ControlManager controlManager;
    protected boolean showSettings = true;
    private String pingString = "";
    private boolean showFps = true;
    private AppActionListener actionListener = new AppActionListener();
    private FilterPostProcessor postProcessor;
    private BloomFilter bloomFilter =
            new BloomFilter(BloomFilter.GlowMode.Objects);
    private SolarWarsGame game;
    private NiftyJmeDisplay niftyJmeDisplay;
    private boolean lostFocus = false;
    /**
     * value for the delay if in network
     */
    private float tempDelay;
    private float lastDelay = 0;
    private float currentDelay = 0;
    private float interpolator = 0;
    /**
     * indicates that the application already is at current max delay
     */
    private boolean syncronized;
    private float realTimePerFrame;
    private float correctedTimePerFrame;
    private FileHandler logFileHandler;
    //==========================================================================
    //      Methods
    //==========================================================================

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
    public ControlManager getControlManager() {
        return controlManager;
    }

    public Nifty getNiftyGUI() {
        return niftyGUI;
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
        statsView = new StatsView("Statistics View",
                assetManager, renderer.getStatistics());
        // move it up so it appears above fps text
        statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        guiNode.attachChild(statsView);
    }

    /**
     * Initializes basic settings.
     */
    private void initSettings() {
        if (settings == null) {
            settings = SolarWarsSettings.getInstance().toAppSettings();
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#start()
     */
    @Override
    public void start() {
        try {
            //initSettings();
            // set some default settings in-case
            // settings dialog is not shown
            boolean loadSettings = false;
            if (settings == null) {
                setSettings(SolarWarsSettings.getInstance().toAppSettings());
                loadSettings = true;
            }

            // show settings dialog
            if (showSettings) {
                if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
                    return;
                }
            }
            BufferedImage buff16 = ImageIO.read(getClass().
                    getResourceAsStream("/Interface/icon16.png"));
            BufferedImage buff32 = ImageIO.read(getClass().
                    getResourceAsStream("/Interface/icon32.png"));
            settings.setIcons(new BufferedImage[]{buff16, buff32});
            settings.setUseJoysticks(true);
            //re-setting settings they can have been merged from the registry.
            setSettings(settings);
            super.start(JmeContext.Type.Display);
        } catch (IOException ex) {
            Logger.getLogger(SolarWarsApplication.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
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
        // init input mappings
        InputMappings.getInstance().initialize(inputManager);
        // add app action listener for mappings
        inputManager.addListener(actionListener,
                InputMappings.EXIT_GAME,
                InputMappings.DEBUG_CAMERA_POS,
                InputMappings.DEBUG_MEMORY,
                InputMappings.DEBUG_HIDE_STATS,
                InputMappings.DEBUG_NIFTY_GUI);
        //setup nifty
        setupNiftyGUI();

        // SETUP GAME CONTENT
        // hide stats
        setDisplayStatView(false);
        setDisplayFps(false);
        // setup the lighting
        setupLights();
        // setup control
        controlManager = ControlManager.getInstance();
        controlManager.initialize(inputManager, rootNode);
//        control = new StandardControl();
//        control.initialize();

        // load, init and start game
        game = SolarWarsGame.getInstance();
        game.initialize(this);
        game.start();

// 		  TODO: remove Attach the logger of input to the client logger.
//        Logger inputLogger = Logger.getLogger(InputManager.class.getName());
//        inputLogger.setUseParentHandlers(true);
//        inputLogger.setParent(clientLogger);
    }

    private void setupNiftyGUI() {
        niftyJmeDisplay = new NiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                viewPort);
        niftyGUI = niftyJmeDisplay.getNifty();
        guiViewPort.addProcessor(niftyJmeDisplay);
        niftyGUI.setDebugOptionPanelColors(NIFTY_USE_COLORED_PANELS);
        if (!NIFTY_LOGGING) {
            Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
            Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
        }
    }

    /**
     * Setup post effects.
     */
    private void setupPostEffects() {

        // setup post effects
        postProcessor = new FilterPostProcessor(assetManager);
        if (BLOOM_ENABLED) {
            bloomFilter.setDownSamplingFactor(2);
            bloomFilter.setBloomIntensity(0.75f);
            postProcessor.addFilter(bloomFilter);
            viewPort.addProcessor(postProcessor);
        }
    }

    /**
     * Attach iso camera control.
     */
    public void attachCamera() {
        if (inputManager != null) {
            // Init controls
            isoCam = IsoCamera.getInstance();
            isoCam.initialize(cam, rootNode);
            isoCam.setMoveSpeed(5f);
            isoCam.registerWithInput(inputManager);
            lastScreenPos = new Vector2f(cam.getWidth() / 2, cam.getHeight() / 2);

            //addControlListener();

        }
        isoCam.reset();
    }
    
    public void attachControls() {
        ControlManager.getInstance().attachControlListeners();
    }

    /**
     * Detach iso camera control.
     */
    public void detachCamera() {
        if (inputManager != null && isoCam != null) {
            isoCam.destroy();
            isoCam = null;
//            controlManager.cleanUp();
            controlManager.detachControlListeners();
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
        // Network delay fixing attempt
        realTimePerFrame = tpf;
        resetSync();
        correctedTimePerFrame =
                tpf + (lastDelay + currentDelay) * timer.getTimePerFrame();

        tpf = correctedTimePerFrame;

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

        // indicate that everything is done and current time can be recoreded
        // for next step
        endSync();

    }

    /**
     * Simple update.
     *
     * @param tpf the tpf
     */
    public void simpleUpdate(float tpf) {
        try {
            if (isoCam != null && controlManager != null && controlManager.isInitialized()) {
                controlManager.update(tpf);
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
        //if (USE_LOG_FILES) {
        //    logFileHandler.close();
        //}
        super.destroy();
    }

    /**
     * Reset the delay counter for network delay.
     */
    private void resetSync() {
        this.tempDelay = 0;
        this.syncronized = false;

    }

    /**
     * Syncs the client to the server with the current delay.
     *
     * @param delay the delay between server and this client.
     */
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

    /**
     * Finishes the syncronisation.
     */
    private void endSync() {
        this.lastDelay = currentDelay;
        this.currentDelay = tempDelay;
    }

    /**
     * Checks if the client is already synced in this frame. Will be reset on
     * frame-end.
     *
     * @return
     */
    public boolean isSyncronized() {
        return syncronized;
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
     * Checks if paused.
     *
     * @return
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Gets the corrected time, according to the delay to the server.
     * correctedTimePerFrame = tpf + (lastDelay + currentDelay) *
     * timer.getTimePerFrame
     *
     * @return
     */
    public float getCorrectedTimePerFrame() {
        return correctedTimePerFrame;
    }

    /**
     * The time on this client needed for the frame.
     *
     * @return
     */
    public float getRealTimePerFrame() {
        return realTimePerFrame;
    }

    private void setupLights() {
        // setup lights
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(2, -10, 0).normalizeLocal());
        sun.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
        rootNode.addLight(sun);
    }

    /**
     * The listener interface for receiving appAction events. The class that is
     * interested in processing a appAction event implements this interface, and
     * the object created with that class is registered with a component using
     * the component's
     * <code>addAppActionListener<code> method. When
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

            if (name.equals(InputMappings.EXIT_GAME)) {
//                stop();
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
            } else if (name.equals(InputMappings.DEBUG_NIFTY_GUI)) {
                if (guiViewPort.getProcessors().contains(niftyJmeDisplay)) {
                    guiViewPort.removeProcessor(niftyJmeDisplay);
                } else {
                    guiViewPort.addProcessor(niftyJmeDisplay);
                }
            }
        }
    }
}