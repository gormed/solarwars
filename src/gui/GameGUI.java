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
 * File: GameGUI.java
 * Type: gui.GameGUI
 * 
 * Documentation created: 31.03.2012 - 19:27:46 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import solarwars.SolarWarsApplication;
import solarwars.SolarWarsGame;

/**
 * The Class GameGUI.
 */
public class GameGUI {

    /** The game. */
    SolarWarsGame game;
    /** The gui elemetns. */
    private ArrayList<GUIElement> guiElemetns;
    /** The width. */
    private float width;
    /** The action listener. */
    private ActionListener actionListener;
    /** The focus. */
    private GUIElement focus;
    /** The height. */
    private float height;
    /** The clickable node. */
    private Node clickableNode;
    /** The deco node. */
    private Node decoNode;

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

    /**
     * Instantiates a new game gui.
     *
     * @param game the game
     */
    public GameGUI(SolarWarsGame game) {
        this.game = game;
        this.width = game.getApplication().getCamera().getWidth();
        this.height = game.getApplication().getCamera().getHeight();
        this.guiElemetns = new ArrayList<GUIElement>();

        this.clickableNode = new Node("ClickableGUI");
        this.decoNode = new Node("DecoNode");

        final InputManager inputManager = game.getApplication().getInputManager();
        final Camera cam = game.getApplication().getCamera();
        final Node guiNode = game.getApplication().getGuiNode();

        guiNode.attachChild(decoNode);
        guiNode.attachChild(clickableNode);

        this.actionListener = new ActionListener() {

            public void onAction(String name, boolean isPressed, float tpf) {
                if (name.equals(SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK)) {
                    //if (isPressed) {
                    // 1. Reset results list.
                    CollisionResults results = new CollisionResults();
                    // 2. calculate ray from camera to mouse pointer
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d =
                            new Vector3f(click2d.x, click2d.y, 0f);
                    Vector3f dir = new Vector3f(click2d.x, click2d.y, 1f).subtractLocal(click3d).normalizeLocal();
                    Ray ray = new Ray(click3d, dir);
                    // 3. Collect intersections between Ray and Shootables in
                    // results list.
                    clickableNode.collideWith(ray, results);
                    // 4. Print the results
                    System.out.println("----- ScreenCollisions? " + results.size()
                            + "-----");
                    for (int i = 0; i < results.size(); i++) {
                        // For each hit, we know distance, impact point, name of
                        // geometry.
                        float dist = results.getCollision(i).getDistance();
                        Vector3f pt = results.getCollision(i).getContactPoint();
                        String hit = results.getCollision(i).getGeometry().getName();
                        System.out.println("* Collision #" + i);
                        System.out.println("  You shot " + hit + " at " + pt
                                + ", " + dist + " wu away.");
                    }
                    // 5. Use the results (we mark the hit object)
                    if (results.size() > 0) {
                        // The closest collision point is what was truly hit:
                        CollisionResult closest = results.getClosestCollision();
                        Node n = closest.getGeometry().getParent();

                        if (n instanceof ClickableGUI) {
                            
                            clickableHit(n, click2d, isPressed, tpf);
                        } else if (n.getParent() instanceof ClickableGUI) {
                            clickableHit(n.getParent(), click2d, isPressed, tpf);
                        }
                    }
                } else {
                }
            }

            private void clickableHit(Node n, Vector2f click2d, boolean isPressed, float tpf) {

                if (n instanceof GUIElement) {
                    GUIElement element = (GUIElement) n;

                    if (element.isVisible()) {
                        ClickableGUI g = (ClickableGUI) n;
                        setFocus(g);
                        g.onClick(click2d, isPressed, tpf);
                    }
                }
            }

            private void setFocus(ClickableGUI g) {
                if (g instanceof GUIElement) {
                    focus = (GUIElement) g;
                }
            }
            //}
        };
        if (inputManager != null) {
            inputManager.addListener(actionListener,
                    SolarWarsApplication.INPUT_MAPPING_LEFT_CLICK);
        }
    }

    /**
     * Adds the gui element.
     *
     * @param guiElement the gui element
     */
    public void addGUIElement(GUIElement guiElement) {
        if (guiElement instanceof ClickableGUI) {
            clickableNode.attachChild(guiElement);
        } else {
            decoNode.attachChild(guiElement);
        }
        guiElemetns.add(guiElement);
    }

    /**
     * Removes the gui element.
     *
     * @param guiElement the gui element
     */
    public void removeGUIElement(GUIElement guiElement) {
        if (guiElement instanceof ClickableGUI) {
            clickableNode.detachChild(guiElement);
        } else {
            decoNode.detachChild(guiElement);
        }
        guiElemetns.remove(guiElement);

    }

    /**
     * Updates the gui elements.
     *
     * @param tpf the tpf
     */
    public void updateGUIElements(float tpf) {
        for (GUIElement e : guiElemetns) {
            e.updateGUI(tpf);
        }
    }

    /**
     * Contains gui element.
     *
     * @param e the e
     * @return true, if successful
     */
    public boolean containsGUIElement(GUIElement e) {
        return guiElemetns.contains(e);
    }

    /**
     * Clean up gui.
     */
    public void cleanUpGUI() {

        ArrayList<GUIElement> clickable = new ArrayList<GUIElement>();
        ArrayList<GUIElement> elements = new ArrayList<GUIElement>();

        for (GUIElement guiElement : guiElemetns) {
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
        guiElemetns.clear();
    }
}
