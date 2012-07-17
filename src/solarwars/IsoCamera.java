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
 * File: IsoCamera.java
 * Type: solarwars.IsoCamera
 * 
 * Documentation created: 14.07.2012 - 19:37:57 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package solarwars;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import logic.Level;

/**
 * The Class IsoCamera.
 */
public class IsoCamera implements AnalogListener, ActionListener {

    /** The instance. */
    private static IsoCamera instance;

    /**
     * Gets the single instance of IsoCamera.
     *
     * @return single instance of IsoCamera
     */
    public static IsoCamera getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IsoCamera();
    }
    /** The Constant CAMERA_HEIGHT. */
    public static float CAMERA_HEIGHT = 12;
    /** The Constant CAMERA_ANGLE. */
    public static final float CAMERA_ANGLE = (float) Math.PI / 2;//8f * (((float) Math.PI) / 18f) ;
    /** The cam. */
    protected Camera cam;
    /** The cam light. */
    protected PointLight camLight;
    /** The root node. */
    protected Node rootNode;
    /** The initial up vec. */
    protected Vector3f initialUpVec;
    /** The drag speed. */
    protected float dragSpeed = 3f;
    /** The move speed. */
    protected float moveSpeed = 3f;
    /** The motion allowed. */
    protected MotionAllowedListener motionAllowed = null;
    /** The enabled. */
    protected boolean enabled = true;
    /** The last click state. */
    protected boolean lastClickState;
    /** The current click state. */
    protected boolean currentClickState;
    /** The is dragged. */
    protected boolean isDragged;
    /** The initial drag pos. */
    protected Vector2f initialDragPos;
    /** The input manager. */
    protected InputManager inputManager;

    /**
     * Instantiates a new iso camera.
     */
    private IsoCamera() {
        super();
    }

    /**
     * Initializes the.
     *
     * @param cam the cam
     * @param rootNode the root node
     */
    public void initialize(Camera cam, Node rootNode) {
        this.rootNode = rootNode;
        float[] rot = {CAMERA_ANGLE, 0, 0};
        this.cam = cam;
        initialUpVec = cam.getUp().clone();
        CAMERA_HEIGHT = Level.getLevelSize(Hub.playersByID.size());
        cam.setLocation(new Vector3f(0, CAMERA_HEIGHT, 0));
        cam.setRotation(new Quaternion(rot));

        camLight = new PointLight();
        camLight.setPosition(cam.getLocation());
        camLight.setColor(ColorRGBA.White);
        rootNode.addLight(camLight);


    }

    /**
     * Reset.
     */
    public void reset() {
        float[] rot = {CAMERA_ANGLE, 0, 0};
        CAMERA_HEIGHT = Level.getLevelSize(Hub.playersByID.size());
        cam.setLocation(new Vector3f(0, CAMERA_HEIGHT, 0));
        cam.setRotation(new Quaternion(rot));
    }

    /**
     * Destroy.
     */
    public void destroy() {
        rootNode.removeLight(camLight);
        this.rootNode = null;
    }

    /**
     * Gets the cam.
     *
     * @return the cam
     */
    public Camera getCam() {
        return cam;
    }

    /**
     * Sets the up vector.
     *
     * @param upVec the new up vector
     */
    public void setUpVector(Vector3f upVec) {
        initialUpVec.set(upVec);
    }

    /**
     * Sets the motion allowed listener.
     *
     * @param listener the new motion allowed listener
     */
    public void setMotionAllowedListener(MotionAllowedListener listener) {
        this.motionAllowed = listener;
    }

    /**
     * Sets the move speed.
     *
     * @param moveSpeed the new move speed
     */
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * Sets the rotation speed.
     *
     * @param rotationSpeed the new rotation speed
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.dragSpeed = rotationSpeed;
    }

    /**
     * Sets the enabled.
     *
     * @param enable the new enabled
     */
    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Checks if is dragged.
     *
     * @return true, if is dragged
     */
    public boolean isDragged() {
        return isDragged;
    }

    /**
     * Gets the initial drag position.
     *
     * @return the initial drag position
     */
    public Vector2f getInitialDragPosition() {
        return initialDragPos;
    }

    /**
     * Register with input.
     *
     * @param inputManager the input manager
     */
    public void registerWithInput(InputManager inputManager) {
        this.inputManager = inputManager;

        String[] mappings = new String[]{
            "ISOCAM_Left",
            "ISOCAM_Right",
            "ISOCAM_Up",
            "ISOCAM_Down",
            "ISOCAM_Forward",
            "ISOCAM_Backward",
            "ISOCAM_DragMove"
        };

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("ISOCAM_Down", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("ISOCAM_Up", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("ISOCAM_DragMove", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("ISOCAM_Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("ISOCAM_Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("ISOCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("ISOCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        //inputManager.addMapping("ISOCAM_Up", new KeyTrigger(KeyInput.KEY_Q));
        //inputManager.addMapping("ISOCAM_Down", new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, mappings);
        //inputManager.setCursorVisible(dragToMove);

        /*Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0) {
        Joystick joystick = joysticks[0];
        joystick.assignAxis("FLYCAM_StrafeRight", "FLYCAM_StrafeLeft", JoyInput.AXIS_POV_X);
        joystick.assignAxis("FLYCAM_Forward", "FLYCAM_Backward", JoyInput.AXIS_POV_Y);
        joystick.assignAxis("FLYCAM_Right", "FLYCAM_Left", joystick.getXAxisIndex());
        joystick.assignAxis("FLYCAM_Down", "FLYCAM_Up", joystick.getYAxisIndex());
        }*/
    }

    /**
     * Rotate camera.
     *
     * @param value the value
     * @param axis the axis
     */
    protected void rotateCamera(float value, Vector3f axis) {
        /*if (dragToMove) {
        if (canDragMove) {
        //                value = -value;
        } else {
        return;
        }
        }
        
        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(dragSpeed * value, axis);
        
        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();
        
        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);
        
        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalize();
        
        cam.setAxes(q);*/
    }

    /**
     * Zoom camera.
     *
     * @param value the value
     */
    protected void zoomCamera(float value) {
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                / (FastMath.DEG_TO_RAD * .5f);
        fovY += value * 0.1f;

        h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    /**
     * Rise camera.
     *
     * @param value the value
     */
    protected void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null) {
            motionAllowed.checkMotionAllowed(pos, vel);
        } else {
            pos.addLocal(vel);
        }

        cam.setLocation(pos);
        camLight.setPosition(cam.getLocation());
    }

    /**
     * Move camera.
     *
     * @param value the value
     * @param sideways the sideways
     */
    protected void moveCamera(float value, boolean sideways) {
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways) {
            cam.getLeft(vel);
        } else {
            vel = new Vector3f(0, 0, 1);
            //cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null) {
            motionAllowed.checkMotionAllowed(pos, vel);
        } else {
            pos.addLocal(vel);
        }

        cam.setLocation(pos);
        camLight.setPosition(cam.getLocation());
    }

    /**
     * Drag camera.
     *
     * @param value the value
     * @param to the to
     */
    void dragCamera(float value, Vector2f to) {
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        Vector2f dir = to.subtract(initialDragPos);

        dir.normalizeLocal();

        vel = new Vector3f(dir.x, 0, -dir.y);
        vel.multLocal(value * dragSpeed);

        if (motionAllowed != null) {
            motionAllowed.checkMotionAllowed(pos, vel);
        } else {
            pos.addLocal(vel);
        }

        cam.setLocation(pos);
        camLight.setPosition(cam.getLocation());
    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.AnalogListener#onAnalog(java.lang.String, float, float)
     */
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (!enabled) {
            return;
        }

        if (name.equals("ISOCAM_Forward")) {
            moveCamera(value, false);
        } else if (name.equals("ISOCAM_Backward")) {
            moveCamera(-value, false);
        } else if (name.equals("ISOCAM_Left")) {
            moveCamera(value, true);
        } else if (name.equals("ISOCAM_Right")) {
            moveCamera(-value, true);
        } else if (name.equals("ISOCAM_Up")) {
            riseCamera(value);
        } else if (name.equals("ISOCAM_Down")) {
            riseCamera(-value);
        } /*else if (name.equals("FLYCAM_ZoomIn")) {
        zoomCamera(value);
        } else if (name.equals("FLYCAM_ZoomOut")) {
        zoomCamera(-value);
        }*/
    }

    /* (non-Javadoc)
     * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
     */
    @Override
    public void onAction(String name, boolean value, float tpf) {
        if (!enabled) {
            return;
        }
        currentClickState = value;
        if (name.equals("ISOCAM_DragMove")) {
            isDragged = value;
            if (isDragged) {
                initialDragPos = inputManager.getCursorPosition().clone();
            }
            //!currentClickState && lastClickState;
//            Vector2f currentSceenPos = inputManager.getCursorPosition();
//            Vector2f dir = lastScreenPos.subtract(currentSceenPos);
//            dir.normalizeLocal();
//            
//            dragCamera(tpf, dir);
//            //inputManager.setCursorVisible(!value);
//            
//            lastScreenPos = currentSceenPos;
        }
        lastClickState = currentClickState;
    }
}
