package settings;

import java.io.File;
import java.io.OutputStream;

public interface SettingsSaver
{	
	public void setParam(String parameter, String value);
	public void getParam(String parameter);
	
	void save(GameSettings settings, OutputStream out) throws GameSettingsException;
	void save(GameSettings settings, File configurationFile) throws GameSettingsException;
	void save(GameSettings settings) throws GameSettingsException;
	
}


