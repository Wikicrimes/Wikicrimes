package org.wikicrimes.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.EntidadeCertificadora;
import org.wikicrimes.model.ImagemMapa;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.model.TipoVitima;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.service.ImagemMapaService;


public class ImagemMapaForm extends GenericForm{
	
	private static Semaphore qrCodeViewedSemaphore = new Semaphore(1);
	
	private ImagemMapaService imagemMapaService;
	private CrimeService crimeService;
	
	private Integer id;
	private Boolean qr;
	private String numFurtos;
	private String numRoubos;
	private String numOutros;
	private String dataIni; 
	private String dataFin;
	private String nomeUsuario;
	
	public void inicializar(){
		ImagemMapa im = imagemMapaService.get(Integer.valueOf(id));
		//Se for QR Code: incrementa.
		if(qr != null && qr.booleanValue()) {
			try {
				qrCodeViewedSemaphore.acquire();
				im.setViewedQrCode(im.getViewedQrCode()+1);
				imagemMapaService.save(im);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if(qrCodeViewedSemaphore.availablePermits() == 0)
					qrCodeViewedSemaphore.release();
			}
		}
		
		Map<String, Object> param = getParams(im); 
		List<BaseObject> crimes =  crimeService.filter(param);
		
		//contagem de crimes
		int contRoubos = 0;
		int contFurtos = 0;
		for(BaseObject o : crimes){
			if(o instanceof Crime){
				Crime c = (Crime)o;
				String tipo = c.getTipoCrime().getNome(); 
				if(tipo.equals("tipocrime.roubo"))
					contRoubos++;
				else if(tipo.equals("tipocrime.furto"))
					contFurtos++;
			}
		}
		
		numRoubos = contRoubos+"";
		numFurtos = contFurtos+"";
		numOutros = (crimes.size() - contFurtos - contRoubos)+"";
		
		//datas
		DateFormat f1 = new SimpleDateFormat("dd,MM,yyyy");
		DateFormat f2 = new SimpleDateFormat("dd/MM/yy");
		String dataIniStr = im.getFiltro().get("dataInicial");
		String dataFimStr = im.getFiltro().get("dataFinal");
		if(dataIniStr == null) dataIniStr = "01,01,1970";
		if(dataFimStr == null) dataFimStr = "01,01,1970";
		try {
			dataIni = f2.format( f1.parse( dataIniStr ) );
			dataFin = f2.format( f1.parse( dataFimStr ) );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//nome do usuário
		nomeUsuario = im.getUsuario().getNome();
	}
	
	public static Map<String, Object> getParams(ImagemMapa im) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
			
		String tipoCrime = im.getFiltro().get("tipoCrime");
		if (tipoCrime != null && !tipoCrime.equals("0") && tipoCrime != "") {
			parameters.put("tipoCrime", new TipoCrime(new Long(tipoCrime)));
		}

		String tipoVitima = im.getFiltro().get("tipoVitima");
		if (tipoVitima != null && !tipoVitima.equals("0")
				&& tipoVitima != "") {
			parameters.put("tipoVitima", new TipoVitima(
					new Long(tipoVitima)));
		}

		String tipoLocal = im.getFiltro().get("tipoLocal");
		if (tipoLocal != null && !tipoLocal.equals("0") && tipoLocal != "") {
			parameters.put("tipoLocal", new TipoLocal(new Long(tipoLocal)));
		}

		String horarioInicial = im.getFiltro().get("horarioInicial");
		if (horarioInicial != null && horarioInicial != "" && !horarioInicial.equals("-1")) {
			parameters.put("horarioInicial", Long.parseLong(horarioInicial));
		}

		String horarioFinal = im.getFiltro().get("horarioFinal");
		if (horarioFinal != null && horarioFinal != "" && !horarioFinal.equals("-1")) {
			parameters.put("horarioFinal", Long.parseLong(horarioFinal));
		}

		try {
			String pattern = "dd,MM,yyyy";
			SimpleDateFormat f = new SimpleDateFormat(pattern);
			
			String dataInicial = im.getFiltro().get("dataInicial");
			if (dataInicial != null && dataInicial != "") {
				Date data;
				data = f.parse(dataInicial);
				parameters.put("dataInicial", data);
			}
	
			String dataFinal = im.getFiltro().get("dataFinal");
			if (dataFinal != null && dataFinal != "") {
				Date data = f.parse(dataFinal);
				parameters.put("dataFinal", data);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//viewport
		String norte = im.getNorth().toString();
		String sul = im.getSouth().toString();
		String leste = im.getEast().toString();
		String oeste = im.getWest().toString();
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

		String entidadeCertificadora = im.getFiltro().get("entidadeCertificadora");
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
		if (entidadeCertificadora != null && entidadeCertificadora.equals("0") && !entidadeCertificadora.equals("-1")) {
			List<BaseObject> entidadesCertificadoras = new ArrayList<BaseObject>();
			parameters
					.put("entidadeCertificadora", entidadesCertificadoras);
		}
		
		String confirmadoPositivamente = im.getFiltro().get("confirmadoPositivamente");
		if (confirmadoPositivamente != null && confirmadoPositivamente != "") {
			parameters.put("crimeConfirmadoPositivamente", new Boolean(confirmadoPositivamente));

		}

		return parameters;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumFurtos() {
		if(numFurtos == null)
			inicializar();
		return numFurtos;
	}
	public void setNumFurtos(String numFurtos) {
		this.numFurtos = numFurtos;
	}
	public String getNumRoubos() {
		if(numRoubos == null)
			inicializar();
		return numRoubos;
	}
	public void setNumRoubos(String numRoubos) {
		this.numRoubos = numRoubos;
	}
	public String getNumOutros() {
		if(numOutros == null)
			inicializar();
		return numOutros;
	}
	public void setNumOutros(String numOutros) {
		this.numOutros = numOutros;
	}
	public String getDataIni() {
		if(dataIni == null)
			inicializar();
		return dataIni;
	}
	public void setDataIni(String dataIni) {
		this.dataIni = dataIni;
	}
	public String getDataFin() {
		if(dataFin == null)
			inicializar();
		return dataFin;
	}
	public void setDataFin(String dataFin) {
		this.dataFin = dataFin;
	}
	public String getNomeUsuario() {
		if(nomeUsuario == null)
			inicializar();
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public ImagemMapaService getImagemMapaService() {
		return imagemMapaService;
	}
	public void setImagemMapaService(ImagemMapaService imagemMapaService) {
		this.imagemMapaService = imagemMapaService;
	}
	public CrimeService getCrimeService() {
		return crimeService;
	}
	public void setCrimeService(CrimeService crimeService) {
		this.crimeService = crimeService;
	}

	public Boolean getQr() {
		return qr;
	}

	public void setQr(Boolean qr) {
		this.qr = qr;
	}
	
}
