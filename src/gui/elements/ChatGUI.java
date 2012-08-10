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
 * File: ChatGUI.java
 * Type: gui.elements.ChatGUI
 * 
 * Documentation created: 14.07.2012 - 19:38:00 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import gui.ClickableGUI;
import gui.GUIElement;
import gui.GameGUI;
import input.KeyInputManager;
import input.KeyboardListener;
import java.util.ArrayList;
import logic.Player;
import net.ChatModule;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class ChatGUI.
 */
public class ChatGUI extends GUIElement implements ClickableGUI {

    /** The Constant COLOR_CHAT_BACKGROUND. */
    public static final ColorRGBA COLOR_CHAT_BACKGROUND = new ColorRGBA(0, 0, 1, 0.6f);
    /** The Constant COLOR_CHAT_FONT. */
    public static final ColorRGBA COLOR_CHAT_FONT = new ColorRGBA(1, 0.5f, 0, 0.5f);
    /** The Constant COLOR_CHAT_TEXTFIELD. */
    public static final ColorRGBA COLOR_CHAT_TEXTFIELD = new ColorRGBA(1, 1, 1, 0.4f);
    /** The Constant PEEK_TIME. */
    public static final float PEEK_TIME = 5;
    /** The gui. */
    private GameGUI gui;
    /** The chat module. */
    private ChatModule chatModule;
    /** The text area. */
    private Panel textArea;
    /** The background. */
    private Panel background;
    /** The chat log. */
    private ArrayList<String> chatLog;
    /** The max messages displayed. */
    private int maxMessagesDisplayed = 12;
    /** The text lines. */
    private ArrayList<TextLine> textLines;
    /** The chat input. */
    private ChatInput chatInput;
    /** The fade max. */
    private float fadeMax;
    /** The fade current. */
    private float fadeCurrent = 0;
    /** The peek fade. */
    private float peekFade = 0;
    /** The Constant FADE_SPEED. */
    public static final int FADE_SPEED = 3500;
    /** The fadeing. */
    private boolean fadeing = false;
    /** The fade direction. */
    private boolean fadeDirection = true;
    /** The peek. */
    private boolean peek = false;

    /**
     * Instantiates a new chat gui.
     *
     * @param gui the gui
     * @param chatModule the chat module
     */
    public ChatGUI(GameGUI gui, ChatModule chatModule) {
        this.gui = gui;
        this.chatModule = chatModule;



        background = new Panel("ChatBackground",
                new Vector3f(
                0,
                0,
                0),
                new Vector2f(
                gui.getWidth() / 4,
                gui.getHeight() / 2.9f),
                COLOR_CHAT_BACKGROUND);
        fadeMax = (5 * gui.getWidth() / 4) - 10;
        this.setLocalTranslation(
                fadeMax,
                0.5f * gui.getHeight(),
                0);
        chatInput = new ChatInput(
                new Vector3f(0, 35 - background.getSize().y, 0),
                Vector3f.UNIT_XYZ,
                gui);

        textLines = new ArrayList<TextLine>(maxMessagesDisplayed);



        textArea = new Panel("TextArea",
                new Vector3f(
                0,
                40,
                0),
                new Vector2f(
                gui.getWidth() / 4.1f,
                gui.getHeight() / 3.5f),
                COLOR_CHAT_TEXTFIELD);


        chatLog = new ArrayList<String>();


        attachChild(background);

        attachChild(textArea);

        for (int i = 0; i < maxMessagesDisplayed; i++) {
            TextLine line = new TextLine("", 0.75f);
            textLines.add(line);
            line.setLocalTranslation(
                    10 - textArea.getSize().x,
                    50 - textArea.getSize().y + (i * line.getBitmapText().getLineHeight()),
                    0);
            textArea.attachChild(line);
        }
        attachChild(chatInput);
        //attachChild(chatInput);
    }

    /**
     * Player says.
     *
     * @param p the p
     * @param message the message
     */
    public void playerSays(Player p, String message) {
        chatLog.add(p.getName() + ": " + message);
    }

    /**
     * Server says.
     *
     * @param message the message
     */
    public void serverSays(String message) {
        chatLog.add(message);
    }

    /**
     * Local player says.
     *
     * @param message the message
     */
    private void localPlayerSays(String message) {
        chatLog.add(Hub.getLocalPlayer().getName() + ": " + message);
        chatModule.localPlayerSendChatMessage(Hub.getLocalPlayer().getId(), message);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {

        if (peek) {
            peekFade += tpf;

            if (peekFade > PEEK_TIME) {
                peekFade = 0;
                hide();
            }

        }
        if (fadeing) {
            if (fadeDirection) {
                fadeCurrent -= tpf * FADE_SPEED;
                this.setLocalTranslation(fadeCurrent, 0.5f * gui.getHeight(), 0);
                if (fadeCurrent <= 3 * gui.getWidth() / 4) {
                    fadeing = false;
                    this.setLocalTranslation(3 * gui.getWidth() / 4, 0.5f * gui.getHeight(), 0);

                }

            } else {
                fadeCurrent += tpf * FADE_SPEED;
                this.setLocalTranslation(fadeCurrent, 0.5f * gui.getHeight(), 0);
                if (fadeCurrent >= fadeMax) {
                    fadeing = false;
                    this.setLocalTranslation(fadeMax, 0.5f * gui.getHeight(), 0);
                    setVisible(true);
                }
            }
        } else {
            if (fadeDirection) {
                //if (fadeCurrent <= 3 * gui.getWidth() / 4) {
                this.setLocalTranslation(3 * gui.getWidth() / 4, 0.5f * gui.getHeight(), 0);
                //}
            } else {
                //if (fadeCurrent >= fadeMax) {
                this.setLocalTranslation(fadeMax, 0.5f * gui.getHeight(), 0);
                //}
            }
        }

        textArea.updateGUI(tpf);
        background.updateGUI(tpf);
        chatInput.updateGUI(tpf);

        if (!chatLog.isEmpty()) {
            int j = maxMessagesDisplayed - 1;

            for (int i = 0; i < textLines.size(); i++) {
                if (chatLog.size() - 1 < i) {
                    break;
                }
                TextLine line = textLines.get(i);

                line.setLineText(chatLog.get((chatLog.size() - 1) - i));

            }
        }
        for (TextLine line : textLines) {
            line.updateGUI(tpf);
        }
    }

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
        textArea.setVisible(show);
        chatInput.setVisible(show);
        background.setVisible(show);
        for (TextLine line : textLines) {
            line.setVisible(show);
        }

    }

    /**
     * Peek.
     */
    public void peek() {
        startFadeIn();
        textArea.setVisible(false);
        chatInput.setVisible(false);
        background.setVisible(false);

        gui.setFocus(null);
        peek = true;
    }

    /**
     * Show.
     */
    public void show() {
        startFadeIn();
        gui.setFocus(chatInput);
        peek = false;
    }

    /**
     * Hide.
     */
    public void hide() {
        //setVisible(false);
        startFadeOut();
        gui.setFocus(null);
        peek = false;
    }

    /**
     * Checks if is fade direction.
     *
     * @return true, if is fade direction
     */
    public boolean isFadeDirection() {
        return fadeDirection;
    }

    /**
     * Checks if is fadeing.
     *
     * @return true, if is fadeing
     */
    public boolean isFadeing() {
        return fadeing;
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#onClick(com.jme3.math.Vector2f, boolean, float)
     */
    @Override
    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        chatInput.onClick(cursor, isPressed, tpf);
    }

    /* (non-Javadoc)
     * @see gui.ClickableGUI#canGainFocus()
     */
    @Override
    public boolean canGainFocus() {
        return false;
    }

    /**
     * The Class ChatInput.
     */
    private class ChatInput extends TextBox {

        /** The input manager. */
        private InputManager inputManager;

        /**
         * Instantiates a new chat input.
         *
         * @param screenPosition the screen position
         * @param scale the scale
         * @param gui the gui
         */
        public ChatInput(Vector3f screenPosition, Vector3f scale, GameGUI gui) {
            super(
                    COLOR_CHAT_FONT, screenPosition,
                    scale, "", COLOR_CHAT_TEXTFIELD,
                    gui);
            inputManager = SolarWarsApplication.getInstance().getInputManager();
            textListener = new ChatInputListener(inputManager, this);

        }

        /* (non-Javadoc)
         * @see gui.elements.TextBox#onClick(com.jme3.math.Vector2f, boolean, float)
         */
        @Override
        public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        }

        /* (non-Javadoc)
         * @see gui.elements.TextBox#onKeyTrigger(java.lang.String, boolean, float)
         */
        @Override
        protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
        }

        /* (non-Javadoc)
         * @see gui.elements.TextBox#updateGUI(float)
         */
        @Override
        public void updateGUI(float tpf) {
            super.updateGUI(tpf);
        }

        /**
         * The listener interface for receiving chatInput events.
         * The class that is interested in processing a chatInput
         * event implements this interface, and the object created
         * with that class is registered with a component using the
         * component's <code>addChatInputListener<code> method. When
         * the chatInput event occurs, that object's appropriate
         * method is invoked.
         *
         * @see ChatInputEvent
         */
        private class ChatInputListener extends KeyboardListener {

            /**
             * Instantiates a new chat input listener.
             *
             * @param inputManager the input manager
             * @param textBox the text box
             */
            public ChatInputListener(InputManager inputManager, TextBox textBox) {
                super(inputManager, textBox);
                if (inputManager != null) {
                    inputManager.addMapping(KeyInputManager.INPUT_MAPPING_ENTER, new KeyTrigger(KeyInput.KEY_RETURN));

                    inputManager.addListener(this, KeyInputManager.INPUT_MAPPING_ENTER);
                }
            }

            /* (non-Javadoc)
             * @see gui.KeyboardListener#onAction(java.lang.String, boolean, float)
             */
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                GUIElement e = gui.getFocusElement();
                TextBox activeTextBox = null;
                if (e instanceof TextBox) {
                    activeTextBox = (TextBox) e;
                } else {
                    return;
                }

                if (!isPressed && activeTextBox.equals(textBox)) {

                    if (name.equals(KeyInputManager.INPUT_MAPPING_ENTER) && !"".equals(caption)) {
                        localPlayerSays(getCaption());
                        //playerSays(Hub.getLocalPlayer(), getCaption());
                        setCaption("");
                    } else if (name.equals(KeyInputManager.INPUT_MAPPING_BACKSPACE) && caption.length() > 0) {
                        caption = caption.substring(0, caption.length() - 1);
                    } else if (!name.equals(KeyInputManager.INPUT_MAPPING_BACKSPACE) && !name.equals(KeyInputManager.INPUT_MAPPING_ENTER)) {
                        caption += name;
                    }
                }
            }
        }
    }

    /**
     * The Class TextLine.
     */
    private class TextLine extends GUIElement {

        /** The line. */
        private Label line;
        /** The line text. */
        private String lineText;

        /**
         * Instantiates a new text line.
         *
         * @param initialText the initial text
         * @param scale the scale
         */
        public TextLine(String initialText, float scale) {
            line = new Label(initialText, Vector3f.ZERO, Vector3f.UNIT_XYZ, COLOR_CHAT_FONT, gui) {

                @Override
                public void updateGUI(float tpf) {
                    text.setText(lineText);
                }

                @Override
                public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
                }
            };
            line.text.scale(scale);
            line.text.setAlignment(Align.Left);
            attachChild(line);
        }

        /* (non-Javadoc)
         * @see gui.GUIElement#setVisible(boolean)
         */
        @Override
        public void setVisible(boolean show) {
            super.setVisible(show);
            line.setVisible(show);
        }

        /* (non-Javadoc)
         * @see gui.GUIElement#updateGUI(float)
         */
        @Override
        public void updateGUI(float tpf) {
            line.updateGUI(tpf);
        }

        /**
         * Gets the line text.
         *
         * @return the line text
         */
        public String getLineText() {
            return lineText;
        }

        /**
         * Sets the line text.
         *
         * @param lineText the new line text
         */
        public void setLineText(String lineText) {
            this.lineText = lineText;
        }

        /**
         * Gets the bitmap text.
         *
         * @return the bitmap text
         */
        public BitmapText getBitmapText() {
            return line.text;
        }
    }
}
