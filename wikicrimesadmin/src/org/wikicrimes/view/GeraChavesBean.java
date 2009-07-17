package org.wikicrimes.view;

import java.util.List;

import org.wikicrimes.business.GeraChavesBusiness;
import org.wikicrimes.business.impl.GeraChavesBusinessImpl;
import org.wikicrimes.model.ChavesCriptografia;

public class GeraChavesBean {
	private ChavesCriptografia chave;
	private String dominio;
	private List<ChavesCriptografia> consultaChaves;
	private GeraChavesBusiness geraChavesBusiness;
	
	public GeraChavesBean() {
		chave = new ChavesCriptografia(); 
		geraChavesBusiness = new GeraChavesBusinessImpl();
	}
	
	private void limpa(){
		chave = new ChavesCriptografia(); 
		consultaChaves=null;
	}
	
	//gera as chaves, verifica se essas chaves existe, mostra pro user e persiste no bd
	public String gerarChaves(){
		String site=chave.getSite();
		chave = geraChavesBusiness.geraChaves(site);
		
		return "geradorChaves";
	}
	
	public String consultaDominio(){
		consultaChaves=geraChavesBusiness.consultaDominio(dominio);
		return "consultaDominio";
	}
	
	//para o menu direcionar para as duas paginas
	public String irConsultaDominio(){
		limpa(); 
		return "consultaDominio";
	}
	
	public String irGeradorChaves(){
		limpa();
		return "geradorChaves";
	}

	public ChavesCriptografia getChave() {
		return chave;
	}

	public void setChave(ChavesCriptografia chave) {
		this.chave = chave;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public List<ChavesCriptografia> getConsultaChaves() {
		return consultaChaves;
	}

	public void setConsultaChaves(List<ChavesCriptografia> consultaChaves) {
		this.consultaChaves = consultaChaves;
	}

	public GeraChavesBusiness getGeraChavesBusiness() {
		return geraChavesBusiness;
	}

	public void setGeraChavesBusiness(GeraChavesBusiness geraChavesBusiness) {
		this.geraChavesBusiness = geraChavesBusiness;
	}
}