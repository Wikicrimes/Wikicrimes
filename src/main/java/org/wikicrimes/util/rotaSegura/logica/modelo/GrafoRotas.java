package org.wikicrimes.util.rotaSegura.logica.modelo;

import java.awt.Color;
import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.CalculoPerigo;
import org.wikicrimes.util.rotaSegura.logica.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.logica.exceptions.CantFindPath;
import org.wikicrimes.util.rotaSegura.logica.exceptions.VertexNotInGraph;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;



/**
 * @author victor
 * Grafo  dos possiveis caminhos entre a origem e o destino especificados pelo usuário no GoogleMaps.
 * O menor caminho para todo vertice eh computado na inserção e remocao de rotas.
 *	
 */
public class GrafoRotas {

	private Map<Ponto,VerticeRotas> vertices;
	private Ponto origem, destino;
	private Rota rotaOriginal;
	
	private int numVertices;
	
	private CalculoPerigo calcPerigo;
	
	public GrafoRotas(Rota rotaOriginal, CalculoPerigo calcPerigo){
		this.calcPerigo = calcPerigo;
		this.rotaOriginal = rotaOriginal;
		this.origem = rotaOriginal.getInicio();
		this.destino = rotaOriginal.getFim();
		vertices = new HashMap<Ponto,VerticeRotas>();
		VerticeRotas noO = new VerticeRotas(origem, ++numVertices);
		VerticeRotas noD = new VerticeRotas(destino, ++numVertices);
		vertices.put(origem, noO);
		vertices.put(destino, noD);
	}
	
	private VerticeRotas inserirVertice(Ponto v){
		if(v == null) throw new InvalidParameterException("ponto === null");
		VerticeRotas no = vertices.get(v);
		if(no == null){
			no =  new VerticeRotas(v, ++numVertices);
			vertices.put(v,no);
		}											 
		return no;
	}
	
	public void inserirRota(Rota rota, double custo, double perigo){
		if(rota == null)
			throw new InvalidParameterException();
		inserirAresta(rota, custo, perigo);
	}
	
	public void inserirRota(Rota rota, double custo){
		if(rota == null)
			throw new InvalidParameterException();
		
		//aproveitar vertices existentes
		//TODO
		
		//dividir em partes pequenas
		List<Rota> rotas = new ArrayList<Rota>();
		/*TESTE*/rotas.add(rota);
//		List<Rota> partes = rota.divide(LogicaRotaSegura.GRANULARIDADE_ROTAS);
//		rotas.addAll(partes);
		
		//unir partes coincidentes
		//TODO

		//adicionar cada parte
		for(Rota parte : rotas) {
			if(parte.size() == 0) continue;
			double custoProporcional = custo*parte.distanciaPercorrida()/rota.distanciaPercorrida();
			double perigoParte = calcPerigo.perigo(parte);
			inserirAresta(parte, custoProporcional, perigoParte);
		}
	}
		
	/*public void inserirCaminho(Caminho caminho, double custo, double perigo){
		//TODO problema: esse custo e perigo teria q dividir nos caminhos menores
		
		if(caminho == null)
			throw new InvalidParameterException();
		
		//TRATAR INTERSECOES
		
		//caso 1, caminhos cruzam: criar um vertice
		//TODO (precisa msm? normalmente o caminho do GM n tende a seguir um anterior, em vez de cruzar?)
		
		//caso 2, caminhos tem segmentos de reta em comum: unir a parte em comum em um só caminho e criar vertices nas bifurcacoes
		List<Caminho> partesIteracaoAnterior = new LinkedList<Caminho>();
		partesIteracaoAnterior.add(caminho);
		for(Caminho caminhoNoGrafo : getCaminhos()){
			List<Caminho> partesIteracaoAtual = new LinkedList<Caminho>();
			for(Caminho parte : partesIteracaoAnterior){
				partesIteracaoAtual.addAll(parte.subtracao(caminhoNoGrafo));
			}
			partesIteracaoAnterior = partesIteracaoAtual;
		}
		
		
		//INSERIR
		
		for(Caminho novoCaminho : partesIteracaoAnterior){
			inserirAresta(novoCaminho, custo, perigo);
			Ponto ini = novoCaminho.getInicio();
			Ponto fim = novoCaminho.getFim();

//			//TODO caso inicio e/ou fim estejam contidos em algum caminho q ja faz parte do grafo
//			for(Aresta aresta: getArestas()){
//				Caminho caminhoAresta = aresta.caminho; 
//				if(caminhoAresta.passaPor(ini)){
//					removerAresta(aresta);
//					Caminho subCaminho1 = caminhoAresta.subCaminho(caminhoAresta.getInicio(), ini);
//					inserirAresta(subCaminho1, custo, perigo);
//					Caminho subCaminho2 = caminhoAresta.sinserirArestaubCaminho(ini, caminhoAresta.getFim());
//					inserirAresta(subCaminho2, custo, perigo);
//				}
//				if(caminhoAresta.passaPor(fim)){
//					removerAresta(aresta);
//					Caminho subCaminho1 = caminhoAresta.subCaminho(caminhoAresta.getInicio(), fim);
//					inserirAresta(subCaminho1, custo, perigo);
//					Caminho subCaminho2 = caminhoAresta.subCaminho(fim, caminhoAresta.getFim());
//					inserirAresta(subCaminho2, custo, perigo);
//				}
//			}
		}
	}*/
	
	private void inserirAresta(Rota caminho, double custo, double perigo){
		VerticeRotas ini = inserirVertice(caminho.getInicio());
		VerticeRotas fim = inserirVertice(caminho.getFim());
		ArestaRotas aresta = new ArestaRotas(ini, fim, caminho, custo, perigo);
		ini.arestasSaindo.add(aresta);
		fim.arestasEntrando.add(aresta);
	}
	
//	private void removerAresta(Aresta aresta){
//		aresta.antecessor.arestasSaindo.remove(aresta);
//		aresta.sucessor.arestasEntrando.remove(aresta);
//	}
	
	public Ponto getOrigem() {
		return origem;
	}
	
	public Ponto getDestino() {
		return destino;
	}
	
	public Rota getRotaOriginal() {
		return rotaOriginal;
	}
	
	
	/**
	 * @return SegmentoReta que vai da ORIGEM ate o DESTINO
	 */
	public Segmento retaOD(){
		return new Segmento(getOrigem(), getDestino());
	}
	
	/**
	 * @return distância (reta) entre a origem e o destino
	 */
	public double distOD(){
		return retaOD().comprimento();
	}
	

	@Override
	public GrafoRotas clone(){
		GrafoRotas g = new GrafoRotas(rotaOriginal, calcPerigo);
		
		List<VerticeRotas> nos = new ArrayList<VerticeRotas>(); 
		nos.addAll(vertices.values());
		Collections.sort(nos, ordemId);
		
		for(VerticeRotas v : nos){
			g.inserirVertice(v.ponto); //insere vertices na ordem pra manter a ordem dos IDs
		}
		for(VerticeRotas v : nos){
			for(ArestaRotas r : v.arestasSaindo){
				g.inserirRota(r.rota.clone(), r.custoGM, r.perigo);
			}
		}
		return g;
	}
	
	private static Comparator<VerticeRotas> ordemId = new Comparator<VerticeRotas>(){
		@Override
		public int compare(VerticeRotas o1, VerticeRotas o2) {
			return o1.id - o2.id;
		}
	};
	
	public Map<Integer,Ponto> getPontos(){
		Map<Integer,Ponto> pontos = new HashMap<Integer,Ponto>();
		for(VerticeRotas v : vertices.values()){
			pontos.put(v.id,v.ponto);
		}
		return pontos;
	}
	
	public List<Rota> getRotas(){
		List<Rota> rotas = new ArrayList<Rota>();
		for(VerticeRotas v : vertices.values())
			for(ArestaRotas a : v.arestasSaindo){
				rotas.add(a.rota);
			}
		return rotas;
	}
	
	public Rectangle getBounds(){
		int minX = Math.min(origem.x, destino.x);
		int minY = Math.min(origem.y, destino.y);
		int maxX = Math.max(origem.x, destino.x);
		int maxY = Math.max(origem.y, destino.y);
		return new Rectangle(minX, minY, maxX-minX, maxY-minY);
	}
	
	public VerticeRotas getVerticeRotas(Ponto p){
		VerticeRotas v = this.vertices.get(p);
		if(v == null)
			throw new InvalidParameterException("o ponto " + p + " nao eh vertice do grafo");
		return v;
	}
	
	public String getDetalhes(Ponto p){
		VerticeRotas v = vertices.get(p);
		if(v != null){
			StringBuilder s = new StringBuilder();
			s.append("id = " + v.id + "\n");
			s.append("x = " + p.x + ", y = " + p.y + "\n");
			return s.toString();
		}else{
			return "null";
		}
	}
	
	
	//CALCULO DE MENOR CAMINHO (Dijkstra)
	public Rota verticesMelhorCaminho(Ponto origem, Ponto destino) throws CantFindPath{
//		/*TESTE*/String teste = "verticesMelhorCaminho(" + id(origem) + ", " + id(destino) + "): " ;

		VerticeRotas vOrigem = vertices.get(origem);
		VerticeRotas vDestino = vertices.get(destino); 
		if(vOrigem == null) 
			throw new VertexNotInGraph(origem);
		if(vDestino == null)
			throw new VertexNotInGraph(destino);
		
		Map<Ponto, VerticeMC> mapaVerticesMC = new HashMap<Ponto, VerticeMC>();
		
		//inicializar fila de prioridade Q
		PriorityQueue<VerticeMC> q = new PriorityQueue<VerticeMC>();
		for(VerticeRotas v: vertices.values()){
			double peso = (v == vertices.get(origem)) ? 0 : Double.POSITIVE_INFINITY;
			VerticeMC w = new VerticeMC(v, peso); 
			q.add(w);
			mapaVerticesMC.put(v.ponto, w);
		}
		
		//loop em Q
		while(!q.isEmpty()){
			VerticeMC v = q.poll();
			for(ArestaRotas a : v.arestasSaindo){
				VerticeMC w = mapaVerticesMC.get(a.sucessor.ponto);
				
				//relaxamento
				if(w.custo > v.custo + a.perigo){
					w.custo = v.custo + a.perigo;
					w.pai = v;
					q.remove(w);//remove e adiciona pra atualizar a posição de W na fila Q
					q.add(w);
				}
			}
		}

		//recuperar menor caminho
		Ponto p = destino;
		Rota c = new Rota(destino);
//		/*TESTE*/Vertice w = null;
//		/*TESTE*/try{
		while(!p.equals(origem)){
			VerticeMC vmc = mapaVerticesMC.get(p);
//			/*TESTE*/teste +=  id(p) + ", ";
//			/*TESTE*/w = vmc;
			VerticeMC pai = vmc.pai;
			if(pai == null){
//				/*DEBUG*/TesteGrafoJung teste = new TesteGrafoJung(this);
//				/*DEBUG*/teste.setTitulo("menor caminho de A até B");
//				/*DEBUG*/teste.addLabel(origem, "A");
//				/*DEBUG*/teste.addLabel(destino, "B");
				throw new CantFindPath(this,vOrigem,vDestino,vmc);
			}
			p = pai.ponto;
			c.addInicio(p);
		}
//		/*TESTE*/}catch(NullPointerException e){
//		/*TESTE*/	System.out.println("grafo:\n" + this);
//		/*TESTE*/	System.out.println(teste);
//		/*TESTE*/	System.out.println("vertice sem pai: " + w);
//		/*TESTE*/	throw e;
//		/*TESTE*/}
		
//		/*TESTE*/System.out.println( id(p));
		return c;
	}
	
	public Rota verticesMelhorCaminho() throws CantFindPath{
		return verticesMelhorCaminho(origem, destino);
	}
	
	public Rota getRota(Ponto... vertices){
		Rota r = new Rota();
		double perigo = 0;
		double custo = 0;
		VerticeRotas v1 = getVerticeRotas(vertices[0]);
		for(int i=1; i<vertices.length; i++){
			VerticeRotas v2 = getVerticeRotas(vertices[i]);
			boolean achou = false;
			for(ArestaRotas a : v1.arestasSaindo){
				if(a.sucessor == v2){
					r.addFim(a.rota);
					perigo += a.perigo;
					custo += a.custoGM;
					achou = true;
					break;
				}
			}
			if(!achou)
				throw new AssertionError();
			v1 = v2;
		}
		return new RotaGM(r, custo, perigo);
	}
	
	public Rota melhorCaminho() throws CantFindPath{
		return getRota(verticesMelhorCaminho().getPontosArray());
	}
	
	public Rota melhorCaminho(Ponto origem, Ponto destino) throws CantFindPath{
		return getRota(verticesMelhorCaminho(origem, destino).getPontosArray());
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(VerticeRotas v : vertices.values()){
			s.append(v.id + ": ");
			for(ArestaRotas a : v.arestasSaindo){
				s.append(a.sucessor.id + ", ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	private class VerticeMC extends VerticeRotas implements Comparable<VerticeMC>{
		
		double custo;
		VerticeMC pai;

		VerticeMC(VerticeRotas v, double custo){
			super(v.ponto,v.id);
			arestasSaindo = v.arestasSaindo;
			arestasEntrando = v.arestasEntrando;
			this.custo = custo;
		}
		
		public int compareTo(VerticeMC o) {
			if(custo == o.custo)
				return 0;
			else if(custo > o.custo)
				return 1;
			else
				return -1;
		}
	}

	//CALCULO DE K MENORES CAMINHOS
	public List<Rota> verticesKMenoresCaminhos(int k){
		List<Rota> rotas = verticesKMenoresCaminhos(origem, destino, k);
		return rotas;
	}
	public List<Rota> verticesKMenoresCaminhos(Ponto origem, Ponto destino, int k){
		double maxWeight = calcPerigo.perigo(rotaOriginal);
		initializeBetterWeights(k, maxWeight);
		List<RotaPromissora> todas = todosCaminhos(origem, destino);
		Collections.sort(todas);
		todas = removeRepetidas(todas);
		List<Rota> kMenores = new ArrayList<Rota>();
		if(k > todas.size()) 
			k = todas.size();
		for(int i=0; i<k; i++)
			kMenores.add(todas.get(i));
		this.betterWeights = null;
		return kMenores;
	}
	private List<RotaPromissora> removeRepetidas(List<RotaPromissora> listaOrdenadaPorPeso){
		List<RotaPromissora> unicos = new ArrayList<RotaPromissora>();
		double pesoI = 0;
		for(int i=0; i<listaOrdenadaPorPeso.size(); i++){
			RotaPromissora rotaI = listaOrdenadaPorPeso.get(i);
			if(rotaI.getPeso() != pesoI){
				pesoI = rotaI.getPeso();
				unicos.add(rotaI);
			}
			for(int j=i+1; j<listaOrdenadaPorPeso.size(); j++, i=j){
				RotaPromissora rotaJ = listaOrdenadaPorPeso.get(j);
				double pesoJ = rotaJ.getPeso();
				if(pesoJ != pesoI)
					break;
				else
					if(!rotaJ.equals(rotaI)){
						unicos.add(rotaJ);
					}
			}
		}
		return unicos;
	}
	private List<RotaPromissora> todosCaminhos(Ponto origem, Ponto destino){
		
		VerticeRotas vOrigem = vertices.get(origem);
		VerticeRotas vDestino = vertices.get(destino); 
		if(vOrigem == null) 
			throw new VertexNotInGraph(origem);
		if(vDestino == null)
			throw new VertexNotInGraph(destino);
		
		origemKMC = vOrigem;
		destinoKMC = vDestino;
		RotaPromissora raiz = new RotaPromissora(new Rota(origem), 0);
		List<RotaPromissora> caminhos = todosCaminhosSubrotina(origemKMC, destinoKMC, raiz);
		origemKMC = destinoKMC = null;
		return caminhos;
	}
	private VerticeRotas origemKMC, destinoKMC;
	private List<RotaPromissora> todosCaminhosSubrotina(VerticeRotas origem, VerticeRotas destino, RotaPromissora caminhoAteOrigem){

		/*DEBUG*/TesteRotasImg teste = new TesteRotasImg(this);
		/*DEBUG*/teste.addRota(caminhoAteOrigem, Color.BLUE);
		/*DEBUG*/teste.salvar();
		
		List<RotaPromissora> caminhos = new ArrayList<RotaPromissora>();

		//condição de parada: qd chega no destino
		if(origem.equals(destino)){
			updateBetterWeights(caminhoAteOrigem.getPeso());
			caminhos.add(caminhoAteOrigem);
			return caminhos;	
		}
			
		//ramificação
		for(ArestaRotas a : origem.arestasSaindo){
			VerticeRotas v = a.sucessor;
			if(caminhoAteOrigem.contem(v.ponto)) continue; //evitar loops
			Rota caminhoAteV = new Rota(caminhoAteOrigem);
			caminhoAteV.addFim(v.ponto);
			double pesoAteV = caminhoAteOrigem.getPeso() + a.perigo;
			RotaPromissora caminhoAteVEPeso = new RotaPromissora(caminhoAteV, pesoAteV);
			double pesoRetaVD = calcPerigo.perigo(new Segmento(v.ponto, destino.ponto));
			if(pesoAteV + pesoRetaVD > getMaxWeight()) return caminhos; //descartar caminhos muito pesados
			caminhos.addAll(todosCaminhosSubrotina(v, destino, caminhoAteVEPeso));
		}
		return caminhos;
	}
	
	private double[] betterWeights;
	private void initializeBetterWeights(int size, double initialWaight) {
		betterWeights = new double[size];
		for(int i=0; i<size; i++) {
			betterWeights[i] = initialWaight;
		}
	}
	private double getMaxWeight() {
		return betterWeights[betterWeights.length-1];
	}
	private void updateBetterWeights(double newWeight) {
		//mantem o array ordenado, insere newWeight na posicao certa e descarta o ultimo valor
		for(int i=0; i<betterWeights.length; i++) {
			if(newWeight < betterWeights[i]) {
				double temp = betterWeights[i];
				betterWeights[i] = newWeight;
				newWeight = temp;
			}
		}
	}
	
	public List<Rota> intersecao(Rota rota){
		List<Rota> intersecoes = new ArrayList<Rota>();
		for(Rota rotaDoGrafo : getRotas()) {
			List<Rota> partes = rota.intersecao(rotaDoGrafo);
			intersecoes.addAll(partes);
		}
		return intersecoes;
	}
	
	
}