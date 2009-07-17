package org.wikicrimes.model;

public class EntidadeCertificadora extends BaseObject {

	private static final long serialVersionUID = 2421208255172831726L;

	private Long idEntidadeCertificadora;

	private String nome;
	private String descricao;
	private String homepage;

	public EntidadeCertificadora() {
	}

	public EntidadeCertificadora(long idEntidadeCertificadora) {
		this.setIdEntidadeCertificadora(idEntidadeCertificadora);
	}

	public Long getIdEntidadeCertificadora() {
		return idEntidadeCertificadora;
	}

	public void setIdEntidadeCertificadora(Long idEntidadeCertificadora) {
		this.idEntidadeCertificadora = idEntidadeCertificadora;
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

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public boolean equals(Object obj) {

		if (!(obj instanceof EntidadeCertificadora)) {
			return false;
		} else {
			EntidadeCertificadora entidadeCertificadora = (EntidadeCertificadora) obj;
			return entidadeCertificadora.getIdEntidadeCertificadora().equals(
					this.getIdEntidadeCertificadora());
		}

	}

	public String toString() {
		String saida = "";
		saida += "***EntidadeCertificadora***\nid: " + idEntidadeCertificadora
				+ "\nnome: " + nome + "\ndescricao: " + descricao
				+ "\nhomepage: " + homepage;
		return saida;
	}

}