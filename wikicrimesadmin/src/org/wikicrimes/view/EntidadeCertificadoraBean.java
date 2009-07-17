package org.wikicrimes.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikicrimes.business.ECBusiness;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;

public class EntidadeCertificadoraBean {

		private EntidadeCertificadora entidadeCertificadora;
		
		private List<BaseObject> entidadeCertificadoras;
		
		private ECBusiness entidadeCertificadoraBusiness;
		
		private Integer qtdECs;
		
		private Integer qtdTotalECs;
		
		private EntidadeCertificadora ecEditar;
		
		private EntidadeCertificadora ecReg;

		
		public EntidadeCertificadoraBean(){
			entidadeCertificadora = new EntidadeCertificadora();
			ecReg = new EntidadeCertificadora();
		}
		
		public String zerar(){
			entidadeCertificadoras =  null;
			qtdECs = -1;
			entidadeCertificadora = new EntidadeCertificadora();
			qtdTotalECs = entidadeCertificadoraBusiness.getEntidadeCertificadoraAll().size();
			return "EC";
		}
		
		public String cadastrar(){
			entidadeCertificadoraBusiness.cadastrar(ecReg);
			this.zerar();
			return "EC";
		}
		
		public String getTotalECs(){
			entidadeCertificadoras = entidadeCertificadoraBusiness.getEntidadeCertificadoraAll();
			qtdECs = entidadeCertificadoras.size();
			return "EC";
		}
		
		public String salvar(){
			entidadeCertificadoraBusiness.update(ecEditar);
			return "EC";
		}
		
				
		public String consultarEC(){
			try {
				Map parameters = new HashMap();

				if (entidadeCertificadora.getNome() != null && entidadeCertificadora.getNome() !="") {
					parameters.put("nomeEC", new String(entidadeCertificadora.getNome()));
				}

				if (entidadeCertificadora.getDescricao() != null && entidadeCertificadora.getDescricao() != "" ) {
					parameters.put("descricaoEC", new String(entidadeCertificadora.getDescricao()));
				}

				if (entidadeCertificadora.getHomepage() != null && entidadeCertificadora.getHomepage() !="") {
					parameters.put("homepageEC", new String(entidadeCertificadora.getHomepage()));
				}

				entidadeCertificadoras=(List<BaseObject>) entidadeCertificadoraBusiness.filter(parameters);

			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();


			}
			qtdECs = entidadeCertificadoras.size();
			return null;
		}
		
		public EntidadeCertificadora getEntidadeCertificadora() {
			return entidadeCertificadora;
		}

		public void setEntidadeCertificadora(EntidadeCertificadora entidadeCertificadora) {
			this.entidadeCertificadora = entidadeCertificadora;
		}


		public EntidadeCertificadora getEcEditar() {
			return ecEditar;
		}


		public void setEcEditar(EntidadeCertificadora ecEditar) {
			this.ecEditar = ecEditar;
		}

		
		public ECBusiness getEntidadeCertificadoraBusiness() {
			return entidadeCertificadoraBusiness;
		}


		public void setEntidadeCertificadoraBusiness(
				ECBusiness entidadeCertificadoraBusiness) {
			this.entidadeCertificadoraBusiness = entidadeCertificadoraBusiness;
		}


		public Integer getQtdECs() {
			return qtdECs;
		}


		public void setQtdECs(Integer qtdECs) {
			this.qtdECs = qtdECs;
		}


		public List<BaseObject> getEntidadeCertificadoras() {
			return entidadeCertificadoras;
		}


		public void setEntidadeCertificadoras(List<BaseObject> entidadeCertificadoras) {
			this.entidadeCertificadoras = entidadeCertificadoras;
		}

		public Integer getQtdTotalECs() {
			return qtdTotalECs;
		}

		public void setQtdTotalECs(Integer qtdTotalECs) {
			this.qtdTotalECs = qtdTotalECs;
		}

		public EntidadeCertificadora getEcReg() {
			return ecReg;
		}

		public void setEcReg(EntidadeCertificadora ecReg) {
			this.ecReg = ecReg;
		}
			
}
