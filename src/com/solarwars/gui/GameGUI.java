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
 * File: GameGUI.java
 * Type: com.solarwars.gui.GameGUI
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars.gui;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.solarwars.SolarWarsApplication;
import com.solarwars.SolarWarsGame;
import com.solarwars.gui.elements.TextBox;
import com.solarwars.input.InputMappings;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class GameGUI.
 */
public class GameGUI {

    private static GameGUI currentGUI = null;

    private void applyGUI() {
        currentGUI = this;
    }

    public static GameGUI getInstance() {
        if (currentGUI != null) {
            return currentGUI;
        }
        return currentGUI = new GameGUI(SolarWarsGame.getInstance());
    }

    /**
     * Instantiates a new game gui.
     *
     * @param game the game
     */
    private GameGUI(SolarWarsGame game) {
        logger.setLevel(SolarWarsApplication.GLOBAL_LOGGING_LEVEL);
        this.game = game;
        this.width = game.getApplication().getCamera().getWidth();
        this.height = game.getApplication().getCamera().getHeight();
        this.guiElements = new ArrayList<GUIElement>();

        this.decoNode = new Node("DecoNode");
        inputManager = game.getApplication().getInputManager();

        inputManager = game.getApplication().getInputManager();
        final Camera cam = game.getApplication().getCamera();
        final Node guiNode = game.getApplication().getGuiNode();

        guiNode.attachChild(decoNode);
        guiNode.attachChild(clickableNode);

        this.clickActionListener = new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(InputMappings.LEFT_CLICK_SELECT)) {
                    onClick(isPressed, tpf, inputManager.getCursorPosition());
                } else {
                }
            }
            //}
        };

        this.keyActionListener = new ActionListener() {

            private boolean ctrlHold = false;
            private boolean vHold = false;

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(InputMappings.CONTROL_MODIFIER)) {
                    ctrlHold = isPressed;
                } else if (name.equals(InputMappings.KEY_COPY_V)) {
                    vHold = isPressed;
                }
                if (ctrlHold && vHold) {
                    if (focus != null && focus instanceof TextBox) {
                        TextBox t = (TextBox) focus;
                        t.setCaption(getClipboardContents());
                    }
                }
            }

            /**
             * Get the String residing on the clipboard.
             *
             * @return any text found on the Clipboard; if none found, return an
             * empty String.
             */
            public String getClipboardContents() {
                String result = "";
                Clipboard clipboard =
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                //odd: the Object param of getContents is not currently used
                Transferable contents = clipboard.getContents(null);
                boolean hasTransferableText =
                        (contents != null)
                        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                if (hasTransferableText) {
                    try {
                        result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException ex) {
                        //highly unlikely since we are using a standard DataFlavor
                        Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                return result;
            }
        };

        this.touchListener = new TouchListener() {

            @Override
            public void onTouch(String name, TouchEvent event, float tpf) {
                float x;
                float y;
                float pressure;
                if (event.getType() == TouchEvent.Type.DOWN) {
                    x = event.getX();
                    y = event.getY();
                    onClick(true, tpf, new Vector2f(x, y));

//                    pressure = event.getPressure();
                } else if (event.getType() == TouchEvent.Type.UP) {
                    x = event.getX();
                    y = event.getY();
                    onClick(false, tpf, new Vector2f(x, y));
//                    onButtonDown(name);
                } else if (event.getType() == TouchEvent.Type.LONGPRESSED) {
                }
//                Log.e("", "Event Type " + event.getType());
                event.setConsumed();
            }
        };

        if (inputManager
                != null) {
            inputManager.addListener(clickActionListener,
                    InputMappings.LEFT_CLICK_SELECT);
            inputManager.addListener(keyActionListener,
                    InputMappings.CONTROL_MODIFIER, InputMappings.KEY_COPY_V);
        }

        applyGUI();
    }
    /** The game. */
    SolarWarsGame game;
    /** The gui elemetns. */
    private ArrayList<GUIElement> guiElements;
    private InputManager inputManager;
    /** The width. */
    private float width;
    /** The action listener. */
    private ActionListener clickActionListener;
    private ActionListener keyActionListener;
    private TouchListener touchListener;
    /** The focus. */
    private GUIElement focus;
    /** The height. */
    private float height;
    /** The clickable node. */
    private final Node clickableNode = new Node("ClickableGUI");
    /** The deco node. */
    private Node decoNode;
    private static final Logger logger = Logger.getLogger(GameGUI.class.getName());

    /**
     * Gets the height.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the focus element.
     *
     * @return the focus element
     */
    public GUIElement getFocusElement() {
        return focus;
    }

    private void onClick(boolean isPressed, float tpf, Vector2f point) {
        //if (isPressed) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. calculate ray from camera to mouse pointer
        Vector2f click2d = point;
        Vector3f click3d =
                new Vector3f(click2d.x, click2d.y, 0f);
        Vector3f dir = new Vector3f(click2d.x, click2d.y, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        // 3. Collect intersections between Ray and Shootables in
        // results list.
        try {
            synchronized (clickableNode) {
                clickableNode.collideWith(ray, results);
            }

            debugRaycasting(results);

            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                for (CollisionResult current : results) {
                    Node n = current.getGeometry().getParent();
                    checkClickableHit(n, click2d, isPressed, tpf);
                    logGUIHit(isPressed, n);
                }
            }
        } catch (IndexOutOfBoundsException exception) {
            Logger.getLogger(GameGUI.class.getName()).
                    log(Level.SEVERE, exception.getMessage(), exception);
        }
    }

    private void logGUIHit(boolean isPressed, Node n) {
        if (isPressed) {
            final String clickMsg = "Clicked at GUI-Element " + n.getName();
            logger.info(clickMsg);
        } else {
            final String releaseMsg = "Released at GUI-Element " + n.getName();
            logger.info(releaseMsg);
        }
    }

    private void checkClickableHit(Node n, Vector2f click2d, boolean isPressed, float tpf) {

        if (n instanceof GUIElement) {
            GUIElement element = (GUIElement) n;

            if (element.isVisible() && n instanceof ClickableGUI) {
                ClickableGUI g = (ClickableGUI) n;
                if (g.canGainFocus()) {
                    setFocus(g);

                }
                g.onClick(click2d, isPressed, tpf);
            }
        }
    }

    private void debugRaycasting(CollisionResults results) {
        if (results.size() <= 0) {
            logger.log(Level.FINE, "Nothing was hit in GUI (2D) raycasting!", results);
            return;
        }
        logger.log(Level.FINE, "There were " + results.size() + " hits! If logging FINER see below:", results);
        String hits = "";
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of
            // geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();
            hits += "* Collision #" + i;
            hits += " - You shot " + hit + " at " + pt
                    + ", " + dist + " wu away.\n";
        }
        logger.log(Level.FINER, hits, results);
    }

    /**
     * Adds the gui element.
     *
     * @param guiElement the gui element
     */
    public void addGUIElement(GUIElement guiElement) {
        saveAddGUIElement(guiElement);
        //addGUIElements.add(guiElement);
    }

    /**
     * Removes the gui element.
     *
     * @param guiElement the gui element
     */
    public void removeGUIElement(GUIElement guiElement) {
        saveRemoveGUIElement(guiElement);
        //removeGUIElements.add(guiElement);

    }

    /**
     * Sets the focus.
     *
     * @param g the new focus
     */
    public void setFocus(ClickableGUI g) {
        if (g instanceof GUIElement) {
            focus = (GUIElement) g;
        } else if (g == null) {
            focus = null;
        }
    }

    /**
     * Updates the gui elements.
     *
     * @param tpf the tpf
     */
    public void updateGUIElements(float tpf) {
//        for (GUIElement element : addGUIElements) {
//            saveAddGUIElement(element);
//        }
//        addGUIElements.clear();

        ArrayList<GUIElement> clone = new ArrayList<GUIElement>(guiElements);

        for (GUIElement e : clone) {
            e.updateGUI(tpf);
        }

//        for (GUIElement element : removeGUIElements) {
//            saveRemoveGUIElement(element);
//        }
//        removeGUIElements.clear();
    }

    /**
     * Adds the gui element.
     *
     * @param guiElement the gui element
     */
    private void saveAddGUIElement(GUIElement guiElement) {
        if (guiElements.contains(guiElement)) {
            return;
        }
        if (guiElement instanceof ClickableGUI) {
            clickableNode.attachChild(guiElement);
        } else {
            decoNode.attachChild(guiElement);
        }
        guiElements.add(guiElement);
    }

    /**
     * Removes the gui element.
     *
     * @param guiElement the gui element
     */
    private void saveRemoveGUIElement(GUIElement guiElement) {
        if (guiElement instanceof ClickableGUI) {
            clickableNode.detachChild(guiElement);
        } else {
            decoNode.detachChild(guiElement);
        }
        guiElements.remove(guiElement);

    }

    /**
     * Contains gui element.
     *
     * @param e the e
     * @return true, if successful
     */
    public boolean containsGUIElement(GUIElement e) {
        return guiElements.contains(e);
    }

    /**
     * Clean up gui.
     */
    public void cleanUpGUI() {

        ArrayList<GUIElement> clickable = new ArrayList<GUIElement>();
        ArrayList<GUIElement> elements = new ArrayList<GUIElement>();

        for (GUIElement guiElement : guiElements) {
            if (guiElement instanceof ClickableGUI) {
                clickable.add(guiElement);
                //clickableNode.detachChild(guiElement);
            } else {
                elements.add(guiElement);
                //decoNode.detachChild(guiElement);
            }
        }

        for (GUIElement guie : elements) {
            decoNode.detachChild(guie);
        }
        for (GUIElement cgui : clickable) {
            clickableNode.detachChild(cgui);
        }

        elements.clear();
        clickable.clear();
        guiElements.clear();
    }
}
