package org.wikicrimes.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victor
 * Pra medir o tempo de execução de trechos de código e identificar gargalos
 */
public class TimeTest {

	private static boolean on = true;
	private static List<Instant> instants;
	
	public static void start() {
		instants = new ArrayList<Instant>();
		saveInstant("start");
	}
	
	public static void saveInstant(String label) {
		long time = System.currentTimeMillis();
		instants.add(new Instant(time, label));
	}
	
	public static void print() {
		if(!on) return;
		long prevTime = instants.get(0).time;
		System.out.println("--------------------------------");
		for(int i=1; i<instants.size(); i++) {
			Instant in = instants.get(i);
			long dt = in.time - prevTime;
			System.out.printf("%20s   %d\n", in.label, dt);
			prevTime = in.time;
		}
		long tf = instants.get(instants.size()-1).time;
		long ti = instants.get(0).time;
		long total = tf - ti;
		System.out.printf("%20s   %d\n", "TOTAL", total);
	}

	private static class Instant{
		long time;
		String label;
		public Instant(long time, String label) {
			this.time = time;
			this.label = label;
		}
	}
	
}
