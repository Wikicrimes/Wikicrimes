package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.wikicrimes.model.AreaObservacao;
import org.wikicrimes.model.PeriodoInformacao;
import org.wikicrimes.model.PontosArea;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.UsuarioService;

public class AreaForm extends GenericForm{

	private Usuario usuario;
	
	private UsuarioService usuarioService;
	
	private AreaObservacao areaObservacao;
	
	private List<SelectItem> periodoInformacao;
	
	private String periodoInfSelecionado;
	
	private String latitudes;
	
	private String longitudes;
	
	private String areaKm2;
	
	private AreaObservacao areaExcluir;
	
	private boolean primeiraVez = true;
	
	private List<AreaObservacao> areas;
	
	public String getAreaKm2() {
		return areaKm2;
	}

	public void setAreaKm2(String areaKm2) {
		this.areaKm2 = areaKm2;
	}

	public AreaForm(){
		areaObservacao = new AreaObservacao();
		//usuario = new Usuario();
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		if(FacesContext.getCurrentInstance().getViewRoot()!=null){
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
			periodoInformacao = new ArrayList<SelectItem>();	
			periodoInformacao.add(new SelectItem("2", bundle.getString("webapp.area.label.diariamente")));
			periodoInformacao.add(new SelectItem("3", bundle.getString("webapp.area.label.semanalmente")));
			periodoInformacao.add(new SelectItem("1", bundle.getString("webapp.area.label.mensalmente")));
		}
		usuario = (Usuario)this.getSessionScope().get("usuario");
		
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	
	public Integer getQtdAreas(){
		if (!expirouSessao()){
			if (areas == null){
				areas = usuarioService.getAreas(usuario);
				primeiraVez = false;
			}
			return areas.size();
		}
		else{
			return -1;
		}
		
	}
	
	public List<AreaObservacao> getAreas(){
		if (!expirouSessao()){
			if(primeiraVez){
				areas = usuarioService.getAreas(usuario);
				primeiraVez = false;
			}	
			
			return areas;
		}
		else{
			areas = new ArrayList<AreaObservacao>();
			return areas;
		}
	}
	
	public Boolean getMostraAlertaQtdAreas(){
		if (!expirouSessao()){
			Integer qtdAreas = usuarioService.getAreas(usuario).size(); 
			if(qtdAreas==2){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public Boolean getMostraAlertaTamArea(){
		if (!expirouSessao()){
			if(areaKm2!=null && !areaKm2.equalsIgnoreCase("")){
				Double areaKm2Long = Double.parseDouble(areaKm2);
			
				if(areaKm2Long >2000000){
					return true;
				}
				else{
					return false;
				}
			}
			else
				return false;
			}
		else{
			return false;
		}
	}
	
	public Boolean getPodeRegistrar(){
		if (!expirouSessao()){
			if(getMostraAlertaTamArea()|| getMostraAlertaQtdAreas()){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return true;
		}
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public AreaObservacao getAreaObservacao() {
		return areaObservacao;
	}

	public void setAreaObservacao(AreaObservacao areaObservacao) {
		this.areaObservacao = areaObservacao;
	}
	
	public List<SelectItem> getPeriodoInformacao() {
		return periodoInformacao;
	}

	public void setPeriodoInformacao(List<SelectItem> periodoInformacao) {
		this.periodoInformacao = periodoInformacao;
	}
	
	
	public String cadastraArea(){
		//String returnPage = FAILURE;
		if (expirouSessao()) {
			return SESSAO_EXPIRADA;
		}
		//areaObservacao = new AreaObservacao();
		PeriodoInformacao periodo = new PeriodoInformacao();
		periodo.setIdPeriodoInformacao(Long.parseLong(periodoInfSelecionado));
		areaObservacao.setPeriodoInformacao(periodo);
		
		String[] lats = latitudes.split(";");
		String[] lngs = longitudes.split(";");
		Set<PontosArea> pontos = new HashSet<PontosArea>();
		PontosArea ponto = new PontosArea();
		for(int i = 0 ;  i < lats.length;i++){
			ponto.setLatitude(Double.parseDouble(lats[i]));
			ponto.setLongitude(Double.parseDouble(lngs[i]));
			ponto.setOrdemCriacao(new Integer(i));
			
			pontos.add(ponto);
			ponto = new PontosArea();
			
		}
		areaObservacao.setUsuario(usuario);
		areaObservacao.setPontos(pontos);
		areaObservacao.setDataHoraRegistro(new Date());
		usuarioService.cadastrarAreaObservacao(areaObservacao);	
		
		return SUCCESS;
	}
	
	public String excluirArea(){
		//System.out.println(areaExcluir.getNome());
		usuarioService.excluirAreaObservacao(areaExcluir);
		areas.remove(areaExcluir);
		return null;
	}

	public String getPeriodoInfSelecionado() {
		return periodoInfSelecionado;
	}

	public void setPeriodoInfSelecionado(String periodoInfSelecionado) {
		this.periodoInfSelecionado = periodoInfSelecionado;
	}

	public String getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(String latitudes) {
		this.latitudes = latitudes;
	}

	public String getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(String longitudes) {
		this.longitudes = longitudes;
	}

	public AreaObservacao getAreaExcluir() {
		return areaExcluir;
	}

	public void setAreaExcluir(AreaObservacao areaExcluir) {
		this.areaExcluir = areaExcluir;
	}

	
	public void setAreas(List<AreaObservacao> areas) {
		this.areas = areas;
	}

	

}
