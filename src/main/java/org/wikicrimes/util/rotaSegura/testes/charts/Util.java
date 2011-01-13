package org.wikicrimes.util.rotaSegura.testes.charts;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Iteration;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Results;
import org.wikicrimes.util.rotaSegura.testes.charts.ResultsModel.Scenario;

public class Util {
	
	static final String dir = ResultsLoader.dir;

	public static int findMaxQuality(Collection<Scenario> scenarios) {
		double max = 0;
		for(Scenario scenario : scenarios) {
			for(Iteration iteration : scenario.results.iterations) {
				max = Math.max(max, iteration.qualitySoFar);
			}
		}
		return (int)Math.ceil(max);
	}
	
	public static int findMaxIterations(Collection<Scenario> scenarios) {
		int max = 0;
		for(Scenario scenario : scenarios) {
			max = Math.max(max, scenario.results.iterations.size()-1);
		}
		return max;
	}
	
	public static List<Iteration> findChangingIterations(Results results){
		List<Iteration> changingIterations = new ArrayList<Iteration>();
		double previousQuality = Double.NaN;
		for(Iteration it : results.iterations){
			if(it.qualitySoFar != previousQuality) {
				changingIterations.add(it);
				previousQuality = it.qualitySoFar;
			}
		}
		return changingIterations;
	}
	
	public static void writeToFile(Image image) {
		try {
			File file = new File(dir, "chart");
			ImageIO.write((RenderedImage)image, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
