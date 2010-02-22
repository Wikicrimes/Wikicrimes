package org.wikicrimes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AreaRisco extends BaseObject{
	
	private static final long serialVersionUID = 1L;

	private Long idAreaRisco;
	
	private String nome;
	private List<PontoLatLng> vertices;
	
	private Usuario usuario;
	private Date dataHoraRegistro;
	
	
	public List<PontoLatLng> setVerticesAndReturn(String vertices){
		List<PontoLatLng> listaPontos = new ArrayList<PontoLatLng>();
		String[] pts = vertices.split("\\|");
		for(String ptStr : pts){
			PontoLatLng pt = new PontoLatLng(ptStr);
			listaPontos.add(pt);
		}
		setVertices(listaPontos);
		return listaPontos;
	}
	public List<PontoLatLng> getVertices() {
		return vertices;
	}
	public void setVertices(List<PontoLatLng> vertices) {
		this.vertices = vertices;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getDataHoraRegistro() {
		return dataHoraRegistro;
	}
	public void setDataHoraRegistro(Date datahoraRegistro) {
		this.dataHoraRegistro = datahoraRegistro;
	}
	public Long getIdAreaRisco() {
		return idAreaRisco;
	}
	public void setIdAreaRisco(Long idAreaRisco) {
		this.idAreaRisco = idAreaRisco;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		String str = "";
		str += idAreaRisco + "|" + nome + "|";
		for(PontoLatLng p : vertices)
			str += p + "|";
		str = str.substring(0, str.length()-1);
		return str;
	}
	
	public static String listToString(List<AreaRisco> list){
		String str = "";
		if(!list.isEmpty()){
			for(AreaRisco a : list)
				str += a + "||";
			str = str = str.substring(0, str.length()-2);
		}
		return str;
	}
}
