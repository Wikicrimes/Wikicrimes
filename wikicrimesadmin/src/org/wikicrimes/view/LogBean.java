package org.wikicrimes.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikicrimes.business.LogBusiness;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Usuario;

public class LogBean {
	
	private Log log;
	
	private Crime crime;
	
	private Usuario usuario;
	
	private List<BaseObject> logs;
	
	private Date data;
	
	private Integer qtdLogs;
	
	private LogBusiness logBusiness;
	
	public LogBean(){
		log= new Log();
	}
	
	public String filtroUser(){
		this.zerar(0);
		logs= logBusiness.getLogsUser(usuario.getIdUsuario().toString());
		qtdLogs= logs.size();
		if(qtdLogs!=0){
			Log tmp = (Log)logs.get(0);
			log = new Log();
			log.setIdObj(tmp.getIdObj());
			log.setUsuario(new Usuario());
		}
		return "log";
	}
	
	public String filtroCrime(){
		this.zerar(-1);
		logs= logBusiness.getLogsCrime(crime.getIdCrime().toString());
		qtdLogs= logs.size();
		if(qtdLogs == 0)
			qtdLogs= -1;
		else{
			Log tmp = (Log)logs.get(0);
			log = new Log();
			log.setIdObj(tmp.getIdObj());
			log.setUsuario(new Usuario());
		}
					
			
			
		return "log";
	}
	
	public String consultarLogs() {

		try {
			Map parameters = new HashMap();
			parameters.put("idObj", new String(log.getIdObj().toString()));
			
			if (log.getUsuario().getPrimeiroNome() != null ) {
				parameters.put("primeiroNome", new String(log.getUsuario().getPrimeiroNome()));
			}
			if (data != null ) {
				parameters.put("data", data);
			}
			if (log.getCampo() != null ) {
				parameters.put("campo", log.getCampo());
			}
			logs = new ArrayList<BaseObject>();
			logs=(List<BaseObject>) logBusiness.filter(parameters);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();


		}
		qtdLogs = logs.size();
		return null;
	}


	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	public void zerar(Integer i){
		if(i==0)
		qtdLogs= 0;
		else 
			qtdLogs= -1;
		logs = null;
		log = null;
		data = null;
	}

	public LogBusiness getLogBusiness() {
		return logBusiness;
	}

	public void setLogBusiness(LogBusiness logBusiness) {
		this.logBusiness = logBusiness;
	}

	public List<BaseObject> getLogs() {
		return logs;
	}

	public void setLogs(List<BaseObject> logs) {
		this.logs = logs;
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getQtdLogs() {
		return qtdLogs;
	}

	public void setQtdLogs(Integer qtdLogs) {
		this.qtdLogs = qtdLogs;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}