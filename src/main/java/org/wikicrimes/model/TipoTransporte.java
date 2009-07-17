package org.wikicrimes.model;

public class TipoTransporte extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4577013617992813090L;

	private Long idTipoTransporte;
	private String nome;
	private String descricao;

	public TipoTransporte() {}
	
	public TipoTransporte(Long idTipoTransporte) {
		this.idTipoTransporte = idTipoTransporte;
	}

	public Long getIdTipoTransporte() {
		return idTipoTransporte;
	}

	public void setIdTipoTransporte(Long idTipoTransporte) {
		this.idTipoTransporte = idTipoTransporte;
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
		
		if (!(obj instanceof TipoTransporte)) {
			return false;
		} else {
			TipoTransporte tipoPapel = (TipoTransporte) obj;
			return tipoPapel.getIdTipoTransporte().equals( this.getIdTipoTransporte() );
		}

	}

	
}
