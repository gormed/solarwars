package settings.loaderImpl;

import java.io.IOException;
import java.io.InputStream;

import settings.BaseSettingsLoader;
import settings.GameSettings;
import settings.GameSettingsException;
import settings.SettingsLoader;

public class JM3SettingsLoader extends BaseSettingsLoader {
	/**
	 * @see SettingsLoader
	 */
	@Override
	public GameSettings load(GameSettings settings, InputStream in)
			throws GameSettingsException {
		try {
			settings.toAppSettings().load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameSettingsException("An error occurred while the configuration load process is running.", e);
		}
		return settings;
	}
}
