package org.wikicrimes.model;

import java.util.Date;
import java.util.Set;

public class Relato extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1466822619156909914L;

	/**
	 * 
	 */
	

	private Long idRelato;
	
	private String descricao;
	
	private String chave;
	
	private Boolean manha;
	
	private Boolean tarde;
	
	private Boolean noite;
	
	private Boolean madrugada;
	
	private UsuarioRedeSocial usuarioRedeSocial;
	
	private Usuario usuario;
	
	private Double latitude;
	
	private Double longitude;
	
	private String ip;
	
	private String tipoRelato;
	
	private String subTipoRelato;

	private String endereco;
	
	private String cidade;
	
	private String estado;
	
	private String pais;
	
	private String cep;
	
    private Date dataHoraRegistro;	
	
    private Set<ConfirmacaoRelato> confirmacoes;    
    
    private String outraRazao;
    
    private Long qtdConfPositivas;
    
    private Long qtdConfNegativas;
    
    private Set<RelatoRazao> razoes;
    
    public Long getQtdConfPositivas() {
		return qtdConfPositivas;
	}

	public void setQtdConfPositivas(Long qtdConfPositivas) {
		this.qtdConfPositivas = qtdConfPositivas;
	}

	public Long getQtdConfNegativas() {
		return qtdConfNegativas;
	}

	public void setQtdConfNegativas(Long qtdConfNegativas) {
		this.qtdConfNegativas = qtdConfNegativas;
	}
	
	public String getOutraRazao() {
		return outraRazao;
	}

	public void setOutraRazao(String outraRazao) {
		this.outraRazao = outraRazao;
	}

	public String getTipoRelato() {
		return tipoRelato;
	}

	public void setTipoRelato(String tipoRelato) {
		this.tipoRelato = tipoRelato;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getIdRelato() {
		return idRelato;
	}

	public void setIdRelato(Long idRelato) {
		this.idRelato = idRelato;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Boolean getManha() {
		return manha;
	}

	public void setManha(Boolean manha) {
		this.manha = manha;
	}

	public Boolean getTarde() {
		return tarde;
	}

	public void setTarde(Boolean tarde) {
		this.tarde = tarde;
	}

	public Boolean getNoite() {
		return noite;
	}

	public void setNoite(Boolean noite) {
		this.noite = noite;
	}

	public Boolean getMadrugada() {
		return madrugada;
	}

	public void setMadrugada(Boolean madrugada) {
		this.madrugada = madrugada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSubTipoRelato() {
		return subTipoRelato;
	}

	public void setSubTipoRelato(String subTipoRelato) {
		this.subTipoRelato = subTipoRelato;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public UsuarioRedeSocial getUsuarioRedeSocial() {
		return usuarioRedeSocial;
	}

	public void setUsuarioRedeSocial(UsuarioRedeSocial usuarioRedeSocial) {
		this.usuarioRedeSocial = usuarioRedeSocial;
	}

	public Set<ConfirmacaoRelato> getConfirmacoes() {
		return confirmacoes;
	}

	public void setConfirmacoes(Set<ConfirmacaoRelato> confirmacoes) {
		this.confirmacoes = confirmacoes;
	}

	public Set<RelatoRazao> getRazoes() {
		return razoes;
	}

	public void setRazoes(Set<RelatoRazao> razoes) {
		this.razoes = razoes;
	}	
	
	
}
