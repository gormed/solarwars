package settings.saverImpl;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import settings.BaseSettingsSaver;
import settings.GameSettings;
import settings.GameSettingsException;
import settings.SettingsSaver;
import settings.SolarWarsSettings;

import com.jme3.export.xml.DOMSerializer;

/**
 * SettingSaver for xml files.
 * 
 * @author fxdapokalypse
 *
 */
public class XMLSettingsSaver extends BaseSettingsSaver  {
	
	public static final String JMONKEY_SETTINGS_CATEGORY = "jmonkey";
	
	/**
	 * The required XMLSettingsLoader version to load that xml file.
	 */
	public static final String XML_SETTINGS_VERSION = "1.0";
	
	/**
	 * Tag name for the settings element.
	 */
	public static final String ELEMENT_SETTINGS = "settings";
	/**
	 * Attribute name for the required version to load that xml file .
	 */
	public static final String ATTRIBUTE_SETTINGS_VERSION = "version";
	
	/**
	 * Tag name for the categorys element.
	 */
	public static final String ELEMENT_CATEGORYS = "categorys";
	/**
	 * Tag name for the category element.
	 */
	public static final String ELEMENT_CATEGORY = "category";
	/**
	 * Attribute name for the category caption.
	 */
	public static final String ATTRIBUTE_CATEGORY_CAPTION = "name";
	
	/**
	 * Tag name for the setting element.
	 */
	public static final String ELEMENT_SETTING =  "setting";
	/**
	 * Attribute name for the setting caption.
	 */
	public static final String ATTRIBUTE_SETTING_CAPTION = "name";
	/**
	 * Attribute name for the type of a setting value.
	 */
	public static final String ATTRIBUTE_SETTING_TYPE = "type";
	/**
	 * Attribute name for the setting value.
	 */
	public static final String ATTRIBUTE_SETTING_VALUE = "value";
	
	public XMLSettingsSaver() {
		
	}
	/**
	 * @see SettingsSaver
	 */
	@Override
	public GameSettings save(GameSettings settings, OutputStream out)
			throws GameSettingsException {
		HashMap<String, Element> categorys = new HashMap<String, Element>();
		try {
			Document doc = DocumentBuilderFactory
						   .newInstance()
						   .newDocumentBuilder()
						   .newDocument();
			Element settingsRoot = createRootElement(doc);
			Element categorysElement = doc.createElement(ELEMENT_CATEGORYS);
			settingsRoot.appendChild(categorysElement);
			
			List<String> settingNames = new ArrayList<String>(settings.keySet());
			Collections.sort(settingNames);
			
			for(String setting : settingNames) {
				String category  = null;
				Object value = settings.get(setting);
				
				int offsetCategorySeperator = setting.indexOf(".");
				if(offsetCategorySeperator  > 0) {
					// retrieve the category name
					category = setting.substring(0, offsetCategorySeperator);
					setting =  setting.substring(offsetCategorySeperator + 1);
				} else {
					category = JMONKEY_SETTINGS_CATEGORY;
				}
				
				if(!categorys.containsKey(category)) {
					createCategoryElement(categorys, doc, categorysElement, category);	
				}
				createSettingsElement(categorys.get(category) , setting, value, doc);
				
			}
			DOMSerializer serializer = new DOMSerializer();
			serializer.setEncoding("UTF-8");
			serializer.serialize(doc, out);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GameSettingsException("An error occurred while the configuration save process is running.", ex);
		}
		return settings;
	}
	
	/**
	 * Convert tokens with special meaning (inside xml documents) into xml entities.
	 * 
	 * @param content 
	 * 	the string which should be escaped.
	 * @return 
	 * 	the escaped string
	 */
	private String escape(String content) {
		return content
				.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("'", "&apos;")
				.replaceAll("\"", "&quot;");
	}
	/**
	 * Helper method for the creation of the category elements.
	 * 
	 * @param categorys 
	 * @param doc
	 * 	the dom object for which the category element should be created.
	 * @param categorysElement
	 * @param category
	 */
	private void createCategoryElement(HashMap<String, Element> categorys,
			Document doc, Element categorysElement, String category) {
		Element categorieElement = doc.createElement(ELEMENT_CATEGORY);
		categorieElement.setAttribute(ATTRIBUTE_CATEGORY_CAPTION, escape(category));
		categorysElement.appendChild(categorieElement);
		categorys.put(category, categorieElement);
	}
	
	/**
	 * Helper method for the creation of the setting elements.
	 * 
	 * @param categorieElement 
	 * @param settingsName
	 * @param settingValue
	 * @param doc
	 * 	the dom object for which the category element should be created.
	 * @return
	 * 	the setting element.
	 */
	private Element createSettingsElement(Element categorieElement, String settingsName,
			Object settingValue, Document doc ) {
		Element settingsElement = doc.createElement(ELEMENT_SETTING);
		settingsElement.setAttribute(ATTRIBUTE_SETTING_CAPTION, escape(settingsName));
		settingsElement.setAttribute(ATTRIBUTE_SETTING_TYPE,  escape(settingValue.getClass().getSimpleName()));
		settingsElement.setAttribute(ATTRIBUTE_SETTING_VALUE, escape(settingValue.toString()));
		
		categorieElement.appendChild(settingsElement);
		return settingsElement;
		
	}
	
	/**
	 * Helper method for the creation of the root element called ELEMENT_SETTINGS.
	 * 
	 * @param document 
	 * 	the dom object for which the root element should be created.
	 * @return 
	 * 	the root element.
	 */
	private Element createRootElement(Document document) {
		Comment settingsComment = document.createComment("SolarWars GameSettings createt at:" + new Date().toString());
		document.appendChild(settingsComment);
		
		Element root =  document.createElement(XMLSettingsSaver.ELEMENT_SETTINGS);
		root.setAttribute(XMLSettingsSaver.ATTRIBUTE_SETTINGS_VERSION,  XMLSettingsSaver.XML_SETTINGS_VERSION);
		document.appendChild(root);
		
		return root;
	}
	
	/**
	 * Simple Test routine.
	 * 
	 * @param args
	 * @throws GameSettingsException
	 */
	public static void main(String[] args) throws GameSettingsException {
		GameSettings settings = SolarWarsSettings.getInstance();
		XMLSettingsSaver saver = new XMLSettingsSaver();
		saver.save(settings, new File("config.xml"));
	}

}
