package org.wikicrimes.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class LocaleBean {

	private static final List<SelectItem> LOCALES;

	static {
		LOCALES = new ArrayList<SelectItem>(2);
		LOCALES.add(new SelectItem("pt_BR", "Português"));
		LOCALES.add(new SelectItem("en", "English"));
	}

	private String locale = "pt_BR";

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<SelectItem> getLocaleList() {
		return LOCALES;
	}

	public void changeLocale(ValueChangeEvent event) {
		locale = event.getNewValue().toString();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(locale));
	}
}
