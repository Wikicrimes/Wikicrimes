package org.wikicrimes.model;

public class TipoPapel extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3073618206149286735L;

	private Long idTipoPapel;
	private String nome;
	private String descricao;

	public TipoPapel() {}
	
	public TipoPapel(Long idTipoPapel) {
		this.idTipoPapel = idTipoPapel;
	}

	public Long getIdTipoPapel() {
		return idTipoPapel;
	}

	public void setIdTipoPapel(Long idTipoPapel) {
		this.idTipoPapel = idTipoPapel;
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
		
		if (!(obj instanceof TipoPapel)) {
			return false;
		} else {
			TipoPapel tipoPapel = (TipoPapel) obj;
			return tipoPapel.getIdTipoPapel().equals( this.getIdTipoPapel() );
		}

	}

	
}
