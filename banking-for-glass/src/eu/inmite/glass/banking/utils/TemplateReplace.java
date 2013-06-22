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
		if (resource.equals("balance")) {
			return "<article>" +
					"<div style=\"padding: 25px 40px 23px 30px; background-color: #333; text-align: right; border: 1px solid #555; color: #2A94FE\">" +
					"<p style=\"float: right; width: 480px;\">${BANK_ACCOUNT}</p>" +
					"<div style=\"clear: both; height: 0;\"></div>" +
					"</div>" +
					"<div style=\"padding: 60px 40px 60px 20px; text-align: right; font-size: 88px\">${BALANCE} ${CURRENCY}</div>" +
					"</article>";
		} else {
			return "<article>" +
					"<div style=\"float: left; height: 360px; width: 22px; background-color: ${COLOR};\"></div>" +
					"<section>" +
					"<div class=\"text-x-large\" style=\"\">" +
					"<p style=\"color: ${COLOR}\">${AMOUNT}<sub>${CURRENCY}</sub></p>" +
					"<p style=\"font-size: 48px\">${MESSAGE}</p>" +
					"</div>" +
					"</section>" +
					"<footer>" +
					"<div>${DATE}</div>" +
					"</footer>" +
					"</article>";
		}
    }
	
}
