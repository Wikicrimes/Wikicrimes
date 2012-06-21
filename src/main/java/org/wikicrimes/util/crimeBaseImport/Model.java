package org.wikicrimes.util.crimeBaseImport;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.wikicrimes.util.Cripto;

public class Model {

//	public Long CRI_IDCRIME;				//gerado
	public Long TLO_IDTIPO_LOCAL = 0L; 
	public Long TCR_IDTIPO_CRIME;			//obrigatorio
	public Long TPA_IDTIPO_PAPEL = 0L; 
	public Long TTR_IDTIPO_TRANSPORTE = 0L;
	public Integer TAU_IDTIPO_ARMA_USADA = 0; 
	public Long TRE_IDTIPO_REGISTRO = 0L; 
//	public Long USU_IDUSUARIO;				//fixo 
	public Timestamp CRI_DATA;				//obrigatorio
	public Integer CRI_QUANTIDADE = 0;
	public Integer CRI_FAIXA_ETARIA = 0;
	public Integer CRI_QTD_MASCULINO = 0;
	public Integer CRI_QTD_FEMININO = 0; 
	public Boolean CRI_SEXO = false;
	public String CRI_DESCRICAO;			//obrigatorio 
	public Double CRI_LATITUDE;				//obrigatorio
	public Double CRI_LONGITUDE;			//obrigatorio
//	public Long CRI_STATUS;					//fixo
	public Long CRI_HORARIO;	 			//derivado (data)
//	public Long CRI_CONFIRMACOES_POSITIVAS;	//fixo
//	public Long CRI_CONFIRMACOES_NEGATIVAS;	//fixo
//	public String CRI_IP;					//fixo
	public Timestamp CRI_DATA_HORA_REGISTRO; 
	public Long TVI_IDTIPO_VITIMA = 0L;
	public String CRI_ENDERECO;				//obrigatorio
	public String CRI_CIDADE;				//obrigatorio
	public String CRI_ESTADO;				//obrigatorio
	public String CRI_PAIS;					//obrigatorio
//	public String CRI_CHAVE;				//gerado
//	public Long CRI_VIEW;					//fixo
	public String CRI_CEP;
	public Long USRS_IDUSUARIO_REDE_SOCIAL = 0L;
	public String CRI_EMBED_NOTICIA;
	public String CRI_LINK_NOTICIA; 
//	public Long CRI_QTD_COMENTARIOS;		//fixo 
	public String CRI_REGISTRADO_PELA_API;
//	public Double CRE_CREDIBILIDADE;		//fixo
	public String CRI_ID_ORIGINAL;
	public Long CRI_ID_BASE_ORIGEM = 0L;
	public String CRI_CACHE_ESTATISTICAS;	//derivado (tipo_crime e tipo_vitima)
	
	//valores fixos
	public static final Long FIXED_USUARIO = 9210L; //fonteexterna@wikicrimes.org
	public static final Long FIXED_STATUS = 0L;
	public static final Long FIXED_CONF_POSITIVAS = 1L;
	public static final Long FIXED_CONF_NEGATIVAS = 0L;
	public static final String FIXED_IP = "200.19.188.105"; //ip do LEC
	public static final Long FIXED_VIEW = 0L;
	public static final Long FIXED_COMENTARIOS = 0L;
	public static final Double FIXED_CREDIBILIDADE = 1D; //credibilidade maxima
	public static final String FIXED_MSG_CONFIRMACAO = "Registro automáxtico de crimes";
	public static final Integer FIXED_RAZAO = 20; //"nao sei"
	
	//valores default
	public static final Long DEFAULT_TIPO_CRIME = 5L; //"violencia"
	public static final Long DEFAULT_LOCAL = 6L; //"pessoa.outros"
	public static final Long DEFAULT_PAPEL = 3L; //"nenhuma"
	public static final Integer DEFAULT_ARMA = 5; //"nao sei"
	public static final Long DEFAULT_TIPO_REGISTRO = 4L; //"nao sei"
	public static final Integer DEFAULT_QUANTIDADE = 1;
	public static final Integer DEFAULT_QTD_MASC = 1;
	public static final Long DEFAULT_VITIMA = 10L; //"nao informado"
	
	public Model() {
		setDefaultValues();
	}

	private void setDefaultValues(){
		TCR_IDTIPO_CRIME = DEFAULT_TIPO_CRIME; 
		TLO_IDTIPO_LOCAL = DEFAULT_LOCAL; //TODO: tentar inferir se n é propriedade.outros = 15
		TPA_IDTIPO_PAPEL = DEFAULT_PAPEL; 
		TAU_IDTIPO_ARMA_USADA = DEFAULT_ARMA; 
		TRE_IDTIPO_REGISTRO = DEFAULT_TIPO_REGISTRO;
		CRI_QUANTIDADE = DEFAULT_QUANTIDADE;
		CRI_QTD_MASCULINO = DEFAULT_QTD_MASC;
		CRI_DATA_HORA_REGISTRO = new Timestamp(new Date().getTime());
		TVI_IDTIPO_VITIMA = DEFAULT_VITIMA;
	}
	
	public boolean checkRequiredFields(){
		return TCR_IDTIPO_CRIME != null && CRI_DATA != null && CRI_DESCRICAO != null && CRI_LATITUDE != null && CRI_LONGITUDE != null
				&& CRI_ENDERECO != null && CRI_CIDADE != null && CRI_PAIS != null && CRI_ID_ORIGINAL != null 
				&& CRI_ID_BASE_ORIGEM != null;
	}
	
	public void setDerivedFields(){
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(new Date(CRI_DATA.getTime()));
		CRI_HORARIO = (long)c.get(Calendar.HOUR_OF_DAY);
		
		CRI_CACHE_ESTATISTICAS = TCR_IDTIPO_CRIME + "|" + TVI_IDTIPO_VITIMA;
	}
	
	public String generateCRI_CHAVE(int id){
		return Cripto.criptografar(CRI_DATA_HORA_REGISTRO.toString() + id);
	}
	
}
