package org.wikicrimes.util;

import java.io.IOException;

import com.maxmind.geoip.LookupService;

public class GeoData {
    // Uncomment for windows
    //private String dir = System.getProperty("user.dir"); 

    // Uncomment for Linux
    //private String dir = "/root/workspace/wikicrimes";
	private String dir = "/home/carlos/workspace_java/wikicrimes";
	//private String dir = System.getProperty("user.dir");
	private String sep = System.getProperty("file.separator");
    private  String dbfile = dir + sep + "GeoIP.dat"; 
    private LookupService cl;
    private static GeoData geo;
	    // You should only call LookupService once, especially if you use
	    // GEOIP_MEMORY_CACHE mode, since the LookupService constructor takes up
	    // resources to load the GeoIP.dat file into memory
	    //LookupService cl = new LookupService(dbfile,LookupService.GEOIP_STANDARD);
	private GeoData(){
		
		try {
			cl = new LookupService(dbfile,LookupService.GEOIP_MEMORY_CACHE);
		} catch (IOException e) {
			System.err.println(e.getMessage() + dbfile);
			e.printStackTrace();
		}
	}    
    public static GeoData getInstance(){
        if (geo == null)
        	return new GeoData();
        else
        	return geo;
    }
    public LookupService getLookupService(){
    	return cl;
    }

	    
}
