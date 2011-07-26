import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.wikicrimes.dao.EstatisticaExternaDao;
import org.wikicrimes.dao.hibernate.EstatisticaExternaDaoHibernate;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.EstatisticaExterna;
import org.wikicrimes.service.EstatisticaExternaService;
import org.wikicrimes.service.impl.EstatisticaExternaServiceImpl;
import org.wikicrimes.util.DelegaciaOcorrencias;


public class Teste {

	public static void main(String[] args) {
		
		EstatisticaExternaService service = new EstatisticaExternaServiceImpl(); 
		//List<DelegaciaOcorrencias> lista = eDao.getTopDPs("Fevereiro");
		/*for (int i=0;i<lista.size();i++){
			System.out.println("Delegacia: " +lista.get(i).getDelegacia()+ "- Ocorrências: "+ lista.get(i).getNumTotalVitimas());
		}*/
		double lng,lat;
		lat=-23.592107;
		lng=-46.551991;
		
		String resposta = service.getEstatisticaExternaResposta("fevereiro", lng, lat, "LESÃO CORPORAL DOLOSA");
		
		System.out.println("Resposta: "+ resposta);
		
		
	}
}
