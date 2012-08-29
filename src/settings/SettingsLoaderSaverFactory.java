package settings;


/**
 * Provide access for various Loader and Saver types.</b>
 * A client can request a "Load/Saved" by specifying a Type.</b>
 * The supported types are provided as class members. </b>
 * Those class members begin with "TYPE_".</b>
 * </br>
 * You can request your own loader implementations, if you place them into </b>
 * the package settings.loaderImpl and rename your loader implementations</b>
 * so that they starts with her type identification and </b>
 * ends with  "SettingsLoader".
 * </br>
 * It is the same procedure for the saver implementations only that</b>
 * you have to place them into the package "settings.saverImpl"</b>
 * and you have to rename her so that they ends with "SettingsSaver".</b>
 * </b>
 * @author fxdapokalypse
 */
public class SettingsLoaderSaverFactory {
	
	/**
	 * Package which contains the loader implementations.
	 */
	private static final String LOADER_PACKAGE = "settings.loaderImpl";
	/**
	 * Package which contains the saver implementations.
	 */
	private static final String SAVER_PACKAGE = "settings.saverImpl";
	
	/**
	 * Type identification for jmonkey settings saver and loader
	 */
	public static final String TYPE_JM3 = "JM3";
	
	/**
	 * Type identification for XML settings saver and loader
	 * 
	 */
	public static final String TYPE_XML = "XML";
	
	/**
	 * Type identification for Android settings saver and loader
	 * 
	 * Notice the type Android isn't yet implemented
	 */
	public static final String TYPE_ANDROID = "DROID";
	
	
	/**
	 * Requests a SettingsLoader by a specified a type.
	 * 
	 * @param type 
	 * 	SettingsLoaded type identification
	 * @return 
	 * 	the requested SettingsLoader
	 * @throws GameSettingsException 
	 *  If the specified type not exists or 
	 *  the Instantiation of the Loader failed. 
	 */
	public static SettingsLoader getLoader(String type) throws GameSettingsException {
		return (SettingsLoader) loadClass(LOADER_PACKAGE, type , SettingsLoader.class); 
	
	}
	
	/**
	 * Requests a SettingsSaver by a specified a type.
	 * 
	 * @param type 
	 * 	SettingsSaver type identification
	 * @return 
	 * 	the requested SettingsSaver
	 * @throws GameSettingsException
	 *  If the specified type not exists or 
	 *  the Instantiation of the Saver failed. 
	 */
	public static SettingsSaver getSaver(String type) throws GameSettingsException {
		return (SettingsSaver) loadClass(SAVER_PACKAGE, type , SettingsSaver.class);
	}
	

	private static Object loadClass(String packageName, String type, Class<?> interfaceObj) throws GameSettingsException {
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		Object loaderSaverObject = null;
		String className = packageName + "." + type + interfaceObj.getSimpleName();
		try {
			Class<?> implClass = ctxLoader.loadClass(className);
			if(!interfaceObj.isAssignableFrom(implClass)) {
				throw new GameSettingsException(String.format("The requested %s does not implement the interface \"%s\" please correct that." , interfaceObj.getSimpleName(), interfaceObj.getName()));
			}
			loaderSaverObject = implClass.newInstance();
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new GameSettingsException(String.format("The %s type \"%s\" isn't yet supported.", interfaceObj.getSimpleName(), type), ex);
			
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new GameSettingsException(String.format("The requested class %s can't instantiate, please provide a empty public constructor." , className), ex);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new GameSettingsException(String.format("The requested class %s can't instantiate, please provide a empty public constructor." , className), ex);
		}
		return loaderSaverObject;
	}
	
	/**
	 * Simple Test routine.
	 *  
	 * @param args
	 * @throws GameSettingsException
	 */
	public static void main(String[] args) throws GameSettingsException {
		SettingsLoader jm3Loader = SettingsLoaderSaverFactory.getLoader("");
		if(jm3Loader != null) {
			System.out.println(jm3Loader.getClass().getName());
		}
		
	}
	
}
