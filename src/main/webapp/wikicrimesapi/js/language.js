/* <![CDATA[ */

function carregarIdioma(idioma){
	if( idioma == 'pt' ){
		
		mensagens['campo.requerido'] = 'campo requerido'
		mensagens['campo.requerido.razao'] = 'você deve marcar pelo menos 1 razão e no máximo 4';
		mensagens['data.invalida'] = 'Data inválida!';	
		mensagens['titulo.kernel.map'] = 'Zonas Perigosas';
		mensagens['limpar.crimes'] = 'Limpar Crimes';
		mensagens['mostrar.crimes'] = 'Mostrar Crimes';
		mensagens['meus.registros'] = 'Meus Registros';
		
		mensagens['move.icone.local.crime'] = 'Mova o ícone ao local do Crime';
		mensagens['crime.registrado.sucesso'] = 'Crime registrado com sucesso!'
		mensagens['registro.crimes.pergunta.tipo.crime'] = 'Que tipo de crime deseja registrar?'
		
		mensagens['titulo.registrar.crime'] = 'Registro de Crime';
		mensagens['tipo.local'] = 'Tipo de Local';
		mensagens['via.publica'] = 'Via Pública';
		mensagens['transporte.coletivo'] = 'Transporte Coletivo';
		mensagens['estabelecimento.comercial'] = 'Estabelecimento Comercial';
		mensagens['residencia'] = 'Residência';
		mensagens['escolas'] = 'Escola';
		mensagens['banco'] = 'Banco';
		mensagens['farmacia'] = 'Farmácia';
		mensagens['posto.gasolina'] = 'Posto de Gasolina';
		mensagens['loterica'] = 'Lotérica';
		mensagens['veiculo'] = 'Veículo';
		mensagens['shopping'] = 'Shopping';
		mensagens['praca.publica'] = 'Praça Pública';
		mensagens['data'] = 'Data';
		mensagens['qtd.vitimas'] = 'Qtd. de Vítimas';
		mensagens['arma.usada'] = 'Arma usada';
		mensagens['branca'] = 'Branca';
		mensagens['fogo'] = 'Fogo';
		mensagens['relacao.crime'] = 'Qual a sua relação com o crime?';
		mensagens['vitima'] = 'Vítima';
		mensagens['testemunha'] = 'Testemunha';
		mensagens['tive.conhecimento'] = 'Tive Conhecimento';
		mensagens['pol.info'] = 'A polícia foi informada?';
		mensagens['sim.190'] = 'Sim (190)';
		mensagens['sim.delegacia'] = 'Sim (Delegacia)';
		mensagens['nao'] = 'Não';
		mensagens['descricao'] = 'Descreva como o fato ocorreu';
		mensagens['horario'] = 'Hora';
		mensagens['qtd.criminosos'] = 'Qtd. Criminosos';
		
		mensagens['causa.motivo.ocorrencia1'] = 'Diga-nos o que você considera como causa/motivo desta ocorrência.';
		mensagens['causa.motivo.ocorrencia2'] = 'Marque no minimo 1 e no máximo 4 opções';
		mensagens['ma.iluminacao.publica'] = 'Má iluminação pública';
		mensagens['desemprego.regiao'] = 'Desemprego na região';
		mensagens['disputa.gangues'] = 'Disputa de gangues';
		mensagens['uso.alcool'] = 'Uso de alcool';
		mensagens['alta.concentracao.pessoas'] = 'Alta concentração de pessoas';
		mensagens['omissao.testemunhas'] = 'Omissão das testemunhas';
		mensagens['impunidade.penal'] = 'Impunidade penal';
		mensagens['violencia.policial'] = 'Violência policial';
		mensagens['crime.organizado'] = 'Crime organizado';
		mensagens['ausencia.lazer.jovens'] = 'Ausência de lazer para os jovens';
		mensagens['facil.acesso.fulga'] = 'Fácil acesso/fuga do criminoso';
		mensagens['trafico.drogas'] = 'Uso/Tráfico de drogas';
		mensagens['criancas.nas.ruas'] = 'Crianças/Adolescentes nas ruas';
		mensagens['falta.policiamento'] = 'Falta de policiamento';
		mensagens['prox.regioes.perig'] = 'Proximidade de regioes perigosas';
		mensagens['pistolagem'] = 'Pistolagem ';
		mensagens['falta.moradia'] = 'Falta de moradia';
		mensagens['crime.passional'] = 'Crime passional';
		mensagens['nao.sei'] = 'Não sei';
		
		mensagens['registrar'] = 'registrar';
		
		
		mensagens['legenda'] = 'Legenda';
		mensagens['agrupador'] = 'Agrupador';
		mensagens['agrupados'] = 'agrupados';
		mensagens['roubo'] = 'Roubo';
		mensagens['furto'] = 'Furto';
		mensagens['outros'] = 'Outros';
		mensagens['denuncia'] = 'Denuncia';
		mensagens['filtrarCrimes'] = 'Filtrar Crimes';
		mensagens['registrarCrime'] = 'Registrar Crime';
		mensagens['filtros'] = 'Filtros';
		mensagens['registro.de.crimes'] = 'Registro de Crimes';
		mensagens['selecione'] = 'selecione...';
		mensagens['periodo'] = 'Período';
		mensagens['de'] = 'de';
		mensagens['ate'] = 'até';
		mensagens['filtrar'] = 'filtrar';
		mensagens['marcar.no.mapa'] = 'marcar no mapa';
		mensagens['cancelar'] = 'cancelar';
		mensagens['todos.crimes'] = 'todos os crimes';
		mensagens['todos.tipos'] = 'todos os tipos';
		
		//tipos de crime
		mensagens['tipo.crime.roubo'] = 'Roubo';
		mensagens['tipo.crime.furto'] = 'Furto';
		mensagens['tipo.crime.outros'] = 'Outros';
		mensagens['tipo.crime.tentativa.roubo'] = 'Tentativa de Roubo';
		mensagens['tipo.crime.tentativa.furto'] = 'Tentativa de Furto';
		
		//tipos de vitimas
		mensagens['tipo.vitima.pessoa'] = 'a pessoa';
		mensagens['tipo.vitima.propriedade'] = 'a propriedade';
		mensagens['tipo.vitima.rixas.ou.brigas'] = 'Rixas ou brigas';
		mensagens['tipo.vitima.violencia.domestica'] = 'Violência doméstica';
		mensagens['tipo.vitima.abuso.de.autoridade'] = 'Abuso de autoridade';
		mensagens['tipo.vitima.homicidio'] = 'Homicídio';
		mensagens['tipo.vitima.tentativa.homicidio'] = 'Tentativa de homicídio';
		mensagens['tipo.vitima.latrocinio'] = 'Latrocínio';
	}else{
		mensagens['legenda'] = 'Legend';
		mensagens['agrupador'] = 'Agrupador';
		mensagens['agrupados'] = 'agrupados';
		mensagens['roubo'] = 'Robbery';
		mensagens['furto'] = 'Theft';
		mensagens['outros'] = 'Other';
		mensagens['denuncia'] = 'Denounce';
		mensagens['filtrarCrimes'] = 'Filter Crimes';
		mensagens['registrarCrime'] = 'Register Crime';
		mensagens['filtros'] = 'Filters';		
		mensagens['periodo'] = 'Período';
		mensagens['de'] = 'de';
		mensagens['ate'] = 'até';
		mensagens['filtrar'] = 'filter';
		mensagens['marcar.no.mapa'] = 'marcar no mapa';
		mensagens['registro.de.crimes'] = 'Registro de Crimes';
		mensagens['selecione'] = 'select...';
		mensagens['cancelar'] = 'cancel';
		mensagens['todos.crimes'] = 'all crimes';
		mensagens['todos.tipos'] = 'all types';
		
		//tipos de crime
		mensagens['tipo.crime.roubo'] = 'Robbery';
		mensagens['tipo.crime.furto'] = 'Theft';
		mensagens['tipo.crime.outros'] = 'Others';
		mensagens['tipo.crime.tentativa.roubo'] = 'Tentativa de Roubo';
		mensagens['tipo.crime.tentativa.furto'] = 'Tentativa de Furto';
		
		//tipos de vitimas
		mensagens['tipo.vitima.pessoa'] = 'a pessoa';
		mensagens['tipo.vitima.propriedade'] = 'a propriedade';
		mensagens['tipo.vitima.rixas.ou.brigas'] = 'Rixas ou brigas';
		mensagens['tipo.vitima.violencia.domestica'] = 'Violência doméstica';
		mensagens['tipo.vitima.abuso.de.autoridade'] = 'Abuso de autoridade';
		mensagens['tipo.vitima.homicidio'] = 'Homicídio';
		mensagens['tipo.vitima.tentativa.homicidio'] = 'Tentativa de homicídio';
		mensagens['tipo.vitima.latrocinio'] = 'Latrocínio';
	}
}	
/* ]]> */