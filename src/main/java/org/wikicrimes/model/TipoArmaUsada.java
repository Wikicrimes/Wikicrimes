package org.wikicrimes.model;

public class TipoArmaUsada extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 70314067775195046L;

	private Long idTipoArmaUsada;
	private String nome;
	private String descricao;

	public TipoArmaUsada() {}
	
	public TipoArmaUsada(Long idTipoArmaUsada) {
		this.idTipoArmaUsada = idTipoArmaUsada;
	}

	public Long getIdTipoArmaUsada() {
		return idTipoArmaUsada;
	}

	public void setIdTipoArmaUsada(Long idTipoArmaUsada) {
		this.idTipoArmaUsada = idTipoArmaUsada;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

  	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof TipoArmaUsada)) {
			return false;
		} else {
			TipoArmaUsada tipoArmaUsada = (TipoArmaUsada) obj;
			return tipoArmaUsada.getIdTipoArmaUsada().equals( this.getIdTipoArmaUsada() );
		}

	}

	
}
