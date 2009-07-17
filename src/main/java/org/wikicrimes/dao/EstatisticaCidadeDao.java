package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.EstatisticaCidade;
import org.wikicrimes.model.EstatisticaEstado;

public interface EstatisticaCidadeDao extends GenericCrudDao {
	
	public List<EstatisticaCidade> getTopTenCidades();
	public List<EstatisticaCidade> getCidadesDoEstado(Long idEstatisticaEstado);
	public EstatisticaCidade getEstatisticaCidade(Long idEstatisticaCidade);
	public EstatisticaCidade getEstatisticaCidade(String nome);
	public String getNomeCidade(Long idEstatisticaCidade);
	public Long getQtdUsuarios(String nome);
	public Long getQtdCrimes(String nome);
	public Long getQtdRoubos(String nome);
	public Long getQtdRouboPessoa(String nome);
	public Long getQtdRouboPropriedade(String nome);
	public Long getQtdTentativaRouboPessoa(String nome);
	public Long getQtdTentativaRouboPropriedade(String nome);
	public Long getQtdFurto(String nome);
	public Long getQtdFurtoPessoa(String nome);
	public Long getQtdFurtoPropriedade(String nome);
	public Long getQtdTentativaFurtoPessoa(String nome);
	public Long getQtdTentativaFurtoPropriedade(String nome);
	public Long getQtdOutros(String nome);
	public Long getQtdOutrosRixas(String nome);
	public Long getQtdOutrosViolenciaDomestica(String nome);
	public Long getQtdOutrosAbusoAutoridade(String nome);
	public Long getQtdOutrosHomicidio(String nome);
	public Long getQtdOutrosTentativaHomicidio(String nome);
	public Long getQtdOutrosLatrocinio(String nome);
	public Long getQtdTurnoUm(String nome);
	public Long getQtdTurnoDois(String nome);
	public Long getQtdTurnoTres(String nome);
	public Long getQtdTurnoQuatro(String nome);
	public EstatisticaEstado getEstatisticaEstado(String nome);
	public Double getLatitude(String nome);
	public Double getLongitude(String nome);
	public Long getQtdTentativaRoubo(String nome);
	public Long getQtdTentativaFurto(String nome);

}
