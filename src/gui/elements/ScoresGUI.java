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
 * File: ScoresGUI.java
 * Type: gui.elements.ScoresGUI
 * 
 * Documentation created: 14.07.2012 - 19:37:59 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
 * The Class ScoresGUI.
 *
 * @author Hans
 */
public class ScoresGUI extends GUIElement {

    /** The Constant FADE_SPEED. */
    public static final int FADE_SPEED = 3500;
    
    /** The fadeing. */
    private boolean fadeing = false;
    
    /** The fade direction. */
    private boolean fadeDirection = true;
    
    /** The gui. */
    private GameGUI gui;
    
    /** The show. */
    private boolean show;
    
    /** The fade max. */
    private float fadeMax;
    
    /** The fade current. */
    private float fadeCurrent = 0;
    
    /** The action listener. */
    private TabActionListener actionListener = new TabActionListener();
    
    /** The player labels. */
    private ArrayList<ScoresLine> playerLabels = new ArrayList<ScoresLine>();
    
    /** The label position. */
    private HashMap<Integer, Vector3f> labelPosition = new HashMap<Integer, Vector3f>(8);
    
    /** The scores label. */
    private Label scoresLabel;
    
    /** The background. */
    private Panel background;
    
    /** The head name label. */
    private Label headNameLabel;
    
    /** The head ships label. */
    private Label headShipsLabel;
    
    /** The head planets label. */
    private Label headPlanetsLabel;
    
    /** The head percent label. */
    private Label headPercentLabel;
    
    /** The head state label. */
    private Label headStateLabel;
    
    /** The head color label. */
    private Label headColorLabel;
    
    /** The background frame. */
    private Panel backgroundFrame;

    /**
     * Instantiates a new scores gui.
     *
     * @param gui the gui
     */
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

    /**
     * Creates the table.
     *
     * @param parentPanel the parent panel
     * @param scale the scale
     */
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

    /**
     * Adds the players.
     */
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

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
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

    /* (non-Javadoc)
     * @see gui.GUIElement#setVisible(boolean)
     */
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
    /**
 * Start fade in.
 */
public void startFadeIn() {
        setVisible(true);
        fadeing = true;
        fadeDirection = true;
    }

    /**
     * Start fade out.
     */
    public void startFadeOut() {
        fadeing = true;
        fadeDirection = false;
    }

    /**
     * Gets the action listener.
     *
     * @return the action listener
     */
    public ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Destroy.
     */
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

    /**
     * The Class ScoresLine.
     */
    private class ScoresLine extends GUIElement {

        /** The player name label. */
        private Label playerNameLabel;
        
        /** The player ships label. */
        private Label playerShipsLabel;
        
        /** The player planets label. */
        private Label playerPlanetsLabel;
        
        /** The player percent label. */
        private Label playerPercentLabel;
        
        /** The player state label. */
        private Label playerStateLabel;
        
        /** The player color panel. */
        private Panel playerColorPanel;
        
        /** The player. */
        private Player player;

        /**
         * Instantiates a new scores line.
         *
         * @param p the p
         * @param scale the scale
         * @param textColor the text color
         * @param parentPanel the parent panel
         */
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

        /* (non-Javadoc)
         * @see gui.GUIElement#setVisible(boolean)
         */
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

        /* (non-Javadoc)
         * @see gui.GUIElement#updateGUI(float)
         */
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

    /**
     * The listener interface for receiving tabAction events.
     * The class that is interested in processing a tabAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addTabActionListener<code> method. When
     * the tabAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see TabActionEvent
     */
    private class TabActionListener implements ActionListener {

        /* (non-Javadoc)
         * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
         */
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
