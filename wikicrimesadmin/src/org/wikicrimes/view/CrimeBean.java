package org.wikicrimes.view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.wikicrimes.business.CrimeBusiness;
import org.wikicrimes.business.LogBusiness;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.CrimeRazao;
import org.wikicrimes.model.Log;
import org.wikicrimes.model.Razao;
import org.wikicrimes.model.TipoArmaUsada;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLog;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.util.Horario;

public class CrimeBean {

	private Crime crime;

	private Crime crimeTemp = new Crime();

	private List<BaseObject> crimes;
	
	private List<SelectItem> razoes;
	
	private List<String> razoesSel;
	
	private Set<CrimeRazao> razoesCrimes;

	private Integer qtdCrimes;

	private Boolean opened;

	private Crime crimeEditar;

	private Usuario usuarioLogado;

	private Integer qtdTotalCrimes;

	private Long horarioFinal;

	private Integer qtdCrimesConf;

	private Integer qtdCrimesAtivos;

	private Integer qtdCrimesInativos;

	private Integer qtdCrimesConfP;

	private Integer qtdCrimesConfN;

	private Integer qtdCrimesNConf;

	private Usuario usuarioRegistro;

	private CrimeBusiness crimeBusiness;

	private LogBusiness logBusiness;

	private Date dataInicial;

	private Date dataFinal;

	private String stringCrimes;

	private ArrayList<Crime> crimesSemEnderecoEncontrado;

	/*
	 * Trata uma string recebida com o(s) crime(s) e guarda os novos endereços
	 * no banco. Caso haja crimes sem endereços encontrados redireciona para uma
	 * nova página
	 */
	public String atualizarEnderecos() {
		Crime crimeAux;
		boolean alterado;
		crimesSemEnderecoEncontrado = new ArrayList<Crime>();
		String[] stringsCrimes = stringCrimes.split(";");

		for (int i = 0; i < stringsCrimes.length; i = i + 6) {
			int id = Integer.parseInt(stringsCrimes[i]);
			crimeAux = (Crime) crimeBusiness.getCrimeById("" + id).get(0);
			alterado = false;

			if (!stringsCrimes[i + 1].equalsIgnoreCase("undefined")) {
				if (!stringsCrimes[i + 1].equals("null")) {
					crimeAux.setPais(stringsCrimes[i + 1]);
					alterado = true;
				}
				if (!stringsCrimes[i + 2].equals("null")) {
					crimeAux.setEstado(stringsCrimes[i + 2]);
					alterado = true;
				}
				if (!stringsCrimes[i + 3].equals("null")) {
					crimeAux.setCidade(stringsCrimes[i + 3]);
					alterado = true;
				}
				if (!stringsCrimes[i + 4].equals("null")) {
					crimeAux.setEndereco(stringsCrimes[i + 4]);
					alterado = true;
				}
				if (!stringsCrimes[i + 5].equals("null")) {
					crimeAux.setCep(stringsCrimes[i + 5]);
					alterado = true;
				}
			}
			// Retira o crime gravado da lista dos crimes;
			if (alterado) {
				crimeBusiness.update(crimeAux);

				for (BaseObject c : crimes)
					if (((Crime) c).getIdCrime() == id) {
						crimes.remove(c);
						break;
					}
			} else {
				for (BaseObject c : crimes)
					if (((Crime) c).getIdCrime() == id) {
						crimesSemEnderecoEncontrado.add((Crime) c);
						break;
					}
			}
		}

		qtdCrimes = crimes.size();
		
		if (crimesSemEnderecoEncontrado != null
				&& crimesSemEnderecoEncontrado.size() != 0){
			crime = new Crime();
			return "crimesNaoEncontrados";
		}
		else
			return null;
	}

	/*
	 * Atualiza endereço de 1 um crime e o remove de
	 * 'crimesSemEnderecoEncontrado' e 'crimes'.
	 */
	public String atualizarEndereco() {
		Crime crimeAux = null;

		for (Crime c : crimesSemEnderecoEncontrado) {
			if (c.getIdCrime().longValue() == crime.getIdCrime().longValue()) {
				crimeAux = c;
				crimesSemEnderecoEncontrado.remove(c);
				crimes.remove(c);
				break;
			}
		}

		if (crime != null && crimeAux != null) {
			if (crime.getPais() != null && crime.getPais().length() > 0
					&& !crime.getPais().equals("null")
					&& !crime.getPais().equals("undefined"))
				crimeAux.setPais(crime.getPais());

			if (crime.getEstado() != null && crime.getEstado().length() > 0
					&& !crime.getEstado().equals("null")
					&& !crime.getEstado().equals("undefined"))
				crimeAux.setEstado(crime.getEstado());

			if (crime.getEndereco() != null && crime.getEndereco().length() > 0
					&& !crime.getEndereco().equals("null")
					&& !crime.getEndereco().equals("undefined"))
				crimeAux.setEndereco(crime.getEndereco());

			if (crime.getCidade() != null && crime.getCidade().length() > 0
					&& !crime.getCidade().equals("null")
					&& !crime.getCidade().equals("undefined"))
				crimeAux.setCidade(crime.getCidade());

			if (crime.getCep() != null && crime.getCep().length() > 0
					&& !crime.getCep().equals("null")
					&& !crime.getCep().equals("undefined"))
				crimeAux.setCep(crime.getCep());

			crimeAux.setLatitude(crime.getLatitude());
			crimeAux.setLongitude(crime.getLongitude());

			crimeBusiness.update(crimeAux);
		}

		qtdCrimes = crimes.size();
		
		if (crimesSemEnderecoEncontrado.size() != 0)
			return null;
		else
			return "enderecoCrimes";
	}

	public String irEnderecoCrimes() {
		crimes = (List<BaseObject>) crimeBusiness.getCrimeSemEndereco();
		qtdCrimes = crimes.size();

		return "enderecoCrimes";
	}

	public ArrayList<Crime> getCrimesSemEnderecoEncontrado() {
		return crimesSemEnderecoEncontrado;
	}

	public void setCrimesSemEnderecoEncontrado(ArrayList<Crime> crimesSemEnderecos) {
		this.crimesSemEnderecoEncontrado = crimesSemEnderecos;
	}

	public String getStringCrimes() {
		return stringCrimes;
	}

	public void setStringCrimes(String stringCrimes) {
		this.stringCrimes = stringCrimes;
	}

	public String atualizarMapa() {
		Crime tmp = new Crime();
		tmp.setLatitude(crimeEditar.getLatitude());
		tmp.setLongitude(crimeEditar.getLongitude());
		List<BaseObject> crimes = crimeBusiness.getCrimeById(crimeEditar
				.getIdCrime().toString());
		crimeEditar = (Crime) crimes.get(0);
		crimeEditar.setLatitude(tmp.getLatitude());
		crimeEditar.setLongitude(tmp.getLongitude());
		crimeBusiness.update(crimeEditar);
		return null;
	}

	public CrimeBean() {
		crime = new Crime();
		crime.setTipoCrime(new TipoCrime());
		crime.setTipoArmaUsada(new TipoArmaUsada());
		opened = true;
		
		razoesSel = new ArrayList<String>();
		
	}

	public String editarCrime() {
		List<BaseObject> crimes = crimeBusiness.getCrimeById(crimeEditar
				.getIdCrime().toString());
		crimeEditar = (Crime) crimes.get(0);

		TipoCrime tmp = new TipoCrime();
		tmp.setIdTipoCrime(crimeEditar.getTipoCrime().getIdTipoCrime());
		tmp.setDescricao(crimeEditar.getTipoCrime().getDescricao());
		crimeTemp.setTipoCrime(tmp);

		TipoArmaUsada tmp1 = new TipoArmaUsada();
		tmp1.setIdTipoArmaUsada(crimeEditar.getTipoArmaUsada()
				.getIdTipoArmaUsada());
		tmp1.setDescricao(crimeEditar.getTipoArmaUsada().getDescricao());
		crimeTemp.setTipoArmaUsada(tmp1);

		crimeTemp.setData(crimeEditar.getData());
		crimeTemp.setHorario(crimeEditar.getHorario());
		crimeTemp.setDescricao(crimeEditar.getDescricao());
		
		razoesCrimes = crimeEditar.getRazoes();
		
		return "crimesEditar";
	}

	public String zerar() {
		crimes = null;
		qtdCrimes = -1;
		crime = new Crime();
		crime.setTipoCrime(new TipoCrime());
		crime.setTipoArmaUsada(new TipoArmaUsada());
		dataInicial = null;
		dataFinal = null;
		opened = true;
		horarioFinal = null;
		qtdTotalCrimes = crimeBusiness.countTotalCrimes();
		qtdCrimesConf = crimeBusiness.countCrimesConf();
		qtdCrimesNConf = crimeBusiness.countCrimesNConf();
		qtdCrimesConfP = crimeBusiness.countCrimesConfP();
		qtdCrimesConfN = crimeBusiness.countCrimesConfN();
		qtdCrimesAtivos = crimeBusiness.countCrimesAtivos();
		qtdCrimesInativos = crimeBusiness.countCrimesInativos();
		
		razoesSel = new ArrayList<String>();
		razoesCrimes = null;

		return "crimes";
	}

	public String getTotalCrimes() {
		crimes = crimeBusiness.getTotalCrimes();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesConf() {
		crimes = crimeBusiness.getCrimesConf();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesAtivos() {
		crimes = crimeBusiness.getCrimesAtivos();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesInativos() {
		crimes = crimeBusiness.getCrimesInativos();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesNConf() {
		crimes = crimeBusiness.getCrimesNConf();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesConfP() {
		crimes = crimeBusiness.getCrimesConfP();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getCrimesConfN() {
		crimes = crimeBusiness.getCrimesConfN();
		qtdCrimes = crimes.size();
		return "crimes";
	}

	public String getByUser() {
		this.zerar();
		crimes = crimeBusiness.getByUser(usuarioRegistro.getIdUsuario());
		qtdCrimes = crimes.size();
		crime.setUsuario(usuarioRegistro);
		opened = false;
		usuarioRegistro = null;
		return "crimes";
	}

	public String consultarCrime() {
		try {
			Map parameters = new HashMap();
			if (crime.getUsuario() != null) {
				parameters.put("usuarioCrime", crime.getUsuario());
			}
			
			if (crime.getChave() != null
					&& !crime.getChave().equalsIgnoreCase("")) {
				parameters.put("chave", new String(crime.getChave()));
			}
			
			if (crime.getIdCrime() != null
					&& crime.getIdCrime() != 0) {
				parameters.put("idCrime", new String(crime.getIdCrime().toString()));
			}
			
			if (crime.getTipoCrime().getIdTipoCrime() != null
					&& crime.getTipoCrime().getIdTipoCrime() != -1) {
				parameters.put("idTipoCrime", new String(crime.getTipoCrime()
						.getIdTipoCrime().toString()));
			}

			if (crime.getTipoArmaUsada().getIdTipoArmaUsada() != null
					&& crime.getTipoArmaUsada().getIdTipoArmaUsada() != -1) {
				parameters.put("idTipoArma", new String(crime
						.getTipoArmaUsada().getIdTipoArmaUsada().toString()));
			}

			if (crime.getHorario() != null && crime.getHorario() != -1) {
				parameters.put("horario", new String(crime.getHorario()
						.toString()));
			}
			if (horarioFinal != null && horarioFinal != -1) {
				parameters.put("horarioFinal", new String(horarioFinal
						.toString()));
			}

			if (crime.getDescricao() != null) {
				parameters.put("descricao", new String(crime.getDescricao()));
			}

			if (crime.getConfirmacoesPositivas() != null
					&& crime.getConfirmacoesPositivas() != -1) {
				parameters.put("confirmacoes", crime.getConfirmacoesPositivas()
						.toString());
			}

			if (dataInicial != null) {

				parameters.put("dataInicial", dataInicial);
			}

			if (dataFinal != null) {

				parameters.put("dataFinal", dataFinal);
			}
			crimes = new ArrayList<BaseObject>();
			crimes = (List<BaseObject>) crimeBusiness.filter(parameters);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}
		qtdCrimes = crimes.size();
		return null;
	}

	public String salvar() {
		int indiceCrimeAntigo = crimes.indexOf(crimeEditar);
		
		Set<CrimeRazao> razoesCrimesTemp = new HashSet<CrimeRazao>();
		List<BaseObject> diversasRazoes = crimeBusiness.listarRazoes();
		
		//verifica se houve alteracao na razao do crime
		for(BaseObject baseObject:diversasRazoes){
			Razao razao = (Razao)baseObject;
			for(String s:razoesSel){
				//compara os ids para formar o objeto certo
				if(s.equals(razao.getIdRazao().toString())){
					CrimeRazao cr = new CrimeRazao();
					cr.setCrime(crimeEditar);
					cr.setRazao(razao);
					razoesCrimesTemp.add(cr);
				}
			}
		}
		
		
		crimeEditar.setRazoes(razoesCrimesTemp);

		crimeBusiness.update(crimeEditar,razoesCrimes);
		
		List<BaseObject> crims = crimeBusiness.getCrimeById(crimeEditar
				.getIdCrime().toString());
		Crime temp = (Crime) crims.get(0);
		crimeEditar.setTipoArmaUsada(temp.getTipoArmaUsada());
		crimeEditar.setTipoCrime(temp.getTipoCrime());
		crimeEditar.setTipoPapel(temp.getTipoPapel());
		
		
		// Criando Log

		if (!crimeEditar.getTipoCrime().getIdTipoCrime().equals(
				crimeTemp.getTipoCrime().getIdTipoCrime())) {
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog tmp = new TipoLog();
			tmp.setIdTipoLog(Long.parseLong("" + 1));
			log.setTipoLog(tmp);
			log.setIdObj(crimeEditar.getIdCrime());
			log.setCampo("Tipo Crime");
			log.setCampoAntigo(crimeTemp.getTipoCrime().getDescricao());
			log.setCampoNovo(crimeEditar.getTipoCrime().getDescricao());
			logBusiness.inserir(log);
		}
		if (!crimeEditar.getTipoArmaUsada().getIdTipoArmaUsada().equals(
				crimeTemp.getTipoArmaUsada().getIdTipoArmaUsada())) {
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog tmp = new TipoLog();
			tmp.setIdTipoLog(Long.parseLong("" + 1));
			log.setTipoLog(tmp);
			log.setIdObj(crimeEditar.getIdCrime());
			log.setCampo("Tipo Arma Usada");
			log.setCampoAntigo(crimeTemp.getTipoArmaUsada().getDescricao());
			log.setCampoNovo(crimeEditar.getTipoArmaUsada().getDescricao());
			logBusiness.inserir(log);
		}
		if (!crimeEditar.getData().equals(crimeTemp.getData())) {
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog tmp = new TipoLog();
			tmp.setIdTipoLog(Long.parseLong("" + 1));
			log.setTipoLog(tmp);
			log.setIdObj(crimeEditar.getIdCrime());
			log.setCampo("Data da Ocorrencia");
			Timestamp date = new Timestamp(crimeTemp.getData().getTime());
			log.setCampoAntigo(date.toString());
			date = new Timestamp(crimeEditar.getData().getTime());
			log.setCampoNovo(date.toString());
			logBusiness.inserir(log);
		}
		if (!crimeEditar.getHorario().equals(crimeTemp.getHorario())) {
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog tmp = new TipoLog();
			tmp.setIdTipoLog(Long.parseLong("" + 1));
			log.setTipoLog(tmp);
			log.setIdObj(crimeEditar.getIdCrime());
			log.setCampo("Horario");
			log.setCampoAntigo(crimeTemp.getHorario().toString());
			log.setCampoNovo(crimeEditar.getHorario().toString());
			logBusiness.inserir(log);
		}
		if (!crimeEditar.getDescricao().equals(crimeTemp.getDescricao())) {
			Log log = new Log();
			log.setUsuario(usuarioLogado);
			log.setData(new Date());
			TipoLog tmp = new TipoLog();
			tmp.setIdTipoLog(Long.parseLong("" + 1));
			log.setTipoLog(tmp);
			log.setIdObj(crimeEditar.getIdCrime());
			log.setCampo("Descricao");
			log.setCampoAntigo(crimeTemp.getDescricao());
			log.setCampoNovo(crimeEditar.getDescricao());
			logBusiness.inserir(log);
		}

		crimes.remove(indiceCrimeAntigo);
		crimes.add(indiceCrimeAntigo, crimeEditar);
		return "crimes";
	}

	public List<SelectItem> getHorarioItens() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		Iterator<Horario> horarios = Horario.iterator();
		while (horarios.hasNext()) {
			Horario horario = horarios.next();
			itens.add(new SelectItem(new Long(horario.ord()), horario
					.toString()));
		}

		return itens;
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

	public Crime getCrime() {
		return crime;
	}

	public void setCrime(Crime crime) {
		this.crime = crime;
	}

	public Crime getCrimeEditar() {
		return crimeEditar;
	}

	public void setCrimeEditar(Crime crimeEditar) {
		this.crimeEditar = crimeEditar;
	}

	public CrimeBusiness getCrimeBusiness() {
		return crimeBusiness;
	}

	public void setCrimeBusiness(CrimeBusiness crimeBusiness) {
		this.crimeBusiness = crimeBusiness;
	}

	public Integer getQtdCrimes() {
		return qtdCrimes;
	}

	public void setQtdCrimes(Integer qtdCrimes) {
		this.qtdCrimes = qtdCrimes;
	}

	public List<BaseObject> getCrimes() {
		return crimes;
	}

	public void setCrimes(List<BaseObject> crimes) {
		this.crimes = crimes;
	}

	public Usuario getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(Usuario usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public Integer getQtdTotalCrimes() {
		return qtdTotalCrimes;
	}

	public void setQtdTotalCrimes(Integer qtdTotalCrimes) {
		this.qtdTotalCrimes = qtdTotalCrimes;
	}

	public Integer getQtdCrimesConf() {
		return qtdCrimesConf;
	}

	public void setQtdCrimesConf(Integer qtdCrimesConf) {
		this.qtdCrimesConf = qtdCrimesConf;
	}

	public Integer getQtdCrimesNConf() {
		return qtdCrimesNConf;
	}

	public void setQtdCrimesNConf(Integer qtdCrimesNConf) {
		this.qtdCrimesNConf = qtdCrimesNConf;
	}

	public Integer getQtdCrimesConfP() {
		return qtdCrimesConfP;
	}

	public void setQtdCrimesConfP(Integer qtdCrimesConfP) {
		this.qtdCrimesConfP = qtdCrimesConfP;
	}

	public Integer getQtdCrimesConfN() {
		return qtdCrimesConfN;
	}

	public void setQtdCrimesConfN(Integer qtdCrimesConfN) {
		this.qtdCrimesConfN = qtdCrimesConfN;
	}

	public Long getHorarioFinal() {
		return horarioFinal;
	}

	public void setHorarioFinal(Long horarioFinal) {
		this.horarioFinal = horarioFinal;
	}

	public LogBusiness getLogBusiness() {
		return logBusiness;
	}

	public void setLogBusiness(LogBusiness logBusiness) {
		this.logBusiness = logBusiness;
	}

	public Crime getCrimeTemp() {
		return crimeTemp;
	}

	public void setCrimeTemp(Crime crimeTemp) {
		this.crimeTemp = crimeTemp;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Integer getQtdCrimesAtivos() {
		return qtdCrimesAtivos;
	}

	public void setQtdCrimesAtivos(Integer qtdCrimesAtivos) {
		this.qtdCrimesAtivos = qtdCrimesAtivos;
	}

	public Integer getQtdCrimesInativos() {
		return qtdCrimesInativos;
	}

	public void setQtdCrimesInativos(Integer qtdCrimesInativos) {
		this.qtdCrimesInativos = qtdCrimesInativos;
	}
	
	public List<SelectItem> getRazoes() {
		if(razoes==null){
			razoes = new ArrayList<SelectItem>();
			List<BaseObject> diversasRazoes = crimeBusiness.listarRazoes();
			
			//realiza a internacionalizacao
			ApplicationFactory factory = (ApplicationFactory) FactoryFinder
			.getFactory(FactoryFinder.APPLICATION_FACTORY);
			String bundleName = "SystemMessages";
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());
			
			for (Iterator<BaseObject> iterator = diversasRazoes.iterator(); iterator.hasNext();) {
				Razao r = (Razao) iterator.next();
				razoes.add(new SelectItem(r.getIdRazao().toString(),bundle.getString(r.getNome())));
			}
		}
		
		razoesSel = new ArrayList<String>();
		for(SelectItem si:razoes){
			for(CrimeRazao cr:razoesCrimes){
				//marca as razoes do banco
				if(cr.getRazao().getIdRazao().toString().equals(si.getValue().toString())){
					razoesSel.add(si.getValue().toString());
				}
			}
		}
		return razoes;
	}

	public void setRazoes(List<SelectItem> razoes) {
		this.razoes = razoes;
	}
	
	public List<String> getRazoesSel() {
		return razoesSel;
	}

	public void setRazoesSel(List<String> razoes) {
		this.razoesSel = razoes;
	}

}
