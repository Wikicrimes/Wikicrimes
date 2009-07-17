package org.wikicrimes.model;

import java.util.Date;

public class ComentarioRelato extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252038181769820706L;

	private Long idComentario;
	private String descComentario;
	private Date dataComentario;
	private Usuario usuario;
	private String link;
	private Relato relato;
	private String embedLink;
	private Long idComentarioPai;
	private UsuarioRedeSocial usuarioRedeSocial; 
	
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof ComentarioRelato)) {
			return false;
		} else {
			ComentarioRelato comentario = (ComentarioRelato) obj;
			return comentario.getIdComentario().equals( this.getIdComentario() );
		}

	}


	public Long getIdComentario() {
		return idComentario;
	}


	public void setIdComentario(Long idComentario) {
		this.idComentario = idComentario;
	}


	public String getDescComentario() {
		return descComentario;
	}


	public void setDescComentario(String descComentario) {
		this.descComentario = descComentario;
	}


	public Date getDataComentario() {
		return dataComentario;
	}


	public void setDataComentario(Date dataComentario) {
		this.dataComentario = dataComentario;
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


	public Relato getRelato() {
		return relato;
	}


	public void setRelato(Relato relato) {
		this.relato = relato;
	}


	public String getEmbedLink() {
		return embedLink;
	}


	public void setEmbedLink(String embedLink) {
		this.embedLink = embedLink;
	}


	public Long getIdComentarioPai() {
		return idComentarioPai;
	}


	public void setIdComentarioPai(Long idComentarioPai) {
		this.idComentarioPai = idComentarioPai;
	}


	public UsuarioRedeSocial getUsuarioRedeSocial() {
		return usuarioRedeSocial;
	}


	public void setUsuarioRedeSocial(UsuarioRedeSocial usuarioRedeSocial) {
		this.usuarioRedeSocial = usuarioRedeSocial;
	}

}
