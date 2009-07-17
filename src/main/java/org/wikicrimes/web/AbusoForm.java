/**
 * 
 */
package org.wikicrimes.web;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.wikicrimes.model.Abuso;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.RelatoService;

/**
 * @author Eurico
 *
 */
public class AbusoForm extends GenericForm {

	private EmailService emailService;
	
	private String subject = "";
	
	private String texto = "";
	
	private String nome = "";
	
	private String email = "";
	
	private String idCrime = "";
	
	private String idRelato = "";
	
	private CrimeService crimeService;
	
	private RelatoService relatoService;
	
	
	public CrimeService getCrimeService() {
		return crimeService;
	}

	public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}

	public RelatoService getRelatoService() {
		return relatoService;
	}

	public void setRelatoService(RelatoService relatoService) {
		this.relatoService = relatoService;
	}

	public String getIdRelato() {
		return idRelato;
	}

	public void setIdRelato(String idRelato) {
		this.idRelato = idRelato;
	}

	/**
	 * 
	 */
	public AbusoForm() {
    	Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
        
    	
        if(usuario !=null) {
        	this.email = usuario.getEmail();
        	this.nome = usuario.getNome();
        }
        this.subject = "Denuncia";
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
	
   
    public void setIdCrime(String idCrime){
    	this.idCrime = idCrime;
    }
    
    public String getIdCrime(){
    	return this.idCrime;
    }
	
	public String enviarEmail() {
		Abuso abuso = new Abuso();
		abuso.setDescricao(texto);
		if(this.idCrime != null && !this.idCrime.equalsIgnoreCase("")) {
			this.texto = this.texto +  '\n' + " Identificador do crime denunciado (idCrime) = " + this.idCrime;
			abuso.setCrime((Crime) crimeService.get(Long.parseLong(idCrime)));
		}
		else {
			this.texto = this.texto +  '\n' + " Identificador da denuncia (idRelato) = " + this.idRelato;
			abuso.setRelato((Relato) relatoService.get(Long.parseLong(idRelato)));
		}
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ip = request.getRemoteAddr();
			abuso.setIp(ip);
			abuso.setUsuario((Usuario) this.getSessionScope().get("usuario"));
			abuso.setDataHoraRegistro(new Date());
			emailService.insert(abuso);
			emailService.enviarEmail(this.nome, this.email, this.subject, this.texto);
			addMessage("faleconosco.efetuado.agradecimento", "");
			return "success";
		} catch(Exception e) {
			e.printStackTrace();
			return "failure";
		}
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
}
