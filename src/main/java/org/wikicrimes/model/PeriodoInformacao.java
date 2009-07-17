package org.wikicrimes.model;

public class PeriodoInformacao extends BaseObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idPeriodoInformacao;
	private String nome;
	private String descricao;
	
	public Long getIdPeriodoInformacao() {
		return idPeriodoInformacao;
	}
	public void setIdPeriodoInformacao(Long idPeriodoInformacao) {
		this.idPeriodoInformacao = idPeriodoInformacao;
	}
	public PeriodoInformacao(){
		
	}
	/*
	 * Construtor com o tipo de perfil
	 * Leo Ayres
	 */
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Integer)) {
			return false;
		} else {
			
			return this.idPeriodoInformacao.equals(obj);
		}

	}
}
