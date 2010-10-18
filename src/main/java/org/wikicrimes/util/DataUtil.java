package org.wikicrimes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {

	/*
	 * public static ResourceBundle getMessages() { ApplicationFactory factory =
	 * (ApplicationFactory) FactoryFinder
	 * .getFactory(FactoryFinder.APPLICATION_FACTORY); String bundleName =
	 * factory.getApplication().getMessageBundle(); ResourceBundle rb =
	 * ResourceBundle.getBundle(bundleName,
	 * FacesContext.getCurrentInstance().getViewRoot().getLocale()); return rb;
	 * }
	 */

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String formatDate(Date data) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static Date toDate(String data) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return format.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
}

