package settings;

import java.io.File;
import java.io.InputStream;

public interface SettingsLoader {
	
	public void setParam(String parameter, String value);
	public void getParam(String parameter);
	
	GameSettings load(GameSettings settings, InputStream in) throws GameSettingsException;
	GameSettings load(GameSettings settings, File configurationFile) throws GameSettingsException;
	GameSettings load(GameSettings settings) throws GameSettingsException;
}
