/**
 * 
 */
package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.wikicrimes.model.EstatisticaCidade;
import org.wikicrimes.model.EstatisticaEstado;
import org.wikicrimes.model.EstatisticaPais;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.EstatisticaService;

/**
 * Form de consultas a tabela de crimes para geração da página de estatisticas
 * @author Marcos de Oliveira
 *
 */
public class EstatisticaForm extends GenericForm {
	
	private EstatisticaService estatisticaService;
	public CrimeService crimeService;
	private String sortColumn = "nome";
    private boolean ascending = true;
	private List<EstatisticaPais> ePais = new ArrayList<EstatisticaPais>();
	private List<EstatisticaEstado> eEstado = new ArrayList<EstatisticaEstado>();
	private List<EstatisticaCidade> eCidade = new ArrayList<EstatisticaCidade>();
	
	private List<EstatisticaCidade> cidadesSemLatLong = new ArrayList<EstatisticaCidade>();
	private List<EstatisticaEstado> estadosSemLatLong = new ArrayList<EstatisticaEstado>();
	private List<EstatisticaPais> paisesSemLatLong = new ArrayList<EstatisticaPais>();
	
	List<EstatisticaCidade> estatisticasCidade = new ArrayList<EstatisticaCidade>();
	List<EstatisticaEstado> fiveEstados = new ArrayList<EstatisticaEstado>();
	
	private String escopoGrafico = "";
	private String graficoLinha = "";
	
	private String siglaEstadoAgrupador = "";
	private String graficoAgrupador = "";
	
	private String escopoEstados = "";
	private String escopoCidades = "";
	private String escopoCidade = "";
	
	private String gCidadesEstado = "";
	private String gEstadosPais = "";
	
	private String gTemporalMundo = "";
	private String gTemporalPais = "";
	private String gTemporalEstado = "";
	private String gTemporalCidade = "";
	
	private String gPizzaTurnoMundo = "";
	private String gPizzaTurnoPais = "";
	private String gPizzaTurnoEstado = "";
	private String gPizzaTurnoCidade = "";
	
	private String gPizzaCrimesMundo = "";
	private String gPizzaCrimesPais = "";
	private String gPizzaCrimesEstado = "";
	private String gPizzaCrimesCidade = "";
	
	private String gBarrasPais = "";
	private String gBarrasEstado = "";
	private String gBarrasCidade = "";
	
	private String gMundo = "";
	private String gMeterMundo = "";
	
	private long maiorValorPais = 0;
	private String siglaMaiorValorPais = "";
	
	private String topTen = "";
	private String topFiveEstados = "";
	
	private Locale locale;
	
	private boolean acesso;
	
	public EstatisticaForm() {
		locale = (Locale) ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getLocale();
		
	}

	public String getgTemporalMundo() {
		String serie1 = "";
		String serie2 = "";
		String serie3 = "";
		String serie4 = "";
		String serie5 = "";
		String ano = "";
		String mes = "";
		String diaFinal = "31";
		String diaInicial = "01";
		List<String> x = new ArrayList<String>();
		for (int i = 0; i <= 5; i++) {
			Calendar calend = java.util.Calendar.getInstance();
			calend.add(Calendar.MONTH, -i);
			ano = Integer.toString(calend.get(Calendar.YEAR));
			mes = Integer.toString(calend.get(Calendar.MONTH)+1);
			String dataInicial = ano + "-" + mes + "-" + diaInicial;
			String dataFinal = ano + "-" + mes + "-" + diaFinal;
			serie1 = this.crimeService.getQtdCrimesByDateInterval(1, dataInicial, dataFinal).toString() + serie1;
			serie2 = this.crimeService.getQtdCrimesByDateInterval(2, dataInicial, dataFinal).toString() + serie2;
			serie3 = this.crimeService.getQtdCrimesByDateInterval(3, dataInicial, dataFinal).toString() + serie3;
			serie4 = this.crimeService.getQtdCrimesByDateInterval(4, dataInicial, dataFinal).toString() + serie4;
			serie5 = this.crimeService.getQtdCrimesByDateInterval(5, dataInicial, dataFinal).toString() + serie5;
			x.add(mes + "/" + ano);
			if(i<5){
				serie1 = "," + serie1;
				serie2 = "," + serie2;
				serie3 = "," + serie3;
				serie4 = "," + serie4;
				serie5 = "," + serie5;
			}
		}
		
		String googleChart = "http://chart.apis.google.com/chart?";
		String chs = "chs=840x280";
		String chf = "&amp;chf=bg,s,efefef";
		String chg = "&amp;chg=20,25";
		String chls = "&amp;chls=5,1,0|5,1,0|5,1,0|5,1,0|5,1,0";
		String chco = "&amp;chco=ff6666,7eb6ff,1b3f8b,cd0000,E38217";
		String chdl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
		chdl = "&amp;chdl=Tentativas+de+Roubo|Tentativas+de+Furto|Furto|Roubo|Outros";
		} else {
			chdl = "&amp;chdl=Attempt Robbery|Attempt Theft|Theft|Robery|Others";
		}
		String chd = "&amp;chd=t:" + serie1 + "|" + serie2 + "|" + serie3 + "|" + serie4 + "|" + serie5;
		String chds="&amp;chds=0,300";
		//String chtt = "&amp;chtt=Últimos+6+meses";
		String cht = "&amp;cht=lc";
		String chxt = "&amp;chxt=x,y";
		String chxl = "&amp;chxl=0:|" + x.get(5) + "|" + x.get(4) + "|" + x.get(3) + "|" + x.get(2) + "|" + x.get(1) + "|" + x.get(0);
		chxl += "|1:|0|50|100|150|200|250|300";
		
		this.gTemporalMundo = googleChart + chs + chf + chg + chls + chco + codeUrl(chdl) + chd + chds + cht + chxt + chxl;
		
		//System.out.println(chd);
		
		return gTemporalMundo;
	}
	
	public String getgTemporalPais() {
		String serie1 = "";
		String serie2 = "";
		String serie3 = "";
		String serie4 = "";
		String serie5 = "";
		String ano = "";
		String mes = "";
		String diaFinal = "31";
		String diaInicial = "01";
		List<String> x = new ArrayList<String>();
		for (int i = 0; i <= 5; i++) {
			Calendar calend = java.util.Calendar.getInstance();
			calend.add(Calendar.MONTH, -i);
			ano = Integer.toString(calend.get(Calendar.YEAR));
			mes = Integer.toString(calend.get(Calendar.MONTH)+1);
			String dataInicial = ano + "-" + mes + "-" + diaInicial;
			String dataFinal = ano + "-" + mes + "-" + diaFinal;
			serie1 = this.crimeService.getQtdCrimesByDateIntervalPais(1, dataInicial, dataFinal, escopoEstados).toString() + serie1;
			serie2 = this.crimeService.getQtdCrimesByDateIntervalPais(2, dataInicial, dataFinal, escopoEstados).toString() + serie2;
			serie3 = this.crimeService.getQtdCrimesByDateIntervalPais(3, dataInicial, dataFinal, escopoEstados).toString() + serie3;
			serie4 = this.crimeService.getQtdCrimesByDateIntervalPais(4, dataInicial, dataFinal, escopoEstados).toString() + serie4;
			serie5 = this.crimeService.getQtdCrimesByDateIntervalPais(5, dataInicial, dataFinal, escopoEstados).toString() + serie5;
			x.add(mes + "/" + ano);
			if(i<5){
				serie1 = "," + serie1;
				serie2 = "," + serie2;
				serie3 = "," + serie3;
				serie4 = "," + serie4;
				serie5 = "," + serie5;
			}
		}
		
		String googleChart = "http://chart.apis.google.com/chart?";
		String chs = "chs=840x280";
		String chf = "&amp;chf=bg,s,efefef";
		String chg = "&amp;chg=20,25";
		String chls = "&amp;chls=5,1,0|5,1,0|5,1,0|5,1,0|5,1,0";
		String chco = "&amp;chco=ff6666,7eb6ff,1b3f8b,cd0000,E38217";
		String chdl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chdl = "&amp;chdl=Tentativas+de+Roubo|Tentativas+de+Furto|Furto|Roubo|Outros";
			} else {
				chdl = "&amp;chdl=Attempt Robbery|Attempt Theft|Theft|Robery|Others";
			}
		String chd = "&amp;chd=t:" + serie1 + "|" + serie2 + "|" + serie3 + "|" + serie4 + "|" + serie5;
		String chds="&amp;chds=0,300";
		//String chtt = "&amp;chtt=Últimos+6+meses";
		String cht = "&amp;cht=lc";
		String chxt = "&amp;chxt=x,y";
		String chxl = "&amp;chxl=0:|" + x.get(5) + "|" + x.get(4) + "|" + x.get(3) + "|" + x.get(2) + "|" + x.get(1) + "|" + x.get(0);
		chxl += "|1:|0|50|100|150|200|250|300";
		
		this.gTemporalPais = googleChart + chs + chf + chg + chls + chco + codeUrl(chdl) + chd + chds + cht + chxt + chxl;
				
		return gTemporalPais;
	}
	
	public String getgTemporalEstado() {
		String serie1 = "";
		String serie2 = "";
		String serie3 = "";
		String serie4 = "";
		String serie5 = "";
		String ano = "";
		String mes = "";
		String diaFinal = "31";
		String diaInicial = "01";
		List<String> x = new ArrayList<String>();
		for (int i = 0; i <= 5; i++) {
			Calendar calend = java.util.Calendar.getInstance();
			calend.add(Calendar.MONTH, -i);
			ano = Integer.toString(calend.get(Calendar.YEAR));
			mes = Integer.toString(calend.get(Calendar.MONTH)+1);
			String dataInicial = ano + "-" + mes + "-" + diaInicial;
			String dataFinal = ano + "-" + mes + "-" + diaFinal;
			serie1 = this.crimeService.getQtdCrimesByDateIntervalEstado(1, dataInicial, dataFinal, escopoCidades).toString() + serie1;
			serie2 = this.crimeService.getQtdCrimesByDateIntervalEstado(2, dataInicial, dataFinal, escopoCidades).toString() + serie2;
			serie3 = this.crimeService.getQtdCrimesByDateIntervalEstado(3, dataInicial, dataFinal, escopoCidades).toString() + serie3;
			serie4 = this.crimeService.getQtdCrimesByDateIntervalEstado(4, dataInicial, dataFinal, escopoCidades).toString() + serie4;
			serie5 = this.crimeService.getQtdCrimesByDateIntervalEstado(5, dataInicial, dataFinal, escopoCidades).toString() + serie5;
			x.add(mes + "/" + ano);
			if(i<5){
				serie1="," + serie1;
				serie2="," + serie2;
				serie3="," + serie3;
				serie4="," + serie4;
				serie5="," + serie5;
			}
		}
		
		String googleChart = "http://chart.apis.google.com/chart?";
		String chs = "chs=840x280";
		String chf = "&amp;chf=bg,s,efefef";
		String chg = "&amp;chg=20,25";
		String chls = "&amp;chls=5,1,0|5,1,0|5,1,0|5,1,0|5,1,0";
		String chco = "&amp;chco=ff6666,7eb6ff,1b3f8b,cd0000,E38217";
		String chdl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chdl = "&amp;chdl=Tentativas+de+Roubo|Tentativas+de+Furto|Furto|Roubo|Outros";
			} else {
				chdl = "&amp;chdl=Attempt Robbery|Attempt Theft|Theft|Robery|Others";
			}
		String chd = "&amp;chd=t:" + serie1 + "|" + serie2 + "|" + serie3 + "|" + serie4 + "|" + serie5;
		String chds="&amp;chds=0,200";
		//String chtt = "&amp;chtt=Últimos+6+meses";
		String cht = "&amp;cht=lc";
		String chxt = "&amp;chxt=x,y";
		String chxl = "&amp;chxl=0:|" + x.get(5) + "|" + x.get(4) + "|" + x.get(3) + "|" + x.get(2) + "|" + x.get(1) + "|" + x.get(0);
		chxl += "|1:|0|50|100|150|200";
		
		this.gTemporalEstado = googleChart + chs + chf + chg + chls + chco + codeUrl(chdl) + chd + chds + cht + chxt + chxl;
		
		return gTemporalEstado;
	}
	
	public String getgTemporalCidade() {
		String serie1 = "";
		String serie2 = "";
		String serie3 = "";
		String serie4 = "";
		String serie5 = "";
		String ano = "";
		String mes = "";
		String diaFinal = "31";
		String diaInicial = "01";
		List<String> x = new ArrayList<String>();
		for (int i = 0; i <= 5; i++) {
			Calendar calend = java.util.Calendar.getInstance();
			calend.add(Calendar.MONTH, -i);
			ano = Integer.toString(calend.get(Calendar.YEAR));
			mes = Integer.toString(calend.get(Calendar.MONTH)+1);
			String dataInicial = ano + "-" + mes + "-" + diaInicial;
			String dataFinal = ano + "-" + mes + "-" + diaFinal;
			serie1 = this.crimeService.getQtdCrimesByDateIntervalCidade(1, dataInicial, dataFinal, escopoCidade).toString() + serie1;
			serie2 = this.crimeService.getQtdCrimesByDateIntervalCidade(2, dataInicial, dataFinal, escopoCidade).toString() + serie2;
			serie3 = this.crimeService.getQtdCrimesByDateIntervalCidade(3, dataInicial, dataFinal, escopoCidade).toString() + serie3;
			serie4 = this.crimeService.getQtdCrimesByDateIntervalCidade(4, dataInicial, dataFinal, escopoCidade).toString() + serie4;
			serie5 = this.crimeService.getQtdCrimesByDateIntervalCidade(5, dataInicial, dataFinal, escopoCidade).toString() + serie5;
			x.add(mes + "/" + ano);
			if(i<5){
				serie1="," + serie1;
				serie2="," + serie2;
				serie3="," + serie3;
				serie4="," + serie4;
				serie5="," + serie5;
			}
		}
		
		String googleChart = "http://chart.apis.google.com/chart?";
		String chs = "chs=840x280";
		String chf = "&amp;chf=bg,s,efefef";
		String chg = "&amp;chg=20,25";
		String chls = "&amp;chls=5,1,0|5,1,0|5,1,0|5,1,0|5,1,0";
		String chco = "&amp;chco=ff6666,7eb6ff,1b3f8b,cd0000,E38217";
		String chdl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chdl = "&amp;chdl=Tentativas+de+Roubo|Tentativas+de+Furto|Furto|Roubo|Outros";
			} else {
				chdl = "&amp;chdl=Attempt Robbery|Attempt Theft|Theft|Robery|Others";
			}
		String chd = "&amp;chd=t:" + serie1 + "|" + serie2 + "|" + serie3 + "|" + serie4 + "|" + serie5;
		String chds="&amp;chds=0,200";
		//String chtt = "&amp;chtt=Últimos+6+meses";
		String cht = "&amp;cht=lc";
		String chxt = "&amp;chxt=x,y";
		String chxl = "&amp;chxl=0:|" + x.get(5) + "|" + x.get(4) + "|" + x.get(3) + "|" + x.get(2) + "|" + x.get(1) + "|" + x.get(0);
		chxl += "|1:|0|50|100|150|200";
		
		this.gTemporalCidade = googleChart + chs + chf + chg + chls + chco + codeUrl(chdl) + chd + chds + cht + chxt + chxl;
				
		return gTemporalCidade;
	}
	
	public void setgTemporalMundo(String gTemporalMundo) {
		this.gTemporalMundo = gTemporalMundo; 
	}
	
	private void mountGraficoTopTen() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht="cht=bhs";
		String chbh="&amp;chbh=10";
		String chs="&amp;chs=150x115";
		String chd="&amp;chd=t:";
		String chds="&amp;chds=0,";
		//String chtt="&amp;chtt=Top+10";
		//String chco="&amp;chco=cc0000,00aa00";
		String chxt="&amp;chxt=x,y,x";
		String chxl="&amp;chxl=0:|0|";

		String cidades = "1:|";
		String segundoX = "2:||Crimes|";
		StringBuilder qtds = new StringBuilder();
		StringBuilder bcidades = new StringBuilder();
		
		estatisticasCidade = estatisticaService.getTopTenCidades();
		
		long limiteEscala = 0;
		for (Iterator iter = estatisticasCidade.iterator(); iter.hasNext();) {
			EstatisticaCidade eCidade = (EstatisticaCidade) iter.next();
			bcidades.insert(0, eCidade.getNome()+ "(" + eCidade.getQuantidadeCrimes() + ")" + "|");
			if(limiteEscala < eCidade.getQuantidadeCrimes()) {
				limiteEscala = eCidade.getQuantidadeCrimes();
			}
			qtds.append(eCidade.getQuantidadeCrimes());
			if (iter.hasNext())
				qtds.append(",");
		}
		chds += Long.toString(limiteEscala);
		long meioEscala = limiteEscala/2;
		chxl += Long.toString(limiteEscala) + "|";
		bcidades.insert(0,cidades);
		String c = codeUrl(bcidades.toString());
		chxl = chxl + c + segundoX;
		chd = chd + qtds;
		
		topTen = googleChart + cht + chbh + chs + chd + chds + chxt + chxl;
		
	}
	
	public String getgMundo() {
		
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=t";
		String chtm = "&amp;chtm=world";
		String chd = "&amp;chd=t:";
		String chdl = "&amp;chdl=First|Second|Third|Forth";
		String chs = "&amp;chs=440x220";
		String chco = "&amp;chco=ffffff,edf0d4,13390a";
		String chld = "&amp;chld=";
		String siglaPaises = "";
		String valores = "";
				
		List<EstatisticaPais> paises = estatisticaService.getAllPais();
		String s = null;
		for (Iterator iter = paises.iterator(); iter.hasNext();) {
			EstatisticaPais currentPais = (EstatisticaPais) iter.next();
			s = currentPais.getSigla();
			if(!(s == null)){
				if (s.length() == 2) {
					siglaPaises += s;
					valores += currentPais.getQuantidadeCrimes();
			
					if(iter.hasNext()) {
						valores += ",";
					}
				}
			}
		}
		if(valores.lastIndexOf(',') == (valores.length() - 1)) {
			valores = valores.substring(0, valores.length()-1);
		}
		chd += valores;
		chld += siglaPaises;
		gMundo = googleChart + cht + chtm + chd + chs + chco + chld;
		gMundo = codeUrl(gMundo);
						
		return gMundo;
	}
	
	public String getgMeterMundo() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String chs = "chs=440x150";
		String cht="&amp;cht=gom";
		String chco = "&amp;chco=edf0d4,13390a";
		String chd="&amp;chd=t:";
		String chl="&amp;chl=";
		
		List<EstatisticaPais> paises = estatisticaService.getAllPais();
		maiorValorPais = paises.get(0).getQuantidadeCrimes();
		siglaMaiorValorPais = paises.get(0).getSigla();
		for (Iterator iter = paises.iterator(); iter.hasNext();) {
			EstatisticaPais currentPais = (EstatisticaPais) iter.next();
			if (currentPais.getQuantidadeCrimes() > maiorValorPais) {
				maiorValorPais = currentPais.getQuantidadeCrimes();
				siglaMaiorValorPais = currentPais.getSigla();
			}
				
		}
		chd += maiorValorPais + ",0";
		chl = chl + siglaMaiorValorPais + "(" + maiorValorPais + ")" + "|(0)";
		gMeterMundo = googleChart + chs + cht + chco + chd + chl;
		String extra = "edf0d4,13390a";
				
		return gMeterMundo;
	}
	
	public String getgBarrasPais(){
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht="cht=bhs";
		String chbh="&amp;chbh=10";
		String chs="&amp;chs=840x240";
		String chd="&amp;chd=t:";
		String chds="&amp;chds=0,2000";
		//String chtt="&amp;chtt=Top+10";
		//String chco="&amp;chco=cc0000,00aa00";
		String chxt="&amp;chxt=x,y,x";
		String chxl="&amp;chxl=0:|0|200|400|600|800|1000|1200|1400|1600|1800|2000|";

		String tipoCrime = "1:|";
		String qtds = "";
		String segundoX = "|2:||Crimes|";
		System.out.println("Estatisticas:Escopo estados do grafigo barra pais e"+escopoEstados);		
		EstatisticaPais estPais = estatisticaService.getEstatisticaPais(escopoEstados);
		
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			tipoCrime = tipoCrime + "Roubos a Pessoa(" + estPais.getQtdRouboPessoa() + ")|Roubos a Propriedade(" + estPais.getQtdRouboPropriedade() + ")|Furtos a Pessoa(" + estPais.getQtdFurtoPessoa()
			+ ")|Furtos a Propriedade(" + estPais.getQtdFurtoPropriedade() + ")|Tentativas Furto a Pessoa(" + estPais.getQtdTentativaFurtoPessoa() + ")|Tentativas Furto a Propriedade(" + estPais.getQtdTentativaFurtoPropriedade() + 
			")|Tentativas Roubo a Pessoa(" + estPais.getQtdTentativaRouboPessoa() + ")|Tentativas Roubo Propriedade(" + estPais.getQtdTentativaRouboPropriedade() + 
			")|Rixas ou Brigas(" + estPais.getQtdOutroRixas() + ")|Abusos de Autoridade(" + estPais.getQtdOutroAbusoAutoridade() + ")|Vilolência Doméstica(" + estPais.getQtdOutroViolenciaDomestica() + 
			")|Tentativas de Homicídio(" + estPais.getQtdOutroTentativaHomicidio() + ")|Homicídios(" + estPais.getQtdOutroHomicidio() + ")|Latrocínio(" + estPais.getQtdOutroLatrocinio() + ")";
		} else {
			tipoCrime = tipoCrime + "Robbery to a person(" + estPais.getQtdRouboPessoa() + ")|Robbery to a property(" + estPais.getQtdRouboPropriedade() + ")|Theft to a person(" + estPais.getQtdFurtoPessoa()
			+ ")|Theft to a property(" + estPais.getQtdFurtoPropriedade() + ")|Attempt theft to a person(" + estPais.getQtdTentativaFurtoPessoa() + ")|Attempt theft to a property(" + estPais.getQtdTentativaFurtoPropriedade() + 
			")|Attempt robbery to a person(" + estPais.getQtdTentativaRouboPessoa() + ")|Attempt robbery to a property(" + estPais.getQtdTentativaRouboPropriedade() + 
			")|Fights, clash or brawl(" + estPais.getQtdOutroRixas() + ")|Authority abuse(" + estPais.getQtdOutroAbusoAutoridade() + ")|Domestic violence(" + estPais.getQtdOutroViolenciaDomestica() + 
			")|Attempt murder(" + estPais.getQtdOutroTentativaHomicidio() + ")|Murder(" + estPais.getQtdOutroHomicidio() + ")|Robbery followed by murder(" + estPais.getQtdOutroLatrocinio() + ")";
		}
		
		qtds = estPais.getQtdOutroLatrocinio() + "," + estPais.getQtdOutroHomicidio() + "," + estPais.getQtdOutroTentativaHomicidio() +
		"," + estPais.getQtdOutroViolenciaDomestica() + "," + estPais.getQtdOutroAbusoAutoridade() + "," + estPais.getQtdOutroRixas() + "," + estPais.getQtdTentativaRouboPropriedade() +
		"," + estPais.getQtdTentativaRouboPessoa() + "," + estPais.getQtdTentativaFurtoPropriedade() + "," + estPais.getQtdTentativaFurtoPessoa() + "," + estPais.getQtdFurtoPropriedade() + 
		"," + estPais.getQtdFurtoPessoa() + "," + estPais.getQtdRouboPropriedade() + "," + estPais.getQtdRouboPessoa();
		
		
		chxl = chxl + codeUrl(tipoCrime) + segundoX;
		chd = chd + qtds;
		
		gBarrasPais = googleChart + cht + chbh + chs + chd + chds + chxt + chxl;
			
		return gBarrasPais;
		
	}
	
	public String getgBarrasEstado(){
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht="cht=bhs";
		String chbh="&amp;chbh=10";
		String chs="&amp;chs=840x240";
		String chd="&amp;chd=t:";
		String chds="&amp;chds=0,1000";
		//String chtt="&amp;chtt=Top+10";
		//String chco="&amp;chco=cc0000,00aa00";
		String chxt="&amp;chxt=x,y,x";
		String chxl="&amp;chxl=0:|0|100|200|300|400|500|600|700|800|900|1000|";

		String tipoCrime = "1:|";
		String qtds = "";
		String segundoX = "|2:||Crimes|";
				
		EstatisticaEstado estEstado = estatisticaService.getEstatisticaEstado(escopoCidades);
		
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			tipoCrime = tipoCrime + "Roubos a Pessoa(" + estEstado.getQtdRouboPessoa() + ")|Roubos a Propriedade(" + estEstado.getQtdRouboPropriedade() + ")|Furtos a Pessoa(" + estEstado.getQtdFurtoPessoa()
			+ ")|Furtos a Propriedade(" + estEstado.getQtdFurtoPropriedade() + ")|Tentativas Furto a Pessoa(" + estEstado.getQtdTentativaFurtoPessoa() + ")|Tentativas Furto a Propriedade(" + estEstado.getQtdTentativaFurtoPropriedade() + 
			")|Tentativas Roubo a Pessoa(" + estEstado.getQtdTentativaRouboPessoa() + ")|Tentativas Roubo Propriedade(" + estEstado.getQtdTentativaRouboPropriedade() + 
			")|Rixas ou Brigas(" + estEstado.getQtdOutroRixas() + ")|Abusos de Autoridade(" + estEstado.getQtdOutroAbusoAutoridade() + ")|Vilolência Doméstica(" + estEstado.getQtdOutroViolenciaDomestica() + 
			")|Tentativas de Homicídio(" + estEstado.getQtdOutroTentativaHomicidio() + ")|Homicídios(" + estEstado.getQtdOutroHomicidio() + ")|Latrocínio(" + estEstado.getQtdOutroLatrocinio() + ")";
		} else {
			tipoCrime = tipoCrime + "Robbery to a person(" + estEstado.getQtdRouboPessoa() + ")|Robbery to a property(" + estEstado.getQtdRouboPropriedade() + ")|Theft to a person(" + estEstado.getQtdFurtoPessoa()
			+ ")|Theft to a property(" + estEstado.getQtdFurtoPropriedade() + ")|Attempt theft to a person(" + estEstado.getQtdTentativaFurtoPessoa() + ")|Attempt theft to a property(" + estEstado.getQtdTentativaFurtoPropriedade() +
			")|Attempt robbery to a person(" + estEstado.getQtdTentativaRouboPessoa() + ")|Attempt robbery to a property(" + estEstado.getQtdTentativaRouboPropriedade() + 
			")|Fights, clash or brawl(" + estEstado.getQtdOutroRixas() + ")|Authority abuse(" + estEstado.getQtdOutroAbusoAutoridade() + ")|Domestic violence(" + estEstado.getQtdOutroViolenciaDomestica() + 
			")|Attempt murder(" + estEstado.getQtdOutroTentativaHomicidio() + ")|Murder(" + estEstado.getQtdOutroHomicidio() + ")|Robbery followed by murder(" + estEstado.getQtdOutroLatrocinio() + ")";
		}
		
		qtds = estEstado.getQtdOutroLatrocinio() + "," + estEstado.getQtdOutroHomicidio() + "," + estEstado.getQtdOutroTentativaHomicidio() +
		"," + estEstado.getQtdOutroViolenciaDomestica() + "," + estEstado.getQtdOutroAbusoAutoridade() + "," + estEstado.getQtdOutroRixas() + "," + estEstado.getQtdTentativaRouboPropriedade() +
		"," + estEstado.getQtdTentativaRouboPessoa() + "," + estEstado.getQtdTentativaFurtoPropriedade() + "," + estEstado.getQtdTentativaFurtoPessoa() + "," + estEstado.getQtdFurtoPropriedade() +
		"," + estEstado.getQtdFurtoPessoa() + "," + estEstado.getQtdRouboPropriedade() + "," + estEstado.getQtdRouboPessoa();
		
		
		chxl = chxl + codeUrl(tipoCrime) + segundoX;
		chd = chd + qtds;
		
		gBarrasEstado = googleChart + cht + chbh + chs + chd + chds + chxt + chxl;
				
		return gBarrasEstado;
		
	}

	public String getgBarrasCidade(){
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht="cht=bhs";
		String chbh="&amp;chbh=10";
		String chs="&amp;chs=840x240";
		String chd="&amp;chd=t:";
		String chds="&amp;chds=0,1000";
		//String chtt="&amp;chtt=Top+10";
		//String chco="&amp;chco=cc0000,00aa00";
		String chxt="&amp;chxt=x,y,x";
		String chxl="&amp;chxl=0:|0|100|200|300|400|500|600|700|800|900|1000|";

		String tipoCrime = "1:|";
		String qtds = "";
		String segundoX = "|2:||Crimes|";
				
		EstatisticaCidade estCidade = estatisticaService.getEstatisticaCidade(escopoCidade);
		
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			tipoCrime = tipoCrime + "Roubos a Pessoa(" + estCidade.getQtdRouboPessoa() + ")|Roubos a Propriedade(" + estCidade.getQtdRouboPropriedade() + ")|Furtos a Pessoa(" + estCidade.getQtdFurtoPessoa()
			+ ")|Furtos a Propriedade(" + estCidade.getQtdFurtoPropriedade() + ")|Tentativas Furto a Pessoa(" + estCidade.getQtdTentativaFurtoPessoa() + ")|Tentativas Furto a Propriedade(" + estCidade.getQtdTentativaFurtoPropriedade() + 
			")|Tentativas Roubo a Pessoa(" + estCidade.getQtdTentativaRouboPessoa() + ")|Tentativas Roubo Propriedade(" + estCidade.getQtdTentativaRouboPropriedade() + 
			")|Rixas ou Brigas(" + estCidade.getQtdOutroRixas() + ")|Abusos de Autoridade(" + estCidade.getQtdOutroAbusoAutoridade() + ")|Vilolência Doméstica(" + estCidade.getQtdOutroViolenciaDomestica() + 
			")|Tentativas de Homicídio(" + estCidade.getQtdOutroTentativaHomicidio() + ")|Homicídios(" + estCidade.getQtdOutroHomicidio() + ")|Latrocínio(" + estCidade.getQtdOutroLatrocinio() + ")";
		} else {
			tipoCrime = tipoCrime + "Robbery to a person(" + estCidade.getQtdRouboPessoa() + ")|Robbery to a property(" + estCidade.getQtdRouboPropriedade() + ")|Furtos a Pessoa(" + estCidade.getQtdFurtoPessoa()
			+ ")|Theft to a property(" + estCidade.getQtdFurtoPropriedade() + ")|Attempt theft to a person(" + estCidade.getQtdTentativaFurtoPessoa() + ")|Attempt theft to a property(" + estCidade.getQtdTentativaFurtoPropriedade() + 
			")|Attempt robbery to a person(" + estCidade.getQtdTentativaRouboPessoa() + ")|Attempt robbery to a property(" + estCidade.getQtdTentativaRouboPropriedade() + 
			")|Fights, clash or brawl(" + estCidade.getQtdOutroRixas() + ")|Authority abuse(" + estCidade.getQtdOutroAbusoAutoridade() + ")|Domestic violence(" + estCidade.getQtdOutroViolenciaDomestica() + 
			")|Attempt murder(" + estCidade.getQtdOutroTentativaHomicidio() + ")|Murder(" + estCidade.getQtdOutroHomicidio() + ")|Robbery followed by murder(" + estCidade.getQtdOutroLatrocinio() + ")";
		}
		
		qtds = estCidade.getQtdOutroLatrocinio() + "," + estCidade.getQtdOutroHomicidio() + "," + estCidade.getQtdOutroTentativaHomicidio() +
		"," + estCidade.getQtdOutroViolenciaDomestica() + "," + estCidade.getQtdOutroAbusoAutoridade() + "," + estCidade.getQtdOutroRixas() + ","  + estCidade.getQtdTentativaRouboPropriedade() +
		"," + estCidade.getQtdTentativaRouboPessoa() + "," + estCidade.getQtdTentativaFurtoPropriedade() + "," + estCidade.getQtdTentativaFurtoPessoa() + "," + estCidade.getQtdFurtoPropriedade() + 
		"," + estCidade.getQtdFurtoPessoa() + "," + estCidade.getQtdRouboPropriedade() + "," + estCidade.getQtdRouboPessoa();
		
		
		chxl = chxl + codeUrl(tipoCrime) + segundoX;
		chd = chd + qtds;
		
		gBarrasCidade = googleChart + cht + chbh + chs + chd + chds + chxt + chxl;
				
		return gBarrasCidade;
		
	}
	
	public String getgPizzaCrimesMundo() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes no Mundo";
		String chs = "&amp;chs=500x150";
		
		List<EstatisticaPais> paises = estatisticaService.getAllPais();
		
		long roubo = 0;
		long furto = 0; 
		long outro = 0;
		long tentativaFurto = 0;
		long tentativaRoubo = 0;
		
		
		for (Iterator iter = paises.iterator(); iter.hasNext();) {
			EstatisticaPais ePais = (EstatisticaPais) iter.next();
			roubo += ePais.getQuantidadeRoubos();
			furto += ePais.getQuantidadeFurtos();
			outro += ePais.getQuantidadeOutros();
			tentativaFurto += ePais.getQtdTentativaFurto();
			tentativaRoubo += ePais.getQtdTentativaRoubo();
			
		}
		
		long total = roubo + furto + outro + tentativaFurto + tentativaRoubo; 
		
		float pRoubos = (roubo * 100) / (float) total;
		float pFurtos = (furto * 100) / (float) total;
		float pOutros = (outro * 100) / (float) total;
		float pTentativaFurto = (tentativaFurto * 100) / (float) total;
		float pTentativaRoubo = (tentativaRoubo * 100) / (float) total;
		
		String chd = "&amp;chd=t:" + pRoubos + "," + pFurtos + "," + pOutros + "," + pTentativaFurto + "," + pTentativaRoubo;
		
		
		//String chd = "&amp;chd=t:" + roubo + "," + furto + "," + outro;
		String chl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chl = "&amp;chl=Roubos(" + roubo + ")|Furtos(" + furto + ")|Outros(" + outro + ")|Tentativas de Furto(" + tentativaFurto + ")|Tentativas de Roubo(" + tentativaRoubo + ")";
		} else {
			chl = "&amp;chl=Robbery(" + roubo + ")|Theft(" + furto + ")|Others(" + outro + ")|Attempt Theft(" + tentativaFurto + ")|Attempt Robbery(" + tentativaRoubo + ")";
		}
		
		gPizzaCrimesMundo = googleChart + cht + chd + chs + chl;
		
		return gPizzaCrimesMundo;
	}
	
	public String getgPizzaCrimesPais() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por turno em " + escopoEstados;
		String chs = "&amp;chs=500x150";
		
		EstatisticaPais paiseCorrente = estatisticaService.getEstatisticaPais(escopoEstados);
		
		long total = paiseCorrente.getQuantidadeRoubos() + paiseCorrente.getQuantidadeFurtos() + paiseCorrente.getQuantidadeOutros() + paiseCorrente.getQtdTentativaFurto() + paiseCorrente.getQtdTentativaRoubo();
		
		float pRoubos = (paiseCorrente.getQuantidadeRoubos() * 100) / (float) total;
		float pFurtos = (paiseCorrente.getQuantidadeFurtos() * 100) / (float) total;
		float pOutros = (paiseCorrente.getQuantidadeOutros() * 100) / (float) total;
		float pTentativaFurto = (paiseCorrente.getQtdTentativaFurto() * 100) / (float) total;
		float pTentativaRoubo = (paiseCorrente.getQtdTentativaRoubo() * 100) / (float) total;
		
		String chd = "&amp;chd=t:" + pRoubos + "," + pFurtos + "," + pOutros + "," + pTentativaFurto + "," + pTentativaRoubo;
				
		//String chd = "&amp;chd=t:" + paiseCorrente.getQuantidadeRoubos() + "," + paiseCorrente.getQuantidadeFurtos() + "," + paiseCorrente.getQuantidadeOutros();
		String chl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chl = "&amp;chl=Roubos(" + paiseCorrente.getQuantidadeRoubos()+ ")|Furtos(" + paiseCorrente.getQuantidadeFurtos() + ")|Outros(" + paiseCorrente.getQuantidadeOutros() + ")|Tentativas de Furto(" + paiseCorrente.getQtdTentativaFurto() + ")|Tentativas de Roubo(" + paiseCorrente.getQtdTentativaRoubo() + ")";
		} else {
			chl = "&amp;chl=Robbery(" + paiseCorrente.getQuantidadeRoubos()+ ")|Theft(" + paiseCorrente.getQuantidadeFurtos() + ")|Others(" + paiseCorrente.getQuantidadeOutros() + ")|Attempt Theft(" + paiseCorrente.getQtdTentativaFurto() + ")|Attempt Robbery(" + paiseCorrente.getQtdTentativaRoubo() + ")";
		}
		//chtt = codeUrl(chtt);
		gPizzaCrimesPais = googleChart + cht + chd + chs + chl;
		
		return gPizzaCrimesPais;
	}
	
	public String getgPizzaCrimesEstado() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes em " + escopoCidades;
		String chs = "&amp;chs=500x150";
		
		EstatisticaEstado estadoCorrente = estatisticaService.getEstatisticaEstado(escopoCidades);
		
		long total = estadoCorrente.getQuantidadeRoubos() + estadoCorrente.getQuantidadeFurtos() + estadoCorrente.getQuantidadeOutros() + estadoCorrente.getQtdTentativaFurto() + estadoCorrente.getQtdTentativaRoubo();
		
		float pRoubos = (estadoCorrente.getQuantidadeRoubos() * 100) / (float) total;
		float pFurtos = (estadoCorrente.getQuantidadeFurtos() * 100) / (float) total;
		float pOutros = (estadoCorrente.getQuantidadeOutros() * 100) / (float) total;
		float pTentativaFurto = (estadoCorrente.getQtdTentativaFurto() * 100) / (float) total;
		float pTentativaRoubo = (estadoCorrente.getQtdTentativaRoubo() * 100) / (float) total;
		
		String chd = "&amp;chd=t:" + pRoubos + "," + pFurtos + "," + pOutros + "," + pTentativaFurto + "," + pTentativaRoubo;
				
		//String chd = "&amp;chd=t:" + estadoCorrente.getQuantidadeRoubos() + "," + estadoCorrente.getQuantidadeFurtos() + "," + estadoCorrente.getQuantidadeOutros();
		String chl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chl = "&amp;chl=Roubos(" + estadoCorrente.getQuantidadeRoubos()+ ")|Furtos(" + estadoCorrente.getQuantidadeFurtos() + ")|Outros(" + estadoCorrente.getQuantidadeOutros() + ")|Tentativas de Furto(" + estadoCorrente.getQtdTentativaFurto() + ")|Tentativas de Roubo(" + estadoCorrente.getQtdTentativaRoubo() + ")";
		} else {
			chl = "&amp;chl=Robbery(" + estadoCorrente.getQuantidadeRoubos()+ ")|Theft(" + estadoCorrente.getQuantidadeFurtos() + ")|Others(" + estadoCorrente.getQuantidadeOutros() + ")|Attempt Theft(" + estadoCorrente.getQtdTentativaFurto() + ")|Attempt Robbery(" + estadoCorrente.getQtdTentativaRoubo() + ")";
		}
		//chtt = codeUrl(chtt);
		gPizzaCrimesEstado = googleChart + cht + chd + chs + chl;
		
		return gPizzaCrimesEstado;
	}
	
	public String getgPizzaCrimesCidade() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes em " + escopoCidade;
		String chs = "&amp;chs=485x150";
		
		EstatisticaCidade cidadeCorrente = estatisticaService.getEstatisticaCidade(escopoCidade);
		
		long total = cidadeCorrente.getQuantidadeRoubos() + cidadeCorrente.getQuantidadeFurtos() + cidadeCorrente.getQuantidadeOutros() + cidadeCorrente.getQtdTentativaFurto() + cidadeCorrente.getQtdTentativaRoubo();
		
		float pRoubos = (cidadeCorrente.getQuantidadeRoubos() * 100) / (float) total;
		float pFurtos = (cidadeCorrente.getQuantidadeFurtos() * 100) / (float) total;
		float pOutros = (cidadeCorrente.getQuantidadeOutros() * 100) / (float) total;
		float pTentativaFurto = (cidadeCorrente.getQtdTentativaFurto() * 100) / (float) total;
		float pTentativaRoubo = (cidadeCorrente.getQtdTentativaRoubo() * 100) / (float) total;
				
		String chd = "&amp;chd=t:" + pRoubos + "," + pFurtos + "," + pOutros + "," + pTentativaFurto + "," + pTentativaRoubo;
				
		//String chd = "&amp;chd=t:" + cidadeCorrente.getQuantidadeRoubos() + "," + cidadeCorrente.getQuantidadeFurtos() + "," + cidadeCorrente.getQuantidadeOutros();
		String chl;
		String local = locale.toString();
		if(local.equalsIgnoreCase("pt_BR")) {
			chl = "&amp;chl=Roubos(" + cidadeCorrente.getQuantidadeRoubos()+ ")|Furtos(" + cidadeCorrente.getQuantidadeFurtos() + ")|Outros(" + cidadeCorrente.getQuantidadeOutros() + ")|Tentativas de Furto(" + cidadeCorrente.getQtdTentativaFurto() + ")|Tentativas de Roubo(" + cidadeCorrente.getQtdTentativaRoubo() + ")";
		} else {
			chl = "&amp;chl=Robbery(" + cidadeCorrente.getQuantidadeRoubos()+ ")|Theft(" + cidadeCorrente.getQuantidadeFurtos() + ")|Others(" + cidadeCorrente.getQuantidadeOutros() + ")|Attempt Theft(" + cidadeCorrente.getQtdTentativaFurto() + ")|Attempt Robbery(" + cidadeCorrente.getQtdTentativaRoubo() + ")";
		}
		//chtt = codeUrl(chtt);
		gPizzaCrimesCidade = googleChart + cht + chd + chs + chl;
		
		return gPizzaCrimesCidade;
	}

	
	public String getgPizzaTurnoMundo() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por turno no Mundo";
		String chs = "&amp;chs=430x150";
		
		List paises = estatisticaService.getAllPais();
		long turnoUm = 0;
		long turnoDois = 0;
		long turnoTres = 0;
		long turnoQuatro = 0;
		
		for (Iterator iter = paises.iterator(); iter.hasNext();) {
			EstatisticaPais ePais = (EstatisticaPais) iter.next();
			turnoUm += ePais.getQtdTurnoUm();
			turnoDois += ePais.getQtdTurnoDois();
			turnoTres += ePais.getQtdTurnoTres();
			turnoQuatro += ePais.getQtdTurnoQuatro();
		}
		
		long total = turnoUm + turnoDois + turnoTres + turnoQuatro;
		
		float pTurnoUm = (turnoUm * 100) / (float) total;
		float pTurnoDois = (turnoDois * 100) / (float) total;
		float pTurnoTres = (turnoTres * 100) / (float) total;
		float pTurnoQuatro = (turnoQuatro * 100) / (float) total; 
		
		//NumberFormat format = NumberFormat.getInstance();
		//format.setMaximumFractionDigits(2);
		//Float pTUm = new Float(format.format(pTurnoUm));
		//Float pTDois = new Float(format.format(pTurnoDois));
		//Float pTTres = new Float(format.format(pTurnoTres));
		//Float pTQuatro = new Float(format.format(pTurnoQuatro));
		
		
		String chd = "&amp;chd=t:" + pTurnoUm + "," + pTurnoDois + "," + pTurnoTres + "," + pTurnoQuatro;
		String chl = "&amp;chl=00:00-06:00(" + turnoUm + ")|06:00-12:00(" + turnoDois + ")|12:00-18:00(" + turnoTres + ")|18:00-24:00(" + turnoQuatro + ")";
		
		gPizzaTurnoMundo = googleChart + cht + chd + chs + chl;
		
		return gPizzaTurnoMundo;
	}
	
	public String getgPizzaTurnoPais() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por turno em " + escopoEstados;
		String chs = "&amp;chs=430x150";
		
		EstatisticaPais paiseCorrente = estatisticaService.getEstatisticaPais(escopoEstados);
		
		long total = paiseCorrente.getQtdTurnoUm() + paiseCorrente.getQtdTurnoDois() + paiseCorrente.getQtdTurnoTres() + paiseCorrente.getQtdTurnoQuatro();
		
		float pTurnoUm = (paiseCorrente.getQtdTurnoUm() * 100) / (float) total;
		float pTurnoDois = (paiseCorrente.getQtdTurnoDois() * 100) / (float) total;
		float pTurnoTres = (paiseCorrente.getQtdTurnoTres() * 100) / (float) total;
		float pTurnoQuatro = (paiseCorrente.getQtdTurnoQuatro() * 100) / (float) total;
				
		String chd = "&amp;chd=t:" + pTurnoUm + "," + pTurnoDois + "," + pTurnoTres + "," + pTurnoQuatro;
		String chl = "&amp;chl=00:00-06:00(" + paiseCorrente.getQtdTurnoUm()+ ")|06:00-12:00(" + paiseCorrente.getQtdTurnoDois() + ")|12:00-18:00(" + paiseCorrente.getQtdTurnoTres() + ")|18:00-24:00(" + paiseCorrente.getQtdTurnoQuatro() + ")";
		//chtt = codeUrl(chtt);
		gPizzaTurnoPais = googleChart + cht + chd + chs + chl;
		
		return gPizzaTurnoPais;
	}
	
	public String getgPizzaTurnoEstado() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por turno em " + escopoCidades;
		String chs = "&amp;chs=430x150";
		
		EstatisticaEstado estadoCorrente = estatisticaService.getEstatisticaEstado(escopoCidades);
		
		long total = estadoCorrente.getQtdTurnoUm() + estadoCorrente.getQtdTurnoDois() + estadoCorrente.getQtdTurnoTres() + estadoCorrente.getQtdTurnoQuatro();
		
		float pTurnoUm = (estadoCorrente.getQtdTurnoUm() * 100) / (float) total;
		float pTurnoDois = (estadoCorrente.getQtdTurnoDois() * 100) / (float) total;
		float pTurnoTres = (estadoCorrente.getQtdTurnoTres() * 100) / (float) total;
		float pTurnoQuatro = (estadoCorrente.getQtdTurnoQuatro() * 100) / (float) total;
				
		String chd = "&amp;chd=t:" + pTurnoUm + "," + pTurnoDois + "," + pTurnoTres + "," + pTurnoQuatro;
				
		//String chd = "&amp;chd=t:" + estadoCorrente.getQtdTurnoUm() + "," + estadoCorrente.getQtdTurnoDois() + "," + estadoCorrente.getQtdTurnoTres() + "," + estadoCorrente.getQtdTurnoQuatro();
		String chl = "&amp;chl=00:00-06:00(" + estadoCorrente.getQtdTurnoUm()+ ")|06:00-12:00(" + estadoCorrente.getQtdTurnoDois() + ")|12:00-18:00(" + estadoCorrente.getQtdTurnoTres() + ")|18:00-24:00(" + estadoCorrente.getQtdTurnoQuatro() + ")";
		
		//chtt = codeUrl(chtt);
		gPizzaTurnoEstado = googleChart + cht + chd + chs + chl;
		
		
		return gPizzaTurnoEstado;
	}
	
	public String getgPizzaTurnoCidade() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por turno em " + escopoCidade;
		String chs = "&amp;chs=430x150";
		
		EstatisticaCidade cidadeCorrente = estatisticaService.getEstatisticaCidade(escopoCidade);
		
		long total = cidadeCorrente.getQtdTurnoUm() + cidadeCorrente.getQtdTurnoDois() + cidadeCorrente.getQtdTurnoTres() + cidadeCorrente.getQtdTurnoQuatro();
		
		float pTurnoUm = (cidadeCorrente.getQtdTurnoUm() * 100) / (float) total;
		float pTurnoDois = (cidadeCorrente.getQtdTurnoDois() * 100) / (float) total;
		float pTurnoTres = (cidadeCorrente.getQtdTurnoTres() * 100) / (float) total;
		float pTurnoQuatro = (cidadeCorrente.getQtdTurnoQuatro() * 100) / (float) total;
				
		String chd = "&amp;chd=t:" + pTurnoUm + "," + pTurnoDois + "," + pTurnoTres + "," + pTurnoQuatro;
				
		//String chd = "&amp;chd=t:" + cidadeCorrente.getQtdTurnoUm() + "," + cidadeCorrente.getQtdTurnoDois() + "," + cidadeCorrente.getQtdTurnoTres() + "," + cidadeCorrente.getQtdTurnoQuatro();
		String chl = "&amp;chl=00:00-06:00(" + cidadeCorrente.getQtdTurnoUm()+ ")|06:00-12:00(" + cidadeCorrente.getQtdTurnoDois() + ")|12:00-18:00(" + cidadeCorrente.getQtdTurnoTres() + ")|18:00-24:00(" + cidadeCorrente.getQtdTurnoQuatro() + ")";
		
		//chtt = codeUrl(chtt);
		gPizzaTurnoCidade = googleChart + cht + chd + chs + chl;
		
		return gPizzaTurnoCidade;
	}
	
	private String codeUrl(String url){
		url = url.replace("Ã", "%C3%83");
		url = url.replace("ã", "%C3%A3");
		url = url.replace("Á", "%C3%81");
		url = url.replace("á", "%C3%A1");
		url = url.replace("Â", "%C3%82");
		url = url.replace("â", "%C3%A2");
		url = url.replace("É", "%C3%89");
		url = url.replace("é", "%C3%A9");
		url = url.replace("Ê", "%C3%8A");
		url = url.replace("ê", "%C3%AA");
		url = url.replace("Î", "%C3%8E");
		url = url.replace("î", "%C3%AE");
		url = url.replace("Í", "%C3%8D");
		url = url.replace("í", "%C3%AD");
		url = url.replace("Õ", "%C3%95");
		url = url.replace("õ", "%C3%B5");
		url = url.replace("Ô", "%C3%94");
		url = url.replace("ô", "%C3%B4");
		url = url.replace("Ó", "%C3%93");
		url = url.replace("ó", "%C3%B3");
		url = url.replace("Û", "%C3%9B");
		url = url.replace("û", "%C3%BB");
		url = url.replace("Ú", "%C3%9A");
		url = url.replace("ú", "%C3%BA");
		url = url.replace("À", "%C3%80");
		url = url.replace("à", "%C3%A0");
		url = url.replace("Ü", "%C3%9C");
		url = url.replace("ü", "%C3%BC");
		url = url.replace("Ç", "%C3%87");
		url = url.replace("ç", "%C3%A7");
		url = url.replace("Ù", "%C3%99");
		url = url.replace("ù", "%C3%B9");
		url = url.replace("Ö", "%C3%96");
		url = url.replace("ö", "%C3%B6");
		return url;
	}
	
	public String getTopTen() {
		mountGraficoTopTen();
		return topTen;
	}
	
	public String getTopFiveEstados() {
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht="cht=bhs";
		String chbh="&amp;chbh=10";
		String chs="&amp;chs=150x115";
		String chd="&amp;chd=t:";
		String chds="&amp;chds=0,";
		//String chtt="&amp;chtt=Top+10";
		//String chco="&amp;chco=cc0000,00aa00";
		String chxt="&amp;chxt=x,y,x";
		String chxl="&amp;chxl=0:|0|";

		String estados = "1:|";
		String segundoX = "2:||Crimes|";
		StringBuilder qtds = new StringBuilder();
		StringBuilder bestados = new StringBuilder();
		
		fiveEstados = estatisticaService.getTopFiveEstados();
		
		
		long limiteEscala = 0;
		for (Iterator iter = fiveEstados.iterator(); iter.hasNext();) {
			EstatisticaEstado eEstado = (EstatisticaEstado) iter.next();
			if (eEstado.getQuantidadeCrimes().intValue()>=1000)
				bestados.insert(0, eEstado.getSigla()+ "(" + eEstado.getQuantidadeCrimes() + ")" + "|");
			else
				bestados.insert(0, eEstado.getSigla()+ "(0" + eEstado.getQuantidadeCrimes() + ")" + "|");
			if(limiteEscala < eEstado.getQuantidadeCrimes()) {
				limiteEscala = eEstado.getQuantidadeCrimes();
			}
			qtds.append(eEstado.getQuantidadeCrimes());
			if (iter.hasNext())
				qtds.append(",");
		}
		chds += Long.toString(limiteEscala);
		long meioEscala = limiteEscala/2;
		chxl += Long.toString(limiteEscala) + "|";
		bestados.insert(0,estados);
		String c = codeUrl(bestados.toString());
		chxl = chxl + c + segundoX;
		chd = chd + qtds;
		
		topFiveEstados = googleChart + cht + chbh + chs + chd + chds + chxt + chxl;
		
		return topFiveEstados;
	}



	public void setTopTen(String topTen) {
		this.topTen = topTen;
	}

	public String getEscopoCidades() {
		return escopoCidades;
	}

	public void setEscopoCidades(String escopoCidades) {
		this.escopoCidades = escopoCidades;
	}

	public String getEscopoEstados() {
		return escopoEstados;
	}

	public void setEscopoEstados(String escopoEstados) {
		this.escopoEstados = escopoEstados;
	}

	public String getEscopoCidade() {
		return escopoCidade;
	}

	public void setEscopoCidade(String escopoCidade) {
		this.escopoCidade = escopoCidade;
	}

	public String getGraficoLinha() {
		return graficoLinha;
	}

	public void setGraficoLinha(String graficoLinha) {
		this.graficoLinha = graficoLinha;
	}

	public String getEscopoGrafico() {
		return escopoGrafico;
	}

	public void setEscopoGrafico(String escopoGrafico) {
		this.escopoGrafico = escopoGrafico;
	}
	
    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setEstatisticaService(EstatisticaService estatisticaService) {
		this.estatisticaService = estatisticaService;
	}
    
    public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}
    
    public CrimeService getCrimeService() {
    	return this.crimeService;
    }

	public List<EstatisticaPais> getePais() {
        
    	ePais = estatisticaService.getAllPais();
         
        
        if(!ePais.isEmpty()) {
        	
        	Comparator comparator = null;
        
        	comparator = new BeanComparator(sortColumn);
        
        	if (!ascending) {
        		comparator = new ReverseComparator(comparator);
        	}
        	
        	Collections.sort(ePais, comparator);
        }
        
        return ePais;
    }
	
	public List geteEstado() {
        
		eEstado = estatisticaService.getEstadosDoPais(escopoEstados);
         
        
        if(!eEstado.isEmpty()) {
        	
        	Comparator comparator = null;
        
        	comparator = new BeanComparator(sortColumn);
        
        	if (!ascending) {
        		comparator = new ReverseComparator(comparator);
        	}
        	
        	Collections.sort(eEstado, comparator);
        }
        
        return eEstado;
    }
	
	public List geteCidade() {
        
		eCidade = estatisticaService.getCidatesDoEstado(escopoCidades);
         
        
        if(!eCidade.isEmpty()) {
        	
        	Comparator comparator = null;
        
        	comparator = new BeanComparator(sortColumn);
        
        	if (!ascending) {
        		comparator = new ReverseComparator(comparator);
        	}
        	
        	Collections.sort(eCidade, comparator);
        }
        
        return eCidade;
    }
	
	public List<EstatisticaCidade> getcidadesSemLatLong() {
		List <EstatisticaCidade>TodasCidades = estatisticaService.getAllCidade();
		cidadesSemLatLong.clear();
		for (EstatisticaCidade c : TodasCidades) {
			if (c.getLatitude() == null || c.getLongitude() == null) {
				cidadesSemLatLong.add(c);
			}
		}
		return cidadesSemLatLong;
	}
	
	public List<EstatisticaEstado> getestadosSemLatLong() {
		List <EstatisticaEstado>TodosEstados = estatisticaService.getAllEstado();
		estadosSemLatLong.clear();
		for (EstatisticaEstado e : TodosEstados) {
			if (e.getLatitude() == null || e.getLongitude() == null) {
				estadosSemLatLong.add(e);
			}
		}
		return estadosSemLatLong;
	}
	
	public List<EstatisticaPais> getpaisesSemLatLong() {
		List <EstatisticaPais>TodosPaises = estatisticaService.getAllPais();
		paisesSemLatLong.clear();
		for (EstatisticaPais p : TodosPaises) {
			if (p.getLatitude() == null || p.getLongitude() == null) {
				paisesSemLatLong.add(p);
			}
		}
		return paisesSemLatLong;
	}
	
	public boolean getacesso() {
		acesso = true;
		//TODO verificar se perfil e de administrador
		if (expirouSessao()) {
			
			acesso = false;
		}
		else if (!isAdmin()){
				addMessage("webapp.pagina.nao.permitida", "");
			
			acesso = false;
		}
		return acesso;
	}
	
    public void saveCidadeLatLong() {
    	EstatisticaCidade c = (EstatisticaCidade)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("cidade");
    	this.estatisticaService.updateEstatisticaCidade(c);
    }
    
    public void saveEstadoLatLong() {
    	EstatisticaEstado e = (EstatisticaEstado)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("estado");
    	this.estatisticaService.updateEstatisticaEstado(e);
    }
    
    public void savePaisLatLong() {
    	EstatisticaPais p = (EstatisticaPais)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("pais");
    	this.estatisticaService.updateEstatisticaPais(p);
    }
	

	public String getgraficoAgrupador() {
		acaoAgrupador();
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por Cidade";
		String chs = "&amp;chs=395x130";
		String chd = "&amp;chd=t:";
		String chl = "&amp;chl=";
		
		siglaEstadoAgrupador=siglaEstadoAgrupador.substring(0,2);
		List<EstatisticaCidade> cidades = estatisticaService.getCidatesDoEstado(siglaEstadoAgrupador);
		
		for (Iterator iter = cidades.iterator(); iter.hasNext();) {
			EstatisticaCidade currentCidade = (EstatisticaCidade) iter.next();
			chd += currentCidade.getQuantidadeCrimes(); 
			chl += currentCidade.getNome() + "(" + currentCidade.getQuantidadeCrimes() + ")";
			if(iter.hasNext()) {
				chd += ",";
				chl += "|";
			}
		}
		
		graficoAgrupador = googleChart + cht + chd + chs + codeUrl(chl);
		
		return graficoAgrupador;
	}
	
	public String getgCidadesEstado(){
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por Cidade";
		String chs = "&amp;chs=813x325";
		String chd = "&amp;chd=t:";
		String chl = "&amp;chl=";
		
		
		List<EstatisticaCidade> cidades = estatisticaService.getCidatesDoEstado(escopoCidades);
		long total = 0;
		for (Iterator iter = cidades.iterator(); iter.hasNext();) {
			EstatisticaCidade currentCidade = (EstatisticaCidade) iter.next();
			total += currentCidade.getQuantidadeCrimes();
		}
		float crimes = 0;
		for (Iterator iter = cidades.iterator(); iter.hasNext();) {
			EstatisticaCidade currentCidade = (EstatisticaCidade) iter.next();
			crimes = (currentCidade.getQuantidadeCrimes() * 100) / (float) total;
			chd += crimes; 
			chl += currentCidade.getNome() + "(" + currentCidade.getQuantidadeCrimes() + ")";
			if(iter.hasNext()) {
				chd += ",";
				chl += "|";
			}
		}
		
		gCidadesEstado = googleChart + cht + chd + chs + codeUrl(chl);
		
		return gCidadesEstado;
	}
	
	public String getgEstadosPais(){
		String googleChart = "http://chart.apis.google.com/chart?";
		String cht = "cht=p3";
		//String chtt = "&amp;chtt=Crimes por Cidade";
		String chs = "&amp;chs=650x260";
		String chd = "&amp;chd=t:";
		String chl = "&amp;chl=";
		
		
		List<EstatisticaEstado> estados = estatisticaService.getEstadosDoPais(escopoEstados);
		
		long total = 0;
		for (Iterator iter = estados.iterator(); iter.hasNext();) {
			EstatisticaEstado currentEstado = (EstatisticaEstado) iter.next();
			total += currentEstado.getQuantidadeCrimes();
		}
		float crimes = 0;
		
		for (Iterator iter = estados.iterator(); iter.hasNext();) {
			EstatisticaEstado currentEstado = (EstatisticaEstado) iter.next();
			crimes = (currentEstado.getQuantidadeCrimes() * 100) / (float) total;
			chd += crimes; 
			chl += currentEstado.getSigla() + "(" + currentEstado.getQuantidadeCrimes() + ")";
			if(iter.hasNext()) {
				chd += ",";
				chl += "|";
			}
		}
		
		gEstadosPais = googleChart + cht + chd + chs + codeUrl(chl);
		
		//System.out.println(gEstadosPais);
		
		return gEstadosPais;
	}
	
    public String acaoPais() {
    	EstatisticaPais p = (EstatisticaPais)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("pais");
    	this.escopoEstados = p.getSigla();
    	return "pais";
    }

    public String acaoEstado() {
    	EstatisticaEstado e = (EstatisticaEstado)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("estados");
    	this.escopoCidades = e.getSigla();
    	return "estado";
    }
    
    public String acaoCidade() {
    	EstatisticaCidade c = (EstatisticaCidade)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute("cidades");
    	this.escopoCidade = c.getNome();
    	return "cidade";
    }
    
    public void acaoAgrupador() {
    	siglaEstadoAgrupador = (String)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("agrupadorEstado");
    }

	public String getSiglaEstadoAgrupador() {
		return siglaEstadoAgrupador;
	}

	public void setSiglaEstadoAgrupador(String siglaEstadoAgrupador) {
		this.siglaEstadoAgrupador = siglaEstadoAgrupador;
	}


}
