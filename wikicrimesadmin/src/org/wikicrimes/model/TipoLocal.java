package org.wikicrimes.model;

public class TipoLocal extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4019952798222943365L;

	private Long idTipoLocal;
	private String nome;
	private String descricao;
	private TipoVitima tipoVitima;

	public TipoVitima getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(TipoVitima tipoVitima) {
		this.tipoVitima = tipoVitima;
	}

	public TipoLocal() {}
	
	public TipoLocal(Long id) {
		idTipoLocal = id;
	}

	public Long getIdTipoLocal() {
		return idTipoLocal;
	}

	public void setIdTipoLocal(Long idTipoLocal) {
		this.idTipoLocal = idTipoLocal;
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
		
		if (!(obj instanceof TipoLocal)) {
			return false;
		} else {
			TipoLocal tipoLocal = (TipoLocal) obj;
			return tipoLocal.getIdTipoLocal().equals( this.getIdTipoLocal() );
		}

	}
	
}
