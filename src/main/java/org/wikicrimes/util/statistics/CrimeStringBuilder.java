package org.wikicrimes.util.statistics;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RelatoRazao;

public class CrimeStringBuilder{

	public static String buildString(List<BaseObject> events) {

		StringBuilder s = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Crime crime = null;
		Relato relato = null;
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i) instanceof Crime) {
				crime = (Crime) events.get(i);
				s.append(crime.getChave());
				s.append("|");
				s.append(crime.getTipoCrime().getIdTipoCrime());
				s.append("|");
				s.append(crime.getLatitude()); 
				s.append("|");
				s.append(crime.getLongitude());
				s.append("|");
				s.append(crime.getIdCrime()); 
				if (crime.getTipoVitima() != null) {
					s.append("|");
					s.append(crime.getTipoVitima().getIdTipoVitima());
				} else {
					s.append("|");
				}
				s.append("|");
				s.append(sdf.format(crime.getData()));
				Set<CrimeRazao> razoes =  crime.getRazoes();
				StringBuilder razoesTexto = new StringBuilder();
				if(razoes != null){
					for (Iterator<CrimeRazao> iterator = razoes.iterator(); iterator.hasNext();) {
						CrimeRazao crimeRazao = (CrimeRazao) iterator.next();
						razoesTexto.append(crimeRazao.getRazao().getIdRazao().toString());
						razoesTexto.append(",");
					}
				}	
				s.append("|");
				s.append(razoesTexto);
				String desc10Letras = "";
				if (crime.getDescricao().length() > 10)
					desc10Letras = crime.getDescricao().substring(0, 10)
							+ "...";
				else
					desc10Letras=crime.getDescricao().substring(0,crime.getDescricao().length());
				s.append("|");
				s.append(desc10Letras.replaceAll("\\|", "").replaceAll("\\\n", ""));
				s.append("|");
				s.append(crime.getVisualizacoes());
				s.append("|");
				s.append(crime.getQtdComentarios());
				s.append("|");
				s.append(crime.getConfirmacoesPositivas());
			}else {
				relato = (Relato) events.get(i);
				
				s.append(relato.getChave());
				s.append("|");
				s.append(relato.getTipoRelato());
				s.append("|");
				s.append(relato.getLatitude());
				s.append("|");
				s.append(relato.getLongitude());
				s.append("|");
				s.append(relato.getIdRelato()); 
				if (relato.getSubTipoRelato() != null) {
					s.append("|");
					s.append(relato.getSubTipoRelato());
				} else {
					s.append("|");
				}
				s.append("|");
				s.append(sdf.format(relato.getDataHoraRegistro()));
				
				Set<RelatoRazao> razoes =  relato.getRazoes();
				StringBuilder razoesTexto = new StringBuilder();
				if(razoes != null){
					for (Iterator<RelatoRazao> iterator = razoes.iterator(); iterator.hasNext();) {
						RelatoRazao relatoRazao = (RelatoRazao) iterator.next();
						razoesTexto.append(relatoRazao.getRazao().getIdRazao().toString());
						razoesTexto.append(",");
					}
				}
				s.append("|");
				s.append(razoesTexto);
			}
			s.append("\n");
		}

		return s.toString();
	}


}
