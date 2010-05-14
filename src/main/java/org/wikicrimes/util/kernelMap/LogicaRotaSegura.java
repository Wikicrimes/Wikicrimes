package org.wikicrimes.util.kernelMap;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wikicrimes.util.kernelMap.testes.TesteCenariosRotas;

/**
 * @author victor
 *	Agrupa a lógica para cálculo de rotas seguras. Um objeto LogicaRotaSegura calcula rotas para o MapaKernel associado a ele.
 */
public class LogicaRotaSegura {
	
	private KernelMap kernel;
	private GrafoRotas grafoRotas;
	private Set<SegmentoReta> trechosJaGerados = new HashSet<SegmentoReta>(); //controle pra nao pedir duas vezes o mesmo trecho ao GoogleMaps
	
	//parâmetros das expansões
	private static /*final*/ int RAZAO_TAMANHO_EXPANSOES = PropertiesLoader.getInt("razao_tam_expansoes"); //quanto maior, MENORES serão as expansões feitas
	private static /*final*/ int QUANTIDADE_EXPANSOES = PropertiesLoader.getInt("qtd_expansoes");
	
	//parâmetros do refinamento
	private final static double TOLERANCIA_ARRODEIO = PropertiesLoader.getInt("tol_arrodeio"); //quanto menor, mais arodeios serão corrigidos pelo algorítmo
	private final static int MAX_APROXIMACAO = PropertiesLoader.getInt("tol_aproximacao"); //distancia máxima entre pontos na obtenção de pontos e rotas aproximadas
	
	
	public LogicaRotaSegura(KernelMap kernel){
		this.kernel = kernel;
	}
	
	/* ---------------- REFINAMENTO ------------------- */
	// ajustes na estratégia de encontrar pontos de desvio ao redor do hotspot
	
	/**
	 * Detecta e elimina "arrodeios".
	 * Este problema ocorre qd o googlemaps faz uma arrodeio desnecessário para alcancar um ponto da "rota segura" calculada pelo server 
	 * que ficou fora do caminho preferido pelo GoogleMaps.
	 * A solução é mover este ponto para um local próximo ao início ou fim do arrodeio, que são bem próximos um do outro.
	 * Obs: se for detectada um "arrodeio", a rotaServidor será modificada por este método
	 */
	public static boolean refinaRota(Caminho rotaServidor, Caminho rotaGoogleMaps){
		//obs: 
		//a rotaServidor eh a que eh resultado direto da logica de rotas seguras (foi gerada pelo servidor)
		//a rotaGoogleMaps eh a gerada pelo googleMaps, ao receber os pontos da rotaServidor como parametro (foi obtida do cliente)
		//as duas sao semelhantes, mas a rotaGoogleMaps tem mais pontos do que a rotaServidor (ou a msm qtdade, mas nunca tem menos)
		//a rotaGoogleMaps serah usada para detectar o "arrodeio"
		//o ponto de correcao serah adicionado a rotaServidor (que serah enviada novamente ao google maps, desta vez evitando arrodeio)
		
//		/*teste*/new PainelTesteRotas(rotaServidor, rotaGoogleMaps,"tiraArrodeio(), azul:rotaServidor , vermelho:rotaGoogleMaps");
		/*teste*/System.out.println("ARRODEIO: ");
		
		//DETECTA
		boolean temArrodeio = false;
		List<Ponto> pontos = rotaGoogleMaps.getPontos();
		for(int i=0; i<pontos.size(); i++){
			Ponto p1 = pontos.get(i); //p1 é cada uma das coordenadas de toda a rotaGoogleMaps
			for(int j=i+1; j<pontos.size(); j++){
				Ponto p2 = pontos.get(j); //p2 é cada uma das coordenadas subsequentes à p1
				double distReta = new SegmentoReta(p1,p2).comprimento(); //distancia entre p1 e p2 em linha reta
				
				//trecho de p1 até p2
				Caminho trecho = new Caminho();
				for(int k=i; k<=j; k++){
					trecho.append(pontos.get(k));
				}
				double distCurva = trecho.distanciaPercorrida(); //distancia entre p1 e p2 seguindo a curva da rota
				
				double distLimite = distReta * TOLERANCIA_ARRODEIO; //distancia limite para o contorno não ser considerado "arrodeio"
																	//este limite é arbitrário
				if(distCurva > distLimite){
					Ponto ida = p1; //p1 é começo de um "arrodeio", agora tem q ver qual é o fim (p2 com menor distância reta até p1)
					
					int indiceVolta = encontraFimArrodeio(rotaGoogleMaps, i, j); //identifica o fim do "arrodeio"
					Ponto volta = pontos.get(indiceVolta);
					/*teste*/System.out.println("ida="+i+", volta="+indiceVolta);
					
					//CORRIGE
					corrigeArrodeio(ida, volta, rotaServidor, rotaGoogleMaps); //corrige o problema
					temArrodeio = true;
					
					//continua do ponto depois da volta
					i = indiceVolta; //continua a partir do ponto seguinte ao fim do arrodeio (logo em seguida vai rodar o i++ do for)
					j = i+1;
					break;
				}
			}
		}
		
		//teste
		if(!temArrodeio)
			/*teste*/System.out.println("nao tem arrodeio");
		
		return temArrodeio;
	}
	
	private static int encontraFimArrodeio(Caminho rotaGoogleMaps, int inicio, int fimPorEnquanto){
		List<Ponto> pontos = rotaGoogleMaps.getPontos();
		double menorDistancia = new SegmentoReta(pontos.get(inicio), pontos.get(fimPorEnquanto)).comprimento();
		int fim = fimPorEnquanto;
		for(int i=fimPorEnquanto+1; i< pontos.size(); i++){
			double distancia = new SegmentoReta(pontos.get(inicio), pontos.get(i)).comprimento();
			menorDistancia = Math.min(menorDistancia,distancia);
			if(menorDistancia == distancia)
				fim = i;
		}
		return fim;
	}
	
	private static void corrigeArrodeio(Ponto ida, Ponto volta, Caminho rotaServidor, Caminho rotaGoogleMaps){
		
		Caminho rotaGoogleMapsAprox = rotaGoogleMaps.getRotaAproximada(rotaServidor, MAX_APROXIMACAO); //pontos da rotaGoogleMaps que mais se aproximam dos pontos da rotaServidor
		Ponto aux = ida;
		while(!rotaGoogleMapsAprox.contem(aux) && aux != null){
			aux = rotaGoogleMaps.pontoAnterior(aux); //caminhando pra tras na rotaGoogleMaps até encontrar um ponto que exista na rotaGoogleMapsAprox
		}
		
		Ponto ptAnteriorGoogleMapsAproxServidor = aux; //pt na rotaGoogleMaps q está próximo a um ponto na rotaServidor e vem antes da ida
		Ponto ptAnteriorServidor = rotaServidor.getPontoPerto(ptAnteriorGoogleMapsAproxServidor, MAX_APROXIMACAO);
		
		if(ptAnteriorGoogleMapsAproxServidor == null || ptAnteriorServidor == null){
			throw new RuntimeException("limite de aproximação excedido");
		}
		
		Ponto pontoCausadorDoArrodeio = rotaServidor.pontoPosterior(ptAnteriorServidor); //o ponto que deve estar na ponta da ida e volta
		rotaServidor.remove(pontoCausadorDoArrodeio);
		Ponto meio = Ponto.medio(ida, volta); //ponto que substitui o pontoCausadorDaIdaVolta
		rotaServidor.addDepois(ptAnteriorServidor, meio);
	}
	
	/*
	public static void refinaRota2(Rota vertices, Rota rota){
		double anguloOD = new SegmentoReta(rota.getInicio(), rota.getFim()).getAngulo();
		
		List<SegmentoReta> segms = rota.getSegmentosReta();
		for(SegmentoReta s : segms){
			double a = s.getAngulo();
			System.out.println(a);
		}
		
	}
	*/
	
	
	/* ---------------- EXPANSÕES ------------------- */
	// abrir árvore de possíveis caminhos da origem até o destino

	/**
	 * Expande novos trechos a partir do ponto P.
	 * O retorno do método é em SegmentosReta para serem mandados pro GoogleMaps
	 */
	public List<SegmentoReta> expandir(Ponto p){
		double raio = getGrafoRotas().getDistanciaRetaOD() / RAZAO_TAMANHO_EXPANSOES; 
		int qt = QUANTIDADE_EXPANSOES;
		return expandir(p, raio, qt);
	}
	
	/**
	 * Expande novos trechos a partir do ponto P.
	 * O retorno do método é em SegmentosReta para serem mandados pro GoogleMaps
	 * @param <br>raio</br> : tamanho de cada trecho expandido
	 * @param <br>qt</br> : quantidade de trechos expandidos
	 */
	public List<SegmentoReta> expandir(Ponto p, double raio, int qt){
		Ponto d = grafoRotas.getDestino();
		List<SegmentoReta> trechos = new ArrayList<SegmentoReta>();
		if(p.equals(d)) 
			return trechos;
		SegmentoReta segmPD = new SegmentoReta(p,d);
		double anguloPD = segmPD.angulo(); //angulo da reta que passa por P e D
		double anguloExpansoes = Math.PI*2 / qt; //angulo entre as expansoes
		
		//expandir
		for(int i=0; i<qt; i++){
			double a = anguloPD + anguloExpansoes * (i+1);
			int x = p.x + (int)(raio * Math.cos(a));
			int y = p.y + (int)(raio * Math.sin(a));
			Ponto pExpandido = new Ponto(x,y); //expansão gerada com o ângulo A e comprimento RAIO
			
			SegmentoReta trecho = new SegmentoReta(p,pExpandido);
			if(!trechosJaGerados.contains(trecho) 
					/*teste*/ //aceita só as expansões que apontam pro destino
					&& new SegmentoReta(trecho.getFim(), d).comprimento() <= segmPD.comprimento()/*teste*/){
				
				trechos.add(trecho);
				trechosJaGerados.add(trecho);
			}
		}
		
//		/*teste*/System.out.println("EXPANSAO, VERTICE: " + grafoRotas.getId(p) + ", TRECHOS: " + trechos);
		return trechos;
	}
	
	public SegmentoReta expandirProDestino(Ponto p){
		Ponto d = grafoRotas.getDestino();
		SegmentoReta trecho = new SegmentoReta(p,d);
		if(!trechosJaGerados.contains(trecho)){
			trechosJaGerados.add(trecho);
			return trecho;
		}else{
			return null;
		}
	}
	
	public double perigo(Caminho rota){
		
		if(rota.distanciaPercorrida() == 0)
			return 0;
		
		double valor = 0;
		for(SegmentoReta segm : rota.getSegmentosReta()){
//			valor = Math.max(valor, perigoMaximo(segm)); //máximo
			valor += perigoDistancia(segm); //perigo * distância 
		}
//		valor /= rota.getDistanciaPercorrida();
//		valor /= kernel.getMaiorDensidade(); //dividido por maxDens pra ficar 'relativo', pra variar entre 0 e 1
		return valor;
	}
	
	private double perigoMaximo(SegmentoReta segm){

		Rectangle bounds = kernel.getBounds();
		double[][] densidade = kernel.getDensidadeGrid();
		
		int xIni = (int)((segm.getInicio().x - bounds.x) / kernel.nodeSize); //coluna na matriz de densidades onde se encontra o ponto inicial do segmento
		int yIni = (int)((segm.getInicio().y - bounds.y) / kernel.nodeSize); //linha do ponto inicial
		int xFim = (int)((segm.getFim().x - bounds.x) / kernel.nodeSize); //coluna do ponto final
		int yFim = (int)((segm.getFim().y - bounds.y) / kernel.nodeSize); //linhado ponto final
		if(xIni>xFim){ int aux=xIni; xIni=xFim; xFim=aux;}
		if(yIni>yFim){ int aux=yIni; yIni=yFim; yFim=aux;}
		
		int cols = Math.abs(xFim-xIni);
		int rows = Math.abs(yFim-yIni);
		
//		double somaPerigo = 0;
		double maxPerigo = 0;
		if(cols == 0 && rows == 0){
			return 0;
		}else if(cols == 0){
			for(int i=0; i<rows; i++){
				double dens = densidade[xIni][yIni+i]; 
//				somaPerigo += dens; 
				maxPerigo = Math.max(maxPerigo, dens);
			}
		}else if(rows == 0){
			for(int i=0; i<cols; i++){
				double dens = densidade[xIni+i][yIni]; 
//				somaPerigo += dens; 
				maxPerigo = Math.max(maxPerigo, dens);
			}
		}else if(cols > rows){
			double proporcao = cols/rows;
			for(int i=0; i<cols; i++){
				int iRow = (int)(i/proporcao);
				double dens = densidade[xIni+i][yIni+iRow]; 
//				somaPerigo += dens; 
				maxPerigo = Math.max(maxPerigo, dens);
			}
		}else{
			double proporcao = rows/cols;
			for(int i=0; i<rows; i++){
				int iCol = (int)(i/proporcao);
				
				double dens=0;
				try{
				dens = densidade[xIni+iCol][yIni+i];
				}catch(RuntimeException e){
					System.out.println(densidade.length + ", "+ densidade[0].length);
					throw e;
				}
				
//				somaPerigo += dens; 
				maxPerigo = Math.max(maxPerigo, dens);
			}
		}
		
		return maxPerigo;
//		return somaPerigo;
	}
	
	/**
	 * Soma o valor de perigo de cada célula cordada pelo Segmento.
	 * Cada valor é multiplicado (ponderado) pelo tamanho do segmento que passa em cima da célula
	 */
	private double perigoDistancia(SegmentoReta segm){
		
		Rectangle bounds = kernel.getBounds();
		double[][] dens = kernel.getDensidadeGrid();
		int node = kernel.nodeSize;
		
		//limita o intervalo de células que participarão do cálculo
		int xIni = (int)((segm.getInicio().x - bounds.x) / node); //coluna na matriz de densidades onde se encontra o ponto inicial do segmento
		int xFim = (int)((segm.getFim().x - bounds.x) / node); //coluna do ponto final
		int yIni = (int)((segm.getInicio().y - bounds.y) / node); //linha do ponto inicial
		int yFim = (int)((segm.getFim().y - bounds.y) / node); //linha do ponto final
		
		//se o fim for maior q o início, inverte
		if(xIni > xFim){ int aux=xIni; xIni=xFim; xFim=aux;}
		if(yIni > yFim){ int aux=yIni; yIni=yFim; yFim=aux;}
		
		//corrigir o caso em q o segm extrapola o mapa de kernel (rota saíndo do viewport do wikicrimes)
		//TODO talvez seja melhor impedir q isso aconteça, ver depois
		if(xIni < 0) xIni = 0;
		if(xFim > dens.length-1) xFim = dens.length-1;
		if(yIni < 0) yIni = 0;
		if(yFim > dens[0].length-1) yFim = dens[0].length-1;
		
		//somar os valores de perigo (ponderados) de cada célula
		double valor = 0;
		for(int i=xIni; i<=xFim; i++){
			for(int j=yIni; j<=yFim; j++){
				Rectangle cel = new Rectangle(bounds.x + i*node, bounds.y + j*node, node, node);
				
				/*teste*/
				double peso=0;
				try{
					peso = tamanhoSegmCortandoQuadrado(segm, cel);
				}catch(AssertionError e){
					//TODO ta dando "java.lang.AssertionError: 3 pontos. Só pode ter 0, 1 ou 2 pontos de interseção"
					System.out.println("i=" + i + "j=" + j);
					throw e;
				}
				/*teste*/
				//double peso = tamanhoSegmCortandoQuadrado(segm, cel);
				
				double d = dens[i][j];
				valor += d*peso;
			}
		}
		
		return valor;
	}
	
	private double tamanhoSegmCortandoQuadrado(SegmentoReta segm, Rectangle celula){
		
		List<Ponto> intersecoes = listaIntersecoes(segm, celula);
		
		if(intersecoes.size() == 1) 
			return 0.0; //o segm toca no canto da célula mas não entra
		
		double tam = 0.0;
		Ponto ini = segm.getInicio();
		Ponto fim = segm.getFim();
		if(!intersecoes.isEmpty()){
			//tem 2 pontos de interseção
			
			if(celula.contains(ini) && celula.contains(fim)){
				//o segmento está completamente contido na célula
				tam = segm.comprimento();
			}else{
				
				if(celula.contains(ini) || celula.contains(fim)){
					//um dos limites do segmento está dentro da célula
					Ponto dentro = null, fora = null;
					if(celula.contains(ini)){
						dentro = ini;
						fora = fim;
					}else{
						dentro = fim;
						fora = ini;
					}
						
					if(new SegmentoReta(intersecoes.get(0), fora).comprimento() 
							> new SegmentoReta(intersecoes.get(1), fora).comprimento())
						intersecoes.remove(0); //remove o q tá mais longe do FORA
					else
						intersecoes.remove(1);
					
					intersecoes.add(dentro); //coloca o DENTRO no lugar do q foi removido
				}
				
				tam = new SegmentoReta(intersecoes.get(0), intersecoes.get(1)).comprimento();
			
			}
		}
		
		return tam;
	}
	
	/**
	 * Lista os pontos de interseção de uma reta em um retângulo.
	 */
	private List<Ponto> listaIntersecoes(SegmentoReta reta, Rectangle retangulo){
		Set<Ponto> intersecoes = new HashSet<Ponto>(); //usando um Set pra descartar os pontos idênticos (se a reta passar no canto da célula, vão ter 2 idênticos, ex: NORTE e OESTE, se for no canto superior esquerdo)
		
		double a = reta.coefA();
		double b = reta.coefB();
		if(!Double.isInfinite(a) && !Double.isInfinite(b)){
			double corte;
			//norte
			corte = (retangulo.y - b) / a; //o valor de X do ponto onde a reta corta o limite norte da célula (que é uma outra reta)
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y));
			//sul
			corte = (retangulo.y+retangulo.height - b) / a;
			if(corte >= retangulo.x && corte <= retangulo.x+retangulo.width)
				intersecoes.add(new Ponto((int)Math.round(corte), retangulo.y+retangulo.height));
			//oeste
			corte = a*retangulo.x + b; //o valor de Y do ponto onde a reta corta o limite oeste da célula
			if(corte >= retangulo.y && corte <= retangulo.y+retangulo.height)
				intersecoes.add(new Ponto(retangulo.x, (int)Math.round(corte)));
			//leste
			corte = a*(retangulo.x+retangulo.width) + b;
			if(corte >= retangulo.y && corte <= retangulo.y+retangulo.height)
				intersecoes.add(new Ponto(retangulo.x+retangulo.width, (int)Math.round(corte)));
		}else{
			int x = reta.getInicio().x;
			intersecoes.add(new Ponto(x, retangulo.y));
			intersecoes.add(new Ponto(x, retangulo.y+retangulo.height));
		}
		
		/*teste*/
		if(intersecoes.size()>2){
			throw new AssertionError(intersecoes.size()+" pontos. Só pode ter 0, 1 ou 2 pontos de interseção");
		}
		/*teste*/
		
		return new ArrayList<Ponto>(intersecoes);
	}
	
//	public static void main(String[] args) {
//		Rectangle b = new Rectangle(0,0,100,100);
//		List<Point> p = new ArrayList<Point>();
//		for(int i=0; i<50; i++){
//			p.add(new Point((int)(b.x+Math.random()*b.width),(int)(b.y+Math.random()*b.height)));
//		}
//		KernelMap k = new KernelMap(10, 30, b, p);
//		LogicaRotaSegura l = new LogicaRotaSegura(k);
//		Rota r0 = new Rota(new Ponto(5,-10), new Ponto(5,110));
//		Rota r1 = new Rota(new Ponto(25,-10), new Ponto(25,110));
//		Rota r2 = new Rota(new Ponto(45,-10), new Ponto(45,110));
//		Rota r3 = new Rota(new Ponto(65,-10), new Ponto(65,110));
//		Rota r4 = new Rota(new Ponto(85,-10), new Ponto(85,110));
//		
//		TesteRotas.mostra("teste", k, r0, r1, r2, r3, r4);
//		System.out.println(l.perigo(r0));
//		System.out.println(l.perigo(r1));
//		System.out.println(l.perigo(r2));
//		System.out.println(l.perigo(r3));
//		System.out.println(l.perigo(r4));
//	}
	
	public void limpar(){
		grafoRotas = null;
		trechosJaGerados = new HashSet<SegmentoReta>();
	}
	
	public GrafoRotas getGrafoRotas() {
		return grafoRotas;
	}
	public void setGrafoRotas(GrafoRotas grafoRotas) {
		this.grafoRotas = grafoRotas;
	}
	public KernelMap getKernel() {
		return kernel;
	}
	
	/*teste*/public static void loadParams(){
	/*teste*/	RAZAO_TAMANHO_EXPANSOES = TesteCenariosRotas.getTamExp();
	/*teste*/	QUANTIDADE_EXPANSOES = TesteCenariosRotas.getNumExp();
	/*teste*/}
	
}