package org.wikicrimes.model;

import java.util.Date;

public class Reputacao extends BaseObject{
	
	private static final long serialVersionUID = 5146469512818705352L;
	
	/** Variables **/
	private Long idReputacao;
	private Double reputacao;
	private Date periodo;
	private Usuario usuario;

	
	
	/** Constructors **/
	public Reputacao() { }
	
	public Reputacao(Long idReputacao) {
		this.idReputacao = idReputacao;
	}
	
	public Reputacao(Long idReputacao, Double reputacao, Date periodo, Usuario usuario) 
	{
		this.idReputacao = idReputacao;
		this.reputacao = reputacao;
		this.usuario = usuario;
		this.periodo = periodo; 
	}
	
	
	
	/** Gets and Sets **/
	public Long getIdReputacao() {
		return idReputacao;
	}
	public void setIdReputacao(Long idReputacao) {
		this.idReputacao = idReputacao;
	}
	
	public Double getReputacao() {
		return reputacao;
	}
	public void setReputacao(Double reputacao) {
		this.reputacao = reputacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public Date getPeriodo() {
		return periodo;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Reputacao)) {
			return false;
		} else {
			Reputacao reputacao = (Reputacao) obj;
			return reputacao.getIdReputacao().equals(idReputacao);
		}

	}

}

