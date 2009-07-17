package org.wikicrimes.model;


public class UsuarioCelular extends BaseObject {
	
	private static final long serialVersionUID = -5500708947043036700L;

	private Long idUsuario;
	
	private String email;
	
	private String telefoneCelular;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

}