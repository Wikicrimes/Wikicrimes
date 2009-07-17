package org.wikicrimes.view.listeners;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * PhaseListener de teste.
 * 
 * @author Fábio Barros
 */
public class MyPhaseListener implements PhaseListener {
	private static final long serialVersionUID = 7700487457990644826L;

	/**
	 * Metodo executado antes da fase.
	 */
	public void beforePhase(PhaseEvent event) {
		System.out.println("Antes da fase: " + event.getPhaseId());
	}

	/**
	 * Metodo executado depois da fase.
	 */
	public void afterPhase(PhaseEvent event) {
		System.out.println("Depois da fase: " + event.getPhaseId());
	}

	/**
	 * Metodo que indica qual fase sera "escutada".
	 */
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
