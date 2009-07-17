package org.wikicrimes.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.UsuarioService;

public class AdminForm extends GenericForm {
	
	private Crime crime;
	
	private Usuario usuarioAdminForm;
	
	private Usuario usuarioEdit;
	
	private UsuarioService usuarioService;
	
	private CrimeService crimeService;

	private EntidadeCertificadora entidadeCertificadora;
	
	private String entCerSel;
	
	private String entCerSelEdit;
	
	private List<BaseObject> usuarios = new ArrayList<BaseObject>();
	
	private int qtdUsuariosConf;
	
	private String dataInicial;

	private String dataFinal;
	
	
	

	public AdminForm() {
		crime = new Crime();
		crime.setTipoCrime(new TipoCrime());
		crime.setTipoArmaUsada(new TipoArmaUsada());
		usuarioAdminForm = new Usuario();		
		usuarioAdminForm.setEntidadeCertificadora(new EntidadeCertificadora());
		
		entidadeCertificadora = new EntidadeCertificadora();
		//qtdUsuariosConf = usuarioService.UsuariosConf().size();
	}
	
	public Integer getSizeUsuarios(){
		return usuarios.size();
	}
	
	public List<SelectItem> getEstadosBrasileiros() {
		List<SelectItem> listaSelect = new ArrayList<SelectItem>();
		listaSelect.add(new SelectItem("AC","AC"));
		listaSelect.add(new SelectItem("AL","AL"));
		listaSelect.add(new SelectItem("AM","AM"));
		listaSelect.add(new SelectItem("AP","AP"));
		listaSelect.add(new SelectItem("BA","BA"));
		listaSelect.add(new SelectItem("CE","CE"));
		listaSelect.add(new SelectItem("DF","DF"));
		listaSelect.add(new SelectItem("ES","ES"));
		listaSelect.add(new SelectItem("GO","GO"));
		listaSelect.add(new SelectItem("MA","MA"));
		listaSelect.add(new SelectItem("MG","MG"));
		listaSelect.add(new SelectItem("MS","MS"));
		listaSelect.add(new SelectItem("MT","MT"));
		listaSelect.add(new SelectItem("PA","PA"));
		listaSelect.add(new SelectItem("PB","PB"));
		listaSelect.add(new SelectItem("PE", "PE"));
		listaSelect.add(new SelectItem("PI","PI"));
		listaSelect.add(new SelectItem("PR","PR"));
		listaSelect.add(new SelectItem("RJ","RJ"));
		listaSelect.add(new SelectItem("RN","RN"));
		listaSelect.add(new SelectItem("RO","RO"));
		listaSelect.add(new SelectItem("RR","RR"));
		listaSelect.add(new SelectItem("RS","RS"));
		listaSelect.add(new SelectItem("SC","SC"));
		listaSelect.add(new SelectItem("SE","SE"));
		listaSelect.add(new SelectItem("SP","SP"));
		listaSelect.add(new SelectItem("TO","TO"));
		return listaSelect;
	}
	public List<SelectItem> getEntidadeCertificadoras() {
		List <SelectItem>entidadeCertificadoraItens = new ArrayList<SelectItem>();
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
		entidadeCertificadoraItens.add(
				new SelectItem("-1",""));
		entidadeCertificadoraItens.add(
				new SelectItem("0",bundle.getString("crime.filtro.option.todasEntidadesCertificadoras")));
		
		List<BaseObject> entidadeCertificadoraAll = crimeService
				.getEntidadeCertificadoraAll();
		for (int i = 0; i < entidadeCertificadoraAll.size(); i++) {
			EntidadeCertificadora entidadeCertificadora = (EntidadeCertificadora) entidadeCertificadoraAll
					.get(i);
			entidadeCertificadoraItens.add(new SelectItem(
					""+entidadeCertificadora.getIdEntidadeCertificadora(),
					entidadeCertificadora.getNome()));
			
		}
		return entidadeCertificadoraItens;
	}
	
	public List<SelectItem> getEntidadeCertificadorasTelaEdicao() {
		List <SelectItem>entidadeCertificadoraItens = new ArrayList<SelectItem>();	
		
		entidadeCertificadoraItens.add(
				new SelectItem("-1",""));		
		List<BaseObject> entidadeCertificadoraAll = crimeService
				.getEntidadeCertificadoraAll();
		for (int i = 0; i < entidadeCertificadoraAll.size(); i++) {
			EntidadeCertificadora entidadeCertificadora = (EntidadeCertificadora) entidadeCertificadoraAll
					.get(i);
			entidadeCertificadoraItens.add(new SelectItem(
					""+entidadeCertificadora.getIdEntidadeCertificadora(),
					entidadeCertificadora.getNome()));
			
		}
		return entidadeCertificadoraItens;
	}

	public int getQtdUsuariosConf() {
		return qtdUsuariosConf;
	}

	public void setQtdUsuariosConf(int qtdUsuariosConf) {
		this.qtdUsuariosConf = qtdUsuariosConf;
	}

	public String acessoListarUsuariosConf(){
		
		String returnPage = FAILURE;
		usuarios= usuarioService.UsuariosConf();
		returnPage = "successUser";
		return returnPage;
	}
	
	
	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public CrimeService getCrimeService() {
		return crimeService;
	}

	public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}
	
	public List<BaseObject> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<BaseObject> usuarios) {
		this.usuarios = usuarios;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Usuario getUsuarioAdminForm() {
		return usuarioAdminForm;
	}

	public void setUsuarioAdminForm(Usuario usuario) {
		this.usuarioAdminForm = usuario;
	}

	
	public EntidadeCertificadora getEntidadeCertificadora() {
		return entidadeCertificadora;
	}

	public void setEntidadeCertificadora(EntidadeCertificadora entidadeCertificadora) {
		this.entidadeCertificadora = entidadeCertificadora;
	}

	public void setTipoArmaUsada(String id) {
		crime.setTipoArmaUsada(new TipoArmaUsada(Long.valueOf(id)));
	}
	
	public String alterarUsuario(){
		if (expirouSessao()) {
			return SESSAO_EXPIRADA;
		}
		if(!isAdmin()){
			return SESSAO_EXPIRADA;
		}
		if(entCerSelEdit.equalsIgnoreCase("-1")){
			Perfil p = new Perfil();
			p.setIdPerfil(Long.parseLong(Perfil.USUARIO+""));
			usuarioEdit.setPerfil(p);
			usuarioEdit.setEntidadeCertificadora(null);
		}
		else{
			Perfil p = new Perfil();
			p.setIdPerfil(Long.parseLong(Perfil.CERTIFICADOR+""));
			usuarioEdit.setPerfil(p);
			EntidadeCertificadora ec = new EntidadeCertificadora();
			ec.setIdEntidadeCertificadora(Long.parseLong(entCerSelEdit));
			usuarioEdit.setEntidadeCertificadora(ec);
		}	
		if(usuarioService.alterarUsuario(usuarioEdit)){
			FacesMessage message = new FacesMessage();			
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setDetail("Alteração efetuada com sucesso!");
			FacesContext.getCurrentInstance().addMessage("editarUsuarioForm", message);
		}
		else{
			FacesMessage message = new FacesMessage();			
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setDetail("Problemas na alteração!");
			FacesContext.getCurrentInstance().addMessage("editarUsuarioForm", message);
		}		
		return null;
	}
	
	public String consultarUsuario() {
		if (expirouSessao()) {
			return SESSAO_EXPIRADA;
		}
		if(!isAdmin()){
			return SESSAO_EXPIRADA;
		}
		try {
			Map parameters = new HashMap();

			if (usuarioAdminForm.getPrimeiroNome() != null ) {
				parameters.put("primeiroNome", new String(usuarioAdminForm.getPrimeiroNome()));
			}

			if (usuarioAdminForm.getEmail() != null ) {
				parameters.put("email", new String(usuarioAdminForm.getEmail()));
			}

			if (usuarioAdminForm.getCidade() != null ) {
				parameters.put("cidade", new String(usuarioAdminForm.getCidade()));
			}

			if (usuarioAdminForm.getEstado() != null && usuarioAdminForm.getEstado() != "") {
				parameters.put("estado", new String(usuarioAdminForm.getEstado()));
			}
			
			if (entCerSel != null && !entCerSel.equalsIgnoreCase("") ) {
				
				parameters.put("entidadeCertificadora", entCerSel);
			}

			if (dataInicial != null && !dataInicial.equalsIgnoreCase("") && !dataInicial.equalsIgnoreCase("0")) {
				String pattern = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date data = sdf.parse(dataInicial);
				parameters.put("dataInicial", data);
			}

			if (dataFinal != null && !dataFinal.equalsIgnoreCase("") && !dataFinal.equalsIgnoreCase("0")) {
				String pattern = "dd/MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date data = sdf.parse(dataFinal);
				parameters.put("dataFinal", data);
			}
			parameters.put("confirmacao", new String(usuarioAdminForm.getConfirmacao()));

			usuarios=(List<BaseObject>) usuarioService.filter(parameters);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			addMessage("errors.geral", e.getMessage());
			
		}

		return "successUser";
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getEntCerSel() {
		return entCerSel;
	}

	public void setEntCerSel(String entCerSel) {
		this.entCerSel = entCerSel;
	}

	public Usuario getUsuarioEdit() {
		
		return usuarioEdit;
	}

	public void setUsuarioEdit(Usuario usuarioEdit) {
		this.usuarioEdit = usuarioEdit;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public String getEntCerSelEdit() {
		if(usuarioEdit.getEntidadeCertificadora() == null){
			entCerSelEdit = "-1";
		}
		else{
			entCerSelEdit = usuarioEdit.getEntidadeCertificadora().getIdEntidadeCertificadora().toString();
		}
		return entCerSelEdit;
	}

	public void setEntCerSelEdit(String entCerSelEdit) {
		
		this.entCerSelEdit = entCerSelEdit;
	}
	
	
}