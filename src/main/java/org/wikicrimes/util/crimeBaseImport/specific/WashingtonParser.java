package org.wikicrimes.util.crimeBaseImport.specific;

import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_OUTROS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_VEICULO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.ATENTADO_AO_PUDOR;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.HOMICIDIO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PESSOA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PROPRIEDADE;
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
 * Base com crimes de Washington
 * 
 * http://data.octo.dc.gov/Main_DataCatalog.aspx
 *
 * @author victor
 */
public class WashingtonParser extends FileParser{

/*
	  
		ORIGINAL			WIKICRIMES
  ---------------------------------------
	0  	NID								=	CRI_ID_ORIGINAL (parte 1)
	1  	CCN								=	CRI_ID_ORIGINAL (parte 2)
	2  	REPORTDATETIME					=	CRI_DATA
	3  	SHIFT							=	
	4  	OFFENSE							=	CRI_IDTIPO_CRIME e TVI_IDTIPO_VITIMA
	5  	METHOD							=	CRI_DESCRICAO
	6  	LASTMODIFIEDDATE				=	
	7  	BLOCKSITEADDRESS				=	CRI_ENDERECO
	8  	LATITUDE						=	CRI_LATITUDE
	9  	LONGITUDE						=	CRI_LONGITUDE
	10 	CITY							=	
	11 	STATE							=	
	12 	WARD							=	
	13 	ANC								=	
	14 	SMD								=	
	15 	DISTRICT						=	
	16 	PSA								=	
	17 	NEIGHBORHOODCLUSTER				=	
	18 	HOTSPOT2006NAME					=	
	19 	HOTSPOT2005NAME					=	
	20 	HOTSPOT2004NAME					=	
	21 	BUSINESSIMPROVEMENTDISTRICT		=	
*/
	
	private static final char separator = ',';
	private static final char quote = '"';
	
	public WashingtonParser(File file) throws IOException, ClassNotFoundException, SQLException {
		super(file);
	}
	
	@Override
	protected String getBaseName() {
		return "washington";
	}

	@Override
	protected String getBaseUrl() {
		return "http://data.octo.dc.gov/Main_DataCatalog.aspx";
	}
	
	@Override
	protected Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException {
		String[] rawFields = FileParser.splitFields(rawData, separator, quote);
		Model crime = new Model();
		crime.CRI_ID_ORIGINAL = rawFields[0] + "/" + rawFields[1];
		crime.CRI_DATA = convertDate(rawFields[2]);
		crime.CRI_ENDERECO = rawFields[7];
		convertCrimeType(crime, rawFields[4]);
		crime.CRI_LATITUDE = Double.valueOf(rawFields[8]);
		crime.CRI_LONGITUDE = Double.valueOf(rawFields[9]);
		crime.CRI_DESCRICAO = rawFields[5];
		setLocationFields(crime);
		return crime;
	}
	
	private static Timestamp convertDate(String str) throws ParseException{
		DateFormat df = new SimpleDateFormat("M/d/yyyy H:mm:ss a");
		Date date = df.parse(str);
		return new Timestamp(date.getTime());
	}
	
	private void convertCrimeType(Model crime, String str) throws IgnoredCrimeException{
		if(str.equals("ADW")){
			
		}else if(str.equals("ARSON")){ //incendio culposo
			throw new IgnoredCrimeException(DISCARDED_TYPE, str);
		}else if(str.equals("BURGLARY")){ //assalto a estabelecimento, arrombamento
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("HOMICIDE")){
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = HOMICIDIO;
		}else if(str.equals("ROBBERY")){ //assalto a pessoa
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("SEX ABUSE")){
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = ATENTADO_AO_PUDOR;
		}else if(str.equals("STOLEN AUTO")){
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_VEICULO;
		}else if(str.equals("THEFT")){ //furto
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("THEFT F/AUTO")){
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_VEICULO;
		}else{
			throw new IgnoredCrimeException(UNKNOWN_TYPE, str);
		}
	}
	
	private static void setLocationFields(Model crime){
		crime.CRI_CIDADE = "Washington";
		crime.CRI_PAIS = "USA";
	}

}
