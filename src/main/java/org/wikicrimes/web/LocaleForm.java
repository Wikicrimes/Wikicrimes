package org.wikicrimes.web;

import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.wikicrimes.util.CookiesHelper;

public class LocaleForm extends GenericForm{

	    public String MudaLocale(){
	        FacesContext context=FacesContext.getCurrentInstance();
	        String languageCode=getLanguageCode(context);
	        context.getViewRoot().setLocale(new Locale(languageCode));
	        Cookie c =CookiesHelper.getCookie("wikicrimes.locale");
	        sessionScope.put("wikicrimes.locale", languageCode);
	        if (c != null) {
	        	if (!c.getValue().equals(languageCode))
	        		CookiesHelper.addCookie("wikicrimes.locale", languageCode, Integer.MAX_VALUE);
	        }
	        else 
	        	CookiesHelper.addCookie("wikicrimes.locale", languageCode, Integer.MAX_VALUE);

	        return null;
	    }

	    private String getLanguageCode(FacesContext context){
	        Map<String,String>params=context.getExternalContext()
	        .getRequestParameterMap();
	        return params.get("languageCode");

	    }
	    public Locale getLocaleAtual() {
	        FacesContext context = FacesContext.getCurrentInstance();
	        UIViewRoot viewRoot = context.getViewRoot();
	        Locale localeAtual = viewRoot.getLocale();
	        return localeAtual;
	    }

	 
	
	/*private static final List<SelectItem> LOCALES;

	static {
		LOCALES = new ArrayList<SelectItem>(2);
		LOCALES.add(new SelectItem("pt_BR", "Portugues"));
		LOCALES.add(new SelectItem("en", "English"));
	}
	private String locale = "pt_BR";

	public String getLocale() {
		return locale;
	}
	public String getBrazil() {
		 FacesContext context = FacesContext.getCurrentInstance();
		    context.getViewRoot().setLocale(Locale.ENGLISH);
		    return null;
	}

	
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<SelectItem> getLocaleList() {
		return LOCALES;
	}

	public void changeLocale(ValueChangeEvent event) {
		locale = event.getNewValue().toString();
		this.getFacesContext().getCurrentInstance().getViewRoot().setLocale(
				new Locale(locale));
	}*/

}
