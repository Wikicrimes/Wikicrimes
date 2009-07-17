package org.wikicrimes.util.kernelMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 
 * @author mairon
 *
 */
public class Test_Format {
	public static void main(String[] args) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern(".00");
		String output = df.format(12345);
		System.out.println("0000" + " " + output + " " + 
				Locale.ENGLISH.toString());
	}
}
