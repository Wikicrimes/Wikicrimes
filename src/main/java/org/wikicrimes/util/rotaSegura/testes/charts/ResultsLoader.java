package org.wikicrimes.util.rotaSegura.testes.charts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.util.DataUtil;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Definition;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Iteration;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Scenario;

public class ResultsLoader {

	static final String dir = "/home/victor/Desktop/testes/rotas 2010.12.06/novo";
	private static final String scenariosFileName = "cenarios.txt";
	private static final String iterationsFileName = "resultados.txt";
	private static final String manualAppreciationsFileName = "avaliacao.txt";
	private static final String toleranceValuesFileName = "tolerancias.txt";
	
	public static ResultsModel loadScenarioTests() {
		ResultsLoader loader = new ResultsLoader();
		ResultsModel results = new ResultsModel(); 
		loader.readScenarios(results);
		loader.readIterations(results);
		loader.readEvaluations(results);
		loader.readTolerances(results);
		return results;
	}
	
	private void readScenarios(ResultsModel results) {
		try {
			
			BufferedReader r = getToFirstLineToRead(scenariosFileName, "#cen");
			
			//le cada cenario de cada linha
			results.scenarios = new HashMap<Integer, Scenario>();
			while(true){
				String linha = r.readLine();
				if(linha.equals("fim"))
					break;
				
				Scenario cenario = parseScenario(linha);
				results.scenarios.put(cenario.id, cenario);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Scenario parseScenario(String line) {
		
		String[] split = line.split("\t");
		
		int id = Integer.valueOf(split[0]);
		String nome = split[1];
		Scenario cenario = new Scenario(id, nome);
		
		double centroLat = Double.valueOf(split[2]);
		double centroLng = Double.valueOf(split[3]);
		PontoLatLng centro = new PontoLatLng(centroLat, centroLng);
		int zoom = Integer.valueOf(split[4]);
		double origemLat = Double.valueOf(split[5]);
		double origemLng = Double.valueOf(split[6]);
		PontoLatLng origem = new PontoLatLng(origemLat, origemLng);
		double destinoLat = Double.valueOf(split[7]);
		double destinoLng = Double.valueOf(split[8]);
		PontoLatLng destino = new PontoLatLng(destinoLat, destinoLng);
		Date dataInicial = DataUtil.toDate(split[9]);
		Date dataFinal = DataUtil.toDate(split[10]);
		Definition definicao = new Definition(centro, origem, destino, zoom, dataInicial, dataFinal);
		cenario.definition = definicao;
		
		return cenario;
	}
	
	private void readIterations(ResultsModel results) {
		try {
			
			BufferedReader r = getToFirstLineToRead(iterationsFileName, "data");
			
			//le cada iteracao de cada linha
			Integer previousId = null;
			int ordinality = 0;
			while(true){
				String linha = r.readLine();
				if(linha == null)
					break;
				
				Iteration iteration = parseIteration(linha);
				Integer id = iteration.idCenario;
				if(id != previousId) {
					ordinality = 0;
					previousId = id;
				}
				iteration.ordinality = ordinality;
				Scenario cenario = results.scenarios.get(id);
				cenario.results.iterations.add(iteration);
				
				ordinality++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Iteration parseIteration(String line) {
		
		String[] split = line.split("\t");
		
		int id = Integer.valueOf(split[1]);
		double distanceSoFar = Double.valueOf(split[3]);
		double qualitySoFar = Double.valueOf(split[4]);
		double timeRun = Double.valueOf(split[5]);
		int gmRequestsSoFar = Integer.valueOf(split[6]);
		
		return new Iteration(id, distanceSoFar, qualitySoFar, timeRun, gmRequestsSoFar);
	}
	
	private void readEvaluations(ResultsModel results) {
		try {
			
			BufferedReader r = getToFirstLineToRead(manualAppreciationsFileName, "#cen");
			
			//le a avaliacao de cenario em cada linha
			while(true){
				String linha = r.readLine();
				if(linha == null)
					break;
				
				String[] split = linha.split("\t");
				
				int id = Integer.valueOf(split[0]);
				
				Integer algorithmStop = null;
				if(!split[1].equals("-"))
					algorithmStop = Integer.valueOf(split[1]);
				
				Integer manualStop = null;
				if(split.length > 2)
					manualStop = Integer.valueOf(split[2]);
				
				Scenario cenario = results.scenarios.get(id);
				cenario.results.satisfactoryStop = algorithmStop;
				cenario.results.firstImprovement = manualStop;
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readTolerances(ResultsModel results) {
		try {
			
			BufferedReader r = getToFirstLineToRead(toleranceValuesFileName, "#cen");
			
			//le a avaliacao de cenario em cada linha
			while(true){
				String linha = r.readLine();
				if(linha == null)
					break;
				
				String[] split = linha.split("\t");
				
				int id = Integer.valueOf(split[0]);
				Double tolerance = Double.valueOf(split[1]);
				Double minDistance = Double.valueOf(split[2]);
				Double avgDensity = Double.valueOf(split[3]);
				Double maxDensity = Double.valueOf(split[4]);
				
				Scenario cenario = results.scenarios.get(id);
				cenario.results.tolerance = tolerance;
				cenario.results.minDistance = minDistance;
				cenario.results.avgDensity = avgDensity;
				cenario.results.maxDensity = maxDensity;
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedReader getToFirstLineToRead(String fileName, String startMarker) {
		
		try {
		
			File file = new File(dir, fileName);
			BufferedReader r = new BufferedReader(new FileReader(file));
			
			//acha a primeira linha de resultado
			String firstWord = r.readLine().split("\t")[0]; 
			while(!firstWord.equals(startMarker)){
				firstWord = r.readLine().split("\t")[0];
			}
		
			return r;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
