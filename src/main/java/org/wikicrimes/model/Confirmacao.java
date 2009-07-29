package org.wikicrimes.model;

import java.util.Date;

public class Confirmacao extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252038181769820706L;

	private Long idConfirmacao;
	private String comentario;
	private Boolean confirma;
	private Date dataConfirmacao;
	private Usuario usuario;
	private Usuario usuarioIndica;
	private String mensagem;
	private UsuarioRedeSocial usuarioRedeSocial;
	private String link;
	private Boolean indicacao;
	private EntidadeCertificadora entidadeCertificadora; 
	private Crime crime;
	private Boolean indicacaoEmail;
	private TipoConfirmacao tipoConfirmacao;
	private String ip;
	private String idicacaoValida;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Confirmacao() {}
	
	public Confirmacao(Long idConfirmacao) {
		this.setIdConfirmacao(idConfirmacao);
	}
	
	public Long getIdConfirmacao() {
		return idConfirmacao;
	}

	public void setIdConfirmacao(Long idConfirmacao) {
		this.idConfirmacao = idConfirmacao;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Boolean getConfirma() {
		return confirma;
	}

	public void setConfirma(Boolean confirma) {
		this.confirma = confirma;
	}

	public Date getDataConfirmacao() {
		return dataConfirmacao;
	}

	public void setDataConfirmacao(Date dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}

	
	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Confirmacao)) {
			return false;
		} else {
			Confirmacao confirmacao = (Confirmacao) obj;
			return confirmacao.getIdConfirmacao().equals( this.getIdConfirmacao() );
		}

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public EntidadeCertificadora getEntidadeCertificadora() {
		return entidadeCertificadora;
	}

	public void setEntidadeCertificadora(EntidadeCertificadora entidadeCertificadora) {
		this.entidadeCertificadora = entidadeCertificadora;
	}

	public Boolean getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(Boolean indicacao) {
		this.indicacao = indicacao;
	}

	public Boolean getIndicacaoEmail() {
		return indicacaoEmail;
	}

	public void setIndicacaoEmail(Boolean indicacaoEmail) {
		this.indicacaoEmail = indicacaoEmail;
	}

	public UsuarioRedeSocial getUsuarioRedeSocial() {
		return usuarioRedeSocial;
	}

	public void setUsuarioRedeSocial(UsuarioRedeSocial usuarioRedeSocial) {
		this.usuarioRedeSocial = usuarioRedeSocial;
	}

	public void setTipoConfirmacao(TipoConfirmacao tipoConfirmacao) {
		this.tipoConfirmacao = tipoConfirmacao;
	}

	public TipoConfirmacao getTipoConfirmacao() {
		return tipoConfirmacao;
	}

	public String getIdicacaoValida() {
		return idicacaoValida;
	}

	public void setIdicacaoValida(String idicacaoValida) {
		this.idicacaoValida = idicacaoValida;
	}

	public Usuario getUsuarioIndica() {
		return usuarioIndica;
	}

	public void setUsuarioIndica(Usuario usuarioIndica) {
		this.usuarioIndica = usuarioIndica;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
