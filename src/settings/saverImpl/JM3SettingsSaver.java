package settings.saverImpl;

import java.io.IOException;
import java.io.OutputStream;

import settings.BaseSettingsSaver;
import settings.GameSettings;
import settings.GameSettingsException;

public class JM3SettingsSaver extends BaseSettingsSaver {

	@Override
	public void save(GameSettings settings, OutputStream out)
			throws GameSettingsException {
		
		try {
			settings.toAppSettings().save(out);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GameSettingsException("An error occurred while the configuration save process is running.", e);
		}
		
	}
	
}
