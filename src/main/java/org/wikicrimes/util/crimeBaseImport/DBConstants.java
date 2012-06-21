package org.wikicrimes.util.crimeBaseImport;

public class DBConstants{

	public static class TipoCrime{
		public static final Long TENTATIVA_DE_ROUBO = 1L; 
		public static final Long TENTATIVA_DE_FURTO = 2L; 
		public static final Long FURTO = 3L; 
		public static final Long ROUBO = 4L; 
		public static final Long VIOLENCIA = 5L; 
	}
	
	public static class SubtipoCrime{
		public static final Long PESSOA = 1L; 
		public static final Long PROPRIEDADE = 2L; 
		public static final Long RIXAS_OU_BRIGAS = 3L; 
		public static final Long VIOLENCIA_DOMESTICA = 4L; 
		public static final Long ABUSO_DE_AUTORIDADE = 5L;
		public static final Long HOMICIDIO = 6L;
		public static final Long TENTATIVA_DE_HOMICIDIO = 7L;
		public static final Long LATROCINIO = 8L;
		public static final Long ATENTADO_AO_PUDOR = 9L;
		public static final Long NAO_INFORMADO = 10L;
	}

	public static class LocalCrime{
		public static final Long PESSOA_VIA_PUBLICA = 1L;
		public static final Long PESSOA_TRANSPORTE_COLETIVO = 2L;
		public static final Long PESSOA_ESTABELECIMENTO_COMERCIAL = 3L;
		public static final Long PESSOA_RESIDENCIA = 4L;
		public static final Long PESSOA_ESCOLA = 5L;
		public static final Long PESSOA_OUTROS = 6L;
		public static final Long PROPRIEDADE_RESIDENCIA = 7L;
		public static final Long PROPRIEDADE_BANCO = 8L;
		public static final Long PROPRIEDADE_FARMACIA = 9L;
		public static final Long PROPRIEDADE_POSTO_GASOLINA = 10L;
		public static final Long PROPRIEDADE_LOTERICA = 11L;
		public static final Long PROPRIEDADE_VEICULO = 12L;
		public static final Long PROPRIEDADE_SHOPPING = 13L;
		public static final Long PESSOA_PRACA_PUBLICA= 14L;
		public static final Long PROPRIEDADE_OUTROS = 15L;
		public static final Long PROPRIEDADE_ESTABELECIMENTO_COMERCIAL = 16L;
		public static final Long PROPRIEDADE_CARGA = 17L;
	}
	
}
