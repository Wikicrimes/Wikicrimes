package org.wikicrimes.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victor
 * 
 * Pra medir o tempo de execução de trechos de código e identificar gargalos
 *
 */
public class BottleneckFinder {

	private static boolean on = false;
	
	private String title;
	private List<String> labels;
	private List<Long> times;
	
	int labelsSize;
	
	public BottleneckFinder(String title) {
		this.title = title;
		labels = new ArrayList<String>();
		times = new ArrayList<Long>();
	}
	
	public void saveTime(String label) {
		long t = System.currentTimeMillis();
		labels.add(label);
		times.add(t);
		labelsSize = Math.max(labelsSize, label.length());
	}
	public void printTimeIntervals() {
		if(!on) return;
		System.out.println("------------" + title + "-------------");
		for(int i=1; i<labels.size(); i++) {
			String label = labels.get(i);
			long ti = times.get(i);
			long ta = times.get(i-1);
			long dt = ti - ta;
		
			String fmt = String.format("%" + labelsSize + "s = %dms", label, dt);
			System.out.println(fmt);
		}
		if(labels.size() > 2) {
			long dt = times.get(times.size()-1) - times.get(0);
			String fmt = String.format("%" + labelsSize + "s = %dms", "TOTAL", dt);
			System.out.println(fmt);
		}
	}
	
}
