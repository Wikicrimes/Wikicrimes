package org.wikicrimes.util.statistics;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikicrimes.util.ServletUtil;
import org.wikicrimes.util.Util;

public class ChartRequestHandler {

	private WikiCrimesEventsRetriever events;
	
	private String typesChart, reasonsChart;
	
	public ChartRequestHandler(WikiCrimesEventsRetriever events) {
		this.events = events;
		makeCharts();
	}
	
	private void makeCharts() {
		int totalEvents = events.getTotalEvents();
		Map<String,Integer> types = events.getTypeHistogram();
		typesChart = makeTypesChartUrl(types, totalEvents, "A52A2A|0066FF|006600|FF9933", 200, 70);
		int totalReasons = events.getTotalReasons();
		Map<String,Integer> reasons = events.getReasonHistogram();
		reasonsChart = makeReasonsChartUrl(reasons, totalReasons, "CD5C5C", 300, 70);
	}

	public String makeTypesChartUrl(Map<String,Integer> histogram, int total, String colors, int width, int height){
		double r = Util.nullToZero(histogram.get("Roubo"));
		double tr = Util.nullToZero(histogram.get("Tentativa de Roubo"));
		double f = Util.nullToZero(histogram.get("Furto"));
		double tf = Util.nullToZero(histogram.get("Tentativa de Furto"));
		double v = Util.nullToZero(histogram.get("Violência"));
		List<Double> values = new ArrayList<Double>(4); 
		values.add(r + tr);
		values.add(f + tf);
		values.add(v);
		values.add(total - (r + tr + f + tf + v));
		List<String> labels = new ArrayList<String>(4);
		labels.add(format("Roubo", values.get(0), total));
		labels.add(format("Furto", values.get(1), total));
		labels.add(format("Violência", values.get(2), total));
		labels.add(format("Outro", values.get(3), total));
		removeZeros(labels, values);
		return makeChartApiUrl("p", values, width, 70, labels, colors);
	}
	
	public String makeReasonsChartUrl(Map<String,Integer> histogram, int total, String colors, int width, int height){
		Map<String,Integer> knownReasons = new HashMap<String, Integer>(histogram);
		int unknown = Util.nullToZero(knownReasons.remove("Não sei"));
		total -= unknown;
		List<String> keysList = new ArrayList<String>(knownReasons.keySet());
		List<Integer> valuesList = new ArrayList<Integer>(knownReasons.values());
		List<String> sortedKeys = Util.sortKeys(keysList, valuesList);
		int n = knownReasons.size();
		String max1 = n >= 1? sortedKeys.get(n-1) : null;
		String max2 = n >= 2? sortedKeys.get(n-2) : null;
		String max3 = n >= 3? sortedKeys.get(n-3) : null;
		double v1 = Util.nullToZero(knownReasons.get(max1));
		double v2 = Util.nullToZero(knownReasons.get(max2));
		double v3 = Util.nullToZero(knownReasons.get(max3));
		List<Double> values = new ArrayList<Double>(4); 
		values.add(v1);
		values.add(v2);
		values.add(v3);
		values.add(total - (v1 + v2 + v3));
		List<String> labels = new ArrayList<String>(4);
		labels.add(format(max1, values.get(0), total));
		labels.add(format(max2, values.get(1), total));
		labels.add(format(max3, values.get(2), total));
		labels.add(format("Outros", values.get(3), total));
		removeZeros(labels, values);
		return makeChartApiUrl("p", values, width, 70, labels, colors);
	}
	
	private String makeChartApiUrl(String type, List<Double> values, int width, int height, List<String> labels, String colors) {
		String cht = "cht=" + type;
		String chd = "chd=t:" + format(values);
		String chs = "chs=" + width + "x" + height;
		String chdl = "chdl=" + formatLabels(labels);
		String chf = "chf=bg,s,65432100";
		String chco = "chco=" + colors;
		String url = "http://chart.googleapis.com/chart?" + cht + "&" + chd + "&"  + chs + "&" + chdl + "&" + chf + "&" + chco;
		return url;
	}
	
	private String format(List<Double> array) {
		double total = Util.sumDoubles(array);
		StringBuilder s = new StringBuilder();
		for(double x : array) {
			s.append(x/total);
			s.append(",");
		}
		if(s.length() > 0)
			s.delete(s.length()-1, s.length());
		return s.toString();
	}
	
	private String formatLabels(List<String> array) {
		StringBuilder s = new StringBuilder();
		for(String x : array) {
			s.append(x);
			s.append("|");
		}
		if(s.length() > 0)
			s.delete(s.length()-1, s.length());
		return ServletUtil.urlEncode(s.toString());
	}
	
	private String format(String label, double value, double total) {
		NumberFormat fmt = NumberFormat.getPercentInstance();
		String pctg = fmt.format(value/total);
		return label + " (" + pctg + ")";
	}
	
	private void removeZeros(List<String> labels, List<Double> values) {
		for(int i=0; i<labels.size(); i++) {
			if(values.get(i) == 0) {
				labels.remove(i);
				values.remove(i);
				i--;
			}
		}
	}

	public String getTypesChart() {
		return typesChart;
	}

	public String getReasonsChart() {
		return reasonsChart;
	}
	
}
