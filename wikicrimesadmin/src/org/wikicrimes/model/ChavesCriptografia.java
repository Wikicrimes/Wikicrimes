package org.wikicrimes.model;

/**
 * 
 * @author philipp
 *
 */
public class ChavesCriptografia extends BaseObject{

	private static final long serialVersionUID = -1354176944742740648L;
	
	private String chavePublica,chavePrivada,site;
	private Long idChave;
	
	
	public String getChavePublica() {
		return chavePublica;
	}
	public void setChavePublica(String chavePublica) {
		this.chavePublica = chavePublica;
	}
	public String getChavePrivada() {
		return chavePrivada;
	}
	public void setChavePrivada(String chavePrivada) {
		this.chavePrivada = chavePrivada;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public Long getIdChave() {
		return idChave;
	}
	public void setIdChave(Long idChave) {
		this.idChave = idChave;
	}
}
