package org.wikicrimes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImagemMapa  extends BaseObject{

	private static final long serialVersionUID = 1L;

	private Long idImagemMapa;
	private String token;//string criptografada pra esconder o id
	
	//centro, zoom , size
	private PontoLatLng centro;
	private Integer zoom;
	private Integer width;
	private Integer height;
	private Integer viewedQrCode;
	
	//poligono
	private List<PontoLatLng> poligono;
	
	//bounds
	private Double north;
	private Double south;
	private Double east;
	private Double west;
	
	//filtro
	private Map<String, String> filtro;
	
	//usuario, data
	private Usuario usuario;
	private Date dataHoraRegistro;
	
	public List<PontoLatLng> setPoligonoAndReturn(String vertices){
		List<PontoLatLng> listaPontos = new ArrayList<PontoLatLng>();
		String[] pts = vertices.split("\\|");
		for(String ptStr : pts){
			PontoLatLng pt = new PontoLatLng(ptStr);
			listaPontos.add(pt);
		}
		setPoligono(listaPontos);
		return listaPontos;
	}
	
	public PontoLatLng getCentro() {
		return centro;
	}
	public void setCentro(PontoLatLng centro) {
		this.centro = centro;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
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
	public Long getIdImagemMapa() {
		return idImagemMapa;
	}
	public void setIdImagemMapa(Long idImagemMapa) {
		this.idImagemMapa = idImagemMapa;
	}
	public List<PontoLatLng> getPoligono() {
		return poligono;
	}
	public void setPoligono(List<PontoLatLng> poligono) {
		this.poligono = poligono;
	}
	public Double getNorth() {
		return north;
	}
	public void setNorth(Double north) {
		this.north = north;
	}
	public Double getSouth() {
		return south;
	}
	public void setSouth(Double south) {
		this.south = south;
	}
	public Double getEast() {
		return east;
	}
	public void setEast(Double east) {
		this.east = east;
	}
	public Double getWest() {
		return west;
	}
	public void setWest(Double west) {
		this.west = west;
	}
	public Map<String, String> getFiltro() {
		return filtro;
	}
	public void setFiltro(Map<String, String> filtro) {
		this.filtro = filtro;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getViewedQrCode() {
		if(viewedQrCode == null)
			return 0;
		return viewedQrCode;
	}
	public void setViewedQrCode(Integer viewedQrCode) {
		this.viewedQrCode = viewedQrCode;
	}
}
