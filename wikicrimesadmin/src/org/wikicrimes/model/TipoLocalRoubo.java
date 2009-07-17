package org.wikicrimes.model;

public class TipoLocalRoubo extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6612955526894456996L;

	private Long idTipoLocalRoubo;
	private String nome;
	private String descricao;

	public TipoLocalRoubo() {}
	
	public TipoLocalRoubo(Long idTipoLocalRoubo) {
		this.idTipoLocalRoubo = idTipoLocalRoubo;
	}

	public Long getIdTipoLocalRoubo() {
		return idTipoLocalRoubo;
	}

	public void setIdTipoLocalRoubo(Long idTipoLocalRoubo) {
		this.idTipoLocalRoubo = idTipoLocalRoubo;
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
		
		if (!(obj instanceof TipoLocalRoubo)) {
			return false;
		} else {
			TipoLocalRoubo tipoLocalRoubo = (TipoLocalRoubo) obj;
			return tipoLocalRoubo.getIdTipoLocalRoubo().equals( this.getIdTipoLocalRoubo() );
		}

	}
	
}
