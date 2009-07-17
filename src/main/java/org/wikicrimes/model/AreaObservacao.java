package org.wikicrimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;

public class AreaObservacao extends BaseObject {

	private static final long serialVersionUID = -8497302325093089638L;
	private Integer idAreaObservacao;	
	private Usuario usuario;
	private String nome;
	private Date dataHoraRegistro;
	
	private Set<PontosArea> pontos;
	//Não mapeado
	private String pontosModoTexto;
	//Não mapeado
	private PeriodoInformacao periodoInformacao;
	
	public PeriodoInformacao getPeriodoInformacao() {
		return periodoInformacao;
	}
	public void setPeriodoInformacao(PeriodoInformacao periodoInformacao) {
		this.periodoInformacao = periodoInformacao;
	}
	public Integer getIdAreaObservacao() {
		return idAreaObservacao;
	}
	public void setIdAreaObservacao(Integer idAreaObservacao) {
		this.idAreaObservacao = idAreaObservacao;
	}	
	public Set<PontosArea> getPontos() {
		return pontos;
	}
	public void setPontos(Set<PontosArea> pontos) {
		this.pontos = pontos;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPontosModoTexto() {
		pontosModoTexto = "";
		BeanComparator ordenaPontos = new BeanComparator("ordemCriacao",   
		    new Comparator() {  
		      public int compare(Object o1, Object o2) {  
		        return ((Integer)o2).compareTo((Integer)o1);  
		      }  
		    }  
		);
		List<PontosArea> listaPontosOrdenada = new ArrayList<PontosArea>();
		for (Iterator iterator = pontos.iterator(); iterator.hasNext();) {
			PontosArea ponto = (PontosArea) iterator.next();
			listaPontosOrdenada.add(ponto);
		}
		Collections.sort(listaPontosOrdenada, ordenaPontos);
		for (Iterator iterator = listaPontosOrdenada.iterator(); iterator.hasNext();) {
			PontosArea ponto = (PontosArea) iterator.next();
			pontosModoTexto+=ponto.getLatitude()+";";
			pontosModoTexto += ponto.getLongitude()+"|";
		}
		return pontosModoTexto;
	}
	public void setPontosModoTexto(String pontosModoTexto) {
		this.pontosModoTexto = pontosModoTexto;
	}
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}
	public void setDataHoraRegistro(Date dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}
}
