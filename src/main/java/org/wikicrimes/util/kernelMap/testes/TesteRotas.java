package org.wikicrimes.util.kernelMap.testes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.wikicrimes.util.kernelMap.GrafoRotas;
import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.Caminho;
import org.wikicrimes.util.kernelMap.RotaGM;

/**
 * Representa rotas graficamente, para testes. 
 * 
 * @author victor
 */
public class TesteRotas {

	static final boolean enabled = false;
	static Rectangle LIMITES_GERAL; //guarda os limites(bounds) da primeira rota, pra aplicar os mesmos limites na segunda
	public static double tolerancia, distReta;

//	public static void main(String[] args) {
//		Ponto p1 = new Ponto(1,1);
//		Ponto p2 = new Ponto(5,5);
//		Rota r1 = new Rota(p1, p2);
//		setLegendaGeral(0.001234, 20);
//		mostra(r1);
//	}
	
	public static void mostra(Caminho... rotas){
		mostra("", rotas);
	}
	
	public static void mostra(GrafoRotas grafo){
		mostra("", grafo);
	}

	public static void mostra(KernelMap kernel, GrafoRotas grafo){
		mostra("", kernel, grafo);
	}
	
	public static void mostra(String titulo, Caminho... rotas){
		if(!enabled)return;
		rotas = rotas.clone();
		PainelTeste painel = constroi(titulo, rotas[0].getPontosArray());
		painel.setRotas(rotas);
		painel.repaint();
	}
		
	public static void mostra(String titulo, GrafoRotas grafo){
		if(!enabled)return;
		grafo = (GrafoRotas)grafo.clone();
		PainelTeste painel = constroi(titulo, grafo.getOrigem(), grafo.getDestino());
		painel.setGrafo(grafo);
		painel.repaint();
	}
	
	public static void mostra(String titulo, KernelMap kernel, GrafoRotas grafo){
		if(!enabled)return;
		grafo = (GrafoRotas)grafo.clone();
		PainelTeste painel = constroi(titulo, grafo.getOrigem(), grafo.getDestino());
		painel.setKernel(kernel);
		painel.setGrafo(grafo);
		painel.repaint();
	}
	
	public static void mostra(String titulo, KernelMap kernel, Caminho... rotas){
		if(!enabled)return;
		PainelTeste painel = constroi(titulo, rotas[0].getPontosArray());
		painel.setKernel(kernel);
		painel.setRotas(rotas);
		painel.repaint();
	}
	
	protected static PainelTeste constroi(String titulo, Ponto... pontosProsLimites){
		List<Ponto> pontos = Arrays.asList(pontosProsLimites);
		PainelTeste painel = new PainelTeste();
		painel.setPreferredSize(new Dimension(620,620));
		if(LIMITES_GERAL == null){
			painel.setLimites(pontos);
			LIMITES_GERAL = painel.limites;
		}else{
			painel.limites = LIMITES_GERAL;
		}
		
		JFrame frame = new JFrame();
		frame.setTitle(titulo);
		frame.add(painel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		return painel;
	}
	
	//seta o limite geral com o valor do limite que enquadra bem o grafo g 
	public static void setLimitesGeral(GrafoRotas g){
		if(g == null){
			LIMITES_GERAL = null;
		}else{	
			PainelTeste p = new PainelTeste();
			p.setLimites(new Caminho(g.getOrigem(),g.getDestino()).getPontos());
			LIMITES_GERAL = p.limites;
		}
	}
	
	public static void setLimitesGeral(Rectangle bounds){
		if(bounds == null){
			LIMITES_GERAL = null;
		}else{	
			PainelTeste p = new PainelTeste();
			p.setLimites(new Ponto(bounds.x, bounds.y), new Ponto(bounds.x+bounds.width, bounds.y+bounds.height));
			LIMITES_GERAL = p.limites;
		}
	}
	
	public static void setLegendaGeral(double tol, double reta){
		tolerancia = tol;
		distReta = reta;
	}
	
	public static void limpar(){
		setLimitesGeral((GrafoRotas)null);
		setLegendaGeral(0, 0);
	}
	
}

class PainelTeste extends JPanel{
	
	private Color[] cores = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.PINK};
	private Ponto[] pontos;
	private Caminho[] rotas;
	private GrafoRotas grafo;
	private KernelMap kernel;
	
	public Rectangle limites;
	private final int BORDA=150;
	private double razao;
	private int raioVertices = 5;
	
	public static NumberFormat f = NumberFormat.getNumberInstance();

	public PainelTeste() {
		super();
//		setLayout(null);
		addMouseListener(new TesteMouseListener());
	}
	
	public void setGrafo(GrafoRotas grafo){
		this.grafo = grafo;
//		for(Ponto p : grafo.getPontos().values())
//			add(new Vertice(p));
	}
	
	public void setPontos(Ponto... pontos){
		this.pontos = pontos;
	}
	
	public void setRotas(Caminho... rotas) {
		this.rotas = rotas;
//		for(Rota r : rotas)
//			for(Ponto p : r.getPontos())
//				add(new Vertice(p));
	}
	
	public void setCores(Color... cores){
		this.cores = cores;
	}

	public void setKernel(KernelMap kernel) {
		this.kernel = kernel;
	}

	@Override
	public void paint(Graphics g) {
//		pegaLimites(rota1.getPontos());
		setRazao();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		pintaKernel(g, kernel);

		pintaRotas(g, rotas);
		
		pintaGrafo(g, grafo);
		
		pintaString(g, "Tolerancia: "+ f.format( TesteRotas.tolerancia ), 5, 15, Color.BLUE);
		pintaString(g, "Reta OD: "+ f.format( TesteRotas.distReta ), 5, 30, Color.BLUE);
	
		
//		for(Component c : getComponents()){
//			c.paint(g);
//		}
	}
	
	protected void pintaRotas(Graphics g, Caminho... rotas){
		if(rotas!= null){
			int i = 0;
			for(Caminho r : rotas){
				g.setColor(cores[i]);
				pintaRota(g, r, true);
				i++;
			}
		}
	}
	
	protected void pintaRota(Graphics g, Caminho rota, boolean numerado){
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
	
	protected void pintaRota(Graphics g, RotaGM rota){
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
				g.setColor(Color.BLUE);
				Ponto p = pontos.get(i);
				pintaPonto(g, p, raioVertices); 
				g.setColor(Color.ORANGE);
				pintaString(g, String.valueOf(i), p, 5, 1);
				double desfav = grafo.desfav(p);
				g.setColor(Color.BLUE);
				pintaString(g, f.format(desfav), p, 5, 2);
			}
			
			//pinta rotas
			List<RotaGM> rotas = grafo.getRotasGM();
			g.setColor(Color.BLUE);
			for(RotaGM rota : rotas){
				pintaRota(g, rota);
			}
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
		g.drawString(s, posX, posY);
	}
	
	protected void pintaKernel(Graphics g, KernelMap kernel){
		if(kernel != null){
			KernelMapRenderer r = new KernelMapRenderer(kernel);
			Image img = r.pintaKernel();
			Rectangle kBounds = kernel.getBounds();
			int dx1 = xGMtoTeste(limites.x) - BORDA;
			int dy1 = yGMtoTeste(limites.y) - BORDA;
			int dx2 = xGMtoTeste(limites.x + limites.width) + BORDA;
			int dy2 = yGMtoTeste(limites.y + limites.height) + BORDA;
			int sx1 = (int)xTesteToGM(xGMtoTeste(limites.x) - BORDA) - kBounds.x;
			int sy1 = (int)yTesteToGM(yGMtoTeste(limites.y) - BORDA) - kBounds.y;
			int sx2 = (int)xTesteToGM(xGMtoTeste(limites.x + limites.width) + BORDA) - kBounds.x;
			int sy2 = (int)yTesteToGM(yGMtoTeste(limites.y + limites.height) + BORDA) - kBounds.y;
			g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
		}
	}
	
	public void setLimites(Ponto... pontos){
		setLimites(Arrays.asList(pontos));
	}
	
	public void setLimites(List<Ponto> pontos){
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
		limites = new Rectangle();
		limites.width = limites.height = maiorAmplitude; //width e height iguais, pra ficar quadrado
		limites.x = menorX - maiorAmplitude/2 + (xMedio-menorX); //x medio fica centralizado
		limites.y = menorY - maiorAmplitude/2 + (yMedio-menorY); //y medio fica centralizado
	}
	
	protected void setRazao(){
		if(limites.width>limites.height)
			razao = (this.getWidth() - BORDA*2.0)/limites.width;
		else
			razao = (this.getHeight() - BORDA*2.0)/limites.height;
	}
	
	protected int xGMtoTeste(int x){
		return (int)((x - limites.x) * razao) + BORDA;
	}
	
	protected int yGMtoTeste(int y){
		return (int)((y - limites.y) * razao) + BORDA;
	}
	
	protected double xTesteToGM(int x){
		return (x - BORDA)/razao + limites.x; 
	}
	
	protected double yTesteToGM(int y){
		return (y - BORDA)/razao + limites.y;
	}
	
	
//	private class Vertice extends JComponent{
//		int raio = 5;
//		Ponto p;
//		
//		public Vertice(Ponto p) {
//			super();
//			this.p = p;
//			int x = xGMtoTeste((int)p.getX());
//			int y = yGMtoTeste((int)p.getY());
//			setBounds(x-raio, y-raio, raio*2, raio*2);
//			setToolTipText("oi");
//			
//			addMouseListener(new MouseAdapter(){
//				@Override
//				public void mouseClicked(MouseEvent e) {
//					System.out.println("oi");
//				}
//			});
//		}
//
//		@Override
//		public void paintComponent(Graphics g) {
//			super.paintComponent(g);
//			g.setColor(Color.RED);
//			g.fillOval(getX()-raio, getY()-raio, raio*2, raio*2);
//		}
//	}
	
	class TesteMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			Point mouse = e.getPoint();
			int idVertice = 0;
			Map<Integer, Ponto> m = grafo.getPontos();
			for(int i : grafo.getPontos().keySet()){
				Ponto p = m.get(i);
				Rectangle r = new Rectangle(xGMtoTeste(p.x)-raioVertices, yGMtoTeste(p.y)-raioVertices, raioVertices*2, raioVertices*2);
				if(r.contains(mouse)){
					idVertice = i;
					break;
				}
			}
			
			if(idVertice != 0){
				System.out.println( grafo.getDetalhes(m.get(idVertice)) );
			}
		}
	}
}