/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.audio.AudioNode;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import solarwars.AudioManager;
import solarwars.SolarWarsApplication;

/**
 *
 * @author Hans
 */
public class BeatBox {

    private Timer globalTimer;
    private ArrayList<Beat> timerTasks;
    private ArrayList<AudioNode> soundSamples;
    private AudioManager audioManager;
    private SolarWarsApplication application;
    private AudioNode baseSound;

    public BeatBox() {
        application = SolarWarsApplication.getInstance();
        audioManager = AudioManager.getInstance();
        timerTasks = new ArrayList<Beat>();
        soundSamples = new ArrayList<AudioNode>();
        globalTimer = new Timer("BeatBoxTimer", true);

    }

    public void setupSounds() {

        baseSound = audioManager.getAudioNode(AudioManager.SOUND_HELI);
        baseSound.setLooping(true);
        baseSound.setVolume(0.2f);
        baseSound.setPitch(1.25f);

        AudioNode baselineSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_CLICK);

        baselineSound.setVolume(0.15f);
        baselineSound.setPositional(false);
        baselineSound.setDirectional(false);
        baselineSound.setPitch(0.9f);

        Beat baselineBeat = new Beat(baselineSound, 400, 200) {

            private int kick = 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == 4) {
                                    sound.setPitch(0.5f);
                                    sound.playInstance();
                                    kick = 0;
                                } else {
                                    sound.setPitch(0.6f);
                                    sound.playInstance();
                                }
                                kick++;
                                return null;
                            }
                        });
            }
        };

        AudioNode secondLineSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_BEEP);

        secondLineSound.setVolume(0.25f);
        secondLineSound.setPositional(false);
        secondLineSound.setDirectional(false);
        secondLineSound.setPitch(0.8f);

        Beat secondLineBeat = new Beat(secondLineSound, 600, 200) {

            private int kick = 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == 5) {
                                    sound.setPitch(0.5f);
                                    sound.playInstance();
                                    
                                } else if (kick == 6) {
                                    sound.setPitch(0.7f);
                                    sound.playInstance();
                                }else if (kick == 7) {
                                    sound.setPitch(0.6f);
                                    sound.playInstance();
                                    kick = 0;
                                } else {
                                    sound.setPitch(0.8f);
                                    sound.playInstance();
                                }
                                kick++;
                                return null;
                            }
                        });
            }
        };

        timerTasks.add(secondLineBeat);
        timerTasks.add(baselineBeat);
    }

    public void play() {
        baseSound.play();

        for (Beat beat : timerTasks) {
            globalTimer.schedule(beat, beat.layback, beat.period);
        }
    }

    public void stop() {
        baseSound.stop();
        for (Beat beat : timerTasks) {
            beat.cancel();
        }
    }

    private abstract class Beat extends TimerTask {

        public AudioNode sound;
        public int period;
        public int layback;

        public Beat(AudioNode sound, int period, int layback) {
            this.period = period;
            this.layback = layback;
            this.sound = sound;
        }
    }
}
