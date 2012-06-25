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
import org.wikicrimes.model.EstatisticaEstado;
import org.wikicrimes.model.Relato;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.web.FiltroForm;

public class ServletManipuladorTXT extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3076147554242159095L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");

		PrintWriter writer = response.getWriter();

		executaManipulacaoTXT(writer, request);
		writer.close();

	}


	

	private void executaManipulacaoTXT(PrintWriter writer,
			HttpServletRequest request) {

		HttpSession sessao = request.getSession();
		FiltroForm filtroForm = (FiltroForm) sessao.getAttribute("filtroForm");
		String tipoCrime = request.getParameter("tc");
		String tipoVitima = request.getParameter("tv");
		String tipoLocal = request.getParameter("tl"); // Data - Ex.: 01,01,2008
		String dataInicial = request.getParameter("di");
		String dataFinal = request.getParameter("df"); // Horario - Ex.: 5
		String horarioInicial = request.getParameter("hi");
		String horarioFinal = request.getParameter("hf");
		String zoom = request.getParameter("z");
		String idCrime = request.getParameter("ic");
		String entidadeCertificadora = request.getParameter("ec");
		String confirmadosPositivamente = request.getParameter("cp");
		String norte = request.getParameter("n");
		String sul = request.getParameter("s");
		String leste = request.getParameter("e");
		String oeste = request.getParameter("w");
		String ignoraData = request.getParameter("id");
		String idRelato = request.getParameter("rl");
		
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
					norte, sul, leste, oeste, null, null);
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
		constroiTXTCrimes(crimes, writer);
	}

	
	private void constroiTXTEstados(List<EstatisticaEstado> estados,
			PrintWriter writer) {

		PrintWriter saida = null;

		saida = new PrintWriter(writer, true);

		String linha = "";

		for (EstatisticaEstado ee : estados) {

			linha = ee.getSigla() + "|" + ee.getQuantidadeCrimes() + "|"
					+ ee.getLatitude() + "|" + ee.getLongitude();
			saida.println(linha);

		}

		saida.close();
	}

	private void constroiTXTCrimes(List<BaseObject> crimes, PrintWriter writer) {

		PrintWriter saida = null;

		saida = new PrintWriter(writer, true);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String linha = "";
		Crime crime = null; // L� do banco e escreve no PrintWriter
		Relato relato = null; // L� do banco e escreve no PrintWriter
		for (int i = 0; i < crimes.size(); i++) {
			if (crimes.get(i) instanceof Crime) {
				crime = (Crime) crimes.get(i);
				linha = crime.getChave() + "|"
						+ crime.getTipoCrime().getIdTipoCrime() + "|"
						+ crime.getLatitude() + "|" + crime.getLongitude() + "|"
						+ crime.getIdCrime(); 
				if (crime.getTipoVitima() != null) {
					linha += "|" + crime.getTipoVitima().getIdTipoVitima();
				} else {
					linha += "|";
				}
				linha += "|" + sdf.format(crime.getData());
				
			}
			else {
				relato = (Relato) crimes.get(i);
				
				linha = relato.getChave() + "|"
						+ relato.getTipoRelato() + "|"
						+ relato.getLatitude() + "|" + relato.getLongitude() + "|"
						+ relato.getIdRelato(); 
				if (relato.getSubTipoRelato() != null) {
					linha += "|" + relato.getSubTipoRelato();
				} else {
					linha += "|";
				}
				linha += "|" + sdf.format(relato.getDataHoraRegistro());
				
				
			}
			
			/*if (i == crimes.size() - 1)
				saida.print(linha);
			else*/
				saida.println(linha);
		}

		saida.close();
	}

	

}
