package org.wikicrimes.util.rotaSegura.testes;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Retangulo;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.logica.modelo.ArestaRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.GrafoRotas;
import org.wikicrimes.util.rotaSegura.logica.modelo.VerticeRotas;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class TesteGrafoJung {

	JFrame frame;
	VisualizationViewer<Ponto, Rota> viewer;
	Layout<Ponto,Rota> layout;
	Graph<Ponto, Rota> grafoJung;
	GrafoRotas grafoRotas;
	
	Map<Ponto,String> pontosComLabel; 
	
	public TesteGrafoJung(GrafoRotas g){
		grafoRotas = g.clone();
		pontosComLabel = new HashMap<Ponto, String>();
		pontosComLabel.put(grafoRotas.getOrigem(), "origem");
		pontosComLabel.put(grafoRotas.getDestino(), "destino");
		montar();
		posicionarGrafo();
	}
	
	public void addLabel(Ponto vertice, String label){
		pontosComLabel.put(vertice, label);
	}
	
	public void setTitulo(String titulo){
		frame.setTitle(titulo);
	}
	
	private void montar(){
		grafoJung = new DirectedSparseMultigraph<Ponto, Rota>(); 
		layout = new StaticLayout<Ponto, Rota>(grafoJung);
		viewer = new VisualizationViewer<Ponto, Rota>(layout, new Dimension(800, 600));
		
		viewer.getRenderContext().setVertexLabelTransformer(new VertexLabelTransformer());

		DefaultModalGraphMouse<Ponto, Rota> maxixe = new DefaultModalGraphMouse<Ponto, Rota>();
		maxixe.setMode(Mode.PICKING);
		viewer.setGraphMouse(maxixe);

		frame = new JFrame();
		frame.add(viewer);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	private void posicionarGrafo(){
		for(Ponto p : grafoRotas.getPontos().values()){
			grafoJung.addVertex(p);
			layout.setLocation(p, normaliza(p, grafoRotas, viewer));
			VerticeRotas v = grafoRotas.getVerticeRotas(p);
			for(ArestaRotas a : v.getArestasSaindo()){
				Ponto p1 = a.getAntecessor().getPonto();
				Ponto p2 = a.getSucessor().getPonto();
				grafoJung.addEdge(a.getRota(), p1, p2);
			}
		}
	}
	
	private Point normaliza(Ponto p, GrafoRotas g, VisualizationViewer<Ponto, Rota> v){
		Rectangle boundsGrafo = g.getBounds();
		boundsGrafo = Retangulo.expandir(boundsGrafo, boundsGrafo.width/2);
		Rectangle boundsViewer = viewer.getBounds();
		double rx = boundsViewer.getWidth()/boundsGrafo.getWidth();
		double ry = boundsViewer.getHeight()/boundsGrafo.getHeight();
		double dx = boundsViewer.getX() - boundsGrafo.getX();
		double dy = boundsViewer.getY() - boundsGrafo.getY();
		int x = (int)((p.x + dx)*rx);
		int y = (int)((p.y + dy)*ry);
//		int x = (int)(p.x*rx + dx);
//		int y = (int)(p.y*ry + dy);
		return new Point(x,y);
	}

	class VertexLabelTransformer implements Transformer<Ponto, String>{
		@Override
		public String transform(Ponto p) {
			return pontosComLabel.get(p);
		}
	}
	
	public static void main(String[] args) {
		Ponto o = new Ponto(2,10);
		Ponto d = new Ponto(20,12);
		Rota rota = new Rota(o, new Ponto(5,5), new Ponto(6,6), d);
		GrafoRotas grafoChato = new GrafoRotas(rota);
		grafoChato.inserirCaminho(rota, 10, 10);
		
		Ponto p1 = new Ponto(7,7);
		grafoChato.inserirCaminho(new Rota(o,p1), 5, 5);
		grafoChato.inserirCaminho(new Rota(p1,d), 7, 7);
		
		TesteGrafoJung teste = new TesteGrafoJung(grafoChato);
		teste.addLabel(p1, "P");
	}
}

