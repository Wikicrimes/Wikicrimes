package org.wikicrimes.util.crimeBaseImport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.wikicrimes.util.crimeBaseImport.specific.AtlantaParser;
import org.wikicrimes.util.crimeBaseImport.specific.CrimespottingParser;
import org.wikicrimes.util.crimeBaseImport.specific.PortlandParser;
import org.wikicrimes.util.crimeBaseImport.specific.WashingtonParser;
import org.wikicrimes.util.crimeBaseImport.specific.CrimespottingParser.CityOption;

public class Main {

	private static final String DIR = "/home/victor/Downloads/";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
		DAO dao = new DAO();
		dao.importCrimes(new CrimespottingParser(CityOption.OAKLAND, new File(DIR, "crime-data")));
		dao.importCrimes(new PortlandParser(new File(DIR, "portland.csv.txt")));
		dao.importCrimes(new AtlantaParser(new File(DIR, "Cobra.txt")));
		dao.importCrimes(new WashingtonParser(new File(DIR, "crime_incidents_2011_CSV.csv")));
	}
	
}
