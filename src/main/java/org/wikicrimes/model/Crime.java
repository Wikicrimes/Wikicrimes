package org.wikicrimes.model;

import java.util.Date;
import java.util.Set;

public class Crime extends BaseObject {

	private static final long serialVersionUID = -8178805938830754193L;

	/**
	 * Constantes
	 */
	public static Long INATIVO = new Long(1);
	public static Long ATIVO = new Long(0);

	public static Long MASCULINO = new Long(1);
	public static Long FEMININO = new Long(0);
	
	/**
	 * Variáveis de Instância
	 */
	private Long idCrime;
	
	private String chave;

	private Set<Confirmacao> confirmacoes;
	
	private Set<CrimeRazao> razoes;
	
	private Double latitude;

	private Double longitude;

	private Long status;
	
	private Long confirmacoesPositivas;
	private Long confirmacoesNegativas;
	
	private Long visualizacoes;
	
	private Long qtdComentarios;
	
	// Dados da ocorrência
	private TipoCrime tipoCrime;

	private TipoRegistro tipoRegistro;

	private TipoLocalRoubo tipoLocalRoubo;
	
	private TipoVitima tipoVitima;

	private TipoLocal tipoLocal;

	private TipoPapel tipoPapel;

	private Usuario usuario;

	private Date data;
	
	private String ip;
	
	private String cep;
	
	private Date dataHoraRegistro;
	
	// Autor
	private Long quantidade;
	
	private Long faixaEtaria;
	
	private TipoArmaUsada tipoArmaUsada;

	private Long sexo;
	
	private TipoTransporte tipoTransporte;

	// Vítimas
	private Long qtdMasculino;

	private Long qtdFeminino;
	
	private String descricao;
	
	private Long horario;
	
	private String endereco;
	
	private String cidade;
	
	private String estado;
	
	private String embedNoticia;
	
	private String linkNoticia;
	
	private String pais;
	
	private String registradoPelaApi;
	
	private UsuarioRedeSocial usuarioRedeSocial;
	
	private Integer identificadorUK;
	
//	TODO Manter lista de Credibilidades para futura atualizacao
//	private Set<Credibilidade> credibilidades;
	
	private Double ultimaCredibilidade;
	

	public UsuarioRedeSocial getUsuarioRedeSocial() {
		return usuarioRedeSocial;
	}

	public void setUsuarioRedeSocial(UsuarioRedeSocial usuarioRedeSocial) {
		this.usuarioRedeSocial = usuarioRedeSocial;
	}

	public Crime(Long idCrime) {
		this.setIdCrime(idCrime);
	}

	public Crime() {
		this.setStatus(ATIVO);
	}

	public Long getIdCrime() {
		return idCrime;
	}

	public void setIdCrime(Long idCrime) {
		this.idCrime = idCrime;
	}

	public Set<Confirmacao> getConfirmacoes() {
		return confirmacoes;
	}

	public void setConfirmacoes(Set<Confirmacao> confirmacoes) {
		this.confirmacoes = confirmacoes;
	}

	public TipoCrime getTipoCrime() {
		return tipoCrime;
	}

	public void setTipoCrime(TipoCrime tipoCrime) {
		this.tipoCrime = tipoCrime;
	}

	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public TipoLocalRoubo getTipoLocalRoubo() {
		return tipoLocalRoubo;
	}

	public void setTipoLocalRoubo(TipoLocalRoubo tipoLocalRoubo) {
		this.tipoLocalRoubo = tipoLocalRoubo;
	}

	public TipoArmaUsada getTipoArmaUsada() {
		return tipoArmaUsada;
	}

	public void setTipoArmaUsada(TipoArmaUsada tipoArmaUsada) {
		this.tipoArmaUsada = tipoArmaUsada;
	}

	public TipoLocal getTipoLocal() {
		return tipoLocal;
	}

	public void setTipoLocal(TipoLocal tipoLocal) {
		this.tipoLocal = tipoLocal;
	}

	public TipoPapel getTipoPapel() {
		return tipoPapel;
	}

	public void setTipoPapel(TipoPapel tipoPapel) {
		this.tipoPapel = tipoPapel;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getSexo() {
		return sexo;
	}

	public void setSexo(Long sexo) {
		this.sexo = sexo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Crime)) {
			return false;
		} else {
			Crime crime = (Crime) obj;
			return crime.getIdCrime().equals(this.getIdCrime());
		}

	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Long getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(Long faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public TipoTransporte getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(TipoTransporte tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public Long getQtdMasculino() {
		return qtdMasculino;
	}

	public void setQtdMasculino(Long qtdMasculino) {
		this.qtdMasculino = qtdMasculino;
	}

	public Long getQtdFeminino() {
		return qtdFeminino;
	}

	public void setQtdFeminino(Long qtdFeminino) {
		this.qtdFeminino = qtdFeminino;
	}

	public Long getHorario() {
	    return horario;
	}

	public void setHorario(Long horario) {
	    this.horario = horario;
	}

	public TipoVitima getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(TipoVitima tipoVitima) {
		this.tipoVitima = tipoVitima;
	}

	public Long getConfirmacoesNegativas() {
		return confirmacoesNegativas;
	}

	public void setConfirmacoesNegativas(Long confirmacoesNegativas) {
		this.confirmacoesNegativas = confirmacoesNegativas;
	}

	public Long getConfirmacoesPositivas() {
		return confirmacoesPositivas;
	}

	public void setConfirmacoesPositivas(Long confirmacoesPositivas) {
		this.confirmacoesPositivas = confirmacoesPositivas;
	}

	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Long getVisualizacoes() {
		return visualizacoes;
	}

	public void setVisualizacoes(Long visualizacoes) {
		this.visualizacoes = visualizacoes;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmbedNoticia() {
		return embedNoticia;
	}

	public void setEmbedNoticia(String embedNoticia) {
		this.embedNoticia = embedNoticia;
	}

	public String getLinkNoticia() {
		return linkNoticia;
	}

	public void setLinkNoticia(String linkNoticia) {
		this.linkNoticia = linkNoticia;
	}

	public Set<CrimeRazao> getRazoes() {
		return razoes;
	}

	public void setRazoes(Set<CrimeRazao> razoes) {
		this.razoes = razoes;
	}

	public Long getQtdComentarios() {
		return qtdComentarios;
	}

	public void setQtdComentarios(Long qtdComentarios) {
		this.qtdComentarios = qtdComentarios;
	}

	public String getRegistradoPelaApi() {
		return registradoPelaApi;
	}

	public void setRegistradoPelaApi(String registradoPelaApi) {
		this.registradoPelaApi = registradoPelaApi;
	}

	// Credibilidade
//	public void setCredibilidades(Set<Credibilidade> credibilidades) {
//		this.credibilidades = credibilidades;
//	}
//	public Set<Credibilidade> getCredibilidades() {
//		return credibilidades;
//	}
//	
//	public Credibilidade getUltimaCredibilidade() {
//		return (Credibilidade)credibilidades.toArray()[credibilidades.size()-1];
//	}

	public Double getUltimaCredibilidade() {
		return ultimaCredibilidade;
	}
	public void setUltimaCredibilidade(Double ultimaCredibilidade) {
		this.ultimaCredibilidade = ultimaCredibilidade;
	}
	
	
	public Integer getIdentificadorUK() {
		return identificadorUK;
	}

	public void setIdentificadorUK(Integer identificadorUK) {
		this.identificadorUK = identificadorUK;
	}

	@Override
	public String toString() {
		return "lat:" + latitude + ", lng:" + longitude;
	}
}


