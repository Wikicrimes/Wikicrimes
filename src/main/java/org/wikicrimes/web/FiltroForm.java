package org.wikicrimes.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.EstatisticaPais;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoVitima;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.DelegaciaService;
import org.wikicrimes.service.EstatisticaService;
import org.wikicrimes.service.RelatoService;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.GeoData;
import org.wikicrimes.util.Horario;

public class FiltroForm extends GenericForm {

	private final Log log = LogFactory.getLog(FiltroForm.class);

	private List<BaseObject> crimes;

	private CrimeService crimeService;
	
	private UsuarioService usuarioService;
	
	private RelatoService relatoService;
	
	private DelegaciaService delegaciaService;
	
	private String tutorAtivado = "0";
	
	private String urlKML;

	private Long horarioInicial;

	private Long horarioFinal;
	
	private String pais;

	private Date dataInicial;

	private Date dataFinal = new Date();

	private String idCrimeRegistrado;
	
	private String chaveRelatoRegistrado;
	
	private String descricaoRelato;

	private String zoomMapa = "4";
	
	//Pesquisa gen�rica
	
	private String tipoPesquisaSel;
	
	private String valorPesquisado;
	
	private List<Crime> crimesRespostaPesqGen = new ArrayList<Crime>();
	
	//Pesquisa gen�rica
	//Controle 10 mais
	
	private List<Crime> dezMais = null;
	
	private String cssVistos;
	
	private String cssComentados;
	
	private String cssConfirmados;
	
	private Double credibilidadeInicial;
	private Double credibilidadeFinal;
	
	//Controle 10 mais
	
	// private String latMapa="-3.738189815174382";

	// private String lngMapa="-38.52081298828125";
	private String latMapa = "-14.235004";
	private String lngMapa = "-51.925280";
	private Long tipoCrime;

	private Long tipoVitima;

	private Long tipoLocal;
	
	private Long entidadeCertificadora;
	
	private Boolean crimeConfirmadoPositivamente;
	
	private Boolean crimeConfirmadoEntCer;

	private int quantidadeCrimes = 0;

	private List<SelectItem> tipoCrimeItens = new ArrayList<SelectItem>();

	private List<SelectItem> tipoVitimaItens = new ArrayList<SelectItem>();

	private List<SelectItem> tipoLocalItens = new ArrayList<SelectItem>();

	private List<SelectItem> horarioItens = new ArrayList<SelectItem>();

	private List<SelectItem> entidadeCertificadoraItens = new ArrayList<SelectItem>();

	private boolean primeiroLogon = true;

	private Integer maxResults = null;	
	
	private boolean primeiraVez= true;
	
	private boolean paisIdentificado=false;
	
	private EstatisticaService estatisticaService;
	
	public void setEstatisticaService(EstatisticaService estatisticaService){
		this.estatisticaService=estatisticaService;
	}
	
	public EstatisticaService getEstatisticaService (){
		return this.estatisticaService;
	}
	

	// public FiltroForm() {
	// showAllIfNotExist();
	// }

	public FiltroForm() {
		cssVistos = "background-color:#C6E2FF;";
		cssComentados = "";
		cssConfirmados = "";

		credibilidadeInicial = 50.0;
		credibilidadeFinal   = 100.0;
		
		/*
		 * Calendar cal = new GregorianCalendar(); cal.setTime(dataFinal);
		 * cal.add(Calendar.DAY_OF_MONTH, -30); dataInicial=cal.getTime();
		 */		
	}
	//Identifica o pais de acordo com o ip
	private void identificaPaisOrigem(){
		paisIdentificado=true;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ip = request.getRemoteAddr();
		if(!ip.equalsIgnoreCase("127.0.0.1") && !ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
			GeoData geo =GeoData.getInstance();
			pais =geo.getLookupService().getCountry(ip).getCode();
			EstatisticaPais ep= this.estatisticaService.getEstatisticaPais(pais);
			if (ep!=null && ep.getLatitude() != null && ep.getLongitude()!= null){
				 this.latMapa=ep.getLatitude().toString();
				 this.lngMapa =ep.getLongitude().toString();
				 System.out.println("[" + new Date() + "] IP: " + ip + " :" + pais);
			 
			}
			this.zoomMapa = "4";
			if (latMapa==null || lngMapa==null){
				System.err.println("[" + new Date() + "] NAO ENCONTRADA LAT LNG DO PAIS: " + geo.getLookupService().getCountry(ip).getName() + " " + pais );
				latMapa = "-14.235004";
				lngMapa = "-51.925280";
			}


		}
	}

	
	public String dezMaisVistos(){
		cssVistos = "background-color:#C6E2FF;";
		cssComentados = "";
		cssConfirmados = "";
		dezMais = crimeService.getCrimesMaisVistos();
		Long cont = new Long(1);
		for (Iterator<Crime> iterator = dezMais.iterator(); iterator
				.hasNext();) {
			Crime crime = (Crime) iterator.next();
			if (crime.getDescricao().length() > 10)
				crime.setDescricao(crime.getDescricao().substring(0, 10)
						+ "...");
			else
				crime.setDescricao(crime.getDescricao().substring(0,
						crime.getDescricao().length() - 1));
			crime.setIdCrime(cont++);
		}
		return null;
	}
	
	public String dezMaisComentados(){
		cssVistos = "";
		cssComentados = "background-color:#C6E2FF;";
		cssConfirmados = "";
		dezMais = crimeService.getCrimesMaisComentados();
		Long cont = new Long(1);
		for (Iterator<Crime> iterator = dezMais.iterator(); iterator
				.hasNext();) {
			Crime crime = (Crime) iterator.next();
			if (crime.getDescricao().length() > 10)
				crime.setDescricao(crime.getDescricao().substring(0, 10)
						+ "...");
			else
				crime.setDescricao(crime.getDescricao().substring(0,
						crime.getDescricao().length() - 1));
			crime.setIdCrime(cont++);
		}
		return null;
	}
	
	public String dezMaisConfirmados(){
		cssVistos = "";
		cssComentados = "";
		cssConfirmados = "background-color:#C6E2FF;";
		dezMais = crimeService.getCrimesMaisConfirmados();
		Long cont = new Long(1);
		for (Iterator<Crime> iterator = dezMais.iterator(); iterator
				.hasNext();) {
			Crime crime = (Crime) iterator.next();
			if (crime.getDescricao().length() > 10)
				crime.setDescricao(crime.getDescricao().substring(0, 10)
						+ "...");
			else
				crime.setDescricao(crime.getDescricao().substring(0,
						crime.getDescricao().length() - 1));
			crime.setIdCrime(cont++);
		}
		return null;
	}
	
	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public void showAllIfNotExist() {

		if (crimes == null) {
			// showAll();
		}
		Usuario usuario = (Usuario) this.getExternalSession().getAttribute(
				"usuario");
		if (usuario == null) {
			usuario = (Usuario) this.getSessionScope().get("usuario");
		}
		if (usuario != null && primeiroLogon) {
			if (usuario.getLat() != null && usuario.getLng() != null) {
				this.setLatMapa(usuario.getLat().toString());
				this.setLngMapa(usuario.getLng().toString());
				this.setZoomMapa("12");
			}
			primeiraVez=true;
			this.primeiroLogon = false;
		}
		else {
			if (!paisIdentificado)
				identificaPaisOrigem();
		}
	}

	public List<SelectItem> getTipoCrimeItens() {
		tipoCrimeItens = new ArrayList<SelectItem>();

		List tipoCrimeAll = crimeService.getTipoCrimeAll();
		for (int i = 0; i < tipoCrimeAll.size(); i++) {
			TipoCrime tipoCrime = (TipoCrime) tipoCrimeAll.get(i);
			this.tipoCrimeItens.add(new SelectItem(tipoCrime.getIdTipoCrime(),
					tipoCrime.getNome()));
		}

		return tipoCrimeItens;
	}

	public List<SelectItem> getEntidadeCertificadoraItens() {
		entidadeCertificadoraItens = new ArrayList<SelectItem>();
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
		this.entidadeCertificadoraItens.add(
				new SelectItem("",""));
		this.entidadeCertificadoraItens.add(
				new SelectItem("0",bundle.getString("crime.filtro.option.todasEntidadesCertificadoras")));
		
		List entidadeCertificadoraAll = crimeService
				.getEntidadeCertificadoraAll();
		for (int i = 0; i < entidadeCertificadoraAll.size(); i++) {
			EntidadeCertificadora entidadeCertificadora = (EntidadeCertificadora) entidadeCertificadoraAll
					.get(i);
			this.entidadeCertificadoraItens.add(new SelectItem(
					""+entidadeCertificadora.getIdEntidadeCertificadora(),
					entidadeCertificadora.getNome()));
			
		}
		return entidadeCertificadoraItens;
	}

	public void populateTipoVitimaItens() {
		this.tipoVitimaItens = new ArrayList<SelectItem>();

		List tipoVitimaItens = crimeService
				.findTipoVitimaByTipoCrime(this.tipoCrime);
		for (int i = 0; i < tipoVitimaItens.size(); i++) {
			TipoVitima tipoVitima = (TipoVitima) tipoVitimaItens.get(i);
			this.tipoVitimaItens.add(new SelectItem(tipoVitima
					.getIdTipoVitima(), tipoVitima.getNome()));
		}
	}

	public void populateTipoLocalItens() {
		this.tipoLocalItens = new ArrayList<SelectItem>();

		List tipoVitimaItens = crimeService
				.findTipoLocalByTipoVitima(this.tipoVitima);
		for (int i = 0; i < tipoVitimaItens.size(); i++) {
			TipoLocal tipoLocal = (TipoLocal) tipoVitimaItens.get(i);
			this.tipoLocalItens.add(new SelectItem(tipoLocal.getIdTipoLocal(),
					tipoLocal.getNome()));
		}
	}

	public List<SelectItem> getHorarioItens() {
		horarioItens = new ArrayList<SelectItem>();

		Iterator<Horario> horarios = Horario.iterator();
		while (horarios.hasNext()) {
			Horario horario = horarios.next();
			horarioItens.add(new SelectItem(new Long(horario.ord()), horario
					.toString()));
		}

		return horarioItens;
	}

	public void setHorarioItens(List<SelectItem> horarioItens) {
		this.horarioItens = horarioItens;
	}

	public String update() {
		String returnPage = null;
		// String returnPage = FAILURE;
		/*
		 * try { Map parameters = new HashMap();
		 * 
		 * if (tipoCrime != null && !tipoCrime.equals(new Long(0))) {
		 * parameters.put("tipoCrime", new TipoCrime(tipoCrime)); }
		 * 
		 * if (tipoVitima != null && !tipoVitima.equals(new Long(0))) {
		 * parameters.put("tipoVitima", new TipoVitima(tipoVitima)); }
		 * 
		 * if (tipoLocal != null && !tipoLocal.equals(new Long(0))) {
		 * parameters.put("tipoLocal", new TipoLocal(tipoLocal)); }
		 * 
		 * if (horarioInicial != null) { parameters.put("horarioInicial",
		 * horarioInicial); }
		 * 
		 * if (horarioFinal != null) { parameters.put("horarioFinal",
		 * horarioFinal); }
		 * 
		 * if (dataInicial != null) { parameters.put("dataInicial",
		 * dataInicial); }
		 * 
		 * if (dataFinal != null) { parameters.put("dataFinal", dataFinal); }
		 * 
		 * crimes = (List<BaseObject>) crimeService.filter(parameters);
		 * 
		 * 
		 * 
		 * returnPage = SUCCESS; } catch (Exception e) {
		 * System.err.println(e.getMessage()); e.printStackTrace();
		 * addMessage("errors.geral", e.getMessage()); }
		 */
		return returnPage;
	}

	public Crime getCrime(Long id){
		
		return (Crime) crimeService.get(id);
	}
	public Crime getCrime(String chave){
		return (Crime) crimeService.getCrime(chave);
	}
	
	public Relato getRelato(String chave){
		return (Relato) relatoService.getRelato(chave);
	}
	
	public List<BaseObject> getDelegaciasFiltrados(String norte, String sul, String leste, String oeste){
		Map parameters = new HashMap();
		if (norte != null && sul != null && leste != null && oeste != null ){
			if (!norte.equals("undefined"))					
				parameters.put("norte", Double.parseDouble(norte));
			if (!sul.equals("undefined"))
				parameters.put("sul", Double.parseDouble(sul));
			if (!leste.equals("undefined"))
				parameters.put("leste", Double.parseDouble(leste));
			if (!oeste.equals("undefined"))
				parameters.put("oeste", Double.parseDouble(oeste));
			
		}
		List<BaseObject> delegacias = delegaciaService.filter(parameters);
		return delegacias;
	}
	public List<BaseObject> getRelatosFiltrados(String norte, String sul, String leste, String oeste,String dataInicial, String dataFinal, boolean recuperar){
		Map parameters = new HashMap();
		
		//viewport
		if (norte != null && sul != null && leste != null && oeste != null ){
			if (!norte.equals("undefined"))					
				parameters.put("norte", Double.parseDouble(norte));
			if (!sul.equals("undefined"))
				parameters.put("sul", Double.parseDouble(sul));
			if (!leste.equals("undefined"))
				parameters.put("leste", Double.parseDouble(leste));
			if (!oeste.equals("undefined"))
				parameters.put("oeste", Double.parseDouble(oeste));
			
		}
		if (dataInicial != null && dataInicial != "") {
			try {
				String pattern = "dd,MM,yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date data = null;
				data = sdf.parse(dataInicial);
				parameters.put("dataInicial", data);
			} catch (ParseException e) {
				System.err.println("FiltroForm -> dataInicial mal formatada: " + dataInicial);
			}
		}

		if (dataFinal != null && dataFinal != "") {
			try {
				String pattern = "dd,MM,yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date data = null;
				data = sdf.parse(dataFinal);
				parameters.put("dataFinal", data);
			} catch (ParseException e) {
				System.err.println("FiltroForm -> dataInicial mal formatada: " + dataInicial);
			}
		}
		if(relatoService != null && recuperar)
			return relatoService.filter(parameters);
		else
			return new ArrayList<BaseObject>();
	}
			
	
	public List<BaseObject> getCrimesFiltrados(String tipoCrime,
			String tipoVitima, String tipoLocal, String horarioInicial,
			String horarioFinal, String dataInicial, String dataFinal,
			String entidadeCertificadora, String crimeConfirmadoPositivamente2,
			String norte, String sul, String leste, String oeste, 
			String ignoraData, String emailUsuario){
		try {
			Map params = getCrimeFilterParameters(tipoCrime, tipoVitima, tipoLocal, 
					horarioInicial, horarioFinal, dataInicial, dataFinal, 
					entidadeCertificadora, crimeConfirmadoPositivamente2, 
					norte, sul, leste, oeste, ignoraData, emailUsuario);
			List<BaseObject> result = crimeService.filter(params);
			undoIgnoreStuff(result, ignoraData);
			return result;
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
			return crimes;
		}
	}
	
	private Map getCrimeFilterParameters(String tipoCrime,
			String tipoVitima, String tipoLocal, String horarioInicial,
			String horarioFinal, String dataInicial, String dataFinal,
			String entidadeCertificadora, String crimeConfirmadoPositivamente2, String norte, String sul, String leste, String oeste, String ignoraData, String emailUsuario) throws ParseException {
		List<BaseObject> crimes = new ArrayList<BaseObject>();
		//se e' o primeira vez
		if(ignoraData!=null){
			if (primeiraVez || ignoraData.equals("true")) { 
				setMaxResults(600);
				dataInicial="";
				
			}
		}	
	
		Map parameters = new HashMap();
		parameters.put("credibilidadeInicial", credibilidadeInicial / 100);
		parameters.put("credibilidadeFinal", credibilidadeFinal / 100);
		
		if (emailUsuario != null && !emailUsuario.equalsIgnoreCase("") && !emailUsuario.equalsIgnoreCase("undefined")) {
			parameters.put("emailUsuario", emailUsuario);
		}
		
		if (tipoCrime != null && !tipoCrime.equals("0") && tipoCrime != "") {
			parameters.put("tipoCrime", new TipoCrime(new Long(tipoCrime)));
		}

		if (tipoVitima != null && !tipoVitima.equals("0")
				&& tipoVitima != "") {
			parameters.put("tipoVitima", new TipoVitima(
					new Long(tipoVitima)));
		}

		if (tipoLocal != null && !tipoLocal.equals("0") && tipoLocal != "") {
			parameters.put("tipoLocal", new TipoLocal(new Long(tipoLocal)));
		}

		if (horarioInicial != null && horarioInicial != "" && !horarioInicial.equals("-1")) {
			parameters.put("horarioInicial", Long.parseLong(horarioInicial));
		}

		if (horarioFinal != null && horarioFinal != "" && !horarioFinal.equals("-1")) {
			parameters.put("horarioFinal", Long.parseLong(horarioFinal));
		}

		if (dataInicial != null && dataInicial != "") {
			try {
				String pattern = "dd,MM,yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date data = sdf.parse(dataInicial);
				parameters.put("dataInicial", data);
			}catch(ParseException e) {
				System.err.println("FiltroForm -> dataInicial mal formatada: " + dataInicial);
			}
		}

		if (dataFinal != null && dataFinal != "") {
			String pattern = "dd,MM,yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date data = sdf.parse(dataFinal);
			parameters.put("dataFinal", data);
		}
		//viewport
		if (norte != null && sul != null && leste != null && oeste != null ){
			if (!norte.equals("undefined"))					
				parameters.put("norte", Double.parseDouble(norte));
			if (!sul.equals("undefined"))
				parameters.put("sul", Double.parseDouble(sul));
			if (!leste.equals("undefined"))
				parameters.put("leste", Double.parseDouble(leste));
			if (!oeste.equals("undefined"))
				parameters.put("oeste", Double.parseDouble(oeste));
			
		}
		
		if (maxResults != null)
			parameters.put("maxResults", this.getMaxResults());
		if (entidadeCertificadora != null
				&& !entidadeCertificadora.equals("0") && !entidadeCertificadora.equals("-1")
				&& entidadeCertificadora != "") {
			List<BaseObject> entidadesCertificadoras = new ArrayList<BaseObject>();
			String[] idsEntidades = entidadeCertificadora.split(" ");
			for (String idEntidade : idsEntidades)
				entidadesCertificadoras.add(new EntidadeCertificadora(Long
						.parseLong(idEntidade)));
			parameters
					.put("entidadeCertificadora", entidadesCertificadoras);
		}
		//se entidadeCertificadora igual a Todas 
		if (entidadeCertificadora != null
				&& entidadeCertificadora.equals("0") && !entidadeCertificadora.equals("-1")) {
			List<BaseObject> entidadesCertificadoras = new ArrayList<BaseObject>();
			parameters
					.put("entidadeCertificadora", entidadesCertificadoras);
		}
		if (crimeConfirmadoPositivamente2 != null && crimeConfirmadoPositivamente2 != ""
				) {
			
			parameters.put("crimeConfirmadoPositivamente", new Boolean(crimeConfirmadoPositivamente2));

		}
		return parameters;
	}
	
	private void undoIgnoreStuff(List<BaseObject> result, String ignoraData){
		if(ignoraData!=null){
			if (primeiraVez || ignoraData.equals("true")) {
				setMaxResults(null);
				if (result !=null && result.size()>0)
					setDataInicial(((Crime) result.get(result.size()-1)).getData());
				primeiraVez=false;
			}
		}	
	}

	public String showAll() {
		String returnPage = FAILURE;

		try {
			Map parameters = new HashMap();
			if (maxResults != null)
				parameters.put("maxResults", this.getMaxResults());
			crimes = crimeService.filter(parameters);

			returnPage = SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}

	public String clear() {
		String returnPage = FAILURE;

		try {
			setDataInicial(null);
			setDataFinal(null);
			setTipoCrime(null);
			setTipoVitima(null);
			setTipoLocal(null);
			setHorarioInicial(null);
			setHorarioFinal(null);
			setEntidadeCertificadora(null);
			setCrimeConfirmadoPositivamente(false);
			

			crimes = null;
			returnPage = SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
		}

		return returnPage;
	}
	

	public CrimeService getCrimeService() {
		return crimeService;
	}

	public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}
	
	public void setDelegaciaService(DelegaciaService delegaciaService) {
		this.delegaciaService = delegaciaService;
	}
	
	public DelegaciaService getDelegaciaService(){
		return this.delegaciaService;
	}

	public Long getHorarioInicial() {
		return horarioInicial;
	}

	public Long getHorarioFinal() {
		return horarioFinal;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {

		this.dataInicial = dataInicial;

	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Long getTipoCrime() {
		return tipoCrime;
	}

	public void setTipoCrime(Long tipoCrime) {
		this.tipoCrime = tipoCrime;
	}

	public Long getTipoVitima() {
		return tipoVitima;
	}

	public void setTipoVitima(Long tipoVitima) {
		this.tipoVitima = tipoVitima;
	}

	public Long getTipoLocal() {
		return tipoLocal;
	}

	public void setTipoLocal(Long tipoLocal) {
		this.tipoLocal = tipoLocal;
	}

	public List<SelectItem> getTipoVitimaItens() {
		return tipoVitimaItens;
	}

	public List<SelectItem> getTipoLocalItens() {
		return tipoLocalItens;
	}

	public void setTipoCrimeItens(List<SelectItem> tipoCrimeItens) {
		this.tipoCrimeItens = tipoCrimeItens;
	}

	public void setTipoVitimaItens(List<SelectItem> tipoVitimaItens) {
		this.tipoVitimaItens = tipoVitimaItens;
	}

	public void setTipoLocalItens(List<SelectItem> tipoLocalItens) {
		this.tipoLocalItens = tipoLocalItens;
	}

	public void setHorarioInicial(Long horarioInicial) {
		this.horarioInicial = horarioInicial;
	}

	public void setHorarioFinal(Long horarioFinal) {
		this.horarioFinal = horarioFinal;
	}

	public List<BaseObject> getCrimes() {
		return crimes;
	}

	public void setCrimes(List<BaseObject> crimes) {
		this.crimes = crimes;
	}

	/*
	 * retorna a quantidade de crimes registrados. leo ayres
	 */
	public int getQuantidadeCrimes() {
		this.quantidadeCrimes = crimeService
				.getQuantidadeCrimesRegistradosAtivos();
		return quantidadeCrimes;
	}

	public void setQuantidadeCrimes(int quantidadeCrimes) {

		this.quantidadeCrimes = quantidadeCrimes;
	}

	public String getZoomMapa() {
		return zoomMapa;
	}

	public void setZoomMapa(String zoomMapa) {
		this.zoomMapa = zoomMapa;
	}

	public String getLatMapa() {
		if (!paisIdentificado)
			identificaPaisOrigem();
		return latMapa;
	}

	public void setLatMapa(String latMapa) {
		this.latMapa = latMapa;
	}

	public String getLngMapa() {
		if (!paisIdentificado)
			identificaPaisOrigem();
		return lngMapa;
	}

	public void setLngMapa(String lngMapa) {
		this.lngMapa = lngMapa;
	}

	public int getCrimesSize() {
		if (crimes != null)
			return crimes.size();
		else
			return -1;
	}
	

	public String getIdCrimeRegistrado() {
		return idCrimeRegistrado;
	}

	public void setIdCrimeRegistrado(String idCrimeRegistrado) {
		this.idCrimeRegistrado = idCrimeRegistrado;
	}
	// GET e SET EntidadeCertificadora
	public Long getEntidadeCertificadora() {
		return entidadeCertificadora;
	}

	public void setEntidadeCertificadora(Long entidadeCertificadora) {
		this.entidadeCertificadora = entidadeCertificadora;
	}
	// GET e SET CrimeConfirmadoPositivamente
	public Boolean getCrimeConfirmadoPositivamente() {
		return crimeConfirmadoPositivamente;
	}

	public void setCrimeConfirmadoPositivamente(Boolean crimeConfirmadoPositivamente) {
		this.crimeConfirmadoPositivamente = crimeConfirmadoPositivamente;
	}

	public boolean isPrimeiraVez() {
		return primeiraVez;
	}

	public void setPrimeiraVez(boolean primeiraVez) {
		this.primeiraVez = primeiraVez;
	}

	public Boolean getCrimeConfirmadoEntCer() {
		return crimeConfirmadoEntCer;
	}

	public void setCrimeConfirmadoEntCer(Boolean crimeConfirmadoEntCer) {
		this.crimeConfirmadoEntCer = crimeConfirmadoEntCer;
	}

	public RelatoService getRelatoService() {
		return relatoService;
	}

	public void setRelatoService(RelatoService relatoService) {
		this.relatoService = relatoService;
	}

	public String getChaveRelatoRegistrado() {
		return chaveRelatoRegistrado;
	}

	public void setChaveRelatoRegistrado(String chaveRelatoRegistrado) {
		this.chaveRelatoRegistrado = chaveRelatoRegistrado;
	}

	public String getDescricaoRelato() {
		return descricaoRelato;
	}

	public void setDescricaoRelato(String descricaoRelato) {
		this.descricaoRelato = descricaoRelato;
	}

	public String getTipoPesquisaSel() {
		return tipoPesquisaSel;
	}

	public void setTipoPesquisaSel(String tipoPesquisaSel) {
		this.tipoPesquisaSel = tipoPesquisaSel;
	}	
	
	public String getValorPesquisado() {
		return valorPesquisado;
	}

	public void setValorPesquisado(String valorPesquisado) {
		this.valorPesquisado = valorPesquisado;
	}

	public List<Crime> getCrimesRespostaPesqGen() {
		return crimesRespostaPesqGen;
	}

	public void setCrimesRespostaPesqGen(List<Crime> crimesRespostaPesqGen) {
		this.crimesRespostaPesqGen = crimesRespostaPesqGen;
	}
	
	public String desabilitaTutor(){
		tutorAtivado = "0";
		Usuario usuario = (Usuario) this.getExternalSession().getAttribute(
		"usuario");
		if (usuario == null) {
			usuario = (Usuario) this.getSessionScope().get("usuario");
		}
		if(usuario != null){			
			Usuario usuarioBanco = (Usuario)usuarioService.get(usuario.getIdUsuario());
			usuario.setTutorAtivado("0");
			usuarioBanco.setTutorAtivado("0");
			usuarioService.update(usuarioBanco);
		}
		return null;
	}

	public String pesquisarGenericamente(){		
		crimesRespostaPesqGen.clear();
		if(tipoPesquisaSel!=null){
			if(tipoPesquisaSel.equalsIgnoreCase("2")){
				Crime crimePesquisado = new Crime();
				crimePesquisado.setDescricao(valorPesquisado);
				crimesRespostaPesqGen = crimeService.pesquisarCrime(crimePesquisado);
				Long cont = new Long(1);
				for (Iterator<Crime> iterator = crimesRespostaPesqGen.iterator(); iterator
						.hasNext();) {
					Crime crime = (Crime) iterator.next();
					if(crime.getDescricao().length()>30)
						crime.setDescricao(crime.getDescricao().substring(0, 30)+"...");
					else
						crime.setDescricao(crime.getDescricao().substring(0, crime.getDescricao().length()));
					
					if(crime.getCidade() == null){
						crime.setCidade("");
					}else{
						crime.setDescricao(crime.getDescricao()+" ("+ crime.getCidade() +")");
					}
					
					crime.setIdCrime(cont++);
				}
			}
		}
		return null;
	}

	public List<Crime> getDezMais() {
		
		if(dezMais==null){
			dezMaisVistos();			
		}	
		return dezMais;
	}

	public void setDezMais(List<Crime> dezMais) {
		this.dezMais = dezMais;
	}

	public String getCssVistos() {
		return cssVistos;
	}

	public void setCssVistos(String cssVistos) {
		this.cssVistos = cssVistos;
	}

	public String getCssComentados() {
		return cssComentados;
	}

	public void setCssComentados(String cssComentados) {
		this.cssComentados = cssComentados;
	}

	public String getCssConfirmados() {
		return cssConfirmados;
	}

	public void setCssConfirmados(String cssConfirmados) {
		this.cssConfirmados = cssConfirmados;
	}

	public String getTutorAtivado() {
		return tutorAtivado;
	}

	public void setTutorAtivado(String tutorAtivado) {
		this.tutorAtivado = tutorAtivado;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Double getCredibilidadeInicial() {
		return credibilidadeInicial;
	}
	public void setCredibilidadeInicial(Double credibilidadeInicial) 
	{
		this.credibilidadeInicial = credibilidadeInicial;
		
		if (credibilidadeInicial > credibilidadeFinal)
			credibilidadeFinal = credibilidadeInicial;
	}

	public Double getCredibilidadeFinal() {
		return credibilidadeFinal;
	}
	public void setCredibilidadeFinal(Double credibilidadeFinal) 
	{
		this.credibilidadeFinal = credibilidadeFinal;
		
		if (credibilidadeInicial > credibilidadeFinal)
			this.credibilidadeFinal = credibilidadeInicial;
	}
	
	public Map<String, String> getFiltroStringMap(){
		Map<String, String> map = new HashMap<String, String>();
		
		//tipo
		if(tipoCrime != null && tipoCrime != 0)
			map.put("tipoCrime", tipoCrime.toString());
		if(tipoVitima != null && tipoVitima != 0)
			map.put("tipoVitima", tipoVitima.toString());
		if(tipoLocal != null && tipoLocal != 0)
			map.put("tipoLocal", tipoLocal.toString());
		
		//data e hora
		DateFormat df = new SimpleDateFormat("dd,MM,yyyy");
		if(dataInicial != null)
			map.put("dataInicial", df.format(dataInicial));
		if(dataFinal != null)
			map.put("dataFinal", df.format(dataFinal));
		if(horarioInicial != null && horarioInicial != -1)
			map.put("horarioInicial", horarioInicial.toString());
		if(horarioFinal != null && horarioFinal != -1)
			map.put("horarioFinal", horarioFinal.toString());
		
		//credibilidade
		if(entidadeCertificadora != null && entidadeCertificadora != 0 )
			map.put("entidadeCertificadora", entidadeCertificadora.toString());
		if(crimeConfirmadoPositivamente != null && crimeConfirmadoPositivamente != false)
			map.put("confirmadoPositivamente", crimeConfirmadoPositivamente.toString());
		
		return map;
	}
	
	public Map<String, Object> getFiltroMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//tipo
		if(tipoCrime != null && tipoCrime != 0)
			map.put("tipoCrime", new TipoCrime(getTipoCrime()));
		if(tipoVitima != null && tipoVitima != 0)
			map.put("tipoVitima", new TipoVitima(getTipoVitima()));
		if(tipoLocal != null && tipoLocal != 0)
			map.put("tipoLocal", new TipoLocal(getTipoLocal()));
		
		//data e hora
		if(dataInicial != null)
			map.put("dataInicial", getDataInicial());
		if(dataFinal != null)
			map.put("dataFinal", getDataFinal());
		if(horarioInicial != null && horarioInicial != -1)
			map.put("horarioInicial", getHorarioInicial());
		if(horarioFinal != null && horarioFinal != -1)
			map.put("horarioFinal", getHorarioFinal());
		
		//credibilidade
		if(entidadeCertificadora != null && entidadeCertificadora != 0 ){
			List<BaseObject> list = new ArrayList<BaseObject>();
			list.add(new EntidadeCertificadora(getEntidadeCertificadora()));
			map.put("entidadeCertificadora", list);
		}
		if(crimeConfirmadoPositivamente != null && crimeConfirmadoPositivamente != false)
			map.put("crimeConfirmadoPositivamente", getCrimeConfirmadoPositivamente());
		if(credibilidadeInicial != null)
			map.put("credibilidadeInicial", getCredibilidadeInicial()/100);
		if(credibilidadeFinal != null)
			map.put("credibilidadeFinal", getCredibilidadeFinal()/100);
		
		return map;
	}
	
	public String getUrlKML() {
		return urlKML;
	}

	public void setUrlKML(String urlKML) {
		this.urlKML = urlKML;
	}
	
}
