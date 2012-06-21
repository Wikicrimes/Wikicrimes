package org.wikicrimes.util.crimeBaseImport.specific;

import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_OUTROS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_VEICULO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.HOMICIDIO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PESSOA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PROPRIEDADE;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.RIXAS_OU_BRIGAS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.VIOLENCIA_DOMESTICA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.FURTO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.ROUBO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.VIOLENCIA;
import static org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException.Reason.DISCARDED_TYPE;
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
 * Base com crimes de San Francisco e Oakland
 * 
 * http://oakland.crimespotting.org/
 * http://sanfrancisco.crimespotting.org/
 *
 * @author victor
 */
public class CrimespottingParser extends FileParser{

/*	
  		ORIGINAL			WIKICRIMES
  ---------------------------------------
	0	Case Number		=	CRI_ID_ORIGINAL
	1	Description		=	CRI_DESCRICAO
	2	Date, Time		=	CRI_DATA
	3	Weekday			=	-
	4	Time			=	-
	5	Crime Type		=	TCR_IDTIPO_CRIME e TVI_IDTIPO_VITIMA
	6	Police Beat		=	?
	7	Zip Code		=	-
	8	Address			=	CRI_ENDERECO
	9	Latitude		=	CRI_LATITUDE
	10	Longitude		=	CRI_LONGITUDE
	11	Accuracy		=	-
	12	URL				=	CRI_LINK_NOTICIA
*/	
	
	public static enum CityOption {
		OAKLAND, SAN_FRANCISCO;
	}
	
	private CityOption city;
	
	private static final char separator = ',';
	private static final char quote = '"';
	
	public CrimespottingParser(CityOption city, File file) throws IOException, ClassNotFoundException, SQLException{
		super(file);
		this.city = city;
	}
	
	@Override
	protected String getBaseName() {
		switch(city){
		case OAKLAND:
			return "oakland";
		case SAN_FRANCISCO:
			return "san francisco";
		default:
			throw new RuntimeException();
		}
	}

	@Override
	protected String getBaseUrl() {
		switch(city){
		case OAKLAND:
			return "http://oakland.crimespotting.org/";
		case SAN_FRANCISCO:
			return "http://sanfrancisco.crimespotting.org/";
		default:
			throw new RuntimeException();
		}
	}
	
	@Override
	public Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException{
		String[] rawFields = FileParser.splitFields(rawData, separator, quote);
		Model crime = new Model();
		crime.CRI_ID_ORIGINAL = rawFields[0];
		crime.CRI_DESCRICAO = rawFields[1];
		crime.CRI_DATA = convertDate(rawFields[2]);
		convertCrimeType(crime, rawFields[5]);
		crime.CRI_ENDERECO = rawFields[8];
		crime.CRI_LATITUDE = Double.valueOf(rawFields[9]);
		crime.CRI_LONGITUDE = Double.valueOf(rawFields[10]);
		crime.CRI_LINK_NOTICIA = rawFields[12];
		setLocationFields(crime);
		return crime;
	}
	
	private static Timestamp convertDate(String str) throws ParseException{
		str = str.substring(1,str.length()-1);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = df.parse(str);
		return new Timestamp(date.getTime());
	}
	
	private static void convertCrimeType(Model crime, String str) throws IgnoredCrimeException{
		
		if(str.charAt(0) == '"' && str.charAt(str.length()-1) == '"'){
			str = str.substring(1, str.length()-1);
		}
		
		if(str.equals("AGGRAVATED ASSAULT")){ //agressao com arma
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("ALCOHOL")){ 
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("ARSON")){ //incendio culposo
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("BURGLARY")){ //assalto a estabelecimento, arrombamento
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("DISTURBING THE PEACE")){
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("MURDER")){ //assassinato
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = HOMICIDIO;
		}else if(str.equals("NARCOTICS")){
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("PROSTITUTION")){
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("ROBBERY")){ //assalto a pessoa
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("SIMPLE ASSAULT")){ //agressao, violencia domestica, ameaca
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = VIOLENCIA_DOMESTICA;
		}else if(str.equals("THEFT")){ //furto
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("VANDALISM")){
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("VEHICLE THEFT")){ //furto de veiculo
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_VEICULO;
		}else{
			throw new IgnoredCrimeException(UNKNOWN_TYPE, str);
		}
	}
	
	private void setLocationFields(Model crime){
		switch(city){
		case OAKLAND:
			crime.CRI_CIDADE = "Oakland";
			break;
		case SAN_FRANCISCO:
			crime.CRI_CIDADE = "San Francisco";
			break;
		}
		crime.CRI_ESTADO = "California";
		crime.CRI_PAIS = "USA";
	}
	
}
