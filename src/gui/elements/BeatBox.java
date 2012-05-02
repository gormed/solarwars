/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import com.jme3.audio.AudioNode;
import java.util.ArrayList;
import java.util.Random;
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
    private Random randomizer;
    private long seed;

    public BeatBox() {
        seed = System.currentTimeMillis();
        randomizer = new Random(seed);
        application = SolarWarsApplication.getInstance();
        audioManager = AudioManager.getInstance();
        timerTasks = new ArrayList<Beat>();
        soundSamples = new ArrayList<AudioNode>();
        globalTimer = new Timer("BeatBoxTimer", true);
        System.out.println("Sound seed is: " + seed);

    }

    public BeatBox(long seed) {
        this.seed = seed;
        randomizer = new Random(seed);
        application = SolarWarsApplication.getInstance();
        audioManager = AudioManager.getInstance();
        timerTasks = new ArrayList<Beat>();
        soundSamples = new ArrayList<AudioNode>();
        globalTimer = new Timer("BeatBoxTimer", true);

    }

    public void setupSounds() {

        final int baseKick = 1 + randomizer.nextInt(5);
        final float basePitch = (randomizer.nextFloat() * 0.5f) + 0.5f;
        
        final int tick = 1 + randomizer.nextInt(5);
        System.out.println("Base kick is: " + baseKick);

        baseSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_HELI);
        baseSound.setLooping(true);
        baseSound.setVolume(0.05f);
        baseSound.setPitch(1.5f);

        Beat baseBeat = new Beat(baseSound, tick * 10, 0) {

            private int tick = 1;

            @Override
            public void run() {
                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (tick == baseKick + 3) {
                                    sound.setPitch(basePitch + .5f);
                                } else if (tick == baseKick + 4) {
                                    sound.setPitch(basePitch + .15f);
                                } else if (tick == baseKick + 7) {
                                    sound.setPitch(basePitch + .3f);
                                } else if (tick == baseKick + 8) {
                                    sound.setPitch(basePitch + .2f);
                                } else if (tick >= baseKick + 9) {
                                    sound.setPitch(basePitch + 0.1f);
                                    tick = 0;
                                } else {
                                    sound.setPitch(basePitch);
                                }
                                tick++;
                                return null;
                            }
                        });

            }
        };

        AudioNode baselineSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_CLICK);

        baselineSound.setVolume(0.05f);
        baselineSound.setPositional(false);
        baselineSound.setDirectional(false);
        baselineSound.setPitch(0.6f);

        Beat baselineBeat = new Beat(baselineSound, 400, 200) {

            private int kickTimes = baseKick;
            //private float basePitch = randomizer.nextFloat() + 0.6f;
            private int kick = 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == kickTimes + 1) {
                                    sound.setPitch(basePitch + .5f);
                                    sound.playInstance();

                                } else if (kick == kickTimes + 2) {
                                    sound.setPitch(0.8f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 3) {
                                    sound.setPitch(basePitch + .3f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 5) {
                                    sound.setPitch(basePitch + .2f);
                                    sound.playInstance();
                                } else if (kick >= kickTimes + kickTimes + 2) {
                                    sound.setPitch(basePitch + .5f);
                                    sound.playInstance();
                                    kick = 0;
                                } else {
                                    sound.setPitch(basePitch + .6f);
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

        secondLineSound.setVolume(0.1f);
        secondLineSound.setPositional(false);
        secondLineSound.setDirectional(false);
        secondLineSound.setPitch(0.8f);

        Beat secondLineBeat = new Beat(secondLineSound, 600, 200) {

            private int kick = 1;
            private int kickTimes = baseKick + 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == kickTimes + 1) {
                                    sound.setPitch(basePitch + .5f);
                                    sound.playInstance();

                                } else if (kick == kickTimes + 2) {
                                    sound.setPitch(basePitch + .7f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 3) {
                                    sound.setPitch(basePitch + .6f);
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

        AudioNode thirdLineSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_BEEP);

        thirdLineSound.setVolume(0.075f);
        thirdLineSound.setPositional(false);
        thirdLineSound.setDirectional(false);
        thirdLineSound.setPitch(1.2f);

        Beat thirdLineBeat = new Beat(thirdLineSound, 1200, 600) {

            private int kick = 1;
            private int kickTimes = baseKick + 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == kickTimes + 3) {
                                    sound.setPitch(1.6f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 4) {
                                    sound.setPitch(1.5f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 6) {
                                    sound.setPitch(1.3f);
                                    sound.playInstance();
                                    kick = 0;
                                } else {
                                    sound.setPitch(1.2f);
                                    sound.playInstance();
                                }
                                kick++;
                                return null;
                            }
                        });
            }
        };

        AudioNode fourthLineSound = new AudioNode(
                SolarWarsApplication.getInstance().getAssetManager(),
                AudioManager.SOUND_ERROR);

        fourthLineSound.setVolume(0.01f);
        fourthLineSound.setPositional(false);
        fourthLineSound.setDirectional(false);
        fourthLineSound.setPitch(0.9f);

        Beat fourthLineBeat = new Beat(fourthLineSound, 2*tick*100, 200) {

            private int kick = 1;
            private int kickTimes = baseKick + 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            public Beat call()
                                    throws Exception {
                                if (kick == 1) {
                                    sound.setPitch(0.8f);
                                    sound.playInstance();
                                } else if (kick == 2) {
                                    sound.setPitch(1.2f);
                                    sound.playInstance();
                                } else if (kick == 4) {
                                    sound.setPitch(1.1f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 1) {
                                    sound.setPitch(0.65f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 2) {
                                    sound.setPitch(0.7f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 4) {
                                    sound.setPitch(0.55f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + kickTimes) {
                                    sound.setPitch(0.75f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 7) {
                                    sound.setPitch(0.65f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 10) {
                                    sound.setPitch(0.55f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + kickTimes + kickTimes) {
                                    sound.setPitch(0.5f);
                                    sound.playInstance();
                                } else if (kick >= kickTimes + 13) {
                                    sound.setPitch(0.6f);
                                    sound.playInstance();
                                    kick = 0;
                                } else {
                                    sound.setPitch(1.0f);
                                    sound.playInstance();
                                }
                                kick++;
                                return null;
                            }
                        });
            }
        };

        timerTasks.add(fourthLineBeat);
        timerTasks.add(thirdLineBeat);
        timerTasks.add(secondLineBeat);
        timerTasks.add(baselineBeat);
        timerTasks.add(baseBeat);
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
