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
 * File: BeatBox.java
 * Type: gui.elements.BeatBox
 * 
 * Documentation created: 14.07.2012 - 19:38:02 by Hans Ferchland
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
 * The Class BeatBox.
 *
 * @author Hans
 */
public class BeatBox {

    /** The global timer. */
    private Timer globalTimer;
    /** The timer tasks. */
    private ArrayList<Beat> timerTasks;
    /** The sound samples. */
    private ArrayList<AudioNode> soundSamples;
    /** The audio manager. */
    private AudioManager audioManager;
    /** The application. */
    private SolarWarsApplication application;
    /** The base sound. */
    private AudioNode baseSound;
    /** The randomizer. */
    private Random randomizer;
    /** The seed. */
    private long seed;

    /**
     * Instantiates a new beat box.
     */
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

    /**
     * Instantiates a new beat box.
     *
     * @param seed the seed
     */
    public BeatBox(long seed) {
        this.seed = seed;
        randomizer = new Random(seed);
        application = SolarWarsApplication.getInstance();
        audioManager = AudioManager.getInstance();
        timerTasks = new ArrayList<Beat>();
        soundSamples = new ArrayList<AudioNode>();
        globalTimer = new Timer("BeatBoxTimer", true);

    }

    /**
     * Setup sounds.
     */
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

                            @Override
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

        secondLineSound.setVolume(0.085f);
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

        thirdLineSound.setVolume(0.065f);
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

        fourthLineSound.setVolume(0.03f);
        fourthLineSound.setPositional(false);
        fourthLineSound.setDirectional(false);
        fourthLineSound.setPitch(0.5f);

        Beat fourthLineBeat = new Beat(fourthLineSound, 2 * tick * 100, 200) {

            private int kick = 1;
            private int kickTimes = baseKick + 1;

            @Override
            public void run() {

                application.enqueue(
                        new Callable<Beat>() {

                            @Override
                            public Beat call()
                                    throws Exception {
                                if (kick == 1) {
                                    sound.setPitch(0.8f);
                                    sound.playInstance();
                                } else if (kick == 4) {
                                    sound.setPitch(1.1f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 2) {
                                    sound.setPitch(0.7f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 4) {
                                    sound.setPitch(0.55f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 7) {
                                    sound.setPitch(0.65f);
                                    sound.playInstance();
                                } else if (kick == kickTimes + 10) {
                                    sound.setPitch(0.55f);
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

    /**
     * Play.
     */
    public void play() {
        if (baseSound != null) {
            baseSound.play();
            for (Beat beat : timerTasks) {
                globalTimer.schedule(beat, beat.layback, beat.period);
            }
        }

    }

    /**
     * Stop.
     */
    public void stop() {
        if (baseSound != null) {
            baseSound.stop();
            for (Beat beat : timerTasks) {
                beat.cancel();
            }
            globalTimer.cancel();
            globalTimer = null;
        }
    }

    /**
     * The Class Beat.
     */
    private abstract class Beat extends TimerTask {

        /** The sound. */
        public AudioNode sound;
        /** The period. */
        public int period;
        /** The layback. */
        public int layback;

        /**
         * Instantiates a new beat.
         *
         * @param sound the sound
         * @param period the period
         * @param layback the layback
         */
        public Beat(AudioNode sound, int period, int layback) {
            this.period = period;
            this.layback = layback;
            this.sound = sound;
        }
    }
}
