package org.wikicrimes.model;

import java.util.Date;
import org.wikicrimes.model.Relato;


public class Abuso extends BaseObject {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4113425568334062583L;
		
	/**
	 * Variáveis de Instância
	 */
	private Long idAbuso;
		
	private Usuario usuario;

	private String ip;
	
	private Date dataHoraRegistro;
	
	private String descricao;
	
	private Crime crime;
	
	private Relato relato;
		
	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public Abuso(Long idAbuso) {
		this.setIdAbuso(idAbuso);
	}

	public Abuso() {
	
	}

	public Long getIdAbuso() {
		return idAbuso;
	}

	public void setIdAbuso(Long idAbuso) {
		this.idAbuso = idAbuso;
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Abuso)) {
			return false;
		} else {
			Abuso ab = (Abuso) obj;
			return ab.getIdAbuso().equals(this.getIdAbuso());
		}

	}
	
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



}
