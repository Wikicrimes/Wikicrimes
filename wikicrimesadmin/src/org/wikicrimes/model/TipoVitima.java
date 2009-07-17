package org.wikicrimes.model;

public class TipoVitima extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4928622871959052984L;

	private Long idTipoVitima;
	private String nome;
	private String descricao;

	public TipoVitima() {}
	
	public TipoVitima(Long idTipoVitima) {
		this.idTipoVitima = idTipoVitima;
	}

	public Long getIdTipoVitima() {
		return idTipoVitima;
	}

	public void setIdTipoVitima(Long idTipoVitima) {
		this.idTipoVitima = idTipoVitima;
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
		
		if (!(obj instanceof TipoVitima)) {
			return false;
		} else {
			TipoVitima tipoVitima = (TipoVitima) obj;
			return tipoVitima.getIdTipoVitima().equals( this.getIdTipoVitima() );
		}

	}

	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + (null == idTipoVitima ? 0 : idTipoVitima.hashCode());
		hash = 31 * hash + (null == nome ? 0 : nome.hashCode());
		hash = 31 * hash + (null == descricao ? 0 : descricao.hashCode());
		return hash;
	}

}
