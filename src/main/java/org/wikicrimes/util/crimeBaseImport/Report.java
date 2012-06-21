package org.wikicrimes.util.crimeBaseImport;

public class Report {

	Parser parser;
	int successCount, discardCount, repeatCount;
	
	public Report(Parser parser) {
		this.parser = parser;
	}

	public void countSuccess(Model crime){
		successCount++;
	}
	
	public void countException(IgnoredCrimeException exception){
		switch(exception.reason){
		case PREVIOUSLY_IMPORTED:
			repeatCount++;
			break;
		case DISCARDED_TYPE:
			discardCount++;
			writeToFile(exception.details);
			break;
		case UNKNOWN_TYPE:
			discardCount++;
			writeToFile(exception.details);
			break;
		case INSUFICIENT_DATA:
			discardCount++;
			writeToFile(exception.rawData);
			break;
		}
	}
	
	public void printSummary(){
		System.out.printf("%s: %d crimes importados, %d descartados, %d repetidos\n", parser.getBaseName(), successCount, discardCount, repeatCount);
	}
	
	private void writeToFile(String str){
		//TODO listar os tipos desconhecidos, pra pessoa incluir no parser 
		//TODO listar razoes dos outros descartes tb, pra pessoa checar se tah descartando certo
	}
	
}
