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
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoAgressorRelato;
import org.wikicrimes.model.TipoBemRoubadoRelato;
import org.wikicrimes.model.TipoConsequenciaRelato;
import org.wikicrimes.model.TipoLocalizacaoRelato;
import org.wikicrimes.model.TipoReportRelato;
import org.wikicrimes.model.TipoViolenciaEscolaRelato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Constantes;

public class RelatoForm extends GenericForm {

	private final Log log = LogFactory.getLog(RelatoForm.class);

	private Relato relato = null;
	public RelatoService service;
	private String id;	
	private Boolean manha=false;	
	private Boolean tarde=false;	
	private Boolean noite=false;	
	private Boolean madrugada=false;	
	public String tipoRelato;	
	public String subTipoRelato;
	public Double latitude;
	public Double longitude;
	
	private String mensagemConf="";

	public UsuarioService usuarioService;	
	
	private List<String> razoesSel = new ArrayList<String>();
	private List<SelectItem> razoes = null;

	private String razaoOutros;
	
	private String email1=null;	
	private String email2=null;	
	private String email3=null;	
	private String email4=null;	
	private String email5=null;
	private String email6=null;	
	
	public RelatoForm()  {
		relato = new Relato();
		relato.setTipoViolenciaEscolaRelato(new TipoViolenciaEscolaRelato());
		relato.setTipoConsequenciaRelato(new TipoConsequenciaRelato());
		relato.setTipoAgressorRelato(new TipoAgressorRelato());
		relato.setTipoBemRoubadoRelato(new TipoBemRoubadoRelato());
		relato.setTipoLocalizacaoRelato(new TipoLocalizacaoRelato());
		relato.setTipoReportRelato(new TipoReportRelato());
		
	}
	
	public String mostrarTipoAgressor(){
		if(relato.getTipoAgressorRelato().getIdTipoAgressorRelato()!=null){
			relato.getTipoAgressorRelato().setIdTipoAgressorRelato(null);
			relato.getTipoReportRelato().setIdTipoReportRelato(null);
			relato.getTipoBemRoubadoRelato().setIdTipoBemRoubadoRelato(null);
			relato.getTipoConsequenciaRelato().setIdTipoConsequenciaRelato(null);			
		}
		return null;
	}

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public RelatoService getRelatoService() {
		return service;
	}

	public void setRelatoService(RelatoService service) {
		this.service = service;
	}

	public Long getIdRelato() {
		return relato.getIdRelato();
	}

	public void setIdRelato(Long id) {
		relato.setIdRelato(id);
	}

	/**
	 * @return
	 */
	public String getUsuario() {
		if (relato.getUsuario() != null) {
			return relato.getUsuario().getIdUsuario().toString();
		}

		return "";
	}

	/**
	 * @param usuario
	 */
	public void setUsuario(Usuario usuario) {
		relato.setUsuario(usuario);
	}

	/**
	 * @param id
	 */
	public void setUsuario(String id) {
		relato.setUsuario(new Usuario(new Long(id)));
	}

	/**
	 * @return
	 */
	public Double getLatitude() {
		return this.latitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return
	 */
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return
	 */
	public String getDescricao() {
		return relato.getDescricao();
	}

	/**
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		relato.setDescricao(descricao);
	}
	
	private String getIp(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ip = request.getRemoteAddr();
		return ip;
	}

	/**
	 * @return
	 * @throws LoginException 
	 */
	public String insert() {
		String returnPage = FAILURE;

		if (expirouSessao()) {			
			return returnPage= SESSAO_EXPIRADA;
		}

		
		if (!madrugada && !manha && !tarde && !noite) {
			addMessage("errors.periodo","");
			return null;
		}
		
		Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
		//verifica se um dos emails e o proprio email de quem registrou o crime
		if (getEmail1().equals(usuario.getEmail()) || getEmail2().equals(usuario.getEmail()) || getEmail3().equals(usuario.getEmail())|| getEmail4().equals(usuario.getEmail())|| getEmail5().equals(usuario.getEmail())|| getEmail6().equals(usuario.getEmail()) ){
			addMessage("confirmacao.recusada.propria","");
			return null;
		}		
		
		boolean digitouEmail = false;	
		
		ConfirmacaoRelato confirmacao=null;
		Set<ConfirmacaoRelato> confirmacoes = new HashSet<ConfirmacaoRelato>();
		
		confirmacao = new ConfirmacaoRelato();
		if(email1!=null && !email1.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email1);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email1,usuario.getIdiomaPreferencial());
			
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);
			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacao.setMensagem(mensagemConf);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		confirmacao = new ConfirmacaoRelato();
		if(email2!=null && !email2.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email2);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email2,usuario.getIdiomaPreferencial());
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);

			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacao.setMensagem(mensagemConf);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		confirmacao = new ConfirmacaoRelato();
		if(email3!=null && !email3.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email3);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email3,usuario.getIdiomaPreferencial());
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);
			confirmacao.setMensagem(mensagemConf);
			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		confirmacao = new ConfirmacaoRelato();
		if(email4!=null && !email4.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email4);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email4,usuario.getIdiomaPreferencial());
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);
			confirmacao.setMensagem(mensagemConf);
			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		confirmacao = new ConfirmacaoRelato();
		if(email5!=null && !email5.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email5);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email5,usuario.getIdiomaPreferencial());
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);
			confirmacao.setMensagem(mensagemConf);
			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		confirmacao = new ConfirmacaoRelato();
		if(email6!=null && !email6.equals("")){
			Usuario usuarioConfirmacao = usuarioService.getUsuario(email6);
			//Se nulo e pq esta indicando esse usuario
			if (usuarioConfirmacao == null) {
				confirmacao.setIndicacao(Constantes.SIM);
				//cria um usuario convidado
				usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao(email6,usuario.getIdiomaPreferencial());
			}
			//seta que essa confirmacao foi indicada por email
			confirmacao.setIndicacaoEmail(Constantes.SIM);
			confirmacao.setMensagem(mensagemConf);
			confirmacao.setUsuario(usuarioConfirmacao);
			confirmacoes.add(confirmacao);
			digitouEmail = true;
		}
		
		
		if(!digitouEmail)
		{
			addMessage("errors.email.confirmacao","");
			return null;
		}		
		
		List<Razao> razoesInsert = new ArrayList<Razao>();
		if(razoesSel.size()<1 || razoesSel.size()>4){
			addMessage("errors.razao","");
			return null;
		}	
		else{			
			for (String razaoSel : razoesSel) {
				Razao r = new Razao();
				r.setIdRazao(Long.parseLong(razaoSel));
				razoesInsert.add(r);
			}
		}		
		
		try {
			usuario = (Usuario) this.getSessionScope().get("usuario");

			relato.setConfirmacoes(confirmacoes);
			
			if (usuario != null) {
				if(relato.getTipoBemRoubadoRelato().getIdTipoBemRoubadoRelato()!=null && !relato.getTipoBemRoubadoRelato().getIdTipoBemRoubadoRelato().equals(new Long(-1))){
					relato.setTipoBemRoubadoRelato((TipoBemRoubadoRelato)service.find(relato.getTipoBemRoubadoRelato()).get(relato.getTipoBemRoubadoRelato().getIdTipoBemRoubadoRelato().intValue()-1));
				}else if(relato.getTipoBemRoubadoRelato().getIdTipoBemRoubadoRelato()==null){
					relato.setTipoBemRoubadoRelato(null);
				}
				if(relato.getTipoViolenciaEscolaRelato().getIdTipoViolenciaEscolaRelato()!=null && !relato.getTipoViolenciaEscolaRelato().getIdTipoViolenciaEscolaRelato().equals(new Long(-1))){
					relato.setTipoViolenciaEscolaRelato((TipoViolenciaEscolaRelato)service.find(relato.getTipoViolenciaEscolaRelato()).get(relato.getTipoViolenciaEscolaRelato().getIdTipoViolenciaEscolaRelato().intValue()-1));
				}else if(relato.getTipoViolenciaEscolaRelato().getIdTipoViolenciaEscolaRelato()==null){
					relato.setTipoViolenciaEscolaRelato(null);
				}
				if(relato.getTipoAgressorRelato().getIdTipoAgressorRelato()!=null && !relato.getTipoAgressorRelato().getIdTipoAgressorRelato().equals(new Long(-1))){
					relato.setTipoAgressorRelato((TipoAgressorRelato)service.find(relato.getTipoAgressorRelato()).get(relato.getTipoAgressorRelato().getIdTipoAgressorRelato().intValue()-1));
				}else if(relato.getTipoAgressorRelato().getIdTipoAgressorRelato()==null){
					relato.setTipoAgressorRelato(null);
				}
				if(relato.getTipoConsequenciaRelato().getIdTipoConsequenciaRelato()!=null && !relato.getTipoConsequenciaRelato().getIdTipoConsequenciaRelato().equals(new Long(-1))){
					relato.setTipoConsequenciaRelato((TipoConsequenciaRelato)service.find(relato.getTipoConsequenciaRelato()).get(relato.getTipoConsequenciaRelato().getIdTipoConsequenciaRelato().intValue()-1));
				}else if(relato.getTipoConsequenciaRelato().getIdTipoConsequenciaRelato()==null){
					relato.setTipoConsequenciaRelato(null);
				}
				if(relato.getTipoLocalizacaoRelato().getIdTipoLocalizacaoRelato()!=null && !relato.getTipoLocalizacaoRelato().getIdTipoLocalizacaoRelato().equals(new Long(-1))){
					relato.setTipoLocalizacaoRelato((TipoLocalizacaoRelato)service.find(relato.getTipoLocalizacaoRelato()).get(relato.getTipoLocalizacaoRelato().getIdTipoLocalizacaoRelato().intValue()-1));
				}else if(relato.getTipoLocalizacaoRelato().getIdTipoLocalizacaoRelato()==null){
					relato.setTipoLocalizacaoRelato(null);
				}
				if(relato.getTipoReportRelato().getIdTipoReportRelato()!=null && !relato.getTipoReportRelato().getIdTipoReportRelato().equals(new Long(-1))){
					relato.setTipoReportRelato((TipoReportRelato)service.find(relato.getTipoReportRelato()).get(relato.getTipoReportRelato().getIdTipoReportRelato().intValue()-1));
				}else if(relato.getTipoReportRelato().getIdTipoReportRelato()==null){
					relato.setTipoReportRelato(null);
				}
				
				
				relato.setUsuario(usuario);
				relato.setIp(this.getIp());
				relato.setTipoRelato(tipoRelato);
				relato.setSubTipoRelato(subTipoRelato);
				relato.setMadrugada(madrugada);
				relato.setManha(manha);
				relato.setTarde(tarde);
				relato.setNoite(noite);
				relato.setLatitude(latitude);
				relato.setLongitude(longitude);
				relato.setDataHoraRegistro(new Date());
				relato.setOutraRazao(razaoOutros);
				relato.setQtdConfNegativas(new Long(0));
				relato.setQtdConfPositivas(new Long(0));
				if (service.insert(relato, razoesInsert)) {
					addMessage("relato.registrado", "");
					FiltroForm filtroForm = (FiltroForm) this.getSessionScope().get("filtroForm");
					filtroForm.setIdCrimeRegistrado(relato.getIdRelato().toString());
					filtroForm.setChaveRelatoRegistrado(relato.getChave());
					filtroForm.setDescricaoRelato(relato.getDescricao().length() >= 98?relato.getDescricao().substring(0, 98) + "...":relato.getDescricao());
					filtroForm.update();
					System.out.println("[" + new Date() + "] " + usuario.getEmail() + " registrou uma denuncia...");
					returnPage = SUCCESS;
				} else {
					addMessage("errors.geral",
							"Erro ao tentar inserir um relato.");
				}
			} else {
				addMessage("errors.geral",
						"Se logue antes de registrar um relato.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}

	public String edit() {
		if (id != null) {
			// assuming edit
			setRelato( (Relato) service.get(Long.getLong(id)));
		}

		return SUCCESS;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipoRelato() {
		return tipoRelato;
	}

	public void setTipoRelato(String tipoRelato) {
		this.tipoRelato = tipoRelato;
		if (this.tipoRelato != null) relato.setTipoRelato(tipoRelato);
	}

	public String getSubTipoRelato() {
		return subTipoRelato;
	}

	public void setSubTipoRelato(String subTipoRelato) {
		this.subTipoRelato = subTipoRelato;
		if (this.subTipoRelato != null) relato.setSubTipoRelato(subTipoRelato);		
	}

	public RelatoService getService() {
		return service;
	}

	public void setService(RelatoService service) {
		this.service = service;
	}

	public Boolean getManha() {
		return manha;
	}

	public void setManha(Boolean manha) {
		this.manha = manha;
	}

	public Boolean getTarde() {
		return tarde;
	}

	public void setTarde(Boolean tarde) {
		this.tarde = tarde;
	}

	public Boolean getNoite() {
		return noite;
	}

	public void setNoite(Boolean noite) {
		this.noite = noite;
	}

	public Boolean getMadrugada() {
		return madrugada;
	}

	public void setMadrugada(Boolean madrugada) {
		this.madrugada = madrugada;
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
			List<BaseObject> razoesBanco = service.listarRazoes();
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

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getEmail4() {
		return email4;
	}

	public void setEmail4(String email4) {
		this.email4 = email4;
	}

	public String getEmail5() {
		return email5;
	}

	public void setEmail5(String email5) {
		this.email5 = email5;
	}

	public String getEmail6() {
		return email6;
	}

	public void setEmail6(String email6) {
		this.email6 = email6;
	}
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getRazaoOutros() {
		return razaoOutros;
	}

	public void setRazaoOutros(String razaoOutros) {
		this.razaoOutros = razaoOutros;
	}
	
	public String getMensagemConf() {
		return mensagemConf;
	}

	public void setMensagemConf(String mensagemConf) {
		this.mensagemConf = mensagemConf;
	}	
}