/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.GUIElement;
import gui.GameGUI;
import java.util.ArrayList;
import java.util.HashMap;
import logic.Gameplay;
import logic.Level;
import logic.Player;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 *
 * @author Hans
 */
public class TabScores extends GUIElement {

    public static final int FADE_SPEED = 3500;
    private boolean fadeing = false;
    private boolean fadeDirection = true;
    private GameGUI gui;
    private boolean show;
    private float fadeMax;
    private float fadeCurrent = 0;
    private TabActionListener actionListener = new TabActionListener();
    private ArrayList<Label> playerLabels = new ArrayList<Label>();
    private HashMap<Integer, Vector3f> labelPosition = new HashMap<Integer, Vector3f>(8);
    private Label scoresLabel;
    private Panel background;
    private Label headLabel;
    private Panel backgroundFrame;

    public TabScores(GameGUI gui) {
        this.gui = gui;
        fadeMax = -(2*gui.getWidth()/3)+10;
        this.setLocalTranslation(fadeMax, 0, 0);
        background = new Panel("TabScores",
                new Vector3f(gui.getWidth() / 3, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 3 + 4, gui.getHeight() / 3 + 4),
                new ColorRGBA(0, 0, 1, 0.2f));
        backgroundFrame = new Panel("TabScoresFrame",
                new Vector3f(gui.getWidth() / 3, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 3, gui.getHeight() / 3),
                new ColorRGBA(0, 0, 1, 0.4f));
        //labelPosition.add(new Vector2f(1, 1));
        for (int i = 0; i < 8; i++) {
            labelPosition.put(i,
                    new Vector3f(
                    gui.getWidth() / 3,
                    (6.5f - i * 0.5f) * gui.getHeight() / 10, 0));
        }

//        floatIn = new Vector3f(gui.getWidth() / 2, gui.getHeight() / 2, 0);
//        floatInNode = new Node("ScoresFloat");
//        floatInNode.setLocalTranslation(floatIn);

        headLabel = new Label(
                "NAME - SHIPS - PLANETS - % ALL - LOST/PLAYING",
                new Vector3f(
                gui.getWidth() / 3,
                (7.2f) * gui.getHeight() / 10, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(0.6f),
                ColorRGBA.Orange, gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        scoresLabel = new Label("SCORES", new Vector3f(gui.getWidth() / 3,
                8f * gui.getHeight() / 10,
                0),
                Vector3f.UNIT_XYZ.clone().multLocal(1.4f),
                ColorRGBA.White, gui) {

            private float time;

            @Override
            public void updateGUI(float tpf) {
                time += tpf;

                if (time < 0.2f) {
                    text.setText(title + "_");
                } else if (time < 0.4f) {
                    text.setText(title);
                } else {
                    time = 0;
                }
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };
        addPlayers();


        this.attachChild(background);
        this.attachChild(backgroundFrame);
        this.attachChild(headLabel);
        this.attachChild(scoresLabel);
        for (Label l : playerLabels) {
            this.attachChild(l);
        }

        //this.attachChild(floatInNode);
        gui.addGUIElement(this);
    }

    private void addPlayers() {
        Label l;
        ArrayList<Player> players = Hub.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            String playerLabel =
                    p.getName() + " - " + p.getShipCount() + " - "
                    + p.getPlanetCount() + " - "
                    + "0% - "
                    + (p.hasLost() ? "LOST" : "PLAYING");
            Vector3f pos = labelPosition.get(i);
            l = new Label(
                    playerLabel,
                    pos,
                    Vector3f.UNIT_XYZ,
                    ColorRGBA.White,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };
            playerLabels.add(l);
        }
    }

    @Override
    public void updateGUI(float tpf) {

        if (fadeing) {
            if (fadeDirection) {
                fadeCurrent += tpf * FADE_SPEED;
                this.setLocalTranslation(fadeCurrent, 0, 0);
                if (fadeCurrent >= -20) {
                    fadeing = false;
                    this.setLocalTranslation(-20, 0, 0);
                }

            } else {
                fadeCurrent -= tpf * FADE_SPEED;
                this.setLocalTranslation(fadeCurrent, 0, 0);
                if (fadeCurrent <= fadeMax) {
                    fadeing = false;
                    this.setLocalTranslation(fadeMax, 0, 0);
                }
            }
        } else {
            //this.setLocalTranslation(0, 0, 0);
        }

        background.updateGUI(tpf);
        scoresLabel.updateGUI(tpf);

        Level l = Gameplay.getCurrentLevel();


        ArrayList<Player> players = Hub.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            float percent = 0;
            if (l != null) {
                percent =
                        (float) p.getPlanetCount()
                        / (float) l.getPlanetSet().size();
                percent *= 100;
            }
            String playerLabel =
                    p.getName() + " - " + p.getShipCount() + " - "
                    + p.getPlanetCount() + " - "
                    + (int) percent + "% - "
                    + (p.hasLost() ? "LOST" : "PLAYING");
            playerLabels.get(i).setCaption(playerLabel);
        }
    }

    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        backgroundFrame.setVisible(show);
        scoresLabel.setVisible(show);
        headLabel.setVisible(show);
        for (Label l : playerLabels) {
            l.setVisible(show);
        }
    }

//    private boolean endedFading() {
//        if (floatInNode.getLocalTranslation().x >= gui.getWidth() / 2) {
//            return true;
//        } else if (floatInNode.getLocalTranslation().x <= -gui.getWidth() / 2) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
    public void startFadeIn() {
        setVisible(true);
        fadeing = true;
        fadeDirection = true;
    }

    public void startFadeOut() {
        fadeing = true;
        fadeDirection = false;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void destroy() {
        this.detachAllChildren();
        SolarWarsApplication.getInstance().
                getInputManager().removeListener(actionListener);
        labelPosition.clear();
        playerLabels.clear();
        fadeCurrent = 0;
        fadeing = false;
        fadeDirection = false;
    }

    private class TabActionListener implements ActionListener {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed && name.equals(SolarWarsApplication.INPUT_MAPPING_TABSCORE)) {
                //setVisible(show = true);
                startFadeIn();
            } else if (!isPressed && name.equals(SolarWarsApplication.INPUT_MAPPING_TABSCORE)) {
                //setVisible(show = false);
                startFadeOut();
            }
        }
    }
}
