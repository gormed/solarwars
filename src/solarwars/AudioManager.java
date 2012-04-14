/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarwars;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hans
 */
public class AudioManager {

    public static final String SOUND_BEEP = "/Sounds/beep.ogg";
    public static final String SOUND_CLICK = "/Sounds/click.ogg";
    public static final String SOUND_CYBER = "/Sounds/cyber.ogg";
    public static final String SOUND_ERROR = "/Sounds/error.ogg";
    public static final String SOUND_EXPLOSION = "/Sounds/explosion.ogg";
    public static final String SOUND_HELI = "/Sounds/heli.ogg";
    public static final String SOUND_LOAD = "/Sounds/load.ogg";
    public static final String SOUND_ROCKET = "/Sounds/rocket.ogg";
    public static final String SOUND_CAPTURE = "/Sounds/capture.ogg";
    
    private static AudioManager instance;
    private SolarWarsApplication application;
    private HashMap<String, AudioNode> audioNodes;

    private AudioManager() {
        application = SolarWarsApplication.getInstance();
        audioNodes = new HashMap<String, AudioNode>();
    }

    public static AudioManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new AudioManager();
    }

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
    
    public AudioNode getAudioNode(String name) {
        return audioNodes.get(name);
    }

    public boolean playSound(String sound) {
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.play();

            return true;
        } else {
            return false;
        }
    }

    public boolean pauseSound(String sound) {
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.pause();

            return true;
        } else {
            return false;
        }
    }

    public boolean stopSound(String sound) {
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.stop();

            return true;
        } else {
            return false;
        }
    }

    public boolean playSoundInstance(String sound) {
        if (audioNodes.containsKey(sound)) {
            AudioNode n = audioNodes.get(sound);

            n.playInstance();

            return true;
        } else {
            return false;
        }
    }
}
