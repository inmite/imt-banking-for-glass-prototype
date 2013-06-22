package eu.inmite.glass.banking.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class TemplateReplace {

	public static String templateReplace(String originalString, Map<String, String> variables) {
		String newString = originalString;
		for (String key: variables.keySet()) {
			newString = newString.replaceAll("\\$\\{" + key.toUpperCase() + "\\}", variables.get(key));
		}
		return newString;
	}
	
	public static String getHTMLTemplateText(String resource) throws Exception {
		URL website = Object.class.getResource("/eu/inmite/glass/banking/resources/" + resource + ".html");
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
	
}
