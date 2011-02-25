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
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoPapel;
import org.wikicrimes.model.TipoRegistro;
import org.wikicrimes.model.TipoTransporte;
import org.wikicrimes.model.TipoVitima;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Constantes;
import org.wikicrimes.util.Horario;

public class CrimeForm extends GenericForm {

	private final Log log = LogFactory.getLog(CrimeForm.class);

	private Crime crime = null;
	
	private List<String> razoesSel = new ArrayList<String>();
	
	private List<SelectItem> razoes = null;
	
	private List<Crime> crimes;

	public CrimeService service;	
	
	public UsuarioService usuarioService;
	
	public Long tipoCrime;
	public Long tipoVitima;
	public Long tipoLocal;
	
	private String id;
	
	private String email1=null;
	
	private String email2=null;
	
	private String email3=null;
	
	private String email4=null;
	
	private String email5=null;
	
	private String email6=null;
	
	private String mensagemConf = "";

	private List tipoVitimaItens;

	private List tipoLocalItens;
	
	private List tipoPapelItens;

	private List<SelectItem> tipoCrimeItens;
	
	

	public CrimeForm()  {		
		crime = new Crime();	
		
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public CrimeService getCrimeService() {
		return service;
	}

	public void setCrimeService(CrimeService service) {
		this.service = service;		
	}

	public Long getIdCrime() {
		return crime.getIdCrime();
	}

	public void setIdCrime(Long id) {
		crime.setIdCrime(id);
	}
	
	public String getTipoTransporte() {
		if (crime.getTipoTransporte() != null) {
			return crime.getTipoTransporte().getIdTipoTransporte().toString();
		}

		return "";
	}

	public void setTipoTransporte(String id) {
		crime.setTipoTransporte(new TipoTransporte(Long.valueOf(id)));
	}

	public Long getTipoCrime() {
		return tipoCrime;
	}

	public void setTipoCrime(Long id) {
		tipoCrime = id;
		if (tipoCrime != null) {
			crime.setTipoCrime(service.getTipoCrime(tipoCrime));
		}
	}
	
	public Long getFaixaEtaria() {
		return crime.getFaixaEtaria();
	}

	public void setFaixaEtaria(Long faixaEtaria) {
		crime.setFaixaEtaria(faixaEtaria);
	}
	
	public Long getHorario() {
		return crime.getHorario();
	}

	public void setHorario(Long horario) {
		crime.setHorario(horario);
	}

	/**
	 * @return
	 */
	public Long getTipoLocal() {
    	return tipoLocal;
	}

	/**
	 * @param id
	 */
	public void setTipoLocal(Long id) {
		tipoLocal = id;
		if (tipoLocal != null) {
			crime.setTipoLocal(service.getTipoLocal(tipoLocal));
		}
	}

	public Long getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(Long id) {
		tipoVitima = id;	
		
		if (tipoCrime != null) {
			crime.setTipoVitima(service.getTipoVitima(tipoVitima));
			populateTipoLocalItens();
		}
		
	}
	
	/**
	 * Retorna uma string contendo todos os emails dos confirmantes separados
	 * por ;
	 * 
	 * @return
	 */
//	public String getConfirmacoes() {
//		Set<Confirmacao> confirmacoes = crime.getConfirmacoes();
//		String emails = "";
//		
//		if (confirmacoes != null) {
//			for (Confirmacao confirmacao : confirmacoes) {
//				if (!emails.equals("")) {
//					emails += ";";
//				}
//				emails += confirmacao.getUsuario().getEmail();
//			}
//		}
//
//		return emails;
//	}

	/**
	 * Como par�metro de entrada � passado uma string contendo uma lista de
	 * emails separadas por v�rgulas que s�o divididas e adicionados em
	 * confirma��o.
	 * 
	 * @param c
	 */
//	public void setConfirmacoes(String c) {
//		String[] emails = c.trim().split(",");
//		Set<Confirmacao> confirmacoes = new HashSet<Confirmacao>();
//		for (int i = 0; i < emails.length; i++) {
//			Confirmacao confirmacao = new Confirmacao();
//			Usuario usuario = usuarioService.retornaUsuarioConfirmacao(emails[i]);
//			confirmacao.setUsuario(usuario);
//			confirmacoes.add(confirmacao);
//		}
//
//		crime.setConfirmacoes(confirmacoes);
//	}

	/**
	 * @return
	 */
	public String getTipoRegistro() {
		if (crime.getTipoRegistro() != null) {
			return crime.getTipoRegistro().getIdTipoRegistro().toString();
		}

		return "";
	}

	/**
	 * @param id
	 */
	public void setTipoRegistro(String id) {
		crime.setTipoRegistro(new TipoRegistro(Long.valueOf(id)));
	}

	/**
	 * @return
	 */
	public String getTipoArmaUsada() {
		if (crime.getTipoArmaUsada() != null) {
			return crime.getTipoArmaUsada().getIdTipoArmaUsada().toString();
		}
		return "";
	}

	/**
	 * @param id
	 */
	public void setTipoArmaUsada(String id) {
		crime.setTipoArmaUsada(new TipoArmaUsada(Long.valueOf(id)));
	}

	/**
	 * @return
	 */
	public String getTipoPapel() {
		if (crime.getTipoPapel() != null) {
			return crime.getTipoPapel().getIdTipoPapel().toString();
		}

		return "";
	}

	/**
	 * @param id
	 */
	public void setTipoPapel(String id) {
		crime.setTipoPapel(new TipoPapel(Long.valueOf(id)));
	}

	/**
	 * @return
	 */
	public String getUsuario() {
		if (crime.getUsuario() != null) {
			return crime.getUsuario().getIdUsuario().toString();
		}

		return "";
	}

	/**
	 * @param usuario
	 */
	public void setUsuario(Usuario usuario) {
		crime.setUsuario(usuario);
	}

	/**
	 * @param id
	 */
	public void setUsuario(String id) {
		crime.setUsuario(new Usuario(new Long(id)));
	}

	/**
	 * @return
	 */
	public Long getQuantidade() {
		return crime.getQuantidade();
	}

	/**
	 * @param quantidade
	 */
	public void setQuantidade(Long quantidade) {
		crime.setQuantidade(quantidade);
	}
	
	/**
	 * @return
	 */
	public Long getQtdMasculino() {
		return crime.getQtdMasculino();
	}

	/**
	 * @param qtdMasculino
	 */
	public void setQtdMasculino(Long qtdMasculino) {
		crime.setQtdMasculino(qtdMasculino);
	}
	
	/**
	 * @return
	 */
	public Long getQtdFeminino() {
		return crime.getQtdFeminino();
	}

	/**
	 * @param qtdMasculino
	 */
	public void setQtdFeminino(Long qtdFeminino) {
		crime.setQtdFeminino(qtdFeminino);
	}

	/**
	 * @return
	 */
	public Double getLatitude() {
		return crime.getLatitude();
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(Double latitude) {
		crime.setLatitude(latitude);
	}

	/**
	 * @return
	 */
	public Double getLongitude() {
		return crime.getLongitude();
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(Double longitude) {
		crime.setLongitude(longitude);
	}

	/**
	 * @return
	 */
	public String getDescricao() {
		return crime.getDescricao();
	}

	/**
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		crime.setDescricao(descricao);
	}

	/**
	 * @return
	 */
	public Date getData() {
		return crime.getData();
	}

	/**
	 * @param data
	 */
	public void setData(Date data) {
		crime.setData(data);
	}

	
	/**
	 * @return
	 */
	public Long getSexo() {
		return crime.getSexo();
	}

	/**
	 * @param sexoVitima
	 */
	public void setSexo(Long sexo) {
		crime.setSexo(sexo);
	}
	
	private String getIp(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ip = request.getRemoteAddr();
		return ip;
	}
	public String atualizaCrime() {
		String returnPage = FAILURE;
		/*if (expirouSessao()) {
			
			return returnPage= SESSAO_EXPIRADA;
		}*/
		Crime crimeTemp=(Crime) service.get(crime.getIdCrime());
		crimeTemp.setCidade(crime.getCidade());
		crimeTemp.setEndereco(crime.getEndereco());
		crimeTemp.setEstado(crime.getEstado());
		crimeTemp.setPais(crime.getPais());
		crimeTemp.setCep(crime.getCep());
		service.update(crimeTemp);
		returnPage="";
		
	return returnPage;	
	}
	/**
	 * @return
	 * @throws LoginException 
	 */
	
	public boolean formataEmbedCrime(int inicio){
		try{	
			if(crime.getEmbedNoticia()!=null && !crime.getEmbedNoticia().equalsIgnoreCase("")){
				String embed = crime.getEmbedNoticia();
				int x = embed.indexOf("width=\"",inicio);
				//System.out.println(x);
				x+=7;
				int y = embed.indexOf("\"",x);
				String embedParte1 = embed.substring(0,x);
				String embedParte2 = embed.substring(y,embed.length());
				//System.out.println(embedParte2);
				embed = embedParte1+330+embedParte2;
				//System.out.println(embed);
				
				x = embed.indexOf("height=\"",x);
				//System.out.println(x);
				x+=8;
				y = embed.indexOf("\"",x);
				embedParte1 = embed.substring(0,x);
				embedParte2 = embed.substring(y,embed.length());
				//System.out.println(embedParte2);
				embed = embedParte1+140+embedParte2;
				//System.out.println(embed);
				crime.setEmbedNoticia(embed);				
				if(embed.indexOf("width=\"",x)!=-1)
					formataEmbedCrime(x);
				
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}	
	
	
	public String insert() {
		String returnPage = FAILURE;
		if(!formataEmbedCrime(0)){
			addMessage("errors.embed.invaliado","");
			return null;
		}
		
		
		Confirmacao confirmacao=null;
		Set<Confirmacao> confirmacoes = new HashSet<Confirmacao>();
		if (expirouSessao()) {
			
			return returnPage= SESSAO_EXPIRADA;
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
			//verifica se data da ocorrencia e maior que data atual
			if (crime.getData().after(new Date())){
				addMessage("errors.dataMaior","");
				return null;
			}
			
			Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
			//verifica se um dos emails e o proprio email de quem registrou o crime
			if (getEmail1().equals(usuario.getEmail()) || getEmail2().equals(usuario.getEmail()) || getEmail3().equals(usuario.getEmail())|| getEmail4().equals(usuario.getEmail())|| getEmail5().equals(usuario.getEmail())|| getEmail6().equals(usuario.getEmail()) ){
				addMessage("confirmacao.recusada.propria","");
				return null;
			}
			
			//verifica se n e usuario certificador pois se for nao precisa de confirmacoes de email
		//	if (!usuario.getPerfil().equals(Perfil.CERTIFICADOR)){
			//Confirma��es
			boolean digitouEmail = false;	
//			verifica se os emails sao iguais
			if (email1.equals(email2) && !email1.equals("") || email1.equals(email3)  && !email1.equals("")  || email1.equals(email4)  && !email1.equals("") || email1.equals(email5)  && !email1.equals("") || email1.equals(email6)  && !email1.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}
			if (email2.equals(email1) && !email2.equals("") || email2.equals(email3)  && !email2.equals("")  || email2.equals(email4)  && !email2.equals("") || email2.equals(email5)  && !email2.equals("") || email2.equals(email6)  && !email2.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}
			if (email3.equals(email1) && !email3.equals("") || email3.equals(email2)  && !email3.equals("")  || email3.equals(email4)  && !email3.equals("") || email3.equals(email5)  && !email3.equals("") || email3.equals(email6)  && !email3.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}
			if (email4.equals(email1) && !email4.equals("") || email4.equals(email2)  && !email4.equals("")  || email4.equals(email5)  && !email4.equals("") || email4.equals(email5)  && !email4.equals("") || email4.equals(email6)  && !email4.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}
			if (email5.equals(email1) && !email5.equals("") || email5.equals(email2)  && !email5.equals("")  || email5.equals(email3)  && !email5.equals("") || email5.equals(email4)  && !email5.equals("") || email5.equals(email6)  && !email5.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}
			if (email6.equals(email1) && !email6.equals("") || email6.equals(email2)  && !email6.equals("")  || email6.equals(email3)  && !email6.equals("") || email6.equals(email4)  && !email6.equals("") || email6.equals(email1)  && !email6.equals("") ){
				addMessage("errors.email.confirmacao.iguais","");
				return null;
			}


			confirmacao = new Confirmacao();
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
			confirmacao = new Confirmacao();
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
			confirmacao = new Confirmacao();
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
				confirmacao.setUsuario(usuarioConfirmacao);
				confirmacao.setMensagem(mensagemConf);
				confirmacoes.add(confirmacao);
				digitouEmail = true;
			}
			confirmacao = new Confirmacao();
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
				confirmacao.setUsuario(usuarioConfirmacao);
				confirmacao.setMensagem(mensagemConf);
				confirmacoes.add(confirmacao);
				digitouEmail = true;
			}
			confirmacao = new Confirmacao();
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
				confirmacao.setUsuario(usuarioConfirmacao);
				confirmacao.setMensagem(mensagemConf);
				confirmacoes.add(confirmacao);
				digitouEmail = true;
			}
			confirmacao = new Confirmacao();
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
				confirmacao.setUsuario(usuarioConfirmacao);
				confirmacao.setMensagem(mensagemConf);
				confirmacoes.add(confirmacao);
				digitouEmail = true;
			}
			
			
			if(!digitouEmail)
			{
				addMessage("errors.email.confirmacao","");
				return null;
			}
			//Confirma��es
			/*	}
			else {
				//usuario certificador portanto ele mesmo confirma seu crime
				confirmacao = new Confirmacao();
				confirmacao.setUsuario(usuario);
				confirmacao.setConfirma(true);
				confirmacao.setDataConfirmacao(new Date());
				confirmacao.setIndicacao(Constantes.NAO);
				confirmacoes.add(confirmacao);
			}*/
			
			crime.setConfirmacoes(confirmacoes);
			if (usuario != null) {

				TipoArmaUsada tipoArmaUsada = service.getTipoArmaUsada(Long
						.valueOf(getTipoArmaUsada()));
				TipoPapel tipoPapel = service.getTipoPapel(Long
						.valueOf(getTipoPapel()));
				TipoRegistro tipoRegistro = service.getTipoRegistro(Long
						.valueOf(getTipoRegistro()));

				Crime crime = getCrime();
				crime.setUsuario(usuario);
				crime.setTipoArmaUsada(tipoArmaUsada);
				crime.setTipoPapel(tipoPapel);
				crime.setTipoRegistro(tipoRegistro);
				crime.setConfirmacoesPositivas(new Long(0));
				crime.setConfirmacoesNegativas(new Long(0));
				crime.setDataHoraRegistro(new Date());
				crime.setIp(this.getIp());
				
				if (service.insert(crime,razoesInsert)) {
					addMessage("crime.registrado", "");
					FiltroForm filtroForm = (FiltroForm) this.getSessionScope().get("filtroForm");
					filtroForm.setIdCrimeRegistrado(crime.getIdCrime().toString());
					filtroForm.update();
					System.out.println("[" + new Date() + "] " + usuario.getEmail() + " registrou um crime...");
					returnPage = SUCCESS;
				} else {
					addMessage("errors.geral",
							"Erro ao tentar inserir um crime.");
				}
			} else {
				addMessage("errors.geral",
						"Se logue antes de registrar um crime.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}
	
	/**
	 * @return
	 */
	public List<SelectItem> getQuantidadeItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		itens.add(new SelectItem("-1", "Selecione"));
		itens.add(new SelectItem("0", "0"));
		itens.add(new SelectItem("1", "1"));
		itens.add(new SelectItem("2", "2"));
		itens.add(new SelectItem("3", "3"));
		itens.add(new SelectItem("4", "4"));
		itens.add(new SelectItem("5", "5"));
		itens.add(new SelectItem("6", "6"));
		itens.add(new SelectItem("7", "7"));
		itens.add(new SelectItem("8", "8"));
		itens.add(new SelectItem("9", "9"));
		itens.add(new SelectItem("10", "10"));
		itens.add(new SelectItem("11", "Mais de 10"));
		itens.add(new SelectItem("12", "N�o Sei"));

		return itens;
	}
	
	public List<SelectItem> getQuantidadeCriminosos() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		itens.add(new SelectItem("-1", "Selecione"));
		itens.add(new SelectItem("1", "1"));
		itens.add(new SelectItem("2", "2"));
		itens.add(new SelectItem("3", "3"));
		itens.add(new SelectItem("4", "4"));
		itens.add(new SelectItem("5", "5"));
		itens.add(new SelectItem("6", "6"));
		itens.add(new SelectItem("7", "7"));
		itens.add(new SelectItem("8", "8"));
		itens.add(new SelectItem("9", "9"));
		itens.add(new SelectItem("10", "10"));
		itens.add(new SelectItem("11", "Mais de 10"));
		itens.add(new SelectItem("12", "N�o Sei"));

		return itens;
	}
	/**
	 * @return
	 */
	public List<SelectItem> getSexoItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		itens.add(new SelectItem("1", "Masculino"));
		itens.add(new SelectItem("0", "Feminino"));

		return itens;
	}
	
	/**
	 * @return
	 */
	public List<SelectItem> getTipoTransporteItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoTransporteAll();
		
		itens.add(new SelectItem("0", "Selecione"));

		for (int i = 0; i < result.size(); i++) {
			TipoTransporte tipoTransporte = (TipoTransporte) result.get(i);
			String id = tipoTransporte.getIdTipoTransporte().toString();
			String nome = tipoTransporte.getNome();

			itens.add(new SelectItem(id, nome));
		}

		return itens;
	}
	
	public void populateTipoVitimaItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		List<BaseObject> result = new ArrayList();
		if (tipoCrime != null && !tipoCrime.equals("0")) {
			Long idTipoCrime = Long.valueOf(getTipoCrime());
			result = service.findTipoVitimaByTipoCrime(idTipoCrime);
		} else {
			result = service.getTipoVitimaAll();
		}

		itens.add(new SelectItem("0", "Selecione"));
		
		for (int i = 0; i < result.size(); i++) {
			TipoVitima tipoVitima = (TipoVitima) result.get(i);
			String id = tipoVitima.getIdTipoVitima().toString();
			String nome = tipoVitima.getNome();

			itens.add(new SelectItem(id, nome));
		}
		
		if (result.size() <= 0) {
			itens.clear();
		}
		
		tipoVitimaItens = itens;
	}

	/**
	 * @return
	 */
	public List getTipoVitimaItens() {
		populateTipoVitimaItens();
		return tipoVitimaItens;
	}

	public void populateTipoLocalItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		List<BaseObject> result = new ArrayList();
		if (tipoVitima != null && !tipoVitima.equals("0")) {
			if(tipoVitima == 3 || tipoVitima == 4 || tipoVitima == 5 || tipoVitima == 6 || tipoVitima == 7 || tipoVitima == 8 || tipoVitima == 9 ){
				result = service.findTipoLocalByTipoVitima(new Long(1));
			}else{			
				Long tipoVitima = Long.valueOf(getTipoVitima());
				result = service.findTipoLocalByTipoVitima(tipoVitima);
			}	
		}		
		else {
			result = service.getTipoLocalAll();
		}
		
		for (int i = 0; i < result.size(); i++) {
			TipoLocal tipoLocal = (TipoLocal) result.get(i);
			String id = tipoLocal.getIdTipoLocal().toString();
			String nome = tipoLocal.getNome();
			
			itens.add(new SelectItem(id, getMessage(nome,"")));
		}
		
		if (result.size() <= 0) {
			itens.clear();
			itens.add(new SelectItem("0", "Nenhum"));
		}

		tipoLocalItens = itens;
	}
	
	/**
	 * @return
	 */
	public List getTipoLocalItens() {
		
		return tipoLocalItens;
	}

	/**
	 * @return
	 */
	public List<SelectItem> getTipoArmaUsadaItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoArmaUsadaAll();
		
		
		
		for (int i = 0; i < result.size(); i++) {
			TipoArmaUsada tipoArmaUsada = (TipoArmaUsada) result.get(i);
			String id = tipoArmaUsada.getIdTipoArmaUsada().toString();
			String nome = tipoArmaUsada.getNome();
			
			itens.add(new SelectItem(id, getMessage(nome, "")));
		}
		return itens;
	}

	/**
	 * @return
	 */
	public List<SelectItem> getTipoRegistroItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoRegistroAll();
		
		
		
		for (int i = 0; i < result.size(); i++) {
			TipoRegistro tipoRegistro = (TipoRegistro) result.get(i);
			String id = tipoRegistro.getIdTipoRegistro().toString();
			String nome = tipoRegistro.getNome();
			itens.add(new SelectItem(id, getMessage(nome,"")));
		}

		return itens;
	}
	
	/**
	 * @return
	 */
	public List<SelectItem> getFaixaEtariaItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		
		itens.add(new SelectItem("0", "Selecione"));
		
		itens.add(new SelectItem("1", "Menor que 16"));
		itens.add(new SelectItem("2", "Entre 16 e 25"));
		itens.add(new SelectItem("3", "Maior que 25"));
		itens.add(new SelectItem("4", "N�o Sei"));

		return itens;
	}

	/**
	 * @return
	 */
	public List<SelectItem> getTipoPapelItens() {
		if (tipoPapelItens==null) {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoPapelAll();
		
		
		for (int i = 0; i < result.size(); i++) {
			TipoPapel tipoPapel = (TipoPapel) result.get(i);
			String id = tipoPapel.getIdTipoPapel().toString();
			String nome = tipoPapel.getNome();

			if(!(tipoVitima==6 && tipoPapel.getIdTipoPapel()==1)) {
				itens.add(new SelectItem(id, getMessage(nome,"")));
			}
		}
		tipoPapelItens=itens;
		return itens;
		}
		else 
			return tipoPapelItens;
	}

	/**
	 * @return
	 */
	public List<SelectItem> getTipoCrimeItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoCrimeAll();
		
		itens.add(new SelectItem("0", "Selecione"));

		for (int i = 0; i < result.size(); i++) {
			TipoCrime tipoCrime = (TipoCrime) result.get(i);
			String id = tipoCrime.getIdTipoCrime().toString();
			String nome = tipoCrime.getNome();

			itens.add(new SelectItem(id, nome));
		}

		return itens;
	}
	
	public String acessoAdministracao() {
		String returnPage = FAILURE;
		// TODO verificar se perfil e de administrador
		if (expirouSessao()) {

			return returnPage = SESSAO_EXPIRADA;
		} else if (!isAdmin()) {
			addMessage("webapp.pagina.nao.permitida", "");

			return returnPage = PAGINA_NAO_PERMITIDA;
		}

		try {
			crimes = service.getCrimesSemEstatisticas();

			returnPage = SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}
	
	public String mostraCrimesSemEstatisticas(){
		String returnPage= FAILURE;
		//TODO verificar se perfil e de administrador
		if (expirouSessao()) {
			
			return returnPage= SESSAO_EXPIRADA;
		}
		else if (!isAdmin()){
				addMessage("webapp.pagina.nao.permitida", "");
			
			return returnPage= PAGINA_NAO_PERMITIDA;
		}
		
		
		try {
			crimes=service.getCrimesSemEstatisticas();
		
		returnPage = SUCCESS;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}
		
		
		return returnPage;
	}
	
	public String mostraCrimesSemChave(){
		String returnPage= FAILURE;
		//TODO verificar se perfil e de administrador
		if (expirouSessao()) {
			
			return returnPage= SESSAO_EXPIRADA;
		}
		else if (!isAdmin()){
				addMessage("webapp.pagina.nao.permitida", "");
			
			return returnPage= PAGINA_NAO_PERMITIDA;
		}
		
		
		try {
			
			service.updateCrimesSemChave();
			
		
		returnPage = SUCCESS;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}
		
		
		return returnPage;
	}
	
	/**
	 * @return
	 */
	public List<SelectItem> getHorarioItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		Iterator<Horario> horarios = Horario.iterator();
		while(horarios.hasNext()) {
		    Horario horario = horarios.next();
		    itens.add(new SelectItem(new Long(horario.ord()), horario.toString()));		    
		}

		return itens;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setTipoVitimaItens(List tipoVitimaItens) {
		this.tipoVitimaItens = tipoVitimaItens;
	}

	public void setTipoLocalItens(List tipoLocalItens) {
		this.tipoLocalItens = tipoLocalItens;
	}
	
	public void setTipoPapelItens(List tipoPapelItens) {
		this.tipoPapelItens = tipoPapelItens;
	}

	public void setTipoCrimeItens(List tipoCrimeItens) {
		this.tipoCrimeItens = tipoCrimeItens;
	}
	
	public String getArtigo(){
		switch (tipoCrime.intValue()) {
		case 3: case 4:
			 return "crime.registro.cadastro.artigo.masc";
			 
		default:
			return "crime.registro.cadastro.artigo.fem";
			
		}
		
		
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
	
	public String edit() {
		
		if (id != null) {
			// assuming edit
			setCrime( (Crime) service.get(Long.getLong(id)));
		}

		return SUCCESS;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	private boolean ascending = true;
	
	private String sortColumn = "data";
	
	public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
	
	public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
    
    public List<Crime> getMaisComentados(){
    	if(crimes == null)
			crimes = service.getCrimesMaisComentados();
    	else if(crimes != service.getCrimesMaisComentados())
    		crimes = service.getCrimesMaisComentados();
    	return crimes;
    }
    
    public List<Crime> getMaisVistos(){
    	if(crimes == null)
    		crimes = service.getCrimesMaisVistos();
    	else if(crimes != service.getCrimesMaisVistos())
    		crimes = service.getCrimesMaisVistos();
    	return crimes;
    }
    
    public List<Crime> getCrimes() {
		return crimes;
	}

	public void setCrimes(List<Crime> crimes) {
		this.crimes = crimes;
	}
	
	public Integer getQtdCrimes(){
		
		if (crimes != null) 
			return new Integer(this.crimes.size());
		else
			return new Integer(0);
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
				razoes.add(new SelectItem(r.getIdRazao().toString(),bundle.getString(r.getNome())));
			}
		}	
		return razoes;
	}

	public void setRazoes(List<SelectItem> razoes) {
		this.razoes = razoes;
	}

	public String getMensagemConf() {
		return mensagemConf;
	}

	public void setMensagemConf(String mensagemConf) {
		this.mensagemConf = mensagemConf;
	}

}
