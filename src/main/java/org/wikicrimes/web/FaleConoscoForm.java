/**
 * 
 */
package org.wikicrimes.web;

import org.wikicrimes.service.EmailService;

/**
 * @author Marcos de Oliveira
 *
 */
public class FaleConoscoForm extends GenericForm {

	private EmailService emailService;
	
	private String subject = "";
	
	private String texto = "";
	
	private String nome = "";
	
	private String email = "";
	
	/**
	 * 
	 */
	public FaleConoscoForm() {
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
	
	public String enviarEmail() {
		try {
			emailService.enviarEmail(this.nome, this.email, this.subject, this.texto);
			return "success";
		} catch(Exception e) {
			e.printStackTrace();
			return "failure";
		}
	}
}
