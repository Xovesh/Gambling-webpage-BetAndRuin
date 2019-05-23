package domainV2.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

@Entity
public class Translation {

	// Translation resources
	private Map<Language, String> translationTable = new HashMap<Language, String>();
	private String defaultText;

	public Translation(String defaultText) {
		this.defaultText = defaultText;
	}

	public void setTranslationText(Language lang, String textToSet) {
		translationTable.put(lang, textToSet);
	}

	public void setDefaultText(String def) {
		defaultText = def;
	}

	public String getTranslationText(Language lang) {
		String out = translationTable.get(lang);
		return (out != null) ? out : defaultText;
	}
	
	public String getDefaultText() {
		return defaultText;
	}
}
