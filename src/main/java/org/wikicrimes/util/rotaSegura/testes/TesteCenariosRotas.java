package org.wikicrimes.util.rotaSegura.testes;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wikicrimes.util.NumerosUtil;


@SuppressWarnings("serial")
public class TesteCenariosRotas extends HttpServlet{

	private static final String resultadosFileName = "resultados.txt";
	private static final String cenariosFileName = "cenarios.txt";
	private static final String paramFileName = "param.txt";
	private static final String toleranciasFileName = "tolerancias.txt";
	public static final String dir = "/home/victor/Desktop/testes/rotas 2010.12.06/novo";
	
	private static Map<String,String> result = new HashMap<String, String>();
	private static Map<String,String> param;
	public static String idCenarioAtual;
	private static String stringCenarioAtual;
	
	//escrever resultados no arquivo
	public static void setResult(String key, Object value){
		if(value instanceof Double || value instanceof Float){
			double d = Double.parseDouble(value.toString());
			value = NumerosUtil.roundToDecimals(d, 5);
		}
		result.put(key, String.valueOf(value));
	}
	public static void salvar(){
		try {
			File resultadosFile = new File(dir, resultadosFileName);
			Writer w = new FileWriter(resultadosFile, true);
			StringBuilder b = new StringBuilder();
			//data e hora
			b.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			//cenario
			String[] cenario = stringCenarioAtual.split("\t");  
			b.append("\t" + cenario[0]); //id
			b.append("\t" + cenario[1]); //nome
			//resultado direto do cenário
//			b.append("\t" + result.get("numCrimes"));
//			b.append("\t" + result.get("densMedia"));
//			b.append("\t" + result.get("densMax"));
//			b.append("\t" + result.get("distReta"));
//			b.append("\t" + result.get("tol"));
//			b.append("\t" + result.get("distIni"));
//			b.append("\t" + result.get("qualiIni"));
			//parametros
//			b.append("\t" + getFatTol());
			//resultados dependentes dos paramentros
			b.append("\t" + result.get("distFin"));
			b.append("\t" + result.get("qualiFin"));
			b.append("\t" + result.get("tempo"));
			b.append("\t" + result.get("reqGM"));
			b.append("\t" + result.get("erro"));
			b.append("\n");
			
			result = new HashMap<String, String>();
//			System.out.println(b.toString());

			w.write(b.toString());
			w.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	//ler cenário do txt e passar pro javascript
	private static int contParam = 0;
	private static int contCenario = 0;
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		PrintWriter out = resp.getWriter();
		
		String acao = req.getParameter("acao"); 
		if(acao != null){
			if(acao.equals("cenarioPrimeiraLinha")){
				String stringCenario = getStringCenarioPrimeiraLinha();
				out.write(stringCenario);
			
			}else if(acao.equals("cenarioSequencia")){
				contCenario++;
				TesteRotasImg.limpar();
				String stringCenario = getStringCenarioSequencia();
				if(stringCenario.equals("fim"))
					contCenario = 0;
				out.write(stringCenario);
			
			}else if(acao.equals("screenshot")){
				int tempo = Integer.parseInt(req.getParameter("tempo"));
				try{
					Thread.sleep(tempo);
					screenshot();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				out.write("screenshot ok");
				
			}else if(acao.equals("param")){
				contParam++;
				param = getMapParam();
				if(param == null){
					out.write("fim");
					contParam = 0;
				}
			}
		}
		
		out.close();
	}
	
	private static String getStringCenarioPrimeiraLinha(){
		try {
			
			File cenarioFile = new File(dir, cenariosFileName);
			BufferedReader r = new BufferedReader(new FileReader(cenarioFile));
			String stringCenario = r.readLine();
			stringCenarioAtual = stringCenario;
			return stringCenario;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	private static String getStringCenarioSequencia(){
		try {
			
			File cenarioFile = new File(dir, cenariosFileName);
			BufferedReader r = new BufferedReader(new FileReader(cenarioFile));
			
			String stringCenario = null;
			
			String primeiraPalavra = r.readLine().split("\t")[0]; 
			while(!primeiraPalavra.equals("#cen")){
				primeiraPalavra = r.readLine().split("\t")[0];
			}
			
			for(int i=0; i<contCenario; i++){
				stringCenario = r.readLine();
				if(stringCenario.equals("fim"))
					return "fim";
			}
			
			stringCenarioAtual = stringCenario;
			idCenarioAtual = stringCenario.split("\t")[0];
			
			return stringCenario;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String getStringCenarioAtual(){
		return stringCenarioAtual;
	}
	
	//ler parametros do txt e passar pro ServletRotaSegura e LogicaRotaSegura
	private static Map<String,String> getMapParam(){
		try {
			
			File paramFile = new File(dir, paramFileName);
			BufferedReader r = new BufferedReader(new FileReader(paramFile));
			
			String stringParam = null;
			for(int i=0; i<=contParam; i++){
				stringParam = r.readLine();
				if(stringParam.equals("fim"))
					return null;
			}
			
			String[] s = stringParam.split("\t");
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("fatTol", s[0]);
			
			return map;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static double getFatTol(){
		if(param == null)
			param = getMapParam();
		return Double.valueOf(param.get("fatTol"));
	}
	
	
	public static void screenshot(){
		try {
			
			//screenshot
	        Robot robot = new Robot();
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        Rectangle captureSize = ge.getMaximumWindowBounds(); 
	        BufferedImage image = robot.createScreenCapture(captureSize);
	        
	        //nome da imagem
	        String[] cenario = stringCenarioAtual.split("\t");  
	        StringBuilder b = new StringBuilder();
	        //cenario
			b.append(cenario[0]); //id
			b.append(", " + cenario[1]); //nome
			//parametros
			b.append(", " + getFatTol()); //parametro
			//data e hora
			b.append(", " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
			String imageName = b.toString();
			File imageFile = new File(dir, imageName);
			
			//salva arquivo
			ImageIO.write(image, "JPG", imageFile);
			
	    }catch(AWTException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void writeToleranceToFile(double tol, double distMin, double densMedia, double densMax) {
		try {
			File file = new File(dir, toleranciasFileName);
			if(!file.exists()) {
				file.createNewFile();
				Writer w = new FileWriter(file, true);
				w.append("#cen\ttol\tdistMin\tdensMedia\tdensMax\n");
				w.close();
			}
			Writer w = new FileWriter(file, true);
			String linha = idCenarioAtual + "\t" + tol + "\t" + distMin + "\t" + densMedia + "\t" + densMax + "\n"; 
			w.append(linha);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
