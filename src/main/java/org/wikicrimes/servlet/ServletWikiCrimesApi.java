package org.wikicrimes.servlet;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.crypto.RSA;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoPapel;
import org.wikicrimes.model.TipoRegistro;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Constantes;
import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.statistics.KernelMapRequestHandler;
import org.wikicrimes.util.statistics.SessionBuffer;
import org.wikicrimes.web.FiltroForm;

public class ServletWikiCrimesApi extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4059683118894911147L;
	//Auto incremento da imagem
	public static int idImage = 2;
	public static Semaphore semaphore = new Semaphore(1);
//	private static String barra = "/";	

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
		String acao = request.getParameter("acao");
		
		SessionBuffer sessionBuffer = new SessionBuffer(request);
		
//		/*teste*/System.out.println("acao: " + acao);
		if(acao.equalsIgnoreCase("pegaImagem")){
			Image imagem = sessionBuffer.getKernelMapImage();
			if(imagem != null)
				ServletUtil.sendImage(response, imagem);
		}else{
		
			PrintWriter out = response.getWriter();
			
			if(acao.equalsIgnoreCase("listaCrimes"))
				acaoListaCrimes(request,response, out);
			else if(acao.equalsIgnoreCase("registrarCrime"))		
				registrarCrimes(request, response, out);
			/*else if(acao.equalsIgnoreCase("kernelMap")){
				try {
					mapaDeKernel(request, response, out);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			else if(acao.equalsIgnoreCase("geraKernel")){
				//calcular o mapa de kernel e criar imagens
//				Rectangle bounds = getLimitesPixel(request);
				gerarKernelMap(request, response, out);
			}
			
			out.close();
		}
	}
	
	public void registrarCrimes(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		PrintWriter saida = null;
		saida = new PrintWriter(out, true);
		JSONObject resposta = new JSONObject();
		try{
			RSA rsa = new RSA();
			String emailCriptografado = request.getParameter("emailUsuario");
			emailCriptografado = emailCriptografado.replaceAll(" ", "+");
			String email = "";
			try{
				email = rsa.decryptEmail(emailCriptografado, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5CtBeB+6MOyHSKP3eW5WM4SInDlemr7MyX1YmqywUmnXdBOsixwYIpERUnZGi1JzoHp/EWc/vOLcvP1XlKsvyf06gvqfCHGdgoq6CVo2Cya5Xl/kOy/jnGeOniE0RYl0+44M1aIyNxN6xw2iyMn+xBPXPrQmayu5OaSK5LBh4RQIDAQAB", false);
			}catch (Exception e) {
				resposta.put("resposta", "w3");
				saida.print(request.getParameter("jsoncallback")+"("+resposta.toString()+")");
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return;
			}		
			
			String descricao = request.getParameter("descricao");
			Double latitude = Double.parseDouble(request.getParameter("lat"));
			Double longitude = Double.parseDouble(request.getParameter("lng"));
			Long tipoCrime = Long.parseLong(request.getParameter("tipoCrime"));
			Long tipoVitima = Long.parseLong(request.getParameter("tipoVitima"));
			Long tipoLocal = Long.parseLong(request.getParameter("tipoLocal"));
			Long qtdC = Long.parseLong(request.getParameter("qtdC"));
			Long qtdV = Long.parseLong(request.getParameter("qtdV"));
			Long tpa = Long.parseLong(request.getParameter("tpa"));
			Long polInfo = Long.parseLong(request.getParameter("polInfo"));
			Long rcrime = Long.parseLong(request.getParameter("rcrime"));
			Long hora = Long.parseLong(request.getParameter("hora"));
			String razoesUrl = request.getParameter("razoes");
			String emailConf1 = request.getParameter("emailConf1");
			String emailConf2 = request.getParameter("emailConf2");
			String[] razoes = razoesUrl.split(";");
			List<Razao> razoesInsert = new ArrayList<Razao>();
			Date data = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				data = sdf.parse(request.getParameter("data"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			ApplicationContext springContext = WebApplicationContextUtils
			.getWebApplicationContext(getServletContext());
			UsuarioService usuarioService = (UsuarioService) springContext
			.getBean("usuarioService");
			CrimeService crimeService = (CrimeService) springContext
			.getBean("crimeService");
			Usuario usuarioBanco = usuarioService.getUsuario(email);
			if(usuarioBanco != null){
				Crime crime = new Crime();
				for (int i = 0; i < razoes.length; i++) {
					Razao razao = new Razao();
					razao.setIdRazao(Long.parseLong(razoes[i]));				
					razoesInsert.add(razao);
				}
				
				crime.setUsuario(usuarioBanco);
				crime.setDescricao(descricao);
				crime.setLatitude(latitude);
				crime.setLongitude(longitude);
//				TipoCrime ObjTipoCrime = new TipoCrime();
//				ObjTipoCrime.setIdTipoCrime(tipoCrime);
				crime.setTipoCrime(crimeService.getTipoCrime(tipoCrime));
//				TipoVitima objTipoVitima = new TipoVitima();
//				objTipoVitima.setIdTipoVitima(tipoVitima);
				crime.setTipoVitima(crimeService.getTipoVitima(tipoVitima));
//				TipoLocal objTipoLocal = new TipoLocal();
//				objTipoLocal.setIdTipoLocal(tipoLocal);
				crime.setTipoLocal(crimeService.getTipoLocal(tipoLocal));
				crime.setQuantidade(qtdC);
				crime.setQtdMasculino(qtdV);
				TipoArmaUsada objTipoArmaUsada = new TipoArmaUsada();
				objTipoArmaUsada.setIdTipoArmaUsada(tpa);
				crime.setTipoArmaUsada(objTipoArmaUsada);
				TipoRegistro objTipoRegistro = new TipoRegistro();
				objTipoRegistro.setIdTipoRegistro(polInfo);
				crime.setTipoRegistro(objTipoRegistro);
				TipoPapel objTipoPapel = new TipoPapel();
				objTipoPapel.setIdTipoPapel(rcrime);
				crime.setTipoPapel(objTipoPapel);
				crime.setData(data);
				crime.setHorario(hora);
				crime.setDataHoraRegistro(new Date());
				crime.setQtdComentarios(new Long(0));
				crime.setConfirmacoesNegativas(new Long(0));
				crime.setConfirmacoesPositivas(new Long(0));
				crime.setVisualizacoes(new Long(0));
				crime.setStatus(new Long(0));
				Confirmacao conf1;
				Confirmacao conf2;
				Set<Confirmacao> confirmacoes = new HashSet<Confirmacao>();
				if( emailConf1 != null && !emailConf1.equalsIgnoreCase("")){
					conf1 = new Confirmacao();
					Usuario usuarioConfirmacao = usuarioService.getUsuario(emailConf1);
					//Se nulo e pq esta indicando esse usuario
					if (usuarioConfirmacao == null) {
						conf1.setIndicacao(Constantes.SIM);
						//cria um usuario convidado
						usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(emailConf1, usuarioBanco.getIdiomaPreferencial());
					}
					//seta que essa confirmacao foi indicada por email
					conf1.setIndicacaoEmail(Constantes.SIM);
					conf1.setUsuario(usuarioConfirmacao);
					confirmacoes.add(conf1);
					
				}
				if( emailConf2 != null && !emailConf2.equalsIgnoreCase("")){
					conf2 = new Confirmacao();
					Usuario usuarioConfirmacao = usuarioService.getUsuario(emailConf2);
					//Se nulo e pq esta indicando esse usuario
					if (usuarioConfirmacao == null) {
						conf2.setIndicacao(Constantes.SIM);
						//cria um usuario convidado
						usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(emailConf2, usuarioBanco.getIdiomaPreferencial());
					}
					//seta que essa confirmacao foi indicada por email
					conf2.setIndicacaoEmail(Constantes.SIM);
					conf2.setUsuario(usuarioConfirmacao);
					confirmacoes.add(conf2);
					
				}
				crime.setConfirmacoes(confirmacoes);
				crime.setRegistradoPelaApi("1");				
				crimeService.insert(crime,razoesInsert);
				resposta.put("resposta", "ok");
				
			}else{
				resposta.put("resposta", "w1");
				saida.print(request.getParameter("jsoncallback")+"("+resposta.toString()+")");
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return;
			}
		}catch (Exception e) {
			resposta.put("resposta", "erro");
			e.printStackTrace();
		}	
			
			
		saida.print(request.getParameter("jsoncallback")+"("+resposta.toString()+")");
		saida.close();
	}
	
	public void acaoListaCrimes(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		PrintWriter saida = null;
		saida = new PrintWriter(out, true);
		String norte = request.getParameter("n");
		String sul = request.getParameter("s");
		String leste = request.getParameter("e");
		String oeste = request.getParameter("w");
		String dataInicial = request.getParameter("di");
		String dataFinal = request.getParameter("df");
		String tipoCrime = request.getParameter("tc");
		String tipoVitima = request.getParameter("tv");
		RSA rsa = new RSA();
		String emailCriptografado = request.getParameter("emailUsuario");
		emailCriptografado = emailCriptografado.replaceAll(" ", "+");
		String email = "";
		if(!emailCriptografado.equalsIgnoreCase("")){			
			try{
				email = rsa.decryptEmail(emailCriptografado, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5CtBeB+6MOyHSKP3eW5WM4SInDlemr7MyX1YmqywUmnXdBOsixwYIpERUnZGi1JzoHp/EWc/vOLcvP1XlKsvyf06gvqfCHGdgoq6CVo2Cya5Xl/kOy/jnGeOniE0RYl0+44M1aIyNxN6xw2iyMn+xBPXPrQmayu5OaSK5LBh4RQIDAQAB", false);
			}catch (Exception e) {
				
				saida.print(request.getParameter("jsoncallback")+"()");
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return;
			}
		}	
		if(dataInicial!=null && !dataInicial.equalsIgnoreCase("undefined"))			
			dataInicial = dataInicial.replaceAll("\\/", ",");
		else
			dataInicial = "";
		if(dataFinal!=null && !dataFinal.equalsIgnoreCase("undefined"))
			dataFinal = dataFinal.replaceAll("\\/", ",");
		else
			dataFinal = "";
		FiltroForm filtroForm = new FiltroForm();
		ApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		CrimeService crimeService = (CrimeService) springContext
				.getBean("crimeService");
		filtroForm.setCrimeService(crimeService);
		filtroForm.setMaxResults(0);
		filtroForm.setPrimeiraVez(false);
		List<BaseObject> crimes = filtroForm.getCrimesFiltrados(tipoCrime, tipoVitima,
				"", "", "", dataInicial, dataFinal, "", "", norte, sul, leste, oeste,email);
		List<String> crimesText = new ArrayList<String>();
		for (Iterator<BaseObject> iterator = crimes.iterator(); iterator.hasNext();) {
			Crime crime = (Crime) iterator.next();
			crimesText.add(crime.getIdCrime()+"|"+crime.getLatitude()+"|"+crime.getLongitude()+"|"+crime.getTipoCrime().getIdTipoCrime());
					
		}
		JSONArray jsonArray = new JSONArray(crimesText);
		saida.print(request.getParameter("jsoncallback")+"("+jsonArray.toString()+")");
		saida.close();
	}
	
	/*
	public void mapaDeKernel(HttpServletRequest request, HttpServletResponse response, PrintWriter outW) throws InterruptedException{
		PrintWriter saida = null;
		saida = new PrintWriter(outW, true);
		JSONObject resposta = new JSONObject();
		
		
		HttpSession httpSession = request.getSession();
		ServletContext context = httpSession.getServletContext();
		String realContextPath = context.getRealPath("//");
		realContextPath += barra + "images" + barra + "KernelMap"+ barra; 
	
		
		//Verifica se a imagem j� chegou no cliente
		if(request.getParameter("deletaImagem") != null){
//			Thread thread = new ThreadImage(realContextPath + imagemDiretorioAleatorio);
//			thread.start();
			KernelImageFilesManager.deletaImagem(httpSession);
			return;	
		}		
		String statusReq = request.getParameter("statusReq");
		if(statusReq.equals("Pri")){
			httpSession.setAttribute("northPixel",request.getParameter("northPixel"));
			httpSession.setAttribute("southPixel",request.getParameter("southPixel"));
			httpSession.setAttribute("eastPixel",request.getParameter("eastPixel"));
			httpSession.setAttribute("westPixel",request.getParameter("westPixel"));
			httpSession.setAttribute("width",request.getParameter("width"));			
			httpSession.setAttribute("height",request.getParameter("height"));
			if(httpSession.getAttribute("pontoXY")!=null)
				httpSession.setAttribute("pontoXY",httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
			else	
				httpSession.setAttribute("pontoXY",request.getParameter("pontoXY"));
		}
		if(statusReq.equals("SegOuMais")||statusReq.equals("Ult")){
			if(httpSession.getAttribute("pontoXY")!=null)
				httpSession.setAttribute("pontoXY",httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
			else
				httpSession.setAttribute("pontoXY",request.getParameter("pontoXY"));
				//httpSession.setAttribute("pontoXY",(String)httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
		}
		
		if(statusReq.equals("PriUlt")||statusReq.equals("Ult")){
			int northPixel;
			int southPixel;
			int eastPixel;
			int westPixel;
			int widthTela;
			int heightTela;
			String pontoXY;
			if(statusReq.equals("PriUlt")){
				northPixel = Integer.parseInt(request.getParameter("northPixel"));
				southPixel = Integer.parseInt(request.getParameter("southPixel"));
				eastPixel = Integer.parseInt(request.getParameter("eastPixel"));
				westPixel = Integer.parseInt(request.getParameter("westPixel"));
				widthTela = Integer.parseInt(request.getParameter("width"));
				heightTela = Integer.parseInt(request.getParameter("height"));
				pontoXY = request.getParameter("pontoXY");
			}else{
				if((String)httpSession.getAttribute("northPixel")==null|| ((String)httpSession.getAttribute("northPixel")).equals("")){
					Thread.sleep(5000);
				}
				if((String)httpSession.getAttribute("northPixel")==null|| ((String)httpSession.getAttribute("northPixel")).equals("")){
					Thread.sleep(10000);
				}
				northPixel = Integer.parseInt((String)httpSession.getAttribute("northPixel"));
				southPixel = Integer.parseInt((String)httpSession.getAttribute("southPixel"));
				eastPixel = Integer.parseInt((String)httpSession.getAttribute("eastPixel"));
				westPixel = Integer.parseInt((String)httpSession.getAttribute("westPixel"));
				widthTela = Integer.parseInt((String)httpSession.getAttribute("width"));
				heightTela = Integer.parseInt((String)httpSession.getAttribute("height"));
				pontoXY = (String)httpSession.getAttribute("pontoXY");
				httpSession.removeAttribute("northPixel");
				httpSession.removeAttribute("southPixel");
				httpSession.removeAttribute("eastPixel");
				httpSession.removeAttribute("westPixel");
				httpSession.removeAttribute("width");
				httpSession.removeAttribute("height");
				httpSession.removeAttribute("pontoXY");
				
			}
			Rectangle limitesPixel = new Rectangle(westPixel, northPixel, widthTela, heightTela);
			
			//calcula o mapa de kernel e gera a imagem
			KernelMap mapKernel = new KernelMap(GRID_NODE, BANDWIDTH, limitesPixel, getPoints(pontoXY)) ;
			String imagePath = KernelImageFilesManager.criarImagem(mapKernel, httpSession);
//			KernelMapRenderer kRend = new KernelMapRenderer(mapKernel);
//			RenderedImage imagem = (RenderedImage)kRend.pintaKernel();
//			httpSession.setAttribute(IMAGEM_KERNEL, imagem);
			
			//Envia informa��es resultates para o cliente
			resposta.put("topLeftX", westPixel);
			resposta.put("topLeftY", northPixel);
			resposta.put("bottomRightX", eastPixel);
			resposta.put("bottomRightY", southPixel);
			resposta.put("imagePath", imagePath);
			resposta.put("nada", "NADA");
			resposta.put("statuRes", "concluido");
		}else{
			resposta.put("statuRes", "nadaAinda");
		}
		saida.print(request.getParameter("jsoncallback")+"("+resposta.toString()+")");
		//saida.print(request.getParameter("jsoncallback")+"("+jsonArray.toString()+")");
		saida.close();
	}
	*/
	
	
	private static void gerarKernelMap(HttpServletRequest request, HttpServletResponse response, PrintWriter outW) throws IOException{
//		KernelMap mapKernel = null;
		PrintWriter saida = null;
		saida = new PrintWriter(outW, true);
		JSONObject resposta = new JSONObject();
		HttpSession httpSession = request.getSession();

		/*debug
		System.out.println("request parameter - northPixel: " + request.getParameter("northPixel") + 
				", westPixel: " + request.getParameter("westPixel") +
				", southPixel: " + request.getParameter("southPixel") + 
				", eastPixel: " + request.getParameter("eastPixel") + 
				", width: " + request.getParameter("width") + 
				", height: " + request.getParameter("height"));
		System.out.println("session attribute - northPixel: " + httpSession.getAttribute("northPixel") + 
				", westPixel: " + httpSession.getAttribute("northPixel") +
				", southPixel: " + httpSession.getAttribute("southPixel") + 
				", eastPixel: " + httpSession.getAttribute("eastPixel") + 
				", width: " + httpSession.getAttribute("width") + 
				", height: " + httpSession.getAttribute("height"));
		/*fim debug*/
		
		String statusReq = request.getParameter("statusReq");
		if(statusReq.equals("Pri")){
			httpSession.setAttribute("northPixel",request.getParameter("northPixel"));
			httpSession.setAttribute("southPixel",request.getParameter("southPixel"));
			httpSession.setAttribute("eastPixel",request.getParameter("eastPixel"));
			httpSession.setAttribute("westPixel",request.getParameter("westPixel"));
			httpSession.setAttribute("width",request.getParameter("width"));			
			httpSession.setAttribute("height",request.getParameter("height"));
			if(httpSession.getAttribute("pontoXY")!=null)
				httpSession.setAttribute("pontoXY",httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
			else	
				httpSession.setAttribute("pontoXY",request.getParameter("pontoXY"));
		}
		if(statusReq.equals("SegOuMais")||statusReq.equals("Ult")){
			if(httpSession.getAttribute("pontoXY")!=null)
				httpSession.setAttribute("pontoXY",httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
			else
				httpSession.setAttribute("pontoXY",request.getParameter("pontoXY"));
				//httpSession.setAttribute("pontoXY",(String)httpSession.getAttribute("pontoXY")+request.getParameter("pontoXY"));
		}
		
		if(statusReq.equals("PriUlt")||statusReq.equals("Ult")){
			int northPixel;
			int southPixel;
			int eastPixel;
			int westPixel;
			int widthTela;
			int heightTela;
			String pontoXY;
			if(statusReq.equals("PriUlt")){
				northPixel = Integer.parseInt(request.getParameter("northPixel"));
				southPixel = Integer.parseInt(request.getParameter("southPixel"));
				eastPixel = Integer.parseInt(request.getParameter("eastPixel"));
				westPixel = Integer.parseInt(request.getParameter("westPixel"));
				widthTela = Integer.parseInt(request.getParameter("width"));
				heightTela = Integer.parseInt(request.getParameter("height"));
				pontoXY = request.getParameter("pontoXY");
			}else{
				try{
					if((String)httpSession.getAttribute("northPixel")==null|| ((String)httpSession.getAttribute("northPixel")).equals("")){
						Thread.sleep(5000);
					}
					if((String)httpSession.getAttribute("northPixel")==null|| ((String)httpSession.getAttribute("northPixel")).equals("")){
						Thread.sleep(10000);
					}
				}catch(InterruptedException e){
					
				}
				northPixel = Integer.parseInt((String)httpSession.getAttribute("northPixel"));
				southPixel = Integer.parseInt((String)httpSession.getAttribute("southPixel"));
				eastPixel = Integer.parseInt((String)httpSession.getAttribute("eastPixel"));
				westPixel = Integer.parseInt((String)httpSession.getAttribute("westPixel"));
				widthTela = Integer.parseInt((String)httpSession.getAttribute("width"));
				heightTela = Integer.parseInt((String)httpSession.getAttribute("height"));
				pontoXY = (String)httpSession.getAttribute("pontoXY");
				httpSession.removeAttribute("northPixel");
				httpSession.removeAttribute("southPixel");
				httpSession.removeAttribute("eastPixel");
				httpSession.removeAttribute("westPixel");
				httpSession.removeAttribute("width");
				httpSession.removeAttribute("height");
				httpSession.removeAttribute("pontoXY");
				
			}
			Rectangle pixelBounds = new Rectangle(westPixel, northPixel, widthTela, heightTela);
			List<Point> points = getPoints("pontoXY");
			KernelMapRequestHandler kmrh = new KernelMapRequestHandler(request, points, pixelBounds);
			SessionBuffer sessionBuffer = new SessionBuffer(request);
			sessionBuffer.saveKernelMap(kmrh.getKernel(), kmrh.getImage());
			
			//Envia informacoes resultates para o cliente
			resposta.put("topLeftX", westPixel);
			resposta.put("topLeftY", northPixel);
			resposta.put("bottomRightX", eastPixel);
			resposta.put("bottomRightY", southPixel);
			resposta.put("nada", "NADA");
			resposta.put("statuRes", "concluido");
		}else{
			resposta.put("statuRes", "nadaAinda");
		}
		saida.print(request.getParameter("jsoncallback")+"("+resposta.toString()+")");
		//saida.print(request.getParameter("jsoncallback")+"("+jsonArray.toString()+")");
		saida.close();
		
	}
	
	private static List<Point> getPoints(String pontosStr){
		List<Point> pontos = new LinkedList<Point>();
		if(pontosStr == null || pontosStr.isEmpty())
			return pontos;
		for(String pontoStr : pontosStr.split("a")){
			pontos.add(new Ponto(pontoStr).invertido());  //FIXME desfazendo a inversao q foi feita la no JavaScript
		}
		return pontos;
	}
}
