package org.wikicrimes.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.text.ParseException;

public class WikiCrimesDataConverter implements Converter {

	private SimpleDateFormat sdf = null;
	
	public WikiCrimesDataConverter() {
		String pattern = "dd/MM/yyyy";
		sdf = new SimpleDateFormat(pattern);
	}
	
	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {

		Date nDate;
		try {
			nDate = sdf.parse(value);
		} catch (ParseException ex) {
			FacesMessage message = new FacesMessage();
			message.setDetail("Data não é válida");
			message.setSummary("Data não é válida");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);
		}
		return nDate;
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext facesContext,
			UIComponent uIComponent, Object value) throws ConverterException {
		if (value != null) {
			return sdf.format(value);			
		} else {
			return "";
		}
	}

}
