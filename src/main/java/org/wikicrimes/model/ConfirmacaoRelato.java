package org.wikicrimes.model;

import java.util.Date;

public class ConfirmacaoRelato extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252038181769820706L;

	private Long idConfirmacao;
	private String comentario;
	private Boolean confirma;
	private Date dataConfirmacao;
	private Usuario usuario;
	private Usuario usuarioIndicado;
	private UsuarioRedeSocial usuarioRedeSocial;
	private String link;
	private Boolean indicacao;
	private EntidadeCertificadora entidadeCertificadora; 
	private Relato relato;
	private Boolean indicacaoEmail;
	private String ip;
	private String mensagem;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ConfirmacaoRelato() {}
	
	public ConfirmacaoRelato(Long idConfirmacao) {
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof ConfirmacaoRelato)) {
			return false;
		} else {
			ConfirmacaoRelato confirmacao = (ConfirmacaoRelato) obj;
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

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public UsuarioRedeSocial getUsuarioRedeSocial() {
		return usuarioRedeSocial;
	}

	public void setUsuarioRedeSocial(UsuarioRedeSocial usuarioRedeSocial) {
		this.usuarioRedeSocial = usuarioRedeSocial;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Usuario getUsuarioIndicado() {
		return usuarioIndicado;
	}

	public void setUsuarioIndicado(Usuario usuarioIndicado) {
		this.usuarioIndicado = usuarioIndicado;
	}	
}
