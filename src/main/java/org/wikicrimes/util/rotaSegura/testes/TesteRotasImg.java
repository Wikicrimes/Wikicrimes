package org.wikicrimes.util.rotaSegura.testes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.Suavizador;
import org.wikicrimes.util.kernelMap.renderer.CellBasedRenderer;
import org.wikicrimes.util.kernelMap.renderer.TransparentToColor;
import org.wikicrimes.util.rotaSegura.geometria.Poligono;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;
import org.wikicrimes.util.rotaSegura.logica.Grafo;
import org.wikicrimes.util.rotaSegura.logica.LogicaRotaSegura;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;

/**
 * Representa rotas graficamente, para testes. 
 * 
 * @author victor
 */
public class TesteRotasImg {

	static final boolean enabled = true;
	static Rectangle limites; //guarda os limites(bounds) da primeira rota, pra aplicar os mesmos limites na segunda
	private final int BORDA = 150;
	private double razao;
	private final int RAIO_VERTICE = 5;
	public static NumberFormat f = NumberFormat.getNumberInstance();
	
	private static String dir = TesteCenariosRotas.dir;
	private Image imagem;
	private String titulo;
	
	private Color[] cores = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.PINK};
	private List<Ponto> pontos = new ArrayList<Ponto>();
	private List<Color> coresPontos = new ArrayList<Color>();
	private List<Segmento> segmentos = new ArrayList<Segmento>();
	private List<Color> coresSegmentos = new ArrayList<Color>();
	private List<Rota> caminhos = new ArrayList<Rota>();
	private List<Color> coresCaminhos = new ArrayList<Color>();
	private GrafoRotas grafo;
	private KernelMap kernel;
	private Map<String,Label> labels = new HashMap<String,Label>();
	
	public TesteRotasImg(Rectangle bounds){
		if(!enabled) return;
		if(limites == null)
			limites = bounds;
		imagem = new BufferedImage(620,620,BufferedImage.TYPE_INT_ARGB_PRE);
	}
	
	public TesteRotasImg(Ponto... pontosProsLimites){
		this(getLimites(pontosProsLimites));
	}
	
	public TesteRotasImg(Collection<Ponto> pontosProsLimites){
		this(pontosProsLimites.toArray(new Ponto[pontosProsLimites.size()]));
	}
	
	public TesteRotasImg(GrafoRotas grafo){
		this(grafo.getOrigem(), grafo.getDestino());
		grafo = grafo.clone();
		setGrafo(grafo);
	}
	
	public TesteRotasImg(KernelMap kernel){
		this(kernel.getBounds());
		setKernel(kernel);
	}
	
	public TesteRotasImg(KernelMap kernel, GrafoRotas grafo){
		this(grafo.getOrigem(), grafo.getDestino());
//		this( "", new Ponto(kernel.getBounds().x,kernel.getBounds().y), new Ponto(kernel.getBounds().x+kernel.getBounds().width,kernel.getBounds().y+kernel.getBounds().height) );
		grafo = grafo.clone();
		setKernel(kernel);
		setGrafo(grafo);
	}
	
	public TesteRotasImg(KernelMap kernel, GrafoRotas grafo, String dir){
		this(kernel, grafo);
		TesteRotasImg.dir = dir;
	}
	
	public void salvar(){
		try {
			paint(imagem.getGraphics());
			String datahora = new SimpleDateFormat("yy-MM-dd, HH-mm-ss").format(new Date()) + ", " + System.currentTimeMillis()%1000;
			String dir = getDirCenario();
			File file = new File(dir, datahora + ", " + titulo);
			ImageIO.write((RenderedImage)imagem, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getDirCenario(){
		String cenario = TesteCenariosRotas.getStringCenarioAtual();
		if(cenario == null){
			return dir;
		}else{
			String[] split = cenario.split("\t");
			String dirCenario = dir + "/" + split[0] + ", " + split[1];
			new File(dirCenario).mkdir();
			return dirCenario;
		}
	}
	
	public void setTitulo(String s){
		this.titulo = s;
	}
	
	public void setDir(String dir){
		TesteRotasImg.dir = dir;
	}
	
	public void setTituloTesteCenarios(String infoIteracao){
		setTitulo(TesteCenariosRotas.idCenarioAtual + ", " + infoIteracao);
	}
	
	public void setGrafo(GrafoRotas grafo){
		this.grafo = grafo;
	}
	
	public void addPonto(Ponto ponto, Color cor){
		pontos.add(ponto);
		coresPontos.add(cor);
	}
	
	public void addPontos(Collection<Ponto> pontos, Color cor) {
		for(Ponto p : pontos)
			addPonto(p, cor);
	}
	
	public void addRota(Rota caminho) {
		caminhos.add(caminho);
		coresCaminhos.add(cores[(caminhos.size()-1)%cores.length]);
	}
	
	public void addRota(Rota caminho, Color cor) {
		caminhos.add(caminho);
		coresCaminhos.add(cor);
	}
	
	public void addRotas(Collection<Rota> rotas, Color cor){
		for(Rota r : rotas)
			addRota(r, cor);
	}
	
	public void addSegmento(Segmento s, Color cor){
		segmentos.add(s);
		coresSegmentos.add(cor);
	}
	
	public void addRetangulo(Rectangle r, Color cor){
		Ponto p1 = new Ponto(r.x, r.y);
		Ponto p2 = new Ponto(r.x+r.width, r.y);
		Ponto p3 = new Ponto(r.x+r.width, r.y+r.height);
		Ponto p4 = new Ponto(r.x, r.y+r.height);
		addSegmento(new Segmento(p1, p2), cor);
		addSegmento(new Segmento(p2, p3), cor);
		addSegmento(new Segmento(p3, p4), cor);
		addSegmento(new Segmento(p4, p1), cor);
	}
	
	public void addPoligono(Poligono p, Color cor){
		List<Ponto> pts = p.getVertices();
		for(int i=1; i<pts.size(); i++)
			addSegmento(new Segmento(pts.get(i-1), pts.get(i)), cor);
		addSegmento(new Segmento(pts.get(pts.size()-1), pts.get(0)), cor);
	}
	
	public void addPoligonos(Collection<Poligono> polis, Color cor){
		for(Poligono poli : polis)
			addPoligono(poli, cor);
	}
	
	public void addGrafo(Grafo<Ponto> g, Color cor){
		for(Ponto p : g.getVertices())
			for(Ponto q : g.getVizinhos(p))
				addSegmento(new Segmento(p,q), cor);
	}
	
	public void setCores(Color... cores){
		this.cores = cores;
	}

	public void setKernel(KernelMap kernel) {
		this.kernel = kernel;
	}
	
	public void setLimites(Ponto... pontos){
		getLimites(Arrays.asList(pontos));
	}
	
	public void addLabel(String key, String texto, int x, int y, Color cor){
		labels.put(key, new Label(texto,x,y,cor));
	}
	
	public static Rectangle getLimites(Ponto... pontos){
		return getLimites(Arrays.asList(pontos));
	}
	
	public static Rectangle getLimites(List<Ponto> pontos){
		int menorX = Integer.MAX_VALUE;
		int menorY = Integer.MAX_VALUE;
		int maiorX = Integer.MIN_VALUE;
		int maiorY = Integer.MIN_VALUE;
		for(Ponto p: pontos){
			menorX = Math.min(menorX, (int)p.getX());
			menorY = Math.min(menorY, (int)p.getY());
			maiorX = Math.max(maiorX, (int)p.getX());
			maiorY = Math.max(maiorY, (int)p.getY());
		}
		int amplitudeX = maiorX - menorX;
		int amplitudeY = maiorY - menorY;
		int maiorAmplitude = Math.max(amplitudeX, amplitudeY); 
		int xMedio = (menorX + maiorX)/2;
		int yMedio = (menorY + maiorY)/2;
		Rectangle limites = new Rectangle();
		limites.width = limites.height = maiorAmplitude; //width e height iguais, pra ficar quadrado
		limites.x = menorX - maiorAmplitude/2 + (xMedio-menorX); //x medio fica centralizado
		limites.y = menorY - maiorAmplitude/2 + (yMedio-menorY); //y medio fica centralizado
		return limites;
	}
	
	public static void limpar(){
		limites = null;
	}

	public void paint(Graphics g) {
//		pegaLimites(rota1.getPontos());
		setRazao();
		RenderedImage imagem = (RenderedImage)this.imagem;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imagem.getWidth(), imagem.getHeight());
		
		pintaKernel(g, kernel);
		pintaGrafo(g, grafo);
		pintaSegmentos(g);
		pintaCaminhos(g);
		pintaPontos(g);
		pintaLabels(g, labels);
	}
	
	protected void pintaSegmentos(Graphics g){
		for(int i=0; i<segmentos.size(); i++){
			Segmento c = segmentos.get(i);
			Color cor = coresSegmentos.get(i);
			g.setColor(cor);
			pintaSegmento(g, c);
		}
	}
	
	protected void pintaSegmento(Graphics g, Segmento segm){
		if(segm!= null){
			Color cor = g.getColor();
				//desenha uma reta entre dois pontos
				g.setColor(cor);
				Ponto p = segm.getInicio();
				int x1 = xGMtoTeste(p.x);
				int y1 = yGMtoTeste(p.y);
				Ponto q = segm.getFim();
				int x2 = xGMtoTeste(q.x);
				int y2 = yGMtoTeste(q.y);
				g.drawLine(x1, y1, x2, y2);
		}
	}
	
	protected void pintaCaminhos(Graphics g){
		for(int i=0; i<caminhos.size(); i++){
			Rota c = caminhos.get(i);
			Color cor = coresCaminhos.get(i);
			g.setColor(cor);
			pintaCaminho(g, c);
		}
	}
	
	protected void pintaCaminho(Graphics g, Rota rota, boolean numerado){
		if(rota != null){
			List<Ponto> pontos = rota.getPontos();
			for(int i=0; i<pontos.size(); i++){
				
				//desenha um ponto
				Ponto p = pontos.get(i);
				int x = xGMtoTeste(p.x);
				int y = yGMtoTeste(p.y);
				g.fillOval(x-2, y-2, 5, 5);
				if(numerado)
					g.drawString(String.valueOf(i), x, y+15);
				
				//desenha uma reta entre dois pontos
				if(i != pontos.size()-1){
					Ponto p2 = pontos.get(i+1);
					int x2 = xGMtoTeste(p2.x);
					int y2 = yGMtoTeste(p2.y);
					g.drawLine(x, y, x2, y2);
				}
			}
		}
	}
	
	protected void pintaCaminho(Graphics g, Rota rota){
		if(rota!= null){
			Color cor = g.getColor();
			List<Ponto> pontos = rota.getPontos();
			for(int i=0; i<pontos.size(); i++){
				g.setColor(cor);
				//desenha um ponto
				Ponto p = pontos.get(i);
				int x = xGMtoTeste(p.x);
				int y = yGMtoTeste(p.y);
				g.fillOval(x-2, y-2, 5, 5);
				
				//desenha uma reta entre dois pontos
				if(i != pontos.size()-1){
					Ponto p2 = pontos.get(i+1);
					int x2 = xGMtoTeste(p2.x);
					int y2 = yGMtoTeste(p2.y);
					g.drawLine(x, y, x2, y2);
				}
			}
			//bota o perigo no meio da rota
//			g.setColor(Color.GREEN);
//			Ponto meio = Ponto.medio(rota.getInicio(), rota.getFim());
//			int xMeio = xGMtoTeste(meio.x);
//			int yMeio = yGMtoTeste(meio.y);
//			g.drawString(f.format(rota.getPerigo()),xMeio, yMeio);
//			g.setColor(Color.BLUE);
		}
	}
	
	protected void pintaGrafo(Graphics g, GrafoRotas grafo){
		if(grafo != null){
			
			//pinta vertices
			Map<Integer,Ponto> pontos = grafo.getPontos();
			for(int i : pontos.keySet()){
				g.setColor(Color.BLACK);
				Ponto p = pontos.get(i);
				/*DEBUG*/if(p == null) { System.out.println("***TesteRotasImg, pintaGrafo(): p=null, i="+i); continue;}
				pintaPonto(g, p, RAIO_VERTICE); 
				pintaString(g, String.valueOf(i), p, 5, 1);
			}
			
			//pinta rotas
			List<Rota> rotas = grafo.getRotas();
			g.setColor(Color.BLACK);
			for(Rota rota : rotas){
//					if(kernel != null)
//						g.setColor(new LogicaRotaSegura(kernel).isToleravel(rota)? Color.BLACK : Color.RED);
				pintaCaminho(g, rota);
			}
		}
	}
	
	protected void pintaPontos(Graphics g){
		for(int i=0; i<pontos.size(); i++){
			Ponto p = pontos.get(i);
			Color cor = coresPontos.get(i);
			g.setColor(cor);
			pintaPonto(g, p, RAIO_VERTICE);
		}
	}
	
	protected void pintaPonto(Graphics g, Ponto p, int raio){
		int x = xGMtoTeste((int)p.getX());
		int y = yGMtoTeste((int)p.getY());
		g.fillOval(x-raio, y-raio, raio*2, raio*2);
	}
	
	protected void pintaString(Graphics g, String s, Ponto p, int raio,int linha){
		int x = xGMtoTeste((int)p.getX());
		int y = yGMtoTeste((int)p.getY());
		g.drawString(s, x+raio, y+raio+10*linha);
	}
	
	protected void pintaString(Graphics g, String s, int posX, int posY, Color cor){
		g.setColor(cor);
		int x = xGMtoTeste(posX);
		int y = yGMtoTeste(posY);
		g.drawString(s, x, y);
	}
	
	protected void pintaLabels(Graphics g, Map<String,Label> labels){
		for(Label label : labels.values())
			pintaString(g, label.texto, label.posicao.x, label.posicao.y, label.cor);
	}
	
	protected void pintaKernel(Graphics g, KernelMap kernel){
		if(kernel != null){
			Suavizador r = new Suavizador(kernel);
			CellBasedRenderer scheme = new TransparentToColor(kernel, Color.RED);
			Image img = r.pintaKernel(scheme);
			Rectangle kBounds = kernel.getBounds();
			int dx1 = xGMtoTeste(limites.x) - BORDA;
			int dy1 = yGMtoTeste(limites.y) - BORDA;
			int dx2 = xGMtoTeste(limites.x + limites.width) + BORDA;
			int dy2 = yGMtoTeste(limites.y + limites.height) + BORDA;
			int sx1 = xTesteToGM(xGMtoTeste(limites.x) - BORDA) - kBounds.x;
			int sy1 = yTesteToGM(yGMtoTeste(limites.y) - BORDA) - kBounds.y;
			int sx2 = xTesteToGM(xGMtoTeste(limites.x + limites.width) + BORDA) - kBounds.x;
			int sy2 = yTesteToGM(yGMtoTeste(limites.y + limites.height) + BORDA) - kBounds.y;
			g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
		}
	}
	
	protected void setRazao(){
		RenderedImage imagem = (RenderedImage)this.imagem;
		if(limites.width>limites.height)
			razao = (imagem.getWidth() - BORDA*2.0)/limites.width;
		else
			razao = (imagem.getHeight() - BORDA*2.0)/limites.height;
	}
	
	protected int xGMtoTeste(int x){
		return (int)((x - limites.x) * razao) + BORDA;
	}
	
	protected int yGMtoTeste(int y){
		return (int)((y - limites.y) * razao) + BORDA;
	}
	
	protected int xTesteToGM(int x){
		return (int)Math.round((x - BORDA)/razao + limites.x); 
	}
	
	protected int yTesteToGM(int y){
		return (int)Math.round((y - BORDA)/razao + limites.y);
	}
		

	class Label{
		String texto;
		Ponto posicao;
		Color cor;
		Label(String texto, int x, int y, Color cor){
			this.posicao = new Ponto(x,y);
			this.texto = texto;
			this.cor = cor;
		}
		@Override
		public String toString() {
			return texto;
		}
	}
	
	public static void teste(Rota rota, String titulo, LogicaRotaSegura logicaRota, Object... labels) {
		TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), logicaRota.getGrafo());
		teste.setTitulo(titulo);
		teste.addRota(rota, Color.BLUE);
		teste.addLabels(labels);
		teste.salvar();
	}
	
	public static void teste(Collection<Rota> rotas, String titulo, LogicaRotaSegura logicaRota, Object... labels) {
		TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), logicaRota.getGrafo());
		teste.setTitulo(titulo);
		for(Rota rota : rotas) {
			teste.addRota(rota, Color.BLUE);
		}
		teste.addLabels(labels);
		teste.salvar();
	}
	
	public static void teste(Queue<Rota> rotas, String titulo, LogicaRotaSegura logicaRota, Object... labels) {
		TesteRotasImg teste = new TesteRotasImg(logicaRota.getKernel(), logicaRota.getGrafo());
		teste.setTitulo(titulo);
		int size = rotas.size();
		Rota[] temp = rotas.toArray(new Rota[size]);
		for(int i=size-1; i>=0; i--) {
			float rg = (float)i/size; 
			Color azul = new Color(rg,rg,1);
			teste.addRota(temp[i], azul);
		}
		teste.addLabels(labels);
		teste.salvar();
	}
	
	private void addLabels(Object[] labels) {
		for(int i=0; i<labels.length; i++) {
			addLabel(""+i, ""+labels[i], limites.x + i*20, limites.y + 20, Color.BLACK);
		}
	}
	
}