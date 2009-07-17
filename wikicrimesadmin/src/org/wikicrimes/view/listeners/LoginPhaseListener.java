package org.wikicrimes.view.listeners;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.wikicrimes.view.UsuarioBean;

/**
 * PhaseListener para controle de autenticacao.
 * 
 * @author Fábio Barros
 */
public class LoginPhaseListener implements PhaseListener {
	/** Serial Version UID. */
	private static final long serialVersionUID = 8403402696846718508L;

	/**
	 * Verifica se o usuario esta logado no sistema.
	 * 
	 * @param event
	 *            {@link PhaseEvent}
	 * @see PhaseListener#beforePhase(PhaseEvent)
	 */
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		String viewId = context.getViewRoot().getViewId();

		UsuarioBean usuarioBean = (UsuarioBean) context.getApplication()
				.evaluateExpressionGet(context, "#{usuarioBean}",
						UsuarioBean.class);

		if (viewId.equals("/logout.jspx") || viewId.equals("/login.jspx")) {
			ExternalContext externalContext = context.getExternalContext();
			HttpSession httpSession = (HttpSession) externalContext
					.getSession(false);
			httpSession.invalidate();
		} else if (usuarioBean.getUsuarioLogado() == null) {
			navigateToView(context, "login");
		}
	}

	/**
	 * Navega para um view.
	 * 
	 * @param context
	 *            Faces Context atrelado a requisição atual
	 * @param view
	 *            outcome para qual se deseja navegar
	 */
	private void navigateToView(FacesContext context, String view) {
		Application application = context.getApplication();
		NavigationHandler navigationHandler = application
				.getNavigationHandler();
		navigationHandler.handleNavigation(context, null, view);
	}

	/**
	 * Metodo da interface PhaseListener. Nao utilizado.
	 * 
	 * @param event
	 *            {@link PhaseEvent}
	 * @see PhaseListener#beforePhase(PhaseEvent)
	 */
	public void beforePhase(PhaseEvent event) {
		// nop
	}

	/**
	 * A que fase queremos "escutar". No caso {@link PhaseId#RESTORE_VIEW}.
	 * 
	 * @return {@link PhaseId#RESTORE_VIEW}
	 * @see PhaseListener#getPhaseId()
	 */
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}
