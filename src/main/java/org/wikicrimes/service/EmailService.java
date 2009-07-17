/**
 * 
 */
package org.wikicrimes.service;

import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.Usuario;

/**
 * @author Marcos de Oliveira
 *
 */
public interface EmailService extends GenericCrudService{
	public void enviarEmail(String nome, String email, String subject, String texto);
	public void enviarEmailRecuperaSenha(Usuario usuario, String locale);
	public void enviarEmailNotificacao(Confirmacao confirmacao, String Locale);
	public void enviarEmailNotificacao(ConfirmacaoRelato confirmacao, String Locale);
	public void enviarEmailNotificacao(Comentario comentario, String Locale);
	public void sendMailConfirmation(Crime crime, String locale);
	public void sendMailConfirmation(Relato relato, String locale);
	public void sendMailConfirmation(Usuario usuario, String locale);
	public void sendMailChanges(final Crime crime, final String locale);
}
