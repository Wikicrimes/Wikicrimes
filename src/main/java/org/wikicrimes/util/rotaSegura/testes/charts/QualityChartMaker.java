package org.wikicrimes.util.rotaSegura.testes.charts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Iteration;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Results;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Scenario;


public class QualityChartMaker {

	private Image image;
	private final int margin = 50;
	private final int xAxisGap = 100;
	private final int yAxisGap = 100;

	public static void main(String[] args) {
		ResultsModel tests = ResultsLoader.loadScenarioTests();
		QualityChartMaker chartMaker = new QualityChartMaker();
		chartMaker.makeChart(tests);
	}
	
	private void makeChart(ResultsModel tests) {
		
		image = new BufferedImage(1300,800,BufferedImage.TYPE_INT_ARGB_PRE);
		clearBackground(image);
		Graphics g = image.getGraphics();
		drawAxes(image);
		
		int n = tests.scenarios.size();
		int maxQuality = Util.findMaxQuality(tests.scenarios.values());

		RenderedImage rImg = (RenderedImage)image; 
		int w = rImg.getWidth() - (2*margin + xAxisGap);
		int h = rImg.getHeight() - (2*margin + yAxisGap);
		int spanX = w/(n-1);
		int stepsY = 20; 
		int spanY = h/stepsY;
		
		int i=0;
		List<Scenario> scenarios = new ArrayList<Scenario>(tests.scenarios.values());
//		Collections.sort(scenarios, ResultsModel.toleranceComparator);
//		Collections.sort(scenarios, ResultsModel.minDistanceComparator);
//		Collections.sort(scenarios, ResultsModel.dangerComparator);
//		Collections.sort(scenarios, ResultsModel.stopQualityComparator);
		Collections.sort(scenarios, ResultsModel.startQualityComparator);
		for(Scenario scenario : scenarios) {
			
			Results results = scenario.results;
			int x = margin + xAxisGap + spanX*i;
			List<Iteration> changingIterations = Util.findChangingIterations(results);
			
			Iteration firstIteration = scenario.results.iterations.get(0);
			int yIni = yPosition(firstIteration.qualitySoFar, maxQuality, h);
			
			//pinta barra cinza: tolerancia
			{
				int yFin = yPosition(results.tolerance, maxQuality, h);
				drawBar(x, yIni, yFin, Color.LIGHT_GRAY, g);
			}

			//pinta barra verde: parada satisfatoria
			if(results.satisfactoryStop != null) {
				Iteration lastIteration = scenario.results.iterations.get(results.satisfactoryStop);
				int yFin = yPosition(lastIteration.qualitySoFar, maxQuality, h);
				drawBar(x, yIni, yFin, Color.GREEN, g);
			}
			
			//pinta barra amarela: primeira melhora significativa
			if(results.firstImprovement != null) {
				Iteration lastIteration = scenario.results.iterations.get(results.firstImprovement);
				int yFin = yPosition(lastIteration.qualitySoFar, maxQuality, h);
				drawBar(x, yIni, yFin, Color.ORANGE, g);
			}

			//pinta marcas azuis: mudancas de qualidade
			for(Iteration it : changingIterations) {
				int y = yPosition(it.qualitySoFar, maxQuality, h);
				drawMarker(x, y, g, Color.BLUE);
			}
			
			//pinta marca vermelha: tolerancia
			{
				int yTol = yPosition(results.tolerance, maxQuality, h);
				drawMarker(x, yTol, g, Color.RED);
				
				int yMinDist = yPosition(results.minDistance, maxQuality, h);
				drawMarker(x, yMinDist, g, Color.CYAN);
				
				double danger = results.minDistance*results.avgDensity/results.maxDensity;
				int yDanger = yPosition(danger, maxQuality, h);
				drawMarker(x, yDanger, g, Color.MAGENTA);
			}
			
			//desenhar label no eixo X
			int y = (h + margin);
			String label = scenario.id + " - " + scenario.name;
			drawXLabel(label, x, y, g);
			
			i++;
		}

		//desenhar marcadores, labels e pontinhos no eixo Y
		g.setColor(Color.BLACK);
		for(int j=0; j<=stepsY; j++) {
			
			int y = (h + margin) - spanY*j;
			int xIni = margin + xAxisGap/2;
			int xFin = rImg.getWidth() - margin;
			
			//pontinhos
			for(int x=xIni; x<=xFin; x+=5) {
				g.drawLine(x, y, x, y);
			}

			//marcadores e labels
			String label = ""+j*spanY*maxQuality/h;
			drawYLabel(label, xIni, y, g);
		}
		
		Util.writeToFile(image);
		
	}
	
	private int yPosition(double value, int max, int h) {
		return (h + margin) - (int)(value*h/max);
	}
	
	private void clearBackground(Image img) {
		RenderedImage rImg = (RenderedImage)image;
		int w = rImg.getWidth();
		int h = rImg.getHeight();

		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
	}
	
	private void drawAxes(Image img) {
		RenderedImage rImg = (RenderedImage)image;
		int w = rImg.getWidth();
		int h = rImg.getHeight();
		
		Graphics g = img.getGraphics();
		g.setColor(Color.BLACK);
		int xAxisY = h - (margin + yAxisGap);
		int yAxisX = margin + xAxisGap/2;
		g.drawLine(0, xAxisY, w, xAxisY); //eixo X
		g.drawLine(yAxisX, 0, yAxisX, h); //eixo Y
	}
	
	private void drawMarker(int x, int y, Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(x-4, y-1, 8, 3);
	}
	
	private void drawYLabel(String label, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x-4, y-1, 8, 3);
		g.drawString(label, x-40, y+5);
	}
	
	private void drawXLabel(String label, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform oldTransform = g2.getTransform();
		
		int ajustedX = x-5;
		int ajustedY = y+5;
		
		double angle = Math.PI/2.0;
		AffineTransform newTransform = new AffineTransform();
		newTransform.setToRotation(angle, ajustedX, ajustedY);
		g2.transform(newTransform);
		
		g2.drawString(label, ajustedX, ajustedY);
		
		g2.setTransform(oldTransform);
	}
	
	private void drawBar(int x, int y1, int y2, Color color, Graphics g) {
		g.setColor(color);
//		RenderedImage rImg = (RenderedImage)image;
//		int h = rImg.getHeight();
		g.fillRect(x-6, y1, 12, y2-y1);
	}
	
}
