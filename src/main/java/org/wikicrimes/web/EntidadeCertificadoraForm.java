package org.wikicrimes.web;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.service.EntidadeCertificadoraService;

public class EntidadeCertificadoraForm extends GenericForm {
	
	
	private String nome;
	private String descricao;
	private String homepage;
	
	private EntidadeCertificadoraService entidadeCertificadoraService;
		

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getHomepage() {
		return homepage;
	}


	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
	
	public String cadastro (){
		if (expirouSessao()) {
			
			return SESSAO_EXPIRADA;
		}
		if(!isAdmin()){
			return SESSAO_EXPIRADA;
		}
		String returnPage = "failure";
		EntidadeCertificadora entCert = new EntidadeCertificadora();
		entCert.setNome(nome);
		entCert.setDescricao(descricao);
		entCert.setHomepage(homepage);
		if(entidadeCertificadoraService.insert(entCert)){
			FacesMessage message = new FacesMessage();			
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setDetail("Entidade Certificadora registrada com sucesso!");
			FacesContext.getCurrentInstance().addMessage("regEntCerForm", message);
		}
		else{
			FacesMessage message = new FacesMessage();			
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setDetail("Problemas no cadastro!");
			FacesContext.getCurrentInstance().addMessage("regEntCerForm", message);
		}		
		returnPage = null;
		

		return returnPage;
	}


	public EntidadeCertificadoraService getEntidadeCertificadoraService() {
		return entidadeCertificadoraService;
	}


	public void setEntidadeCertificadoraService(
			EntidadeCertificadoraService entidadeCertificadoraService) {
		this.entidadeCertificadoraService = entidadeCertificadoraService;
	}
	
		
}