package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.ComentarioRelato;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.RedeSocial;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.RelatoRazao;
import org.wikicrimes.model.RepasseRelato;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoPapel;
import org.wikicrimes.model.TipoRegistro;
import org.wikicrimes.model.TipoVitima;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.OpensocialService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Cripto;
import org.wikicrimes.web.FiltroForm;

public class ServletOpensocial extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1139515643893315082L;

	/**
	 * 
	 */
	private static ApplicationContext ctx;
	private static SessionFactory sf;
	private final static String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDHDCCAoWgAwIBAgIJAMbTCksqLiWeMA0GCSqGSIb3DQEBBQUAMGgxCzAJBgNV\n"
			+ "BAYTAlVTMQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEUMBIG\n"
			+ "A1UEChMLR29vZ2xlIEluYy4xDjAMBgNVBAsTBU9ya3V0MQ4wDAYDVQQDEwVscnlh\n"
			+ "bjAeFw0wODAxMDgxOTE1MjdaFw0wOTAxMDcxOTE1MjdaMGgxCzAJBgNVBAYTAlVT\n"
			+ "MQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEUMBIGA1UEChML\n"
			+ "R29vZ2xlIEluYy4xDjAMBgNVBAsTBU9ya3V0MQ4wDAYDVQQDEwVscnlhbjCBnzAN\n"
			+ "BgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAseBXZ4NDhm24nX3sJRiZJhvy9eDZX12G\n"
			+ "j4HWAMmhAcnm2iBgYpAigwhVHtOs+ZIUIdzQHvHeNd0ydc1Jg8e+C+Mlzo38OvaG\n"
			+ "D3qwvzJ0LNn7L80c0XVrvEALdD9zrO+0XSZpTK9PJrl2W59lZlJFUk3pV+jFR8NY\n"
			+ "eB/fto7AVtECAwEAAaOBzTCByjAdBgNVHQ4EFgQUv7TZGZaI+FifzjpTVjtPHSvb\n"
			+ "XqUwgZoGA1UdIwSBkjCBj4AUv7TZGZaI+FifzjpTVjtPHSvbXqWhbKRqMGgxCzAJ\n"
			+ "BgNVBAYTAlVTMQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEU\n"
			+ "MBIGA1UEChMLR29vZ2xlIEluYy4xDjAMBgNVBAsTBU9ya3V0MQ4wDAYDVQQDEwVs\n"
			+ "cnlhboIJAMbTCksqLiWeMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEA\n"
			+ "CETnhlEnCJVDXoEtSSwUBLP/147sqiu9a4TNqchTHJObwTwDPUMaU6XIs2OTMmFu\n"
			+ "GeIYpkHXzTa9Q6IKlc7Bt2xkSeY3siRWCxvZekMxPvv7YTcnaVlZzHrVfAzqNsTG\n"
			+ "P3J//C0j+8JWg6G+zuo5k7pNRKDY76GxxHPYamdLfwk=\n"
			+ "-----END CERTIFICATE-----";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/xml; charset=iso-8859-1");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("iso-8859-1");

		PrintWriter out = response.getWriter();

		out.println("<?xml version=\"1.0\"?>");

		ctx = WebApplicationContextUtils.getWebApplicationContext(this
				.getServletContext());
		OpensocialService opensocialService = (OpensocialService) ctx
				.getBean("opensocialService");
		String acao = request.getParameter("acao");
		String resposta = "";
		if(request.getParameter("dominioRedeSocial")==null || request.getParameter("dominioRedeSocial").equals("ning.com")){
			resposta = "permitir";
		}else{
			resposta = verificaCertificado(request);
		}
		if(resposta.equalsIgnoreCase("permitir")){
			resposta = "";
			if (acao.equals("faleConosco")) {
	
				String idUsuarioRedeSocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
	
				String descFaleConosco = request.getParameter("descFaleConosco");
				descFaleConosco = codificaCaracteres(descFaleConosco);
	
				descFaleConosco = URLDecoder.decode(descFaleConosco, "iso-8859-1");
				try {
					setUp();
					UsuarioRedeSocial urs = new UsuarioRedeSocial();
					urs.setIdUsuarioDentroRedeSocial(idUsuarioRedeSocial);
					RedeSocial rs = new RedeSocial();
					rs.setDominioRedeSocial(dominioRedesocial);
					urs.setRedeSocial(rs);
					UsuarioRedeSocial ursBanco = opensocialService
							.getUsuarioRedeSocial(urs).get(0);
					String emailUsuario = "";
					if (ursBanco.getUsuario() == null)
						emailUsuario = "wikicrimes@wikicrimes.org";
					else
						emailUsuario = ursBanco.getUsuario().getEmail();
					EmailService service = (EmailService) ctx
							.getBean("emailService");
					service.enviarEmail("Id do Usuario:" + idUsuarioRedeSocial,
							emailUsuario, "Email Fale Conosco do Orkut",
							descFaleConosco);
	
					resposta = "ok";
					tearDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
			if (acao.equals("registrarComentario")) {
	
				String idUsuarioRedeSocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String idNotificacao = request.getParameter("idNotificacao");
				String descComentario = request.getParameter("descComentario");
				descComentario = codificaCaracteres(descComentario);
	
				descComentario = URLDecoder.decode(descComentario, "iso-8859-1");
				ComentarioRelato cr = new ComentarioRelato();
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedeSocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedesocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
	
				cr.setUsuarioRedeSocial(ursBanco);
				cr.setDescComentario(descComentario);
				cr.setDataComentario(new Date());
				Relato r = new Relato();
				r.setChave(idNotificacao);
				cr.setRelato(opensocialService.buscaRelato(r));
				opensocialService.registrarBaseObject(cr);
				System.out.println("[" + new Date() + "] "
						+ ursBanco.getIdUsuarioDentroRedeSocial()
						+ " registrou um comentário");
				resposta = "ok";
			}
			
			if (acao.equals("registrarComentarioCrime")) {
				
				String idUsuarioRedeSocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String idNotificacao = request.getParameter("idNotificacao");
				String descComentario = request.getParameter("descComentario");
				descComentario = codificaCaracteres(descComentario);
	
				descComentario = URLDecoder.decode(descComentario, "iso-8859-1");
				Comentario comentario = new Comentario();
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedeSocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedesocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
	
				comentario.setUsuarioRedeSocial(ursBanco);
				comentario.setComentario(descComentario);
				comentario.setDataConfirmacao(new Date());
				Crime c = new Crime();
				c.setChave(idNotificacao);
				comentario.setCrime(opensocialService.buscaCrime(c));
				opensocialService.registrarBaseObject(comentario);
				System.out.println("[" + new Date() + "] "
						+ ursBanco.getIdUsuarioDentroRedeSocial()
						+ " registrou um comentário");
				resposta = "ok";
			}
			
			if (acao.equals("registrarRelato")) {
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedeSocial = request
						.getParameter("dominioRedeSocial");
				String descricao = request.getParameter("descricao");
				String latitude = request.getParameter("lat");
				String longitude = request.getParameter("lng");
				String turno = request.getParameter("turno");
				String razoesUrl = request.getParameter("razoes");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				String[] razoes = razoesUrl.split(";");
				String[] turnos = turno.split(";");
				descricao = codificaCaracteres(descricao);
				Relato r = new Relato();
				if (turnos[0].equalsIgnoreCase("1"))
					r.setMadrugada(true);
				else
					r.setMadrugada(false);
				if (turnos[1].equalsIgnoreCase("1"))
					r.setManha(true);
				else
					r.setManha(false);
				if (turnos[2].equalsIgnoreCase("1"))
					r.setTarde(true);
				else
					r.setTarde(false);
				if (turnos[3].equalsIgnoreCase("1"))
					r.setNoite(true);
				else
					r.setNoite(false);
	
				List<RelatoRazao> razoesInsert = new ArrayList<RelatoRazao>();
	
				r.setDescricao(descricao);
				r.setQtdConfNegativas(new Long(0));
				r.setQtdConfPositivas(new Long(0));
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedeSocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
				r.setUsuarioRedeSocial(ursBanco);
				r.setLatitude(Double.parseDouble(latitude));
				r.setLongitude(Double.parseDouble(longitude));
				r.setDataHoraRegistro(new Date());
				r.setTipoRelato("6");
				for (int i = 0; i < razoes.length; i++) {
					RelatoRazao relatoRazao = new RelatoRazao();
					Razao razao = new Razao();
					razao.setIdRazao(Long.parseLong(razoes[i]));
					relatoRazao.setRazao(razao);
					relatoRazao.setRelato(r);
					razoesInsert.add(relatoRazao);
				}
				opensocialService.registrarRelato(r, razoesInsert);
				r.setChave(Cripto.criptografar(r.getIdRelato().toString()
						+ r.getDataHoraRegistro().toString()));
				opensocialService.registrarBaseObject(r);
	
				opensocialService.registrarRepasses(arrayAmigos, rs, null, r,
						ursBanco);
				System.out.println("[" + new Date() + "] "
						+ ursBanco.getIdUsuarioDentroRedeSocial()
						+ " registrou um alerta com chave=" + r.getChave());
				resposta = "ok";
			}
			
			if (acao.equals("registrarCrime")) {
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedeSocial = request
						.getParameter("dominioRedeSocial");
				String descricao = request.getParameter("descricao");
				String latitude = request.getParameter("lat");
				String longitude = request.getParameter("lng");
				Integer tipoCrime = Integer.parseInt(request.getParameter("tipoCrime"));
				Integer tipoVitima = Integer.parseInt(request.getParameter("tipoVitima"));
				Integer tipoLocal = Integer.parseInt(request.getParameter("tipoLocal"));
				Integer qtdC = Integer.parseInt(request.getParameter("qtdC"));
				Integer qtdV = Integer.parseInt(request.getParameter("qtdV"));
				Integer tpa = Integer.parseInt(request.getParameter("tpa"));
				Integer hora = Integer.parseInt(request.getParameter("hora"));
				Date data = null;
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				try {
					data = sdf.parse(request.getParameter("data"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Integer polInfo = Integer.parseInt(request.getParameter("polInfo"));
				Integer rcrime = Integer.parseInt(request.getParameter("rcrime"));
				
				String razoesUrl = request.getParameter("razoes");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				String[] razoes = razoesUrl.split(";");
				
				descricao = codificaCaracteres(descricao);
				Crime c = new Crime();
				
	
				List<CrimeRazao> razoesInsert = new ArrayList<CrimeRazao>();
	
				c.setDescricao(descricao);
				c.setConfirmacoesNegativas(new Long(0));
				c.setConfirmacoesPositivas(new Long(0));
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedeSocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
				c.setUsuarioRedeSocial(ursBanco);
				c.setLatitude(Double.parseDouble(latitude));
				c.setLongitude(Double.parseDouble(longitude));
				c.setDataHoraRegistro(new Date());
				TipoVitima tpv = new TipoVitima();
				tpv.setIdTipoVitima(new Long(tipoVitima));
				c.setTipoVitima(tpv);
				TipoLocal tpl = new TipoLocal();
				tpl.setIdTipoLocal(new Long(tipoLocal));
				c.setTipoLocal(tpl);
				c.setQuantidade(new Long(qtdC));
				c.setQtdMasculino(new Long(qtdV));
				TipoArmaUsada tipoArmaUsada = new TipoArmaUsada();
				tipoArmaUsada.setIdTipoArmaUsada(new Long(tpa));
				c.setTipoArmaUsada(tipoArmaUsada);
				TipoPapel tp = new TipoPapel();
				tp.setIdTipoPapel(new Long(rcrime));
				c.setTipoPapel(tp);
				TipoRegistro tr = new TipoRegistro();
				tr.setIdTipoRegistro(new Long (polInfo));
				c.setTipoRegistro(tr);
				c.setHorario(new Long(hora));
				c.setData(data);
				TipoCrime tc = new TipoCrime();
				tc.setIdTipoCrime(new Long(tipoCrime));
				c.setTipoCrime(tc);
				for (int i = 0; i < razoes.length; i++) {
					CrimeRazao crimeRazao = new CrimeRazao();
					Razao razao = new Razao();
					razao.setIdRazao(Long.parseLong(razoes[i]));
					crimeRazao.setRazao(razao);
					crimeRazao.setCrime(c);
					razoesInsert.add(crimeRazao);
				}
				opensocialService.registrarCrime(c, razoesInsert);
				c.setChave(Cripto.criptografar(c.getIdCrime().toString()
						+ c.getDataHoraRegistro().toString()));
				opensocialService.registrarBaseObject(c);
	
				opensocialService.registrarRepassesCrime(arrayAmigos, rs, null, c,
						ursBanco);
				System.out.println("[" + new Date() + "] "
						+ ursBanco.getIdUsuarioDentroRedeSocial()
						+ " registrou um alerta de crime com chave=" + c.getChave());
				resposta = "ok";
			}
			
			
			if (acao.equals("confirmarNotificacao")) {
				RelatoService relatoService = (RelatoService) ctx
				.getBean("relatoService");
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedeSocial = request
						.getParameter("dominioRedeSocial");
				String chaveRelato = request.getParameter("idNotificacao");
				String confirma = request.getParameter("confirma");
	
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedeSocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
				Relato r = new Relato();
				r.setChave(chaveRelato);
				r = opensocialService.buscaRelato(r);
				ConfirmacaoRelato cr = new ConfirmacaoRelato();
				cr.setRelato(r);
				cr.setUsuarioRedeSocial(ursBanco);
	
				if (opensocialService.verificaConfirmacao(cr)) {
					resposta = "ja confirmou";
				} else {
					if (confirma.equals("1"))
						cr.setConfirma(true);
					else
						cr.setConfirma(false);
					cr.setIndicacao(false);
					cr.setIndicacaoEmail(false);
					cr.setDataConfirmacao(new Date());
					opensocialService.registrarBaseObject(cr);
					relatoService.increntaNumConfirmacoes(r, cr.getConfirma());
					System.out.println("[" + new Date() + "] "
							+ ursBanco.getIdUsuarioDentroRedeSocial()
							+ " confirmou o alerta com chave=" + r.getChave());
					resposta = "ok";
				}
	
			}
			
			if (acao.equals("salvarConfiguracoes")) {
				try{	
					RelatoService relatoService = (RelatoService) ctx
					.getBean("relatoService");
					String idUsuarioRedesocial = request
							.getParameter("idUsuarioRedeSocial");
					String dominioRedeSocial = request
							.getParameter("dominioRedeSocial");
					String statusTutor = request.getParameter("statusTutor");
					UsuarioRedeSocial urs = new UsuarioRedeSocial();
					urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
					RedeSocial rs = new RedeSocial();
					rs.setDominioRedeSocial(dominioRedeSocial);
					urs.setRedeSocial(rs);
					UsuarioRedeSocial ursBanco = opensocialService
							.getUsuarioRedeSocial(urs).get(0);
					ursBanco.setAtivarTutor(Integer.parseInt(statusTutor));
					opensocialService.registrarBaseObject(ursBanco);
					resposta = "ok";
				}catch (Exception e) {
					e.printStackTrace();
					resposta = "erro";
				}
			}
			
			if (acao.equals("confirmarNotificacaoCrime")) {
				CrimeService crimeService = (CrimeService) ctx
				.getBean("crimeService");
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedeSocial = request
						.getParameter("dominioRedeSocial");
				String chaveCrime = request.getParameter("idNotificacao");
				String confirma = request.getParameter("confirma");
	
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedeSocial);
				urs.setRedeSocial(rs);
				UsuarioRedeSocial ursBanco = opensocialService
						.getUsuarioRedeSocial(urs).get(0);
				Crime c = new Crime();
				c.setChave(chaveCrime);
				c = opensocialService.buscaCrime(c);
				Confirmacao conf = new Confirmacao();
				conf.setCrime(c);
				conf.setUsuarioRedeSocial(ursBanco);
	
				if (opensocialService.verificaConfirmacao(conf)) {
					resposta = "ja confirmou";
				} else {
					if (confirma.equals("1"))
						conf.setConfirma(true);
					else
						conf.setConfirma(false);
					conf.setIndicacao(false);
					conf.setIndicacaoEmail(false);
					conf.setDataConfirmacao(new Date());
					opensocialService.registrarBaseObject(conf);
					crimeService.atualizaContador(conf.getConfirma(),
							conf.getCrime());
					System.out.println("[" + new Date() + "] "
							+ ursBanco.getIdUsuarioDentroRedeSocial()
							+ " confirmou o crime com chave=" + c.getChave());
					resposta = "ok";
				}
	
			}
			
			if (acao.equals("recuperarComentarios")) {
				String chaveRelato = request.getParameter("chaveRelato");
				Relato relato = new Relato();
				relato.setChave(chaveRelato);
				try {
					setUp();
					List<ComentarioRelato> comentarios = opensocialService
							.getComentarios(relato);
	
					int cont = 0;
					for (ComentarioRelato comentario : comentarios) {
						resposta += comentario.getIdComentario();
						resposta += "|" + comentario.getDescComentario();
						if (comentario.getIdComentarioPai() == null
								|| comentario.getIdComentarioPai() == 0)
							resposta += "|pai";
						else
							resposta += "|" + comentario.getIdComentarioPai();
						resposta += "|"
								+ comentario.getUsuarioRedeSocial()
										.getIdUsuarioDentroRedeSocial();
						if (cont != comentarios.size() - 1)
							resposta += "#";
						cont++;
					}
					tearDown();
	
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
			
			if (acao.equals("recuperarComentariosCrime")) {
				String chaveCrime = request.getParameter("chaveCrime");
				Crime crime = new Crime();
				crime.setChave(chaveCrime);
				try {
					setUp();
					List<Comentario> comentarios = opensocialService
							.getComentarios(crime);
	
					int cont = 0;
					for (Comentario comentario : comentarios) {
						resposta += comentario.getIdComentario();
						resposta += "|" + comentario.getComentario();
						if (comentario.getIdComentarioPai() == null
								|| comentario.getIdComentarioPai() == 0)
							resposta += "|pai";
						else
							resposta += "|" + comentario.getIdComentarioPai();
						if(comentario.getUsuarioRedeSocial()!=null){	
							resposta += "|"
									+ comentario.getUsuarioRedeSocial()
											.getIdUsuarioDentroRedeSocial();
						}else
							resposta += "|-1";
						if (cont != comentarios.size() - 1)
							resposta += "#";
						cont++;
					}
					tearDown();
	
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
			
			if (acao.equals("relatosMaisRecentes")) {
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String idUsuarioRedeSocial = request
						.getParameter("idUsuarioRedeSocial");
				String data = "";
				try {
					setUp();
					RedeSocial rs = new RedeSocial();
					rs.setDominioRedeSocial(dominioRedesocial);
					rs = (RedeSocial) opensocialService.getBaseObjects(rs).get(0);
					UsuarioRedeSocial urs = new UsuarioRedeSocial();
					urs.setRedeSocial(rs);
					urs.setIdUsuarioDentroRedeSocial(idUsuarioRedeSocial);
					List<UsuarioRedeSocial> list = opensocialService
							.getUsuarioRedeSocial(urs);
					urs = list.get(0);
					List<RepasseRelato> repasses = opensocialService.getRelatos(
							rs.getIdRedeSocial(), urs);
					int cont = 0;
					boolean eRelato = false;
					for (RepasseRelato repasse : repasses) {
						if(repasse.getRelato()!=null){	
							resposta += repasse.getRelato().getChave();
							resposta += "|" + repasse.getRelato().getLatitude();
							resposta += "|" + repasse.getRelato().getLongitude();
							resposta += "|"
								+ repasse.getUsuarioEnvio()
										.getIdUsuarioDentroRedeSocial();
							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(repasse.getRelato().getDataHoraRegistro());
							resposta += "|" + gc.get(Calendar.DAY_OF_MONTH) + "/"
										+ (gc.get(Calendar.MONTH) + 1) + "/"
										+ gc.get(Calendar.YEAR);
							resposta += "|" +repasse.getRelato().getTipoRelato() ;
							eRelato = true;
						}else{
							resposta += repasse.getCrime().getChave();
							resposta += "|" + repasse.getCrime().getLatitude();
							resposta += "|" + repasse.getCrime().getLongitude();
							resposta += "|"
								+ repasse.getUsuarioEnvio()
										.getIdUsuarioDentroRedeSocial();
							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(repasse.getCrime().getDataHoraRegistro());
							resposta += "|" + gc.get(Calendar.DAY_OF_MONTH) + "/"
									+ (gc.get(Calendar.MONTH) + 1) + "/"
									+ gc.get(Calendar.YEAR);
							resposta += "|" +repasse.getCrime().getTipoCrime().getIdTipoCrime() ;
							eRelato = false;
						}
						
	
						
						if(eRelato)
							resposta += "|1";
						else
							resposta += "|0";
						
						if (cont != repasses.size() - 1)
							resposta += "#";
						cont++;
					}
					tearDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
			if (acao.equals("realizaLogin")) {
				String idUsuarioRedeSocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String login = request.getParameter("login");
				String senha = request.getParameter("senha");
				String vizualizarCrimeOpensocial = request
						.getParameter("vizualizarCrimeOpensocial");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				UsuarioService usuarioService = (UsuarioService) ctx
						.getBean("usuarioService");
				Usuario user = new Usuario();
				user.setEmail(login);
				user.setSenha(senha);
				Usuario usuario = usuarioService.login(user);
				if (usuario != null) {
					UsuarioRedeSocial urs = new UsuarioRedeSocial();
					urs.setIdUsuarioDentroRedeSocial(idUsuarioRedeSocial);
	
					urs.setVisualizarCrimes(Integer
							.parseInt(vizualizarCrimeOpensocial));
	
					RedeSocial redeSocial = new RedeSocial();
					redeSocial.setDominioRedeSocial(dominioRedesocial);
					redeSocial = (RedeSocial) opensocialService.getBaseObjects(redeSocial).get(0);
					urs.setRedeSocial(redeSocial);
					urs.setUsuario(usuario);
					urs.setDataHoraRegistro(new Date());
					usuarioService.salvar(urs);
					List<UsuarioRedeSocial> usuariosRS = new ArrayList<UsuarioRedeSocial>(0);
					for (int i = 0; i < arrayAmigos.length; i++) {
						UsuarioRedeSocial urerRS = new UsuarioRedeSocial();
						urerRS.setIdUsuarioDentroRedeSocial(arrayAmigos[i]);
						usuariosRS.add(urerRS);
					}
					List<RepasseRelato> repasses = opensocialService.getRepasses(usuariosRS);
					List<RepasseRelato> repassesDistintos = new ArrayList<RepasseRelato>(0);
					boolean achou = false;
					for (Iterator iterator = repasses.iterator(); iterator
							.hasNext();) {
						RepasseRelato repasseRelato = (RepasseRelato) iterator
								.next();
						for(int i = 0 ; i< repassesDistintos.size();i++){
							if(repassesDistintos.get(i).getRelato()!=null && repasseRelato.getRelato()!=null && repassesDistintos.get(i).getRelato().getIdRelato().equals(repasseRelato.getRelato().getIdRelato())){
								achou = true;
								break;
							}
							if(repassesDistintos.get(i).getCrime()!=null && repasseRelato.getCrime()!=null && repassesDistintos.get(i).getCrime().getIdCrime().equals(repasseRelato.getCrime().getIdCrime())){
								achou = true;
								break;
							}
						}
						if(!achou)
							repassesDistintos.add(repasseRelato);
						achou = false;						
					}
					if(repassesDistintos.size()>0)
						opensocialService.registrarRepassesPrimeiraVez(repassesDistintos, urs);
					System.out.println("[" + new Date() + "] " + login
							+ " realizou o seu login pelo " + dominioRedesocial);
					resposta = "valido;" + usuario.getLat() + ";"
							+ usuario.getLng();
				} else {
					resposta = "invalido";
				}
			}
			if (acao.equals("verificaRegistro")) {
				try {
					setUp();
	
					String idUsuarioRedesocial = request
							.getParameter("idUsuarioRedeSocial");
					String dominioRedesocial = request
							.getParameter("dominioRedeSocial");
					UsuarioRedeSocial urs = new UsuarioRedeSocial();
					RedeSocial rs = new RedeSocial();
					rs.setDominioRedeSocial(dominioRedesocial);
					urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
					urs.setRedeSocial(rs);
					List<UsuarioRedeSocial> ursResult = opensocialService
							.getUsuarioRedeSocial(urs);
					if (ursResult != null) {
						Usuario usuarioPesquisado = ursResult.get(0).getUsuario();
						if (usuarioPesquisado != null) {
							System.out
									.println("["
											+ new Date()
											+ "] "
											+ usuarioPesquisado.getEmail()
											+ " esta usando a app do wikicrimes no "+dominioRedesocial);
							resposta = "registrado";
							resposta += ";" + usuarioPesquisado.getLat();
							resposta += ";" + usuarioPesquisado.getLng();
						} else {
							resposta = "registrado_rede_social";
							resposta += ";" + ursResult.get(0).getCidade();
							resposta += ";" + ursResult.get(0).getPais();							
							System.out
									.println("["
											+ new Date()
											+ "] "
											+ idUsuarioRedesocial
											+ " esta usando a app do wikicrimes no "+dominioRedesocial);
						}
						resposta += ";" + ursResult.get(0).getAtivarTutor();
					} else {
						resposta = "nao_registrado";
					}
					// UsuarioService usuarioService =
					// (UsuarioService)ctx.getBean("usuarioService");
					// Usuario usuario = new Usuario();
					// usuario.set
					tearDown();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
			if (acao.equals("listCrimes")) {
				try {
					String norte = request.getParameter("n");
					String sul = request.getParameter("s");
					String leste = request.getParameter("e");
					String oeste = request.getParameter("w");
					String dominioRedeSocial = request
							.getParameter("dominioRedeSocial");
					setUp();
					// HashMap params = new HashMap();
					// params.put("maxResults", 4000);
					FiltroForm filtroForm = new FiltroForm();
					ApplicationContext springContext = WebApplicationContextUtils
							.getWebApplicationContext(getServletContext());
					CrimeService crimeService = (CrimeService) springContext
							.getBean("crimeService");
					filtroForm.setCrimeService(crimeService);
					List<BaseObject> crimes = filtroForm.getCrimesFiltrados("", "",
							"", "", "", "", "", "", "", norte, sul, leste, oeste,
							"true",null);
					// /List<Crime> crimes = opensocialService.getCrimes(null);
					for (BaseObject bo : crimes) {
	
						Crime crime = (Crime) bo;
						resposta += "#" + crime.getChave();
						boolean achou = false;
						if(crime.getUsuario()!=null){	
							for (UsuarioRedeSocial urs : crime.getUsuario().getRedesSociais()) {
								if (urs.getRedeSocial().getDominioRedeSocial()
										.equalsIgnoreCase(dominioRedeSocial)) {
									if (urs.getVisualizarCrimes() == 1)
										resposta += "#"
												+ urs.getIdUsuarioDentroRedeSocial();
									else
										resposta += "#" + "achou e nao liberou";
									achou = true;
									break;
								}
		
							}
						}	
						if (!achou)
							resposta += "#" + "nao achou";
						// resposta += "#"+crime.getUsuario().getEmail();
						resposta += "#" + crime.getLatitude();
						resposta += "#" + crime.getLongitude();
						resposta += "#" + crime.getTipoCrime().getNome();
						resposta += "#" + crime.getIdCrime();
						resposta += "#|";
							
					}
	
					RelatoService rService = (RelatoService) springContext
							.getBean("relatoService");
					filtroForm.setRelatoService(rService);
					List<BaseObject> relatos = filtroForm.getRelatosFiltrados(
							norte, sul, leste, oeste,null,null, true);
					for (BaseObject bo : relatos) {
						Relato r = (Relato) bo;
						resposta += "#" + r.getChave();
						boolean achou = false;
						// for (UsuarioRedeSocial urs :
						// r.getUsuario().getRedesSociais()) {
						// if(urs.getRedeSocial().getDominioRedeSocial().equalsIgnoreCase(dominioRedeSocial)){
						// if(urs.getVisualizarCrimes())
						// resposta += "#"+urs.getIdUsuarioRedeSocial();
						// else
						// resposta += "#"+"achou e nao liberou";
						// achou = true;
						// break;
						// }
						//						
						// }
						// if(!achou)
						resposta += "#" + "nao achou";
						// resposta += "#"+crime.getUsuario().getEmail();
						resposta += "#" + r.getLatitude();
						resposta += "#" + r.getLongitude();
						resposta += "#" + r.getTipoRelato();
						resposta += "#" + r.getIdRelato();
						resposta += "#|";
					}
					resposta += "";
					tearDown();
				} catch (Exception e) {
					e.printStackTrace();
					resposta = "";
				}
			}
			if (acao.equals("registrarUsuarioOrkut")) {
				String idUsuarioRedesocial = request.getParameter("id");
				String cidade = request.getParameter("cidade");
				String pais = request.getParameter("pais");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				UsuarioRedeSocial urs = new UsuarioRedeSocial();
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedesocial);
				rs.setDominioRedeSocial(dominioRedesocial);
				rs = (RedeSocial) opensocialService.getBaseObjects(rs).get(0);
				urs.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				urs.setVisualizarCrimes(1);
				urs.setCidade(cidade);
				urs.setPais(pais);
				urs.setRedeSocial(rs);
				urs.setDataHoraRegistro(new Date());
				if (!opensocialService.idCadastrado(urs)) {
					UsuarioService usuarioService = (UsuarioService) ctx
							.getBean("usuarioService");
					usuarioService.salvar(urs);
					List<UsuarioRedeSocial> usuariosRS = new ArrayList<UsuarioRedeSocial>(0);
					for (int i = 0; i < arrayAmigos.length; i++) {
						UsuarioRedeSocial urerRS = new UsuarioRedeSocial();
						urerRS.setIdUsuarioDentroRedeSocial(arrayAmigos[i]);
						usuariosRS.add(urerRS);
					}
					List<RepasseRelato> repasses = opensocialService.getRepasses(usuariosRS);
					List<RepasseRelato> repassesDistintos = new ArrayList<RepasseRelato>(0);
					boolean achou = false;
					for (Iterator iterator = repasses.iterator(); iterator
							.hasNext();) {
						RepasseRelato repasseRelato = (RepasseRelato) iterator
								.next();
						for(int i = 0 ; i< repassesDistintos.size();i++){
							if(repassesDistintos.get(i).getRelato()!=null && repasseRelato.getRelato()!=null && repassesDistintos.get(i).getRelato().getIdRelato().equals(repasseRelato.getRelato().getIdRelato())){
								achou = true;
								break;
							}
							if(repassesDistintos.get(i).getCrime()!=null && repasseRelato.getCrime()!=null && repassesDistintos.get(i).getCrime().getIdCrime().equals(repasseRelato.getCrime().getIdCrime())){
								achou = true;
								break;
							}
						}
						if(!achou)
							repassesDistintos.add(repasseRelato);
						achou = false;						
					}
					if(repassesDistintos.size()>0)
						opensocialService.registrarRepassesPrimeiraVez(repassesDistintos, urs);
					System.out.println("[" + new Date() + "] "
							+ idUsuarioRedesocial + " do " + dominioRedesocial
							+ " adicionou Wikicrimes via Open Social");
					resposta = "registrado";
				} else {
					resposta = "ja_registrado";
				}
			}
			if (acao.equals("repassarRelato")) {
				try {
					setUp();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String chaveRelato = request.getParameter("idNotificacao");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedesocial);
				rs = (RedeSocial) opensocialService.getBaseObjects(rs).get(0);
				UsuarioRedeSocial usuRedSocial = new UsuarioRedeSocial();
				usuRedSocial.setRedeSocial(rs);
				usuRedSocial.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				UsuarioRedeSocial usuarioEnvio = opensocialService
						.getUsuarioRedeSocial(usuRedSocial).get(0);
				Relato relato = new Relato();
				relato.setChave(chaveRelato);
				relato = (Relato) opensocialService.getBaseObjects(relato).get(0);
	
				UsuarioRedeSocial ursAux = null;
	
				opensocialService.registrarRepasses(arrayAmigos, rs, ursAux,
						relato, usuarioEnvio);
				System.out.println("[" + new Date() + "] " + idUsuarioRedesocial
						+ " repassou o alerta com chave=" + relato.getChave()
						+ " para seus amigos");
				try {
	
					tearDown();
					resposta = "ok";
				} catch (Exception e) {
					resposta = "erro";
					e.printStackTrace();
				}
	
			}
			
			if (acao.equals("repassarCrime")) {
				try {
					setUp();
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				String idUsuarioRedesocial = request
						.getParameter("idUsuarioRedeSocial");
				String dominioRedesocial = request
						.getParameter("dominioRedeSocial");
				String chaveCrime = request.getParameter("idNotificacao");
				String amigos = request.getParameter("amigos");
				String[] arrayAmigos = amigos.split(";");
				RedeSocial rs = new RedeSocial();
				rs.setDominioRedeSocial(dominioRedesocial);
				rs = (RedeSocial) opensocialService.getBaseObjects(rs).get(0);
				UsuarioRedeSocial usuRedSocial = new UsuarioRedeSocial();
				usuRedSocial.setRedeSocial(rs);
				usuRedSocial.setIdUsuarioDentroRedeSocial(idUsuarioRedesocial);
				UsuarioRedeSocial usuarioEnvio = opensocialService
						.getUsuarioRedeSocial(usuRedSocial).get(0);
				Crime crime = new Crime();
				crime.setChave(chaveCrime);
				crime = (Crime) opensocialService.getBaseObjects(crime).get(0);
	
				UsuarioRedeSocial ursAux = null;
	
				opensocialService.registrarRepassesCrime(arrayAmigos, rs, ursAux,
						crime, usuarioEnvio);
				System.out.println("[" + new Date() + "] " + idUsuarioRedesocial
						+ " repassou o alerta com chave=" + crime.getChave()
						+ " para seus amigos");
				try {
	
					tearDown();
					resposta = "ok";
				} catch (Exception e) {
					resposta = "erro";
					e.printStackTrace();
				}
	
			}
			
			if (acao.equals("registrarUsuarioWikiCrimes")) {
				UsuarioService usuarioService = (UsuarioService) ctx
						.getBean("usuarioService");
				String cidade = request.getParameter("cidade");
				String pais = request.getParameter("pais");
				String email = request.getParameter("email");
				String senha = request.getParameter("senha");
				String estado = request.getParameter("estado");
				String lat = request.getParameter("lat");
				String lng = request.getParameter("lng");
				String nome = request.getParameter("nome");
				String unome = request.getParameter("unome");
				String idioma = request.getParameter("idioma");
				String news = request.getParameter("news");
	
				Usuario user = new Usuario();
				// checa se ja existe email cadastrado ou se e convidado
				Usuario userResult = usuarioService.getUsuario(email);
				if (userResult == null
						|| userResult.getPerfil().getIdPerfil() == Perfil.CONVIDADO) {
					// se nao for convidado nao passa
					if (userResult != null
							&& userResult.getPerfil().getIdPerfil() == Perfil.CONVIDADO) {
						user.setIdUsuario(userResult.getIdUsuario());
					}
					user.setPerfil(new Perfil(Perfil.USUARIO));
					user.setCidade(cidade);
					user.setDataHoraRegistro(new Date());
					user.setEmail(email);
					user.setEstado(estado);
					user.setIdiomaPreferencial(idioma);
					user.setIp(request.getRemoteAddr());
					user.setLat(Double.parseDouble(lat));
					user.setLng(Double.parseDouble(lng));
					user.setPais(pais);
					user.setPrimeiroNome(nome);
					user.setReceberNewsletter(news);
					user.setSenha(senha);
					user.setUltimoNome(unome);
					usuarioService.insert(user);
					System.out.println("[" + new Date() + "] " + email
							+ " registrou-se via Open Social");
					resposta = "registrado";
				} else {
					resposta = "email_existente";
				}
			}
		}	

		out.println("<dados texto='" + resposta + "'>");
		out.println("</dados>");
		out.close();
		request.getSession().invalidate();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static String verificaCertificado(HttpServletRequest request) {
		String resposta = "";
		try {
			OAuthServiceProvider provider = new OAuthServiceProvider(null,
					null, null);
			OAuthConsumer consumer = new OAuthConsumer(null, "orkut.com", null,
					provider);
			consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, CERTIFICATE);

			String method = request.getMethod();
			String requestUrl = getRequestUrl(request);
			List<OAuth.Parameter> requestParameters = getRequestParameters(request);

			OAuthMessage message = new OAuthMessage(method, requestUrl,
					requestParameters);

			OAuthAccessor accessor = new OAuthAccessor(consumer);

			message.validateSignature(accessor);
			resposta += "permitir";

		} catch (OAuthProblemException ope) {

			resposta += "nao_permitir";
		} catch (Exception e) {
			resposta += e;
			System.out.println(e);
			throw new ServletException(e);
		} finally {
			return resposta;
		}

	}

	public static String getRequestUrl(HttpServletRequest request) {
		StringBuilder requestUrl = new StringBuilder();
		String scheme = request.getScheme();
		int port = request.getLocalPort();

		requestUrl.append(scheme);
		requestUrl.append("://");
		requestUrl.append(request.getServerName());

		if ((scheme.equals("http") && port != 80)
				|| (scheme.equals("https") && port != 443)) {
			requestUrl.append(":");
			requestUrl.append(port);
		}

		requestUrl.append(request.getContextPath());
		requestUrl.append(request.getServletPath());

		return requestUrl.toString();
	}

	/**
	 * Constructs and returns a List of OAuth.Parameter objects, one per
	 * parameter in the passed request.
	 * 
	 * @param request
	 *            Servlet request object with methods for retrieving the full
	 *            set of parameters passed with the request
	 */
	public static List<OAuth.Parameter> getRequestParameters(
			HttpServletRequest request) {

		List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();

		for (Object e : request.getParameterMap().entrySet()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;

			for (String value : entry.getValue()) {
				parameters.add(new OAuth.Parameter(entry.getKey(), value));
			}
		}

		return parameters;
	}

	protected static void setUp() throws Exception {
		// the following is necessary for lazy loading
		sf = (SessionFactory) ctx.getBean("sessionFactory");
		// open and bind the session for this test thread.
		Session s = sf.openSession();
		try{
			TransactionSynchronizationManager
					.bindResource(sf, new SessionHolder(s));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Sessão já aberta!");
		}	
		// setup code here
	}

	protected static void tearDown() throws Exception {
		// unbind and close the session.
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
				.getResource(sf);
		Session s = holder.getSession();
		s.flush();
		TransactionSynchronizationManager.unbindResource(sf);
		SessionFactoryUtils.closeSession(s);
		// teardown code here
	}
	
	protected static String codificaCaracteres(String descComentario){
		descComentario = descComentario.replaceAll("aagudo", "á");
		descComentario = descComentario.replaceAll("eagudo", "é");
		descComentario = descComentario.replaceAll("iagudo", "í");
		descComentario = descComentario.replaceAll("oagudo", "ó");
		descComentario = descComentario.replaceAll("uagudo", "ú");
		descComentario = descComentario.replaceAll("acircu", "â");
		descComentario = descComentario.replaceAll("ecircu", "ê");
		descComentario = descComentario.replaceAll("icircu", "î");
		descComentario = descComentario.replaceAll("ocircu", "ô");
		descComentario = descComentario.replaceAll("ucircu", "û");
		descComentario = descComentario.replaceAll("cagudo", "ç");
		descComentario = descComentario.replaceAll("atil", "ã");
		descComentario = descComentario.replaceAll("otil", "õ");
		descComentario = descComentario.replaceAll("acrase", "à");
		return descComentario;
	}
}
