package org.wikicrimes.util.rotaSegura.testes.charts;

import static org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.fasterResultComparator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Iteration;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Results;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Scenario;

public class IterationsChartMaker {

	static final String dir = ResultsLoader.dir;

	private Image image;
	private final int margin = 50;
	private final int xAxisGap = 100;
	private final int yAxisGap = 180;

	public static void main(String[] args) {
		ResultsModel tests = ResultsLoader.loadScenarioTests();
		IterationsChartMaker chartMaker = new IterationsChartMaker();
		chartMaker.makeChart(tests);
		printPercentages(tests);
	}
	
	private void makeChart(ResultsModel tests) {
		
		image = new BufferedImage(1300,1000,BufferedImage.TYPE_INT_ARGB_PRE);
		clearBackground(image);
		Graphics g = image.getGraphics();
		drawAxes(image);
		
		int n = tests.scenarios.size();
		int maxIterations = Util.findMaxIterations(tests.scenarios.values());

		RenderedImage rImg = (RenderedImage)image; 
		int w = rImg.getWidth() - (2*margin + xAxisGap);
		int h = rImg.getHeight() - (2*margin + yAxisGap);
		int spanX = w/(n-1);
		int spanY = h/(maxIterations+1);
		
		int i=0;
		List<Scenario> scenarios = new ArrayList<Scenario>(tests.scenarios.values());
		Collections.sort(scenarios, fasterResultComparator);
		for(Scenario scenario : scenarios) {
			
			Results results = scenario.results;
			int x = margin + xAxisGap + spanX*i;
			List<Iteration> changingIterations = Util.findChangingIterations(results);

			//pinta barras

			int y = (h + margin);
			drawBar(x, y, Color.RED, g);
			
			if(results.firstImprovement != null) {
				y = (h + margin) - spanY*results.firstImprovement;
				drawBar(x, y, Color.ORANGE, g);
			}
			
			if(results.satisfactoryStop != null) {
				y = (h + margin) - spanY*results.satisfactoryStop;
				drawBar(x, y, Color.GREEN, g);
			}
			
			//pinta mudancas de qualidade
			for(Iteration it : changingIterations) {
				y = (h + margin) - spanY*it.ordinality;
				drawMarker(x, y, g);
			}
			
			//desenhar label no eixo X
			y = (h + margin);
			String label = scenario.id + " - " + scenario.name;
			drawXLabel(label, x, y, g);
			
			i++;
		}
		
		g.setColor(Color.BLACK);
		for(int j=1; j<=maxIterations; j++) {

			//fazer pontinhos pra cada linha
			int y = (h + margin) - spanY*j;
			int xIni = margin + xAxisGap/2;
			int xFin = rImg.getWidth() - margin;
			for(int x=xIni; x<=xFin; x+=5) {
				g.drawLine(x, y, x, y);
			}
			
			//desenhar marcadores e labels no eixo Y
			String label = ""+j;
			drawYLabel(label, xIni, y, g);
		}
		
		writeToFile(image);
		
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
	
	private void drawMarker(int x, int y, Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(x-4, y-1, 8, 3);
	}
	
	private void drawYLabel(String label, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x-4, y-1, 8, 3);
		g.drawString(label, x-20, y+5);
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
	
	private void drawBar(int x, int y, Color color, Graphics g) {
		g.setColor(color);
		int upperY = margin;
		int barHeight = y-upperY;
		g.fillRect(x-6, upperY, 12, barHeight);
	}
	
	private void writeToFile(Image image) {
		try {
			File file = new File(dir, "chart");
			ImageIO.write((RenderedImage)image, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printPercentages(ResultsModel tests) {
		int successCount = 0;
		int improvementCount = 0;
		for(Scenario s : tests.scenarios.values()) {
			if(s.results.satisfactoryStop != null)
				successCount++;
			else if(s.results.firstImprovement != null)
				improvementCount++;
		}
		int n = tests.scenarios.size();
		int successPercentage = successCount*100/n;
		int improvementPercentage = improvementCount*100/n;
		int failPercentage = 100 - (successPercentage + improvementPercentage);
		System.out.println("SATISFATORIA: " + successPercentage + "%, MELHORADA: " + improvementPercentage + "%, SEM MELHORA: " + failPercentage + "%");
	}
	
}
