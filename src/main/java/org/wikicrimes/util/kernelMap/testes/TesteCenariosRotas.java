package org.wikicrimes.util.kernelMap.testes;

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


public class TesteCenariosRotas extends HttpServlet{

	private static final String resultadosFileName = "resultados.txt";
	private static final String cenariosFileName = "cenarios.txt";
	private static final String paramFileName = "param.txt";
	private static final String dir = "/home/victor/Desktop/testes rotas/serie de testes/teste auto 2";
	
	private static Map<String,String> result = new HashMap<String, String>();
	private static Map<String,String> param;
	
//	/*teste*/
//	public static void main(String[] args) {
//		setResult("numCrimes", 4);
//		setResult("distReta", 2);
//		setResult("qualiIni", 3);
//		setResult("tol", 0);
//		setResult("distIni", 1);
//		setResult("distFin", 5);
//		salvar();
//		screenshot(1000);
//	}
//	/*teste*/
	
	//escrever resultados no arquivo
	public static void setResult(String key, Object value){
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
			String[] cenario = getStringCenario().split("\t");  
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
			b.append("\t" + getTamExp());
			b.append("\t" + getNumExp());
			b.append("\t" + getFatTol());
			//resultados dependentes dos paramentros
			b.append("\t" + result.get("distFin"));
			b.append("\t" + result.get("qualiFin"));
			b.append("\t" + result.get("tempo"));
			b.append("\t" + result.get("reqGM"));
			b.append("\t" + result.get("erro"));
			b.append("\n");
			
			System.out.println(b.toString());

			w.write(b.toString());
			w.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	//ler cenário do txt e passar pro javascript
	private static int cont = 0;
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		
		String acao = req.getParameter("acao"); 
		if(acao != null){
			if(acao.equals("cenario")){
				out.write(getStringCenario());
			
			}else if(acao.equals("param")){
//				screenshot(0);
				screenshot(1000);
//				screenshot(2000);
				cont++;
				param = getMapParam();
				if(param == null){
					out.write("fim");
					cont = 0;
				}
			}
		}
		
		out.close();
	}
	private static String getStringCenario(){
		try {
			
			File cenarioFile = new File(dir, cenariosFileName);
			BufferedReader r = new BufferedReader(new FileReader(cenarioFile));
			return r.readLine();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	
	//ler parametros do txt e passar pro ServletRotaSegura e LogicaRotaSegura
	private static Map<String,String> getMapParam(){
		try {
			
			File paramFile = new File(dir, paramFileName);
			BufferedReader r = new BufferedReader(new FileReader(paramFile));
			
			String stringParam = null;
			for(int i=0; i<=cont; i++){
				stringParam = r.readLine();
				if(stringParam.equals("fim"))
					return null;
			}
			
			String[] s = stringParam.split("\t");
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("tamExp", s[0]);
			map.put("numExp", s[1]);
			map.put("fatTol", s[2]);
			
			return map;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static int getTamExp(){
		if(param == null)
			param = getMapParam();
		return Integer.valueOf(param.get("tamExp"));
	}
	public static int getNumExp(){
		if(param == null)
			param = getMapParam();
		return Integer.valueOf(param.get("numExp"));
	}
	public static double getFatTol(){
		if(param == null)
			param = getMapParam();
		return Double.valueOf(param.get("fatTol"));
	}
	
	
	
	public static void screenshot(int milis){
		try {
			
			//espera um poquim pra rota verde aparecer
			Thread.sleep(milis);
			
			//screenshot
	        Robot robot = new Robot();
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        Rectangle captureSize = ge.getMaximumWindowBounds(); 
	        BufferedImage image = robot.createScreenCapture(captureSize);
	        
	        //nome da imagem
	        StringBuilder b = new StringBuilder();
	        //cenario
	        String[] cenario = getStringCenario().split("\t");  
			b.append(cenario[0]); //id
			b.append(", " + cenario[1]); //nome
			//parametros
			b.append("." + getTamExp());
			b.append("." + getNumExp());
			b.append("." + getFatTol());
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
}
