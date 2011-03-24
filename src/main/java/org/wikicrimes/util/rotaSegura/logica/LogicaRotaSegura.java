package org.wikicrimes.util.rotaSegura.logica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.wikicrimes.util.kernelmap.KernelMap;
import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.googlemaps.ConstantesGM;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.RotaPromissora;

/**
 * @author victor
 *	Agrupa a l�gica para c�lculo de rotas seguras. Um objeto LogicaRotaSegura calcula rotas para o MapaKernel associado a ele.
 */
public class LogicaRotaSegura {
	
	private KernelMap kernel;
	private GrafoRotas grafo;
	private CalculoPerigo calcPerigo;
	
	//PARAMETROS
	public static final int GRANULARIDADE_ROTAS = PropertiesLoader.getInt("saferoutes.route_granularity");
	
	public LogicaRotaSegura(KernelMap kernel){
		this.kernel = kernel;
	}
	
	/**
	 * Divide um caminho em partes toler�veis e partes n�o toler�veis.
	 * problema: se vc tem 2 caminhos toler�veis, ao juntar pode dar um caminho nao toler�vel, j� q a distancia percorrida aumenta
	 */
	public List<Rota> separarRotasToleraveis(Rota rota){
		//TODO nao ta prestando pq qd c junta 2 peda�os toleraveis, o resultado pode nao ser
		CalculoPerigo calcPerigo = getCalculoPerigo();
		List<Rota> rotas = new LinkedList<Rota>();
		double tam = rota.distanciaPercorrida();
		Ponto inicio = rota.getInicio();
		Ponto fim = inicio;
		Rota anterior = null;
		boolean eraToleravel = true;
		for(int i=1; i*GRANULARIDADE_ROTAS<=tam; i++){
			double dist = i*GRANULARIDADE_ROTAS;
			fim = rota.searchPoint(dist);
			Rota r = rota.subRota(inicio, fim);
			if(eraToleravel != calcPerigo.isToleravel(r)){
				if(anterior != null){ //nao � a primeira itera��o
					rotas.add(anterior);
					inicio = anterior.getFim();
					r = rota.subRota(inicio, fim);
				}
				eraToleravel = !eraToleravel;
			}
			anterior = r;
		}
		
		//ultima vez (o q restou da ultima iteracao at� o fim do caminho)
		fim = rota.getFim();
		Rota r = rota.subRota(inicio, fim);
		if(eraToleravel != calcPerigo.isToleravel(r) && anterior != null){
			rotas.add(anterior); //parte anterior
			r = rota.subRota(anterior.getFim(), fim); //parte q mudou
		}
		rotas.add(r); //parte q mudou (se entrou no if) OU parte anterior at� o fim (se eraToleravel==isToleravel(c)) OU a rota inteira (se anterior==null)
		
		return rotas;
	}
	
	/**
	 * Dividir um caminho entre partes que passam em �reas perigosas e partes que passam em �reas seguras.
	 * A dist�ncia n�o � considerada.
	 */
	public List<Rota> separarPartesPerigosas(Rota caminho){
		CalculoPerigo calcPerigo = getCalculoPerigo();
		List<Rota> caminhos = new LinkedList<Rota>();
		double tol = kernel.getMediaDens()*CalculoPerigo.FATOR_TOLERANCIA;
		double tam = caminho.distanciaPercorrida();
		Ponto inicio = caminho.getInicio();
		Ponto fim = inicio;
		boolean eraSeguro = true;
		for(int i=1; i*GRANULARIDADE_ROTAS<=tam; i++){
			double dist = i*GRANULARIDADE_ROTAS;
			fim = caminho.searchPoint(dist);
			double dif = calcPerigo.perigo(fim)-tol;
			if(eraSeguro != dif>0){
				Rota c = caminho.subRota(inicio, fim);
				caminhos.add(c);
				inicio = fim;
				eraSeguro = !eraSeguro;
			}
		}
		Rota c = caminho.subRota(inicio, caminho.getFim());
		caminhos.add(c); //ultima parte 
		return caminhos;
	}
	
	private List<Rota> rotasJaPassadas = new ArrayList<Rota>();
	
	public Queue<Rota> criarAlternativas(Rota rota, int iteracao){
//		/*DEBUG*/TesteRotasImg.teste(rota, "criarAlternativas, parametro", this);
		Queue<Rota> rotas = new PriorityQueue<Rota>();
		
		if(rota.size() < 2)
			return rotas;
		
		//descartar parametros ja passados anteriormente
		if(rotasJaPassadas.contains(rota)){
			return rotas;
		}else{
			rotasJaPassadas.add(rota);
		}
		
		if(iteracao == 0){
			
			//TODO rodar essas 2 estrategias denovo em outras iteracoes, 
			//atualizando soh as ligacoes da origem e destino pro grafo gerado na estrategia 
			
			Queue<Rota> altCL = new AlternativasGrafoDeCaminhosLivres(this).getAlternativas(rota);
//			/*DEBUG*/TesteRotasImg.teste(altCL, "criarAlternativas, CL", this);
			rotas.addAll(altCL);
			
			Queue<Rota> altGV = new AlternativasGrafoVisibilidade(this).getAlternativas(rota);
//			/*DEBUG*/TesteRotasImg.teste(altGV, "criarAlternativas, GV", this);
			rotas.addAll(altGV);
			
			rotas = dividirRotasGrandes(rotas);
			
		}
		
		Queue<Rota> altPD = new AlternativasPontoDesvio(this).getAlternativas(rota);
//		/*DEBUG*/TesteRotasImg.teste(altPD, "criarAlternativas, PD", this);
		rotas.addAll(altPD);
		
//		/*DEBUG*/TesteRotasImg.teste(rotas, "criarAlternativas, resultado", this);
		return rotas;
	}
	
	/**
	 * Caso uma rota tenha mais pontos intermediarios do que o limite permitido pela api 
	 * do GoogleMaps, ela eh dividida em partes menores 
	 */
	private Queue<Rota> dividirRotasGrandes(Queue<Rota> rotas){
		Queue<Rota> rotasDivididas = new PriorityQueue<Rota>();
		while(!rotas.isEmpty()){
			RotaPromissora r = (RotaPromissora)rotas.poll();
			int tamanho = r.size();
			int max = ConstantesGM.DIRECTIONS_MAX_WAYPOINTS;
			if(tamanho > max){
				int numPedacos = tamanho / max;
				if(tamanho%max > 0) numPedacos++;
				for(int i=0; i<numPedacos; i+=max-1){
					Rota pedaco = new Rota(r.getPontos().subList(i, i+max));
					rotasDivididas.add(new RotaPromissora(pedaco, r.getPeso()));
				}
			}else{
				rotasDivididas.add(r);
			}
		}
		return rotasDivididas;
	}
	
	public void limpar(){
		grafo = null;
		kernel = null;
		calcPerigo = null;
		rotasJaPassadas = new ArrayList<Rota>();
	}
	
	public GrafoRotas getGrafo() {
		return grafo;
	}
	public void setGrafo(GrafoRotas grafoRotas) {
		this.grafo = grafoRotas;
	}
	public KernelMap getKernel() {
		return kernel;
	}
	public CalculoPerigo getCalculoPerigo(){
		if(calcPerigo == null)
			calcPerigo = new CalculoPerigo(this);
		return calcPerigo;
	}
}