package org.wikicrimes.dao;

import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;

public interface CrimeDao extends GenericCrudDao {

	public List<BaseObject> filter(Map parameters);
	public List<Crime> getByUser(Long idUsuario);
	public void incrementaContador(Boolean tipo,Long idCrime);
	public void incrementaView(Long idCrime);
	public Integer getQTDCrimesAtivos();
	public List<Crime> getCrimesSemEstatisticas();
	public Integer getQtdCrimesByDateInterval(int tipoCrime, String dataInicio, String dataFim);
	public Integer getQtdCrimesByDateIntervalPais(final int tipoCrime, final String dataInicio, final String dataFim, final String siglaPais);
	public Integer getQtdCrimesByDateIntervalEstado(final int tipoCrime, final String dataInicio, final String dataFim, final String siglaEstado);
	public Integer getQtdCrimesByDateIntervalCidade(final int tipoCrime, final String dataInicio, final String dataFim, final String nomeCidade);
	public List<Crime> getCrimesSemChave();
	public List<Crime> getCrimesByViewPort(final double norte, final double sul, final double leste, final double oeste);
	public List<Crime> pesquisarCrime(Crime crime);
	public List<Crime> getCrimesMaisVistos();
	public List<Crime> getCrimesMaisComentados();
	public List<Crime> getCrimesMaisConfirmados();
	public void atualizaContadorCometarios(Long idCrime);
	public Map<String,Integer> contaCrimesArea(double latitude, double longitude, double raio,long dataIni, long dataFim);
	
	public boolean realizaAtivacao(String codApp);
}
