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
 * File: Percentage.java
 * Type: gui.elements.Percentage
 * 
 * Documentation created: 31.03.2012 - 19:27:48 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.GameGUI;
import solarwars.Hub;

/**
 * The Class Percentage.
 */
public class Percentage extends Label {

    private boolean valueChanged = false;
    private int percentage = 50;
    private final Panel background;
    private final Panel backgroundFrame;
//    private final Label frame;
    private float fadeMax;
    private float peek;
    private float fadeCurrent = 0;
    public static final float PEEK_DURATION = 1.5f;
    public static final int FADE_SPEED = 400;
    private boolean fadeing = false;
    private boolean fadeDirection = true;

    /**
     * Instantiates a new percentage.
     *
     * @param gui the gui
     */
    public Percentage(GameGUI gui) {
        super("Percentage: " + (int) (Hub.getLocalPlayer().getShipPercentage() * 100) + "%",
                new Vector3f(
                0,
                0,
                0),
                new Vector3f(0.7f, 0.7f, 1),
                new ColorRGBA(1, 0.5f, 0, 0.7f), gui);

        fadeMax = 1.02f * gui.getHeight();
        fadeCurrent = 0.95f * gui.getHeight();
        this.setLocalTranslation(
                gui.getWidth() / 2,
                fadeCurrent,
                0);

//        frame = new Label(title, screenPosition, scale.clone().multLocal(1.025f), new ColorRGBA(0, 0, 0, 0.5f), gui) {
//
//            @Override
//            public void updateGUI(float tpf) {
//                setCaption(text.getText());
//            }
//
//            @Override
//            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
//            }
//        };
        background = new Panel("Percentage_Back",
                new Vector3f(
                0,
                0.1f * gui.getHeight(),
                0),
                new Vector2f(
                130,
                text.getLineHeight() * 2.5f),
                new ColorRGBA(0, 0, 1, 0.2f));
        backgroundFrame = new Panel("Percentage_Back",
                new Vector3f(
                0,
                0.1f * gui.getHeight(),
                0),
                new Vector2f(
                135,
                text.getLineHeight() * 2.6f),
                new ColorRGBA(0, 0, 1, 0.2f));
        detachChild(text);

        attachChild(background);
        attachChild(backgroundFrame);
//        attachChild(frame);
        attachChild(text);
//        font = FontLoader.getInstance().getFont("SolarWarsFont32");
//        text = new BitmapText(font, false);
//        text.setText();
//        text.setColor(ColorRGBA.Orange);
//        
//        Vector3f scale = new Vector3f(0.5f, 0.5f, 1);
//        Vector3f pos = new Vector3f(
//                gui.getWidth()/2 - text.getLineWidth()/2, 
//                gui.getHeight() - text.getLineHeight(), 
//                0);
//        pos.multLocal(scale);
//        
//        setLocalTranslation(pos);
//        text.setLocalScale(scale);
//        
//        this.attachChild(text);
//        
//        setVisible(true);
    }

    private boolean refreshPercentage() {
        int newPerc = (int) (Hub.getLocalPlayer().getShipPercentage() * 100);
        boolean changed = (percentage != newPerc);
        if (changed) {
            this.percentage = newPerc;

            valueChanged = changed;
        }
        return changed;
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {
        boolean changed = refreshPercentage();
        peek += tpf;

        if (changed) {
            startFadeIn();
            peek = 0;
        }

        if (peek > PEEK_DURATION) {
            startFadeOut();
            peek = 0;
        }

        if (fadeing) {
            if (fadeDirection) {
                fadeCurrent -= tpf * FADE_SPEED;
                this.setLocalTranslation(0.5f * gui.getWidth(), fadeCurrent, 0);
                if (fadeCurrent <= 0.95f * gui.getHeight()) {
                    fadeing = false;
                    fadeCurrent = 0.95f * gui.getHeight();
                    this.setLocalTranslation(0.5f * gui.getWidth(), fadeCurrent, 0);
                }

            } else {
                fadeCurrent += tpf * FADE_SPEED;
                this.setLocalTranslation(0.5f * gui.getWidth(), fadeCurrent, 0);
                if (fadeCurrent >= fadeMax) {
                    fadeing = false;
                    fadeCurrent = fadeMax;
                    this.setLocalTranslation(0.5f * gui.getWidth(), fadeCurrent, 0);
                    setVisible(true);
                }
            }
        } else {
        }

        background.updateGUI(tpf);
        backgroundFrame.updateGUI(tpf);
//        frame.updateGUI(tpf);

        text.setText("Percentage: " + percentage + "%");
    }

    public void startFadeIn() {
        setVisible(true);
        fadeing = true;
        fadeDirection = true;
    }

    public void startFadeOut() {
        //setVisible(true);
        fadeing = true;
        fadeDirection = false;
    }
    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */

    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        backgroundFrame.setVisible(show);
//        frame.setVisible(show);
        text.setCullHint(show ? CullHint.Never : CullHint.Always);
    }

    /* (non-Javadoc)
     * @see gui.elements.Label#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
    }
}
