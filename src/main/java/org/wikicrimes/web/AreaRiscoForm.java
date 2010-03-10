package org.wikicrimes.web;

import java.util.List;

import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.AreaRiscoService;


public class AreaRiscoForm extends GenericForm{
	
	private AreaRiscoService areaRiscoService;
	private Usuario usuario;
	private List<AreaRisco> minhasAreas;
	private AreaRisco areaPraExcluir;

	public void excluirArea(){
		areaRiscoService.delete(areaPraExcluir);
	}
	
	public AreaRiscoService getAreaRiscoService() {
		return areaRiscoService;
	}
	public void setAreaRiscoService(AreaRiscoService areaRiscoService) {
		this.areaRiscoService = areaRiscoService;
	}
	public List<AreaRisco> getMinhasAreas() {
		minhasAreas = areaRiscoService.listAreas(usuario);
		return minhasAreas;
	}
	public void setMinhasAreas(List<AreaRisco> minhasAreas) {
		this.minhasAreas = minhasAreas;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public AreaRisco getAreaPraExcluir() {
		return areaPraExcluir;
	}
	public void setAreaPraExcluir(AreaRisco areaPraExcluir) {
		this.areaPraExcluir = areaPraExcluir;
	}
	
}
