package org.wikicrimes.util.kernelMap;

import java.security.InvalidParameterException;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * @author victor
 * Grafo  dos possíveis caminhos entre a origem e o destino especificados pelo usuário no GoogleMaps.
 * O menor caminho para todo vértice é computado na inserção e remoção de rotas.
 *	
 */
public class GrafoRotas {

	private Map<Ponto,No> vertices;
	private Ponto origem, destino;
	
	private LogicaRotaSegura logicaRota;
	
	public GrafoRotas(Ponto origem, Ponto destino, LogicaRotaSegura logica){
		this.logicaRota = logica;
		
		this.origem = origem;
		this.destino = destino;
		vertices = new HashMap<Ponto,No>();
		No noO = new No(origem);
		noO.perigoOV = 0;
		noO.custoOV = 0;
		No noD = new No(destino);
		vertices.put(origem, noO);
		vertices.put(destino, noD);
		noO.custoOV = 0;
	}
	
	private No inserirVertice(Ponto v){
		No no = null;
		if(!vertices.containsKey(v)){
			no =  new No(v);
			vertices.put(v,no);
		}else{
			no = vertices.get(v); 
		}											 
		return no;
	}
		
	public int inserirRota(RotaGM trecho){
		
		if(trecho == null)
			throw new InvalidParameterException("nao pode inserir um TrechoRotaGM null num GrafoRotas");
		
		//insere
		No ini = inserirVertice(trecho.getInicio());
		No fim = inserirVertice(trecho.getFim());
		ini.trechosSaindo.put(fim, trecho);
		fim.trechosEntrando.put(ini, trecho);
		
		//atualiza menor caminho para FIM e tds seus sucessores (busca em largura), se for necessário
		Queue<RotaGM> q = new ArrayDeque<RotaGM>();
		q.add(trecho);
		while(!q.isEmpty()){
			trecho = q.poll();
			if(atualizaMenorCaminhoInsercao(trecho))
				//continua a busca em largura só se mudar alguma coisa
				for(RotaGM t : fim.trechosSaindo.values())
					q.add(t);
		}
		
		return fim.id;
	}
	
	private boolean atualizaMenorCaminhoInsercao(RotaGM trecho){
		No v = vertices.get(trecho.getFim());
		No pai = vertices.get(trecho.getInicio());
		
		//atualiza menor caminho do vertice V, se for o caso
		boolean mudou = false;
		double novoCustoOV = pai.custoOV + trecho.getCusto();
		double novoPerigoOV = Math.max(pai.perigoOV, trecho.getPerigo()); //perigo baseado em máximo
//		double novoPerigoOV = (pai.perigoOP*pai.custoOP + trecho.getPerigo()*trecho.getCusto()) / (pai.custoOP+trecho.getCusto()); //perigo baseado em média
		double novaDesfavOV = desfav(novoCustoOV, novoPerigoOV);
		double velhaDesfavOV = desfav(v.custoOV, v.perigoOV);
		if(novaDesfavOV < velhaDesfavOV){
			v.custoOV = novoCustoOV;
			v.perigoOV = novoPerigoOV;
			v.antecessor = pai;
			mudou = true;
		}
		return mudou;
	}
	
	
	/**
	 * Retona a sequência de vértices que define o menor caminho da ORIGEM até o DESTINO.
	 * Obs: A Rota retornada não é a concatenação das rotas do caminho, contém apenas os vértices. 
	 */
	public Rota menorCaminho(){
		Ponto p = destino; 
		if(p == null)
			return null;
		
		Rota r = new Rota();
		Ponto aux = p;
		r.add(p);
		while(aux != origem){
			No noAnte = vertices.get(aux).antecessor;
			if(noAnte == null)
				return null; //nao tem caminho de O até P
			Ponto pontoAnte = noAnte.ponto;
			r.addInicio(pontoAnte);
			aux = pontoAnte;
		}
		return r;
	}
	
	/**
	 * Retona a concatenação dos trechos de rota que formam o menor caminho da ORIGEM até o DESTINO.
	 */
	public Rota menorCaminhoDetalhado(){
		Rota r = new Rota();

		No noDest = vertices.get(destino);
		No noOrig = vertices.get(origem);
		No noAux = noDest;
		while(!noAux.equals(noOrig)){
			No noAnte = noAux.antecessor;
			if(noAnte == null)
				return null; //nao tem caminho de O até P
			RotaGM trecho = noAux.trechosEntrando.get(noAnte);
			r.addInicio(trecho);
			noAux = noAnte;
		}
		return r;
	}
	
	/**
	 * Mede quão promissor é este vértice, para fazer parte do melhor caminho
	 * Nomes alternativos: naoPromessa, unlikeliness, naoProbabilidade, naoPreferencia, quantoNaoPromissor
	 * Qanto maior este valor, menos preferência o vértice terá para participar da melhor rota
	 */
	public double desfav(Ponto p){
		return desfav(vertices.get(p));
	}
	
	private double desfav(No no){
		double desfavOP = GrafoRotas.desfav(no.custoOV, no.perigoOV);
		double desfavPD = GrafoRotas.desfav(no.custoVD, no.perigoVD);
		return desfavOP + desfavPD;
	}
	
//	public static double desfav(double distancia, double perigo){
//		if(perigo < 0 || perigo > 1 || Double.isNaN(perigo))
//			throw new AssertionError("O perigo deve estar entre 0 e 1. Perigo = " + perigo);
//		
//		/*teste*/perigo *= 0.9; //ir até 0.9,em vez de 1, pra nao dar muita desfav infinita
//		
//		//o perigo amplifica a distância
//		return distancia/(1-perigo);//se o perigo for 0, a desfavorabilidade é igual ao custo
//								    //quanto maior o perigo, mais a desfavorabilidade é amplificada
//								    //se o perigo for 1, a desfavorabilidade é infinita
//	}
	
	private static double desfav(double distancia, double perigo){
//		/*teste*/ perigo = Math.exp(perigo);
//		/*teste*/ perigo *= 200;
//		/*teste*/ perigo = Math.pow(perigo, 4);
		//TODO ajeitar o TESTE pra mostrar os detalhes do calculo do perigo ao pasar o mouse no vertice 
		//o perigo amplifica a distância
//		return distancia * (perigo+1); //se o perigo for 0, a desfavorabilidade é igual ao custo
	    							   //quanto maior o perigo, mais a desfavorabilidade é amplificada
//		if(distancia==Double.POSITIVE_INFINITY)
//			return distancia;
//		return distancia+perigo;
		return perigo;
	}
	
	
	
	private int ultimaId = 0;
	public class No implements Comparable<No>{
		
		private int id;
		
		private Ponto ponto;
		private Map<No,RotaGM> trechosSaindo;
		private Map<No,RotaGM> trechosEntrando;
		private No antecessor;

		private double custoOV; //custo para chegar em P a partir da ORIGEM, pelo menor caminho possível
		private double perigoOV; //perigo pelo qual passa o menor caminho da ORIGEM até P
		private final double custoVD; //distância reta entre P e o DESTINO (não muda)
		private final double perigoVD; //perigo pelo qual passa a reta entre P e DESTINO (não muda)
		
//		private static final double FATOR_ESTIMATIVA = 2; //razão provável entre a distância reta entre 2 pontos 
														  //e a distância percorrida pela rota gerada pelo GoogleMaps 
														  //entre os mesmos 2 pontos 

		public No(Ponto p) {
			id = ++ultimaId;
			ponto = p;
			trechosSaindo = new HashMap<No,RotaGM>();
			trechosEntrando = new HashMap<No,RotaGM>();
			custoOV = Double.POSITIVE_INFINITY;
			perigoOV = Double.POSITIVE_INFINITY;
			
			//calculo do desfavPD
			SegmentoReta segmPD = new SegmentoReta(ponto,destino); 
			custoVD = segmPD.getComprimento();
			perigoVD = logicaRota.perigo(new Rota(segmPD));
		}
		
		private double desfav(){
			return GrafoRotas.this.desfav(ponto);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof No){
				No outro = (No)obj;
				return this.ponto.equals(outro.ponto);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.ponto.hashCode();
		}
		
		@Override
		public String toString() {
			return String.valueOf(id);
		}

		@Override
		public int compareTo(No outroNo) {
			//ordena por menor estimativa de chegar no destino
			double esse = this.desfav();
			/*teste*/if(esse != GrafoRotas.this.desfav(this)) throw new AssertionError("desfavs retornand valor diferente");
			double outro = GrafoRotas.this.desfav(outroNo);
			/*teste*/if(outro != outroNo.desfav()) throw new AssertionError("desfavs retornand valor diferente");
			
			if(esse < outro)
				return -1;
			else if(esse > outro)
				return +1;
			else
				return 0;
		}
	}


	public Ponto getOrigem() {
		return origem;
	}
	
	public Ponto getDestino() {
		return destino;
	}
	
	public List<Rota> getRotas(){
		List<Rota> rotas = new ArrayList<Rota>();
		for(No no : vertices.values())
			for(Rota rota : no.trechosSaindo.values()){
				rotas.add(rota);
			}
		return rotas;
	}
	
	public List<RotaGM> getRotasGM(){
		List<RotaGM> rotas = new ArrayList<RotaGM>();
		for(No no : vertices.values())
			for(RotaGM rota : no.trechosSaindo.values()){
				rotas.add(rota);
			}
		return rotas;
	}
	
	public Map<Integer,Ponto> getPontos(){
		Map<Integer,Ponto> pontos = new HashMap<Integer,Ponto>();
		for(No no : vertices.values()){
			pontos.put(no.id,no.ponto);
		}
		return pontos;
	}
	
	public int getId(Ponto p){
		No no = vertices.get(p);
		if(no != null)
			return no.id;
		else
			return -1;
	}
	
	public RotaGM getTrecho(Rota rota){
		No ini = vertices.get(rota.getInicio());
		No fim = vertices.get(rota.getFim());
		if(rota != null && ini !=  null && fim != null){
			return ini.trechosSaindo.get(fim);
		}
		return null;
	}
	
	/**
	 * @return SegmentoReta que vai da ORIGEM ate o DESTINO
	 */
	public SegmentoReta getSegmentoRetaOD(){
		return new SegmentoReta(getOrigem(), getDestino());
	}
	
	/**
	 * @return distância (reta) entre a origem e o destino
	 */
	public double getDistanciaRetaOD(){
		return getSegmentoRetaOD().getComprimento();
	}
	

	@Override
	public Object clone(){
		GrafoRotas g = new GrafoRotas(origem, destino, logicaRota);
		
		List<No> nos = new ArrayList<No>(); 
		nos.addAll(vertices.values());
		Collections.sort(nos, ordemId);
		
		for(No v : nos){
			g.inserirVertice(v.ponto); //insere vertices na ordem pra manter a ordem dos IDs
		}
		for(No v : nos){
			for(RotaGM r : v.trechosSaindo.values()){
				g.inserirRota((RotaGM)r.clone());
			}
		}
		return g;
	}
	
	public String getDetalhes(Ponto p){
		No no = vertices.get(p);
		Ponto pai = no.antecessor.ponto;
		if(no != null){
			StringBuilder s = new StringBuilder();
			s.append("id = " + getId(p) + "\n");
			s.append("x = " + p.x + ", y = " + p.y + "\n");
			s.append("idPai = " + pai.x + ", y = " + pai.y + "\n");
			s.append("distOV = " + no.custoOV + "\n");
			s.append("distVD = " + no.custoVD + "\n");
			s.append("perigoOV = " + no.perigoOV + "\n");
			s.append("perigoVD = " + no.perigoVD + "\n");
//			s.append("desfavOV = " + desfav(no.custoOP, no.perigoOP) + "\n");
//			s.append("desfavVD = " + desfav(no.custoPD, no.perigoPD) + "\n");
			s.append("desfavTotal = " + desfav(no) + "\n");
			return s.toString();
		}else{
			return "null";
		}
	}
	
	private static Comparator<No> ordemId = new Comparator<No>(){
		@Override
		public int compare(No o1, No o2) {
			return o1.id - o2.id;
		}
	};
	

	// -------------- Fila dos Pontos mais promissores, candidados a expansão ---------------//
	

	public Queue<Ponto> getFilaPontosParaExpandir(){
		return mascara;
	}
	
	
	private final Queue<Ponto> mascara = new FilaPontosPromissores();
	private final Queue<No> pontosPromissores = new PriorityQueue<No>(); 
	private class FilaPontosPromissores extends AbstractQueue<Ponto>{
		
		@Override
		public int size() {
			return pontosPromissores.size();
		}

		@Override
		public boolean offer(Ponto p) {
			No no = vertices.get(p);
			if(no == null)
				throw new InvalidParameterException("um ponto que não é vértice de um GrafoRotas não pode ser adicionado a sua FilaPontosPromissores");
			return pontosPromissores.offer(no);
		}

		@Override
		public Ponto poll() {
			No no = pontosPromissores.poll();
			if(no != null)
				return no.ponto;
			else
				return null;
		}
		
		@Override
		public Ponto peek() {
			No no = pontosPromissores.peek();
			if(no != null)
				return no.ponto;
			else
				return null;
		}
		
		@Override
		public Iterator<Ponto> iterator() {
			throw new UnsupportedOperationException();
		}
	}
	
}