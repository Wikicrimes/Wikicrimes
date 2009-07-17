package org.wikicrimes.model;


public class TipoCrime extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016801239119903082L;

	private Long idTipoCrime;
	private String nome;
	private String descricao;

	public TipoCrime() {}
	
	public TipoCrime(Long idTipoCrime) {
		this.idTipoCrime = idTipoCrime;
	}

	public Long getIdTipoCrime() {
		return idTipoCrime;
	}

	public void setIdTipoCrime(Long idTipoCrime) {
		this.idTipoCrime = idTipoCrime;
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
		
		if (!(obj instanceof TipoCrime)) {
			return false;
		} else {
			TipoCrime tipoCrime = (TipoCrime) obj;
			return tipoCrime.getIdTipoCrime().equals( this.getIdTipoCrime() );
		}

	}

	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + (null == idTipoCrime ? 0 : idTipoCrime.hashCode());
		hash = 31 * hash + (null == nome ? 0 : nome.hashCode());
		hash = 31 * hash + (null == descricao ? 0 : descricao.hashCode());
		return hash;
	}
  	
}
