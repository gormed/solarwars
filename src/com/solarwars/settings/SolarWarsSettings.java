package com.solarwars.settings;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;

import com.jme3.system.AppSettings;

/**
 * 
 * @author fxdapokalyse
 */
public class SolarWarsSettings extends  GameSettings {
	
	 /**
	  * Instance holder for a SolarWarsSettings Singleton
	  */
	 protected static SolarWarsSettings instance = null;
	 
	 /**
     * Gets the single instance of SolarWarsSettings.
     *
     * @return single instance of SolarWarsSettings
     */
	 public static SolarWarsSettings getInstance() {
		 if(SolarWarsSettings.instance == null) {
			SolarWarsSettings.instance =  new SolarWarsSettings();
		 }
		return SolarWarsSettings.instance;
	 }
	
	/**
	 * List of supported network property names for convenience use.
	 * 
	 * @author fxdapokalyse
	 */
	public static final class Network {
		/**
		 * Default Port for the Client and Server
		 */
		public static final String PORT = "network.port";
		/**
		 * IP Adress of your soloarwars client
		 */
		public static final String ID_ADDRESS = "network.ip";
		/**
		 * Default Server address for multiplayer games
		 */
		public static final String FAVOURITE_SERVER = "network.favourite_server";
		/**
		 * The count of possible players for multiplayer game
		 */
		public static final String Max_Player_Number = "network.maxPlayerNumber";
	}
	/**
	 * List of supported graphic property names for convenience use.
	 * 
	 * @author fxdapokalyse
	 */
	public static final class Graphics {
		/**
		 *  dimension width of window in px
		 */
		public static final String DIMENSION_WIDTH = "Width";
		/**
		 *  dimension height of window in px
		 */
		public static final String DIMENSION_HEIGHT = "Height";
		/**
		 * 
		 */
		public static final String BITS_PER_PIXEL = "BitsPerPixel";
		/**
		 * 
		 */
		public static final String FREQUENCY = "Frequency";
		/**
		 * 
		 */
		public static final String DEPTH_BITS = "DepthBits";
		public static final String STENCIL_BITS = "StencilBits";
		public static final String SAMPLES = "Samples";
		public static final String FULLSCREEN = "Fullscreen";
		public static final String RENDERER = "Renderer";
		public static final String VSYNC = "VSync";
		public static final String FRAMERATE = "FrameRate";
		public static final String TOON_SHADER = "TOON_SHADER";
		public static final String BLOOM_ENABLED = "BLOOM_ENABLED";
		public static final String ENABLE_VERTEX_ARRAYS = "USE_VA";
		public static final String DEBUG_RAYCASTS = "DEBUG_RAYCASTS";
	}
	/**
	 * List of supported Control property names for convenience use.
	 * 
	 * @author fxdapokalyse
	 */
	public static final class Controls {
		public static final String USE_INPUT = "UseInput";
		public static final String DISABLE_JOYSTICKS = "DisableJoysticks";
	}
	
	/**
	 * List of supported Audio property names for convenience use.
	 * 
	 * @author fxdapokalyse
	 */
	public static final class Audio {
		public static final String AUDIO_RENDERER = "AudioRenderer";
		public static final String SOUNDS_ENABLED = "audio.muteSounds";
		public static final String MUSIC_ENABLED  = "audio.muteMusic";
	}
	
	/**
	 * List of supported Game property names for convenience use.
	 * 
	 * @author fxdapokalyse
	 */
	public static final class Game {
		public static final String SETTINGS_DIALOG_IMAGE = "SettingsDialogImage";
		public static final String Title = "Title";
		public static final String ENABLE_LOG_FILES = "game.useLogFiles";
		public static final String LOG_LEVEL = "game.globalLogLevel";
		public static final String PLAYER_NAME = "game.playerName";
		public static final String FAVOURITE_SEED = "game.seed";
		
	}
	
	public SolarWarsSettings() {
		this(true, true);
	}

	public SolarWarsSettings(boolean loadDefaults, boolean loadFromDataSource) {
		super();
		this.setLoaderSaverType(SettingsLoaderSaverFactory.TYPE_XML);
		initialize(loadDefaults, loadFromDataSource);
	}
	
	
	@Override
	protected void defineDefaultSettings(boolean loadDefaults) {
		if(!loadDefaults)
			return;
		
		this.put(Network.ID_ADDRESS, "127.0.0.1");
		this.put(Network.PORT, 6142);
		this.put(Network.FAVOURITE_SERVER, "127.0.0.1");
		this.put(Network.Max_Player_Number, 8);
		
		this.put(Graphics.DIMENSION_WIDTH, 1024);
		this.put(Graphics.DIMENSION_HEIGHT, 768);
		this.put(Graphics.BITS_PER_PIXEL, 24);
		this.put(Graphics.FREQUENCY, 60);
		this.put(Graphics.DEPTH_BITS, 24);
		this.put(Graphics.STENCIL_BITS, 0);
		this.put(Graphics.SAMPLES, 4);
		this.put(Graphics.FULLSCREEN, false);
		this.put(Graphics.RENDERER, AppSettings.LWJGL_OPENGL2);
		this.put(Graphics.VSYNC, true);
        this.put(Graphics.FRAMERATE, 100);
        this.put(Graphics.ENABLE_VERTEX_ARRAYS, false);
        this.put(Graphics.TOON_SHADER, true);
        this.put(Graphics.BLOOM_ENABLED,true);
        this.put(Graphics.DEBUG_RAYCASTS, false);
		
		this.put(Audio.AUDIO_RENDERER, AppSettings.LWJGL_OPENAL);
		this.put(Audio.SOUNDS_ENABLED, true);
		this.put(Audio.MUSIC_ENABLED, true);
		
		this.put(Controls.DISABLE_JOYSTICKS, true);
		this.put(Controls.USE_INPUT, true);
		
        this.put(Game.SETTINGS_DIALOG_IMAGE, "/Interface/solarwars_v2.png");
        this.put(Game.Title, "SolarWars_");
        this.put(Game.FAVOURITE_SEED, "42");
        this.put(Game.PLAYER_NAME, "Player");   
	}
	/**
	 * 
	 */
	@Override
	public File getDefaulfConfigFile() {
		return new File("gameSettings.cfg");
	}
	/**
	 * Rerieve the current global log level.
	 * 
	 * @return the current global log level.
	 * @author fxdapokalypse
	 * @deprecated 
	 * 
	 */
	public Level getGlobalLogLevel() {
		String level = this.getString(Game.LOG_LEVEL);
		if(level ==  null) {
			level = "INFO";
		}
		return Level.parse(level);
	}
	 
	/**
	 * Set the global log level by a log Level constant which represents the log level.
	 * </br>
	 * @param the Level constant
	 * @author fxdapokalypse 
	 * @deprecated 
	 * 
	 */
	public void setGlobalLogLevel(Level level) {
		this.setGlobalLogLevel(level.getName());
	}
	
	/**
	 * Set the GlobalLogLevel by a string which represents the log level.
	 * </br>
	 * @param level the log level e.g.: ["ALL", "INFO", "ERROR", "OFF", "WARNING"]
	 * @throws IllegalArgumentException if the given log level isn't supported.
	 * @author fxdapokalypse 
	 * @deprecated 
	 */
	public void setGlobalLogLevel(String level) {
		String supportedLevels[] = {"ALL", "INFO", "ERROR", "OFF", "WARNING"};
		if(!Arrays.asList(supportedLevels).contains(level)) {
			throw new IllegalArgumentException("The LogLevel \"" + level + "\" isn't supported." );
		}
		this.put(Game.LOG_LEVEL, level);
	}
	/**
	 * Checked if the bloom shader is turned on. 
	 * </br>
	 * @return true, if the bloom shader is enabled.
	 * @author fxdapokalypse 
	 */
	public boolean isBloomEnabled() {
		return this.getBoolean(Graphics.BLOOM_ENABLED);
	}
	/**
	 * Checked if the toon_shader is turned on. 
	 * </br>
	 * @return true, if the toon shader is enabled.
	 * @author fxdapokalypse 
	 */
	public boolean isToonEnabled() {
		return this.getBoolean(Graphics.TOON_SHADER);
	}
	
	/**
	 * Checks if the log should be written into files.
	 * </br>
	 * @return  true, if "ENABLE_LOG_FILES" is enabled.
	 * @author fxdapokalypse 
	 * @deprecated
	 */
	public boolean isFileLoggingEnabled() {
		return this.getBoolean (Game.ENABLE_LOG_FILES);
	}
	
	/**
	 * Checked if DEBUG_RAYCASTS is turned on. 
	 * @TODO: NEED More information about the Setting "DEBUG_RAYCASTS".
	 * </br>
	 * @return true, if DEBUG_RAYCASTS is enabled.
	 * @author fxdapokalypse 
	 */
	public boolean isDEBUG_RAYCASTSEnabled() {
		return this.getBoolean(Graphics.DEBUG_RAYCASTS);
	}

	public int getDefaultPort() {
		return  this.getInteger(Network.PORT);
	}
	
	
	public void setDefaultPort(int port) {
		if (port < 0 || port > 65535 ) {
			throw new IllegalArgumentException("Invalid port value a port must be between 0 and 65535");
		}
		if (port < 1024 ) {
			throw new IllegalArgumentException("Well known Ports(0-1023) are not acceptable");
		}
		this.put(Network.PORT, port);
	}
	
	public boolean isMusicEnabled() {
		return this.getBoolean(Audio.MUSIC_ENABLED);
	}
	
	public void setMusicEnabled(boolean state) {
		 this.put(Audio.MUSIC_ENABLED, state);
	}

	public boolean isSoundEnabled() {
		return this.getBoolean(Audio.SOUNDS_ENABLED);
	}
	
	public void setSoundEnabled(boolean state) {
		this.putBoolean( Audio.SOUNDS_ENABLED, state );
	}
	
	public void setIpAddressFavouriteServer(String ipAddress) {
		this.putString(Network.FAVOURITE_SERVER, ipAddress);
	}
	
	public String getIpAddressFavouriteServer() {
		return this.getString(Network.FAVOURITE_SERVER);
	}
	
	public String getPlayerName() {
		return this.getString(Game.PLAYER_NAME);
	}
	
	public void setPlayerName(String playerName) {
		this.put(Game.PLAYER_NAME, playerName);
	}
	
	public String getSeed() {
		return this.getString(Game.FAVOURITE_SEED);
	}
	
	public void setSeed(String seed) {
		 this.put( Game.FAVOURITE_SEED , seed);
	}

	public int getMaxPlayerNumber() {
		return this.getInteger(Network.Max_Player_Number);
	}
	
	public void setMaxPlayerNumber(int playerNumber) {
		if(playerNumber <= 0 ) {
			throw new IllegalArgumentException("The player number must greater than 0.");
		}  
		this.put(Network.Max_Player_Number, playerNumber);
	}

	
}
