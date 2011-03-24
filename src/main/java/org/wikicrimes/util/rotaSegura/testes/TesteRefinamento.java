package org.wikicrimes.util.rotaSegura.testes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.logica.Refinamento;

public class TesteRefinamento extends TesteRotasFrame{
	
	public static void main(String[] args) {
		TesteRefinamento teste = new TesteRefinamento();
		PainelTesteRefinamento painel = constroi(teste);
		teste.addRota(painel.rota, Color.BLUE);
		teste.addRota(painel.vertices, Color.BLACK);
		painel.repaint();
		painel.requestFocus();
	}
	
	public TesteRefinamento(){
		super(new Ponto(0,0), new Ponto(1000,1000));
	}
	
	protected static PainelTesteRefinamento constroi(TesteRefinamento teste){
		PainelTesteRefinamento painel = teste.new PainelTesteRefinamento();
		painel.setPreferredSize(new Dimension(620,620));
		
		JFrame frame = new JFrame();
		frame.add(painel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		return painel;
	}

	@SuppressWarnings("serial")
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
					vertices.addFim(new Ponto(x, y));
				}else if(e.getButton() == MouseEvent.BUTTON1){
					rota.addFim(new Ponto(x, y));
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
					Refinamento.refinaRota(vertices, rota);
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
}
