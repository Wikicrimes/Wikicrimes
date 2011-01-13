package org.wikicrimes.util.rotaSegura.testes.charts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Results;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Scenario;

public class IterationsQualityScatterPlotMaker {

	static final String dir = ResultsLoader.dir;

	private Image image;
	private final int margin = 50;
	private final int xAxisGap = 100;
	private final int yAxisGap = 100;

	public static void main(String[] args) {
		ResultsModel tests = ResultsLoader.loadScenarioTests();
		IterationsQualityScatterPlotMaker chartMaker = new IterationsQualityScatterPlotMaker();
		chartMaker.makeChart(tests);
	}
	
	private void makeChart(ResultsModel tests) {
		
		image = new BufferedImage(1300,800,BufferedImage.TYPE_INT_ARGB_PRE);
		clearBackground(image);
		Graphics g = image.getGraphics();
		drawAxes(image);
		
		int maxIterations = Util.findMaxIterations(tests.scenarios.values());
		int maxQuality = Util.findMaxQuality(tests.scenarios.values());

		RenderedImage rImg = (RenderedImage)image; 
		int w = rImg.getWidth() - (2*margin + xAxisGap);
		int h = rImg.getHeight() - (2*margin + yAxisGap);
		int spanX = w/(maxIterations-1);
		int stepsY = 20; 
		int spanY = h/stepsY;
		
		List<Scenario> scenarios = new ArrayList<Scenario>(tests.scenarios.values());
		for(Scenario scenario : scenarios) {
			
			Results results = scenario.results;
			if(results.satisfactoryStop == null)
				continue;
			int stop = results.satisfactoryStop;
			int x = margin + xAxisGap/2 + spanX*stop;
			double quality = results.iterations.get(stop).qualitySoFar;
			int y = margin + h - (int)(h*quality/maxQuality);
			
			//pinta pontos
			g.setColor(Color.BLACK);
			g.fillOval(x-3, y-3, 5, 5);
			
		}
		
		//desenhar marcadores e labels no eixo Y
		for(int j=1; j<=stepsY; j++) {
			int y = (h + margin) - spanY*j;
			int x = margin + xAxisGap/2;
			String label = ""+j*spanY*maxQuality/h;
			drawYLabel(label, x, y, g);
		}
		
		//desenhar marcadores e labels no eixo X
		for(int j=1; j<=maxIterations; j++) {
			int y = h + margin;
			int x = margin + xAxisGap/2 + spanX*j;
			String label = ""+j;
			drawXLabel(label, x, y, g);
		}
		
		Util.writeToFile(image);
		
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
	
	private void drawYLabel(String label, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x-4, y-1, 8, 3);
		g.drawString(label, x-30, y+5);
	}
	
	private void drawXLabel(String label, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x-1, y-4, 3, 8);
		g.drawString(label, x-5, y+20);
	}
	
}
