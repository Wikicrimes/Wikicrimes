package org.wikicrimes.util.rotaSegura.geometria;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.wikicrimes.util.kernelMap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.testes.TesteRotasImg;

/**
 * Conjunto de coordenadas ou conjunto de segmentos de reta ligados.
 * 
 * @author Victor E.
 */
public class Rota {

	private List<Ponto> pontos;
	
	private final static int MAX_APROXIMACAO = PropertiesLoader.getInt("saferoutes.point_approximation_limit"); //distancia máxima entre pontos na obtenção de pontos e rotas aproximadas

	public Rota() {
		pontos = new LinkedList<Ponto>();
	}

	public Rota(List<? extends Point> pontos) {
		this();
		for (Point ponto : pontos) {
			if (ponto != null)
			if(ponto instanceof Ponto)
				addFim((Ponto)ponto);
			else
				addFim(new Ponto(ponto));
		}
	}

	public Rota(Point... ponto){
		this(Arrays.asList(ponto));
	}

	public Rota(Segmento... segm) {
		this();
		for (Segmento s : segm) {
			if (s != null)
				addFim(s);
		}
	}

	public Rota(Rota rota) {
		pontos = new LinkedList<Ponto>(rota.pontos);
	}

	/**
	 * @param string
	 *            : uma String no formato xxx,xxxaxxx,xxx;xxx,xxx (pontos do
	 *            formato xxx,xxx separados por ';')
	 */
	public Rota(String string) {
		pontos = new LinkedList<Ponto>();
		for (String str : string.split(";")) {
			pontos.add(new Ponto(str));
		}
	}

	/**
	 * Adiciona uma Coordenada no fim desta Rota
	 */
	public void addFim(Ponto ponto) {
		if (pontos.contains(ponto)) {
			// System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(ponto.clone()); // nao pode ter objetos iguais, senao vai dar problemas pra identificar
		} else {
			pontos.add(ponto);
		}
	}
	
	/**
	 * Adiciona uma Coordenada no fim desta Rota
	 */
	public void addInicio(Ponto ponto) {
		if (pontos.contains(ponto)) {
			// System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(0, ponto.clone());
		} else {
			pontos.add(0, ponto);
		}
	}

	public void addDepois(Ponto pontoAnterior, Ponto novoPonto) {
		int index = pontos.indexOf(pontoAnterior);
		if (pontos.contains(novoPonto)) {
			// System.err.println("coordenada repetida sendo adicionada numa Rota");
			pontos.add(index + 1, novoPonto.clone());
		} else {
			pontos.add(index + 1, novoPonto);
		}
	}

	/**
	 * Adiciona todas as Coordenadas da rota passada como parâmetro no fim desta
	 * Rota
	 */
	public void addFim(Rota rota) {
		for (Ponto coord : rota.getPontos()) {
			addFim(coord);
		}
	}

	public void addInicio(Rota rota) {
		List<Ponto> pts = rota.getPontos();
		for (int i = pts.size() - 1; i >= 0; i--) {
			addInicio(pts.get(i));
		}
	}

	public void addFim(Segmento segm) {
		if (pontos.isEmpty()) {
			pontos.add(segm.getInicio());
			pontos.add(segm.getFim());
		} else if (segm.getInicio().equals(this.getFim())) {
			pontos.add(segm.getFim());
		} else {
			throw new RuntimeException(
					"tentou-se adicionar um SegmentoReta a uma Rota, "
							+ "mas o início do SegmentoReta é diferente do fim da rota");
		}
	}

	public boolean remove(Ponto p) {
		return pontos.remove(p);
	}

	public Ponto pontoAnterior(Ponto p) {
		if (contem(p) && !isFirst(p)) {
			int index = pontos.indexOf(p);
			return pontos.get(index - 1);
		} else {
			return null;
		}
	}

	public Ponto pontoPosterior(Ponto p) {
		if (contem(p) && !isLast(p)) {
			int index = pontos.indexOf(p);
			return pontos.get(index + 1);
		} else {
			return null;
		}
	}

	/**
	 * @return as Coordenadas que compôem a Rota
	 */
	public List<Ponto> getPontos() {
		return pontos;
	}

	public Ponto[] getPontosArray() {
		return pontos.toArray(new Ponto[pontos.size()]);
	}
	
	/**
	 * @return os SegmentosReta que compôem a Rota. É equivalente ao
	 *         getCoordenadas.
	 */
	public List<Segmento> getSegmentosReta() {
		List<Segmento> listSeg = new LinkedList<Segmento>();
		for (int i = 0; i < pontos.size() - 1; i++) {
			listSeg.add(new Segmento(pontos.get(i), pontos.get(i + 1)));
		}
		return listSeg;
	}

	public int size() {
		return pontos.size();
	}

	public boolean isLast(Ponto p) {
		return pontos.lastIndexOf(p) == pontos.size() - 1;
	}

	public boolean isFirst(Ponto p) {
		return pontos.indexOf(p) == 0;
	}

	public boolean contem(Ponto p) {
		return pontos.contains(p);
	}

	public Ponto getInicio() {
		if (pontos.isEmpty())
			return null;
		return pontos.get(0);
	}

	public Ponto getFim() {
		if (pontos.isEmpty())
			return null;
		return pontos.get(pontos.size() - 1);
	}

	public double distanciaPercorrida() {
		double distancia = 0;
		List<Segmento> segms = getSegmentosReta();
		for (Segmento segm : segms) {
			distancia += segm.comprimento();
		}
		return distancia;
	}

	/**
	 * @return distância (reta) entre a ORIGEM e o DESTINO
	 */
	public double distanciaRetaOD() {
		return new Segmento(getInicio(), getFim()).comprimento();
	}

	public Rota invertida() {
		Rota inv = new Rota();
		for (Ponto p : pontos) {
			inv.addFim(p.invertido());
		}
		return inv;
	}

	public Rota rotacionada(double angulo) {
		Rota rota = new Rota();
		for (Ponto p : this.getPontos()) {
			rota.addFim(p.rotacionado(angulo));
		}
		return rota;
	}

	public static List<Rota> rotaciona(List<Rota> rotas, double angulo) {
		List<Rota> rotas2 = new ArrayList<Rota>();
		for (Rota r : rotas) {
			rotas2.add(r.rotacionada(angulo));
		}
		return rotas2;
	}
	
	/**
	 * Verifica se esta rota contém um ponto próximo do passado por parametro.<br>
	 * Motivação desta aproximação: Precisa-se comparar a rota segura gerada
	 * pelo server com a rota gerada pelo googlemaps (a partir da primeira).
	 * Esta segunda rota deveria conter pontos idênticos a todos os pontos
	 * daquela primeira rota, mas isto não acontece (se um dos pontos não cair
	 * dentro de uma rua, por ex.).
	 */
	public boolean contemPerto(Ponto p, int distMax) {
		return getPontoPerto(p, distMax) != null;
	}

	/**
	 * Obtém uma coordenada que pertence à rota e que é a mais próxima da
	 * coordenada passada por parâmetro.<br>
	 * Motivação desta aproximação: Precisa-se comparar a rota segura gerada
	 * pela lógica de rotas seguras com a rota gerada pelo googlemaps (a partir
	 * da primeira). Esta segunda rota deveria conter pontos idênticos a todos
	 * os pontos daquela primeira rota, mas isto nem sempre acontece (por ex: se
	 * um dos pontos cair dentro de um quarteirão).
	 */
	public Ponto getPontoPerto(Ponto p, int distMax) {

		if (p == null)
			return null;

		// sobrescreve o método equals para esta Coordenada
		PontoAproximado ptAproximado = new PontoAproximado(p.x, p.y);

		int index = -1;
		while (ptAproximado.proximidade <= distMax && index == -1) {
			index = pontos.indexOf(ptAproximado); // indice da coordenada próxima (a qual foi identificada pelo metodo equals)
			// (todas as comparações feitas neste método indexOf usarão a implementação do equals de CoordenadaAproximada)
			ptAproximado.proximidade++;
		}

		if (index != -1)
			return pontos.get(index);
		else
			return null;
	}

	/**
	 * Obtém os pontos desta rota que correspondem a pontos aproximados aos de
	 * uma outra rota semelhante. Obs: espera-se que a "outraRota" tenha menos
	 * pontos do que esta
	 */
	public Rota getRotaAproximada(Rota outraRota, int distMax) {
		Rota rotaAproximada = new Rota();
		for (Ponto p : outraRota.getPontos()) {
			Ponto aprox = this.getPontoPerto(p, distMax);
			if (aprox != null)
				rotaAproximada.addFim(aprox);
		}
		return rotaAproximada;
	}

	/**
	 * O intuito desta classe é sobrescrever o método equals de Coordenada para
	 * ser usado nos métodos getCoordenadaAproximada() e containsAproximado().
	 * Esta implementação do método equals reconhece Coordenadas próximas como
	 * se fossem iguais.
	 */
	private class PontoAproximado extends Ponto {
		public PontoAproximado(int x, int y) {
			super(x, y);
		}

		// método equals aproximado (basta estar perto pra ser considerado
		// "igual")
		private int proximidade;

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Ponto) {
				Ponto outra = (Ponto) obj;
				return super.isPerto(outra, proximidade);
			} else {
				return false;
			}
		}

	}
	
	@Override
	public String toString() {
		if (pontos.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pontos.size() - 1; i++) {
			sb.append(pontos.get(i).toString());
			sb.append(";");
		}
		sb.append(pontos.get(pontos.size() - 1).toString());
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rota) {
			Rota rota = (Rota) obj;
			return getPontos().equals(rota.getPontos());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int code = getInicio().hashCode() + getFim().hashCode();
		return code;
	}

	@Override
	public Rota clone() {
		Rota r = new Rota();
		for (Ponto p : this.pontos)
			r.addFim(p);
		return r;
	}

	public void limpar() {
		pontos.clear();
	}
	
	/**
	 * Retorna true se o ponto estiver contido em algum SegmentoReta desta rota.
	 * @param tol - tolerancia
	 */
	public boolean passaPor(Ponto p) {
		for (Segmento s : getSegmentosReta())
			if (s.passaPor(p))
				return true;
		return false;
	}
	
	public boolean passaPor(Segmento segm){
		for (Segmento s : getSegmentosReta())
			if (s.passaPor(segm))
				return true;
		return false;
	}

	public void promoverPontoParaVertice(Ponto p) throws PontoForaDoCaminhoException{
		for (Segmento s : getSegmentosReta())
			if (s.passaPor(p)) {
				addDepois(s.getInicio(), p);
				return;
			}
		throw new PontoForaDoCaminhoException(p, this);
	}

	public Rota subRota(Ponto p1, Ponto p2) {
		int i1 = 0;
		int i2 = 0;
		Rota temp = (Rota)clone();
		try {
			i1 = temp.pontos.indexOf(p1);
			if (i1 == -1){
				temp.promoverPontoParaVertice(p1);
				i1 = temp.pontos.indexOf(p1);
			}
			i2 = temp.pontos.indexOf(p2);
			if (i2 == -1){
				temp.promoverPontoParaVertice(p2);
				i2 = temp.pontos.indexOf(p2);
			}
		} catch (PontoForaDoCaminhoException e) {
			e.printStackTrace();
			return null;
		}
		
		Rota sub = new Rota();
		for (int i = i1; i <= i2; i++) {
			sub.addFim(temp.pontos.get(i));
		}
		return sub;
	}
	
	public Rota subRotaPorVertices(Ponto p1, Ponto p2) {
		int i1 = checarIndice(p1);
		int i2 = checarIndice(p2);
		if(i1 >= i2) throw new InvalidParameterException("p2 nao esta depois de p1");
		
		List<Ponto> sublista = pontos.subList(i1, i2+1);
		return new Rota(sublista);
	}
	
	public List<Rota> dividirEmPontosExistentes(Ponto... pontos){
		List<Rota> partes = new LinkedList<Rota>();
		if(pontos.length == 0) {
			partes.add(this);
			return partes;
		}
		Ponto p1 = pontos[0];
		Ponto p2 = null;
		for(int i=1; i<pontos.length; i++){
			p2 = pontos[i];
			if(!p1.equals(p2))
				partes.add( subRota(p1, p2) );
			p1 = p2;
		}
		return partes;
	}
	
	public List<Rota> dividirAprox(List<Ponto> pontos){
		return dividirAprox(pontos.toArray(new Ponto[pontos.size()]));
	}
	
	public List<Rota> dividirAprox(Ponto... pontos){
		Ponto[] pontosAprox = new Ponto[pontos.length]; 
		for(int i=0; i<pontos.length; i++){
			Ponto p = pontos[i];
			Ponto pAprox = getPontoPerto(p, MAX_APROXIMACAO);
			if(pAprox == null && passaPor(p)){
				try {
					promoverPontoParaVertice(p);
					pAprox = p;
				} catch (PontoForaDoCaminhoException e) {
					e.printStackTrace();
				}
			}
			if(pAprox == null) throw new AssertionError();
			pontosAprox[i] = pAprox;
		}
		return dividirEmPontosExistentes(pontosAprox);
	}
	
	public List<Rota> dividir(double tamanhoParte){
		List<Ponto> ptsDivisores = new ArrayList<Ponto>();
		ptsDivisores.add(getInicio());
		double posicao = 0;
		double tamanho = distanciaPercorrida();
		Ponto ptAnterior = getInicio();
		while(posicao+tamanhoParte < tamanho) {
			Ponto p = buscarPonto(ptAnterior, tamanhoParte);
			ptsDivisores.add(p);
			posicao += tamanhoParte;
			ptAnterior = p;
		}
		ptsDivisores.add(getFim());
		
		//TODO testar se ta entrando algum elemento nulo em ptsDivisores
		
		Ponto[] arrayPtsDivisores = ptsDivisores.toArray(new Ponto[ptsDivisores.size()]);
		return dividirAprox(arrayPtsDivisores);
	}
	
	public List<Rota> intersecao(Rota rota){
		//TODO
		throw new UnsupportedOperationException();
	}
	
	public Ponto intersecaoPonto(Segmento segm){
		for(Segmento s : getSegmentosReta()){
			Ponto p = segm.intersecao(s);
			if(p != null)
				return p;
		}
		return null;
	}
	
	public Ponto intersecaoPonto(Rota rota){
		for(Segmento s : getSegmentosReta()){
			Ponto p = rota.intersecaoPonto(s);
			if(p != null)
				return p;
		}
		return null;
	}
	
	public List<Rota> subtracao(Rota caminho){
		List<Rota> partesDiferentes = new LinkedList<Rota>();
		Rota resto = (Rota)this.clone(); //copia do Caminho "this" q vai sendo consumida no decorrer deste metodo
		for(Segmento s : caminho.getSegmentosReta()){
			if(resto.passaPor(s)){
				Ponto iniResto = resto.getInicio();
				Ponto fimResto = resto.getFim();
				Ponto iniSegm = s.getInicio();
				Ponto fimSegm = s.getFim();
				if(!resto.getInicio().isPerto(s.getInicio())){
					Rota parteAntes = resto.subRota(iniResto, iniSegm);
					partesDiferentes.add(parteAntes);
				}
				resto = resto.subRota(fimSegm, fimResto);
			}else{
				//TODO caso passar por um pedaço de "s"
			}
		}
		partesDiferentes.add(resto);
		return partesDiferentes;
	}
	
	public Ponto buscarPonto(Ponto partida, double dist) {
		try {
			promoverPontoParaVertice(partida);
			return buscarPontoExistente(partida, dist);
		} catch (PontoForaDoCaminhoException e) {
			e.printStackTrace();
			throw new InvalidParameterException("a rota nao passa pelo Ponto 'partida'");
		}
	}

	/**
	 * Percorre a rota a partir do início por "dist" unidades de comprimento e
	 * retorna o Ponto resultante
	 */
	public Ponto buscarPontoExistente(double dist) {
		return buscarPontoExistente(getInicio(), dist);
	}

	/**
	 * Percorre a rota a partir do Ponto "partida" por "dist" unidades de
	 * comprimento e retorna o Ponto resultante
	 */
	public Ponto buscarPontoExistente(Ponto partida, double dist) {
		//TODO da pra implementar isso melhor, tipo busca binária
		double distRota = this.distanciaPercorrida();
		if(dist > distRota)
			throw new InvalidParameterException("A distancia passada para a busca eh maior do que o tamanho da rota");
		
		Rota partidaAteFim = subRotaPorVertices(partida, getFim());
		double distParte = partidaAteFim.distanciaPercorrida();
		if(dist > distParte)
			throw new InvalidParameterException("A distancia passada para a busca eh maior do que o tamanho da rota depois de 'partida'");
		
		List<Ponto> pontos = partidaAteFim.getPontos();
		double cont = distRota - distParte;
		for (int i = 0; i < pontos.size()-1; i++) {
			Ponto p1 = pontos.get(i);
			Ponto p2 = pontos.get(i + 1);
			Segmento s = new Segmento(p1, p2);
			double compS = s.comprimento();
			cont += compS;
			if (cont > dist) {// o contador acabou de passar da dist, entao estamos no segmento que contem o ponto
				// tem dois triangulos proporcionais e falta só 2 lados de um deles (os catetos do triangulo pequeno)
				double hipotGrande = compS;
				double hipotPequena = dist - cont + hipotGrande; // pedaço de S q ainda entra
				double razao = hipotGrande / hipotPequena; // razao entre o S inteiro e o pedaço
				double catetoXGrande = p2.x - p1.x;
				double catetoYGrande = p2.y - p1.y;
				double catetoXPeq = catetoXGrande / razao;
				double catetoYPeq = catetoYGrande / razao;
				double x = p1.x + catetoXPeq;
				double y = p1.y + catetoYPeq;
				return new Ponto((int) Math.round(x), (int) Math.round(y));
			}
		}
		throw new AssertionError();
	}

	/**
	 * Percorre a rota voltando a partir do fim por "dist" unidades de
	 * comprimento e retorna o Ponto resultante
	 */
	public Ponto buscarPontoVoltando(double dist) {
		return buscarPontoVoltando(getFim(), dist);
	}

	/**
	 * Percorre a rota voltando a partir do Ponto "partida" por "dist" unidades
	 * de comprimento e retorna o Ponto resultante
	 */
	public Ponto buscarPontoVoltando(Ponto partida, double dist) {
		//TODO da pra implementar isso melhor, tipo busca binária
		boolean chegou = false; // se ja passou pelo ponto "partida"
		double cont = .0; // vai acumulando a distancia percorrida a medida que
							// anda no for abaixo
		// as 2 variáveis acima são objetos (wrappers) pq precisam ser
		// modificadas dentro da "subrotinaBuscaPonto"
		for (int i = pontos.size() - 1; i > 0; i--) {
			Ponto p1 = pontos.get(i);
			Ponto p2 = pontos.get(i - 1);
			if (p1.equals(partida))
				chegou = true;
			Segmento s = new Segmento(p1, p2);
			double compS = s.comprimento();
			if (chegou) {
				cont += compS;
				if (cont > dist) {// o contador acabou de passar da dist, entao estamos no segmento que contem o ponto
					// tem dois triangulos proporcionais e falta só 2 lados de um deles (os catetos do triangulo pequeno)
					double hipotGrande = compS;
					double hipotPequena = dist - cont + hipotGrande; // pedaço de S q ainda entra
					double razao = hipotGrande / hipotPequena; // razao entre o S inteiro e o pedaço
					double catetoXGrande = p2.x - p1.x;
					double catetoYGrande = p2.y - p1.y;
					double catetoXPeq = catetoXGrande / razao;
					double catetoYPeq = catetoYGrande / razao;
					double x = p1.x + catetoXPeq;
					double y = p1.y + catetoYPeq;
					return new Ponto((int) Math.round(x), (int) Math.round(y));
				}
			}
		}
		return null;
	}
	
	public Rectangle getBounds() {
		return Ponto.getBounds(getPontos());
	}
	
	public static Rectangle getBounds(Collection<Rota> rotas) {
		List<Ponto> pontos = new ArrayList<Ponto>();
		for(Rota rota : rotas) {
			pontos.addAll(rota.getPontos());
		}
		return Ponto.getBounds(pontos);
	}
	
	public int checarIndice(Ponto p) {
		int i = pontos.indexOf(p);
		if(i == -1)
			throw new InvalidParameterException("p nao eh vertice da rota");
		else
			return i;
	}

}

class PontoForaDoCaminhoException extends Exception{
	Ponto ponto;
	Rota caminho;
	public PontoForaDoCaminhoException(Ponto ponto, Rota caminho) {
		super("Ponto: " + ponto);
		this.ponto = ponto;
		this.caminho = caminho;
	}
}