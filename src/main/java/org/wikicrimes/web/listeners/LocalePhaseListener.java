package org.wikicrimes.web.listeners;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.Cookie;

import org.wikicrimes.util.CookiesHelper;

public class LocalePhaseListener implements PhaseListener{
	private static final long serialVersionUID = -257630463765246905L;

	public void afterPhase(PhaseEvent event) {
/*		FacesContext context = event.getFacesContext().getCurrentInstance();
		String languageCode = (String) context.getExternalContext().getSessionMap().get("wikicrimes.locale");
		if (languageCode == null){
			Cookie cookie = CookiesHelper.getCookie("wikicrimes.locale");
			if (cookie != null) {
					context.getViewRoot().setLocale(new Locale(cookie.getValue()));
			}
		} 
		else{
			context.getViewRoot().setLocale(new Locale(languageCode));
		}
*/		
	}

	public void beforePhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext().getCurrentInstance();
		String languageCode = (String) context.getExternalContext().getSessionMap().get("wikicrimes.locale");
		if (languageCode == null){
			Cookie cookie = CookiesHelper.getCookie("wikicrimes.locale");
			if (cookie != null) {
					context.getViewRoot().setLocale(new Locale(cookie.getValue()));
			}
		} 
		else{
			context.getViewRoot().setLocale(new Locale(languageCode));
		}
			/*if (!context.getViewRoot().getViewId().equals("/filtro.xhtml") && !context.getViewRoot().getViewId().equals("/mostrarDados.xhtml")){
			Cookie cookie = CookiesHelper.getCookie("wikicrimes.locale");
			if (cookie != null) {
		
				context.getViewRoot().setLocale(new Locale(cookie.getValue()));
			}
		}*/
		
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
