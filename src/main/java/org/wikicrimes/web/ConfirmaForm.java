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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoConfirmacao;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.ComentarioService;
import org.wikicrimes.service.ConfirmacaoRelatoService;
import org.wikicrimes.service.ConfirmacaoService;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.util.Constantes;
import org.wikicrimes.util.Horario;

public class ConfirmaForm extends GenericForm {

	private final Log log = LogFactory.getLog(ConfirmaForm.class);
	
	private RelatoService relatoService;

	private Confirmacao confirmacao = null;
	
	private ConfirmacaoRelato confirmacaoRelato = null;	

	public CrimeService crimeService;
	
	public ComentarioService comentarioService;

	public ConfirmacaoService confirmacaoService;
	
	private EmailService emailService;
	
	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	private ConfirmacaoRelatoService confirmacaoRelatoService;

	private Long idConfirmacao=null;
	
	private String idConfirmacaoRelato;

	private Long confirma;
	
	public Comentario comentario;
	
	private List<Comentario> comentarios;
	
	private String idCrime;
	
	private List<SelectItem> razoes = null;
	
	private String idMotivo;
	
	private Boolean p;//motivo positivo ou n
	
	public List<SelectItem> motivoItens = null;
	
	public RelatoService getRelatoService() {
		return relatoService;
	}

	public void setRelatoService(RelatoService relatoService) {
		this.relatoService = relatoService;
	}
	
	private void populaMotivoItens(){
		motivoItens = new ArrayList<SelectItem>();
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
		//retorna lista de motivos com o parametro positivo ou n�o
		List<TipoConfirmacao> lst = confirmacaoService.getTipoConfirmacoes(getP());
		for (TipoConfirmacao motivo : lst){
			if(confirmacao.getIdConfirmacao() != null) {
				//nesse caso todos os motivos aparecem. Confirmacao de crime por indicacao
			motivoItens.add(new SelectItem(motivo.getIdTipoConfirmacao().toString(),bundle.getString(motivo.getDescricao())));
			}
			else {
				if(!motivo.getDescricao().endsWith("confianca"))
				motivoItens.add(new SelectItem(motivo.getIdTipoConfirmacao().toString(),bundle.getString(motivo.getDescricao())));	
			}
		}	
	}
	public List<SelectItem> getMotivoItens() {
		populaMotivoItens();
		return motivoItens;
	}

	public String getIdMotivo() {
		return idMotivo;
	}

	public void setIdMotivo(String motivo) {
		this.idMotivo = motivo;
	}

	public void setMotivoItens(List<SelectItem> motivoItens) {
		this.motivoItens = motivoItens;
	}

	public List<String> getRazoesSel() {
		return razoesSel;
	}

	public void setRazoesSel(List<String> razoes) {
		this.razoesSel = razoes;
	}	
	
	public List<SelectItem> getRazoes() {
		Relato relato = new Relato();
		relato.setIdRelato(confirmacaoRelato.getRelato().getIdRelato());
		List lst = relatoService.listarRazoesSelecionadas(relato);
		for (Razao razaoSel : (List<Razao>)lst) {
			razoesSel.add(razaoSel.getIdRazao().toString());
		}
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
				//Tire as opcoes, proximidade, impunidade, pistolagem, omiss�o de testemunhas e crime passional .
				if (r.getIdRazao().intValue() < 11 || r.getIdRazao().intValue() > 14 && r.getIdRazao().intValue() != 18)
					razoes.add(new SelectItem(r.getIdRazao().toString(),bundle.getString(r.getNome())));
			}
		}	
		return razoes;
	}

	public void setRazoes(List<SelectItem> razoes) {
		this.razoes = razoes;
	}		

	private List<String> razoesSel = new ArrayList<String>();

	public String getIdCrime() {
		return idCrime;
	}

	public void setIdCrime(String idCrime) {
		this.idCrime = idCrime;
		if (idCrime != null  && comentarios == null)
			comentarios= comentarioService.getComentariosByCrime(idCrime);
	}
	
	public ConfirmacaoRelato getConfirmacaoRelato() {
		return confirmacaoRelato;
	}

	public void setConfirmacaoRelato(ConfirmacaoRelato confirmacaoRelato) {
		this.confirmacaoRelato = confirmacaoRelato;
	}

	public List<Comentario> getComentarios() {
			return comentarios;
	}
	
	public int getNumeroComentarios(){
		return comentarios.size();
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	public String getIdConfirmacaoRelato() {
		return idConfirmacaoRelato;
	}
	
	public ConfirmacaoRelatoService getConfirmacaoRelatoService() {
		return confirmacaoRelatoService;
	}

	public void setConfirmacaoRelatoService(
			ConfirmacaoRelatoService confirmacaoRelatoService) {
		this.confirmacaoRelatoService = confirmacaoRelatoService;
	}

	public void setIdConfirmacaoRelato(String idConfirmacaoRelato) {
		if(idConfirmacaoRelato!=null && !idConfirmacaoRelato.equalsIgnoreCase("")){
			confirmacaoRelato = confirmacaoRelatoService.getConfirmacao(Long.parseLong(idConfirmacaoRelato));
			this.idConfirmacaoRelato = idConfirmacaoRelato;
		}
	}
	
	public ConfirmaForm() {
		confirmacao = new Confirmacao();
		confirmacao.setCrime(new Crime());
		comentario = new Comentario();
		if (idCrime != null)
			comentarios= comentarioService.getComentariosByCrime(idCrime);
	}
	private String getIp(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ip = request.getRemoteAddr();
		return ip;
	}
	public String salvarComentario(){
		String returnPage= FAILURE;
		if (expirouSessao()) {
			
			return returnPage= SESSAO_EXPIRADA;
		}
		try {
		comentario.setCrime((Crime) crimeService.getCrime(this.getIdCrime()));
		comentario.setDataConfirmacao(new Date());
		comentario.setUsuario((Usuario) this.getSessionScope().get("usuario"));
		comentario.setIp(getIp());
		comentarioService.salvaComentario(comentario);
		Crime cAtualizaCont = new Crime();
		//cAtualizaCont.setIdCrime(this.getIdCrime());
		cAtualizaCont.setChave(this.getIdCrime());
		crimeService.atualizaContadorCometarios(cAtualizaCont);
		emailService.enviarEmailNotificacao(comentario, FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());
		returnPage = SUCCESS;
		addMessage("crime.comentario.realizado", "");
		
		}
		catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}
		
		
		return returnPage;
	}
	
	/**
	 * @TODO MELHORAR CODIGO
	 * @return
	 */
	public String getQuantidade() {
		Long quantidade = getCrime().getQuantidade();
		if (quantidade != null) {
			if (quantidade.equals(new Long(1))) {
				return "1";
			}
			if (quantidade.equals(new Long(2))) {
				return "2";
			}
			if (quantidade.equals(new Long(3))) {
				return "3";
			}
			if (quantidade.equals(new Long(4))) {
				return "4";
			}
			if (quantidade.equals(new Long(5))) {
				return "5";
			}
			if (quantidade.equals(new Long(6))) {
				return "6";
			}
			if (quantidade.equals(new Long(7))) {
				return "7";
			}
			if (quantidade.equals(new Long(8))) {
				return "8";
			}
			if (quantidade.equals(new Long(9))) {
				return "9";
			}
			if (quantidade.equals(new Long(10))) {
				return "10";
			}
			if (quantidade.equals(new Long(11))) {
				return "Mais de 10";
			}
		}

		return "0";
	}

	public String getFaixaEtaria() {
		Long faixaEtaria = getCrime().getFaixaEtaria();
		if (faixaEtaria != null) {
			if (faixaEtaria.equals(new Long(1))) {
				return "Menor";
			}
			if (faixaEtaria.equals(new Long(2))) {
				return "At� 25 Anos";
			}
			if (faixaEtaria.equals(new Long(3))) {
				return "Maior que 25 anos";
			}
		}

		return null;
	}

	public String getSexo() {
		Long sexo = getCrime().getSexo();
		if (sexo != null) {
			if (sexo.equals(new Long(1))) {
				return "Masculino";
			}
			if (sexo.equals(new Long(0))) {
				return "Feminino";
			}
		}

		return null;
	}

	public String getQtdMasculino() {
		Long quantidade = getCrime().getQtdMasculino();
		if (quantidade != null) {
			if (quantidade.equals(new Long(0))) {
				return "0";
			}
			if (quantidade.equals(new Long(1))) {
				return "1";
			}
			if (quantidade.equals(new Long(2))) {
				return "2";
			}
			if (quantidade.equals(new Long(3))) {
				return "3";
			}
			if (quantidade.equals(new Long(4))) {
				return "4";
			}
			if (quantidade.equals(new Long(5))) {
				return "5";
			}
			if (quantidade.equals(new Long(6))) {
				return "6";
			}
			if (quantidade.equals(new Long(7))) {
				return "7";
			}
			if (quantidade.equals(new Long(8))) {
				return "8";
			}
			if (quantidade.equals(new Long(9))) {
				return "9";
			}
			if (quantidade.equals(new Long(10))) {
				return "10";
			}
			if (quantidade.equals(new Long(11))) {
				return "Mais de 10";
			}
		}

		return null;
	}

	public String getQtdFeminino() {
		Long quantidade = getCrime().getQtdFeminino();
		if (quantidade != null) {
			if (quantidade.equals(new Long(0))) {
				return "0";
			}
			if (quantidade.equals(new Long(1))) {
				return "1";
			}
			if (quantidade.equals(new Long(2))) {
				return "2";
			}
			if (quantidade.equals(new Long(3))) {
				return "3";
			}
			if (quantidade.equals(new Long(4))) {
				return "4";
			}
			if (quantidade.equals(new Long(5))) {
				return "5";
			}
			if (quantidade.equals(new Long(6))) {
				return "6";
			}
			if (quantidade.equals(new Long(7))) {
				return "7";
			}
			if (quantidade.equals(new Long(8))) {
				return "8";
			}
			if (quantidade.equals(new Long(9))) {
				return "9";
			}
			if (quantidade.equals(new Long(10))) {
				return "10";
			}
			if (quantidade.equals(new Long(11))) {
				return "Mais de 10";
			}
		}

		return null;
	}

	public String getHorario() {
		Long horario = getCrime().getHorario();
		if (horario != null) {
			Iterator it = Horario.iterator();
			while (it.hasNext()) {
				Horario h = (Horario) it.next();
				Long value = new Long(h.ord());
				if (value.equals(horario)) {
					return h.toString();
				}
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	public String simConfirma() {
		confirmacao.setConfirma(Constantes.SIM);
		return confirma();
	}

	public String naoConfirma() {
		confirmacao.setConfirma(Constantes.NAO);
		return confirma();
	}
	
	public String simConfirmaRelato() {
		if(confirmacaoRelato.getConfirma() != null){
			addMessage("errors.confirmacao.ja.feita", "");
			return null;
		}	
		confirmacaoRelato.setConfirma(Constantes.SIM);
		return confirmaRelato();
	}

	public String naoConfirmaRelato() {
		if(confirmacaoRelato.getConfirma() != null){
			addMessage("errors.confirmacao.ja.feita", "");
			return null;
		}	
		confirmacaoRelato.setConfirma(Constantes.NAO);
		return confirmaRelato();
	}
	
	public String confirmaMotivo(){
		//checa se j� foi confirmado
		if (confirmacao.getIdConfirmacao()!=null){
		Confirmacao tempConfirma = confirmacaoService.getConfirmacao(confirmacao.getIdConfirmacao());
		if (tempConfirma.getConfirma()!=null){
			addMessage("confirmacao.recusada.jarealizada",
			"");
			return SUCCESS;
			}
		}
		if (getP())
			confirmacao.setConfirma(Constantes.SIM);
		else
			confirmacao.setConfirma(Constantes.NAO);
		confirmacao.setTipoConfirmacao(confirmacaoService.getTipoConfirmacao(new Long(getIdMotivo())));
		confirmacao.setCrime(((Crime) crimeService.getCrime(getIdCrime())));
		return confirma();
	}

	protected String confirma() {
		String returnPage = FAILURE;
		
		try {
			
			if (confirmacao.getIdConfirmacao() != null) {
					confirmacao.setDataConfirmacao(new Date());
					confirmacao.setIp(getIp());
					
				if (confirmacaoService.update(confirmacao)) {

					crimeService.atualizaContador(confirmacao.getConfirma(),
							confirmacao.getCrime());
					emailService.enviarEmailNotificacao(confirmacao, FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());
					addMessage("confirmacao.realizada", "");
					returnPage = SUCCESS;
				} else {
					addMessage("errors.geral",
							"Erro ao tentar realizar uma confirma��o de crime.");
				}
			}
			else {
				if (expirouSessao()) {
					
					return returnPage= SESSAO_EXPIRADA;
				}
				Usuario user= (Usuario) this.getSessionScope().get("usuario");
				if (user!=null) {
					confirmacao.setUsuario(user);
					//verifica se foi o usuario que registrou esse crime. Usuario nao pode confirmar o proprio crime que registrou
					if (confirmacao.getCrime().getUsuario() != null && confirmacao.getCrime().getUsuario().equals(user)){
						addMessage("confirmacao.recusada.propria",
						"");
						return "";
					}
					//verifica se usuario ja confirmou esse crime
					if (!confirmacaoService.getJaConfirmou(confirmacao)){
						confirmacao.setDataConfirmacao(new Date());
						confirmacao.setIp(getIp());
						confirmacaoService.insert(confirmacao);
						crimeService.atualizaContador(confirmacao.getConfirma(),
								confirmacao.getCrime());
						emailService.enviarEmailNotificacao(confirmacao, FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());
						addMessage("confirmacao.realizada", "");
						returnPage = SUCCESS;
					}
					else {
						addMessage("confirmacao.recusada.jarealizada",
						"");
						return "";
					}
				}
				else {
					addMessage("errors.geral",
					"Usuario nulo");
				} 
					
			}

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}
	
	protected String confirmaRelato() {
		String returnPage = FAILURE;

		try {
			
			if (confirmacaoRelato.getIdConfirmacao() != null) {				
					confirmacaoRelato.setDataConfirmacao(new Date());
					confirmacaoRelato.setIp(getIp());
					if (confirmacaoRelatoService.update(confirmacaoRelato)) {
						relatoService.increntaNumConfirmacoes(confirmacaoRelato.getRelato(),confirmacaoRelato.getConfirma());
	//					crimeService.atualizaContador(confirmacao.getConfirma(),
	//							confirmacao.getCrime());
	
						addMessage("confirmacao.realizada", "");
						returnPage = SUCCESS;
					} else {
						addMessage("errors.geral",
								"Erro ao tentar realizar uma confirma��o de crime.");
					}				
			}		

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}

	public Confirmacao getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(Confirmacao conf) {
		confirmacao = conf;
	}

	public ConfirmacaoService getConfirmacaoService() {
		return confirmacaoService;
	}

	public void setConfirmacaoService(ConfirmacaoService confirmacaoService) {
		this.confirmacaoService = confirmacaoService;
	}

	public Crime getCrime() {
		return confirmacao.getCrime();
	}

	public void setCrime(Crime crime) {
		confirmacao.setCrime(crime);
	}

	public CrimeService getCrimeService() {
		return crimeService;
	}

	public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}

	public Long getConfirma() {
		return confirma;
	}

	public void setConfirma(Long confirma) {
		this.confirma = confirma;
	}

	public Long getIdConfirmacao() {
		return idConfirmacao;
	}

	public void setIdConfirmacao(Long idConfirmacao) {
		this.idConfirmacao = idConfirmacao;
		if (idConfirmacao != null && idConfirmacao!=0) {
			confirmacao = (Confirmacao) confirmacaoService.getConfirmacao(idConfirmacao);
		}
	}

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		if (comentario == null)
			comentario = new Comentario();
		this.comentario = comentario;
	}

	public ComentarioService getComentarioService() {
		return comentarioService;
	}

	public void setComentarioService(ComentarioService comentarioService) {
		this.comentarioService = comentarioService;
	}

	public void setP(Boolean p) {
		this.p = p;		
	}

	public Boolean getP() {
		return p;
	}

}
