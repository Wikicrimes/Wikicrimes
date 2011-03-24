package org.wikicrimes.util.rotaSegura.logica;

import java.util.List;

import org.wikicrimes.util.kernelmap.PropertiesLoader;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;
import org.wikicrimes.util.rotaSegura.geometria.Rota;
import org.wikicrimes.util.rotaSegura.geometria.Segmento;


public class Refinamento {
	
	
	private final static double TOLERANCIA_ARRODEIO = PropertiesLoader.getInt("saferoutes.refinement.loop_prunning_tolerance"); //quanto menor, mais arodeios ser�o corrigidos pelo algor�tmo
	private final static int TOLERANCIA_APROXIMACAO = PropertiesLoader.getInt("saferoutes.point_approximation_limit"); //distancia m�xima entre pontos na obten��o de pontos e rotas aproximadas
	
	
	/**
	 * Detecta e elimina "arrodeios".
	 * Este problema ocorre qd o googlemaps faz uma arrodeio desnecess�rio para alcancar um ponto da "rota segura" calculada pelo server 
	 * que ficou fora do caminho preferido pelo GoogleMaps.
	 * A solu��o � mover este ponto para um local pr�ximo ao in�cio ou fim do arrodeio, que s�o bem pr�ximos um do outro.
	 * Obs: se for detectada um "arrodeio", a "modelo" ser� modificada por este m�todo
	 */
	public static boolean refinaRota(Rota modelo, Rota resultado){
		//obs: 
		//a "modelo" eh a que eh resultado direto da logica de rotas seguras (foi gerada pelo servidor)
		//a "resultado" eh a gerada pelo googleMaps, ao receber os pontos da "modelo" como parametro (foi obtida do cliente)
		//as duas sao semelhantes, mas a "resultado" tem mais pontos do que a "modelo" (ou a msm qtdade, mas nunca tem menos)
		//a "resultado" serah usada para detectar o "arrodeio"
		//o ponto de correcao serah adicionado a "modelo" (que serah enviada novamente ao google maps, desta vez evitando arrodeio)
		
//		/*teste*/new PainelTesteRotas("modelo", "resultado","tiraArrodeio(), azul:"modelo" , vermelho:"resultado"");
		/*teste*/System.out.println("ARRODEIO: ");
		
		//DETECTA
		boolean temArrodeio = false;
		List<Ponto> pontos = resultado.getPontos();
		for(int i=0; i<pontos.size(); i++){
			Ponto p1 = pontos.get(i); //p1 � cada uma das coordenadas de toda a "resultado"
			for(int j=i+1; j<pontos.size(); j++){
				Ponto p2 = pontos.get(j); //p2 � cada uma das coordenadas subsequentes � p1
				double distReta = new Segmento(p1,p2).comprimento(); //distancia entre p1 e p2 em linha reta
				
				//trecho de p1 at� p2
				Rota trecho = new Rota();
				for(int k=i; k<=j; k++){
					trecho.addFim(pontos.get(k));
				}
				double distCurva = trecho.distanciaPercorrida(); //distancia entre p1 e p2 seguindo a curva da rota
				
				double distLimite = distReta * TOLERANCIA_ARRODEIO; //distancia limite para o contorno n�o ser considerado "arrodeio"
																	//este limite � arbitr�rio
				if(distCurva > distLimite){
					Ponto ida = p1; //p1 � come�o de um "arrodeio", agora tem q ver qual � o fim (p2 com menor dist�ncia reta at� p1)
					
					int indiceVolta = encontraFimArrodeio(resultado, i, j); //identifica o fim do "arrodeio"
					Ponto volta = pontos.get(indiceVolta);
					/*teste*/System.out.println("ida="+i+", volta="+indiceVolta);
					
					//CORRIGE
					corrigeArrodeio(ida, volta, modelo, resultado); //corrige o problema
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
	
	private static int encontraFimArrodeio(Rota resultado, int inicio, int fimPorEnquanto){
		List<Ponto> pontos = resultado.getPontos();
		double menorDistancia = new Segmento(pontos.get(inicio), pontos.get(fimPorEnquanto)).comprimento();
		int fim = fimPorEnquanto;
		for(int i=fimPorEnquanto+1; i< pontos.size(); i++){
			double distancia = new Segmento(pontos.get(inicio), pontos.get(i)).comprimento();
			menorDistancia = Math.min(menorDistancia,distancia);
			if(menorDistancia == distancia)
				fim = i;
		}
		return fim;
	}
	
	private static void corrigeArrodeio(Ponto ida, Ponto volta, Rota modelo, Rota resultado){
		
		Rota resultadoAprox = resultado.getRotaAproximada(modelo, TOLERANCIA_APROXIMACAO); //pontos da "resultado" que mais se aproximam dos pontos da "modelo"
		Ponto aux = ida;
		while(!resultadoAprox.contem(aux) && aux != null){
			aux = resultado.pontoAnterior(aux); //caminhando pra tras na "resultado" at� encontrar um ponto que exista na "resultado"Aprox
		}
		
		Ponto ptAnteriorGoogleMapsAproxServidor = aux; //pt na "resultado" q est� pr�ximo a um ponto na "modelo" e vem antes da ida
		Ponto ptAnteriorServidor = modelo.getPontoPerto(ptAnteriorGoogleMapsAproxServidor, TOLERANCIA_APROXIMACAO);
		
		if(ptAnteriorGoogleMapsAproxServidor == null || ptAnteriorServidor == null){
			throw new RuntimeException("limite de aproxima��o excedido");
		}
		
		Ponto pontoCausadorDoArrodeio = modelo.pontoPosterior(ptAnteriorServidor); //o ponto que deve estar na ponta da ida e volta
		modelo.remove(pontoCausadorDoArrodeio);
		Ponto meio = Ponto.medio(ida, volta); //ponto que substitui o pontoCausadorDaIdaVolta
		modelo.addDepois(ptAnteriorServidor, meio);
	}
	
	/*
	public static void refinaRota2(Rota vertices, Rota rota){
		double anguloOD = new Segmento(rota.getInicio(), rota.getFim()).angulo();
		
		List<Segmento> segms = rota.getSegmentosReta();
		for(Segmento s : segms){
			double a = s.angulo();
			System.out.println(a);
		}
		
	}
	*/
	
}