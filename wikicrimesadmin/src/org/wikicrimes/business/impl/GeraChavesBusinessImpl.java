package org.wikicrimes.business.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wikicrimes.business.GeraChavesBusiness;
import org.wikicrimes.dao.GeraChavesDao;
import org.wikicrimes.model.ChavesCriptografia;
import org.wikicrimes.util.RSA;

@Component("geraChavesBusiness")
@Transactional(readOnly = true)
public class GeraChavesBusinessImpl implements GeraChavesBusiness{
	
	@Autowired
	@Qualifier("geraChavesDaoHibernate")
	private GeraChavesDao geraChavesDAO;
	private RSA rsa;
	
	public GeraChavesDao getGeraChavesDAO() {
		return geraChavesDAO;
	}

	public void setGeraChavesDAO(GeraChavesDao geraChavesDAO) {
		this.geraChavesDAO = geraChavesDAO;
	}

	@Override
	@Transactional(readOnly=false)
	public void adicionaChavesGeradas(ChavesCriptografia cc) {
		geraChavesDAO.adicionaChavesGeradas(cc);
	}

	@Override
	public List<ChavesCriptografia> consultaDominio(String dominio) {
		return geraChavesDAO.consultaDominio(dominio);
	}

	@Override
	public boolean verificaChaveGerada(ChavesCriptografia cc) {
		return geraChavesDAO.verificaChaveGerada(cc);
	}
	
	@Transactional(readOnly=false)
	public ChavesCriptografia geraChaves(String site){
		rsa = new RSA();
		ChavesCriptografia cc=null;
		try {
			while(true){
				cc = rsa.gerarChaves();
				
				if(!verificaChaveGerada(cc)){
					cc.setSite(site);
					adicionaChavesGeradas(cc);
					break;
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return cc;
	}
}
