package org.wikicrimes.util;

import java.awt.Color;

public class ColorUtil {

	public static String colorToHex(Color c) {
		  return Integer.toHexString( c.getRGB() & 0x00ffffff ); 
		}
	
}
