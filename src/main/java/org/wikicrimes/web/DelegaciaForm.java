package org.wikicrimes.web;

import java.util.List;

import org.wikicrimes.model.Delegacia;
import org.wikicrimes.service.DelegaciaService;

public class DelegaciaForm {
	
	private DelegaciaService delegaciaService;
	private String idDelegacia;
	private String chave;
	private Delegacia delegacia;
	private List<Delegacia> delegaciaList;
	private double latitude;
	private double longitude;
	private String descricao;
	private String endereco;
	private String telefone;
	
	public DelegaciaForm(){
		this.delegacia = new Delegacia();
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}



	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public DelegaciaService getDelegaciaService() {
		return delegaciaService;
	}

	public void setDelegaciaService(DelegaciaService delegaciaService) {
		this.delegaciaService = delegaciaService;
	}

	public String getIdDelegacia() {
		return this.idDelegacia;
	}
	
	public void setIdDelegacia(String idDelegacia) {
		this.idDelegacia = idDelegacia;
	}

	public Delegacia getDelegacia() {
		this.delegacia = (Delegacia)delegaciaService.getDelegaciaPorChave(chave);
		return delegacia;
	}

	public void setDelegacia(Delegacia delegacia) {
		this.delegacia = delegacia;
	}

	public List<Delegacia> getDelegaciaList() {
		return delegaciaList;
	}

	public void setDelegaciaList(List<Delegacia> delegaciaList) {
		this.delegaciaList = delegaciaList;
	}
	
}
