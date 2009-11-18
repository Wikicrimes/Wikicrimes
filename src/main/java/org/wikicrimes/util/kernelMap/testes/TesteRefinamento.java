package org.wikicrimes.util.kernelMap.testes;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import org.wikicrimes.util.kernelMap.LogicaRotaSegura;
import org.wikicrimes.util.kernelMap.Ponto;
import org.wikicrimes.util.kernelMap.Rota;


public class TesteRefinamento extends TesteRotas{
	
	public static void main(String[] args) {
		mostra("");
	}
	
	public static void mostra(String titulo){
		PainelTesteRefinamento painel = constroi(titulo);
		painel.setRotas(painel.rota, painel.vertices);
		painel.repaint();
		painel.requestFocus();
	}
	
	protected static PainelTesteRefinamento constroi(String titulo){
		PainelTesteRefinamento painel = new PainelTesteRefinamento();
		painel.setPreferredSize(new Dimension(620,620));
		if(TesteRotas.LIMITES_GERAL == null){
			painel.setLimites(new Ponto(0,0), new Ponto(1000,1000));
			TesteRotas.LIMITES_GERAL = painel.limites;
		}else{
			painel.limites = TesteRotas.LIMITES_GERAL;
		}
		
		JFrame frame = new JFrame();
		frame.setTitle(titulo);
		frame.add(painel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		return painel;
	}
}

class PainelTesteRefinamento extends PainelTeste {

	Rota rota;
	Rota vertices;
	
	public PainelTesteRefinamento(){
		rota = new Rota();
		vertices = new Rota();
		removeMouseListener(getMouseListeners()[0]); //tira o TesteVerticeListener
		addMouseListener(new TesteRefMouseListener());
		addKeyListener(new TesteRefKeyListener());
	}
	
	private class TesteRefMouseListener extends MouseAdapter{
		
		@Override
		public void mousePressed(MouseEvent e) {
			Point mouse = e.getPoint();
			int x = (int)xTesteToGM(mouse.x);
			int y = (int)yTesteToGM(mouse.y); 
			
			if(e.getButton() == MouseEvent.BUTTON3){
				vertices.add(new Ponto(x, y));
			}else if(e.getButton() == MouseEvent.BUTTON1){
				rota.add(new Ponto(x, y));
			}else{
				
			}
			
			repaint();
		}
		
	}
	
	private class TesteRefKeyListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_ENTER:
				LogicaRotaSegura.refinaRota(vertices, rota);
				break;
			case KeyEvent.VK_DELETE:
				vertices.limpar();
				rota.limpar();
				break;
			}
			repaint();
		}
	}
	
}
