package org.wikicrimes.model;

import java.util.Set;

public class AreaObservacao extends BaseObject {

	private static final long serialVersionUID = -8497302325093089638L;
	private Integer idAreaObservacao;	
	private Usuario usuario;	
	private Set<PontosArea> pontos;
	private PeriodoInformacao periodoInformacao;
	public PeriodoInformacao getPeriodoInformacao() {
		return periodoInformacao;
	}
	public void setPeriodoInformacao(PeriodoInformacao periodoInformacao) {
		this.periodoInformacao = periodoInformacao;
	}
	public Integer getIdAreaObservacao() {
		return idAreaObservacao;
	}
	public void setIdAreaObservacao(Integer idAreaObservacao) {
		this.idAreaObservacao = idAreaObservacao;
	}	
	public Set<PontosArea> getPontos() {
		return pontos;
	}
	public void setPontos(Set<PontosArea> pontos) {
		this.pontos = pontos;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
