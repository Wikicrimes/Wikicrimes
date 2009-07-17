package org.wikicrimes.view.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

public class WikiCrimesSearchDataConverter extends WikiCrimesDataConverter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value == null || value.equals("")) {
			return null;
		} else {
			return super.getAsObject(context, component, value);
		}
	}

}
