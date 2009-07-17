package org.wikicrimes.util.kernelMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mairon
 *
 */
public class MyMapKernel {
	
	//public ArrayList <Celula> arrayListCellWithEstimativas;
	private ArrayList <Celula> arrayListCelulaEstimacaoNaoZero = new ArrayList<Celula>();	
	
	//Matriz de todos as celulas
	public Celula [][] matrizCelulas;
	private Util util ;
	
	
	private long inicio;
	private long fim;
	private PainelKernel painelKernel;
	
	private boolean imprime = false;
	
	private ArrayList<SegmentoReta> arrayListRotaOriginal = new ArrayList<SegmentoReta>();
	private ArrayList<Hotspot> arrayListHotspot = new ArrayList<Hotspot>();
	private ArrayList<Coordenada> arrayListRotaSegura = new ArrayList<Coordenada>();	
	private ArrayList<Coordenada> arrayListPontosForaRota = new ArrayList<Coordenada>();
	
	
//	int xMin = 553610;  //Real 	Simpol
//	int yMin = 9586252; //Real	Simpol
	
	//Simpol
//	private int xMin = 553110; //Simpol
//	private int yMin = 9585252;	//Simpol
	
	//Wikicrimes
	private int xMin = 0;
	private int yMin = 0;
	
	//Wikicrimes
//	private int xMax = Util.tamXMatriz * Util.tamCelulaPixel;
//	private int yMax = Util.tamYMatriz * Util.tamCelulaPixel;

	private int xMax;
	private int yMax;

	
	//Wikicrimes
	private double longitudeMin;
	private double longitudeMax;
	
	//Wikicrimes
	private double latitudeMin;
	private double latitudeMax;
	
	public boolean carregouKernel = false;
	private Dados dados;
	
//	int xMin = 0;
	
//	int yMin = 0;
	
	//SIMPOL (NÃO IMPLEMENTADO)
//	int xMax = 556955;
//	int yMax = 9587965;
	
	//Conversao de um ponto x,y para longetude e latitude (escala)
	public static int incrementoCoordenadaX = 1;	
	public static int incrementoCoordenadaY = 1;
	
	private static int fimProgresso = -1;
	private static int progresso = 0;
	
	//Grafico
//	GraficoLinha graficoLinha;
	//Interface
	/*
	JFrame frameGrafico;
	*/
	public MyMapKernel(Util util){		
		this.util = util;
		matrizCelulas = new Celula[util.tamXMatriz][util.tamYMatriz];
		
		for (int i = 0 ; i < util.tamXMatriz ; i ++){
			for (int j = 0 ; j < util.tamYMatriz ; j ++){
//				matrizCelulas[i][j] = new Celula(new Color(232,232,232),i,j);
				matrizCelulas[i][j] = new Celula(Color.DARK_GRAY,i,j, matrizCelulas);
			}
		}
		
		//Cria o mapa de kernel
		painelKernel = new PainelKernel(this);
		dados = new Dados();
	}
	
	public void inicializaCalculo() {
		
		carregouKernel = false;

		//Coloca os paremetros do algoritmo
		painelKernel.setLarguraBanda(Util.larguraBanda);
		painelKernel.setNumCamadasCor(Util.numCamadasCor);
		painelKernel.setTamX(util.tamXMatriz);
		painelKernel.setTamY(util.tamYMatriz);
		painelKernel.setXMin(xMin);
		painelKernel.setYMin(yMin);
		painelKernel.calculaXeY();
		painelKernel.calculaLimites();
		
		
		//Adiciona os eventos pontuais (dados) no mapa
		//adicionaPontos();
		adicionaPontosWiki();

		//Cacula as estimativas de cada celula e pinta o mapa de kernel
		//Mostra o Grafico da função Kernel
        pintaKernel(calculaEstimativas());
//		mostraGrafico(true);
        //37.47594794878128 / 37.40780092202727 / -122.08179473876953 / -122.20195770263672
        //-122.18770980834961 / 37.43520348658563

        
//        converteLongitudeParaPixel(-122.20195770263672, -122.08179473876953, -122.18770980834961);
//        converteLatitudeParaPixel(37.40780092202727, 37.47594794878128, 37.43520348658563);
        
//        converteLongitudeParaPixel(-122.18770980834961);
//        converteLatitudeParaPixel(37.43520348658563);
        
        //procuraHotSpots();
        
        //verificaSeRotaEstaEmHotSpot();
        
        //calculaRotaSegura();
        
        //buscaHeuristicaMelhorRota();
        
        carregouKernel = true;
	}	


	//Interface
	/*
	//Cria a Janala do programas
	private JFrame frame = new JFrame("Mapa de Kernel");
	
	public void mostrar() {
		
//		mudaLookAndFeel(frame);
		//Tamanho da Janela		
		frame.setSize(760, 550);
		
		/*
		//Interface
		frame.setContentPane(painelKernel);
		*/
		
		/*
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		frame.setLocationRelativeTo(null);		
		frame.setVisible(true);
		
		//Cria o Ajustador de cores
//		JFrame f = new ScrollBarMudaIntervaloCor(this);
//		f.show();
	}
	*/
	

	public void criaImage(Integer width, Integer heigth,String path, String fileName) {
		ImageMaker imageMaker = new ImageMaker(); 
		imageMaker.createImage(width, heigth);
		Graphics2D graphics2D = imageMaker.graphics2D;
		painelKernel.criaImage(graphics2D);
		imageMaker.saveImage(path + fileName);
	}
	
	public Point getTopLeft(){
		return new Point(matrizCelulas[0][0].getXEsq(), matrizCelulas[0][0].getYCima());
	}
	
	public Point getBottomRight(){
		return new Point(matrizCelulas[util.tamXMatriz - 1][util.tamYMatriz - 1].getXDir(), matrizCelulas[util.tamXMatriz - 1][util.tamYMatriz - 1].getYBaixo());
	}
	
	
	//Interface
	/*
	private void mudaLookAndFeel(JFrame frame ){
        //LookAndFeel - Windows
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	*/
	
	private void adicionaPontosWiki(){

		
		progresso = 0;
		fimProgresso = -1;
		//mostraAtualizaJProgressBar("Calculando estimação por kernel - célula ", "Criando o mapa de Kernel");		
		
		inicio = System.currentTimeMillis();
		
		if(imprime)
			System.err.print("Adiciona pontos do wikicrimes...");
		
		//Carrega a lista de ponto da aldeota
//		List<Point> listPontos = dados.getPontosSimpol();
//		List<Point> listPontos = dados.getPontosReais();
		List<Ponto> listPontos = dados.getPontosWiki();
		
		//fimProgresso = listPontos.size();
		
		//Pocura se os pontos do wikicrimes estao dentro do mapa e grava suas delimitacões em suas repectivas celulas
		for (Ponto ponto : listPontos){
			//progresso++;
			

			int posicaoClikX = (int) ponto.getLongitude();
			int posicaoClikY = (int) ponto.getLatitude();
			
			
			//Retura as duas últimas casas decimais
//			int posicaoClikX = (int)(ponto.getXd() * 0.01);
//			int posicaoClikY = (int)(ponto.getYd() * 0.01);
		
			label:{
				for (int x = 0; x < util.tamXMatriz; x ++){
					for (int y = 0; y < util.tamYMatriz; y ++){
						if(matrizCelulas[x][y].isThere(posicaoClikX, posicaoClikY)){
						//System.out.println(matrizCelulas[x][y].getPosicao());
							
							//Armazena o ponto na celula
							matrizCelulas[x][y].addPontos(posicaoClikX, posicaoClikY);
							//System.out.println("Célula localizada. hehehhe");
							
							//Calcula a posição em pixel 
//							posicaoClikX = (posicaoClikX - xMin) / incrementoCoordenadaX; 
//							posicaoClikY = (posicaoClikY - yMin) / incrementoCoordenadaY;
							
							/*if(imprime)
								System.out.println("posicaoClikX: " +posicaoClikX + " / posicaoClikY: " +posicaoClikY);
							
							//Adiciona na lista de pontos para ser mostrado no mapa
							if(imprime)
								Util.arrayListPontos.add(new Point(posicaoClikX, posicaoClikY));*/
							
							break label;
						}
					}
				}
			}
		}
		
		if(imprime)
			System.err.println(" pontos adicionados. \nPontos encontrados na Aldeota: " + Util.arrayListPontos.size());		

        fim = System.currentTimeMillis(); 
        
        if(imprime)
        	System.err.println("Tempo de adicionar pontos: " + ((double)(fim - inicio) / (double)1000)+" segundos.");
	
	}
	
	
	private void adicionaPontos() {
		
		progresso = 0;
		fimProgresso = -1;
		//Interface
		/*
		mostraAtualizaJProgressBar("Calculando estimação por kernel - célula ", "Criando o mapa de Kernel");		
		*/
		
		inicio = System.currentTimeMillis();
		
		if(imprime)
			System.err.print("Adiciona pontos da Aldeota...");
		
		//Carrega a lista de ponto da aldeota
//		List<Point> listPontos = dados.getPontosSimpol();
		List<Point> listPontos = dados.getPontosReais();
		
		//fimProgresso = listPontos.size();
		
		//Pocura se os pontos da aldeota estao dentro do mapa e grava suas delimitacões me suas repectivas celeulas
		for (Point point : listPontos){
			//progresso++;
			
			//Retura as duas últimas casas decimais
			int posicaoClikX = (int)(point.getX() * 0.01);
			int posicaoClikY = (int)(point.getY() * 0.01);
		
			label:{
				for (int x = 0; x < util.tamXMatriz; x ++){
					for (int y = 0; y < util.tamYMatriz; y ++){
						if(matrizCelulas[x][y].isThere(posicaoClikX, posicaoClikY)){
						//System.out.println(matrizCelulas[x][y].getPosicao());
							
							//Armazena o ponto na celula
							matrizCelulas[x][y].addPontos(posicaoClikX, posicaoClikY);
							//System.out.println("Célula localizada. hehehhe");

							
							//Calcula a posição em pixel 
							posicaoClikX = (posicaoClikX - xMin) / incrementoCoordenadaX; 
							posicaoClikY = (posicaoClikY - yMin) / incrementoCoordenadaY;
							//System.out.println("posicaoClikX: " +posicaoClikX);
							//System.out.println("posicaoClikY: " +posicaoClikY);
							
							//Adiciona na lista de pontos para ser mostrado no mapa
							Util.arrayListPontos.add(new Point(posicaoClikX, posicaoClikY ));
							
							break label;
						}
					}
				}
			}
		}
		
		if(imprime)
			System.err.println(" pontos adicionados. \nPontos encontrados na Aldeota: " + Util.arrayListPontos.size());		

        fim = System.currentTimeMillis(); 
        
        if(imprime)
        	System.err.println("Tempo de adicionar pontos: " + ((double)(fim - inicio) / (double)1000)+" segundos.");
	}
	
	//Interface
	/*
	private void mostraAtualizaJProgressBar(final String texto,final String tituloFrame) {
		Thread progressBar = new Thread(){
			public void run() {
				try {
					JFrameProgressBar progressBar = new JFrameProgressBar(tituloFrame);
					progressBar.alteraTexto(texto + progresso + " de " + fimProgresso);
					
//					System.out.println("progresso: " + progresso + "" + "fimProgresso: " + fimProgresso);
										
					while (progresso != fimProgresso){
//						System.err.println("Teste " + progresso);
						
						
						progressBar.alteraPorcentagem(progresso * 100 / fimProgresso);
						progressBar.alteraTexto(texto + progresso + " de " + fimProgresso);	
						Thread.sleep(10);
										
					}
					
//					System.out.println("progresso 2: " + progresso + "" + "fimProgresso: " + fimProgresso);
					
					fimProgresso = -1;
					progresso = 0;
					progressBar.terminado();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		};			
		
		progressBar.setPriority(Thread.MAX_PRIORITY);
		progressBar.start();
	}
	
	*/
	
	public void adicionaUmPonto(int posicaoX, int posicaoY){
	//Calcula a posição real 
		Point posicaoReal = getPosicaoReal(posicaoX, posicaoY,MyMapKernel.incrementoCoordenadaX, MyMapKernel.incrementoCoordenadaY);
		
		int posicaoClikX = (int) posicaoReal.getX() + xMin;
		int posicaoClikY = (int) posicaoReal.getY() + yMin;
				
//		posicaoClikX = (e.getX() + xMin);
//		posicaoClikY = (e.getY() + yMin);
		
		if(imprime)
			System.out.println("Posição do click: "+posicaoClikX + " - " + posicaoClikY);
		
		//Na hora de dezenhar, não precisa de subir o valor das coordenadas
//		arrayListPontos.add(new Point(posicaoClikX - xMin - (numCelulasX * 4 * pixelTamCelula), posicaoClikY - yMin - (numCelulasY * 4 * pixelTamCelula) ));
		
		Util.arrayListPontos.add(new Point(posicaoX, posicaoY ));
		
		if(imprime)
			System.out.print("Localiza a célula... ");
				
		//Verifica qual celula pertence ao ponto e adiciona a esta(s) células pinta de verde
		label:{
			for (int x = 0; x < util.tamXMatriz; x ++){
				for (int y = 0; y < util.tamYMatriz; y ++){
					if(matrizCelulas[x][y].isThere(posicaoClikX, posicaoClikY)){
						if(imprime)
							System.out.println(matrizCelulas[x][y].getPosicao());
						
						//Armazena o ponto na celula
						matrizCelulas[x][y].addPontos(posicaoClikX, posicaoClikY);
						if(imprime)
							System.out.println("Célula localizada. hehehhe");
						break label;
					}
				}
			}
		}
		
		//Cacula as estimativas de cada celula e pinta o mapa de kernel
		//Mostra o Grafico da função Kernel
		pintaKernel(calculaEstimativas());
		//mostraGrafico(true);
	}
	
	private ArrayList <Celula> calculaEstimativas(){
		
		inicio = System.currentTimeMillis();
        
		//Array de todos as estimativas
		ArrayList <Celula> arrayListCellWithEstimativas = new ArrayList(); 
		
		//Array de todos as celulas
		double [][] arrayEstimativas = new double[util.tamXMatriz][util.tamYMatriz];
		
		if(imprime)
			System.err.print("Calcula estimativas de cada célula... ");

		//Zera as estimativas
		for (int x = 0; x < util.tamXMatriz; x ++){
			for (int y = 0; y < util.tamYMatriz; y ++){
				matrizCelulas[x][y].setEstimativa(0);
			}
		}
		
		fimProgresso = util.tamXMatriz * util.tamYMatriz;
		
		//Calcula pontos vizinhos de cada celula do arrayCelulas e armazena a estimativa de cada
		for (int x = 0; x < util.tamXMatriz; x ++){
			for (int y = 0; y < util.tamYMatriz; y ++){
				progresso++;
				
				// (a)Armazena a esimativa de kernel da celula atual do laço até a úlima celula********
				calcularPontosVizinhos(matrizCelulas[x][y]);
				
				// (a)ArrayList de celulas com estimativas 
				arrayListCellWithEstimativas.add(matrizCelulas[x][y]);

				// (b)Array de estimaticas
//				arrayEstimativas[x][y] = matrizCelulas[x][y].getEstimativa();
				
			}
		}
		
//		 (b)Posicao dos visinhos sobre a celula do evento de click (OBS: Não deveria estar detro desse for pois so precisa ser executado uma vez)
//		calcularPontosVizinhos(encontraCelulaClicada(e.getX(),e.getY()));
		
		//Imprime o centro de uma célula que possui o evento clicado
//		encontraCelulaClicada(e.getX(),e.getY()).getThis();
		
//		for (int x = 0; x < tamX; x ++){
//			for (int y = 0; y < tamY; y ++){
//				
//			}			
//		}
		
		if(imprime)
			System.err.println("Estimativas calculadas.");
		
		//Para mostrar as estimativas de cada célula
		//mostraEstimativas(arrayEstimativas);
		
		
		fim = System.currentTimeMillis();
        
		if(imprime)
			System.err.println("Tempo para calcular estimativas: " + ((double)(fim - inicio) / (double)1000)+" seguntos.");
		
		//this.arrayListCellWithEstimativas = arrayListCellWithEstimativas; 
		
		return arrayListCellWithEstimativas;
		
	}
	
	private void mostraEstimativas(double [][] arrayEstimativas) {
		
		for (int x = 0; x < util.tamXMatriz; x ++){
			for (int y = 0; y < util.tamYMatriz; y ++){
				if(imprime)
					System.out.println("arrayEstimativas["+x+"]"+"["+y+"]: "+ arrayEstimativas[x][y]);
			}
		}
	}
	
	private Celula encontraCelulaClicada(int posicaoX, int posicaoY) {
		
		//Calcula a posição real 
		Point posicaoReal = getPosicaoReal(posicaoX, posicaoY,MyMapKernel.incrementoCoordenadaX,MyMapKernel.incrementoCoordenadaY);
		
		posicaoX = (int) posicaoReal.getX() + xMin;
		posicaoY = (int) posicaoReal.getY() + yMin;
		
		
		Celula celulaAnalise = null;
		
		//Verifica qual celula pertence ao ponto clicado e pinta de verde
		label:{
			for (int x = 0; x < util.tamXMatriz; x ++){
				for (int y = 0; y < util.tamYMatriz; y ++){
					if(matrizCelulas[x][y].isThere(posicaoX, posicaoY)){
						celulaAnalise = matrizCelulas[x][y].getThis();
						celulaAnalise.setCor(Color.black);
						break label;
					}
				}
			}
		}
		
		
		return celulaAnalise;
	}
	
	//Calcula pontos vizinhos e estimativa para um dado ponto no mapa
	private void calcularPontosVizinhos(Celula celulaAnalise) {//Chamar o centro somente uma vez**********
		
		double estimativaIntecidadeCelula = 0;
		Point pointCentroCelulaAnalise = celulaAnalise.getCentro();
		
		
		//Analiza estimativa da propria celula, a não ser que o raio seja de tamanho 0 (zero)
		estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaAnalise);
		
		//Numero de celuas a serem analizadas
		int numCelulas = (Util.larguraBanda / Util.tamCelulaPixel);
		
		//Calcula o vizinhos deste ponto respeitando o tamanho do raio
		//Procura todos os eventos visinhos acima desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaCima(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			}
		}
		
		//Procura todos os eventos visinhos abaixo desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaBaixo(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			}
		}

		//Procura todos os eventos visinhos a esquerda desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaEsq(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			}
		}

		//Procura todos os eventos visinhos a direita desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaDir(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			}
		}

		//Procura todos os eventos visinhos ao nordeste desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaNordeste(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
				
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaCima(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
				
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaDir(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
			}
		}

		//Procura todos os eventos visinhos ao noroeste desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaNoroeste(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaCima(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
				
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaEsq(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
			}
			
		}

		//Procura todos os eventos visinhos ao sudeste desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaSudeste(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaBaixo(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
				
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaDir(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
			}
		}

		//Procura todos os eventos visinhos ao sudoeste desta celula e incrementa a funcao Intensidade
		for (int i = 0; i < numCelulas; i ++){
			Celula celulaVisinha = celulaAnalise.getCelulaSudoeste(i + 1);			
			if(celulaVisinha != null){
				estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinha);
//				celulaVisinha.setCor(Color.green);
			
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaBaixo(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
				
				for (int j = 0; j < (numCelulas - (i + 1)); j ++){
					Celula celulaVisinhaTest = celulaVisinha.getCelulaEsq(j + 1);			
					if(celulaVisinhaTest != null){
						estimativaIntecidadeCelula += calculaEstimacaoKernel(pointCentroCelulaAnalise, celulaVisinhaTest);
//						celulaVisinhaTest.setCor(Color.green);
					}
				}
			
			}
		}
		
		//System.out.println("estimativaIntecidadeCelula: " + estimativaIntecidadeCelula);
		
		celulaAnalise.setEstimativa(estimativaIntecidadeCelula);
	}
	
	private double calculaEstimacaoKernel(Point pointCentroCelulaAnalise , Celula celulaQualquerVisinha){
		double distancia;
		double estimativaIntecidadeCelula = 0;
		Point [] arrayPontosNaCelula = celulaQualquerVisinha.getArrayPontos();
		
		//Verifica a distancia de todos os pontos desta celula qualquer visinha
		for(int i = 0; i < arrayPontosNaCelula.length; i++ ){
			distancia = getDistanciaEntrePontos(pointCentroCelulaAnalise, arrayPontosNaCelula[i]);
			
			//Verifica se distancia é menor ou igual ao raio
			if(distancia <= Util.larguraBanda * MyMapKernel.incrementoCoordenadaX){
				estimativaIntecidadeCelula += calculaFuncaoIntensiade(distancia);
			}
		}
		
		return estimativaIntecidadeCelula;
	}
	
	
	private double getDistanciaEntrePontos(Point pointCentroCelulaAnalise, Point pointQualquerCelulaVisinha){
		return Math.sqrt(Math.pow((pointQualquerCelulaVisinha.getX() - pointCentroCelulaAnalise.getX()), 2) + Math.pow((pointQualquerCelulaVisinha.getY() - pointCentroCelulaAnalise.getY()), 2));
	}
	
	private double calculaFuncaoIntensiade(double distanciaEntreCentroEVizinho){
		return (3 / (Math.PI * Math.pow(Util.larguraBanda  * MyMapKernel.incrementoCoordenadaX,2))) * (Math.pow((1 - ( Math.pow(distanciaEntreCentroEVizinho  ,2) / Math.pow(Util.larguraBanda * MyMapKernel.incrementoCoordenadaX,2) ) ) , 2));
	}
	
	public ArrayList <Celula> pintaKernel(ArrayList<Celula> arrayListCellWithEstimativas) {
		
		inicio = System.currentTimeMillis();
        
		if(imprime)
			System.err.print("Desenha o mapa... ");
		
		Object [] arrayCelulas = arrayListCellWithEstimativas.toArray();
		
		//Ordena pelo valor de estimação
//		InsertionSorterCelula.sort(arrayCelulas);
		
		QuickSorterCelula quickSorterCelula = new QuickSorterCelula();
		quickSorterCelula.sort(arrayCelulas);	
		
		//Imprime arrayCeluasOrdenadas para teste
//		for(int i = 0; i < arrayCelulas.length; i++){
//			System.out.println("Ordenado: "+((Celula)arrayCelulas[i]).getEstimativa());
//		}
		
		arrayListCelulaEstimacaoNaoZero.clear();
		 
		//Copia todas as celulas cuja estimação foi diferente de 0
		for (int i = 0; i < arrayCelulas.length; i++){
			double estimativa = ((Celula)arrayCelulas[i]).getEstimativa();
			if (estimativa != 0){
				arrayListCelulaEstimacaoNaoZero.add(((Celula)arrayCelulas[i]));
			}else{
//				((Celula)arrayCelulas[i]).setCor(new Color(232,232,232));
				((Celula)arrayCelulas[i]).setCor(Color.BLACK);
//				((Celula)arrayCelulas[i]).setCor(new Color(0, 0, 0, 0));
			}
		}
		
		//ordenado: indice 0: menor valor; size - 1: maior valor
		if(arrayListCelulaEstimacaoNaoZero.size() != 0){
			intervalo5 = arrayListCelulaEstimacaoNaoZero.get(arrayListCelulaEstimacaoNaoZero.size() - 1).getEstimativa();//Coloca o maior valor para o intervalo 5
		}else{
			intervalo5 = 0;			
		}
		
		
		
//		coresKernel_1();
//		coresKernel_2();
//		coresKernel_3();
//		coresKernel_4();
		coresKernel_5();
		
		/*
		//Interface
		//show the results of the click
		painelKernel.repaint();
		*/
		
		if(imprime)
			System.err.println("Mapa desenhado.");
		
		fim = System.currentTimeMillis();
        
		if(imprime)
			System.err.println("Tempo para desenhar o mapa: " + ((double)(fim - inicio) / (double)1000)+" seguntos.");
		
		
		return arrayListCelulaEstimacaoNaoZero;
	}
	
	private boolean geraGrafico = true;

	public Color getCor(int num){
		switch (num) {
		case 1:
			return corIntervalo1;
		case 2:
			return corIntervalo2;
		case 3:
			return corIntervalo3;
		case 4:
			return corIntervalo4;					
		case 5:
			return corIntervalo5;

		default:
			return null;
		}
		
	}

	public double getIntervalo(int num){
		switch (num) {
		case 1:
			return intervalo1;
		case 2:
			return intervalo2;
		case 3:
			return intervalo3;
		case 4:
			return intervalo4;					
		case 5:
			return intervalo5;

		default:
			return -10;
		}
		
	}
	
	public void setIntervalo(int num, double intervalo){
		switch (num) {
		case 1:
			intervalo1 = intervalo;
		case 2:
			intervalo2 = intervalo;
		case 3:
			intervalo3 = intervalo;
		case 4:
			intervalo4 = intervalo;	
		case 5:
			intervalo5 = intervalo;

		default:
			
		}
		
	}
	
	//Cores ordenadas por células cuja estimacao
	private void coresKernel_1(){
		
		int intevaloCor = 256 / Util.numCamadasCor;
		int numCelulasComEstimativaNaoZero = arrayListCelulaEstimacaoNaoZero.size();
		int intervaloEventos = (int) Math.ceil((double)numCelulasComEstimativaNaoZero / (double) Util.numCamadasCor);
		int cor = 0;
		
		for (int i = 0; i < numCelulasComEstimativaNaoZero; i++){
			if (i % intervaloEventos != 0){//Multiplos de Ex: 60 (300 eventos / 5) muda de cor
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color((intevaloCor*2*cor >= 255? 255 - (intevaloCor*cor - 100)  : 255),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor)));
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color(255,255 - intevaloCor*cor,255 - intevaloCor*cor));
			}else{
				if (cor < Util.numCamadasCor){
					cor++;	
				}
				
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color((intevaloCor*2*cor >= 255? 255 - (intevaloCor*cor - 100) : 255),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor)));
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color(255,255 - intevaloCor*cor,255 - intevaloCor*cor));
			}			
		}
	}
	
	
	public double intervalo1 = 0.00001;//Menor
	public double intervalo2 = 0.00005;
	public double intervalo3 = 0.00008;// Cresce
	public double intervalo4 = 0.000125;
	public double intervalo5 = 0;// Maior
	
	
//	public double intervalo1 = 0.000009;
//	public double intervalo2 = 0.00009;
//	public double intervalo3 = 0.001;
//	public double intervalo4 = 0.005;
//	public double intervalo5 = 0.0058;
	
	public static Color corIntervalo1 = new Color(255,240,240);
	public static Color corIntervalo2 = new Color(255,200,200);
	public static Color corIntervalo3 = new Color(255,150,150);
	public static Color corIntervalo4 = new Color(255,100,100);
	public static Color corIntervalo5 = new Color(255,0,0);
	
	//Cores por intervalos fixos
	private void coresKernel_2(){
		int intevaloCor = 255 / Util.numCamadasCor;
		int numCelulasComEstimativaNaoZero = arrayListCelulaEstimacaoNaoZero.size();
		int intervaloEventos = (int) Math.ceil((double)numCelulasComEstimativaNaoZero / (double) Util.numCamadasCor);
		int cor = 0;
		
		
//		0.00000028498886671959465
		
		for (int i = 0; i < numCelulasComEstimativaNaoZero; i++){
			if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() > 0 && 
					arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo1){                                                                                                  
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo1);
			}else{
				if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo2){//
					arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo2);
				}else{
					if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo3){
						arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo3);
					}else{
						if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo4){
							arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo4);
						}else{
							if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo5){
								arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo5);
							}
						}							
					}					
				}
			}
		}
	}
	
	
//	Cores ordenadas por valores de kernel
	private void coresKernel_5() {
		
		int numCelulasComEstimativaNaoZero = arrayListCelulaEstimacaoNaoZero.size();
		double maiorEstimacaoKernel = 0;
		double menorEstimacaoKernel = 0;
		
		if(arrayListCelulaEstimacaoNaoZero.size() != 0){
			maiorEstimacaoKernel = arrayListCelulaEstimacaoNaoZero.get(numCelulasComEstimativaNaoZero - 1).getEstimativa();
			menorEstimacaoKernel = arrayListCelulaEstimacaoNaoZero.get(0).getEstimativa();
		}

		double intervaloEventos2 = maiorEstimacaoKernel  / (double) Util.numCamadasCor;
		
		intervalo1 = menorEstimacaoKernel + intervaloEventos2;
		intervalo2 = menorEstimacaoKernel + (intervaloEventos2 * 2);
		intervalo3 = menorEstimacaoKernel + (intervaloEventos2 * 3);
		intervalo4 = menorEstimacaoKernel + (intervaloEventos2 * 4);
		
		
		
		for (int i = 0; i < numCelulasComEstimativaNaoZero; i++){
			if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() > 0 && 
					arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo1){                                                                                                  
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo1);
			}else{
				if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo2){//
					arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo2);
				}else{
					if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo3){
						arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo3);
					}else{
						if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo4){
							arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo4);
						}else{
							if (arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa() <= intervalo5){
								arrayListCelulaEstimacaoNaoZero.get(i).setCor(corIntervalo5);
							}
						}							
					}					
				}
			}
		}	
		
	}
	
	
	
	
	
	private void procuraHotSpots() {
//		ArrayList<Integer> numeroRegioesHotSpot = new ArrayList<Integer>();
		
		int numRegioesHotSpots = 0;
		
		for(Celula celula : arrayListCelulaEstimacaoNaoZero){
			
			if(celula.getCor().equals(corIntervalo5) && celula.getRegiaoCodigo() == 0){
				numRegioesHotSpots++;
				Hotspot hotspot = new Hotspot();
				identificaHotSpots(celula, numRegioesHotSpots, hotspot);
				hotspot.encontraVerticesTangentes();
				arrayListHotspot.add(hotspot);
			}
			
			
		}
		
		/*System.out.println("Numero de hotspots: " + numRegioesHotSpots);*/
		
		/*for (Hotspot hotspot : arrayListHotspot) {
			for (Celula celula : hotspot.getArrayListCelulas()) {
				System.out.println(celula.getRegiaoCodigo());
			}
		}*/
		
	}
	
	private void identificaHotSpots(Celula celula, int numRegioesHotSpots, Hotspot hotspot) {
		
		if(celula.getCor().equals(corIntervalo5) && celula.getRegiaoCodigo() == 0){
			celula.setRegiaoCodigo(numRegioesHotSpots);
			hotspot.adicionaCelula(celula);
			celula.setCor(Color.RED);			
			
			identificaHotSpots(celula.getCelulaEsq(1), numRegioesHotSpots, hotspot);
			identificaHotSpots(celula.getCelulaDir(1), numRegioesHotSpots, hotspot);
			identificaHotSpots(celula.getCelulaCima(1), numRegioesHotSpots, hotspot);
			identificaHotSpots(celula.getCelulaBaixo(1), numRegioesHotSpots, hotspot);
							
		}		
	}
	
	ArrayList<SegmentoReta> arrayListRotaSeguraAuxTotal = new ArrayList<SegmentoReta>();
	
	//Verifica se a cada subrota gerada passa por algum hotspot
	private void verificaSeRotaEstaEmHotSpot() {
		arrayListRotaSeguraAuxTotal = new ArrayList<SegmentoReta>();
		
		for (Hotspot hotspot : arrayListHotspot) {

			ArrayList<Celula> arrayListCelulas = hotspot.getArrayListCelulas();
				
			int ultimoIndex = arrayListRotaOriginal.size() - 1;
			for (int index = 0; index < arrayListRotaOriginal.size(); index++){
				
				SegmentoReta rotaSegura = arrayListRotaOriginal.get(index);
				
				
				/*System.out.println("Teste: " + rotaSegura.getInicioRotaPixelX() + ", " +
						rotaSegura.getInicioRotaPixelY() + " - " +
						rotaSegura.getFimRotaPixelX() + ", " +
						rotaSegura.getFimRotaPixelY());*/	
				
				boolean passaPorHotSpot = false;
				boolean inicioDentro = false;
				boolean fimDentro = false;
				
/*				boolean todoLadoEsq = false;
				boolean todoLadoDir = false;
				boolean todoLadoCima = false;
				boolean todoLadoBaixo = false;
*/				
				
				//Totas as subrotas internas (que cortam o hotspot) por cada hotspot
				ArrayList<SegmentoReta> arrayListRotaSeguraAux = new ArrayList<SegmentoReta>();
				
				for (Celula celula : arrayListCelulas) {
					int sul = celula.getYCima();
					int norte = celula.getYBaixo();
					int oeste = celula.getXEsq();
					int leste = celula.getXDir();
					
					rotaSegura.codificaPontosExtremos(norte, sul, oeste, leste);
					if(rotaSegura.isCasoTrivial()) continue;
					
//					double exX = (leste - oeste) / 2;
//					double exY = (norte - sul) / 2;
					
					//Testa o caso trivial de o segmento de reta estar fora da celula e nao
					//chegar a cruzar-la
/*					if(rotaSegura.getInicioRotaPixelY() <= oeste &&	rotaSegura.getFimRotaPixelY() <= oeste){
						todoLadoEsq = true;
					}
					if(rotaSegura.getInicioRotaPixelX() <= sul && rotaSegura.getFimRotaPixelX() <= sul){
						todoLadoBaixo = true;
					}
					if(rotaSegura.getInicioRotaPixelX() >= norte && rotaSegura.getFimRotaPixelX() >= norte){
						todoLadoCima = true;
					}
					if(rotaSegura.getInicioRotaPixelY() >= leste && rotaSegura.getFimRotaPixelY() >= leste){
						todoLadoDir = true;
					}
*/					
					
					//Verifica primeiro se a rota inicia ou termina dentro de um hotSpot
					if(index == 0){
						if(rotaSegura.getInicioRotaPixelY() >= oeste && rotaSegura.getInicioRotaPixelY() <= leste &&
								rotaSegura.getInicioRotaPixelX() >= sul && rotaSegura.getInicioRotaPixelX() <= norte){
							//celula.setCor(Color.DARK_GRAY);
							passaPorHotSpot = false;
							arrayListRotaSeguraAux = new ArrayList<SegmentoReta>();
							break;
						}
					} else if(index == ultimoIndex){
						if(rotaSegura.getFimRotaPixelY() >= oeste && rotaSegura.getFimRotaPixelY() <= leste &&
								rotaSegura.getFimRotaPixelX() >= sul && rotaSegura.getFimRotaPixelX() <= norte ){
							//celula.setCor(Color.DARK_GRAY);
							passaPorHotSpot = false;
							arrayListRotaSeguraAux = new ArrayList<SegmentoReta>();
							break;
						}
					}
					
					
					//Testa se extremos (inicio e fim) estão dentro do hotspot (considerando todas as janelas)
					if(rotaSegura.getInicioRotaPixelY() >= oeste && rotaSegura.getInicioRotaPixelY() <= leste &&
							rotaSegura.getInicioRotaPixelX() >= sul && rotaSegura.getInicioRotaPixelX() <= norte){
						inicioDentro = true;
					}
					
					if(rotaSegura.getFimRotaPixelY() >= oeste && rotaSegura.getFimRotaPixelY() <= leste &&
							rotaSegura.getFimRotaPixelX() >= sul && rotaSegura.getFimRotaPixelX() <= norte ){
						fimDentro = true;
					}
					
					//Testa
					if (inicioDentro && fimDentro){
						passaPorHotSpot = false;
						arrayListRotaSeguraAux = new ArrayList<SegmentoReta>();
						//celula.setCor(Color.PINK);
						break;
					}					
				
					if (!passaPorHotSpot && equacaoRetaX(rotaSegura.getInicioRotaPixelY(), rotaSegura.getInicioRotaPixelX(),
							rotaSegura.getFimRotaPixelY(), rotaSegura.getFimRotaPixelX(), oeste,
							sul,norte)) {
						//celula.setCor(Color.BLUE);
						passaPorHotSpot = true;
						arrayListRotaSeguraAux.add(rotaSegura);
						hotspot.adicionaRota(rotaSegura);
					}else if (!passaPorHotSpot && equacaoRetaX(rotaSegura.getInicioRotaPixelY(), rotaSegura.getInicioRotaPixelX(),
							rotaSegura.getFimRotaPixelY(), rotaSegura.getFimRotaPixelX(), leste,
							sul,norte)) {
						//celula.setCor(Color.GREEN);
						passaPorHotSpot = true;
						arrayListRotaSeguraAux.add(rotaSegura);
						hotspot.adicionaRota(rotaSegura);
					}else if (!passaPorHotSpot && equacaoRetaY(rotaSegura.getInicioRotaPixelY(), rotaSegura.getInicioRotaPixelX(),
							rotaSegura.getFimRotaPixelY(), rotaSegura.getFimRotaPixelX(), sul,
							oeste, leste)) {
						//celula.setCor(Color.BLACK);
						passaPorHotSpot = true;
						arrayListRotaSeguraAux.add(rotaSegura);
						hotspot.adicionaRota(rotaSegura);
					}else if (!passaPorHotSpot && equacaoRetaY(rotaSegura.getInicioRotaPixelY(), rotaSegura.getInicioRotaPixelX(),
							rotaSegura.getFimRotaPixelY(), rotaSegura.getFimRotaPixelX(), norte,
							oeste, leste)) {
						//celula.setCor(Color.CYAN);
						passaPorHotSpot = true;
						arrayListRotaSeguraAux.add(rotaSegura);
						hotspot.adicionaRota(rotaSegura);
					}
				}
				
				/*if(passaPorHotSpot) System.out.println("Passa por hotspot");
				for (SegmentoReta rotaSeguraAux : arrayListRotaSeguraAux){
					System.out.println(rotaSeguraAux.getInicioRotaPixelX() + ", " +
							rotaSeguraAux.getInicioRotaPixelY() + " - " +
							rotaSeguraAux.getFimRotaPixelX() + ", " +
							rotaSeguraAux.getFimRotaPixelY());	
				}
				if(passaPorHotSpot) System.out.println("fim");*/
				
				/*for (RotaSegura rotaSeguraAux : arrayListRotaSeguraAux){
					arrayListRotaSeguraAuxTotal.add(rotaSeguraAux);
				}*/
			}		
			
//			Coordenada coordenada = new Coordenada(1,1);
//			hotspot.adicionaRota(coordenada);
		}
		
		
		
		/*System.out.println("Número de pontos: " + arrayListRotaSeguraAuxTotal.size());*/
	}
	
	//(ya - yb)x + (xb - xa)y + (xa.yb - xb.ya) = 0
	private boolean equacaoRetaX(long xInicio, long yInicio, long xFim, long yFim,
			double exX,	long intervaloA, long intervaloB){
//		return ((yInicio - yFim)*exX + (xFim - xInicio)*y + (xInicio*yFim - xFim*yInicio)) == 0;
		double y = (((xFim*yInicio - xInicio*yFim ) - (yInicio - yFim)*exX) / (xFim - xInicio));
		return ((y >= intervaloA) && (y <= intervaloB)) ;
	}
	
	private boolean equacaoRetaY(long xInicio, long yInicio, long xFim, long yFim,
			double exY,	long intervaloA, long intervaloB){
//		double x = ((yInicio - yFim)*x + (xFim - xInicio)*exY + (xInicio*yFim - xFim*yInicio)) == 0;
		double x = (((xFim*yInicio - xInicio*yFim) - (xFim - xInicio)*exY) / (yInicio - yFim)) ;
		return ((x >= intervaloA) && (x <= intervaloB));
	}
	
	
	//(ya - yb)x + (xb - xa)y + (xa.yb - xb.ya) = 0
	private boolean equacaoRetaX_2(long xInicio, long yInicio, long xFim, long yFim,
			double exX,	long intervaloA, long intervaloB){
//		return ((yInicio - yFim)*exX + (xFim - xInicio)*y + (xInicio*yFim - xFim*yInicio)) == 0;
		double y = (((xFim*yInicio - xInicio*yFim ) - (yInicio - yFim)*exX) / (xFim - xInicio));
		/*System.out.println("y:" + y + " - " + ((y > intervaloA) && (y < intervaloB)));*/
		return ((y >= intervaloA) && (y <= intervaloB));
	}
	
	private boolean equacaoRetaY_2(long xInicio, long yInicio, long xFim, long yFim,
			double exY,	long intervaloA, long intervaloB){
//		double x = ((yInicio - yFim)*x + (xFim - xInicio)*exY + (xInicio*yFim - xFim*yInicio)) == 0;
		double x = (((xFim*yInicio - xInicio*yFim) - (xFim - xInicio)*exY) / (yInicio - yFim)) ;
		/*System.out.println("x: " + x + " - " + ((x > intervaloA) && (x < intervaloB)));*/
		return ((x >= intervaloA) && (x <= intervaloB));
	}
	
	
	public String getRequisicaoVerticesHotspot(){
		String marcadoresVerticesHotspot = "";
		marcadoresVerticesHotspot +=  getRequisicaoVerticesTangenciaHotspot();
		
		if (!marcadoresVerticesHotspot.equalsIgnoreCase("")){
			//marcadoresVerticesHotspot += "a" + getRequisicaoVerticesNaoTangenciaHotspot();
		}
		else{
			//marcadoresVerticesHotspot += getRequisicaoVerticesNaoTangenciaHotspot();
		}
		
		return marcadoresVerticesHotspot;
	}
	
	public String getRequisicaoVerticesEntraSaiHotSpot(){
		String marcadoresVerticesEntraSaiHotSpot = "";
		
		for(Hotspot hotspot : arrayListHotspot){
			ArrayList<SegmentoReta> arrayListRotaSegura = hotspot.getArrayListRotas();
			
			int ultimoElemento = arrayListRotaSegura.size() - 1;
			
			//Verifica se so tem um elemento no arraylist
			if (ultimoElemento == 0){
				SegmentoReta rotaSegura = arrayListRotaSegura.get(ultimoElemento);
				if (!marcadoresVerticesEntraSaiHotSpot.equals("")){
					marcadoresVerticesEntraSaiHotSpot += "a" + rotaSegura.getInicioRotaPixelX() + "," +
					rotaSegura.getInicioRotaPixelY() + "a" +
					rotaSegura.getFimRotaPixelX() + "," +
					rotaSegura.getFimRotaPixelY();
				}else{
					marcadoresVerticesEntraSaiHotSpot += rotaSegura.getInicioRotaPixelX() + "," +
					rotaSegura.getInicioRotaPixelY() + "a" +
					rotaSegura.getFimRotaPixelX() + "," +
					rotaSegura.getFimRotaPixelY();					
				}
				
				hotspot.getCoordenadasEntraSaiHotspot()[0] = new Coordenada(rotaSegura.getInicioRotaPixelX(),
						rotaSegura.getInicioRotaPixelY());
				hotspot.getCoordenadasEntraSaiHotspot()[1] = new Coordenada(rotaSegura.getFimRotaPixelX(),
						rotaSegura.getFimRotaPixelY());
				
			}else{
				for (int i = 0; i < arrayListRotaSegura.size(); i++){
					
					SegmentoReta rotaSegura = arrayListRotaSegura.get(i);
					
					if(i == 0){
						if (!marcadoresVerticesEntraSaiHotSpot.equals("")){
							marcadoresVerticesEntraSaiHotSpot += "a" +
								rotaSegura.getInicioRotaPixelX() + "," +
								rotaSegura.getInicioRotaPixelY();
						}else {
							marcadoresVerticesEntraSaiHotSpot +=
								rotaSegura.getInicioRotaPixelX() + "," +
								rotaSegura.getInicioRotaPixelY();						
						}
						
						hotspot.getCoordenadasEntraSaiHotspot()[0] = new Coordenada(rotaSegura.getInicioRotaPixelX(),
								rotaSegura.getInicioRotaPixelY());
						
							
					}else if(i == ultimoElemento){
						marcadoresVerticesEntraSaiHotSpot += "a" +
							rotaSegura.getFimRotaPixelX() + "," +
							rotaSegura.getFimRotaPixelY();
						
						hotspot.getCoordenadasEntraSaiHotspot()[1] = new Coordenada(rotaSegura.getFimRotaPixelX(),
								rotaSegura.getFimRotaPixelY());
					}else{
						//Adiciona na lista os pontos que vão ser removidos da rota -> são os pontos
						//dentro do hotspot
						arrayListPontosForaRota.add(new Coordenada(rotaSegura.getInicioRotaPixelX(),
								rotaSegura.getInicioRotaPixelY()));
						
						//Adiciona na lista os pontos que vão ser removidos da rota -> são os pontos
						//dentro do hotspot
						arrayListPontosForaRota.add(new Coordenada(rotaSegura.getFimRotaPixelX(),
								rotaSegura.getFimRotaPixelY()));
					}
				}
			}
		}
		
		return marcadoresVerticesEntraSaiHotSpot;
	}
	
	public String getRequisicaoVerticesTangenciaHotspot(){
		String marcadoresVerticesHotspot = "";
		
		for (Hotspot hotspot : arrayListHotspot){
			ArrayList<Coordenada> arrayListVerticesTangencia = hotspot.getArrayListVerticesTangenciaFolga();
			
			if (!marcadoresVerticesHotspot.equalsIgnoreCase(""))
				marcadoresVerticesHotspot += "a";
			
			int ultimoElemento = arrayListVerticesTangencia.size() - 1;
			for (int i = 0; i < arrayListVerticesTangencia.size(); i++){
				Coordenada coordenada = arrayListVerticesTangencia.get(i);
				
				if(i == ultimoElemento){
					marcadoresVerticesHotspot += 
					coordenada.getLatitudeYPixel() + "," +
					coordenada.getLongitudeXPixel();	
				}else{
					marcadoresVerticesHotspot += 
					coordenada.getLatitudeYPixel() + "," +
					coordenada.getLongitudeXPixel() + "a";
				}
			}
		}
		
		return marcadoresVerticesHotspot;
	}
	
	public String getRequisicaoVerticesNaoTangenciaHotspot(){

		String marcadoresVerticesHotspot = "";
		
		for (Hotspot hotspot : arrayListHotspot){
			ArrayList<Coordenada> arrayListVerticesTangencia = hotspot.getArrayListVerticesNaoTangencia();
			
			if (!marcadoresVerticesHotspot.equalsIgnoreCase(""))
				marcadoresVerticesHotspot += "a";
			
			int ultimoElemento = arrayListVerticesTangencia.size() - 1;
			for (int i = 0; i < arrayListVerticesTangencia.size(); i++){
				Coordenada coordenada = arrayListVerticesTangencia.get(i);
				
				if(i == ultimoElemento){
					marcadoresVerticesHotspot += 
					coordenada.getLatitudeYPixel() + "," +
					coordenada.getLongitudeXPixel();	
				}else{
					marcadoresVerticesHotspot += 
					coordenada.getLatitudeYPixel() + "," +
					coordenada.getLongitudeXPixel() + "a";
				}
			}
		}
		
		return marcadoresVerticesHotspot;
	
	}
	
	
	private void calculaRotaSegura() {
		ArrayList<Coordenada> arrayListCoordenadas = new ArrayList<Coordenada>();
		
		String verticesEntraSaiHotspot = getRequisicaoVerticesEntraSaiHotSpot();
		String coordenadas [] = verticesEntraSaiHotspot.split("a");
		
		
		for (String coordenada : coordenadas){
			if (!coordenada.equalsIgnoreCase("")){
				arrayListCoordenadas.add(new Coordenada(Integer.parseInt(coordenada.split(",")[0]),
						Integer.parseInt(coordenada.split(",")[1])));	
			}
			
		}
		
		
		int ultmaIteracao = arrayListRotaOriginal.size() - 1;
		
		for (int i = 0; i < arrayListRotaOriginal.size(); i++){
			
			SegmentoReta segmentoReta = arrayListRotaOriginal.get(i);
			boolean encontrou = false;
			boolean encontrouUltimo = false;
			
			for (Coordenada coordenada : arrayListPontosForaRota){
				
				if(coordenada.getLongitudeXPixel() == segmentoReta.getInicioRotaPixelX() &&
						coordenada.getLatitudeYPixel() == segmentoReta.getInicioRotaPixelY()){
					encontrou = true;
				}
				
				if(i == ultmaIteracao){
					if(coordenada.getLongitudeXPixel() == segmentoReta.getFimRotaPixelX() &&
						coordenada.getLatitudeYPixel() == segmentoReta.getFimRotaPixelY()){
						encontrouUltimo = true;
					}
				}
				
				if (encontrou && i != ultmaIteracao){
					break;
				}
			}
			
			if (!encontrou){
				arrayListRotaSegura.add(new Coordenada(
						segmentoReta.getInicioRotaPixelX(),
						segmentoReta.getInicioRotaPixelY()));
			}
			
			if((i == ultmaIteracao) && !encontrouUltimo){
				arrayListRotaSegura.add(new Coordenada(
						segmentoReta.getFimRotaPixelX(),
						segmentoReta.getFimRotaPixelY()));
			}
		}
		
	}
	
	public String buscaHeuristicaMelhorRota() {
		String pontosDesvio = "";
		int ultimo = arrayListRotaSegura.size() - 1;
		boolean passaPorHotspot = false;
		
		//Testa se tem uma rota plotada
		if (ultimo > 0){
			Coordenada coordenadaOrigem = arrayListRotaSegura.get(0);
			Coordenada coordenadaDestino = arrayListRotaSegura.get(ultimo);
		
			for(Hotspot hotspot : arrayListHotspot){
				if(isDentroHotspot(coordenadaOrigem, coordenadaDestino, hotspot)){
					pontosDesvio += verificaPossiveisPontosTangencia(hotspot,
							new Coordenada(coordenadaOrigem.getLongitudeXPixel(),
									coordenadaOrigem.getLatitudeYPixel()),
							new Coordenada(coordenadaOrigem.getLongitudeXPixel(),
									coordenadaOrigem.getLatitudeYPixel()),
							new Coordenada(coordenadaDestino.getLongitudeXPixel(),
									coordenadaDestino.getLatitudeYPixel())) + "a";
					passaPorHotspot = true;
				}
			}
			
			if(!passaPorHotspot){
				pontosDesvio += coordenadaOrigem.getLongitudeXPixel() + "," +
					coordenadaOrigem.getLatitudeYPixel() + "a";
				
				pontosDesvio += getPontoIntermediario(coordenadaOrigem, coordenadaDestino) + "a";
				
				pontosDesvio += coordenadaDestino.getLongitudeXPixel() + "," +
				coordenadaDestino.getLatitudeYPixel() + "a";
			}
		}
		
		if (pontosDesvio.equals("")) {
			return "";
		}
		else{
			return pontosDesvio.substring(0,pontosDesvio.length() - 1);
		}
	}
	
	private String getPontoIntermediario(Coordenada coordenadaOrigem,
			Coordenada coordenadaDestino) {

		
		long x = (coordenadaOrigem.getLongitudeXPixel() + coordenadaDestino.getLongitudeXPixel()) / 2;
		long y = (coordenadaOrigem.getLatitudeYPixel() + coordenadaDestino.getLatitudeYPixel()) / 2;
		
		
		return x + "," + y;
	}

	public String getDesvioRotaDoHotspot(){
		String pontosDesvio = "";
			
		for (Hotspot hotspot : arrayListHotspot){
			//Faz o desvio do hotspot Teste:
			if(hotspot.getCoordenadasEntraSaiHotspot()[0] != null && hotspot.getCoordenadasEntraSaiHotspot()[1] != null){
				pontosDesvio += verificaPossiveisPontosTangencia(hotspot,
						new Coordenada(hotspot.getCoordenadasEntraSaiHotspot()[0].getLongitudeXPixel(),
								hotspot.getCoordenadasEntraSaiHotspot()[0].getLatitudeYPixel()),
						new Coordenada(hotspot.getCoordenadasEntraSaiHotspot()[0].getLongitudeXPixel(),
								hotspot.getCoordenadasEntraSaiHotspot()[0].getLatitudeYPixel()),
						new Coordenada(hotspot.getCoordenadasEntraSaiHotspot()[1].getLongitudeXPixel(),
								hotspot.getCoordenadasEntraSaiHotspot()[1].getLatitudeYPixel())) + "a";
			}
		}
		
		return pontosDesvio.substring(0,pontosDesvio.length() - 1);
	}
	
	
	
	private String verificaPossiveisPontosTangencia(Hotspot hotspot, Coordenada coordenadaAtual,
			Coordenada coordenadaOrigem, Coordenada coordenadaDestino) {
		
		String pontosDesvio1 = "";
		String pontosDesvio2 = "";
		String pontosDesvio = "";
		String pontosDesvioOrigem = "";
		String pontosDesvioDestino = "";
		
		double distancia1 = 0;
		double distancia2 = 0;
		
		pontosDesvioOrigem += coordenadaOrigem.getLongitudeXPixel() + "," + coordenadaOrigem.getLatitudeYPixel() + "a";
		pontosDesvioDestino += coordenadaDestino.getLongitudeXPixel() + "," + coordenadaDestino.getLatitudeYPixel() + "a";
		
		pontosDesvio += pontosDesvioOrigem; 
		
		pontosDesvio1 += getDistanciaContorno(hotspot, coordenadaAtual, coordenadaOrigem, coordenadaDestino, 1);
		
		pontosDesvio2 += getDistanciaContorno(hotspot, coordenadaAtual, coordenadaOrigem, coordenadaDestino, 2);
		
		distancia1 = getDistanciaDesvio(pontosDesvioOrigem + pontosDesvio1 + pontosDesvioDestino);
			
		distancia2 = getDistanciaDesvio(pontosDesvioOrigem + pontosDesvio2 + pontosDesvioDestino);
		
		if(distancia1 > distancia2){
			pontosDesvio += pontosDesvio1;
		}else{
			pontosDesvio += pontosDesvio2;
		}
		
		pontosDesvio += pontosDesvioDestino;
		
		return pontosDesvio.substring(0,pontosDesvio.length() - 1);
			
	}
	
	
	private double getDistanciaDesvio(String pontosDesvioMaisInicioFim) {
		double distancia = 0;
		
		String [] pontos = pontosDesvioMaisInicioFim.split("a");
		
		
		Point pointInicial = new Point(Integer.parseInt(pontos[0].split(",")[0]),
				Integer.parseInt(pontos[0].split(",")[1])); 
		for(int i = 1; i < pontos.length; i++){
			Point pointAtual = new Point(Integer.parseInt(pontos[i].split(",")[0]),
					Integer.parseInt(pontos[i].split(",")[1]));
			
			distancia += getDistanciaEntrePontos(pointInicial, pointAtual);
			
			pointInicial = pointAtual;
		}
		
		
		
		return distancia;
	}

	private String getDistanciaContorno(Hotspot hotspot, Coordenada coordenadaAtual,
			Coordenada coordenadaOrigem, Coordenada coordenadaDestino, int lado){
		
		String pontosDesvio = "";
		double distanciaAnterior = 0;//getDistanciaEntrePontos
		double distanciaAtual = 0;
		boolean mudou = false;//Evita ficar no loop infinito;
		int entrou = 0;
		Coordenada coordenadaAux = null;
		boolean testouLado = false;
		
		ArrayList<Coordenada> arrayListVerticesTangencia = hotspot.getArrayListVerticesTangenciaFolga();
		
		while(isDentroHotspot(coordenadaAtual, coordenadaDestino, hotspot)){
			mudou = false;
			
			pula:{
			
				for (Coordenada coordenadaVerticeTangencia : arrayListVerticesTangencia){
					
					Coordenada coordenadaVertice = new Coordenada(coordenadaVerticeTangencia.getLatitudeYPixel(),
							coordenadaVerticeTangencia.getLongitudeXPixel());
					
					if(!isDentroHotspot(coordenadaAtual, coordenadaVertice, hotspot)){
//						System.out.println("1");
						
						if(entrou == 0 && lado == 2 && !testouLado){
							//Da um continue para pegar outro vertice (passar para o outro lado),
							//e coloca a variavel lado = 1 para nao checar mais
							testouLado = true; 
							
							coordenadaAux = coordenadaVertice;
							
							continue;
						}else if(entrou == 1 && lado == 2){
							if(coordenadaAux.getLatitudeYPixel() == coordenadaVertice.getLatitudeYPixel() &&
									coordenadaAux.getLongitudeXPixel() == coordenadaVertice.getLongitudeXPixel()){
								continue;//Para evitar o vicio de voltar ao mesmo lado de novo
							}
						}
						
						distanciaAtual = getDistanciaEntrePontos(
								new Point((int) coordenadaOrigem.getLongitudeXPixel(),(int)coordenadaOrigem.getLatitudeYPixel()),
								new Point((int)coordenadaVertice.getLongitudeXPixel(),(int)coordenadaVertice.getLatitudeYPixel()));
						
						if(distanciaAtual > distanciaAnterior){
							pontosDesvio += coordenadaVerticeTangencia.getLatitudeYPixel() + "," +
								coordenadaVerticeTangencia.getLongitudeXPixel() + "a";
							coordenadaAtual = new Coordenada(coordenadaVerticeTangencia.getLatitudeYPixel(),
									coordenadaVerticeTangencia.getLongitudeXPixel());
							distanciaAnterior = distanciaAtual;
							entrou++;
							mudou = true;
							break pula;
						}
					}	
				}
			}
			
			//if (entrou == 2) break;
			if (!mudou){
				System.out.println("NAO MUDOU");
				break;//Contornou os vertices mas não enchergou o destino
			}
		}
		
		return pontosDesvio;
	}

	private boolean isDentroHotspot(Coordenada coordenadaInicio, Coordenada coordenadaFim, Hotspot hotspot){

//		if(isVerticeConseguente(coordenadaInicio, coordenadaFim, hotspot)){
//			return false;
//		}
		
		ArrayList<Celula> arrayListCelulas = hotspot.getArrayListCelulas();
		
		for (Celula celula : arrayListCelulas) {
			int sul = celula.getYCima();
			int norte = celula.getYBaixo();
			int oeste = celula.getXEsq();
			int leste = celula.getXDir();
			
			if (equacaoRetaX_2(coordenadaInicio.getLatitudeYPixel(), coordenadaInicio.getLongitudeXPixel(),
					coordenadaFim.getLatitudeYPixel(), coordenadaFim.getLongitudeXPixel(), oeste,
					sul,norte)) {
				//celula.setCor(Color.BLUE);
				return true;
			}else if (equacaoRetaX_2(coordenadaInicio.getLatitudeYPixel(), coordenadaInicio.getLongitudeXPixel(),
					coordenadaFim.getLatitudeYPixel(), coordenadaFim.getLongitudeXPixel(), leste,
					sul,norte)) {
				//celula.setCor(Color.GREEN);
				return true;
			}else if (equacaoRetaY_2(coordenadaInicio.getLatitudeYPixel(), coordenadaInicio.getLongitudeXPixel(),
					coordenadaFim.getLatitudeYPixel(), coordenadaFim.getLongitudeXPixel(), sul,
					oeste, leste)) {
				//celula.setCor(Color.BLACK);
				return true;
			}else if (equacaoRetaY_2(coordenadaInicio.getLatitudeYPixel(), coordenadaInicio.getLongitudeXPixel(),
					coordenadaFim.getLatitudeYPixel(), coordenadaFim.getLongitudeXPixel(), norte,
					oeste, leste)) {
				//celula.setCor(Color.CYAN);
				return true;
			}
		}
		
		return false;		
	}
	
	private boolean isVerticeConseguente(Coordenada coordenadaInicio,
			Coordenada coordenadaFim, Hotspot hotspot) {
		
		for(int i = 0; i < hotspot.getArrayListVerticesTangencia().size(); i++){
			Coordenada coordenadaAtual = hotspot.getArrayListVerticesTangencia().get(i);
			
			if (i >= 1){
				Coordenada coordenadaAnterior = hotspot.getArrayListVerticesTangencia().get(i - 1);
				
				if(coordenadaInicio.getLongitudeXPixel() == coordenadaAnterior.getLatitudeYPixel() &&
						coordenadaInicio.getLatitudeYPixel() == coordenadaAnterior.getLongitudeXPixel() &&
						coordenadaFim.getLongitudeXPixel() == coordenadaAtual.getLatitudeYPixel() &&
						coordenadaFim.getLatitudeYPixel() == coordenadaAtual.getLongitudeXPixel()){
					return true;
				}
			}else{
				//Testa o ultimo com o primeiro
				Coordenada coordenadaAnterior = hotspot.getArrayListVerticesTangencia().get(hotspot.getArrayListVerticesTangencia().size() - 1);
				
				if(coordenadaInicio.getLongitudeXPixel() == coordenadaAnterior.getLatitudeYPixel() &&
						coordenadaInicio.getLatitudeYPixel() == coordenadaAnterior.getLongitudeXPixel() &&
						coordenadaFim.getLongitudeXPixel() == coordenadaAtual.getLatitudeYPixel() &&
						coordenadaFim.getLatitudeYPixel() == coordenadaAtual.getLongitudeXPixel()){
					return true;
				}
				
			}
		}
		
		
		
		return false;
	}

	public String getRotaSegura(){
		String rotaSegura = "";
		
		for (Coordenada coordenada : arrayListRotaSegura){
			rotaSegura += coordenada.getLongitudeXPixel() + "," +
						coordenada.getLatitudeYPixel() + "a";
		}
		
		return rotaSegura.substring(0,rotaSegura.length() - 1);
	}
	
	//Cores ordenadas por quantidade de céluas
	private void coresKernel_4() {
		
		int intevaloCor = 256 / Util.numCamadasCor;
		int numCelulasComEstimativaNaoZero = arrayListCelulaEstimacaoNaoZero.size();
		int intervaloEventos = (int) Math.ceil((double)numCelulasComEstimativaNaoZero / (double) Util.numCamadasCor);
		int cor = 0;
		int numCor = 0;
		
		for (int i = 0; i < numCelulasComEstimativaNaoZero; i++){ //Do menor para o maior
			if (i % intervaloEventos != 0){//Multiplos de Ex: 60 (300 eventos / 5) muda de cor
				
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(getCor(numCor));
				
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color((intevaloCor*2*cor >= 255? 255 - (intevaloCor*cor - 100)  : 255),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor)));
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color(255,255 - intevaloCor*cor,255 - intevaloCor*cor));
			}else{
				if (cor < Util.numCamadasCor){
					cor++;
					numCor++;
					
					if(! (i == 0)){
						this.setIntervalo(numCor, arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa());
						if(imprime)
							System.out.println("Modifica Intervalo: " + numCor);
					}
				}
				
				arrayListCelulaEstimacaoNaoZero.get(i).setCor(getCor(numCor));
				
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color((intevaloCor*2*cor >= 255? 255 - (intevaloCor*cor - 100) : 255),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor),(intevaloCor*2*cor >= 255? 0 : 255 - intevaloCor*cor)));
//				arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color(255,255 - intevaloCor*cor,255 - intevaloCor*cor));
			}
			
			if(! (i == 4)){
				this.setIntervalo(numCor, arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa());
				if(imprime)
					System.out.println("Modifica Intervalo: " + numCor);
			}
			
		}
	
		
	}
	
	
	private void coresKernel_3() {
		
		int numCelulasComEstimativaNaoZero = arrayListCelulaEstimacaoNaoZero.size();
		
		double corMax = 255;
		double corMin = 0;
		int corValor = -1;
		double kernelMax = 1;
		double kernelMin = 0.001;
		double kernelValor = -1;
		
		
		
		for (int i = 0; i < numCelulasComEstimativaNaoZero; i++){
			corValor = -1;
			kernelValor = arrayListCelulaEstimacaoNaoZero.get(i).getEstimativa();
			double corValorTest = ((((kernelValor - kernelMin) * (corMax - corMin)) / (kernelMax - kernelMin)) - corMin);
			corValor = (int) corValorTest;
			if(imprime)
				System.out.println("kernelValor: "+ kernelValor + ", corValor:" + corValorTest);
			arrayListCelulaEstimacaoNaoZero.get(i).setCor(new Color(255,corValor,corValor));
		}
	}
	
	

	private Point getPosicaoReal(int posicaoX, int posicaoY, int incrementoX, int incrementoY){
//		Ver quantos quadradinhos (células), menos uma, partindo do (0,0) até do evento
		int numCelulasX = (int) Math.floor(((double)(posicaoX) / (double) Util.tamCelulaPixel));
		int numCelulasY = (int) Math.floor(((double)(posicaoY) / (double) Util.tamCelulaPixel));

		//Número de posições até a célula antes do evento
		int numPosicoesXAteCelulaAntesEvento = numCelulasX * Util.tamCelulaPixel;
		int numPosicoesYAteCelulaAntesEvento = numCelulasY * Util.tamCelulaPixel;
		
		//Número de posições da célula antes do evento até o evento
		int numPosicoesXCelulaAntesEventoAteEvento = posicaoX - numPosicoesXAteCelulaAntesEvento;
		int numPosicoesYCelulaAntesEventoAteEvento = posicaoY - numPosicoesYAteCelulaAntesEvento;

		posicaoX = incrementoX * (numPosicoesXAteCelulaAntesEvento + numPosicoesXCelulaAntesEventoAteEvento); 
		posicaoY = incrementoY * (numPosicoesYAteCelulaAntesEvento + numPosicoesYCelulaAntesEventoAteEvento);
		
		return new Point(posicaoX,posicaoY);
	}
	
	public void toolTipInformation(int posicaoX, int posicaoY){
		
		//Calcula a posição real 
		Point posicaoReal = getPosicaoReal(posicaoX, posicaoY,MyMapKernel.incrementoCoordenadaX,MyMapKernel.incrementoCoordenadaY);
		
		posicaoX = (int) posicaoReal.getX() + xMin;
		posicaoY = (int) posicaoReal.getY() + yMin;
		
		//Posicao em pixel		
//		posicaoX = (posicaoX + xMin);
//		posicaoY = (posicaoY + yMin);				
		
		Celula celulaAnalise = null;
		
		int x = -1;
		int y = -1;
		
		//Verifica qual celula pertence ao ponto clicado e pinta de verde
		label:{
			for (x = 0; x < util.tamXMatriz; x ++){
				for (y = 0; y < util.tamYMatriz; y ++){
					if(matrizCelulas[x][y].isThere(posicaoX, posicaoY)){
						celulaAnalise = matrizCelulas[x][y].getThis();
						break label;
					}
				}
			}
		}
		
		if (celulaAnalise != null){
			
			/*
			//Interface
			painelKernel.setToolTipText("Posição (x,y): "+posicaoX+", "+posicaoY +
					" / Matriz ["+x+"]["+y+"] /" + 
					" Estimação por Kernel:" + celulaAnalise.getEstimativa());
			*/
			
		}		
	}
	
	int pixelXConvertido;
	int pixelYConvertido;
	
	/*public static double converteLongitudeParaPixel(double longitudePontoWiki){
		return ((-1 * xMax * longitudeMin + xMax * longitudePontoWiki - 1 * xMin * longitudePontoWiki + xMin * longitudeMax) / (longitudeMax - longitudeMin));
	}
	
	public static double converteLatitudeParaPixel(double latitudePontoWiki){
		return ((-1 * yMax * latitudeMin + yMax * latitudePontoWiki - 1 * yMin * latitudePontoWiki + yMin * latitudeMax) / (latitudeMax - latitudeMin));
	}

	public static double convertePixelParaLongitude(double longitudePontoXPixel){
		return ((-1 * longitudeMax * xMin + longitudeMax * longitudePontoXPixel - 1 * longitudeMin * longitudePontoXPixel + longitudeMin * xMax) / (xMax - xMin));
	}
	
	public static double convertePixelParaLatitude(double latitudePontoYPixel){
		return ((-1 * latitudeMax * yMin + latitudeMax * latitudePontoYPixel - 1 * latitudeMin * latitudePontoYPixel + latitudeMin * yMax) / (yMax - yMin));
	}*/
	
	
	
	public double getIntervalo1() {
		return intervalo1;
	}

	public void setIntervalo1(double intervalo1) {
		this.intervalo1 = intervalo1;
	}

	public double getIntervalo2() {
		return intervalo2;
	}

	public void setIntervalo2(double intervalo2) {
		this.intervalo2 = intervalo2;
	}

	public double getIntervalo3() {
		return intervalo3;
	}

	public void setIntervalo3(double intervalo3) {
		this.intervalo3 = intervalo3;
	}

	public double getIntervalo4() {
		return intervalo4;
	}

	public void setIntervalo4(double intervalo4) {
		this.intervalo4 = intervalo4;
	}

	public double getIntervalo5() {
		return intervalo5;
	}

	public void setIntervalo5(double intervalo5) {
		this.intervalo5 = intervalo5;
	}

	public ArrayList<Celula> getArrayListCelulaEstimacaoNaoZero() {
		return arrayListCelulaEstimacaoNaoZero;
	}

	public Dados getDados() {
		return dados;
	}

	public void setLongitudeMin(double longitudeMin) {
		this.longitudeMin = longitudeMin;
	}

	public void setLongitudeMax(double longitudeMax) {
		this.longitudeMax = longitudeMax;
	}

	public void setLatitudeMin(double latitudeMin) {
		this.latitudeMin = latitudeMin;
	}

	public void setLatitudeMax(double latitudeMax) {
		this.latitudeMax = latitudeMax;
	}

	public int getXMin() {
		return xMin;
	}

	public void setXMin(int min) {
		xMin = min;
	}

	public int getYMin() {
		return yMin;
	}

	public void setYMin(int min) {
		yMin = min;
	}

	public int getXMax() {
		return xMax;
	}

	public void setXMax(int max) {
		xMax = max;
	}

	public int getYMax() {
		return yMax;
	}

	public void setYMax(int max) {
		yMax = max;
	}

	public Celula[][] getMatrizCelulas() {
		return matrizCelulas;
	}

	public ArrayList<SegmentoReta> getArrayListRotaSegura() {
		return arrayListRotaOriginal;
	}
	
	public ArrayList<SegmentoReta> getArrayListRotaSeguraAuxTotal() {
		return arrayListRotaSeguraAuxTotal;
	}

	public void setArrayListRotaSegura(ArrayList<SegmentoReta> arrayListRotaSegura) {
		this.arrayListRotaOriginal = arrayListRotaSegura;
	}
	
	//Interface
	/*
	public JFrame getFrame() {
		return frame;
	}
	*/
}
