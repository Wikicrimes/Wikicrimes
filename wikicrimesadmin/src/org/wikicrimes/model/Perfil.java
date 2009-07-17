package org.wikicrimes.model;

public class Perfil extends BaseObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idPerfil;
	private String nome;
	private String descricao;
	public static final int USUARIO=1;
	public static final int CONVIDADO=2;
	public static final int ADMINISTRADOR=3;
	public static final int CERTIFICADOR=4;
	public Perfil(){
		
	}
	/*
	 * Construtor com o tipo de perfil
	 * Leo Ayres
	 */
	public Perfil(int tipo){
		this.setIdPerfil(new Long(tipo));
	}
	public Long getIdPerfil() {
		return idPerfil;
	}
	
	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}
	
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
			
			return this.getIdPerfil().equals(obj);
		}

	}
}
