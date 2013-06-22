package eu.inmite.glass.banking.utils;

import java.util.Map;

public class TemplateReplace {

	public static String templateReplace(String originalString, Map<String, String> variables) {
		String newString = originalString;
		for (String key: variables.keySet()) {
			newString = newString.replaceAll("\\$\\{" + key.toUpperCase() + "\\}", variables.get(key));
		}
		return newString;
	}
	
}
