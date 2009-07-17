package org.wikicrimes.util.kernelMap;


/**
 * 
 * @author mairon
 *
 */
public class JFrameProgressBar {/*extends JFrame{
	
	private JProgressBar progressBar;
	
	private JPanel panelAux;
	
	public JFrameProgressBar(String titulo) {
		
		
		Container container = this.getContentPane();
		container.setLayout(new GridBagLayout());
		
		panelAux = new JPanel(new GridLayout());
		container.add(panelAux);
		
		JPanel panelAux2 = new JPanel(new GridLayout());
		panelAux.add(panelAux2);
		panelAux2.setPreferredSize(new Dimension(350,22));
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		panelAux2.add(progressBar, new GridBagConstraints(0, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		

		this.setTitle(titulo);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 105);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setEnabled(false);
		this.setVisible(true);
			
		
		//progressBar.setValue(12);
//		mudaLookAndFeel(this);	
		
	}
	
	private void mudaLookAndFeel(JFrame frame ){
         LookAndFeel - Windows 
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void alteraPorcentagem(int valor){
		progressBar.setValue(valor);
	}
	
	public void alteraTexto(String texto){
		panelAux.setBorder(BorderFactory.createTitledBorder(texto));
//		SwingUtilities.updateComponentTreeUI(panelAux);
	}
	
	public void terminado(){
		this.dispose();
	}
	
	
	
	
	public static void main(String args[]) {
		
		JFrameProgressBar progressBar = new JFrameProgressBar("Progresso");
		
		progressBar.alteraTexto("Oi");
		progressBar.alteraPorcentagem(100);
		
//	    JFrame f = new JFrame("JProgressBar Sample");
//	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    Container content = f.getContentPane();
//	    
//	    
//	    JProgressBar progressBar = new JProgressBar();
//	    progressBar.setValue(25);
//	    progressBar.setStringPainted(true);
//	    Border border = BorderFactory.createTitledBorder("Reading...");
//	    progressBar.setBorder(border);
//	    
//	    content.add(progressBar, BorderLayout.NORTH);
//	    f.setSize(300, 100);
//	    f.setVisible(true);
//	    
//	    try {
//	    	int valor = 10;
//	    	
//	    	while(valor != 101){
//		    	Thread.sleep(100);
//				progressBar.setValue(valor++);
//	    	}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	    
	    
	  }*/
	}	