package org.wikicrimes.util.crimeBaseImport;

/**
 * 
 * Pra informar (pro metodo mais externo) q um crime deve ser descartado. Ex: pq eh de um tipo q nao existe na base do Wikicrimes
 * 
 * @author victor
 *
 */
@SuppressWarnings("serial")
public class IgnoredCrimeException extends Exception{

	public static enum Reason {
		PREVIOUSLY_IMPORTED, //ja existe no banco um crime com mesmo id_original e id_base_origem 
		DISCARDED_TYPE, //tipo de crime nao existe no banco, ex: prostituicao, drogas, fraude, incendio culposo, etc
		UNKNOWN_TYPE, //tipo de crime desconhecido, nao incluido no codigo do converter
		INSUFICIENT_DATA; //dados insuficientes, algum campo obritgatorio ficou null
	}
	
	public final Reason reason;
	public String details;
	public String rawData;
	public Parser converter;
	
	public IgnoredCrimeException(Reason reason){
		super("Crime ignorado, razao: " + reason);
		this.reason = reason;
	}
	
	public IgnoredCrimeException(Reason reason, String details){
		super("Crime ignorado, razao: " + reason + " (" + details + ")");
		this.reason = reason;
		this.details = details;
	}
	
	public void setRawData(String str){
		this.rawData = str;
	}
	
	public void setConverter(Parser converter){
		this.converter = converter;
	}
	
}
