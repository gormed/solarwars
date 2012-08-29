package com.solarwars.settings.loaderImpl;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.solarwars.settings.BaseSettingsLoader;
import com.solarwars.settings.GameSettings;
import com.solarwars.settings.GameSettingsException;
import com.solarwars.settings.SolarWarsSettings;
import com.solarwars.settings.saverImpl.XMLSettingsSaver;


/**
 * SettingLoader for xml files.
 * 
 * @author fxdapokalypse
 *
 */
public class XMLSettingsLoader extends BaseSettingsLoader {

	public XMLSettingsLoader() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see SettingsLoader
	 */
	@Override
	public GameSettings load(GameSettings settings, InputStream in)
			throws GameSettingsException {
		Document doc = null;
		
		try {
			doc = DocumentBuilderFactory
			.newInstance()
			.newDocumentBuilder()
			.parse(in);
			
			checkDocumentStructure(doc);
			checkDocumentVersion(doc);
			loadSettings(doc, settings);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			if(!(ex instanceof GameSettingsException)) {
				String msg = "An error occurred while the configuration save process is running.";
				throw new GameSettingsException(msg, ex);
			}
		}
		
		return settings;
	}
	
	/**
	 * Execute a check whether the parsed document is in the expected structure.
	 * If the check is failed a Game Setting Exception is threw
	 * 
	 * @param doc
	 * @throws GameSettingsException
	 */
	private void checkDocumentStructure(Document doc) throws GameSettingsException {
		//TODO: XSD validation
	}
	
	/**
	 * Execute a check whether the parsed document is in the expected setting version.
	 * If the check is failed a Game Setting Exception is threw
	 * 
	 * @param doc
	 * @throws GameSettingsException
	 */
	private void checkDocumentVersion(Document doc) throws GameSettingsException {

		Element rootElement = doc.getDocumentElement();
		if( rootElement == null || !XMLSettingsSaver.ELEMENT_SETTINGS.equals(rootElement.getNodeName())) {
			throw new GameSettingsException(String.format("The expected root element \"%s\" is missing.", XMLSettingsSaver.ELEMENT_SETTINGS));
		}
		
		if( !XMLSettingsSaver.XML_SETTINGS_VERSION.equals(readAttribute(rootElement, XMLSettingsSaver.ATTRIBUTE_SETTINGS_VERSION)) ) {
			throw new GameSettingsException(
					String.format("The parsed document is in the wrong version, the expected version is \"%s\".", XMLSettingsSaver.XML_SETTINGS_VERSION)
			);
		}
	}
	
	/**
	 * Retrieve the settings form dom object.
	 * 
	 * @param doc
	 * @param settings
	 * @throws GameSettingsException 
	 */
	private void loadSettings(Document doc, GameSettings settings) throws GameSettingsException {
		String setting = "";
		String value = "";
		String type = "";
		
		NodeList categoryElements = doc.getElementsByTagName(XMLSettingsSaver.ELEMENT_CATEGORY);
		for( int i = 0; i < categoryElements.getLength(); i++ ) {
			Element categoryElement = (Element) categoryElements.item( i );
			String category = readAttribute(categoryElement, XMLSettingsSaver.ATTRIBUTE_CATEGORY_CAPTION);
			
			if( !XMLSettingsSaver.JMONKEY_SETTINGS_CATEGORY.equals( category )) {
				setting  = category + ".";
			} else {
				setting = "";
			}
			
			NodeList settingElements = categoryElement.getElementsByTagName( XMLSettingsSaver.ELEMENT_SETTING );
			for (int j = 0; j < settingElements.getLength(); j++) {
				String settingName = null;
				Element settingElement = (Element) settingElements.item( j );
				settingName = setting + readAttribute(settingElement, XMLSettingsSaver.ATTRIBUTE_SETTING_CAPTION);
				type	 = readAttribute(settingElement, XMLSettingsSaver.ATTRIBUTE_SETTING_TYPE);
				value	 = readAttribute(settingElement, XMLSettingsSaver.ATTRIBUTE_SETTING_VALUE);
				writeToGameSettings( settings , settingName, type, value);
			}
		}
	}
	
	/**
	 * Write a setting into  the GameSettings object.
	 * 
	 * @param settings
	 * @param setting
	 * @param type
	 * @param value
	 * @throws GameSettingsException 
	 */
	private void writeToGameSettings(GameSettings settings, String setting, String type,
			String value) throws GameSettingsException {
		
		if(type.equals(String.class.getSimpleName())) {
			settings.putString(setting, value);
		} else if(type.equals(Integer.class.getSimpleName())) {
			settings.putInteger(setting, Integer.parseInt(value, 10));
		} else if(type.equals(Float.class.getSimpleName())) {
			settings.putFloat(setting, Float.parseFloat(value));
		} else if(type.equals(Boolean.class.getSimpleName())) {
			settings.putBoolean(setting, Boolean.valueOf(value));
		} else {
			throw new GameSettingsException(String.format("The type %s isn't supported yet.", type));
		} 
	}
	
	/**
	 * Convenience helper for accessing the attribute value 
	 * of a element.
	 * 
	 * @param element 
	 * @param name
	 * @return
	 */
	String readAttribute(Element element, String name) {
		String value = element.getAttribute(name);
		if(value != null) {
			value = unescape(value);
		}
		return value;
	}
	
	
	
	/**
	 * Convert xml entities into tokens with special meaning (inside xml documents).
	 * 
	 * @param content 
	 * 	the string which should be unesaped.
	 * @return 
	 * 	the unesaped string
	 */
	private String unescape(String content) {
		return content
				.replaceAll("&amp;", "&")
				.replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">")
				.replaceAll("&apos;", "'")
				.replaceAll("&quot;", "\"");
	}
	
	/**
	 * Simple Test routine.
	 * 
	 * @param args
	 * @throws GameSettingsException
	 */
	public static void main(String[] args) throws GameSettingsException {
		
		SolarWarsSettings settings = new SolarWarsSettings(false, false);
		System.out.println( settings.getPlayerName());
		XMLSettingsLoader loader = new XMLSettingsLoader();
		loader.load(settings, new File("config.xml"));
		System.out.println( settings.getPlayerName());
		
	}

}
