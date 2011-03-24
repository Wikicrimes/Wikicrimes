package org.wikicrimes.util.kernelmap;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.servlet.ServletKernelMap;
import org.wikicrimes.util.rotaSegura.geometria.Ponto;


public class AvaliacaoPerigo{
	
	private CrimeService crimeService; 

	private static final int PROPORCAO_TAMANHO_MAPA = 10; //proporcao do tamanho do mapa de kernel em relacao ao circulo avaliado
	private static final int ZOOM = 12;
	private static final int NODE_SIZE = 5;
	private static final double BANDWIDTH = 30;
	
	//condicoes pra avaliacao
	private double amplitudeMinima = 0.000025; //diferenca entre a menor e a maior densidade do mapa de kernel 
	
	public AvaliacaoPerigo(CrimeService crimeService){
		this.crimeService = crimeService;
	}

	/**
	 * @param centro : lat lng
	 * @param raioKm : raio do circulo, em km 
	 * @param dataInicial : o periodo comeca na dataInicial e termina na data de hoje
	 * @return um double variando de 0 (densidade minima do mapa) a 1 (densidade maxima do mapa)
	 * obs:o "mapa" eh um quadrado grande que contem o circulo.
	 */
	public double avaliarCirculo(PontoLatLng centro, double raioKm, Date dataInicial, double crediblidadeMin, double crediblidadeMax){
		
		//conversoes e mapa de kernel
		Ponto centroPixel = centro.toPixel(ZOOM);
		int raioPixel = raioKmToPixel(centro, raioKm);
		int lado = raioPixel*PROPORCAO_TAMANHO_MAPA;
		Rectangle boundsMapa = new Rectangle(centroPixel.x-lado/2, centroPixel.y-lado/2, lado, lado);
		List<Point> crimes = buscaCrimes(boundsMapa, dataInicial, crediblidadeMin, crediblidadeMax);
		KernelMap kernel = new KernelMap(NODE_SIZE, BANDWIDTH, boundsMapa, crimes);
		
		//calculo de densidades
		double maxMapa = kernel.getMaxDens();
		double minMapa = kernel.getMinDens();
		double amplitude = maxMapa-minMapa; 
		if(amplitude < amplitudeMinima) return -1;
		double mediaCirculo = densidadeMediaNoCirculo(kernel, centroPixel, raioPixel);
		double normalizada = mediaCirculo/maxMapa;
		return normalizada;
	}
	
	private int raioKmToPixel(PontoLatLng centro, double raio){
		PontoLatLng pontoPerimetro = centro.transladarKm(raio, 0);
		Ponto pontoPerimetroPixel = pontoPerimetro.toPixel(ZOOM);
		Ponto centroPixel = centro.toPixel(ZOOM);
		int raioPixel = (int)Math.round(Ponto.distancia(pontoPerimetroPixel, centroPixel));
		return raioPixel;
	}
	
	private double densidadeMediaNoCirculo(KernelMap kernel, Ponto centro, int raio){
		int xIni = centro.x - raio;
		int xFin = centro.x + raio;
		int yIni = centro.y - raio;
		int yFin = centro.y + raio;
		double densidadeAcumulada = 0.0;
		int contCelulasCirculo = 0;
		for(int x=xIni; x<=xFin; x+=NODE_SIZE){
			for(int y=yIni; y<=yFin; y+=NODE_SIZE){
				Ponto posicaoCelula = new Ponto(x,y);
				double distanciaAoCentro = Ponto.distancia(posicaoCelula, centro); 
				if(distanciaAoCentro <= raio){
					contCelulasCirculo++;
					densidadeAcumulada += kernel.getDensity(posicaoCelula);
				}
			}
		}
		return densidadeAcumulada/contCelulasCirculo;
	}
	
	private List<Point> buscaCrimes(Rectangle boundsPixel, Date dataInicial, double credMin, double credMax){
		
		Point pixelNO = new Point((int)boundsPixel.getMinX(), (int)boundsPixel.getMinY());
		Point pixelSE = new Point((int)boundsPixel.getMaxX(), (int)boundsPixel.getMaxY());
		PontoLatLng latlngNO = PontoLatLng.fromPixel(pixelNO, ZOOM);
		PontoLatLng latlngSE = PontoLatLng.fromPixel(pixelSE, ZOOM);
		
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("norte", latlngNO.lat);
		params.put("sul", latlngSE.lat);
		params.put("leste", latlngSE.lng);
		params.put("oeste", latlngNO.lng);
		params.put("dataInicial", dataInicial);
		params.put("dataFinal", new Date());
		params.put("credibilidadeInicial", credMin);
		params.put("credibilidadeFinal", credMax);
		List<BaseObject> crimes = crimeService.filter(params);
		List<Point> crimesPixel = ServletKernelMap.toPixel(crimes, ZOOM);
		
		return crimesPixel;
	}

}
