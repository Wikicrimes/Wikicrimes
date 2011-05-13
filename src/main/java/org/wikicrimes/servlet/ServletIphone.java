package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.web.FiltroForm;

public class ServletIphone extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3076147554242159095L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		//response.setContentType("text/plain");

		String acao = request.getParameter("acao");

		if(acao != null){

			if(acao.equals("getMarkerDetail")){

				PrintWriter writer = response.getWriter();
				System.out.println("Id do Crime: "+request.getParameter("idCrime"));
				writer.println(getMarkerData(request.getParameter("idCrime")));
				writer.close();

			}else{

				if(acao.equals("getMarkers")){

					PrintWriter writer = response.getWriter();

					StringBuilder stringCrimes = new StringBuilder();
					writer.println(constroiStringCrimes(executaManipulacaoIphone(stringCrimes, request), stringCrimes));
					writer.close();
				}
			}
		}
	}

	private List<BaseObject> executaManipulacaoIphone(StringBuilder stringCrimes,
			HttpServletRequest request) {

		HttpSession sessao = request.getSession();
		FiltroForm filtroForm = (FiltroForm) sessao.getAttribute("filtroForm");
		String tipoCrime = "";
		String tipoVitima = "";
		String tipoLocal = ""; // Data - Ex.: 01,01,2008
		String dataInicial = request.getParameter("di");
		String dataFinal = request.getParameter("df"); // Horario - Ex.: 5
		String horarioInicial = "";
		String horarioFinal = "";
		String idCrime = "";
		String entidadeCertificadora = "";
		String confirmadosPositivamente = "";
		String norte = request.getParameter("n");
		String sul = request.getParameter("s");
		String leste = request.getParameter("e");
		String oeste = request.getParameter("w");
		String ignoraData = "";
		String idRelato = "";

		List<BaseObject> crimes = null;

		if (filtroForm == null) {
			filtroForm = new FiltroForm();
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());  
			CrimeService crimeService = (CrimeService)springContext.getBean("crimeService");
			filtroForm.setCrimeService(crimeService);

		} 
		crimes = filtroForm.getCrimesFiltrados(tipoCrime, tipoVitima,
				tipoLocal, horarioInicial, horarioFinal, dataInicial,
				dataFinal, entidadeCertificadora, confirmadosPositivamente,
				norte, sul, leste, oeste, null);
		String dataMaisAntiga = dataInicial;
		if(crimes.size()>0){
			Crime crimeMaisAntigo =(Crime) crimes.get(crimes.size()-1);

			String pattern = "dd,MM,yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try{
				dataMaisAntiga = sdf.format(crimeMaisAntigo.getData());
			}catch (Exception e) {
				dataMaisAntiga = dataInicial;
			}
		}	
		crimes.addAll(filtroForm.getRelatosFiltrados(norte, sul, leste, oeste,dataMaisAntiga,dataFinal, true));

		if (idCrime != null ) {
			if (!idCrime.equals("undefined") && !idCrime.equals("")) {
				Crime crime = filtroForm.getCrime(idCrime);
				if (crime == null){

					crime = filtroForm.getCrime(Long.parseLong(idCrime));

				}
				if(!crimes.contains(crime))
					crimes.add(crime);
			}
		}
		if (idRelato != null ) {
			if (!idRelato.equals("undefined") && !idRelato.equals("")) {
				Relato relato = filtroForm.getRelato(idRelato);
				//		
				crimes.add(relato);
			}
		}
		return crimes;
	}


	private StringBuilder constroiStringCrimes(List<BaseObject> crimes, StringBuilder stringCrimes) {

		String linha = "";
		Crime crime = null; // L� do banco e escreve no StringBuilder
		for (int i = 0; i < crimes.size(); i++) {
			if (crimes.get(i) instanceof Crime) {
				crime = (Crime) crimes.get(i);
				linha = crime.getTipoCrime().getIdTipoCrime() + "|"
				+ crime.getLatitude() + "|" + crime.getLongitude() + "|"
				+ crime.getChave() + "\n"; 

			}

			stringCrimes.append(linha);
		}

		return stringCrimes;
		//	saida.close();
	}

	private StringBuilder getMarkerData(String idCrime){

		StringBuilder detailCrime = new StringBuilder();
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext()); 
		CrimeService crimeService = (CrimeService)springContext.getBean("crimeService");
		Crime crime = crimeService.getCrime(idCrime);
		detailCrime.append(crime.getTipoCrime().getNome() + "|" + crime.getData() + "|" + crime.getHorario() + "\n");

		return detailCrime;
	}
}