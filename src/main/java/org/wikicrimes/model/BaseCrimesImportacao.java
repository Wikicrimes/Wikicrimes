package org.wikicrimes.model;

import java.util.Date;

@SuppressWarnings("serial")
public class BaseCrimesImportacao extends BaseObject{

	private Long id;
	
	private String nome;
	
	private String url;
	
	private Date dataUltimaImportacao;
	
	private Date dataHoraRegistro;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDataUltimaImportacao() {
		return dataUltimaImportacao;
	}

	public void setDataUltimaImportacao(Date ultimaImportacao) {
		this.dataUltimaImportacao = ultimaImportacao;
	}

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}
	
}
