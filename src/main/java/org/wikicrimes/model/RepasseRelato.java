package org.wikicrimes.model;

import java.util.Date;

public class RepasseRelato extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6848135825731181218L;

	private Long idRepasseRelato;
	
	private UsuarioRedeSocial usuarioEnvio;
	
	private UsuarioRedeSocial usuarioRecebimento;
	
	private Relato relato;
	
	private Crime crime;
	
	private Date dataHoraRegistro;

	public Long getIdRepasseRelato() {
		return idRepasseRelato;
	}

	public void setIdRepasseRelato(Long idRepasseRelato) {
		this.idRepasseRelato = idRepasseRelato;
	}	

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public UsuarioRedeSocial getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(UsuarioRedeSocial usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public UsuarioRedeSocial getUsuarioRecebimento() {
		return usuarioRecebimento;
	}

	public void setUsuarioRecebimento(UsuarioRedeSocial usuarioRecebimento) {
		this.usuarioRecebimento = usuarioRecebimento;
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}
	
	
}