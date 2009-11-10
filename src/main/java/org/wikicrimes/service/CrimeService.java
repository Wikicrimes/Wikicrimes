package org.wikicrimes.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoPapel;
import org.wikicrimes.model.TipoRegistro;
import org.wikicrimes.model.TipoTransporte;
import org.wikicrimes.model.TipoVitima;

public interface CrimeService extends GenericCrudService {

	public List<BaseObject> getTipoLocalAll();
	public TipoLocal getTipoLocal(Long id);
	public Crime getCrime(String chave);
	public List<BaseObject> getTipoArmaUsadaAll();
	public TipoArmaUsada getTipoArmaUsada(Long id);
	public List<BaseObject> getTipoRegistroAll();
	public TipoRegistro getTipoRegistro(Long id);
	public List<BaseObject> getTipoPapelAll();
	public TipoPapel getTipoPapel(Long id);
	public List<BaseObject> getTipoCrimeAll();
	public TipoCrime getTipoCrime(Long id);
	public List<BaseObject> getTipoTransporteAll();
	public TipoTransporte getTipoTransporte(Long id);
	public List<BaseObject> getEntidadeCertificadoraAll();
	public EntidadeCertificadora getEntidadeCertificadora(Long id);
	public List<BaseObject> findTipoVitimaByTipoCrime(Long tipoCrime);
	public List<BaseObject> findTipoLocalByTipoVitima(Long tipoVitima);
	public List<BaseObject> getTipoVitimaAll();
	public TipoVitima getTipoVitima(Long id);
	public List<BaseObject> filter(Map parameters);
	public void atualizaContador(Boolean tipo, Crime crime);
	public void atualizaVisualizacoes(Crime crime);
	public void atualizaContadorCometarios(Crime crime);
	public int getQuantidadeCrimesRegistrados();
	public int getQuantidadeCrimesRegistradosAtivos();
	public List<Crime> getByUser(Long idUsuario);
	public List<Crime> getCrimesSemEstatisticas();
	public Integer getQtdCrimesByDateInterval(int tipoCrime, String dataInicio,String dataFim);
	public Integer getQtdCrimesByDateIntervalPais(final int tipoCrime,final String dataInicio, final String dataFim,final String siglaPais);
	public Integer getQtdCrimesByDateIntervalEstado(final int tipoCrime,final String dataInicio, final String dataFim,final String siglaEstado);
	public Integer getQtdCrimesByDateIntervalCidade(final int tipoCrime,final String dataInicio, final String dataFim,final String nomeCidade);
	public void updateCrimesSemChave();
	public List<BaseObject> listarRazoes();
	public boolean insert(BaseObject bo , List<Razao> razoes);
	public List<Crime> pesquisarCrime(Crime crime);
	public List<Crime> getCrimesMaisVistos();
	public List<Crime> getCrimesMaisComentados();
	public List<Crime> getCrimesMaisConfirmados();
	public void update(Crime crime, Set<Confirmacao> confirmacoes, List<Razao> razoes);
	
	public Map<String,Integer> numeroCrimesArea(double latitude, double longitude, double raio,long dataIni, long dataFim);
	
	public boolean realizaAtivacao(String codApp);
	
}
