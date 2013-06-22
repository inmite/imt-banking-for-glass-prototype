package eu.inmite.glass.banking.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tomas Vondracek
 */
public class FormattingUtils {

	private static final String FIO_REST_DATE = "yyyy-MM-ddZZZZ";

	private static final DateFormat sFioDateFormatter = new SimpleDateFormat(FIO_REST_DATE);

	public static Date parseFioDate(String date) {
		try {
			return sFioDateFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormat.getDateInstance().format(date);
	}
}
