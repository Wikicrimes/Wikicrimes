package org.wikicrimes.util.kernelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Conjunto de coordenadas ou conjunto de segmentos de reta ligados.
 * 
 * @author Victor E.
 */
public class Caminho {

	private List<Ponto> pontos;
	
	private final static int MAX_APROXIMACAO = PropertiesLoader.getInt("tol_aproximacao"); //distancia m�xima entre pontos na obten��o de pontos e rotas aproximadas

	public Caminho() {
		pontos = new LinkedList<Ponto>();
	}

	public Caminho(List<Ponto> pontos) {
		this();
		for (Ponto ponto : pontos) {
			if (ponto != null)
				append(ponto);
		}
	}

	public Caminho(Ponto... ponto) {
		this(Arrays.asList(ponto));
	}

	public Caminho(SegmentoReta... segm) {
		this();
		for (SegmentoReta s : segm) {
			if (s != null)
				append(s);
		}
	}

	public Caminho(Caminho rota) {
		pontos = new LinkedList<Ponto>(rota.pontos);
	}

	/**
	 * @param string
	 *            : uma String no formato xxx,xxxaxxx,xxxaxxx,xxx (pontos do
	 *            formato xxx,xxx separados por 'a')
	 */
	public Caminho(String string) {
		pontos = new LinkedList<Ponto>();
		for (String str : string.split("a")) {
			pontos.add(new Ponto(str));
		}
	}

	/**
	 * Adiciona uma Coordenada no fim desta Rota
	 */
	public void append(Ponto ponto) {
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
	 * Adiciona todas as Coordenadas da rota passada como par�metro no fim desta
	 * Rota
	 */
	public void append(Caminho rota) {
		for (Ponto coord : rota.getPontos()) {
			append(coord);
		}
	}

	public void addInicio(Caminho rota) {
		List<Ponto> pts = rota.getPontos();
		for (int i = pts.size() - 1; i >= 0; i--) {
			addInicio(pts.get(i));
		}
	}

	public void append(SegmentoReta segm) {
		if (pontos.isEmpty()) {
			pontos.add(segm.getInicio());
			pontos.add(segm.getFim());
		} else if (segm.getInicio().equals(this.getFim())) {
			pontos.add(segm.getFim());
		} else {
			throw new RuntimeException(
					"tentou-se adicionar um SegmentoReta a uma Rota, "
							+ "mas o in�cio do SegmentoReta � diferente do fim da rota");
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
	 * @return as Coordenadas que comp�em a Rota
	 */
	public List<Ponto> getPontos() {
		return pontos;
	}

	public Ponto[] getPontosArray() {
		return pontos.toArray(new Ponto[pontos.size()]);
	}
	
	/**
	 * @return os SegmentosReta que comp�em a Rota. � equivalente ao
	 *         getCoordenadas.
	 */
	public List<SegmentoReta> getSegmentosReta() {
		List<SegmentoReta> listSeg = new LinkedList<SegmentoReta>();
		for (int i = 0; i < pontos.size() - 1; i++) {
			listSeg.add(new SegmentoReta(pontos.get(i), pontos.get(i + 1)));
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
		List<SegmentoReta> segms = getSegmentosReta();
		for (SegmentoReta segm : segms) {
			distancia += segm.comprimento();
		}
		return distancia;
	}

	/**
	 * @return dist�ncia (reta) entre a ORIGEM e o DESTINO
	 */
	public double distanciaRetaOD() {
		return new SegmentoReta(getInicio(), getFim()).comprimento();
	}

	public Caminho invertida() {
		Caminho inv = new Caminho();
		for (Ponto p : pontos) {
			inv.append(p.invertido());
		}
		return inv;
	}

	public Caminho rotacionada(double angulo) {
		Caminho rota = new Caminho();
		for (Ponto p : this.getPontos()) {
			rota.append(p.rotacionado(angulo));
		}
		return rota;
	}

	public static List<Caminho> rotaciona(List<Caminho> rotas, double angulo) {
		List<Caminho> rotas2 = new ArrayList<Caminho>();
		for (Caminho r : rotas) {
			rotas2.add(r.rotacionada(angulo));
		}
		return rotas2;
	}
	
	/**
	 * Verifica se esta rota cont�m um ponto pr�ximo do passado por parametro.<br>
	 * Motiva��o desta aproxima��o: Precisa-se comparar a rota segura gerada
	 * pelo server com a rota gerada pelo googlemaps (a partir da primeira).
	 * Esta segunda rota deveria conter pontos id�nticos a todos os pontos
	 * daquela primeira rota, mas isto n�o acontece (se um dos pontos n�o cair
	 * dentro de uma rua, por ex.).
	 */
	public boolean contemPerto(Ponto p, int distMax) {
		return getPontoPerto(p, distMax) != null;
	}

	/**
	 * Obt�m uma coordenada que pertence � rota e que � a mais pr�xima da
	 * coordenada passada por par�metro.<br>
	 * Motiva��o desta aproxima��o: Precisa-se comparar a rota segura gerada
	 * pela l�gica de rotas seguras com a rota gerada pelo googlemaps (a partir
	 * da primeira). Esta segunda rota deveria conter pontos id�nticos a todos
	 * os pontos daquela primeira rota, mas isto nem sempre acontece (por ex: se
	 * um dos pontos cair dentro de um quarteir�o).
	 */
	public Ponto getPontoPerto(Ponto p, int distMax) {

		if (p == null)
			return null;

		// sobrescreve o m�todo equals para esta Coordenada
		PontoAproximado ptAproximado = new PontoAproximado(p.x, p.y);

		int index = -1;
		while (ptAproximado.proximidade <= distMax && index == -1) {
			index = pontos.indexOf(ptAproximado); // indice da coordenada pr�xima (a qual foi identificada pelo metodo equals)
			// (todas as compara��es feitas neste m�todo indexOf usar�o a implementa��o do equals de CoordenadaAproximada)
			ptAproximado.proximidade++;
		}

		if (index != -1)
			return pontos.get(index);
		else
			return null;
	}

	/**
	 * Obt�m os pontos desta rota que correspondem a pontos aproximados aos de
	 * uma outra rota semelhante. Obs: espera-se que a "outraRota" tenha menos
	 * pontos do que esta
	 */
	public Caminho getRotaAproximada(Caminho outraRota, int distMax) {
		Caminho rotaAproximada = new Caminho();
		for (Ponto p : outraRota.getPontos()) {
			Ponto aprox = this.getPontoPerto(p, distMax);
			if (aprox != null)
				rotaAproximada.append(aprox);
		}
		return rotaAproximada;
	}

	/**
	 * O intuito desta classe � sobrescrever o m�todo equals de Coordenada para
	 * ser usado nos m�todos getCoordenadaAproximada() e containsAproximado().
	 * Esta implementa��o do m�todo equals reconhece Coordenadas pr�ximas como
	 * se fossem iguais.
	 */
	private class PontoAproximado extends Ponto {
		public PontoAproximado(int x, int y) {
			super(x, y);
		}

		// m�todo equals aproximado (basta estar perto pra ser considerado
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
			sb.append("a");
		}
		sb.append(pontos.get(pontos.size() - 1).toString());
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Caminho) {
			Caminho rota = (Caminho) obj;
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
	public Object clone() {
		Caminho r = new Caminho();
		for (Ponto p : this.pontos)
			r.append(p);
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
		for (SegmentoReta s : getSegmentosReta())
			if (s.passaPor(p))
				return true;
		return false;
	}
	
	public boolean passaPor(SegmentoReta segm){
		for (SegmentoReta s : getSegmentosReta())
			if (s.passaPor(segm))
				return true;
		return false;
	}

	public void promoverPontoParaVertice(Ponto p) throws PontoForaDoCaminhoException{
		for (SegmentoReta s : getSegmentosReta())
			if (s.passaPor(p)) {
				addDepois(s.getInicio(), p);
				return;
			}
		throw new PontoForaDoCaminhoException(p, this);
	}

	public Caminho subCaminho(Ponto p1, Ponto p2) {
		int i1 = 0;
		int i2 = 0;
		Caminho temp = (Caminho)clone();
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
		
		Caminho sub = new Caminho();
		for (int i = i1; i <= i2; i++) {
			sub.append(temp.pontos.get(i));
		}
		return sub;
	}
	
	public List<Caminho> dividir(Ponto... pontos){
		List<Caminho> partes = new LinkedList<Caminho>();
		Ponto p1 = pontos[0];
		Ponto p2 = null;
		for(int i=1; i<pontos.length; i++){
			p2 = pontos[i];
			partes.add( subCaminho(p1, p2) );
			p1 = p2;
		}
		return partes;
	}
	
	public List<Caminho> dividirAprox(Ponto... pontos){
		Ponto[] pontosAprox = new Ponto[pontos.length]; 
		for(int i=0; i<pontos.length; i++){
			Ponto p = pontos[i];
			Ponto pAprox = getPontoPerto(p, MAX_APROXIMACAO);
			pontosAprox[i] = pAprox;
			/*teste*/System.out.println("p:" + p + ", pAprox:" + pAprox);
		}
		return dividir(pontosAprox);
	}
	
	public Ponto intersecao(SegmentoReta segm){
		for(SegmentoReta s : getSegmentosReta()){
			Ponto p = segm.intersecao(s);
			if(p != null)
				return p;
		}
		return null;
	}
	
	public Ponto intersecao(Caminho rota){
		for(SegmentoReta s : getSegmentosReta()){
			Ponto p = rota.intersecao(s);
			if(p != null)
				return p;
		}
		return null;
	}
	
	public List<Caminho> subtracao(Caminho caminho){
		List<Caminho> partesDiferentes = new LinkedList<Caminho>();
		Caminho resto = (Caminho)this.clone(); //copia do Caminho "this" q vai sendo consumida no decorrer deste metodo
		for(SegmentoReta s : caminho.getSegmentosReta()){
			if(resto.passaPor(s)){
				Ponto iniResto = resto.getInicio();
				Ponto fimResto = resto.getFim();
				Ponto iniSegm = s.getInicio();
				Ponto fimSegm = s.getFim();
				if(!resto.getInicio().isPerto(s.getInicio())){
					Caminho parteAntes = resto.subCaminho(iniResto, iniSegm);
					partesDiferentes.add(parteAntes);
				}
				resto = resto.subCaminho(fimSegm, fimResto);
			}else{
				//TODO caso passar por um peda�o de "s"
			}
		}
		partesDiferentes.add(resto);
		return partesDiferentes;
	}

	/**
	 * Percorre a rota a partir do in�cio por "dist" unidades de comprimento e
	 * retorna o Ponto resultante
	 */
	public Ponto buscarPonto(double dist) {
		return buscarPonto(getInicio(), dist);
	}

	/**
	 * Percorre a rota a partir do Ponto "partida" por "dist" unidades de
	 * comprimento e retorna o Ponto resultante
	 */
	public Ponto buscarPonto(Ponto partida, double dist) {
		//TODO da pra implementar isso melhor, tipo busca bin�ria
		boolean chegou = false; // se ja passou pelo ponto "partida"
		double cont = .0;
		for (int i = 0; i < pontos.size() - 1; i++) {
			Ponto p1 = pontos.get(i);
			Ponto p2 = pontos.get(i + 1);
			if (p1.equals(partida))
				chegou = true;
			SegmentoReta s = new SegmentoReta(p1, p2);
			double compS = s.comprimento();
			if (chegou) {
				cont += compS;
				if (cont > dist) {// o contador acabou de passar da dist, entao estamos no segmento que contem o ponto
					// tem dois triangulos proporcionais e falta s� 2 lados de um deles (os catetos do triangulo pequeno)
					double hipotGrande = compS;
					double hipotPequena = dist - cont + hipotGrande; // peda�o de S q ainda entra
					double razao = hipotGrande / hipotPequena; // razao entre o S inteiro e o peda�o
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
		//TODO da pra implementar isso melhor, tipo busca bin�ria
		boolean chegou = false; // se ja passou pelo ponto "partida"
		double cont = .0; // vai acumulando a distancia percorrida a medida que
							// anda no for abaixo
		// as 2 vari�veis acima s�o objetos (wrappers) pq precisam ser
		// modificadas dentro da "subrotinaBuscaPonto"
		for (int i = pontos.size() - 1; i > 0; i--) {
			Ponto p1 = pontos.get(i);
			Ponto p2 = pontos.get(i - 1);
			if (p1.equals(partida))
				chegou = true;
			SegmentoReta s = new SegmentoReta(p1, p2);
			double compS = s.comprimento();
			if (chegou) {
				cont += compS;
				if (cont > dist) {// o contador acabou de passar da dist, entao estamos no segmento que contem o ponto
					// tem dois triangulos proporcionais e falta s� 2 lados de um deles (os catetos do triangulo pequeno)
					double hipotGrande = compS;
					double hipotPequena = dist - cont + hipotGrande; // peda�o de S q ainda entra
					double razao = hipotGrande / hipotPequena; // razao entre o S inteiro e o peda�o
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

}

class PontoForaDoCaminhoException extends Exception{
	Ponto ponto;
	Caminho caminho;
	public PontoForaDoCaminhoException(Ponto ponto, Caminho caminho) {
		super("Ponto: " + ponto);
		this.ponto = ponto;
		this.caminho = caminho;
	}
}