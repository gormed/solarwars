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
 * File: ChatGUI.java
 * Type: gui.elements.ChatGUI
 * 
 * Documentation created: 31.03.2012 - 19:27:47 by Hans Ferchland
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
import gui.KeyInputMap;
import gui.KeyboardListener;
import java.util.ArrayList;
import logic.Player;
import net.ChatModule;
import net.NetworkManager;
import solarwars.Hub;
import solarwars.SolarWarsApplication;

/**
 * The Class ChatGUI.
 */
public class ChatGUI extends GUIElement implements ClickableGUI {

    public static final ColorRGBA COLOR_CHAT_BACKGROUND = new ColorRGBA(0, 0, 1, 0.75f);
    public static final ColorRGBA COLOR_CHAT_FONT = new ColorRGBA(0, 0, 1, 0.6f);
    public static final ColorRGBA COLOR_CHAT_TEXTFIELD = new ColorRGBA(1, 1, 1, 0.5f);
    /** The gui. */
    private GameGUI gui;
    private ChatModule chatModule;
    /** The text area. */
    private Panel textArea;
    private Panel background;
    /** The chat log. */
    private ArrayList<String> chatLog;
    private int maxMessagesDisplayed = 12;
    private ArrayList<TextLine> textLines;
    private ChatInput chatInput;
    private float fadeMax;
    private float fadeCurrent = 0;
    public static final int FADE_SPEED = 3500;
    private boolean fadeing = false;
    private boolean fadeDirection = true;

    /**
     * Instantiates a new chat gui.
     *
     * @param gui the gui
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

    public void playerSays(Player p, String message) {
        chatLog.add(p.getName() + ": " + message);
    }

    private void localPlayerSays(String message) {
        chatLog.add(Hub.getLocalPlayer().getName() + ": " + message);
        chatModule.localPlayerSendChatMessage(Hub.getLocalPlayer().getId(), message);
    }

    /* (non-Javadoc)
     * @see gui.GUIElement#updateGUI(float)
     */
    @Override
    public void updateGUI(float tpf) {

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
                }
            }
        } else {
            //this.setLocalTranslation(0, 0, 0);
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

    public void startFadeIn() {
        setVisible(true);
        fadeing = true;
        fadeDirection = true;
    }

    public void startFadeOut() {
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

    public void peek() {
        
        startFadeIn();        
        gui.setFocus(null);
    }

    public void show() {
        //setVisible(true);
        startFadeIn();
        gui.setFocus(chatInput);
    }

    public void hide() {
        //setVisible(false);
        startFadeOut();
        gui.setFocus(null);
    }

    public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        chatInput.onClick(cursor, isPressed, tpf);
    }

    public boolean canGainFocus() {
        return false;
    }

    private class ChatInput extends TextBox {

        private InputManager inputManager;

        public ChatInput(Vector3f screenPosition, Vector3f scale, GameGUI gui) {
            super(
                    COLOR_CHAT_FONT, screenPosition,
                    scale, "", COLOR_CHAT_TEXTFIELD,
                    gui);
            inputManager = SolarWarsApplication.getInstance().getInputManager();
            textListener = new ChatInputListener(inputManager, this);

        }

        @Override
        public void onClick(Vector2f cursor, boolean isPressed, float tpf) {
        }

        @Override
        protected void onKeyTrigger(String key, boolean isPressed, float tpf) {
        }

        @Override
        public void updateGUI(float tpf) {
            super.updateGUI(tpf);
        }

        private class ChatInputListener extends KeyboardListener {

            public ChatInputListener(InputManager inputManager, TextBox textBox) {
                super(inputManager, textBox);
                inputManager.addMapping(KeyInputMap.INPUT_MAPPING_ENTER, new KeyTrigger(KeyInput.KEY_RETURN));

                inputManager.addListener(this, KeyInputMap.INPUT_MAPPING_ENTER);
            }

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

                    if (name.equals(KeyInputMap.INPUT_MAPPING_ENTER) && !"".equals(caption)) {
                        localPlayerSays(getCaption());
                        //playerSays(Hub.getLocalPlayer(), getCaption());
                        setCaption("");
                    } else if (name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && caption.length() > 0) {
                        caption = caption.substring(0, caption.length() - 1);
                    } else if (!name.equals(KeyInputMap.INPUT_MAPPING_BACKSPACE) && !name.equals(KeyInputMap.INPUT_MAPPING_ENTER)) {
                        caption += name;
                    }
                }
            }
        }
    }

    private class TextLine extends GUIElement {

        private Label line;
        private String lineText;

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

        @Override
        public void setVisible(boolean show) {
            super.setVisible(show);
            line.setVisible(show);
        }

        @Override
        public void updateGUI(float tpf) {
            line.updateGUI(tpf);
        }

        public String getLineText() {
            return lineText;
        }

        public void setLineText(String lineText) {
            this.lineText = lineText;
        }

        public BitmapText getBitmapText() {
            return line.text;
        }
    }
}
