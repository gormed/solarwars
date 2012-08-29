package settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

/**
 * Base GameSettingsClass which provide a interface 
 * to the wrapped  AppSettings and provide routines 
 * for settings loading and saving.
 * 
 * Furthermore can a sub class define a set of default settings. 
 * and can define the url to the desired configuration file. 
 * 
 * @author fxdapokalypse
 *
 */
public abstract class GameSettings implements Map<String, Object>, Cloneable {
	
	private AppSettings settings = null;
	private String settingsLoaderSaverType = SettingsLoaderSaverFactory.TYPE_JM3;
	
	/**
	 * Convert the GameSettings to a Jmonkey AppSettings class.
	 * 
	 * @return 
	 */
	public AppSettings toAppSettings() {
		return this.settings;
	}
	
	/**
	 * Create a template configuration file which contains the default settings.
	 * 
	 * @throws IOException if an io error occurs, while the the configuration file is written.
	 */
	public void createConfigTemplate() throws GameSettingsException {
		GameSettings settingsClone;
		try {
			settingsClone = (GameSettings) this.clone();
			settingsClone.initialize(true, false);
			settingsClone.save();
		} catch (CloneNotSupportedException e) {
			// should not happen
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Define the path to the desired configuration file.
	 * 
	 * @return the path to the configuration file as File object.
	 * 
	 */
	public abstract File getDefaulfConfigFile ();
	
	/**
	 * Get the current save and loader type.
	 * 
	 * @return the string representation of the save and loader type. 
	 * @see SettingsLoaderSaverFactory See for a list of valid Loader- and SaverType.
	 */
	public String getLoaderSaverType() {
		return this.settingsLoaderSaverType;
	}
	/**
	 * Set the current save and loader type.
	 * 
	 * @param type
	 * @see SettingsLoaderSaverFactory See for a list of valid Loader- and SaverType.
	 */
	public void setLoaderSaverType(String type) {
		this.settingsLoaderSaverType = type;
	}
	
	/**
	 * Initialize a singleton instance and load the settings. </br>
	 * Notice if the defined configuration file not exists,
	 * it will be created automatically.
	 * 
	 * @param loadDefaults if true then the default settings will be loaded.
	 * @param loadFromDataSource if true then the settings will be loaded
	 * 							 from a configuration file.
	 */
	protected void initialize (boolean loadDefaults, boolean loadFromDataSource) {
		if (this.settings == null) {
			this.settings = new AppSettings(loadDefaults);
		} 
		this.defineDefaultSettings (loadDefaults);
		
		if(loadFromDataSource) {
			try {
				load();
			} catch (GameSettingsException ex) {
				ex.printStackTrace();
				try {
					this.settings = null;
					initialize(true, false);
					save();
				} catch (GameSettingsException e1) {
					e1.printStackTrace();
				}
				// TODO: Config Exception werfen
			}
		}
	}
	
	/**
	 * Define a set of default settings.
	 * See SolarWarsSettings for a example.
	 * 
	 * @param loadDefaults
	 * @see SolarWarsSettings
	 */
	protected abstract void defineDefaultSettings (boolean loadDefaults);

	protected  SettingsLoader getLoader() throws GameSettingsException {
		return SettingsLoaderSaverFactory.getLoader( this.getLoaderSaverType() );
	}
	
	protected SettingsSaver getSaver() throws GameSettingsException {
		return SettingsLoaderSaverFactory.getSaver( this.getLoaderSaverType() );
	}
	
	//==========================================================================
    //  Map Wrapper
    //==========================================================================
	
	
	

	public void save() throws GameSettingsException {
		save(getDefaulfConfigFile());
	}
	
	public void save(File file) throws GameSettingsException {
		SettingsSaver saver = getSaver();
		saver.save(this, file);
	}
	
	
	public void save (OutputStream out) throws GameSettingsException {
		SettingsSaver saver = getSaver();
		saver.save(this, out);
	}
	
	public void load() throws GameSettingsException {
		load(getDefaulfConfigFile());
	}
	
	public void load(File file) throws GameSettingsException {
		SettingsLoader loader = getLoader();
		loader.load(this, file);
		
	}
	
	public void load (InputStream in) throws GameSettingsException {
		SettingsLoader loader = getLoader();
		loader.load(this, in);
	}
	
	@Override
	public void clear() {
		settings.clear();
		
	}

	@Override
	public boolean containsKey(Object key) {
		return settings.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return settings.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return settings.entrySet();
	}

	@Override
	public Object get(Object key) {
		return settings.get(key);
	}

	@Override
	public boolean isEmpty() {
		return settings.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return settings.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		return settings.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		 settings.putAll(m);
		
	}

	@Override
	public Object remove(Object key) {
		return settings.remove(key);
	}

	@Override
	public int size() {
		return settings.size();
	}

	@Override
	public Collection<Object> values() {
		return settings.values();
	}
	
	//==========================================================================
    //  AppSettings Wrapper
    //==========================================================================
	
	/**
     * Get an integer from the settings.
     * <p>
     * If the key is not set, then 0 is returned.
     */
    public int getInteger(String key) {
       return settings.getInteger(key);
    }

    /**
     * Get a boolean from the settings.
     * <p>
     * If the key is not set, then false is returned.
     */
    public boolean getBoolean(String key) {
        return settings.getBoolean(key);
    }

    /**
     * Get a string from the settings.
     * <p>
     * If the key is not set, then null is returned.
     */
    public String getString(String key) {
        return settings.getString(key);
    }
    
    /**
     * Get a float from the settings.
     * <p>
     * If the key is not set, then 0.0 is returned.
     */
    public float getFloat(String key) {
       return settings.getFloat(key);
    }

    /**
     * Set an integer on the settings.
     */
    public void putInteger(String key, int value) {
        settings.putInteger(key, value);
    }

    /**
     * Set a boolean on the settings.
     */
    public void putBoolean(String key, boolean value) {
        settings.putBoolean(key, value);
    }

    /**
     * Set a string on the settings.
     */
    public void putString(String key, String value) {
        settings.putString(key, value);
    }
    
    /**
     * Set a float on the settings.
     */
    public void putFloat(String key, float value) {
        settings.putFloat(key, value);
    }
    
    /**
     * Enable or disable mouse emulation on touchscreen based devices.
     * This will convert taps on the touchscreen or movement of finger
     * over touchscreen (only the first) into the appropriate mouse events.
     * 
     * @param emulateMouse If mouse emulation should be enabled.
     */
    public void setEmulateMouse(boolean emulateMouse) {
       settings.setEmulateMouse(emulateMouse);
    }
    
    /**
     * Returns true if mouse emulation is enabled, false otherwise.
     * 
     * @return Mouse emulation mode.
     */
    public boolean isEmulateMouse() {
        return settings.isEmulateMouse();
    }
    
    /**
     * Specify if the X or Y (or both) axes should be flipped for emulated mouse.
     * 
     * @param flipX Set to flip X axis
     * @param flipY Set to flip Y axis
     * 
     * @see #setEmulateMouse(boolean) 
     */
    public void setEmulateMouseFlipAxis(boolean flipX, boolean flipY) {
       this.settings.setEmulateMouseFlipAxis(flipX, flipY);
    }
    
    public boolean isEmulateMouseFlipX() {
        return settings.isEmulateMouseFlipX();
    }
    
    public boolean isEmulateMouseFlipY() {
        return settings.isEmulateMouseFlipX();
    }

    /**
     * @param frameRate The frame-rate is the upper limit on how high
     * the application's frames-per-second can go.
     * (Default: -1 no frame rate limit imposed)
     */
    public void setFrameRate(int frameRate) {
        settings.setFrameRate(frameRate);
    }

    /**
     * @param use If true, the application will initialize and use input.
     * Set to false for headless applications that do not require keyboard
     * or mouse input.
     * (Default: true)
     */
    public void setUseInput(boolean use) {
        settings.setUseInput(use);
    }

    /**
     * @param use If true, the application will initialize and use joystick
     * input. Set to false if no joystick input is desired.
     * (Default: false)
     */
    public void setUseJoysticks(boolean use) {
        settings.setUseJoysticks(use);
    }

    /**
     * Set the graphics renderer to use, one of:<br>
     * <ul>
     * <li>AppSettings.LWJGL_OPENGL1 - Force OpenGL1.1 compatability</li>
     * <li>AppSettings.LWJGL_OPENGL2 - Force OpenGL2 compatability</li>
     * <li>AppSettings.LWJGL_OPENGL3 - Force OpenGL3.3 compatability</li>
     * <li>AppSettings.LWJGL_OPENGL_ANY - Choose an appropriate 
     * OpenGL version based on system capabilities</li>
     * <li>null - Disable graphics rendering</li>
     * </ul>
     * @param renderer The renderer to set
     * (Default: AppSettings.LWJGL_OPENGL2)
     */
    public void setRenderer(String renderer) {
        settings.setRenderer(renderer);
    }

    /**
     * Set a custom graphics renderer to use. The class should implement 
     * the {@link JmeContext} interface.
     * @param clazz The custom context class.
     * (Default: not set)
     */
    public void setCustomRenderer(Class<? extends JmeContext> clazz){
        settings.setCustomRenderer(clazz);
    }

    /**
     * Set the audio renderer to use. One of:<br>
     * <ul>
     * <li>AppSettings.LWJGL_OPENAL - Default for LWJGL</li>
     * <li>null - Disable audio</li>
     * </ul>
     * @param audioRenderer 
     * (Default: LWJGL)
     */
    public void setAudioRenderer(String audioRenderer) {
        settings.setAudioRenderer(audioRenderer);
    }

    /**
     * @param value the width for the rendering display.
     * (Default: 640)
     */
    public void setWidth(int value) {
       settings.setWidth(value);
    }

    /**
     * @param value the height for the rendering display.
     * (Default: 480)
     */
    public void setHeight(int value) {
        settings.setHeight(value);
    }

    /**
     * Set the resolution for the rendering display
     * @param width The width
     * @param height The height
     * (Default: 640x480)
     */
    public void setResolution(int width, int height) {
        settings.setResolution(width, height);
    }

    /**
     * Set the frequency, also known as refresh rate, for the 
     * rendering display.
     * @param value The frequency
     * (Default: 60)
     */
    public void setFrequency(int value) {
        settings.setFrequency(value);
    }

    /**
     * Sets the number of depth bits to use.
     * <p>
     * The number of depth bits specifies the precision of the depth buffer.
     * To increase precision, specify 32 bits. To decrease precision, specify
     * 16 bits. On some platforms 24 bits might not be supported, in that case,
     * specify 16 bits.<p>
     * (Default: 24)
     * 
     * @param value The depth bits
     */
    public void setDepthBits(int value){
        settings.setDepthBits(value);
    }
    
    /**
     * Set the number of stencil bits.
     * <p>
     * This value is only relevant when the stencil buffer is being used.
     * Specify 8 to indicate an 8-bit stencil buffer, specify 0 to disable
     * the stencil buffer.
     * </p>
     * (Default: 0)
     * 
     * @param value Number of stencil bits
     */
    public void setStencilBits(int value){
        settings.setStencilBits(value);
    }
    
    /**
     * Set the bits per pixel for the display. Appropriate
     * values are 16 for RGB565 color format, or 24 for RGB8 color format.
     * 
     * @param value The bits per pixel to set
     * (Default: 24)
     */
    public void setBitsPerPixel(int value) {
       settings.setBitsPerPixel(value);
    }

    /**
     * Set the number of samples per pixel. A value of 1 indicates
     * each pixel should be single-sampled, higher values indicate
     * a pixel should be multi-sampled.
     * 
     * @param value The number of samples
     * (Default: 1)
     */
    public void setSamples(int value) {
        settings.setSamples(value);
    }

    /**
     * @param title The title of the rendering display
     * (Default: jMonkeyEngine 3.0)
     */
    public void setTitle(String title) {
       settings.setTitle(title);
    }

    /**
     * @param value true to enable full-screen rendering, false to render in a window
     * (Default: false)
     */
    public void setFullscreen(boolean value) {
        settings.setFullscreen(value);
    }

    /**
     * Set to true to enable vertical-synchronization, limiting and synchronizing
     * every frame rendered to the monitor's refresh rate.
     * @param value 
     * (Default: false)
     */
    public void setVSync(boolean value) {
        settings.setVSync(value);
    }
    
    /**
     * Enable 3D stereo.
     * <p>This feature requires hardware support from the GPU driver. 
     * @see <a href="http://en.wikipedia.org/wiki/Quad_buffering">http://en.wikipedia.org/wiki/Quad_buffering</a><br />
     * Once enabled, filters or scene processors that handle 3D stereo rendering
     * could use this feature to render using hardware 3D stereo.</p>
     * (Default: false)
     */
    public void setStereo3D(boolean value){
        settings.setStereo3D(value);
    }

    /**
     * Sets the application icons to be used, with the most preferred first.
     * For Windows you should supply at least one 16x16 icon and one 32x32. The former is used for the title/task bar,
     * the latter for the alt-tab icon.
     * Linux (and similar platforms) expect one 32x32 icon.
     * Mac OS X should be supplied one 128x128 icon.
     * <br/>
     * The icon is used for the settings window, and the LWJGL render window. Not currently supported for JOGL.
     * Note that a bug in Java 6 (bug ID 6445278, currently hidden but available in Google cache) currently prevents
     * the icon working for alt-tab on the settings dialog in Windows.
     *
     * @param value An array of BufferedImages to use as icons.
     * (Default: not set)
     */
    public void setIcons(Object[] value) {
    	settings.setIcons(value);
    }
    
    /**
     * Sets the path of the settings dialog image to use.
     * <p>
     * The image will be displayed in the settings dialog when the 
     * application is started.
     * </p>
     * (Default: /com/jme3/app/Monkey.png)
     * 
     * @param path The path to the image in the classpath. 
     */
    public void setSettingsDialogImage(String path) {
        settings.setSettingsDialogImage(path);
    }

    /**
     * Get the framerate.
     * @see #setFrameRate(int) 
     */
    public int getFrameRate() {
        return settings.getFrameRate();
    }

    /**
     * Get the use input state.
     * @see #setUseInput(boolean) 
     */
    public boolean useInput() {
       return settings.useInput();
    }

    /**
     * Get the renderer
     * @see #setRenderer(java.lang.String) 
     */
    public String getRenderer() {
       return settings.getRenderer();
    }

    /**
     * Get the width
     * @see #setWidth(int) 
     */
    public int getWidth() {
    	return settings.getWidth();
    }

    /**
     * Get the height
     * @see #setHeight(int) 
     */
    public int getHeight() {
    	return settings.getHeight();
    }

    /**
     * Get the bits per pixel
     * @see #setBitsPerPixel(int) 
     */
    public int getBitsPerPixel() {
    	return settings.getBitsPerPixel();
    }

    /**
     * Get the frequency
     * @see #setFrequency(int) 
     */
    public int getFrequency() {
    	return settings.getFrequency();
    }

    /**
     * Get the number of depth bits
     * @see #setDepthBits(int)
     */
    public int getDepthBits() {
    	return settings.getDepthBits();
    }

    /**
     * Get the number of stencil bits
     * @see #setStencilBits(int) 
     */
    public int getStencilBits() {
    	return settings.getStencilBits();
    }

    /**
     * Get the number of samples
     * @see #setSamples(int) 
     */
    public int getSamples() {
        return settings.getSamples();
    }

    /**
     * Get the application title
     * @see #setTitle(java.lang.String) 
     */
    public String getTitle() {
        return settings.getTitle();
    }

    /**
     * Get the vsync state
     * @see #setVSync(boolean) 
     */
    public boolean isVSync() {
        return settings.isVSync();
    }

    /**
     * Get the fullscreen state
     * @see #setFullscreen(boolean) 
     */
    public boolean isFullscreen() {
    	return settings.isFullscreen();
    }

    /**
     * Get the use joysticks state
     * @see #setUseJoysticks(boolean) 
     */
    public boolean useJoysticks() {
        return settings.useJoysticks();
    }

    /**
     * Get the audio renderer
     * @see #setAudioRenderer(java.lang.String) 
     */
    public String getAudioRenderer() {
        return settings.getAudioRenderer();
    }
    
    /**
     * Get the stereo 3D state
     * @see #setStereo3D(boolean) 
     */
    public boolean useStereo3D(){
        return settings.useStereo3D();  
    }

    /**
     * Get the icon array
     * @see #setIcons(java.lang.Object[]) 
     */
    public Object[] getIcons() {
        return settings.getIcons();
    }
    
    /**
     * Get the settings dialog image
     * @see #setSettingsDialogImage(java.lang.String) 
     */
    public String getSettingsDialogImage() {
       return settings.getSettingsDialogImage();
    }

}
