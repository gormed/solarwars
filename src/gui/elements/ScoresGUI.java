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
public class ScoresGUI extends GUIElement {

    public static final int FADE_SPEED = 3500;
    private boolean fadeing = false;
    private boolean fadeDirection = true;
    private GameGUI gui;
    private boolean show;
    private float fadeMax;
    private float fadeCurrent = 0;
    private TabActionListener actionListener = new TabActionListener();
    private ArrayList<ScoresLine> playerLabels = new ArrayList<ScoresLine>();
    private HashMap<Integer, Vector3f> labelPosition = new HashMap<Integer, Vector3f>(8);
    private Label scoresLabel;
    private Panel background;
    private Label headNameLabel;
    private Label headShipsLabel;
    private Label headPlanetsLabel;
    private Label headPercentLabel;
    private Label headStateLabel;
    private Label headColorLabel;
    private Panel backgroundFrame;

    public ScoresGUI(GameGUI gui) {
        this.gui = gui;
        fadeMax = -(2 * gui.getWidth() / 3) + 10;
        this.setLocalTranslation(fadeMax, 0, 0);
        
        background = new Panel("TabScores",
                new Vector3f(gui.getWidth() / 3, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 3 + 4, gui.getHeight() / 3 + 4),
                new ColorRGBA(0, 0, 1, 0.2f));
        
        backgroundFrame = new Panel("TabScoresFrame",
                new Vector3f(gui.getWidth() / 3, gui.getHeight() / 2, 0),
                new Vector2f(gui.getWidth() / 3, gui.getHeight() / 3),
                new ColorRGBA(0, 0, 1, 0.4f));
        
        for (int i = 0; i < 8; i++) {
            labelPosition.put(i,
                    new Vector3f(
                    gui.getWidth() / 3,
                    (6.5f - i * 0.5f) * gui.getHeight() / 10, 0));
        }
        
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
        createTable(background, 0.6f);
        this.attachChild(headNameLabel);
        this.attachChild(scoresLabel);
        for (ScoresLine l : playerLabels) {
            this.attachChild(l);
        }

        //this.attachChild(floatInNode);
        gui.addGUIElement(this);
    }

    private void createTable(Panel parentPanel, float scale) {
        ColorRGBA textColor = ColorRGBA.Orange;
        float height = 7.2f * gui.getHeight() / 10;
        this.headNameLabel = new Label(
                "NAME",
                new Vector3f(parentPanel.getSize().x / 3, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        this.headShipsLabel = new Label(
                "SHIPS",
                new Vector3f(2 * parentPanel.getSize().x / 3, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };
        this.headPlanetsLabel = new Label(
                "PLANETS",
                new Vector3f(0.95f * parentPanel.getSize().x, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };
        this.headPercentLabel = new Label(
                "% OF ALL",
                new Vector3f(3.8f * parentPanel.getSize().x / 3, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };
        this.headStateLabel = new Label(
                "STATE",
                new Vector3f(4.7f * parentPanel.getSize().x / 3, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        this.headColorLabel = new Label(
                "Color",
                new Vector3f(5.55f * parentPanel.getSize().x / 3, height, 0),
                Vector3f.UNIT_XYZ.clone().multLocal(scale),
                textColor,
                gui) {

            @Override
            public void updateGUI(float tpf) {
            }

            @Override
            public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
            }
        };

        attachChild(headColorLabel);
        attachChild(headNameLabel);
        attachChild(headPercentLabel);
        attachChild(headShipsLabel);
        attachChild(headStateLabel);
        attachChild(headPlanetsLabel);

    }

    private void addPlayers() {
        Label l;
        ScoresLine line;
        ArrayList<Player> players = Hub.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            line = new ScoresLine(
                    p,
                    1.0f,
                    ColorRGBA.White.clone(),
                    background);
            line.setLocalTranslation(0, labelPosition.get(i).y, 0);
            playerLabels.add(line);
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

        for (ScoresLine line : playerLabels) {
            line.updateGUI(tpf);
        }


//        ArrayList<Player> players = Hub.getPlayers();
//        for (int i = 0; i < players.size(); i++) {
//            Player p = players.get(i);
//            float percent = 0;
//            if (l != null) {
//                percent =
//                        (float) p.getPlanetCount()
//                        / (float) l.getPlanetSet().size();
//                percent *= 100;
//            }
//            String playerLabel =
//                    p.getName() + " - " + p.getShipCount() + " - "
//                    + p.getPlanetCount() + " - "
//                    + (int) percent + "% - "
//                    + (p.hasLost() ? "LOST" : "PLAYING");
//            playerLabels.get(i).setCaption(playerLabel);
//        }
    }

    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        background.setVisible(show);
        backgroundFrame.setVisible(show);
        scoresLabel.setVisible(show);
        headNameLabel.setVisible(show);
        headNameLabel.setVisible(show);
        headPercentLabel.setVisible(show);
        headShipsLabel.setVisible(show);
        headStateLabel.setVisible(show);
        headPlanetsLabel.setVisible(show);
        headColorLabel.setVisible(show);
        for (ScoresLine l : playerLabels) {
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

    private class ScoresLine extends GUIElement {

        private Label playerNameLabel;
        private Label playerShipsLabel;
        private Label playerPlanetsLabel;
        private Label playerPercentLabel;
        private Label playerStateLabel;
        private Panel playerColorPanel;
        private Player player;

        public ScoresLine(
                Player p,
                float scale,
                ColorRGBA textColor,
                Panel parentPanel) {

            this.player = p;
            float percent = 0;
            Level l = Gameplay.getCurrentLevel();
            if (l != null) {
                percent =
                        (float) p.getPlanetCount()
                        / (float) l.getPlanetSet().size();
                percent *= 100;
            }

            String playerName = p.getName();
            int ships = p.getShipCount();

            int planets = p.getPlanetCount();
            String state = (p.hasLost() ? "LOST" : "PLAYING");
            ColorRGBA playerColor = p.getColor();
            textColor = playerColor.clone().addLocal(ColorRGBA.White.clone().multLocal(0.65f));
            textColor.clamp();
            //textColor.addLocal(playerColor.clone().multLocal(0.1f));
            textColor.a = 1.0f;

            this.playerNameLabel = new Label(
                    playerName,
                    new Vector3f(parentPanel.getSize().x / 3, 0, 0),
                    Vector3f.UNIT_XYZ.clone().multLocal(scale),
                    textColor,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };

            this.playerShipsLabel = new Label(
                    ships + "",
                    new Vector3f(2 * parentPanel.getSize().x / 3, 0, 0),
                    Vector3f.UNIT_XYZ.clone().multLocal(scale),
                    textColor,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };
            this.playerPlanetsLabel = new Label(
                    planets + "",
                    new Vector3f(0.95f * parentPanel.getSize().x, 0, 0),
                    Vector3f.UNIT_XYZ.clone().multLocal(scale),
                    textColor,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };
            this.playerPercentLabel = new Label(
                    (int) percent + " %",
                    new Vector3f(3.75f * parentPanel.getSize().x / 3, 0, 0),
                    Vector3f.UNIT_XYZ.clone().multLocal(scale),
                    textColor,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };
            this.playerStateLabel = new Label(
                    state,
                    new Vector3f(4.85f * parentPanel.getSize().x / 3, 0, 0),
                    Vector3f.UNIT_XYZ.clone().multLocal(scale),
                    textColor,
                    gui) {

                @Override
                public void updateGUI(float tpf) {
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };

            this.playerColorPanel = new Panel(
                    "PlayerColor",
                    new Vector3f(5.55f * parentPanel.getSize().x / 3, -2, 0),
                    Vector2f.UNIT_XY.clone().multLocal(15f),
                    playerColor);
            //playerColorPanel.material.setColor("GlowColor", playerColor);
            
            attachChild(playerNameLabel);
            attachChild(playerPercentLabel);
            attachChild(playerShipsLabel);
            attachChild(playerStateLabel);
            attachChild(playerPlanetsLabel);
            attachChild(playerColorPanel);
        }

        @Override
        public void setVisible(boolean show) {
            super.setVisible(show);
            playerNameLabel.setVisible(show);
            playerPercentLabel.setVisible(show);
            playerShipsLabel.setVisible(show);
            playerStateLabel.setVisible(show);
            playerPlanetsLabel.setVisible(show);
            playerColorPanel.setVisible(show);
        }

        @Override
        public void updateGUI(float tpf) {
            float percent = 0;
            Level l = Gameplay.getCurrentLevel();
            if (l != null) {
                percent =
                        (float) player.getPlanetCount()
                        / (float) l.getPlanetSet().size();
                percent *= 100;
            }

            String playerName = player.getName();
            int ships = player.getShipCount();

            int planets = player.getPlanetCount();
            String state = (player.hasLost() ? "LOST" : "PLAYS");
            ColorRGBA playerColor = player.getColor();

            playerColorPanel.material.setColor("Color", playerColor);
            playerNameLabel.setCaption(playerName);
            playerPercentLabel.setCaption((int) percent + " %");
            playerPlanetsLabel.setCaption(planets + "");
            playerShipsLabel.setCaption(ships + "");
            playerStateLabel.setCaption(state);

            playerNameLabel.updateGUI(tpf);
            playerPercentLabel.updateGUI(tpf);
            playerShipsLabel.updateGUI(tpf);
            playerStateLabel.updateGUI(tpf);
            playerPlanetsLabel.updateGUI(tpf);
            playerColorPanel.updateGUI(tpf);


        }
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
