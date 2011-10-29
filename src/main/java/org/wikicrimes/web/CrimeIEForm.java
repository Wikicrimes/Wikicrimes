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
import org.wikicrimes.service.LogUbiquityService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.Horario;
import org.wikicrimes.web.converter.WikiCrimesDataConverter;

public class CrimeIEForm extends GenericForm {

	private final Log log = LogFactory.getLog(CrimeForm.class);

	private Crime crime = null;
		
	private List<Crime> crimes;

	public CrimeService service;
	
	public Long tipoVitima; 
	
	public Crime crimeTemp;
	
	public String enderecoSIA;
	
	public Long tipoLocal;
	
	public Long tipoCrime;
	
	public String textoUbiq;
	
	public LogUbiquityService logUbiquityService;
	
	public UsuarioService usuarioService;
	
	private List tipoVitimaItens;
	
	private List TipoCrimeItens;

	private List tipoLocalItens;
	
	private List tipoPapelItens;

	private List<SelectItem> razoes = null;
	
	private List<String> razoesSel = new ArrayList<String>();
	
	private List<SelectItem> tipoCrimeItens;
	
	public String dataDaOcorrencia;
	
	public CrimeIEForm()  {		
		crime = new Crime();
		crimeTemp = new Crime();
		razoesSel.add("19");
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

	public List getTipoVitimaItens() {
		populateTipoVitimaItens();
		return tipoVitimaItens;
	}
	
	public String insert() {
		crime.setTipoLocal(service.getTipoLocal(Long.valueOf(getTipoLocal())));
		crime.setTipoPapel(service.getTipoPapel(Long.valueOf(getTipoPapel())));
		crime.setTipoRegistro(service.getTipoRegistro(Long.valueOf(getTipoRegistro())));
		crime.setTipoCrime(service.getTipoCrime(Long.valueOf(getTipoCrime())));
		crime.setTipoVitima(service.getTipoVitima(Long.valueOf(getTipoVitima())));
		crime.setTipoArmaUsada(service.getTipoArmaUsada(Long.valueOf(getTipoArmaUsada())));
		crime.setData(new Date(dataDaOcorrencia));
		List<Razao> razoesInsert = new ArrayList<Razao>();
		if(!(razoesSel.size()<1 || razoesSel.size()>4)){
			for (String razaoSel : razoesSel) {
				Razao r = new Razao();
				r.setIdRazao(Long.parseLong(razaoSel));
				razoesInsert.add(r);
			}
		}
		Set<Confirmacao> confirmacoes = new HashSet<Confirmacao>();
		Confirmacao confirmacao= new Confirmacao();
		Usuario usuarioConfirmacao = usuarioService.retornaUsuarioConfirmacao("wikicrimes@wikicrimes.org","Pt");
		confirmacao.setIndicacaoEmail(false);
		confirmacao.setUsuario(usuarioConfirmacao);
		confirmacoes.add(confirmacao);
		crime.setConfirmacoes(confirmacoes);
		
		Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
		Crime crime = getCrime();
		crime.setUsuario(usuario);
		crime.setConfirmacoesPositivas(new Long(0));
		crime.setConfirmacoesNegativas(new Long(0));
		crime.setDataHoraRegistro(new Date());
		crime.setIp(this.getIp());
		service.insert(crime,razoesInsert);
		//InterfaceSIA.setIdCrime(InterfaceSIA.getIdSIA(), Integer.parseInt(crime.getIdCrime().toString()));
		//Criando log
		
//		if (!crimeTemp.getEndereco().equals(
//				crime.getEndereco())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Endereco");
//			log.setCampoAntigo(crimeTemp.getEndereco());
//			log.setCampoNovo(crime.getEndereco());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getCidade().equals(
//				crime.getCidade())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Cidade");
//			log.setCampoAntigo(crimeTemp.getCidade());
//			log.setCampoNovo(crime.getCidade());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getEstado().equals(
//				crime.getEstado())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Estado");
//			log.setCampoAntigo(crimeTemp.getEstado());
//			log.setCampoNovo(crime.getEstado());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getPais().equals(
//				crime.getPais())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Pais");
//			log.setCampoAntigo(crimeTemp.getPais());
//			log.setCampoNovo(crime.getPais());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getCep().equals(
//				crime.getCep())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("CEP");
//			log.setCampoAntigo(crimeTemp.getCep());
//			log.setCampoNovo(crime.getCep());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getLatitude().equals(
//				crime.getLatitude())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Latitude");
//			log.setCampoAntigo(crimeTemp.getLatitude().toString());
//			log.setCampoNovo(crime.getLatitude().toString());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
//		if (!crimeTemp.getLongitude().equals(
//				crime.getLongitude())) {
//			LogUbiquity log = new LogUbiquity();
//			log.setData(new Date());
//			log.setCrime(service.getCrime(crime.getChave()));
//			log.setCampo("Longitude");
//			log.setCampoAntigo(crimeTemp.getLongitude().toString());
//			log.setCampoNovo(crime.getLongitude().toString());
//			Integer idSIA = InterfaceSIA.getIdSIA();
//			if(idSIA== null) idSIA=1;
//			log.setIdSIA(idSIA);
//			logUbiquityService.inserir(log);
//		}
		crime = new Crime();
		return "success";
	}
	
	
	public void populateTipoVitimaItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		List<BaseObject> result = new ArrayList();
		
			result = service.getTipoVitimaAll();
					
		for (int i = 0; i < result.size(); i++) {
			TipoVitima tipoVitima = (TipoVitima) result.get(i);
			String id = tipoVitima.getIdTipoVitima().toString();
			String nome = tipoVitima.getDescricao();

			itens.add(new SelectItem(id, nome));
		}
		
		if (result.size() <= 0) {
			itens.clear();
		}
		
		tipoVitimaItens = itens;
	}

	public void setRazoes(List<SelectItem> razoes) {
		this.razoes = razoes;
	}
	
	public Long getTipoLocal() {
    	return tipoLocal;
	}
	
	public Long getTipoCrime() {
    	return tipoCrime;
	}

	public List<String> getRazoesSel() {
		return razoesSel;
	}

	public void setRazoesSel(List<String> razoes) {
		this.razoesSel = razoes;
	}
	
	public void setTipoLocal(Long id) {
		tipoLocal = id;
		if (tipoLocal == 0){
			crime.setTipoLocal(new TipoLocal(Long.valueOf(tipoLocal)));
		}else
		if (tipoLocal != null ) 
			crime.setTipoLocal(service.getTipoLocal(tipoLocal));
	}
	
	public void setTipoCrime(Long id) {
		tipoCrime = id;
		if (tipoCrime == 0){
			crime.setTipoCrime(new TipoCrime(Long.valueOf(tipoCrime)));
		}else
		if (tipoCrime != null ) 
			crime.setTipoCrime(service.getTipoCrime(tipoCrime));
	}
	
	public List<SelectItem> getTipoPapelItens() {
		if (tipoPapelItens==null) {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoPapelAll();
		
		
		for (int i = 0; i < result.size(); i++) {
			TipoPapel tipoPapel = (TipoPapel) result.get(i);
			String id = tipoPapel.getIdTipoPapel().toString();
			String nome = tipoPapel.getNome();
			
			itens.add(new SelectItem(id, getMessage(nome,"")));
		}
		tipoPapelItens=itens;
		return itens;
		}
		else 
			return tipoPapelItens;
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
	
	public Date getDataUbiq() {
		return crime.getData();
	}

	public void setDataUbiq(String dataUbiq) {
		WikiCrimesDataConverter converter = new WikiCrimesDataConverter();
		Date date= (Date)converter.getAsObject(facesContext, null, dataUbiq);
		crime.setData(date);
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
	
		public List getTipoLocalItens() {
			List<SelectItem> itens = new ArrayList<SelectItem>();
			List<BaseObject> result = new ArrayList();
				result = service.getTipoLocalAll();
			
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
			return tipoLocalItens;
	}

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

		public List<SelectItem> getTipoCrimeItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<BaseObject> result = service.getTipoCrimeAll();
		
		for (int i = 0; i < result.size(); i++) {
			TipoCrime tipoCrime = (TipoCrime) result.get(i);
			String id = tipoCrime.getIdTipoCrime().toString();
			String nome = tipoCrime.getDescricao();

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

	public Long getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(Long tipoVitima) {
		this.tipoVitima = tipoVitima;
	}

	public String getDataDaOcorrencia() {
		return dataDaOcorrencia;
	}

	public void setDataDaOcorrencia(String dataDaOcorrencia) {
		this.dataDaOcorrencia = dataDaOcorrencia;
	}

	public Crime getCrimeTemp() {
		return crimeTemp;
	}

	public void setCrimeTemp(Crime crimeTemp) {
		this.crimeTemp = crimeTemp;
	}

	public LogUbiquityService getLogUbiquityService() {
		return logUbiquityService;
	}

	public void setLogUbiquityService(LogUbiquityService logUbiquityService) {
		this.logUbiquityService = logUbiquityService;
	}

	public String getEnderecoSIA() {
		return enderecoSIA;
	}

	public void setEnderecoSIA(String enderecoSIA) {
		this.enderecoSIA = enderecoSIA;
	}

	}
