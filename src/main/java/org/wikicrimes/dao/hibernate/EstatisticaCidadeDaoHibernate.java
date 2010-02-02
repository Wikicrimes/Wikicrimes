package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.wikicrimes.dao.EstatisticaCidadeDao;
import org.wikicrimes.model.EstatisticaCidade;
import org.wikicrimes.model.EstatisticaEstado;

public class EstatisticaCidadeDaoHibernate extends GenericCrudDaoHibernate
		implements EstatisticaCidadeDao {

	public EstatisticaCidadeDaoHibernate() {
		setEntity(EstatisticaCidade.class);
	}

	public List<EstatisticaCidade> getCidadesDoEstado(Long idEstatisticaEstado) {
    	String query = "from EstatisticaCidade ";

    	if (idEstatisticaEstado != null) {
    	    query += "where estatisticaEstado = " + idEstatisticaEstado;
    	}

    	return getHibernateTemplate().find(query);
		
	}	
	
	public EstatisticaCidade getEstatisticaCidade(Long idEstatisticaCidade) {
    	String query = "from EstatisticaCidade ";

    	if (idEstatisticaCidade != null) {
    	    query += "where idEstatisticaCidade = " + idEstatisticaCidade;
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0); 
	}
	
	public EstatisticaCidade getEstatisticaCidade(String nome) {
		String query = "from EstatisticaCidade ";

    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0);
	}
	
	public EstatisticaEstado getEstatisticaEstado(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	
    	return eC.get(0).getEstatisticaEstado();
	}

	public String getNomeCidade(Long idEstatisticaCidade) {
		String query = "from EstatisticaCidade ";
    	if (idEstatisticaCidade != null) {
    	    query += "where idEstatisticaCidade = " + idEstatisticaCidade;
    	}
    	
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getNome();
  	}

	public Long getQtdCrimes(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQuantidadeCrimes(); 
	}

	public Long getQtdFurto(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQuantidadeFurtos(); 
	}

	public Long getQtdFurtoPessoa(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdFurtoPessoa(); 
	}

	public Long getQtdFurtoPropriedade(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdFurtoPropriedade(); 
	}

	public Long getQtdOutros(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQuantidadeOutros(); 
	}

	public Long getQtdOutrosAbusoAutoridade(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroAbusoAutoridade(); 
	}

	public Long getQtdOutrosHomicidio(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroHomicidio(); 
	}

	public Long getQtdOutrosLatrocinio(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroLatrocinio(); 
	}

	public Long getQtdOutrosRixas(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroRixas(); 
	}

	public Long getQtdOutrosTentativaHomicidio(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroTentativaHomicidio(); 
	}

	public Long getQtdOutrosViolenciaDomestica(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdOutroViolenciaDomestica(); 
	}

	public Long getQtdRouboPessoa(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdRouboPessoa(); 
	}

	public Long getQtdRouboPropriedade(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdRouboPropriedade(); 
	}

	public Long getQtdRoubos(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQuantidadeRoubos();
	}

	public Long getQtdTentativaFurtoPessoa(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaFurtoPessoa(); 
	}

	public Long getQtdTentativaFurtoPropriedade(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaFurtoPropriedade();
	}

	public Long getQtdTentativaRouboPessoa(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaRouboPessoa(); 
	}

	public Long getQtdTentativaRouboPropriedade(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaRouboPropriedade(); 
	}

	public Long getQtdTurnoDois(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTurnoDois(); 
	}

	public Long getQtdTurnoQuatro(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTurnoQuatro(); 
	}

	public Long getQtdTurnoTres(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTurnoTres(); 
	}

	public Long getQtdTurnoUm(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTurnoUm(); 
	}

	public Long getQtdUsuarios(String nome) {
		String query = "from EstatisticaCidade ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQuantidadeUsuarios(); 
	}

	public Double getLatitude(String nome) {
		String query = "from EstatisticaPais ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getLatitude();
	}

	public Double getLongitude(String nome) {
		String query = "from EstatisticaPais ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getLongitude();
	}
	
	public Long getQtdTentativaRoubo(String nome){
		String query = "from EstatisticaPais ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaRoubo();
	}
	
	public Long getQtdTentativaFurto(String nome){
		String query = "from EstatisticaPais ";
    	if (nome != null) {
    	    query += "where nome = '" + nome + "'";
    	}
    	List<EstatisticaCidade> eC = getHibernateTemplate().find(query);
    	return eC.get(0).getQtdTentativaFurto();
	}
	
	public List<EstatisticaCidade> getTopTenCidades() {
		String query = "from EstatisticaCidade order by ECI_QTD_CRIMES desc";
		getHibernateTemplate().setMaxResults(5);
		List<EstatisticaCidade> resultado = getHibernateTemplate().find(query);
		getHibernateTemplate().setMaxResults(1000);
		return resultado;
	}
}
