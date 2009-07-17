package org.wikicrimes.business.impl;

/**
 * Execao de validacao de PIB.
 * @author F�bio Barros
 */
public class PibInalidoException extends Exception {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8513843675676480854L;

	public PibInalidoException() {
		super("Valor do PIB n�o pode ser menor que a populacao!");
	}
}
