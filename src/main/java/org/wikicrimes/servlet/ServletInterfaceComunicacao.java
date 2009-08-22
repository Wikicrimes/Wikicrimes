package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.dao.GenericCrudDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoLocalizacaoRelato;
import org.wikicrimes.model.TipoViolenciaEscolaRelato;
import org.wikicrimes.model.UsuarioCelular;

public class ServletInterfaceComunicacao extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4059683118894911147L;
	
	private static ApplicationContext ctx;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		response.setContentType("text/plain");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");
		
		PrintWriter out = response.getWriter();
		
		String dadosCrime = request.getParameter("dados_crime");
		String dadosUsuario = request.getParameter("dados_usuario");
		ctx = WebApplicationContextUtils.getWebApplicationContext(this
				.getServletContext());
		GenericCrudDao genericCrudDao = (GenericCrudDao) ctx
		.getBean("opensocialDao");
		if(dadosCrime != null && dadosCrime != "undefined" && !dadosCrime.equalsIgnoreCase(""))
			out.print(tratarDadosCrime(dadosCrime, genericCrudDao));
		if(dadosUsuario != null && dadosUsuario != "undefined" && !dadosUsuario.equalsIgnoreCase(""))
			out.println(tratarDadosUsuario(dadosUsuario, genericCrudDao));
		
		out.close();
	}
	
	public String tratarDadosCrime(String dadosCrime, GenericCrudDao genericCrudDao){		
		String [] array = dadosCrime.split(";");
		if( array.length == 10){
			String tipoViolenciaEscola = array[0];			
			String tipoLocalizacao = array[1];
			String tipoAgressor = array[2];
			String tipoReport = array[3];
			String tipoConsequencia = array[4];			
			Relato relato = new Relato();
			if(tipoViolenciaEscola.equals("1.")){
				TipoViolenciaEscolaRelato tipoViolenciaEscolaRelato = new TipoViolenciaEscolaRelato();
				tipoViolenciaEscolaRelato.setIdTipoViolenciaEscolaRelato(new Long(1));
				relato.setTipoViolenciaEscolaRelato(tipoViolenciaEscolaRelato);
			}
			if(tipoViolenciaEscola.equals("2.")){
				TipoViolenciaEscolaRelato tipoViolenciaEscolaRelato = new TipoViolenciaEscolaRelato();
				tipoViolenciaEscolaRelato.setIdTipoViolenciaEscolaRelato(new Long(2));
				relato.setTipoViolenciaEscolaRelato(tipoViolenciaEscolaRelato);
			}
			if(tipoViolenciaEscola.equals("3.")){
				TipoViolenciaEscolaRelato tipoViolenciaEscolaRelato = new TipoViolenciaEscolaRelato();
				tipoViolenciaEscolaRelato.setIdTipoViolenciaEscolaRelato(new Long(3));
				relato.setTipoViolenciaEscolaRelato(tipoViolenciaEscolaRelato);
			}
			if(tipoViolenciaEscola.equals("4.")){
				TipoViolenciaEscolaRelato tipoViolenciaEscolaRelato = new TipoViolenciaEscolaRelato();
				tipoViolenciaEscolaRelato.setIdTipoViolenciaEscolaRelato(new Long(4));
				relato.setTipoViolenciaEscolaRelato(tipoViolenciaEscolaRelato);
			}
			if(tipoLocalizacao.equals("1-")){
				TipoLocalizacaoRelato tipoLocalizacaoRelato= new TipoLocalizacaoRelato();
				tipoLocalizacaoRelato.setIdTipoLocalizacaoRelato(new Long(1));
				relato.setTipoLocalizacaoRelato(tipoLocalizacaoRelato);
			}
			if(tipoLocalizacao.equals("2-")){
				TipoLocalizacaoRelato tipoLocalizacaoRelato= new TipoLocalizacaoRelato();
				tipoLocalizacaoRelato.setIdTipoLocalizacaoRelato(new Long(2));
				relato.setTipoLocalizacaoRelato(tipoLocalizacaoRelato);
			}
			if(tipoLocalizacao.equals("3-")){
				TipoLocalizacaoRelato tipoLocalizacaoRelato= new TipoLocalizacaoRelato();
				tipoLocalizacaoRelato.setIdTipoLocalizacaoRelato(new Long(3));
				relato.setTipoLocalizacaoRelato(tipoLocalizacaoRelato);
			}
			if(tipoLocalizacao.equals("4-")){
				TipoLocalizacaoRelato tipoLocalizacaoRelato= new TipoLocalizacaoRelato();
				tipoLocalizacaoRelato.setIdTipoLocalizacaoRelato(new Long(4));
				relato.setTipoLocalizacaoRelato(tipoLocalizacaoRelato);
			}
			if(tipoLocalizacao.equals("5-")){
				TipoLocalizacaoRelato tipoLocalizacaoRelato= new TipoLocalizacaoRelato();
				tipoLocalizacaoRelato.setIdTipoLocalizacaoRelato(new Long(5));
				relato.setTipoLocalizacaoRelato(tipoLocalizacaoRelato);
			}
			relato.setTipoRelato("6");
			relato.setSubTipoRelato("3");
			relato.setDescricao("");
			relato.setLatitude(Double.parseDouble(array[5]));
			relato.setLongitude(Double.parseDouble(array[6]));
			Integer hora = Integer.parseInt(array[8]);
			if(hora>=0 && hora<5){
				relato.setMadrugada(true);
				relato.setTarde(false);
				relato.setNoite(false);
				relato.setManha(false);
			}
			if(hora>=5 && hora<12){
				relato.setMadrugada(false);
				relato.setTarde(false);
				relato.setNoite(false);
				relato.setManha(true);
			}
			if(hora>=12 && hora<18){
				relato.setMadrugada(false);
				relato.setTarde(true);
				relato.setNoite(false);
				relato.setManha(false);
			}
			if(hora>18 && hora<=24){
				relato.setMadrugada(false);
				relato.setTarde(false);
				relato.setNoite(true);
				relato.setManha(false);
			}
			//crc.setData(array[7]+"_"+array[8]);
			relato.setDataHoraRegistro(new Date());
			UsuarioCelular usc = new UsuarioCelular();
			usc.setTelefoneCelular(array[9]);
			List<BaseObject> usuarios = (List<BaseObject>) genericCrudDao.find(usc);
			if( usuarios.size() == 0 ){
				genericCrudDao.save(usc);
				relato.setUsuarioCelular((UsuarioCelular)genericCrudDao.find(usc).get(0));
			}else{
				relato.setUsuarioCelular((UsuarioCelular)usuarios.get(0));
			}
			genericCrudDao.save(relato);		
			
			return "success -> report crime";
		}else{
			return "failure -> report crime";
		}	
	}
	
	public String tratarDadosUsuario(String dadosUsuario, GenericCrudDao genericCrudDao){
		String [] array = dadosUsuario.split(";");
		if( array.length == 2){
			UsuarioCelular usc = new UsuarioCelular();
			if(array[0] != null && !array[0].equalsIgnoreCase("")){	
				usc.setEmail(array[0]);
				if( genericCrudDao.find(usc).size() > 0 )
					return "failure -> user email already exists";
			}	
			usc.setTelefoneCelular(array[1]);
			usc.setEmail(null);
			if(genericCrudDao.find(usc).size()>0)
				return "failure -> user mobile phone already exists";
			usc.setEmail(array[0]);
			genericCrudDao.save(usc);
			return "success -> register user";
		}else{
			return "failure -> register user";
		}	
		
	}
}
