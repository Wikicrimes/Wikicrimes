package org.wikicrimes.model;


public class TipoConfirmacao extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4399750411381825659L;
	private Long idTipoConfirmacao;
	private String descricao;
	private Boolean positiva;
	public TipoConfirmacao(){}
	
	public TipoConfirmacao (Long idConfirmacao){
		setIdTipoConfirmacao(idConfirmacao);
	}
	
	public Long getIdTipoConfirmacao() {
		return idTipoConfirmacao;
	}

	public void setIdTipoConfirmacao(Long idConfirmacao) {
		this.idTipoConfirmacao = idConfirmacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String comentario) {
		this.descricao = comentario;
	}

	public Boolean getPositiva() {
		return positiva;
	}

	public void setPositiva(Boolean confirma) {
		this.positiva = confirma;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof TipoConfirmacao)) {
			return false;
		} else {
			TipoConfirmacao tipoconfirmacao = (TipoConfirmacao) obj;
			return tipoconfirmacao.getIdTipoConfirmacao().equals( this.getIdTipoConfirmacao() );
		}

	}

	
		
}
