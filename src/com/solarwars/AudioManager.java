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
 * File: AudioManager.java
 * Type: com.solarwars.AudioManager
 * 
 * Documentation created: 14.07.2012 - 19:37:58 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.solarwars.settings.SolarWarsSettings;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AudioManager.
 *
 * @author Hans
 */
public class AudioManager {

    /**
     * The Constant SOUND_BEEP.
     */
    public static final String SOUND_BEEP = "Sounds/beep.ogg";
    /**
     * The Constant SOUND_CLICK.
     */
    public static final String SOUND_CLICK = "Sounds/click.ogg";
    /**
     * The Constant SOUND_CYBER.
     */
    public static final String SOUND_CYBER = "Sounds/cyber.ogg";
    /**
     * The Constant SOUND_ERROR.
     */
    public static final String SOUND_ERROR = "Sounds/error.ogg";
    /**
     * The Constant SOUND_EXPLOSION.
     */
    public static final String SOUND_EXPLOSION = "Sounds/explosion.ogg";
    /**
     * The Constant SOUND_HELI.
     */
    public static final String SOUND_HELI = "Sounds/heli.ogg";
    /**
     * The Constant SOUND_LOAD.
     */
    public static final String SOUND_LOAD = "Sounds/load.ogg";
    /**
     * The Constant SOUND_ROCKET.
     */
    public static final String SOUND_ROCKET = "Sounds/rocket.ogg";
    /**
     * The Constant SOUND_CAPTURE.
     */
    public static final String SOUND_CAPTURE = "Sounds/capture.ogg";
    /**
     * The instance.
     */
    private static AudioManager instance;
    /**
     * The application.
     */
    private SolarWarsApplication application;
    /**
     * The audio nodes.
     */
    private HashMap<String, AudioNode> audioNodes;

    /**
     * Instantiates a new audio manager.
     */
    private AudioManager() {
        application = SolarWarsApplication.getInstance();
        audioNodes = new HashMap<String, AudioNode>();
    }

    /**
     * Gets the single instance of AudioManager.
     *
     * @return single instance of AudioManager
     */
    public static AudioManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new AudioManager();
    }

    /**
     * Initializes the.
     */
    public void initialize() {
        final AssetManager assetManager = application.getAssetManager();

        AudioNode beep = new AudioNode(
                assetManager, SOUND_CLICK, false);
        beep.setVolume(0.25f);
        AudioNode click = new AudioNode(
                assetManager, SOUND_BEEP, false);
        click.setVolume(0.25f);
        AudioNode cyber = new AudioNode(
                assetManager, SOUND_CYBER, false);
        AudioNode error = new AudioNode(
                assetManager, SOUND_ERROR, false);
        error.setVolume(0.25f);
        AudioNode explosion = new AudioNode(
                assetManager, SOUND_EXPLOSION, false);
        AudioNode heli = new AudioNode(
                assetManager, SOUND_HELI, false);
        heli.setLooping(true);
        heli.setVolume(0.25f);

        AudioNode load = new AudioNode(
                assetManager, SOUND_LOAD, false);
        load.setVolume(0.25f);
        AudioNode rocket = new AudioNode(
                assetManager, SOUND_ROCKET, false);

        AudioNode capture = new AudioNode(
                assetManager, SOUND_CAPTURE, false);
        capture.setVolume(0.3f);

        audioNodes.put(SOUND_BEEP, beep);
        audioNodes.put(SOUND_CLICK, click);
        audioNodes.put(SOUND_CYBER, cyber);
        audioNodes.put(SOUND_ERROR, error);
        audioNodes.put(SOUND_EXPLOSION, explosion);
        audioNodes.put(SOUND_HELI, heli);
        audioNodes.put(SOUND_LOAD, load);
        audioNodes.put(SOUND_ROCKET, rocket);
        audioNodes.put(SOUND_CAPTURE, capture);

        for (Map.Entry<String, AudioNode> entry : audioNodes.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().setPositional(false);
                entry.getValue().setDirectional(false);
            }
        }
    }

    /**
     * Gets the audio node.
     *
     * @param name the name
     * @return the audio node
     */
    public AudioNode getAudioNode(String name) {
        return audioNodes.get(name);
    }

    /**
     * Play sound.
     *
     * @param sound the sound
     * @return true, if successful
     */
    public boolean playSound(String sound) {
        if (isSoundDisabled()) {
            return true;
        }
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.play();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Pause sound.
     *
     * @param sound the sound
     * @return true, if successful
     */
    public boolean pauseSound(String sound) {
        if (isSoundDisabled()) {
            return true;
        }
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.pause();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Stop sound.
     *
     * @param sound the sound
     * @return true, if successful
     */
    public boolean stopSound(String sound) {
        if (isSoundDisabled()) {
            return true;
        }
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.stop();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Play sound instance.
     *
     * @param sound the sound
     * @return true, if successful
     */
    public boolean playSoundInstance(String sound) {
        if (isSoundDisabled()) {
            return true;
        }
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.playInstance();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the sound is enabled.
     *
     * @return true, if the sound is enabled
     */
    private boolean isSoundDisabled() {
        return !SolarWarsSettings.getInstance().isSoundEnabled();
    }
}
