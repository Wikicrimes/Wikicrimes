package org.wikicrimes.model;

public class TipoRegistro extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1649533906191866272L;

	private Long idTipoRegistro;
	private String nome;
	private String descricao;

	public TipoRegistro() {}
	
	public TipoRegistro(Long idTipoRegistro) {
		this.idTipoRegistro = idTipoRegistro;
	}

	public Long getIdTipoRegistro() {
		return idTipoRegistro;
	}

	public void setIdTipoRegistro(Long idTipoRegistro) {
		this.idTipoRegistro = idTipoRegistro;
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
		
		if (!(obj instanceof TipoRegistro)) {
			return false;
		} else {
			TipoRegistro tipoRegistro = (TipoRegistro) obj;
			return tipoRegistro.getIdTipoRegistro().equals( this.getIdTipoRegistro() );
		}

	}

	
}
