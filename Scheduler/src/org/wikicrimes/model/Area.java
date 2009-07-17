package org.wikicrimes.model;

import java.util.ArrayList;
import java.util.List;

public class Area {
	
	private Long id = new Long(-1);
	private Long periodoInformacao = new Long(1);
	private List<Crime> crimes = new ArrayList<Crime>();
	private List<Ponto> pontos = new ArrayList<Ponto>();
	private String nome; 
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Crime> getCrimes() {
		return crimes;
	}

	public void setCrimes(List<Crime> crimes) {
		this.crimes = crimes;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPeriodoInformacao() {
		return periodoInformacao;
	}
	
	public void setPeriodoInformacao(Long periodoInformacao) {
		this.periodoInformacao = periodoInformacao;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}
	
}
