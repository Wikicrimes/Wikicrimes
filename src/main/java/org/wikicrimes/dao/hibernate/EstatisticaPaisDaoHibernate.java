package org.wikicrimes.dao.hibernate;

import org.wikicrimes.dao.EstatisticaPaisDao;
import org.wikicrimes.model.EstatisticaEstado;
import org.wikicrimes.model.EstatisticaPais;
import java.util.List;

public class EstatisticaPaisDaoHibernate extends GenericCrudDaoHibernate
		implements EstatisticaPaisDao {

	public EstatisticaPaisDaoHibernate() {
		setEntity(EstatisticaPais.class);
	}

	public EstatisticaPais getEstatisticaPais(Long idEstatisticaPais) {
    	String query = "from EstatisticaPais ";

    	if (idEstatisticaPais != null) {
    	    query += "where idEstatisticaPais = " + idEstatisticaPais;
    	}

    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0);
	}
	
	public EstatisticaPais getEstatisticaPais(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	if (eP.size()>0)
    		return eP.get(0);
    	else
	    	return null;
	}

	public String getNomePais(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getNome();
	}

	public Long getQtdCrimes(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQuantidadeCrimes();
	}

	public Long getQtdFurto(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQuantidadeFurtos();
	}

	public Long getQtdFurtoPessoa(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdFurtoPessoa();
	}

	public Long getQtdFurtoPropriedade(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdFurtoPropriedade();
	}

	public Long getQtdOutros(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQuantidadeOutros();
	}

	public Long getQtdOutrosAbusoAutoridade(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroAbusoAutoridade();
	}

	public Long getQtdOutrosHomicidio(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroHomicidio();
	}

	public Long getQtdOutrosLatrocinio(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroLatrocinio();
	}

	public Long getQtdOutrosRixas(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroRixas();
	}

	public Long getQtdOutrosTentativaHomicidio(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroTentativaHomicidio();
	}

	public Long getQtdOutrosViolenciaDomestica(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdOutroViolenciaDomestica();
	}

	public Long getQtdRouboPessoa(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdRouboPessoa();
	}

	public Long getQtdRouboPropriedade(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdRouboPropriedade();
	}

	public Long getQtdRoubos(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQuantidadeRoubos();
	}

	public Long getQtdTentativaFurtoPessoa(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaFurtoPessoa();
	}

	public Long getQtdTentativaFurtoPropriedade(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaFurtoPropriedade();
	}

	public Long getQtdTentativaRouboPessoa(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaRouboPessoa();
	}

	public Long getQtdTentativaRouboPropriedade(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaRouboPropriedade();
	}

	public Long getQtdTurnoDois(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTurnoDois();
	}

	public Long getQtdTurnoQuatro(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTurnoQuatro();
	}

	public Long getQtdTurnoTres(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTurnoTres();
	}

	public Long getQtdTurnoUm(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTurnoUm();
	}

	public Long getQtdUsuarios(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQuantidadeUsuarios();
	}

	public String getSigla(Long idEstatisticaPais) {
		String query = "from EstatisticaPais ";
    	if (idEstatisticaPais != null) {
    	    query += "where idEstatisticaPais = " + idEstatisticaPais;
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getSigla();
	}
	
	public Double getLatitude(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getLatitude();
	}

	public Double getLongitude(String sigla) {
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getLongitude();
	}
	
	public Long getQtdTentativaRoubo(String sigla){
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaRoubo();
	}
	
	public Long getQtdTentativaFurto(String sigla){
		String query = "from EstatisticaPais ";
    	if (sigla != null) {
    	    query += "where sigla = '" + sigla + "'";
    	}
    	List<EstatisticaPais> eP = getHibernateTemplate().find(query);
    	return eP.get(0).getQtdTentativaFurto();
	}
}
