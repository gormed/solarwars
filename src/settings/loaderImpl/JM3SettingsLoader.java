package settings.loaderImpl;

import java.io.IOException;
import java.io.InputStream;

import settings.BaseSettingsLoader;
import settings.GameSettings;
import settings.GameSettingsException;

public class JM3SettingsLoader extends BaseSettingsLoader {
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
