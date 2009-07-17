package org.wikicrimes.view.listeners;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.wikicrimes.view.LocaleBean;

public class LocalePhaseListener implements PhaseListener {

	private static final long serialVersionUID = -8857025099581792789L;

	public void afterPhase(PhaseEvent event) {
		/* nop */
	}

	public void beforePhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		LocaleBean localeBean = (LocaleBean) context.getApplication()
				.evaluateExpressionGet(context, "#{localeBean}",
						LocaleBean.class);
		context.getViewRoot().setLocale(new Locale(localeBean.getLocale()));
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
