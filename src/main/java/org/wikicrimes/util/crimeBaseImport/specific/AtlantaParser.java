package org.wikicrimes.util.crimeBaseImport.specific;

import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_OUTROS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.LocalCrime.PROPRIEDADE_VEICULO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.HOMICIDIO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.NAO_INFORMADO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PESSOA;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.PROPRIEDADE;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.SubtipoCrime.RIXAS_OU_BRIGAS;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.FURTO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.ROUBO;
import static org.wikicrimes.util.crimeBaseImport.DBConstants.TipoCrime.VIOLENCIA;
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
 * Base com crimes de Atlanta
 * 
 * http://www.atlantapd.org/
 *
 * @author victor
 */
public class AtlantaParser extends FileParser{

/*	
  		ORIGINAL			WIKICRIMES
  ---------------------------------------
	0	MI_PRINX			=	CRI_ID_ORIGINAL (parte 1)
	1	offense_id			=	CRI_ID_ORIGINAL (parte 2)
	2	rpt_date			=	-
	3	occur_date			=	CRI_DATA (data)
	4	occur_time			=	CRI_DATA (hora)
	5	poss_date			=	
	6	poss_time			=	
	7	beat				=	
	8	apt_office_prefix	=	
	9	apt_office_num		=	
	10	location			=	CRI_ENDERECO
	11	MinOfucr			=	
	12	MinOfibr_code		=	
	13	dispo_code			=	
	14	MaxOfnum_victims	=	
	15	Shift				=	
	16	Avg Day				=	
	17	loc_type			=	
	18	UC2 Literal			=	CRI_IDTIPO_CRIME e TVI_IDTIPO_VITIMA
	19	neighborhood		=	
	20	npu					=	
	21	x					=	CRI_LONGITUDE
	22	y					=	CRI_LATITUDE

*/	
	
	public AtlantaParser(File file) throws IOException, ClassNotFoundException, SQLException {
		super(file);
	}

	@Override
	protected String getBaseName() {
		return "atlanta";
	}

	@Override
	protected String getBaseUrl() {
		return "http://www.atlantapd.org/";
	}
	
	@Override
	public Model specificConvert(String rawData) throws ParseException, IgnoredCrimeException{
		String[] rawFields = splitFields(rawData, 10, 23);
//		String[] rawFields = CrimespottingConverter.splitFields(rawData, ',', '"');
		Model crime = new Model();
		crime.CRI_ID_ORIGINAL = rawFields[0] + "/" + rawFields[1];
		crime.CRI_DATA = convertDate(rawFields[3], rawFields[4]);
		crime.CRI_ENDERECO = rawFields[10];
		convertCrimeType(crime, rawFields[18]);
		crime.CRI_LATITUDE = Double.valueOf(rawFields[22]);
		crime.CRI_LONGITUDE = Double.valueOf(rawFields[21]);
		crime.CRI_DESCRICAO = rawFields[18];
		setLocationFields(crime);
		return crime;
	}
	
	/**
	 * campos separados por virgula
	 * virgulas podem ocorrer no meio de um campo especifico (o endereco)
	 */
	static String[] splitFields(String rawData, int fieldIndex, int fieldCount) throws ParseException{
		String[] rawFields = rawData.split(",");
		if(rawFields.length > fieldCount){ //tem virgulas no endereco
			int dif = rawFields.length - fieldCount;
			String[] fixedRawFields = new String[fieldCount];
			for(int i=0; i<fieldIndex; i++){
				fixedRawFields[i] = rawFields[i]; 
			}
			fixedRawFields[fieldIndex] = "";
			for(int i=fieldIndex; i<fieldIndex+dif; i++){
				//reagrupar partes do endereco separadas por virgula
				fixedRawFields[fieldIndex] += "," + rawFields[i]; 
			}
			fixedRawFields[fieldIndex] = fixedRawFields[fieldIndex].substring(1);
			for(int i=fieldIndex+dif; i<rawFields.length; i++){
				fixedRawFields[i-dif] = rawFields[i]; 
			}
			return fixedRawFields;
		}else{
			return rawFields;
		}
	}
	
	private static Timestamp convertDate(String dateStr, String hourStr) throws ParseException{
		String datehourStr = dateStr;
		if(!hourStr.equals("NULL")){
			dateStr = dateStr.split(" ")[0];
			hourStr = hourStr.split(" ")[1];
			datehourStr = dateStr + " " + hourStr;
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Date date = df.parse(datehourStr);
		return new Timestamp(date.getTime());
	}
	
	private static void convertCrimeType(Model crime, String str) throws IgnoredCrimeException{
		if(str.equals("AGG ASSAULT")){ //agressao com arma
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = RIXAS_OU_BRIGAS;
		}else if(str.equals("AUTO THEFT")){ 
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_VEICULO;
		}else if(str.equals("BURGLARY")){ //assalto a estabelecimento, arrombamento
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PROPRIEDADE;
			crime.TLO_IDTIPO_LOCAL = PROPRIEDADE_OUTROS;
		}else if(str.equals("LARCENY")){ //pequeno furto
			crime.TCR_IDTIPO_CRIME = FURTO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("ROBBERY")){ //assalto a pessoa
			crime.TCR_IDTIPO_CRIME = ROUBO;
			crime.TVI_IDTIPO_VITIMA = PESSOA;
		}else if(str.equals("RAPE")){ //estupro
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = NAO_INFORMADO;
		}else if(str.equals("HOMICIDE")){
			crime.TCR_IDTIPO_CRIME = VIOLENCIA;
			crime.TVI_IDTIPO_VITIMA = HOMICIDIO;
		}else{
			throw new IgnoredCrimeException(UNKNOWN_TYPE, str);
		}
	}
	
	private static void setLocationFields(Model crime){
		crime.CRI_CIDADE = "Atlanta";
		crime.CRI_ESTADO = "Georgia";
		crime.CRI_PAIS = "USA";
	}

}
