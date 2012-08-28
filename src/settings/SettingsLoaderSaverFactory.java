package settings;


/**
 * 
 * @author Yves
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
	 * Type indicator for jmonkey settings saver and loader
	 */
	public static final String TYPE_JM3 = "JM3";
	
	/**
	 * Type indicator for XML settings saver and loader
	 * 
	 * Notice the type XML isn't yet implemented
	 */
	public static final String TYPE_XML = "XML";
	
	/**
	 * Type indicator for Android settings saver and loader
	 * 
	 * Notice the type Android isn't yet implemented
	 */
	public static final String TYPE_ANDROID = "DROID";
	
	
	public static SettingsLoader getLoader(String type) throws GameSettingsException {
		return (SettingsLoader) loadClass(LOADER_PACKAGE, type , SettingsLoader.class); 
	
	}
	
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
	
	public static void main(String[] args) throws GameSettingsException {
		SettingsLoader jm3Loader = SettingsLoaderSaverFactory.getLoader(SettingsLoaderSaverFactory.TYPE_JM3);
		if(jm3Loader != null) {
			System.out.println(jm3Loader.getClass().getName());
		}
		
	}
	
}
