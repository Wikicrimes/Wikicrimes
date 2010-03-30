package org.wikicrimes.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.hibernate.SessionFactory;
import org.jdom.JDOMException;
import org.springframework.context.ApplicationContext;
import org.wikicrimes.model.Usuario;

import br.com.exceptions.PredicadoException;
import br.com.exceptions.SujeitoException;
import br.com.modelo.Conceito;
import br.com.modelo.Palavra;
import br.com.modelo.Pergunta;
import br.com.modelo.Resposta;
import br.com.modelo.Sentenca;
import br.com.modelo.redeInferencial.GrafoInferencial;
import br.com.parser.ArvoreSentencas;
import br.com.parser.ParametrosSIA;
import br.com.parser.SIA;
import br.com.parser.SIE;

public class ServletUbiq extends HttpServlet {
	
	private static final long serialVersionUID = -4896169276863055650L;

	private static ApplicationContext ctx;
	private static SessionFactory sf;

	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		response.setHeader ("Pragma", "no-cache");
		response.setHeader ("Cache-Control", "no-cache");
		//Para resolver o problema da codificação de caractere tem que setar um setHeade com o codigo
		response.setDateHeader ("Expires", 0);
		//ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		//CrimeForm crimeForm = (CrimeForm)ctx.getBean("crimeIEForm");  only for services
		FacesContext faces = FacesContext.getCurrentInstance();
		List<GrafoInferencial> grafosInferenciais = new ArrayList<GrafoInferencial>();
		 List<Sentenca> sentencas = new ArrayList<Sentenca>();
		PrintWriter out = response.getWriter();
		String acao = request.getParameter("acao");
		String resposta = "0";
		String endereco ="0";
		String acusacao ="0";
				
		if(acao.equalsIgnoreCase("1")){
			Usuario u = (Usuario)request.getSession().getAttribute("usuario");
			if (u!=null)
				resposta = "1";
			else
				resposta= "3";
			out.println(resposta);
		}
		
		if(acao.equalsIgnoreCase("2")){
			/*String texto = "Na noite de sábado último, um jovem identificado como " +
					"Geison Santos de Oliveira, foi executado com vários tiros. " +
					"O crime ocorreu na Rua Titan 33, que já foi palco de diversos " +
					"delitos naquele bairro.";*/
			resposta = "2";
			String texto = request.getParameter("texto");
			System.out.println(texto);
			String textAnalyzed =getSyntacticAnalysis(texto);
		    sentencas = ArvoreSentencas.geraArvore(textAnalyzed);
			Set<Pergunta> objetivos = new HashSet<Pergunta>();
			int paramF = 1;
			Double valorCorte = 10.0;
			ParametrosSIA parametrosSIA = new ParametrosSIA(paramF,valorCorte);
			
			// 1a.Pergunta) "where?" sobre objeto "crime" 
			Conceito crime = Conceito.getConceitoBD("crime");
			//grafoConceitos.addConceito(crime);
			crime.populaArestasBD(paramF);
			
			Pergunta perguntaAux = new Pergunta("Onde?",crime,1);
			
			Conceito conceitoRelacionado = Conceito.getConceitoBD("local");
			//grafoConceitos.addConceito(conceitoRelacionado);
			conceitoRelacionado.populaArestasBD(paramF);
			perguntaAux.addConceitoRelacionado(conceitoRelacionado);
			
			conceitoRelacionado = Conceito.getConceitoBD("endereço");
			//grafoConceitos.addConceito(conceitoRelacionado);
			conceitoRelacionado.populaArestasBD(paramF);
			perguntaAux.addConceitoRelacionado(conceitoRelacionado);
			
			objetivos.add(perguntaAux);
			
			// 2a.Pergunta) "What Kind Of?" sobre objeto "crime" (pergunta com tipoResposta=2-Alternativa Unica) 
			perguntaAux = new Pergunta("Qual tipo de?",crime,2);
			conceitoRelacionado = Conceito.getConceitoBD("tipo");
			//grafoConceitos.addConceito(conceitoRelacionado);
			conceitoRelacionado.populaArestasBD(paramF);
			perguntaAux.addConceitoRelacionado(conceitoRelacionado);
			perguntaAux.setQtdRespostasAlternativas(1);
			
			//---- Respostas Alternativas
	  		  
			Conceito roubo = Conceito.getConceitoBD("roubo");
			//grafoConceitos.addConceito(furto);
			roubo.populaArestasBD(paramF);
			
			Conceito furto = Conceito.getConceitoBD("furto");
			//grafoConceitos.addConceito(furto);
			furto.populaArestasBD(paramF);
			
			Conceito violencia = Conceito.getConceitoBD("violência");
			//grafoConceitos.addConceito(violencia);
			violencia.populaArestasBD(paramF);
			
			Conceito tentativa = Conceito.getConceitoBD("tentativa");
			//grafoConceitos.addConceito(tentativa);
			tentativa.populaArestasBD(paramF);
			
			Conceito amante = Conceito.getConceitoBD("amante");
			//grafoConceitos.addConceito(amante);
			amante.populaArestasBD(paramF);
			
			Conceito marido = Conceito.getConceitoBD("marido");
			//grafoConceitos.addConceito(marido);
			marido.populaArestasBD(paramF);
			
			Conceito esposa = Conceito.getConceitoBD("esposa");
			//grafoConceitos.addConceito(esposa);
			esposa.populaArestasBD(paramF);
			
			Conceito luta = Conceito.getConceitoBD("luta");
			//grafoConceitos.addConceito(luta);
			luta.populaArestasBD(paramF);
			
			Conceito abuso = Conceito.getConceitoBD("abuso");
			//grafoConceitos.addConceito(abuso);
			abuso.populaArestasBD(paramF);
			
			Conceito autoridade = Conceito.getConceitoBD("autoridade");
			//grafoConceitos.addConceito(autoridade);
			autoridade.populaArestasBD(paramF);
			
			Conceito morte = Conceito.getConceitoBD("morte");
			//grafoConceitos.addConceito(morte);
			morte.populaArestasBD(paramF);
			
			Conceito homicidio = Conceito.getConceitoBD("homicídio");
			//grafoConceitos.addConceito(homicidio);
			homicidio.populaArestasBD(paramF);
			
			Resposta respostaTreinamento = new Resposta("Roubo");
			respostaTreinamento.addConceitoRelacionado(roubo);
			//resposta.addConceitoRelacionado(violencia);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Tentativa de Roubo");
			respostaTreinamento.addConceitoRelacionado(roubo);
			//resposta.addConceitoRelacionado(violencia);
			respostaTreinamento.addConceitoRelacionado(tentativa);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Furto");
			respostaTreinamento.addConceitoRelacionado(furto);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Tentativa de Furto");
			respostaTreinamento.addConceitoRelacionado(furto);
			respostaTreinamento.addConceitoRelacionado(tentativa);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Violencia Domestica");
			respostaTreinamento.addConceitoRelacionado(violencia);
			respostaTreinamento.addConceitoRelacionado(amante);
			respostaTreinamento.addConceitoRelacionado(marido);
			respostaTreinamento.addConceitoRelacionado(esposa);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Rixas ou Brigas");
			respostaTreinamento.addConceitoRelacionado(luta);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Abuso de Autoridade");
			respostaTreinamento.addConceitoRelacionado(abuso);
			respostaTreinamento.addConceitoRelacionado(autoridade);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Homicidio");
			//resposta.addConceitoRelacionado(morte);
			respostaTreinamento.addConceitoRelacionado(homicidio);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Tentativa de Homicidio");
			//resposta.addConceitoRelacionado(morte);
			respostaTreinamento.addConceitoRelacionado(homicidio);
			respostaTreinamento.addConceitoRelacionado(tentativa);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			respostaTreinamento = new Resposta("Latrocinio");
			respostaTreinamento.addConceitoRelacionado(homicidio);
			respostaTreinamento.addConceitoRelacionado(roubo);
			//resposta.addConceitoRelacionado(violencia);
			perguntaAux.addRespostaAlternativa(respostaTreinamento);
			
			objetivos.add(perguntaAux);      
			
			try {
				 grafosInferenciais = SIA.main(sentencas, objetivos, parametrosSIA);
			
				
			} catch (PredicadoException e) {
				System.out.println(e.getMessage());
				return;
			} catch (SujeitoException e) {
				System.out.println(e.getMessage());
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (JDOMException e) {
				
				System.out.println("entrou");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			// Extrair informações conforme Objetivos
			if (objetivos!=null) {
				SIE.verificaObjetivos(grafosInferenciais, sentencas, objetivos, valorCorte);
				
				System.err.println("***Entendendo Respostas***");
				
				for (Pergunta pergunta : objetivos) {
		        	System.out.println("Pergunta: "+pergunta.getDescricao() + "-" + pergunta.getObjeto().getNomePortugues());
		        	if (pergunta.temResposta()) {
		        		System.out.println("R: "+pergunta.getRespostaAresta().getDescricao());
		        	
		        		// Gerando Pergunta.respostaTexto ou Pergunta.respostaAlternativa
		            	
		            	// 1a.Pergunta) Onde ocorreu o "crime"?
		            	if (pergunta.getDescricao()=="Onde?") {
		            		pergunta.setRespostaTexto(respostaWhere(pergunta));
		            		endereco = pergunta.getRespostaTexto();
		            		endereco = endereco.trim();
		            		System.out.println("Resposta Texto: "+pergunta.getRespostaTexto());
		            	}
		            	
		            	// 2a.Pergunta) Qual o tipo de "crime"?
		            	if (pergunta.getDescricao()=="Qual tipo de?") {
		            		pergunta.setRespostaTexto(respostaWhatKindOf(pergunta));
		            		acusacao = pergunta.getRespostaTexto();
		            		acusacao = acusacao.trim();
		            		System.out.println("Resposta Texto: "+pergunta.getRespostaTexto());
		            	}
		            }
		        	else {
		        		System.out.println("R: Resposta NAO encontrada");
		        	}
		        	System.out.println();
		        	
		        	
		        }
				if(acusacao.equalsIgnoreCase("Tentativa de Roubo")){
					acusacao="1";
				}else if(acusacao.equalsIgnoreCase("Tentativa de Furto")){
					acusacao="2";
				}else if(acusacao.equalsIgnoreCase("Furto")){
					acusacao="3";
				}else if(acusacao.equalsIgnoreCase("Roubo")){
					acusacao="4";
				}else{
					acusacao="5";
				}
				
				
//				for (Pergunta pergunta : objetivos) {
//					if(pergunta.getParteSentencaResposta().getAllTexto()!=null){
//						endereco = pergunta.getParteSentencaResposta().getAllTexto();
//					}else{
//						endereco ="Endereço não encontrado";
//					}
//					for (Resposta respostaSelecionada : pergunta.getRespostaAlternativaSelecionada()) {
//						System.out.println("----> Alternativa Selecionada: "+ respostaSelecionada.getDescricao());
//						acusacao = respostaSelecionada.getDescricao();  
//					}
//					
//				}
				
			}
			
			else {
				System.err.println("SIA Não recebeu objetivos !!!");
			}
					
			
			
//			String texto = request.getParameter("texto");
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1; else idSIA++; 
//			InterfaceSIA.setIdSIA(idSIA);
//			ArquivoOutputStream.atribuiArquivoSaida("resultadoSIA_"+idSIA.toString()+".rtf");
//			//ArquivoOutputStream.atribuiArquivoSaida("resultadoIE");
//			String textAnalyzed =getSyntacticAnalysis(texto);
//			System.out.println(texto);
//			List<Sentenca> sentencas = ArvoreSentencas.geraArvore(textAnalyzed);
//			Set<Pergunta> objetivos = new HashSet<Pergunta>();
//			
//			// 1a.Pergunta "where?" sobre objeto "crime" 
//			Conceito crime = Conceito.getConceitoBD("crime");
//			crime.populaArestasBD();
//			Pergunta perguntaAux = new Pergunta("Where?",crime,1);
//			Conceito conceitoRelacionado = Conceito.getConceitoBD("local");
//			conceitoRelacionado.populaArestasBD();
//			perguntaAux.addConceitoRelacionado(conceitoRelacionado);
//			conceitoRelacionado = Conceito.getConceitoBD("endereço");
//			conceitoRelacionado.populaArestasBD();
//			perguntaAux.addConceitoRelacionado(conceitoRelacionado);
//			objetivos.add(perguntaAux);
//			String endereco = new String();
//		try{	
//			SIA.main(sentencas, objetivos);
//			for (Pergunta pergunta : objetivos){
//				System.out.println("Pergunta: "+pergunta.getDescricao() + "-" + pergunta.getObjeto().getNomePortugues()); 
//				if (pergunta.temResposta() && pergunta.getDescricao()=="Where?") {
//					 System.out.println("R: "+pergunta.getRespostaAresta().getDescricao()); 
//					pergunta.setRespostaTexto(InterfaceSIA.respostaWhere(pergunta));
//					endereco = pergunta.getRespostaTexto();
//					System.out.println("Resposta Texto: "+pergunta.getRespostaTexto()); 
//				} else {
//			         System.out.println("R: Resposta NAO encontrada");
//		         }
//		         System.out.println();
//			}
//		}  catch (Exception e){}   
//		if (endereco.equalsIgnoreCase("Indefinida"))
//			endereco = "";
//		out.println(endereco);
		
		
		
			/*CrimeIEForm crimeIEForm = (CrimeIEForm) faces.getApplication()
				.evaluateExpressionGet(faces, "#{crimeIEForm}",
						CrimeIEForm.class);
				crimeIEForm = new CrimeIEForm();
				crimeIEForm.setDescricao("Crime registrado pelo Ubiquity!");
				resposta = "2";*/
		//	resposta ="2";
		//	System.out.println(resposta);

			out.println(resposta+"@"+endereco+"@"+acusacao);
		}
			
	}

	public static String getSyntacticAnalysis(String texto) throws HttpException, IOException{
		
		//parametros  a serem enviados para processamento via post
		NameValuePair[] data = {
				new NameValuePair("text", texto),
				new NameValuePair("visual", "cg-dep"),
				new NameValuePair("parser", "dep-eb"),				
				new NameValuePair("symbol", "unfiltered")			
		};

		PostMethod post = new PostMethod("http://beta.visl.sdu.dk/");//URI
		post.setPath("/visl/pt/parsing/automatic/dependency.php");//caminho da aplicacao
		post.setRequestBody(data);
		/*String auth_password = new String();
		
		 try {
			    MessageDigest md = MessageDigest.getInstance("SHA-1");
			    md.update("daa260fb6c".getBytes());
			    auth_password = md.digest().toString();
			  } catch (NoSuchAlgorithmException e) {  }
			  
		NameValuePair[] data = {
				new NameValuePair("auth_key", "vladiacelia"),
				//new NameValuePair("auth_password", auth_password),
				//new NameValuePair("mode", "flat"),
				//new NameValuePair("text", texto),
				//new NameValuePair("lang", "pt")			
		};

		PostMethod post = new PostMethod("http://beta.visl.sdu.dk/");//URI
		post.setPath("/tools/remoting.html");//caminho da aplicacao
		post.setRequestBody(data);*/
		
		
		//executa a resquicao
		HttpClient client = new HttpClient();
		client.executeMethod(post);

		//recebe a resposta como um InputStream
		InputStream in = post.getResponseBodyAsStream();
		
		//converte o InputStream recebido em uma string (todo o codigo fonte da pagina recebida)
		//String pag = convertStreamToString(in);
		//System.out.println(pag);
		//converte o InputStream recebido em uma string (somente o codigo fonte da resposta a partir da pagina recebida)
		String resposta = convertStreamToStringResposta(in);
		System.out.println(resposta);
		return resposta;
	}

	public static String respostaWhere(Pergunta pergunta) {
		String respostaTexto = "Indefinida";
		List<Palavra> palavrasResposta = pergunta.getRespostaAresta().getParteSentencaDominio().getAllPalavras();
		for (Palavra palavra : palavrasResposta) {
			//System.out.println("palavra Resposta: "+ palavra.getNomeOriginal()+"-"+palavra.getClasseGramatical());
			if (palavra.getClasseGramatical().equalsIgnoreCase("PROP")) {
				respostaTexto = palavra.getNomeOriginal() + " ";
				for (Palavra palavraFilho : palavra.getFilhosPalavra()) {
					if (palavraFilho.getClasseGramatical().equalsIgnoreCase("NUM") || palavraFilho.getClasseGramatical().equalsIgnoreCase("PROP")) {
						respostaTexto += palavraFilho.getNomeOriginal() + " ";				
					}
				}
				respostaTexto = respostaTexto.replace("="," ");
				return respostaTexto; 
			}
		}
		return respostaTexto;
	}
	public static String respostaWhatKindOf(Pergunta pergunta) {
		String respostaTexto = "";
		List<Resposta> respostaAlternativaSelecionada = pergunta.getRespostaAlternativaSelecionada();
		for (Resposta respostaSelecionada : respostaAlternativaSelecionada) {
			respostaTexto += respostaSelecionada.getDescricao() + " ";
		}
		if (respostaTexto.equals("")) {
			respostaTexto="Indefinida";
		}
		return respostaTexto;
	}
	public static String convertStreamToStringResposta(InputStream in) {
		
		/*To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.*/
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
	
		String line = null;
		boolean resposta = false;
		try {
			while ((line = reader.readLine()) != null) {
						
				//o inicio da resposta comeca com a tag "<dl>"
				if (line.trim().startsWith("<dl>"))	resposta = true;
							
				if (resposta) {
					sb.append(line.trim() + "\n");
				}
				//o fim da resposta é feito com a tag "</dl>"
				if (resposta) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return sb.toString();
	}
}