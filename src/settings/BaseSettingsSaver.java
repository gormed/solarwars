package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for a settings loader which provide
 * a template implementation for a SettingLoader.
 * 
 * A subclass must only provides the method 
 * load(GameSettings settings, OutputStrean out). 
 * 
 * @author fxdapokalyse
 *
 */
public abstract class BaseSettingsSaver implements SettingsSaver {
	
	private static final File DEFAULT_CONFIGURATION  = new File("gameSettings.cfg");
	
	private Map<String, String> parameter = new HashMap<String, String>();
	

	@Override
	public void setParam(String parameter, String value) {
		this.parameter.put(parameter, value);
	}

	@Override
	public void getParam(String parameter) {
		this.parameter.get(parameter);

	}

	@Override
	public GameSettings save(GameSettings settings, File configurationFile)
			throws GameSettingsException {
		FileOutputStream os = null;
		FileLock lock = null;
		try {
			os = new FileOutputStream(configurationFile);
			try {
				lock = os.getChannel().lock();
			} catch (IOException e) {
				e.printStackTrace();
			}
			save(settings, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new GameSettingsException("Configuration file cannot found.", e);
		} finally {
			try {
				if(lock != null) {
					lock.release();
					lock = null;
				}
				if(os != null) {
					os.close();
					os = null;
				}
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		return settings;

	}

	@Override
	public GameSettings save(GameSettings settings) throws GameSettingsException {
		return save(settings, BaseSettingsSaver.DEFAULT_CONFIGURATION);
	}

}
