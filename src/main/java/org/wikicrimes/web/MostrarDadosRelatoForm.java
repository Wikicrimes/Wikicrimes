
package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.ConfirmacaoRelatoService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.util.Constantes;

public class MostrarDadosRelatoForm extends GenericForm {

	private final Log log = LogFactory.getLog(MostrarDadosRelatoForm.class);

	private Relato relato = null;

	private RelatoService relatoService;
	
	private EmailService emailService;

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	private Long idRelato;
	
	private List<SelectItem> razoes = null;
	
	private List<String> razoesSel = new ArrayList<String>();
	
	private ConfirmacaoRelato confirmacaoRelato;
	
	private ConfirmacaoRelatoService confirmacaoRelatoService;
	
	public ConfirmacaoRelatoService getConfirmacaoRelatoService() {
		return confirmacaoRelatoService;
	}

	public void setConfirmacaoRelatoService(
			ConfirmacaoRelatoService confirmacaoRelatoService) {
		this.confirmacaoRelatoService = confirmacaoRelatoService;
	}

	public MostrarDadosRelatoForm() {
		relato = new Relato();
		confirmacaoRelato = new ConfirmacaoRelato();
		confirmacaoRelato.setRelato(relato);
	}

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public RelatoService getRelatoService() {
		return relatoService;
	}

	public void setRelatoService(RelatoService relatoService) {
		this.relatoService = relatoService;
	}

	public Long getIdRelato() {
		return idRelato;
	}

	public void setIdRelato(Long idRelato) {
		this.idRelato = idRelato;
		if (idRelato != null) {
			relato = (Relato) relatoService.get(idRelato);			
			confirmacaoRelato.setRelato(relato);
			List lst = relatoService.listarRazoesSelecionadas(relato);
			for (Razao razaoSel : (List<Razao>)lst) {
				razoesSel.add(razaoSel.getIdRazao().toString());
			}
			
			//relatoService.atualizaVisualizacoes(relato);
		}
	}
	
	public List<String> getRazoesSel() {
		return razoesSel;
	}

	public void setRazoesSel(List<String> razoes) {
		this.razoesSel = razoes;
	}	
	
	public List<SelectItem> getRazoes() {
		
		if(razoes==null){
			razoes = new ArrayList<SelectItem>();
			List<BaseObject> razoesBanco = relatoService.listarRazoes();
			ApplicationFactory factory = (ApplicationFactory) FactoryFinder
			.getFactory(FactoryFinder.APPLICATION_FACTORY);
			String bundleName = factory.getApplication().getMessageBundle();
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
				
			for (Iterator iterator = razoesBanco.iterator(); iterator.hasNext();) {
				Razao r = (Razao) iterator.next();
				//11 12 13 14, 18 (A pedido do Vasco nao utilizar essas causas)
				//Tire as opcoes, proximidade, impunidade, pistolagem, omissão de testemunhas e crime passional .
				if (r.getIdRazao().intValue() < 11 || r.getIdRazao().intValue() > 14 && r.getIdRazao().intValue() != 18)
					razoes.add(new SelectItem(r.getIdRazao().toString(),bundle.getString(r.getNome())));
			}
		}	
		return razoes;
	}

	public void setRazoes(List<SelectItem> razoes) {
		this.razoes = razoes;
	}
	
	public String simConfirma(){
		confirmacaoRelato.setConfirma(Constantes.SIM);
		return confirmaRelato();		
	}
	
	public String naoConfirma(){
		confirmacaoRelato.setConfirma(Constantes.NAO);
		return confirmaRelato();
	}
	
	protected String confirmaRelato() {
		String returnPage = FAILURE;
		
		try {
			//boolean opcao = confirmacaoRelato.getConfirma();
			//confirmacaoRelato.setConfirma(null);
			Usuario usuarioLogado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
			Relato relato2 = (Relato) relatoService.get(relato.getIdRelato());			
			confirmacaoRelato.setRelato(relato2);
			confirmacaoRelato.setUsuario(usuarioLogado);
			if(confirmacaoRelatoService.getJaConfirmou(confirmacaoRelato) || relato.getIdRelato()==0){
				addMessage("relato.ja.confirmado", "");
				return null;
			
			}	
			else{	
				confirmacaoRelato.setDataConfirmacao(new Date());
				//confirmacaoRelato.setConfirma(opcao);
				confirmacaoRelato.setIndicacao(Constantes.NAO);
			
				if(confirmacaoRelatoService.insert(confirmacaoRelato)){
					relatoService.increntaNumConfirmacoes(confirmacaoRelato.getRelato(),confirmacaoRelato.getConfirma());
					//verificar se somente é relato de orkut ou de usuario wikicrimes
					if (confirmacaoRelato.getRelato().getUsuario()!=null)
						emailService.enviarEmailNotificacao(confirmacaoRelato, FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());
					addMessage("confirmacao.realizada", "");
					returnPage = SUCCESS;
				}	
			}	
			
//			if (confirmacaoRelato.getIdConfirmacao() != null) {				
//					confirmacaoRelato.setDataConfirmacao(new Date());
//					if (confirmacaoRelatoService.update(confirmacaoRelato)) {
//	
//	//					crimeService.atualizaContador(confirmacao.getConfirma(),
//	//							confirmacao.getCrime());
//	
//						addMessage("confirmacao.realizada", "");
//						returnPage = SUCCESS;
//					} else {
//						addMessage("errors.geral",
//								"Erro ao tentar realizar uma confirmação de crime.");
//					}				
//			}		

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}
	
}
