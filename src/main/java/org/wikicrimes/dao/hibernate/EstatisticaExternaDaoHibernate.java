package org.wikicrimes.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.wikicrimes.dao.EstatisticaExternaDao;
import org.wikicrimes.model.EstatisticaExterna;
import org.wikicrimes.model.FonteExterna;
import org.wikicrimes.util.DelegaciaOcorrencias;
import org.wikicrimes.util.DelegaciaProxima;
import org.wikicrimes.util.Util;

public class EstatisticaExternaDaoHibernate extends GenericCrudDaoHibernate
implements EstatisticaExternaDao{

	public EstatisticaExternaDaoHibernate() {
		setEntity(EstatisticaExterna.class);
	}
	
	public EstatisticaExterna getEstatisticaExterna(String mes, String dp,
			String tipoCrime) {
		String query="";
		String parametros= "";
		String ultimoMesBanco = ultimoMesBanco(dp);
		EstatisticaExterna e = new EstatisticaExterna();
		
		//O parametro mes é opcional. Caso o último mês que há no banco seja anterior ao atual, pega-se o último mês do banco.
		if(mes.isEmpty() )  mes = ultimoMesBanco;
		
//		if(Util.getMonthAsInt(mes)> Util.getMonthAsInt(ultimoMesBanco) ){
			parametros = "  mes = '" + ultimoMesBanco+"' and delegacia = '"+ dp + "'" ;
//		}else{
//			parametros = " mes = '" + mes+"' and delegacia = '"+ dp + "'" ;
//		}
		//O parametro tipoCrime também é opcional.
		if(!tipoCrime.isEmpty()) {
			query +=  "from EstatisticaExterna where tipo like '%"+tipoCrime +"%' and " + parametros;
			
			List<EstatisticaExterna> eC = getHibernateTemplate().find(query);
			
		 	e = eC.get(0);
			
		}else{
			query += "select sum(numVitimas) from EstatisticaExterna where "+ parametros;
			int numVitimas;
			numVitimas = Integer.parseInt(getSession().createQuery(query).setCacheable(false).uniqueResult().toString());
			if(numVitimas >0){
			
				e.setNumVitimas(numVitimas);
				e.setDelegacia(dp);
				e.setMes(mes);
				e.setTipo("");
				
			}
			
		}
		return e;
		
		
	}
	
	public String ultimoMesBanco(String dp){
		String query = "from EstatisticaExterna ";
		
		if(dp!=null){
			query += "where delegacia like '"+ dp + "' group by mes" ;
		}
		List<EstatisticaExterna> eC = getHibernateTemplate().find(query);
		

		int aux = 0;
		String mesAtual = null;
		for (EstatisticaExterna e : eC) {
			String mes = e.getMes();
			int auxMes = Util.getMonthAsInt(mes);
			
			if(auxMes > aux) {
				aux = auxMes;
				mesAtual = mes;
			} 
		}
		
		return mesAtual;
	}

	@Override
	public String getDP(double lat, double lng) {
		String retorno = "";
				
		if(lat != 0 & lng != 0){
			String queryFonte = "from FonteExterna where latitude is not null and longitude is not null";
			List<FonteExterna> fontes = getHibernateTemplate().find(queryFonte);
			if(!fontes.isEmpty()){
				ArrayList<DelegaciaProxima> delegacias = 	new ArrayList<DelegaciaProxima>();
				for (FonteExterna f : fontes) {
					double distancia = (Math.acos(
							Math.sin(
									lat * Math.PI / 180
							        ) 
							 	* Math.sin(
									f.getLatitude() * Math.PI / 180
							      	) 
								+ Math.cos(
							      		lat * Math.PI / 180
							      	) 
								* Math.cos(
							      		f.getLatitude() * Math.PI / 180
							      	) 
								* Math.cos(
									(
										lng - f.getLongitude()
									) 
									* Math.PI / 180
							        )
							     )
							 * 180 / Math.PI) * 60 * 1.1515; 
					DelegaciaProxima delegacia = new DelegaciaProxima();
							delegacia.setIdFonteExterna(f.getIdFonteExterna());
							delegacia.setLatitude(f.getLatitude()); 
							delegacia.setLongitude(f.getLongitude());
							delegacia.setDistancia(distancia);
							delegacia.setNome(f.getNome());			
							
							delegacias.add(delegacia);
				}	
				Collections.sort(delegacias);
				
				retorno = delegacias.get(0).getNome().replace("Ocorrências Mensais -", "");
				retorno = retorno.replace("2011" , "");
				retorno = retorno.trim();
				
			}
			
			/*String query = "select new.wikicrimes.util.DelegaciaProxima(f.idFonteExterna, f.latitude, f.longitude," +
			"((ACOS(SIN("+lat+" * PI() / 180) * SIN(f.latitude * PI() / 180) + COS("+lat+
			" * PI() / 180) * COS(f.latitude * PI() / 180) * COS(("+lng+
			" - f.longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) as distance, f.nome) FROM FonteExterna f"+
			" having distance is not null order by distance";
			
			//getSession(true).createQuery(query).setCacheable(false).list();
			List<DelegaciaProxima> eC = getSession().createQuery(query).setCacheable(false).list();
			retorno = eC.get(0).getNome().replace("Ocorrências Mensais -", "");
			retorno = retorno.replace("2011" , "");
			retorno = retorno.trim();*/
		}
			return retorno;	
		
	}

	@Override
	public List<DelegaciaOcorrencias> getTopDPs(String mes) {
		//String query ="select new org.wikicrimes.util.DelegaciaOcorrencias(sum(Ee.numVitimas), Ee.delegacia) from EstatisticaExterna Ee";
		
		String queryVitimas ="select sum(numVitimas) from EstatisticaExterna ";
		String queryDelegacias = "select delegacia from EstatisticaExterna ";
		
		if (mes != null ) {
    	    queryVitimas += "where mes = '" + mes + "' group by delegacia order by sum(numVitimas) desc";
    	    queryDelegacias+= "where mes = '" + mes + "' group by delegacia order by sum(numVitimas) desc";
    	    //query += "where mes = '" + mes + "' group by delegacia order by sum(numVitimas) desc";
		}
			
		List<Long> numVitimas;
		List<String> delegacias;
		
		//List<DelegaciaOcorrencias> teste = getSession().createQuery(query).setCacheable(false).list(); 
		
		numVitimas = getSession().createQuery(queryVitimas).setCacheable(false).list();
		delegacias = getSession().createQuery(queryDelegacias).setCacheable(false).list();
		
		ArrayList<DelegaciaOcorrencias> ocorrencias = new ArrayList<DelegaciaOcorrencias>();
		
		if(delegacias.size()==numVitimas.size()){
			for (int i=0;i<delegacias.size();i++) {
				DelegaciaOcorrencias ocorrencia = new DelegaciaOcorrencias();
				ocorrencia.setDelegacia(delegacias.get(i));
				ocorrencia.setNumTotalVitimas(numVitimas.get(i));
				
				ocorrencias.add(ocorrencia);
			}
		}
    	return ocorrencias;
    	
	}

	@Override
	public int getCrescimento(EstatisticaExterna estatistica) {
		String query = "from EstatisticaExterna ";
		String mesAnterior = getMesAnterior(estatistica.getMes()); 
		EstatisticaExterna estatisticaComparar;
		
		//Tratamento para Tipo de Crime vazio
		if(estatistica.getTipo().isEmpty()){
			query = "select sum(numVitimas) from EstatisticaExterna where mes = '"+mesAnterior+"' and delegacia = '"+estatistica.getDelegacia()+"' ";
			
			estatisticaComparar = new EstatisticaExterna();
			
			int numVitimas;
			numVitimas = Integer.parseInt(getSession().createQuery(query).setCacheable(false).uniqueResult().toString());
			
			estatisticaComparar.setNumVitimas(numVitimas);
			
		}else{
			
			if (estatistica.getMes()!= null ) {
				query += "where mes = '" + mesAnterior+"' and delegacia ='"+estatistica.getDelegacia()+"' and tipo = '"+estatistica.getTipo()+ "'  order by numVitimas desc";
			}
			List<EstatisticaExterna> eC = getHibernateTemplate().find(query);
			if(eC.size()>0){
				estatisticaComparar = eC.get(0);
			}else{
	    		//Não houve estatística para o mês anterior. Logo, houve um crescimento.
	    		return 1;
			}
			
		}
    	
   		if(estatistica.getNumVitimas() > estatisticaComparar.getNumVitimas()){
   			return 1; //Aumentou o número de crimes na região
   		}
   		if(estatistica.getNumVitimas() < estatisticaComparar.getNumVitimas()){
   			return -1; //Diminuiu o número de crimes na região
   		}
   		if(estatistica.getNumVitimas() == estatisticaComparar.getNumVitimas()){
   			return 0; //O número de crimes na região continuou o mesmo
   		}    	
    	return 0;
	}

	@Override
	public int getRankDp(String dp, String mes) {
		String ultimoMesBanco = ultimoMesBanco(dp);
		List<DelegaciaOcorrencias> ocorrencias; 
		
		if (mes.isEmpty()) mes = ultimoMesBanco;
		
		if(Util.getMonthAsInt(mes)>Util.getMonthAsInt(ultimoMesBanco)){
			ocorrencias = getTopDPs(ultimoMesBanco);
		}else{
			ocorrencias = getTopDPs(mes);
		}
		
		
		if(ocorrencias!=null){
			for(int i=0;i<ocorrencias.size();i++){
				if(dp.equals(ocorrencias.get(i).getDelegacia())){
					return i+1; //Posição no rank de delegacias mais perigosas
				}
			}
		}
		return 0;
		
	}
	/*@Override
	public FonteExterna getFonteExterna(long idFonte){
		
		String query = "from FonteExterna ";
		
		if(idFonte>0){
			query+= "where idFonteExterna = '" + idFonte+"' ";
		}
		
		List<FonteExterna> eC = getHibernateTemplate().find(query);
		
		if(eC.size()>0) return eC.get(0);
		
		return new FonteExterna();
	}*/
	
	@Override
	public String getMesAnterior(String mes){
		String mesAnterior="";
		
		if(mes.equalsIgnoreCase("Janeiro")){
			mesAnterior = "Dezembro";
		}
		if(mes.equalsIgnoreCase("Fevereiro")){
			mesAnterior = "Janeiro";
		}
		if(mes.equalsIgnoreCase("Março")){
			mesAnterior = "Fevereiro";
		}
		if(mes.equalsIgnoreCase("Abril")){
			mesAnterior = "Março";
		}
		if(mes.equalsIgnoreCase("Maio")){
			mesAnterior = "Abril";
		}
		if(mes.equalsIgnoreCase("Junho")){
			mesAnterior = "Maio";
		}
		if(mes.equalsIgnoreCase("Julho")){
			mesAnterior = "Junho";
		}
		if(mes.equalsIgnoreCase("Agosto")){
			mesAnterior = "Julho";
		}
		if(mes.equalsIgnoreCase("Setembro")){
			mesAnterior = "Agosto";
		}
		if(mes.equalsIgnoreCase("Outubro")){
			mesAnterior = "Setembro";
		}
		if(mes.equalsIgnoreCase("Novembro")){
			mesAnterior = "Outubro";
		}
		if(mes.equalsIgnoreCase("Dezembro")){
			mesAnterior = "Novembro";
		}
		
		return mesAnterior;
		
	}
	
	@Override
	public double getTaxaCrescimento(EstatisticaExterna estatistica){
		double taxa;
		String query;
		String mesAnterior = getMesAnterior(estatistica.getMes()); 
		
		if(estatistica.getTipo()==null | estatistica.getTipo().isEmpty()){
			query = "select sum(numVitimas) from EstatisticaExterna ";
			
			if(!estatistica.getMes().isEmpty()){
				query += " where mes = '" + mesAnterior+"' and delegacia ='"+estatistica.getDelegacia()+"' ";
			}
			
			int numVitimas = Integer.parseInt(getSession().createQuery(query).setCacheable(false).uniqueResult().toString());
			
			//List<Integer> eC = getSession().createQuery(query).setCacheable(false).list();
	    	if(numVitimas>0){
	    			    		
	    		taxa = (double)numVitimas/(double)estatistica.getNumVitimas();
	    		return taxa; //retorna a taxa de crescimento.
	    	}else{
	    		//Não houve registro para o mês anterior.
	    		return 0;
	    	}
			
		}else{
			query = "from EstatisticaExterna ";
		
		
			if (estatistica.getMes()!= null ) {
				query += " where mes = '" + mesAnterior+"' and delegacia ='"+estatistica.getDelegacia()+"' and tipo like '%"+estatistica.getTipo()+ "%'  order by numVitimas desc";
			}
    	
			List<EstatisticaExterna> eC = getHibernateTemplate().find(query);
	    	if(eC.size()>0){
	    		EstatisticaExterna estatisticaAnterior = eC.get(0);
	    		
	    		taxa = (double)estatisticaAnterior.getNumVitimas()/(double)estatistica.getNumVitimas();
	    		return taxa; //retorna a taxa de crescimento.
	    	}else{
	    		//Não houve registro para o mês anterior.
	    		return 0;
	    	}
		}
		
	}
	
	@Override
	public FonteExterna getFonteExternaPorDp(String dp){
		String query = "from FonteExterna ";
				
    	if (!dp.isEmpty()) {
    	    query += "where nome like'%"+ dp +"%' ";
    	}
    	
    	List<FonteExterna> eC = getHibernateTemplate().find(query);
		
    	return eC.get(0);
	}
}
