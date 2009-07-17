package org.wikicrimes.model;

import java.util.Date;

public class Comentario extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252038181769820706L;

	private Long idComentario;
	private String comentario;
	private Date dataConfirmacao;
	private Usuario usuario;
	private String link;
	private Crime crime;
	private String embedLink;

	public String getEmbedLink() {
		return embedLink;
	}

	public void setEmbedLink(String embedLink) {
		this.embedLink = embedLink;
	}

	public Comentario() {}
	
	public Comentario(Long idComentario) {
		this.setIdComentario(idComentario);
	}
	
	public Long getIdComentario() {
		return idComentario;
	}

	public void setIdComentario(Long idComentario) {
		this.idComentario = idComentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
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
		
		if (!(obj instanceof Comentario)) {
			return false;
		} else {
			Comentario comentario = (Comentario) obj;
			return comentario.getIdComentario().equals( this.getIdComentario() );
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
	
}
