var jaMostrouAlerta = false;
var jaMostrouNotificacoes = false;
var jaMostrouAjudaPesquisa = false;
var jaMostrouInteracaoMapa = false;
var jaMostrouTipoMapa = false;
function verificarRegras(situacao){
	var numAleatorio = geraNumero(1,100);
	if(situacao['tem_notificacoes']){		
		if(situacao['qtd_move_map']>=3){			
			if(!jaMostrouAjudaPesquisa && numAleatorio> 60){
				mostraBalaoPesquisaEndereco();
				return;
			}	
		}
		if(situacao['qtd_move_map']>=5){
			
			if(!jaMostrouInteracaoMapa && numAleatorio> 60){
				mostrarBalaoInteracaoMapa()
				return;
			}	
		}
		if(situacao['zoom']>=15 && map.getCurrentMapType() != G_SATELLITE_MAP){
			
			if(!jaMostrouTipoMapa && numAleatorio> 40){
				mostrarBalaoTipoMapa();
				return;
			}	
		}
	}else{		
		if(numAleatorio <= 50){
			if(!jaMostrouAlerta){
				mostraBalaoAlertarAmigos();
				return;
			}	
		}else if(!jaMostrouNotificacoes){
			mostraBalaoNotificacoes();
			return;
		}	
			
	
		if(situacao['qtd_move_map']>=2){
			
			if(!jaMostrouAjudaPesquisa && numAleatorio> 60){
				mostraBalaoPesquisaEndereco();
				return;
			}	
		}
		if(situacao['qtd_move_map']>=4){
			
			if(!jaMostrouInteracaoMapa && numAleatorio> 80){
				mostrarBalaoInteracaoMapa()
				return;
			}	
		}
		if(situacao['zoom']>=15 && map.getCurrentMapType() != G_SATELLITE_MAP){
			
			if(!jaMostrouTipoMapa && numAleatorio> 40){
				mostrarBalaoTipoMapa();
				return;
			}	
		}
	}	
}

function mostraBalaoAlertarAmigos(){
	criaBalaoSetaCima(prefs.getMsg("balao.ajuda.alertar"),conteudoAjudaRelacionadaNotificacoes());
	mostrarBalao('360px','100px');
	jaMostrouAlerta = true;
}

function mostraBalaoPesquisaEndereco(){
	criaBalaoSetaCima(prefs.getMsg("balao.ajuda.pesquisar.endereco"),conteudoSemAjudaRelacionada());
	mostrarBalao('560px','92px');
	jaMostrouAjudaPesquisa = true;
}

function mostraBalaoNotificacoes(){
	criaBalaoSetaCima(prefs.getMsg("balao.ajuda.notificacoes"),conteudoAjudaRelacionadaRegistrarAlerta());
	mostrarBalao('148px','142px');
	jaMostrouNotificacoes = true;
}

function mostrarBalaoInteracaoMapa(){
	criaBalaoSetaCima(prefs.getMsg("balao.ajuda.manipulacao.mapa"),conteudoSemAjudaRelacionada());
	mostrarBalao('316px','210px');
	jaMostrouInteracaoMapa = true;
}

function mostrarBalaoTipoMapa(){
	criaBalaoSetaBaixo(prefs.getMsg("balao.ajuda.tipo.mapa"),conteudoSemAjudaRelacionada());
	mostrarBalao('562px','342px');
	jaMostrouTipoMapa = true;
}

function alterarEstadoSituacao(situacao){
	if(tutorEstaAtivado)	
		verificarRegras(situacao);
}

function geraNumero(inferior,superior){
    var numPossibilidades = superior - inferior;
    var aleat = Math.random() * numPossibilidades;
    aleat = Math.floor(aleat);
    return parseInt(inferior) + aleat;
}

function desabilitaTutor(time){
	var flag = false;
	if(tutorEstaAtivado){
		tutorEstaAtivado = false;
		flag = true;
	}	
	setTimeout(function(){
		if(flag)
			tutorEstaAtivado = true; 
	},time);
}