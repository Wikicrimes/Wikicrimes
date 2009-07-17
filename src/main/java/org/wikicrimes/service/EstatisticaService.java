/**
 * 
 */
package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.EstatisticaCidade;
import org.wikicrimes.model.EstatisticaEstado;
import org.wikicrimes.model.EstatisticaPais;


/**
 * Interface para acessar todas as tabelas de estatistica do BD
 * @author Marcos de Oliveira
 *
 */
public interface EstatisticaService extends GenericCrudService {
	
	// ESTATISTICA CIDADE
	public void updateEstatisticaCidade(EstatisticaCidade eCidade);
	public List getAllCidade();
	public List<EstatisticaCidade> getTopTenCidades();
	public List<EstatisticaCidade> getCidatesDoEstado(String siglaEstado);
	public EstatisticaCidade getEstatisticaCidade(Long idEstatisticaCidade);
	public EstatisticaCidade getEstatisticaCidade(String nomeCidade);
	public String getNomeCidade(Long idEstatisticaCidade);
	public Long getEstatisticaCidadeQtdUsuarios(String nomeCidade);
	public Long getEstatisticaCidadeQtdCrimes(String nomeCidade);
	public Long getEstatisticaCidadeQtdRoubos(String nomeCidade);
	public Long getEstatisticaCidadeQtdRouboPessoa(String nomeCidade);
	public Long getEstatisticaCidadeQtdRouboPropriedade(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaRouboPessoa(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaRouboPropriedade(String nomeCidade);
	public Long getEstatisticaCidadeQtdFurto(String nomeCidade);
	public Long getEstatisticaCidadeQtdFurtoPessoa(String nomeCidade);
	public Long getEstatisticaCidadeQtdFurtoPropriedade(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaFurtoPessoa(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaFurtoPropriedade(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutros(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosRixas(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosViolenciaDomestica(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosAbusoAutoridade(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosHomicidio(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosTentativaHomicidio(String nomeCidade);
	public Long getEstatisticaCidadeQtdOutrosLatrocinio(String nomeCidade);
	public Long getEstatisticaCidadeQtdTurnoUm(String nomeCidade);
	public Long getEstatisticaCidadeQtdTurnoDois(String nomeCidade);
	public Long getEstatisticaCidadeQtdTurnoTres(String nomeCidade);
	public Long getEstatisticaCidadeQtdTurnoQuatro(String nomeCidade);
	public EstatisticaEstado getEstatisticaEstadoDaEstatisticaCidade(String nomeCidade);
	public Double getEstatisticaCidadeLatitude(String nomeCidade);
	public Double getEstatisticaCidadeLongitude(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaRoubo(String nomeCidade);
	public Long getEstatisticaCidadeQtdTentativaFurto(String nomeCidade);
	
	//ESTATISTICA ESTADO
	public List<EstatisticaEstado> getTopFiveEstados();
	public void updateEstatisticaEstado(EstatisticaEstado eEstado);
	public List getAllEstado();
	public List<EstatisticaEstado> getEstadosDoPais(String siglaPais);
	public EstatisticaEstado getEstatisticaEstado(String siglaEstado);
	public EstatisticaEstado getEstatisticaEstado(Long idEstatisticaEstado);
	public String getNomeEstado(String siglaEstado);
	public Long getEstatisticaEstadoQtdUsuarios(String siglaEstado);
	public Long getEstatisticaEstadoQtdCrimes(String siglaEstado);
	public Long getEstatisticaEstadoRoubos(String siglaEstado);
	public Long getEstatisticaEstadoQtdRouboPessoa(String siglaEstado);
	public Long getEstatisticaEstadoQtdRouboPropriedade(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaRouboPessoa(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaRouboPropriedade(String siglaEstado);
	public Long getEstatisticaEstadoQtdFurto(String siglaEstado);
	public Long getEstatisticaEstadoQtdFurtoPessoa(String siglaEstado);
	public Long getEstatisticaEstadoQtdFurtoPropriedade(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaFurtoPessoa(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaFurtoPropriedade(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutros(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosRixas(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosViolenciaDomestica(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosAbusoAutoridade(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosHomicidio(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosTentativaHomicidio(String siglaEstado);
	public Long getEstatisticaEstadoQtdOutrosLatrocinio(String siglaEstado);
	public Long getEstatisticaEstadoQtdTurnoUm(String siglaEstado);
	public Long getEstatisticaEstadoQtdTurnoDois(String siglaEstado);
	public Long getEstatisticaEstadoQtdTurnoTres(String siglaEstado);
	public Long getEstatisticaEstadoQtdTurnoQuatro(String siglaEstado);
	public EstatisticaPais getEstatisticaPaisDoEstado(String siglaEstado);
	public Double getEstatisticaEstadoLatitude(String siglaEstado);
	public Double getEstatisticaEstadoLongitude(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaRoubo(String siglaEstado);
	public Long getEstatisticaEstadoQtdTentativaFurto(String siglaEstado);
	
	//ESTATISTICA PAIS
	public void updateEstatisticaPais(EstatisticaPais ePais);
	public List getAllPais();
	public EstatisticaPais getEstatisticaPais(Long idEstatisticaPais);
	public EstatisticaPais getEstatisticaPais(String siglaPais);
	public String getNomePais(String siglaPais);
	public String getSiglaPais(Long idEstatisticaPais);
	public Long getEstatisticaPaisQtdUsuarios(String siglaPais);
	public Long getEstatisticaPaisQtdCrimes(String siglaPais);
	public Long getEstatisticaPaisQtdRoubos(String siglaPais);
	public Long getEstatisticaPaisQtdRouboPessoa(String siglaPais);
	public Long getEstatisticaPaisQtdRouboPropriedade(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaRouboPessoa(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaRouboPropriedade(String siglaPais);
	public Long getEstatisticaPaisQtdFurto(String siglaPais);
	public Long getEstatisticaPaisQtdFurtoPessoa(String siglaPais);
	public Long getEstatisticaPaisQtdFurtoPropriedade(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaFurtoPessoa(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaFurtoPropriedade(String siglaPais);
	public Long getEstatisticaPaisQtdOutros(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosRixas(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosViolenciaDomestica(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosAbusoAutoridade(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosHomicidio(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosTentativaHomicidio(String siglaPais);
	public Long getEstatisticaPaisQtdOutrosLatrocinio(String siglaPais);
	public Long getEstatisticaPaisQtdTurnoUm(String siglaPais);
	public Long getEstatisticaPaisQtdTurnoDois(String siglaPais);
	public Long getEstatisticaPaisQtdTurnoTres(String siglaPais);
	public Long getEstatisticaPaisQtdTurnoQuatro(String siglaPais);
	public Double getEstatisticaPaisLatitude(String siglaPais);
	public Double getEstatisticaPaisLongitude(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaRoubo(String siglaPais);
	public Long getEstatisticaPaisQtdTentativaFurto(String siglaPais);

}
