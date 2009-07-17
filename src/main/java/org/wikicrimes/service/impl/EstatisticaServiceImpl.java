/**
 * 
 */
package org.wikicrimes.service.impl;


import java.util.List;

import org.wikicrimes.dao.EstatisticaCidadeDao;
import org.wikicrimes.dao.EstatisticaEstadoDao;
import org.wikicrimes.dao.EstatisticaPaisDao;
import org.wikicrimes.model.EstatisticaCidade;
import org.wikicrimes.model.EstatisticaEstado;
import org.wikicrimes.model.EstatisticaPais;
import org.wikicrimes.service.EstatisticaService;

/**
 * @author Marcos de Oliveira
 *
 */
public class EstatisticaServiceImpl extends GenericCrudServiceImpl implements
		EstatisticaService {
	
	private EstatisticaCidadeDao estatisticaCidadeDao;
	
	private EstatisticaEstadoDao estatisticaEstadoDao;
	
	private EstatisticaPaisDao estatisticaPaisDao;
	

	public List<EstatisticaCidade> getTopTenCidades() {
		return estatisticaCidadeDao.getTopTenCidades();
	}
	
	public List<EstatisticaEstado> getTopFiveEstados() {
		return estatisticaEstadoDao.getTopFiveEstados();
	}
	
	
	public List getAllPais() {
		return estatisticaPaisDao.getAll();
	}
	
	public List getAllEstado() {
		return estatisticaEstadoDao.getAll();
	}
	
	public List getAllCidade() {
		return estatisticaCidadeDao.getAll();
	}
	
	public List<EstatisticaCidade> getCidatesDoEstado(String siglaEstado){
		EstatisticaEstado eE = estatisticaEstadoDao.getEstatisticaEstado(siglaEstado);
		List<EstatisticaCidade> cidades = estatisticaCidadeDao.getCidadesDoEstado(eE.getIdEstatisticaEstado());
		return cidades;
	}
	
	public List<EstatisticaEstado> getEstadosDoPais(String siglaPais) {
		EstatisticaPais eP = estatisticaPaisDao.getEstatisticaPais(siglaPais);
		List<EstatisticaEstado> estados = estatisticaEstadoDao.getEstadosDoPais(eP.getIdEstatisticaPais());
		return estados;
	}
	
	public String getNomePais(String siglaPais) {
		return estatisticaPaisDao.getNomePais(siglaPais);
	}
	
	public String getNomeEstado(String siglaEstado) {
		return estatisticaEstadoDao.getNomeEstado(siglaEstado);
	}

	public Long getEstatisticaEstadoRoubos(String siglaEstado) {
		return estatisticaEstadoDao.getQtdRoubos(siglaEstado);
	}

	
	public EstatisticaCidade getEstatisticaCidade(Long idEstatisticaCidade) {
		return estatisticaCidadeDao.getEstatisticaCidade(idEstatisticaCidade);
	}
	
	public EstatisticaCidade getEstatisticaCidade(String nomeCidade) {
		return estatisticaCidadeDao.getEstatisticaCidade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdCrimes(String nomeCidade) {
		return estatisticaCidadeDao.getQtdCrimes(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdFurto(String nomeCidade) {
		return estatisticaCidadeDao.getQtdFurto(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdFurtoPessoa(String nomeCidade) {
		return estatisticaCidadeDao.getQtdFurtoPessoa(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdFurtoPropriedade(String nomeCidade) {
		return estatisticaCidadeDao.getQtdFurtoPropriedade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutros(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutros(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosAbusoAutoridade(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosAbusoAutoridade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosHomicidio(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosHomicidio(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosLatrocinio(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosLatrocinio(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosRixas(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosRixas(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosTentativaHomicidio(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosTentativaHomicidio(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdOutrosViolenciaDomestica(String nomeCidade) {
		return estatisticaCidadeDao.getQtdOutrosViolenciaDomestica(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdRouboPessoa(String nomeCidade) {
		return estatisticaCidadeDao.getQtdRouboPessoa(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdRouboPropriedade(String nomeCidade) {
		return estatisticaCidadeDao.getQtdRouboPropriedade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdRoubos(String nomeCidade) {
		return estatisticaCidadeDao.getQtdRoubos(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTentativaFurtoPessoa(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTentativaFurtoPessoa(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTentativaFurtoPropriedade(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTentativaFurtoPropriedade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTentativaRouboPessoa(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTentativaRouboPessoa(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTentativaRouboPropriedade(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTentativaRouboPropriedade(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTurnoDois(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTurnoDois(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTurnoQuatro(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTurnoQuatro(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTurnoTres(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTurnoTres(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdTurnoUm(String nomeCidade) {
		return estatisticaCidadeDao.getQtdTurnoUm(nomeCidade);
	}

	public Long getEstatisticaCidadeQtdUsuarios(String nomeCidade) {
		return estatisticaCidadeDao.getQtdUsuarios(nomeCidade);
	}

	public org.wikicrimes.model.EstatisticaEstado getEstatisticaEstadoDaEstatisticaCidade(String nomeCidade) {
		return estatisticaCidadeDao.getEstatisticaEstado(nomeCidade);
	}
	
	public EstatisticaEstado getEstatisticaEstado(String siglaEstado) {
		return estatisticaEstadoDao.getEstatisticaEstado(siglaEstado);
	}

	public org.wikicrimes.model.EstatisticaEstado getEstatisticaEstado(Long idEstatisticaEstado) {
		return estatisticaEstadoDao.getEstatisticaEstado(idEstatisticaEstado);
	}

	public Long getEstatisticaEstadoQtdCrimes(String siglaEstado) {
		return estatisticaEstadoDao.getQtdCrimes(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdFurto(String siglaEstado) {
		return estatisticaEstadoDao.getQtdFurto(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdFurtoPessoa(String siglaEstado) {
		return estatisticaEstadoDao.getQtdFurtoPessoa(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdFurtoPropriedade(String siglaEstado) {
		return estatisticaEstadoDao.getQtdFurtoPropriedade(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutros(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutros(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosAbusoAutoridade(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosAbusoAutoridade(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosHomicidio(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosHomicidio(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosLatrocinio(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosLatrocinio(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosRixas(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosRixas(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosTentativaHomicidio(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosTentativaHomicidio(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdOutrosViolenciaDomestica(String siglaEstado) {
		return estatisticaEstadoDao.getQtdOutrosViolenciaDomestica(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdRouboPessoa(String siglaEstado) {
		return estatisticaEstadoDao.getQtdRouboPessoa(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdRouboPropriedade(String siglaEstado) {
		return estatisticaEstadoDao.getQtdRouboPropriedade(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTentativaFurtoPessoa(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTentativaFurtoPessoa(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTentativaFurtoPropriedade(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTentativaFurtoPropriedade(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTentativaRouboPessoa(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTentativaRouboPessoa(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTentativaRouboPropriedade(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTentativaRouboPropriedade(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTurnoDois(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTurnoDois(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTurnoQuatro(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTurnoQuatro(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTurnoTres(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTurnoTres(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdTurnoUm(String siglaEstado) {
		return estatisticaEstadoDao.getQtdTurnoUm(siglaEstado);
	}

	public Long getEstatisticaEstadoQtdUsuarios(String siglaEstado) {
		return estatisticaEstadoDao.getQtdUsuarios(siglaEstado);
	}

	public EstatisticaPais getEstatisticaPaisDoEstado(String siglaEstado) {
		return estatisticaEstadoDao.getEstatisticaPais(siglaEstado);
	}

	public EstatisticaPais getEstatisticaPais(Long idEstatisticaPais) {
		return estatisticaPaisDao.getEstatisticaPais(idEstatisticaPais);
	}
	
	public EstatisticaPais getEstatisticaPais(String siglaPais) {
		return estatisticaPaisDao.getEstatisticaPais(siglaPais);
	}

	public Long getEstatisticaPaisQtdCrimes(String siglaPais) {
		return estatisticaPaisDao.getQtdCrimes(siglaPais);
	}

	public Long getEstatisticaPaisQtdFurto(String siglaPais) {
		return estatisticaPaisDao.getQtdFurto(siglaPais);
	}

	public Long getEstatisticaPaisQtdFurtoPessoa(String siglaPais) {
		return estatisticaPaisDao.getQtdFurtoPessoa(siglaPais);
	}

	public Long getEstatisticaPaisQtdFurtoPropriedade(String siglaPais) {
		return estatisticaPaisDao.getQtdFurtoPropriedade(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutros(String siglaPais) {
		return estatisticaPaisDao.getQtdOutros(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosAbusoAutoridade(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosAbusoAutoridade(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosHomicidio(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosHomicidio(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosLatrocinio(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosLatrocinio(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosRixas(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosRixas(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosTentativaHomicidio(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosTentativaHomicidio(siglaPais);
	}

	public Long getEstatisticaPaisQtdOutrosViolenciaDomestica(String siglaPais) {
		return estatisticaPaisDao.getQtdOutrosViolenciaDomestica(siglaPais);
	}

	public Long getEstatisticaPaisQtdRouboPessoa(String siglaPais) {
		return estatisticaPaisDao.getQtdRouboPessoa(siglaPais);
	}

	public Long getEstatisticaPaisQtdRouboPropriedade(String siglaPais) {
		return estatisticaPaisDao.getQtdTentativaRouboPropriedade(siglaPais);
	}

	public Long getEstatisticaPaisQtdRoubos(String siglaPais) {
		return estatisticaPaisDao.getQtdRoubos(siglaPais);
	}

	public Long getEstatisticaPaisQtdTentativaFurtoPessoa(String siglaPais) {
		return estatisticaPaisDao.getQtdTentativaFurtoPessoa(siglaPais);
	}

	public Long getEstatisticaPaisQtdTentativaFurtoPropriedade(String siglaPais) {
		return estatisticaPaisDao.getQtdTentativaFurtoPropriedade(siglaPais);
	}

	public Long getEstatisticaPaisQtdTentativaRouboPessoa(String siglaPais) {
		return estatisticaPaisDao.getQtdTentativaRouboPessoa(siglaPais);
	}

	public Long getEstatisticaPaisQtdTentativaRouboPropriedade(String siglaPais) {
		return estatisticaPaisDao.getQtdTentativaRouboPropriedade(siglaPais);
	}

	public Long getEstatisticaPaisQtdTurnoDois(String siglaPais) {
		return estatisticaPaisDao.getQtdTurnoDois(siglaPais);
	}

	public Long getEstatisticaPaisQtdTurnoQuatro(String siglaPais) {
		return estatisticaPaisDao.getQtdTurnoQuatro(siglaPais);
	}

	public Long getEstatisticaPaisQtdTurnoTres(String siglaPais) {
		return estatisticaPaisDao.getQtdTurnoTres(siglaPais);
	}

	public Long getEstatisticaPaisQtdTurnoUm(String siglaPais) {
		return estatisticaPaisDao.getQtdTurnoUm(siglaPais);
	}

	public Long getEstatisticaPaisQtdUsuarios(String siglaPais) {
		return estatisticaPaisDao.getQtdUsuarios(siglaPais);
	}

	public String getNomeCidade(Long idEstatisticaCidade) {
		return estatisticaCidadeDao.getNomeCidade(idEstatisticaCidade);
	}

	public String getSiglaEstado(Long idEstatisticaEstado) {
		return estatisticaEstadoDao.getSiglaEstado(idEstatisticaEstado);
	}

	public String getSiglaPais(Long idEstatisticaPais) {
		return estatisticaPaisDao.getSigla(idEstatisticaPais);
	}

	public EstatisticaCidadeDao getEstatisticaCidadeDao() {
		return estatisticaCidadeDao;
	}

	public void setEstatisticaCidadeDao(EstatisticaCidadeDao estatisticaCidadeDao) {
		this.estatisticaCidadeDao = estatisticaCidadeDao;
	}

	public EstatisticaEstadoDao getEstatisticaEstadoDao() {
		return estatisticaEstadoDao;
	}

	public void setEstatisticaEstadoDao(EstatisticaEstadoDao estatisticaEstadoDao) {
		this.estatisticaEstadoDao = estatisticaEstadoDao;
	}

	public EstatisticaPaisDao getEstatisticaPaisDao() {
		return estatisticaPaisDao;
	}

	public void setEstatisticaPaisDao(EstatisticaPaisDao estatisticaPaisDao) {
		this.estatisticaPaisDao = estatisticaPaisDao;
	}
	
	public Double getEstatisticaCidadeLatitude(String nomeCidade) {
		return this.estatisticaCidadeDao.getLatitude(nomeCidade);
	}
	
	public Double getEstatisticaCidadeLongitude(String nomeCidade) {
		return this.estatisticaCidadeDao.getLongitude(nomeCidade);
	}
	
	public Double getEstatisticaEstadoLatitude(String siglaEstado) {
		return this.estatisticaEstadoDao.getLatitude(siglaEstado);
	}
	
	public Double getEstatisticaEstadoLongitude(String siglaEstado) {
		return this.estatisticaEstadoDao.getLongitude(siglaEstado);
	}
	
	public Double getEstatisticaPaisLatitude(String siglaPais) {
		return this.estatisticaPaisDao.getLatitude(siglaPais);
	}
	
	public Double getEstatisticaPaisLongitude(String siglaPais) {
		return this.estatisticaPaisDao.getLongitude(siglaPais);
	}
	
	public Long getEstatisticaCidadeQtdTentativaRoubo(String nomeCidade){
		return this.estatisticaCidadeDao.getQtdTentativaRoubo(nomeCidade);
	}
	
	public Long getEstatisticaCidadeQtdTentativaFurto(String nomeCidade) {
		return this.estatisticaCidadeDao.getQtdTentativaFurto(nomeCidade);
	}
	
	public Long getEstatisticaEstadoQtdTentativaRoubo(String siglaEstado){
		return this.estatisticaEstadoDao.getQtdTentativaRoubo(siglaEstado);
	}
	
	public Long getEstatisticaEstadoQtdTentativaFurto(String nomeCidade) {
		return this.estatisticaEstadoDao.getQtdTentativaFurto(nomeCidade);
	}
	
	public Long getEstatisticaPaisQtdTentativaRoubo(String siglaPais){
		return this.estatisticaPaisDao.getQtdTentativaRoubo(siglaPais);
	}
	
	public Long getEstatisticaPaisQtdTentativaFurto(String siglaPais) {
		return this.estatisticaPaisDao.getQtdTentativaFurto(siglaPais);
	}
	
	public void updateEstatisticaCidade(EstatisticaCidade eCidade) {
		this.estatisticaCidadeDao.save(eCidade);
	}
	
	public void updateEstatisticaEstado(EstatisticaEstado eEstado) {
		this.estatisticaEstadoDao.save(eEstado);
	}
	
	public void updateEstatisticaPais(EstatisticaPais ePais) {
		this.estatisticaPaisDao.save(ePais);
	}

}
