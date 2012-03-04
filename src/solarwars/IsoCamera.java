/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *  
 * @author Hans
 */
public class IsoCamera implements AnalogListener, ActionListener {

    private static IsoCamera instance;
    
    public static IsoCamera getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IsoCamera();
    }
    
    public static final float CAMERA_HEIGHT = 8;
    public static final float CAMERA_ANGLE = (float)Math.PI/2;//8f * (((float) Math.PI) / 18f) ;
    
    protected Camera cam;
    protected PointLight camLight;
    protected Node rootNode;
    protected Vector3f initialUpVec;
    protected float rotationSpeed = 1f;
    protected float moveSpeed = 3f;
    protected MotionAllowedListener motionAllowed = null;
    protected boolean enabled = true;
    protected boolean dragToRotate = false;
    protected boolean canRotate = false;
    protected InputManager inputManager;

    /**
     * Creates a new FlyByCamera to control the given Camera object.
     * @param cam
     */
    private IsoCamera() {
        super();
    }
    
    public void initialize(Camera cam, Node rootNode) {
        this.rootNode = rootNode;
        float[] rot = {CAMERA_ANGLE, 0, 0};
        this.cam = cam;
        initialUpVec = cam.getUp().clone();
        cam.setLocation(new Vector3f(0, CAMERA_HEIGHT, 0));
        cam.setRotation(new Quaternion(rot));
        
        camLight = new PointLight();
        camLight.setPosition(cam.getLocation());
        camLight.setColor(ColorRGBA.White);
        rootNode.addLight(camLight);
    }
    
    public Camera getCam() {
        return cam;
    }

    /**
     * Sets the up vector that should be used for the camera.
     * @param upVec
     */
    public void setUpVector(Vector3f upVec) {
        initialUpVec.set(upVec);
    }

    public void setMotionAllowedListener(MotionAllowedListener listener) {
        this.motionAllowed = listener;
    }

    /**
     * Sets the move speed. The speed is given in world units per second.
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * Sets the rotation speed.
     * @param rotationSpeed
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * @param enable If false, the camera will ignore input.
     */
    public void setEnabled(boolean enable) {
        if (enabled && !enable) {
            if (!dragToRotate || (dragToRotate && canRotate)) {
                inputManager.setCursorVisible(true);
            }
        }
        enabled = enable;
    }

    /**
     * @return If enabled
     * @see FlyByCamera#setEnabled(boolean)
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return If drag to rotate feature is enabled.
     *
     * @see FlyByCamera#setDragToRotate(boolean) 
     */
    public boolean isDragToRotate() {
        return dragToRotate;
    }

    /**
     * Set if drag to rotate mode is enabled.
     * 
     * When true, the user must hold the mouse button
     * and drag over the screen to rotate the camera, and the cursor is
     * visible until dragged. Otherwise, the cursor is invisible at all times
     * and holding the mouse button is not needed to rotate the camera.
     * This feature is disabled by default.
     * 
     * @param dragToRotate True if drag to rotate mode is enabled.
     */
    public void setDragToRotate(boolean dragToRotate) {
        this.dragToRotate = dragToRotate;
        inputManager.setCursorVisible(dragToRotate);
    }

    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager) {
        this.inputManager = inputManager;

        String[] mappings = new String[]{
            "ISOCAM_Left",
            "ISOCAM_Right",
            "ISOCAM_Up",
            "ISOCAM_Down",
            "ISOCAM_Forward",
            "ISOCAM_Backward"
        };

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("ISOCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("ISOCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        //inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("ISOCAM_Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("ISOCAM_Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("ISOCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("ISOCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("ISOCAM_Up", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("ISOCAM_Down", new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, mappings);
        //inputManager.setCursorVisible(dragToRotate);

        /*Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0) {
            Joystick joystick = joysticks[0];
            joystick.assignAxis("FLYCAM_StrafeRight", "FLYCAM_StrafeLeft", JoyInput.AXIS_POV_X);
            joystick.assignAxis("FLYCAM_Forward", "FLYCAM_Backward", JoyInput.AXIS_POV_Y);
            joystick.assignAxis("FLYCAM_Right", "FLYCAM_Left", joystick.getXAxisIndex());
            joystick.assignAxis("FLYCAM_Down", "FLYCAM_Up", joystick.getYAxisIndex());
        }*/
    }

    protected void rotateCamera(float value, Vector3f axis) {
        /*if (dragToRotate) {
            if (canRotate) {
//                value = -value;
            } else {
                return;
            }
        }

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

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

    protected void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value / moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null) {
            motionAllowed.checkMotionAllowed(pos, vel);
        } else {
            pos.addLocal(vel);
        }

        cam.setLocation(pos);
        camLight.setPosition(cam.getLocation());
    }

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

    public void onAction(String name, boolean value, float tpf) {
        if (!enabled) {
            return;
        }

        /*if (name.equals("FLYCAM_RotateDrag") && dragToRotate) {
            canRotate = value;
            inputManager.setCursorVisible(!value);
        }*/
    }
}
