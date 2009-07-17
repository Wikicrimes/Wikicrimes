package org.wikicrimes.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.wikicrimes.business.CrimeBusiness;
import org.wikicrimes.business.ECBusiness;
import org.wikicrimes.business.LogBusiness;
import org.wikicrimes.business.UsuarioBusiness;
import org.wikicrimes.business.UsuarioRedeSocialBusiness;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.TipoLog;
import org.wikicrimes.model.Usuario;


public class UsuarioBean {


	private String entCerEditSel;

	private String perfilSel;

	private UsuarioBusiness usuarioBusiness;

	private UsuarioRedeSocialBusiness usuarioRedeSocialBusiness;

	private Usuario usuario= new Usuario();

	private Integer qtdUsuarios;

	private Usuario usuarioLogado;

	private Crime crimeConf;

	private Usuario usuarioEditar;

	private Usuario usuarioTemp = new Usuario();

	private Integer qtdTotalUsuarios;

	private Integer qtdUsuariosConf;

	private Integer qtdUsuariosNConf;

	private Integer qtdUsuariosConv;

	private Integer qtdUsuariosAdm;
	
	private Integer qtdUsuariosRedeSocial;

	private Integer qtdUsuariosArea;

	private String ecSel;

	private CrimeBusiness crimeBusiness;

	private LogBusiness logBusiness;

	private List<BaseObject> usuarios;

	private Date dataInicial;

	private Date dataFinal;
	
	private String operadores;
	

	private ECBusiness entidadeCertificadoraBusiness;

	public UsuarioBean(){
		usuario= new Usuario();
	}

	public void gerarCSV(){
		try {
			HttpServletResponse resp = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			resp.setContentType("application/csv");   
			resp.setHeader("Content-Disposition", "inline; filename=Usuarios.csv" );  
			PrintWriter out = resp.getWriter();
			out.println("\"NAME\",\"LASTNAME\",\"EMAIL\"");
			for(BaseObject user : usuarios)
				out.println("\""+((Usuario) user).getPrimeiroNome()+"\",\""+((Usuario) user).getUltimoNome()+"\",\""+((Usuario) user).getEmail()+"\"");
				
			
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String autenticar() {
		usuarios = usuarioBusiness.autenticar(usuario);
		if(usuarios.size()!=0)
			usuario= (Usuario)usuarios.get(0);
		else {
			return null;
		} 

		usuarioLogado = usuario;
		usuario = new Usuario();
		return "index";


	}
	
	public String setUserLog(){
		this.zerar();
		usuarios = new ArrayList();
		usuarios.add(usuarioEditar);
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String editarUser(){
		List<BaseObject> users = usuarioBusiness.getUserById(usuarioEditar.getIdUsuario().toString());
		usuarioEditar = (Usuario)users.get(0);
		usuarioTemp.setPrimeiroNome(usuarioEditar.getPrimeiroNome());
		usuarioTemp.setEmail(usuarioEditar.getEmail());
		usuarioTemp.setCidade(usuarioEditar.getCidade());
		usuarioTemp.setEstado(usuarioEditar.getEstado());
		usuarioTemp.setIdiomaPreferencial(usuarioEditar.getIdiomaPreferencial());
		usuarioTemp.setPerfil(usuarioEditar.getPerfil());
		usuarioTemp.setEntidadeCertificadora(usuarioEditar.getEntidadeCertificadora());

		return "usuariosEditar";
	}

	public String zerar(){
		usuarios = null;
		qtdUsuarios = -1;
		dataInicial = null;
		dataFinal = null;
		usuario= new Usuario();
		qtdTotalUsuarios = usuarioBusiness.countTotalUsuarios();
		qtdUsuariosConf = usuarioBusiness.countUsuariosConf();
		qtdUsuariosNConf = usuarioBusiness.countUsuariosNConf();
		qtdUsuariosConv = usuarioBusiness.countUsuariosConv();
		qtdUsuariosAdm = usuarioBusiness.countUsuariosAdmin();
		qtdUsuariosArea = usuarioBusiness.countUsuariosArea();
		qtdUsuariosRedeSocial = usuarioRedeSocialBusiness.countUsuariosRedeSocial();
		return "usuarios";
	}

	public String getTotalUsuarios(){
		usuarios = usuarioBusiness.getTotalUsuarios();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String getUsuariosConf(){
		usuarios = usuarioBusiness.getUsuariosConf();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String getUsuariosNConf(){
		usuarios = usuarioBusiness.getUsuariosNConf();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String getUsuariosConv(){
		usuarios = usuarioBusiness.getUsuariosConv();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String getUsuariosAdm(){
		usuarios = usuarioBusiness.getUsuariosAdm();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}

	public String getUsuariosArea(){
		usuarios = usuarioBusiness.getUsuariosArea();
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}


	public String salvar(){
		int indiceUsuarioAntigo = usuarios.indexOf(usuarioEditar);
		if(entCerEditSel == null)
			entCerEditSel = "";
		if( entCerEditSel!=null && !entCerEditSel.equalsIgnoreCase("") && perfilSel.equalsIgnoreCase("4")){
			EntidadeCertificadora entCerEdit = new EntidadeCertificadora();
			entCerEdit.setIdEntidadeCertificadora(Long.parseLong(entCerEditSel));
			usuarioEditar.setEntidadeCertificadora(entCerEdit);
		}
		else {
			usuarioEditar.setEntidadeCertificadora(null);
		}

		if(perfilSel!=null && !perfilSel.equalsIgnoreCase("")){
			Perfil perf = new Perfil();
			perf.setIdPerfil(Long.parseLong(perfilSel));
			usuarioEditar.setPerfil(perf);
		}
		entCerEditSel= null;

		// Criando Log

		if(!usuarioEditar.getPrimeiroNome().equals(usuarioTemp.getPrimeiroNome())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Primeiro Nome");
			log.setCampoAntigo(usuarioTemp.getPrimeiroNome().toString());
			log.setCampoNovo(usuarioEditar.getPrimeiroNome().toString());
			logBusiness.inserir(log);
		}
		if(!usuarioEditar.getEmail().equals(usuarioTemp.getEmail())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Email");
			log.setCampoAntigo(usuarioTemp.getEmail());
			log.setCampoNovo(usuarioEditar.getEmail());
			logBusiness.inserir(log);
		}
		if(!usuarioEditar.getCidade().equals(usuarioTemp.getCidade())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Cidade");
			log.setCampoAntigo(usuarioTemp.getCidade());
			log.setCampoNovo(usuarioEditar.getCidade());
			logBusiness.inserir(log);
		}
		if(usuarioEditar.getEstado() != null )
			if (!usuarioEditar.getEstado().equals(usuarioTemp.getEstado())){
				Log log = new Log();
				log.setUsuario(usuarioLogado);
				log.setData(new Date());
				TipoLog temp = new TipoLog();
				temp.setIdTipoLog(Long.parseLong(""+2));
				log.setTipoLog(temp);
				log.setIdObj(usuarioEditar.getIdUsuario());
				log.setCampo("Estado");
				log.setCampoAntigo(usuarioTemp.getEstado());
				log.setCampoNovo(usuarioEditar.getEstado());
				logBusiness.inserir(log);
			}
		if(!usuarioEditar.getIdiomaPreferencial().equals(usuarioTemp.getIdiomaPreferencial())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Idioma");
			log.setCampoAntigo(usuarioTemp.getIdiomaPreferencial());
			log.setCampoNovo(usuarioEditar.getIdiomaPreferencial());
			logBusiness.inserir(log);
		}
		if(!usuarioEditar.getPerfil().getIdPerfil().equals(usuarioTemp.getPerfil().getIdPerfil())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Tipo Usuário");
			log.setCampoAntigo(usuarioTemp.getPerfil().getIdPerfil().toString());
			log.setCampoNovo(usuarioEditar.getPerfil().getIdPerfil().toString());
			logBusiness.inserir(log);
		}
		if(usuarioEditar.getEntidadeCertificadora() != null && usuarioTemp.getEntidadeCertificadora()!= null)
		if(!usuarioEditar.getEntidadeCertificadora().getIdEntidadeCertificadora()
				.equals(usuarioTemp.getEntidadeCertificadora().getIdEntidadeCertificadora())){
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog temp = new TipoLog();
			temp.setIdTipoLog(Long.parseLong(""+2));
			log.setTipoLog(temp);
			log.setIdObj(usuarioEditar.getIdUsuario());
			log.setCampo("Entidade Certificadora");
			log.setCampoAntigo(usuarioTemp.getEntidadeCertificadora().getIdEntidadeCertificadora().toString());
			log.setCampoNovo(usuarioEditar.getEntidadeCertificadora().getIdEntidadeCertificadora().toString());
			logBusiness.inserir(log);
		}


		usuarioBusiness.update(usuarioEditar);
		List<BaseObject> users = usuarioBusiness.getUserById(usuarioEditar.getIdUsuario().toString());
		Usuario temp = (Usuario)users.get(0);
		usuarioEditar.setEntidadeCertificadora(temp.getEntidadeCertificadora());
		usuarios.remove(indiceUsuarioAntigo);
		usuarios.add(indiceUsuarioAntigo, usuarioEditar);
		return "usuarios";
	}


	public String getUsuariosConfCrime(){
		this.zerar();
		usuarios = new ArrayList();
		List<BaseObject> crims = crimeBusiness.getCrimeById(crimeConf.getIdCrime().toString());
		crimeConf = (Crime)crims.get(0);

		Set<Confirmacao> conf= crimeConf.getConfirmacoes();
		for (Iterator iterator = conf.iterator(); iterator.hasNext();) {
			Confirmacao confirmacao = (Confirmacao) iterator.next();			
			usuarios.add(confirmacao.getUsuario());			
		}
		qtdUsuarios = usuarios.size();
		return "usuarios";
	}
	
	
	public String consultarUsuario() {

		try {
			Map parameters = new HashMap();

			if (usuario.getPrimeiroNome() != null ) {
				parameters.put("primeiroNome", new String(usuario.getPrimeiroNome()));
			}

			if (usuario.getEmail() != null ) {
				parameters.put("email", new String(usuario.getEmail()));
			}

			if (usuario.getCidade() != null ) {
				parameters.put("cidade", new String(usuario.getCidade()));
			}

			if (usuario.getEstado() != null ) {
				parameters.put("estado", new String(usuario.getEstado()));
			}

			if (usuario.getIdiomaPreferencial() != null && usuario.getIdiomaPreferencial() != "-1") {
				parameters.put("idioma", new String(usuario.getIdiomaPreferencial()));
			}

			if (ecSel != null ) {
				parameters.put("entidadeCertificadora", ecSel);
			}

			if (dataInicial != null) {

				parameters.put("dataInicial", dataInicial);
			}

			if (dataFinal != null) {

				parameters.put("dataFinal", dataFinal);
			}
			if (usuario.getQtdCrimes() != null ) {
				parameters.put("qtdCrimes", usuario.getQtdCrimes());
			}
			if (operadores != null ) {
				parameters.put("operadores", operadores);
			}
			if (usuario.getConfirmacao() != null ) {
				parameters.put("confirmacao", new String(usuario.getConfirmacao()));
			}
			
			usuarios = (List<BaseObject>)usuarioBusiness.filter(parameters);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();


		}
		qtdUsuarios = usuarios.size();
		return null;
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

		List<BaseObject> entidadeCertificadoraAll = entidadeCertificadoraBusiness.getEntidadeCertificadoraAll();
		for (int i = 0; i < entidadeCertificadoraAll.size(); i++) {
			EntidadeCertificadora entidadeCertificadora = (EntidadeCertificadora) entidadeCertificadoraAll
			.get(i);
			entidadeCertificadoraItens.add(new SelectItem(
					entidadeCertificadora.getIdEntidadeCertificadora()+"",
					entidadeCertificadora.getNome()));

		}
		return entidadeCertificadoraItens;
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

	public Usuario getUsuarioEditar() {
		return usuarioEditar;
	}

	public void setUsuarioEditar(Usuario usuarioEditar) {
		this.usuarioEditar = usuarioEditar;
	}

	public String getOperadores() {
		return operadores;
	}
	
	public void setOperadores(String operadores) {
		this.operadores = operadores;
	}

	public List<BaseObject> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<BaseObject> usuarios) {
		this.usuarios = usuarios;
	}
	public UsuarioBusiness getUsuarioBusiness() {
		return usuarioBusiness;
	}

	public void setUsuarioBusiness(UsuarioBusiness usuarioBusiness) {
		this.usuarioBusiness = usuarioBusiness;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}


	public ECBusiness getEntidadeCertificadoraBusiness() {
		return entidadeCertificadoraBusiness;
	}

	public void setEntidadeCertificadoraBusiness(
			ECBusiness entidadeCertificadoraBusiness) {
		this.entidadeCertificadoraBusiness = entidadeCertificadoraBusiness;
	}

	public Integer getQtdUsuarios() {
		return qtdUsuarios;
	}

	public void setQtdUsuarios(Integer qtdUsuarios) {
		this.qtdUsuarios = qtdUsuarios;
	}

	public String getEcSel() {
		return ecSel;
	}

	public void setEcSel(String ecSel) {
		this.ecSel = ecSel;
	}


	public String getEntCerEditSel() {
		String retorno;
		if (usuarioEditar.getEntidadeCertificadora()== null)
			retorno = "";
		else
			retorno = usuarioEditar.getEntidadeCertificadora().getIdEntidadeCertificadora().toString();

		return retorno;
	}


	public void setEntCerEditSel(String entCerEditSel) {
		this.entCerEditSel = entCerEditSel;
	}


	public Crime getCrimeConf() {
		return crimeConf;
	}


	public void setCrimeConf(Crime crimeConf) {
		this.crimeConf = crimeConf;
	}


	public CrimeBusiness getCrimeBusiness() {
		return crimeBusiness;
	}


	public void setCrimeBusiness(CrimeBusiness crimeBusiness) {
		this.crimeBusiness = crimeBusiness;
	}


	public Integer getQtdTotalUsuarios() {
		return qtdTotalUsuarios;
	}


	public void setQtdTotalUsuarios(Integer qtdTotalUsuarios) {
		this.qtdTotalUsuarios = qtdTotalUsuarios;
	}


	public Integer getQtdUsuariosConf() {
		return qtdUsuariosConf;
	}


	public void setQtdUsuariosConf(Integer qtdUsuariosConf) {
		this.qtdUsuariosConf = qtdUsuariosConf;
	}


	public Integer getQtdUsuariosNConf() {
		return qtdUsuariosNConf;
	}


	public void setQtdUsuariosNConf(Integer qtdUsuariosNConf) {
		this.qtdUsuariosNConf = qtdUsuariosNConf;
	}


	public Integer getQtdUsuariosConv() {
		return qtdUsuariosConv;
	}


	public void setQtdUsuariosConv(Integer qtdUsuariosConv) {
		this.qtdUsuariosConv = qtdUsuariosConv;
	}


	public Integer getQtdUsuariosAdm() {
		return qtdUsuariosAdm;
	}


	public void setQtdUsuariosAdm(Integer qtdUsuariosAdm) {
		this.qtdUsuariosAdm = qtdUsuariosAdm;
	}

	public String getPerfilSel() {
		String retorno;
		if (usuarioEditar.getPerfil()== null)
			retorno = "";
		else
			retorno = usuarioEditar.getPerfil().getIdPerfil().toString();

		return retorno;
	}

	public void setPerfilSel(String perfilSel) {
		this.perfilSel = perfilSel;
	}

	public Integer getQtdUsuariosArea() {
		return qtdUsuariosArea;
	}

	public void setQtdUsuariosArea(Integer qtdUsuariosArea) {
		this.qtdUsuariosArea = qtdUsuariosArea;
	}

	public Usuario getUsuarioTemp() {
		return usuarioTemp;
	}

	public void setUsuarioTemp(Usuario usuarioTemp) {
		this.usuarioTemp = usuarioTemp;
	}

	public LogBusiness getLogBusiness() {
		return logBusiness;
	}

	public void setLogBusiness(LogBusiness logBusiness) {
		this.logBusiness = logBusiness;
	}

	public Integer getQtdUsuariosRedeSocial() {
		return qtdUsuariosRedeSocial;
	}

	public void setQtdUsuariosRedeSocial(Integer qtdUsuariosRedeSocial) {
		this.qtdUsuariosRedeSocial = qtdUsuariosRedeSocial;
	}

	public UsuarioRedeSocialBusiness getUsuarioRedeSocialBusiness() {
		return usuarioRedeSocialBusiness;
	}

	public void setUsuarioRedeSocialBusiness(
			UsuarioRedeSocialBusiness usuarioRedeSocialBusiness) {
		this.usuarioRedeSocialBusiness = usuarioRedeSocialBusiness;
	}

}
