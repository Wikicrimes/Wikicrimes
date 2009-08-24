
package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.ConfirmacaoRelatoService;
import org.wikicrimes.service.ConfirmacaoService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Constantes;

public class MostrarDadosRelatoForm extends GenericForm {

	private final Log log = LogFactory.getLog(MostrarDadosRelatoForm.class);

	private Relato relato = null;
	
	private Relato relatoEditar = null;

	private RelatoService relatoService;
	
	private EmailService emailService;
	
	private String email1,email2;
	
	private String mensagemConf;
	
	private String chave;
	
	private UsuarioService usuarioService;
	
	private ConfirmacaoService confirmacaoService;

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	private Long idRelato;
	
	private String relatoIndicacao;
	
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
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		String chaveEditar = (String)session.getAttribute("chaveRelato");
		if(chaveEditar != null && !chaveEditar.equalsIgnoreCase("")){
			relato = relatoService.getRelato(chaveEditar);
			session.removeAttribute("chaveRelato");
		}
		
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
	
	public String prepararEditarRelato(){
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		session.setAttribute("chaveEditarRelato", relatoEditar.getChave());
		return "editarRelato";
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

	public Relato getRelatoEditar() {
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		String chaveEditar = (String)session.getAttribute("chaveEditarRelato");
		if(chaveEditar != null && !chaveEditar.equalsIgnoreCase("")){
			relatoEditar = relatoService.getRelato(chaveEditar);
			session.removeAttribute("chaveEditarRelato");
		}
		
		return relatoEditar;
	}

	public void setRelatoEditar(Relato relatoEditar) {
		this.relatoEditar = relatoEditar;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getMensagemConf() {
		return mensagemConf;
	}

	public void setMensagemConf(String mensagemConf) {
		this.mensagemConf = mensagemConf;
	}
	
	public String updateRelato(){
		
		if (expirouSessao()) {			
			return SESSAO_EXPIRADA;
		}
		
		Usuario usuarioLogado = (Usuario)facesContext.getExternalContext().getSessionMap().get("usuario");
		Set<ConfirmacaoRelato> confirmacoes = new HashSet<ConfirmacaoRelato>();
		ConfirmacaoRelato confirmacaoRelatoEditar = new ConfirmacaoRelato();
		
		if(mensagemConf!=null && mensagemConf.length()>255){
			addMessageFaces("mostrar.dados.textarea.tamanho.max", new Integer(255).toString(),"mostrarDadosRelatoForm");
			return null;
		}
		
		//validacao se os campos sao nulos
		if(email2.equals("") && email1.equals("")){
			addMessageFaces("errors.email.confirmacao", "","mostrarDadosRelatoForm");
			//addMessage("emails.campos.vazios","");
			return null;
		}
		
		//quando os dois campos tem emails iguais
		if(email1.equalsIgnoreCase(email2)){
			addMessageFaces("errors.email.confirmacao.iguais", "","mostrarDadosRelatoForm");
			//addMessage("emails.campos.vazios","");
			return null;
		}
		
		if(email1!=null && !email1.equals("")){
			if(confirmacaoService.verificaSeJaIndicou(relatoEditar, email1)){
				addMessageFaces("usuario.ja.indicado.relato", email1,"mostrarDadosRelatoForm");
				return null;
			}
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email1);
			if (usuarioConfirmacao == null) {
				confirmacaoRelatoEditar.setIndicacao(Constantes.SIM);
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email1,usuarioLogado.getIdiomaPreferencial());
			
			}
			if(relatoEditar.getUsuario()==null || !relatoEditar.getUsuario().getEmail().equals(usuarioLogado.getEmail()))
				confirmacaoRelatoEditar.setUsuarioIndicado(usuarioLogado);
			
			confirmacaoRelatoEditar.setMensagem(mensagemConf);
			confirmacaoRelatoEditar.setRelato(relatoEditar);
			confirmacaoRelatoEditar.setIndicacaoEmail(Constantes.SIM);
			confirmacaoRelatoEditar.setUsuario(usuarioConfirmacao);
			confirmacoes.add(confirmacaoRelatoEditar);
			//crimeEditar.getConfirmacoes().add(confirmacaoEditar);
			
		}
		
		if(email2!=null && !email2.equals("")){
			if(confirmacaoService.verificaSeJaIndicou(relatoEditar, email2)){
				addMessageFaces("usuario.ja.indicado.relato", email2,"mostrarDadosRelatoForm");
				return null;
			}
			confirmacaoRelatoEditar = new ConfirmacaoRelato();
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email2);
			if (usuarioConfirmacao == null) {
				confirmacaoRelatoEditar.setIndicacao(Constantes.SIM);
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email2,usuarioLogado.getIdiomaPreferencial());
			
			}
			
			if(!relatoEditar.getUsuario().getEmail().equals(usuarioLogado.getEmail()))
				confirmacaoRelatoEditar.setUsuario(usuarioLogado);
			
			confirmacaoRelatoEditar.setMensagem(mensagemConf);
			confirmacaoRelatoEditar.setRelato(relatoEditar);
			confirmacaoRelatoEditar.setIndicacaoEmail(Constantes.SIM);
			confirmacaoRelatoEditar.setUsuario(usuarioConfirmacao);
			//crimeEditar.getConfirmacoes().add(confirmacaoEditar);
			confirmacoes.add(confirmacaoRelatoEditar);
		}

		relatoService.update(relatoEditar,confirmacoes);
		addMessage("crime.atualizado.sucesso", "");
		
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		session.setAttribute("chaveRelato", relatoEditar.getChave());
		
		return "retornaDadosRelato";
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public ConfirmacaoService getConfirmacaoService() {
		return confirmacaoService;
	}

	public void setConfirmacaoService(ConfirmacaoService confirmacaoService) {
		this.confirmacaoService = confirmacaoService;
	}

	public String getRelatoIndicacao() {		
		return relatoIndicacao;
	}

	public void setRelatoIndicacao(String relatoIndicacao) {
		this.relatoIndicacao = relatoIndicacao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
		if (chave != null && !chave.equals("")) {
			relatoEditar = (Relato) relatoService.getRelato(chave);		
			confirmacaoRelato.setRelato(relato);
			List lst = relatoService.listarRazoesSelecionadas(relatoEditar);
			for (Razao razaoSel : (List<Razao>)lst) {
				razoesSel.add(razaoSel.getIdRazao().toString());
			}
		}
	}
}
