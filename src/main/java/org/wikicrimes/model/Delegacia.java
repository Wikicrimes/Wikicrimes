package org.wikicrimes.model;

public class Delegacia extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 98667337591203994L;
	private Long idDelegacia;
	private String descricao;
	private double latitude;
	private double longitude;
	private String endereco;
	private String cidade;
	private String estado;
	private String pais;
	private String telefone;
	private Integer tipoDelegacia;
	private String chave;
	
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Integer getTipoDelegacia() {
		return tipoDelegacia;
	}
	public void setTipoDelegacia(Integer tipoDelegacia) {
		this.tipoDelegacia = tipoDelegacia;
	}
	public Long getIdDelegacia() {
		return idDelegacia;
	}
	public void setIdDelegacia(Long idDelegacia) {
		this.idDelegacia = idDelegacia;
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
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
}
