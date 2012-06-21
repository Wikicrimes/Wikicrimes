package org.wikicrimes.util.crimeBaseImport.specific;

import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_OUTROS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_VEICULO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.ATENTADO_AO_PUDOR;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.HOMICIDIO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.NAO_INFORMADO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PESSOA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PROPRIEDADE;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.RIXAS_OU_BRIGAS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.VIOLENCIA_DOMESTICA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.FURTO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.ROUBO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.VIOLENCIA;
import static org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException.Reason.DISCARDED_TYPE;
import static org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException.Reason.INSUFICIENT_DATA;
import static org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException.Reason.UNKNOWN_TYPE;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.wikicrimes.util.crimeBaseImport.FileParser;
import org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException;
import org.wikicrimes.util.crimeBaseImport.Model;

/**
 * Base com crimes de Portland
 * 
 * http://civicapps.org/datasets/crime-incidents-2011
 *
 * @author victor
 */
public class PortlandParser extends FileParser{

	/*
	 * 0	Record ID			=	CRI_ID_ORIGINAL
	 * 1	Report Date			=	CRI_DATA (data)
	 * 2	Report Time			=	CRI_DATA (hora)
	 * 3	Major Offense Type	=	CRI_IDTIPO_CRIME e TVI_IDTIPO_VITIMA
	 * 4	Address				=	CRI_ENDERECO
	 * 5	Neighborhood		=	CRI_ENDERECO
	 * 6	Police Precinct		=	
	 * 7	Police District		=	
	 * 8	X Coordinate		=	CRI_LONGITUDE
	 * 9	Y Coordinate		=	CRI_LATITUDE
	 */

	private static final char separator = ',';
	private static final char quote = '"';
	
	public PortlandParser(File file) throws IOException, ClassNotFoundException, SQLException {
		super(file);
	}
	
	@Override
	protected String getBaseName() {
		return "portland";
	}

	@Override
	protected String getBaseUrl() {
		return "http://civicapps.org/datasets/crime-incidents-2011";
	}
	
	@Override
	public Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException{
		String[] rawFields = FileParser.splitFields(rawData, separator, quote);
		Model crime = new Model();
		crime.CRI_ID_ORIGINAL = rawFields[0];
		crime.CRI_DATA = convertDate(rawFields[1], rawFields[2]);
		crime.CRI_ENDERECO = convertAddress(rawFields[4], rawFields[5]);
		convertCrimeType(crime, rawFields[3]);
		crime.CRI_LATITUDE = convertCoordinate(rawFields[9]); //TODO q unidade eh essa?
		crime.CRI_LONGITUDE = convertCoordinate(rawFields[8]);
		crime.CRI_DESCRICAO = rawFields[3];
		setLocationFields(crime);
		return crime;
	}
	
	private static String unquote(String str){
		if(str.charAt(0) != quote || str.charAt(str.length()-1) != quote)
			throw new RuntimeException();
		return str.substring(1, str.length()-1);
	}
	
	private static Timestamp convertDate(String dateStr, String hourStr) throws ParseException{
		String datehourStr = unquote(dateStr) + " " + unquote(hourStr);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		Date date = df.parse(datehourStr);
		return new Timestamp(date.getTime());
	}
	
	private static String convertAddress(String address, String neighborhood){
		return unquote(address) + ", " + unquote(neighborhood);
	}
	
	private void convertCrimeType(Model crime, String str) throws IgnoredCrimeException{
		str = str.substring(1, str.length()-1);
		if(str.equals("Aggravated Assault")){ //agressao com arma
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("Arson")){  //incendio culposo
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Assault, Simple")){ //agressao, violencia domestica, ameaca 
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = VIOLENCIA_DOMESTICA;
		}else if(str.equals("Burglary")){ //assalto a estabelecimento, arrombamento
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("Curfew")){ //toque de recolher
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Disorderly Conduct")){ //vandalismo
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("Drugs")){
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("DUII")){ //dirigir embriagado
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Embezzlement")){ //estelionato 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Forgery")){ //falsificacao
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Fraud")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Gambling")){ //jogos ilegais (caca niqueis, etc)
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Homicide")){ 
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = HOMICIDIO;
		}else if(str.equals("Kidnap")){ //sequestro
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Larceny")){ //pequeno furto
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("Liquor Laws")){ //bebidas alcoólicas
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Motor Vehicle Theft")){ 
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_VEICULO;
		}else if(str.equals("Offenses Against Family")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Prostitution")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Rape")){ //estupro
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = NAO_INFORMADO;
		}else if(str.equals("Robbery")){ //assalto a pessoa
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("Runaway")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Sex Offenses")){ 
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = ATENTADO_AO_PUDOR;
		}else if(str.equals("Stolen Property")){ 
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("Trespass")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("Vandalism")){ 
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("Weapons")){ 
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else{
			throw new IgnoredCrimeException(UNKNOWN_TYPE, str);
		}
	}
	
	private Double convertCoordinate(String str) throws IgnoredCrimeException{
		if(str.length() == 0)
			throw new IgnoredCrimeException(INSUFICIENT_DATA);
		else return Double.valueOf(str);
	}
	
	private static void setLocationFields(Model crime){
		crime.CRI_CIDADE = "Portland";
		crime.CRI_ESTADO = "Oregon";
		crime.CRI_PAIS = "USA";
	}
	
}
