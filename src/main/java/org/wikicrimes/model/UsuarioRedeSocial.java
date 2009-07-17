package org.wikicrimes.model;

import java.util.Date;

public class UsuarioRedeSocial extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5288365278146939204L;
	
	private Long idUsuarioRedeSocial;
	
	private Usuario usuario;
	
	private RedeSocial redeSocial;
	
	private String idUsuarioDentroRedeSocial;
	
	private Integer visualizarCrimes;
	
	private Date dataHoraRegistro;
	
	private String cidade;
	
	private String pais;
	
	private Integer ativarTutor;

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Integer getVisualizarCrimes() {
		return visualizarCrimes;
	}

	public void setVisualizarCrimes(Integer visualizarCrimes) {
		this.visualizarCrimes = visualizarCrimes;
	}

	public Long getIdUsuarioRedeSocial() {
		return idUsuarioRedeSocial;
	}

	public void setIdUsuarioRedeSocial(Long idUsuarioRedeSocial) {
		this.idUsuarioRedeSocial = idUsuarioRedeSocial;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public RedeSocial getRedeSocial() {
		return redeSocial;
	}

	public void setRedeSocial(RedeSocial redeSocial) {
		this.redeSocial = redeSocial;
	}

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public String getIdUsuarioDentroRedeSocial() {
		return idUsuarioDentroRedeSocial;
	}

	public void setIdUsuarioDentroRedeSocial(String idUsuarioDentroRedeSocial) {
		this.idUsuarioDentroRedeSocial = idUsuarioDentroRedeSocial;
	}

	public Integer getAtivarTutor() {
		return ativarTutor;
	}

	public void setAtivarTutor(Integer ativarTutor) {
		this.ativarTutor = ativarTutor;
	}	

}
